package hearrun.business;

import hearrun.view.layout.Map;

import java.util.ArrayList;

/**
 * Created by joshuabarth on 12.01.17.
 */
public class Spiel {
    private Map aktMap;
    private ArrayList<Spieler> spielerListe;
    private int aktSpieler;

    public Spiel(Map map, ArrayList<Spieler> spielerListe){
        this.spielerListe = new ArrayList<>();
        this.aktMap = map;
        this.spielerListe = spielerListe;

        for (Spieler s : spielerListe)
            System.out.println(s);


    }

    public Map getAktMap(){
        return aktMap;
    }

    public Spieler getAktSpieler(){
        return spielerListe.get(aktSpieler);
    }

    public Spieler getSpielerByNr(int nr){
        return spielerListe.get(nr);

    }

    public int getSpieleranzahl(){
        return spielerListe.size();
    }



    public void nextSpieler(){
        aktSpieler = (aktSpieler +1) %spielerListe.size();
    }

    public void setPlayerNames(int nr, String name){
        spielerListe.get(nr).setName(name);
    }


}
