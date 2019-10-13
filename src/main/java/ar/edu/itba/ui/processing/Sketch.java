package ar.edu.itba.ui.processing;

import ar.edu.itba.model.Election;
import ar.edu.itba.model.Person;
import ar.edu.itba.model.handlers.Media;
import ar.edu.itba.model.handlers.NewsPaper;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.utils.Random;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sketch extends PApplet {

    List<String> partiesList ;
    Map<String, Color> colorMap;

    PFont f;

    /*public static void main(String[] args){
        PApplet.main("ar.edu.itba.ui.processing.Sketch", args);
    }*/



    public void draw(){

        //DRAW PERSONS
        List<Person> personList = Profiler.getInstance().getPersons();
        Map<String, Long> parties = personList.stream().collect(Collectors.groupingBy(Person::getPoliticalParty, Collectors.counting()));
        int  j=0;

        for (final Map.Entry<String, Long> e : parties.entrySet()) {
            int i=0;

            int redColor = colorMap.get(e.getKey()).getR();
            int greenColor = colorMap.get(e.getKey()).getG();
            int blueColor = colorMap.get(e.getKey()).getB();

            int l = Math.toIntExact(e.getValue())*10/100;

            for (int k=0; k<l ; k++) {

                fill(redColor,greenColor,blueColor);
                arc(15+i,32+j,20, 20, PI, 2*PI);
                ellipse(15+i, 15+j, 20,20);
                i+=25; //move on x
            }
            j+=30; //move on y
        }
        //DRAW PERSONS

        //DRAW MEDIA
        List<NewsPaper> newsPapersList = Media.getInstance().getSources();
        Map<String,Integer> typeOfNews = new HashMap<>();
        for(NewsPaper np : newsPapersList){
            for (int i=0 ; i<np.getNews().size() ; i++  ){
                increment(typeOfNews,np.getNews().get(i).getSubject());
            }
        }
        int  y1=50, y2=25;
        for(final Map.Entry<String, Integer> e : typeOfNews.entrySet()){
            int x=5, i=0;

            for (int k=0; k<e.getValue() ; k++) {
                //line
                stroke(1);
                strokeWeight(2);
                line(x+i, y1+j, x+i, y2+j);

                //text
                fill(1);
                textSize (22);
                textAlign(RIGHT);
                text(e.getKey(),width - 100,j+y1-5);

                i+=4;
            }
            j+=30;
        }
        //DRAW MEDIA

        noLoop();

    }

    public Map<String, Color> assignColor(List<String> partiesList){

        Map<String, Color> colorMap = new HashMap<>();


        for (int k=0; k<partiesList.size(); k++) {

            int redColor = Random.generateInt(0, 255);
            int greenColor = Random.generateInt(0, 255);
            int blueColor = Random.generateInt(0, 255);

            Color color = new Color(redColor,greenColor,blueColor);
            colorMap.put(partiesList.get(k), color);
        }

        return colorMap;
    }


    public void settings(){
        size(600, 600);
    }

    public void setup(){
        partiesList = Election.getInstance().getPartiesList();
        colorMap = assignColor(partiesList);
        f = createFont("Arial",16,true);
    }

    public static<K> void increment(Map<K,Integer> map, K key) {
        if (map.computeIfPresent(key, (k, v) -> v + 1) == null)
            map.put(key, 1);
    }

    public class Color {
        private int r;
        private int g;
        private int b;

        public Color(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }

        public int getR() {
            return r;
        }

        public int getG() {
            return g;
        }

        public int getB() {
            return b;
        }
    }



}


