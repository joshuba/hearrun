package hearrun.view.layout;

import hearrun.business.SpielController;
import hearrun.view.controller.ViewController;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by joshuabarth on 14.01.17.
 */
public class CompleteLayout extends StackPane {
    private GameLayout gameLayout;
    private ViewController viewController;
    private SpielController spielController;
    private MainMenu mainMenu;
    private Stage stage;

    public CompleteLayout(Stage stage, SpielController spielController){
         this.viewController = new ViewController(stage, spielController);
         this.spielController = spielController;
         this.stage = stage;
         this.maxHeight(stage.getHeight());
         this.maxWidth(stage.getWidth());

         mainMenu = new MainMenu(spielController);

         setMainMenu();

    }

    public void setGameLayout(){
        this.getChildren().removeAll(mainMenu);
        //Falls das gamelayout nicht da ist hinzufuegen
        if(this.getChildren().isEmpty()){
            this.getChildren().addAll(gameLayout);
        }


    }

    public void setMainMenu(){
        //this.getChildren().removeAll(gameLayout);
        this.getChildren().addAll(mainMenu);
    }

    public ViewController getViewController(){
        return this.viewController;
    }

    public void resetGameLayout(){
        this.getChildren().removeAll(gameLayout);
        gameLayout = new GameLayout(stage, spielController, viewController);

    }

    public MainMenu getMainMenu(){
        return this.mainMenu;
    }




}
