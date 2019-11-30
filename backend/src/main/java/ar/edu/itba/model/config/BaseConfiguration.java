package ar.edu.itba.model.config;

import ar.edu.itba.model.config.profile.MediaParty;
import ar.edu.itba.model.config.profile.MinistryPartyEconomic;
import ar.edu.itba.model.config.profile.ProfileOracle;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseConfiguration {

    protected static final double EPSILON = 0.0001;

    protected void validateMedia(final List<ConfigMedia> media, final List<String> parties) throws Exception {
        //final List<ConfigMedia> media = inputData.getMedia();
        validateNoRep(media.stream().map(m -> m.getName()).collect(Collectors.toList()), "Media can not be repeated");
        for (final ConfigMedia m : media) {
            try {
                validateRational(m.getNewsProb());
                validateRational(m.getLieProb());
                validateNoRep(m.getParties().stream().map(p -> p.getName()).collect(Collectors.toList()), "Media parties can not have elements repeated");
                validatePercentage(m.getMinPercentage());
                validatePercentage(m.getMaxPercentage());
                if (m.getMinPercentage() > m.getMaxPercentage())
                    throw new Exception("Max percentage must be greater or equal than min percentage");
                if (m.getTimeTolerance() <= 0)
                    throw new Exception("Time tolerance must be greater than zero");
                double probSum = 0;
                for (final MediaParty p : m.getParties()) {
                    //if (!inputData.getParties().contains(p.getName()))
                    if (!parties.contains(p.getName()))
                        throw new Exception("Media party " + p.getName() + " does not belong to an available party");
                    probSum += p.getProb();
                }
                if (Math.abs(probSum - 1D) > EPSILON)
                    throw new Exception("Sum of parties probability must be 1");
            }
            catch (final Exception e) {
                e.printStackTrace();
                throw new Exception("Media " + m.getName() + " is invalid");
            }
        }
    }

    protected void validateOracle(final ConfigOracle oracle) throws Exception {
        //final ConfigOracle oracle = inputData.getOracle();
        validateRational(oracle.getProb());
        if (oracle.getTimeTolerance() <= 0)
            throw new Exception("Invalid oracle news tolerance");
        if (oracle.getImpactTolerance() <= 0)
            throw new Exception("Invalid oracle impact tolerance");

        final double minPercentage = oracle.getMinPercentage();
        final double maxPercentage = oracle.getMaxPercentage();
        validatePercentage(minPercentage);
        validatePercentage(maxPercentage);
        if (minPercentage > maxPercentage)
            throw new Exception("Max percentage must be greater or equal than min percentage");
    }

    protected void validateEconomicMinistry(final List<ConfigEconomicMinistry> ministryList) throws Exception{
        //final List<ConfigEconomicMinistry> ministryList = inputData.getEconomicMinistry();
        for(ConfigEconomicMinistry cem : ministryList){
            validateRational(cem.getProb());
            for(MinistryPartyEconomic mpe : cem.getEconomic()){
                validateRational(mpe.getCompetence());
                double min = mpe.getMaxRational();
                double max = mpe.getMaxRational();
                validateRational(min);
                validateRational(max);
                if (min > max)
                    throw new Exception("Max percentage must be greater or equal than min percentage");
            }
        }
    }

    protected void validateProfileOracle(final ProfileOracle oracle) throws Exception {
        validateRational(oracle.getMinProb());
        validateRational(oracle.getMaxProb());
        if (oracle.getMinProb() > oracle.getMaxProb())
            throw new Exception("Profile ProfileOracle max rational must be greater or equal than min rational");
        validateRational(oracle.getLiePenalty());
        validateRational(oracle.getTrueReward());
    }

    protected void validateNoRep(final List<? extends Object> l, final String message) throws Exception {
        if (l.stream().distinct().count() != l.size())
            throw new Exception(message);
    }

    protected void validateRational(final double r) throws Exception {
        if (r < 0 || r > 1)
            throw new Exception("Rational " + r + " must be between 0 and 1");
    }

    protected void validatePercentage(final double r) throws Exception{
        if (r < 0 || r > 100)
            throw new Exception("Percentage " + r + " must be between 0 and 100");
    }

    protected abstract void validateConfiguration() throws Exception;
}
