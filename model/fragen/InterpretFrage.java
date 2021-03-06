package hearrun.model.fragen;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Leo Back & Joshua Barth
 */
public class InterpretFrage extends Frage {
    private String path;

    public InterpretFrage(String fragetext, String[] antworten, int richtigIndex, String path) {
        super(fragetext, antworten, richtigIndex);
        this.path = path;
    }

    public String getPath(){
        return path;
    }


    public static InterpretFrage generiereFrage(String path, ID3v2[] alleInterpreten) throws hearrun.model.exceptions.TagNeededException {

        // Fragetext generieren
        String[] texte = {"Der Ausschnitt aus dem Song stammt von einem der folgenden Interpreten. \nWelcher ist der Richtige?",
            "Zu welchem der 4 Interpreten lässt sich der Songschnipsel zuordnen?",
            "Welcher Interpret hat diesen Song geschrieben?"};
        String fragetext = texte[(int) (Math.random() * texte.length)];
        String interpret = "";
        try {
            interpret = new Mp3File(path).getId3v2Tag().getArtist();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        if (interpret == null)
            throw new hearrun.model.exceptions.TagNeededException();

        ArrayList<String> antworten = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            ID3v2 randAntwort = alleInterpreten[(int) (Math.random() * alleInterpreten.length)];
            if (randAntwort.getArtist() == null)
                throw new hearrun.model.exceptions.TagNeededException();

            if(!(randAntwort.getArtist() == null) &&
                    !randAntwort.getArtist().equals(interpret)) { //zweifach richtige antwort durch Interpret vermeiden
                boolean add = true;
                for (String s : antworten)
                    if (s.equals(randAntwort.getArtist())) {
                        i--;
                        add = false;
                        break;
                    }

                if (add)
                    antworten.add(randAntwort.getArtist());
            }else
                i--;
        }
        antworten.add(interpret);
        Collections.shuffle(antworten);


        int richtigIndex = -1;
        //richtigIndex finden
        for (int i = 0; i < antworten.size(); i++) {
            if(antworten.get(i).equals(interpret)) {
                richtigIndex = i;
                break;
            }
        }

        return new InterpretFrage(fragetext, antworten.toArray(new String[antworten.size()]), richtigIndex, path);
    }
}
