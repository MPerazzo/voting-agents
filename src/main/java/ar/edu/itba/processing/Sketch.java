package ar.edu.itba.processing;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.handlers.Profiler;
import ar.edu.itba.utils.Random;
import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Sketch extends PApplet {

    /*public static void main(String[] args){
        PApplet.main("ar.edu.itba.processing.Sketch", args);
    }*/

    public void draw(){


        fill(color(255, 204, 0));
       /* arc(15,32,20, 20, PI, 2*PI);
        circle(15, 15, 20);


        arc(40,32,20, 20, PI, 2*PI);
        circle(40, 15, 20);

        fill(color(97, 192, 255));
        arc(65,32,20, 20, PI, 2*PI);
        circle(65, 15, 20);*/
        List<Person> personList = Profiler.getPersons();
        Map<String, Long> parties = personList.stream().collect(Collectors.groupingBy(Person::getPoliticalOrientation, Collectors.counting()));
        int j=0;
        List<Color> colors = getColors(parties);
        int k = 0;
        for (final Map.Entry<String, Long> e : parties.entrySet()) {
            Color color = colors.get(k);
            fill(color(color.getR(), color.getG(), color.getB()));
            for (int i = 0, l=0; l < e.getValue(); i = i+25, l = l+5) {

                arc(15+i,32+j,20, 20, PI, 2*PI);
                ellipse(15+i, 15+j, 20,20);

            }
            j+=30;
            k++;
        }

        noLoop();

    }

    public List<Color> getColors(final Map<String, Long> parties) {
        List<Color> colors = new ArrayList<>();
        for (String party : parties.keySet()) {
            colors.add(new Color(Random.generateInt(0, 255), Random.generateInt(0, 255), Random.generateInt(0, 255)));
        }
        return colors;
    }

    public void settings(){
        size(600, 600);
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
