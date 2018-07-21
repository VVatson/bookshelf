package com.kumaev.bookshelf.event;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookshelfEventDispatcher {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendEvent(BookshelfEvent event){
        rabbitTemplate.convertAndSend(RabbitConfiguration.BOOKSHELF_MESSAGE_QUEUE, event);
    }
}
