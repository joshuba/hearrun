package hearrun.view.layout;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * Created by Josh on 23.01.17.
 */
public class IntroScreen extends VBox {
    private DirectoryChooser auswahl;
    private Button auswahlButton;
    private Label ueberschrift;
    private Label path;
    private SpielController spielController;
    private Stage stage;
    private Button start;
    private PictureGalery p;
    private boolean pfadchosen;
    private HBox pfadchoser;

    public IntroScreen(Stage stage, SpielController spielController) {
        pfadchosen = false;
        this.spielController = spielController;
        this.stage = stage;
        this.setId("introScreen");
        p = new PictureGalery("/hearrun/resources/manual/");
        auswahlButton = new Button("Wähle Pfad");
        path = new Label();
        path.setStyle("-fx-text-fill: white");
        ueberschrift = new Label("Einführung");
        ueberschrift.setStyle("-fx-text-alignment: center; -fx-font-size: 35; -fx-text-fill: white");


        pfadchoser = new HBox();
        pfadchoser.getChildren().addAll(path, auswahlButton);

        this.getChildren().addAll(ueberschrift, p,  pfadchoser);

        //styling
        ueberschrift.setPadding(new Insets(0, 0, 30, 0));
        this.setAlignment(Pos.CENTER);
        auswahlButton.setPadding(new Insets(20, 0, 20, 0));
        auswahlButton.setId("");
        pfadchoser.setAlignment(Pos.CENTER);
        pfadchoser.setSpacing(30);

        auswahlButton.setOnAction((e) -> waehlePfad());
        p.minHeightProperty().bind(stage.heightProperty().subtract(400));
        p.maxHeightProperty().bind(stage.heightProperty().subtract(400));


    }

    public void waehlePfad() {

        String pfad = new DirectoryChooser().showDialog(stage).getAbsolutePath();
        path.setText(pfad);
        start = new Button("Laden & Starten");
        start.setId("buttonGreenHover");
        if(pfadchosen == false){
            this.getChildren().addAll(start);
            pfadchosen = true;
        }


        start.setOnAction(e -> {
            spielController.getProperties().setProperty("musicPath", pfad);
            spielController.ladeMusik();
        });
    }
}
