package ar.edu.itba.model;

import java.util.Map;

public class News {
    private final String subject;
    private final String media;
    private final Map<String, Double> impact;

    public News(String subject, String media, Map<String, Double> impact) {
        this.subject = subject;
        this.media = media;
        this.impact = impact;
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
}
