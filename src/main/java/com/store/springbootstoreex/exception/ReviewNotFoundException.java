package com.store.springbootstoreex.exception;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super(String.format("Comment with id '%s' not found", id));
    }
}
