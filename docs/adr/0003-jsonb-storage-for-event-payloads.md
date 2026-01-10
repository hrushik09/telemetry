# ADR 0003: JSONB storage for event payloads

## Status

Accepted (2026-01-10)

## Context

Similar to the unified schema for event ingestion, we need to decide on how to store event payloads in the database. Two
approaches were considered:

1. Wide table with extracted columns: Common metrics (temperature, humidity, pm25, etc.) will be extracted from the
   event payload and stored in separate columns. The full payload will be stored in a JSONB column.
2. Narrow table with JSONB columns: Minimal columns with full payload stored in JSONB column. Individual metrics will be
   extracted from the JSONB payload on the fly.

## Decision

Use a narrow table with a JSONB column for event payloads.

```postgresql
CREATE TABLE telemetry_events
(
    time            TIMESTAMPTZ NOT NULL,
    received_at     TIMESTAMPTZ NOT NULL,
    device_id       TEXT        NOT NULL,
    device_type     TEXT        NOT NULL,
    event_id        UUID        NOT NULL,
    sequence_number BIGINT      NOT NULL,
    payload         JSONB       NOT NULL,
    metadata        JSONB       NOT NULL,
    UNIQUE (device_id, event_id)
);
```

A GIN index with `jsonb_path_ops` is used for queries.

```postgresql
CREATE INDEX idx_telemetry_payload
    ON telemetry_events USING GIN (payload jsonb_path_ops);
```

## Consequences

**Positive**:

- New sensor types can be added without schema migrations.
- GIN index provides acceptable query performance for containment queries.

**Negative**:

- Slightly more complex SQL with JSONB operators.
- Range queries on payload fields (e.g. `temperature > 20`) cannot use GIN index efficiently.
- May need to extract hot columns later if query performance becomes an issue.
