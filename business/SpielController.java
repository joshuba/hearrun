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

    public SpielController(Stage stage, String dateiname){
        this.stage = stage;
        waehleMapErstelleSpiel(dateiname, 4);


    }

    private void waehleMapErstelleSpiel(String dateiname, int spieleranzahl){
        this.aktSpiel = new Spiel(dateiname, spieleranzahl); //TODO SPIELERANZAHL
        this.layout = new CompleteLayout(stage, this);
        layout.getViewController().baueSpielfeldAuf();
        layout.getViewController().setFeldId(0,0,layout.getViewController().erkenneFeldId(0,0)); //Setze Alle Player aufs erste Feld

    }


    public CompleteLayout getLayout(){
        return this.layout;
    }

    public void moveAktSpieler(int schritte) {
            layout.getViewController().moveForward(schritte,aktSpiel.getAktSpieler());
            getAktSpiel().nextSpieler();

    }

    public Spiel getAktSpiel(){
        return aktSpiel;
    }

    private void setzeSpielerAnzahl(int anz){
        for(int i = 0; i<anz; i++){
            aktSpiel.erstelleSpieler("testspieler_" + (i+1));

        }

    }





}
