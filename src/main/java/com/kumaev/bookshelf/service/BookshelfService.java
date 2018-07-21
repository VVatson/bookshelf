package com.kumaev.bookshelf.service;

import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.model.Order;
import com.kumaev.bookshelf.model.Reader;

import java.util.List;

public interface BookshelfService {
    List<Book> getBooks();

    List<Reader> getReaders();

    Order borrowBook(Order body);

    Order returnBook(Long orderId);
}
