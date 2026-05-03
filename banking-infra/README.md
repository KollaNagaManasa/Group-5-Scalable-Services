# Banking platform infrastructure

This repository orchestrates the four banking microservices (each maintained in its **own Git repository**), PostgreSQL instances (database-per-service), and RabbitMQ.

## Recommended workspace layout (multi-repo)

Clone each repo as a sibling folder:

```text
workspace/
  customer-service/       # git@github.com:YOU/customer-service.git
  account-service/
  transaction-service/
  notification-service/
  banking-infra/            # this repo — git@github.com:YOU/banking-infra.git
```

From `banking-infra`:

```bash
docker compose up --build
```

Health checks:

- http://localhost:8081/actuator/health  
- http://localhost:8082/actuator/health  
- http://localhost:8083/actuator/health  
- http://localhost:8084/actuator/health  

Swagger UI (each service): `http://localhost:PORT/swagger-ui/index.html`

## Kubernetes (Minikube)

Build images against Minikube’s Docker daemon, then apply manifests:

```bash
minikube start
minikube docker-env --shell powershell | Invoke-Expression
docker build -t customer-service:latest ../customer-service
docker build -t account-service:latest ../account-service
docker build -t transaction-service:latest ../transaction-service
docker build -t notification-service:latest ../notification-service
kubectl apply -f k8s/
```

## Seed data (CSV)

CSV files live in `seed-data/`. Generate them with:

```bash
cd seed-data
python generate_seed_csv.py
```

Load into local Postgres using `psql`/`COPY` or your preferred import tool. Service-specific SQL helpers are documented in `seed-data/README.md`.
