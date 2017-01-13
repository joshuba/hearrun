package hearrun.business;

/**
 * Created by joshuabarth on 13.01.17.
 */
public class Spieler {
    private String name;
    private int time;
    private int aktx;
    private int akty;
    private int lastx;
    private int lasty;

    public Spieler(String name){
        this.name = name;
        aktx = 0;
        akty = 0;
        lastx = aktx;
        lasty = akty;

    }

    public void move(int x, int y){
       lastx = aktx;
       lasty = akty;

        aktx = x;
        akty = y;

    }

    public int getAktX(){
        return this.aktx;
    }

    public int getAktY() {
        return akty;
    }

    public int getLastX(){
        return this.lastx;
    }

    public int getLastY(){
        return this.lasty;

    }
}
