package ar.edu.itba.model.config;


import ar.edu.itba.model.Election;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.Oracle;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.model.config.profile.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Configuration {

    private static final double EPSILON = 0.0001;

    private static Configuration instance;

    private InputData inputData;

    private Configuration() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream("config.json"));
        Gson gson = new GsonBuilder().create();
        inputData = gson.fromJson(reader, InputData.class);
        validateConfiguration();
    }

    public void init() {
        Media.getInstance().setSources(inputData.getMedia(), inputData.getSubjects());
        Profiler.getInstance().setProfiles(generateProfilerMap());
        Election.getInstance().setProperties(inputData.getElection().getInitialRuler(), inputData.getElection().getPeriod(), inputData.getParties());
        Oracle.getInstance().setProperties(inputData.getOracle().getProb(), inputData.getOracle().getMinPercentage(),inputData.getOracle().getMaxPercentage(), inputData.getOracle().getTimeTolerance(), inputData.getOracle().getImpactTolerance(), inputData.getParties(), inputData.getSubjects());
    }

    private Map<Profile, Integer> generateProfilerMap() {
        final Map<Profile, Integer> m = new HashMap<>();
        final List<Profile> profiles = inputData.getProfiles();
        final List<Integer> population = inputData.getPopulation();
        for (int i = 0 ; i < profiles.size() ; i++)
            m.put(profiles.get(i), population.get(i));
        return m;
    }

    private void validateConfiguration() throws Exception {
        validateSimulation();
        validatePopulation();
        validateElection();
        validateParties();
        validateMedia();
        validateOracle();
        validateSubjects();
        validateProfiles();
    }

    private void validateSimulation() throws Exception {
        if (inputData.getExecutionTime() <= 0 || inputData.getDt() <= 0)
            throw new Exception("Execution time or dt must be greater than zero");
    }

    private void validatePopulation() throws Exception {
        if (inputData.getPopulation().size() != inputData.getProfiles().size())
            throw new Exception("Population entries must be equal than profiles available");
        for (final int number : inputData.getPopulation()) {
            if (number < 0)
                throw new Exception("Population number can not be negative");
        }
    }

    private void validateElection() throws Exception {
        final ConfigElection election = inputData.getElection();
        if (!inputData.getParties().contains(election.getInitialRuler()))
            throw new Exception("InitialParty " + election.getInitialRuler() + " does not belong to an available party");
        if (election.getPeriod() <= 0)
            throw new Exception("Period must be greater than zero");
    }

    private void validateParties() throws Exception {
        validateNoRep(inputData.getParties(), "Parties can not be repeated");
    }

    private void validateMedia() throws Exception {
        final List<ConfigMedia> media = inputData.getMedia();
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
                    if (!inputData.getParties().contains(p.getName()))
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

    private void validateOracle() throws Exception {
        final ConfigOracle oracle = inputData.getOracle();
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
        validateRational(oracle.getProb());
    }

    private void validateSubjects() throws Exception {
        validateNoRep(inputData.getSubjects(), "Subjects can not be repeated");
    }

    private void validateProfiles() throws Exception {
        final Set<String> names = new HashSet<>();
        for (final Profile p : inputData.getProfiles()) {
            try {
                if (names.contains(p.getName()))
                    throw new Exception("Profiles can be repeated");
                names.add(p.getName());

                validateProfileOracle(p.getOracle());
                validateProfileEconomic(p.getEconomic());
                validateProfileParties(p.getParties());
                validateProfileMediaTrust(p.getMediaTrust());
                validateProfileFriendsTrust(p.getFriendshipConfig());
                validateProfileInterests(p.getInterests());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Profile " + p.getName() + " is invalid");
            }
        }
    }

    private void validateProfileOracle(final ProfileOracle oracle) throws Exception {
        validateRational(oracle.getMinProb());
        validateRational(oracle.getMaxProb());
        if (oracle.getMinProb() > oracle.getMaxProb())
            throw new Exception("Profile ProfileOracle max rational must be greater or equal than min rational");
        validateRational(oracle.getLiePenalty());
        validateRational(oracle.getTrueReward());
    }

    private void validateProfileEconomic(final Economic e) throws Exception {
        if (Math.abs(e.getLowProb() + e.getMidProb() + e.getHighProb() - 1D) >= EPSILON)
            throw new Exception("Profile Sum of economic probabilities must be 1");
    }

    private void validateProfileParties(final List<ProfileParty> parties) throws Exception {
        validateNoRep(parties.stream().map(p -> p.getName()).collect(Collectors.toList()), "Profile Parties can not have elements repeated");
        for (final ProfileParty p : parties) {
            if (!inputData.getParties().contains(p.getName()))
                throw new Exception("Profile ProfileParty name " + p.getName() + " does not belong to an available party");
            if (p.getMinScore() > p.getMaxScore())
                throw new Exception("Profile ProfileParty max score must be greater or equal than min score");
        }
    }

    private void validateProfileMediaTrust(final List<MediaTrust> mediaTrusts) throws Exception {
        validateNoRep(mediaTrusts.stream().map(m -> m.getName()).collect(Collectors.toList()), "Profile MediaTrust can not have elements repeated");
        for (final MediaTrust m : mediaTrusts) {
            if (!inputData.getMediaNames().contains(m.getName()))
                throw new Exception("Profile MediaTrust name " + m.getName() + " does not belong to an available media");
            validateRational(m.getRational());
        }
    }

    private void validateProfileFriendsTrust(final FriendshipConfig friendshipConfig) throws Exception {
        if (friendshipConfig.getMinRational() > friendshipConfig.getMaxRational())
            throw new Exception("Profile FriendTrust Max rational must be greater than min rational");
        validateRational(friendshipConfig.getMinRational());
        validateRational(friendshipConfig.getMaxRational());
        if (friendshipConfig.getMinFriends() < 0 || friendshipConfig.getMaxFriends() < 0)
            throw new Exception("Min and max friends must be positive");
        if (friendshipConfig.getMinFriends() > friendshipConfig.getMaxFriends())
            throw new Exception("Min friends must be greater than min friends");
    }

    private void validateProfileInterests(final List<Interest> interests) throws Exception {
        validateNoRep(interests.stream().map(i -> i.getName()).collect(Collectors.toList()), "Profile interests can not be repeated");
        for (final Interest i : interests) {
            if (!inputData.getSubjects().contains(i.getName()))
                throw new Exception("Interest name " + i.getName() + " does not belong to an available interest");
            validateRational(i.getRational());
        }
    }

    private void validateNoRep(final List<? extends Object> l, final String message) throws Exception {
        if (l.stream().distinct().count() != l.size())
            throw new Exception(message);
    }

    private void validateRational(final double r) throws Exception {
        if (r < 0 || r > 1)
            throw new Exception("Rational " + r + " must be between 0 and 1");
    }

    private void validatePercentage(final double r) throws Exception{
        if (r < 0 || r > 100)
            throw new Exception("Percentage " + r + " must be between 0 and 100");
    }

    public static Configuration getInstance() throws Exception {
        if (instance == null)
            instance = new Configuration();
        return instance;
    }

    public int getExecutionTime() {
        return inputData.getExecutionTime();
    }

    public int getDt() {
        return inputData.getDt();
    }

    public List<String> getPoliticalParties() {
        return inputData.getParties();
    }
}
