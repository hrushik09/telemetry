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
        @NotNull
        @ValidUuid
        String eventId,
        @NotNull
        @Size(min = 1, max = 255)
        String deviceId,
        @NotNull
        @ValidDeviceType(allowed = {"temperature", "humidity", "air_quality", "temperature_humidity"})
        String deviceType,
        @NotNull
        @ValidIsoDateTime
        String timestamp,
        @NotNull
        Map<String, Object> payload,
        @NotNull
        @Valid
        Metadata metadata,
        @NotNull
        @Positive
        Long sequenceNumber
) {
    record Metadata(
            @NotNull
            @Size(min = 1, max = 255)
            String firmwareVersion,
            @NotNull
            @Min(value = 0)
            @Max(value = 100)
            Integer batteryLevel,
            @NotNull
            @Min(value = 0)
            @Max(value = 100)
            Integer signalStrength,
            @NotNull
            @Valid
            Location location
    ) {
        record Location(
                @NotNull
                @Min(value = -90)
                @Max(value = 90)
                Double latitude,
                @NotNull
                @Min(value = -180)
                @Max(value = 180)
                Double longitude
        ) {
        }
    }
}
