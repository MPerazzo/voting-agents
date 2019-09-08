package ar.edu.itba.model.enums;

import java.util.Map;

public enum PoliticalParty {
    LEFT,
    RIGHT;

    public static PoliticalParty getPoliticalParty(final Map<PoliticalParty, Double> politicalOrientation) {
        double maxValue = 0;
        PoliticalParty p = null;
        for (Map.Entry<PoliticalParty, Double> e : politicalOrientation.entrySet()) {
            if (e.getValue() > maxValue) {
                maxValue = e.getValue();
                p = e.getKey();
            }
        }
        return p;
    }
}
