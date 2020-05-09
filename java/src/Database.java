import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static boolean verbinding = true;


    // Functie om verbinding te maken met de database.
    private static Connection maakVerbinding() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            verbinding = true;
            return conn;

        } catch (Exception e) {
            verbinding = false;
            System.out.println("Failed to connect to database");
            return null;
        }
    }

    // Functie voor het inserten van temperatuur, luchtdruk, -vochtigheid en -sterkte.
    public static void insertLog(double temperatuur, int luchtdruk, int luchtvochtigheid, int lichtsterkte) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "INSERT INTO log(Temperatuur, Luchtvochtigheid, Luchtdruk, Lichtsterkte)\n" +
                    "VALUES (" + temperatuur + ", " + luchtvochtigheid + ", " + luchtdruk + ", " + lichtsterkte + ")";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie voor inserten van temperatuur, luchtdruk en -vochtigheid.
    public static void insertLog(double temperatuur, int luchtdruk, int luchtvochtigheid) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "INSERT INTO log(Temperatuur, Luchtvochtigheid, Luchtdruk)\n" +
                    "VALUES (" + temperatuur + ", " + luchtvochtigheid + ", " + luchtdruk + ")";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie voor het inserten van lichtsterkte.
    public static void insertLog(int lichtsterkte) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "INSERT INTO log(Lichtsterkte)\n" +
                    "VALUES (" + lichtsterkte + ")";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie voor het inserten van een profiel.
    public static void insertProfiel(String gebruikersnaam) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "insert into profile(Gebruikersnaam)\n" +
                    "VALUES ('" + gebruikersnaam + "')";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie voor het ophalen van alle profielen.
    public static ArrayList<Profiel> selectProfielen() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return null;

            String query = "SELECT * FROM profile";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            ArrayList<Profiel> resultaat = new ArrayList<>();
            while (rs.next()) { // Array met profielen vullen met de gebruikersnamen
                Profiel p = new Profiel(
                        (String) rs.getObject(2),
                        (int) rs.getObject(1),
                        (double) rs.getObject(3),
                        (int) rs.getObject(4)
                );
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

    // Functie voor het updaten van de temperatuur per profiel (instelling, wanneer gaat de kachel aan).
    public static void updateProfielTemp(double temperatuur, int id) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "UPDATE profile\n" +
                    "SET TempVerwarmen = '" + temperatuur + "'\n" +
                    "WHERE ProfileId = '" + id + "'";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie voor het updaten van het licht per profiel (instelling, wanneer gaat de lamp aan).
    public static void updateProfielLicht(int licht, int id) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "UPDATE profile\n" +
                    "SET LichtWaarde = '" + licht + "'\n" +
                    "WHERE ProfileId = '" + id + "'";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie om het laatst ingelogde profiel op te halen.
    public static Profiel selectRecentsteProfiel() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return null;

            String query = "SELECT * FROM profile\n" +
                    "WHERE LaatsteLogin = (\n" +
                    "\tSELECT MAX(LaatsteLogin) FROM profile\n" +
                    ");";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            // wanneer er geen profiel gevonden wordt in de database wordt een prfiel met ID -1 gereturned.
            Profiel p = new Profiel("", -1, 20, 5);
            while (rs.next()) {
                p = new Profiel(
                        (String) rs.getObject(2),
                        (int) rs.getObject(1),
                        (double) rs.getObject(3),
                        (int) rs.getObject(4)
                );
            }

            conn.close();
            return p;

        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    // Functie om het laatste ingelogde profile te updaten.
    public static void updateProfielLaatsteLogin(int id) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "UPDATE profile\n" +
                    "SET LaatsteLogin = NOW()\n" +
                    "WHERE ProfileId = " + id + ";";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Functie om alle nummers op te halen.
    public static ArrayList<Nummer> selectNummers() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return null;

            String query = "SELECT * FROM nummer";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            ArrayList<Nummer> resultaat = new ArrayList<>();
            while (rs.next()) { // Array met profielen vullen met de gebruikersnamen
                Nummer n = new Nummer(
                        (int) rs.getObject(1),
                        (String) rs.getObject(2),
                        (String) rs.getObject(3),
                        (int) rs.getObject(4),
                        (String) rs.getObject(5)
                );
                resultaat.add(n);
            }

            conn.close();
            return resultaat;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat mis bij ophalen nummers");
            return new ArrayList<Nummer>();
        }
    }

    // Functie om alle afspeellijsten op te halen.
    public static ArrayList<Afspeellijst> selectAfspeellijsten(int profileId) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return null;

            String query = "SELECT * FROM afspeellijst where ProfileId = '" + profileId + "'";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            ArrayList<Afspeellijst> resultaat = new ArrayList<>();
            while (rs.next()) { // Array met afspeellijsten vullen met de afspeellijst-informatie
                Afspeellijst a = new Afspeellijst(
                        (int) rs.getObject(1),
                        (int) rs.getObject(2),
                        (String) rs.getObject(3)

                );
                resultaat.add(a);
            }

            conn.close();
            return resultaat;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat mis bij ophalen afspeellijsten");
            return new ArrayList<Afspeellijst>();
        }
    }

    // Functie om de nummers uit een bepaalde afspeellijst op te kunnen halen.
    public static ArrayList<Nummer> selectNummersUitAfspeellijst(int afspeellijstId) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return null;

            String query = "select * from nummer as n join afspeellijst_nummer as an on n.NummerId = an.NummerId join afspeellijst as a on a.AfspeellijstId = an.AfspeellijstId where a.AfspeellijstId ='" + afspeellijstId + "'";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();


            ArrayList<Nummer> resultaat = new ArrayList<>();
            while (rs.next()) {
                Nummer nummer = new Nummer(
                        (int) rs.getObject(1),
                        (String) rs.getObject(2),
                        (String) rs.getObject(3),
                        (int) rs.getObject(4),
                        (String) rs.getObject(5)
                );

                resultaat.add(nummer);
            }
            conn.close();
            return resultaat;

        } catch (Exception e) {
            System.out.println("Ging wat mis bij ophalen van de nummers van een afspeellijst");
            return new ArrayList<Nummer>();
        }
    }

    // Functie voor het inserten van een nieuwe afspeellijst.
    public static void insertAfspeellijst(int ProfileId, String Naam) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "INSERT INTO afspeellijst (ProfileId, Naam) VALUES ('" + ProfileId + "', '" + Naam + "')";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat fout bij het inserten van een afspeellijst");
        }
    }


    // Functie voor het inserten van een nummer in een bepaalde afspeellist.
    public static void insertNummerInAfspeellijst(int AfspeellijstId, int NummerId) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "INSERT INTO afspeellijst_nummer VALUES ('" + AfspeellijstId + "', '" + NummerId + "')";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat fout bij het inserten van een nummer in een afspeellijst");
        }
    }

    // Functie voor het verwijderen van een nummer uit een bepaalde afspeellist.
    public static void deleteNummerUitAfspeellijst(int AfspeellijstId, int NummerId) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "delete from afspeellijst_nummer where AfspeellijstId = '" + AfspeellijstId + "' AND NummerId =  '" + NummerId + "'";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat fout bij verwijderen van een nummer uit een afspeellijst");
        }
    }



    // Functie voor het verwijderen van een afspeellijst.
    public static void deleteAfspeellijst(int AfspeellijstId) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) return;

            String query = "delete from afspeellijst_nummer where AfspeellijstId = '" + AfspeellijstId + "'";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            String query2 = "delete from afspeellijst where AfspeellijstId = '" + AfspeellijstId + "'";

            PreparedStatement preparedStmt2 = conn.prepareStatement(query2);
            preparedStmt2.execute();

            conn.close();


        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat fout bij het verwijderen van een afspeellijst");
        }
    }

}
