package hearrun.view.layout;

import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Feld extends HBox{
    private Feldtyp feldtyp;
    private boolean besetzt;




    public Feld(Feldtyp feldtyp){
        this.feldtyp = feldtyp;
        setLeer();

        this.setMinHeight(30);
        this.setMinWidth(30);
       //this.prefHeightProperty().bind(stage.heightProperty());
        //System.out.println(this.prefHeightProperty().getValue());
        //this.prefWidthProperty().bind(stage.widthProperty());
    }

    public Feldtyp getFeldtyp(){
        return this.feldtyp;
    }

    public void setBesetzt(String id){
        this.setId(id);
    }

    public void setLeer(){
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
    }






}
