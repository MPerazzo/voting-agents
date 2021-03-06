package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.config.ConfigEconomicMinistry;
import ar.edu.itba.model.config.profile.Economic;
import ar.edu.itba.model.config.profile.MinistryPartyEconomic;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.utils.EconomicRandom;
import ar.edu.itba.utils.Random;

import java.util.*;

public class EconomicMinistry {

    private static EconomicMinistry instance = new EconomicMinistry();

    private static Map<String, Double> prob = new HashMap<>();
    private static Map<String, Map<SocialClass, Double>> minImpact = new HashMap<>();
    private static Map<String, Map<SocialClass, Double>> maxImpact = new HashMap<>();
    private static Map<String, Map<SocialClass, Double>> competence = new HashMap<>();

    private static final List<EconomicAction> actions = new LinkedList<>();

    private static Random r = EconomicRandom.getInstance();

    private EconomicMinistry() {

    }

    public void setProperties(final List<ConfigEconomicMinistry> economicMinistries) {
        prob.clear();
        minImpact.clear();
        maxImpact.clear();
        competence.clear();

        for (final ConfigEconomicMinistry e : economicMinistries){
            prob.put(e.getName(), e.getProb());
            Map<SocialClass, Double> minAux = new HashMap<>();
            Map<SocialClass, Double> maxAux = new HashMap<>();
            Map<SocialClass, Double> competenceAux = new HashMap<>();
            for(MinistryPartyEconomic mpe : e.getEconomic()){
                //REFACTOR

                if(mpe.getName().equals("low")){
                    minAux.put(SocialClass.LOW,mpe.getMinRational());
                    maxAux.put(SocialClass.LOW,mpe.getMaxRational());
                    competenceAux.put(SocialClass.LOW,mpe.getCompetence());
                }
                if(mpe.getName().equals("mid")){
                    minAux.put(SocialClass.MID,mpe.getMinRational());
                    maxAux.put(SocialClass.MID,mpe.getMaxRational());
                    competenceAux.put(SocialClass.MID,mpe.getCompetence());
                }
                if(mpe.getName().equals("high")){
                    minAux.put(SocialClass.HIGH,mpe.getMinRational());
                    maxAux.put(SocialClass.HIGH,mpe.getMaxRational());
                    competenceAux.put(SocialClass.HIGH,mpe.getCompetence());
                }
            }
            minImpact.put(e.getName(),minAux);
            maxImpact.put(e.getName(),maxAux);
            competence.put(e.getName(),competenceAux);
        }

    }


    public static Optional<EconomicAction> generateEconomicAction(final String ruler) {

        if (r.generateDouble() > prob.get(ruler))
            return Optional.empty();

        Map<SocialClass, Double> impact = new HashMap<>();
        for (final SocialClass s : SocialClass.values()){

           double effect = 1.0;

           if (r.generateDouble() > competence.get(ruler).get(s)){
               effect = -1.0;
           }

            impact.put(s, effect * r.generateDouble(minImpact.get(ruler).get(s), maxImpact.get(ruler).get(s)));
            //System.out.println(impact.get(s));
        }
        final EconomicAction e = new EconomicAction(ruler, impact);
        actions.add(e);
        return Optional.of(e);
    }

    public static void clear() {
        actions.clear();
    }

    public static EconomicMinistry getInstance() {
        return instance;
    }

    public static List<EconomicAction> getActions() {
        return actions;
    }
}
