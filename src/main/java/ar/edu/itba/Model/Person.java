package ar.edu.itba.Model;

import ar.edu.itba.Model.Enums.PoliticalParty;
import ar.edu.itba.Model.Enums.Subject;

import java.util.Map;

public class Person {

    //Internal Status
    private int id;
    private double economicWellness;
    private PoliticalParty politicalParty;
    private Map<Integer, Double> friendList; // private Map<Person, Double> friendList;

    //Multipliers
    private Map<Media, Double> mediaTrust; // private Map<Person,Double> mediaTrust;
    private Map<Integer, Double> influenceability; //private Map<Person, Double> influenceability;
    private Map<Subject, Double> interests;

    public Person(int id, double economicWellness, PoliticalParty politicalParty, Map<Integer, Double> friendList, Map<Media, Double> mediaTrust, Map<Integer, Double> influenceability, Map<Subject, Double> interests) {
        this.id = id;
        this.economicWellness = economicWellness;
        this.politicalParty = politicalParty;
        this.friendList = friendList;
        this.mediaTrust = mediaTrust;
        this.influenceability = influenceability;
        this.interests = interests;
    }

    public int getId() {
        return id;
    }

    public double getEconomicWellness() {
        return economicWellness;
    }

    public void setEconomicWellness(double economicWellness) {
        this.economicWellness = economicWellness;
    }

    public PoliticalParty getPoliticalParty() {
        return politicalParty;
    }

    public void setPoliticalParty(PoliticalParty politicalParty) {
        this.politicalParty = politicalParty;
    }

    public Map<Integer, Double> getFriendList() {
        return friendList;
    }

    public void setFriendList(Map<Integer, Double> friendList) {
        this.friendList = friendList;
    }

    public Map<Media, Double> getMediaTrust() {
        return mediaTrust;
    }

    public void setMediaTrust(Map<Media, Double> mediaTrust) {
        this.mediaTrust = mediaTrust;
    }

    public Map<Integer, Double> getInfluenceability() {
        return influenceability;
    }

    public void setInfluenceability(Map<Integer, Double> influenceability) {
        this.influenceability = influenceability;
    }

    public Map<Subject, Double> getInterests() {
        return interests;
    }

    public void setInterests(Map<Subject, Double> interests) {
        this.interests = interests;
    }
}
