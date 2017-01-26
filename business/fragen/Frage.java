package hearrun.business.fragen;


import hearrun.business.Fragetyp;

public abstract class Frage {

    protected String fragetext;
    protected String[] antworten;
    protected int richtigIndex;
    protected String path;


    public Frage(String fragetext, String[] antworten, int richtigIndex){
        this.fragetext = fragetext;
        this.antworten = antworten;
        this.richtigIndex = richtigIndex;
    }

    public int getRichtigIndex(){
        return richtigIndex;
    }

    public String getFragetext(){
        return fragetext;
    }

    public String[] getAntworten(){
        return antworten;
    }

    public String getPath(){
        return this.path;
    }

    public Fragetyp getFragetyp() {
        if (this instanceof CoverWahlFrage)
            return Fragetyp.CoverWahlFrage;
        else if (this instanceof CoverTitelFrage)
            return Fragetyp.CoverTitelFrage;
        else if (this instanceof FaktFrage)
            return Fragetyp.FaktFrage;
        else if (this instanceof InterpretFrage)
            return Fragetyp.InterpretFrage;
        else
            return Fragetyp.Titelfrage;
    }

}