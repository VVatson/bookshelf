package com.kumaev.bookshelf.repository;

import com.kumaev.bookshelf.model.Statistics;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends CrudRepository<Statistics, Long> {
}
