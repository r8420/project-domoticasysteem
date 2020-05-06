import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NummerOverzicht  extends JDialog implements ActionListener {


    public NummerOverzicht(){
        setTitle("Nummer overzicht");
        setSize(400, 600);
        setLayout(new FlowLayout());

        setVisible(true);


//    Database.selectDBnummers();
    }




    public static void main(String[] args) {
        NummerOverzicht a = new NummerOverzicht();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
