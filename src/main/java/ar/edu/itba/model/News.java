package ar.edu.itba.model;

import java.time.LocalDate;
import java.util.Objects;


public class News {
    private final String subject;
    private final String media;
    private final String party;

    private final double impact;
    private double totalImpact;
    private double realImpact;

    private final LocalDate date;

    public News(final String subject, final String media, final String party, final double impact, final LocalDate date) {
        this.subject = subject;
        this.media = media;
        this.party = party;
        this.impact = impact;
        this.date = date;
    }
    public LocalDate getDate(){ return date; }

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
                Objects.equals(date, news.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, media, party, impact, date);
    }
}
