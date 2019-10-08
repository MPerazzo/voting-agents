package ar.edu.itba.model.handlers;

import ar.edu.itba.model.News;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Omniscient {

    private Map<LocalDate,List<News>> trueNews;

    public Omniscient(Map<LocalDate, List<News>> news) {
        trueNews = news;
    }


    public void addNews(News news){
        if (trueNews.containsKey(news.getDate())){
            trueNews.get(news.getDate()).add(news);
        }else{
            List<News> newsList = new ArrayList<>();
            newsList.add(news);
            trueNews.put(LocalDate.now(),newsList);
        }
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
