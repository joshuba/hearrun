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

    }

    public void waehleMapErstelleSpiel(String dateiname){
        this.aktSpiel = new Spiel(dateiname, 1); //TODO SPIELERANZAHL
        this.layout = new CompleteLayout(stage, aktSpiel);
        layout.getViewController().baueSpielfeldAuf();
        layout.getViewController().setFeldId(0,0,"lol");
    }


    public CompleteLayout getLayout(){
        return this.layout;
    }

    public void moveSpieler(){
        layout.getViewController().nextPossibleField(aktSpiel.getAktSpieler());

    }





}
