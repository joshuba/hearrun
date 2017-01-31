package hearrun.view.layout;

import hearrun.view.controller.SpielController;
import hearrun.view.controller.ViewController;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by joshuabarth on 09.01.17.
 */
public class TopLayout extends VBox {
    ViewController viewController;
    SpielController spielController;
    public TopLayout(ViewController viewController, SpielController spielController){
        this.viewController = viewController;
        this.spielController = spielController;
        this.setId("topLayout");
        this.setMinHeight(50);


        Button pause = new Button();
        pause.setId("pauseButton");
        //Button frage = new Button("FRAGE STELLEN");

        pause.setOnAction((e) -> viewController.setMainMenu());
        //frage.setOnAction((e) -> spielController.stelleAktFrage());


        this.getChildren().addAll(pause);
    }
}
