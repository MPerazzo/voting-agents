package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.profile.Party;
import ar.edu.itba.utils.Random;

import java.util.*;

public class NewsPaper {

    private final String id;
    private final double prob;
    private final List<Party> parties;
    private final List<String> subjects;
    private final List<News> news = new LinkedList<>();

    public NewsPaper(final String id, final double prob, final List<Party> parties, final List<String> subjects) {
        this.id = id;
        this.prob = prob;
        this.parties = parties;
        this.subjects = subjects;
    }

    public Optional<News> generateNews() {
        final double r = Random.generateDouble();
        if (r > prob)
            return Optional.empty();
        final String s = getRandomSubject();
        final Map<String, Double> impact = new HashMap<>();
        for (final Party p : parties)
            impact.put(p.getName(), Random.generateDouble(p.getMinScore(), p.getMaxScore()));

        final News n = new News(s, id, impact);
        news.add(n);

        return Optional.of(n);
    }

    private String getRandomSubject() {
        return subjects.get(Random.generateInt(0, subjects.size() - 1));
    }
}
