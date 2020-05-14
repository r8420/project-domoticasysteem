import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Hashtable;

public class MainScherm extends JFrame implements ChangeListener, MouseListener, ActionListener {


    public static void main(String[] args) throws InterruptedException {
        MainScherm scherm = new MainScherm();
    }

    private boolean arduinoAansluiting;

    /* scherm-componenten */
    private JLabel jlLichtsterkte;
    private JLabel jlTemperatuur;
    private JLabel jlLuchtdruk;
    private JLabel jlLuchtvochtigheid;
    private JLabel jlProfielNaam;
    private JLabel jlAnderProfielAfb;
    private JSpinner jspVerwarmingsTemperatuur;
    private JSlider jslMaxLichtsterkte;
//    private JButton jbLichtAan;
//    private JButton jbLichtUit;

    /* muziekspeler-componenten */
    private JButton jlAfspeellijstOverzicht;
    private JButton jlAfspeellijstToevoegen;
    private JButton jlNummerOverzicht;
    private JLabel jlSkip;
    private JLabel jlSkipBack;
    private JLabel jlPLay;
    private JLabel jlPuntjes;
    private JLabel jlNaamMuziek;
    private JLabel jlHuidigeTijd;
    private JLabel jlKachelStatus;
    private JSlider jsTijdMuziek;
    private JPanel jpMuziekKnoppen;
    private Timer muziekSliderTimer;
    private boolean gepauzeerd = true;
    private boolean valueWASadjusting;

    private Nummer nummer = null;
    private Afspeellijst afspeellijst = null;

    /* connectie/update */
    private Timer metingTimer;
    private MainInput mainInput;

    /* meetwaardes */
    private double temperatuur;
    private double lichtsterkte;
    private int luchtdruk;
    private int luchtvochtigheid;

    private Profiel profiel;

    public MainScherm() throws InterruptedException {

        /*
         * maak verbinding
         * */
        mainInput = new MainInput();
        mainInput.startPiSocket();
        arduinoAansluiting = mainInput.arduinoStart();


        /*
         * default scherm settings
         * */
        setSize(1000, 750);
        setMinimumSize(new Dimension(1000, 800));
        setLocation(500, 0);
        setTitle("Domotica Systeem");
        setResizable(false);
        setIconImage(Functies.maakImage("src/images/icon.png"));


        /*
         * standaard layout settings
         * */
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int standaardInset = 20;
        c.insets = new Insets(10, standaardInset, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;


        /*
         * muziekspeler panel
         * */
        // het vak voor de muziekpanel en de tekstvak van de muzieknaam
        JPanel jpMuziekspeler = new JPanel();
        jpMuziekspeler.setMaximumSize(new Dimension(700, 600));
        jpMuziekspeler.setLayout(new FlowLayout());
        jlNaamMuziek = new JLabel("Selecteer een nummer.", SwingConstants.CENTER);
        jlNaamMuziek.setFont(new Font("Arial", Font.PLAIN, 30));
        jlNaamMuziek.setPreferredSize(new Dimension(600, 100));
        jlNaamMuziek.setMinimumSize(new Dimension(600, 100));

        Border border = BorderFactory.createLineBorder(Color.black, 1);
        jlNaamMuziek.setBorder(border);
        jpMuziekspeler.add(jlNaamMuziek);


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
        jpMuziekspeler.add(jsTijdMuziek);

        // de panel voor de knoppen van de mp3
        jpMuziekKnoppen = new JPanel();
        jpMuziekKnoppen.setLayout(null);
        jpMuziekKnoppen.setPreferredSize(new Dimension(600, 60));
        jpMuziekspeler.add(jpMuziekKnoppen);
        jlSkipBack = Functies.maakFotoLabel("src/images/skip_back.png");
        jlSkipBack.setBounds(190, 0, 50, 50);
        jlPLay = Functies.maakFotoLabel("src/images/play.png");
        jlPLay.setBounds(270, 0, 60, 60);
        jlSkip = Functies.maakFotoLabel("src/images/skip_forward.png");
        jlSkip.setBounds(360, 0, 50, 50);
        jlPuntjes = Functies.maakFotoLabel("src/images/3_puntjes.png");
        jlPuntjes.setBounds(440, 0, 50, 50);

        // de knoppen voor de dropdown van 3_puntjes.png
        jlAfspeellijstOverzicht = new JButton("Afspeellijst overzicht");
        jlAfspeellijstOverzicht.setBounds(400, 61, 180, 20);
        jlAfspeellijstOverzicht.addActionListener(this);


        jlAfspeellijstToevoegen = new JButton("Afspeellijst toevoegen");
        jlAfspeellijstToevoegen.setBounds(400, 82, 180, 20);
        jlAfspeellijstToevoegen.addActionListener(this);


        jlNummerOverzicht = new JButton("Nummer overzicht");
        jlNummerOverzicht.setBounds(400, 103, 180, 20);
        jlNummerOverzicht.addActionListener(this);


        jlPuntjes.addMouseListener(this);
        jlPLay.addMouseListener(this);
        jlSkip.addMouseListener(this);
        jlSkipBack.addMouseListener(this);
        jpMuziekKnoppen.add(jlSkipBack);
        jpMuziekKnoppen.add(Box.createHorizontalStrut(30));
        jpMuziekKnoppen.add(jlPLay);
        jpMuziekKnoppen.add(Box.createHorizontalStrut(30));
        jpMuziekKnoppen.add(jlSkip);
        jpMuziekKnoppen.add(Box.createHorizontalStrut(30));
        jpMuziekKnoppen.add(jlPuntjes);
        jpMuziekKnoppen.add(jlAfspeellijstOverzicht);
        jpMuziekKnoppen.add(jlAfspeellijstToevoegen);
        jpMuziekKnoppen.add(jlNummerOverzicht);



        /*
         * verwarming / temperatuur panel
         * */
        JPanel jpVerwarming = new JPanel();
        jpVerwarming.setLayout(new GridBagLayout());
        jspVerwarmingsTemperatuur = new JSpinner(new SpinnerNumberModel(0, 0, 50, 0.5));
        jspVerwarmingsTemperatuur.setPreferredSize(new Dimension(50, 30));
        jspVerwarmingsTemperatuur.addChangeListener(this);
        c.weightx = 0;
        jpVerwarming.add(new JLabel("Huidige temperatuur: "), c);
        jlTemperatuur = new JLabel("-");
        jpVerwarming.add(jlTemperatuur, c);
        c.gridy = 1;
        jpVerwarming.add(new JLabel("Verwarmen tot en met: "), c);
        jpVerwarming.add(jspVerwarmingsTemperatuur, c);
        c.weightx = 1;
        jlKachelStatus = Functies.maakFotoLabel("src/images/kachelUit.png");
        jpVerwarming.add(jlKachelStatus,c);
        c.gridy = 0;


        /*
         * licht panel
         * */
        JPanel jpLicht = new JPanel();
        jpLicht.setLayout(new GridBagLayout());



        jlLichtsterkte = new JLabel("Huidige lichtsterkte: -");
        c.weightx = 0;
        c.weightx = 1;
        jpLicht.add(jlLichtsterkte, c);
        c.gridy = 1;
        c.gridwidth = 3;
        jpLicht.add(new JLabel("Licht aan tot en met: "), c);
        c.gridy = 2;

        // de slider kan zo een waarde van 0 tot 10 krijgen.
        int maxLichtWaarde = 10;
        jslMaxLichtsterkte = new JSlider(0, maxLichtWaarde, maxLichtWaarde / 2);
        jslMaxLichtsterkte.setMajorTickSpacing((int) (maxLichtWaarde * 0.1));
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(0, new JLabel("Donker"));
        labelTable.put(maxLichtWaarde, new JLabel("Licht"));
        jslMaxLichtsterkte.setLabelTable(labelTable);
        jslMaxLichtsterkte.setPaintTicks(true);
        jslMaxLichtsterkte.setPaintLabels(true);
        jslMaxLichtsterkte.addChangeListener(this);
        jslMaxLichtsterkte.setPreferredSize(new Dimension(400, 40));
        jpLicht.add(jslMaxLichtsterkte, c);

        c.gridy = 0;
        c.gridwidth = 1;


        /*
         * lucht panel
         * */
        JPanel jpLucht = new JPanel();
        jpLucht.setLayout(new GridBagLayout());
        jlLuchtdruk = new JLabel("Luchtdruk: -");
        jlLuchtvochtigheid = new JLabel("Luchtvochtigheid: -");
        c.weightx = 0;
        jpLucht.add(jlLuchtdruk, c);
        c.weightx = 1;
        jpLucht.add(jlLuchtvochtigheid, c);


        /*
         * zijkant panel (profiel knoppen)
         * */
        JPanel jpZijkant = new JPanel();
        jpZijkant.setLayout(new GridBagLayout());

        JLabel jlProfielAfb = Functies.maakFotoLabel("src/images/profiel.png");
        jlAnderProfielAfb = Functies.maakFotoLabel("src/images/anderprofiel.png");
        jlAnderProfielAfb.addMouseListener(this);

        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.weighty = 0;
        jpZijkant.add(jlProfielAfb, c);
        jpZijkant.add(jlAnderProfielAfb, c);
        c.gridy = 1;
        c.weighty = 1;
        jlProfielNaam = new JLabel();
        jpZijkant.add(jlProfielNaam, c);
        jpZijkant.add(new JLabel("Ander profiel"), c);


        // reset GridBagConstraints
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);


        /*
         * Vormgeving
         * */
        Border testBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        jpMuziekspeler.setBorder(testBorder);
        jpVerwarming.setBorder(testBorder);
        jpLicht.setBorder(testBorder);
        jpLucht.setBorder(testBorder);

        getContentPane().setBackground(new Color(255, 145, 164));
        jpZijkant.setBackground(new Color(255, 145, 164));

        jlNummerOverzicht.setBackground(new Color(255, 145, 164));
        jlAfspeellijstOverzicht.setBackground(new Color(255, 145, 164));
        jlAfspeellijstToevoegen.setBackground(new Color(255, 145, 164));
        
        jpMuziekspeler.setBackground(new Color(255, 205, 214));
        jslMaxLichtsterkte.setBackground(new Color(255, 205, 214));
        jsTijdMuziek.setBackground(new Color(255, 205, 214));
        jpMuziekKnoppen.setBackground(new Color(255, 205, 214));
        jpVerwarming.setBackground(new Color(255, 205, 214));
        jpLucht.setBackground(new Color(255, 205, 214));
        jpLicht.setBackground(new Color(255, 205, 214));


        /*
         * voeg alle panels toe aan het hoofdscherm
         * */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.insets = new Insets(10, 10, 10, 10);

        c.gridy = 0;
        c.gridx = 0;
        c.weighty = 1;
        add(jpMuziekspeler, c);

        c.weighty = 0.3;
        c.gridy = 1;
        add(jpVerwarming, c);

        c.gridy = 2;
        add(jpLicht, c);

        c.gridy = 3;
        add(jpLucht, c);

        c.gridy = 0;
        c.gridx = 1;
        c.weightx = 0;
        c.gridheight = 4;
        add(jpZijkant, c);


        /*
         * timer voor opvragen van nieuwe gegevens
         * */
        metingTimer = new Timer(0, e -> {
            updateMeetWaardes();
        });
        metingTimer.setDelay(10000); // millisec, 1.000 = 1 sec
        metingTimer.start();


        /*
         * timer voor updaten van de muziekslider
         * */
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
        // timer wordt pas gestart wanneer het eerste nummer wordt geselecteerd

        /*
         * acties om uit te voeren wanneer het scherm sluit
         * */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                muziekSliderTimer.stop();
                metingTimer.stop();
                e.getWindow().dispose(); // sluit het scherm
            }
        });


        /*
         * stel mainscherm in op laatste gebruiker
         * */
        profiel = Database.selectRecentsteProfiel();
        if (profiel == null) { // geen database verbinding
            geenDatabase_Dialog();
            profiel = new Profiel("", -1, 20, 5);

        } else if (profiel.getId() == -1) { // is er geen laatste gebruiker, maak dan gast account
            Database.insertProfiel("Gast");
            profiel = Database.selectRecentsteProfiel();
        }
        setLocationRelativeTo(null);
        /* Maak het scherm zichtbaar */
        setVisible(true);

        /* error voor database verbinding */
        if (profiel != null) {
            updateSchermSettings();
        } else {
            geenDatabase_Dialog();
        }
    }

    public void updateSchermSettings() {
        setProfielNaam(profiel.getNaam());
        jspVerwarmingsTemperatuur.setValue(profiel.getTempVerwarmen());
        jslMaxLichtsterkte.setValue(profiel.getLichtWaarde());

    }


    public void updateMeetWaardes() {

        System.out.println("Update meetwaardes!" + ((arduinoAansluiting || mainInput.piIsConnected()) ? "" : " (geen verbinding)"));

        boolean arduinoMeetIets = false;
        boolean piMeetIets = false;

        if (arduinoAansluiting) {

            String arduinoMeting = mainInput.arduinoSensor();
            if (arduinoMeting != null && !arduinoMeting.equals("fail")) {

                lichtsterkte = Double.parseDouble(arduinoMeting);
                lichtsterkte = Math.round(lichtsterkte/102.4); // Opgehaalde lichtwaarde van de Arduino relativeren naar 0 tot 10, overeenkomend met de slider.

                System.out.println(lichtsterkte);
                arduinoMeetIets = true;
                setLichtsterkte((int) lichtsterkte);

            }

            // Hier bepaal je of de lamp "Aan" of "Uit" staat, op basis van de lichtwaarde, en de instelling van een profiel.
            try {
                if (lichtsterkte <= jslMaxLichtsterkte.getValue()) {
                    mainInput.sendPiMessage("LAMP ON");
                    System.out.println("Lamp on");
                } else {
                    mainInput.sendPiMessage("LAMP OFF");
                    System.out.println("Lamp off");
                }
                mainInput.waitForPiResponse();

            } catch (NullPointerException nullpointer){
                System.out.println(nullpointer);
            }
        }

        if (mainInput.piIsConnected()) {

            mainInput.sendPiMessage("METING");
            String response = mainInput.waitForPiResponse();

            if (response != null && !response.equals("fail")) {
                piMeetIets = true;
                String[] piMetingen = response.split(" ");

                temperatuur = Double.parseDouble(piMetingen[0]);
                luchtvochtigheid = Integer.parseInt(piMetingen[1]);
                luchtdruk = Integer.parseInt(piMetingen[2]);

                setTemperatuur(temperatuur);
                setLuchtvochtigheid(luchtvochtigheid);
                setLuchtdruk(luchtdruk);

                pasVerwarmingAan();
            }
        }

        /* log de sensordata in de database */
        if (piMeetIets && arduinoMeetIets) {
            Database.insertLog(temperatuur, luchtdruk, luchtvochtigheid, (int) lichtsterkte);
        } else if (piMeetIets) {
            Database.insertLog(temperatuur, luchtdruk, luchtvochtigheid);
        } else if (arduinoMeetIets) {
            Database.insertLog((int) lichtsterkte);
        }
    }

    public void pasVerwarmingAan() {
        if (temperatuur <= (double) jspVerwarmingsTemperatuur.getValue()){
            jlKachelStatus.setIcon(new ImageIcon("src/images/kachelAan.png"));
            System.out.println("Kachel Aan");
        } else {
            jlKachelStatus.setIcon(new ImageIcon("src/images/kachelUit.png"));
            System.out.println("Kachel Uit");
        }
    }

    public void setTemperatuur(double temp) {
        jlTemperatuur.setText(temp + " ℃");
    }

    public void setLichtsterkte(int licht) {
        jlLichtsterkte.setText("Huidige lichtsterkte: " + licht);
    }

    public void setLuchtdruk(int druk) {
        jlLuchtdruk.setText("Luchtdruk: " + druk + " mBar");
    }

    public void setLuchtvochtigheid(int luchtvochtigheid) {
        jlLuchtvochtigheid.setText("Luchtvochtigheid: " + luchtvochtigheid + " %");
    }

    public void setProfielNaam(String naam) {
        jlProfielNaam.setText(naam);
    }

    public void setAfspeellijst(Afspeellijst afspeellijst) {
        this.afspeellijst = afspeellijst;
    }

    public void setMuziekText(String text) {
        setMuziekText(text, false);
    }

    public void setMuziekText(String text, boolean isWarning) {
        jlNaamMuziek.setText(text);
        if (isWarning) jlNaamMuziek.setForeground(Color.RED);
        else jlNaamMuziek.setForeground(Color.BLACK);
    }

    public void geenDatabase_Dialog() {
        JOptionPane.showMessageDialog(this, "Er is waarschijnlijk geen verbinding met de database", "Foutmelding", JOptionPane.ERROR_MESSAGE);
    }

    public void setNummer(Nummer nummer) {
        this.nummer = nummer;

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

    public void startNummer() {
        if (nummer == null) return;

        mainInput.sendPiMessage("MUSIC PLAY " + nummer.getBestandsNaam());
        String response = mainInput.waitForPiResponse();
        if (response != null && !response.equals("fail")) {
            jlPLay.setIcon(new ImageIcon("src/images/pause.png"));
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

            jlPLay.setIcon(new ImageIcon("src/images/pause.png"));
            gepauzeerd = false;
            muziekSliderTimer.start();
        }
    }

    public void pause() {

        mainInput.sendPiMessage("MUSIC PAUSE");
        String response = mainInput.waitForPiResponse();
        if (response != null && response.equals("success")) {
            jlPLay.setIcon(new ImageIcon("src/images/play.png"));
            gepauzeerd = true;
            muziekSliderTimer.stop();
        }
    }

    public void nextSong() {
        if (afspeellijst == null) return;
        afspeellijst.nextSong();
        setNummer(afspeellijst.getCurrentSong());
        startNummer();
    }

    public void previousSong() {
        if (afspeellijst == null) return;
        afspeellijst.previousSong();
        setNummer(afspeellijst.getCurrentSong());
        startNummer();
    }

    public Nummer getNummer() {
        return nummer;
    }

    public Afspeellijst getAfspeellijst() {
        return afspeellijst;
    }

    @Override
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

        } else if (e.getSource() == jslMaxLichtsterkte) {

            jslMaxLichtsterkte.repaint();

            if (jslMaxLichtsterkte.getValueIsAdjusting())
                return; // zolang gebruiker slider nog vast heeft (adjusting), niks doen.

            // maximale lichtsterkte is veranderd
            try {
                System.out.println("Lamp aan vanaf: " + jslMaxLichtsterkte.getValue());
                Database.updateProfielLicht(jslMaxLichtsterkte.getValue(), profiel.getId());
            } catch (NullPointerException np) {
                geenDatabase_Dialog();
            }

        } else if (e.getSource() == jspVerwarmingsTemperatuur) {
            // verwarmingstemperatuur is veranderd
            pasVerwarmingAan();
            try {
                Database.updateProfielTemp((double) jspVerwarmingsTemperatuur.getValue(), profiel.getId());
            } catch (NullPointerException np) {
                geenDatabase_Dialog();
            }

        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == jlAnderProfielAfb) {

            ProfielenManagement profielDialog = new ProfielenManagement(this);
            if (profielDialog.anderProfielGeselecteerd()) {
                // Het geselecteerde profiel ophalen, zodat het mainscherm aangepast kan worden.
                profiel = profielDialog.getGeselecteerdProfiel();
                updateSchermSettings(); // pas instellingen/gebruikersnaam aan op het nieuwe profiel
            }

        } else if (e.getSource() == jlPLay) {

            if (nummer == null) {
                setMuziekText("Kies eerst een nummer", true);
                return;
            }

            if (gepauzeerd) {
                unpause();
            } else {
                pause();
            }

        } else if (e.getSource() == jlPuntjes) {

            // open/sluit dropdown-menu'tje
            if (jpMuziekKnoppen.getHeight() < 80) {
                jpMuziekKnoppen.setSize(jpMuziekKnoppen.getWidth(), 125);
                jpMuziekKnoppen.setPreferredSize(new Dimension(jpMuziekKnoppen.getWidth(), 125));
            } else {
                jpMuziekKnoppen.setSize(jpMuziekKnoppen.getWidth(), 60);
                jpMuziekKnoppen.setPreferredSize(new Dimension(jpMuziekKnoppen.getWidth(), 60));
            }
        } else if (e.getSource() == jlSkip) {

            nextSong();

        } else if (e.getSource() == jlSkipBack) {

            previousSong();

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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == jlAfspeellijstOverzicht) {

            try {
                AfspeellijstOverzicht overzicht = new AfspeellijstOverzicht(profiel.getId(), this);
            } catch (NullPointerException NPE) {
                geenDatabase_Dialog();
            }

        } else if (e.getSource() == jlNummerOverzicht) {

            try {
                NummerOverzicht overzicht2 = new NummerOverzicht(profiel.getId(), this); // nummer-overzicht dialog
            } catch (NullPointerException NPE) {
//                NPE.printStackTrace();
                geenDatabase_Dialog();
            }

        } else if (e.getSource() == jlAfspeellijstToevoegen) {

            try {
                if (Database.selectAfspeellijsten(profiel.getId()).size() >= 8) {
                    JOptionPane.showMessageDialog(this, "Het maximaal aantal afspeellijsten is bereikt!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                AfspeellijstToevoegen toevoegen = new AfspeellijstToevoegen(this);
                if (toevoegen.getOk() && !toevoegen.getJtfNewAfspeellijst().equals("")) {

                    if(!Database.insertAfspeellijst(profiel.getId(), toevoegen.getJtfNewAfspeellijst())) {
                        JOptionPane.showMessageDialog(this, "Een Afspeellijst mag een naam van maximaal 15 symbolen hebben.", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                    }

                } else if (toevoegen.getOk() && toevoegen.getJtfNewAfspeellijst().equals("")) {
                    JOptionPane.showMessageDialog(this, "Er is geen naam ingevuld!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NullPointerException NPE) {
                geenDatabase_Dialog();
            }

        }
    }
}

