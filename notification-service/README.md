# Notification Service

Consumes **`TransactionCreated`** messages from RabbitMQ, logs high-value alerts and account-status notifications in **`notification_db`** / `notifications_log`.

## Run locally

```bash
export DB_URL=jdbc:postgresql://localhost:5436/notification_db
export DB_USER=notification
export DB_PASSWORD=notification
export RABBITMQ_HOST=localhost
mvn spring-boot-run
```

- Port: **8084**
- Logs API: `GET /notifications`
- Account status webhook: `POST /notifications/account-status`
- Swagger: http://localhost:8084/swagger-ui/index.html  

## Configuration

| Variable | Purpose |
|----------|---------|
| `RABBITMQ_*` | Broker connection |
| `DB_*`, `SERVER_PORT` | Database and HTTP port |

SMTP may be added via Spring Mail properties for real delivery; logs persist regardless.

## Docker

```bash
docker build -t notification-service:latest .
```

Compose and manifests are in **`banking-infra`**.
