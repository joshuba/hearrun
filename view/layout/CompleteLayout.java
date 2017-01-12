package hearrun.view.layout;

import hearrun.business.Spiel;
import hearrun.view.controller.ViewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.text.View;

/**
 * Created by Josh on 09.01.17.
 */
public class CompleteLayout extends BorderPane{
    private ViewController viewController;
    private Stage stage;
    private int border;



    public CompleteLayout(Stage stage, Spiel spiel){
        this.setId("completeLayout");
        this.stage = stage;

        this.viewController = new ViewController(stage);

        CenterLayout centerLayout = new CenterLayout();
        SideBar leftLayout = new SideBar();
        SideBar rightLayout = new SideBar();
        TopLayout topLayout = new TopLayout();



        //CompleteLayout stylen





        //Komponenten zusammenfügen
        this.setCenter(centerLayout);
        this.setLeft(leftLayout);
        this.setRight(rightLayout);
        this.setTop(topLayout);

        viewController.setCenterLayout(centerLayout);
        viewController.setLeftLayout(leftLayout);
        viewController.setRightLayout(rightLayout);

        viewController.setSpiel(spiel);




    }

    public ViewController getViewController(){
        return this.viewController;
    }


}
