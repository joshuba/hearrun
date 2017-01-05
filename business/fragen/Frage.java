package hearrun.business.fragen;

public class Frage {
    private String fragetext;
    private String[] antworten;
    private int richtigIndex;

    public Frage(String fragetext, String [] antworten, int richtigIndex){
        this.antworten=antworten;
        this.fragetext=fragetext;
        this.richtigIndex=richtigIndex;
    }
}