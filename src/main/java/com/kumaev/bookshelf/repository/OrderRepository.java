package com.kumaev.bookshelf.repository;

import com.kumaev.bookshelf.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    Iterable<Order> findAllByBookId(Long bookId);

    Iterable<Order> findAllByReaderId(Long readerId);
}
