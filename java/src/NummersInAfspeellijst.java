import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NummersInAfspeellijst extends JDialog implements ActionListener, MouseListener {

    private Afspeellijst afspeellijst;
    private ArrayList<Nummer> nummers;
    private JLabel jlKiesNummer;
    private MainScherm hoofdscherm;

    private static Color geselecteerdKleur = Color.BLACK;
    private static Border standaardBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    private static Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 3);

    // Een Afspeellijst meegeven om zo de informatie van een afspeellijst op te kunnen halen.
    public NummersInAfspeellijst(Afspeellijst afspeellijst, JDialog frame, MainScherm root) {
        super(frame, true);
        this.hoofdscherm = root;
        this.afspeellijst = afspeellijst;

        nummers = Database.selectNummersUitAfspeellijst(afspeellijst.getAfspeellijstId());

        // afspeellijst.getNaam() halen uit de meegegeven afspeellijst.
        setTitle("Nummers in afspeellijst " + afspeellijst.getNaam());
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
        jlKiesNummer = new JLabel("Nummers in afspeellijst: " + afspeellijst.getNaam());

        jpNummers.setBorder(standaardBorder);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        jpNummers.add(jlKiesNummer, c);
        jlKiesNummer.setHorizontalAlignment(SwingConstants.CENTER);

        // Foreach om alle nummers te printen binnen afspeellijst X
        for (Nummer nummer : nummers) {
            JPanel jpNummerBalk = new JPanel();
            jpNummerBalk.setBackground(new Color(255, 145, 164));
            jpNummerBalk.setLayout(new GridBagLayout());
            jpNummerBalk.setBorder(standaardBorder);
            JLabel jlMin = Functies.maakFotoLabel("src/images/min.png");
            jlMin.setPreferredSize(new Dimension(20, 20));
            JLabel jlNaamNummer = new JLabel("  " + nummer.getNaam() + " - " + nummer.getArtiest());

            Nummer currentSong = hoofdscherm.getJpMuziekspeler().getNummer();
            Afspeellijst currentAfspeellijst = hoofdscherm.getJpMuziekspeler().getAfspeellijst();
            if (currentSong != null && currentAfspeellijst != null &&
                    nummer.getNummerId() == currentSong.getNummerId() &&
                    afspeellijst.getAfspeellijstId() == currentAfspeellijst.getAfspeellijstId())
            {
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
            jpNummerBalk.add(jlMin, c);
            c.gridx = 0;
            c.weightx = 1;
            jpNummers.add(jpNummerBalk, c);

            MouseListener listener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getSource() == jlMin) {
                        if (verwijderNummer() == JOptionPane.YES_OPTION) {
                            Database.deleteNummerUitAfspeellijst(afspeellijst.getAfspeellijstId(), nummer.getNummerId());
                            setVisible(false);
                        }
                    } else if (e.getSource() == jlNaamNummer) {

                        for (Component c : jpNummers.getComponents()) {
                            if (c instanceof JPanel && ((JPanel) c).getBorder() == selectedBorder) {
                                ((JPanel) c).getComponent(0).setForeground(null);
                                ((JPanel) c).setBorder(standaardBorder);
                            }
                        }
                        jlNaamNummer.setForeground(geselecteerdKleur);
                        jpNummerBalk.setBorder(selectedBorder);


                        hoofdscherm.getJpMuziekspeler().setNummer(nummer);
                        hoofdscherm.getJpMuziekspeler().setAfspeellijst(afspeellijst);
                        hoofdscherm.getJpMuziekspeler().startNummer();

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
            jlMin.addMouseListener(listener);
            jlNaamNummer.addMouseListener(listener);
        }

        setLocationRelativeTo(frame);
        setVisible(true);

    }

    // Functie voor een JOptionPane, aangezien this binnen de Mouselistener anders slaat op de Mouselistener i.p.v het scherm
    private int verwijderNummer() {
        return JOptionPane.showConfirmDialog(this, "Weet u zeker dat u dit nummer wilt verwijderen?", "Bevestiging", JOptionPane.YES_NO_OPTION);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
