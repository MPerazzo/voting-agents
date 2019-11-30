package ar.edu.itba.model;

public class Event {
    private final int date;
    private final String subject;
    private final String party;
    private final double impact;

    public Event(String subject, String party, double impact, int date) {
        this.date = date;
        this.subject = subject;
        this.party = party;
        this.impact = impact;
    }

    public int getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public String getParty() {
        return party;
    }

    public double getImpact() {
        return impact;
    }


}
