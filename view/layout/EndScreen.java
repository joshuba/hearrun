package hearrun.view.layout;

import hearrun.model.Spieler;
import hearrun.controller.SpielController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

import java.util.*;

/**Finale Zusammenfassung des Spiels. Zeigt in Form von Listen einige Statistiken an und makiert mittels einer Animation
 * den Gewinner
 *
 * @author Leo Back & Joshua Barth
 */
public class EndScreen extends BorderPane {
    private SpielController spielController;
    private HBox oben;
    private FlowPane mitte;
    private HBox unten;
    private Label ueberschrift;
    private Button back;
    private ListView<HBox> [] boxen;
    private int gewinner;


    public EndScreen(SpielController spielController){
        boxen = new ListView[4];
        this.spielController = spielController;
        back = new Button("Hauptmenü");
        gewinner = spielController.getAktSpiel().getAktSpieler().getNr();


        spielController.getEffectPlayer().play("/hearrun/resources/sounds/sieg.mp3");
        spielController.getLayout().getViewController().gameLayoutBlury(true);
        showResults();
        animateWinner();

        back.setOnAction(e -> {
            spielController.beendeSpiel();
            spielController.getLayout().getMainMenu().setContinue(false);
            spielController.getLayout().getViewController().setMainMenu();
        });



    }

    public void showResults(){
        ueberschrift = new Label(spielController.getAktSpiel().getAktSpieler().getName() + " hat gewonnen!");
        oben = new HBox();
        mitte = new FlowPane(Orientation.HORIZONTAL);
        unten = new HBox();
        int gewinnerID = spielController.getAktSpiel().getAktSpieler().getNr();

        for(Spieler s: spielController.getSpielerListe()){
            ListView<HBox> lv = new ListView();
            lv.getStyleClass().add("maps-spieler-liste");
            if(s.getNr() == gewinnerID){
                lv.setId("gewinnerListView");
            }
            lv.getItems().addAll(baueListe(s));
            boxen[s.getNr()] = lv;
            mitte.getChildren().addAll(lv);

            //Styling Listview
            lv.prefHeightProperty().bind(spielController.getStage().heightProperty().divide(3.5));
            lv.prefWidthProperty().setValue(350);
        }

        oben.getChildren().add(ueberschrift);
        unten.getChildren().addAll(back);

        this.setTop(oben);
        this.setCenter(mitte);
        this.setBottom(unten);


        //styling
        oben.setAlignment(Pos.CENTER);
        oben.setId("grossText");
        ueberschrift.setId("grossText");
        oben.setPadding(new Insets(40,0,0,0));
        mitte.setAlignment(Pos.CENTER);
        mitte.setHgap(40);
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
        farbeName.setId("endScreenFill"+(s.getNr()+1));
        farbeName.setPadding(new Insets(20,0,20,0));
        farbeName.setSpacing(4);
        farbeName.setAlignment(Pos.CENTER);
        String nameS = s.getName();
        Label name = new Label(s.getName());
        name.setId("grossText");


        Circle farbKreis = new Circle(20);
        farbKreis.setFill(SpielerAnzeige.getfarbeByNummer(s.getNr()));
        farbeName.getChildren().addAll(farbKreis, name);

        //Box mit gelaufenen Feldern
        HBox gelaufen = new HBox();
        gelaufen.setAlignment(Pos.CENTER_LEFT);
        HBox icon = new HBox();
        icon.setMinSize(21,21);
        icon.setId("gelaufeneFelder");
        Label anz = new Label(" Gelaufene Felder:\t\t\t" + a.get("felder").toString());
        gelaufen.getChildren().addAll(icon, anz);

        //Box mit richtig beantworteten Fragen
        HBox fragenRichtig = new HBox();
        fragenRichtig.setAlignment(Pos.CENTER_LEFT);
        HBox icon2 = new HBox();
        icon2.setMinSize(21,21);
        icon2.setId("richtigeAntworten");
        Label richtig = new Label(" Richtige Antworten:\t\t" + a.get("fragenRichtig").toString()+ "x");
        fragenRichtig.getChildren().addAll(icon2, richtig);

        //Box mit falsch beantworteten Fragen
        HBox fragenFalsch = new HBox();
        fragenFalsch.setAlignment(Pos.CENTER_LEFT);
        HBox icon3 = new HBox();
        icon3.setMinSize(21,21);
        icon3.setId("falscheAntworten");
        Label falsch = new Label(" Falsche Antworten:\t\t\t" + a.get("fragenFalsch").toString()+ "x");
        fragenFalsch.getChildren().addAll(icon3, falsch);

        //Box mit falsch beantworteten Fragen
        HBox frageZeit = new HBox();
        frageZeit.setAlignment(Pos.CENTER_LEFT);
        HBox icon4 = new HBox();
        icon4.setMinSize(21,21);
        icon4.setId("zeitAbgelaufen");
        Label abgelaufen = new Label(String.format(" Zeit abgelaufen:\t\t\t%dx", a.get("zeitAbgelaufen")));
        frageZeit.getChildren().addAll(icon4, abgelaufen);

        //Box mit Leben am Ende
        HBox lebenAmEnde = new HBox();
        lebenAmEnde.setAlignment(Pos.CENTER_LEFT);
        HBox icon5 = new HBox();
        icon5.setMinSize(21,21);
        icon5.setId("lebenAmEnde");
        Label leben = new Label(String.format(" Eingesetzte Leben:\t\t\t%d", a.get("usedHearts")));
        lebenAmEnde.getChildren().addAll(icon5, leben);

        //Box mit Durchschnittswert
        HBox durchschnittsbox = new HBox();
        durchschnittsbox.setAlignment(Pos.CENTER_LEFT);
        HBox icon6 = new HBox();
        icon6.setMinSize(21,21);
        icon6.setId("durchschnitt");
        float durchschnitt = a.get("fragenFalsch") + a.get("fragenRichtig") + a.get("zeitAbgelaufen");
        //Falls anz nicht 0 ist
        if(durchschnitt != 0){
            durchschnitt = a.get("fragenRichtig") / durchschnitt;
        }
        Label d = new Label(" Im Durchschnitt richtig:\t\t" + (int)(durchschnitt * 100) + "%");
        durchschnittsbox.getChildren().addAll(icon6, d);



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

    public void animateWinner(){
        KeyFrame k1 = new KeyFrame(Duration.millis(1), a ->{
            boxen[gewinner].setStyle("-fx-effect: inherit ;");
        });

        KeyFrame k2 = new KeyFrame(Duration.millis(350), a ->{
            boxen[gewinner].setStyle("-fx-effect: dropshadow(three-pass-box, #7DFC56, 20, 0.8, 0, 0);");


        });

        Timeline t = new Timeline();
        t.setAutoReverse(true);
        t.setCycleCount(Animation.INDEFINITE);
        t.getKeyFrames().addAll(k1,k2);

         t.play();

    }


}
