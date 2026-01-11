package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.constraints.*;

import java.util.Map;

record IngestEventRequest(
        @NotNull(message = "Missing eventId")
        @ValidUuid(message = "Invalid eventId")
        String eventId,
        @NotNull(message = "Missing deviceId")
        @Size(min = 1, max = 255, message = "Invalid length for deviceId")
        String deviceId,
        @NotNull(message = "Missing deviceType")
        @ValidDeviceType(message = "Invalid deviceType", allowedDeviceTypes = {"temperature", "humidity", "air_quality", "temperature_humidity"})
        String deviceType,
        @NotNull(message = "Missing timestamp")
        @ValidIsoDateTime(message = "Invalid timestamp")
        String timestamp,
        @NotNull(message = "Missing payload")
        Map<String, Object> payload,
        @NotNull(message = "Missing metadata")
        Metadata metadata,
        @NotNull(message = "Missing sequenceNumber")
        @Positive(message = "sequenceNumber must be positive")
        Long sequenceNumber
) {
    record Metadata(
            @NotNull(message = "Missing firmwareVersion")
            @Size(min = 1, max = 255, message = "Invalid length for firmwareVersion")
            String firmwareVersion,
            @NotNull(message = "Missing batteryLevel")
            @Min(value = 0, message = "batteryLevel must be non-negative")
            @Max(value = 100, message = "batteryLevel must be at most 100")
            Integer batteryLevel,
            @NotNull(message = "Missing signalStrength")
            @Min(value = 0, message = "signalStrength must be non-negative")
            @Max(value = 100, message = "signalStrength must be at most 100")
            Integer signalStrength,
            @NotNull(message = "Missing location")
            Metadata.Location location
    ) {
        record Location(
                @NotNull(message = "Missing latitude")
                @Min(value = -90, message = "Invalid latitude")
                @Max(value = 90, message = "Invalid latitude")
                Double latitude,
                @NotNull(message = "Missing longitude")
                @Min(value = -180, message = "Invalid longitude")
                @Max(value = 180, message = "Invalid longitude")
                Double longitude
        ) {
        }
    }
}
