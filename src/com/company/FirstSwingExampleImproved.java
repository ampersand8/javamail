package com.company;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.tree.*;
import java.util.Properties;
import java.io.*;

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
    private TableModel model;
    private JTable mailTable;
    private Object[] columnNames;

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
        try {
            Object rowData[][] = getlocalmails("inbox");
            this.columnNames = new Object[]{ "Subject" };
            this.mailTable = new JTable(rowData, this.columnNames);
            return new JScrollPane(this.mailTable);
        } catch (IOException e) {

        }
        return null;
    }

    private void updateMailTable(String folder) {
        try {
            Object rowData[][] = getlocalmails(folder);
            DefaultTableModel newmodel = new DefaultTableModel(rowData,this.columnNames);
            this.mailTable.setModel(newmodel);
            newmodel.fireTableDataChanged();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
        }
    }

    private static JPanel createToolBarPanel() {
        final JToolBar zoomToolBar = new JToolBar();
        zoomToolBar.add(new JButton("Fetch"));
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

    private String[][] getlocalmails(String folder) throws IOException {
        File directory = new File(folder);
        String[][] out = new String[10][1];
        if (directory.exists() && directory.isDirectory()) {
           File[] listofmails = directory.listFiles();

            for (int i = 0; i < listofmails.length; i++) {
                out[i][0] = listofmails[i].getName();
            }
        }
        else {
            throw new IOException("Folder "+folder+" does not exist");
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
        tree.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                try {
                    String selPath = tree.getPathForLocation(e.getX(), e.getY()).getLastPathComponent().toString().toLowerCase();
                    if (selPath != null) {
                        updateMailTable(selPath);
                    }
                }
                catch (NullPointerException err) {

                }
            }
        });
        return tree;
    }

    public String[] getProperty(String propFileName) throws IOException {
        Properties prop = new Properties();

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }

    }

    public void fetch_mail() {

    }
}

