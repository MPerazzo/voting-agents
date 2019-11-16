package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.Profile;

import java.util.List;
import java.util.stream.Collectors;

public class InitialInputData {
    private int executionTime;
    private int dt;
    private List<Integer> population;
    private ConfigElection election;
    private List<String> parties;
    private List<ConfigMedia> media;
    private ConfigOracle oracle;
    private List<String> subjects;
    private List<Profile> profiles;
    private List<ConfigEconomicMinistry> economicMinistry;

    public int getExecutionTime() {
        return executionTime;
    }

    public int getDt() {
        return dt;
    }

    public ConfigElection getElection() {
        return election;
    }

    public List<Integer> getPopulation() {
        return population;
    }

    public List<String> getParties() {
        return parties;
    }

    public List<ConfigMedia> getMedia() { return media; }

    public List<String> getMediaNames() {
        return media.stream().map(m -> m.getName()).collect(Collectors.toList());
    }

    public ConfigOracle getOracle() {
        return oracle;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public List<ConfigEconomicMinistry> getEconomicMinistry() { return economicMinistry; }
}
