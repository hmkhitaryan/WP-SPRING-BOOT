package com.egs.account.exception;

/**
 * Created by User on 11.09.2016.
 */
public class UserNotFoundException extends RuntimeException {

    private String userId;

    public UserNotFoundException() { }

    public UserNotFoundException(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
