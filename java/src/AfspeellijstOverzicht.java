import java.util.ArrayList;

public class AfspeellijstOverzicht {



    public AfspeellijstOverzicht(){
    Database.selectDBafspeellijsten();

    }


    public static void main(String[] args) {
        AfspeellijstOverzicht overzicht = new AfspeellijstOverzicht();
    }
}
