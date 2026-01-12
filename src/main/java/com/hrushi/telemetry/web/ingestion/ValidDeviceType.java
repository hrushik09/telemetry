package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DeviceTypeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidDeviceType {
    String message() default "must be a valid device type: {allowed}";

    String[] allowed();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
