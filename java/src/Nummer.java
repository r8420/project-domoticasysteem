import java.util.ArrayList;

public class Nummer {
    private int tijdsduur;
    private String naam;
    private String artiest;
    private String bestandsNaam;
    private int nummerId;



    public int getNummerId() {
        return nummerId;
    }

    public int getTijdsduur() {
        return tijdsduur;
    }

    public Nummer(int nummerId, String naam, String artiest, int tijdsduur, String bestandsNaam) {
        this.nummerId = nummerId;
        this.naam = naam;
        this.artiest = artiest;
        this.tijdsduur = tijdsduur;
        this.bestandsNaam = bestandsNaam;
    }

    public String toString() {
        return naam + " door: " + artiest + " tijdsduur: " + tijdsduur + " min";
    }

    public String getNaam() {
        return naam;
    }

    public String getArtiest() {
        return artiest;
    }

    public String getBestandsNaam() {
        return bestandsNaam;
    }





}

