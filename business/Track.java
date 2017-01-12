package hearrun.business;

import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.mpatric.mp3agic.InvalidDataException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * Track Objekt
 */
public class Track {
/*
    private SimpleStringProperty title;
    private SimpleStringProperty artist;
    private SimpleStringProperty album;
*/
    private String path;


    Track(String path) {
        this.path = path;

        /*
        try {
            Mp3File mp3File = new Mp3File(path);

            this.title = new SimpleStringProperty(mp3File.getId3v2Tag().getTitle());
            this.artist = new SimpleStringProperty(mp3File.getId3v2Tag().getArtist());
            this.album = new SimpleStringProperty(mp3File.getId3v2Tag().getAlbum());

        } catch (UnsupportedTagException | InvalidDataException | IOException e) {
            e.printStackTrace();
        }
        */
    }



/*
    public ObservableValue<String> getTitle() {
        return title;
    }

    public ObservableValue<String> getArtist() {
        return artist;
    }

    public ObservableValue<String> getAlbum() {
        return album;
    }
*/

    public String getPath(){
        return path;
    }
/*
    public Image getCover() {
        try {
            Mp3File mp3File = new Mp3File(path);

            byte[] coverBytes = mp3File.getId3v2Tag().getAlbumImage();

            if (coverBytes == null) {
                return null;
            }
            BufferedImage coverBuffered = ImageIO.read(new ByteArrayInputStream(coverBytes));

            return SwingFXUtils.toFXImage(coverBuffered, null);


        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            e.printStackTrace();
        }
        return null;

    }
*/
}
