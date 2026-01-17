package com.hrushi.telemetry.web.readings;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsoDateTimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidIsoDateTime {
    String message() default "must be a valid ISO 8601 date time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
