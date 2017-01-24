package hearrun.view.layout;

import hearrun.business.SpielController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by Josh on 23.01.17
 */
public class LoadingScreen extends VBox{
    private Label text;
    private ProgressBar bar;
    private SpielController spielController;
    private SimpleFloatProperty progres;
    private SimpleBooleanProperty ladeMusik;
    private Button reset;
    private Label ueberschrift;
    private Label fragenAnzahl;



    public LoadingScreen(SimpleBooleanProperty ladeMusik, SimpleFloatProperty progres, SimpleIntegerProperty anzahlFragenProp, SpielController spielController){
        this.setId("loadingScreen");
        this.spielController = spielController;
        this.progres = progres;
        this.ladeMusik = ladeMusik;
        this.fragenAnzahl = new Label("Verzeichnis einlesen...");
        bar = new ProgressBar();
        reset = new Button("reset");
        ueberschrift = new Label("Musik einlesen");
        ueberschrift.setId("grossText");


        text = new Label("Deine Musik wird eingelesen, dies kann abhängig von der Mediathekgröße einige Sekunden in Anspruch nehmen. \nFragen werden erstellt...");
        this.getChildren().addAll(ueberschrift, text,bar, fragenAnzahl,reset);
        bar.progressProperty().bind(progres);

        reset.setOnAction((e)-> spielController.resetMusicPathPropertie());

        ladeMusik.addListener((observable, oldValue, newValue) -> {
            if(newValue){
                zeigeFertig();
            }
        });

        anzahlFragenProp.addListener((observable, oldValue, newValue)
                -> Platform.runLater(() ->fragenAnzahl.setText(newValue + " Fragen generiert")));

        //styling
        this.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(40,0,30,0));
        bar.setMinWidth(600);
        ueberschrift.setPadding(new Insets(0,0,100,0));
    }

    private void zeigeFertig() {
        KeyFrame k1 = new KeyFrame(Duration.millis(1000), a -> fragenAnzahl.setText("Abgeschlossen!"));

        Timeline done = new Timeline();
        done.setAutoReverse(false);
        done.setCycleCount(2);
        done.getKeyFrames().addAll(k1);
        done.setOnFinished(a -> spielController.getLayout().getViewController().setMainMenu());
        done.play();


    }
}
