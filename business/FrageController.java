package hearrun.business;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import hearrun.business.exceptions.TagNeededException;
import hearrun.business.fragen.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Josh on 28.12.16.
 */
public class FrageController {
    private static final int FRAGEZEIT = 10;
    private final String XMLPATH = "src/hearrun/resources/data/quiz.xml";
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

    }


    public Frage getFrage(Fragetyp fragetyp) {
        return alleFragen.getRand(fragetyp);
    }

    public Frage getFrage() {
        return alleFragen.getRand();
    }

    public void leseMusikEin(String path) {


        spielController.getLayout().getViewController().zeigeLadeScreen(readingOnOff, musicReadingProgress);
        leseEinGeneriereFragen(path);

    }

    private void leseEinGeneriereFragen(String path) {

        Task<Integer> readMusic = new Task<Integer>() {
            @Override protected Integer call() throws Exception {
                readingOnOff.setValue(false);


                System.out.println("Musik einlesen:");
                if (isCancelled()) {
                    updateMessage("Cancelled");
                }
                File root = new File(path);
                File[] files = root.listFiles();
                // Dateien einlesen
                if (files != null) {
                    for (File f : files)
                        if (f.getName().endsWith(".mp3") || f.getName().endsWith(".wav") || f.getName().endsWith(".aac"))
                            tracks.add(f);
                        else if (f.isDirectory())
                            leseEinGeneriereFragen(f.getAbsolutePath());
                }
                updateProgress(20, 100);
                musicReadingProgress.setValue(0.2);
                System.out.println(musicReadingProgress.getValue());




                // dateien in Frageobjekte parsen
                // checken, wieviele Fragen pro Typ ca erstellt werden können
                int anz = tracks.size() / MENGE_FRAGETYP;

                // Alle titel einlesen
                ID3v2[] titel = Util.getAllTitles(tracks.toArray(new File[tracks.size()]));
                Image[] covers = Util.getAllCovers(tracks.toArray(new File[tracks.size()]));

                //Zufallsfaktor erhöhen
                Collections.shuffle(tracks);

                //CoverTitelFragen einlesen
                ArrayList<File> tracksCP = (ArrayList<File>) tracks.clone();
                int akt = 0;
                for (int i = 0; i < anz; i++) {
                    try {
                        alleFragen.add(CoverTitelFrage.generiereFrage(new Mp3File(tracksCP.get(akt).getAbsolutePath()).getId3v2Tag(), titel));
                    } catch (TagNeededException e) {
                        akt++;
                        i--;
                        continue;
                    } catch (UnsupportedTagException | IOException | InvalidDataException e) {
                        e.printStackTrace();
                    }
                    tracksCP.remove(akt);
                }
                updateProgress(40, 100);
                musicReadingProgress.setValue(0.4);
                System.out.println(musicReadingProgress.getValue());



                // CoverWahlFragen einlesen
                tracksCP = (ArrayList<File>) tracks.clone();
                akt = 0;
                for (int i = 0; i < anz; i++) {
                    try {
                        alleFragen.add(CoverWahlFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), covers));

                    } catch (TagNeededException e) {
                        akt++;
                        i--;
                        continue;
                    }
                    tracksCP.remove(akt);

                }
                updateProgress(60, 100);
                musicReadingProgress.setValue(0.6);
                System.out.println(musicReadingProgress.getValue());



                //InterpretFragen einlesen
                tracksCP = (ArrayList<File>) tracks.clone();
                akt = 0;

                for (int i = 0; i < anz; i++) {
                    try {
                        alleFragen.add(InterpretFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), titel));
                    } catch (TagNeededException e) {
                        akt++;
                        i--;
                        continue;
                    }
                    tracksCP.remove(akt);
                }
                updateProgress(80, 100);
                musicReadingProgress.setValue(0.8);
                System.out.println(musicReadingProgress.getValue());


                // TitelFragen einlesen
                tracksCP = (ArrayList<File>) tracks.clone();
                akt = 0;

                for (int i = 0; i < anz; i++) {
                    try {
                        alleFragen.add(TitelFrage.generiereFrage(tracksCP.get(akt).getAbsolutePath(), titel));
                    } catch (TagNeededException e) {
                        akt++;
                        i--;
                        continue;
                    }
                    tracksCP.remove(akt);
                }
                updateProgress(100, 100);
                musicReadingProgress.setValue(1);

                System.out.println("DONE");
                readingOnOff.setValue(true);

                return null;
            }
        };

        Thread t = new Thread(readMusic);
        t.start();




    }

    private void leseXMLein(String path) {
        //Lese FrageXML ein
        inputFile = new File(path);
        dbFactory = DocumentBuilderFactory.newInstance();

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
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
