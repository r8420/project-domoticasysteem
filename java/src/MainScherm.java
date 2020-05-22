import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class MainScherm extends JFrame implements ChangeListener, MouseListener {


    public static void main(String[] args) throws InterruptedException {
        MainScherm scherm = new MainScherm();
    }

    private boolean arduinoAansluiting;

    /* scherm-componenten */
    private JLabel jlLichtsterkte;
    private JLabel jlTemperatuur;
    private JLabel jlKachelStatus;
    private JLabel jlLuchtdruk;
    private JLabel jlLuchtvochtigheid;
    private JLabel jlProfielNaam;
    private JLabel jlAnderProfielAfb;
    private JSpinner jspVerwarmingsTemperatuur;
    private JSlider jslMaxLichtsterkte;
    private LichtGraphicsPanel lichtGraphicsPanel;


    /* connectie/update */
    private Timer metingTimer;
    private MainInput mainInput;

    /* meetwaardes */
    private double temperatuur;
    private double lichtsterkte;
    private int luchtdruk;
    private int luchtvochtigheid;

    private Profiel profiel;

    private MuziekSpeler jpMuziekspeler;

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
        c.anchor = GridBagConstraints.CENTER;

        /*
         * verwarming / temperatuur panel
         * */
        JPanel jpVerwarming = new JPanel();
        jpVerwarming.setLayout(new GridLayout(1, 2));

        JPanel jpVerwarmingLinks = new JPanel(new GridBagLayout());
        JPanel jpVerwarmingRechts = new JPanel(new GridBagLayout());
        jpVerwarming.add(jpVerwarmingLinks);
        jpVerwarming.add(jpVerwarmingRechts);

        jlTemperatuur = new JLabel("-");
        jlTemperatuur.setFont(new Font(jlTemperatuur.getFont().getName(), Font.BOLD, 50));
        jlKachelStatus = Functies.maakFotoLabel("src/images/kachelUit.png");
        jspVerwarmingsTemperatuur = new JSpinner(new SpinnerNumberModel(0, 0, 50, 0.5));
        jspVerwarmingsTemperatuur.setPreferredSize(new Dimension(50, 30));
        jspVerwarmingsTemperatuur.addChangeListener(this);

        c.weightx = 0.5;
        jpVerwarmingLinks.add(jlTemperatuur, c);
        jpVerwarmingLinks.add(jlKachelStatus, c);

        c.anchor = GridBagConstraints.LINE_END;
        jpVerwarmingRechts.add(new JLabel("Verwarmen tot en met: "), c);
        c.anchor = GridBagConstraints.LINE_START;
        c.weightx = 1;
        jpVerwarmingRechts.add(jspVerwarmingsTemperatuur, c);


        /*
         * licht panel
         * */
        JPanel jpLicht = new JPanel();
        jpLicht.setLayout(new GridLayout(1, 2));

        JPanel jpLichtLinks = new JPanel(new GridBagLayout());
        JPanel jpLichtRechts = new JPanel(new GridBagLayout());
        jpLicht.add(jpLichtLinks);
        jpLicht.add(jpLichtRechts);

        jlLichtsterkte = new JLabel("Huidige lichtsterkte: -");
        lichtGraphicsPanel = new LichtGraphicsPanel();


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

        // links
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0.5;
        c.weighty = 1;
        jpLichtLinks.add(jlLichtsterkte, c);
        c.weightx = 1;
        c.fill = GridBagConstraints.BOTH;
        c.ipady = -5;
        c.insets = new Insets(20, 0, 20, 0);
        jpLichtLinks.add(lichtGraphicsPanel, c);

        c.ipady = 0; //reset
        c.insets = new Insets(0, 0, 0, 0); //reset


        // rechts
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_END;
        jpLichtRechts.add(new JLabel("Licht aan tot en met: "), c);
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy++;
        jpLichtRechts.add(jslMaxLichtsterkte, c);

        c.anchor = GridBagConstraints.CENTER; // reset
        c.gridy = 0; // reset


        /*
         * lucht panel
         * */
        JPanel jpLucht = new JPanel();
        jpLucht.setLayout(new GridBagLayout());

        jlLuchtdruk = new JLabel("-");
        jlLuchtvochtigheid = new JLabel("-");

        jlLuchtdruk.setFont(new Font(jlLuchtdruk.getFont().getName(), Font.BOLD, 30));
        jlLuchtvochtigheid.setFont(new Font(jlLuchtvochtigheid.getFont().getName(), Font.BOLD, 30));


        jpLucht.add(Functies.maakFotoLabel("src/images/Luchtdruk.png"), c);
        jpLucht.add(jlLuchtdruk, c);
        jpLucht.add(Functies.maakFotoLabel("src/images/Waterdruppel.png"), c);
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
        c.fill = GridBagConstraints.NONE;
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
        jpVerwarming.setBorder(testBorder);
        jpLicht.setBorder(testBorder);
        jpLucht.setBorder(testBorder);

        Color zalmroze = new Color(255, 145, 164);
        Color lichtZalmroze = new Color(255, 205, 214);

        getContentPane().setBackground(zalmroze);
        jpZijkant.setBackground(zalmroze);

        jslMaxLichtsterkte.setBackground(lichtZalmroze);

        jpLucht.setBackground(lichtZalmroze);

        jpLichtLinks.setBackground(lichtZalmroze);
        jpLichtRechts.setBackground(lichtZalmroze);
        jpVerwarmingLinks.setBackground(lichtZalmroze);
        jpVerwarmingRechts.setBackground(lichtZalmroze);

        /*
         * voeg alle panels toe aan het hoofdscherm
         * */
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.insets = new Insets(10, 10, 10, 10);

        jpMuziekspeler = new MuziekSpeler(this, mainInput);
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
         * acties om uit te voeren wanneer het scherm sluit
         * */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                jpMuziekspeler.stopSlider();
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
                lichtsterkte = Math.round(lichtsterkte / 102.4); // Opgehaalde lichtwaarde van de Arduino relativeren naar 0 tot 10, overeenkomend met de slider.

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

            } catch (NullPointerException nullpointer) {
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
        if (temperatuur <= (double) jspVerwarmingsTemperatuur.getValue()) {
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
        lichtGraphicsPanel.setLichtsterkte(licht);
        jlLichtsterkte.setText("Huidige lichtsterkte: " + licht);
    }

    public void setLuchtdruk(int druk) {
        jlLuchtdruk.setText(druk + " mBar");
    }

    public void setLuchtvochtigheid(int luchtvochtigheid) {
        jlLuchtvochtigheid.setText(luchtvochtigheid + " %");
    }

    public void setProfielNaam(String naam) {
        jlProfielNaam.setText(naam);
    }


    public void geenDatabase_Dialog() {
        JOptionPane.showMessageDialog(this, "Er is waarschijnlijk geen verbinding met de database", "Foutmelding", JOptionPane.ERROR_MESSAGE);
    }

    public MuziekSpeler getJpMuziekspeler() {
        return jpMuziekspeler;
    }


    public Profiel getProfiel() {
        return profiel;
    }


    @Override
    public void stateChanged(ChangeEvent e) {

        if (e.getSource() == jslMaxLichtsterkte) {

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
                jpMuziekspeler.setAfspeellijst(null);
                profiel = profielDialog.getGeselecteerdProfiel();
                updateSchermSettings(); // pas instellingen/gebruikersnaam aan op het nieuwe profiel
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

