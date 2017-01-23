package hearrun.view.layout;

import hearrun.business.SpielController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.util.List;

/**
 * Created by joshuabarth on 14.01.17.
 */
public class MainMenu extends VBox{
    private Button continu;
    private Button newGame;
    private Button settings;
    private Button exit;
    private boolean continueAn;
    private SpielController spielController;



    public MainMenu(SpielController spielController){
        this.spielController = spielController;
        continueAn = false;
        this.setId("mainMenu");
        this.setMinHeight(700);
        this.setMinWidth(300);
        this.setAlignment(Pos.CENTER);

        mainMenuWindow();

    }



    public void activateContinue(){
        //Falls noch kein Spiel erstellt wurde wird ein Continue Button angezeigt, der bleibt
            this.getChildren().removeAll(this.getChildren());
            this.getChildren().addAll(continu, newGame, settings, exit);
            continueAn = true;


    }

    public void deactivateContinue(){
        this.getChildren().removeAll(continu, newGame, settings,exit);
        this.getChildren().addAll(newGame, settings, exit);
    }

    public void newGameWindow() {

        //Entferne Hauptmenü
        removeAllElements();
        spielController.getLayout().resetGameLayout();


        //Baue neuesMenü auf
        VBox container1 = new VBox();
        HBox links = new HBox();
        HBox rechts = new HBox();
        Label spielfeld = new Label("Waehle eine Karte: ");
        Label spieleranzahl = new Label("Waehle Spieler: ");
        ListView maps = new ListView();
        ListView spieler = new ListView();
        Button addSpieler = new Button("Add");
        Button back = new Button("Back");
        Button start = new Button("Start");

        links.getChildren().addAll(spielfeld, maps);
        rechts.getChildren().addAll(spieleranzahl, spieler, addSpieler);
        this.getChildren().addAll(container1,back, start);
        container1.getChildren().addAll(links, rechts);


        back.setOnAction((e) -> mainMenuWindow());
        start.setOnAction((e) -> spielController.starteSpiel());



    }
    public void removeAllElements(){
        this.getChildren().removeAll(this.getChildren());
    }

    public void mainMenuWindow(){

            //Entferne new GameWindow falls es existiert
            removeAllElements();

            newGame = new Button("New Game");
            continu =  new Button("Continue");
            settings = new Button("Settings");
            exit = new Button("Exit Game");

            this.getChildren().addAll(newGame,settings,exit);

            newGame.setOnAction((e) -> newGameWindow());
            exit.setOnAction((e) -> spielController.beendeProgramm());
            continu.setOnAction((e)-> spielController.getLayout().setGameLayout());
            settings.setOnAction((e)-> settingsWindow());




    }

    public void settingsWindow(){
        int gerundet;
        this.getChildren().removeAll(this.getChildren());
        Slider antwortZeit = new Slider(4,15,Integer.valueOf(spielController.getProperties().getProperty("antwortZeit")));
        antwortZeit.setBlockIncrement(12);
        antwortZeit.valueProperty().addListener((obs, oldValue, newValue) -> {
            spielController.getProperties().setProperty("antwortZeit",String.valueOf(newValue.intValue()));
            System.out.println(spielController.getProperties().getProperty("antwortZeit"));
        });



        Slider volume = new Slider();

        Button button = new Button("Change Music Path");
        Label pfad = new Label(spielController.getProperties().getProperty("musicPath"));
        Button back = new Button("Back");
        this.getChildren().addAll(antwortZeit,volume,button, pfad, back);
        back.setOnAction((e)->spielController.getLayout().getViewController().setMainMenu());


        button.setOnAction((e) -> {
            String newpath = new DirectoryChooser().showDialog(spielController.getLayout().getViewController().getStage()).getAbsolutePath();
            pfad.setText(newpath);
            spielController.getProperties().setProperty("musicPath", newpath);

            spielController.beendeSpiel();
            spielController.ladeMusik();
        });
    }

    public void newGameAnAus(boolean anAus){
        this.newGame.setDisable(!anAus);
    }




}
