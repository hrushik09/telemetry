# ADR 0002: Unified Event Schema for Sensors

## Status

Accepted (2026-01-10)

## Context

The telemetry system needs to ingest data from multiple sensor types: temperature, humidity, and air quality. We need to
decide whether to use:

1. Unified schema: Single event structure for all sensor types with flexible `payload` field that varies by the sensor
   type.
2. Sensor-specific schema: Separate event structures for each sensor type. This might potentially need separate API
   endpoints too.

## Decision

Use a unified event schema for all sensor types with the following structure:

```json
{
  "eventId": "UUID",
  "deviceId": "string",
  "deviceType": "string",
  "timestamp": "ISO-8601",
  "payload": {},
  "metadata": {
    "firmwareVersion": "string",
    "batteryLevel": "integer",
    "signalStrength": "integer",
    "location": {
       "latitude": "float",
       "longitude": "float"
    }
  },
  "sequenceNumber": "long"
}
```

The `payload` field contains sensor-specific data and is validated based on `device_type`:

| device_type            | payload fields                                               |
|------------------------|--------------------------------------------------------------|
| `temperature`          | `temperature`, `unit`                                        |
| `humidity`             | `humidity`, `unit`                                           |
| `air_quality`          | `pm25`, `pm10`, `co2`, `voc`, `aqi`                          |
| `temperature_humidity` | `temperature`, `temperatureUnit`, `humidity`, `humidityUnit` |

## Consequences

**Positive**:

- Simple API and client implementation
- New sensor types can be added without api changes

**Negative**:

- Payload validation requires sensor-specific checks based on `device_type`