package com.jolupbisang.demo.application.segment;

import com.jolupbisang.demo.application.segment.dto.SegmentMessage;
import com.jolupbisang.demo.global.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegmentSaver {
    private final SegmentService segmentService;

    @RabbitListener(queues = RabbitMQConfig.SEGMENT_QUEUE)
    public void receiveSingleSegment(
            SegmentMessage message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag
    ) throws IOException {
        try {
            segmentService.saveCompletedSegment(message);
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("Failed to process segment. DTO: {}. Error: {}", message, e.getMessage());
            channel.basicNack(deliveryTag, false, false);
        }
    }
}
