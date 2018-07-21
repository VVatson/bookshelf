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
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.service.ReaderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@WebMvcTest(ReaderController.class)
public class ReaderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReaderService readerService;

    private ObjectMapper mapper = new ObjectMapper();

    private final static String READER_PATH = "/reader";
    private final static String READER_WITH_ID_PATH = "/reader/1";
    private final static String READER_FIND_BY_NAME_PATH = "/reader/findByName";

    private final static Reader READER = createReader();


    @Test
    public void ShouldReturn200HttpStatusCodeWhenPayloadIsValidAndNewReaderIsAdded() throws Exception {
        //Arrange
        doNothing().when(readerService).addReader(any(Reader.class));

        //Act and Assert
        mockMvc.perform(createAddReaderRequest()).andExpect(status().isCreated());
    }

    @Test
    public void ShouldReturn200HttpStatusCodeWhenGettingReaderById() throws Exception {
        //Arrange
        when(readerService.getReader(anyLong())).thenReturn(READER);

        //Act and Assert
        mockMvc.perform(createGetReaderRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(READER.getId().intValue())))
                .andExpect(jsonPath("phone", is(READER.getPhone())))
                .andExpect(jsonPath("name", is(READER.getName())))
                .andExpect(jsonPath("email", is(READER.getEmail())))
                .andExpect(jsonPath("age", is(READER.getAge().intValue())));
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenReaderToBeGettedIsNotPresent() throws Exception {
        //Arrange
        when(readerService.getReader(anyLong()))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act and Assert
        mockMvc.perform(createGetReaderRequest())
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn202HttpStatusCodeWhenUpdateReader() throws Exception {
        //Arrange
        doNothing().when(readerService).updateReader(anyLong(), any(Reader.class));

        //Act and Assert
        mockMvc.perform(createUpdateReaderRequest())
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenReaderToBeUpdatedIsNotPresent() throws Exception {
        //Arrange
        doThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L))
                .when(readerService).updateReader(anyLong(), any(Reader.class));

        //Act and Assert
        mockMvc.perform(createUpdateReaderRequest())
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn204HttpStatusCodeWhenDeleteReader() throws Exception {
        //Arrange
        doNothing().when(readerService).deleteReader(anyLong());

        //Act and Assert
        mockMvc.perform(createDeleteReaderRequest())
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenReaderToBeDeletedIsNotPresent() throws Exception {
        //Arrange
        doThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L))
                .when(readerService).deleteReader(anyLong());

        //Act and Assert
        mockMvc.perform(createDeleteReaderRequest())
                .andExpect(status().isNotFound());
    }

    @Test
    public void ShouldReturn200HttpStatusCodeWhenFindingReaderByName() throws Exception {
        //Arrange
        when(readerService.getReaderByName(anyString())).thenReturn(READER);

        //Act and Assert
        mockMvc.perform(createFindReaderByNameRequest())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(READER.getId().intValue())))
                .andExpect(jsonPath("phone", is(READER.getPhone())))
                .andExpect(jsonPath("name", is(READER.getName())))
                .andExpect(jsonPath("email", is(READER.getEmail())))
                .andExpect(jsonPath("age", is(READER.getAge().intValue())));
    }

    @Test
    public void shouldReturn404HttpStatusCodeWhenFindingReaderByNameIsNotPresent() throws Exception {
        //Arrange
        doThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), "Bob"))
                .when(readerService).getReaderByName(anyString());

        //Act and Assert
        mockMvc.perform(createFindReaderByNameRequest())
                .andExpect(status().isNotFound());
    }

    private MockHttpServletRequestBuilder createAddReaderRequest() throws JsonProcessingException {
        String jsonInString = mapper.writeValueAsString(READER);
        return post(READER_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonInString);
    }

    private MockHttpServletRequestBuilder createUpdateReaderRequest() throws JsonProcessingException {
        String jsonInString = mapper.writeValueAsString(READER);
        return put(READER_WITH_ID_PATH)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(jsonInString);
    }

    private MockHttpServletRequestBuilder createDeleteReaderRequest() {
        return delete(READER_WITH_ID_PATH);
    }

    private MockHttpServletRequestBuilder createFindReaderByNameRequest() {
        return get(READER_FIND_BY_NAME_PATH)
                .param("name", "Bob");
    }

    private MockHttpServletRequestBuilder createGetReaderRequest() {
        return get(READER_WITH_ID_PATH);
    }

    private static Reader createReader() {
        return new Reader()
                .id(1L)
                .name("Bob")
                .email("bob@java.com")
                .phone("22-14")
                .age(18);
    }
}
