package KBS;

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
    private ArrayList<Profiel> profielen = new ArrayList<>();
    private JLabel jlKiesProfiel, jlProfielPlaatje, jlProfielToevoegen;

    public Profielen(JFrame frame){
        super(frame, true);
        setTitle("Kies profiel");
        setLayout(new GridBagLayout());
        setSize(800,300);

        //De knoppen plaatjes geven en de labels tekst geven
        jlKiesProfiel = new JLabel("Kies Profiel");
        jlProfielPlaatje =  maakFotoLabel("src/profielToevoegen.png");
        jlProfielToevoegen = new JLabel("Profiel Toevoegen");

        profielen.add(new Profiel("MafklapperKees"));
        profielen.add(new Profiel("Henk Klinkelhamer"));
        profielen.add(new Profiel("Geert-Jan Bazelman"));
        profielen.add(new Profiel("MafklapperKees"));
        profielen.add(new Profiel("Henk Klinkelhamer"));
        profielen.add(new Profiel("Geert-Jan Bazelman"));
        profielen.add(new Profiel("MafklapperKees"));
        profielen.add(new Profiel("Henk Klinkelhamer"));
        profielen.add(new Profiel("Geert-Jan Bazelman"));

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
            JLabel jlPlaatje = maakFotoLabel("src/profiel.png");
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

    private JLabel maakFotoLabel(String locatie) {
        try {
            BufferedImage myPicture = ImageIO.read(new File(locatie));
            return new JLabel(new ImageIcon(myPicture));
        } catch (IOException e) {
            return new JLabel("X");
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == jlProfielPlaatje || e.getSource() == jlProfielToevoegen) {
            if (profielen.size() >= 9){
                JOptionPane.showMessageDialog(this,"Het maximaal aantal profielen is bereikt!", "Foutmelding",JOptionPane.ERROR_MESSAGE);
                return;
            }
            // toevoegen profiel wordt aangeklikt
            System.out.println("toevoegen profiel");
            ProfielToevoegen nieuwProfiel = new ProfielToevoegen(new JFrame());
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


