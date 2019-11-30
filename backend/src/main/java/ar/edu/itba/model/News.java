package ar.edu.itba.model;

import java.util.Objects;


public class News {

    private final Event event;

    private final String subject;
    private final String media;
    private final String party;

    private final double impact;
    private double totalImpact;
    private double realImpact;

    private final int time;

    public News(final Event event, final String media, final int time) {
        this(event.getSubject(), media, event.getParty(), event.getImpact(), time, event);
    }

    public News(final String subject, final String media, final String party, final double impact, final Integer time,
                final Event event) {
        this.subject = subject;
        this.media = media;
        this.party = party;
        this.impact = impact;
        this.time = time;
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public String getSubject() {
        return subject;
    }

    public String getMedia() {
        return media;
    }

    public String getParty() { return party; }

    public double getImpact() {
        return impact;
    }

    public double getTotalImpact() {
        return totalImpact;
    }

    public double getRealImpact() {
        return realImpact;
    }

    public double getImpactDifference() { return totalImpact - realImpact; }

    public int getTime() {
        return time;
    }

    public void updateRealImpact(final double realImpact) {
        this.realImpact += realImpact;
    }

    public void updateTotalImpact(final double thresholdImpact) {
        this.totalImpact += thresholdImpact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return Double.compare(news.impact, impact) == 0 &&
                Objects.equals(subject, news.subject) &&
                Objects.equals(media, news.media) &&
                Objects.equals(party, news.party) &&
                Objects.equals(time, news.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, media, party, impact, time);
    }
}
