package org.chous.bets.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.chous.bets.exception.EmailSendException;
import org.chous.bets.service.MailService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Service
@Profile("local")
public class LocalMailServiceImpl implements MailService {

    @Override
    @Profile("local")
    public void send(String to, String subject, String text) {

        to = "kidminsk@yandex.ru";
        String from = "kidimpminsk@gmail.com";
        final String username = "kidimpminsk";
        final String password = "jgpdnhecvehsuwdm";

        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            message.setSubject(subject, "UTF-8");

            message.setText(text, "UTF-8");

            Transport.send(message);

            log.info("Message was successfully sent.");

        } catch (MessagingException e) {
            throw new EmailSendException("Не удалось отправить письмо: " + e.getMessage());
        }
    }
}
