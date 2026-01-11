package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UuidValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidUuid {
    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
