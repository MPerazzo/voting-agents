package ar.edu.itba.model.handlers;

import ar.edu.itba.model.config.profile.Profile;
import ar.edu.itba.model.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Profiler {

    private static int id = 1;
    private static Map<Profile, Integer> profiles;
    private static final List<Person> persons = new LinkedList<>();

    private Profiler() {
    }

    public static List<Person> generatePersons() throws Exception {
        final Map<Profile, List<Person>> m = new HashMap<>();
        for (final Map.Entry<Profile, Integer> e : profiles.entrySet()) {
            final List<Person> profilePersons = e.getKey().generatePersons(e.getValue(), id);
            m.put(e.getKey(), profilePersons);
            persons.addAll(profilePersons);
            id += e.getValue();
        }
        Friendship.generateFriendships(m, persons);
        return persons;
    }

    public static void setProfiles(final Map<Profile, Integer> l) {
        profiles = l;
    }
}
