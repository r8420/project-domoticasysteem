import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ProfielToevoegen extends JDialog implements ActionListener {
    private JTextField jtfNewProfile;
    private JButton jbNewProfile;
    private boolean ok;

    public String getJtfNewProfile() {
        return jtfNewProfile.getText();
    }

    public boolean getOk() {
        return ok;
    }

    public ProfielToevoegen(JDialog frame) {
        super(frame, true);
        setTitle("Nieuwe profiel aanmaken");
        setSize(300, 130);
        setLayout(new FlowLayout());

        JLabel jlNewProfile = new JLabel("Naam Profiel:");
        jtfNewProfile = new JTextField(20);
        jbNewProfile = new JButton("Profiel aanmaken");
        JButton jbAnnuleren = new JButton("Annuleren");
        add(jlNewProfile);
        add(jtfNewProfile);
        add(jbNewProfile);
        jbNewProfile.addActionListener(this);
        add(jbAnnuleren);
        jbAnnuleren.addActionListener(this);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbNewProfile) {
            ok = true;
        }
        setVisible(false);
    }
}


