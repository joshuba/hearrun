package hearrun.view.controller;

import hearrun.business.Spiel;
import hearrun.business.Spieler;
import hearrun.view.layout.*;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josh on 09.01.17.
 */
public class ViewController {
    private Stage stage;
    private CenterLayout centerLayout;
    private SideBar leftLayout;
    private SideBar rightLayout;
    private Spiel spiel;

    public ViewController(Stage stage){
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
        centerLayout.baueSpielfeldAuf(spiel.getAktMap());
    }



    public void setSpiel(Spiel spiel){
        this.spiel = spiel;
    }

    public void setFeldId(int x, int y, String id){
        spiel.getAktMap().getFeld(x,y).setBesetzt(id);
    }

    public void nextPossibleField(Spieler spieler){
        if(spieler == null){
            spieler = spiel.getAktSpieler();
        }
        int x = spieler.getAktX();
        int y = spieler.getAktY();
        Map map = this.spiel.getAktMap();

        //nach rechts
        if(map.getFeld(x+1,y).getFeldtyp() != Feldtyp.LeeresFeld && x+1 != spieler.getLastX() && x+1<=map.getFeldBreite()){
            System.out.println("rechts gefunden");
            spiel.getAktMap().getFeld(x,y).setLeer(); //Setze aktuelles Feld zurueck
            spiel.getAktMap().getFeld(x+1,y).setBesetzt("lol"); //Setze neues Feld auf besetzt
            spieler.move(x+1,y);

        }
        //nach links
        else if(map.getFeld(x-1,y).getFeldtyp() != Feldtyp.LeeresFeld && x-1 != spieler.getLastX() && x-1 >= 0){
            System.out.println("links gefunden");
            spiel.getAktMap().getFeld(x,y).setLeer();
            spiel.getAktMap().getFeld(x-1,y).setBesetzt("lol");
            spieler.move(x-1,y);



        }
        //nach Oben
        else if(map.getFeld(x,y-1).getFeldtyp() != Feldtyp.LeeresFeld && y-1 != spieler.getLastY() && y-1 >= 0){
            System.out.println("oben gefunden");
            spiel.getAktMap().getFeld(x,y).setLeer();
            spiel.getAktMap().getFeld(x,y-1).setBesetzt("lol");
            spieler.move(x,y-1);



        }
        //nach unten
        else if(map.getFeld(x,y+1).getFeldtyp() != Feldtyp.LeeresFeld && y+1 != spieler.getLastY() && y+1 <= map.getFeldHoehe()){
            System.out.println("unten gefunden");
            spiel.getAktMap().getFeld(x,y).setLeer();
            spiel.getAktMap().getFeld(x,y+1).setBesetzt("lol");
            spieler.move(x,y+1);




        }



    }







}
