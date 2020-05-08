import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MainInput {

    static Socket s;
    static PrintWriter pw;
    private SerialPort sp;

    public String[] piSensoren() {
        String[] splitStr = new String[3];

        try {
            byte[] bytes = s.getInputStream().readNBytes(19);
            String string = new String(bytes);
            splitStr = string.split("\\s+");

        } catch (IOException u) {
            System.out.println("fail");
            u.printStackTrace();
        }
        return splitStr;
    }

    public boolean socketStart() {
        try {
            s = new Socket("piri", 8000);
            return true;

        } catch (IOException IE) {
            System.out.println("socketStart: No server found");
            return false;
        }
    }

    public void socketStop() {
        try {
            pw = new PrintWriter(s.getOutputStream());
            pw.write("stop");
            pw.flush();
            pw.close();
            s.close();

        } catch (IOException IE) {
            System.out.println("fail");
        }
    }

    public boolean arduinoStart() throws InterruptedException {
        sp = SerialPort.getCommPort("COM3");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("arduinoStart: succes");
//            TimeUnit.MILLISECONDS.sleep(100);
            return true;

        } else {
            System.out.println("arduinoStart: fail");
            return false;
        }
    }

    public String arduinoSensor() {
        String ldrWaarde = null;

        try {
            while (sp.getInputStream().available() > 0) {
                byte[] bytes = sp.getInputStream().readNBytes(4);
                ldrWaarde = new String(bytes);
                System.out.println("LDR waarde: " + ldrWaarde);

            }
        } catch (NullPointerException | IOException NE) {
            System.out.println("Arduinno LDR leest niks");
            ldrWaarde = null;
        }
        return ldrWaarde;
    }

    public void sendMessage(String Naam) {
        try {
            pw = new PrintWriter(s.getOutputStream());
            pw.write(Naam);
            pw.flush();
            System.out.println("gelukt");
        }catch (IOException | NullPointerException IOE){
            System.out.println("geen verbinding met pi");
        }

    }
}



