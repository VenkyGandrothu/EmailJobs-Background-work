package com.example.emailqueue.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.emailqueue.config.RabbitMq;

@Component
public class EmailJobProducer {
    
    private final RabbitTemplate rabbitTemplate;

    public EmailJobProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJob(Long emailJob) {
        rabbitTemplate.convertAndSend(
                RabbitMq.EMAIL_QUEUE,
                emailJob
        );
    }

}
