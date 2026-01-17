package com.hrushi.telemetry.web.readings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface FieldType {
    String field();

    PayloadDataType type();

    String[] allowedValues() default {};
}
