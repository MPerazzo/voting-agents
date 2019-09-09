package ar.edu.itba.model;

import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.enums.Subject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Person {

    //Internal Status
    private final int id;
    private double economicWellness;
    private SocialClass socialClass;
    private  Map<PoliticalParty, Double> politicalOrientation;

    //Multipliers
    private final Map<MediaId, Double> mediaTrust;
    private Map<Person, Double> friendsTrust;
    private final Map<Subject, Double> interests;

    public Person(final int id, final double economicWellness, final Map<PoliticalParty, Double> politicalOrientation,
                  final Map<MediaId, Double> mediaTrust, final Map<Subject, Double> interests) throws Exception {
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

    public void setPoliticalOrientation(Map<PoliticalParty, Double> politicalOrientation) {
        this.politicalOrientation = politicalOrientation;
    }

    public void update(final Subject s, final MediaId mediaId, final Map<PoliticalParty, Double> impact) {
        if (!interests.containsKey(s))
            return;

        final double multiplier = interests.get(s) * mediaTrust.get(mediaId);
        for (final Map.Entry<PoliticalParty, Double> e : impact.entrySet()) {
            final double oldValue = politicalOrientation.get(e.getKey());
            politicalOrientation.put(e.getKey(), multiplier * (oldValue + e.getValue()));
        }
    }

    public void update(final PoliticalParty ruler, final Map<SocialClass, Double> impact) {
        final double newValue = impact.get(socialClass);
        final double oldValue = politicalOrientation.get(ruler);
        politicalOrientation.put(ruler, newValue + oldValue);
    }

    public Map<PoliticalParty, Double> update() {
        final Map<PoliticalParty, Double> m = new HashMap<>();

        //init map
        for (final Map.Entry<PoliticalParty, Double> e : politicalOrientation.entrySet())
            m.put(e.getKey(), e.getValue());

        for (final Person p : friendsTrust.keySet()) {
            for (final Map.Entry<PoliticalParty, Double> e : p.politicalOrientation.entrySet()) {
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
