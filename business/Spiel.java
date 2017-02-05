package hearrun.business;

import hearrun.view.layout.Feld;
import hearrun.view.layout.Map;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;

/**
 * Das Modell eines Spiels. Es verwaltet die Maps und Spieler.
 */
public class Spiel {
    private Map aktMap;
    private ArrayList<Spieler> spielerListe;
    private int aktSpieler;
    private SimpleBooleanProperty sieg;

    public Spiel(Map map, ArrayList<Spieler> spielerListe){
        this.spielerListe = new ArrayList<>();
        this.aktMap = map;
        this.spielerListe = spielerListe;
        sieg = new SimpleBooleanProperty();
        sieg.set(false);


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

    public Feld getAktFeld(){
        return aktMap.getFeld(spielerListe.get(aktSpieler).getAktX(), spielerListe.get(aktSpieler).getAktY());
    }

    public void setSieg(){
        this.sieg.setValue(true);
    }

    public SimpleBooleanProperty getSiegStatus(){
        return this.sieg;
    }


}
