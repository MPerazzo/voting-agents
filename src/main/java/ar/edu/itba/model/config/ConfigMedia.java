package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.Party;

import java.util.List;

public class ConfigMedia {
    private String name;
    private double prob;
    private List<Party> parties;

    public String getName() {
        return name;
    }

    public double getProb() {
        return prob;
    }

    public List<Party> getParties() {
        return parties;
    }
}
