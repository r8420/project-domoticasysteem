import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AfspeellijstOverzicht extends JDialog {

    private ArrayList<Afspeellijst> afspeellijsten;
    private JLabel jlKiesAfspeellijst;
    private MainScherm hoofdscherm;

    private static Color geselecteerdKleur = Color.BLACK;
    private static Border standaardBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
    private static Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 3);


    public AfspeellijstOverzicht(int profileId, MainScherm frame) {
        super(frame, true);
        this.hoofdscherm = frame;
        setTitle("Afspeellijst overzicht");
        getContentPane().setBackground(new Color(255, 145, 164));
        setSize(400, 600);
        setLayout(new GridBagLayout());

        afspeellijsten = Database.selectAfspeellijsten(profileId);
        GridBagConstraints c = new GridBagConstraints();
        JPanel jpAfspeellijsten = new JPanel();
        jpAfspeellijsten.setBackground(new Color(255, 205, 214));
        jpAfspeellijsten.setLayout(new GridBagLayout());
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        add(jpAfspeellijsten, c);
        jlKiesAfspeellijst = new JLabel("Afspeellijsten:");

        jpAfspeellijsten.setBorder(standaardBorder);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        jpAfspeellijsten.add(jlKiesAfspeellijst, c);
        jlKiesAfspeellijst.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 1;


        c.ipady = 20;
        c.ipadx = 20;
        c.fill = GridBagConstraints.HORIZONTAL;

        for (Afspeellijst afspeellijst : afspeellijsten) {

            c.gridy++;

            JPanel jpAfspeellijstBalk = new JPanel();
            jpAfspeellijstBalk.setBackground(new Color(255, 145, 164));
            jpAfspeellijstBalk.setLayout(new GridBagLayout());
            jpAfspeellijstBalk.setBorder(standaardBorder);
            JLabel jlMin = Functies.maakFotoLabel("src/images/min.png");
            JLabel jlPlay = Functies.maakFotoLabel("src/images/play_small.png");
            jlMin.setPreferredSize(new Dimension(20, 20));

            JLabel jlNaamAfspeellijst = new JLabel("  " + afspeellijst.getNaam());

            Afspeellijst currentAfspeellijst = hoofdscherm.getAfspeellijst();
            if (currentAfspeellijst != null && afspeellijst.getAfspeellijstId() == currentAfspeellijst.getAfspeellijstId()) {
                jlNaamAfspeellijst.setForeground(geselecteerdKleur);
                jpAfspeellijstBalk.setBorder(selectedBorder);
            }


            c.insets = new Insets(0, 20, 0, 20);
            c.weightx = 1;
            c.gridx = 0;
            jpAfspeellijstBalk.add(jlNaamAfspeellijst, c);


            c.weightx = 1;
            jpAfspeellijsten.add(jpAfspeellijstBalk, c);

            c.insets = new Insets(0, 0, 0, 20);
            c.gridx = 1;
            c.weightx = 0;
            if (afspeellijst.getAantalNummers() > 0) jpAfspeellijstBalk.add(jlPlay, c);

            c.gridx = 2;
            jpAfspeellijstBalk.add(jlMin, c);

            c.gridx = 1;
            MouseListener listener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getSource() == jlMin) {
                        System.out.println("Verwijder " + afspeellijst.getNaam());
                        if (verwijderAfspeellijst() == JOptionPane.YES_OPTION) {
                            Database.deleteAfspeellijst(afspeellijst.getAfspeellijstId());
                            setVisible(false);
                        }
                    } else if (e.getSource() == jlNaamAfspeellijst) {
                        System.out.println("Open " + afspeellijst.getNaam());
                        openAfspeellijstDialog(afspeellijst);

                    } else if (e.getSource() == jlPlay) {

                        for (Component c : jpAfspeellijsten.getComponents()) {  // un-highlight de andere geselecteerde afspeellijst
                            if (c instanceof JPanel && ((JPanel) c).getBorder() == selectedBorder) {
                                ((JPanel) c).getComponent(0).setForeground(null);
                                ((JPanel) c).setBorder(standaardBorder);
                            }
                        }
                        jlNaamAfspeellijst.setForeground(geselecteerdKleur);
                        jpAfspeellijstBalk.setBorder(selectedBorder);

                        System.out.println("Speel " + afspeellijst.getNaam());
                        frame.setAfspeellijst(afspeellijst);
                        frame.setNummer(afspeellijst.getCurrentSong());
                        frame.startNummer();
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
            jlPlay.addMouseListener(listener);
            jlMin.addMouseListener(listener);
            jlNaamAfspeellijst.addMouseListener(listener);
        }

        setLocationRelativeTo(frame);
        setVisible(true);

    }

    // Functie voor een JOptionPane aangezien this binnen de Mouselistener slaat op de MouseListener i.p.v het scherm.
    private int verwijderAfspeellijst() {
        return JOptionPane.showConfirmDialog(this, "Weet u zeker dat u de afspeellijst wilt verwijderen?", "Bevestiging", JOptionPane.YES_NO_OPTION);
    }

    // Functie om een bepaalde afspeellijst te openen, aangezien this in de MouseListener slaat op de MouseListener i.p.v het scherm. Meegeven afspeellijst voor het scherm om te openen.
    private void openAfspeellijstDialog(Afspeellijst afspeellijst) {
        NummersInAfspeellijst Dialoog = new NummersInAfspeellijst(afspeellijst, this, hoofdscherm);
    }
}




