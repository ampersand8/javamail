package com.company;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * Created by simon on 4/23/15.
 */
public class FetchMail implements Runnable {
    private Properties properties;
    private javax.mail.Session session;
    private Properties settings;

    public FetchMail(Properties settings) {
        this.settings = settings;
        properties = System.getProperties();
        properties.put("mail.store.protocol","pop3s");
        session = javax.mail.Session.getDefaultInstance(properties);
    }

    private void saveMessageToFile(Message msg) {
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

    @Override
    public void run() {
        try {
            javax.mail.Store store = session.getStore();
            store.connect(settings.getProperty("HOST"), settings.getProperty("USER"), settings.getProperty("PASSWORD"));
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
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}
