package hearrun.business.fragen;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.business.exceptions.TagNeededException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

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

    private CoverWahlFrage(String fragetext, int richtigIndex, Image[] antworten, String path) {
        super(fragetext, null, richtigIndex);
        this.antworten = antworten;
        this.path = path;
    }

    public String getPath() {
        return path;
    }


    public static CoverWahlFrage generiereFrage(String path, Image[] alleCover) throws TagNeededException {

        Image srcCover = null;
        ArrayList<Image> antworten = new ArrayList<>();

        try {
            ID3v2 srctags = new Mp3File(path).getId3v2Tag();


            // Titel einlesen
            if (srctags.getTitle() == null || srctags.getAlbumImage() == null)
                throw new TagNeededException();
            else {
                srcCover = SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(srctags.getAlbumImage())), null);
            }


        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        // Antworten generieren
        for (int i = 0; i < 3; i++) {
            Image randAntwort = alleCover[(int) (Math.random() * alleCover.length)];
            if (!coverEquals(randAntwort, srcCover)) {
                boolean add = true;

                for (Image img : antworten) {
                    if (img.equals(randAntwort)) {
                        i--;
                        add = false;
                        break;
                    }
                }
                if (add)
                    antworten.add(randAntwort);
            } else {
                System.out.println("Cover wurden als gleich erkannt.");
                i--;
            }
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

    /**
     * Vergleiche pixelweise JavaFX-Images, um doppelte CoverAntworten zu vermeiden.
     * Hintergrund ist, dass die equals() - Methode nicht die Bilder vergleicht, sondern
     * Objekt-Identitäten.
     * <p>
     * Hierbei wird davon ausgegegangen, dass Cover, deren oberes Viertel gleich ist, als
     * gleich angesehen werden können, um die Laufzeit zu verringern.
     *
     * @param imgA Das erste Bild
     * @param imgB Das zweite Bild
     * @return ob die Bilder gleich sind oder nicht.
     */
    private static boolean coverEquals(Image imgA, Image imgB) {
        // Wenn die Bilder ungleich groß sind, können sie nicht gleich sein.
        if (imgA.getWidth() == imgB.getWidth() && imgA.getHeight() == imgB.getHeight()) {
            double width = imgA.getWidth();
            double height = imgA.getHeight();

            PixelReader a = imgA.getPixelReader();
            PixelReader b = imgB.getPixelReader();

            for (int y = 0; y < height / 2; y++) {
                for (int x = 0; x < width / 2; x++) {
                    // Sobald ein pixel ungleich ist, gib false zurück
                    // System.out.println("Bild 1 " + x + "|" + y + " Pixel" + a.getArgb(x, y) + " == " + b.getArgb(x, y));
                    if (a.getArgb(x, y) != b.getArgb(x, y)) {
                        return false;
                    }
                }
            }
        } else {
            return false;
        }

        return true;
    }


    public Image getCover(int i) {
        return antworten[i];
    }


}

