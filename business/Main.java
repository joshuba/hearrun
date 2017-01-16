package hearrun.business;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

/**
 * Created by Josh on 09.01.17
 */
public class Main extends Application {
    private Stage primaryStage;

    @Override
    public void init() throws Exception {
        super.init();

    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        SpielController spielController = new SpielController(primaryStage, "map1.txt", 3);

        Scene scene = new Scene(spielController.getLayout());


        primaryStage.setTitle("Hear and Run - alpha 0.01");
        //primaryStage.initStyle(StageStyle.UTILITY); //Nur "schliessen Button"
        primaryStage.setMinWidth(1600);
        primaryStage.setMinHeight(1000);


        primaryStage.setScene(scene);
       //primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scene.getStylesheets().add(("/hearrun/view/layout/css/felder.css"));
        scene.getStylesheets().add(("/hearrun/view/layout/css/layout.css"));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }


}
