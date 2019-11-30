package ar.edu.itba.model.config.profile;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.utils.ConfigRandom;
import ar.edu.itba.utils.Random;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Profile {

    private String name;
    private ProfileOracle oracle;
    private Economic economic;
    private List<ProfileParty> parties;
    private List<MediaTrust> mediaTrust;
    private FriendshipConfig friendshipConfig;
    private List<Interest> interests;

    private ConfigRandom r = ConfigRandom.getInstance();

    public List<Person> generatePersons(final int n, int idStart) throws Exception {
        int i = 0;
        final List<Person> persons = new LinkedList<>();
        while (i++ < n)
            persons.add(generatePerson(idStart++));
        return persons;
    }

    private Person generatePerson(int id) throws Exception {
        final double economicWellness = generateEconomicWellness(economic.getLowProb(), economic.getMidProb(), economic.getHighProb());
        final Map<String, Double> politicalOrientation = generatePoliticalOrientation();
        final Map<String, Double> mediaTrust = generateMediaTrust();
        final Map<String, Double> interests = generateInterests();
        final double skepticism = generateSkepticism(oracle.getMinProb(), oracle.getMaxProb());
        return new Person(id, economicWellness, politicalOrientation, mediaTrust, interests, skepticism, oracle.getLiePenalty(), oracle.getTrueReward());
    }

    private double generateSkepticism(final double minRational, final double maxRational) {
        return r.generateDouble(minRational, maxRational);
    }

    private double generateEconomicWellness(double lowProb, double midProb, double highProb) {

        midProb = lowProb + midProb;

        final double rand = r.generateDouble();
        final double wellness;

        if (rand >= 0 && rand < lowProb)
            wellness = r.generateDouble(SocialClass.LOW.getStart(), SocialClass.LOW.getEnd());
        else if (rand >= lowProb && rand < midProb)
            wellness = r.generateDouble(SocialClass.MID.getStart(), SocialClass.MID.getEnd());
        else
            wellness = r.generateDouble(SocialClass.HIGH.getStart(), SocialClass.HIGH.getEnd());
        return wellness;
    }

    private Map<String, Double> generatePoliticalOrientation()  {
        final Map<String, Double> politicalOrientation = new HashMap();
        for (final ProfileParty p : parties)
            politicalOrientation.put(p.getName(), r.generateDouble(p.getMinScore(), p.getMaxScore()));
        return politicalOrientation;
    }

    private Map<String, Double> generateMediaTrust() {
        final Map<String, Double> mediaTrust = new HashMap<>();
        for (final MediaTrust mt : this.mediaTrust)
            mediaTrust.put(mt.getName(), r.generateDouble(mt.getMinRational(), mt.getMaxRational()));
        return mediaTrust;
    }

    private Map<String, Double> generateInterests() {
        final Map<String, Double> interests = new HashMap<>();
        for (final Interest i : this.interests)
            interests.put(i.getName(), i.getRational());
        return interests;
    }


    public String getName() {
        return name;
    }

    public ProfileOracle getOracle() {
        return oracle;
    }

    public Economic getEconomic() {
        return economic;
    }

    public List<ProfileParty> getParties() {
        return parties;
    }

    public List<MediaTrust> getMediaTrust() {
        return mediaTrust;
    }

    public FriendshipConfig getFriendshipConfig() {
        return friendshipConfig;
    }

    public List<Interest> getInterests() {
        return interests;
    }
}
