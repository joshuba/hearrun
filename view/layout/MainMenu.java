package hearrun.view.layout;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import hearrun.business.Spieler;
import hearrun.view.controller.ViewController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
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


    public MainMenu(SpielController spielController, ViewController viewController) {
        this.spielController = spielController;
        this.viewController = viewController;
        continueAn = false;
        this.setId("mainMenu");
        this.setMinHeight(700);
        this.setMinWidth(300);
        this.setAlignment(Pos.CENTER);

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

        //Baue neues Menü auf
        BorderPane menuContainer = new BorderPane();
        HBox links = new HBox();
        HBox rechts = new HBox();
        Label spielfeldText = new Label("Waehle eine Karte: ");
        Label spielerText = new Label("Waehle Spieler: ");
        ListView <Map> maps = new ListView<>();
        ListView <String> spieler = new ListView<>();
        ArrayList <Spieler> spielerliste = new ArrayList<>();
        ObservableList <String> spielerObs = FXCollections.observableArrayList();
        Button addSpieler = new Button("Add");
        Button removeSpieler = new Button("Remove");
        Button back = new Button("Back");
        Button start = new Button("Start");

        // maps initialisieren
        maps.setItems(leseMapsEin());
        maps.getSelectionModel().select(0);

        spieler.setItems(spielerObs);
        spieler.setCellFactory(TextFieldListCell.forListView());
        spieler.setEditable(true);


        menuContainer.setLeft(links);
        menuContainer.setRight(rechts);

        menuContainer.setPadding(new Insets(40));

        links.getChildren().addAll(spielfeldText, maps);
        rechts.getChildren().addAll(spielerText, spieler, new VBox(addSpieler, removeSpieler));

        this.getChildren().addAll(menuContainer, back, start);


        back.setOnAction((e) -> mainMenuWindow());

        spieler.setOnEditCommit((t) -> {
            spielerObs.set(t.getIndex(), t.getNewValue());
            spielerliste.get(t.getIndex()).setName(t.getNewValue());
        });

        spielerObs.addListener((ListChangeListener<String>) c -> {
            if (spielerObs.size() > 3)
                addSpieler.setDisable(true);
        });

        spieler.getSelectionModel().getSelectedItems().addListener((ListChangeListener<String>) c -> {
            if (spieler.getSelectionModel().getSelectedItems().size() > 0)
                removeSpieler.setDisable(false);
            else
                removeSpieler.setDisable(true);
        });

        start.setOnAction((e) -> {
            spielController.setMap(maps.getSelectionModel().getSelectedItem());
            spielController.setSpieler(spielerliste);
            spielController.getLayout().resetGameLayout();
            spielController.starteSpiel();
        });

        final Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        actionEvent -> spieler.edit(spieler.getItems().size() - 1)));
        animation.setCycleCount(1);


        addSpieler.setOnAction((e) -> {
            Spieler neuerSpieler = new Spieler("Spieler " + (spielerliste.size() + 1));
            spielerliste.add(neuerSpieler);
            spielerObs.add(neuerSpieler.toString());
            animation.play();

        });

        removeSpieler.setDisable(true);

        removeSpieler.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE)
                removeSpieler.fire();
        });

        removeSpieler.setOnAction(e -> {
            spielerliste.remove(spieler.getSelectionModel().getSelectedIndex());
            spielerObs.remove(spieler.getSelectionModel().getSelectedIndex());
        });

        addSpieler.fire();
    }

    public void removeAllElements() {
        this.getChildren().removeAll(this.getChildren());
    }

    private ObservableList<Map> leseMapsEin() {

        File mapsFile = new File(Main.class.getResource("/hearrun/resources/Data").getPath());

        File[] maps = mapsFile.listFiles((dir, name) -> name.startsWith("map"));

        ObservableList <Map> mapsList = FXCollections.observableArrayList();

        for (File f : maps)
            mapsList.add(new Map(f.getPath(), spielController.getStage().widthProperty(), spielController.getStage().heightProperty()));
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
        antwortZeit.setMaxWidth(300);
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
            try {
                String newpath = new DirectoryChooser().showDialog(spielController.getLayout().getViewController().getStage()).getAbsolutePath();
                pfad.setText(newpath);
                spielController.getProperties().setProperty("musicPath", newpath);
                spielController.ladeMusik();
            } catch (NullPointerException ignored){}


        });
    }

    public void newGameAnAus(boolean anAus) {
        this.newGame.setDisable(!anAus);
    }

    public static void main (String[] args){
        System.out.println(Main.class.getResource("/hearrun/resources/Data").getPath());

    }
}
