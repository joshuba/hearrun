package hearrun.view.layout;

import hearrun.business.Spiel;
import hearrun.business.SpielController;
import hearrun.business.fragen.InterpretFrage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Josh on 19.01.17.
 */
public class Wuerfel extends VBox{
    private int index;
    private ImageView iv;
    private Button wuerfeln;
    private Label anzeige;
    private int rad;
    private int ergebnis;
    private SpielController spielcontroller;


    public Wuerfel(int index, SpielController spielController){
        this.spielcontroller = spielController;
        anzeige = new Label();
        iv = new ImageView();
        this.index = index;


        if(index == -1){
            this.wuerfeln = new Button();
            wuerfeln.setId("negWuerfel");


        }else if(index == 0){
            this.wuerfeln = new Button();
            wuerfeln.setId("negWuerfel");


        }else if(index == 1){
            this.wuerfeln = new Button();
            wuerfeln.setId("posWuerfel");

        }

        //stylen
        wuerfeln.setMinSize(60,60);
        anzeige.setMinSize(60,60);


        rad = 0;

        this.getChildren().addAll(iv, wuerfeln, anzeige);
        wuerfeln.setOnAction((e) -> wuerfelProzess());
    }



    public int wuerfeln(){
        int zahl = (int)(Math.random() * 3 + 1);
        //Falls die Frage falsch oder gar nicht beantwortet wurde
        if(index <= 0){
            return zahl*(-1);
        }else{
            return zahl;
        }
    }

    private int gebeZahlRad(){
        rad = ((rad+1) % 4);
        if(rad == 0){
            rad = rad+1;
        }
        if(index <= 0){
            return rad*(-1);
        }else{
            return rad;
        }

    }

    public void wuerfelProzess(){
        spielcontroller.getEffectPlayer().play("src/hearrun/resources/sounds/wuerfel.mp3");
        wuerfeln.setDisable(true);
        ergebnis = wuerfeln();
        System.out.println("Ergebnis: " + ergebnis);

        KeyFrame k = new KeyFrame(
                    Duration.millis(30),
                    a -> anzeige.setText(Integer.toString(gebeZahlRad()))
            );

            Timeline t = new Timeline(k);
            t.setCycleCount(70);

            t.setOnFinished(b -> ergebnisZeigen());
            t.play();

        }

    public void ergebnisZeigen(){
        anzeige.setText(Integer.toString(ergebnis));

        KeyFrame k = new KeyFrame(
                Duration.millis(1500)
        );

        Timeline t = new Timeline(k);
        t.setOnFinished(b -> zurueck());
        t.play();

    }

    public void zurueck(){
        spielcontroller.getLayout().bluryAnAus(false);
        spielcontroller.getLayout().setGameLayout();
        spielcontroller.moveAndAskNext(ergebnis);

    }





    }
