package com.kumaev.bookshelf.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumaev.bookshelf.model.Order;
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.exception.DuplicateIdException;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.service.BookshelfService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookshelfController.class)
public class BookshelfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookshelfService bookshelfService;

    private ObjectMapper mapper = new ObjectMapper();

    private final static String BOOKSHELF_READERS_PATH = "/bookshelf/readers";
    private final static String BOOKSHELF_BOOKS_PATH = "/bookshelf/books";
    private final static String BOOKSHELF_BORROW_PATH = "/bookshelf/borrow";
    private final static String BOOKSHELF_RETURN_PATH = "/bookshelf/return/1";

    @Test
    public void shouldReturn200HttpStatusCodeWhenGetAllBooks() throws Exception {
        //Arrange
        List<Book> books = getBooks();
        given(bookshelfService.getBooks()).willReturn(books);

        //Act and Assert
        mockMvc.perform(get(BOOKSHELF_BOOKS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(books),false));
    }

    @Test
    public void shouldReturn200HttpStatusCodeWhenGetAllReaders() throws Exception {
        //Arrange
        List<Reader> readers = getReaders();
        given(bookshelfService.getReaders()).willReturn(readers);

        //Act and Assert
        mockMvc.perform(get(BOOKSHELF_READERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(readers),false));
    }

    @Test
    public void shouldReturn200HttpStatusCodeWhenBookWasBorrowed() throws Exception {
        //Arrange
        Order order = getOrder();
        MockHttpServletRequestBuilder request = createBorrowRequest();
        when(bookshelfService.borrowBook(any(Order.class))).thenReturn(order);

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("bookId", is(order.getBookId().intValue())))
                .andExpect(jsonPath("readerId", is(order.getReaderId().intValue())));
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenBookToBeBorrowedIsNotPresent() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder request = createBorrowRequest();
        when(bookshelfService.borrowBook(any(Order.class)))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenReaderToBeBorrowingIsNotPresent() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder request = createBorrowRequest();
        when(bookshelfService.borrowBook(any(Order.class)))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn409HttpStatusCodeWhenBookIdToBeBorrowedIsNotAvailable() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder request = createBorrowRequest();
        when(bookshelfService.borrowBook(any(Order.class)))
                .thenThrow(new DuplicateIdException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenOrderToBeFinishingIsNotPresent() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder request = createReturnRequest();
        when(bookshelfService.returnBook(anyLong()))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturn200HttpStatusCodeWhenBookWasReturned() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder request = createReturnRequest();
        when(bookshelfService.returnBook(anyLong())).thenReturn(getCompletedOrder());

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("complete", is(getCompletedOrder().isComplete())));
    }

    private MockHttpServletRequestBuilder createBorrowRequest() throws JsonProcessingException {
        String jsonInString = mapper.writeValueAsString(getOrder());
        return post(BOOKSHELF_BORROW_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonInString);
    }

    private MockHttpServletRequestBuilder createReturnRequest() {
        return post(BOOKSHELF_RETURN_PATH)
                .param("orderId", "1");
    }

    private Order getOrder() {
        return new Order()
                .id(1L)
                .bookId(2L)
                .readerId(3L)
                .timeOrder(123456L)
                .complete(false);
    }

    private Order getCompletedOrder() {
        return getOrder()
                .complete(true);
    }

    private List<Book> getBooks() {
        Book book1 = new Book()
                .id(1L)
                .author("Dostoevsky")
                .name("Idiot")
                .status(Book.StatusEnum.AVAILABLE)
                .year(1868L);

        Book book2 = new Book()
                .id(2L)
                .author("Dostoevsky")
                .name("Crime and Punishment")
                .status(Book.StatusEnum.AVAILABLE)
                .year(1868L);

        return Arrays.asList(book1, book2);
    }

    private List<Reader> getReaders() {
        Reader reader1 = new Reader()
                .id(1L)
                .age(15)
                .name("Mike")
                .email("mike@java.ru")
                .phone("23-45");

        Reader reader2 = new Reader()
                .id(2L)
                .age(16)
                .name("Bob")
                .email("bob@java.ru")
                .phone("24-45");

        return Arrays.asList(reader1, reader2);
    }
}
