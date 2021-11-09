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

        /*
        * Hear you need to use your information, use your email address and active SMTP */
        final String username = "XXXXXXX@XXXXXXXX"; // Email address
        final String password = "XXXXXXXXXXXXXXXX"; // Password or activation STMP code

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

                case "signed up":
                    title = "Activation Code";
                    content = "you have signed up in ATMachine</p><br/><br/><p><b>The activation code is: <font size=3>"
                            + number + "</font></b><p>This code is just for one time use</p>";
                    break;

                case "activate code":
                    title = "Activation Code";
                    content = "you have requested password changing in ATMachine</p><br/><br/><p><b>The activation code is: <font size=3>"
                            + number + "</font></b><p>This code is just for one time use</p>";
                    break;

                case "activation":
                    title = "Activated Account";
                    content = "</p><br/><br/><p><b>Your account in ATMachine is activated now!</b>";
                    break;

                case "password changed":
                    title = "APassword Is Changed";
                    content = "</p><br/><br/><p><b>Your password in ATMachine account is changed!</b>";
                    break;

                case "etransfer":
                    title = "E-Transfer Receiving";
                    content = "</p><br/><br/><p><b>You Have received <font size=3><U>$" + amount +
                            "</U></font> via E-Transfer from <font size=3><U>"
                            + from + "</U></font></b>,<br/>please log in ATMachine for deposit";
                    break;
            }

            String messageTitle = "ATMachine - " + title;
            String messsageBody =
                    "<html>" +
                    "<body>" +
                    "<br/>" +
                    "<br/>" +
                    "<h><U>ATMachine Application</U></h><br/><br/><br/>" +
                    "<p>Dear <b>" + name + "</b>,<br/>" +
                    "<p>This is an automate email and sending to you because " + content + "</p><br/><br/><br/><br/>" +
                    "<p>If you do not have account at the ATMachine application, then please discard this mail</p><br/>" +
                    "<p>**Please DO NOT REPLY**</p><br/><br/>" +
                    "<p>ATMachine admin team</p>" +
                    "<p>Regards</p>" +
                    "</body>" +
                    "</html>";

            message.setSubject(messageTitle);

            // Now set the actual message
            message.setContent(messsageBody, "text/html; charset=utf-8");

            // Send message
            Transport.send(message);
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
