package hearrun.model.ereignisse;

import java.util.Random;

/**
 * @author Leo Back & Joshua Barth
 */
public enum Ereignis {
    LEBEN, LAUFEN_POSITIV, LAUFEN_NEGATIV;

    public static Ereignis zufallsEreignis() {
        int random = new Random().nextInt(3);
        if (random == 0)
            return LEBEN;
        else if(random == 1)
            return LAUFEN_POSITIV;
        else
            return LAUFEN_NEGATIV;
    }
}
