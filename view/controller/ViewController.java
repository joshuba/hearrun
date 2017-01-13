package hearrun.view.controller;

import hearrun.business.Spiel;
import hearrun.view.layout.CenterLayout;
import hearrun.view.layout.Feld;
import hearrun.view.layout.Map;
import hearrun.view.layout.SideBar;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Josh on 09.01.17.
 */
public class ViewController {
    private Stage stage;
    private CenterLayout centerLayout;
    private SideBar leftLayout;
    private SideBar rightLayout;
    private Spiel spiel;

    public ViewController(Stage stage){
        this.stage = stage;

    }


    public void setCenterLayout(CenterLayout centerLayout) {
        this.centerLayout = centerLayout;
    }

    public void setLeftLayout(SideBar leftLayout) {
        this.leftLayout = leftLayout;
    }

    public void setRightLayout(SideBar rightLayout) {
        this.rightLayout = rightLayout;
    }



    public void baueSpielfeldAuf(){
        centerLayout.baueSpielfeldAuf(spiel.getAktMap());
    }



    public void setSpiel(Spiel spiel){
        this.spiel = spiel;
    }

    public void setFeldId(int x, int y, String id){
        spiel.getAktMap().getFeld(x,y).setBesetzt(id);
    }







}
