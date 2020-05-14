import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NummerOverzicht extends JDialog implements ActionListener {
    private ArrayList<Nummer> nummers;
    private JLabel jlKiesNummer;
    private int ProfileId;
    private MainScherm hoofdscherm;
    private static Color geselecteerdKleur = Color.BLACK;
    private static Border standaardBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    private static Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 3);


    public NummerOverzicht(int ProfileId, MainScherm frame) {
        super(frame, true);
        this.ProfileId = ProfileId;
        this.hoofdscherm = frame;
        this.nummers = Database.selectNummers();
        setTitle("Nummer overzicht");
        getContentPane().setBackground(new Color(255, 145, 164));
        setSize(400, 600);
        setLayout(new GridBagLayout());


        GridBagConstraints c = new GridBagConstraints();
        JPanel jpNummers = new JPanel();
        jpNummers.setBackground(new Color(255, 205, 214));
        jpNummers.setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(jpNummers, c);
        jlKiesNummer = new JLabel("Nummers:");

        jpNummers.setBorder(standaardBorder);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        jpNummers.add(jlKiesNummer, c);
        jlKiesNummer.setHorizontalAlignment(SwingConstants.CENTER);

        // Foreach om alle nummers te kunnen printen, plusje om een nummer toe te voegen aan een afspeellijst
        for (Nummer nummer : nummers) {
            JPanel jpNummerBalk = new JPanel();
            jpNummerBalk.setBackground(new Color(255, 145, 164));
            jpNummerBalk.setLayout(new GridBagLayout());
            jpNummerBalk.setBorder(standaardBorder);
            JLabel jlPlus = Functies.maakFotoLabel("src/images/plus.png");
            jlPlus.setPreferredSize(new Dimension(20, 20));

            JLabel jlNaamNummer = new JLabel("  " + nummer.getNaam() + " - " + nummer.getArtiest());

            Nummer currentSong = frame.getNummer();
            if (currentSong != null && nummer.getNummerId() == currentSong.getNummerId()) {
                jlNaamNummer.setForeground(geselecteerdKleur);
                jpNummerBalk.setBorder(selectedBorder);
            }

            c.gridy++;
            c.ipady = 20;
            c.ipadx = 20;
            c.insets = new Insets(0, 20, 0, 20);
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
                    if (e.getSource() == jlPlus) {

                        nummerToevoegenDialog(nummer);

                    } else if (e.getSource() == jlNaamNummer) {

                        for (Component c : jpNummers.getComponents()) {  // un-highlight het andere geselecteerde nummer
                            if (c instanceof JPanel && ((JPanel) c).getBorder() == selectedBorder) {
                                ((JPanel) c).getComponent(0).setForeground(null);
                                ((JPanel) c).setBorder(standaardBorder);
                            }
                        }
                        jlNaamNummer.setForeground(geselecteerdKleur);
                        jpNummerBalk.setBorder(selectedBorder);

                        System.out.println("Speel " + nummer.getNaam() + " af");
                        hoofdscherm.setNummer(nummer);
                        hoofdscherm.setAfspeellijst(null);
                        hoofdscherm.startNummer();
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

        setLocationRelativeTo(frame);
        setVisible(true);

    }

    private void nummerToevoegenDialog(Nummer nummer) {
        NummerToevoegen nummerToevoegen = new NummerToevoegen(ProfileId, nummer, this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
