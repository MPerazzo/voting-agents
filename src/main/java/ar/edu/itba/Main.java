package ar.edu.itba;

import ar.edu.itba.model.Election;
import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.Friendship;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.model.handlers.UpdateManager;
import ar.edu.itba.ui.election.ElectionNewsInfluence;
import ar.edu.itba.utils.Metrics;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Main {

    private static long executionTime;
    private static long dt;

    public static void main(String[] args) throws Exception {

        init();

        final List<Person> persons = Profiler.generatePersons();
        final UpdateManager u = UpdateManager.getInstance();
        u.setPersons(persons);

        int currentTime = 0;
        final List<ElectionNewsInfluence> electionsUI = new LinkedList<>();
        while (currentTime < executionTime) {
            Metrics.printPartiesState(persons);

            final List<News> news = Media.generateNews();
            if (!news.isEmpty()) {
                u.updatePersons(news);
                u.updatePersons();
            }
            currentTime += dt;

            Optional<String> result = Election.generateElection(persons, currentTime);
            if (!result.isEmpty()) {
                final ElectionNewsInfluence electionUI = new ElectionNewsInfluence();
                electionUI.compute();
                electionsUI.add(electionUI);
                Media.clear();
                Friendship.clear();
            }
        }
        for (int i = electionsUI.size() - 1 ; i >= 0 ; i--)
            electionsUI.get(i).showOnScreen();
    }

    private static void init() throws Exception {
        final Configuration configuration = Configuration.getInstance();
        configuration.init();
        executionTime = configuration.getExecutionTime();
        dt = configuration.getDt();
    }
}
