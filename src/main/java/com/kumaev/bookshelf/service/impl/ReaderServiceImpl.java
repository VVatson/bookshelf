package com.kumaev.bookshelf.service.impl;

import com.google.common.collect.Lists;
import com.kumaev.bookshelf.exception.DuplicateIdException;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.repository.ReaderRepository;
import com.kumaev.bookshelf.service.admin.OrderAdminService;
import com.kumaev.bookshelf.service.ReaderService;
import com.kumaev.bookshelf.service.admin.ReaderAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService, ReaderAdminService {

    private final ReaderRepository readerRepository;

    private final OrderAdminService orderAdminService;

    @Autowired
    public ReaderServiceImpl(ReaderRepository readerRepository, OrderAdminService orderAdminService) {
        this.readerRepository = readerRepository;
        this.orderAdminService = orderAdminService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addReader(Reader reader) {
        checkReaderIdDuplicate(reader.getId());
        this.readerRepository.save(reader);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateReader(Long readerId, Reader reader) {
        checkReaderExistence(readerId);
        reader.setId(readerId);             //ignores attempts to change the ID
        this.readerRepository.save(reader);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Reader getReader(Long readerId) {
        return this.readerRepository.findById(readerId)
                .orElseThrow(() -> new ObjectNotFoundException(Reader.class.getSimpleName(), readerId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteReader(Long readerId) {
        orderAdminService.checkReaderLiability(readerId);
        checkReaderExistence(readerId);
        this.readerRepository.deleteById(readerId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Reader getReaderByName(String name) {
        return this.readerRepository.findByName(name).stream()
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(Reader.class.getSimpleName(), name));
    }

    public void checkReaderExistence(Long readerId) {
        if (readerId == null || !this.readerRepository.existsById(readerId)) {
            throw new ObjectNotFoundException(Reader.class.getSimpleName(), readerId);
        }
    }

    @Override
    public List<Reader> getReaders() {
        Iterable<Reader> iterable = readerRepository.findAll();
        List<Reader> readers = Lists.newArrayList(iterable);
        return readers;
    }

    private void checkReaderIdDuplicate(Long readerId) {
        if (readerId != null && this.readerRepository.existsById(readerId)) {
            throw new DuplicateIdException(Reader.class.getSimpleName(), readerId);
        }
    }
}
