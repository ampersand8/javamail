package com.company;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.table.*;
import java.awt.event.*;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

/**
 * Just a first try at a simple Mailclient, nothing serious
 * @author simon
 */


public class Javamail extends WindowAdapter {
    private final JFrame frame;
    private JTree tree;
    private TableModel model;
    private JTable mailTable;
    private Object[] columnNames;
    private final String PROPFILENAME = "/home/simon/IdeaProjects/test01/resources/config.properties";

    public Javamail(JFrame frame) {
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

    private JPanel createToolBarPanel() {
        final JToolBar zoomToolBar = new JToolBar();
        JButton fetchbutton = new JButton("Fetch");
        fetchbutton.addActionListener(new MyListener(this));
        zoomToolBar.add(fetchbutton);
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

    public String getProperty(String property) throws IOException {
        Properties prop = new Properties();

        InputStream inputStream = new FileInputStream(PROPFILENAME);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + PROPFILENAME + "' not found in the classpath");
        }
        return prop.getProperty(property);
    }

    public void fetch_mail() {
        Properties properties = System.getProperties();
        properties.put("mail.store.protocol","pop3s");
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties);

        try {
            javax.mail.Store store = session.getStore();
            store.connect(getProperty("host"), getProperty("user"), getProperty("password"));
            Folder folder = store.getFolder("Inbox");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();

            for (int i = 0; i < messages.length; i++) {
                Message msg = messages[i];
                saveMessageToFile(msg);

                String from = InternetAddress.toString(msg.getFrom());
                if (from != null) {
                    System.out.println("From: " + from);
                }

                String to = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
                if (to != null) {
                    System.out.println("To: " + to);
                }

                String subject = msg.getSubject();
                if (subject != null) {
                    System.out.println("Subject: " + subject);
                }

                Date sent = msg.getSentDate();
                if (sent != null) {
                    System.out.println("Sent: " + sent);
                }

                // Empty line to separate header from body
                System.out.println();

                // This could lead to troubles if anything but text was sent
                System.out.println(msg.getContent());

            /* In der endgueltigen Version sollen die Mails geloest werden */

                // Mark this message for deletion when the session is closed
                // msg.setFlag( Flags.Flag.DELETED, true ) ;
            }

            folder.close(true);
            store.close();
        }
        catch(MessagingException | IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMessageToFile(Message msg) {
        Calendar calendar = Calendar.getInstance();
        long timeinmillis = calendar.getTimeInMillis();
        String filename = Long.toString(timeinmillis);
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("inbox/"+filename), "utf-8"))) {
            writer.write("From: " + InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO)) + "\nTO: " + InternetAddress.toString(msg.getFrom()) + "\nSubject: " + msg.getSubject() + "\n" + msg.getContent());
        }
        catch(MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}

