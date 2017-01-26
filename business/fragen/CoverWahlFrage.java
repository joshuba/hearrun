package hearrun.business.fragen;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.business.exceptions.TagNeededException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Beschreibung
 */
public class CoverWahlFrage extends Frage {
    private Image[] antworten;
    private String path;

    public CoverWahlFrage(String fragetext, int richtigIndex, Image[] antworten, String path) {
        super(fragetext, null, richtigIndex);
        this.antworten = antworten;
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    public static CoverWahlFrage generiereFrage(String path, Image[] alleCover) throws TagNeededException {
        String titel;
        Image srcCover = null;
        ArrayList<Image> antworten = new ArrayList<>();

        try {
            ID3v2 srctags = new Mp3File(path).getId3v2Tag();


            // Titel einlesen
            if (srctags.getTitle() == null || srctags.getAlbumImage() == null)
                throw new TagNeededException();
            else {
                titel = srctags.getTitle();
                srcCover = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(srctags.getAlbumImage())), null);
            }


        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        // Antworten generieren
        for (int i = 0; i < 3; i++) {
            Image randAntwort = alleCover[(int) (Math.random() * alleCover.length)];
            if (!randAntwort.equals(srcCover))
                antworten.add(randAntwort);
            else
                i--;
        }
        antworten.add(srcCover);
        Collections.shuffle(antworten);

        // richtigIndex erzeugen
        int richtigIndex = -1;
        for (int i = 0; i < antworten.size(); i++)
            if (antworten.get(i).equals(srcCover))
                richtigIndex = i;


        // Fragetext generieren
        String[] texte = {
                "Der folgende Titel erschien in einem der folgenden Alben. Welches ist es?",
                "Als Teil welches Albums erschien der folgende Titel?"
        };
        String fragetext = texte[(int) (Math.random() * texte.length)];

        return new CoverWahlFrage(fragetext, richtigIndex, antworten.toArray(new Image[antworten.size()]), path);
    }

    public Image getCover(int i){
        return antworten[i];
    }



}

