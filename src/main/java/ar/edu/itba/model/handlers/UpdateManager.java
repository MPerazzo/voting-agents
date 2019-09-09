package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.PoliticalParty;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UpdateManager {
    private List<Person> persons = new LinkedList<>();

    private static UpdateManager instance = null;

    private UpdateManager() {
    }

    public static UpdateManager getInstance() {
        if (instance == null)
            instance = new UpdateManager();
        return instance;
    }

    public void setPersons(final List<Person> persons) {
        this.persons = persons;
    }

    public void updatePersons(final List<News> news) {
        for (final Person p : persons) {
            for (final News n : news)
                p.update(n.getSubject(), n.getMedia(), n.getImpact());
        }
    }

    public void updatePersons(final EconomicAction economicAction) {
        for (final Person p : persons)
            p.update(economicAction.getRuler(), economicAction.getImpact());
    }

    public void updatePersons() {
        final Map<Person, Map<PoliticalParty, Double>> currentState = new HashMap<>();

        for (final Person p : persons)
            currentState.put(p, p.update());

        for (final Person p : persons)
            p.setPoliticalOrientation(currentState.get(p));
    }
}
