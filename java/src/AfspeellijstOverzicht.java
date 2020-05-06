import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class AfspeellijstOverzicht extends JDialog implements ActionListener, MouseListener {

        private ArrayList<Afspeellijst> afspeellijsten;
        private JLabel jlKiesAfspeellijst;

    public AfspeellijstOverzicht() {
            setTitle("Afspeellijst overzicht");
            setSize(400, 600);
            setLayout(new GridBagLayout());

            afspeellijsten = Database.selectDBafspeellijsten();

            JPanel jpAfspeellijsten = new JPanel();
            jpAfspeellijsten.setLayout(new GridBagLayout());
            jlKiesAfspeellijst = new JLabel("Afspeellijsten:");
            GridBagConstraints c = new GridBagConstraints();
            Border border = BorderFactory.createLineBorder(Color.black, 1);
            c.weightx = 1;
            c.gridwidth = 5;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 10, 0);
            add(jlKiesAfspeellijst, c);

            for (Afspeellijst afspeellijst : afspeellijsten) {
              JLabel jlPlus = Functies.maakFotoLabel("src/images/plus.png");
              jlPlus.setPreferredSize(new Dimension(20, 20));
              JLabel jlNaamAfspeellijst = new JLabel("  " + afspeellijst.getNaam());
                c.gridy++;
                c.ipady = 20;
                c.ipadx = 20;
                c.insets = new Insets(0, 20, 0, 20);
                c.fill = GridBagConstraints.HORIZONTAL;
                add(jlNaamAfspeellijst, c);
                jlPlus.setHorizontalAlignment(SwingConstants.RIGHT);
                add(jlPlus, c);
                jlNaamAfspeellijst.setBorder(border);

                    MouseListener listener = new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getSource() == jlPlus){
                            System.out.println("Verwijder afspeellijst: " + afspeellijst.getNaam());
                            Database.deleteDBafspeellijst(afspeellijst.getAfspeellijstId());
                            dispose();
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
            }



//        Border testBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
            add(jpAfspeellijsten);
//        jpNummers.setBorder(border);

            setVisible(true);

        }



    public static void main(String[] args) {
        AfspeellijstOverzicht overzicht = new AfspeellijstOverzicht();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }




    public void mouseClicked(MouseEvent e) {

    }
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
}
