package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;
import ar.edu.itba.model.config.ConfigMedia;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Media {
    private final List<NewsPaper> sources = new LinkedList<>();

    private static final Media instance = new Media();

    private Media () {

    }

    public void setSources(final List<ConfigMedia> media, final List<String> subjects) {
        sources.clear();
        for (final ConfigMedia m : media)
            sources.add(new NewsPaper(m.getName(), m.getNewsProb(), m.getLieProb(), m.getMinPercentage(),
                    m.getMaxPercentage(), m.getTimeTolerance(), m.getParties(), subjects));
    }

    public List<News> generateNews(int time) throws Exception {
        final List<News> news = new LinkedList<>();
        for (NewsPaper p : sources) {
            final Optional<News> n = p.generateNews(time);
            if (n.isPresent())
                news.add(n.get());
        }
        return news;
    }

    public List<NewsPaper> getSources(){ return sources;}

    public static Media getInstance() {
        return instance;
    }

    public void clear() {
        for (final NewsPaper newsPaper : sources)
            newsPaper.getNews().clear();
    }

}
