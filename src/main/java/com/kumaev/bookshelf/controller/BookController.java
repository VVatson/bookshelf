package com.kumaev.bookshelf.controller;

import com.kumaev.bookshelf.api.BookApi;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.model.Statistics;
import com.kumaev.bookshelf.service.BookService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class BookController implements BookApi {

    private final BookService bookService;

    @Autowired
    public BookController(final BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public ResponseEntity<Void> addBook(
            @ApiParam(value = "Book object that needs to be added to the shelf", required = true)
            @Valid @RequestBody Book book) {
        bookService.addBook(book);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Book> getBookById(
            @ApiParam(value = "ID of book to return", required=true) @PathVariable("bookId") Long bookId) {
        Book book = bookService.getBook(bookId);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateBook(
            @ApiParam(value = "ID of book that needs to be updated", required = true)
            @PathVariable("bookId") Long bookId,
            @ApiParam(value = "Book object that needs to be added to the shelf", required = true)
            @Valid @RequestBody Book body) {
        bookService.updateBook(bookId, body);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Void> deleteBook(
            @ApiParam(value = "Book id to delete", required=true)
            @PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Book> findBookByName(
            @NotNull @ApiParam(value = "Name to filter by", required = true)
            @Valid @RequestParam(value = "name") String name) {
        Book book = bookService.getBookByName(name);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Book>> findBooksByAuthor(
            @NotNull @ApiParam(value = "Author values that need to be considered for filter", required = true)
            @Valid @RequestParam(value = "author") String author) {
        List<Book> books = bookService.getBookByAuthor(author);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Statistics> getBookStatisticsById(
            @ApiParam(value = "ID of book to return",required=true) @PathVariable("bookId") Long bookId) {
        Statistics statistics = bookService.getBookStatistics(bookId);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
