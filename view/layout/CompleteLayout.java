package hearrun.view.layout;

import hearrun.business.Player;
import hearrun.business.SpielController;
import hearrun.view.controller.ViewController;
import hearrun.view.layout.FrageFenster.TextFrage;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 14.01.17.
 */
public class CompleteLayout extends StackPane {
    private GameLayout gameLayout;
    private ViewController viewController;
    private SpielController spielController;
    private MainMenu mainMenu;
    private Stage stage;
    private Player player;

    public CompleteLayout(Stage stage, SpielController spielController, Player player){
         this.viewController = new ViewController(stage, spielController);
         this.spielController = spielController;
         this.player = player;
         this.stage = stage;
         this.maxHeight(stage.getHeight());
         this.maxWidth(stage.getWidth());

         mainMenu = new MainMenu(spielController);

         setMainMenu();


    }

    public void setGameLayout(){
        this.getChildren().removeAll(this.getChildren());
        //Falls das gamelayout nicht da ist hinzufuegen
        if(this.getChildren().isEmpty()){
            this.getChildren().addAll(gameLayout);
        }
        player.stopLoop();
        player.play("src/hearrun/resources/music/2.mp3");

    }

    public void setMainMenu(){
        player.stopLoop();

        //this.getChildren().removeAll(gameLayout);
        player.play("src/hearrun/resources/music/1.mp3", true);
        this.getChildren().addAll(mainMenu);
        mainMenu.mainMenuWindow();
    }

    public ViewController getViewController(){
        return this.viewController;
    }

    public void resetGameLayout(){
        this.getChildren().removeAll(gameLayout);
        gameLayout = new GameLayout(stage, spielController, viewController);

    }

    public MainMenu getMainMenu(){
        return this.mainMenu;
    }

    public void zeigeTextFrage(TextFrage frage){
        this.getChildren().add(frage);


    }

    public void bluryAnAus(boolean anAus){
        this.gameLayout.Blury(anAus);
    }




}
