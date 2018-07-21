package com.kumaev.bookshelf.exception;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BookshelfControllerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors objectNotFoundExceptionHandler(ObjectNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ExceptionHandler(BookStatisticsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors bookStatisticsNotFoundExceptionHandler(BookStatisticsNotFoundException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ExceptionHandler(DuplicateIdException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors duplicateIdExceptionHandler(DuplicateIdException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ExceptionHandler(UnavailableBookException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors unavailableBookExceptionHandler(UnavailableBookException ex) {
        return new VndErrors("error", ex.getMessage());
    }

    @ExceptionHandler(CannotBeDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors objectCannotBeDeletedExceptionHandler(CannotBeDeletedException ex) {
        return new VndErrors("error", ex.getMessage());
    }

}
