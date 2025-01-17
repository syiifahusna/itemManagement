package com.itemManagement.exceptionhandler;

public class ApiNotExistException extends RuntimeException{
    public ApiNotExistException(String message) {
        super(message);
    }
}
