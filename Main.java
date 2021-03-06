package hearrun;

import hearrun.controller.SpielController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;

/**Hear & Run ist eine Mischung aus Brettspiel und Musikquiz
 *
 *
 * @author Leo Back & Joshua Barth
 */
public class Main extends Application {
    public static final Color spielerEinsFarbe = new Color(221f / 255, 66f / 255, 85f / 255, 1);
    public static final Color spielerZweiFarbe = new Color(51f / 255, 195f / 255, 98f / 255, 1);
    public static final Color spielerDreiFarbe = new Color(66f / 255, 107f / 255, 221f / 255, 1);
    public static final Color spielerVierFarbe = new Color(109f / 255, 106f / 255, 94f / 255, 1);
    public static boolean testMode = false;
    public static boolean coverTestMode = false;


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


        primaryStage.setTitle("Hear & Run");
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
        if(args.length > 0 && args[0].equals("test")){
            System.out.println("Gegebene Argumente ->" + args[0] + "<-");

            System.out.println("TESTMODUS\n Achtung Wuerfel kann nur noch einen Wert würfeln!");
            testMode = true;
        }
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
