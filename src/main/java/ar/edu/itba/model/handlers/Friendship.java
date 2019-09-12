package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.config.profile.FriendshipConfig;
import ar.edu.itba.model.config.profile.Profile;
import ar.edu.itba.utils.Random;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friendship {

    private Friendship() {

    }

    public static void generateFriendships(final Map<Profile, List<Person>> profilePersons, final List<Person> persons) {
        for (final Map.Entry<Profile, List<Person>> e : profilePersons.entrySet()) {
            for (final Person p : e.getValue())
                p.setFriendsTrust(generateFriendships(p, persons, e.getKey().getFriendshipConfig()));
        }
    }

    private static Map<Person, Double> generateFriendships(final Person p, final List<Person> persons,
                                                           final FriendshipConfig friendshipConfig) {
        final Map<Person, Double> friendsTrust = new HashMap<>();
        final int f = Random.generateInt(friendshipConfig.getMinFriends(), friendshipConfig.getMaxFriends());
        for (int i = 0 ; i < f ; i++) {
            Person friend;
            do {
                 friend = persons.get(Random.generateInt(0, persons.size() - 1));
            } while(friendsTrust.containsKey(friend) || friend.equals(p));
            friendsTrust.put(friend, Random.generateDouble(friendshipConfig.getMinRational(), friendshipConfig.getMaxRational()));
        }
        return friendsTrust;
    }
}
