package com.jolupbisang.demo.global.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // 상수 정의 (오타 방지 및 재사용)
    public static final String SEGMENT_EXCHANGE = "segment.exchange";
    public static final String SEGMENT_QUEUE = "segment.queue";
    public static final String SEGMENT_ROUTING_KEY = "segment.create";

    // Dead Letter Queue 설정 (처리 실패 메시지 이동)
    public static final String DEAD_LETTER_EXCHANGE = "dead.order.exchange";
    public static final String DEAD_LETTER_QUEUE = "dead.order.queue";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.order.create";


    // 1. Exchange (메시지 라우팅 역할) 생성
    @Bean
    public Exchange orderExchange() {
        return new TopicExchange(SEGMENT_EXCHANGE, true, false); // durable: true
    }

    // 2. Queue (메시지 저장소) 생성
    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(SEGMENT_QUEUE) // durable: true
                .withArgument("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE) // 처리 실패 시 이동할 DLX
                .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY) // DLX로 보낼 때 사용할 라우팅 키
                .build();
    }

    // 3. Binding (Exchange와 Queue 연결)
    @Bean
    public Binding orderBinding(Queue orderQueue, Exchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with(SEGMENT_ROUTING_KEY)
                .noargs();
    }

    // --- Dead Letter Queue 관련 설정 ---
    @Bean
    public Exchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, Exchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DEAD_LETTER_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
