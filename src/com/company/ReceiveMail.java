package com.company;


import java.io.* ;
import java.util.* ;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * A simple program for receiving mail messages
 * from gmail using POP3S.
 *
 * @version 02-APR-2015
 */
public class ReceiveMail
{
   /**
    * Receives a text email.
    *
    * @param args Not used
    */
   public static void main( String[] args )
   {
      // The IP address of the POP3 server
      String host = "pop.gmail.com" ;

      // Username and password
      String user     = "b.simonpeter90@gmail.com" ;
      String password = "infantryman32" ;

      // Get system properties
      Properties properties = System.getProperties() ;

      // Request POP3S
      properties.put( "mail.store.protocol", "pop3s" ) ;

      // Get the default Session object
      javax.mail.Session session = javax.mail.Session.getDefaultInstance(properties) ;

      try
      {
         // Get a store for the POP3S protocol
         javax.mail.Store store = session.getStore() ;

         // Connect to the current host using the specified username and password
         store.connect( host, user, password ) ;

         // Create a Folder object corresponding to the given name
         Folder folder = store.getFolder( "Inbox" ) ;

         // Open the Folder
         folder.open(Folder.READ_WRITE);

         // Get the messages from the server
         Message[] messages = folder.getMessages();

         // Display message
         for(int i = 0; i < messages.length; i++) {
            Message msg = messages[i];

            String from = InternetAddress.toString(msg.getFrom());
            if( from != null ) {
               System.out.println("From: " + from);
            }

            String to = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
            if( to != null )
	    {
               System.out.println( "To: " + to ) ;
            }

            String subject = msg.getSubject() ;
            if( subject != null )
	    {
               System.out.println( "Subject: " + subject ) ;
            }

            Date sent = msg.getSentDate() ;
            if( sent != null )
	    {
               System.out.println( "Sent: " + sent ) ;
            }

            // Empty line to separate header from body
            System.out.println() ;

            // This could lead to troubles if anything but text was sent
            System.out.println(msg.getContent()) ;

            /* In der endgueltigen Version sollen die Mails geloest werden */

            // Mark this message for deletion when the session is closed
            // msg.setFlag( Flags.Flag.DELETED, true ) ;
         }

         folder.close( true ) ;
         store.close() ;
      }
      catch( MessagingException | IOException e )
      {
         e.printStackTrace() ;
      }
   }
}
