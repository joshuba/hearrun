package hearrun.view.layout;

import hearrun.business.SpielController;
import hearrun.view.controller.ViewController;
import javafx.scene.control.Button;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Josh on 09.01.17.
 */
public class GameLayout extends BorderPane{


    public GameLayout(Stage stage, SpielController spielController, ViewController viewController){
        this.setId("gameLayout");

        //Panes initalisieren
        CenterLayout centerLayout = new CenterLayout(viewController);
        SpielerAnzeige leftLayout = new SpielerAnzeige(spielController.getSpielerListe(), "links");
        SpielerAnzeige rightLayout = new SpielerAnzeige(spielController.getSpielerListe(), "rechts");
        TopLayout topLayout = new TopLayout(viewController, spielController);

        //Komponenten zusammenfügen
        this.setCenter(centerLayout);
        this.setLeft(leftLayout);
        this.setRight(rightLayout);
        this.setTop(topLayout);

        //Dem Viewcontroller übergeben
        viewController.setCenterLayout(centerLayout);
        viewController.setLeftLayout(leftLayout);
        viewController.setRightLayout(rightLayout);
    }

    public void Blury(boolean anAus){
        BoxBlur bb = new BoxBlur();
        this.setEffect(bb);

        if(anAus){
            bb.setHeight(50);
            bb.setWidth(50);
            bb.setIterations(10);
        }else{
            bb.setHeight(0);
            bb.setWidth(0);
            bb.setIterations(0);
        }

    }



}
