# Smart Environment Monitoring System

A telemetry system for monitoring temperature, humidity, and air quality from sensors in a smart environment such as a
home, office, or industrial facility.

## Overview of the proposed system:

- Collect readings from sensors
- Process readings asynchronously
- Store processed data in a TimescaleDB database
- Visualize data in a dashboard

## Tech Stack

- Java 25
- Spring Boot with Spring MVC
- TimescaleDB
- Docker for containerization
- Kafka for async processing