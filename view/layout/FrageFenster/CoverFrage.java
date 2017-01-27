package hearrun.view.layout.FrageFenster;

import hearrun.business.Fragetyp;
import hearrun.business.Main;
import hearrun.business.Player;
import hearrun.business.SpielController;
import hearrun.business.fragen.CoverTitelFrage;
import hearrun.business.fragen.CoverWahlFrage;
import hearrun.business.fragen.Frage;
import hearrun.view.layout.Wuerfel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by joshuabarth on 16.01.17.
 */
public class CoverFrage extends FrageFenster {
    private ImageView [] buttons;
    private ImageView richtigButton;
    private ImageView cover;
    private HBox alleCover;


    public CoverFrage(Frage frage, SpielController spielController){
        super(frage,spielController);
        alleCover = new HBox();



        this.buttons = new ImageView[4];
        buttons [0] = new ImageView();
        buttons [1] = new ImageView();
        buttons [2] = new ImageView();
        buttons [3] = new ImageView();
        alleCover.getChildren().addAll(buttons[0], buttons[1], buttons[2], buttons[3]);

        vBox.getChildren().addAll(textfeld, alleCover);


        richtigButton = buttons[frage.getRichtigIndex()];

        for(int i = 0; i<4; i++){
            //Frageninfos auslesen und in GUI einsetzen
            buttons[i].setImage((((CoverWahlFrage)frage).getCover(i)));
            buttons[i].setId("normalButton");


        buttons[0].setOnMouseClicked((e)-> buttonPress(buttons[0]));
        buttons[1].setOnMouseClicked((e)-> buttonPress(buttons[1]));
        buttons[2].setOnMouseClicked((e)-> buttonPress(buttons[2]));
        buttons[3].setOnMouseClicked((e)-> buttonPress(buttons[3]));


            buttons[0].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(700));
            buttons[0].setPreserveRatio(true);
            buttons[1].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(700));
            buttons[1].setPreserveRatio(true);
            buttons[2].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(700));
            buttons[2].setPreserveRatio(true);
            buttons[3].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(700));
            buttons[3].setPreserveRatio(true);
            alleCover.setSpacing(8);
            alleCover.setAlignment(Pos.CENTER);








        }





    }

    public void buttonPress(ImageView bx){
        //disableAllButtons();
        timeline.stop();

        if(bx == richtigButton){
            bx.setId("richtigButton");
            this.falschRichtig.setValue(1);
            fertig();

            effectPlayer.play(Main.class.getResource("../resources/sounds/right.mp3").getPath());


        }else{
            bx.setId("falschButton");
            this.falschRichtig.setValue(0);
            fertig();

            effectPlayer.play(Main.class.getResource("../resources/sounds/wrong.mp3").getPath());

        }

    }

    public void fertig(){
        musicPlayer.fadeOut();
        zeigeRichtigOderFalsch();
        disableAllButtons();


    }

    public void disableAllButtons(){
        buttons[0].setDisable(true);
        buttons[1].setDisable(true);
        buttons[2].setDisable(true);
        buttons[3].setDisable(true);
    }

    private void richtigButtonFaerben(){
        if(richtigButton.getId().equals("richtigButton")){
            richtigButton.setId("normalButton");
        }else if(richtigButton.getId().equals("normalButton")){
            richtigButton.setId("richtigButton");
        }

    }

    public void zeigeRichtigOderFalsch(){
        KeyFrame k = new KeyFrame(
                Duration.millis(300),
                a -> richtigButtonFaerben()
        );

        Timeline t = new Timeline(k);
        t.setCycleCount(6);
        if(falschRichtig.getValue() == -1){
            effectPlayer.play(Main.class.getResource("../resources/sounds/wrong.mp3").getPath());

        }

        t.setOnFinished(b -> wuerfeln(falschRichtig.getValue()));
        t.play();


    }



}
