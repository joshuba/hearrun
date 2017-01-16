package hearrun.view.layout;

import hearrun.business.SpielController;
import hearrun.view.controller.ViewController;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by Josh on 09.01.17.
 */
public class GameLayout extends BorderPane{
    private ViewController viewController;
    private SpielController spielController;
    private Stage stage;
    private int border;



    public GameLayout(Stage stage, SpielController spielController, ViewController viewController){
        this.setId("gameLayout");
        this.stage = stage;
        this.viewController = viewController;
        this.spielController = spielController;

        //Panes initalisieren
        CenterLayout centerLayout = new CenterLayout(viewController);
        SideBar leftLayout = new SideBar();
        SideBar rightLayout = new SideBar();
        TopLayout topLayout = new TopLayout(viewController, spielController);

        //Komponenten zusammenfügen
        this.setCenter(centerLayout);
        this.setLeft(leftLayout);
        this.setRight(rightLayout);
        this.setTop(topLayout);

        //Dem Viewcontroller übergeben
        viewController.setCenterLayout(centerLayout);
        viewController.setLeftLayout(leftLayout);
        viewController.setRightLayout(rightLayout);


    }




}
