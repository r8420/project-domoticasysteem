import java.util.ArrayList;

public class Afspeellijst {

    private int afspeellijstId;
    private int profileId;
    private String naam;
    private ArrayList<Nummer> nummers;
    private Nummer nummer;


    public Afspeellijst(int afspeellijstId, int profileId, String naam){
       this.afspeellijstId = afspeellijstId;
       this.profileId = profileId;
        this.naam = naam;
    }

    public void voegNummerToe(Nummer nummer){
        nummers.add(nummer);
    }

   public String toString(){

        return "Afspeellijstnaam: " + naam;
   }

    public  String getNaam() {
        return naam;
    }

    public String printNummers(){
        return nummer.getNaam();
    }



}

