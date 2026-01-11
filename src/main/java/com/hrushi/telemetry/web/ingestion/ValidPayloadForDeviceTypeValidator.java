package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class ValidPayloadForDeviceTypeValidator implements ConstraintValidator<ValidPayloadForDeviceType, IngestEventRequest> {
    private Map<String, Set<String>> requiredFieldsByDeviceType;

    @Override
    public void initialize(ValidPayloadForDeviceType constraintAnnotation) {
        this.requiredFieldsByDeviceType = Stream.of(constraintAnnotation.mappings())
                .collect(Collectors.toMap(
                        PayloadFieldMapping::deviceType,
                        mapping -> Set.of(mapping.requiredFields())
                ));
    }

    @Override
    public boolean isValid(IngestEventRequest value, ConstraintValidatorContext context) {
        if (value == null || value.payload() == null || value.deviceType() == null) {
            return true;
        }
        Set<String> requiredFields = requiredFieldsByDeviceType.get(value.deviceType());
        if (requiredFields == null) {
            return true;
        }
        boolean valid = true;
        Map<String, Object> payload = value.payload();
        for (String field : requiredFields) {
            if (!payload.containsKey(field) || payload.get(field) == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Missing required field '" + field + "' in payload for device type '" + value.deviceType() + "'")
                        .addPropertyNode("payload")
                        .addConstraintViolation();
                valid = false;
            }
        }
        return valid;
    }
}
