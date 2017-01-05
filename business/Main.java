package hearrun.business;

import hearrun.business.fragen.Frage;

/**
 * Created by Josh on 05.01.17.
 */
public class Main {

    public static void main(String[] args) {
        FrageController controller = new FrageController();

        Frage frage = controller.frageStellen(Fragetyp.Faktfrage);
        System.out.println(frage.getFragetext());
        for(int i = 0; i<frage.getAntworten().length;i++){
            System.out.println(i + ". " + frage.getAntworten()[i]);
        }
        System.out.println("Richtig: " + frage.getRichtigIndex());






    }
}
