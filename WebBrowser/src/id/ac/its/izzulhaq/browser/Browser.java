package id.ac.its.izzulhaq.browser;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Browser extends JFrame {

    private JTextField addressBar;
    private JEditorPane mainDisplay;

    public Browser() {
        super("Theozu Browser v1.0");

        addressBar = new JTextField("Enter a URL");
        addressBar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFile(e.getActionCommand());
            }
        });
        add(addressBar, BorderLayout.NORTH);

        mainDisplay = new JEditorPane();
        mainDisplay.setEditable(false);
        mainDisplay.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    loadFile(e.getURL().toString());
                }
            }
        });
        add(new JScrollPane(mainDisplay), BorderLayout.CENTER);

        setSize(800, 600);
        setVisible(true);
    }

    private void loadFile(String userText) {
        try {
            mainDisplay.setPage(userText);
            addressBar.setText(userText);
        }
        catch (Exception e) {
            System.out.println("Oh,no!!");
            e.printStackTrace();
        }
    }
}
