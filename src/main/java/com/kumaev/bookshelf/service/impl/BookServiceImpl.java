package com.kumaev.bookshelf.service.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.kumaev.bookshelf.exception.DuplicateIdException;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.exception.UnavailableBookException;
import com.kumaev.bookshelf.repository.BookRepository;
import com.kumaev.bookshelf.service.admin.BookStatisticsAdminService;
import com.kumaev.bookshelf.service.admin.OrderAdminService;
import com.kumaev.bookshelf.exception.*;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.model.Statistics;
import com.kumaev.bookshelf.service.BookService;
import com.kumaev.bookshelf.service.admin.BookAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class BookServiceImpl implements BookService, BookAdminService {

    private final BookRepository bookRepository;

    private final BookStatisticsAdminService bookStatisticsAdminService;

    private final OrderAdminService orderAdminService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookStatisticsAdminService bookStatisticsAdminService,
                           OrderAdminService orderAdminService) {
        this.bookRepository = bookRepository;
        this.bookStatisticsAdminService = bookStatisticsAdminService;
        this.orderAdminService = orderAdminService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBook(Book book) {
        checkBookIdDuplicate(book.getId());
        Book savedBook = this.bookRepository.save(book);
        this.bookStatisticsAdminService.createBookStatistics(savedBook.getId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Book getBook(Long bookId) {
        return this.bookRepository.findById(bookId)
                .orElseThrow(() -> new ObjectNotFoundException(Book.class.getSimpleName(), bookId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBook(Long bookId, Book body) {
        checkBookExistence(bookId);
        body.setId(bookId);             //ignores attempts to change the ID
        this.bookRepository.save(body);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBook(Long bookId) {
        orderAdminService.checkBookAvailable(bookId);
        checkBookExistence(bookId);
        this.bookStatisticsAdminService.deleteBookStatistics(bookId);
        this.bookRepository.deleteById(bookId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Book getBookByName(String name) {
        return this.bookRepository.findByName(name).stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(Book.class.getSimpleName(), name));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Book> getBookByAuthor(String author) {
        Collection<Book> books = this.bookRepository.findByAuthor(author);
        return Lists.newArrayList(books);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Statistics getBookStatistics(Long bookId) {
        return this.bookStatisticsAdminService.getBookStatistics(bookId);
    }

    public void checkBookAvailable(Long bookId) {
        Book book = getBook(bookId);
        if (book.getStatus().equals(Book.StatusEnum.UNAVAILABLE)) {
            throw new UnavailableBookException(bookId);
        }
    }

    public List<Book> getBooks() {
        Iterable<Book> iterable = bookRepository.findAll();
        List<Book> books =  Lists.newArrayList(iterable);
        return books;
    }

    public void borrowBook(Long bookId) {
        Book book = getBook(bookId);
        book.setStatus(Book.StatusEnum.UNAVAILABLE);
        bookRepository.save(book);
    }

    public void returnBook(Long bookId) {
        Book book = getBook(bookId);
        book.setStatus(Book.StatusEnum.AVAILABLE);
        bookRepository.save(book);
    }

    public void checkBookExistence(Long bookId) {
        if (bookId == null || !this.bookRepository.existsById(bookId)) {
            throw new ObjectNotFoundException(Book.class.getSimpleName(), bookId);
        }
    }

    protected void checkBookIdDuplicate(Long bookId) {
        if (bookId != null && this.bookRepository.existsById(bookId)) {
            throw new DuplicateIdException(Book.class.getSimpleName(), bookId);
        }
    }
}
