package hearrun.view.layout;

import hearrun.view.controller.ViewController;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Created by Josh on 09.01.17.
 */
public class CompleteLayout extends BorderPane{
    private ViewController viewController;


    public CompleteLayout(){
        this.viewController = new ViewController();

        CenterLayout centerLayout = new CenterLayout();
        this.getChildren().addAll(centerLayout);

        viewController.setCenterLayout(centerLayout);




    }


}
