package hearrun.business;

import hearrun.view.controller.ViewController;
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
    private ViewController viewController;

    public Spiel(String mapName, int spieleranzahl, ViewController viewController){
        this.viewController = viewController;
        this.spieleranzahl = spieleranzahl;
        this.spielerListe = new ArrayList<Spieler>();
        leseMapVonDateiEin(mapName);
        erstelleSpieler("testspieler");


    }

    public void leseMapVonDateiEin(String mapName){
        this.aktMap = new Map(mapName, viewController);

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
        return this.spieleranzahl;
    }

    public void erstelleSpieler(String name){
        for (int i=0; i<spieleranzahl; i++){
            Spieler spieler = new Spieler(i, name);
            spielerListe.add(spieler);

        }
        aktSpieler = 0;
    }

    public void nextSpieler(){
        aktSpieler = (aktSpieler +1) %spieleranzahl;
    }

    public void setPlayerNames(int nr, String name){
        spielerListe.get(nr).setName(name);
    }
}
