package hearrun.view.layout;

import hearrun.business.Spiel;
import hearrun.business.SpielController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

/**
 * Created by Josh on 23.01.17.
 */
public class LoadingScreen extends VBox{
    private Label text;
    private ProgressBar bar;
    private SpielController spielController;
    private SimpleFloatProperty progres;
    private SimpleBooleanProperty ladeMusik;
    private Button reset;
    private Label ueberschrift;



    public LoadingScreen(SimpleBooleanProperty ladeMusik, SimpleFloatProperty progres, SpielController spielController){
        this.setId("loadingScreen");
        this.spielController = spielController;
        this.progres = progres;
        this.ladeMusik = ladeMusik;
        bar = new ProgressBar();
        reset = new Button("reset");
        ueberschrift = new Label("Musik einlesen");
        ueberschrift.setId("grossText");


        text = new Label("Deine Musik wird eingelesen, dies kann abhängig von der Mediathekgröße einige Sekunden in Anspruch nehmen. \nFragen werden erstellt...");
        this.getChildren().addAll(ueberschrift, text,bar, reset);
        bar.progressProperty().bind(progres);

        reset.setOnAction((e)-> spielController.resetMusicPathPropertie());

        ladeMusik.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue==true){
                    zeigeFertig();
                }

            }
        });

        //styling
        this.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(40,0,30,0));
        bar.setMinWidth(600);
        ueberschrift.setPadding(new Insets(0,0,100,0));





    }

    private void zeigeFertig() {

        KeyFrame k1 = new KeyFrame(Duration.millis(1000), a ->{
            text.setText("DONE!");


        });

        Timeline done = new Timeline();
        done.setAutoReverse(false);
        done.setCycleCount(2);
        done.getKeyFrames().addAll(k1);
        done.setOnFinished(a -> spielController.getLayout().getViewController().setMainMenu());
        done.play();


    }
}
