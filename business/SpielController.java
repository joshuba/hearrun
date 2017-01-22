package hearrun.business;

import hearrun.business.fragen.Frage;
import hearrun.view.layout.Feld;
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
    private FrageController frageController;
    private Player musicPlayer;
    private Player effectPlayer;



    public SpielController(Stage stage, String dateiname, int spieleranzahl){
        this.stage = stage;
        this.musicPlayer = new Player();
        this.effectPlayer = new Player();
        this.dateiname = dateiname;
        this.spieleranzahl = spieleranzahl;
        this.completeLayout = new CompleteLayout(stage, this);
        frageController = new FrageController();




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
        completeLayout.getViewController().movePlayer(schritte,aktSpiel.getAktSpieler());

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
        musicPlayer.stop();
    }

    public void beendeProgramm(){
        musicPlayer.stop();
        effectPlayer.stop();
        stage.close();
    }

    public void stelleAktFrage(){
        Feld aktFeld = aktSpiel.getAktMap().getFeld(aktSpiel.getAktSpieler().getAktX(), aktSpiel.getAktSpieler().getAktY());
        Fragetyp fragetyp = aktFeld.getPassendenFragetyp();
        Frage frage = frageController.getFrage(fragetyp);

        //Zeige frage
        getLayout().getViewController().zeigeFrage(frage, fragetyp);



    }
    public Player getMusicPlayer(){
        return this.musicPlayer;
    }

    public Player getEffectPlayer(){
        return this.effectPlayer;
    }

    public void stelleFrage(){
        stelleAktFrage();

    }

    public void moveAndAskNext(int schritte){
        completeLayout.getViewController().movePlayer(schritte,aktSpiel.getAktSpieler());
        nextSpieler();

    }









}
