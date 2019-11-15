package ar.edu.itba;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.Election;
import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.Configuration;
import ar.edu.itba.model.handlers.EconomicMinistry;
import ar.edu.itba.model.handlers.*;
import ar.edu.itba.ui.election.ElectionDebugUI;
import ar.edu.itba.ui.election.ElectionUI;
import ar.edu.itba.utils.Metrics;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    private static long executionTime;
    private static long dt;
    private static boolean DEBUG = false;

    public static void main(String[] args) throws Exception {
        init();

        final List<Person> persons = Profiler.getInstance().generatePersons();
        final UpdateManager u = UpdateManager.getInstance();
        u.setPersons(persons);

        ElectionUI.setPreviousCount(Profiler.getInstance().getPersons());

        int currentTime = 0;
        final List<ElectionUI> electionsUI = new LinkedList<>();
        final List<ElectionDebugUI> electionsDebugUI = new LinkedList<>();
        while (currentTime < executionTime) {
            Metrics.printPartiesState(persons);

            Oracle.getInstance().generateEvent(currentTime);
            final List<News> news = Media.getInstance().generateNews(currentTime);
            final Optional<EconomicAction> economicAction = EconomicMinistry.generateEconomicAction(Election.getInstance().getRuler());

            if (!news.isEmpty()) {
                u.updatePersonsNews(news);
            }
            if(economicAction.isPresent()){
                u.updatePersonsEconomic(economicAction.get());
            }
            u.updatePersons();
            currentTime += dt;

            Optional<String> result = Election.getInstance().generateElection(persons, currentTime);
            if (!result.isEmpty()) {
                electionsUI.add(computeElectionUI());
                if (DEBUG)
                    electionsDebugUI.add(computeElectionDebugUI());
                clearMetricsUI();
            }
        }
        for (int i = electionsUI.size() - 1 ; i >= 0 ; i--) {
            electionsUI.get(i).showOnScreen();
            if (DEBUG)
                electionsDebugUI.get(i).showOnScreen();
        }
    }

    private static ElectionUI computeElectionUI() throws Exception {
        final ElectionUI electionUI = new ElectionUI();
        electionUI.compute();
        return electionUI;
    }

    private static ElectionDebugUI computeElectionDebugUI() throws Exception {
        final ElectionDebugUI electionUI = new ElectionDebugUI();
        electionUI.compute();
        return electionUI;
    }

    private static void clearMetricsUI() {
        Media.getInstance().clear();
        Friendship.clear();
        EconomicMinistry.clear();
    }

    private static void init() throws Exception {
        final Configuration configuration = Configuration.getInstance();
        configuration.init();
        executionTime = configuration.getExecutionTime();
        dt = configuration.getDt();
    }
}
