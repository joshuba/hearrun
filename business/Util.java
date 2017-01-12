package hearrun.business;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;

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

}
