package com.kumaev.bookshelf.service.admin;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.exception.BookStatisticsNotFoundException;
import com.kumaev.bookshelf.model.Statistics;
import com.kumaev.bookshelf.repository.StatisticsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookStatisticsAdminService.class, secure = false)
public class BookStatisticsAdminServiceTest {

    @MockBean
    StatisticsRepository statisticsRepository;

    @Autowired
    BookStatisticsAdminService bookStatisticsAdminService;

    private final static Statistics BOOK_STATISTICS = createBookStatistics();

    @Test
    public void shouldReturnBooksStatistics() {
        //Arrange
        when(statisticsRepository.findById(anyLong())).thenReturn(Optional.of(BOOK_STATISTICS));

        //Act and Assert
        assertEquals(BOOK_STATISTICS, bookStatisticsAdminService.getBookStatistics(1L));
    }

    @Test (expected = BookStatisticsNotFoundException.class)
    public void shouldThrowExceptionWhenBookStatisticsNotPresent() {
        //Arrange
        when(statisticsRepository.findById(anyLong()))
                .thenThrow(new BookStatisticsNotFoundException(1L));

        //Act and Assert
        bookStatisticsAdminService.getBookStatistics(1L);
    }

    @Test (expected = BookStatisticsNotFoundException.class)
    public void shouldThrowExceptionWhenBookStatisticsToBeUpdatedNotPresent() {
        //Arrange
        when(statisticsRepository.findById(anyLong()))
                .thenThrow(new BookStatisticsNotFoundException(1L));

        //Act and Assert
        bookStatisticsAdminService.updateBookStatistics(1L, 5);
    }

    private static Statistics createBookStatistics() {
        return new Statistics()
                .id(1L);
    }
}
