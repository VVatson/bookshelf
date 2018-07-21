package com.kumaev.bookshelf.controller;

import com.kumaev.bookshelf.api.BookshelfApi;
import com.kumaev.bookshelf.model.Order;
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.service.BookshelfService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class BookshelfController implements BookshelfApi {

    private final BookshelfService bookshelfService;

    @Autowired
    public BookshelfController(final BookshelfService bookshelfService) {
        this.bookshelfService = bookshelfService;
    }

    @Override
    public ResponseEntity<List<Book>> getBooks() {
        List<Book> books = bookshelfService.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Reader>> getReaders() {
        List<Reader> readers = bookshelfService.getReaders();
        return new ResponseEntity<>(readers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> borrowBook(
            @ApiParam(value = "order placed for borrowing the book", required = true)
            @Valid @RequestBody Order body) {
        Order order = bookshelfService.borrowBook(body);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Order> returnBook(
            @NotNull @ApiParam(value = "Order id to return book to the self", required = true)
            @PathVariable("orderId") Long orderId) {
        Order order = bookshelfService.returnBook(orderId);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
