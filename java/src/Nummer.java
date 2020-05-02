public class Nummer {
    private double tijdsduur;
    private String naam;
    private String artiest;
    private int nummerId;

    public Nummer(int nummerId, String naam, String artiest, double tijdsduur){
        this.nummerId = nummerId;
        this.naam = naam;
        this.artiest = artiest;
        this.tijdsduur = tijdsduur;
    }

    public String toString(){
        return naam + " door: " +artiest+ " tijdsduur: " +tijdsduur + " min";
    }

    public String getNaam() {
        return naam;
    }
}

