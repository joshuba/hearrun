package hearrun.business;

/**
 * Created by joshuabarth on 13.01.17.
 */
public class Spieler {
    private String name;
    private int time;
    private int aktx;
    private int akty;

    public Spieler(String name){
        this.name = name;
        aktx = 0;
        akty = 0;

    }

    public void move(int x, int y){
        aktx = x;
        akty = y;
    }

    public int getAktX(){
        return this.aktx;
    }

    public int getAktY() {
        return akty;
    }
}
