package ar.edu.itba.model.config.profile;

import ar.edu.itba.model.enums.SocialClass;

import java.util.Map;

public class MinistryPartyEconomic {
    private String name;
    private double minRational;
    private double maxRational;
    private double competence;

    public String getName() { return name; }

    public double getMinRational() {
        return minRational;
    }

    public double getMaxRational() {
        return maxRational;
    }

    public double getCompetence() {
        return competence;
    }
}
