package org.chous.bets.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestException extends RuntimeException {

    private final String message;
    private final Object[] args;
}
