package com.kumaev.bookshelf.repository;

import com.kumaev.bookshelf.model.Reader;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ReaderRepository extends CrudRepository<Reader, Long> {
    Collection<Reader> findByName(String name);
}
