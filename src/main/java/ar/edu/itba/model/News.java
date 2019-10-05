package ar.edu.itba.model;

public class News {
    private final String subject;
    private final String media;
    private final String party;

    private final double impact;
    private double totalImpact;
    private double realImpact;

    public News(final String subject, final String media, final String party, final double impact) {
        this.subject = subject;
        this.media = media;
        this.party = party;
        this.impact = impact;
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

    public void updateRealImpact(final double realImpact) {
        this.realImpact += realImpact;
    }

    public void updateTotalImpact(final double thresholdImpact) {
        this.totalImpact += thresholdImpact;
    }

}
