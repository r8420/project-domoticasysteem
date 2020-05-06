import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class NummerOverzicht  extends JDialog implements ActionListener {
    private ArrayList<Nummer> nummers = Database.selectDBnummers();
    private JLabel jlKiesNummer, jlNaamNummer, jlPlus;

    public NummerOverzicht(){
        setTitle("Nummer overzicht");
        setSize(400, 600);
        setLayout(new GridBagLayout());

        JPanel jpNummers = new JPanel();
        jpNummers.setLayout(new GridBagLayout());
        jlKiesNummer = new JLabel("Nummers:");
        GridBagConstraints c = new GridBagConstraints();
        Border border = BorderFactory.createLineBorder(Color.black, 1);
        c.weightx = 1;
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,10,0);
        add(jlKiesNummer,c);

        for (Nummer nummer: nummers) {
            jlPlus = Functies.maakFotoLabel("src/images/plus.png");
            jlPlus.setPreferredSize(new Dimension(20,20));
            jlNaamNummer = new JLabel("  " + nummer.getNaam() + " - " + nummer.getArtiest());
            c.gridy++;
            c.ipady = 20;
            c.ipadx = 20;
            c.insets = new Insets(0,20,0,20);
            c.fill = GridBagConstraints.HORIZONTAL;
            add(jlNaamNummer, c);
            jlPlus.setHorizontalAlignment(SwingConstants.RIGHT);
            add(jlPlus, c);
            jlNaamNummer.setBorder(border);
        }

        Border testBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        add(jpNummers);
        jpNummers.setBorder(testBorder);

        setVisible(true);

    }




    public static void main(String[] args) {
        NummerOverzicht a = new NummerOverzicht();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
