import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;

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

    public boolean arduinoStart() {
        sp = SerialPort.getCommPort("COM3");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("arduinoStart: succes");
            return true;

        } else {
            System.out.println("arduinoStart: fail");
            return false;
        }
    }

    public String arduinoSensor() {
        String Arduino = "";

        try {
            byte[] bytes = sp.getInputStream().readNBytes(3);
            Arduino = new String(bytes);
            System.out.println(Arduino);

        } catch (IOException IE) {
            System.out.println("nothing to read");

        } catch (NullPointerException NE) {
            System.out.println("geweldig jammer");
            Arduino = "0";
        }
        return Arduino;
    }

    public void sendMessage(String Naam) throws IOException {
        pw = new PrintWriter(s.getOutputStream());
        pw.write(Naam);
        pw.flush();
        System.out.println("gelukt");

    }
}



