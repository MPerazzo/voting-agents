package ar.edu.itba;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.handlers.*;
import ar.edu.itba.utils.Metrics;
import processing.core.PApplet;

import java.util.List;
import java.util.Optional;

public class Main {

    private static final int MINUTE = 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;
    private static final int YEAR = DAY * 365;

    //seconds
    private final static long executionTime = DAY * 4;
    private final static long dt = DAY;

    public static void main(String[] args) throws Exception {

        final List<Person> persons = Profiler.generatePersons(100);
        Friendship.generateFriendships(persons);
        final UpdateManager u = UpdateManager.getInstance();
        u.setPersons(persons);
        PoliticalParty ruler = PoliticalParty.LEFT;

        Metrics.printPartiesState(persons);

        int currentTime = 0;
        while (currentTime < executionTime) {
            final List<News> news = Media.generateNews();
            if (!news.isEmpty())
                u.updatePersons(news);

            Optional<EconomicAction> e = EconomicMinistry.generateEconomicAction(ruler);
            if (e.isPresent())
                u.updatePersons(e.get());

            if (!news.isEmpty() | e.isPresent())
                u.updatePersons();

            currentTime += dt;
        }

        PApplet.main("ar.edu.itba.processing.Sketch", args);
        Metrics.printPartiesState(persons);
    }
}
