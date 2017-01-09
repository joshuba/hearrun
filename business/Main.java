package hearrun.business;

import hearrun.view.layout.CompleteLayout;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by Josh on 09.01.17.
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
        CompleteLayout layout = new CompleteLayout();
        Scene scene = new Scene(layout);

        primaryStage.setTitle("Hear and Run - alpha 0.01");


        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);

        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
