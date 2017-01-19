package hearrun.view.layout.FrageFenster;

import hearrun.business.Player;
import hearrun.business.SpielController;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.Wuerfel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by joshuabarth on 16.01.17.
 */
public class TextFrage extends VBox {
    private  Frage frage;
    private VBox vBox;
    private HBox hBox;
    private StackPane textfeld;
    private Label fragetext;
    private Button [] buttons;
    private Button richtigButton;
    private int richtigIndex;
    private ProgressBar time;
    private Label aktZeitAnzeige;
    private SimpleIntegerProperty falschRichtig;
    private SimpleIntegerProperty aktZeit;
    private final int zeit = 5;
    private int decounter;
    private Player player;
    private Timeline timeline;
    private SpielController spielController;
    private Label aktSpieler;
    private HBox container;



    public TextFrage(Frage frage, SpielController spielController){
        this.spielController = spielController;
        this.setId("TextFrage");
        this.player = spielController.getPlayer();
        this.frage = frage;

        richtigIndex = frage.getRichtigIndex();
        falschRichtig = new SimpleIntegerProperty();
        aktZeit = new SimpleIntegerProperty();
        falschRichtig.setValue(-1);
        this.buttons = new Button[4];

        //Elemente erstellen
        vBox = new VBox();
        hBox = new HBox();
        container = new HBox();


        textfeld = new StackPane();
        fragetext = new Label("Frage: ");
        buttons [0] = new Button();
        buttons [1] = new Button();
        buttons [2] = new Button();
        buttons [3] = new Button();
        time = new ProgressBar();
        aktZeitAnzeige = new Label();
        aktSpieler = new Label("Spieler: " + Integer.toString(spielController.getAktSpiel().getAktSpieler().getNr()+1));


        //Zusammenbauen
        textfeld.getChildren().add(fragetext);
        vBox.getChildren().addAll(textfeld, buttons[0], buttons[1], buttons[2], buttons[3]);
        hBox.getChildren().addAll(time, aktZeitAnzeige);
        container.getChildren().addAll(vBox, hBox);
        this.getChildren().addAll(aktSpieler, container);



        //Stylen
        vBox.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        container.setAlignment(Pos.CENTER);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        textfeld.setId("frageTextfeld");
        textfeld.setPadding(new Insets(0,0,60,0));

        time.setRotate(90);
        hBox.setAlignment(Pos.CENTER_RIGHT);


        aktZeitAnzeige.textProperty().bind(aktZeit.asString());
        time.progressProperty().bind(aktZeit.multiply(-1));



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
        player.stopLoop();
        if(frage.getPath() != null){
            player.playRandomNSeconds(frage.getPath(),zeit);
        }

        decounter = zeit;


        timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                a -> aktZeit.set(decounter--)));
        timeline.setOnFinished(b -> fertig());
        timeline.setCycleCount(zeit+2);
        timeline.play();


    }

    public void fertig(){
        player.stopLoop();
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

        }else{
            bx.setId("falschButton");
            this.falschRichtig.setValue(0);
            fertig();
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
        this.getChildren().addAll(w);
    }






}
