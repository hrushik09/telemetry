#!/bin/bash
echo "Cleaning up development environment..."
docker compose -f deployment/docker-compose/infra.yaml down -v --remove-orphans
echo "Done!"