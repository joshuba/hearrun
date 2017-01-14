package hearrun.view.controller;

import hearrun.business.SpielController;
import hearrun.business.Spieler;
import hearrun.view.layout.*;
import javafx.stage.Stage;

/**
 * Created by Josh on 09.01.17.
 */
public class ViewController {
    private Stage stage;
    private CenterLayout centerLayout;
    private SideBar leftLayout;
    private SideBar rightLayout;
    private SpielController spielController;

    public ViewController(Stage stage, SpielController spielController){
        this.spielController = spielController;
        this.stage = stage;
    }


    public void setCenterLayout(CenterLayout centerLayout) {
        this.centerLayout = centerLayout;
    }

    public void setLeftLayout(SideBar leftLayout) {
        this.leftLayout = leftLayout;
    }

    public void setRightLayout(SideBar rightLayout) {
        this.rightLayout = rightLayout;
    }

    public void baueSpielfeldAuf(){
        //Felder werden in das Gridpane gesetzt
        centerLayout.baueSpielfeldAuf(spielController.getAktSpiel().getAktMap());
    }

    public void setFeldId(int x, int y, String id){
        spielController.getAktSpiel().getAktMap().getFeld(x,y).setBesetztID(id);
    }

    public void moveForward(int i, Spieler s){
        int c = 1;
        while(c <= i){
            nextPossibleField(s);
            c++;
        }
    }

    private void nextPossibleField(Spieler spieler) {
        int counter = 0;
        if (spieler == null) {
            spieler = spielController.getAktSpiel().getAktSpieler();
        }
        int x = spieler.getAktX();
        int y = spieler.getAktY();
        Map map = this.spielController.getAktSpiel().getAktMap();


            //nach rechts
            if (x + 1 < map.getFeldBreite() && map.getFeld(x + 1, y).getFeldtyp() != Feldtyp.LeeresFeld && x + 1 != spieler.getLastX()) {
                spieler.move(x + 1, y); //bewegeSPieler
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY())); //Setze aktuelles Feld zurueck
                spielController.getAktSpiel().getAktMap().getFeld(x + 1, y).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY())); //Setze neues Feld auf besetzt

            }
            //nach links
            else if (x - 1 >= 0 && map.getFeld(x - 1, y).getFeldtyp() != Feldtyp.LeeresFeld && x - 1 != spieler.getLastX()) {
                spieler.move(x - 1, y);
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY()));
                map.getFeld(x - 1, y).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY()));
            }
            //nach Oben
            else if (y - 1 >= 0 && map.getFeld(x, y - 1).getFeldtyp() != Feldtyp.LeeresFeld && y - 1 != spieler.getLastY()) {
                spieler.move(x, y - 1);
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY()));
                map.getFeld(x, y - 1).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY()));
            }
            //nach unten
            else if (y + 1 <= map.getFeldHoehe() && map.getFeld(x, y + 1).getFeldtyp() != Feldtyp.LeeresFeld && y + 1 != spieler.getLastY()) {
                spieler.move(x, y + 1);
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY()));
                map.getFeld(x, y + 1).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY()));
            }
            erkenneFeldId(x,y);


    }

    public String erkenneFeldId(int x, int y) {
        int anz = spielController.getAktSpiel().getSpieleranzahl();
        boolean leer = true;
        String idZahl = "p";

        //Wenn der spieler auf dem Feld steht merke zahl z.B. 13: spieler 1 und 3 auf feld
        for (int i = 0; i <anz; i++) {
            if(spielController.getAktSpiel().getSpielerByNr(i).stehtAufFeld(x,y)){
                idZahl = idZahl + (spielController.getAktSpiel().getSpielerByNr(i).getNr()+1);
                leer = false;
            }
        }
        //Falls leer
        if (leer){
            idZahl = "leer";
        }

        String feldtyp = spielController.getAktSpiel().getAktMap().getFeld(x, y).getFeldtyp().toString() + idZahl;
        feldtyp = feldtyp.toLowerCase();
        return feldtyp;

    }

    public Stage getStage(){
        return this.stage;
    }

    public void setGameLayout(){
        spielController.getLayout().setGameLayout();
    }

    public void setMainMenu(){
        spielController.getLayout().setMainMenu();
    }











}
