package hearrun.view.layout;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Created by Josh on 28.01.17.
 */
public class Starter extends StackPane {

    private ParallelTransition startTransition;
    private ScaleTransition scaleTransition;
    private ScaleTransition startScaleTransition;
    private ScaleTransition initTransition;
    private FadeTransition fadeTransition;
    private SequentialTransition startsSequentialTransition;
    private DoubleProperty expandToMaxProperty;
    private final ImageView baseImage;

    public Starter(String base, double fitWidth, double fitHeight)
    {
        baseImage = new ImageView(base);
        init(fitWidth, fitHeight);
    }

    public Starter(Image base, double fitWidth, double fitHeight)
    {
        baseImage = new ImageView(base);
        init(fitWidth, fitHeight);
    }

    private void init(double fitWidth, double fitHeight)
    {
        expandToMaxProperty = new SimpleDoubleProperty(1.2);

        baseImage.setFitWidth(fitWidth);
        baseImage.setFitHeight(fitHeight);


        scaleTransition = new ScaleTransition(Duration.millis(200), this);
        scaleTransition.setCycleCount(1);
        scaleTransition
                .setInterpolator(Interpolator.EASE_BOTH);



        getChildren()
                .addAll(baseImage);

        setOnMouseEntered((MouseEvent t) -> {
            scaleTransition.setFromX(getScaleX());
            scaleTransition.setFromY(getScaleY());
            scaleTransition.setToX(expandToMaxProperty.get());
            scaleTransition.setToY(expandToMaxProperty.get());
            scaleTransition.playFromStart();
        });

        setOnMouseExited((MouseEvent t) -> {
            scaleTransition.setFromX(getScaleX());
            scaleTransition.setFromY(getScaleY());
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.playFromStart();
        });

        setOnMouseClicked((MouseEvent t) -> {
            startScaleTransition.setFromX(getScaleX());
            startScaleTransition.setFromY(getScaleY());
            startScaleTransition.setToX(2);
            startScaleTransition.setToY(2);
            fadeTransition.setFromValue(1.0f);
            fadeTransition.setToValue(0.5f);
            startsSequentialTransition.playFromStart();
        });
    }

    public DoubleProperty expandToMaxProperty()
    {
        return expandToMaxProperty;
    }

}
