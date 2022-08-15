package com.defuname.springbootstoreex.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(String.format("User with id '%s' not found", id));
    }
}
