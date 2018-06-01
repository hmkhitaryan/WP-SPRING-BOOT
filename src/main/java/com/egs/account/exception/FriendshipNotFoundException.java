package com.egs.account.exception;

public class FriendshipNotFoundException extends RuntimeException {
    private String message;

    public FriendshipNotFoundException(String message) {
        this.message = message;
    }
}