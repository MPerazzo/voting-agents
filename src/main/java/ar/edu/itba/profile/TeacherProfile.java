package ar.edu.itba.profile;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.Subject;

import java.util.HashMap;
import java.util.Map;

public class TeacherProfile extends BaseProfile {

    private static double SCORE = 10D;
    private static double DELTA_SCORE = 2D;

    private static TeacherProfile instance = null;

    private TeacherProfile() {
        this.mediaTrust = 0.5;
    }

    public static TeacherProfile getInstance() {
        if (instance == null)
            instance = new TeacherProfile();
        return instance;
    }

    @Override
    public Person generatePerson(final int id) throws Exception {
        return new Person(id, generateEconomicWellness(), generatePoliticalOrientation(),
                generateMediaTrust(), generateInterests());
    }

    @Override
    protected double generateEconomicWellness() throws Exception {
        return super.generateEconomicWellness(0.25, 0.70, 0.05);
    }

    @Override
    protected Map<PoliticalParty, Double> generatePoliticalOrientation() throws Exception {
        return super.generatePoliticalOrientation(0.70, 0.30, SCORE, DELTA_SCORE);
    }

    @Override
    protected Map<MediaId, Double> generateMediaTrust() throws Exception {
        return super.generateMediaTrust(0.70, 0.30, 0.5);
    }

    @Override
    protected Map<Subject, Double> generateInterests() {
        Map<Subject, Double> interests = new HashMap<>();
        interests.put(Subject.CULTURAL, 0.5);
        interests.put(Subject.POLITICS, 0.6);
        return interests;
    }
}
