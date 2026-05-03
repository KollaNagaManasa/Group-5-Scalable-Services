# Transaction Service

Spring Boot microservice for transfers (idempotent), statements, daily INR limits, and RabbitMQ **`TransactionCreated`** events. Owns **`transaction_db`** (`transactions`, idempotency keys).

## Run locally

Requires Account Service and RabbitMQ reachable.

```bash
export DB_URL=jdbc:postgresql://localhost:5435/transaction_db
export DB_USER=transaction
export DB_PASSWORD=transaction
export ACCOUNT_SERVICE_BASE_URL=http://localhost:8082
export RABBITMQ_HOST=localhost
mvn spring-boot-run
```

- Port: **8083**
- Transfer: `POST /transactions/transfer` with header **`Idempotency-Key`**
- Swagger: http://localhost:8083/swagger-ui/index.html  

## Configuration

| Variable | Purpose |
|----------|---------|
| `ACCOUNT_SERVICE_BASE_URL` | Account Service base URL for balance mutations |
| `RABBITMQ_HOST`, `RABBITMQ_PORT`, `RABBITMQ_USER`, `RABBITMQ_PASSWORD` | Message broker |
| `DB_*`, `SERVER_PORT` | Database and port |

## Docker

```bash
docker build -t transaction-service:latest .
```

Use **`banking-infra`** for full stack compose / Kubernetes.
