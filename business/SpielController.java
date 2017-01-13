package hearrun.business;

import hearrun.view.layout.CompleteLayout;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 12.01.17.
 */
public class SpielController {
    private Spiel aktSpiel;
    private CompleteLayout layout;
    private Stage stage;

    public SpielController(Stage stage){
        this.stage = stage;
        Spieler spieler1 = new Spieler("Hans");

    }

    public void waehleMapErstelleSpiel(String dateiname){
        this.aktSpiel = new Spiel(dateiname);
        this.layout = new CompleteLayout(stage, aktSpiel);
        layout.getViewController().baueSpielfeldAuf();
    }


    public CompleteLayout getLayout(){
        return this.layout;
    }

    public void moveSpieler(int x, int y){
        //spieler.move(x,y);
        layout.getViewController().setFeldId(x,y,"lol");

    }





}
