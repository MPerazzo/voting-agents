package ar.edu.itba.model.config.profile;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.utils.Random;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Profile {

    private String name;
    private Economic economic;
    private List<Party> parties;
    private List<MediaTrust> mediaTrust;
    private FriendshipConfig friendshipConfig;
    private List<Interest> interests;

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
        return new Person(id, economicWellness, politicalOrientation, mediaTrust, interests);
    }

    private double generateEconomicWellness(double lowProb, double midProb, double highProb) {

        midProb = lowProb + midProb;

        final double rand = Random.generateDouble();
        final double wellness;

        if (rand >= 0 && rand < lowProb)
            wellness = Random.generateDouble(SocialClass.LOW.getStart(), SocialClass.LOW.getEnd());
        else if (rand >= lowProb && rand < midProb)
            wellness = Random.generateDouble(SocialClass.MID.getStart(), SocialClass.MID.getEnd());
        else
            wellness = Random.generateDouble(SocialClass.HIGH.getStart(), SocialClass.HIGH.getEnd());
        return wellness;
    }

    private Map<String, Double> generatePoliticalOrientation()  {
        final Map<String, Double> politicalOrientation = new HashMap();
        for (final Party p : parties)
            politicalOrientation.put(p.getName(), Random.generateDouble(p.getMinScore(), p.getMaxScore()));
        return politicalOrientation;
    }

    private Map<String, Double> generateMediaTrust() {
        final Map<String, Double> mediaTrust = new HashMap<>();
        for (final MediaTrust mt : this.mediaTrust)
            mediaTrust.put(mt.getName(), mt.getRational());
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

    public Economic getEconomic() {
        return economic;
    }

    public List<Party> getParties() {
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
