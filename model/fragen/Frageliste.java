package hearrun.model.fragen;

import hearrun.model.Fragetyp;

import java.util.ArrayList;

/**
 * Die Frageliste verwaltet intern die verschiedenen Frage-Objekte.
 *
 * Sie verwaltet dabei pro Fragetyp eine Array-List, damit schnell Fragen zu einem bestimmten Typ
 * geliefert werden können.
 */
public class Frageliste {
    private ArrayList<hearrun.model.fragen.Frage> coverTitelFragen;
    private ArrayList<hearrun.model.fragen.Frage> coverWahlFragen;
    private ArrayList<hearrun.model.fragen.Frage> faktFragen;
    private ArrayList<hearrun.model.fragen.Frage> interpretFragen;
    private ArrayList<hearrun.model.fragen.Frage> titelFragen;

    public Frageliste() {
        coverTitelFragen = new ArrayList<>();
        coverWahlFragen = new ArrayList<>();
        faktFragen = new ArrayList<>();
        interpretFragen = new ArrayList<>();
        titelFragen = new ArrayList<>();
    }

    public void add(hearrun.model.fragen.Frage f) {
        //Unterscheide den typ der übergebenen Frage und ordne ihn richtig ein
        if (f instanceof CoverWahlFrage) {
            coverWahlFragen.add(f);
        } else if (f instanceof hearrun.model.fragen.CoverTitelFrage) {
            coverTitelFragen.add(f);
        } else if (f instanceof hearrun.model.fragen.FaktFrage) {
            faktFragen.add(f);
        } else if (f instanceof hearrun.model.fragen.InterpretFrage) {
            interpretFragen.add(f);
        } else if (f instanceof hearrun.model.fragen.TitelFrage) {
            titelFragen.add(f);
        }
    }

    /**
     * Liefert eine zufällige Frage zu einem bestimmten Fragetyp, der übergeben wird.
     * @param typ der gewünschte Fragetyp.
     * @return eine zufällige Frage
     */
    public hearrun.model.fragen.Frage getRand(Fragetyp typ) {
        hearrun.model.fragen.Frage rand = null;
        int index;
        try {
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
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Aus einer Liste wurden bereits alle Fragen gestellt");
            System.exit(1);
        }
        return rand;
    }

    public hearrun.model.fragen.Frage getRand() {
        ArrayList<hearrun.model.fragen.Frage> alleFragen = new ArrayList<>();
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

    public int size(Fragetyp typ) {
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

    public int size() {
        return coverTitelFragen.size() +
                coverWahlFragen.size() +
                faktFragen.size() +
                interpretFragen.size() +
                titelFragen.size();
    }

    public void clear() {
        faktFragen.clear();
        titelFragen.clear();
        interpretFragen.clear();
        coverTitelFragen.clear();
        coverWahlFragen.clear();
    }
}





