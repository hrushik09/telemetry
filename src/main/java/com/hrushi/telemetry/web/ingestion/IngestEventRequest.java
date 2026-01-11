package com.hrushi.telemetry.web.ingestion;

import com.hrushi.telemetry.web.ingestion.PayloadFieldMapping.FieldType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.Map;

import static com.hrushi.telemetry.web.ingestion.PayloadFieldMapping.PayloadDataType.FLOAT;
import static com.hrushi.telemetry.web.ingestion.PayloadFieldMapping.PayloadDataType.STRING;

@ValidPayloadForDeviceType(mappings = {
        @PayloadFieldMapping(deviceType = "temperature", fieldTypes = {
                @FieldType(field = "temperature", type = FLOAT),
                @FieldType(field = "unit", type = STRING, allowedValues = {"celsius", "fahrenheit"})
        }),
        @PayloadFieldMapping(deviceType = "humidity", fieldTypes = {
                @FieldType(field = "humidity", type = FLOAT),
                @FieldType(field = "unit", type = STRING, allowedValues = {"percent"})
        }),
        @PayloadFieldMapping(deviceType = "air_quality", fieldTypes = {
                @FieldType(field = "pm25", type = FLOAT),
                @FieldType(field = "pm10", type = FLOAT),
                @FieldType(field = "co2", type = FLOAT),
                @FieldType(field = "voc", type = FLOAT),
                @FieldType(field = "aqi", type = FLOAT)
        }),
        @PayloadFieldMapping(deviceType = "temperature_humidity", fieldTypes = {
                @FieldType(field = "temperature", type = FLOAT),
                @FieldType(field = "temperatureUnit", type = STRING, allowedValues = {"celsius", "fahrenheit"}),
                @FieldType(field = "humidity", type = FLOAT),
                @FieldType(field = "humidityUnit", type = STRING, allowedValues = {"percent"})
        })
})
record IngestEventRequest(@NotNull @ValidUuid String eventId,
                          @NotNull @Size(min = 1, max = 255) String deviceId,
                          @NotNull @ValidDeviceType(allowed = {"temperature", "humidity", "air_quality", "temperature_humidity"}) String deviceType,
                          @NotNull @ValidIsoDateTime String timestamp,
                          @NotNull Map<String, Object> payload,
                          @NotNull @Valid Metadata metadata,
                          @NotNull @Positive Long sequenceNumber
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
