package hearrun.controller;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.Main;
import hearrun.model.Fragetyp;
import hearrun.model.exceptions.TagNeededException;
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
import java.util.Collections;


/**
 * Der Fragecontroller ist im wesentlichen für das Einlesen der Musikbibliothek bei Programmstart,
 * sowie für die Bereitstellung der generierten Fragen zuständig.
 */
public class FrageController {
    private final String XMLPATH = "/hearrun/resources/Data/quiz.xml";

    //Für Faktfragen
    private Document doc;

    private hearrun.model.fragen.Frageliste alleFragen;

    private SimpleFloatProperty musicReadingProgress;
    private SimpleBooleanProperty readingOnOff;
    private SimpleBooleanProperty success;
    private SimpleIntegerProperty fragenAnzahl;

    private ArrayList<File> tracks;
    private ArrayList<Image> covers;


    public FrageController() {

        alleFragen = new hearrun.model.fragen.Frageliste();
        tracks = new ArrayList<>();
        covers = new ArrayList<>();
        musicReadingProgress = new SimpleFloatProperty();
        readingOnOff = new SimpleBooleanProperty(false);
        success = new SimpleBooleanProperty(true);
        musicReadingProgress.setValue(15);
        fragenAnzahl = new SimpleIntegerProperty();
    }

    /**
     * Gibt eine Frage zum angegebenen Fragetyp mit.
     *
     * @param fragetyp Der gewünschte Fragetyp.
     * @return eine zufällige Frage zum übergebenen Fragetyp
     */
    public hearrun.model.fragen.Frage getFrage(Fragetyp fragetyp) {
        return alleFragen.getRand(fragetyp);
    }

    /**
     * Gibt eine zufällige Frage zum angegebenen Fragetyp mit.
     *
     * @return eine zufällige Frage  aus der Frageliste.
     */
    public hearrun.model.fragen.Frage getFrage() {
        return alleFragen.getRand();
    }

    public void leseMusikEin(String path) {
        readingOnOff.setValue(false);
        leseEinGeneriereFragen(path);
        leseXMLein(XMLPATH);
    }

    /**
     * Diese Methode liest rekursiv den übergebenen Ordner ein.
     * Danach prüft sie, ob der eingelesene Ordner genügend gepflegte Mp3-Tags
     * enthält, um eine ausreichende Fragenanzahl generieren zu können.
     * <p>
     * Dabei geht sie für jeden der Fragetypen ähnlich vor:
     * 1.   Eine Liste aus Mp3-Tags erstellen, die als entsprechender Antworten-Pool dienen
     * 2.   Jede Datei durchgehen und zu jeder (validen) Datei eine Frage erstellen
     * (Wobei die Datei als richtige Antwort für die jeweilige Frage dient)
     * <p>
     * Dabei fängt sie "TagsNeededExceptions" ab. Diese bedeuten, dass der Datei ein Tag fehlt, der
     * essentiell ist für die Beantwortung der Frage (beispielsweise das Cover für CoverWahlFragen)
     * <p>
     * Währenddessen atualisiert sie eine IntegerProperty, die von der GUI als Grundlage für eine
     * ProgressBar genutzt wird.
     *
     * @param path Der Pfad, aus dem die Musik eingelesen werden soll.
     */
    private void leseEinGeneriereFragen(String path) {
        new Thread(() -> {
            tracks.clear();
            alleFragen.clear();
            try {
                leseOrdnerEin(new File(path));
            } catch (InvalidDataException | IOException | UnsupportedTagException e) {
                e.printStackTrace();
            }
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
                        alleFragen.add(hearrun.model.fragen.CoverTitelFrage.generiereFrage(new Mp3File(tracksCP.get(akt).getAbsolutePath()).getId3v2Tag(), titel));
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
                        alleFragen.add(hearrun.model.fragen.CoverWahlFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), covers.toArray(new Image[covers.size()])));

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
                        alleFragen.add(hearrun.model.fragen.InterpretFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), titel));
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
                        alleFragen.add(hearrun.model.fragen.TitelFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), titel));
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


            } else {
                success.setValue(true);
                success.setValue(false);
            }

        }).start();
    }


    private void leseOrdnerEin(File root) throws InvalidDataException, IOException, UnsupportedTagException {
        File[] files = root.listFiles();
        // Dateien einlesen
        if (files != null) {
            for (File f : files)
                if (f.getName().endsWith(".mp3") && !f.getName().startsWith(".") && new Mp3File(f.getAbsolutePath()).getId3v2Tag() != null)
                    tracks.add(f);
                else if (f.isDirectory())
                    leseOrdnerEin(f);
        }
    }

    /**
     * erstellt eine Statistik über die einglesene Bibliothek. Diese Methode sichert ab, dass
     * keine Bibliotheken eingelesen werden können, die zu wenige Cover, Interpreten oder auch Tracks enthalten.
     * <p>
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
            ArrayList<String> covers = new ArrayList<>();
            for (File f : tracks) {
                ID3v2 tags = new Mp3File(f.getAbsolutePath()).getId3v2Tag();
                // Wenn im aktuellen Song tags, Album und Cover vorhanden sind:
                if (tags != null && tags.getAlbumImage() != null && tags.getAlbum() != null) {
                    if (coverBytes.size() == 0) { // füge erstes Album hinzu
                        coverBytes.add(tags.getAlbumImage());
                        covers.add(tags.getAlbum());
                    } else { // teste ob Album des Songs doppelt vorkommt
                        boolean istVorhanden = false;
                        for (String s : covers) {
                            if (s.equals(tags.getAlbum())) {
                                istVorhanden = true;
                                break;
                            }
                        }
                        // füge sowohl Album-String der
                        if (!istVorhanden) {
                            coverBytes.add(tags.getAlbumImage());
                            this.covers.add(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(tags.getAlbumImage())), null));
                            covers.add(tags.getAlbum());
                        }
                    }
                }
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }

        return tracks.size() >= 40 && interpreten.size() >= 20 && covers.size() >= 20;
    }

    /**
     *  Liest eine XML.Datei mit statischen Fakt-Fragen ein.
     * @param path der Pfad zur XML-Datei
     */
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
            alleFragen.add(new hearrun.model.fragen.FaktFrage(fragetext, antwortTexte, richtigIndex));
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

    public SimpleBooleanProperty successProperty() {
        return success;
    }
}
