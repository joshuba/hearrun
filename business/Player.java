package hearrun.business;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Josh on 28.12.16.
 */
public class Player {
    SimpleAudioPlayer player;

    public Player(String path){
        player = new SimpleMinim().loadMP3File(path);
    }

    public void play() {
        player.play();
    }

    public static void main (String[] args){
        File audioFile = new File("");
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            Clip audioClip = (Clip) AudioSystem.getLine(info);

            audioClip.open(audioInputStream);
            audioClip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


}

