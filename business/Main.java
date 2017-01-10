package hearrun.business;

import hearrun.view.layout.CompleteLayout;
import hearrun.view.layout.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        CompleteLayout layout = new CompleteLayout(primaryStage);
        Scene scene = new Scene(layout);



        primaryStage.setTitle("Hear and Run - alpha 0.01");
        //primaryStage.initStyle(StageStyle.UTILITY); //Nur "schliessen Button"
        primaryStage.setMinWidth(1600);
        primaryStage.setMinHeight(1000);


        primaryStage.setScene(scene);
       //primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scene.getStylesheets().add(("/hearrun/view/layout/layout1.css"));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }


}
