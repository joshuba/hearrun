package hearrun.view.layout;

import javafx.animation.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;

/**
 * Created by Josh on 29.01.17.
 */
public class CircleSpawner extends GridPane {
    private Stage stage;
    private int max = 5;
    private HBox [][] raster;
    SimpleIntegerProperty anzahl;
    int index;
    private Timeline spawn;



    public CircleSpawner(Stage stage){

        BoxBlur bb = new BoxBlur();
        //this.setEffect(bb);
        bb.setHeight(5);
        bb.setWidth(5);
        bb.setIterations(1);

        anzahl = new SimpleIntegerProperty();
        anzahl.setValue(0);
        index = 0;

        raster = new HBox[10][5];

        //this.setGridLinesVisible(true);
        this.setAlignment(Pos.CENTER);
        this.stage = stage;

        for (int i = 0; i<10; i++){
            for(int j = 0; j <5; j++){
                HBox hbox = new HBox();
                //hbox.setId("TEST");
                hbox.prefHeightProperty().bind(stage.heightProperty());
                hbox.prefWidthProperty().bind(stage.widthProperty());

                GridPane.setConstraints(hbox, i,j );
                raster[i][j] = hbox;
                this.getChildren().add(hbox);
            }
        }




    }

    public void createRandomCircle(){
        double breite = stage.widthProperty().getValue();
        double hoehe = stage.heightProperty().getValue();
        Point p = getKoordinaten();
        int x = p.x;
        int y = p.y;
        int farbe = randomWithRange(1,7);


        Circle c1 = new Circle();
        c1.setRadius(20);
        c1.centerXProperty().setValue(x);
        c1.centerYProperty().setValue(y);
        c1.setFill(Color.WHITE);


        getFromGrid(x,y).getChildren().addAll(c1);
        getFromGrid(x,y).setAlignment(Pos.CENTER);



        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(10000), getFromGrid(x,y));
            scaleTransition.setCycleCount(1);
            int faktor = randomWithRange(4,16);


            scaleTransition.setFromX(0);
            scaleTransition.setFromY(0);
            scaleTransition.setToX(faktor);
            scaleTransition.setToY(faktor);
            scaleTransition.play();

        });

        KeyFrame k2 = new KeyFrame(Duration.millis(100), a ->{
            c1.setId("kreis" + farbe);


        });


        KeyFrame k3 = new KeyFrame(Duration.millis(5000), a ->{
            FadeTransition fadeTransition = new FadeTransition(Duration.millis(1000), getFromGrid(x,y));
            fadeTransition.setCycleCount(1);


            fadeTransition.setFromValue(100);
            fadeTransition.setToValue(0);
            fadeTransition.play();
            fadeTransition.setOnFinished(e -> {
                removeFromGrid(x, y);
            });
        });

        Timeline zoom = new Timeline();
        zoom.setAutoReverse(false);
        zoom.setCycleCount(1);
        zoom.getKeyFrames().addAll(k1,k2, k3);
        zoom.play();




    }

    public void play(){
        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            createRandomCircle();

        });
        KeyFrame k2 = new KeyFrame(Duration.millis(1000), a ->{

        });

        spawn = new Timeline();
        spawn.setAutoReverse(false);
        spawn.setCycleCount(1);
        spawn.getKeyFrames().addAll(k1,k2);
        spawn.playFromStart();
        spawn.setOnFinished(e -> {
            anzahl.setValue(++index);
        });


        anzahl.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if(newValue.intValue() < 2){
                    spawn.playFromStart();


                }
                if(newValue.intValue() == 2){
                    anzahl.setValue(0);
                    index = 0;
                }
            }
        });


    }

    public void stop(){
        spawn.stop();

    }

    private int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    private Point getKoordinaten(){
        boolean nebeneinander = true;
        int x = 0;
        int y = 0;
        while(nebeneinander){
            x =randomWithRange(0,9);
            y = randomWithRange(0,4);
            if(hatNachbarOderGleich(x,y) == false){
                nebeneinander = false;
            }
        }



        return new Point(x,y);

    }

    private boolean hatNachbarOderGleich(int x, int y){
/* x+1 <= 9 && raster[x+1][y].getChildren().isEmpty() &&
                x-1 >= 0 && raster[x-1][y].getChildren().isEmpty() &&
                y+1 <= 4 && raster[x][y+1].getChildren().isEmpty() &&
                y-1 >= 0 && raster[x][y-1].getChildren().isEmpty() &&
                */


        //falls kein kreis nebenan oder auf dem selben feld ist
        if(x+1 <= 9 && raster[x+1][y].getChildren().isEmpty() &&
                x-1 >= 0 && raster[x-1][y].getChildren().isEmpty() &&
                y+1 <= 4 && raster[x][y+1].getChildren().isEmpty() &&
                y-1 >= 0 && raster[x][y-1].getChildren().isEmpty() &&
                raster[x][y].getChildren().isEmpty()){

            return false;

        } else{
            return true;

        }

    }

    private void removeFromGrid(int x, int y){
        raster[x][y].getChildren().remove(0);
    }

    private HBox getFromGrid(int x, int y){
        return raster[x][y];
    }






}
