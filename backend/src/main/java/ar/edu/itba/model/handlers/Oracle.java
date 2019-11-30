package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Event;
import ar.edu.itba.model.News;
import ar.edu.itba.utils.MediaRandom;
import ar.edu.itba.utils.Random;


import java.util.*;
import java.util.stream.Collectors;

public class Oracle {


    private static Oracle instance = new Oracle();

    private static final Map<Integer, Event> events = new LinkedHashMap<>();

    private double prob;
    private double minPercentage;
    private double maxPercentage;

    private int dayTolerance;
    private double impactTolerance;

    private List<String> parties;
    private List<String> subjects;

    private Random r = MediaRandom.getInstance();

     public void generateEvent(final int time) {
        final double r = this.r.generateDouble();
        if (r > prob)
            return;
        final String subject = getRandomSubject();
        final double impact = generateImpact();
        final String party = getRandomParty();

        final Event e = new Event(subject, party, impact, time);
        events.put(time, e);
    }

    private String getRandomSubject() {
        return subjects.get(r.generateInt(0, subjects.size() - 1));
    }

    private String getRandomParty() {
        return parties.get(r.generateInt(0, parties.size() - 1));
    }

    public Event getToleratedEvent() {
        return getEvent(dayTolerance);
    }

    public Event getEvent(final int tolerance) {
        final int lastEventTime = events.entrySet().stream().collect(Collectors.toList()).get(events.size() - 1).getKey();
        final List<Event> filteredEvents = events.entrySet().stream().filter(e -> e.getKey() >= lastEventTime - tolerance).map(e -> e.getValue()).collect(Collectors.toList());
        return filteredEvents.get(r.generateInt(0, filteredEvents.size() - 1));
    }

    public boolean checkIfTrueNews(final News news){
        final Event newsEvent = news.getEvent();
        return news.getSubject().equals(newsEvent.getSubject()) && news.getParty().equals(newsEvent.getParty()) &&
                validateImpact(newsEvent.getImpact(), news.getImpact()) && validateTime(news);
    }

    private boolean validateImpact(double eventImpact, double newsImpact){
        if (Math.abs(eventImpact - newsImpact) <= impactTolerance)
            return true;
        return false;
    }

    private boolean validateTime(final News news) {
        final int lastEventTime = events.entrySet().stream().collect(Collectors.toList()).get(events.size() - 1).getKey();
        final int newsTime = news.getTime();
        return lastEventTime - newsTime <= dayTolerance;
     }

     public boolean isEmpty() {
         return events.size() == 0;
     }

    public void setProperties(final double prob, final double minPercentage, final double maxPercentage,
                              final int dayTolerance,final double impactTolerance, final List<String> parties, final List<String> subjects) {
        this.prob = prob;
        this.parties = parties;
        this.subjects = subjects;
        this.minPercentage = minPercentage;
        this.maxPercentage = maxPercentage;
        this.dayTolerance = dayTolerance;
        this.impactTolerance = impactTolerance;
    }

    protected double generateImpact(){
        return r.generateDouble(minPercentage, maxPercentage);
    }

    public static Oracle getInstance() {
        return instance;
    }
}
