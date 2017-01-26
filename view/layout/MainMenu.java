package hearrun.view.layout;

import hearrun.business.Main;
import hearrun.business.SpielController;
import hearrun.business.Spieler;
import hearrun.view.controller.ViewController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by joshuabarth on 14.01.17
 */
public class MainMenu extends VBox {
    private Button cont;
    private Button newGame;
    private Button settings;
    private Button exit;
    private boolean continueAn;
    private SpielController spielController;
    private ViewController viewController;
    private int spielerAnzahl;


    public MainMenu(SpielController spielController, ViewController viewController) {
        this.spielController = spielController;
        this.viewController = viewController;
        continueAn = false;
        this.setId("mainMenu");
        this.setMinHeight(700);
        this.setMinWidth(300);
        this.setAlignment(Pos.CENTER);
        this.spielerAnzahl = 0;

        mainMenuWindow();

    }


    public void activateContinue() {
        //Falls noch kein Spiel erstellt wurde wird ein Continue Button angezeigt, der bleibt
        this.getChildren().removeAll(this.getChildren());
        this.getChildren().addAll(cont, newGame, settings, exit);
        continueAn = true;


    }

    public void deactivateContinue() {
        this.getChildren().removeAll(cont, newGame, settings, exit);
        this.getChildren().addAll(newGame, settings, exit);
    }

    public void newGameWindow() {
        //Entferne Hauptmenü
        removeAllElements();
        spielController.getLayout().resetGameLayout();


        //Baue neues Menü auf
        BorderPane menuContainer = new BorderPane();
        HBox links = new HBox();
        HBox rechts = new HBox();
        Label spielfeldText = new Label("Waehle eine Karte: ");
        Label spielerText = new Label("Waehle Spieler: ");
        ListView <Map> maps = new ListView<>();
        ListView<String> spieler = new ListView<>();
        ArrayList <Spieler> spielerliste = new ArrayList<>();
        ObservableList <String> spielerObs = FXCollections.observableArrayList();
        Button addSpieler = new Button("Add");
        Button back = new Button("Back");
        Button start = new Button("Start");

        // maps initialisieren
        maps.setItems(leseMapsEin());
        maps.getSelectionModel().select(0);

        spieler.setItems(spielerObs);
        spieler.setCellFactory(TextFieldListCell.forListView());
        spieler.setEditable(true);
        spieler.setOnEditCommit((t) -> {
            spielerObs.set(t.getIndex(), t.getNewValue());
            spielerliste.get(t.getIndex()).setName(t.getNewValue());
        });

        menuContainer.setLeft(links);
        menuContainer.setRight(rechts);

        menuContainer.setPadding(new Insets(40));

        links.getChildren().addAll(spielfeldText, maps);
        rechts.getChildren().addAll(spielerText, spieler, addSpieler);

        this.getChildren().addAll(menuContainer, back, start);


        back.setOnAction((e) -> mainMenuWindow());
        start.setOnAction((e) -> {
            spielController.setMap(maps.getSelectionModel().getSelectedItem());
            spielController.setSpieler(spielerliste);
            spielController.starteSpiel();
        });
        addSpieler.setOnAction((e) -> {
            spielerAnzahl++;
            Spieler neuerSpieler = new Spieler(spielerAnzahl, "Spieler " + spielerAnzahl);
            spielerliste.add(neuerSpieler);
            spielerObs.add(neuerSpieler.toString());
        });
    }

    public void removeAllElements() {
        this.getChildren().removeAll(this.getChildren());
    }

    private ObservableList<Map> leseMapsEin() {

        File mapsFile = new File(Main.class.getResource("/hearrun/resources/Data").getPath());

        File[] maps = mapsFile.listFiles((dir, name) -> name.startsWith("map"));

        ObservableList <Map> mapsList = FXCollections.observableArrayList();

        for (File f : maps)
            mapsList.add(new Map(f.getPath(), viewController));
        return mapsList;

    }

    public void mainMenuWindow() {

        //Entferne new GameWindow falls es existiert
        removeAllElements();

        newGame = new Button("New Game");
        cont = new Button("Continue");
        settings = new Button("Settings");
        exit = new Button("Exit Game");

        this.getChildren().addAll(newGame, settings, exit);

        newGame.setOnAction((e) -> newGameWindow());
        exit.setOnAction((e) -> spielController.beendeProgramm());
        cont.setOnAction((e) -> spielController.getLayout().setGameLayout());
        settings.setOnAction((e) -> settingsWindow());


    }

    public void settingsWindow() {
        this.getChildren().removeAll(this.getChildren());
        Slider antwortZeit = new Slider(4, 15, Integer.valueOf(spielController.getProperties().getProperty("antwortZeit")));
        antwortZeit.setBlockIncrement(12);
        antwortZeit.valueProperty().addListener((obs, oldValue, newValue) -> {
            spielController.getProperties().setProperty("antwortZeit", String.valueOf(newValue.intValue()));
            System.out.println(spielController.getProperties().getProperty("antwortZeit"));
        });


        Slider volume = new Slider();

        Button button = new Button("Change Music Path");
        Label pfad = new Label(spielController.getProperties().getProperty("musicPath"));
        Button back = new Button("Back");
        this.getChildren().addAll(antwortZeit, volume, button, pfad, back);
        back.setOnAction((e) -> spielController.getLayout().getViewController().setMainMenu());


        button.setOnAction((e) -> {
            String newpath = new DirectoryChooser().showDialog(spielController.getLayout().getViewController().getStage()).getAbsolutePath();
            pfad.setText(newpath);
            spielController.getProperties().setProperty("musicPath", newpath);

            spielController.beendeSpiel();
            spielController.ladeMusik();
        });
    }

    public void newGameAnAus(boolean anAus) {
        this.newGame.setDisable(!anAus);
    }

    public static void main (String[] args){
        System.out.println(Main.class.getResource("/hearrun/resources/Data").getPath());

    }
}
