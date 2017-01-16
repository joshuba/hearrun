package hearrun.view.layout;

import hearrun.business.SpielController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
        ListView mapauswahl = new ListView();
        HBox spieler = new HBox();

        VBox spieleranzahl = new VBox();
        Label schrift = new Label("Spieleranzahl");
        ChoiceBox anzahl = new ChoiceBox();
        ListView spielerListe = new ListView();

        Button back = new Button("Back");
        Button start = new Button("Start");

        this.getChildren().addAll(container1, back, start);
        container1.getChildren().addAll(mapauswahl,spieler);
        spieler.getChildren().addAll(spieleranzahl,spielerListe);
        spieleranzahl.getChildren().addAll(schrift, anzahl);

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


}
