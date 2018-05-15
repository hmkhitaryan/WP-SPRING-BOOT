package com.egs.account.exception;

/**
 * Created by User on 20.09.2016.
 */
public class DocumentNotFoundException extends RuntimeException {
    private String documentID;

    public DocumentNotFoundException(String documentID) {
        this.documentID = documentID;
    }
}
