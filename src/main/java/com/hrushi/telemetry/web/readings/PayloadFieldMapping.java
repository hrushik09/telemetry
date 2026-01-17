package com.hrushi.telemetry.web.readings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface PayloadFieldMapping {
    String deviceType();

    FieldType[] fieldTypes();
}
