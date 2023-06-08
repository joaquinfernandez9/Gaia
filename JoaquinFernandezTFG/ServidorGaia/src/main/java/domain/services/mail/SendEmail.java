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
public class SendEmail {

    public void generateAndSendEmail(String to, String msg, String subject) throws MessagingException {
        Properties mailProp;
        Session getMailSession;
        MimeMessage generateMailMessage;
        mailProp = System.getProperties();
        mailProp.put("mail.smtp.port", Integer.parseInt("587"));
        mailProp.put("mail.smtp.auth", "true");
        mailProp.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailProp.put("mail.smtp.starttls.enable", "true");
        getMailSession = Session.getDefaultInstance(mailProp, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        generateMailMessage.setSubject(subject);
        String emailBody = msg;
        generateMailMessage.setContent(emailBody, "text/html");
        Transport transport = getMailSession.getTransport("smtp");
        transport.connect("smtp.gmail.com",
                "gaia.mail.svr@gmail.com",
                "cbflpeaovrqklcbc");
        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }
}
