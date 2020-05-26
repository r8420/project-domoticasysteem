import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

public class MuziekSpeler extends JPanel implements ActionListener, MouseListener, ChangeListener {
    private JButton jbAfspeellijstOverzicht;
    private JButton jbAfspeellijstToevoegen;
    private JButton jbNummerOverzicht;
    private JLabel jlSkip;
    private JLabel jlSkipBack;
    private JLabel jlPlay;
    private JLabel jlPuntjes;
    private JLabel jlNaamMuziek;
    private JLabel jlHuidigeTijd;
    private JSlider jsTijdMuziek;
    private JPanel jpMuziekKnoppen;
    private Timer muziekSliderTimer;
    private boolean gepauzeerd = true;
    private boolean valueWASadjusting;

    private Nummer nummer = null;
    private Afspeellijst afspeellijst = null;
    private Afspeellijst alleNummers;
    private MainInput mainInput;
    private MainScherm mainscherm;


    public MuziekSpeler(MainScherm mainscherm, MainInput mainInput) {

        this.mainscherm = mainscherm;
        this.mainInput = mainInput;

        setMaximumSize(new Dimension(700, 600));
        setLayout(new FlowLayout());
        jlNaamMuziek = new JLabel("Selecteer een nummer.", SwingConstants.CENTER);
        jlNaamMuziek.setFont(new Font("Arial", Font.PLAIN, 30));
        jlNaamMuziek.setPreferredSize(new Dimension(600, 100));
        jlNaamMuziek.setMinimumSize(new Dimension(600, 100));

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        jlNaamMuziek.setBorder(border);
        add(jlNaamMuziek);


        // de slider krijgt de max tijd en de huidige
        int maxTijd = 10;
        int huidigeTijd = 0;
        jlHuidigeTijd = new JLabel("0:00");
        jsTijdMuziek = new JSlider(0, maxTijd, 0);
        jsTijdMuziek.setEnabled(false);
        jsTijdMuziek.setMajorTickSpacing((int) (maxTijd * 0.1));
        Hashtable<Integer, JLabel> tijdLableTable = new Hashtable<>();
        tijdLableTable.put(0, jlHuidigeTijd);
        tijdLableTable.put(maxTijd, new JLabel("00:00"));
        jsTijdMuziek.setValue(huidigeTijd);
        jsTijdMuziek.setLabelTable(tijdLableTable);
        jsTijdMuziek.setPaintLabels(true);
        jsTijdMuziek.setPreferredSize(new Dimension(600, 40));
        jsTijdMuziek.setUI(new LightSliderUI(jsTijdMuziek));
        jsTijdMuziek.addChangeListener(this);
        add(jsTijdMuziek);

        // de panel voor de knoppen van de mp3
        jpMuziekKnoppen = new JPanel();
        jpMuziekKnoppen.setLayout(null);
        jpMuziekKnoppen.setPreferredSize(new Dimension(600, 60));
        add(jpMuziekKnoppen);
        jlSkipBack = Functies.maakFotoLabel("src/images/skip_back.png");
        jlSkipBack.setBounds(190, 0, 50, 50);
        jlPlay = Functies.maakFotoLabel("src/images/play.png");
        jlPlay.setBounds(270, 0, 60, 60);
        jlSkip = Functies.maakFotoLabel("src/images/skip_forward.png");
        jlSkip.setBounds(360, 0, 50, 50);
        jlPuntjes = Functies.maakFotoLabel("src/images/3_puntjes.png");
        jlPuntjes.setBounds(440, 0, 50, 50);

        // de knoppen voor de dropdown van 3_puntjes.png
        jbAfspeellijstOverzicht = new JButton("Afspeellijst overzicht");
        jbAfspeellijstOverzicht.setBounds(400, 61, 180, 20);
        jbAfspeellijstOverzicht.addActionListener(this);


        jbAfspeellijstToevoegen = new JButton("Afspeellijst toevoegen");
        jbAfspeellijstToevoegen.setBounds(400, 82, 180, 20);
        jbAfspeellijstToevoegen.addActionListener(this);


        jbNummerOverzicht = new JButton("Nummer overzicht");
        jbNummerOverzicht.setBounds(400, 103, 180, 20);
        jbNummerOverzicht.addActionListener(this);


        jlPuntjes.addMouseListener(this);
        jlPlay.addMouseListener(this);
        jlSkip.addMouseListener(this);
        jlSkipBack.addMouseListener(this);
        jpMuziekKnoppen.add(jlSkip);
        jpMuziekKnoppen.add(jlSkipBack);
        jpMuziekKnoppen.add(Box.createHorizontalStrut(30));
        jpMuziekKnoppen.add(jlPlay);
        jpMuziekKnoppen.add(Box.createHorizontalStrut(30));

        jpMuziekKnoppen.add(Box.createHorizontalStrut(30));
        jpMuziekKnoppen.add(jlPuntjes);
        jpMuziekKnoppen.add(jbAfspeellijstOverzicht);
        jpMuziekKnoppen.add(jbAfspeellijstToevoegen);
        jpMuziekKnoppen.add(jbNummerOverzicht);


        Border testBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        setBorder(testBorder);


        Color zalmroze = new Color(255, 145, 164);
        Color lichtZalmroze = new Color(255, 205, 214);


        jbNummerOverzicht.setBackground(zalmroze);
        jbAfspeellijstOverzicht.setBackground(zalmroze);
        jbAfspeellijstToevoegen.setBackground(zalmroze);

        setBackground(lichtZalmroze);

        jsTijdMuziek.setBackground(lichtZalmroze);
        jpMuziekKnoppen.setBackground(lichtZalmroze);

        alleNummers = new Afspeellijst(Database.selectNummers());


        muziekSliderTimer = new Timer(1000, e -> {
            if (jsTijdMuziek.getValueIsAdjusting()) return;
            if (jsTijdMuziek.getValue() == nummer.getTijdsduur()) {

                if (afspeellijst != null) {  // als er een afspeellijst speelt, ga naar volgend nummer
                    afspeellijst.nextSong();
                    setNummer(afspeellijst.getCurrentSong());
                    startNummer();
                    return;
                }

                pause();  // geen afspeellijst, stop aan het einde van nummer.
                return;
            }
            int nieuweTijd = jsTijdMuziek.getValue() + 1;
            jsTijdMuziek.setValue(nieuweTijd);
            jlHuidigeTijd.setText(Functies.intToTimestamp(nieuweTijd));
            jsTijdMuziek.repaint();
        });

    }

    public Afspeellijst getAfspeellijst() {
        return afspeellijst;
    }

    public void setAfspeellijst(Afspeellijst afspeellijst) {
        this.afspeellijst = afspeellijst;
    }

    public void stopSlider() {
        muziekSliderTimer.stop();
    }


    public void setNummer(Nummer nummer) {
        this.nummer = nummer;

        if (nummer == null) return;
        jsTijdMuziek.setMaximum(nummer.getTijdsduur());
        Hashtable<Integer, JLabel> newLabelTable = new Hashtable<>();
        newLabelTable.put(0, jlHuidigeTijd);
        newLabelTable.put(nummer.getTijdsduur(), new JLabel(Functies.intToTimestamp(nummer.getTijdsduur())));
        jsTijdMuziek.setLabelTable(newLabelTable);

        setMuziekText(nummer.getNaam() + " - " + nummer.getArtiest());

        jsTijdMuziek.setValue(0);
        jsTijdMuziek.setEnabled(true);

        jpMuziekKnoppen.repaint(); // dit update de aangepaste waardes op het scherm
    }

    public void setMuziekText(String text) {
        setMuziekText(text, false);
    }

    public void setMuziekText(String text, boolean isWarning) {
        jlNaamMuziek.setText(text);
        if (isWarning) jlNaamMuziek.setForeground(Color.RED);
        else jlNaamMuziek.setForeground(Color.BLACK);
    }

    public void startNummer() {
        if (nummer == null) return;

        mainInput.sendPiMessage("MUSIC PLAY " + nummer.getBestandsNaam());
        String response = mainInput.waitForPiResponse();
        if (response != null && !response.equals("fail")) {
            jlPlay.setIcon(new ImageIcon("src/images/pause.png"));
            gepauzeerd = false;
            muziekSliderTimer.start();
        }
    }

    public void unpause() {

        // nummer opnieuw starten als play wordt aangedrukt wanneer een nummer is afgelopen.
        if (jsTijdMuziek.getValue() == nummer.getTijdsduur()) {
            jsTijdMuziek.setValue(0);
            startNummer();
            return;
        }

        mainInput.sendPiMessage("MUSIC UNPAUSE");
        String response = mainInput.waitForPiResponse();

        if (response != null && !response.equals("fail")) {

            jlPlay.setIcon(new ImageIcon("src/images/pause.png"));
            gepauzeerd = false;
            muziekSliderTimer.start();
        }
    }

    public void pause() {

        mainInput.sendPiMessage("MUSIC PAUSE");
        String response = mainInput.waitForPiResponse();
        if (response != null && response.equals("success")) {
            jlPlay.setIcon(new ImageIcon("src/images/play.png"));
            gepauzeerd = true;
            muziekSliderTimer.stop();
        }
    }


    public Nummer getNummer() {
        return nummer;
    }

    public void nextSong() {
        if (nummer == null) {
            alleNummers.setCurrentSong(-1);
            alleNummers.nextSong();
            setNummer(alleNummers.getCurrentSong());
            startNummer();
            return;
        }

        if (afspeellijst == null) {
            alleNummers.nextSong();
            setNummer(alleNummers.getCurrentSong());
            startNummer();
            return;
        }
        afspeellijst.nextSong();
        setNummer(afspeellijst.getCurrentSong());
        startNummer();
    }

    public void previousSong() {
        if (afspeellijst == null) {
            alleNummers.previousSong();
            setNummer(alleNummers.getCurrentSong());
            startNummer();
            return;
        }
        afspeellijst.previousSong();
        setNummer(afspeellijst.getCurrentSong());
        startNummer();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbAfspeellijstOverzicht) {

            try {
                AfspeellijstOverzicht overzicht = new AfspeellijstOverzicht(mainscherm.getProfiel().getId(), mainscherm);
            } catch (NullPointerException NPE) {
                mainscherm.geenDatabase_Dialog();
            }

        } else if (e.getSource() == jbNummerOverzicht) {

            try {
                NummerOverzicht overzicht2 = new NummerOverzicht(mainscherm.getProfiel().getId(), mainscherm); // nummer-overzicht dialog
            } catch (NullPointerException NPE) {

                mainscherm.geenDatabase_Dialog();
            }

        } else if (e.getSource() == jbAfspeellijstToevoegen) {

            try {
                if (Database.selectAfspeellijsten(mainscherm.getProfiel().getId()).size() >= 8) {
                    JOptionPane.showMessageDialog(mainscherm, "Het maximaal aantal afspeellijsten is bereikt!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                AfspeellijstToevoegen toevoegen = new AfspeellijstToevoegen(mainscherm);
                if (toevoegen.getOk() && !toevoegen.getJtfNewAfspeellijst().equals("")) {

                    if (!Database.insertAfspeellijst(mainscherm.getProfiel().getId(), toevoegen.getJtfNewAfspeellijst())) {
                        JOptionPane.showMessageDialog(mainscherm, "Een Afspeellijst mag een naam van maximaal 15 symbolen hebben.", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (toevoegen.getOk() && toevoegen.getJtfNewAfspeellijst().equals("")) {
                    JOptionPane.showMessageDialog(mainscherm, "Er is geen naam ingevuld!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NullPointerException NPE) {
                mainscherm.geenDatabase_Dialog();
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() == jlPlay) {

            if (nummer == null) {
                setMuziekText("Kies eerst een nummer", true);
                return;
            }

            if (gepauzeerd) {
                unpause();
            } else {
                pause();
            }

        } else if (mouseEvent.getSource() == jlPuntjes) {

            // open/sluit dropdown-menu'tje
            if (jpMuziekKnoppen.getHeight() < 80) {
                jpMuziekKnoppen.setSize(jpMuziekKnoppen.getWidth(), 125);
                jpMuziekKnoppen.setPreferredSize(new Dimension(jpMuziekKnoppen.getWidth(), 125));
            } else {
                jpMuziekKnoppen.setSize(jpMuziekKnoppen.getWidth(), 60);
                jpMuziekKnoppen.setPreferredSize(new Dimension(jpMuziekKnoppen.getWidth(), 60));
            }
        } else if (mouseEvent.getSource() == jlSkip) {

            nextSong();

        } else if (mouseEvent.getSource() == jlSkipBack) {

            previousSong();

        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {}

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == jsTijdMuziek) {

            jlHuidigeTijd.setText(Functies.intToTimestamp(jsTijdMuziek.getValue()));
            jsTijdMuziek.repaint();

            // zolang gebruiker slider nog vast heeft (adjusting), niks doen.
            if (jsTijdMuziek.getValueIsAdjusting()) {
                valueWASadjusting = true;
                return;
            }
            // wanner de slider wordt aangepast zonder dat een persoon dat doet, negeer het dan.
            if (!valueWASadjusting) return;

            // een persoon heeft een tijd aangegeven:
            valueWASadjusting = false;
            mainInput.sendPiMessage("MUSIC SET_TIME " + jsTijdMuziek.getValue());
            mainInput.waitForPiResponse();
        }
    }
}