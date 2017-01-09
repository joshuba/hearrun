package hearrun.business;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Josh on 28.12.16.
 */
public class Playlist {
    private ArrayList<Track> trackList;
    private int akt;

    Playlist(String root) {
        this.akt = 0;
        this.trackList = new ArrayList<>();
        readTracks(root);
    }

    void changeRoot(String root) {
        this.akt = 0;
        trackList = new ArrayList<>();
        readTracks(root);
    }

    private void readTracks(String path) {
        File rootFile = new File(path);
        File[] fileArray = rootFile.listFiles();

        if (fileArray != null) {
            for (File f : fileArray) {
                if (f.getName().endsWith(".mp3")) {
                    trackList.add(new Track(f.getAbsolutePath()));
                } else if (f.isDirectory()) {
                    readTracks(f.getAbsolutePath());
                }
            }
        }
        Collections.shuffle(trackList); //Wird direkt geshuffelt
    }

    public Track get(int nr) {
        akt = nr;
        return trackList.get(nr);

    }

    Track get() {
        return get(akt);
    }

    public int getIndex(){
        return akt;
    }


}
