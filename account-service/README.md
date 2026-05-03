# Account Service

Spring Boot microservice for accounts (create, status, balance) and internal debit/credit used by transfers. Owns **`account_db`** / `accounts` including replicated **`customer_name`** (fetched from Customer Service when omitted).

## Run locally

```bash
export DB_URL=jdbc:postgresql://localhost:5434/account_db
export DB_USER=account
export DB_PASSWORD=account
export CUSTOMER_SERVICE_BASE_URL=http://localhost:8081
mvn spring-boot-run
```

- Port: **8082**
- Swagger: http://localhost:8082/swagger-ui/index.html  

## Configuration

| Variable | Purpose |
|----------|---------|
| `CUSTOMER_SERVICE_BASE_URL` | Base URL for Customer Service REST calls (replicated name) |
| `DB_*`, `SERVER_PORT` | Database and HTTP port |

## Docker

```bash
docker build -t account-service:latest .
```

Orchestration lives in **`banking-infra`**.
