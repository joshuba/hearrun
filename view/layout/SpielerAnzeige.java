package hearrun.view.layout;

import hearrun.Main;
import hearrun.model.Spieler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Die Anzeigen der Spieler im Game-Layout.
 * Es gibt jeweils eine rechte und eine linke.
 */
public class SpielerAnzeige extends VBox {
    private Stage stage;
    private SimpleIntegerProperty aktuellerSpieler;
    private String aktiv;

    public SpielerAnzeige(ArrayList<Spieler> spielerListe, String position, Stage stage) {
        setMinWidth(150);
        setMaxWidth(150);
        setPadding(new Insets(0,20,0,20));
        this.stage = stage;
        aktuellerSpieler = new SimpleIntegerProperty(0);
        aktiv = "aktiv";

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

        VBox boxUnten = new VBox(unten);
        boxUnten.setVgrow(boxUnten, Priority.ALWAYS);
        boxUnten.setAlignment(Pos.BOTTOM_CENTER);
        VBox boxOben = new VBox(oben);
        boxOben.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(boxOben, boxUnten);
    }

    /**
     * Initialisiert eine Spieler-Anzeige mit Farbe, Achievements und Listener, ob der Spieler der aktuelle ist.
     * @param spielerNummer Die Nummer des Spielers im Spiel, 1, 2, 3 oder 4
     * @param spielerListe Die Liste der Spieler
     * @return Eine VBox, die alle Informationen über den Spieler anzeigt.
     */
    private VBox initSpielerAnzeige(int spielerNummer, ArrayList<Spieler> spielerListe) {
        VBox anzeige = new VBox();
        anzeige.prefHeightProperty().bind(stage.heightProperty());
        anzeige.setAlignment(Pos.TOP_CENTER);
        anzeige.getStyleClass().add("spieler-box");
        anzeige.setMaxHeight(160);

        StackPane spielerFarbe = new StackPane();
        Circle farbKreis = new Circle(20);
        farbKreis.setFill(getfarbeByNummer(spielerNummer));
        Label nummerLabel = new Label("" + (spielerNummer + 1));
        nummerLabel.getStyleClass().add("spieler-nummer");
        spielerFarbe.getChildren().addAll(farbKreis, nummerLabel);
        spielerFarbe.setAlignment(Pos.CENTER);
        spielerFarbe.setPadding(new Insets(10, 0,0,0));

        ListView<HBox> achievements = new ListView<>();
        ObservableMap<String, Integer> achievementsMap = spielerListe.get(spielerNummer).getAchievements();
        achievements.setMaxHeight(100);
        achievementsMap.addListener((MapChangeListener<String, Integer>) change ->
                updateAchievements(achievementsMap, achievements)
        );
        achievements.getStyleClass().add("achievements");


        Label spielerName = new Label(spielerListe.get(spielerNummer).getName());
        spielerName.setId("schriftMittel");
        anzeige.getChildren().addAll(spielerFarbe, spielerName, achievements);
        updateAchievements(achievementsMap, achievements);
        anzeige.setAlignment(Pos.CENTER);


        if (spielerNummer == 0) {
            anzeige.getStyleClass().add(aktiv);
        }


        aktuellerSpieler.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == spielerNummer) {
                anzeige.getStyleClass().add(aktiv);
            } else
                anzeige.getStyleClass().remove(aktiv);
         });

        return anzeige;
    }

    public static Color getfarbeByNummer(int nr) {
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

    /**
     * Aktualisiert die Anzeige der Achievements in der Player-Anzeige.
     *
     * @param achievementMap Das Dictionary, das die Achievements.
     * @param achievements Die ListView, die die Achievements anzeigt.
     */
    private void updateAchievements(ObservableMap<String, Integer> achievementMap, ListView<HBox> achievements) {
        class Herzen extends HBox {
            private Herzen() {
                int leben = achievementMap.get("leben");
                this.setAlignment(Pos.BASELINE_CENTER);
                for (int i = 0; i < leben; i++) {
                    Label l = new Label();
                    l.getStyleClass().add("leben-icon");
                    l.setMinSize(30, 30);
                    getChildren().add(l);
                }

                if (leben == 0) {
                    Label l = new Label();
                    l.getStyleClass().add("kein-leben-icon");
                    l.setMinSize(30, 30);
                    getChildren().add(l);
                }
            }
        }

        achievements.getItems().clear();
        achievements.getItems().add(new Herzen());

        int anz = achievementMap.get("fragenRichtig");
        Label fragenRichtig = new Label("  " + anz);
        HBox icon = new HBox();
        icon.setId("richtigeAntworten");
        icon.setPrefSize(30,30);

        fragenRichtig.setTextAlignment(TextAlignment.CENTER);
        HBox hb = new HBox(icon, fragenRichtig);
        hb.setAlignment(Pos.CENTER);
        achievements.getItems().add(hb);
    }

    public void setAktuellerSpieler(int i) {
        aktuellerSpieler.setValue(i);
    }


}
