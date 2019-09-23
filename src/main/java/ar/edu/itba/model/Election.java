package ar.edu.itba.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Election {
    private static String ruler;
    private static int period;
    private static List<String> partiesList;

    private Election() {
    }

    public static void setProperties(final String r, final int p, final List<String> parties) {
        ruler = r;
        period = p;
       partiesList = parties;
    }

    public static Optional<String> generateElection(final List<Person> persons, final long executionTime) {
        if (executionTime % period != 0)
            return Optional.empty();
        final Map<String, Long> parties = persons.stream().collect(Collectors.groupingBy(Person::getPoliticalParty, Collectors.counting()));
        final String electedParty = parties.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        ruler = electedParty;
        return Optional.of(electedParty);
    }

    public static String getRuler() {
        return ruler;
    }

    public static int getPeriod() {
        return period;
    }

    public static List<String> getPartiesList(){ return partiesList;}
}
