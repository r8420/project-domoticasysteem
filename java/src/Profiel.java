
public class Profiel {
    private String naam;
    private int id;
    private double tempVerwarmen;
    private int lichtWaarde;

    public Profiel(String naam){
        this.naam = naam;
    }

    @Override
    public String toString() {
        return "Profiel{" +
                "naam='" + naam + '\'' +
                ", id=" + id +
                ", tempVerwarmen=" + tempVerwarmen +
                ", lichtWaarde=" + lichtWaarde +
                '}';
    }

    public Profiel(String naam, int id, double tempVerwarmen, int lichtWaarde) {
        this.naam = naam;
        this.id = id;
        this.tempVerwarmen = tempVerwarmen;
        this.lichtWaarde = lichtWaarde;
    }

    public String getNaam() {
        return naam;
    }

    public double getTempVerwarmen() {
        return tempVerwarmen;
    }

    public int getId() {
        return id;
    }

    public int getLichtWaarde() {
        return lichtWaarde;
    }
}
