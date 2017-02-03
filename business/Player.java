package hearrun.business;

import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import hearrun.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.FileNotFoundException;
import java.util.Random;

/**
 * Der Musik-Player. Er kapselt Funktionalitäten des Minim-Players.
 */
public class Player {
    private SimpleAudioPlayer player;
    private SimpleMinim minim;
    private boolean fadeIn;
    private boolean fadeOut;


    public Player() {
        minim = new SimpleMinim();
        fadeOut = false;
        fadeIn = false;


    }

    /**
     * Spielt den übergebenen Song per Threading ab.
     *
     * @param file Pfad zur Datei, die abgespielt werden soll.
     * @param loop ob der Song gelooped wird oder nicht
     */
    public void play(String file, boolean loop) {

        if (loop) {
            minim = new SimpleMinim();
            new Thread(() -> {
                player = minim.loadMP3File(file);
                player.loop();
            }).start();
        } else {
            play(file);
        }
    }

    public void play(String file) {
            new Thread(() -> {
                SimpleMinim minim = new SimpleMinim();
                minim.loadMP3File(Main.getFilePathFromResourcePath(file)).play();
                minim.stop();
            }).start();
    }

    /**
     * Player-Funktion für das Abspielen von Song-Ausschnitten in den Quiz-Fragen.
     * <p>
     * Um das häufig auftretende "ausklingen" eines Songs, das dazu führt, das der Songausschnitt
     * nicht identifizierbar wird (da kein oder nur wenige Sekunden Sound angespielt wird), wird
     * der zufällige Ausschnitt nur aus den ersten 80% des Songs gewählt.
     *
     * @param file Pfad zum MP3-File.
     * @param n    Sekunden, die aus dem Lied abgespielt werden.
     */
    public void playRandomNSeconds(String file, int n) {
        try {
            minim = new SimpleMinim(true);
            player = minim.loadMP3File(file);

            int start = (new Random().nextInt((int) (0.8 * player.length() - n)));

            player.play(start);
            fadeIn();

            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000 * n),
                    a -> {
                        if (!fadeOut) {
                            //fadeOut();
                        }

                    }));
            timeline.play();

        } catch (IllegalArgumentException e) {
            System.err.println("Der Song " + file + " ist kürzer als " + n + "s!\n\n");
            e.printStackTrace();
        }
    }

    public void stop() {
        minim.stop();
    }

    private void setVolume(float volume) {
        float gain = (float) (20 * Math.log10(volume / 100));   //Linear zu DB umrechnungsformel
        player.setGain(gain);
    }

    private int getVolume() {
        float gain = player.getGain();
        return (int) Math.pow(10, ((gain / 20) + 2));

    }

    /**
     * Ermöglicht, die Musik-Laustärke langsam zu erhöhen
     */
    public void fadeIn() {
        int aktVol = getVolume();
        KeyFrame k1 = new KeyFrame(Duration.ZERO, a -> {
            setVolume(0);
            System.out.println(getVolume());
        });

        KeyFrame k2 = new KeyFrame(Duration.millis(300), a -> {
            setVolume(aktVol / 10);
            System.out.println(getVolume());
        });

        KeyFrame k3 = new KeyFrame(Duration.millis(500), a -> {
            setVolume(aktVol / 4);
            System.out.println(getVolume());
        });

        KeyFrame k4 = new KeyFrame(Duration.millis(600), a -> {
            setVolume(aktVol / 3);
            System.out.println(getVolume());
        });

        KeyFrame k5 = new KeyFrame(Duration.millis(700), a -> {
            setVolume(aktVol / 2);
            System.out.println(getVolume());
            fadeIn = false;
        });

        KeyFrame k6 = new KeyFrame(Duration.millis(800), a -> {
            setVolume(aktVol);
            System.out.println(getVolume());
            fadeIn = false;
        });

        Timeline fadein = new Timeline();
        fadein.setAutoReverse(false);
        fadein.setCycleCount(1);
        fadein.getKeyFrames().addAll(k1, k2, k3, k4, k5, k6);

        fadeIn = true;

        if (!fadeOut) {
            fadein.play();
        }


    }

    /**
     * Ermöglicht, die Musik langsam auf Lautstärke 0 abzusenken und anschließend den player zu beenden.
     */
    public void fadeOut() {
        int aktVol = getVolume();
        KeyFrame k1 = new KeyFrame(Duration.ZERO, a -> {
            setVolume(aktVol);

        });

        KeyFrame k2 = new KeyFrame(Duration.millis(100), a -> {
            setVolume(aktVol - ((int) aktVol / 4));


        });

        KeyFrame k3 = new KeyFrame(Duration.millis(200), a -> {
            setVolume(aktVol - ((int) aktVol / 2));


        });

        KeyFrame k4 = new KeyFrame(Duration.millis(300), a -> {
            setVolume(0);
            fadeOut = false;


        });

        Timeline fadeout = new Timeline();
        fadeout.setAutoReverse(false);
        fadeout.setCycleCount(1);
        fadeout.getKeyFrames().addAll(k1, k2, k3, k4);
        fadeout.setOnFinished(e -> stop());

        fadeOut = true;

        if (!fadeIn)
            fadeout.play();
    }
}



