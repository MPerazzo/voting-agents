package ar.edu.itba.utils;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {

    private Metrics() {

    }

    public static void printPartiesState(final List<Person> persons) throws Exception {
        final Map<String, Integer> m = new HashMap<>();
        for (final String p : Configuration.getInstance().getPoliticalParties())
            m.put(p, 0);

        for (final Person p : persons)
            m.put(p.getPoliticalParty(), m.get(p.getPoliticalParty()) + 1);

        for (final Map.Entry<String, Integer> e : m.entrySet())
            System.out.println("Party " + e.getKey() + " has " + e.getValue() + " persons");
    }
}
