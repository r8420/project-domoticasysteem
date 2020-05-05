import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Database {

    private static boolean verbinding = true;

    private static Connection maakVerbinding() {
        try {

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn =  DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            verbinding = true;
            return conn;

        } catch (Exception e) {
            verbinding = false;
            System.out.println("Geen database-verbinding");
            return null;
        }
    }

    public static void insertDBLog(double temperatuur, int luchtdruk, int luchtvochtigheid, int lichtsterkte) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

            String query = "INSERT INTO log(Temperatuur, Luchtvochtigheid, Luchtdruk, Lichtsterkte)\n" +
                    "VALUES (" + temperatuur + ", " + luchtvochtigheid + ", " + luchtdruk + ", " + lichtsterkte + ")";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void insertDBLog(double temperatuur, int luchtdruk, int luchtvochtigheid) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

            String query = "INSERT INTO log(Temperatuur, Luchtvochtigheid, Luchtdruk)\n" +
                    "VALUES (" + temperatuur + ", " + luchtvochtigheid + ", " + luchtdruk + ")";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void insertDBLog(int lichtsterkte) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

            String query = "INSERT INTO log(Lichtsterkte)\n" +
                    "VALUES (" + lichtsterkte + ")";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void insertDBprofile(String gebruikersnaam) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

            String query = "insert into profile(Gebruikersnaam)\n" +
                    "VALUES ('" + gebruikersnaam + "')";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.execute();

            conn.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static ArrayList<Profiel> selectDBprofiles() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return null;}

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

    public static void updateDBtemp(double temperatuur, int id) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

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

    public static void updateDBlicht(int licht, int id) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

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

    public static Profiel selectLastProfile() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return null;}

            String query = "SELECT * FROM profile\n" +
                    "WHERE LaatsteLogin = (\n" +
                    "\tSELECT MAX(LaatsteLogin) FROM profile\n" +
                    ");";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            rs.next();
            Profiel p = new Profiel(
                    (String) rs.getObject(2),
                    (int) rs.getObject(1),
                    (double) rs.getObject(3),
                    (int) rs.getObject(4)
            );

            conn.close();
            return p;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public static void updateLastUsedProfile(int id) {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return;}

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


    public static ArrayList<Nummer> selectDBnummers() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return null;}

            String query = "SELECT * FROM nummer";

            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet rs = preparedStmt.executeQuery();

            ArrayList<Nummer> resultaat = new ArrayList<>();
            while (rs.next()) { // Array met profielen vullen met de gebruikersnamen
                Nummer n = new Nummer(
                        (int) rs.getObject(1),
                        (String) rs.getObject(2),
                        (String) rs.getObject(3),
                        (double) rs.getObject(4)


                );
                resultaat.add(n);
            }

            conn.close();
            System.out.println(resultaat);
            return resultaat;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat mis bij ophalen nummers");
            return new ArrayList<Nummer>();
        }
    }

    public static ArrayList<Afspeellijst> selectDBafspeellijsten() {
        try {

            Connection conn = maakVerbinding();
            if (!verbinding) {return null;}

            String query = "SELECT * FROM afspeellijst";

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
            System.out.println(resultaat);
            return resultaat;

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Ging wat mis bij ophalen afspeellijsten");
            return new ArrayList<Afspeellijst>();
        }
    }

    public static ArrayList<Nummer> selectDBafspeellijstNummer(String welkeAfspeellijst) {

        try {

            Class.forName("com.mysql.jdbc.Driver");

            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/domotica", "root", "");
            String query = "select a.Naam, n.NummerId, n.Naam, n.Artiest, n.Tijdsduur from nummer as n join afspeellijst_nummer as an on n.NummerId = an.NummerId join afspeellijst as a on a.AfspeellijstId = an.AfspeellijstId where a.Naam ='" + welkeAfspeellijst +"'";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = conn.prepareStatement(query);

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            String afspeellijstnaam = "";
            int nummerId = 0;
            String nummerNaam = "";
            String nummerArtiest = "";
            double nummerTijdsduur = 0.0;


            ArrayList<Nummer> resultaat = new ArrayList<>();
            while (rs.next()) {
                System.out.println();
                afspeellijstnaam = rs.getString("a.Naam");
                Nummer nummer = new Nummer(
                        nummerId = rs.getInt("n.NummerId"),
                        nummerNaam = rs.getString("n.Naam"),
                        nummerArtiest = rs.getString("n.Artiest"),
                        nummerTijdsduur = rs.getDouble("n.TijdsDuur"));

                resultaat.add(nummer);
            }
            conn.close();
            System.out.println("Afspeellijst: " + afspeellijstnaam + " , met de nummers:\n" + resultaat);
            return resultaat;

        } catch (Exception e) {
            System.out.println("Ging wat mis bij ophalen gezeik");
            return new ArrayList<Nummer>();
        }
    }
}
