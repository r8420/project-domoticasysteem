import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class ProfielenManagement extends JDialog implements MouseListener {
    private ArrayList<Profiel> profielen;
    private JLabel jlProfielPlaatje, jlProfielToevoegen;
    private boolean anderProfiel = false;
    private Profiel geselecteerdProfiel;

    public boolean anderProfielGeselecteerd() {
        return anderProfiel;
    }

    public Profiel getGeselecteerdProfiel() {
        return geselecteerdProfiel;
    }

    public ProfielenManagement(JFrame frame) {
        super(frame, true);

        setTitle("Kies profiel");
        getContentPane().setBackground(new Color(255, 205, 214));
        setLayout(new GridBagLayout());
        setSize(800, 300);

        //De knoppen plaatjes geven en de labels tekst geven
        JLabel jlKiesProfiel = new JLabel("Kies Profiel");
        jlProfielPlaatje = Functies.maakFotoLabel("src/images/profielToevoegen.png");
        jlProfielToevoegen = new JLabel("Profiel Toevoegen");

        profielen = Database.selectProfielen();

        if (profielen != null) {
            //De labels toevoegen aan het scherm
            GridBagConstraints c = new GridBagConstraints();
            c.weightx = 1;
            c.gridwidth = 5;
            c.gridx = 0;
            c.gridy = 0;
            add(jlKiesProfiel, c);
            c.gridwidth = 1;
            c.insets = new Insets(20, 0, 0, 0);
            c.gridy = 1;
            add(jlProfielPlaatje, c);
            c.gridy = 2;
            c.insets = new Insets(0, 0, 0, 0);
            add(jlProfielToevoegen, c);
            c.gridy = 1;


            /* set alle beschikbare profielen netjes naast/onder elkaar */
            for (Profiel profiel : profielen) {
                c.gridx++;
                if (c.gridx == 5) {
                    c.gridy += 2;
                    c.gridx = 0;
                }
                JLabel jlPlaatje = Functies.maakFotoLabel("src/images/profiel.png");
                JLabel jlNaam = new JLabel(profiel.getNaam());
                MouseListener listener = new MouseListener() {
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Geklikt op profiel: " + profiel.getNaam());
                        anderProfiel = true;
                        geselecteerdProfiel = profiel;
                        Database.updateProfielLaatsteLogin(profiel.getId());
                        dispose();
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
                c.insets = new Insets(20, 0, 0, 0);
                add(jlPlaatje, c);
                c.insets = new Insets(0, 0, 0, 0);
                c.gridy++;
                add(jlNaam, c);
                c.gridy--;
            }

            // Het "Profiel toevegen"-label klikbaar maken
            jlProfielPlaatje.addMouseListener(this);
            jlProfielToevoegen.addMouseListener(this);
            setLocationRelativeTo(frame);
            setVisible(true);
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        } else {
            JOptionPane.showMessageDialog(this, "Er is waarschijnlijk geen verbinding met de database", "Foutmelding", JOptionPane.ERROR_MESSAGE);
        }


    }

    public void mouseClicked(MouseEvent e) {

        /* profiel toevoegen */
        if (e.getSource() == jlProfielPlaatje || e.getSource() == jlProfielToevoegen) {

            if (profielen.size() >= 9) {
                JOptionPane.showMessageDialog(this, "Het maximaal aantal profielen is bereikt!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Nieuwe Profiel-toevoegen-dialog openen
            ProfielToevoegen nieuwProfielDialog = new ProfielToevoegen(this);

            if (!nieuwProfielDialog.getOk()) { // kruisje of annuleren, niks doen
                return;
            }

            if (nieuwProfielDialog.getJtfNewProfile().equals("")) {
                JOptionPane.showMessageDialog(this, "Profiel toevoegen mislukt.\nEen profielnaam mag niet leeg zijn.", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Wanneer er op "Profiel aanmaken" is gedrukt en er een gebruikersnaam is ingevuld.
            if (nieuwProfielDialog.getOk()) {

                if (Database.insertProfiel(nieuwProfielDialog.getJtfNewProfile())){ // Het nieuwe profiel in de database zetten.
                    System.out.println("Profiel toegevoegd!");
                    geselecteerdProfiel = Database.selectRecentsteProfiel(); // Het nieuwe profiel ophalen, inclusief standaardinstellingen en ProfileId.
                    anderProfiel = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Een accountnaam mag niet langer zijn dan 10 symbolen of speciale tekens bevatten", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                }
            }
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
}


