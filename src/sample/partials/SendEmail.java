package sample.partials;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmail {

    public static void mailing(String to, String name, String number, String from, String amount, String type) {
        String host = "smtp.gmail.com";
        final String username = "atmachine.application@gmail.com";
        final String password = "dhnqbscpjiryntok";

        // Get system properties
        Properties props = System.getProperties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        // Get the default Session object.
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(to));

            // Set To: header field of the header.
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));

            String title = "";
            String content = "";

            switch (type) {

                case "activate code":
                    title = "Activation Code";
                    content = "The activation code is: " + number + "\n\n\n\n\n\n";
                    break;

                case "activation":
                    title = "Activated Account";
                    content = "Your account in ATMachine is activated now!\n\n\n\n\n\n";
                    break;

                case "password changed":
                    title = "APassword Is Changed";
                    content = "Your account in ATMachine is activated now!\n\n\n\n\n\n";
                    break;

                case "etransfer":
                    title = "E-Transfer Receiving";
                    content = "Your Have received " + amount + "$ via E-Transfer from " + from + "\nplease log in ATMachine for deposit\n\n\n\n\n\n";
                    break;
            }

            String messageTitle = "ATMachine - " + title;
            String messsageBody = " From: ATMachine Application\n\n" +
                                  " Dear " + name + ", \n\n" +
                                  " This is an automate email and sending to you because you have signed up in ATMachine\n\n\n\n\n" +
                                  content +
                                  " This code is just for one time use\n\n\n" +
                                  " If you have not signed up at the ATMachine application, then please discard this mail\n\n" +
                                  " Please DO NOT REPLY\n\n" +
                                  " Regards";

            message.setSubject(messageTitle);

            // Now set the actual message
            message.setText(messsageBody);

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
