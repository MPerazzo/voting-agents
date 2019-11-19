package ar.edu.itba.model.config;

import ar.edu.itba.Main;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.profile.MediaTrust;
import ar.edu.itba.model.config.profile.ProfileOracle;
import ar.edu.itba.model.handlers.EconomicMinistry;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.Oracle;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.utils.Random;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RunConfiguration extends BaseConfiguration {
    private RunInputData runInputData;

    public RunConfiguration() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream("run.json"));
        Gson gson = new GsonBuilder().create();
        runInputData = gson.fromJson(reader, RunInputData.class);
        validateConfiguration();
    }

    @Override
    protected void validateConfiguration() throws Exception {
        if (runInputData.getExecutionTime() > 0)
            validateSimulation();

        if (runInputData.getMedia() != null)
            validateMedia(runInputData.getMedia(), InitialConfiguration.getInstance().getPoliticalParties());

        if (runInputData.getOracle() != null)
            validateOracle(runInputData.getOracle());

        if (runInputData.getProfileOracle() != null)
            validateProfileOracle(runInputData.getProfileOracle());

        if (runInputData.getEconomicMinistry() != null)
            validateEconomicMinistry(runInputData.getEconomicMinistry());

        if (runInputData.getMediaTrust() != null)
            validateProfileMediaTrust(runInputData.getMediaTrust());
    }

    private void validateSimulation() throws Exception {
        if (runInputData.getExecutionTime() <= 0)
            throw new Exception("Execution time must be greater than zero");
    }

    private void validateProfileMediaTrust(final List<MediaTrust> mediaTrusts) throws Exception {
        validateNoRep(mediaTrusts.stream().map(m -> m.getName()).collect(Collectors.toList()), "Profile MediaTrust can not have elements repeated");
        for (final MediaTrust m : mediaTrusts) {
            if (!InitialConfiguration.getInstance().getMediaNames().contains(m.getName()))
                throw new Exception("Profile MediaTrust name " + m.getName() + " does not belong to an available media");
            validateRational(m.getMinRational());
            validateRational(m.getMaxRational());
            if (m.getMinRational() > m.getMaxRational())
                throw new Exception("Media Trust min must be greater than max");
        }
    }

    public void overrideConfiguration() throws Exception {
        if (runInputData.getExecutionTime() > 0)
            Main.setExecutionTime(runInputData.getExecutionTime());

        if (runInputData.getMedia() != null)
            Media.getInstance().setSources(runInputData.getMedia(), InitialConfiguration.getInstance().getSubjects());

        if (runInputData.getOracle() != null)
        Oracle.getInstance().setProperties(runInputData.getOracle().getProb(), runInputData.getOracle().getMinPercentage(), runInputData.getOracle().getMaxPercentage(), runInputData.getOracle().getTimeTolerance(), runInputData.getOracle().getImpactTolerance(),
                InitialConfiguration.getInstance().getPoliticalParties(), InitialConfiguration.getInstance().getSubjects());

        if (runInputData.getEconomicMinistry() != null)
        EconomicMinistry.getInstance().setProperties(runInputData.getEconomicMinistry());

        if (runInputData.getProfileOracle() != null) {
            ProfileOracle profileOracle = runInputData.getProfileOracle();
            for (Person p : Profiler.getInstance().getPersons()) {
                final double skepticism = Random.generateDouble(profileOracle.getMinProb(), profileOracle.getMaxProb());
                p.setParams(skepticism, profileOracle.getLiePenalty(), profileOracle.getTrueReward());
            }
        }

        if (runInputData.getMediaTrust() != null) {
            final List<MediaTrust> mediaTrust = runInputData.getMediaTrust();
            final Map<String, Double> mapMediaTrust = new HashMap<>();
            for (final MediaTrust mt : mediaTrust)
                mapMediaTrust.put(mt.getName(), Random.generateDouble(mt.getMinRational(), mt.getMaxRational()));
            for (Person p : Profiler.getInstance().getPersons())
                p.setParams(mapMediaTrust);
        }

        if (runInputData.getFriendshipConfig() != null) {
            for (final Person p : Profiler.getInstance().getPersons()) {
                p.setFriends(runInputData.getFriendshipConfig().getMinRational(), runInputData.getFriendshipConfig().getMaxRational(),
                        runInputData.getFriendshipConfig().getMinFriends(), runInputData.getFriendshipConfig().getMaxFriends());
            }
        }
    }
}
