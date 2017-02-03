package hearrun.view.layout.FrageFenster;

import hearrun.business.Fragetyp;
import hearrun.Main;
import hearrun.view.controller.SpielController;
import hearrun.business.fragen.CoverTitelFrage;
import hearrun.business.fragen.Frage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Created by joshuabarth on 16.01.17
 */
public class ButtonFrage extends FrageFenster {
    private Button[] buttons;
    private Button richtigButton;
    private ImageView cover;


    public ButtonFrage(Frage frage, SpielController spielController, boolean mitHeart) {
        super(frage, spielController, mitHeart);

        //falls es eine CoverTitelFrage ist
        if (frage.getFragetyp() == Fragetyp.CoverTitelFrage) {
            cover = new ImageView(((CoverTitelFrage) frage).getCover());
            cover.minHeight(20);
            cover.fitHeightProperty().bind(spielController.getLayout().getViewController().getStage().heightProperty().divide(4));
            cover.setPreserveRatio(true);
            vBox.getChildren().addAll(cover);
            VBox.setMargin(cover, new Insets(0, 0, 40, 0));
        }


        this.buttons = new Button[4];
        buttons[0] = new Button();
        buttons[1] = new Button();
        buttons[2] = new Button();
        buttons[3] = new Button();

        vBox.getChildren().addAll(textfeld, buttons[0], buttons[1], buttons[2], buttons[3], wuerfelBox);

        //Frageninfos auslesen und in GUI einsetzen
        buttons[0].setText("A" + "   " + frage.getAntworten()[0]);
        buttons[1].setText("B" + "   " + frage.getAntworten()[1]);
        buttons[2].setText("C" + "   " + frage.getAntworten()[2]);
        buttons[3].setText("D" + "   " + frage.getAntworten()[3]);

        richtigButton = buttons[frage.getRichtigIndex()];
        buttons[0].setId("frageButton");
        buttons[1].setId("frageButton");
        buttons[2].setId("frageButton");
        buttons[3].setId("frageButton");


        buttons[0].setOnAction((e) -> buttonPress(buttons[0]));
        buttons[1].setOnAction((e) -> buttonPress(buttons[1]));
        buttons[2].setOnAction((e) -> buttonPress(buttons[2]));
        buttons[3].setOnAction((e) -> buttonPress(buttons[3]));


    }

    public void buttonPress(Button bx) {
        disableAllButtons();
        timeline.stop();

        if (bx == richtigButton) {
            bx.setId("frageButtonTrue");
            this.falschRichtig.setValue(1);
            fertig();

            effectPlayer.play("/hearrun/resources/sounds/right.mp3");


        } else {
            bx.setId("frageButtonFalse");
            this.falschRichtig.setValue(0);
            fertig();

            effectPlayer.play("/hearrun/resources/sounds/wrong.mp3");

        }

    }

    public void fertig() {
        if (!(frage.getFragetyp() == Fragetyp.CoverTitelFrage))
            musicPlayer.fadeOut();
        zeigeRichtigOderFalsch();
        disableAllButtons();
        aktualisiereAchievement();
    }

    public void disableAllButtons() {
        buttons[0].setDisable(true);
        buttons[1].setDisable(true);
        buttons[2].setDisable(true);
        buttons[3].setDisable(true);
    }

    protected void richtigButtonFaerben() {
        if (richtigButton.getId().equals("frageButtonTrue")) {
            richtigButton.setId("frageButton");
        } else if (richtigButton.getId().equals("frageButton")) {
            richtigButton.setId("frageButtonTrue");
        }

    }
}
