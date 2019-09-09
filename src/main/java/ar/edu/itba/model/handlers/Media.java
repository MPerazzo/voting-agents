package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;
import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.utils.Random;

import java.util.LinkedList;
import java.util.List;

public class Media {
    private final static List<NewsPaper> sources = new LinkedList<>();
    private static final double PROB = 0.5;

    public static List<News> generateNews() {
        final List<News> news = new LinkedList<>();
        for (NewsPaper p : sources) {
                if (Random.generateDouble() > PROB) {
                final News n = p.generateNews();
                news.add(n);
            }
        }
        return news;
    }

    static {
        for (final MediaId m : MediaId.values())
            sources.add(new NewsPaper(m));
    }
}
