package hearrun.view.layout;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
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

        if(spielController.getAktSpiel().getAktSpieler().getAktX() == 0 && spielController.getAktSpiel().getAktSpieler().getAktY() == 0 && index <=0){
            zurueck();
        }else{
            anzeige = new Label();
            anzeige.setId("grossText");
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
            this.setMaxHeight(120);
            wuerfeln.setMinSize(60,60);

            rad = 0;
            this.setAlignment(Pos.TOP_CENTER);
            this.setPadding(new Insets(30,0,0,0));
            this.getChildren().addAll(iv, wuerfeln, anzeige);
            wuerfeln.setOnAction((e) -> wuerfelProzess());


            this.setOnKeyPressed(key -> {
                if (key.getCode() == KeyCode.ENTER || key.getCode() == KeyCode.SPACE)
                    wuerfeln.fire();
            });
        }

    }



    public int wuerfeln(){
        int zahl = (int)(Math.random() * 4 + 1);
        //Falls die Frage falsch oder gar nicht beantwortet wurde
        if(index <= 0){
            return zahl*(-1);
        }else{
            return zahl;
        }
    }



    public void wuerfelProzess(){
        spielcontroller.getMusicPlayer().play(Main.class.getResource("/hearrun/resources/sounds/wuerfel.mp3").getPath());
        wuerfeln.setDisable(true);
        ergebnis = wuerfeln();
        System.out.println("Ergebnis: " + ergebnis);
        System.out.println("index: " + index);


        KeyFrame k1 = new KeyFrame(Duration.millis(0), a ->{
            anzeige.setText(Integer.toString(1));
           });
        KeyFrame k2 = new KeyFrame(Duration.millis(50), a ->{
            anzeige.setText(Integer.toString(2));
        });
        KeyFrame k3 = new KeyFrame(Duration.millis(100), a ->{
            anzeige.setText(Integer.toString(3));
        });
        KeyFrame k4 = new KeyFrame(Duration.millis(0), a ->{
            anzeige.setText(Integer.toString(-1));
        });
        KeyFrame k5 = new KeyFrame(Duration.millis(50), a ->{
            anzeige.setText(Integer.toString(-2));
        });
        KeyFrame k6 = new KeyFrame(Duration.millis(100), a ->{
            anzeige.setText(Integer.toString(-3));
        });

        Timeline wuerfelt = new Timeline();
        wuerfelt.setAutoReverse(true);
        wuerfelt.setCycleCount(30);
        if(index <= 0){
            wuerfelt.getKeyFrames().addAll(k4, k5, k6);
        }else{
            wuerfelt.getKeyFrames().addAll(k1, k2, k3);

        }
        wuerfelt.setOnFinished(b -> ergebnisZeigen());
        wuerfelt.play();










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
