package com.kumaev.bookshelf.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.exception.BookStatisticsNotFoundException;
import com.kumaev.bookshelf.exception.CannotBeDeletedException;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.model.Statistics;
import com.kumaev.bookshelf.event.BookshelfEventDispatcher;
import com.kumaev.bookshelf.repository.BookRepository;
import com.kumaev.bookshelf.service.admin.BookStatisticsAdminService;
import com.kumaev.bookshelf.service.admin.OrderAdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookService.class, secure = false)
public class BookServiceTest {

    @MockBean
    BookRepository bookRepository;

    @MockBean
    BookStatisticsAdminService bookStatisticsAdminService;

    @MockBean
    OrderAdminService orderAdminService;

    @MockBean
    BookshelfEventDispatcher bookshelfEventDispatcher;

    @Autowired
    BookService bookService;

    private final static Book BOOK = createBook();

    @Test
    public void shouldReturnBookById() {
        //Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(BOOK));

        //Act and Assert
        assertEquals(BOOK, bookService.getBook(1L));
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeGettedNotPresent() {
        //Arrange
        when(bookRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        bookService.getBook(1L);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeGettedByNameNotPresent() {
        //Arrange
        when(bookRepository.findByName(anyString()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), "Crime and Punishment"));

        //Act and Assert
        bookService.getBookByName("Crime and Punishment");
    }

    @Test
    public void shouldReturnEmptyCollectionWhenBooksToBeGettedByAuthorAreNotPresent() {
        //Arrange
        when(bookRepository.findByAuthor(anyString())).thenReturn(Collections.emptyList());

        //Act and Assert
        assertEquals(0, bookService.getBookByAuthor("Dostoevsky").size());
    }

    @Test
    public void shouldReturnListOfBooksByAuthor() {
        //Arrange
        when(bookRepository.findByAuthor(anyString())).thenReturn(Collections.singletonList(BOOK));

        //Act and Assert
        assertEquals(1, bookService.getBookByAuthor("Dostoevsky").size());
        assertEquals(BOOK, bookService.getBookByAuthor("Dostoevsky").get(0));
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeUpdatedNotPresent() {
        //Arrange
        when(bookRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        bookService.updateBook(1L, BOOK);
    }

    @Test
    public void shouldReturnBookStatisticsByBookId() {
        //Arrange
        when(bookStatisticsAdminService.getBookStatistics(anyLong())).thenReturn(createBookStatistics());

        //Act and Assert
        assertEquals(createBookStatistics(), bookService.getBookStatistics(1L));
    }

    @Test(expected = BookStatisticsNotFoundException.class)
    public void shouldThrowExceptionWhenBookStatisticsToBeGettedNotPresent() {
        //Arrange
        when(bookStatisticsAdminService.getBookStatistics(anyLong()))
                .thenThrow(new BookStatisticsNotFoundException(1L));

        //Act
        bookService.getBookStatistics(1L);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeDeletedNotPresent() {
        //Arrange
        when(bookRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        bookService.deleteBook(1L);
    }

    @Test (expected = CannotBeDeletedException.class)
    public void shouldThrowExceptionWhenBookToBeDeletedIsUnavailable() {
        //Arrange
        doThrow(new CannotBeDeletedException("Test"))
                .when(orderAdminService).checkBookAvailable(anyLong());

        //Act and Assert
        bookService.deleteBook(1L);
    }

    private static Book createBook() {
        return new Book()
                .id(1L)
                .author("Dostoevsky")
                .name("Idiot")
                .status(Book.StatusEnum.AVAILABLE)
                .year(1868L);
    }

    private static Statistics createBookStatistics() {
        return new Statistics()
                .id(1L);
    }
}
