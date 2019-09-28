package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private Map<String, Double> getImpact(final String party, final double thresholdImpact) throws Exception {
        final Map<String, Double> netImpact = new HashMap<>();

        int divisor = politicalOrientation.size() - 1;
        final double sign = Math.signum(thresholdImpact);
        for (final String p : politicalOrientation.keySet()) {
            if (p.equals(party))
                netImpact.put(p, thresholdImpact);
            else {
                final double differential = -sign * thresholdImpact / divisor;
                if (isBetweenBounds(politicalOrientation.get(party) + differential))
                    netImpact.put(p, differential);
                else {
                    netImpact.put(p, 0D);
                    divisor--;
                }
            }
        }
        if (divisor == 0)
            throw new Exception("Illegal state");
        return netImpact;
    }

    private double impactThreshold(final String party, final double impact) {
        if (politicalOrientation.get(party) + impact > MAX)
            return MAX - politicalOrientation.get(party);
        if (politicalOrientation.get(party) + impact < MIN)
            return - politicalOrientation.get(party);
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
        return politicalOrientation.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public Map<String, Double> getPoliticalOrientation() {
        return politicalOrientation;
    }

    public void update(final News n) throws Exception {
        final String s = n.getSubject();
        final String mediaId = n.getMedia();
        final String party = n.getParty();

        if (!interests.containsKey(s) || !mediaTrust.containsKey(mediaId))
            return;

        if (!isInBounds(politicalOrientation.get(party)))
            return;

        final double thresholdImpact = impactThreshold(party, n.getImpact());
        final Map<String, Double> impact = getImpact(party, thresholdImpact);

        final double multiplier = interests.get(s) * mediaTrust.get(mediaId);
        for (final Map.Entry<String, Double> e : impact.entrySet()) {
            final double oldValue = politicalOrientation.get(e.getKey());
            politicalOrientation.put(e.getKey(), oldValue + multiplier * e.getValue());
        }

        verify();
        n.updateRealImpact(thresholdImpact * multiplier);
    }

    private void verify() throws Exception {
        final double sum = politicalOrientation.values().stream().reduce(0D, Double::sum);
        if (Math.abs(sum - MAX) > EPSILON)
            throw new Exception("Illegal state");
    }

    public void update(final String ruler, final Map<SocialClass, Double> impact) {
        final double newValue = impact.get(socialClass);
        final double oldValue = politicalOrientation.get(ruler);
        politicalOrientation.put(ruler, newValue + oldValue);
    }

    public Map<String, Double> update() {
        final Map<String, Double> m = new HashMap<>();

        //init map
        for (final Map.Entry<String, Double> e : politicalOrientation.entrySet())
            m.put(e.getKey(), e.getValue());

        for (final Person p : friendsTrust.keySet()) {
            for (final Map.Entry<String, Double> e : p.politicalOrientation.entrySet()) {
                final double friendPoliticalOrientation = p.politicalOrientation.get(e.getKey());
                final double multiplier = friendsTrust.get(p);
                final double oldValue = m.get(e.getKey());
                m.put(e.getKey(), oldValue + friendPoliticalOrientation * multiplier);
            }
        }
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
