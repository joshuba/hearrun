package hearrun.business;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.Main;
import hearrun.business.exceptions.TagNeededException;
import hearrun.business.fragen.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.Arrays;
import java.util.Collections;


/**
 * Created by Josh on 28.12.16
 */
public class FrageController {
    private final String XMLPATH = "/hearrun/resources/Data/quiz.xml";

    //Für Faktfragen
    private Document doc;

    private Frageliste alleFragen;

    private SimpleFloatProperty musicReadingProgress;
    private SimpleBooleanProperty readingOnOff;
    private SimpleIntegerProperty fragenAnzahl;

    private ArrayList<File> tracks;
    private ArrayList<Image> covers;



    public FrageController() {

        alleFragen = new Frageliste();
        tracks = new ArrayList<>();
        covers = new ArrayList<>();
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
        readingOnOff.setValue(false);
        leseEinGeneriereFragen(path);
        leseXMLein(XMLPATH);
    }

    private void leseEinGeneriereFragen(String path) {

        new Thread(() -> {
            tracks.clear();
            alleFragen.clear();
            leseOrdnerEin(new File(path));
            musicReadingProgress.setValue(0);
            fragenAnzahl.setValue(0);

            if (testeTracks()) {

                // Alle Titel - Tags einlesen
                ID3v2[] titel = new ID3v2[tracks.size()];
                for (int i = 0; i < tracks.size(); i++) {
                    musicReadingProgress.setValue(musicReadingProgress.get() + 0.05 / tracks.size());
                    try {
                        titel[i] = new Mp3File(tracks.get(i).getAbsolutePath()).getId3v2Tag();


                    } catch (IOException | UnsupportedTagException | InvalidDataException e) {
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


                System.out.println(alleFragen.size() - alleFragen.size(Fragetyp.FaktFrage) + " Fragen generiert aus " + tracks.size() + " Songs.");
                System.out.println("Darunter " + alleFragen.size(Fragetyp.CoverWahlFrage) + " Cover-Wahl-Fragen");
                System.out.println("Darunter " + alleFragen.size(Fragetyp.InterpretFrage) + " Interpret-Fragen");
                System.out.println("Darunter " + alleFragen.size(Fragetyp.Titelfrage) + " Titel-Fragen");
                System.out.println("Darunter " + alleFragen.size(Fragetyp.CoverTitelFrage) + " Cover-Titel-Fragen");
                System.out.println("Darunter " + alleFragen.size(Fragetyp.FaktFrage) + " Fakt-Fragen");
            } else
                System.out.println("verkackt!");
        }).start();
    }


    private void leseOrdnerEin(File root) {
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

    /**
     * erstellt eine Statistik über die einglesene Bibliothek. Diese Methode sichert ab, dass
     * keine Bibliotheken eingelesen werden können, die zu wenige Cover, Interpreten oder auch Tracks enthalten.
     *
     * Aus Laufzeit-Gründen liest sie wärend des Testens auch gleich alle, nicht doppelten, Cover ein.
     *
     * @return wahr, wenn Anzahl und Variabilität der Tracks genügt, sonst falsch
     */
    private boolean testeTracks() {
        ArrayList<String> interpreten = new ArrayList<>();

        try {
            // 1.: Interpreten prüfen
            // erstelle sortierte Bibliothek aus Interpreten
            for (File f : tracks) {

                ID3v2 tags = new Mp3File(f.getAbsolutePath()).getId3v2Tag();
                if (tags != null && tags.getArtist() != null) {

                    String interpret = new Mp3File(f.getAbsolutePath()).getId3v2Tag().getArtist();


                    // den ersten Interpret hinzufügen
                    if (interpreten.size() == 0) {
                        interpreten.add(interpret);
                    } else {
                        // ist der interpret in interpreten?
                        boolean istVorhanden = false;
                        for (String s : interpreten) {
                            if (s.equals(interpret)) {
                                istVorhanden = true;// wenn ja, füge ihn nicht noch einmal hinzu
                                break;
                            }
                        }

                        if (!istVorhanden) // Wenn er noch nicht vorhanden ist, füge ihn hinzu
                            interpreten.add(interpret);
                    }
                }
            }


            // 2.: Alben prüfen
            // erstelle Bibliothek aus allen, nicht doppelten, Alben mit vorhandenem Cover.
            ArrayList<byte[]> coverBytes = new ArrayList<>();
            for (File f : tracks) {
                ID3v2 tags = new Mp3File(f.getAbsolutePath()).getId3v2Tag();
                // Wenn im aktuellen Song tags, Album und Cover vorhanden sind:
                if (tags != null && tags.getAlbumImage() != null && tags.getAlbum() != null) {
                    if (coverBytes.size() == 0) // füge erstes Album hinzu
                        coverBytes.add(tags.getAlbumImage());
                    else { // teste ob Album des Songs doppelt vorkommt
                        boolean istVorhanden = false;
                        for (byte[] b : coverBytes) {
                            if (Arrays.equals(b, tags.getAlbumImage())) {
                                istVorhanden = true;
                                break;
                            }
                        }
                        // füge sowohl Album-String der
                        if (!istVorhanden) {
                            coverBytes.add(tags.getAlbumImage());
                            covers.add(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(tags.getAlbumImage())), null));

                        }
                    }
                }
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        System.out.println("Anzahl Tracks: " + tracks.size() +
                "\nAnzahl einzelner Alben: " + covers.size() +
                "\nAnzahl einzelner Interpreten: " + interpreten.size());

        return tracks.size() >= 40 && interpreten.size() >= 30 && covers.size() >= 30;
    }

    private void leseXMLein(String path) {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        try {

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
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

    public SimpleFloatProperty musicReadingProgressProperty() {
        return musicReadingProgress;
    }

    public SimpleBooleanProperty readingOnOffProperty() {
        return readingOnOff;
    }

    public SimpleIntegerProperty fragenAnzahlProperty() {
        return fragenAnzahl;
    }
}
