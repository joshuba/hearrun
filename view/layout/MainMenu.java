package hearrun.view.layout;

import hearrun.business.SpielController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by joshuabarth on 14.01.17.
 */
public class MainMenu extends VBox{
    private Button continu;
    private Button newGame;
    private Button settings;
    private Button exit;
    private boolean continueAn = false;

    public MainMenu(SpielController spielController){
        this.setId("mainMenu");
        this.setMinHeight(700);
        this.setMinWidth(300);


        newGame = new Button("New Game");
        continu =  new Button("Continue");
        settings = new Button("Settings");
        exit = new Button("Exit Game");



        this.getChildren().addAll(newGame,settings,exit);

        newGame.setOnAction((e) -> spielController.starteSpiel());
        exit.setOnAction((e) -> spielController.beendeProgramm());
        continu.setOnAction((e)-> spielController.getLayout().setGameLayout());





        this.setAlignment(Pos.CENTER);

    }



    public void activateContinue(){
        //Falls noch kein Spiel erstellt wurde wird ein Continue Button angezeigt, der bleibt
        if(!continueAn){
            this.getChildren().removeAll(newGame,settings,exit);
            this.getChildren().addAll(continu, newGame, settings, exit);
        }


    }

    public void deactivateContinue(){
        this.getChildren().removeAll(continu, newGame, settings,exit);
        this.getChildren().addAll(newGame, settings, exit);
    }


}
