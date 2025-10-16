package com.jolupbisang.demo.application.segment.command;

import com.jolupbisang.demo.application.segment.event.CompletedSegmentReceivedEvent;
import com.jolupbisang.demo.global.config.RabbitMQConfig;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SegmentCreationEventListener {

    private final SegmentCreationService segmentCreationService;

    @RabbitListener(queues = RabbitMQConfig.SEGMENT_QUEUE)
    public void receiveSingleSegment(CompletedSegmentReceivedEvent message,
                                     Channel channel,
                                     @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        try {
            segmentCreationService.saveAll(message.meetingId(), message.completed());
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("Failed to process segment. DTO: {}. Error: {}", message, e.getMessage());
        }
    }

}
