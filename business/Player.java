package hearrun.business;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

/**
 * Created by Josh on 28.12.16.
 */
public class Player {
    private String root;
    private Playlist playlist;
    private boolean play;
    private Track aktTrack;
    private Media media;
    private MediaPlayer player;
    private static final Duration FADE_DURATION = Duration.seconds(2.0); //fading todo



    public Player(String root){
        this.root = root;
        this.playlist = new Playlist(root);
        this.play = false;

        aktTrack = playlist.get();
        player = new MediaPlayer(media);

    }

    public void play(){
        if(media == null){
            media = new Media(new File(aktTrack.getPath()).toURI().toString());
        }
        player.play();
    }

    public void play(double von, double bis){

         if(media == null){
             media = new Media(new File(aktTrack.getPath()).toURI().toString());
         }
        player.setStartTime(new Duration(von));
        player.setStopTime(new Duration(bis));
        player.play();

    }


    public void pause(){
        player.pause();
    }

    public void setAktTrack(Track t){
        this.aktTrack = aktTrack;
    }

    public void stop(){
        player.stop();
    }



}

