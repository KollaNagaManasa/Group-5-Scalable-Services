# Customer Service

Spring Boot microservice for customer profiles and KYC status. Owns **`customer_db`** / `customers` only.

## Run locally

```bash
export DB_URL=jdbc:postgresql://localhost:5433/customer_db
export DB_USER=customer
export DB_PASSWORD=customer
mvn spring-boot-run
```

- Port: **8081** (override with `SERVER_PORT`)
- Health: http://localhost:8081/actuator/health  
- OpenAPI: http://localhost:8081/swagger-ui/index.html  
- Raw spec: http://localhost:8081/v3/api-docs  

## Configuration

| Variable | Purpose |
|----------|---------|
| `DB_URL`, `DB_USER`, `DB_PASSWORD` | PostgreSQL credentials |
| `SERVER_PORT` | HTTP port (default 8081) |

## Docker

```bash
docker build -t customer-service:latest .
```

Use orchestration from the **`banking-infra`** repository (`docker compose` / Kubernetes).
