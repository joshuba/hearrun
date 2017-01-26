package hearrun.view.layout;

import hearrun.business.Fragetyp;
import hearrun.view.controller.ViewController;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Feld extends HBox{
    private Feldtyp feldtyp;



    public Feld(Feldtyp feldtyp, ViewController viewController){
        this.feldtyp = feldtyp;
        setLeer();
        /*
        RotateTransition rt = new RotateTransition(Duration.millis(1000), this);
        rt.setByAngle(180);
        rt.setCycleCount(200);
        rt.setAutoReverse(true);
        rt.play();
*/


        this.prefHeightProperty().bind(viewController.getStage().heightProperty());
        this.prefWidthProperty().bind(viewController.getStage().widthProperty());
    }

    public Feldtyp getFeldtyp(){
        return this.feldtyp;
    }

    public void setBesetztID(String id){
        this.setId(id);
    }

    public void setLeer(){
        switch(feldtyp){
            case CoverFeld: this.setId("coverfeldleer");
                break;
            case EreignisFeld: this.setId("ereignisfeldleer");
                break;
            case FaktFeld: this.setId("faktfeldleer");
                break;
            case InterpretFeld: this.setId("interpretfeldleer");
                break;
            case TitelFeld: this.setId("titelfeldleer");
                break;
            case EndFeld: this.setId("endfeldleer");
                break;
        }
    }

    public Fragetyp getPassendenFragetyp(){
        switch(this.feldtyp){
            case CoverFeld:
                double rand = Math.random() * 1;
                System.out.println(rand);
                if (Math.round(rand) == 1) {
                    return Fragetyp.CoverTitelFrage;
                }
                return Fragetyp.CoverWahlFrage;

            case EreignisFeld:
                System.out.println("TODO: Ereignisfeler");
                return null;

            case FaktFeld: return Fragetyp.FaktFrage;
            case InterpretFeld: return Fragetyp.InterpretFrage;
            case TitelFeld: return Fragetyp.Titelfrage;
            case EndFeld:
                System.out.println("TODO: Gewinnfeld");
                return null;

        }
        return null;
    }

    public String getLeerId(){
        switch(feldtyp){
            case CoverFeld: return "coverfeldleer";
            case EreignisFeld: return"ereignisfeldleer";
            case FaktFeld: return "faktfeldleer";
            case InterpretFeld: return"interpretfeldleer";
            case TitelFeld: return "titelfeldleer";
            case EndFeld: return "endfeldleer";

        }
        return null;


    }

    public void zoomIn(){
        ScaleTransition st = new ScaleTransition(Duration.millis(2000), this);
        st.setByX(1.2f);
        st.setByY(1.2f);
        st.setAutoReverse(true);

        st.play();
    }






}
