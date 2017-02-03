package hearrun.view.layout.FrageFenster;

import hearrun.Main;
import hearrun.view.controller.SpielController;
import hearrun.business.fragen.CoverWahlFrage;
import hearrun.business.fragen.Frage;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Created by joshuabarth on 16.01.17.
 */
public class CoverFrage extends FrageFenster {
    private ImageView [] buttons;
    private ImageView richtigButton;
    private ImageView cover;
    private HBox alleCover;



    public CoverFrage(Frage frage, SpielController spielController, boolean mitHeart){
        super(frage,spielController, mitHeart);
        alleCover = new HBox();



        this.buttons = new ImageView[4];
        buttons [0] = new ImageView();
        buttons [1] = new ImageView();
        buttons [2] = new ImageView();
        buttons [3] = new ImageView();
        alleCover.getChildren().addAll(buttons[0], buttons[1], buttons[2], buttons[3]);

        hover();
        vBox.getChildren().addAll(textfeld, alleCover, wuerfelBox);



        richtigButton = buttons[frage.getRichtigIndex()];

        for(int i = 0; i<4; i++){
            //Frageninfos auslesen und in GUI einsetzen
            buttons[i].setImage((((CoverWahlFrage)frage).getCover(i)));
            buttons[i].setId("coverButton");


        buttons[0].setOnMouseClicked((e)-> buttonPress(buttons[0]));
        buttons[1].setOnMouseClicked((e)-> buttonPress(buttons[1]));
        buttons[2].setOnMouseClicked((e)-> buttonPress(buttons[2]));
        buttons[3].setOnMouseClicked((e)-> buttonPress(buttons[3]));







            buttons[0].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().divide(4));
            buttons[0].setPreserveRatio(true);
            buttons[1].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().divide(4));
            buttons[1].setPreserveRatio(true);
            buttons[2].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().divide(4));
            buttons[2].setPreserveRatio(true);
            buttons[3].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().divide(4));
            buttons[3].setPreserveRatio(true);
            alleCover.setSpacing(16);
            alleCover.setAlignment(Pos.CENTER);








        }





    }

    public void buttonPress(ImageView bx){
        //disableAllButtons();
        timeline.stop();

        if(bx == richtigButton){
            bx.setId("richtigCoverButton");
            this.falschRichtig.setValue(1);
            fertig();

            effectPlayer.play("/hearrun/resources/sounds/right.mp3");


        }else{
            bx.setId("falschCoverButton");
            this.falschRichtig.setValue(0);
            fertig();

            effectPlayer.play("/hearrun/resources/sounds/wrong.mp3");

        }

    }

    public void fertig(){
        musicPlayer.fadeOut();
        zeigeRichtigOderFalsch();
        disableAllButtons();
        aktualisiereAchievement();
    }

    public void disableAllButtons(){
        buttons[0].setDisable(true);
        buttons[1].setDisable(true);
        buttons[2].setDisable(true);
        buttons[3].setDisable(true);
    }

    protected void richtigButtonFaerben(){
        if(richtigButton.getId().equals("richtigCoverButton")){
            richtigButton.setId("coverButton");
        }else if(richtigButton.getId().equals("coverButton")){
            richtigButton.setId("richtigCoverButton");
        }

    }


    public void hover(){

        for(int i=0; i<buttons.length;i++){
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), buttons[i]);
            scaleTransition.setCycleCount(1);
            scaleTransition.setInterpolator(Interpolator.EASE_IN);


            buttons[i].setOnMouseEntered((MouseEvent t) -> {
                scaleTransition.setFromX(getScaleX());
                scaleTransition.setFromY(getScaleY());
                scaleTransition.setToX(1.08);
                scaleTransition.setToY(1.08);
                scaleTransition.playFromStart();
            });

            buttons[i].setOnMouseExited((MouseEvent t) -> {
                scaleTransition.setFromX(getScaleX());
                scaleTransition.setFromY(getScaleY());
                scaleTransition.setToX(1);
                scaleTransition.setToY(1);
                scaleTransition.playFromStart();
            });


        }




    }





}
