package hearrun.view.layout;

import hearrun.business.Main;
import hearrun.business.Spieler;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

/**
 * Created by joshuabarth on 09.01.17.
 */
public class SpielerAnzeige extends VBox {
    public SpielerAnzeige(ArrayList<Spieler> spielerListe, String position) {
        this.setId("sideBar");
        this.setMaxWidth(200);


        FlowPane oben;

        FlowPane unten;

        if (position.equals("links")) {
            // Spielernamen anzeigen
            oben = initSpielerAnzeige(0, spielerListe);


            if (spielerListe.size() > 2) { // Mehr als 2 Spieler: zeige oben und unten einen Spieler an.
                unten = initSpielerAnzeige(2, spielerListe);
            } else { // sonst unten leer lassen
                unten = new FlowPane();
            }
        } else { // Position "rechts"
            if (spielerListe.size() > 1) { // Mehr als 1 Spieler: zeige oben einen Spieler an.
                oben = initSpielerAnzeige(1, spielerListe);

                if (spielerListe.size() > 3) { // 4 Spieler: zeige unten auch einen Spieler an.
                    unten = initSpielerAnzeige(3, spielerListe);
                } else { // Sonst lass unten leer
                    unten = new FlowPane();
                }

            } else { // nur ein Spieler: lass oben und unten leer.
                oben = new FlowPane();
                unten = new FlowPane();
            }
        }

        this.getChildren().addAll(oben, unten);
    }

    private FlowPane initSpielerAnzeige(int spielerNummer, ArrayList<Spieler> spielerListe) {
        FlowPane anzeige = new FlowPane();

        Circle spielerFarbe = new Circle(20);
        Label spielerName = new Label(spielerListe.get(spielerNummer).getName());
        spielerFarbe.setFill(getfarbeByNummer(spielerNummer));
        anzeige.getChildren().addAll(spielerFarbe, spielerName);

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
}
