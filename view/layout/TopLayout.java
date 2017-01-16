package hearrun.view.layout;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by joshuabarth on 09.01.17.
 */
public class TopLayout extends VBox {
    public TopLayout(){
        this.setId("topLayout");
        this.setMinHeight(50);

        Button settings = new Button("Menu");
        this.getChildren().add(settings);
    }
}
