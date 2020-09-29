package com.somacode.celmybell.config.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException() {
        super("Unauthorized");
    }
}