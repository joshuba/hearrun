package hearrun.view.layout;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import javafx.stage.Stage;
import sun.rmi.rmic.Main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by joshuabarth on 10.01.17.
 */
public class Map {
    private String mapName;
    private Feld [][] spielFeld;
    private int feldBreite;
    private int feldHoehe;
    private String dateiName;

    public Map(String dateiName, Stage stage){
        this.dateiName = dateiName;


        try {
            BufferedReader r = new BufferedReader(new FileReader("src/hearrun/resources/Data/" + dateiName));
            String line = r.readLine();

            //Titel und Groesse auslesen
            if (line != null && line.startsWith("*")) {
                mapName = line.substring(1);
                feldBreite = Integer.parseInt(r.readLine().substring(1));
                feldHoehe = Integer.parseInt(r.readLine().substring(1));

                this.spielFeld = new Feld[feldBreite][feldHoehe];

                System.out.println("mapName: "+ mapName + " Breite: "+ feldBreite + " Hoehe: " + feldHoehe);
            }
           line = r.readLine();
            int row = 0;
            while(line.startsWith("#") && !line.contains("!")){
                line = line.substring(1);
                String zeichen [] = line.split(" ");
                for (int i = 0; i<zeichen.length; i++){
                       Feld feld = new Feld(erkenneFeldtyp(zeichen[i]), stage);
                       spielFeld[i][row] = feld;

                }
                line = r.readLine();
                row++;

            }

                r.close();
            } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }



    }

    private Feldtyp erkenneFeldtyp(String s){
        switch (s) {
            case "I":
                return Feldtyp.InterpretFeld;
            case "C":
                return Feldtyp.CoverFeld;
            case "E":
                return Feldtyp.EreignisFeld;
            case "T":
                return Feldtyp.TitelFeld;
            case "F":
                return Feldtyp.FaktFeld;
            case "R":
                return Feldtyp.getRandomFeldtyp(); //Zufallsfelder
            case "L":
                return Feldtyp.LeeresFeld;
            case "X":
                return Feldtyp.EndFeld;

        }
        System.out.println("Lesefehler");
        return null;
    }

    public int getFeldBreite(){
        return this.feldBreite;
    }

    public int getFeldHoehe(){
        return this.feldHoehe;
    }

    public Feld getFeld(int c, int r){
        return spielFeld[c][r];

    }







}
