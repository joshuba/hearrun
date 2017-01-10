package hearrun.view.layout;

import javafx.scene.layout.HBox;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Feld extends HBox{
    private Feldtyp feldtyp;
    private static  double height = 120;
    private static double weight = 120;


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

        this.setMinHeight(height);
        this.setMinWidth(weight);
    }

    public Feldtyp getFeldtyp(){
        return this.feldtyp;
    }




}
