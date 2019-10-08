package ar.edu.itba.model.handlers;

import ar.edu.itba.model.config.profile.MediaParty;
import ar.edu.itba.utils.Random;

import java.util.List;

public abstract class Creator {
    private final List<MediaParty> parties;
    private final List<String> subjects;
    private final double minPercentage;
    private final double maxPercentage;
    public final double prob;

    public Creator(List<MediaParty> parties, List<String> subjects, final double minPercentage,final double maxPercentage, final double prob ) {
        this.parties = parties;
        this.subjects = subjects;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.prob=prob;
    }

    public String generateParty() throws Exception {
        double probSum = 0;
        double r = Random.generateDouble();
        for (final MediaParty p : parties) {
            probSum += p.getProb();
            if (r <= probSum)
                return p.getName();
        }
        throw new Exception("Sum of probabilities is not one");
    }

    public String getRandomSubject() {
        return subjects.get(Random.generateInt(0, subjects.size() - 1));
    }

    public double generateImpact(){
        return Random.generateDouble(minPercentage, maxPercentage);
    }
}
