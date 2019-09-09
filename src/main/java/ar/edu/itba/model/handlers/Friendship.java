package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Person;
import ar.edu.itba.utils.Random;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Friendship {

    private static int MIN_FRIENDS = 1;
    private static int MAX_FRIENDS = 8;

    public static void generateFriendships(final List<Person> persons) {
        for (final Person p : persons) {
            final Map<Person, Double> friendsTrust = generateFriendships(p, persons);
            p.setFriendsTrust(friendsTrust);
        }
    }

    private static Map<Person, Double> generateFriendships(final Person p, final List<Person> persons) {
        final Map<Person, Double> friendsTrust = new HashMap<>();
        final int f = Random.generateInt(MIN_FRIENDS, MAX_FRIENDS);
        for (int i = 0 ; i < f ; i++) {
            final Person friend = persons.get(Random.generateInt(0, persons.size() - 1));
            if (!friendsTrust.containsKey(friend) && !friend.equals(p))
                friendsTrust.put(friend, Random.generateDouble());
        }
        return friendsTrust;
    }
}
