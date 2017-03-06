package hearrun.view.layout;

import hearrun.controller.SpielController;
import hearrun.controller.ViewController;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**Enthält den Menuebutton
 *
 * @author Leo Back & Joshua Barth
 */
public class TopLayout extends VBox {
    private ViewController viewController;
    private SpielController spielController;
    private AnchorPane container;

    public TopLayout(ViewController viewController, SpielController spielController){
        this.viewController = viewController;
        this.spielController = spielController;
        this.setId("topLayout");
        this.setMinHeight(90);

        Button pause = new Button("Pause");
        Button testEnde = new Button("Finish");
        testEnde.setId("buttonRedHover");
        container = new AnchorPane();

        container.getChildren().addAll(pause, testEnde);
        container.setPadding(new Insets(20,0,0,10));
        container.setLeftAnchor(pause,0.0);
        container.setRightAnchor(testEnde, 10.0);


        pause.setId("pauseButton");

        pause.setOnAction((e) -> viewController.setMainMenu());
        testEnde.addEventHandler(ActionEvent.ACTION, d -> {
            spielController.getAktSpiel().setSieg();
        });

        this.getChildren().addAll(container);

    }


}
