package com.store.springbootstoreex.exception;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(Long id) {
        super(String.format("Cart with id '%s' not found", id));
    }
}
