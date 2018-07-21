package com.kumaev.bookshelf.service.admin;

public interface OrderAdminService {

    void checkReaderLiability(Long readerId);

    void checkBookAvailable(Long bookId);
}
