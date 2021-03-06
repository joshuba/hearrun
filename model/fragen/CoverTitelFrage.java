package hearrun.model.fragen;

import com.mpatric.mp3agic.ID3v2;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * @author Leo Back & Joshua Barth
 */
public class CoverTitelFrage extends Frage {

    private Image cover;

    public CoverTitelFrage(String fragetext, String[] antworten, int richtigIndex, Image cover) {
        super(fragetext, antworten, richtigIndex);
        this.cover = cover;

    }

    public static CoverTitelFrage generiereFrage(ID3v2 srctags, ID3v2[] alleTitel) throws hearrun.model.exceptions.TagNeededException {
        try {

            Image cover;
            String titel;

            // Cover einlesen
            if (srctags.getAlbumImage() == null || srctags.getAlbum() == null)
                throw new hearrun.model.exceptions.TagNeededException();
            else
                cover = SwingFXUtils.toFXImage(ImageIO.read(
                        new ByteArrayInputStream(srctags.getAlbumImage())), null);

            // Titel einlesen
            if (srctags.getTitle() == null)
                throw new hearrun.model.exceptions.TagNeededException();
            else
                titel = srctags.getTitle();

            //antworten generieren
            ArrayList<String> antworten = new ArrayList<>();


            for (int i = 0; i < 3; i++) {
                ID3v2 randAntwort = alleTitel[(int) (Math.random() * alleTitel.length)];

                if (randAntwort.getTitle() == null || randAntwort.getAlbum() == null)
                    throw new hearrun.model.exceptions.TagNeededException();
                if (!(randAntwort.getTitle() == null && randAntwort.getAlbum() == null) &&
                        !randAntwort.getTitle().equals(titel) && !randAntwort.getAlbum().equals(srctags.getAlbum())) {//zweifach richtige antwort durch titel / album vermeiden

                    boolean add = true;

                    for (String s : antworten) {
                        if (s.equals(randAntwort.getTitle())) {
                            i--;
                            add = false;
                            break;
                        }
                    }

                    if (add)
                        antworten.add(randAntwort.getTitle());
                } else {
                    i--;
                }

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

            // Fragetext erstellen
            String[] texte = {"Welcher der folgenden Titel ist im obenstehenden Album enthalten?",
                    "Welcher Titel erscheint in diesem Album?",
                    "Auf diesem Album ist einer der folgenden Titel erschienen. Welcher ist es?",
                    "Einer der folgenden Titel ist Teil dieses Albums. Welcher ist es?"};
            String fragetext = texte[(int) (Math.random() * texte.length)];

            return new CoverTitelFrage(fragetext, antworten.toArray(new String[antworten.size()]), richtigIndex, cover);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Image getCover() {
        return this.cover;
    }

}
