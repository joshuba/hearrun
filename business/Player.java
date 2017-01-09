package hearrun.business;

import de.hsrm.mi.eibo.simpleplayer.SimpleAudioPlayer;
import de.hsrm.mi.eibo.simpleplayer.SimpleMinim;
import java.io.File;

/**
 * Created by Josh on 28.12.16.
 */
public class Player {
    private String root;
    private Playlist playlist;
    private boolean play;

    private SimpleMinim simpleMinim;
    private SimpleAudioPlayer player;
    private Track aktTrack;

    Player(String root) {
        this.root = root;
        this.playlist = new Playlist(root);
        this.play = false;

        simpleMinim = new SimpleMinim(true);
        aktTrack = playlist.get();
        if (player == null) {

            player = simpleMinim.loadMP3File(aktTrack.getPath());

        }
    }

    public void play(double von, double bis) {


        if (player == null) {

            player = simpleMinim.loadMP3File(aktTrack.getPath());
        }
        player.play();

    }

    public void play(int nr){
        play(playlist.get(nr));
    }

    private void play(Track t) {


        simpleMinim.stop();

        setAktTrack(t);
        player.play();
    }

    //MP3Player beenden
    void stop() {
        simpleMinim.stop();
    }

    public void pause() {
        player.pause();
    }


    private void setAktTrack(Track t) {
        aktTrack = t;
        player = simpleMinim.loadMP3File(t.getPath());

    }

    public Track getAktTrack() {
        return aktTrack;
    }


    public void setPlay(boolean b) {
        play = b;
    }

    public boolean getPlay() {
        return play;
    }

    public void setVolume(float volume){



        float gain = (float) (20*Math.log10(volume/100));   //Linear zu DB umrechnungsformel

        player.setGain(gain);

    }

    public void mute(){
        if(player.isMuted()){
            player.unmute();
        }else{
            player.mute();
        }
    }

    public String getRoot(){
        return new File(root).getAbsolutePath();
    }

    public void setPosition(double percent){

        int millis =(int) (player.length() * percent);

        player.play(millis);
        player.pause();

    }


    public float getAktTime(){
        return player.position();
    }

    public float getAktLength(){
        return player.length()-player.position();
    }

    public long getLength(){
        return player.length();
    }

    public boolean isMuted(){
        return player.isMuted();
    }

    public void changeRoot(String root){
        playlist.changeRoot(root);
    }

    public int getIndex(){
        return playlist.getIndex();
    }

    public void refreshTrack(){
        player.pause();
        aktTrack = playlist.get();
        player = simpleMinim.loadMP3File(aktTrack.getPath());
    }

}

