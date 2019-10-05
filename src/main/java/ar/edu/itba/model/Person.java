package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.handlers.Friendship;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class Person {

    final static double EPSILON = 0.01;
    final static double MAX = 100;
    final static double MIN = 0;

    //Internal Status
    private final int id;
    private double economicWellness;
    private SocialClass socialClass;
    private Map<String, Double> politicalOrientation;

    //Multipliers
    private final Map<String, Double> mediaTrust;
    private Map<Person, Double> friendsTrust;
    private final Map<String, Double> interests;

    public Person(final int id, final double economicWellness, final Map<String, Double> politicalOrientation,
                  final Map<String, Double> mediaTrust, final Map<String, Double> interests) throws Exception {
        this.id = id;
        this.economicWellness = economicWellness;
        this.socialClass = SocialClass.getSocialClass(economicWellness);
        this.politicalOrientation = normalize(politicalOrientation);
        this.mediaTrust = mediaTrust;
        this.interests = interests;
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
        final double sign = Math.signum(thresholdImpact);
        for (final String p : sortKeys(m, Math.signum(thresholdImpact))) {
            if (p.equals(party))
                netImpact.put(p, thresholdImpact);
            else {
                final double differential = impactThreshold(m, p, -sign * remaining / divisor);
                netImpact.put(p, differential);
                remaining += differential;
                divisor--;
            }
        }
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
        if (m.get(party) + impact > MAX)
            return MAX - m.get(party);
        if (m.get(party) + impact < MIN)
            return - m.get(party);
        return impact;
    }

    private boolean isBetweenBounds(final double value) {
        return value >= MIN
                || value <= MAX;
    }

    private boolean isInBounds(final double value) {
        if (!(Math.abs(value - MAX) <= EPSILON || Math.abs(value) <= EPSILON))
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
        final String s = n.getSubject();
        final String mediaId = n.getMedia();
        final String party = n.getParty();
        final double multiplier = interests.get(s) * mediaTrust.get(mediaId);
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
        if (Math.abs(sum - MAX) > EPSILON)
            throw new Exception("Illegal state");
    }

    public void update(final String ruler, final Map<SocialClass, Double> impact) {
        final double newValue = impact.get(socialClass);
        final double oldValue = politicalOrientation.get(ruler);
        politicalOrientation.put(ruler, newValue + oldValue);
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
