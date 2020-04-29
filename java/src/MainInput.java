import com.fazecast.jSerialComm.*;

import java.io.IOException;
import java.net.Socket;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

        } catch (IOException u){
            System.out.println("fail");
            u.printStackTrace();
        }
        return splitStr;
    }

    public boolean socketStart() {
        try {
            s = new Socket("piri", 8000);
            return true;

        } catch (IOException IE){
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

        } catch (IOException IE){
            System.out.println("fail");
        }
    }

    public boolean arduinoStart(){
        sp = SerialPort.getCommPort("COM3");
        sp.setComPortParameters(9600, 8, 1, 0);
        sp.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING,0,0);

        if (sp.openPort()) {
            System.out.println("arduinoStart: succes");
            return true;

        } else {
            System.out.println("arduinoStart: fail");
            return false;
        }
    }

    public String arduinoSensor(){
        String Arduino = "";

        try {
            byte[] bytes = sp.getInputStream().readNBytes(3);
            Arduino = new String(bytes);
            System.out.println(Arduino);

        } catch (IOException IE){
            System.out.println("nothing to read");

        } catch (NullPointerException NE){
            System.out.println("geweldig jammer");
            Arduino = "0";
        }
        return Arduino;
    }

    public void insertDBLog(double temperatuur, int luchtdruk, int luchtvochtigheid, int lichtsterkte) {
        try {
            java.util.Date uDate = new java.util.Date();
            DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String s = df.format(uDate);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "INSERT INTO log(Date, Temperatuur, Luchtvochtigheid, Luchtdruk, Lichtsterkte)\n" +
                    "VALUES ('"+s+"', " + temperatuur+", " + luchtvochtigheid+ ", " + luchtdruk + ", " + lichtsterkte + ")";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insertDBLog(double temperatuur, int luchtdruk, int luchtvochtigheid) {
        try {
            java.util.Date uDate = new java.util.Date();
            DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String s = df.format(uDate);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "INSERT INTO log(Date, Temperatuur, Luchtvochtigheid, Luchtdruk)\n" +
                    "VALUES ('"+s+"', " + temperatuur+", " + luchtvochtigheid+ ", " + luchtdruk + ")";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void insertDBLog(int lichtsterkte) {
        try {
            java.util.Date uDate = new java.util.Date();
            DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String s = df.format(uDate);
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "INSERT INTO log(Date, Lichtsterkte)\n" +
                    "VALUES ('"+s+"', " + lichtsterkte + ")";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void insertDBprofile(String gebruikersnaam){
        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "insert into profile(Gebruikersnaam)\n" +
                    "VALUES ('"+gebruikersnaam+"')";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static ArrayList<Profiel> selectDBprofiles(){
        try {
            // mysql-connector.jar benodigd om dit te laten functioneren.
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "SELECT * FROM profile";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            ArrayList<Profiel> resultaat = new ArrayList<>(); // Array met profielen vullen met de gebruikersnamen
            while (rs.next()) {
                Profiel p = new Profiel(
                        (String)rs.getObject(2));

                resultaat.add(p);
            }

            conn.close();
            return resultaat;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat mis bij ophalen profielen");
            return new ArrayList<Profiel>();
        }
    }

    public static void updateDBtemp(double temperatuur, String profiel){
        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "UPDATE profile\n" +
                    "SET TempVerwarmen = '"+temperatuur+"'\n" +
                    "WHERE Gebruikersnaam = '"+profiel+"'";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            preparedStmt.execute();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static double selectDBtemp(String profiel) {
        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "SELECT TempVerwarmen FROM profile WHERE Gebruikersnaam = '"+profiel+"'";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            double TempVerwarmen =0.0;
            while (rs.next()) {
                TempVerwarmen = rs.getDouble(1);
            }
            conn.close();

            return TempVerwarmen;


        } catch (Exception e) {
            System.out.println("Ging wat mis bij ophalen temperatuur");
            return 0;
        }
    }


}



