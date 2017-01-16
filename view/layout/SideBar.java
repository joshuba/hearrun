package hearrun.view.layout;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

/**
 * Created by joshuabarth on 09.01.17.
 */
public class SideBar extends VBox{
    public SideBar(){
        this.setId("sideBar");
        this.setMaxWidth(200);

;
        FlowPane oben = new FlowPane();

        Circle kreis = new Circle();
        oben.getChildren().add(kreis);
        kreis.setRadius(20);

        FlowPane unten = new FlowPane();

        this.getChildren().addAll(oben,unten);


        Circle kreis2 = new Circle();
        unten.getChildren().add(kreis2);
        kreis.setRadius(20);
    }
}
