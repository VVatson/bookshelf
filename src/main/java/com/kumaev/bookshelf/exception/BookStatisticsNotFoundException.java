package com.kumaev.bookshelf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookStatisticsNotFoundException extends RuntimeException {

    public BookStatisticsNotFoundException(Long bookId){
        super("The data about book with ID '" + bookId + "' could not be found.");
    }
}