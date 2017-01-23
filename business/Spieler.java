package hearrun.business;


import com.sun.javafx.scene.paint.GradientUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshuabarth on 13.01.17.
 */
public class Spieler {
    private String name;
    private int nr;
    private int time;
    private ArrayList<Point> log;

    public Spieler(int nr, String name){
        this.nr = nr;
        this.name = name;
        log = new ArrayList<Point>();
        log.add(new Point(0,0)); //Für den Start wird die letzte Position auf 0 gesetzt
        log.add(new Point(0,0)); //aktPos


    }

    public void move(int x, int y){
        log.add(new Point(x,y));


    }

    public int getAktX(){
        return log.get(log.size()-1).x;
    }

    public int getAktY() {
        return log.get(log.size()-1).y;

    }

    public int getLastX(){
            return log.get(log.size()-2).x;
    }

    public int getLastY(){
            return log.get(log.size()-2).y;
    }

    public int getNr(){
        return this.nr;
    }

    public boolean stehtAufFeld(int x, int y){
        if (x == getAktX() && y == getAktY()){
            return true;
        }
        return false;
    }

    public void setName(String name){
        this.name = name;
    }

    public void moveBack(){

            log.remove(log.size()-1);


    }

    public int getLogSize(){
        return log.size();
    }
}
