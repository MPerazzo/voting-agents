package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.Friendship;
import ar.edu.itba.model.handlers.Oracle;
import ar.edu.itba.utils.Random;


import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class Person {

    private final static double ECONOMIC_TO_IMPACT = 100;

    private final static double EPSILON = 0.1;

    private final static double IMPACT_MIN = 0D;
    private final static double IMPACT_MAX = 100D;

    private final static double MEDIA_TRUST_MIN = 0D;
    private final static double MEDIA_TRUST_MAX = 1D;

    //Internal Status
    private final int id;
    private double economicWellness;
    private SocialClass socialClass;
    private Map<String, Double> politicalOrientation;

    //Multipliers
    private final Map<String, Double> mediaTrust;
    private Map<Person, Double> friendsTrust;
    private final Map<String, Double> interests;

    private final Oracle oracle = Oracle.getInstance();

    private double skepticism;
    private double liePenalty;
    private double trueReward;

    public Person(final int id, final double economicWellness, final Map<String, Double> politicalOrientation,
                  final Map<String, Double> mediaTrust, final Map<String, Double> interests,
                  final double skepticism, final double liePenalty, final double trueReward) throws Exception {
        this.id = id;
        this.economicWellness = economicWellness;
        this.socialClass = SocialClass.getSocialClass(economicWellness);
        this.politicalOrientation = normalize(politicalOrientation);
        this.mediaTrust = mediaTrust;
        this.interests = interests;
        this.skepticism = skepticism;
        this.liePenalty = liePenalty;
        this.trueReward = trueReward;
    }

    //only called once
    private Map<String, Double> normalize(final Map<String, Double> scoreMap) {
        final double total = scoreMap.values().stream().collect(Collectors.summingDouble(d -> d.doubleValue()));
        final Map<String, Double> normalized = new HashMap<>();
        for (final Map.Entry<String, Double> e : scoreMap.entrySet())
            normalized.put(e.getKey(), ((e.getValue() / total) * 100));
        return normalized;
    }

    private Map<String, Double> getImpact(final Map<String, Double> m,
                                          final String party, final double thresholdImpact) throws Exception {
        final Map<String, Double> netImpact = new HashMap<>();

        int divisor = m.size() - 1;
        double remaining = thresholdImpact;

        for (final String p : sortKeys(m, Math.signum(thresholdImpact))) {
            if (p.equals(party)){
                //System.out.println("Thresh: " + thresholdImpact);
                netImpact.put(p, thresholdImpact);
            }else {
                final double differential = impactThreshold(m, p, -1.0 * remaining / divisor);
                //System.out.println("diff: " + differential);
                netImpact.put(p, differential);
                remaining += differential;
                divisor--;
            }
        }
        //System.out.println("Rem: " + remaining);
        if (Math.abs(remaining) > EPSILON)
            throw new Exception("Illegal state");
        return netImpact;
    }

    private Set<String> sortKeys(final Map<String, Double> m, final double sign) {
        if (sign >= 0)
            return m.entrySet().stream().sorted(comparingByValue())
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)).keySet();
        else
            return m.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new)).keySet();
    }

    private double impactThreshold(final Map<String, Double> m, final String party, final double impact) {
        if (m.get(party) + impact > IMPACT_MAX)
            return IMPACT_MAX - m.get(party);
        if (m.get(party) + impact < IMPACT_MIN)
            return - m.get(party);
        return impact;
    }

    private double mediaTrustThreshold(final double trust) {
        if (trust > MEDIA_TRUST_MAX)
            return MEDIA_TRUST_MAX;
        else if (trust < MEDIA_TRUST_MIN)
            return MEDIA_TRUST_MIN;
        return trust;
    }

    private boolean isBetweenBounds(final double value) {
        return value >= IMPACT_MIN
                || value <= IMPACT_MAX;
    }

    private boolean isInBounds(final double value) {
        if (!(Math.abs(value - IMPACT_MAX) <= EPSILON || Math.abs(value) <= EPSILON))
            return true;
        return false;
    }

    public void setFriendsTrust(final Map<Person, Double> friendsTrust) {
        this.friendsTrust = friendsTrust;
    }

    public void setPoliticalOrientation(Map<String, Double> politicalOrientation) {
        this.politicalOrientation = politicalOrientation;
    }

    public String getPoliticalParty() {
        return politicalOrientation.entrySet().stream().max(comparingByValue()).get().getKey();
    }

    public Map<String, Double> getPoliticalOrientation() {
        return politicalOrientation;
    }

    public void update(final News n) throws Exception {
        final double r = Random.generateDouble();
        if (r <= skepticism) {
            if (oracle.checkIfTrueNews(n)) {
                final double trust = mediaTrustThreshold(mediaTrust.get(n.getMedia()) + trueReward);
                mediaTrust.put(n.getMedia(), trust);
            }
            else {
                final double trust = mediaTrustThreshold(mediaTrust.get(n.getMedia()) - liePenalty);
                mediaTrust.put(n.getParty(), trust);
                return;
            }
        }
        final String s = n.getSubject();
        final String mediaId = n.getMedia();
        final String party = n.getParty();
//        final double multiplier = interests.get(s) * mediaTrust.get(mediaId);
        final double multiplier = mediaTrust.get(mediaId);
        final double thresholdImpact = impactThreshold(politicalOrientation, party, n.getImpact());
        final double realImpact = thresholdImpact * multiplier;

        if (!interests.containsKey(s) || !mediaTrust.containsKey(mediaId))
            return;

        final Map<String, Double> impact = getImpact(politicalOrientation, party, realImpact);

        for (final Map.Entry<String, Double> e : impact.entrySet()) {
            final double oldValue = politicalOrientation.get(e.getKey());
            politicalOrientation.put(e.getKey(), oldValue + e.getValue());
        }

        verify(politicalOrientation);
        n.updateTotalImpact(thresholdImpact);
        n.updateRealImpact(realImpact);
    }

    private void verify(final Map<String, Double> m) throws Exception {
        final double sum = m.values().stream().reduce(0D, Double::sum);
        if (Math.abs(sum - IMPACT_MAX) > EPSILON)
            throw new Exception("Illegal state");
    }

    public void update(final EconomicAction e) throws Exception {

        final double economicThreshold = SocialClass.economicThreshold(economicWellness, e.getImpact().get(socialClass));

        final double impactThreshold = impactThreshold(politicalOrientation, e.getRuler(), e.getImpact().get(socialClass) * ECONOMIC_TO_IMPACT);
        final Map<String, Double> impact = getImpact(politicalOrientation, e.getRuler(), impactThreshold);

        for (final Map.Entry<String, Double> entry : impact.entrySet()) {
            final double oldValue = politicalOrientation.get(entry.getKey());
            politicalOrientation.put(entry.getKey(), oldValue + entry.getValue());
        }
        e.update(socialClass, impactThreshold);

        economicWellness += economicThreshold;
        this.socialClass = SocialClass.getSocialClass(economicWellness);
    }

    public Map<String, Double> update() throws Exception {
        final Map<String, Double> m = new HashMap<>();

        //init map
        for (final Map.Entry<String, Double> e : politicalOrientation.entrySet())
            m.put(e.getKey(), e.getValue());

        for (final Person p : friendsTrust.keySet()) {
            final String party = p.getPoliticalParty();
            final double value = p.getPoliticalOrientation().get(party);
            final double multiplier = friendsTrust.get(p);
            final double realImpact = impactThreshold(m, party, multiplier * value);
            Friendship.updateScore(party, realImpact);
            final Map<String, Double> differential = getImpact(m, party, realImpact);
            differential.forEach((k, v) -> m.merge(k, v, Double::sum));
        }
        verify(m);
        return m;
    }

    public void setParams(final double skepticism, final double liePenalty, final double trueReward) {
        this.skepticism = skepticism;
        this.liePenalty = liePenalty;
        this.trueReward = trueReward;
    }

    public SocialClass getSocialClass() {
        return socialClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
