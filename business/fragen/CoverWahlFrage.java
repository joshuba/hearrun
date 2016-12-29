package hearrun.business.fragen;

import javafx.scene.image.Image;

/**
 * Beschreibung
 */
public class CoverWahlFrage extends CoverFrage {
    private Image[] anworten;

    public CoverWahlFrage(String fragetext, int richtigIndex, Image[] antworten){
        super(fragetext, null, richtigIndex);
        this.anworten = antworten;
    }

    public Image[] getAnworten(){
        return anworten;
    }


}
