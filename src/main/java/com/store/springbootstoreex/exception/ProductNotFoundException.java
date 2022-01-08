package com.store.springbootstoreex.exception;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String id) {
        super(String.format("Product with id '%s' not found", id));
    }
}
