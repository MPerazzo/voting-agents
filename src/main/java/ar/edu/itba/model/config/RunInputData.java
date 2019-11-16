package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.MediaTrust;
import ar.edu.itba.model.config.profile.ProfileOracle;

import java.util.List;

public class RunInputData {
    private int executionTime;
    private List<ConfigMedia> media;
    private ConfigOracle oracle;
    private ProfileOracle profileOracle;
    private List<ConfigEconomicMinistry> economicMinistry;
    private List<MediaTrust> mediaTrust;


    public int getExecutionTime() {
        return executionTime;
    }

    public List<ConfigMedia> getMedia() { return media; }

    public ConfigOracle getOracle() {
        return oracle;
    }

    public ProfileOracle getProfileOracle() { return profileOracle; }

    public List<ConfigEconomicMinistry> getEconomicMinistry() { return economicMinistry; }

    public List<MediaTrust> getMediaTrust() {
        return mediaTrust;
    }
}
