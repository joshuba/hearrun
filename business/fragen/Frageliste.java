package hearrun.business.fragen;

import hearrun.business.Fragetyp;

import java.util.ArrayList;

/**
 * Created by Leo on 07.01.2017.
 */
public class Frageliste {
    private ArrayList<Frage> coverTitelFragen;
    private ArrayList<Frage> coverWahlFragen;
    private ArrayList<Frage> faktFragen;
    private ArrayList<Frage> interpretFragen;
    private ArrayList<Frage> titelFragen;

    public Frageliste() {
        coverTitelFragen = new ArrayList<>();
        coverWahlFragen = new ArrayList<>();
        faktFragen = new ArrayList<>();
        interpretFragen = new ArrayList<>();
        titelFragen = new ArrayList<>();
    }

    public void add(Frage f) {
        //Unterscheide den typ der übergebenen Frage und ordne ihn richtig ein
        if (f instanceof CoverWahlFrage) {
            coverWahlFragen.add(f);
        } else if (f instanceof CoverTitelFrage) {
            coverTitelFragen.add(f);
        } else if (f instanceof FaktFrage) {
            faktFragen.add(f);
        } else if (f instanceof InterpretFrage) {
            interpretFragen.add(f);
        } else if (f instanceof TitelFrage) {
            titelFragen.add(f);
        }
    }


    public Frage getRand(Fragetyp typ) {
        switch (typ) {
            case CoverTitelFrage:
                return coverTitelFragen.get((int) (Math.random() * coverTitelFragen.size()));
            case CoverWahlFrage:
                return coverWahlFragen.get((int) (Math.random() * coverWahlFragen.size()));
            case FaktFrage:
                return faktFragen.get((int) (Math.random() * faktFragen.size()));
            case InterpretFrage:
                return interpretFragen.get((int) (Math.random() * interpretFragen.size()));
            default: //titelFrage
                return titelFragen.get((int) (Math.random() * titelFragen.size()));
        }
    }

    public Frage getRand() {
        ArrayList <Frage> alleFragen = new ArrayList<>();
        alleFragen.addAll(coverTitelFragen);
        alleFragen.addAll(coverWahlFragen);
        alleFragen.addAll(faktFragen);
        alleFragen.addAll(interpretFragen);
        alleFragen.addAll(titelFragen);

        return alleFragen.get((int) (Math.random() * alleFragen.size()));
    }

    public int size(Fragetyp typ){
        switch (typ) {
            case CoverTitelFrage:
                return coverTitelFragen.size();
            case CoverWahlFrage:
                return coverWahlFragen.size();
            case FaktFrage:
                return faktFragen.size();
            case InterpretFrage:
                return interpretFragen.size();
            default: //titelFrage
                return titelFragen.size();
        }
    }




}
