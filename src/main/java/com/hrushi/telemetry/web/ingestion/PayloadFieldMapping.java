package com.hrushi.telemetry.web.ingestion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface PayloadFieldMapping {
    String deviceType();

    FieldType[] fieldTypes();

    enum PayloadDataType {
        FLOAT, STRING
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface FieldType {
        String field();

        PayloadDataType type();

        String[] allowedValues() default {};
    }
}
