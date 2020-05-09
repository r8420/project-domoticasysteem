import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class MainInput {

    static Socket s;
    static PrintWriter pw;
    private SerialPort sp;

    public boolean piIsConnected() {
        return s != null;
    }

    public void startPiSocket() {
        String host = "pisander";
        int port = 8000;

        try {
            s = new Socket(host, port);
            System.out.println("Succesfully connected to Pi on [host: \"" + host + "\", port: " + port + ']');
            pw = new PrintWriter(s.getOutputStream());

        } catch (IOException IE) {
            System.out.println("Couldn't connect to Pi on [host: \"" + host + "\", port: " + port + ']');
        }
    }

    public void sendPiMessage(String message) {
        if (s == null) return;
        try {

            // leeg de buffer als daar nog wat in staat
            int available = s.getInputStream().available();
            if (available > 0) {
                String oldMsg = new String(s.getInputStream().readNBytes(available));
                System.out.println("Pi buffer bevat ongelezen informatie: \n\t" + oldMsg);
            }

            pw.write(message);
            pw.flush();

        } catch (IOException IOE) {
            System.out.println("Couldn't send Pi a message due to IOException");
            IOE.printStackTrace();
        }
    }

    public String waitForPiResponse() {
        if (s == null) return null;
        try {
            long startTimeNano = System.nanoTime();

            int sleepTime = 5; // ms tussen checks voor antwoord
            long maxWaitTime = 1000000 * 500; // maximale wachttijd voordat method afbreekt
            long timeWaited;

            // wacht totdat er een response is. Breek af na na maxWaitTime
            int available = s.getInputStream().available();
            while (available == 0) {
                timeWaited = (System.nanoTime() - startTimeNano);
                if (timeWaited > maxWaitTime) {
                    System.out.println("Ontvangen duurt te lang("+timeWaited/1000000+"ms). waitForResponse() afgebroken.");
                    return null;
                }
                Thread.sleep(sleepTime);
                available = s.getInputStream().available();
            }

            String message = new String(s.getInputStream().readNBytes(available));

            int passedMs = (int) (System.nanoTime() - startTimeNano) / 1000000;
            System.out.println("Received answer (" + passedMs + "ms): \n\t" + message);
            return message;

        } catch (IOException u) {
            System.out.println("getMessage() IOException");
            u.printStackTrace();

        } catch (InterruptedException ie) {
            System.out.println("getMessage() InterruptedException");
        }
        return null;
    }

    public boolean arduinoStart() throws InterruptedException {
        sp = SerialPort.getCommPort("COM3");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (sp.openPort()) {
            System.out.println("Succesfully connected to Arduino");
//            TimeUnit.MILLISECONDS.sleep(100);
            return true;

        } else {
            System.out.println("Couldn't connect to Arduino");
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
}



