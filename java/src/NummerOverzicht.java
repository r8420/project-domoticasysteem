import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NummerOverzicht  extends JDialog implements ActionListener {
    private ArrayList<Nummer> nummers = Database.selectDBnummers();
    private JLabel jlKiesNummer;

    public NummerOverzicht(){
        setTitle("Nummer overzicht");
        setSize(400, 600);
        setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        JPanel jpNummers = new JPanel();
        jpNummers.setLayout(new GridBagLayout());
        c.insets = new Insets(10,10,10,10);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(jpNummers, c);
        jlKiesNummer = new JLabel("Nummers:");

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        jpNummers.setBorder(border);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0,0,10,0);
        jpNummers.add(jlKiesNummer,c);
        jlKiesNummer.setHorizontalAlignment(SwingConstants.CENTER);


        for (Nummer nummer: nummers) {
            JPanel jpNummerBalk = new JPanel();
            jpNummerBalk.setLayout(new GridBagLayout());
            jpNummerBalk.setBorder(border);
            JLabel jlPlus = Functies.maakFotoLabel("src/images/plus.png");
            jlPlus.setPreferredSize(new Dimension(20,20));
            JLabel jlNaamNummer = new JLabel("  " + nummer.getNaam() + " - " + nummer.getArtiest() );
            c.gridy++;
            c.ipady = 20;
            c.ipadx = 20;
            c.insets = new Insets(0,20,0,20);
            c.fill = GridBagConstraints.HORIZONTAL;
            jpNummerBalk.add(jlNaamNummer, c);
            c.gridx = 1;
            c.weightx = 0;
            jpNummerBalk.add(jlPlus, c);
            c.gridx = 0;
            c.weightx = 1;
            jpNummers.add(jpNummerBalk, c);
            MouseListener listener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getSource() == jlPlus){
                        System.out.println("Doe wat met nummer: " + nummer.getNaam());
                    }else if (e.getSource() == jlNaamNummer){
                        System.out.println("Herres");
                    }
                }
                public void mousePressed(MouseEvent e) {

                }
                public void mouseReleased(MouseEvent e) {

                }
                public void mouseEntered(MouseEvent e) {

                }
                public void mouseExited(MouseEvent e) {

                }
            };
            jlPlus.addMouseListener(listener);
            jlNaamNummer.addMouseListener(listener);
        }




        setVisible(true);

    }




    public static void main(String[] args) {
        NummerOverzicht a = new NummerOverzicht();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
