package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;

import java.util.Map;

public class EconomicAction {
    private final Map<SocialClass, Double> impact;

    public EconomicAction(final Map<SocialClass, Double> impact) {
        this.impact = impact;
    }

    public Map<SocialClass, Double> getImpact() {
        return impact;
    }
}
