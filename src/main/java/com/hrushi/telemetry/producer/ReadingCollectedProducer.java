package com.hrushi.telemetry.producer;

import com.hrushi.telemetry.events.ReadingCollectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ReadingCollectedProducer {
    private static final Logger log = LoggerFactory.getLogger(ReadingCollectedProducer.class);
    private final KafkaTemplate<String, ReadingCollectedEvent> readingCollectedKafkaTemplate;

    ReadingCollectedProducer(KafkaTemplate<String, ReadingCollectedEvent> readingCollectedKafkaTemplate) {
        this.readingCollectedKafkaTemplate = readingCollectedKafkaTemplate;
    }

    public void publish(ReadingCollectedEvent readingCollectedEvent) {
        log.debug("publishing event: {}", readingCollectedEvent);
        CompletableFuture<SendResult<String, ReadingCollectedEvent>> future = readingCollectedKafkaTemplate.send("collect-readings-topic", readingCollectedEvent.deviceId(), readingCollectedEvent);
        future.whenCompleteAsync((result, ex) -> {
            if (ex == null) {
                log.debug("event published successfully: deviceId={}, partition={}, offset={}",
                        readingCollectedEvent.deviceId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("failed to publish event: deviceId={}", readingCollectedEvent.deviceId(), ex);
            }
        });
    }
}
