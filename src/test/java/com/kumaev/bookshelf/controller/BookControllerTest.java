package com.kumaev.bookshelf.controller;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.model.Statistics;
import com.kumaev.bookshelf.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collections;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private ObjectMapper mapper = new ObjectMapper();

    private final static String BOOK_PATH = "/book";
    private final static String BOOK_STATISTICS_PATH = "/book/1/statistics";
    private final static String BOOK_WITH_ID_PATH = "/book/1";
    private final static String BOOK_FIND_BY_AUTHOR_PATH = "/book/findByAuthor";
    private final static String BOOK_FIND_BY_NAME_PATH = "/book/findByName";

    private final static Book BOOK = createBook();

    @Test
    public void ShouldReturn200HttpStatusCodeWhenPayloadIsValidAndNewBookIsAdded() throws Exception {
        //Arrange
        doNothing().when(bookService).addBook(any(Book.class));

        //Act and Assert
        mockMvc.perform(createAddBookRequest()).andExpect(status().isCreated());
    }

    @Test
    public void ShouldReturn200HttpStatusCodeWhenGettingBookById() throws Exception {
        //Arrange
        when(bookService.getBook(anyLong())).thenReturn(BOOK);

        //Act and Assert
        mockMvc.perform(createGetBookRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(BOOK.getId().intValue())))
                .andExpect(jsonPath("author", is(BOOK.getAuthor())))
                .andExpect(jsonPath("name", is(BOOK.getName())))
                .andExpect(jsonPath("status", is(BOOK.getStatus().name())))
                .andExpect(jsonPath("year", is(BOOK.getYear().intValue())));
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenBookToBeGettedIsNotPresent() throws Exception {
        //Arrange
        MockHttpServletRequestBuilder request = createGetBookRequest();

        when(bookService.getBook(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn202HttpStatusCodeWhenUpdateBook() throws Exception {
        //Arrange
        doNothing().when(bookService).updateBook(anyLong(), any(Book.class));

        //Act and Assert
        mockMvc.perform(createUpdateBookRequest())
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenBookToBeUpdatedIsNotPresent() throws Exception {
        //Arrange
        doThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L))
                .when(bookService).updateBook(anyLong(), any(Book.class));

        //Act and Assert
        mockMvc.perform(createUpdateBookRequest())
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn204HttpStatusCodeWhenDeleteBook() throws Exception {
        //Arrange
        doNothing().when(bookService).deleteBook(anyLong());

        //Act and Assert
        mockMvc.perform(createDeleteBookRequest())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenBookToBeDeletedIsNotPresent() throws Exception {
        //Arrange
        doThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L))
                .when(bookService).deleteBook(anyLong());

        //Act and Assert
        mockMvc.perform(createDeleteBookRequest())
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn200HttpStatusCodeWhenFindingBookByName() throws Exception {
        //Arrange
        when(bookService.getBookByName(anyString())).thenReturn(BOOK);

        //Act and Assert
        mockMvc.perform(createFindBookByNameRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(BOOK.getId().intValue())))
                .andExpect(jsonPath("author", is(BOOK.getAuthor())))
                .andExpect(jsonPath("name", is(BOOK.getName())))
                .andExpect(jsonPath("status", is(BOOK.getStatus().name())))
                .andExpect(jsonPath("year", is(BOOK.getYear().intValue())));
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenFindingBookByNameIsNotPresent() throws Exception {
        //Arrange
        doThrow(new ObjectNotFoundException(Book.class.getSimpleName(), "Dostoevsky"))
                .when(bookService).getBookByName(anyString());

        //Act and Assert
        mockMvc.perform(createFindBookByNameRequest())
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn200HttpStatusCodeWhenFindingBooksByAuthor() throws Exception {
        //Arrange
        when(bookService.getBookByAuthor(anyString())).thenReturn(Collections.singletonList(BOOK));

        //Act and Assert
        mockMvc.perform(createFindBooksByAuthorRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(BOOK.getId().intValue())))
                .andExpect(jsonPath("$[0].author", is(BOOK.getAuthor())))
                .andExpect(jsonPath("$[0].name", is(BOOK.getName())))
                .andExpect(jsonPath("$[0].status", is(BOOK.getStatus().name())))
                .andExpect(jsonPath("$[0].year", is(BOOK.getYear().intValue())));
    }

    @Test
    public void ShouldReturn200HttpStatusCodeWhenGettingBookStatisticsById() throws Exception {
        //Arrange
        when(bookService.getBookStatistics(anyLong())).thenReturn(createBookStatistics());

        //Act and Assert
        mockMvc.perform(createGetBookStatisticsRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(BOOK.getId().intValue())))
                .andExpect(jsonPath("popularity", is(0)))
                .andExpect(jsonPath("averageReadingTime", is(0)));
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenBookStatisticsToBeGettedIsNotPresent() throws Exception {
        //Arrange
        when(bookService.getBookStatistics(anyLong()))
                .thenThrow(new ObjectNotFoundException(Book.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(createGetBookStatisticsRequest())
                .andExpect(status().isNotFound());
    }

    private MockHttpServletRequestBuilder createAddBookRequest() throws JsonProcessingException {
        String jsonInString = mapper.writeValueAsString(BOOK);
        return post(BOOK_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonInString);
    }

    private MockHttpServletRequestBuilder createUpdateBookRequest() throws JsonProcessingException {
        String jsonInString = mapper.writeValueAsString(BOOK);
        return put(BOOK_WITH_ID_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonInString);
    }

    private MockHttpServletRequestBuilder createDeleteBookRequest() {
        return delete(BOOK_WITH_ID_PATH);
    }

    private MockHttpServletRequestBuilder createFindBookByNameRequest() {
        return get(BOOK_FIND_BY_NAME_PATH)
                .param("name", "Idiot");
    }

    private MockHttpServletRequestBuilder createFindBooksByAuthorRequest() {
        return get(BOOK_FIND_BY_AUTHOR_PATH)
                .param("author", "Dostoevsky");
    }

    private MockHttpServletRequestBuilder createGetBookStatisticsRequest() {
        return get(BOOK_STATISTICS_PATH);
    }

    private MockHttpServletRequestBuilder createGetBookRequest() {
        return get(BOOK_WITH_ID_PATH);
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
        return new Statistics().id(1L);
    }
}
