package ar.edu.itba.model;

import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.Subject;

import java.util.Map;
import java.util.Objects;

public class Person {

    //Internal Status
    private final int id;
    private final double economicWellness;
    private final Map<PoliticalParty, Double> politicalOrientation;

    //Multipliers
    private final Map<MediaId, Double> mediaTrust;
    private Map<Person, Double> friendsTrust;
    private final Map<Subject, Double> interests;

    public Person(final int id, final double economicWellness, final Map<PoliticalParty, Double> politicalOrientation,
                  final Map<MediaId, Double> mediaTrust, final Map<Subject, Double> interests) {
        this.id = id;
        this.economicWellness = economicWellness;
        this.politicalOrientation = politicalOrientation;
        this.mediaTrust = mediaTrust;
        this.interests = interests;
    }

    public void setFriendsTrust(Map<Person, Double> friendsTrust) {
        this.friendsTrust = friendsTrust;
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
