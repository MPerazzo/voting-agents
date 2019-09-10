package ar.edu.itba.processing;

import processing.core.PApplet;

public class Sketch extends PApplet {

    /*public static void main(String[] args){
        PApplet.main("ar.edu.itba.processing.Sketch", args);
    }*/

    public void draw (){


        fill(color(255, 204, 0));
        arc(15,32,20, 20, PI, 2*PI);
        circle(15, 15, 20);

        fill(color(97, 192, 255));
        arc(40,32,20, 20, PI, 2*PI);
        circle(40, 15, 20);

        fill(color(97, 192, 255));
        arc(65,32,20, 20, PI, 2*PI);
        circle(65, 15, 20);



    }

    public void settings(){
        size(600, 600);
    }


}
