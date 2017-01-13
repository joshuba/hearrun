package hearrun.view.layout;

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
    public TopLayout(ViewController viewController){
        this.viewController = viewController;
        this.setId("topLayout");
        this.setMinHeight(50);

        Button settings = new Button("GO");
        settings.setOnAction((e) -> viewController.nextPossibleField(null));

        this.getChildren().add(settings);
    }
}
