package hearrun.business;

import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Random;

/**
 * Created by Josh on 28.12.16.
 */
public class Player {
    private SimpleAudioPlayer player;
    private SimpleMinim minim;
    private boolean loop;
    private Thread loopedPlayer;
    private float aktVolume;
    private int panVol;
    private boolean fadeIn;
    private boolean fadeOut;


    public Player() {
        minim = new SimpleMinim();
        aktVolume = 80;



    }

    public void play(String file, boolean loop) {

        player = minim.loadMP3File(file);
        if (loop) {
            player = minim.loadMP3File(file);

            player.loop();
        }else {
            play(file);
        }
    }

    public void stop() {
        minim.stop();
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
            SimpleMinim minim = new SimpleMinim(true);
            player = minim.loadMP3File(file);



            int start = (new Random().nextInt(player.length() - n));

                player.play(start);
            fadeIn();


            Timeline timeline = new Timeline(new KeyFrame(
                    Duration.millis(1000*n),
                    a -> {
                        if(!fadeOut){
                            fadeOut();
                        }

                    }));
            timeline.play();

        } catch (IllegalArgumentException e) {
            System.err.println("Der Song " + file + " ist zu kürzer als " + n + "s!\n\n");
            e.printStackTrace();
        }
    }

    public void setVolume(float volume){
        System.out.println(player.getGain());
        float gain = (float) (20*Math.log10(volume/100));   //Linear zu DB umrechnungsformel
        player.setGain(gain);

    }

    public void setMute(boolean anAus){
        if(anAus){
            player.mute();
        }else{
            player.unmute();
        }

    }


    public static void main (String[] args){
        new Player().playRandomNSeconds("cantina.mp3", 5);
    }

    private int gebeVolIn(){
        if(panVol < aktVolume){
            panVol += 1;
            System.out.println(panVol);
            return panVol;
        }else{
            System.out.println("Fehler beim Einfaden");
            panVol = 0;
            return (int)aktVolume;
        }
    }

    private int gebeVolOut(){
        System.out.println(panVol);

        if(panVol > 0){
            panVol -= 1;
            return panVol;
        }else{
            System.out.println("Fehler beim Ausfaden");
            panVol = 0;
            return 0;
        }
    }

    private void fadeIn(){
        fadeIn = true;
        setVolume(0);
        panVol = 0;

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(10),
                a -> setVolume(gebeVolIn())));
        timeline.setCycleCount((int)aktVolume);
        timeline.setOnFinished(event -> {
            panVol = (int) aktVolume;
            fadeIn = false;

        });

        if(!fadeOut)
            timeline.play();

    }

    public void fadeOut(){
        fadeOut = true;

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(10),
                a -> setVolume(gebeVolOut())));
        timeline.setCycleCount((int)aktVolume);
        timeline.setOnFinished(event -> {
            panVol = 0;
            fadeOut = false;
            minim.stop();
        });

        if(!fadeIn)
            timeline.play();


    }


}



