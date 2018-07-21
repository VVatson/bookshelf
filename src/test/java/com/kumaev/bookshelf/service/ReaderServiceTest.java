package com.kumaev.bookshelf.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.exception.CannotBeDeletedException;
import com.kumaev.bookshelf.exception.DuplicateIdException;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.repository.ReaderRepository;
import com.kumaev.bookshelf.service.admin.OrderAdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ReaderService.class, secure = false)
public class ReaderServiceTest {

    @MockBean
    ReaderRepository readerRepository;

    @MockBean
    OrderAdminService orderAdminService;

    @Autowired
    ReaderService readerService;

    private final static Reader READER = createReader();

    @Test(expected = DuplicateIdException.class)
    public void shouldThrowExceptionWhenReaderToBeAddedHasDuplicatingId() {
        //Arrange
        when(readerRepository.existsById(anyLong())).thenReturn(true);

        //Act and Assert
        readerService.addReader(READER);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenReaderToBeGettedNotPresent() {
        //Arrange
        when(readerRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act and Assert
        readerService.getReader(1L);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenReaderToBeUpdatedNotPresent() {
        //Arrange
        when(readerRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act and Assert
        readerService.updateReader(1L, READER);
    }

    @Test (expected = ObjectNotFoundException.class)
    public void shouldThrowExceptionWhenReaderToBeDeletedNotPresent() {
        //Arrange
        when(readerRepository.findById(anyLong()))
                .thenThrow(new ObjectNotFoundException(Reader.class.getSimpleName(), 1L));

        //Act and Assert
        readerService.deleteReader(1L);
    }

    @Test (expected = CannotBeDeletedException.class)
    public void shouldThrowExceptionWhenReaderToBeDeletedIsBorrowing() {
        //Arrange
        doThrow(new CannotBeDeletedException("Test"))
                .when(orderAdminService).checkReaderLiability(anyLong());

        //Act and Assert
        readerService.deleteReader(1L);
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
