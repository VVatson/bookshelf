package com.kumaev.bookshelf.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.model.Order;
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.event.BookshelfEventDispatcher;
import com.kumaev.bookshelf.repository.OrderRepository;
import com.kumaev.bookshelf.service.admin.BookAdminService;
import com.kumaev.bookshelf.service.admin.BookStatisticsAdminService;
import com.kumaev.bookshelf.service.admin.ReaderAdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookshelfService.class, secure = false)
public class BookshelfServiceTest {

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    BookAdminService bookAdminService;

    @MockBean
    BookStatisticsAdminService bookStatisticsAdminService;

    @MockBean
    ReaderAdminService readerAdminService;

    @MockBean
    BookshelfEventDispatcher bookshelfEventDispatcher;

    @MockBean
    BookService bookService;

    @MockBean
    ReaderService readerService;

    @Autowired
    BookshelfService bookshelfService;

    private final static Reader READER = createReader();
    private final static Book BOOK = createBook();

    @Test
    public void shouldReturnListOfBooks() {
        //Arrange
        List<Book> books = Arrays.asList(BOOK);
        when(bookAdminService.getBooks()).thenReturn(books);

        //Act and Assert
        assertEquals(books, bookshelfService.getBooks());
    }

    @Test
    public void shouldReturnListOfReaders() {
        //Arrange
        List<Reader> readers = Arrays.asList(READER);
        when(readerAdminService.getReaders()).thenReturn(readers);

        //Act and Assert
        assertEquals(readers, bookshelfService.getReaders());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenOrderIdNotPresent() {
        //Arrange
        when(orderRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Order.class.getSimpleName(), 1L));

        //Act and Assert
        bookshelfService.returnBook(1L);
    }

    private static Reader createReader() {
        return new Reader()
                .id(1L)
                .name("Bob")
                .email("bob@java.com")
                .phone("22-14")
                .age(18);
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
