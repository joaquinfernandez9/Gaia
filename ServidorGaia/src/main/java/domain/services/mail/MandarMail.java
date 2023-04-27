package domain.services.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import java.util.Properties;
import jakarta.mail.Session;

@Log4j2
public class MandarMail {

    public void generateAndSendEmail(String to, String msg, String subject) throws MessagingException {
        Properties mailProp;
        Session getMailSession;
        MimeMessage generateMailMessage;

        // 1
        mailProp = System.getProperties();
        mailProp.put("mail.smtp.port", Integer.parseInt("587"));
        mailProp.put("mail.smtp.auth", "true");
        mailProp.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailProp.put("mail.smtp.starttls.enable", "true");

        // 2

        getMailSession = Session.getDefaultInstance(mailProp, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        generateMailMessage.setSubject(subject);
        String emailBody = msg;
        generateMailMessage.setContent(emailBody, "text/html");

        // 3

        Transport transport = getMailSession.getTransport("smtp");

        // Enter your correct gmail UserID and Password
        // if you have 2FA enabled then provide App Specific Password
        transport.connect("smtp.gmail.com",
                "gaia.mail.svr@gmail.com",
                "Gaia2023");

        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}
