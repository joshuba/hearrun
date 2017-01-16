package hearrun.view.layout;

import java.util.Random;

/**
 * Created by joshuabarth on 10.01.17.
 */
public enum Feldtyp {
    CoverFeld, FaktFeld, InterpretFeld, TitelFeld, EreignisFeld, LeeresFeld, EndFeld;



    public static Feldtyp getRandomFeldtyp()  {
        Feldtyp feldtyp = Feldtyp.values()[new Random().nextInt(Feldtyp.values().length)];
        while(feldtyp == Feldtyp.LeeresFeld || feldtyp == Feldtyp.EndFeld){
            feldtyp = Feldtyp.values()[new Random().nextInt(Feldtyp.values().length)];

        }
        return feldtyp;

    }

}


