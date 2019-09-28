package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.ConfigMedia;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Media {
    private final static List<NewsPaper> sources = new LinkedList<>();

    private Media () {

    }

    public static void setSources(final List<ConfigMedia> media, final List<String> subjects) {
        for (final ConfigMedia m : media)
            sources.add(new NewsPaper(m.getName(), m.getProb(), m.getMinPercentage(), m.getMaxPercentage(),
                    m.getParties(), subjects));
    }

    public static List<News> generateNews() throws Exception {
        final List<News> news = new LinkedList<>();
        for (NewsPaper p : sources) {
            final Optional<News> n = p.generateNews();
            if (n.isPresent())
                news.add(n.get());
        }
        return news;
    }

    public static List<NewsPaper> getSources(){ return sources;}

    public static void clear() {
        for (final NewsPaper newsPaper : sources)
            newsPaper.getNews().clear();
    }

}
