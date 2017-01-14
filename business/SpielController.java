package hearrun.business;

import hearrun.view.layout.GameLayout;
import hearrun.view.layout.CompleteLayout;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 12.01.17.
 */
public class SpielController {
    private Spiel aktSpiel;
    private CompleteLayout completeLayout;
    private Stage stage;
    private String dateiname;
    private int spieleranzahl;



    public SpielController(Stage stage, String dateiname, int spieleranzahl){
        this.stage = stage;
        this.dateiname = dateiname;
        this.spieleranzahl = spieleranzahl;

        this.completeLayout = new CompleteLayout(stage, this);



    }

    private void waehleMapErstelleSpiel(String dateiname, int spieleranzahl){

        this.aktSpiel = new Spiel(dateiname, spieleranzahl, completeLayout.getViewController()); //Erstelle spiel
        completeLayout.getViewController().baueSpielfeldAuf();
        completeLayout.getViewController().setFeldId(0,0, completeLayout.getViewController().erkenneFeldId(0,0)); //Setze Alle Player aufs erste Feld

    }


    public CompleteLayout getLayout(){
        return this.completeLayout;
    }

    public void moveAktSpieler(int schritte) {
        completeLayout.getViewController().moveForward(schritte,aktSpiel.getAktSpieler());

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

    public void starteSpiel(){
        if(aktSpiel != null){
            beendeSpiel();
        }
        completeLayout.getViewController().resetGameLayout();
        waehleMapErstelleSpiel(dateiname, spieleranzahl);
        completeLayout.getViewController().setGameLayout();

    }

    public void beendeSpiel(){
        this.aktSpiel = null;
        this.completeLayout.getViewController().resetGameLayout();
    }

    public void beendeProgramm(){
        stage.close();
    }







}
