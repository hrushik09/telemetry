package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsoDateTimeValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidIsoDateTime {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
