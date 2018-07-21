package com.kumaev.bookshelf.service;

import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.model.Statistics;

import java.util.List;

public interface BookService {
    void addBook(Book book);

    Book getBook(Long bookId);

    void updateBook(Long bookId, Book body);

    void deleteBook(Long bookId);

    Book getBookByName(String name);

    List<Book> getBookByAuthor(String author);

    Statistics getBookStatistics(Long bookId);
}
