package com.hrushi.telemetry.producer;

import com.hrushi.telemetry.events.EventIngested;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
class EventIngestedProducer {
    private static final Logger log = LoggerFactory.getLogger(EventIngestedProducer.class);
    private final KafkaTemplate<String, EventIngested> kafkaTemplate;

    EventIngestedProducer(KafkaTemplate<String, EventIngested> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    void publish(EventIngested eventIngested) {
        log.debug("publishing event: {}", eventIngested);
        CompletableFuture<SendResult<String, EventIngested>> future = kafkaTemplate.send("event-ingestion-topic", eventIngested.deviceId(), eventIngested);
        future.whenCompleteAsync((result, ex) -> {
            if (ex == null) {
                log.debug("event published successfully: deviceId={}, partition={}, offset={}",
                        eventIngested.deviceId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("failed to publish event: deviceId={}", eventIngested.deviceId(), ex);
            }
        });
    }
}
