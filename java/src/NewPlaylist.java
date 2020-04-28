import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewPlaylist extends JDialog implements ActionListener{
    private JLabel jlNewPlaylist;
    private JTextField jtfNewPlaylist;
    private JButton jbNewPlaylist;
    private JButton jbAnnuleren;

    public NewPlaylist(JFrame frame){
        super(frame, true);
        setTitle("Nieuwe afspeellijst aanmaken");
        setLayout(new FlowLayout());
        setSize(300,130);

        jlNewPlaylist = new JLabel("Naam afspeellijst");
        jtfNewPlaylist = new JTextField(20);
        jbNewPlaylist = new JButton("Afspeellijst aanmaken");
        jbAnnuleren = new JButton("Annuleren");
        add(jlNewPlaylist);
        add(jtfNewPlaylist);
        add(jbNewPlaylist);
        jbNewPlaylist.addActionListener(this);
        add(jbAnnuleren);
        jbAnnuleren.addActionListener(this);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jbNewPlaylist){
            dispose();
        } else if (e.getSource() == jbAnnuleren){
            dispose();
        }
    }
}


