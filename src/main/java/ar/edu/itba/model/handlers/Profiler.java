package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Person;
import ar.edu.itba.profile.TeacherProfile;

import java.util.LinkedList;
import java.util.List;

public class Profiler {

    private final static int id = 1;
    private final static TeacherProfile teacherProfile = TeacherProfile.getInstance();
    private final static List<Person> persons = new LinkedList<>();

    public static List<Person> generatePersons(final int n) throws Exception {
        persons.addAll(teacherProfile.generatePersons(n, id));
        return persons;
    }
}
