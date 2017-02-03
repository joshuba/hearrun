package hearrun.view.layout.FrageFenster;

import hearrun.Main;
import hearrun.business.Player;
import hearrun.business.Spieler;
import hearrun.view.controller.SpielController;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.Wuerfel;
import javafx.animation.*;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public abstract class FrageFenster extends Fenster {

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
    protected boolean heartAnimation;



    public FrageFenster(Frage frage, SpielController spielController, boolean heartAnimation){
        super(spielController);
        this.heartAnimation = heartAnimation;
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
        fragetext.setId("frageText");
        fragetext.setWrapText(true);
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






        if(heartAnimation){
            addHeartAnimation();
        }



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

        spielController.getLoopPlayer().fadeOut();
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

    abstract void richtigButtonFaerben();


    public void zeigeRichtigOderFalsch() {
        KeyFrame k = new KeyFrame(
                Duration.millis(300),
                a -> richtigButtonFaerben()
        );

        Timeline t = new Timeline(k);
        t.setCycleCount(6);
        if (falschRichtig.getValue() == -1) {
            effectPlayer.play("/hearrun/resources/sounds/wrong.mp3");

        }

        t.setOnFinished(b -> wuerfeln(falschRichtig.getValue()));
        t.play();
    }



    public void wuerfeln(int index){
        if (spielController.getAktSpiel().getAktSpieler().getLeben() > 0 && index != 1) {
            spielController.stelleAktFrage(true);
            spielController.getAktSpiel().getAktSpieler().removeLeben();
            spielController.getAktSpiel().getAktSpieler().addUsedHeart();


        } else {
            Wuerfel w = new Wuerfel(index, spielController);
            w.setAlignment(Pos.CENTER);
            this.getChildren().add(w);
            w.requestFocus(); // Fokus darauf setzen damit Shortcut funktioniert.
            wuerfelBox.getChildren().addAll(w);
        }
    }

    private void addHeartAnimation() {
        HBox ereignisHeart = new HBox();
        ereignisHeart.setAlignment(Pos.CENTER);
        ereignisHeart.setMinSize(50,50);
        ereignisHeart.setMaxSize(50,50);

        this.getChildren().add(ereignisHeart);
        wuerfelBox.getChildren().addAll(ereignisHeart);
        wuerfelBox.setAlignment(Pos.CENTER);


        ereignisHeart.setId("leben-icon-big");

        ScaleTransition st = new ScaleTransition(Duration.millis(800), ereignisHeart);
        st.setFromY(0);
        st.setFromX(0);
        st.setToY(10);
        st.setToX(10);


        FadeTransition ft2 = new FadeTransition(Duration.millis(150), ereignisHeart);
        ft2.setFromValue(0);
        ft2.setToValue(100);


        FadeTransition ft = new FadeTransition(Duration.millis(800), ereignisHeart);
        ft.setFromValue(100);
        ft.setToValue(0);
        ft.setOnFinished(e -> {
            this.getChildren().removeAll(ereignisHeart);
            wuerfelBox.getChildren().removeAll(ereignisHeart);
        });

        KeyFrame k1 = new KeyFrame(Duration.millis(1000), a -> {
            ft2.play();
            ft.play();
            st.play();
        });

        Timeline f = new Timeline();
        f.setAutoReverse(false);
        f.setCycleCount(1);
        f.getKeyFrames().addAll(k1);
        f.play();







    }


    public void getProgress(){
        progress.setValue(progressWert);
        progressWert += progressIndex;

    }

    protected void aktualisiereAchievement() {
        // Statistik zu gewonnenen Spielen aktualisieren
        if (falschRichtig.getValue() == 1) {
            Spieler spieler = spielController.getAktSpiel().getAktSpieler();
            spieler.addRichtigeFrage();
        }else if(falschRichtig.getValue() == 0){
            Spieler spieler = spielController.getAktSpiel().getAktSpieler();
            spieler.addFalscheFrage();
        }else if(falschRichtig.getValue() == -1){
            Spieler spieler = spielController.getAktSpiel().getAktSpieler();
            spieler.addZeitAbgelaufen();
        }

    }







}
