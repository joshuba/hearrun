package hearrun.business;

import hearrun.business.fragen.FaktFrage;
import hearrun.business.fragen.Frage;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.File;


/**
 * Created by Josh on 28.12.16.
 */
public class FrageController {

    public FrageController()  {
        //Lese FrageXML ein

        try {
            File inputFile = new File("/Users/Josh/IdeaProjects/HearRun/src/hearrun/Resources/Data/quiz.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            System.out.println("XML eingelesen, Rootelement :" + doc.getDocumentElement().getNodeName());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void frageStellen(Fragetyp fragetyp) {
        switch (fragetyp) {
            case Faktfrage:
                stelleFaktfrage();
                break;

        }

    }

    public Frage stelleFaktfrage(){


        return null;

    }

}
