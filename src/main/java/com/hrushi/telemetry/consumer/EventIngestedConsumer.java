package com.hrushi.telemetry.consumer;

import com.hrushi.telemetry.events.EventIngested;
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
class EventIngestedConsumer {
    private static final Logger log = LoggerFactory.getLogger(EventIngestedConsumer.class);

    @RetryableTopic(
            attempts = "4",
            backOff = @BackOff(delay = 1000, multiplier = 2, maxDelay = 10000),
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            include = RetryableException.class
    )
    @KafkaListener(
            topics = "event-ingestion-topic",
            groupId = "event-ingestion-group",
            containerFactory = "eventIngestedKafkaListenerContainerFactory"
    )
    void consume(ConsumerRecord<String, EventIngested> record) {
        EventIngested eventIngested = record.value();
        log.debug("received event: {}", eventIngested);
        processEvent(eventIngested);
        log.debug("event processed successfully");
    }

    @DltHandler
    void handleDlt(ConsumerRecord<String, EventIngested> record, @Header(KafkaHeaders.EXCEPTION_MESSAGE) String exceptionMessage) {
        log.error("Event exhausted retries: deviceId={}, partition={}, offset={}, exceptionMessage={}",
                record.value().deviceId(), record.partition(), record.offset(), exceptionMessage);
    }

    private void processEvent(EventIngested eventIngested) {
        log.debug("processing event: {}", eventIngested);
    }
}
