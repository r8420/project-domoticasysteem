import java.util.ArrayList;

public class Afspeellijst {

    private int afspeellijstId;
    private int profileId;
    private String naam;
    private ArrayList<Nummer> nummers;
    private int currentSong;

    public int getAfspeellijstId() {
        return afspeellijstId;
    }

    public Afspeellijst(ArrayList<Nummer> nummers) {
        this.nummers = nummers;
    }

    public Afspeellijst(int afspeellijstId, int profileId, String naam) {
        this.afspeellijstId = afspeellijstId;
        this.profileId = profileId;
        this.naam = naam;
        this.currentSong = 0;

        this.nummers = Database.selectNummersUitAfspeellijst(afspeellijstId);
    }

    public String getNaam() {
        return naam;
    }

    public void setCurrentSong(int currentSong) {
        this.currentSong = currentSong;
    }

    public Nummer getCurrentSong() {
        if (nummers.size() > 0) return nummers.get(currentSong);
        else return null;
    }

    public void nextSong() {
        currentSong++;
        if (currentSong >= nummers.size()) {
            currentSong = 0;
        }
    }

    public void previousSong() {
        currentSong--;
        if (currentSong < 0) {
            currentSong = nummers.size() - 1;
        }
    }

    public int getAantalNummers() {
        return nummers.size();
    }
}

