package com.orion.mdd.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE)
public class UserFoundException extends Exception {
    
    public UserFoundException(String message) {
        super(message);
    }
    
}
