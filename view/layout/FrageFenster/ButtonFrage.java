package hearrun.view.layout.FrageFenster;

import hearrun.business.Fragetyp;
import hearrun.business.Player;
import hearrun.business.SpielController;
import hearrun.business.fragen.CoverTitelFrage;
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
public class ButtonFrage extends FrageFenster {
    private Button [] buttons;
    private Button richtigButton;
    private ImageView cover;


    public ButtonFrage(Frage frage, SpielController spielController){
        super(frage,spielController);

        if(frage.getFragetyp() == Fragetyp.CoverTitelFrage){
            cover = new ImageView(((CoverTitelFrage)frage).getCover());
            cover.fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(500));
            cover.setPreserveRatio(true);

            this.getChildren().add(2, cover);
        }



        this.buttons = new Button[4];
        buttons [0] = new Button();
        buttons [1] = new Button();
        buttons [2] = new Button();
        buttons [3] = new Button();

        vBox.getChildren().addAll(textfeld, buttons[0], buttons[1], buttons[2], buttons[3]);

        //Frageninfos auslesen und in GUI einsetzen
        buttons[0].setText(frage.getAntworten()[0]);
        buttons[1].setText(frage.getAntworten()[1]);
        buttons[2].setText(frage.getAntworten()[2]);
        buttons[3].setText(frage.getAntworten()[3]);
        richtigButton = buttons[frage.getRichtigIndex()];
        buttons[0].setId("normalButton");
        buttons[1].setId("normalButton");
        buttons[2].setId("normalButton");
        buttons[3].setId("normalButton");


        buttons[0].setOnAction((e)-> buttonPress(buttons[0]));
        buttons[1].setOnAction((e)-> buttonPress(buttons[1]));
        buttons[2].setOnAction((e)-> buttonPress(buttons[2]));
        buttons[3].setOnAction((e)-> buttonPress(buttons[3]));



    }

    public void buttonPress(Button bx){
        disableAllButtons();
        timeline.stop();

        if(bx == richtigButton){
            bx.setId("richtigButton");
            this.falschRichtig.setValue(1);
            fertig();

            effectPlayer.play("src/hearrun/resources/sounds/right.mp3");


        }else{
            bx.setId("falschButton");
            this.falschRichtig.setValue(0);
            fertig();

            effectPlayer.play("src/hearrun/resources/sounds/wrong.mp3");

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
            effectPlayer.play("src/hearrun/resources/sounds/wrong.mp3");

        }

        t.setOnFinished(b -> wuerfeln(falschRichtig.getValue()));
        t.play();


    }



}
