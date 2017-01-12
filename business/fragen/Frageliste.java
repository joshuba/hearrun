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
        Frage rand;
        int index;
        switch (typ) {
            case CoverTitelFrage:
                index = (int) (Math.random() * coverTitelFragen.size());
                rand = coverTitelFragen.get(index);
                coverTitelFragen.remove(index);
                return rand;
            case CoverWahlFrage:
                index = (int) (Math.random() * coverWahlFragen.size());
                rand = coverWahlFragen.get(index);
                coverWahlFragen.remove(index);
                return rand;
            case FaktFrage:
                index = (int) (Math.random() * faktFragen.size());
                rand = faktFragen.get(index);
                faktFragen.remove(index);
                return rand;
            case InterpretFrage:
                index = (int) (Math.random() * interpretFragen.size());
                rand = interpretFragen.get(index);
                interpretFragen.remove(index);
                return rand;
            default: //titelFrage
                index = (int) (Math.random() * titelFragen.size());
                rand = titelFragen.get(index);
                titelFragen.remove(index);
                return rand;
        }
    }

    public Frage getRand() {
        ArrayList <Frage> alleFragen = new ArrayList<>();
        if (coverTitelFragen.size() > 0)
            alleFragen.addAll(coverTitelFragen);
        if (coverWahlFragen.size() > 0)
            alleFragen.addAll(coverWahlFragen);
        if (faktFragen.size() > 0)
            alleFragen.addAll(faktFragen);
        if (interpretFragen.size() > 0)
            alleFragen.addAll(interpretFragen);
        if (titelFragen.size() > 0)
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
