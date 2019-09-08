package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.model.utils.Random;

import java.util.*;

public class EconomicMinistry {

    private static final double MIN_SCORE = 2D;
    private static final double MAX_SCORE = 4D;

    private static final List<EconomicAction> economicActions = new LinkedList<>();

    public static EconomicAction generateEconomicAction() {
        Map<SocialClass, Double> impact = new HashMap<>();
        for (final SocialClass s : SocialClass.values())
            impact.put(s, Random.generateDoubleSigned(MIN_SCORE, MAX_SCORE));
        final EconomicAction e = new EconomicAction(impact);
        return e;
    }
}
