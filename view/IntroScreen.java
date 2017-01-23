package hearrun.view;

import hearrun.business.Spiel;
import hearrun.business.SpielController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by Josh on 23.01.17.
 */
public class IntroScreen extends VBox{
    private Label text;
    private DirectoryChooser auswahl;
    private Button auswahlButton;
    private Label path;
    private VBox container;
    private SpielController spielController;
    private Stage stage;
    private Button start;

    public IntroScreen(Stage stage, SpielController spielController){
        this.spielController = spielController;
        this.stage = stage;
        text =new Label("Wilkommen! Um HearRun spielen zu können, gib bitte den Pfad zu deiner Musik an, die du im Spiel verwenden willst");
        auswahlButton = new Button("Waehle Pfad");
        path = new Label();

        container = new VBox();

        this.getChildren().addAll(text, auswahlButton, path);

        //styling
        this.setId("introScreen");
        this.setAlignment(Pos.CENTER);
        auswahlButton.setPadding(new Insets(20,0,20,0));

        auswahlButton.setOnAction((e)-> waehlePfad());




    }

    public void waehlePfad(){
        String pfad = new DirectoryChooser().showDialog(stage).getAbsolutePath();
        path.setText(pfad);
        start = new Button("Laden & Starten");
        this.getChildren().addAll(start);
        spielController.getProperties().setProperty("musicPath", pfad);

        start.setOnAction((e) -> {
            spielController.ladeMusik();

        });
    }
}
