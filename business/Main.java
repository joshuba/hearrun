package hearrun.business;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Properties;

/**
 * Created by Josh on 09.01.17
 */
public class Main extends Application {
    private Stage primaryStage;
    private SpielController spielController;

    @Override
    public void init() throws Exception {
        super.init();

    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        spielController = new SpielController(primaryStage, "map1.txt", 3);

        Scene scene = new Scene(spielController.getLayout());


        primaryStage.setTitle("Hear and Run - alpha 0.01");
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);


        primaryStage.setScene(scene);

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scene.getStylesheets().add(("/hearrun/view/layout/css/felder.css"));
        scene.getStylesheets().add(("/hearrun/view/layout/css/layout.css"));
        primaryStage.show();



    }

    public static void main(String[] args) {
        launch(args);



        //Player player = new Player();
        //player.playRandomNSeconds("/Users/Josh/IdeaProjects/Hearrun/music/music/1-01 Carry On Wayward Son.mp3", 6);


    }

    @Override
    public void stop() throws Exception {
        spielController.getMusicPlayer().stop();
        spielController.getEffectPlayer().stop();
        spielController.beendeProgramm();
    }
}
