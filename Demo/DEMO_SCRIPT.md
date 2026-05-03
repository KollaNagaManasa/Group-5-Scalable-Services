# Banking Microservices Demo Script

## Video Demo Checklist & Script

This script guides you through recording a comprehensive demo of the Banking Microservices application. Duration: ~15-20 minutes.

---

## PART 1: DEPLOYMENT & INFRASTRUCTURE (2-3 minutes)

### Scene 1.1: Docker Compose Architecture Overview
**What to show:**
- Open `docker-compose.yml` in VS Code
- Point out the service structure:
  - 4 Databases (PostgreSQL)
  - 1 Message Queue (RabbitMQ)
  - 4 Microservices

**Script:**
> "This is our Banking Microservices application built with Java Spring Boot. We have a database-per-service architecture with 4 independent services: Customer, Account, Transaction, and Notification. Each has its own PostgreSQL database for data isolation. We also use RabbitMQ for asynchronous event communication between Transaction and Notification services."

### Scene 1.2: Deploy with Docker Compose
**Terminal Commands:**
```bash
# Navigate to project
cd /Users/rashiverma@286/Desktop/scalable_services_assignment_2026/Group-5-Scalable-Services

# Show current status
docker compose ps

# Show all services running
docker compose logs --tail=5 customer-service
```

**Script:**
> "We use Docker Compose to orchestrate all services locally. All 10 containers (4 databases, 4 services, RabbitMQ, and related infra) are currently running. Here you can see the logs showing the services are healthy and accepting requests."

### Scene 1.3: Show Kubernetes Manifests (Optional but recommended)
**File to show:**
```bash
# Show K8s manifests
cat banking-infra/k8s/20-apps.yaml | head -50
```

**Script:**
> "We also have Kubernetes manifests prepared in the `banking-infra/k8s` folder for production deployment on Minikube or any Kubernetes cluster. The manifests define deployments, services, and environment configurations for each microservice."

---

## PART 2: CRUD OPERATIONS (4-5 minutes)

### Scene 2.1: Create a Customer
**Tool:** Postman or `curl`
**Endpoint:** `POST http://localhost:8081/customers`

**Request Body:**
```json
{
  "name": "Rashi Verma",
  "email": "rashi@bank.com",
  "phone": "+91-9876543210",
  "kycStatus": "VERIFIED"
}
```

**Script:**
> "First, let's create a new customer. We're making a POST request to the Customer Service on port 8081. The service validates the input, stores it in the customer database, and returns the created customer with an ID."

**Expected Response:**
```json
{
  "customerId": 1003,
  "name": "Rashi Verma",
  "email": "rashi@bank.com",
  "phone": "+91-9876543210",
  "kycStatus": "VERIFIED",
  "createdAt": "2026-05-04T..."
}
```

### Scene 2.2: Create an Account for the Customer
**Endpoint:** `POST http://localhost:8082/accounts`

**Request Body:**
```json
{
  "customerId": 1003,
  "accountNumber": "ACC-2026-5001",
  "accountType": "SAVINGS",
  "currency": "INR",
  "initialBalance": 50000
}
```

**Script:**
> "Now let's create a bank account for this customer. Account Service calls the Customer Service to validate that the customer exists (inter-service communication), then creates the account with the provided balance."

**Expected Response:**
```json
{
  "accountId": 2001,
  "customerId": 1003,
  "accountNumber": "ACC-2026-5001",
  "accountType": "SAVINGS",
  "balance": 50000.00,
  "currency": "INR",
  "status": "ACTIVE",
  "createdAt": "2026-05-04T..."
}
```

### Scene 2.3: READ - Get Customer and Account Details
**Endpoint:** `GET http://localhost:8081/customers/1003`

**Script:**
> "Reading the customer details. This retrieves the customer record from the database."

**Endpoint:** `GET http://localhost:8082/accounts/ACC-2026-5001`

**Script:**
> "Retrieving the account details from Account Service."

### Scene 2.4: UPDATE - Update Customer KYC Status
**Endpoint:** `PUT http://localhost:8081/customers/1003`

**Request Body:**
```json
{
  "kycStatus": "PENDING_REVIEW"
}
```

**Script:**
> "Updating the customer's KYC status. The change is persisted in the customer database."

### Scene 2.5: UPDATE - Update Account Status
**Endpoint:** `PUT http://localhost:8082/accounts/ACC-2026-5001/status`

**Request Body:**
```json
{
  "status": "FROZEN"
}
```

**Script:**
> "We can also update the account status. This demonstrates how Account Service enforces business logic - a frozen account cannot process transactions."

### Scene 2.6: DELETE - Simulate Account Closure
**Endpoint:** `DELETE http://localhost:8082/accounts/ACC-2026-5001`

**Script:**
> "Finally, we can close an account. The service marks the account as CLOSED in the database."

---

## PART 3: INTER-SERVICE COMMUNICATION (3-4 minutes)

### Scene 3.1: Transaction Transfer (REST Communication)
**Endpoint:** `POST http://localhost:8083/transactions/transfer`

**Request Body:**
```json
{
  "fromAccountNumber": "ACC-2026-5001",
  "toAccountNumber": "ACC-2026-5002",
  "amount": 5000,
  "reason": "Payment for services"
}
```

**Headers:**
```
Idempotency-Key: txn-2026-001
```

**Script:**
> "Now let's perform a transaction transfer. This is where inter-service communication really shines. The Transaction Service needs to:
> 1. Call Account Service to validate both accounts exist and are active
> 2. Check balance and transaction limits
> 3. Perform atomic debit and credit operations
> 4. Publish an event to RabbitMQ for the Notification Service
> Let's watch this happen in real-time."

**Expected Response:**
```json
{
  "transactionId": 3001,
  "fromAccount": "ACC-2026-5001",
  "toAccount": "ACC-2026-5002",
  "amount": 5000.00,
  "status": "SUCCESS",
  "createdAt": "2026-05-04T...",
  "message": "Transaction processed successfully"
}
```

### Scene 3.2: Show REST Call Flow in Logs
**Terminal Command:**
```bash
docker compose logs --tail=20 transaction-service
docker compose logs --tail=20 account-service
```

**Script:**
> "Looking at the logs, we can see:
> - Transaction Service received the transfer request
> - It made REST calls to Account Service to validate and update accounts
> - Both account operations succeeded
> - The transaction was recorded with SUCCESS status"

### Scene 3.3: Async Event Publishing (RabbitMQ)
**Terminal Command:**
```bash
docker compose logs --tail=20 notification-service
```

**Script:**
> "The Transaction Service published a 'TransactionCreated' event to RabbitMQ. The Notification Service is listening on this queue and consuming the event asynchronously. You can see in the logs that it received and processed the event."

---

## PART 4: DATABASE PERSISTENCE & DATA FLOW (3-4 minutes)

### Scene 4.1: Connect to Customer Database
**Terminal Command:**
```bash
psql -h localhost -U customer -d customer_db -p 5443 -c "SELECT * FROM customers LIMIT 5;"
```

**Script:**
> "Let's verify the data persistence by connecting directly to the PostgreSQL database. Here we can see the customers we created earlier are persisted in the customer_db database."

### Scene 4.2: Check Account Database
**Terminal Command:**
```bash
psql -h localhost -U account -d account_db -p 5434 -c "SELECT account_number, account_type, balance, status FROM accounts LIMIT 5;"
```

**Password:** `account`

**Script:**
> "In the Account database, we can see the accounts with their current balances and status. Notice how the balance changed after our transfer transaction."

### Scene 4.3: Check Transaction Database
**Terminal Command:**
```bash
psql -h localhost -U transaction -d transaction_db -p 5435 -c "SELECT txn_id, account_number, txn_type, amount, status FROM transactions LIMIT 10;"
```

**Password:** `transaction`

**Script:**
> "The Transaction database shows the transaction history - both debit and credit entries for the transfer we performed. Each transaction is recorded with timestamp and status."

### Scene 4.4: Check Notification Log
**Terminal Command:**
```bash
psql -h localhost -U notification -d notification_db -p 5436 -c "SELECT notification_id, account_number, event_type, status FROM notifications_log LIMIT 5;"
```

**Password:** `notification`

**Script:**
> "The Notification database tracks all notifications that were sent. When the transaction event was published, the Notification Service consumed it and logged it here."

### Scene 4.5: Show Data Flow Diagram
**File to show:**
Open README.md and show the Context Map mermaid diagram

**Script:**
> "This diagram shows our data flow architecture. Each service owns its own database. When Transaction Service needs account data, it calls Account Service via REST (synchronous). For notifications, it publishes an event to RabbitMQ (asynchronous), which the Notification Service consumes independently."

---

## PART 5: MONITORING & LOGS (2-3 minutes)

### Scene 5.1: Docker Container Logs
**Terminal Commands:**
```bash
# View live logs from all services
docker compose logs -f --tail=50

# View specific service logs
docker compose logs account-service
docker compose logs transaction-service
docker compose logs notification-service
```

**Script:**
> "Docker Compose provides real-time logs for all running containers. We can see startup messages, request logs, and any errors. This is crucial for debugging in development."

### Scene 5.2: Docker Stats / Resource Monitoring
**Terminal Command:**
```bash
docker stats --no-stream
```

**Script:**
> "Here's the resource usage for each container - CPU, memory, network I/O. This helps us monitor performance and identify resource constraints."

### Scene 5.3: Spring Boot Actuator Health Endpoints
**Browser/Postman:**
```
GET http://localhost:8081/actuator/health
GET http://localhost:8082/actuator/health
GET http://localhost:8083/actuator/health
GET http://localhost:8084/actuator/health
```

**Script:**
> "Each Spring Boot service exposes health check endpoints. We can see the status is UP for all services, plus component-specific health (database connectivity, etc.)"

**Expected Response:**
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL"
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

### Scene 5.4: Prometheus Metrics
**Terminal Command:**
```bash
curl -s http://localhost:8081/actuator/prometheus | head -30
```

**Script:**
> "Spring Boot also exposes Prometheus metrics for detailed application monitoring. These can be scraped by monitoring tools like Prometheus and visualized in Grafana."

### Scene 5.5: Swagger/OpenAPI Documentation
**Browser:**
```
http://localhost:8081/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html
http://localhost:8083/swagger-ui/index.html
http://localhost:8084/swagger-ui/index.html
```

**Script:**
> "Each service provides interactive API documentation through Swagger UI. This makes it easy to explore and test all available endpoints."

---

## PART 6: KUBERNETES & MINIKUBE DEPLOYMENT (2-3 minutes)

### Scene 6.1: Show Kubernetes Manifests Structure
**Terminal Commands:**
```bash
ls -la banking-infra/k8s/
cat banking-infra/k8s/00-namespace-config-secret.yaml
```

**Script:**
> "For production deployment, we have Kubernetes manifests prepared. This file creates the namespace, configmaps, and secrets for our services."

### Scene 6.2: Show Deployment Manifests
**Terminal Command:**
```bash
cat banking-infra/k8s/20-apps.yaml | head -80
```

**Script:**
> "These Kubernetes deployment manifests define how our microservices should run in a cluster. Each service has a deployment with resource requests/limits, liveness probes, and readiness probes for health checking."

### Scene 6.3: Show Data Store Manifests
**Terminal Command:**
```bash
cat banking-infra/k8s/10-datastores.yaml | head -50
```

**Script:**
> "The datastores manifest defines StatefulSets for PostgreSQL databases and a StatefulSet for RabbitMQ, ensuring persistent storage and ordered deployment."

### Scene 6.4: How to Deploy on Minikube (Demo/Instructions)
**Terminal Commands (just show, don't necessarily deploy):**
```bash
# Start Minikube
minikube start

# Apply manifests
kubectl apply -f banking-infra/k8s/00-namespace-config-secret.yaml
kubectl apply -f banking-infra/k8s/10-datastores.yaml
kubectl apply -f banking-infra/k8s/20-apps.yaml

# Check pods
kubectl get pods -n banking

# Port forward
kubectl port-forward -n banking svc/customer-service 8081:8081
```

**Script:**
> "To deploy on Minikube or any Kubernetes cluster, we apply the YAML manifests in order:
> 1. Namespaces, ConfigMaps, and Secrets
> 2. Data stores (databases and message queue)
> 3. Application services
> 
> Then we can port-forward to access services locally, and use standard kubectl commands for monitoring and troubleshooting."

---

## PART 7: AUTOMATION & CI/CD SETUP (1-2 minutes)

### Scene 7.1: Dockerfiles
**Files to show:**
```bash
cat customer-service/Dockerfile
cat account-service/Dockerfile
```

**Script:**
> "Each service has a Dockerfile that builds a Docker image. We use multi-stage builds for optimization - compiling the Maven project and packaging the JAR in the final image."

### Scene 7.2: Docker Compose as Orchestration
**Show:**
```bash
cat docker-compose.yml | grep -A 3 "services:"
```

**Script:**
> "Docker Compose is our local development and testing orchestration tool. It manages:
> - Service dependencies (ensuring databases start before applications)
> - Environment variables and configuration injection
> - Network connectivity between containers
> - Volume mounts for database initialization
> - Port mappings for external access"

### Scene 7.3: Build and Start Process
**Terminal Command (show history):**
```bash
# Show the build command
docker compose up -d --build

# Show build logs
docker compose logs --tail=10 customer-service
```

**Script:**
> "When we run 'docker compose up -d --build':
> 1. Docker builds images for each service using their Dockerfiles
> 2. Creates and starts containers in dependency order
> 3. All services become available on their configured ports"

---

## BONUS: ADVANCED SCENARIOS (Optional, 2-3 minutes)

### Scene B.1: Error Handling - Insufficient Funds
**Endpoint:** `POST http://localhost:8083/transactions/transfer`

**Request Body:**
```json
{
  "fromAccountNumber": "ACC-2026-5001",
  "toAccountNumber": "ACC-2026-5002",
  "amount": 500000
}
```

**Script:**
> "Let's test error handling. We're trying to transfer more than the account balance. The system should reject this with a proper error message."

**Expected Response (400 or 422):**
```json
{
  "status": "FAILED",
  "error": "Insufficient balance",
  "message": "Account balance is less than transfer amount"
}
```

### Scene B.2: Duplicate Prevention with Idempotency Key
**Endpoint:** `POST http://localhost:8083/transactions/transfer` (same request, same idempotency key)

**Script:**
> "The same request with the same Idempotency-Key returns the same result without creating a duplicate transaction. This ensures idempotent operations in distributed systems."

### Scene B.3: Business Logic - Daily Transfer Limit
**Multiple Transfer Requests:**
```bash
# First transfer: 150,000 INR (within limit)
# Second transfer: 60,000 INR (exceeds 200,000 daily limit)
```

**Script:**
> "Transaction Service enforces business rules like daily transfer limits. The second transfer exceeds the limit and is rejected."

---

## RECORDING TIPS

1. **Screen Recording:** Use QuickTime Player (macOS), OBS, or similar
2. **Audio:** Speak clearly, explain each step
3. **Pacing:** Take time to show output, let viewers read responses
4. **Switching Scenes:** Use clear transitions between sections
5. **Terminal:** Increase font size for visibility
6. **Timestamps:** Note the time for each section for easier editing
7. **Background:** Have VS Code, Terminal, and Postman available
8. **Clean State:** Ensure services are fresh and databases reset before recording final version

---

## VIDEO STRUCTURE SUMMARY

**Total Duration: ~15-20 minutes**

| Section | Duration | Key Demos |
|---------|----------|-----------|
| Deployment & Infrastructure | 2-3 min | Docker Compose, K8s manifests |
| CRUD Operations | 4-5 min | Create, Read, Update, Delete across services |
| Inter-Service Communication | 3-4 min | REST calls, RabbitMQ events, async processing |
| Database Persistence | 3-4 min | Direct DB queries, data verification |
| Monitoring & Logs | 2-3 min | Container logs, health checks, metrics |
| Kubernetes & Minikube | 2-3 min | K8s manifests, deployment instructions |
| Automation & CI/CD | 1-2 min | Dockerfiles, Compose orchestration |
| Bonus: Advanced Scenarios | 2-3 min | Error handling, idempotency, business rules |

---

## POST-DEMO CHECKLIST

- [ ] All services are running (`docker compose ps`)
- [ ] Customer created successfully
- [ ] Account created and linked to customer
- [ ] Transfer transaction succeeded
- [ ] RabbitMQ event was consumed
- [ ] All databases show correct data
- [ ] Health checks return UP status
- [ ] Logs are clear and show inter-service communication
- [ ] Kubernetes manifests are explained
- [ ] Video captures all requirements
- [ ] Audio is clear and well-paced
- [ ] No sensitive data exposed in video

---

