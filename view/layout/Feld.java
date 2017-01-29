package hearrun.view.layout;

import hearrun.business.Fragetyp;
import hearrun.view.controller.SpielController;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Feld extends HBox{
    private Feldtyp feldtyp;




    public Feld(Feldtyp feldtyp, ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty){
        this.feldtyp = feldtyp;
        setLeer();
        /*
        RotateTransition rt = new RotateTransition(Duration.millis(1000), this);
        rt.setByAngle(180);
        rt.setCycleCount(200);
        rt.setAutoReverse(true);
        rt.play();

*/




        this.prefHeightProperty().bind(heightProperty);
        this.prefWidthProperty().bind(widthProperty);
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

            case EreignisFeld: return Fragetyp.Ereignis;
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
        ScaleTransition st = new ScaleTransition(Duration.millis(50), this);
        st.setByX(0.2f);
        st.setByY(0.2f);
        st.setAutoReverse(false);

        ScaleTransition st2 = new ScaleTransition(Duration.millis(50), this);
        st2.setByX(-0.2f);
        st2.setByY(-0.2f);
        st2.setAutoReverse(false);



        KeyFrame k1 = new KeyFrame(Duration.millis(200), a ->{
            st.play();
        });
        KeyFrame k2 = new KeyFrame(Duration.millis(100), a ->{
            st2.play();
        });
        Timeline t = new Timeline(k2,k1);
        t.play();



    }










}
