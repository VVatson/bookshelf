package com.kumaev.bookshelf.service.admin;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.repository.ReaderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReaderAdminService.class, secure = false)
public class ReaderAdminServiceTest {

    @MockBean
    ReaderRepository readerRepository;

    @MockBean
    OrderAdminService orderAdminService;

    @Autowired
    ReaderAdminService readerAdminService;

    private final static Reader READER = createReader();

    @Test
    public void shouldReturnListOfReaders() {
        //Arrange
        List<Reader> readers = Arrays.asList(READER);
        when(readerRepository.findAll()).thenReturn(readers);

        //Act and Assert
        assertEquals(readers, readerAdminService.getReaders());
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenReaderIdIsNull() {
        //Act
        readerAdminService.checkReaderExistence(null);
    }

    @Test(expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenReaderToBeCheckedIsUnavailable() {
        //Arrange
        when(readerRepository.existsById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act
        readerAdminService.checkReaderExistence(null);
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
