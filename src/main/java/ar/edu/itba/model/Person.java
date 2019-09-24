package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Person {

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

    private Map<String, Double> netImpact(final Map<String, Double> impact) {
        final Map<String, Double> netImpact = new HashMap<>();

        for (final Map.Entry<String, Double> e : impact.entrySet())
            netImpact.put(e.getKey(), 0D);

        final int parties = impact.size();
        for (final Map.Entry<String, Double> e1 : impact.entrySet()) {
            for (final Map.Entry<String, Double> e2 : impact.entrySet()) {
                final double oldValue = netImpact.get(e2.getKey());
                final double sign = Math.signum(e1.getValue());
                if (!e1.getKey().equals(e2.getKey()))
                    netImpact.put(e2.getKey(), oldValue - sign * e1.getValue()/(parties - 1));
                else
                    netImpact.put(e2.getKey(), oldValue +  sign * e1.getValue());

            }
        }
        return netImpact;
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

    public void update(final News n) {
        final String s = n.getSubject();
        final String mediaId = n.getMedia();
        final Map<String, Double> impact = n.getImpact();
        final Map<String, Double> netImpact = netImpact(n.getImpact());
        final Map<String, Double> impactDifferential = new HashMap<>();

        if (!interests.containsKey(s) || !mediaTrust.containsKey(mediaId))
            return;

        final double multiplier = interests.get(s) * mediaTrust.get(mediaId);
        for (final Map.Entry<String, Double> e : netImpact.entrySet()) {
            final double oldValue = politicalOrientation.get(e.getKey());
            politicalOrientation.put(e.getKey(), oldValue + multiplier * e.getValue());
            impactDifferential.put(e.getKey(), impact.get(e.getKey()));
        }
        n.updateImpactDifferential(impactDifferential);
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
