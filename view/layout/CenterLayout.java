package hearrun.view.layout;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * Gridpane, welches das Spielfeld darstellt
 */
public class CenterLayout extends GridPane {

    public CenterLayout(){
        this.setId("centerLayout");
        this.setGridLinesVisible(false);
        this.setAlignment(Pos.CENTER);
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
