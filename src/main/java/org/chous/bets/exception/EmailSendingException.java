package org.chous.bets.exception;

public class EmailSendingException extends RestException {

    public EmailSendingException(String message) {
        super(message, null);
    }
}