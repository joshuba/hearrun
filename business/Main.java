package hearrun.business;

import hearrun.business.fragen.*;
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
        stelleFrage(controller.getFrage());
    }


    public void stelleFrage(Frage frage) {

        if (frage instanceof CoverWahlFrage) {
            System.out.println(frage.getFragetext());
            System.out.println(((CoverWahlFrage) frage).getPath());
            for (Image i : ((CoverWahlFrage) frage).getAnworten())
                Util.showImage(i);
            System.out.println(frage.getRichtigIndex());
        } else if (frage instanceof InterpretFrage) {
            System.out.println(frage.getFragetext());
            System.out.println(((InterpretFrage) frage).getPath());
            for (String s : frage.getAntworten())
                System.out.println(s);
            System.out.println(frage.getRichtigIndex());
        } else if (frage instanceof TitelFrage) {
            System.out.println(frage.getFragetext());
            System.out.println(((TitelFrage) frage).getPath());
            for (String s : frage.getAntworten())
                System.out.println(s);
            System.out.println(frage.getRichtigIndex());
        } else {
            System.out.println(frage.getFragetext());
            for (String s : frage.getAntworten())
                System.out.println(s);
            System.out.println(frage.getRichtigIndex());
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        controller = new FrageController();

        for (int i = 0; i < 10; i++)
            stelleFrage(controller.getFrage(Fragetyp.Titelfrage));


    }


}
