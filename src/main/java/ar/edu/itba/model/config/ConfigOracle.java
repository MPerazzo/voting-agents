package ar.edu.itba.model.config;

public class ConfigOracle {
    private double prob;
    private int timeTolerance;
    private double impactTolerance;
    private double minPercentage;
    private double maxPercentage;

    public double getProb() {
        return prob;
    }

    public int getTimeTolerance() {
        return timeTolerance;
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
}
