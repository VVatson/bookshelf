package com.kumaev.bookshelf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String object, Long id){
        super("Could not find " + object + " with ID '" + id + "'.");
    }

    public ObjectNotFoundException(String object, String name){
        super("The " + object + " '" + name + "' could not be found.");
    }
}