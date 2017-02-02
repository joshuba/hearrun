package hearrun.view.layout;

import hearrun.view.controller.ViewController;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import javax.swing.text.View;

/**
 * Gridpane, welches das Spielfeld darstellt
 */
public class CenterLayout extends GridPane {

    private ViewController viewController;

    public CenterLayout(ViewController viewController){
        this.viewController = viewController;
        this.setId("centerLayout");
        //this.setAlignment(Pos.CENTER);
        //this.setGridLinesVisible(true);
    }

    private Node getNodeFromGridPane(int col, int row) {
        for (Node node : getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    public void baueSpielfeldAuf(Map map){
        for (int i = 0; i<map.getFeldBreite(); i++){
            for(int j = 0; j <map.getFeldHoehe(); j++){

                GridPane.setConstraints(map.getFeld(i,j), i,j );
                this.getChildren().add(map.getFeld(i,j));
            }
        }
    }



}
