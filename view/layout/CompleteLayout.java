package hearrun.view.layout;

import hearrun.Main;
import hearrun.business.Player;
import hearrun.view.controller.SpielController;
import hearrun.view.controller.ViewController;
import hearrun.view.layout.FrageFenster.Fenster;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**Das Oberlayout, welches zu allen anderen Layouts wechseln kann.
 *
 * @author Leo Back & Joshua Barth
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
        spielController.getLoopPlayer().stop();
        spielController.getLoopPlayer().play(Main.getFilePathFromResourcePath("/hearrun/resources/music/Lakechiller.mp3", false), true);

        bluryAnAus(false);
        this.getChildren().clear();
        this.getChildren().addAll(gameLayout);

        mainMenu.kreisSpawningAnAus(false);

        //Falls das Introfenster noch nicht weggeklickt wurde zeige es an
        if(spielController.getProperties().getProperty("tutorialZeigen").equals("true")){
            showIntro();
        }

    }

    public void setMainMenu() {
        spielController.getLoopPlayer().stop();
        spielController.getLoopPlayer().play(Main.getFilePathFromResourcePath("/hearrun/resources/music/1.mp3", false), true);
        this.getChildren().clear();
        this.getChildren().addAll(mainMenu);
        mainMenu.showMainMenu();
        mainMenu.kreisSpawningAnAus(true);
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

    //Intro wenn man das Spiel zum ersten mal startet
    public void showIntro(){
        VBox window = new VBox();
        Label label = new Label("Hi, da Du zum ersten Mal spielst: In den Spielecken siehst du an der farbigen Makierung welcher Spieler an der Reihe ist. Klicke auf das blinkende Feld um die deine jeweilige Frage zu erhalten zu erhalten. " +
                "Weitere Hilfestellungen findest du im Hilfe-Menü. Viel Spaß!");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(0,10,15,10));
        Button dontshow = new Button("Nicht mehr anzeigen");


        //styling
        window.setMinSize(500,200);
        window.setMaxSize(500,200);
        label.setId("schriftMittel");
        label.setStyle("-fx-text-alignment: center");
        window.getChildren().addAll(label, dontshow);
        window.setId("tutorial");
        window.setAlignment(Pos.CENTER);
        window.setMargin(window,new Insets(10,10,10,10));

        this.getChildren().addAll(window);

        TranslateTransition t = new TranslateTransition(Duration.millis(600), window);
        t.setFromY(-400);
        t.setFromX(0);
        t.setToX(0);
        t.setToY(0);
        t.play();

        dontshow.setOnAction(e -> {
            spielController.getProperties().setProperty("tutorialZeigen", "false");
            this.getChildren().remove(window);




        });

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
