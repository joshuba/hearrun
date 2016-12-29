package hearrun.business.fragen;

import javafx.scene.image.Image;

/**
 * Created by Josh on 28.12.16.
 */
public class CoverTitelFrage extends CoverFrage{

    private Image cover;

    public CoverTitelFrage(String fragetext, String [] antworten, int richtigIndex, Image cover){
        super(fragetext, antworten, richtigIndex);
        this.cover = cover;

    }




}
