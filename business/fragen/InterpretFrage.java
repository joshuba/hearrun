package hearrun.business.fragen;

import hearrun.business.Track;

/**
 * Created by Josh on 28.12.16.
 */
public class InterpretFrage extends Frage {
    Track song;

    public InterpretFrage(String fragetext, String[] antworten, int richtigIndex) {
        super(fragetext, antworten, richtigIndex);
    }
}
