import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfielToevoegen extends JDialog implements ActionListener {
    private String gebruikersnaam;
    private JLabel jlNewProfile;
    private JTextField jtfNewProfile;
    private JButton jbNewProfile;
    private JButton jbAnnuleren;

    public ProfielToevoegen(JFrame frame) {
        super(frame, true);
        setTitle("Nieuwe profiel aanmaken");
        setSize(300, 130);
        setLayout(new FlowLayout());

        jlNewProfile = new JLabel("Naam Profiel:");
        jtfNewProfile = new JTextField(20);
        jbNewProfile = new JButton("Profiel aanmaken");
        jbAnnuleren = new JButton("Annuleren");
        add(jlNewProfile);
        add(jtfNewProfile);
        add(jbNewProfile);
        jbNewProfile.addActionListener(this);
        add(jbAnnuleren);
        jbAnnuleren.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbNewProfile) {
            gebruikersnaam = jtfNewProfile.getText();
            System.out.println(gebruikersnaam);
            // INSERT INTO (de gebruikersnaam)
            dispose();
        } else if (e.getSource() == jbAnnuleren) {
            dispose();
        }
    }
}


