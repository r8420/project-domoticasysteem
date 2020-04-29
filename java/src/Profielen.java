import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Profielen extends JDialog implements MouseListener, ActionListener {
    private ArrayList<Profiel> profielen;
    private JLabel jlKiesProfiel, jlProfielPlaatje, jlProfielToevoegen;

    public Profielen(JFrame frame){
        super(frame, true);

        setTitle("Kies profiel");
        setLayout(new GridBagLayout());
        setSize(800,300);

        //De knoppen plaatjes geven en de labels tekst geven
        jlKiesProfiel = new JLabel("Kies Profiel");
        jlProfielPlaatje =  Functies.maakFotoLabel("src/images/profielToevoegen.png");
        jlProfielToevoegen = new JLabel("Profiel Toevoegen");

        MainInput mainInput = new MainInput();

        profielen = MainInput.selectDBprofiles();

        //De labels toevoegen aan het scherm
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.gridwidth = 5;
        c.gridx = 0;
        c.gridy = 0;
        add(jlKiesProfiel, c);
        c.gridwidth = 1;
        c.insets = new Insets(20,0,0,0);
        c.gridy = 1;
        add(jlProfielPlaatje,c);
        c.gridy = 2;
        c.insets = new Insets(0,0,0,0);
        add(jlProfielToevoegen,c);
        c.gridy = 1;

        for (Profiel profiel: profielen){
            c.gridx++;
            if (c.gridx == 5){
                c.gridy += 2;
                c.gridx = 0;
            }
            JLabel jlPlaatje = Functies.maakFotoLabel("src/images/profiel.png");
            JLabel jlNaam = new JLabel(profiel.getNaam());
            MouseListener listener = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    System.out.println("Geklikt op profiel: " + profiel.getNaam());
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
            jlPlaatje.addMouseListener(listener);
            jlNaam.addMouseListener(listener);
            c.insets = new Insets(20,0,0,0);
            add(jlPlaatje,c);
            c.insets = new Insets(0,0,0,0);
            c.gridy++;
            add(jlNaam,c);
            c.gridy--;
        }

        //De label klikbaar maken
        jlProfielPlaatje.addMouseListener(this);
        jlProfielToevoegen.addMouseListener(this);

        setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == jlProfielPlaatje || e.getSource() == jlProfielToevoegen) {
            if (profielen.size() >= 9) {
                JOptionPane.showMessageDialog(this, "Het maximaal aantal profielen is bereikt!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ProfielToevoegen nieuwProfiel = new ProfielToevoegen(new JFrame());

            if (nieuwProfiel.GetIsOk() && !nieuwProfiel.getJtfNewProfile().equals("")) {
                // toevoegen profiel wordt aangeklikt
                System.out.println("profiel met succes toegevoegd");
               MainInput.insertDBprofile(nieuwProfiel.getJtfNewProfile());

            } else {
                System.out.println("profiel toevoegen mislukt");
            }
            dispose();
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}


    public void actionPerformed(ActionEvent e) {

    }
}


