package ar.edu.itba.model.profile;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.enums.Subject;
import ar.edu.itba.utils.Random;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class BaseProfile {

    protected double mediaTrust;

    public List<Person> generatePersons(final int n, int idStart) throws Exception {
        int i = 0;
        final List<Person> persons = new LinkedList<>();
        while (i++ < n)
            persons.add(generatePerson(idStart++));
        return persons;
    }

    protected double generateEconomicWellness(double lowProb, double midProb, double highProb) throws Exception {

        if (lowProb + midProb + highProb != 1D)
            throw new Exception("Wrong distribution");

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

    protected Map<PoliticalParty, Double> generatePoliticalOrientation(double leftProb, double rightProb, double score,
                                                                       double deltaScore) throws Exception {
        if (leftProb + rightProb != 1D)
            throw new Exception("Wrong distribution");

        final double rand = Random.generateDouble();
        final Map<PoliticalParty, Double> m = new HashMap<>();

        if (rand >= 0 && rand < leftProb) {
            m.put(PoliticalParty.LEFT, score - deltaScore);
            m.put(PoliticalParty.RIGHT, deltaScore);
        }
        else {
            m.put(PoliticalParty.LEFT, deltaScore);
            m.put(PoliticalParty.RIGHT, score - deltaScore);
        }
        return m;
    }

    protected Map<MediaId, Double> generateMediaTrust(double leftProb, double rightProb, double r) throws Exception {
        if (leftProb + rightProb != 1D)
            throw new Exception("Wrong distribution");

        final double rand1 = Random.generateDouble();
        final double rand2 = Random.generateDouble();
        final double rand3 = Random.generateDouble();
        final Map<MediaId, Double> m = new HashMap<>();

        if (rand1 < leftProb) {
            m.put(MediaId.PAGINA12, Random.generateDouble(0, mediaTrust));
            m.put(MediaId.CRONICA, Random.generateDouble(0, mediaTrust));
        }

        if (rand2 < rightProb) {
            m.put(MediaId.CLARIN, Random.generateDouble(0, mediaTrust));
            m.put(MediaId.LANACION, Random.generateDouble(0, mediaTrust));
        }

        if (rand3 < r)
            m.put(MediaId.INFOBAE, Random.generateDouble(0, mediaTrust));

        return m;
    }

    public abstract Person generatePerson(final int id) throws Exception;
    protected abstract double generateEconomicWellness() throws Exception;
    protected abstract Map<PoliticalParty, Double> generatePoliticalOrientation() throws Exception;
    protected abstract Map<MediaId, Double> generateMediaTrust() throws Exception;
    protected abstract Map<Subject, Double> generateInterests();
}
