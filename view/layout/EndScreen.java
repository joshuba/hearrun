package hearrun.view.layout;

import hearrun.Main;
import hearrun.business.Spieler;
import hearrun.view.controller.SpielController;
import javafx.collections.ObservableMap;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Josh on 02.02.17.
 */
public class EndScreen extends BorderPane {
    private SpielController spielController;
    private HBox oben;
    private FlowPane mitte;
    private HBox unten;
    private Label ueberschrift;
    private Button back;

    public EndScreen(SpielController spielController){
        this.spielController = spielController;
        back = new Button("Hauptmenü");


        spielController.getEffectPlayer().play(Main.class.getResource("/hearrun/resources/sounds/sieg.mp3").getPath());
        spielController.getLayout().getViewController().gameLayoutBlury(true);
        showResults();

        back.setOnAction(e -> {
            spielController.beendeSpiel();
            spielController.getLayout().getViewController().setMainMenu();
        });



    }

    public void showResults(){
        ueberschrift = new Label(spielController.getAktSpiel().getAktSpieler().getName() + " hat gewonnen!");
        oben = new HBox();
        mitte = new FlowPane(Orientation.HORIZONTAL);
        unten = new HBox();

        for(Spieler s: spielController.getSpielerListe()){
            ListView<HBox> lv = new ListView();
            lv.setId("achievements");
            lv.getItems().addAll(baueListe(s));
            mitte.getChildren().addAll(lv);

            //Styling Listview
            lv.prefHeightProperty().bind(spielController.getStage().heightProperty().divide(4));
            lv.minWidth(500);
            lv.maxWidth(500);
        }

        oben.getChildren().add(ueberschrift);
        unten.getChildren().addAll(back);

        this.setTop(oben);
        this.setCenter(mitte);
        this.setBottom(unten);


        //styling
        oben.setAlignment(Pos.CENTER);
        oben.setId("grossText");
        oben.setPadding(new Insets(40,0,0,0));
        mitte.setAlignment(Pos.CENTER);
        mitte.setHgap(400);
        mitte.setVgap(50);

        mitte.setColumnHalignment(HPos.CENTER);

        unten.setAlignment(Pos.CENTER);
        unten.setPadding(new Insets(0,0,40,0));

    }

    public ArrayList baueListe(Spieler s){
        ArrayList<HBox> newList = new ArrayList<>();
        ObservableMap<String, Integer> a = s.getAchievements();

        //Box mit Farbe und Name füllen
        HBox farbeName = new HBox();
        farbeName.setSpacing(4);
        farbeName.setAlignment(Pos.CENTER);
        farbeName.setId("grossText");
        Label name = new Label(s.getName());
        Circle farbKreis = new Circle(20);
        farbKreis.setFill(SpielerAnzeige.getfarbeByNummer(s.getNr()));
        farbeName.getChildren().addAll(farbKreis, name);

        //Box mit gelaufenen Feldern
        HBox gelaufen = new HBox();
        Label anz = new Label("Gelaufene Felder:       " + a.get("felder").toString());
        gelaufen.getChildren().addAll(anz);

        //Box mit richtig beantworteten Fragen
        HBox fragenRichtig = new HBox();
        Label richtig = new Label("Richtige Antworten:       " + a.get("fragenRichtig").toString());
        fragenRichtig.getChildren().addAll(richtig);

        //Box mit falsch beantworteten Fragen
        HBox fragenFalsch = new HBox();
        Label falsch = new Label("Falsche Antworten:       " + a.get("fragenFalsch").toString());
        fragenFalsch.getChildren().addAll(falsch);

        //Box mit falsch beantworteten Fragen
        HBox frageZeit = new HBox();
        Label abgelaufen = new Label("Zeit abgelaufen:       " + a.get("zeitAbgelaufen").toString() + "x");
        frageZeit.getChildren().addAll(abgelaufen);

        //Box mit Leben am Ende
        HBox lebenAmEnde = new HBox();
        Label leben = new Label("Leben am Ende:       " + a.get("leben").toString());
        lebenAmEnde.getChildren().addAll(leben);

        //Box mit Durchschnittswert
        HBox durchschnittsbox = new HBox();
        float durchschnitt = a.get("fragenFalsch") + a.get("fragenRichtig") + a.get("zeitAbgelaufen");
        //Falls anz nicht 0 ist
        if(durchschnitt != 0){
            durchschnitt = a.get("fragenRichtig") / durchschnitt;
        }
        DecimalFormat f = new DecimalFormat("#0.00");
        Label d = new Label("Um Durchschnitt richtig:       " + f.format(durchschnitt));
        durchschnittsbox.getChildren().addAll(d);



        //Zusammenfuegen
        newList.add(farbeName);
        newList.add(gelaufen);
        newList.add(fragenRichtig);
        newList.add(fragenFalsch);
        newList.add(frageZeit);
        newList.add(lebenAmEnde);
        newList.add(durchschnittsbox);


        return newList;
    }


}
