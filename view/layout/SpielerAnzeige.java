package hearrun.view.layout;

import hearrun.Main;
import hearrun.business.Spieler;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by joshuabarth on 09.01.17.
 */
public class SpielerAnzeige extends VBox {
    private Stage stage;
    public SpielerAnzeige(ArrayList<Spieler> spielerListe, String position, Stage stage) {
        this.setMinWidth(200);
        this.stage = stage;

        VBox oben;

        VBox unten;

        if (position.equals("links")) {
            // Spielernamen anzeigen
            oben = initSpielerAnzeige(0, spielerListe);


            if (spielerListe.size() > 2) { // Mehr als 2 Spieler: zeige oben und unten einen Spieler an.
                unten = initSpielerAnzeige(2, spielerListe);
            } else { // sonst unten leer lassen
                unten = new VBox();
            }
        } else { // Position "rechts"
            if (spielerListe.size() > 1) { // Mehr als 1 Spieler: zeige oben einen Spieler an.
                oben = initSpielerAnzeige(1, spielerListe);

                if (spielerListe.size() > 3) { // 4 Spieler: zeige unten auch einen Spieler an.
                    unten = initSpielerAnzeige(3, spielerListe);
                } else { // Sonst lass unten leer
                    unten = new VBox();
                }

            } else { // nur ein Spieler: lass oben und unten leer.
                oben = new VBox();
                unten = new VBox();
            }
        }

        this.getChildren().addAll(oben, unten);
    }

    private VBox initSpielerAnzeige(int spielerNummer, ArrayList<Spieler> spielerListe) {
        VBox anzeige = new VBox();
        anzeige.prefHeightProperty().bind(stage.heightProperty());
        anzeige.setAlignment(Pos.TOP_CENTER);
        anzeige.getStyleClass().add("spieler-box");

        StackPane spielerFarbe = new StackPane();
        Circle farbKreis = new Circle(20);
        farbKreis.setFill(getfarbeByNummer(spielerNummer));
        Label nummerLabel = new Label("" + (spielerNummer + 1));
        nummerLabel.getStyleClass().add("spieler-nummer");
        spielerFarbe.getChildren().addAll(farbKreis, nummerLabel);
        spielerFarbe.setAlignment(Pos.CENTER);

        ListView<HBox> achievements = new ListView<>();
        ObservableMap<String, Integer> achievementsMap = spielerListe.get(spielerNummer).getAchievements();
        achievements.setMaxHeight(100);
        achievementsMap.addListener((MapChangeListener<String, Integer> ) change ->
                updateAchievements(achievementsMap, achievements)
        );
        achievements.getStyleClass().add("achievements");


        Label spielerName = new Label(spielerListe.get(spielerNummer).getName());
        anzeige.getChildren().addAll(spielerFarbe, spielerName, achievements);
        updateAchievements(achievementsMap, achievements);
        return anzeige;
    }

    private Color getfarbeByNummer(int nr) {
        switch (nr) {
            case 0:
                return Main.spielerEinsFarbe;
            case 1:
                return Main.spielerZweiFarbe;
            case 2:
                return Main.spielerDreiFarbe;
            default:
                return Main.spielerVierFarbe;
        }
    }

    private void updateAchievements(ObservableMap<String, Integer> achievementMap, ListView<HBox> achievements) {
        class Herzen extends HBox {
            public Herzen() {
                int leben = achievementMap.get("leben");
                this.setAlignment(Pos.BASELINE_CENTER);
                for(int i = 0; i < leben; i++) {
                    System.out.println("herz hinzufügen..." + "\nAnzahl Leben = " + leben);
                    Label l = new Label();
                    l.getStyleClass().add("leben-icon");
                    l.setMinSize(30,30);
                    getChildren().add(l);
                }
            }
        }
        achievements.getItems().clear();
        achievements.getItems().add(new Herzen());

        Label fragenRichtig = new Label(achievementMap.get("fragenRichtig") + " Fragen richtig beantwortet");
        fragenRichtig.setTextAlignment(TextAlignment.CENTER);
        HBox hb = new HBox(fragenRichtig);
        hb.setAlignment(Pos.BASELINE_CENTER);

        achievements.getItems().add(hb);
    }
}
