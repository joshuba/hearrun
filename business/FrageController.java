package hearrun.business;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.business.exceptions.TagNeededException;
import hearrun.business.exceptions.TracksNeededException;
import hearrun.business.fragen.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Josh on 28.12.16
 */
public class FrageController {
    private static final int FRAGEZEIT = 10;
    private final String XMLPATH = "/hearrun/resources/Data/quiz.xml";
    private final int MENGE_FRAGETYP = 5;

    //Für Faktfragen
    private File inputFile;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;
    private Frageliste alleFragen;
    private SpielController spielController;

    private SimpleFloatProperty musicReadingProgress;
    private SimpleBooleanProperty readingOnOff;
    private SimpleIntegerProperty fragenAnzahl;

    ArrayList<File> tracks;


    public FrageController(SpielController spielController) {

        alleFragen = new Frageliste();
        this.spielController = spielController;
        tracks = new ArrayList<>();
        leseXMLein(XMLPATH);
        musicReadingProgress = new SimpleFloatProperty();
        readingOnOff = new SimpleBooleanProperty();
        readingOnOff.setValue(false);
        musicReadingProgress.setValue(15);
        fragenAnzahl = new SimpleIntegerProperty();
    }


    public Frage getFrage(Fragetyp fragetyp) {
        return alleFragen.getRand(fragetyp);
    }

    public Frage getFrage() {
        return alleFragen.getRand();
    }

    public void leseMusikEin(String path) {
        spielController.getLayout().getViewController().zeigeLadeScreen(readingOnOff, musicReadingProgress, fragenAnzahl);
        leseEinGeneriereFragen(path);
    }

    private void leseEinGeneriereFragen(String path) {

        new Thread(() -> {
            tracks.clear();
            alleFragen.clear();
            leseOrdnerEin(new File(path));
            musicReadingProgress.setValue(0);
            fragenAnzahl.setValue(0);

            ID3v2[] titel = new ID3v2[tracks.size()];

            for (int i = 0; i < tracks.size(); i++) {
                musicReadingProgress.setValue(musicReadingProgress.get() + 0.05 / tracks.size());
                try {
                    titel[i] = new Mp3File(tracks.get(i).getAbsolutePath()).getId3v2Tag();


                } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                    e.printStackTrace();
                }
            }

            // Alle Cover einlesen
            ArrayList<Image> covers = new ArrayList<>();
            musicReadingProgress.setValue(musicReadingProgress.get() + 0.1 / tracks.size());
            for (File track : tracks) {
                try {
                    Mp3File mp3 = new Mp3File(track.getAbsolutePath());
                    if (mp3.getId3v2Tag() != null) {
                        byte[] coverBytes = mp3.getId3v2Tag().getAlbumImage();

                        if (coverBytes != null)
                            covers.add(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(coverBytes)), null));
                    }

                } catch (UnsupportedTagException | IOException | InvalidDataException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    System.out.println(track.getName());
                    e.printStackTrace();
                }
            }

            //Zufallsfaktor erhöhen
            Collections.shuffle(tracks);

            //CoverTitelFragen einlesen
            ArrayList<File> tracksCP = (ArrayList<File>) tracks.clone();
            int akt = 0;
            for (int i = 0; i < tracks.size(); i++) {
                musicReadingProgress.setValue(musicReadingProgress.get() + 0.3 / tracks.size());
                fragenAnzahl.set(alleFragen.size());
                try {
                    alleFragen.add(CoverTitelFrage.generiereFrage(new Mp3File(tracksCP.get(akt).getAbsolutePath()).getId3v2Tag(), titel));
                } catch (TagNeededException e) {
                    akt++;
                    i--;
                    continue;
                } catch (UnsupportedTagException | IOException | InvalidDataException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                tracksCP.remove(akt);
            }
            musicReadingProgress.setValue(0.4);


            // CoverWahlFragen einlesen
            tracksCP = (ArrayList<File>) tracks.clone();
            akt = 0;
            for (int i = 0; i < tracks.size(); i++) {
                musicReadingProgress.setValue(musicReadingProgress.get() + 0.2 / tracks.size());
                fragenAnzahl.set(alleFragen.size());
                try {
                    alleFragen.add(CoverWahlFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), covers.toArray(new Image[covers.size()])));

                } catch (TagNeededException e) {
                    akt++;
                    i--;
                    continue;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                tracksCP.remove(akt);

            }
            musicReadingProgress.setValue(0.6);


            //InterpretFragen einlesen
            tracksCP = (ArrayList<File>) tracks.clone();
            akt = 0;

            for (int i = 0; i < tracks.size(); i++) {
                musicReadingProgress.setValue(musicReadingProgress.get() + 0.2 / tracks.size());
                fragenAnzahl.set(alleFragen.size());
                try {
                    alleFragen.add(InterpretFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), titel));
                } catch (TagNeededException e) {
                    akt++;
                    i--;
                    continue;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                tracksCP.remove(akt);
            }
            musicReadingProgress.setValue(0.8);


            // TitelFragen einlesen
            tracksCP = (ArrayList<File>) tracks.clone();
            akt = 0;

            for (int i = 0; i < tracks.size(); i++) {
                musicReadingProgress.setValue(musicReadingProgress.get() + 0.2 / tracks.size());
                fragenAnzahl.set(alleFragen.size());
                try {
                    alleFragen.add(TitelFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), titel));
                } catch (TagNeededException e) {
                    akt++;
                    i--;
                    continue;
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
                tracksCP.remove(akt);
            }
            musicReadingProgress.setValue(1);
            fragenAnzahl.set(alleFragen.size());

            readingOnOff.setValue(true);
            readingOnOff.setValue(false);


            System.out.println(alleFragen.size() - alleFragen.size(Fragetyp.FaktFrage) + " Fragen generiert aus " + tracks.size() + " Songs.");
            System.out.println("Darunter " + alleFragen.size(Fragetyp.CoverWahlFrage) + " Cover-Wahl-Fragen");
            System.out.println("Darunter " + alleFragen.size(Fragetyp.InterpretFrage) + " Interpret-Fragen");
            System.out.println("Darunter " + alleFragen.size(Fragetyp.Titelfrage) + " Titel-Fragen");
            System.out.println("Darunter " + alleFragen.size(Fragetyp.CoverTitelFrage) + " Cover-Titel-Fragen");
        }).start();
    }


    public void leseOrdnerEin(File root) {
        File[] files = root.listFiles();
        // Dateien einlesen
        if (files != null) {
            for (File f : files)
                if (f.getName().endsWith(".mp3"))
                    tracks.add(f);
                else if (f.isDirectory())
                    leseOrdnerEin(f);
        }
    }

    private void testeTracks() {
        // erstelle sortierte Bibliothek aus Interpreten
        ArrayList<String> interpreten = new ArrayList<>();
        int anzahlVerschInterpreten = 0;
        for (File f : tracks) {
            try {
                String interpret = new Mp3File(f.getAbsolutePath()).getId3v2Tag().getArtist();

                // den ersten Interpret hinzufügen
                if (interpreten.size() == 0) {
                    interpreten.add(interpret);
                    anzahlVerschInterpreten++;
                } else
                    // ist der interpret in interpreten?
                    for (String s : interpreten)
                        if (!s.equals(interpret)) {
                            interpreten.add(interpret); // wenn ja, erhöhe Anzahl der Interpreten
                            anzahlVerschInterpreten++;
                            break;
                        }

            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                e.printStackTrace();
            }

            // if (tracks.size() > )


        }
    }

    private void leseXMLein(String path) {

        dbFactory = DocumentBuilderFactory.newInstance();

        try {

            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(Main.class.getResourceAsStream(path));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        NodeList alleFaktfragenNL = doc.getElementsByTagName("frage");

        System.out.println("XML erfolgreich eingelesen!");

        //erstelle Frage-Objekte zu allen Fraktfragen in der xml
        for (int i = 0; i < alleFaktfragenNL.getLength(); i++) {
            //extrahiere Fragetext
            Element frage = (Element) alleFaktfragenNL.item(i);
            String fragetext = frage.getElementsByTagName("fragetext").item(0).getTextContent();

            // extrahiere Antworten und richtigIndex
            NodeList antworten = frage.getElementsByTagName("antwort");
            String[] antwortTexte = new String[antworten.getLength()];
            int richtigIndex = -1;
            for (int j = 0; j < antworten.getLength(); j++) {
                antwortTexte[j] = antworten.item(j).getTextContent();

                if (antworten.item(j).getAttributes().item(0).toString().contains("ja"))
                    richtigIndex = j;
            }

            // Füge die Frage der Frageliste hinzu
            alleFragen.add(new FaktFrage(fragetext, antwortTexte, richtigIndex));
        }
    }


}
