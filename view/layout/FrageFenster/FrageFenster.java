package hearrun.view.layout.FrageFenster;

import hearrun.business.Player;
import hearrun.business.SpielController;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.Wuerfel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Oberklasse der Fragefenster
 */
public class FrageFenster extends BorderPane {

    protected SpielController spielController;
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
    protected Label aktSpieler;
    protected HBox catPic;
    protected VBox top;
    protected VBox wuerfelBox;



    public FrageFenster(Frage frage, SpielController spielController){
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
        top = new VBox();
        wuerfelBox = new VBox();

        textfeld = new StackPane();
        fragetext = new Label();
        time = new ProgressBar();
        time.setId("progressBar");
        aktSpieler = new Label(spielController.getAktSpiel().getAktSpieler().getName());
        catPic = new HBox();


        //Zusammenbauen
        textfeld.getChildren().add(fragetext);
        timerBox.getChildren().addAll(time);
        top.getChildren().addAll(catPic, aktSpieler);
        this.setTop(top);
        this.setCenter(vBox);
        this.setRight(timerBox);
        this.setPadding(new Insets(0,0,0,100));


        //Stylen
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(20,100,0,0));
        catPic.setMinSize(80,80);
        catPic.setMaxSize(80,80);
        catPic.setId(spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(), spielController.getAktSpiel().getAktSpieler().getAktY()).getLeerId());

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





    }

    public void starteAntworPhase(){
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
            System.out.println(progress);
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

    }


    public SimpleIntegerProperty getResult(){
        return this.falschRichtig;
    }




    public void zeigeRichtigOderFalsch(){


    }



    public void wuerfeln(int index){
        Wuerfel w = new Wuerfel(index, spielController);
        w.setAlignment(Pos.CENTER);
        this.getChildren().add(w);
        wuerfelBox.getChildren().addAll(w);

    }


    public void getProgress(){
        progress.setValue(progressWert);
        progressWert += progressIndex;

    }







}
