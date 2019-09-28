package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.profile.MediaParty;
import ar.edu.itba.model.config.profile.ProfileParty;
import ar.edu.itba.utils.Random;

import java.util.*;

public class NewsPaper {

    private final String name;
    private final double prob;
    private final double minPercentage;
    private final double maxPercentage;
    private final List<MediaParty> parties;
    private final List<String> subjects;
    private final List<News> news = new LinkedList<>();

    public NewsPaper(final String name, final double prob, final double minPercentage, final double maxPercentage,
                     final List<MediaParty> parties, final List<String> subjects) {
        this.name = name;
        this.prob = prob;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.parties = parties;
        this.subjects = subjects;
    }

    public Optional<News> generateNews() throws Exception {
        final double r = Random.generateDouble();
        if (r > prob)
            return Optional.empty();
        final String s = getRandomSubject();
        final double impact = Random.generateDouble(minPercentage, maxPercentage);
        final String party = generateParty(parties);

        final News n = new News(s, name, party, impact);
        news.add(n);

        return Optional.of(n);
    }

    private String generateParty(List<MediaParty> parties) throws Exception {
        double probSum = 0;
        double r = Random.generateDouble();
        for (final MediaParty p : parties) {
            probSum += p.getProb();
            if (r <= probSum)
                return p.getName();
        }
        throw new Exception("Sum of probabilities is not one");
    }

    private String getRandomSubject() {
        return subjects.get(Random.generateInt(0, subjects.size() - 1));
    }

    public String getName() {
        return name;
    }

    public List<News> getNews() {
        return news;
    }
}
