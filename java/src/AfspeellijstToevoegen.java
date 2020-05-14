import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AfspeellijstToevoegen extends JDialog implements ActionListener {
    private JTextField jtfNewAfspeellijst;
    private JButton jbNewAfspeellijst;
    private boolean ok = false;

    public String getJtfNewAfspeellijst() {
        return jtfNewAfspeellijst.getText();
    }

    public boolean getOk() {
        return ok;
    }

    public AfspeellijstToevoegen(JFrame frame) {
        super(frame, true);
        setTitle("Nieuwe afspeellijst aanmaken");
        getContentPane().setBackground(new Color(255, 205, 214));
        setSize(300, 130);
        setLayout(new FlowLayout());

        JLabel jlNewProfile = new JLabel("Naam Afspeellijst:");
        jtfNewAfspeellijst = new JTextField(20);
        jbNewAfspeellijst = new JButton("Afspeellijst aanmaken");
        JButton jbAnnuleren = new JButton("Annuleren");
        add(jlNewProfile);
        add(jtfNewAfspeellijst);
        add(jbNewAfspeellijst);
        jbNewAfspeellijst.addActionListener(this);
        add(jbAnnuleren);
        jbAnnuleren.addActionListener(this);
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbNewAfspeellijst) {
            ok = true;
        }
        setVisible(false);
    }
}
