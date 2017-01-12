package hearrun.business;

import hearrun.business.fragen.CoverTitelFrage;
import hearrun.business.fragen.CoverWahlFrage;
import hearrun.business.fragen.FaktFrage;
import hearrun.business.fragen.Frage;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Josh on 05.01.17.
 */
public class Main extends Application {
    FrageController controller;

    public static void main(String[] args) {
        launch(args);
    }


    public void stelleFrage() {
        Frage frage = controller.getFrage();

        if (frage instanceof CoverWahlFrage) {
            System.out.println(frage.getFragetext());
            System.out.println(((CoverWahlFrage) frage).getPath());
            for (Image i : ((CoverWahlFrage) frage).getAnworten())
                showImage(i);
            System.out.println(frage.getRichtigIndex());
        }else{
            System.out.println(frage.getFragetext());
            for (String s : frage.getAntworten())
                System.out.println(s);
            System.out.println(frage.getRichtigIndex());
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new FrageController();

        for(int i = 0; i < 10; i++)
            stelleFrage();
    }

    public void showImage (Image i){
        try {
            BufferedImage img = SwingFXUtils.fromFXImage(i, null);
            File temp = File.createTempFile("img", ".png");

            ImageIO.write(img, "png", temp);

            Desktop.getDesktop().open(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
