package hearrun.controller;

import hearrun.model.FrageGenerator;
import hearrun.model.ereignisse.Ereignis;
import hearrun.view.layout.Feld;
import hearrun.view.layout.CompleteLayout;
import hearrun.view.layout.Map;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Der Spiel-Controller verwaltet ein Spiel. Er managed die Spielabläufe. Mithilfe von Properties werden
 * die wichtigsten Spielinfos gespeichert und bleiben erhalten.
 */
public class SpielController {
    private hearrun.model.Spiel aktSpiel;
    private CompleteLayout completeLayout;
    private Stage stage;
    private Map map;
    private ArrayList<hearrun.model.Spieler> spielerListe;
    private FrageGenerator frageGenerator;
    private hearrun.model.Player musicPlayer;
    private hearrun.model.Player effectPlayer;
    private hearrun.model.Player loopPlayer;
    private Properties properties;
    private String path;
    private SimpleBooleanProperty sieg;



    public SpielController(Stage stage) {
        properties = new Properties();
        path = erstellePropPfad();
        this.stage = stage;
        this.musicPlayer = new hearrun.model.Player();
        this.effectPlayer = new hearrun.model.Player();
        this.loopPlayer = new hearrun.model.Player();
        this.completeLayout = new CompleteLayout(stage, this);

        readProperties();

        frageGenerator = new FrageGenerator();
        ladeMusik();


    }


    private void waehleMapErstelleSpiel(Map map, ArrayList<hearrun.model.Spieler> spielerListe) {
        this.aktSpiel = new hearrun.model.Spiel(map, spielerListe); //Erstelle spiel
        sieg = getAktSpiel().getSiegStatus();
        completeLayout.getViewController().baueSpielfeldAuf();
        completeLayout.getViewController().setFeldId(0, 0, completeLayout.getViewController().erkenneFeldId(0, 0)); //Setze Alle Player aufs erste Feld
        getLayout().getViewController().macheFelderKlickbar();

        //Wenn das spiel zuende ist rufe endscreen auf
        sieg.addListener(new javafx.beans.value.ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue.booleanValue() == true && oldValue.booleanValue() == false){
                    getLayout().getViewController().zeigeEndscreen();
                }
            }
        });




    }


    public CompleteLayout getLayout() {
        return this.completeLayout;
    }

    public void moveAktSpieler(int schritte) {
        completeLayout.getViewController().movePlayer(schritte, aktSpiel.getAktSpieler());

    }

    public void nextSpieler() {
        this.getAktSpiel().nextSpieler();
        getLayout().getGameLayout().setAktSpieler(getAktSpiel().getAktSpieler().getNr());
    }

    public hearrun.model.Spiel getAktSpiel() {
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
        //Property :)
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

    public void stelleAktFrage(boolean mitHeart) {
        Feld aktFeld = aktSpiel.getAktMap().getFeld(aktSpiel.getAktSpieler().getAktX(), aktSpiel.getAktSpieler().getAktY());
        hearrun.model.Fragetyp fragetyp = aktFeld.getPassendenFragetyp();
        if (fragetyp == hearrun.model.Fragetyp.Ereignis)
            getLayout().getViewController().zeigeEreignis(Ereignis.zufallsEreignis());
        else {
            getLayout().getViewController().zeigeIntroUndFrage(frageGenerator.getFrage(fragetyp), fragetyp, mitHeart);
        }
    }

    public hearrun.model.Player getMusicPlayer() {
        return this.musicPlayer;
    }

    public hearrun.model.Player getEffectPlayer() {
        return this.effectPlayer;
    }

    public void moveAndAskNext(int schritte) {
        completeLayout.getViewController().movePlayer(schritte, aktSpiel.getAktSpieler());
        //next wird nach dem laufen aufgerufen

    }


    public void ladeMusik() {
        if (properties.getProperty("musicPath") == null) {
            getLayout().getViewController().zeigeIntroScreen();
        } else {
            System.out.println("Lese Musik ein: " + properties.getProperty("musicPath"));
            completeLayout.getViewController().zeigeLadeScreen(
                    frageGenerator.readingOnOffProperty(),
                    frageGenerator.musicReadingProgressProperty(),
                    frageGenerator.fragenAnzahlProperty()
            );

            frageGenerator.leseMusikEin(properties.getProperty("musicPath"));

            frageGenerator.successProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    getLayout().getViewController().getLoadingScreen().zeigeFehler();
                }
            });
        }
    }


    public void readProperties() {
        //Prüfe ob Properties angelegt wurden
        try {
            InputStream input = new FileInputStream(path);
            properties.load(input);

        } catch (FileNotFoundException e) {
            properties.setProperty("antwortZeit", "8");
            properties.setProperty("tutorialZeigen", "true");
            writeProperties();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeProperties() {

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

    public static String erstellePropPfad() {
        String path = null;

        //MAC
        if (System.getProperties().getProperty("os.name").contains("Mac")) {
            File file = new File(System.getProperty("user.home") + "/Library/Application Support/HearRun");
            file.mkdir();
            path = System.getProperties().getProperty("user.home") + "/Library/Application Support/HearRun/game.properties";
            System.out.println(path);
        }
        //Windows
        if (System.getProperties().getProperty("os.name").contains("Win")) {
            File file = new File(System.getProperty("user.home") + "/AppData/Local/HearRun");
            file.mkdir();
            path = System.getProperties().getProperty("user.home") + "/AppData/Local/HearRun/game.properties";

        }
        //Linux
        if (System.getProperties().getProperty("os.name").contains("Lin")) {
            System.out.println("Du nutzt Linux");
            File file = new File(System.getProperty("user.home") + ".congig/HearRun");
            file.mkdir();
            path = System.getProperties().getProperty("user.home") + "/.congig/HearRun/game.properties";
        }
        return path;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void resetSettingsProperty() {
        properties.remove("musicPath");
        properties.remove("antwortZeit");
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setSpieler(ArrayList<hearrun.model.Spieler> spielerListe) {
        this.spielerListe = spielerListe;
    }

    public ArrayList<hearrun.model.Spieler> getSpielerListe() {
        return this.spielerListe;
    }

    public Stage getStage() {
        return stage;
    }

    public hearrun.model.Player getLoopPlayer(){
        return this.loopPlayer;
    }


}
