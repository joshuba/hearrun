package hearrun.view.controller;

import hearrun.business.*;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.Feld;
import hearrun.view.layout.CompleteLayout;
import hearrun.view.layout.Map;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

import javax.swing.event.ChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by joshuabarth on 12.01.17
 */
public class SpielController {
    private Spiel aktSpiel;
    private CompleteLayout completeLayout;
    private Stage stage;
    private Map map;
    private ArrayList<Spieler> spielerListe;
    private FrageController frageController;
    private Player musicPlayer;
    private Player effectPlayer;
    private Properties properties;
    private String path;


    public SpielController(Stage stage) {
        properties = new Properties();
        erstellePropPfad();
        this.stage = stage;
        this.musicPlayer = new Player();
        this.effectPlayer = new Player();
        this.completeLayout = new CompleteLayout(stage, this);
        readProperties();

        frageController = new FrageController();
        ladeMusik();


    }



    private void waehleMapErstelleSpiel(Map map, ArrayList<Spieler> spielerListe) {
        this.aktSpiel = new Spiel(map, spielerListe); //Erstelle spiel
        completeLayout.getViewController().baueSpielfeldAuf();
        completeLayout.getViewController().setFeldId(0, 0, completeLayout.getViewController().erkenneFeldId(0, 0)); //Setze Alle Player aufs erste Feld
        getLayout().getViewController().macheFelderKlickbar();


    }


    public CompleteLayout getLayout() {
        return this.completeLayout;
    }

    public void moveAktSpieler(int schritte) {
        completeLayout.getViewController().movePlayer(schritte, aktSpiel.getAktSpieler());

    }

    public void nextSpieler() {
        this.getAktSpiel().nextSpieler();

    }

    public Spiel getAktSpiel() {
        return aktSpiel;
    }

    public void setPlayerName(int nr, String name) {
        aktSpiel.getSpielerByNr(nr).setName(name);
    }

    public void starteSpiel() {
        if (aktSpiel != null) {
            beendeSpiel();
        }

        completeLayout.getViewController().resetGameLayout();
        waehleMapErstelleSpiel(map, spielerListe);
        completeLayout.getViewController().setGameLayout();
        getLayout().getViewController().setFeldAuswahlMakierung(false);

        getLayout().getViewController().setFeldAuswahlMakierung(true);




    }

    public void beendeSpiel() {
        this.aktSpiel = null;
        this.completeLayout.getViewController().resetGameLayout();
        musicPlayer.stop();
        effectPlayer.stop();


    }

    public void beendeProgramm() {
        writeProperties();
        musicPlayer.stop();
        effectPlayer.stop();
        effectPlayer.stop();
        stage.close();
    }

    public void stelleAktFrage() {
        Feld aktFeld = aktSpiel.getAktMap().getFeld(aktSpiel.getAktSpieler().getAktX(), aktSpiel.getAktSpieler().getAktY());
        Fragetyp fragetyp = aktFeld.getPassendenFragetyp();
        Frage frage = frageController.getFrage(fragetyp);

        //Zeige frage
        getLayout().getViewController().zeigeIntroUndFrage(frage, fragetyp);
    }

    public Player getMusicPlayer() {
        return this.musicPlayer;
    }

    public Player getEffectPlayer() {
        return this.effectPlayer;
    }

    public void stelleFrage() {
        stelleAktFrage();

    }

    public void moveAndAskNext(int schritte) {
        completeLayout.getViewController().movePlayer(schritte, aktSpiel.getAktSpieler());
        nextSpieler();

    }


    public void ladeMusik(){
        if(properties.getProperty("musicPath")== null){
            getLayout().getViewController().zeigeIntroScreen();
        }else{
            System.out.println("Versuche Musik einzulesen: " + properties.getProperty("musicPath"));
            completeLayout.getViewController().zeigeLadeScreen(
                    frageController.readingOnOffProperty(),
                    frageController.musicReadingProgressProperty(),
                    frageController.fragenAnzahlProperty()
            );

            frageController.leseMusikEin(properties.getProperty("musicPath"));

            frageController.successProperty().addListener((observable, oldValue, newValue) -> {
                if(!newValue) {
                    getLayout().getViewController().getLoadingScreen().zeigeFehler();
                }
            });
        }
    }



    public void readProperties(){
        //Prüfe ob Properties angelegt wurden
        try {
            InputStream input = new FileInputStream(path);
            properties.load(input);

        } catch (FileNotFoundException e) {
            properties.setProperty("aktVolume", "100");
            properties.setProperty("antwortZeit", "7");
            writeProperties();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeProperties(){

        try {
            OutputStream output = new FileOutputStream(path);

            // save properties to project root folder
            properties.store(output, null);

        } catch (FileNotFoundException e) {
            System.out.println("Fehler beim Speichern der Properties");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void erstellePropPfad(){
        //MAC
        if(System.getProperties().getProperty("os.name").contains("Mac")){
            System.out.println("Du nutzt macOs");
            File file = new File(System.getProperty("user.home") + "/Library/Application Support/HearRun");
            file.mkdir();
            path = System.getProperties().getProperty("user.home") + "/Library/Application Support/HearRun/game.properties";
            System.out.println(path);
        }
        //Windows
        if(System.getProperties().getProperty("os.name").contains("Win")){
            System.out.println("Du nutzt Windows, ieh");
            File file = new File(System.getProperty("user.home") + "/AppData/Local/HearRun");
            file.mkdir();
            path = System.getProperties().getProperty("user.home") + "/AppData/Local/HearRun/game.properties";

        }
        //Linux
        if(System.getProperties().getProperty("os.name").contains("Lin")){
            System.out.println("Du nutzt Linux");
            File file = new File(System.getProperty("user.home") + ".congig/HearRun");
            file.mkdir();
            path = System.getProperties().getProperty("user.home") + "/.congig/HearRun/game.properties";
        }
    }

    public Properties getProperties(){
        return this.properties;
    }

    public void resetMusicPathPropertie(){
        properties.remove("musicPath");
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setSpieler(ArrayList<Spieler> spielerListe) {
        this.spielerListe = spielerListe;
    }

    public ArrayList<Spieler> getSpielerListe() {
        return this.spielerListe;
    }

    public Stage getStage() {
        return stage;
    }
}
