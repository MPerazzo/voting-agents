package ar.edu.itba.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Election {
    private static String ruler;
    private static int period;

    private Election() {
    }

    public static void setProperties(final String r, final int p) {
        ruler = r;
        period = p;
    }

    public static String generateElection(final List<Person> persons) {
        Map<String, Long> parties = persons.stream().collect(Collectors.groupingBy(Person::getPoliticalOrientation, Collectors.counting()));
        parties.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        return null;
    }

    public static String getRuler() {
        return ruler;
    }

    public static int getPeriod() {
        return period;
    }
}
