package com.hrushi.telemetry.web.ingestion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface PayloadFieldMapping {
    String deviceType();

    String[] requiredFields();
}
