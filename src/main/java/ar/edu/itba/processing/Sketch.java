package ar.edu.itba.processing;

import ar.edu.itba.model.Person;
import ar.edu.itba.model.handlers.Profiler;
import processing.core.PApplet;

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

        for (final Map.Entry<String, Long> e : parties.entrySet()) {
            for (int i = 0; i < e.getValue()/10; i = i+25) {
                arc(15+i,32,20, 20, PI, 2*PI);
                circle(15+i, 15, 20);
            }
        }


    }

    public void settings(){
        size(600, 600);
    }


}
