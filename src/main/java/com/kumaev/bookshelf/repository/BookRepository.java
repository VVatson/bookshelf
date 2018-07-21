package com.kumaev.bookshelf.repository;

import com.kumaev.bookshelf.model.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Collection<Book> findByName(String name);

    Collection<Book> findByAuthor(String author);
}
