package hearrun.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Reräsentiert einen Spieler.
 * Implementiert Methoden zum Laufen sowie zum aktualisieren der Achievements.
 * (gelaufene Felder, eingesetzte Leben, falsche Fragen, richtige Fragen, falsche Fragen,
 * Fragen, bei denen die Zeit abgelaufen ist).
 */
public class Spieler {
    private String name;
    private ArrayList<Point> log;
    private ObservableMap<String, Integer> achievements;
    private int gelaufen;
    private int nr;

    public Spieler(String name) {
        this.name = name;
        log = new ArrayList<>();
        log.add(new Point(0, 0)); //Für den Start wird die letzte Position auf 0 gesetzt
        log.add(new Point(0, 0)); //aktPos
        achievements = FXCollections.observableHashMap();
        gelaufen = 0;

        achievements.put("felder", 0);
        achievements.put("fragenRichtig", 0);
        achievements.put("leben", 0);
        achievements.put("fragenFalsch" , 0);
        achievements.put("zeitAbgelaufen" , 0);
        achievements.put("usedHearts" , 0);
    }


    public void move(int x, int y) {
        log.add(new Point(x, y));
        gelaufen++;
        achievements.put("felder", gelaufen);


    }

    public void moveBack() {
        log.remove(log.size() - 1);
        gelaufen++;
        achievements.put("felder", gelaufen);
    }

    public int getAktX() {
        return log.get(log.size() - 1).x;
    }

    public int getAktY() {
        return log.get(log.size() - 1).y;

    }

    public int getLastX() {
        return log.get(log.size() - 2).x;
    }

    public int getLastY() {
        return log.get(log.size() - 2).y;
    }

    public boolean stehtAufFeld(int x, int y) {
        if (x == getAktX() && y == getAktY()) {
            return true;
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogSize() {
        return log.size();
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return getName();
    }

    public void addLeben() {
        int leben = achievements.get("leben");
        achievements.put("leben", leben + 1);
    }

    public void removeLeben() {
        int leben = achievements.get("leben");
        achievements.put("leben", leben - 1);
    }

    public int getLeben() {
        return achievements.get("leben");
    }

    public void addRichtigeFrage() {
        int richtigeFragen = achievements.get("fragenRichtig");
        achievements.put("fragenRichtig", richtigeFragen + 1);
    }

    public ObservableMap<String, Integer> getAchievements() {
        return achievements;
    }

    public int getNr(){
        return this.nr;
    }

    public void setNr(int nr){
        this.nr = nr;
    }

    public void addFalscheFrage() {
        int falschefragen = achievements.get("fragenFalsch");
        achievements.put("fragenFalsch", falschefragen + 1);
    }

    public void addZeitAbgelaufen() {
        int zeitAbgelaufen = achievements.get("zeitAbgelaufen");
        achievements.put("zeitAbgelaufen", zeitAbgelaufen + 1);
    }

    public void addUsedHeart() {
        int hearts = achievements.get("usedHearts");
        achievements.put("usedHearts", hearts + 1);
    }
}
