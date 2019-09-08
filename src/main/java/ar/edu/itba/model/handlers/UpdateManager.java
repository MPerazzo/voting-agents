package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.News;
import ar.edu.itba.model.Person;

import java.util.LinkedList;
import java.util.List;

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

    }

    public void updatePersons(final EconomicAction economicAction) {

    }

    public void updatePersons() {

    }
}
