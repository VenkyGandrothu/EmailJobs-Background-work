package com.example.emailqueue.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMq {

    // Name of the queue (mailbox) — reuse this constant when publishing / listening
    public static final String EMAIL_QUEUE = "email.queue";

    @Bean
    public Queue emailQueue() {
        // durable = queue survives RabbitMQ restarts
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }
}
