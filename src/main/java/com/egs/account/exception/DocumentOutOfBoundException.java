package com.egs.account.exception;

public class DocumentOutOfBoundException extends RuntimeException {
    private String message;

    public DocumentOutOfBoundException(String message) {
        super(message);
    }
}
