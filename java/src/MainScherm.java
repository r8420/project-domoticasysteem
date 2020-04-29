
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;

public class MainScherm extends JFrame implements ChangeListener, MouseListener, ActionListener {

    public static void main(String[] args) {
        MainScherm scherm = new MainScherm();
    }

    private boolean arduinoAansluiting;
    private boolean piAansluiting;

    /* scherm-componenten */
    private JLabel jlLichtsterkte, jlTemperatuur, jlLuchtdruk, jlLuchtvochtigheid, jlProfielNaam, jlAnderProfielAfb, jlInstellingenAfb;
    private JSpinner jspVerwarmingsTemperatuur;
    private JSlider jslMaxLichtsterkte;
    private JButton jbLichtAan, jbLichtUit;

    /* connectie/update */
    private Timer timer;
    private MainInput mainInput;

    /* meetwaardes */
    private double temperatuur;
    private int lichtsterkte;
    private int luchtdruk;
    private int luchtvochtigheid;

    public MainScherm() {

        /* maak verbinding */
        mainInput = new MainInput();
        piAansluiting = mainInput.socketStart();
        arduinoAansluiting = mainInput.arduinoStart();


        /* default scherm settings */
        setSize(1000,750);
        setMinimumSize(new Dimension(1000, 800));
        setTitle("Domotica Systeem");
//        setResizable(false);


        /* standaard layout settings */
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        int standaardInset = 20;
        c.insets = new Insets(10,standaardInset,0,0);
        c.anchor = GridBagConstraints.LINE_START;


        /* muziekspeler panel */
        JPanel jpMuziekspeler = new JPanel();
        jpMuziekspeler.setLayout(new FlowLayout());


        /* verwarming / temperatuur panel*/
        JPanel jpVerwarming = new JPanel();
        jpVerwarming.setLayout(new GridBagLayout());
        jspVerwarmingsTemperatuur = new JSpinner(new SpinnerNumberModel(20, 0, 25, 0.5));
        jspVerwarmingsTemperatuur.setPreferredSize(new Dimension(50,30));
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

        int maxLichtWaarde = 10; // de slider kan zo een waarde van 0 tot 10 krijgen.
        jslMaxLichtsterkte = new JSlider(0,maxLichtWaarde,maxLichtWaarde/2);
        jslMaxLichtsterkte.setMajorTickSpacing((int)(maxLichtWaarde*0.1));
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put( 0, new JLabel("Donker") );
        labelTable.put(maxLichtWaarde, new JLabel("Licht") );
        jslMaxLichtsterkte.setLabelTable( labelTable );
        jslMaxLichtsterkte.setPaintTicks(true);
        jslMaxLichtsterkte.setPaintLabels(true);
        jslMaxLichtsterkte.addChangeListener(this);
        jslMaxLichtsterkte.setPreferredSize(new Dimension(400,40));
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
        c.insets = new Insets(0,0,0,0);
        c.weighty = 0;
        jpZijkant.add(jlProfielAfb, c);
        jpZijkant.add(jlAnderProfielAfb, c);
//        jpZijkant.add(jlInstellingenAfb, c);
        c.gridy = 1;
        c.weighty = 1;
        jlProfielNaam = new JLabel("Naam");
        jpZijkant.add(jlProfielNaam, c);
        jpZijkant.add(new JLabel("Ander profiel"), c);
//        jpZijkant.add(new JLabel("Instellingen"), c);


        // reset GridBagConstraints
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 0;
        c.insets = new Insets(0,0,0,0);


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
        c.insets = new Insets(10,10,10,10);

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
        c.weightx = 0.1;
        c.gridheight = 4;
        add(jpZijkant, c);


        /* timer voor opvragen van nieuwe gegevens */
        timer = new Timer(0, e -> {
            updateMeetWaardes();
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

        /* Maak het scherm zichtbaar */
        setVisible(true);
    }


    public void updateMeetWaardes() {

        System.out.println("Update meetwaardes!" + ((arduinoAansluiting || piAansluiting) ? "" : " (geen verbinding)"));

        if (arduinoAansluiting) {
            String arduinoMeting = mainInput.arduinoSensor();
            lichtsterkte = (int) Double.parseDouble(arduinoMeting);

            setLichtsterkte(lichtsterkte);
        }

        if (piAansluiting) {
            String[] piMetingen = mainInput.piSensoren();
            temperatuur = Double.parseDouble(piMetingen[0]);
            luchtvochtigheid = (int) Double.parseDouble(piMetingen[1]);
            luchtdruk = (int) Double.parseDouble(piMetingen[2]);

            setTemperatuur(temperatuur);
            setLuchtvochtigheid(luchtvochtigheid);
            setLuchtdruk(luchtdruk);
        }

        /* log de sensordata in de database */
        if (piAansluiting && arduinoAansluiting) {
            mainInput.insertDBLog(temperatuur, luchtdruk, luchtvochtigheid, lichtsterkte);
        } else if (piAansluiting) {
            mainInput.insertDBLog(temperatuur, luchtdruk, luchtvochtigheid);
        } else if (arduinoAansluiting) {
            mainInput.insertDBLog(lichtsterkte);
        }

    }

    public void veranderProfiel() {
        /* deze functie moet de profielsettings aanpassen wanneer iemand een ander profiel selecteert
        * update jslMaxLichtsterkte
        * update jspVerwarmingsTemperatuur
        * update jlProfielNaam
        * */
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
            System.out.println("Lamp aan vanaf: " + jslMaxLichtsterkte.getValue());

        } else if (e.getSource() == jspVerwarmingsTemperatuur) {
            // verwarmingstemperatuur is veranderd
            System.out.println("Verwarmen vanaf: " + jspVerwarmingsTemperatuur.getValue());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        if (e.getSource() == jlAnderProfielAfb) {
            // ander profiel wordt aangeklikt
            System.out.println("ander profiel");
            Profielen profielDialog = new Profielen(this);

        } else if (e.getSource() == jlInstellingenAfb) {
            // opties wordt aangeklikt
            System.out.println("instellingen");
        }
    }

    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbLichtAan) {
            // licht aan
            System.out.println("licht aan");
        } else if (e.getSource() == jbLichtUit) {
            // licht uit
            System.out.println("licht uit");
        }
    }
}
