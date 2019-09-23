package ar.edu.itba.model;

import ar.edu.itba.model.config.Configuration;

import java.util.HashMap;
import java.util.Map;

public class News {
    private final String subject;
    private final String media;
    private final Map<String, Double> impact;
    private final Map<String, Double> impactDifferential;

    public News(String subject, String media, Map<String, Double> impact) throws Exception {
        this.subject = subject;
        this.media = media;
        this.impact = impact;
        this.impactDifferential = new HashMap<>();
        for (final String party : Configuration.getInstance().getPoliticalParties())
            impactDifferential.put(party, 0D);
    }

    public String getSubject() {
        return subject;
    }

    public String getMedia() {
        return media;
    }

    public Map<String, Double> getImpact() {
        return impact;
    }

    public Map<String, Double> getImpactDifferential() {
        return impactDifferential;
    }

    public void updateImpactDifferential(final Map<String, Double> impactDifferential) {
        for (final Map.Entry<String, Double> e : impactDifferential.entrySet())
            this.impactDifferential.put(e.getKey(), this.impactDifferential.get(e.getKey()) + impactDifferential.get(e.getKey()));
    }
}
