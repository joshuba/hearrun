package hearrun.business;
import hearrun.business.fragen.FaktFrage;
import hearrun.business.fragen.Frage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;


/**
 * Created by Josh on 28.12.16.
 */
public class FrageController {
    private static final int FRAGEZEIT  = 10;

    //Für Faktfragen
    private File inputFile;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    private Document doc;
    private int faktFragenAnz;
    private NodeList alleFaktfragen;


    public FrageController()  {
        leseXMLein("/Users/Josh/IdeaProjects/HearRun/src/hearrun/resources/Data/quiz.xml");
    }



    public Frage frageStellen(Fragetyp fragetyp) {
        switch (fragetyp) {
            case Faktfrage:
                return(stelleFaktfrage());

        }
        return null;
    }

    public Frage stelleFaktfrage(){
        String fragetext = null;
        String [] antworten = null;
        int richtigIndex = -1;

        //Waehle eine zufaellige Frage aus allen Fragen aus
        Node frage = alleFaktfragen.item((int)((Math.random()) * faktFragenAnz));

        //Extrahiere Fragetext
        if (frage.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) frage;
            fragetext = eElement.getElementsByTagName("fragetext").item(0).getTextContent();

            //Extrahiere Anworten und schreibe in Array
            int size = eElement.getElementsByTagName("antwort").getLength(); //Anzahl der Anworten
            antworten = new String [size];
            for(int i = 0; i<size; i++){
                antworten[i] = eElement.getElementsByTagName("antwort").item(i).getTextContent();

                //Erkenne richtige Antwort
                if(eElement.getElementsByTagName("antwort").item(i).getAttributes().item(0).toString().contains("ja")){
                    richtigIndex = i;
                }
            }
        }

        return new FaktFrage(fragetext,antworten,richtigIndex);

    }

    private void leseXMLein(String path){
        //Lese FrageXML ein
        inputFile = new File(path);
        dbFactory = DocumentBuilderFactory.newInstance();

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);
        }catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        doc.getDocumentElement().normalize();
        alleFaktfragen = doc.getElementsByTagName("frage");
        faktFragenAnz = alleFaktfragen.getLength();

        System.out.println("XML erfolgreich eingelesen!");


    }









}
