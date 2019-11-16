package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.ProfileOracle;

import java.util.List;

public class RunInputData {
    private int executionTime;
    private List<ConfigMedia> media;
    private ConfigOracle oracle;
    private List<ConfigEconomicMinistry> economicMinistry;
    private ProfileOracle profileOracle;

    public int getExecutionTime() {
        return executionTime;
    }

    public List<ConfigMedia> getMedia() { return media; }

    public ConfigOracle getOracle() {
        return oracle;
    }

    public ProfileOracle getProfileOracle() { return profileOracle; }

    public List<ConfigEconomicMinistry> getEconomicMinistry() { return economicMinistry; }
}
