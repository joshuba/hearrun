package hearrun.view.layout;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Feld extends HBox{
    private Feldtyp feldtyp;



    public Feld(Feldtyp feldtyp){
        this.feldtyp = feldtyp;
        switch(feldtyp){
            case CoverFeld: this.setId("coverFeldLeer");
            break;
            case EreignisFeld: this.setId("ereignisFeldLeer");
            break;
            case FaktFeld: this.setId("faktFeldLeer");
            break;
            case InterpretFeld: this.setId("interpretFeldLeer");
            break;
            case TitelFeld: this.setId("titelFeldLeer");
            break;
            case EndFeld: this.setId("endFeldLeer");
            break;
        }
        this.setMinHeight(80);
        this.setMinWidth(80);
       //this.prefHeightProperty().bind(stage.heightProperty());
        //System.out.println(this.prefHeightProperty().getValue());
        //this.prefWidthProperty().bind(stage.widthProperty());
    }

    public Feldtyp getFeldtyp(){
        return this.feldtyp;
    }




}
