package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.utils.Random;

import java.util.*;

public class EconomicMinistry {

    private static EconomicMinistry instance = new EconomicMinistry();

    private static double prob;
    private static double minRational;
    private static double maxRational;
    private static double competence;

    private static final List<EconomicAction> actions = new LinkedList<>();

    private EconomicMinistry() {

    }

    public void setProperties(final double prob, final double minRational, final double maxRational, final double competence) {
        this.prob = prob;
        this.minRational = minRational;
        this.maxRational = maxRational;
        this.competence = competence;
    }

    public static List<EconomicAction> generateEconomicAction(final String ruler) {

        if (Random.generateDouble() > prob)
            return actions;

        Map<SocialClass, Double> impact = new HashMap<>();
        for (final SocialClass s : SocialClass.values()){

           double effect = 1.0;

           if (Random.generateDouble()>competence){
               effect = -1.0;
           }

            impact.put(s, effect * Random.generateDouble(minRational, maxRational));
            //System.out.println(impact.get(s));
        }
        final EconomicAction e = new EconomicAction(ruler, impact);
        actions.add(e);
        return actions;
    }



    public static EconomicMinistry getInstance() {
        return instance;
    }



}
