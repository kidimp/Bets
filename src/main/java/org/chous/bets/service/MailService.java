package org.chous.bets.service;

public interface MailService {

    void send(String to, String subject, String text);
}
