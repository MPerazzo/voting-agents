package ar.edu.itba.utils;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.PoliticalParty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metrics {

    public static void printPartiesState(final List<Person> persons) {
        final Map<PoliticalParty, Integer> m = new HashMap<>();
        for (final PoliticalParty p : PoliticalParty.values())
            m.put(p, 0);

        for (final Person p : persons)
            m.put(p.getPoliticalOrientation(), m.get(p.getPoliticalOrientation()) + 1);

        for (final Map.Entry<PoliticalParty, Integer> e : m.entrySet())
            System.out.println("Political party " + e.getKey() + " has " + e.getValue() + " persons");
    }
}
