package com.kumaev.bookshelf.event;

import java.io.Serializable;

public class BookshelfEvent implements Serializable {

    private String reader;
    private String book;
    private OrderType type;

    public enum OrderType { BORROWING, RETURNING }

    // Default constructor is needed to deserialize JSON
    public BookshelfEvent() {
    }

    public BookshelfEvent(String reader, String book, OrderType type) {
        this.reader = reader;
        this.book = book;
        this.type = type;
    }

    public String getReader() {
        return this.reader;
    }

    public String getBook() {
        return this.book;
    }

    public OrderType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return "BookshelfEvent { " +
                " reader = '" + reader + '\'' +
                ", type = " + type +
                ", book = " + book +
                '}';
    }
}