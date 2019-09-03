package ar.edu.itba.Model;

import ar.edu.itba.Model.Enums.MediaId;

import java.util.List;

public class Media {

    private MediaId id;
    private List<News> news;

    
    public MediaId getId() {
        return id;
    }

    public void setId(MediaId id) {
        this.id = id;
    }

    public List<News> getNews() {
        return news;
    }

    public void setNews(List<News> news) {
        this.news = news;
    }
}
