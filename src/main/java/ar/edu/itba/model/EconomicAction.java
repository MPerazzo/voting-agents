package ar.edu.itba.model;

import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.SocialClass;

import java.util.Map;

public class EconomicAction {
    private final PoliticalParty ruler;
    private final Map<SocialClass, Double> impact;

    public EconomicAction(final PoliticalParty ruler, final Map<SocialClass, Double> impact) {
        this.ruler = ruler;
        this.impact = impact;
    }

    public Map<SocialClass, Double> getImpact() {
        return impact;
    }

    public PoliticalParty getRuler() {
        return ruler;
    }
}
