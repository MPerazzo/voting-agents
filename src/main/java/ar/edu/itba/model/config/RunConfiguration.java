package ar.edu.itba.model.config;

import ar.edu.itba.model.Person;
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
        validateSimulation();
        validateMedia(runInputData.getMedia(), InitialConfiguration.getInstance().getPoliticalParties());
        validateOracle(runInputData.getOracle());
        validateProfileOracle(runInputData.getProfileOracle());
        validateEconomicMinistry(runInputData.getEconomicMinistry());
    }

    private void validateSimulation() throws Exception {
        if (runInputData.getExecutionTime() <= 0)
            throw new Exception("Execution time must be greater than zero");
    }

    public void overrideConfiguration() throws Exception {
        Media.getInstance().setSources(runInputData.getMedia(), InitialConfiguration.getInstance().getSubjects());
        Oracle.getInstance().setProperties(runInputData.getOracle().getProb(), runInputData.getOracle().getMinPercentage(), runInputData.getOracle().getMaxPercentage(), runInputData.getOracle().getTimeTolerance(), runInputData.getOracle().getImpactTolerance(),
                InitialConfiguration.getInstance().getPoliticalParties(), InitialConfiguration.getInstance().getSubjects());
        EconomicMinistry.getInstance().setProperties(runInputData.getEconomicMinistry());

        ProfileOracle profileOracle = runInputData.getProfileOracle();
        for (Person p : Profiler.getInstance().getPersons()) {
            final double skepticism = Random.generateDouble(profileOracle.getMinProb(), profileOracle.getMaxProb());
            p.setParams(skepticism, profileOracle.getLiePenalty(), profileOracle.getTrueReward());
        }
    }
}
