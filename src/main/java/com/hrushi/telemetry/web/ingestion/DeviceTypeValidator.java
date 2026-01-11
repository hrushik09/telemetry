package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

class DeviceTypeValidator implements ConstraintValidator<ValidDeviceType, String> {
    private Set<String> validDeviceTypes;

    @Override
    public void initialize(ValidDeviceType constraintAnnotation) {
        validDeviceTypes = Set.of(constraintAnnotation.allowed());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return validDeviceTypes.contains(value);
    }
}
