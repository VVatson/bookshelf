package com.kumaev.bookshelf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UnavailableBookException extends RuntimeException {

    public UnavailableBookException(Long id){
        super("The book with ID '" + id + "' was borrowed by another reader.");
    }
}