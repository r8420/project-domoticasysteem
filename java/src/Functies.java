import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class Functies {


    /**
     * Maakt een label met daarin de afbeelding van de meegegeven afbeeldingslocatie
     * een geldige locatie is "src/images/profiel.png"
     */
    public static JLabel maakFotoLabel(String locatie) {
        try {
            BufferedImage myPicture = ImageIO.read(new File(locatie));
            return new JLabel(new ImageIcon(myPicture));
        } catch (IOException e) {
            return new JLabel("<image not found>");
        }
    }

    public static String intToTimestamp(int seconds) {
        int min = seconds / 60;
        int sec = (seconds - min * 60);
        return min + ":" + ((sec < 10) ? ("0" + sec) : sec);
    }

}
