package hearrun.view.controller;

import hearrun.business.Fragetyp;
import hearrun.Main;
import hearrun.business.Spieler;
import hearrun.business.ereignisse.Ereignis;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.FrageFenster.EreignisFenster;
import hearrun.view.layout.IntroScreen;
import hearrun.view.layout.*;
import hearrun.view.layout.FrageFenster.CoverFrage;
import hearrun.view.layout.FrageIntro;
import hearrun.view.layout.FrageFenster.ButtonFrage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

/**Controller ueber die meisten Ablaeufe, die etwas an der GUI aendern
 *
 * @author Leo Back & Joshua Barth
 */
public class ViewController {
    private Stage stage;
    private CenterLayout centerLayout;
    private SpielController spielController;
    private LoadingScreen ls;
    private SimpleBooleanProperty feldAuswahlMakierung;
    private SimpleBooleanProperty spielerLaeuft;


    public ViewController(Stage stage, SpielController spielController) {
        this.spielController = spielController;
        this.stage = stage;
        spielerLaeuft = new SimpleBooleanProperty();
        spielerLaeuft.setValue(false);
        feldAuswahlMakierung = new SimpleBooleanProperty(false);

        feldAuswahlMakierung.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("NEU " + newValue.booleanValue());
            }
        });


        feldAuswahlMakierung.addListener((observable, oldValue, newValue) -> {

            if (newValue && !spielController.getAktSpiel().getSiegStatus().getValue()) {
                //spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(),spielController.getAktSpiel().getAktSpieler().getAktY()).aktuellesFeldMakierung(true);
                spielController.getAktSpiel().getAktFeld().aktuellesFeldMakierung(true);

            }

            if (!newValue) {
                spielController.getAktSpiel().getAktFeld().aktuellesFeldMakierung(false);
                //spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(),spielController.getAktSpiel().getAktSpieler().getAktY()).aktuellesFeldMakierung(false);

            }


            //feldBlinkenLassen(spielController.getAktSpiel().getAktFeld().getX(), spielController.getAktSpiel().getAktFeld().getY());
            //System.out.println("aktfeld = " + spielController.getAktSpiel().getAktFeld().getX() + " " + spielController.getAktSpiel().getAktFeld().getY());

        });


    }


    public void setCenterLayout(CenterLayout centerLayout) {
        this.centerLayout = centerLayout;
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
        spielerLaeuft.setValue(true);

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

                    if(spielController.getAktSpiel().getSiegStatus().getValue() == false){
                        nextPossibleField(s);
                        spielController.getEffectPlayer().play("/hearrun/resources/sounds/move.mp3");
                    }


                }));
        forward.setCycleCount(anz);

        Timeline back = new Timeline(new KeyFrame(
                Duration.millis(400),
                a -> {
                    lastPossibleField(s);
                    spielController.getEffectPlayer().play("/hearrun/resources/sounds/move.mp3"); //Spiele sound nur so oft wie feld gewechselt wird

                }));
        back.setCycleCount(anz);



        if(i>0){
            forward.play();

            forward.setOnFinished(e -> {
                spielerLaeuft.setValue(false);
                spielController.nextSpieler();
                feldAuswahlMakierung.setValue(true);
            });
        }else{
            if(anz != 0){ //Falls mindestens ein Feld zurueck gegangen werden kann
                back.play();

                back.setOnFinished(e -> {
                    spielerLaeuft.setValue(false);

                    spielController.nextSpieler();
                    feldAuswahlMakierung.setValue(true);
                });

            }else{
                spielController.getEffectPlayer().play("/hearrun/resources/sounds/moveFailure.mp3"); //Falls kein Feld mehr da ist
                feldBlinkenLassen(0,0);
                feldAuswahlMakierung.setValue(true);
                spielController.nextSpieler();
                spielerLaeuft.setValue(false);

            }
        }
    }

    private void nextPossibleField(Spieler spieler) {
        if (spieler == null) {
            spieler = spielController.getAktSpiel().getAktSpieler();
        }
        int x = spieler.getAktX();
        int y = spieler.getAktY();
        Map map = this.spielController.getAktSpiel().getAktMap();

        //Falls das neue Feld das Endfeld ist



            //nach rechts
            if (x + 1 < map.getFeldBreite() && map.getFeld(x + 1, y).getFeldtyp() != Feldtyp.LeeresFeld && x + 1 != spieler.getLastX()) {
                spieler.move(x + 1, y); //bewegeSPieler
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY())); //Setze aktuelles Feld zurueck
                spielController.getAktSpiel().getAktMap().getFeld(x + 1, y).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY())); //Setze neues Feld auf besetzt
                map.getFeld(x+1, y).zoomIn();

                if(map.getFeld(x + 1, y).getFeldtyp() == Feldtyp.EndFeld){
                    spielController.getAktSpiel().setSieg();
                }

            }
            //nach links
            else if (x - 1 >= 0 && map.getFeld(x - 1, y).getFeldtyp() != Feldtyp.LeeresFeld && x - 1 != spieler.getLastX()) {
                spieler.move(x - 1, y);
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY()));
                map.getFeld(x - 1, y).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY()));
                map.getFeld(x-1, y).zoomIn();

                if(map.getFeld(x - 1, y).getFeldtyp() == Feldtyp.EndFeld){
                    spielController.getAktSpiel().setSieg();
                }

            }
            //nach Oben
            else if (y - 1 >= 0 && map.getFeld(x, y - 1).getFeldtyp() != Feldtyp.LeeresFeld && y - 1 != spieler.getLastY()) {
                spieler.move(x, y - 1);
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(),spieler.getLastY()));
                map.getFeld(x, y - 1).setBesetztID(erkenneFeldId(spieler.getAktX(),spieler.getAktY()));
                map.getFeld(x, y-1).zoomIn();

                if(map.getFeld(x, y-1).getFeldtyp() == Feldtyp.EndFeld){
                    spielController.getAktSpiel().setSieg();
                }

            }
            //nach unten
            else if (y + 1 <= map.getFeldHoehe() && map.getFeld(x, y + 1).getFeldtyp() != Feldtyp.LeeresFeld && y + 1 != spieler.getLastY()) {
                spieler.move(x, y + 1);
                map.getFeld(x, y).setBesetztID(erkenneFeldId(spieler.getLastX(), spieler.getLastY()));
                map.getFeld(x, y + 1).setBesetztID(erkenneFeldId(spieler.getAktX(), spieler.getAktY()));
                map.getFeld(x, y + 1).zoomIn();

                if (map.getFeld(x, y+1).getFeldtyp() == Feldtyp.EndFeld) {
                    spielController.getAktSpiel().setSieg();

                }


                erkenneFeldId(x, y);


            }


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
            map.getFeld(spieler.getAktX(), spieler.getAktY()).zoomIn();

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

    public void resetGameLayout(){

        spielController.getLayout().resetGameLayout();
    }

    public void zeigeFrage(Frage frage, Fragetyp fragetyp, boolean mitHeart){
        if(fragetyp == Fragetyp.Titelfrage || fragetyp == Fragetyp.InterpretFrage || fragetyp == Fragetyp.CoverTitelFrage || fragetyp == Fragetyp.FaktFrage){
            ButtonFrage bf = new ButtonFrage(frage, spielController, mitHeart);
            gameLayoutBlury(true);
            spielController.getLayout().zeigeFenster(bf);
            bf.starteAntwortPhase();

        }else if (fragetyp == Fragetyp.CoverWahlFrage){
            CoverFrage cf = new CoverFrage(frage, spielController, mitHeart);
            gameLayoutBlury(true);
            spielController.getLayout().zeigeFenster(cf);
            cf.starteAntwortPhase();


        }
    }

    public void zeigeEreignis(Ereignis e) {
        gameLayoutBlury(true);
        EreignisFenster ereignisFenster = new EreignisFenster(e, spielController);

        spielController.getLayout().zeigeFenster(ereignisFenster);
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
        ls = new LoadingScreen(bool, prog, anzFragen, spielController);
        spielController.getLayout().setLoadingScreen(ls);
    }

    public void zeigeIntroUndFrage(Frage frage, Fragetyp fragetyp, boolean mitHeart){
        FrageIntro fi = new FrageIntro(spielController);

        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            spielController.getLayout().setFrageIntro(fi);
            fi.requestFocus();
            System.out.println(fi.isFocused());
            spielController.getEffectPlayer().play("/hearrun/resources/sounds/fragestellung.mp3");

        });

        KeyFrame k2 = new KeyFrame(Duration.millis(2000), a ->{
            spielController.getLayout().removeFrageIntro(fi);
            zeigeFrage(frage, fragetyp, mitHeart);
        });

        Timeline fadeout = new Timeline();
        fadeout.setAutoReverse(false);
        fadeout.setCycleCount(1);
        fadeout.getKeyFrames().addAll(k1, k2);
        fadeout.play();



    }

    public void macheFelderKlickbar(){
        int breite = spielController.getAktSpiel().getAktMap().getFeldBreite();
        int hoehe = spielController.getAktSpiel().getAktMap().getFeldHoehe();
        for(int i = 0; i<breite;i++){
            for(int j=0; j<hoehe; j++){
                Feld f = spielController.getAktSpiel().getAktMap().getFeld(i,j);
                EventHandler<MouseEvent> panePressed = (e -> {

                    if (e.getButton() == MouseButton.PRIMARY && f == spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(),
                            spielController.getAktSpiel().getAktSpieler().getAktY()) && !spielerLaeuft.getValue().booleanValue()){
                        spielController.stelleAktFrage(false);
                        feldAuswahlMakierung.setValue(false);

                    }
                });
                f.addEventHandler(MouseEvent.MOUSE_PRESSED, panePressed);

                f.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.SPACE) {
                        if (f == spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(), spielController.getAktSpiel().getAktSpieler().getAktY())){
                            spielController.stelleAktFrage(false);
                            feldAuswahlMakierung.setValue(false);
                        }
                    }
                });


            }
        }
    }

    public LoadingScreen getLoadingScreen() {
        return ls;
    }



    public void setFeldAuswahlMakierung(boolean anAus){
        if(anAus){
            feldAuswahlMakierung.setValue(true);
        }else{
            feldAuswahlMakierung.set(false);
        }
    }

    public void zeigeEndscreen() {

        EndScreen endScreen = new EndScreen(spielController);
        spielController.getLayout().showEndScreen(endScreen);
        endScreen.showResults();



    }









}
