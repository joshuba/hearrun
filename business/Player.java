package hearrun.business;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


/**
 * Created by Josh on 28.12.16
 */
public class Player {

    public Player() {

    }

    public void play(String file, boolean loop) {
        if (file.startsWith("/hearrun"))
            file = Main.class.getResource(file).toString();

        MediaPlayer player = new MediaPlayer(new Media(file));

        player.setCycleCount(20);

        player.play();
    }

    public void stop() {
    }



    public void play(String file) {
    }

    public void playRandomNSeconds(String file, int n) {

    }

    public void setVolume(float volume){

    }

    public int getVolume(){
        return 0;

    }

    public void setMute(boolean anAus){

    }

    public void fadeIn(){
    }

    public void fadeOut(){

    }
}



