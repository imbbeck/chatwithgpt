package com.nbs.nbs.exception;

public class IdAlreadyExistsException extends RuntimeException{
    public IdAlreadyExistsException(String message) {
        super(message);
    }
}
