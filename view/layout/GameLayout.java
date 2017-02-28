package hearrun.view.layout;

import hearrun.controller.SpielController;
import hearrun.controller.ViewController;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**Das Zentrale Layout waehrend dem Spiel
 *
 * @author Leo Back & Joshua Barth
 */
public class GameLayout extends BorderPane{
    SpielerAnzeige leftLayout;
    SpielerAnzeige rightLayout;
    public GameLayout(Stage stage, SpielController spielController, ViewController viewController){
        this.setId("gameLayout");


        //Panes initalisieren
        CenterLayout centerLayout = new CenterLayout(viewController);
        leftLayout = new SpielerAnzeige(spielController.getSpielerListe(), "links", viewController.getStage());
        rightLayout = new SpielerAnzeige(spielController.getSpielerListe(), "rechts", viewController.getStage());
        TopLayout topLayout = new TopLayout(viewController, spielController);

        //Komponenten zusammenfügen
        this.setCenter(centerLayout);
        this.setLeft(leftLayout);
        this.setRight(rightLayout);
        this.setTop(topLayout);

        HBox bottom = new HBox();
        bottom.setMinHeight(50);

        this.setBottom(bottom);


        //Dem Viewcontroller übergeben
        viewController.setCenterLayout(centerLayout);

    }

    public void blury(boolean anAus){
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

    public void setAktSpieler(int i) {
        leftLayout.setAktuellerSpieler(i);
        rightLayout.setAktuellerSpieler(i);
    }


}
