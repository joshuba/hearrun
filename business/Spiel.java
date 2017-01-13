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
    private int spieleranzahl;

    public Spiel(String mapName, int spieleranzahl){
        this.spieleranzahl = spieleranzahl;
        this.spielerListe = new ArrayList<Spieler>();
        leseMapVonDateiEin(mapName);
        erstelleSpieler();


    }

    public void leseMapVonDateiEin(String mapName){
        this.aktMap = new Map(mapName);

    }

    public Map getAktMap(){
        return aktMap;
    }

    public Spieler getAktSpieler(){
        return spielerListe.get(aktSpieler);
    }

    private void erstelleSpieler(){
        Spieler spieler1 = new Spieler("Hans");
        spielerListe.add(spieler1);
        aktSpieler = 0;
    }
}
