package org.chous.bets.exception;

public class EmailSendException extends RestException {

    public EmailSendException(String message) {
        super(message, null);
    }
}
