package hearrun.business.fragen;


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

}