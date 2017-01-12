package hearrun.business;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Leo on 10.01.2017.
 */
public class Util {

    public static ID3v2[] getAllTitles(File[] files){
        ID3v2[] titel = new ID3v2[files.length];
        for (int i = 0; i < files.length; i++){
            try {
                titel[i] = new Mp3File(files[i].getAbsolutePath()).getId3v2Tag();
            } catch (IOException | UnsupportedTagException | InvalidDataException e) {
                e.printStackTrace();
            }
        }
        return titel;
    }

    public static Image[] getAllCovers(File[] files){
        ArrayList<Image> covers = new ArrayList<>();

        for(int i = 0; i < files.length; i++){
            try{

                byte[] coverBytes = new Mp3File(files[i].getAbsolutePath()).getId3v2Tag().getAlbumImage();
                if (coverBytes != null)
                    covers.add(SwingFXUtils.toFXImage(ImageIO.read(new ByteArrayInputStream(coverBytes)), null));


            } catch (UnsupportedTagException | IOException | InvalidDataException e) {
                e.printStackTrace();
            }
        }
        return covers.toArray(new Image[covers.size()]);
    }

}
