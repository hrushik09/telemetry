package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Map;

@ValidPayloadForDeviceType(message = "Invalid payload for device type", mappings = {
        @PayloadFieldMapping(deviceType = "temperature", requiredFields = {"temperature", "unit"}),
        @PayloadFieldMapping(deviceType = "humidity", requiredFields = {"humidity", "unit"}),
        @PayloadFieldMapping(deviceType = "air_quality", requiredFields = {"pm25", "pm10", "co2", "voc", "aqi"}),
        @PayloadFieldMapping(deviceType = "temperature_humidity", requiredFields = {"temperature", "temperatureUnit", "humidity", "humidityUnit"})
})
record IngestEventRequest(
        @NotNull(message = "Missing eventId")
        @ValidUuid(message = "Invalid eventId")
        String eventId,
        @NotNull(message = "Missing deviceId")
        @Size(min = 1, max = 255, message = "deviceId should be between 1 and 255 characters")
        String deviceId,
        @NotNull(message = "Missing deviceType")
        @ValidDeviceType(message = "Invalid deviceType", allowedDeviceTypes = {"temperature", "humidity", "air_quality", "temperature_humidity"})
        String deviceType,
        @NotNull(message = "Missing timestamp")
        @ValidIsoDateTime(message = "timestamp should be in ISO 8601 format")
        String timestamp,
        @NotNull(message = "Missing payload")
        Map<String, Object> payload,
        @NotNull(message = "Missing metadata")
        @Valid
        Metadata metadata,
        @NotNull(message = "Missing sequenceNumber")
        @Positive(message = "sequenceNumber should be positive")
        Long sequenceNumber
) {
    record Metadata(
            @NotNull(message = "Missing firmwareVersion")
            @Size(min = 1, max = 255, message = "firmwareVersion should be between 1 and 255 characters")
            String firmwareVersion,
            @NotNull(message = "Missing batteryLevel")
            @Min(value = 0, message = "batteryLevel should be between 0 and 100")
            @Max(value = 100, message = "batteryLevel should be between 0 and 100")
            Integer batteryLevel,
            @NotNull(message = "Missing signalStrength")
            @Min(value = 0, message = "signalStrength should be between 0 and 100")
            @Max(value = 100, message = "signalStrength should be between 0 and 100")
            Integer signalStrength,
            @NotNull(message = "Missing location")
            @Valid
            Location location
    ) {
        record Location(
                @NotNull(message = "Missing latitude")
                @Min(value = -90, message = "latitude should be between -90 and 90")
                @Max(value = 90, message = "latitude should be between -90 and 90")
                Double latitude,
                @NotNull(message = "Missing longitude")
                @Min(value = -180, message = "longitude should be between -180 and 180")
                @Max(value = 180, message = "longitude should be between -180 and 180")
                Double longitude
        ) {
        }
    }
}
