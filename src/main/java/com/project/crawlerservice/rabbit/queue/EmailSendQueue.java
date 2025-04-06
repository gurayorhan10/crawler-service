package com.project.crawlerservice.rabbit.queue;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class EmailSendQueue {

    public static final String QUEUE_NAME = "q.email-send";

    @Bean
    public Queue emailSendQueueCreated() {
        return new Queue(QUEUE_NAME, true);
    }

}
