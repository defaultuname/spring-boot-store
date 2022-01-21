package com.store.springbootstoreex.exception;

public class CategoryIsNotEmptyException extends RuntimeException {
    public CategoryIsNotEmptyException(Long id) {
        super(String.format("Some products are in category with id '%s'! Please change them before deleting.", id));
    }
}
