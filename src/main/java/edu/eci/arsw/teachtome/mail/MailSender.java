package edu.eci.arsw.teachtome.mail;

import com.sun.mail.smtp.SMTPTransport;
import edu.eci.arsw.teachtome.model.User;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MailSender implements MailSenderInterface {
    private static final String SMTP_SERVER = "smtp.gmail.com";
    private static final String USERNAME = "teachtomemail@gmail.com";
    private static final String PASSWORD = "7P{BZFU_SjFR[4#!";
    private static final String EMAIL_TO_CC = "";
    private final Properties prop;

    public MailSender() {
        prop = System.getProperties();
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.host", SMTP_SERVER);
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25"); // default port 25
        prop.put("mail.smtp.starttls.enable", "true");
    }

    @Override
    public void sendCreatedUserEmail(User user) {
        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(USERNAME));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(EMAIL_TO_CC, false));
            msg.setSubject("Account Successfully Created");
            msg.setDataHandler(new DataHandler(new HTMLDataSource(getHtmlMessage(user))));
            msg.setSentDate(new Date());
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
            t.sendMessage(msg, msg.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            Logger.getLogger(MailSender.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private String getHtmlMessage(User user) {
        return String.format("<div style=\"text-align:center;\"> <div style=\"display: inline-block;text-align:center;margin-left:2px;margin-right:2px;box-shadow: 0px 0px 20px -7px rgba(0,0,0,0.75);\"><h1 style=\"color: #8c211c\">Welcome %s %s</h1><br><a>Your account in Teach To Me was successfully created</a></div></div>", user.getFirstName(), user.getLastName());
    }
}
