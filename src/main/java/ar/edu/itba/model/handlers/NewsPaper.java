package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Event;
import ar.edu.itba.model.News;
import ar.edu.itba.model.config.profile.MediaParty;
import ar.edu.itba.utils.Random;

import java.util.*;

public class NewsPaper extends Creator{

    private final String name;
    private final double lieProbability = 0.5;


    private final List<News> news = new LinkedList<>();

    public NewsPaper(final String name, final double prob, final double minPercentage, final double maxPercentage,
                     final List<MediaParty> parties, final List<String> subjects) {
        super(parties,subjects,minPercentage,maxPercentage,prob);
        this.name = name;
    }

    public Optional<News> generateNews(int time) throws Exception{
        final double r = Random.generateDouble();
        Optional<News> n;
        if (r>lieProbability){
            n = generateEventBasedNews(time);
        }else{
            n = generateRandomNews(time);
        }
        return n;
    }

    public Optional<News> generateRandomNews(int time)throws Exception{
        final double r = Random.generateDouble();
        if (r > prob)
            return Optional.empty();
        final String subject = getRandomSubject();
        final double impact = generateImpact();
        final String party = generateParty();
        final int date = time;

        final News n = new News(subject, name, party, impact, date);
        news.add(n);

        return Optional.of(n);
    }

    public Optional<News> generateEventBasedNews(int time){
        final double r = Random.generateDouble();
        if (r > prob)
            return Optional.empty();

        Random random = new Random();
        Event e = Oracle.getInstance().getEvents().get(time).get(random.generateInt(0, Oracle.getInstance().getEvents().get(time).size()));
        
        final String subject = e.getSubject();
        final String party = e.getParty();
        final int date = e.getDate();
        final double impact = e.getImpact(); //ACA AGREGAR LOGICA DE GENERACION DE IMPACTO, ES DECIR, VER SI FAVORECER AL PARTIDO O NO ETC

        final News n = new News(subject, name, party, impact, date);
        news.add(n);

        return Optional.of(n);
    }

    public String getName() {
        return name;
    }

    public List<News> getNews() {
        return news;
    }
}
