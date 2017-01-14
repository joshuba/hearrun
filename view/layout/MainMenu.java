package hearrun.view.layout;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 14.01.17.
 */
public class MainMenu extends VBox{
    public MainMenu(){
        this.setMinHeight(700);
        this.setMinWidth(300);
        Button newGame = new Button("New Game");
        this.getChildren().add(newGame);
        this.setAlignment(Pos.CENTER);

    }


}
