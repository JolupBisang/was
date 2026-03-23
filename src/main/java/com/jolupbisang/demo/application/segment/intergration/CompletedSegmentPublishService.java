package com.jolupbisang.demo.application.segment.intergration;

import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.global.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompletedSegmentPublishService {

    private final RabbitTemplate rabbitTemplate;

    @EventListener
    public void publish(CompletedSegmentReceivedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SEGMENT_EXCHANGE,
                RabbitMQConfig.SEGMENT_ROUTING_KEY,
                event,
                message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                },
                new CorrelationData(UUID.randomUUID().toString())
        );
    }
}
