package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;

import java.util.Map;

public class EconomicAction {
    private final String ruler;
    private final Map<SocialClass, Double> impact;

    public EconomicAction(final String ruler, final Map<SocialClass, Double> impact) {
        this.ruler = ruler;
        this.impact = impact;
    }

    public Map<SocialClass, Double> getImpact() {
        return impact;
    }

    public String getRuler() {
        return ruler;
    }
}
