package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        this.politicalOrientation = politicalOrientation;
        this.mediaTrust = mediaTrust;
        this.interests = interests;
    }

    public void setFriendsTrust(Map<Person, Double> friendsTrust) {
        this.friendsTrust = friendsTrust;
    }

    public void setPoliticalOrientation(Map<String, Double> politicalOrientation) {
        this.politicalOrientation = politicalOrientation;
    }

    public String getPoliticalOrientation() {
        return politicalOrientation.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    public void update(final News n) {
        final String s = n.getSubject();
        final String mediaId = n.getMedia();
        final Map<String, Double> impact = n.getImpact();
        final Map<String, Double> impactDifferential = new HashMap<>();

        if (!interests.containsKey(s) || !mediaTrust.containsKey(mediaId))
            return;

        final double multiplier = interests.get(s) * mediaTrust.get(mediaId);
        for (final Map.Entry<String, Double> e : impact.entrySet()) {
            final double oldValue = politicalOrientation.get(e.getKey());
            final double differential = multiplier * e.getValue();
            politicalOrientation.put(e.getKey(), oldValue + differential);
            impactDifferential.put(e.getKey(), differential);
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
