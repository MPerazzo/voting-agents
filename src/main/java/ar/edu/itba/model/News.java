package ar.edu.itba.model;

import ar.edu.itba.model.enums.MediaId;
import ar.edu.itba.model.enums.PoliticalParty;
import ar.edu.itba.model.enums.Subject;

import java.util.Map;

public class News {
    private final Subject subject;
    private final MediaId media;
    private final Map<PoliticalParty, Double> impact;

    public News(Subject subject, MediaId media, Map<PoliticalParty, Double> impact) {
        this.subject = subject;
        this.media = media;
        this.impact = impact;
    }

    public Subject getSubject() {
        return subject;
    }

    public MediaId getMedia() {
        return media;
    }

    public Map<PoliticalParty, Double> getImpact() {
        return impact;
    }
}
