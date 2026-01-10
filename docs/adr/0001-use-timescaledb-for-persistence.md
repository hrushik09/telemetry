# ADR 0001: Use TimescaleDB for persistence

## Status

Accepted (2026-10-01)

## Context

We are using postgresql as the primary database. As most of our use cases are time-series-based, we should consider
using TimescaleDB as a replacement. It provides HyperTable support out of the box.

## Decision

Use TimescaleDB for persistence.

## Consequences

**Positive**

- Familiar postgresql syntax.
- Automatic partitioning and improved query performance for time-series data.
- Native compression for older data.

**Negative**

- Additional operational complexity (managing chunk sizes, retention policies)
- Team will need to learn TimescaleDB-specific concepts (hypertables, continuous aggregates)
- Potential vendor lock-in for TimescaleDB-specific features
