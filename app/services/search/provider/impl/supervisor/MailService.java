package services.search.provider.impl.supervisor;

import play.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * MailService ...
 *
 * @author vadim
 * @date 1/9/13
 */
public class MailService {

    public static final String DEFAULT_EMAIL_FROM = "proxy.supervisor.noreply@gmail.com";
    public static final String DEFAULT_EMAIL_FROM_PASSWD = "proxy12345";
    public static int DEFAULT_SMTP_PORT = 465;
    public static final String EMAIL_REGEXP = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEXP);
    private static final MailService DEFAULT_INSTANCE = new MailService(DEFAULT_EMAIL_FROM, DEFAULT_EMAIL_FROM_PASSWD, DEFAULT_SMTP_PORT);
    private final String emailFrom;
    private final String pass;
    private int smtpPort = 465;

    public MailService(String email, String pass, int smtpPort) throws IllegalArgumentException, NullPointerException {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException(String.format("Invalid email %s", email));
        }
        this.emailFrom = email;
        this.pass = pass;
        this.smtpPort = smtpPort;
    }

    public static MailService defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public MailService(String emailFrom, String pass) throws IllegalArgumentException, NullPointerException {
        this(emailFrom, pass, DEFAULT_SMTP_PORT);
    }

    public boolean isEmailValid(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public void mailTo(String email, String subject, String message) {
        Logger.debug(String.format("Start sending email '%s' to %s", subject, email));
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.debug", "false");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailFrom, pass);
                    }
                });
        Message simpleMessage = new MimeMessage(session);
        try {
            InternetAddress fromAddress = new InternetAddress(emailFrom);
            InternetAddress toAddress = new InternetAddress(email);
            simpleMessage.setFrom(fromAddress);
            simpleMessage.setRecipient(MimeMessage.RecipientType.TO, toAddress);
            simpleMessage.setSubject(subject);
            simpleMessage.setText(message);
            Transport.send(simpleMessage);
            Logger.info(String.format("Message to %s was send successfully", email));
        } catch (MessagingException e) {
            Logger.error(String.format("Failed to send message to %s because of: %s", email, e));
        }
    }
}
