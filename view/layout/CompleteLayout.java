package hearrun.view.layout;

import hearrun.business.Player;
import hearrun.business.SpielController;
import hearrun.view.IntroScreen;
import hearrun.view.controller.ViewController;
import hearrun.view.layout.FrageFenster.TextFrage;
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
    private Player musicPlayer;

    public CompleteLayout(Stage stage, SpielController spielController){
        this.viewController = new ViewController(stage, spielController);
        mainMenu = new MainMenu(spielController);
        this.spielController = spielController;
        this.musicPlayer = spielController.getMusicPlayer();
        this.stage = stage;

    }

    public void setGameLayout(){
        bluryAnAus(false);
        this.getChildren().removeAll(this.getChildren());
        //Falls das gamelayout nicht da ist hinzufuegen
        if(this.getChildren().isEmpty()){
            this.getChildren().addAll(gameLayout);
        }
        musicPlayer.stop();

    }

    public void setMainMenu(){
        this.getChildren().removeAll(this.getChildren());

        musicPlayer.stop();


        musicPlayer.play("src/hearrun/resources/music/4.mp3", true);
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

    public void newGameAnAus(boolean anAus){
        mainMenu.newGameAnAus(anAus);
    }


    public void setIntroscreen(IntroScreen introscreen) {
        this.getChildren().removeAll();
        this.getChildren().addAll(introscreen);
    }

    public void setLoadingScreen(LoadingScreen ls) {
        this.getChildren().removeAll();
        this.getChildren().addAll(ls);

    }
}
