package com.kumaev.bookshelf.service.admin;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.exception.UnavailableBookException;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookAdminService.class, secure = false)
public class BookAdminServiceTest {

    @MockBean
    BookRepository bookRepository;

    @MockBean
    BookStatisticsAdminService bookStatisticsAdminService;

    @MockBean
    OrderAdminService orderAdminService;

    @Autowired
    BookAdminService bookAdminService;

    private final static Book BOOK = createBook();

    @Test
    public void shouldReturnListOfBooks() {
        //Arrange
        List<Book> books = Arrays.asList(BOOK);
        when(bookRepository.findAll()).thenReturn(books);

        //Act and Assert
        assertEquals(books, bookAdminService.getBooks());
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeCheckedByIdNotPresent() {
        //Arrange
        when(bookRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        bookAdminService.checkBookAvailable(1L);
    }

    @Test (expected = UnavailableBookException.class)
    public void shouldThrowExceptionWhenBookToBeCheckedByIdIsUnavailable() {
        //Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(BOOK.status(Book.StatusEnum.UNAVAILABLE)));

        //Act and Assert
        bookAdminService.checkBookAvailable(1L);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeCheckedByIdIsNull() {

        //Act and Assert
        bookAdminService.checkBookExistence(null);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenBookToBeCheckedOnExistenceByIdNotPresent() {
        //Arrange
        when(bookRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        bookAdminService.checkBookExistence(1L);
    }

    private static Book createBook() {
        return new Book()
                .id(1L)
                .author("Dostoevsky")
                .name("Idiot")
                .status(Book.StatusEnum.AVAILABLE)
                .year(1868L);
    }
}
