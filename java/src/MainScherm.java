import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Hashtable;

public class MainScherm extends JFrame implements ChangeListener, MouseListener, ActionListener {


    public static void main(String[] args) throws InterruptedException {
        MainScherm scherm = new MainScherm();
    }

    private boolean arduinoAansluiting;
    private boolean piAansluiting;

    /* scherm-componenten */
    private JLabel jlLichtsterkte, jlTemperatuur, jlLuchtdruk, jlLuchtvochtigheid, jlProfielNaam, jlAnderProfielAfb, jlInstellingenAfb, jlPuntjes, jlOpvuller;
    private JSpinner jspVerwarmingsTemperatuur;
    private JSlider jslMaxLichtsterkte;
    private JButton jbLichtAan, jbLichtUit, jlAfspleellijstOverzicht, jlAfspleellijstToevoegen, jlNummerOverzicht;
    private JLabel jlSkip, jlSkipBack, jlPLay, JlNaamMuziek;
    private JSlider jsTijdMuziek;

    /* connectie/update */
    private Timer timer;
    private MainInput mainInput;

    /* meetwaardes */
    private double temperatuur;
    private int lichtsterkte;
    private int luchtdruk;
    private int luchtvochtigheid;
    private boolean playOrPause, newSong = true;

    private long timestamp;
    private long timestampPrev = 0;
    private int pl1 = 60;
    private JPanel outer;

    private Profiel profiel;
    private ArrayList<Afspeellijst> afspeellijstenList;

    public MainScherm() throws InterruptedException {

        /* maak verbinding */
        mainInput = new MainInput();
        piAansluiting = mainInput.socketStart();
        arduinoAansluiting = mainInput.arduinoStart();



        /* default scherm settings */
        setSize(1000, 750);
        setMinimumSize(new Dimension(1000, 800));
        setLocation(500, 0);
        setTitle("Domotica Systeem");
//        setResizable(false);


        /* standaard layout settings */
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int standaardInset = 20;
        c.insets = new Insets(10, standaardInset, 0, 0);
        c.anchor = GridBagConstraints.LINE_START;


        /* muziekspeler panel */
        /** het vak voor de muziekpanel en de tekstvak van de muzieknaam */
        JPanel jpMuziekspeler = new JPanel();
        jpMuziekspeler.setMaximumSize(new Dimension(700, 600));
        jpMuziekspeler.setLayout(new FlowLayout());
        JlNaamMuziek = new JLabel("Luis Fonsi - Despacito ft. Daddy Yankee", SwingConstants.CENTER);
        JlNaamMuziek.setPreferredSize(new Dimension(600, 100));
        JlNaamMuziek.setMinimumSize(new Dimension(600, 100));
        Border border = BorderFactory.createLineBorder(Color.black, 1);
        JlNaamMuziek.setBorder(border);
        jpMuziekspeler.add(JlNaamMuziek);


        /** de slider krijgt de max tijd en de huidige */
        int maxTijd = 10;
        int huidigeTijd = 0;
        jsTijdMuziek = new JSlider(0, maxTijd, 0);
        jsTijdMuziek.setEnabled(false);
        jsTijdMuziek.setMajorTickSpacing((int) (maxTijd * 0.1));
        Hashtable<Integer, JLabel> tijdLableTable = new Hashtable<>();
        tijdLableTable.put(0, new JLabel("00:00"));
        tijdLableTable.put(maxTijd, new JLabel("03:21"));
        jsTijdMuziek.setValue(huidigeTijd);
        jsTijdMuziek.setLabelTable(tijdLableTable);
        jsTijdMuziek.setPaintLabels(true);
        jsTijdMuziek.setPreferredSize(new Dimension(600, 40));
        jpMuziekspeler.add(jsTijdMuziek);

        /** de panel voor de knoppen van de mp3 */
        outer = new JPanel();
        outer.setLayout(null);
        outer.setPreferredSize(new Dimension(600, 60));
        jpMuziekspeler.add(outer);
        jlSkipBack = Functies.maakFotoLabel("src/images/skip_back.png");
        jlSkipBack.setBounds(190, 0, 50, 50);
        jlPLay = Functies.maakFotoLabel("src/images/play.png");
        jlPLay.setBounds(270, 0, 60, 60);
        jlSkip = Functies.maakFotoLabel("src/images/skip_forward.png");
        jlSkip.setBounds(360, 0, 50, 50);
        jlPuntjes = Functies.maakFotoLabel("src/images/3_puntjes.png");
        jlPuntjes.setBounds(440, 0, 50, 50);

        /** de knoppen voor de dropdown van 3_puntjes.png */
        jlAfspleellijstOverzicht = new JButton("Afspeellijst overzicht");
        jlAfspleellijstOverzicht.setBackground(Color.ORANGE);
        jlAfspleellijstOverzicht.setBounds(400, 61, 180, 20);
        jlAfspleellijstOverzicht.addActionListener(this);


        jlAfspleellijstToevoegen = new JButton("Afspleellijst toevoegen");
        jlAfspleellijstToevoegen.setBackground(Color.ORANGE);
        jlAfspleellijstToevoegen.setBounds(400, 82, 180, 20);
        jlAfspleellijstToevoegen.addActionListener(this);


        jlNummerOverzicht = new JButton("Nummer overzicht");
        jlNummerOverzicht.setBackground(Color.ORANGE);
        jlNummerOverzicht.setBounds(400, 103, 180, 20);
        jlNummerOverzicht.addActionListener(this);


        jlPuntjes.addMouseListener(this);
        jlPLay.addMouseListener(this);
        jlSkip.addMouseListener(this);
        jlSkipBack.addMouseListener(this);
        outer.add(jlSkipBack);
        outer.add(Box.createHorizontalStrut(30));
        outer.add(jlPLay);
        outer.add(Box.createHorizontalStrut(30));
        outer.add(jlSkip);
        outer.add(Box.createHorizontalStrut(30));
        outer.add(jlPuntjes);
        outer.add(jlAfspleellijstOverzicht);
        outer.add(jlAfspleellijstToevoegen);
        outer.add(jlNummerOverzicht);



        /* verwarming / temperatuur panel*/
        JPanel jpVerwarming = new JPanel();
        jpVerwarming.setLayout(new GridBagLayout());
        jspVerwarmingsTemperatuur = new JSpinner(new SpinnerNumberModel(0, 0, 25, 0.5));
        jspVerwarmingsTemperatuur.setPreferredSize(new Dimension(50, 30));
        jspVerwarmingsTemperatuur.addChangeListener(this);

        c.weightx = 0;
        jpVerwarming.add(new JLabel("Huidige temperatuur: "), c);
        c.weightx = 1;
        jlTemperatuur = new JLabel("-");
        jpVerwarming.add(jlTemperatuur, c);
        c.gridy = 1;
        c.weightx = 0;
        jpVerwarming.add(new JLabel("Verwarmen vanaf: "), c);
        c.weightx = 1;
        jpVerwarming.add(jspVerwarmingsTemperatuur, c);
        c.gridy = 0;


        /* licht panel */
        JPanel jpLicht = new JPanel();
        jpLicht.setLayout(new GridBagLayout());
        jbLichtAan = new JButton("Aan");
        jbLichtUit = new JButton("Uit");
        jlLichtsterkte = new JLabel("Huidige lichtsterkte: -");
        c.weightx = 0;
        jpLicht.add(jbLichtAan, c);
        jpLicht.add(jbLichtUit, c);
        c.weightx = 1;
        jpLicht.add(jlLichtsterkte, c);
        c.gridy = 1;
        c.gridwidth = 3;
        jpLicht.add(new JLabel("Licht aan vanaf: "), c);
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


        /* lucht panel */
        JPanel jpLucht = new JPanel();
        jpLucht.setLayout(new GridBagLayout());
        jlLuchtdruk = new JLabel("Luchtdruk: -");
        jlLuchtvochtigheid = new JLabel("Luchtvochtigheid: -");
        c.weightx = 0;
        jpLucht.add(jlLuchtdruk, c);
        c.weightx = 1;
        jpLucht.add(jlLuchtvochtigheid, c);


        /* zijkant panel (profiel knoppen) */
        JPanel jpZijkant = new JPanel();
        jpZijkant.setLayout(new GridBagLayout());

        JLabel jlProfielAfb = Functies.maakFotoLabel("src/images/profiel.png");
        jlAnderProfielAfb = Functies.maakFotoLabel("src/images/anderprofiel.png");
        jlInstellingenAfb = Functies.maakFotoLabel("src/images/instellingen.png");
        jlAnderProfielAfb.addMouseListener(this);
        jlInstellingenAfb.addMouseListener(this);

        c.anchor = GridBagConstraints.PAGE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.weighty = 0;
        jpZijkant.add(jlProfielAfb, c);
        jpZijkant.add(jlAnderProfielAfb, c);
//        jpZijkant.add(jlInstellingenAfb, c);
        c.gridy = 1;
        c.weighty = 1;
        jlProfielNaam = new JLabel();
        jpZijkant.add(jlProfielNaam, c);
        jpZijkant.add(new JLabel("Ander profiel"), c);
//        jpZijkant.add(new JLabel("Instellingen"), c);


        // reset GridBagConstraints
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 0, 0);


        /* tijdelijke borders rondom de panels */
        Border testBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
        jpMuziekspeler.setBorder(testBorder);
        jpVerwarming.setBorder(testBorder);
        jpLicht.setBorder(testBorder);
        jpLucht.setBorder(testBorder);
//        jpZijkant.setBorder(testBorder);


        /* voeg alle panels toe aan het hoofdscherm */
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


        /* timer voor opvragen van nieuwe gegevens */
        timer = new Timer(0, e -> {
            try {
                updateMeetWaardes();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        timer.setDelay(10000); // millisec, 1.000 = 1 sec
        timer.start();


        /* wat te doen als op kruisje wordt gedrukt */
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (piAansluiting) mainInput.socketStop(); // verbreek verbinding
                e.getWindow().dispose(); // sluit het scherm
            }
        });


        /* stel mainscherm in op laatste gebruiker */
        Profiel recentProfiel = Database.selectLastProfile();
        if (recentProfiel == null) { // is er geen laatste gebruiker, maak dan gast account
            Database.insertDBprofile("Gast");
            profiel = Database.selectLastProfile();
        } else {
            profiel = recentProfiel;
        }

        afspeellijstenList = Database.selectDBafspeellijsten(profiel.getId());

        /* Maak het scherm zichtbaar */
        setVisible(true);

        /* error voor database verbinding */
        if (profiel != null) {
            updateSchermSettings();
        } else {
            JOptionPane.showMessageDialog(this, "Er is waarschijnlijk geen verbinding met de database", "Foutmelding", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateSchermSettings() {
        setProfielNaam(profiel.getNaam());
        jspVerwarmingsTemperatuur.setValue(profiel.getTempVerwarmen());
        jslMaxLichtsterkte.setValue(profiel.getLichtWaarde());

    }


    public void updateMeetWaardes() throws IOException {
        boolean arduinoMeetIets = false;
        System.out.println("Update meetwaardes!" + ((arduinoAansluiting || piAansluiting) ? "" : " (geen verbinding)"));

        if (arduinoAansluiting) {

            String arduinoMeting = mainInput.arduinoSensor();
            if (arduinoMeting != null) {
                lichtsterkte = Integer.parseInt(arduinoMeting);
                arduinoMeetIets = true;
                setLichtsterkte(lichtsterkte);

            }
        }

        if (piAansluiting) {
            mainInput.sendMessage("read sensors");
            String[] piMetingen = mainInput.piSensoren();
            temperatuur = Double.parseDouble(piMetingen[0]);
            luchtvochtigheid = Integer.parseInt(piMetingen[1]);
            luchtdruk = Integer.parseInt(piMetingen[2]);

            setTemperatuur(temperatuur);
            setLuchtvochtigheid(luchtvochtigheid);
            setLuchtdruk(luchtdruk);
        }

        /* log de sensordata in de database */
        if (piAansluiting && (arduinoAansluiting && arduinoMeetIets)) {
            Database.insertDBLog(temperatuur, luchtdruk, luchtvochtigheid, lichtsterkte);
        } else if (piAansluiting) {
            Database.insertDBLog(temperatuur, luchtdruk, luchtvochtigheid);
        } else if (arduinoAansluiting && arduinoMeetIets) {
            Database.insertDBLog(lichtsterkte);
        }

    }

    public void setTemperatuur(double temp) {
        jlTemperatuur.setText(String.valueOf(temp) + " ℃");
    }

    public void setLichtsterkte(int licht) {
        jlLichtsterkte.setText("Huidige lichtsterkte: " + licht);
    }

    public void setLuchtdruk(int druk) {
        jlLuchtdruk.setText("Luchtdruk: " + druk);
    }

    public void setLuchtvochtigheid(int luchtvochtigheid) {
        jlLuchtvochtigheid.setText("Luchtvochtigheid: " + luchtvochtigheid);
    }

    public void setProfielNaam(String naam) {
        jlProfielNaam.setText(naam);
    }


    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == jslMaxLichtsterkte && !jslMaxLichtsterkte.getValueIsAdjusting()) {
            // maximale lichtsterkte is veranderd
            try {
                System.out.println("Lamp aan vanaf: " + jslMaxLichtsterkte.getValue());
                Database.updateDBlicht(jslMaxLichtsterkte.getValue(), profiel.getId());
            } catch (NullPointerException np) {
                JOptionPane.showMessageDialog(this, "Er is waarschijnlijk geen verbinding met de database", "Foutmelding", JOptionPane.ERROR_MESSAGE);
            }


        } else if (e.getSource() == jspVerwarmingsTemperatuur) {
            // verwarmingstemperatuur is veranderd
            try {
                Database.updateDBtemp((double) jspVerwarmingsTemperatuur.getValue(), profiel.getId());
                System.out.println("Verwarmen vanaf: " + jspVerwarmingsTemperatuur.getValue());
            } catch (NullPointerException np) {
                JOptionPane.showMessageDialog(this, "Er is waarschijnlijk geen verbinding met de database", "Foutmelding", JOptionPane.ERROR_MESSAGE);
            }

        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == jlAnderProfielAfb) {
            // ander profiel wordt aangeklikt
            System.out.println("ander profiel");

            ProfielenManagement profielDialog = new ProfielenManagement(this);
            if (profielDialog.anderProfielGeselecteerd()) {
                // Nieuw profiel aanmaken zodat de gebruikersnaam kan worden opgehaald, deze wordt getoond op Mainscherm.
                profiel = profielDialog.getGeselecteerdProfiel();
                updateSchermSettings(); // pas instellingen/gebruikersnaam aan op het nieuwe profiel
                System.out.println("Instelling voor temperatuur: " + profiel.getTempVerwarmen());
            }
        } else if (e.getSource() == jlPLay) {
            if (newSong) {
                JlNaamMuziek.setText("geen muziek gekozen");
                JlNaamMuziek.setForeground(Color.RED);
            } else {
                timestamp = System.currentTimeMillis() / 1000;

                if (timestamp - timestampPrev >= 1) {
                    if (playOrPause) {
                        System.out.println("play");
                        jlPLay.setIcon(new ImageIcon("src/images/pause.png"));
                        playOrPause = !playOrPause;
                        mainInput.sendMessage("unpause");
                    } else {
                        jlPLay.setIcon(new ImageIcon("src/images/play.png"));
                        System.out.println("pause");
                        playOrPause = !playOrPause;
                        mainInput.sendMessage("pause");
                    }
                    timestampPrev = System.currentTimeMillis() / 1000;
                }
            }
        } else if (e.getSource() == jlPuntjes) {
            if (pl1 < 80) {
                pl1 = 125;
                outer.setSize(outer.getWidth(), pl1);
            } else {
                pl1 = 60;
                outer.setSize(outer.getWidth(), pl1);

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbLichtAan) {
            // licht aan
            System.out.println("licht aan");
        } else if (e.getSource() == jbLichtUit) {
            // licht uit
            System.out.println("licht uit");

        } else if (e.getSource() == jlAfspleellijstOverzicht) {
            AfspeellijstOverzicht overzicht = new AfspeellijstOverzicht(profiel.getId(), this);
        } else if (e.getSource() == jlNummerOverzicht) {
            NummerOverzicht overzicht2 = new NummerOverzicht(profiel.getId(), this);
        } else if (e.getSource() == jlAfspleellijstToevoegen) {
            if (afspeellijstenList.size() >= 8) {
                JOptionPane.showMessageDialog(this, "Het maximaal aantal afspeellijsten is bereikt!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
                return;
            }
            AfspeellijstToevoegen toevoegen = new AfspeellijstToevoegen(this);
            if (toevoegen.getOk() && !toevoegen.getJtfNewAfspeellijst().equals("")) {
                Database.insertDBAfspeellijst(profiel.getId(), toevoegen.getJtfNewAfspeellijst());
            } else if (toevoegen.getOk() && toevoegen.getJtfNewAfspeellijst().equals("")) {
                JOptionPane.showMessageDialog(this, "Er is geen naam ingevuld!", "Foutmelding", JOptionPane.ERROR_MESSAGE);
            }
        }

        afspeellijstenList = Database.selectDBafspeellijsten(profiel.getId());
    }
}

