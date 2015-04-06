package com.company;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Beispielklasse einer sehr einfachen ersten Swing-Applikation.
 * Hier werden beide Bedienelemente dargestellt.
 * <br>
 * <b>Neu in Auflage 2</b>
 *
 * @author Michael Inden
 *
 * Copyright 2012 by Michael Inden
 */


public class FirstSwingExampleImproved extends WindowAdapter {
    private final JFrame frame;
    private JTree tree;

    public FirstSwingExampleImproved(JFrame frame) {
        this.frame = frame;


        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // JLabel bzw. JButton mit Positionierungsangaben zum JFrame hinzuf�gen
        frame.add(TreeExample(), BorderLayout.WEST);
        frame.add(createMailTable(), BorderLayout.CENTER);
        frame.add(createToolBarPanel(), BorderLayout.NORTH);

        frame.setSize(400, 200);
        frame.setVisible(true);
    }

    private JScrollPane createMailTable() {
/*        Object rowData[][] = {{"Row1-Column1", "Row1-Column2", "Row1-Column3"},
                {"Row2-Column1", "Row2-Column2", "Row2-Column3"}}; */
        Object rowData[][] = getlocalmails("inbox");
        Object columnNames[] = { "Subject" };
        JTable mailTable = new JTable(rowData, columnNames);

        return new JScrollPane(mailTable);
    }

    private static JPanel createToolBarPanel() {
        final JToolBar zoomToolBar = new JToolBar();
        zoomToolBar.add(new JButton("+"));
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

    private String[][] getlocalmails(String folder) {
        File directory = new File(folder);
        File[] listofmails = directory.listFiles();
        String[][] out = new String[10][1];
        for (int i = 0; i < listofmails.length; i++) {
            out[i][0] = listofmails[i].getName();
        }
        return out;
    }

    @Override
    public void windowClosing(final WindowEvent event) {
        final int answer = JOptionPane.showConfirmDialog(frame, "Do you want to close this program?");
        if(answer == JOptionPane.YES_OPTION) {
            frame.setVisible(false);
            frame.dispose();

            System.exit(0);
        }
    }

    public JTree TreeExample() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        DefaultMutableTreeNode inbox = new DefaultMutableTreeNode("Inbox");
        DefaultMutableTreeNode outbox = new DefaultMutableTreeNode("Outbox");

        root.add(inbox);
        root.add(outbox);

        tree = new JTree(root);
        return tree;
    }
}

