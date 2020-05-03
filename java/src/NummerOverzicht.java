import java.util.ArrayList;

public class NummerOverzicht {


    public NummerOverzicht(){
    Database.selectDBnummers();
    }




    public static void main(String[] args) {
        NummerOverzicht a = new NummerOverzicht();
    }
}
