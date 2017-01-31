package hearrun.view.layout.FrageFenster;

import hearrun.business.Player;
import hearrun.view.controller.SpielController;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.Wuerfel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Oberklasse der Fragefenster
 */
public class FrageFenster extends Fenster {

    protected Player effectPlayer;
    protected   Player musicPlayer;

    private int zeit;
    private float progressWert;
    private float progressIndex;

    protected  Frage frage;
    protected int richtigIndex;
    protected SimpleIntegerProperty falschRichtig;
    protected int decounter;
    protected SimpleIntegerProperty aktZeit;
    protected SimpleFloatProperty progress;

    protected VBox vBox;
    protected VBox timerBox;
    protected StackPane textfeld;
    protected Label fragetext;
    protected ProgressBar time;
    protected Timeline timeline;
    protected VBox wuerfelBox;



    public FrageFenster(Frage frage, SpielController spielController){
        super(spielController);
        progress = new SimpleFloatProperty();
        zeit = Integer.valueOf(spielController.getProperties().getProperty("antwortZeit"));

        this.setId("FrageFenster");
        this.spielController = spielController;
        this.musicPlayer = spielController.getMusicPlayer();
        this.effectPlayer = spielController.getEffectPlayer();
        this.frage = frage;

        richtigIndex = frage.getRichtigIndex();
        falschRichtig = new SimpleIntegerProperty();
        aktZeit = new SimpleIntegerProperty();
        aktZeit.setValue(zeit);
        falschRichtig.setValue(-1); //= Nicht beantwortet

        //Elemente erstellen
        vBox = new VBox();
        timerBox = new VBox();
        wuerfelBox = new VBox();

        textfeld = new StackPane();
        fragetext = new Label();
        time = new ProgressBar();
        time.setId("progressBar");


        //Zusammenbauen
        textfeld.getChildren().add(fragetext);
        timerBox.getChildren().addAll(time);
        this.setCenter(vBox);
        this.setRight(timerBox);
        this.setPadding(new Insets(0,0,0,100));


        //Stylen
        top.setPadding(new Insets(20,100,0,0));

        vBox.setAlignment(Pos.CENTER);

        textfeld.setId("frageTextfeld");
        textfeld.setPadding(new Insets(0,0,60,0));

        aktSpieler.setId("grossText");

        time.minWidthProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(200));
        time.setRotate(90);
        time.minHeightProperty().bind(spielController.getLayout().getViewController().getStage().widthProperty().divide(36));
        timerBox.setMinWidth(100);
        timerBox.setMaxWidth(100);
        timerBox.setPadding(new Insets(0,0,110,0));
        timerBox.setAlignment(Pos.CENTER);

        wuerfelBox.setMinHeight(140);
        wuerfelBox.setId("wuerfelBox");






        //Frageninfos auslesen und in GUI einsetzen
        this.fragetext.setText(frage.getFragetext());



        progress.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if(newValue.floatValue() >= 0.6 && newValue.floatValue() < 0.8){
                    time.setId("progressBarOrange");
                }else if(newValue.floatValue() >= 0.8){
                    time.setId("progressBarRed");

                }
            }
        });



    }

    public void starteAntwortPhase(){
        zeit = Integer.valueOf(spielController.getProperties().getProperty("antwortZeit"));
        progressIndex = (100/zeit+1)/100F;
        progressWert = 0;

        musicPlayer.stop();
        if(frage.getPath() != null){
            musicPlayer.playRandomNSeconds(frage.getPath(),zeit);
        }
        decounter = zeit;

        KeyFrame k1 = new KeyFrame(Duration.millis(1000), a ->{

            aktZeit.set(decounter--);
            getProgress();
        });


        timeline = new Timeline(k1);
        timeline.setOnFinished(b -> fertig());
        timeline.setAutoReverse(false);
        timeline.setCycleCount(zeit+1);
        timeline.play();

        time.progressProperty().bind(progress);


    }

    protected void fertig(){
        musicPlayer.fadeOut();
        zeigeRichtigOderFalsch();
        aktualisiereAchievement();

    }


    public SimpleIntegerProperty getResult(){
        return this.falschRichtig;
    }




    public void zeigeRichtigOderFalsch(){


    }



    public void wuerfeln(int index){
        if (spielController.getAktSpiel().getAktSpieler().getLeben() > 0) {
            spielController.stelleAktFrage();
            spielController.getAktSpiel().getAktSpieler().removeLeben();
        } else {
            Wuerfel w = new Wuerfel(index, spielController);
            w.setAlignment(Pos.CENTER);
            this.getChildren().add(w);
            w.requestFocus(); // Fokus darauf setzen damit Shortcut funktioniert.
            wuerfelBox.getChildren().addAll(w);
        }
    }


    public void getProgress(){
        progress.setValue(progressWert);
        progressWert += progressIndex;

    }

    protected void aktualisiereAchievement() {
        // Statistik zu gewonnenen Spielen aktualisieren
        if (falschRichtig.getValue() == 1) {
            ObservableMap<String, String> achievements = spielController.getAktSpiel().getAktSpieler().getAchievements();
            int gewonneneSpiele = Character.getNumericValue(
                    achievements.get("fragen").charAt(achievements.get("fragen").length()-1)
            );
            achievements.put("fragen", "Richtige Fragen: " + ++gewonneneSpiele);
        }

    }







}
