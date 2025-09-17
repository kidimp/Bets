package org.chous.bets.service;

public interface MailServiceAPI {

    void send(String to, String subject, String text);
}
