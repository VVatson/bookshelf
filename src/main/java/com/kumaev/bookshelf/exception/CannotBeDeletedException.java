package com.kumaev.bookshelf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CannotBeDeletedException extends RuntimeException {

    public CannotBeDeletedException(String message){
        super(message);
    }
}