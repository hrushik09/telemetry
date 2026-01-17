package com.hrushi.telemetry.consumer;

import com.hrushi.telemetry.events.EventIngested;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
class EventIngestedConsumer {
    private static final Logger log = LoggerFactory.getLogger(EventIngestedConsumer.class);

    @KafkaListener(topics = "event-ingestion-topic", groupId = "event-ingestion-group", containerFactory = "eventIngestedKafkaListenerContainerFactory")
    void consume(ConsumerRecord<String, EventIngested> record, Acknowledgment acknowledgment) {
        try {
            EventIngested eventIngested = record.value();
            log.debug("received event: {}", eventIngested);
            processEvent(eventIngested);
            acknowledgment.acknowledge();
            log.debug("event processed successfully");
        } catch (Exception e) {
            log.error("failed to process event from partition={}, offset={}", record.partition(), record.offset(), e);
        }
    }

    private void processEvent(EventIngested eventIngested) {
        log.debug("processing event: {}", eventIngested);
    }
}
