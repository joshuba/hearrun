package hearrun.view.layout;

import hearrun.Main;
import hearrun.business.Player;
import hearrun.view.controller.SpielController;
import hearrun.view.controller.ViewController;
import hearrun.view.layout.FrageFenster.Fenster;
import javafx.geometry.Pos;
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
    private Player musicPlayer;

    public CompleteLayout(Stage stage, SpielController spielController) {
        this.viewController = new ViewController(stage, spielController);
        mainMenu = new MainMenu(spielController, viewController);
        this.spielController = spielController;
        this.musicPlayer = spielController.getMusicPlayer();
        this.stage = stage;

    }

    public void setGameLayout() {
        musicPlayer.stop();
        musicPlayer.play(Main.getFilePathFromResourcePath("/hearrun/resources/music/Lakechiller.mp3", false), true);

        bluryAnAus(false);
        this.getChildren().clear();
        this.getChildren().addAll(gameLayout);

        mainMenu.kreisSpawningAnAus(false);
    }

    public void setMainMenu() {
        musicPlayer.stop();
        musicPlayer.play(Main.getFilePathFromResourcePath("/hearrun/resources/music/1.mp3", false), true);
        this.getChildren().clear();
        this.getChildren().addAll(mainMenu);
        mainMenu.showMainMenu();
        mainMenu.kreisSpawningAnAus(true);

        System.out.println(mainMenu.getChildren().size());
    }

    public ViewController getViewController() {
        return this.viewController;
    }

    public void resetGameLayout() {
        this.getChildren().removeAll(gameLayout);
        gameLayout = new GameLayout(stage, spielController, viewController);
    }

    public MainMenu getMainMenu() {
        return this.mainMenu;
    }

    public void zeigeFenster(Fenster fenster) {

        for (Node n : getChildren()) {
            if (n instanceof Fenster) {
                getChildren().remove(n);
                break;
            }
        }

        bluryAnAus(true);
        getChildren().add(fenster);
        // Leertaste für neue Frage ausschalten
        fenster.requestFocus();
    }

    public void bluryAnAus(boolean anAus) {
        this.gameLayout.blury(anAus);
    }

    public void newGameAnAus(boolean anAus) {
        mainMenu.newGameAnAus(anAus);
    }


    public void setIntroscreen(IntroScreen introscreen) {
        this.getChildren().removeAll();
        this.getChildren().addAll(introscreen);
    }

    public void setLoadingScreen(LoadingScreen ls) {
        this.getChildren().removeAll(this.getChildren());
        this.getChildren().addAll(ls);

    }

    public void setFrageIntro(FrageIntro frageIntro) {
        this.getChildren().addAll(frageIntro);
        frageIntro.setAlignment(Pos.CENTER);
    }

    public void removeFrageIntro(FrageIntro frageIntro) {
        this.getChildren().removeAll(frageIntro);
    }

    public void showEndScreen(EndScreen endScreen) {
        this.getChildren().addAll(endScreen);
    }

    public GameLayout getGameLayout() {
        return this.gameLayout;
    }
}
