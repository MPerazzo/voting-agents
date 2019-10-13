package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Event;
import ar.edu.itba.model.News;
import ar.edu.itba.model.config.profile.MediaParty;
import ar.edu.itba.utils.Random;

import java.util.*;

public class NewsPaper {

    private final String name;
    private final double newsProb;
    private final double lieProb;
    private final double minPercentage;
    private final double maxPercentage;
    private final int timeTolerance;

    private final List<MediaParty> parties;
    private final List<String> subjects;
    private final List<News> news = new LinkedList<>();

    private final Oracle oracle = Oracle.getInstance();

    public NewsPaper(final String name, final double newsProb, final double lieProb, final double minPercentage, final double maxPercentage,
                     final int timeTolerance, final List<MediaParty> parties, final List<String> subjects) {
        this.name = name;
        this.newsProb = newsProb;
        this.lieProb = lieProb;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.timeTolerance = timeTolerance;
        this.parties = parties;
        this.subjects = subjects;
    }

    public Optional<News> generateNews(final int time) {
        final double r1 = Random.generateDouble();
        if (r1 > newsProb)
            return Optional.empty();

        final double r2 = Random.generateDouble();
        Optional<News> news;
        if (r2 > lieProb)
            news = generateNonBiasedNews(time);
        else
            news = generateBiasedNews(time);
        this.news.add(news.get());
        return news;
    }

    private Optional<News> generateBiasedNews(final int time) {
        final Event e = oracle.getEvent(timeTolerance);
        final String subject = getRandomSubject();
        final String party = getParty();
        final double impact = Random.generateDouble(e.getImpact() + minPercentage, e.getImpact() + maxPercentage);
        return Optional.of(new News(subject, name, party, impact, time, e));
    }

    public Optional<News> generateNonBiasedNews(final int time) {
        return Optional.of(new News(oracle.getToleratedEvent(), name, time));
    }

    private String getRandomSubject() {
        return subjects.get(Random.generateInt(0, subjects.size() - 1));
    }

    private String getParty() {
        double probSum = 0;
        String party = null;
        final double r = Random.generateDouble();
        for (final MediaParty p : parties) {
            probSum += p.getProb();
            if (r <= probSum)
                party = p.getName();
        }
        return party;
    }

    public String getName() {
        return name;
    }

    public List<News> getNews() {
        return news;
    }
}
