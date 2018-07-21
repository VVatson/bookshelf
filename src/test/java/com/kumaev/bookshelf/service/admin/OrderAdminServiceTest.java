package com.kumaev.bookshelf.service.admin;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.kumaev.bookshelf.exception.CannotBeDeletedException;
import com.kumaev.bookshelf.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@WebMvcTest(value = OrderAdminService.class, secure = false)
public class OrderAdminServiceTest {

    @MockBean
    OrderRepository orderRepository;

    @Autowired
    OrderAdminService orderAdminService;

    @Test(expected = CannotBeDeletedException.class)
    public void shouldThrowExceptionWhenBookToBeCheckedIsUnavailable() {
        //Arrange
        when(orderRepository.findAllByBookId(anyLong()))
                .thenThrow(new CannotBeDeletedException("Test"));

        //Act
        orderAdminService.checkBookAvailable(1L);
    }

    @Test(expected = CannotBeDeletedException.class)
    public void shouldThrowExceptionWhenReaderToBeCheckedIsBorrowing() {
        //Arrange
        when(orderRepository.findAllByReaderId(anyLong()))
                .thenThrow(new CannotBeDeletedException("Test"));

        //Act
        orderAdminService.checkReaderLiability(1L);
    }
}
