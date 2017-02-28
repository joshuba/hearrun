package hearrun.model.fragen;


import hearrun.model.Fragetyp;

/**
 * Die Spielinterne Repräsentation der Fragen.
 * Die verschiedenen Fragetypen erben von dieser Klasse.
 * <p>
 * Fragen werden von außerhalb nicht per Konstruktor erzeugt, sondern nur über die
 * generiereFrage-Methode
 */
public abstract class Frage {

    protected String fragetext;
    private String[] antworten;
    private int richtigIndex;
    protected String path;

    protected Frage(String fragetext, String[] antworten, int richtigIndex) {
        this.fragetext = fragetext;
        this.antworten = antworten;
        this.richtigIndex = richtigIndex;
    }

    public int getRichtigIndex() {
        return richtigIndex;
    }

    public String getFragetext() {
        return fragetext;
    }

    public String[] getAntworten() {
        return antworten;
    }

    public String getPath() {
        return this.path;
    }

    public Fragetyp getFragetyp() {
        if (this instanceof CoverWahlFrage)
            return Fragetyp.CoverWahlFrage;
        else if (this instanceof hearrun.model.fragen.CoverTitelFrage)
            return Fragetyp.CoverTitelFrage;
        else if (this instanceof FaktFrage)
            return Fragetyp.FaktFrage;
        else if (this instanceof hearrun.model.fragen.InterpretFrage)
            return Fragetyp.InterpretFrage;
        else
            return Fragetyp.Titelfrage;
    }


}