package ar.edu.itba.model.config;


import ar.edu.itba.model.Election;
import ar.edu.itba.model.handlers.EconomicMinistry;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.Oracle;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.model.config.profile.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class InitialConfiguration extends BaseConfiguration {

    private static InitialConfiguration instance;

    private InitialInputData initialInputData;

    private InitialConfiguration() throws Exception {
        Reader reader = new InputStreamReader(new FileInputStream("config.json"));
        Gson gson = new GsonBuilder().create();
        initialInputData = gson.fromJson(reader, InitialInputData.class);
        validateConfiguration();
    }

    public void init() {
        Media.getInstance().setSources(initialInputData.getMedia(), initialInputData.getSubjects());
        Profiler.getInstance().setProfiles(generateProfilerMap());
        Election.getInstance().setProperties(initialInputData.getElection().getInitialRuler(), initialInputData.getElection().getPeriod(), initialInputData.getParties());
        Oracle.getInstance().setProperties(initialInputData.getOracle().getProb(), initialInputData.getOracle().getMinPercentage(), initialInputData.getOracle().getMaxPercentage(), initialInputData.getOracle().getTimeTolerance(), initialInputData.getOracle().getImpactTolerance(), initialInputData.getParties(), initialInputData.getSubjects());
        EconomicMinistry.getInstance().setProperties(initialInputData.getEconomicMinistry());
    }

    private Map<Profile, Integer> generateProfilerMap() {
        final Map<Profile, Integer> m = new HashMap<>();
        final List<Profile> profiles = initialInputData.getProfiles();
        final List<Integer> population = initialInputData.getPopulation();
        for (int i = 0 ; i < profiles.size() ; i++)
            m.put(profiles.get(i), population.get(i));
        return m;
    }

    protected void validateConfiguration() throws Exception {
        validateSimulation();
        validatePopulation();
        validateElection();
        validateParties();
        validateMedia(initialInputData.getMedia(), initialInputData.getParties());
        validateOracle(initialInputData.getOracle());
        validateSubjects();
        validateProfiles();
        validateEconomicMinistry(initialInputData.getEconomicMinistry());
    }

    private void validateSimulation() throws Exception {
        if (initialInputData.getExecutionTime() <= 0 || initialInputData.getDt() <= 0)
            throw new Exception("Execution time or dt must be greater than zero");
    }

    private void validatePopulation() throws Exception {
        if (initialInputData.getPopulation().size() != initialInputData.getProfiles().size())
            throw new Exception("Population entries must be equal than profiles available");
        for (final int number : initialInputData.getPopulation()) {
            if (number < 0)
                throw new Exception("Population number can not be negative");
        }
    }

    private void validateElection() throws Exception {
        final ConfigElection election = initialInputData.getElection();
        if (!initialInputData.getParties().contains(election.getInitialRuler()))
            throw new Exception("InitialParty " + election.getInitialRuler() + " does not belong to an available party");
        if (election.getPeriod() <= 0)
            throw new Exception("Period must be greater than zero");
    }

    private void validateParties() throws Exception {
        validateNoRep(initialInputData.getParties(), "Parties can not be repeated");
    }

    private void validateSubjects() throws Exception {
        validateNoRep(initialInputData.getSubjects(), "Subjects can not be repeated");
    }

    private void validateProfiles() throws Exception {
        final Set<String> names = new HashSet<>();
        for (final Profile p : initialInputData.getProfiles()) {
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

    private void validateProfileEconomic(final Economic e) throws Exception {
        if (Math.abs(e.getLowProb() + e.getMidProb() + e.getHighProb() - 1D) >= EPSILON)
            throw new Exception("Profile Sum of economic probabilities must be 1");
    }

    private void validateProfileParties(final List<ProfileParty> parties) throws Exception {
        validateNoRep(parties.stream().map(p -> p.getName()).collect(Collectors.toList()), "Profile Parties can not have elements repeated");
        for (final ProfileParty p : parties) {
            if (!initialInputData.getParties().contains(p.getName()))
                throw new Exception("Profile ProfileParty name " + p.getName() + " does not belong to an available party");
            if (p.getMinScore() > p.getMaxScore())
                throw new Exception("Profile ProfileParty max score must be greater or equal than min score");
        }
    }

    private void validateProfileMediaTrust(final List<MediaTrust> mediaTrusts) throws Exception {
        validateNoRep(mediaTrusts.stream().map(m -> m.getName()).collect(Collectors.toList()), "Profile MediaTrust can not have elements repeated");
        for (final MediaTrust m : mediaTrusts) {
            if (!initialInputData.getMediaNames().contains(m.getName()))
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
            if (!initialInputData.getSubjects().contains(i.getName()))
                throw new Exception("Interest name " + i.getName() + " does not belong to an available interest");
            validateRational(i.getRational());
        }
    }

    public static InitialConfiguration getInstance() throws Exception {
        if (instance == null)
            instance = new InitialConfiguration();
        return instance;
    }

    public int getExecutionTime() {
        return initialInputData.getExecutionTime();
    }

    public int getDt() {
        return initialInputData.getDt();
    }

    public List<String> getPoliticalParties() {
        return initialInputData.getParties();
    }

    public List<String> getSubjects() {
        return initialInputData.getSubjects();
    }

}
