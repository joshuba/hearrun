package hearrun.business;

import ddf.minim.AudioListener;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Josh on 28.12.16.
 */
public class Player {
    private SimpleAudioPlayer player;
    private SimpleMinim minim;
    private boolean loop;
    private Thread loopedPlayer;


    public Player() {
        minim = new SimpleMinim(true);


    }

    public void play(String file, boolean loop) {

        player = minim.loadMP3File(file);
        if (loop) {
            loopedPlayer = new Thread() {

                @Override
                public void interrupt () {
                    minim.stop();
                    super.interrupt();
                }

                @Override
                public void run() {

                    while (true) {
                        if (!player.isPlaying()) {
                            player = minim.loadMP3File(file);
                            player.play(0);
                            minim.stop();
                        }
                    }
                }
            };
            loopedPlayer.setDaemon(true);
            loopedPlayer.start();
        }else {
            play(file);
        }
    }

    public void stopLoop () {
        if (loopedPlayer != null)
            loopedPlayer.interrupt();
    }

    public void play(String file) {
        Thread player = new Thread(() -> {
            minim.loadMP3File(file).play();
            minim.stop();
        });
        player.start();
    }

    public void playRandomNSeconds(String file, int n) {
        try {
            player = minim.loadMP3File(file);

            int start = (new Random().nextInt(player.length() - n));
            int end = start + n;

            new Thread(() -> {
                player.play(start);
                try {
                    Thread.sleep(n * 1000);
                } catch (InterruptedException ignored) {}
                minim.stop();
            }).start();
        } catch (IllegalArgumentException e) {
            System.err.println("Der Song " + file + " ist zu kürzer als " + n + "s!\n\n");
            e.printStackTrace();
        }
    }
}

