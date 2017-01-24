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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by joshuabarth on 16.01.17.
 */
public class TextFrage extends BorderPane {
   private SpielController spielController;
    private Player effectPlayer;
    private  Player musicPlayer;

    private int zeit;
    float progressWert;
    float progressIndex;

    private  Frage frage;
    private int richtigIndex;
    private SimpleIntegerProperty falschRichtig;
    private int decounter;
    private SimpleIntegerProperty aktZeit;
    private SimpleFloatProperty progress;


    private VBox vBox;
    private VBox timerBox;
    private StackPane textfeld;
    private Label fragetext;
    private Button [] buttons;
    private Button richtigButton;
    private ProgressBar time;
    private Label aktZeitAnzeige;
    private Timeline timeline;
    private Label aktSpieler;
    private HBox catPic;
    private VBox top;



    public TextFrage(Frage frage, SpielController spielController){
        progress = new SimpleFloatProperty();
        zeit = Integer.valueOf(spielController.getProperties().getProperty("antwortZeit"));

        this.setId("TextFrage");
        this.spielController = spielController;
        this.musicPlayer = spielController.getMusicPlayer();
        this.effectPlayer = spielController.getEffectPlayer();
        this.frage = frage;

        richtigIndex = frage.getRichtigIndex();
        falschRichtig = new SimpleIntegerProperty();
        aktZeit = new SimpleIntegerProperty();
        aktZeit.setValue(zeit);
        falschRichtig.setValue(-1); //= Nicht beantwortet
        this.buttons = new Button[4];

        //Elemente erstellen
        vBox = new VBox();
        timerBox = new VBox();
        top = new VBox();

        textfeld = new StackPane();
        fragetext = new Label("Frage: ");
        buttons [0] = new Button();
        buttons [1] = new Button();
        buttons [2] = new Button();
        buttons [3] = new Button();
        time = new ProgressBar();
        time.setId("progressBar");
        aktZeitAnzeige = new Label();
        aktSpieler = new Label("Spieler " + Integer.toString(spielController.getAktSpiel().getAktSpieler().getNr()+1));
        catPic = new HBox();


        //Zusammenbauen
        textfeld.getChildren().add(fragetext);
        vBox.getChildren().addAll(textfeld, buttons[0], buttons[1], buttons[2], buttons[3]);
        timerBox.getChildren().addAll(time, aktZeitAnzeige);
        top.getChildren().addAll(catPic, aktSpieler);
        this.setTop(top);
        this.setCenter(vBox);
        this.setRight(timerBox);
        this.setPadding(new Insets(0,0,0,100));


        //Stylen
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(0,100,0,0));
        catPic.setMinSize(80,80);
        catPic.setMaxSize(80,80);
        catPic.setId(spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(), spielController.getAktSpiel().getAktSpieler().getAktY()).getLeerId());

        vBox.setAlignment(Pos.CENTER);

        textfeld.setId("frageTextfeld");
        textfeld.setPadding(new Insets(0,0,60,0));

        aktSpieler.setId("grossText");

        time.setRotate(90);
        time.setMinWidth(700);
        time.setMinHeight(50);
        timerBox.setMinWidth(100);
        timerBox.setMaxWidth(100);
        timerBox.setAlignment(Pos.CENTER);






        //Frageninfos auslesen und in GUI einsetzen
        this.fragetext.setText(frage.getFragetext());
        buttons[0].setText(frage.getAntworten()[0]);
        buttons[1].setText(frage.getAntworten()[1]);
        buttons[2].setText(frage.getAntworten()[2]);
        buttons[3].setText(frage.getAntworten()[3]);
        richtigButton = buttons[frage.getRichtigIndex()];
        buttons[0].setId("normalButton");
        buttons[1].setId("normalButton");
        buttons[2].setId("normalButton");
        buttons[3].setId("normalButton");


        buttons[0].setOnAction((e)-> buttonPress(buttons[0]));
        buttons[1].setOnAction((e)-> buttonPress(buttons[1]));
        buttons[2].setOnAction((e)-> buttonPress(buttons[2]));
        buttons[3].setOnAction((e)-> buttonPress(buttons[3]));




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
        aktZeitAnzeige.textProperty().bind(aktZeit.asString());


    }

    public void fertig(){
        musicPlayer.fadeOut();
        zeigeRichtigOderFalsch();
        disableAllButtons();





    }

    public void buttonPress(Button bx){
        disableAllButtons();
        timeline.stop();

        if(bx == richtigButton){
            bx.setId("richtigButton");
            this.falschRichtig.setValue(1);
            fertig();

            effectPlayer.play("src/hearrun/resources/sounds/right.mp3");


        }else{
            bx.setId("falschButton");
            this.falschRichtig.setValue(0);
            fertig();

            effectPlayer.play("src/hearrun/resources/sounds/wrong.mp3");

        }

    }

    public SimpleIntegerProperty getResult(){
        return this.falschRichtig;
    }

    public void disableAllButtons(){
        buttons[0].setDisable(true);
        buttons[1].setDisable(true);
        buttons[2].setDisable(true);
        buttons[3].setDisable(true);
    }

    public void zeigeRichtigOderFalsch(){
        KeyFrame k = new KeyFrame(
                Duration.millis(300),
                a -> richtigButtonFaerben()
        );

        Timeline t = new Timeline(k);
        t.setCycleCount(6);
        if(falschRichtig.getValue() == -1){
            effectPlayer.play("src/hearrun/resources/sounds/wrong.mp3");

        }

        t.setOnFinished(b -> wuerfeln(falschRichtig.getValue()));
        t.play();


    }

    private void richtigButtonFaerben(){
        if(richtigButton.getId().equals("richtigButton")){
            richtigButton.setId("normalButton");
        }else if(richtigButton.getId().equals("normalButton")){
            richtigButton.setId("richtigButton");
        }

    }

    public void wuerfeln(int index){
        Wuerfel w = new Wuerfel(index, spielController);
        w.setPadding(new Insets(30,0,0,0));
        w.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(w);
    }

    public void getProgress(){
        System.out.println(progressWert);

         progress.setValue(progressWert);
        progressWert += progressIndex;

    }







}
