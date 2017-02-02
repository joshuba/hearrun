package hearrun.view.layout.FrageFenster;

import hearrun.business.ereignisse.Ereignis;
import hearrun.view.controller.SpielController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import static hearrun.business.ereignisse.Ereignis.LAUFEN_POSITIV;
import static hearrun.business.ereignisse.Ereignis.LEBEN;

/**
 * Created by Leo on 29.01.2017.
 */
public class EreignisFenster extends Fenster {

    private int schritte;

    public EreignisFenster(Ereignis ereignis, SpielController spielController) {
        super(spielController);

        catPic.setPadding(new Insets(0, 0, 0, 500));

        HBox ereignisIcon = new HBox();
        ereignisIcon.setMaxSize(50, 50);
        ereignisIcon.setAlignment(Pos.CENTER);
        Label ueberschrift = new Label();
        Label text = new Label();
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
            text.setText("Du rückst 8 Felder nach vorne!");
            schritte = 8;
        } else {
            ereignisIcon.getStyleClass().add("feld-icon-positiv");
            ueberschrift.setText("Felder zurück rücken.");
            text.setText("Du rückst 8 Felder nach hinten!");
            schritte = -8;
        }

        ueberschrift.setId("grossText");
        ueberschrift.setPadding(new Insets(0, 0, 100, 0));
        text.setTextAlignment(TextAlignment.CENTER);

        VBox all = new VBox(ereignisIcon, ueberschrift, text, weiterspielen);
        all.setAlignment(Pos.CENTER);
        setCenter(all);

        weiterspielen.setOnAction(e -> {
            spielController.getLayout().bluryAnAus(false);
            spielController.getLayout().setGameLayout();
            spielController.moveAndAskNext(schritte);
        });
    }

}
