package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPayloadForDeviceTypeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidPayloadForDeviceType {
    String message();

    PayloadFieldMapping[] mappings();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
