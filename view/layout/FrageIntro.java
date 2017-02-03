package hearrun.view.layout;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * Created by Josh on 24.01.17.
 */
public class FrageIntro extends StackPane {
    private HBox bild;
    private HBox weiss;
    private Label label;
    private SpielController spielController;
    private VBox container;
    private VBox hintergrund;

    public FrageIntro(SpielController spielController){
        //this.setId("frageIntro");
        this.spielController = spielController;
        bild = new HBox();
        container = new VBox();
        weiss = new HBox();
        hintergrund = new VBox();

        weiss.setStyle("-fx-background-color: white");
        weiss.prefWidthProperty().bind(this.widthProperty());
        weiss.prefHeightProperty().bind(this.heightProperty().add(400));

        hintergrund.prefWidthProperty().bind(this.widthProperty());
        hintergrund.prefHeightProperty().bind(this.heightProperty().add(400));
        hintergrund.setStyle("-fx-background-color: white");

        //spielController.getLayout().getGameLayout().blury(true);

        label = new Label(spielController.getAktSpiel().getAktSpieler().getName() + "\n" + spielController
        .getAktSpiel().getAktFeld().getPassendenFragetyp());
        label.setStyle("-fx-text-alignment: center; -fx-font-size: 20");


        container.getChildren().addAll(bild,label);


        label.setAlignment(Pos.TOP_CENTER);
        container.setAlignment(Pos.CENTER);

        setID();
        this.getChildren().addAll(hintergrund, container, weiss);
        weiss.setOpacity(0);
        hintergrund.setOpacity(0);

        //styling
        bild.setMinSize(100,100);
        bild.setMaxSize(100,100);




        start();



    }

    public void start(){
        Feld aktfeld = spielController.getAktSpiel().getAktMap().getFeld(spielController.getAktSpiel().getAktSpieler().getAktX(), spielController.getAktSpiel().getAktSpieler().getAktY());




        ScaleTransition st = new ScaleTransition(Duration.millis(1000), container);
        st.setFromX(5);
        st.setFromY(5);
        st.setToX(8);
        st.setToY(8);


        RotateTransition rt = new RotateTransition(Duration.millis(500), this.bild);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setCycleCount(2);

        ScaleTransition st2 = new ScaleTransition(Duration.millis(1200), container);
        st2.setByX(1.1);
        st2.setByY(1.1);

        ScaleTransition st3 = new ScaleTransition(Duration.millis(400), container);
        st3.setByX(70);
        st3.setByY(70);

        FadeTransition ft = new FadeTransition(Duration.millis(300), weiss);
        ft.setFromValue(0);
        ft.setToValue(150);

        FadeTransition ft2 = new FadeTransition(Duration.millis(1000), hintergrund);
        ft2.setFromValue(0);
        ft2.setToValue(150);



        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            st.play();
            rt.play();
            st2.play();
            ft2.play();
        });


        KeyFrame k2 = new KeyFrame(Duration.millis(1200), a ->{
            ft.play();
        });



        st2.setOnFinished(e-> st3.play());

        Timeline t = new Timeline();
        t.setAutoReverse(false);
        t.setCycleCount(1);
        t.getKeyFrames().addAll(k1, k2);
        t.play();


    }

    private void setID(){
        int x = spielController.getAktSpiel().getAktSpieler().getAktX();
        int y = spielController.getAktSpiel().getAktSpieler().getAktY();

        String feldtyp = spielController.getAktSpiel().getAktMap().getFeld(x, y).getFeldtyp().toString();
        feldtyp = feldtyp.toLowerCase();
        feldtyp += "p" + (spielController.getAktSpiel().getAktSpieler().getNr()+1) + "BIG";
        System.out.println(feldtyp);



        this.bild.setId(feldtyp);
    }
}
