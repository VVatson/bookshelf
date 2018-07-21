package com.kumaev.bookshelf.service.admin;

import com.kumaev.bookshelf.model.Book;

import java.util.List;

public interface BookAdminService {

    void checkBookAvailable(Long bookId);

    void checkBookExistence(Long bookId);

    List<Book> getBooks();

    void borrowBook(Long bookId);

    void returnBook(Long bookId);
}
