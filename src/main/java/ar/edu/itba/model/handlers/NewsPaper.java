package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;
import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.Subject;
import ar.edu.itba.model.utils.Random;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class NewsPaper {

    private static final double MIN_SCORE = 0D;
    private static final double MAX_SCORE = 0.5D;

    private final MediaId id;
    private final List<News> news = new LinkedList<>();

    public NewsPaper(final MediaId id) {
        this.id = id;
    }

    public News generateNews() {
        final Subject s = Subject.getRandomSubject();
        final Map<PoliticalParty, Double> impact = new HashMap<>();
        for (final PoliticalParty p : PoliticalParty.values())
            impact.put(p, Random.generateDoubleSigned(MIN_SCORE, MAX_SCORE));

        final News n = new News(s, id, impact);
        news.add(n);

        return n;
    }
}
