package com.kumaev.bookshelf.service.impl;

import com.kumaev.bookshelf.model.Order;
import com.kumaev.bookshelf.model.Reader;
import com.kumaev.bookshelf.exception.DuplicateIdException;
import com.kumaev.bookshelf.exception.ObjectNotFoundException;
import com.kumaev.bookshelf.model.Book;
import com.kumaev.bookshelf.event.BookshelfEvent;
import com.kumaev.bookshelf.event.BookshelfEventDispatcher;
import com.kumaev.bookshelf.repository.OrderRepository;
import com.kumaev.bookshelf.service.BookService;
import com.kumaev.bookshelf.service.BookshelfService;
import com.kumaev.bookshelf.service.ReaderService;
import com.kumaev.bookshelf.service.admin.BookAdminService;
import com.kumaev.bookshelf.service.admin.BookStatisticsAdminService;
import com.kumaev.bookshelf.service.admin.ReaderAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

import static com.kumaev.bookshelf.event.BookshelfEvent.OrderType.BORROWING;
import static com.kumaev.bookshelf.event.BookshelfEvent.OrderType.RETURNING;

@Service
public class BookshelfServiceImpl implements BookshelfService {

    private final OrderRepository orderRepository;

    private final BookAdminService bookAdminService;

    private final BookStatisticsAdminService bookStatisticsAdminService;

    private final ReaderAdminService readerAdminService;

    private final BookshelfEventDispatcher bookshelfEventDispatcher;

    private final BookService bookService;

    private final ReaderService readerService;

    @Autowired
    public BookshelfServiceImpl(BookAdminService bookAdminService, OrderRepository orderRepository,
                                BookStatisticsAdminService bookStatisticsAdminService, ReaderAdminService readerAdminService,
                                final BookshelfEventDispatcher bookshelfEventDispatcher, BookService bookService, ReaderService readerService) {
        this.bookAdminService = bookAdminService;
        this.orderRepository = orderRepository;
        this.bookStatisticsAdminService = bookStatisticsAdminService;
        this.readerAdminService = readerAdminService;
        this.bookshelfEventDispatcher = bookshelfEventDispatcher;
        this.bookService = bookService;
        this.readerService = readerService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Book> getBooks() {
        return bookAdminService.getBooks();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<Reader> getReaders() {
        return readerAdminService.getReaders();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Order borrowBook(Order order) {
        checkOrderIdDuplicate(order.getId());
        order.setTimeOrder(System.currentTimeMillis());
        order.setComplete(false);
        bookAdminService.checkBookAvailable(order.getBookId());
        readerAdminService.checkReaderExistence(order.getReaderId());
        bookAdminService.borrowBook(order.getBookId());
        Order response = orderRepository.save(order);
        BookshelfEvent event = createEvent(BORROWING, order);
        bookshelfEventDispatcher.sendEvent(event);
        return response;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Order returnBook(Long orderId) {
        Order order = getOrder(orderId);
        Integer minutes = calculateReadingTime(order);
        bookStatisticsAdminService.updateBookStatistics(order.getBookId(), minutes);
        bookAdminService.returnBook(order.getBookId());
        orderRepository.deleteById(orderId);
        order.setComplete(true);
        BookshelfEvent event = createEvent(RETURNING, order);
        bookshelfEventDispatcher.sendEvent(event);
        return order;
    }

    protected Order getOrder(Long orderId) {
        return this.orderRepository.findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException(Order.class.getSimpleName(), orderId));
    }

    protected void checkOrderIdDuplicate(Long orderId) {
        if (orderId != null && this.orderRepository.existsById(orderId)) {
            throw new DuplicateIdException(Order.class.getSimpleName(), orderId);
        }
    }

    protected BookshelfEvent createEvent(BookshelfEvent.OrderType type, Order order) {
        Book book = bookService.getBook(order.getBookId());
        Reader reader = readerService.getReader(order.getReaderId());
        return new BookshelfEvent(reader.getName(), book.getName(), type);
    }

    private Integer calculateReadingTime(final Order order) {
        Long time = System.currentTimeMillis();
        Long totalTime = time - order.getTimeOrder();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(totalTime);
        return cal.get(Calendar.MINUTE);
    }
}
