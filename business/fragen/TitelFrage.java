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
 * Created by Josh on 28.12.16.
 */
public class TitelFrage extends Frage {
    private String path;

    public TitelFrage(String fragetext, String[] antworten, int richtigIndex, String path) {
        super(fragetext, antworten, richtigIndex);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static TitelFrage generiereFrage(String path, ID3v2[] alleTitel) throws TagNeededException {

        // Fragetext generieren
        String[] texte = {"Aus welchem Song stammt der abgespielte Songschnipsel?",
                "Zu welchem der 4 Songs lässt sich das Songschnipsel zuordnen?",
                "Welcher Song wird gerade abgespielt?"};
        String fragetext = texte[(int) (Math.random() * texte.length)];
        String titel = "";
        try {
            titel = new Mp3File(path).getId3v2Tag().getTitle();
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        if (titel == null)
            throw new TagNeededException();

        ArrayList<String> antworten = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ID3v2 randAntwort = alleTitel[(int) (Math.random() * alleTitel.length)];
            if (!(randAntwort.getTitle() == null) &&
                    !randAntwort.getTitle().equals(titel)) { // zweifach richtige antwort durch Titel vermeiden

                boolean add = true;

                for (String s : antworten) {
                    if (s.equals(randAntwort.getTitle())) {
                        i--;
                        add = false;
                        break;
                    }
                }

                antworten.add(randAntwort.getTitle());
            } else
                i--;
        }
        antworten.add(titel);
        Collections.shuffle(antworten);

        int richtigIndex = -1;
        //richtigIndex finden
        for (int i = 0; i < antworten.size(); i++) {
            if (antworten.get(i).equals(titel)) {
                richtigIndex = i;
                break;
            }
        }

        return new TitelFrage(fragetext, antworten.toArray(new String[antworten.size()]), richtigIndex, path);
    }
}
