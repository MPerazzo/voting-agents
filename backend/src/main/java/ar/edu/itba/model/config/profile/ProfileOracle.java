package ar.edu.itba.model.config.profile;

public class ProfileOracle {
    private double minProb;
    private double maxProb;
    private double liePenalty;
    private double trueReward;

    public double getMinProb() {
        return minProb;
    }

    public double getMaxProb() {
        return maxProb;
    }

    public double getLiePenalty() {
        return liePenalty;
    }

    public double getTrueReward() {
        return trueReward;
    }
}
