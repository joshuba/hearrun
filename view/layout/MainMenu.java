package hearrun.view.layout;

import hearrun.Main;
import hearrun.controller.SpielController;
import hearrun.model.Spieler;
import hearrun.controller.ViewController;
import javafx.animation.*;
import javafx.beans.property.SimpleIntegerProperty;
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

/**Im Main Menu werden die verschiedenen Untermenues immer erst initalisiert und beim wechsel durch
 * die Animationsmethode gejagt, in der die alten Panes entfernt und die neuen hinzugefügt werden
 *
 * @author Leo Back & Joshua Barth
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
    private HBox logo;


    public MainMenu(SpielController spielController, ViewController viewController) {
        mainMenuElements = new VBox();
        settingsElements = new VBox();
        settingsElements.setSpacing(15);
        newGameElements = new VBox();
        newGameElements.setSpacing(15);
        helpElements = new VBox();
        helpElements.setSpacing(15);

        mainMenuElements.setAlignment(Pos.CENTER);
        p = new PictureGalery("/hearrun/resources/manual/");
        this.setId("mainMenu");
        this.spielController = spielController;
        this.viewController = viewController;
        continueAn = false;
        //mainMenuElements.setId("mainMenu");
        this.setMinHeight(700);
        this.setMinWidth(300);

        this.setAlignment(Pos.CENTER);
        circleSpawner = new CircleSpawner(spielController.getStage());
        circleSpawner.setOpacity(60);

        this.getChildren().addAll(circleSpawner);
        kreisSpawningAnAus(true);


    }

    /**
     * Elemente fuer das "Neues Spiel" Menue
     */
    public void initNewGameWindow() {
        //Baue neues Menü auf
        HBox menuContainer = new HBox();
        VBox links = new VBox();
        VBox rechts = new VBox();

        Label ueberschrift = new Label("Neues Spiel");
        ueberschrift.setId("grossText");
        ueberschrift.setPadding(new Insets(0,0,40,0));

        Label spielfeldText = new Label("Wähle eine Karte: ");
        spielfeldText.setId("schriftMittel");
        Label spielerText = new Label("Wähle Spieler: ");
        spielerText.setId("schriftMittel");
        ListView<Map> maps = new ListView<>();
        ListView<String> spieler = new ListView<>();
        ArrayList<Spieler> spielerliste = new ArrayList<>();
        ObservableList<String> spielerObs = FXCollections.observableArrayList();
        Button addSpieler = new Button("+");
        Button removeSpieler = new Button("-");
        Button back = new Button("zurück");
        Button start = new Button("Starte Spiel");

        addSpieler.setId("plus-minus-button");
        removeSpieler.setId("plus-minus-button");

        start.setId("buttonGreenHover");

        // maps initialisieren
        maps.setItems(leseMapsEin());
        maps.getSelectionModel().select(0);

        spieler.setItems(spielerObs);
        spieler.setCellFactory(TextFieldListCell.forListView());
        spieler.setEditable(true);

        maps.getStyleClass().add("maps-spieler-liste");
        spieler.getStyleClass().add("maps-spieler-liste");

        menuContainer.getChildren().addAll(links, rechts);

        menuContainer.setPadding(new Insets(40));
        menuContainer.setSpacing(40);
        menuContainer.setAlignment(Pos.BASELINE_CENTER);
        menuContainer.getStyleClass().add("text-middle");

        links.getChildren().addAll(spielfeldText, maps);
        rechts.getChildren().addAll(spielerText, new HBox(spieler, new VBox(addSpieler, removeSpieler)));

        newGameElements.getChildren().addAll(ueberschrift, menuContainer, back, start);
        newGameElements.setAlignment(Pos.CENTER);


        back.setOnAction((e) -> {
            initMainMenuWindow();
            menuUebergang(newGameElements, mainMenuElements, false);
        });

        spieler.setOnEditCommit((t) -> {
            spielerObs.set(t.getIndex(), t.getNewValue());
            String nameS = t.getNewValue();
            if(t.getNewValue().length() >= 12){
                nameS = t.getNewValue().substring(0,14);
                nameS += " ...";
            }
            spielerliste.get(t.getIndex()).setName(nameS);
        });

        spielerObs.addListener((ListChangeListener<String>) c -> {
            if (spielerObs.size() > 3)
                addSpieler.setDisable(true);
            else if (spielerObs.size() < 4)
                addSpieler.setDisable(false);
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
        ObservableList<Map> mapsList = FXCollections.observableArrayList();

        int i = 1;
        while (true) {
            try {
                String path = new File((Main.getFilePathFromResourcePath("/hearrun/resources/Data/map" + i + ".txt", true))).getAbsolutePath();
                mapsList.add(new Map(path, spielController.getStage().widthProperty(), spielController.getStage().widthProperty()));
            } catch (NullPointerException e){
                break;
            }
            i++;
        }

        return mapsList;

    }
    /**
     * Zentrale Methode, von aussen aufrufbar. Ruft das Obermenü auf
     * Wenn ein spiel laeuft wird ein "Fortsetzen" Button angezeigt
     */
    public void showMainMenu() {
        //Falls ein spiel erstellt wurde schalte continue an
        if (spielController.getAktSpiel() != null) {
            continueAn = true;
        }
        initMainMenuWindow();


        getChildren().clear();
        getChildren().add(circleSpawner);
        this.getChildren().addAll(mainMenuElements);

        mainMenuElements.setOpacity(1);


    }


    private void initMainMenuWindow() {
        mainMenuElements.getChildren().clear();
        newGame = new Button("Neues Spiel");
        newGame.setId("buttonGreenHover");
        cont = new Button("Fortfahren");
        settings = new Button("Einstellungen");
        help = new Button("Hilfe");
        exit = new Button("Spiel beenden");
        exit.setId("buttonRedHover");
        //Baue neues Menü auf
        logo = new HBox();
        logo.setId("hearRunLogo");
        logo.setMaxHeight(150);
        logo.setMinHeight(150);
        logo.setAlignment(Pos.TOP_CENTER);
        VBox buttons = new VBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(60,0,0,0));
        buttons.setSpacing(15);
        mainMenuElements.getChildren().addAll(logo, buttons);



        if (continueAn) {
            buttons.getChildren().removeAll(buttons.getChildren());
            buttons.getChildren().addAll(cont, newGame, help, settings, exit);

        } else {
            if (buttons.getChildren().size() == 0)
                buttons.getChildren().addAll(newGame, help, settings, exit);
        }

        newGame.setOnAction((e) -> {
            initNewGameWindow();
            menuUebergang(mainMenuElements, newGameElements, true);
        });
        exit.setOnAction((e) -> spielController.beendeProgramm());
        cont.setOnAction((e) -> {

            mainMenuElements.getChildren().removeAll(mainMenuElements.getChildren());

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
        Label ueberschrift = new Label("Einstellungen");
        ueberschrift.setId("grossText");
        ueberschrift.setPadding(new Insets(0,0,40,0));

        HBox antwortBox = new HBox();
        Label aktZeit = new Label();
        aktZeit.setId("schriftMittel");
        Label zeitTitel = new Label("Antwortzeit");
        zeitTitel.setId("schriftMittel");

        Label pfadTitel = new Label("Aktueller Musikpfad");
        pfadTitel.setId("schriftMittel");
        HBox pfadBox = new HBox();

        zeitTitel.setPadding(new Insets(0,0,10,0));
        SimpleIntegerProperty time = new SimpleIntegerProperty();


        Slider antwortZeit = new Slider(4, 15, Integer.valueOf(spielController.getProperties().getProperty("antwortZeit")));
        antwortZeit.setBlockIncrement(12);
        antwortZeit.setMaxWidth(300);
        antwortZeit.setMinWidth(300);

        antwortZeit.valueProperty().addListener((obs, oldValue, newValue) -> {
            spielController.getProperties().setProperty("antwortZeit", String.valueOf(newValue.intValue()));
        });
        antwortBox.setAlignment(Pos.CENTER);
        antwortBox.setSpacing(10);
        antwortBox.getChildren().addAll(antwortZeit, aktZeit);
        time.bind(antwortZeit.valueProperty());
        aktZeit.textProperty().bind(time.asString().concat(" s"));


        Button button = new Button("ändern");
        Label pfad = new Label(spielController.getProperties().getProperty("musicPath"));
        button.setId("buttonRedHover");
        pfadBox.getChildren().addAll(pfad, button);
        pfadBox.setSpacing(10);
        pfadBox.setAlignment(Pos.CENTER);

        Button back = new Button("zurück");
        settingsElements.setAlignment(Pos.CENTER);
        back.setOnAction((e) -> {
            initMainMenuWindow();
            menuUebergang(settingsElements, mainMenuElements, false);
        });


        button.setOnAction((e) -> {
            try {
                spielController.getLoopPlayer().fadeOut();
                DirectoryChooser dc = new DirectoryChooser();
                dc.setInitialDirectory(new File(spielController.getProperties().get("musicPath").toString()));
                String newpath = dc.showDialog(spielController.getLayout().getViewController().getStage()).getAbsolutePath();
                pfad.setText(newpath);
                spielController.getProperties().setProperty("musicPath", newpath);
                settingsElements.getChildren().clear();
                this.getChildren().clear();
                spielController.ladeMusik();
            } catch (NullPointerException ignored) {}
        });
        settingsElements.getChildren().addAll(ueberschrift, zeitTitel, antwortBox, pfadTitel, pfadBox, back);

    }

    private void initHelpMenu() {
        Label text = new Label("Hilfe");
        text.setId("grossText");
        Button button = new Button("Zurück");
        helpElements.getChildren().addAll(text, p, button);
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



    public void kreisSpawningAnAus(boolean anAus) {
        if (anAus) {
            circleSpawner.play();
        } else {
            circleSpawner.stop();
        }

    }
    /**
     * Beim wechsel in ein anderes Menue gibt es entweder eine aufsteigende oder absteigende Animation
     */
    private void menuUebergang(VBox von, VBox zu, boolean vorwaerts) {
        int zoomVon = 0;
        int zoomZu = 1;
        if (!vorwaerts) {
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


        KeyFrame k1 = new KeyFrame(Duration.millis(1), a -> {
            ft.play();

        });

        KeyFrame k2 = new KeyFrame(Duration.millis(50), a -> {
            zu.setOpacity(1);

        });

        KeyFrame k3 = new KeyFrame(Duration.millis(1), a -> {
            st.play();
        });


        Timeline t = new Timeline();
        t.setAutoReverse(false);
        t.setCycleCount(1);
        t.getKeyFrames().addAll(k1, k2, k3);
        t.play();


        ft.setOnFinished(e -> {
            this.getChildren().remove(von);
            von.getChildren().removeAll(von.getChildren());
        });



    }
    public void setContinue(boolean anAus){
        if(anAus){
            continueAn = true;
        }else{
            continueAn = false;
        }
    }


}
