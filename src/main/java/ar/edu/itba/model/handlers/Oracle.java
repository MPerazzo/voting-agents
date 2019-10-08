package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Event;
import ar.edu.itba.model.News;
import ar.edu.itba.model.config.profile.MediaParty;
import ar.edu.itba.utils.Random;


import java.util.*;

public class Oracle extends Creator{


    private static Oracle instance = null;

    private static final Map<Integer, List<Event>> events = new HashMap<>();
    private static final List<Integer> dates = new ArrayList<>();

    private final int dayTolerance; //tolerancia de dias, es decir ultimas N noticias
    private final double impactTolerance; //tolerancia

    private Oracle(final double prob, final int dayTolerance,final double impactTolerance, final double minPercentage, final double maxPercentage, final List<MediaParty> parties, final List<String> subjects) {
        super(parties,subjects,minPercentage,maxPercentage,prob);
        this.dayTolerance= dayTolerance;
        this.impactTolerance= impactTolerance;
    }


    public static Oracle getInstance(final double prob, final int dayTolerance,final double impactTolerance, final double minPercentage, final double maxPercentage, final List<MediaParty> parties, final List<String> subjects) {
        if (instance == null){
            instance = new Oracle(prob, dayTolerance, impactTolerance, minPercentage, maxPercentage, parties, subjects);
        }
        return instance;
    }

     public void generateEvent(int time) throws Exception {
        final double r = Random.generateDouble();
        if (r > prob)
            return;
        final String subject = getRandomSubject();
        final double impact = generateImpact();
        final String party = generateParty();
        final Integer date = time;

        final Event e = new Event(subject, party, impact, date);

        if(events.containsKey(time)){
            events.get(time).add(e);
        }else{
            List<Event> dayList = new ArrayList<>();
            dayList.add(e);
            dates.add(time);
            events.put(time,dayList);
        }

    }

    public boolean checkIfTrueNews(News news){

        List<Integer> lastN = dates.subList(Math.max(dates.size() - dayTolerance, 0), dates.size());
        for(Integer date : lastN){
            for (Event e : events.get(dates.get(date))){
                if(compareImpact(e.getImpact(),news.getImpact()) &&
                        e.getParty().equals(news.getParty()) &&
                        e.getSubject().equals(news.getSubject())){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean compareImpact(double eventImpact, double newsImpact){
        if (Math.abs(eventImpact-newsImpact)<=impactTolerance) return true;
        return false;
    }

    public static Map<Integer, List<Event>> getEvents() {
        return events;
    }
}
