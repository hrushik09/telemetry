package com.hrushi.telemetry.producer;

import com.hrushi.telemetry.events.ReadingCollected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ReadingCollectedProducer {
    private static final Logger log = LoggerFactory.getLogger(ReadingCollectedProducer.class);
    private final KafkaTemplate<String, ReadingCollected> readingCollectedKafkaTemplate;

    ReadingCollectedProducer(KafkaTemplate<String, ReadingCollected> readingCollectedKafkaTemplate) {
        this.readingCollectedKafkaTemplate = readingCollectedKafkaTemplate;
    }

    public void publish(ReadingCollected readingCollected) {
        log.debug("publishing event: {}", readingCollected);
        CompletableFuture<SendResult<String, ReadingCollected>> future = readingCollectedKafkaTemplate.send("collect-readings-topic", readingCollected.deviceId(), readingCollected);
        future.whenCompleteAsync((result, ex) -> {
            if (ex == null) {
                log.debug("event published successfully: deviceId={}, partition={}, offset={}",
                        readingCollected.deviceId(), result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("failed to publish event: deviceId={}", readingCollected.deviceId(), ex);
            }
        });
    }
}
