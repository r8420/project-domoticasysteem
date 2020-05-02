import java.util.ArrayList;

public class NummerOverzicht {
    private ArrayList<Nummer> nummers;

    public NummerOverzicht(){
    nummers = Database.selectDBnummers();
    }

    public void printNummers(){
            for (Nummer n : nummers){
                System.out.println(n);
            }
        }


    public static void main(String[] args) {
        NummerOverzicht overzicht = new NummerOverzicht();
        overzicht.printNummers();
    }
}
