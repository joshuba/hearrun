package hearrun.view.layout.FrageFenster;

import hearrun.Main;
import hearrun.model.ereignisse.Ereignis;
import hearrun.controller.SpielController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Random;

import static hearrun.model.ereignisse.Ereignis.LAUFEN_POSITIV;
import static hearrun.model.ereignisse.Ereignis.LEBEN;

/**
 * Das Ereignis-Fenster zeigt auftretende Zufallsereignisse, ausgelöst durch die
 * Ereignis-Felder im Spiel.
 */
public class EreignisFenster extends Fenster {

    private int schritte;

    public EreignisFenster(Ereignis ereignis, SpielController spielController) {
        super(spielController);

        catPic.setPadding(new Insets(0, 0, 0, 500));

        HBox ereignisIcon = new HBox();
        ereignisIcon.setMaxSize(80, 80);
        ereignisIcon.setMinSize(80, 80);

        ereignisIcon.setAlignment(Pos.CENTER);
        Label ueberschrift = new Label();
        Label text = new Label();
        text.setId("schriftMittel");

        Button weiterspielen = new Button("Weiterspielen");

        schritte = 1;
        if (ereignis == LEBEN) {
            ereignisIcon.getStyleClass().add("leben-icon");
            ueberschrift.setText("Extra-Leben");
            text.setText(
                    "Du erhältst ein zusätzliches Leben! \n" +
                    "Wenn du die nächste Frage falsch beantwortest,\n" +
                    "kannst du eine weitere Frage von demselben Typ beantworten."
            );
            spielController.getAktSpiel().getAktSpieler().addLeben();
        } else if (ereignis == LAUFEN_POSITIV) {
            ereignisIcon.getStyleClass().add("feld-icon-positiv");
            ueberschrift.setText("Felder vor rücken.");
            schritte = 5 + new Random().nextInt(4);
            if(Main.testMode){
                schritte = 1;
            }
            text.setText(String.format("Du rückst %s " + (schritte == 1? "Feld": "Felder") + " nach vorne!", schritte));
        } else {
            ereignisIcon.getStyleClass().add("feld-icon-negativ");
            ueberschrift.setText("Felder zurück rücken.");
            schritte = 2 + new Random().nextInt(4);
            if(Main.testMode){
                schritte = 1;
            }
            text.setText(String.format("Du rückst %s " + (schritte == 1? "Feld": "Felder") + " nach hinten!", schritte));
            schritte *= -1;
        }

        ueberschrift.setId("grossText");
        ueberschrift.setPadding(new Insets(0, 0, 100, 0));
        text.setTextAlignment(TextAlignment.CENTER);

        VBox all = new VBox(ereignisIcon, ueberschrift, text, weiterspielen);
        all.setAlignment(Pos.CENTER);
        all.setSpacing(20);
        setCenter(all);

        weiterspielen.setOnAction(e -> {
            spielController.getLayout().bluryAnAus(false);
            spielController.getLayout().setGameLayout();
            spielController.moveAndAskNext(schritte);
        });
    }

}
