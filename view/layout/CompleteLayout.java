package hearrun.view.layout;

import hearrun.Main;
import hearrun.business.Player;
import hearrun.view.controller.SpielController;
import hearrun.view.controller.ViewController;
import hearrun.view.layout.FrageFenster.Fenster;
import javafx.geometry.Pos;
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
        mainMenu = new MainMenu(spielController, viewController);
        this.spielController = spielController;
        this.musicPlayer = spielController.getMusicPlayer();
        this.stage = stage;

    }

    public void setGameLayout(){
        musicPlayer.stop();
        musicPlayer.play(Main.class.getResource("/hearrun/resources/music/Lakechiller.mp3").getPath(), true);

        bluryAnAus(false);
        this.getChildren().removeAll(this.getChildren());
        //Falls das gamelayout nicht da ist hinzufuegen
        if(this.getChildren().isEmpty()){
            this.getChildren().addAll(gameLayout);
        }
        mainMenu.kreisSpawningAnAus(false);
        System.out.println("Kreise aus");

    }

    public void setMainMenu(){


        musicPlayer.stop();
        musicPlayer.play(Main.class.getResource("/hearrun/resources/music/4.mp3").getPath(), true);
        this.getChildren().removeAll(this.getChildren());



        this.getChildren().addAll(mainMenu);
        mainMenu.showMainMenu();
        mainMenu.kreisSpawningAnAus(true);
        System.out.println("Kreise an");

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

    public void zeigeFenster(Fenster fenster){
        this.getChildren().clear();
        this.getChildren().add(fenster);
    }

    public void bluryAnAus(boolean anAus){
        this.gameLayout.blury(anAus);
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

    public void setFrageIntro(FrageIntro frageIntro){
        this.getChildren().addAll(frageIntro);
        frageIntro.setAlignment(Pos.CENTER);
    }

    public void removeFrageIntro(FrageIntro frageIntro){
        this.getChildren().removeAll(frageIntro);
    }

    public GameLayout getGameLayout(){
        return this.gameLayout;
    }
}
