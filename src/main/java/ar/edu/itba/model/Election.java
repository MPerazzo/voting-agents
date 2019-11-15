package ar.edu.itba.model;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Election {
    private String ruler;
    private int period;
    private List<String> partiesList;

    private static final Election instance = new Election();

    private Election() {
    }

    public void setProperties(final String r, final int p, final List<String> parties) {
        ruler = r;
        period = p;
        partiesList = parties;
    }

    public Optional<String> generateElection(final List<Person> persons, final long executionTime) {
        if (executionTime % period != 0)
            return Optional.empty();
        final Map<String, Long> parties = persons.stream().collect(Collectors.groupingBy(Person::getPoliticalParty, Collectors.counting()));
        final String electedParty = parties.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
        ruler = electedParty;
        return Optional.of(electedParty);
    }

    public static Election getInstance() {
        return instance;
    }

    public List<String> getPartiesList(){ return partiesList;}

    public String getRuler() { return ruler; }
}
