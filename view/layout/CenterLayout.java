package hearrun.view.layout;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * Created by Josh on 09.01.17.
 */
public class CenterLayout extends GridPane {

    public CenterLayout(){
        this.setId("centerLayout");
        this.maxHeight(200);


        this.setGridLinesVisible(true);

        this.setPadding(new Insets(10,10,10,10));
        this.setAlignment(Pos.CENTER);

        for (int i = 0; i<14; i++){
            for(int j = 0; j <14; j++){
                Label label = new Label("HALLO");
                GridPane.setConstraints(label, i,j );
                this.getChildren().add(label);


            }
        }
        /*
        Label label1 = new Label("Label 1");
        GridPane.setConstraints(label1, 0,0);

        Label label2 = new Label("Label 2");
        GridPane.setConstraints(label2, 1,0);

        Label label3 = new Label("Label 3");
        GridPane.setConstraints(label3, 0,1);

        Label label4 = new Label("Label 4");
        GridPane.setConstraints(label4, 1,1);

*/



        //this.getChildren().addAll(label1, label2, label3, label4);

        //GridPane.setConstraints(label,0,0);


    }






}
