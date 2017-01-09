package hearrun.view.controller;

import hearrun.view.layout.CenterLayout;
import hearrun.view.layout.SideBar;

/**
 * Created by Josh on 09.01.17.
 */
public class ViewController {
    private CenterLayout centerLayout;
    private SideBar leftLayout;
    private SideBar rightLayout;


    public void setCenterLayout(CenterLayout centerLayout){
        this.centerLayout = centerLayout;
    }

    public void setLeftLayout(SideBar leftLayout){
        this.leftLayout = leftLayout;
    }

    public void setRightLayout(SideBar rightLayout) {
        this.rightLayout = rightLayout;
    }
    
}
