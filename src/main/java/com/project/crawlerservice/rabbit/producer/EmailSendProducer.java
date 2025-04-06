package com.project.crawlerservice.rabbit.producer;

import com.project.crawlerservice.rabbit.data.EmailSendMessage;
import com.project.crawlerservice.rabbit.queue.EmailSendQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class EmailSendProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(EmailSendMessage message){
        if(Objects.nonNull(message) && Objects.nonNull(message.getTo()) && Objects.nonNull(message.getTitle())
                && Objects.nonNull(message.getContent()) && Objects.nonNull(message.getSimple())){
            rabbitTemplate.convertAndSend(EmailSendQueue.QUEUE_NAME, message);
        }
    }

}
