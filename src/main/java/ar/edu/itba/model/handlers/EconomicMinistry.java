package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.config.ConfigEconomicMinistry;
import ar.edu.itba.model.config.profile.MinistryPartyEconomic;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.utils.Random;

import java.util.*;

public class EconomicMinistry {

    private static EconomicMinistry instance = new EconomicMinistry();

    private static Map<String, Double> prob = new HashMap<>();
    private static Map<String, Map<SocialClass, Double>> minRational = new HashMap<>();
    private static Map<String, Map<SocialClass, Double>> maxRational = new HashMap<>();
    private static Map<String, Map<SocialClass, Double>> competence = new HashMap<>();

    private static final List<EconomicAction> actions = new LinkedList<>();

    private EconomicMinistry() {

    }

    public void setProperties(final List<ConfigEconomicMinistry> economicMinistries) {

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
            minRational.put(e.getName(),minAux);
            maxRational.put(e.getName(),maxAux);
            competence.put(e.getName(),competenceAux);
        }

    }


    public static List<EconomicAction> generateEconomicAction(final String ruler) {

        if (Random.generateDouble() > prob.get(ruler))
            return actions;

        Map<SocialClass, Double> impact = new HashMap<>();
        for (final SocialClass s : SocialClass.values()){

           double effect = 1.0;

           if (Random.generateDouble()>competence.get(ruler).get(s)){
               effect = -1.0;
           }

            impact.put(s, effect * Random.generateDouble(minRational.get(ruler).get(s), maxRational.get(ruler).get(s)));
            //System.out.println(impact.get(s));
        }
        final EconomicAction e = new EconomicAction(ruler, impact);
        actions.add(e);
        return actions;
    }



    public static EconomicMinistry getInstance() {
        return instance;
    }

    public static List<EconomicAction> getActions() {
        return actions;
    }
}
