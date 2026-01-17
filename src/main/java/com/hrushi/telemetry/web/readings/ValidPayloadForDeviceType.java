package com.hrushi.telemetry.web.readings;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PayloadForDeviceTypeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidPayloadForDeviceType {
    String message() default "must be a valid payload for device type";

    PayloadFieldMapping[] mappings();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
