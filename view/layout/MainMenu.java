package hearrun.view.layout;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import hearrun.business.Spieler;
import hearrun.view.controller.ViewController;
import javafx.animation.*;
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
public class MainMenu extends StackPane {
    private Button cont;
    private Button newGame;
    private Button settings;
    private Button exit;
    private Button help;
    private boolean continueAn;
    private SpielController spielController;
    private ViewController viewController;
    private VBox mainMenuElements;
    private VBox settingsElements;
    private VBox newGameElements;
    private VBox helpElements;
    private CircleSpawner circleSpawner;
    private PictureGalery p;



    public MainMenu(SpielController spielController, ViewController viewController) {
        mainMenuElements = new VBox();
        mainMenuElements.setSpacing(15);
        settingsElements = new VBox();
        settingsElements.setSpacing(15);
        newGameElements = new VBox();
        newGameElements.setSpacing(15);
        helpElements = new VBox();
        helpElements.setSpacing(15);

        mainMenuElements.setAlignment(Pos.CENTER);
        p = new PictureGalery(Main.class.getResource("/hearrun/resources/manual/").getPath());
        this.setId("mainMenu");
        this.spielController = spielController;
        this.viewController = viewController;
        continueAn = false;
        //mainMenuElements.setId("mainMenu");
        this.setMinHeight(700);
        this.setMinWidth(300);
        this.setAlignment(Pos.CENTER);
        circleSpawner = new CircleSpawner(spielController.getStage());

        this.getChildren().addAll(circleSpawner);
        kreisSpawningAnAus(true);




    }



    public void initNewGameWindow() {


        //Baue neues Menü auf
        BorderPane menuContainer = new BorderPane();
        HBox links = new HBox();
        HBox rechts = new HBox();
        Label spielfeldText = new Label("Waehle eine Karte: ");
        Label spielerText = new Label("Wähle Spieler: ");
        ListView <Map> maps = new ListView<>();
        ListView <String> spieler = new ListView<>();
        ArrayList <Spieler> spielerliste = new ArrayList<>();
        ObservableList <String> spielerObs = FXCollections.observableArrayList();
        Button addSpieler = new Button("+");
        Button removeSpieler = new Button("-");
        Button back = new Button("zurück");
        Button start = new Button("Starte Spiel");

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

        newGameElements.getChildren().addAll(menuContainer, back, start);
        newGameElements.setAlignment(Pos.CENTER);


        back.setOnAction((e) -> {
            initMainMenuWindow();
            menuUebergang(newGameElements, mainMenuElements, false);
        });

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
            newGameElements.getChildren().removeAll(newGameElements.getChildren());
            this.getChildren().removeAll(newGameElements);

        });

        final Timeline animation = new Timeline(
                new KeyFrame(Duration.seconds(0.1),
                        actionEvent -> spieler.edit(spieler.getItems().size() - 1)));
        animation.setCycleCount(1);


        addSpieler.setOnAction((e) -> {
            Spieler neuerSpieler = new Spieler("Spieler " + (spielerliste.size() + 1));
            spielerliste.add(neuerSpieler);
            neuerSpieler.setNr(spielerliste.indexOf(neuerSpieler));
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


    private ObservableList<Map> leseMapsEin() {

        File mapsFile = new File(Main.class.getResource("/hearrun/resources/Data").getPath());

        File[] maps = mapsFile.listFiles((dir, name) -> name.startsWith("map"));

        ObservableList <Map> mapsList = FXCollections.observableArrayList();

        for (File f : maps)
            mapsList.add(new Map(f.getPath(), spielController.getStage().widthProperty(), spielController.getStage().heightProperty()));
        return mapsList;

    }

    public void showMainMenu(){
        //Falls ein spiel erstellt wurde schalte continue an
        if(spielController.getAktSpiel() != null){
            continueAn = true;
        }
            initMainMenuWindow();
            this.getChildren().addAll(mainMenuElements);

        mainMenuElements.setOpacity(1);



    }


    private void initMainMenuWindow() {
        newGame = new Button("Neues Spiel");
        cont = new Button("Fortfahren");
        settings = new Button("Einstellungen");
        help = new Button("Hilfe");
        exit = new Button("Spiel beenden");
        exit.setId("buttonRedHover");

        if(continueAn){
            mainMenuElements.getChildren().removeAll(mainMenuElements.getChildren());
            mainMenuElements.getChildren().addAll(cont, newGame, help, settings, exit);

        }else{
            mainMenuElements.getChildren().addAll(newGame, help, settings, exit);
        }

        newGame.setOnAction((e) -> {
            initNewGameWindow();
            menuUebergang(mainMenuElements, newGameElements, true);
        });
        exit.setOnAction((e) -> spielController.beendeProgramm());
        cont.setOnAction((e) -> {
            spielController.getLayout().setGameLayout();
            this.getChildren().removeAll(mainMenuElements);

        });
        settings.setOnAction(e -> {
                ititSettingsWindow();
                menuUebergang(mainMenuElements, settingsElements, true);

            });
        help.setOnAction(e -> {
            initHelpMenu();
            menuUebergang(mainMenuElements, helpElements, true);

        });

    }

    private void ititSettingsWindow() {
        Slider antwortZeit = new Slider(4, 15, Integer.valueOf(spielController.getProperties().getProperty("antwortZeit")));
        antwortZeit.setBlockIncrement(12);
        antwortZeit.setMaxWidth(300);
        antwortZeit.valueProperty().addListener((obs, oldValue, newValue) -> {
            spielController.getProperties().setProperty("antwortZeit", String.valueOf(newValue.intValue()));
            System.out.println(spielController.getProperties().getProperty("antwortZeit"));
        });

        Slider volume = new Slider();

        Button button = new Button("Musikpfad ändern");
        Label pfad = new Label(spielController.getProperties().getProperty("musicPath"));
        Button back = new Button("zurück");
        settingsElements.getChildren().addAll(antwortZeit, volume, button, pfad, back);
        settingsElements.setAlignment(Pos.CENTER);
        back.setOnAction((e) -> {
            initMainMenuWindow();
            menuUebergang(settingsElements, mainMenuElements, false);
        });


        button.setOnAction((e) -> {
            try {
                String newpath = new DirectoryChooser().showDialog(spielController.getLayout().getViewController().getStage()).getAbsolutePath();
                pfad.setText(newpath);
                spielController.getProperties().setProperty("musicPath", newpath);
                settingsElements.getChildren().clear();
                this.getChildren().clear();
                spielController.ladeMusik();
            } catch (NullPointerException ignored){}


        });

    }

    private void initHelpMenu(){
        Button button = new Button("Zurück");
        helpElements.getChildren().addAll(p, button);
        helpElements.setAlignment(Pos.CENTER);
        p.minHeightProperty().bind(viewController.getStage().heightProperty().subtract(400));
        p.maxHeightProperty().bind(viewController.getStage().heightProperty().subtract(400));

        button.setOnAction((e) -> {
            initMainMenuWindow();
            menuUebergang(helpElements, mainMenuElements, false);
        });


    }


    public void newGameAnAus(boolean anAus) {
        this.newGame.setDisable(!anAus);
    }

    public static void main (String[] args){
        System.out.println(Main.class.getResource("/hearrun/resources/Data").getPath());

    }

    public void kreisSpawningAnAus(boolean anAus){
        if(anAus){
            circleSpawner.play();
        }else{
            circleSpawner.stop();
        }

    }

    private void menuUebergang(VBox von, VBox zu, boolean vorwaerts){
        int zoomVon = 0;
        int zoomZu = 1;
        if(!vorwaerts){
            zoomVon = 4;
            zoomZu = 1;
        }


        this.getChildren().addAll(zu);
        zu.setOpacity(0);

        FadeTransition ft = new FadeTransition(Duration.millis(100), von);
        ft.setFromValue(1.0f);
        ft.setToValue(0.0);

        ScaleTransition st = new ScaleTransition(Duration.millis(250), zu);
        st.setFromX(zoomVon);
        st.setFromY(zoomVon);
        st.setToX(zoomZu);
        st.setToY(zoomZu);


        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            ft.play();

        });

        KeyFrame k2 = new KeyFrame(Duration.millis(50), a ->{
            zu.setOpacity(1);

        });

        KeyFrame k3 = new KeyFrame(Duration.millis(1), a ->{
            st.play();
        });


        Timeline t = new Timeline();
        t.setAutoReverse(false);
        t.setCycleCount(1);
        t.getKeyFrames().addAll(k1,k2,k3);
        t.play();



        ft.setOnFinished(e -> {
            this.getChildren().remove(von);
            von.getChildren().removeAll(von.getChildren());
        });


/*
        this.getChildren().addAll(zu);
        TranslateTransition t = new TranslateTransition(Duration.millis(250), von);
        t.setToY(von.getTranslateY() - 400);

        TranslateTransition t1 = new TranslateTransition(Duration.millis(500), zu);
        t1.setToY(von.getTranslateY());

        t.play();
        t1.play();


        t.setOnFinished(e -> {
            this.getChildren().remove(von);
            von.getChildren().removeAll(von.getChildren());
        });
        */

    }


}
