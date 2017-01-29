package hearrun.view.layout;

import hearrun.view.controller.SpielController;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Created by Josh on 24.01.17.
 */
public class FrageIntro extends HBox {
    private HBox bild;
    private Label label;
    private SpielController spielController;

    public FrageIntro(SpielController spielController){
        this.setId("frageIntro");
        this.spielController = spielController;
        bild = new HBox();
        label = new Label(spielController.getAktSpiel().getAktSpieler().getName());
        setID();
        this.getChildren().addAll(bild, label);

        //styling
        bild.setMinSize(100,100);
        System.out.println(bild.getId());
        label.setId("grossText");
        start();


    }

    public void start(){
        Feld aktfeld = spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(), spielController.getAktSpiel().getAktSpieler().getAktY());

        ScaleTransition st = new ScaleTransition(Duration.millis(500)), bild;
        st.setByX(1.5f);
        st.setByY(1.5f);
        st.byXProperty().bind(this.widthProperty());
        st.byYProperty().bind(this.heightProperty());

        st.setAutoReverse(false);

        ScaleTransition st2 = new ScaleTransition(Duration.millis(500), this);
        st2.setByX(1.5f);
        st2.setByY(1.5f);
        st.setAutoReverse(false);




        KeyFrame k1 = new KeyFrame(Duration.millis(0), a ->{
            st2.play();
        });

        KeyFrame k2 = new KeyFrame(Duration.millis(1000), a ->{
            st.play();
        });

        KeyFrame k3 = new KeyFrame(Duration.millis(0), a ->{
            st.play();
        });

        Timeline fadein = new Timeline();
        fadein.setAutoReverse(false);
        fadein.setCycleCount(1);
        fadein.getKeyFrames().addAll(k1,k2);
        //fadein.play();


    }

    private void setID(){
        int x = spielController.getAktSpiel().getAktSpieler().getAktX();
        int y = spielController.getAktSpiel().getAktSpieler().getAktY();

        String feldtyp = spielController.getAktSpiel().getAktMap().getFeld(x, y).getFeldtyp().toString();
        feldtyp += "leer";

        feldtyp = feldtyp.toLowerCase();

        bild.setId(feldtyp);
    }
}
