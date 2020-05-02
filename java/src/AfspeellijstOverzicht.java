import java.util.ArrayList;

public class AfspeellijstOverzicht {
    private ArrayList<Afspeellijst> afspeellijsten;


    public AfspeellijstOverzicht(){
    afspeellijsten = Database.selectDBafspeellijsten();

    }

    public void printAfspeellijsten(){
        for (Afspeellijst afspeellijst : afspeellijsten){
            System.out.println(afspeellijst);
        }
    }

    public static void main(String[] args) {
        AfspeellijstOverzicht overzicht = new AfspeellijstOverzicht();
        overzicht.printAfspeellijsten();


    }
}
