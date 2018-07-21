package com.kumaev.bookshelf.service;

import com.kumaev.bookshelf.model.Reader;

public interface ReaderService {

    void addReader(Reader reader);

    void updateReader(Long readerId, Reader reader);

    Reader getReader(Long readerId);

    void deleteReader(Long readerId);

    Reader getReaderByName(String name);
}
