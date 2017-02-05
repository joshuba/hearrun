package hearrun;

import hearrun.view.controller.SpielController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

/**
 * Created by Josh on 09.01.17
 */
public class Main extends Application {
    public static final Color spielerEinsFarbe = new Color(221f / 255, 66f / 255, 85f / 255, 1);
    public static final Color spielerZweiFarbe = new Color(51f / 255, 195f / 255, 98f / 255, 1);
    public static final Color spielerDreiFarbe = new Color(66f / 255, 107f / 255, 221f / 255, 1);
    public static final Color spielerVierFarbe = new Color(109f / 255, 106f / 255, 94f / 255, 1);


    private Stage primaryStage;
    private SpielController spielController;

    @Override
    public void init() throws Exception {
        super.init();

    }


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        spielController = new SpielController(primaryStage);


        Scene scene = new Scene(spielController.getLayout());


        primaryStage.setTitle("Hear and Run - alpha 0.01");
        primaryStage.setMinWidth(1100);
        primaryStage.setMinHeight(800);


        primaryStage.setScene(scene);

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        scene.getStylesheets().add(("/hearrun/view/layout/css/felder.css"));
        scene.getStylesheets().add(("/hearrun/view/layout/css/layout.css"));
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void stop() throws Exception {
        spielController.beendeProgramm();
        System.exit(0);
    }

    public static String getFilePathFromResourcePath(String path, boolean overwrite) {
        String syspath = SpielController.erstellePropPfad();

        syspath = syspath.substring(0, syspath.lastIndexOf("/"));

        String[] pfadSplit = path.split("/");
        String dateiName = pfadSplit[pfadSplit.length - 1];

        File f = new File(syspath + "/" + dateiName);

        if (!f.exists() || overwrite) {
            try {

                InputStream stream = Main.class.getResourceAsStream(path);

                byte[] fileBytes = inputStreamToByteArray(stream);

                ByteArrayOutputStream out = new ByteArrayOutputStream(fileBytes.length);

                FileOutputStream fo = new FileOutputStream(f);

                fo.write(fileBytes);

                fo.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f.getAbsolutePath();

    }

    private static byte[] inputStreamToByteArray(InputStream inStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = inStream.read(buffer)) > 0) {
            baos.write(buffer, 0, bytesRead);
        }
        return baos.toByteArray();
    }
}
