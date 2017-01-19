package hearrun.view.layout;

import hearrun.business.Spiel;
import hearrun.business.SpielController;
import hearrun.view.controller.ViewController;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.text.View;

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

        Button settings = new Button("Menü");
        settings.setOnAction((e) -> viewController.setMainMenu());

        this.getChildren().addAll(settings);
    }
}
