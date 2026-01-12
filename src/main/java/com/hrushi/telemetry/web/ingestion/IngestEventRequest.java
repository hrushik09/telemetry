package com.hrushi.telemetry.web.ingestion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Map;

@ValidPayloadForDeviceType(mappings = {
        @PayloadFieldMapping(deviceType = "temperature", fieldTypes = {
                @FieldType(field = "temperature", type = PayloadDataType.FLOAT),
                @FieldType(field = "unit", type = PayloadDataType.STRING, allowedValues = {"celsius", "fahrenheit"})
        }),
        @PayloadFieldMapping(deviceType = "humidity", fieldTypes = {
                @FieldType(field = "humidity", type = PayloadDataType.FLOAT),
                @FieldType(field = "unit", type = PayloadDataType.STRING, allowedValues = {"percent"})
        }),
        @PayloadFieldMapping(deviceType = "air_quality", fieldTypes = {
                @FieldType(field = "pm25", type = PayloadDataType.FLOAT),
                @FieldType(field = "pm10", type = PayloadDataType.FLOAT),
                @FieldType(field = "co2", type = PayloadDataType.FLOAT),
                @FieldType(field = "voc", type = PayloadDataType.FLOAT),
                @FieldType(field = "aqi", type = PayloadDataType.FLOAT)
        }),
        @PayloadFieldMapping(deviceType = "temperature_humidity", fieldTypes = {
                @FieldType(field = "temperature", type = PayloadDataType.FLOAT),
                @FieldType(field = "temperatureUnit", type = PayloadDataType.STRING, allowedValues = {"celsius", "fahrenheit"}),
                @FieldType(field = "humidity", type = PayloadDataType.FLOAT),
                @FieldType(field = "humidityUnit", type = PayloadDataType.STRING, allowedValues = {"percent"})
        })
})
record IngestEventRequest(@NotNull @ValidUuid String eventId,
                          @NotNull @Positive Long sequenceNumber,
                          @NotNull @ValidIsoDateTime String timestamp,
                          @NotNull @Size(min = 1, max = 255) String deviceId,
                          @NotNull @ValidDeviceType(allowed = {"temperature", "humidity", "air_quality", "temperature_humidity"}) String deviceType,
                          @NotNull Map<String, Object> payload,
                          @NotNull @Valid Metadata metadata
) {
    record Metadata(@NotNull @Size(min = 1, max = 255) String firmwareVersion,
                    @NotNull @Min(value = 0) @Max(value = 100) Integer batteryLevel,
                    @NotNull @Min(value = 0) @Max(value = 100) Integer signalStrength,
                    @NotNull @Valid Location location
    ) {
        record Location(@NotNull @Min(value = -90) @Max(value = 90) Double latitude,
                        @NotNull @Min(value = -180) @Max(value = 180) Double longitude) {
        }
    }
}
