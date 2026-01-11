package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class PayloadForDeviceTypeValidator implements ConstraintValidator<ValidPayloadForDeviceType, IngestEventRequest> {
    private Map<String, Set<String>> requiredFieldsByDeviceType;
    private Map<String, Map<String, FieldType>> fieldTypesByDeviceType;

    @Override
    public void initialize(ValidPayloadForDeviceType constraintAnnotation) {
        this.requiredFieldsByDeviceType = Stream.of(constraintAnnotation.mappings())
                .collect(Collectors.toMap(
                        PayloadFieldMapping::deviceType,
                        mapping -> Arrays.stream(mapping.fieldTypes())
                                .map(FieldType::field)
                                .collect(Collectors.toSet())
                ));

        this.fieldTypesByDeviceType = Stream.of(constraintAnnotation.mappings())
                .collect(Collectors.toMap(
                        PayloadFieldMapping::deviceType,
                        mapping -> Stream.of(mapping.fieldTypes())
                                .collect(Collectors.toMap(
                                        FieldType::field,
                                        fieldType -> fieldType
                                ))
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

        // check required fields presence
        for (String field : requiredFields) {
            if (!payload.containsKey(field) || payload.get(field) == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Missing required field '" + field + "' in payload for device type '" + value.deviceType() + "'")
                        .addPropertyNode("payload")
                        .addPropertyNode(field)
                        .addConstraintViolation();
                valid = false;
            }
        }

        // check field data types
        Map<String, FieldType> fieldTypes = fieldTypesByDeviceType.get(value.deviceType());
        if (fieldTypes != null) {
            for (Map.Entry<String, Object> entry : payload.entrySet()) {
                String field = entry.getKey();
                Object fieldValue = entry.getValue();
                FieldType fieldType = fieldTypes.get(field);
                if (fieldType != null && fieldValue != null) {
                    if (!isValidType(fieldValue, fieldType)) {
                        context.disableDefaultConstraintViolation();
                        String message = "Field '" + field + "' must be of type " + fieldType.type() +
                                         (fieldType.allowedValues().length > 0
                                                 ? " with allowed values: " + String.join(", ", fieldType.allowedValues())
                                                 : "");
                        context.buildConstraintViolationWithTemplate(message)
                                .addPropertyNode("payload")
                                .addPropertyNode(field)
                                .addConstraintViolation();
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    private boolean isValidType(Object fieldValue, FieldType fieldType) {
        return switch (fieldType.type()) {
            case FLOAT -> fieldValue instanceof Double || fieldValue instanceof Integer || fieldValue instanceof Long;
            case STRING -> {
                if (!(fieldValue instanceof String)) {
                    yield false;
                }
                String[] allowed = fieldType.allowedValues();
                if (allowed.length == 0) {
                    yield true;
                }
                yield Set.of(allowed).contains(fieldValue);
            }
        };
    }
}
