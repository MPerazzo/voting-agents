package ar.edu.itba.model.handlers;

import ar.edu.itba.model.Event;
import ar.edu.itba.model.News;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Oracle {
    private static final Oracle instance = new Oracle();

    private final Map<Integer, List<Event>> events = new HashMap<>();
    private double prob;
    private int tolerance;

    private Oracle() {
    }

    public static Oracle getInstance() {
        return instance;
    }

    public void setParams(final double prob, final int tolerance) {
        this.prob = prob;
        this.tolerance = tolerance;
    }

    public void generateEvent() {

    }

    public boolean checkIfTrueNews(News news){
        if(trueNews.containsKey(news.getDate())){
            if (trueNews.get(news.getDate()).contains(news)){
                return true;
            }
        }
        return false;
    }
}
