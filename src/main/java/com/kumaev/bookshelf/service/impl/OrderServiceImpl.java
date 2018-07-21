package com.kumaev.bookshelf.service.impl;

import com.kumaev.bookshelf.exception.CannotBeDeletedException;
import com.kumaev.bookshelf.repository.OrderRepository;
import com.kumaev.bookshelf.service.admin.OrderAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderAdminService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @Override
    public void checkReaderLiability(Long readerId) {
        if (this.orderRepository.findAllByReaderId(readerId).iterator().hasNext()) {
            throw new CannotBeDeletedException("Reader " + readerId +
                    " cannot be deleted because he has books that need to be returned");
        }
    }

    @Override
    public void checkBookAvailable(Long bookId) {
        if (this.orderRepository.findAllByBookId(bookId).iterator().hasNext()) {
            throw new CannotBeDeletedException("Book " + bookId +
                    " cannot be deleted because it was borrowed by a reader.");
        }
    }
}
