package ar.edu.itba;

import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.model.handlers.UpdateManager;
import ar.edu.itba.utils.Metrics;

import java.util.List;

public class Main {

    //seconds
    private static long executionTime;
    private static long dt;

    public static void main(String[] args) throws Exception {

        //porcessing libreria grafica

        init();
        final List<Person> persons = Profiler.generatePersons();
        final UpdateManager u = UpdateManager.getInstance();
        u.setPersons(persons);

        Metrics.printPartiesState(persons);

        int currentTime = 0;
        while (currentTime < executionTime) {
            final List<News> news = Media.generateNews();
            if (!news.isEmpty()) {
                u.updatePersons(news);
                u.updatePersons();
            }
            currentTime += dt;
        }
        Metrics.printPartiesState(persons);
    }

    private static void init() throws Exception {
        final Configuration configuration = Configuration.getInstance();
        configuration.init();
        executionTime = configuration.getExecutionTime();
        dt = configuration.getDt();
    }
}
