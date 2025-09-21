package org.chous.bets.exception;

public class DataNotFoundException extends RestException {

    public DataNotFoundException(String message) {
        super(message, null);
    }

    public DataNotFoundException(String message, String... args) {
        super(message, args);
    }
}
