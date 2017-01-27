package hearrun.view.layout.FrageFenster;

import hearrun.business.Main;
import hearrun.business.SpielController;
import hearrun.business.fragen.CoverWahlFrage;
import hearrun.business.fragen.Frage;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
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
    private boolean zoomOut;
    private boolean zoomIn;


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

            buttons[0].setOnMouseEntered((e)-> hover(buttons[0], true));
            buttons[1].setOnMouseEntered((e)-> hover(buttons[1], true));
            buttons[2].setOnMouseEntered((e)-> hover(buttons[2], true));
            buttons[3].setOnMouseEntered((e)-> hover(buttons[3], true));

            buttons[0].setOnMouseExited((e)-> hover(buttons[0], false));
            buttons[1].setOnMouseExited((e)-> hover(buttons[1], false));
            buttons[2].setOnMouseExited((e)-> hover(buttons[2], false));
            buttons[3].setOnMouseExited((e)-> hover(buttons[3], false));



            buttons[0].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(750));
            buttons[0].setPreserveRatio(true);
            buttons[1].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(750));
            buttons[1].setPreserveRatio(true);
            buttons[2].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(750));
            buttons[2].setPreserveRatio(true);
            buttons[3].fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().subtract(750));
            buttons[3].setPreserveRatio(true);
            alleCover.setSpacing(16);
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

            effectPlayer.play(Main.class.getResource("/hearrun/resources/sounds/right.mp3").getPath());


        }else{
            bx.setId("falschButton");
            this.falschRichtig.setValue(0);
            fertig();

            effectPlayer.play(Main.class.getResource("/hearrun/resources/sounds/wrong.mp3").getPath());

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
            effectPlayer.play(Main.class.getResource("/hearrun/resources/sounds/wrong.mp3").getPath());

        }

        t.setOnFinished(b -> wuerfeln(falschRichtig.getValue()));
        t.play();


    }

    public void hover(ImageView iv, boolean anAus){
        ScaleTransition st = new ScaleTransition(Duration.millis(100), iv);
        st.setByX(0.1f);
        st.setByY(0.1f);
        st.setAutoReverse(false);
        st.setOnFinished(e->  zoomIn = false);


        ScaleTransition st1 = new ScaleTransition(Duration.millis(100), iv);
        st1.setByX(-0.1f);
        st1.setByY(-0.1f);
        st1.setAutoReverse(false);
        st1.setOnFinished(e->  zoomOut = false);



        if(anAus && !zoomIn && !zoomOut ){
            zoomIn = true;
            st.play();


            st.play();
        }else if(!anAus && !zoomIn && !zoomOut){
            System.out.println("DEHOVER");
            zoomOut = true;
            st1.play();
        }




    }





}
