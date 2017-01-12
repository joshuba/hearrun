package hearrun.business;

import hearrun.business.fragen.Frage;
import hearrun.business.fragen.InterpretFrage;

/**
 * Created by Josh on 05.01.17.
 */
public class Main {

    public static void main(String[] args) {
        FrageController controller = new FrageController();

        Frage faktFrage = controller.frageStellen(Fragetyp.FaktFrage);
        System.out.println(faktFrage.getFragetext());
        for(int i = 0; i<faktFrage.getAntworten().length;i++){
            System.out.println(i + ". " + faktFrage.getAntworten()[i]);
        }
        System.out.println("Richtig: " + faktFrage.getRichtigIndex());

        Frage coverFrage = controller.frageStellen(Fragetyp.CoverTitelFrage);

        System.out.println(coverFrage.getFragetext());
        for (String s : coverFrage.getAntworten())
            System.out.println(s);
        System.out.println(coverFrage.getRichtigIndex());

        coverFrage = controller.frageStellen(Fragetyp.CoverTitelFrage);

        System.out.println(coverFrage.getFragetext());
        for (String s : coverFrage.getAntworten())
            System.out.println(s);
        System.out.println(coverFrage.getRichtigIndex());

        coverFrage = controller.frageStellen(Fragetyp.CoverTitelFrage);

        System.out.println(coverFrage.getFragetext());
        for (String s : coverFrage.getAntworten())
            System.out.println(s);
        System.out.println(coverFrage.getRichtigIndex());

        coverFrage = controller.frageStellen(Fragetyp.CoverTitelFrage);

        System.out.println(coverFrage.getFragetext());
        for (String s : coverFrage.getAntworten())
            System.out.println(s);
        System.out.println(coverFrage.getRichtigIndex());
/*
        InterpretFrage interpretFrage = (InterpretFrage) controller.frageStellen(Fragetyp.InterpretFrage);
        System.out.println(interpretFrage.getFragetext());
        for(String s : interpretFrage.getAntworten())
            System.out.println(s);
        System.out.println(interpretFrage.getRichtigIndex());
        System.out.println(interpretFrage.getPath());

        interpretFrage = (InterpretFrage) controller.frageStellen(Fragetyp.InterpretFrage);
        System.out.println(interpretFrage.getFragetext());
        for(String s : interpretFrage.getAntworten())
            System.out.println(s);
        System.out.println(interpretFrage.getRichtigIndex());
        System.out.println(interpretFrage.getPath());

        interpretFrage = (InterpretFrage) controller.frageStellen(Fragetyp.InterpretFrage);
        System.out.println(interpretFrage.getFragetext());
        for(String s : interpretFrage.getAntworten())
            System.out.println(s);
        System.out.println(interpretFrage.getRichtigIndex());
        System.out.println(interpretFrage.getPath());
*/




    }
}
