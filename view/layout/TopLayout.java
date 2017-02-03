package hearrun.view.layout;

import hearrun.view.controller.SpielController;
import hearrun.view.controller.ViewController;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**Enthält den Menuebutton
 *
 * @author Leo Back & Joshua Barth
 */
public class TopLayout extends VBox {
    private ViewController viewController;
    private SpielController spielController;
    private HBox container;

    public TopLayout(ViewController viewController, SpielController spielController){
        this.viewController = viewController;
        this.spielController = spielController;
        this.setId("topLayout");
        this.setMinHeight(90);

        Button pause = new Button();
        container = new HBox();
        container.getChildren().addAll(pause);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(10,0,0,10));

        pause.setId("pauseButton");
        pause.setOnAction((e) -> viewController.setMainMenu());

        this.getChildren().addAll(container);

    }


}
