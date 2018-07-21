package com.kumaev.bookshelf.service.admin;

import com.kumaev.bookshelf.model.Statistics;

public interface BookStatisticsAdminService {
    Statistics getBookStatistics(Long bookId);

    void deleteBookStatistics(Long bookId);

    void createBookStatistics(Long book);

    void updateBookStatistics(Long bookId, Integer minutes);
}
