package com.kumaev.bookshelf.service.impl;

import com.kumaev.bookshelf.exception.BookStatisticsNotFoundException;
import com.kumaev.bookshelf.model.Statistics;
import com.kumaev.bookshelf.repository.StatisticsRepository;
import com.kumaev.bookshelf.service.admin.BookStatisticsAdminService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookStatisticsServiceImpl implements BookStatisticsAdminService {

    private final StatisticsRepository statisticsRepository;

    public BookStatisticsServiceImpl(StatisticsRepository statisticsRepository) {
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public Statistics getBookStatistics(Long bookId) {
        return this.statisticsRepository.findById(bookId)
                .orElseThrow(() -> new BookStatisticsNotFoundException(bookId));
    }

    @Override
    public void deleteBookStatistics(Long bookId) {
        this.statisticsRepository.deleteById(bookId);
    }

    @Override
    public void createBookStatistics(Long bookId) {
        Statistics statistics = new Statistics();
        statistics.setId(bookId);
        this.statisticsRepository.save(statistics);
    }

    @Override
    public void updateBookStatistics(Long bookId, Integer minutes) {
        Statistics bookStatistics = getBookStatistics(bookId);
        long popularity = bookStatistics.getPopularity();
        if (popularity == 0l) {
            bookStatistics.setAverageReadingTime(minutes.longValue());
        } else {
            Long newAverageReadingTime = (bookStatistics.getAverageReadingTime() + minutes.longValue()) / 2;
            bookStatistics.setAverageReadingTime(newAverageReadingTime);
        }
        bookStatistics.setPopularity(++popularity);
        this.statisticsRepository.save(bookStatistics);
    }
}
