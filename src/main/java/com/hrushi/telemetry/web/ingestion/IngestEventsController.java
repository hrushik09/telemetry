package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ingest/events")
class IngestEventsController {
    private static final Logger log = LoggerFactory.getLogger(IngestEventsController.class);

    @PostMapping(version = "1")
    void ingestEvents(@Valid @RequestBody IngestEventRequest request) {
        log.info("Received ingest event request: {}", request);
    }
}
