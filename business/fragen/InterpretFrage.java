package hearrun.business.fragen;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.business.exceptions.TagNeededException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by  Josh on 28.12.16.
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

    public static InterpretFrage generiereFrage(String path, ID3v2[] alleInterpreten) throws TagNeededException{

        // Fragetext generieren
        String[] texte = {"Der ausschnitt aus dem Song stammt von einem der folgenden Interpreten. Welcher ist der richtige?",
            "Zu welchem der 4 Interpreten lässt sich das Songschnipsel zuordnen?",
            "Welcher Interpret hat den Song geschrieben?"};
        String fragetext = texte[(int) (Math.random() * texte.length)];
        String interpret = "";
        try {
            interpret = new Mp3File(path).getId3v2Tag().getArtist();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        ArrayList<String> antworten = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            ID3v2 randAntwort = alleInterpreten[(int) (Math.random() * alleInterpreten.length)];
            if(!randAntwort.getArtist().equals(interpret)) {//zweifach richtige antwort durch Interpret vermeiden
                antworten.add(randAntwort.getTitle());
            }else
                i--;
        }
        antworten.add(interpret);
        Collections.shuffle(antworten);

        int richtigIndex = -1;
        //richtigIndex finden
        for (int i = 0; i < antworten.size(); i++) {
            if(antworten.get(i).equals(interpret)){
                richtigIndex = i;
                break;
            }
        }

        return new InterpretFrage(fragetext, antworten.toArray(new String[antworten.size()]), richtigIndex, path);
    }
}
