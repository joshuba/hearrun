package hearrun.business;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshuabarth on 13.01.17.
 */
public class Spieler {
    private String name;
    private int time;
    private ArrayList<Point> log;
    private ObservableMap<String, String> achievements;
    private int gelaufen;

    public Spieler(String name){
        this.name = name;
        log = new ArrayList<>();
        log.add(new Point(0,0)); //Für den Start wird die letzte Position auf 0 gesetzt
        log.add(new Point(0,0)); //aktPos
        achievements = FXCollections.observableHashMap();
        gelaufen = 0;

        achievements.put("gelaufen", "Gelaufene Felder: " + gelaufen);
        achievements.put("fragen", "Richtige Fragen: " + gelaufen);

    }

    public void move(int x, int y){
        log.add(new Point(x,y));
        gelaufen++;
        achievements.put("gelaufen", "Gelaufene Felder: " + gelaufen);
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
            gelaufen++;
            achievements.put("gelaufen", "Gelaufene Felder: " + gelaufen);

    }

    public int getLogSize(){
        return log.size();
    }

    public String getName(){
        return this.name;
    }

    public String toString() {
        return getName();
    }

    public void setAchievement(String achievement, String value) {
        achievements.put(achievement, value);
    }

    public ObservableMap<String, String> getAchievements() {
        return achievements;
    }
}
