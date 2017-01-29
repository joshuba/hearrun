package hearrun.business.ereignisse;

import java.util.Random;

/**
 * Created by Leo on 29.01.2017
 */
public enum Ereignis {
    LEBEN, LAUFEN_POSITIV, LAUFEN_NEGATIV;

    public static Ereignis zufallsEreignis() {
        int random = new Random().nextInt(2);
        if (random == 0)
            return LEBEN;
        else if(random == 1)
            return LAUFEN_POSITIV;
        else
            return LAUFEN_NEGATIV;
    }
}
