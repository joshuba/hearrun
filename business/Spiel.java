package hearrun.business;

import hearrun.view.layout.Map;

/**
 * Created by joshuabarth on 12.01.17.
 */
public class Spiel {
    private Map aktMap;

    public Spiel(String mapName){
        leseMapVonDateiEin(mapName);


    }

    public void leseMapVonDateiEin(String mapName){
        this.aktMap = new Map(mapName);

    }

    public Map getAktMap(){
        return aktMap;
    }
}
