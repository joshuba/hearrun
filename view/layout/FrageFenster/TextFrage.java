package hearrun.view.layout.FrageFenster;

import hearrun.business.Player;
import hearrun.business.fragen.Frage;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Created by joshuabarth on 16.01.17.
 */
public class TextFrage extends HBox {
    private  Frage frage;
    private VBox vBox;
    private HBox hBox;
    private StackPane textfeld;
    private Label fragetext;
    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;
    private int richtigIndex;
    private ProgressBar time;
    private Label aktZeitAnzeige;
    private SimpleIntegerProperty falschRichtig;
    private SimpleIntegerProperty aktZeit;
    private int zeit = 5;
    private int decounter;
    private Player player;


    public TextFrage(Frage frage, Player player){
        this.setId("TextFrage");
        this.player = player;
        this.frage = frage;
        richtigIndex = frage.getRichtigIndex();
        falschRichtig = new SimpleIntegerProperty();
        aktZeit = new SimpleIntegerProperty();
        falschRichtig.setValue(-1);

        //Elemente erstellen
        vBox = new VBox();
        hBox = new HBox();

        textfeld = new StackPane();
        fragetext = new Label("Frage: ");
        b1 = new Button();
        b2 = new Button();
        b3 = new Button();
        b4 = new Button();
        time = new ProgressBar();
        aktZeitAnzeige = new Label();


        //Zusammenbauen
        textfeld.getChildren().add(fragetext);
        vBox.getChildren().addAll(textfeld, b1, b2, b3, b4);
        hBox.getChildren().addAll(time, aktZeitAnzeige);
        this.getChildren().addAll(vBox,hBox);



        //Stylen
        vBox.setAlignment(Pos.CENTER);
        this.setAlignment(Pos.CENTER);
        textfeld.setId("frageTextfeld");
        textfeld.setPadding(new Insets(0,0,60,0));

        time.setRotate(90);
        hBox.setAlignment(Pos.CENTER_RIGHT);



        //Frageninfos auslesen und in GUI einsetzen
        this.fragetext.setText(frage.getFragetext());
        this.b1.setText(frage.getAntworten()[0]);
        this.b2.setText(frage.getAntworten()[1]);
        this.b3.setText(frage.getAntworten()[2]);
        this.b4.setText(frage.getAntworten()[3]);


        b1.setOnAction((e)-> zeigeRichtigOderFalsch(0, b1));
        b2.setOnAction((e)-> zeigeRichtigOderFalsch(1, b2));
        b3.setOnAction((e)-> zeigeRichtigOderFalsch(2, b3));
        b4.setOnAction((e)-> zeigeRichtigOderFalsch(3, b4));




    }

    public void starteAntworPhase(){
        player.stopLoop();
        if(frage.getPath() != null){
            player.playRandomNSeconds(frage.getPath(),10);

        }
        decounter = 11;

        Thread t =new Thread(){
            public void run(){
                System.out.println("RRRR");
                    int counter = 10;

                    while(counter > 0 && falschRichtig.getValue() != 1 && falschRichtig.getValue() != 0){



                            counter--;
                            Platform.runLater(new Runnable() {
                                @Override public void run() {
                                    decounter--;
                                    aktZeit.setValue(decounter);
                                }
                            });


                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                    System.out.println("LOL");
                    player.stopLoop();
                    this.notifyAll();

                }



        };





        aktZeitAnzeige.textProperty().bind(aktZeit.asString());
        time.progressProperty().bind(aktZeit.multiply(-1));


    }

    public void zeigeRichtigOderFalsch(int index, Button bx){
        disableAllButtons();

        if(index == richtigIndex){
            bx.setId("richtigButton");
            System.out.println("Richtige Antwort");
            this.falschRichtig.setValue(1);

        }else{
            bx.setId("falschButton");
            System.out.println("Falsche Antwort");
            this.falschRichtig.setValue(0);
        }

    }

    public SimpleIntegerProperty getResult(){
        return this.falschRichtig;
    }

    public void disableAllButtons(){
        b1.setDisable(true);
        b2.setDisable(true);
        b3.setDisable(true);
        b4.setDisable(true);
    }




}
