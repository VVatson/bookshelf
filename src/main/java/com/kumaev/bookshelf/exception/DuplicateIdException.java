package com.kumaev.bookshelf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateIdException extends RuntimeException {

    public DuplicateIdException(String object, Long id){
        super("The " + object + " ID '" + id + "' is already in use.");
    }
}