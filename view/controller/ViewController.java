package hearrun.view.controller;

import hearrun.business.Fragetyp;
import hearrun.business.SpielController;
import hearrun.business.Spieler;
import hearrun.business.fragen.Frage;
import hearrun.view.IntroScreen;
import hearrun.view.layout.*;
import hearrun.view.layout.FrageIntro;
import hearrun.view.layout.FrageFenster.TextFrage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by Josh on 09.01.17
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

    public void movePlayer(int i, Spieler s){
        int anz;

        //Falls man mehr Felder zurueck muss als da sind, landet man auf dem ersten
        if(i>0){
            anz = i;
        }else{
            anz = i*(-1);
            if(s.getLogSize()-2 < anz){
                anz = s.getLogSize()-2;
            }
        }


        Timeline forward = new Timeline(new KeyFrame(
                Duration.millis(400),
                a -> {
                    nextPossibleField(s);
                    spielController.getEffectPlayer().play("src/hearrun/resources/sounds/move.mp3");
                }));
        forward.setCycleCount(anz);

        Timeline back = new Timeline(new KeyFrame(
                Duration.millis(400),
                a -> {
                    lastPossibleField(s);
                    spielController.getEffectPlayer().play("src/hearrun/resources/sounds/move.mp3"); //Spiele sound nur so oft wie feld gewechselt wird

                }));
        back.setCycleCount(anz);



        if(i>0){
            System.out.println("FORWARD");
            forward.play();
        }else{
            if(anz != 0){ //Falls mindestens ein Feld zurueck gegangen werden kann
                back.play();
            }else{
                spielController.getEffectPlayer().play("src/hearrun/resources/sounds/moveFailure.mp3"); //Falls kein Feld mehr da ist
                feldBlinkenLassen(0,0);

            }
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

    private void lastPossibleField(Spieler spieler){
        if (spieler == null) {
            spieler = spielController.getAktSpiel().getAktSpieler();
        }
        int x = spieler.getAktX();
        int y = spieler.getAktY();
        Map map = this.spielController.getAktSpiel().getAktMap();

        if(!(x == 0 && y == 0)){
            spieler.moveBack(); //Gehe zurück
            map.getFeld(x, y).setBesetztID(erkenneFeldId(x, y)); //Setze aktuelles Feld zurueck
            spielController.getAktSpiel().getAktMap().getFeld(spieler.getAktX(), spieler.getAktY()).setBesetztID(erkenneFeldId(spieler.getAktX(), spieler.getAktY())); //Setze neues Feld auf b

        }





    }

    public String erkenneFeldId(int x, int y) {
        int anz = spielController.getAktSpiel().getSpieleranzahl();

        boolean leer = true;
        String idZahl = "p";

        //Wenn der spieler auf dem Feld steht merke zahl z.B. 13: spieler 1 und 3 auf feld
        for (int i = 0; i <anz; i++) {
            if(spielController.getAktSpiel().getSpielerByNr(i).stehtAufFeld(x,y)){
                idZahl = idZahl + (i+1);
                leer = false;
            }
        }
        //Falls leer
        if (leer){
            idZahl = "leer";
        }

        String feldtyp = spielController.getAktSpiel().getAktMap().getFeld(x, y).getFeldtyp().toString() + idZahl;
        feldtyp = feldtyp.toLowerCase();
        System.out.println(feldtyp);
        return feldtyp;

    }

    public Stage getStage(){
        return this.stage;
    }

    public void setGameLayout(){

        spielController.getLayout().setGameLayout();
    }

    public void setMainMenu(){
        //Wenn das spiel laeuft schalte continue ein
        if(spielController.getAktSpiel() != null){
            spielController.getLayout().setMainMenu();
            spielController.getLayout().getMainMenu().activateContinue();
            gameLayoutBlury(true);
        }else{
            spielController.getLayout().setMainMenu();
        }
    }

    public void resetGameLayout(){
        spielController.getLayout().resetGameLayout();
    }

    public void zeigeFrage(Frage frage, Fragetyp fragetyp){

        if(fragetyp == Fragetyp.CoverTitelFrage){

        }else if (fragetyp == Fragetyp.CoverWahlFrage){

        }else{
            TextFrage t = new TextFrage(frage, spielController);
            gameLayoutBlury(true);
            spielController.getLayout().zeigeTextFrage(t);

            t.starteAntworPhase();

        }
    }


    public void gameLayoutBlury(boolean anAus){
        spielController.getLayout().bluryAnAus(anAus);
    }

    public void feldBlinkenLassen(int x, int y){
        KeyFrame k1 = new KeyFrame(Duration.ZERO, a ->{
            spielController.getAktSpiel().getAktMap().getFeld(x, y).setLeer(); //Setze aktuelles Feld zurueck
        });

        KeyFrame k2 = new KeyFrame(Duration.millis(200), a ->{
            spielController.getAktSpiel().getAktMap().getFeld(x, y).setBesetztID(erkenneFeldId(x, y)); //Setze aktuelles Feld zurueck
        });

        Timeline blink = new Timeline();
        blink.setOnFinished(a -> spielController.getAktSpiel().getAktMap().getFeld(x, y).setBesetztID(erkenneFeldId(x, y)));
        blink.setAutoReverse(true);
        blink.setCycleCount(6);
        blink.getKeyFrames().addAll(k1,k2);
        blink.play();

    }

    public void newGameButtonAnAus(boolean anAus){
        spielController.getLayout().newGameAnAus(anAus);
    }

    public void zeigeIntroScreen(){
        IntroScreen is = new IntroScreen(stage, spielController);
        spielController.getLayout().setIntroscreen(is);
    }

    public Stage getWindowStage(){
        return this.stage;
    }

    public void zeigeLadeScreen(SimpleBooleanProperty bool, SimpleFloatProperty prog, SimpleIntegerProperty anzFragen){
        LoadingScreen ls = new LoadingScreen(bool, prog, anzFragen, spielController);
        spielController.getLayout().setLoadingScreen(ls);
    }

    public void zeigeIntroUndFrage(Frage frage, Fragetyp fragetyp){
        FrageIntro fi = new FrageIntro(spielController);

        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            spielController.getLayout().setFrageIntro(fi);
            System.out.println("INTRO DA");
        });

        KeyFrame k2 = new KeyFrame(Duration.millis(5000), a ->{
            spielController.getLayout().removeFrageIntro(fi);
            zeigeFrage(frage, fragetyp);
            System.out.println("INTRO WEG");

        });

        Timeline fadeout = new Timeline();
        fadeout.setAutoReverse(false);
        fadeout.setCycleCount(1);
        fadeout.getKeyFrames().addAll(k1, k2);
        fadeout.play();

    }








}
