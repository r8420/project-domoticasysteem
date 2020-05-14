import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NummerToevoegen extends JDialog implements ActionListener {

    private ArrayList<Afspeellijst> afspeellijsten;
    private JLabel jlKiesAfspeellijst;
    private Nummer nummer;


    public NummerToevoegen(int profileId, Nummer nummer, JDialog frame) {
        super(frame, true);
        this.nummer = nummer;
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
        jlKiesAfspeellijst = new JLabel("Kies een afspeellijst:");

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        jpAfspeellijsten.setBorder(border);
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 10, 0);
        jpAfspeellijsten.add(jlKiesAfspeellijst, c);
        jlKiesAfspeellijst.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 1;


        for (Afspeellijst afspeellijst : afspeellijsten) {
            JPanel jpAfspeellijstBalk = new JPanel();
            jpAfspeellijstBalk.setBackground(new Color(255, 145, 164));
            jpAfspeellijstBalk.setLayout(new GridBagLayout());
            jpAfspeellijstBalk.setBorder(border);
            JLabel jlNaamAfspeellijst = new JLabel("  " + afspeellijst.getNaam());
            c.gridy++;
            c.ipady = 20;
            c.ipadx = 20;
            c.insets = new Insets(0, 20, 0, 20);
            c.fill = GridBagConstraints.HORIZONTAL;
            jpAfspeellijstBalk.add(jlNaamAfspeellijst, c);
            c.gridx = 1;
            c.weightx = 0;
            c.gridx = 0;
            c.weightx = 1;
            jpAfspeellijsten.add(jpAfspeellijstBalk, c);


            MouseListener listener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getSource() == jlNaamAfspeellijst) {
                        boolean toegevoegd = Database.insertNummerInAfspeellijst(afspeellijst.getAfspeellijstId(), nummer.getNummerId());
                        if (toegevoegd) {
                            nummerToegevoegdDialog();
                        } else {
                            nummerNietToegevoegdDialog();
                        }
                        setVisible(false);
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
            jlNaamAfspeellijst.addMouseListener(listener);
        }

        setLocationRelativeTo(frame);
        setVisible(true);

    }

    // Functie voor een JOptionPane aangezien this binnen de Mouselistener slaat op de MouseListener i.p.v het scherm.
    private void nummerToegevoegdDialog() {
        JOptionPane.showMessageDialog(this, "Nummer toegevoegd aan afspeellijst.", "Bevestiging", JOptionPane.INFORMATION_MESSAGE);
    }

    private void nummerNietToegevoegdDialog() {
        JOptionPane.showMessageDialog(this, "Nummer staat al in afspleellijst.", "Mislukt", JOptionPane.ERROR_MESSAGE);
        // dit hebben we opzettelijk verkeerd gespeld, geen punten aftrek hiervoor aub
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}




