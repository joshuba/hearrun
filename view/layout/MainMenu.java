package hearrun.view.layout;

import hearrun.business.SpielController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        if(!continueAn){
            this.getChildren().removeAll(newGame,settings,exit);
            this.getChildren().addAll(continu, newGame, settings, exit);
            continueAn = true;
        }


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

        //newGame.setOnAction((e) -> spielController.starteSpiel());
        newGame.setOnAction((e) -> newGameWindow());
        exit.setOnAction((e) -> spielController.beendeProgramm());
        continu.setOnAction((e)-> spielController.getLayout().setGameLayout());

    }

    public void settingsWindow(){
        removeAllElements();
        Slider antwortZeit = new Slider();
        Slider volume = new Slider();
        Button button = new Button();
        Button back = new Button();
        this.getChildren().addAll(antwortZeit,volume,button, back);
        back.setOnAction((e)->mainMenuWindow());
    }


}
