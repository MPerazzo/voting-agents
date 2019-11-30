package ar.edu.itba.model;

import ar.edu.itba.model.enums.SocialClass;

import java.util.HashMap;
import java.util.Map;

public class EconomicAction {
    private final String ruler;
    private final Map<SocialClass, Double> totalImpact;
    private final Map<SocialClass, Double> impact;

    public EconomicAction(final String ruler, final Map<SocialClass, Double> impact) {
        this.ruler = ruler;
        this.impact = impact;
        this.totalImpact = new HashMap<>();
        for (SocialClass s : SocialClass.values())
            totalImpact.put(s, 0D);
    }

    public Map<SocialClass, Double> getImpact() {
        return impact;
    }

    public Map<SocialClass, Double> getTotalImpact() {
        return totalImpact;
    }

    public void update(final SocialClass socialClass, final double impact) {
        totalImpact.put(socialClass, totalImpact.get(socialClass) + impact);
    }

    public String getRuler() {
        return ruler;
    }
}
