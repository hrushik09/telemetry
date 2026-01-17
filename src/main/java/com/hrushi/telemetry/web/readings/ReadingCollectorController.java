package com.hrushi.telemetry.web.readings;

import com.hrushi.telemetry.events.ReadingCollectedEvent;
import com.hrushi.telemetry.producer.ReadingCollectedProducer;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/readings")
class ReadingCollectorController {
    private static final Logger log = LoggerFactory.getLogger(ReadingCollectorController.class);
    private final ReadingCollectedProducer readingCollectedProducer;

    ReadingCollectorController(ReadingCollectedProducer readingCollectedProducer) {
        this.readingCollectedProducer = readingCollectedProducer;
    }

    @PostMapping(version = "1")
    void collectReading(@Valid @RequestBody SensorReadingRequest request) {
        log.info("received collect reading request: {}", request);
        readingCollectedProducer.publish(new ReadingCollectedEvent(request.deviceId()));
    }
}
