package hearrun.business;

/**
 * Created by joshuabarth on 13.01.17.
 */
public class Spieler {
    private String name;
    private int nr;
    private int time;
    private int aktx;
    private int akty;
    private int lastx;
    private int lasty;

    public Spieler(int nr, String name){
        this.nr = nr;
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

    public int getNr(){
        return this.nr;
    }

    public boolean stehtAufFeld(int x, int y){
        if (x == this.getAktX() && y == this.getAktY()){
            return true;
        }
        return false;
    }

    public void setName(String name){
        this.name = name;
    }
}
