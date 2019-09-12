package ar.edu.itba.model.handlers;

import ar.edu.itba.model.EconomicAction;
import ar.edu.itba.model.enums.SocialClass;
import ar.edu.itba.utils.Random;

import java.util.*;

public class EconomicMinistry {

    private static double prob;
    private static double minScore;
    private static double maxScore;

    private static final List<EconomicAction> actions = new LinkedList<>();

    private EconomicMinistry() {

    }

    public static void setProperties(final double p, final double mins, final double maxs) {
        prob = p;
        minScore = mins;
        maxScore = maxs;
    }

    public static Optional<EconomicAction> generateEconomicAction(final String ruler) {

        if (Random.generateDouble() > prob)
            return Optional.empty();

        Map<SocialClass, Double> impact = new HashMap<>();
        for (final SocialClass s : SocialClass.values())
            impact.put(s, Random.generateDoubleSigned(minScore, maxScore));
        final EconomicAction e = new EconomicAction(ruler, impact);
        actions.add(e);
        return Optional.of(e);
    }
}
