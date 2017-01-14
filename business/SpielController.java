package hearrun.business;

import hearrun.view.layout.CompleteLayout;
import hearrun.view.layout.MainMenu;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 12.01.17.
 */
public class SpielController {
    private Spiel aktSpiel;
    private CompleteLayout layout;
    private Stage stage;


    public SpielController(Stage stage, String dateiname, int spieleranzahl){
        this.stage = stage;

        starteSpiel(dateiname,spieleranzahl);

    }

    private void waehleMapErstelleSpiel(String dateiname, int spieleranzahl){
        this.layout = new CompleteLayout(stage, this); //Baue Layout auf
        this.aktSpiel = new Spiel(dateiname, spieleranzahl, layout.getViewController()); //Erstelle spiel
        layout.getViewController().baueSpielfeldAuf();
        layout.getViewController().setFeldId(0,0,layout.getViewController().erkenneFeldId(0,0)); //Setze Alle Player aufs erste Feld

    }


    public CompleteLayout getLayout(){
        return this.layout;
    }

    public void moveAktSpieler(int schritte) {
            layout.getViewController().moveForward(schritte,aktSpiel.getAktSpieler());

    }

    public void nextSpieler(){
        this.getAktSpiel().nextSpieler();
    }

    public Spiel getAktSpiel(){
        return aktSpiel;
    }

    public void setPlayerName(int nr, String name){
        aktSpiel.getSpielerByNr(nr).setName(name);
    }

    public void starteSpiel(String dateiname, int spieleranzahl){
        waehleMapErstelleSpiel(dateiname, spieleranzahl);

    }







}
