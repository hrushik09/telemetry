package com.hrushi.telemetry.web.readings;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

class IsoDateTimeValidator implements ConstraintValidator<ValidIsoDateTime, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
