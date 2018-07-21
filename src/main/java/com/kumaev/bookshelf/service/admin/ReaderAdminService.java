package com.kumaev.bookshelf.service.admin;

import com.kumaev.bookshelf.model.Reader;

import java.util.List;

public interface ReaderAdminService {

    void checkReaderExistence(Long readerId);

    List<Reader> getReaders();
}
