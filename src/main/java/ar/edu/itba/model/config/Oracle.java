package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.MediaParty;

import java.util.List;

public class Oracle {
    private double prob;
    private int newsTolerance;
    private double impactTolerance;
    private double minPercentage;
    private double maxPercentage;
    private List<MediaParty> parties;

    public double getProb() {
        return prob;
    }

    public int getNewsTolerance() {
        return newsTolerance;
    }

    public double getImpactTolerance() {
        return impactTolerance;
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
