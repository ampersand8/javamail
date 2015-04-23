package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by simon on 4/23/15.
 */
public class CreateMail extends WindowAdapter {
    private JFrame createMailFrame;

    CreateMail() {
        createMailFrame = new JFrame();
        createMailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // JLabel bzw. JButton mit Positionierungsangaben zum JFrame hinzufuegen
        createMailFrame.add(createEditor(), BorderLayout.CENTER);
        createMailFrame.add(createToolBarPanel(), BorderLayout.NORTH);

        createMailFrame.setSize(400, 200);
        createMailFrame.setVisible(true);
    }

    private JPanel createToolBarPanel() {
        final JToolBar zoomToolBar = new JToolBar();
        JButton sendButton = new JButton("Send");
        zoomToolBar.add(sendButton);
        zoomToolBar.add(new JButton("-"));
        zoomToolBar.addSeparator();
        zoomToolBar.add(new JButton("100%"));

        final JToolBar skipToolBar = new JToolBar();
        skipToolBar.add(new JButton("|<-"));
        skipToolBar.add(new JButton("<<"));
        skipToolBar.add(new JButton(">>"));
        skipToolBar.add(new JButton("->|"));

        final JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbarPanel.add(zoomToolBar);
        toolbarPanel.add(skipToolBar);

        return toolbarPanel;
    }

    private JScrollPane createEditor() {
        JTextArea text = new JTextArea();
        JScrollPane scroll = new JScrollPane(text);
        return scroll;
    }
/*
    @Override
    public void windowClosing(final WindowEvent event) {
        final int answer = JOptionPane.showConfirmDialog(createMailFrame, "Do you want to cancel writing this mail?");
        if(answer == JOptionPane.YES_OPTION) {
            createMailFrame.setVisible(false);
            createMailFrame.dispose();

            System.exit(0);
        }
    }*/
}
