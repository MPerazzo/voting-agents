package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.MediaParty;

import java.util.List;

public class ConfigMedia {
    private String name;
    private double prob;
    private double minPercentage;
    private double maxPercentage;
    private List<MediaParty> parties;

    public String getName() {
        return name;
    }

    public double getProb() {
        return prob;
    }

    public double getMinPercentage() {
        return minPercentage;
    }

    public double getMaxPercentage() {
        return maxPercentage;
    }

    public List<MediaParty> getParties() {
        return parties;
    }
}
