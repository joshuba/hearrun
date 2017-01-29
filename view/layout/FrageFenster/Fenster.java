package hearrun.view.layout.FrageFenster;

import hearrun.view.controller.SpielController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Leo on 29.01.2017.
 */
public abstract class Fenster extends BorderPane {
    protected SpielController spielController;
    protected Label aktSpieler;
    protected HBox catPic;
    protected VBox top;


    public Fenster(SpielController spielController) {
        this.spielController = spielController;
        aktSpieler = new Label(spielController.getAktSpiel().getAktSpieler().getName());

        catPic = new HBox();
        catPic.setMinSize(80,80);
        catPic.setMaxSize(80,80);
        catPic.setId(spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(), spielController.getAktSpiel().getAktSpieler().getAktY()).getLeerId());

        top = new VBox();
        top.setAlignment(Pos.CENTER);
        top.getChildren().addAll(catPic, aktSpieler);
        top.setPadding(new Insets(20,0,0,0));
        this.setTop(top);

    }
}
