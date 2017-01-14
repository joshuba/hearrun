package hearrun.view.layout;

import hearrun.view.controller.ViewController;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Feld extends HBox{
    private Feldtyp feldtyp;



    public Feld(Feldtyp feldtyp, ViewController viewController){
        this.feldtyp = feldtyp;
        setLeer();


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






}
