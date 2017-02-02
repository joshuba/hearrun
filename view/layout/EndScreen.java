package hearrun.view.layout;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Created by Josh on 02.02.17.
 */
public class EndScreen extends StackPane {
    private SpielController spielController;
    private BorderPane container1;
    private Label ueberschrift;

    public EndScreen(SpielController spielController){
        this.spielController = spielController;
        container1 = new BorderPane();


        spielController.getEffectPlayer().play(Main.class.getResource("/hearrun/resources/sounds/sieg.mp3").getPath());
        showResults();

    }

    public void showResults(){
        ueberschrift = new Label(spielController.getAktSpiel().getAktSpieler().getName() + " hat gewonnen!");
        this.getChildren().add(container1);
        container1.setTop(ueberschrift);


        //styling
        ueberschrift.setAlignment(Pos.CENTER);
    }
}
