package hearrun.view.layout;

import hearrun.business.Fragetyp;
import hearrun.view.controller.SpielController;
import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**Ein einzelnes Feld. Hat immer einen klar zugewiesenen Feldtyp. Besitzt einige Funktionen wie das aktuelles Feld makieren
 *
 * @author Leo Back & Joshua Barth
 */
public class Feld extends StackPane{
    private Feldtyp feldtyp;
    private int x;
    private int y;


    public Feld(Feldtyp feldtyp, ReadOnlyDoubleProperty widthProperty, ReadOnlyDoubleProperty heightProperty, int x, int y){
        this.feldtyp = feldtyp;
        setLeer();
        this.x = x;
        this.y = y;

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
        double x = this.getScaleX();
        double y = this.getScaleY();

        ScaleTransition st = new ScaleTransition(Duration.millis(80), this);
        st.setFromY(0.6);
        st.setFromY(0.6);
        st.setToY(1.0);
        st.setToX(1.0);


        ScaleTransition st2 = new ScaleTransition(Duration.millis(120), this);
        st2.setFromY(1.0);
        st2.setFromX(1.0);
        st2.setToY(0.6);
        st2.setToX(0.6);




        KeyFrame k1 = new KeyFrame(Duration.millis(100), a ->{
            st.play();
        });
        KeyFrame k2 = new KeyFrame(Duration.millis(1), a ->{
            st2.play();
        });

        Timeline t = new Timeline(k2,k1);
        st2.setOnFinished(e -> {
            //st3.play();
            resetSizeZoom();



        });
        t.play();



    }

    public int getX(){
        return this.x;

    }

    public int getY(){
        return this.y;
    }

    public void aktuellesFeldMakierung(boolean anAus){
        if(anAus){
            HBox aktBild = new HBox();
            aktBild.setId(this.getId());
            aktBild.setAlignment(Pos.CENTER);
            aktBild.setStyle("-fx-background-size: 80%");




            HBox ladeRad = new HBox();
            ladeRad.setId("aktFeldMakierung");
            ladeRad.setAlignment(Pos.CENTER);


            this.getChildren().removeAll(this.getChildren());
            this.getChildren().addAll(ladeRad, aktBild);




                FadeTransition ft = new FadeTransition(Duration.millis(1000), ladeRad);
                ft.setFromValue(100);
                ft.setToValue(0);
                ft.setCycleCount(Animation.INDEFINITE);
               ft.setAutoReverse(true);
               ft.play();



             ScaleTransition st = new ScaleTransition(Duration.millis(1000), aktBild);
                st.setFromY(getScaleY()+0.1);
                st.setFromX(getScaleY()+0.1);
                st.setToY(0.9);
                st.setToX(0.9);
                st.setCycleCount(Animation.INDEFINITE);
                st.setAutoReverse(true);

                st.play();

                // Damit KeyListener funktioniert.
                this.requestFocus();
        }else{
            this.getChildren().removeAll(this.getChildren());
            this.setLayoutX(1);
            this.setLayoutY(1);
            this.setScaleX(1);
            this.setScaleY(1);
            resetSizeZoom();




        }

    }
    public void resetSizeZoom(){
        this.setOpacity(150);
        this.setLayoutX(0);
        this.setLayoutY(0);
        this.setScaleX(1);
        this.setScaleY(1);
    }



}
