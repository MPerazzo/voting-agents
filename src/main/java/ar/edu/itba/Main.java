package ar.edu.itba;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.handlers.*;

import java.util.List;

public class Main {

    private static final int MINUTE = 60;
    private static final int HOUR = MINUTE * 60;
    private static final int DAY = HOUR * 24;
    private static final int YEAR = DAY * 365;

    //seconds
    private final static long executionTime = YEAR * 2;
    private final static long dt = DAY;

    public static void main(String[] args) throws Exception {

        final List<Person> persons = Profiler.generatePersons(100);
        Friendship.generateFriendships(persons);
        final UpdateManager u = UpdateManager.getInstance();
        u.setPersons(persons);

        int currentTime = 0;
        while (currentTime < executionTime) {
            final List<News> news = Media.generateNews();
            u.updatePersons(news);

            if (dt % (DAY * 5) == 0) {
                EconomicAction e = EconomicMinistry.generateEconomicAction();
                u.updatePersons(e);
            }

            u.updatePersons();

            currentTime += dt;
        }
    }
}
