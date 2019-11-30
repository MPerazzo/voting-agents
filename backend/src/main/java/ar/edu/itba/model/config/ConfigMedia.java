package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.MediaParty;

import java.util.List;

public class ConfigMedia {
    private String name;
    private double newsProb;
    private double lieProb;
    private double minPercentage;
    private double maxPercentage;
    private int timeTolerance;
    private List<MediaParty> parties;

    public String getName() {
        return name;
    }

    public double getNewsProb() {
        return newsProb;
    }

    public double getLieProb() {
        return lieProb;
    }

    public double getMinPercentage() {
        return minPercentage;
    }

    public double getMaxPercentage() {
        return maxPercentage;
    }

    public int getTimeTolerance() {
        return timeTolerance;
    }

    public List<MediaParty> getParties() {
        return parties;
    }
}
