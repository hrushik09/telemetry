package com.hrushi.telemetry.consumer;

import com.hrushi.telemetry.events.ReadingCollected;
import com.hrushi.telemetry.exception.RetryableException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.BackOff;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
class ReadingCollectedConsumer {
    private static final Logger log = LoggerFactory.getLogger(ReadingCollectedConsumer.class);

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(delay = 1000, multiplier = 2, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            include = RetryableException.class
    )
    @KafkaListener(
            topics = "collect-readings-topic",
            groupId = "collect-readings-group",
            containerFactory = "readingCollectedKafkaListenerContainerFactory"
    )
    void consume(ConsumerRecord<String, ReadingCollected> record) {
        ReadingCollected readingCollected = record.value();
        log.debug("received event: {}", readingCollected);
        processEvent(readingCollected);
        log.debug("event processed successfully");
    }

    @DltHandler
    void handleDlt(ConsumerRecord<String, ReadingCollected> record, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage) {
        log.error("Event exhausted retries: deviceId={}, partition={}, offset={}, exceptionMessage={}",
                record.value().deviceId(), record.partition(), record.offset(), exceptionMessage);
    }

    private void processEvent(ReadingCollected readingCollected) {
        log.debug("processing event: {}", readingCollected);
    }
}
