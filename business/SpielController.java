package hearrun.business;

import hearrun.business.fragen.Frage;
import hearrun.view.layout.Feld;
import hearrun.view.layout.CompleteLayout;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

/**
 * Created by joshuabarth on 12.01.17.
 */
public class SpielController {
    private Spiel aktSpiel;
    private CompleteLayout completeLayout;
    private Stage stage;
    private String dateiname;
    private int spieleranzahl;
    private FrageController frageController;
    private Player musicPlayer;
    private Player effectPlayer;
    private Properties properties;
    private String path;


    public SpielController(Stage stage, String dateiname, int spieleranzahl) {
        properties = new Properties();
        erstellePropPfad();
        this.stage = stage;
        this.musicPlayer = new Player();
        this.effectPlayer = new Player();
        this.dateiname = dateiname;
        this.spieleranzahl = spieleranzahl;
        this.completeLayout = new CompleteLayout(stage, this);
        readProperties();

        frageController = new FrageController(this);
        ladeMusik();


    }

    private void waehleMapErstelleSpiel(String dateiname, int spieleranzahl) {

        this.aktSpiel = new Spiel(dateiname, spieleranzahl, completeLayout.getViewController()); //Erstelle spiel
        completeLayout.getViewController().baueSpielfeldAuf();
        completeLayout.getViewController().setFeldId(0, 0, completeLayout.getViewController().erkenneFeldId(0, 0)); //Setze Alle Player aufs erste Feld

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
        waehleMapErstelleSpiel(dateiname, spieleranzahl);
        completeLayout.getViewController().setGameLayout();


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
            frageController.leseMusikEin(properties.getProperty("musicPath"));

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











}
