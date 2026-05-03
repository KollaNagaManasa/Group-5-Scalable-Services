# Banking Microservices Demo - Recording & Testing Guide

## Pre-Recording Checklist

### System Preparation
- [ ] All services running: `docker compose ps` shows all containers "Up"
- [ ] Terminal font size increased (16pt+) for visibility
- [ ] Screen resolution at least 1920x1080
- [ ] Microphone tested and working
- [ ] Recording software ready (QuickTime, OBS, etc.)
- [ ] Close unnecessary applications to reduce CPU usage
- [ ] Turn off notifications

### Service Verification
- [ ] Customer Service responding (port 8081)
- [ ] Account Service responding (port 8082)
- [ ] Transaction Service responding (port 8083)
- [ ] Notification Service responding (port 8084)
- [ ] All databases accessible
- [ ] RabbitMQ UI accessible (http://localhost:15672)

**Quick Verification Command:**
```bash
for port in 8081 8082 8083 8084; do
  echo "Testing port $port:"
  curl -s http://localhost:$port/actuator/health | jq '.status'
done
```

### Window Layout Setup

**Option 1 (Recommended for Demo):**
1. **Left Side (60%):** VS Code
   - Open: `docker-compose.yml` and `README.md`
   - Open: Kubernetes manifests in `banking-infra/k8s/`
2. **Right Side (40%):** Terminal
   - Show logs with: `docker compose logs -f --tail=30`

**Option 2 (For API Testing):**
1. **Top Left:** VS Code
2. **Top Right:** Postman (with collection imported)
3. **Bottom Left:** Terminal (for logs)
4. **Bottom Right:** Browser (for databases/metrics)

### Tools to Have Open/Ready
- [ ] Postman with `Banking-Microservices-Demo.postman_collection.json` imported
- [ ] Terminal (2-3 windows recommended)
- [ ] VS Code with project open
- [ ] Browser (Chrome/Firefox)
- [ ] `psql` or database client
- [ ] Docker Desktop showing container stats

---

## Step-by-Step Recording Guide

### PRE-RECORDING TEST (5 minutes)
Run this sequence to ensure everything works:

```bash
#!/bin/bash
# Test script - run 5 minutes before recording

echo "=== Testing Banking Microservices ==="

# 1. Verify all containers running
echo -e "\n1. Checking containers..."
docker compose ps | grep "Up"

# 2. Test all health endpoints
echo -e "\n2. Testing health endpoints..."
for service in 8081 8082 8083 8084; do
  status=$(curl -s http://localhost:$service/actuator/health | grep -o '"status":"[^"]*"')
  echo "Port $service: $status"
done

# 3. Create test customer
echo -e "\n3. Creating test customer..."
curl -s -X POST http://localhost:8081/customers \
  -H 'Content-Type: application/json' \
  -d '{"name":"Demo User","email":"demo@test.com","phone":"+91-1234567890","kycStatus":"VERIFIED"}' | jq '.customerId'

# 4. Check database connectivity
echo -e "\n4. Testing database connectivity..."
psql -h localhost -U customer -d customer_db -p 5443 -c "SELECT COUNT(*) FROM customers;" 2>/dev/null

echo -e "\n=== All Tests Complete ==="
```

---

## Demo Execution Timeline

### PART 1: INTRO & DEPLOYMENT (0:00-3:00)

**Script to Say:**
> "Today I'm demonstrating a Banking Microservices application built with Java Spring Boot, Docker, and Kubernetes. We have 4 independent microservices with database-per-service architecture, asynchronous messaging with RabbitMQ, and complete CRUD operations with inter-service communication."

**Actions:**
1. (0:00) Open docker-compose.yml
2. (0:30) Explain service structure, point out ports
3. (1:00) Show `docker compose ps` output
4. (1:30) Show Dockerfile examples
5. (2:00) Show K8s manifests structure
6. (2:30) Show deployment overview

**Talking Points:**
- Database per service for isolation
- Docker Compose for local development
- Kubernetes manifests for production
- Spring Boot with Actuator for monitoring

---

### PART 2: CRUD OPERATIONS (3:00-8:00)

**Script to Say:**
> "Now let's perform CRUD operations across different services. I'll create a customer, then an account, and update their information to demonstrate data persistence and validation."

#### CREATE (3:00-3:30)
1. Use Postman: POST `/customers`
2. Show JSON response with customerId
3. Explain response validation

**Key Points to Mention:**
- Request validation
- ID auto-generation
- JSON serialization/deserialization

#### READ (3:30-4:00)
1. GET `/customers/{id}` - show data retrieval
2. GET `/accounts/{accountNumber}` - show data from different service
3. Explain data isolation between services

**Key Points to Mention:**
- ACID properties maintained per service
- Data consistency within boundaries
- Query performance

#### UPDATE (4:00-4:30)
1. PUT `/customers/{id}` - update KYC status
2. PUT `/accounts/{id}/status` - update account status
3. Show updated data with GET

**Key Points to Mention:**
- Idempotent operations
- Validation before update
- Audit trails

#### CREATE (Second Account) (4:30-5:00)
1. Create second account for demonstration
2. Show data in Postman

#### DELETE (5:00-5:30)
1. Close/freeze account
2. Show in database that account is CLOSED
3. Try to perform transaction on closed account

**Key Points to Mention:**
- Logical deletion
- Status-based business rules
- Data integrity

#### DATA VERIFICATION (5:30-8:00)
1. Connect to customer_db: `psql -h localhost -U customer -d customer_db -p 5443`
2. Query: `SELECT * FROM customers ORDER BY created_at DESC LIMIT 5;`
3. Show actual database persistence
4. Connect to account_db and show same

**Key Points to Mention:**
- Data actually persisted
- Database schema
- Relationships between tables

---

### PART 3: INTER-SERVICE COMMUNICATION (8:00-12:00)

**Script to Say:**
> "This is where microservices architecture becomes powerful. When we perform a transaction transfer, it involves synchronous REST calls to Account Service and asynchronous event publishing to RabbitMQ for the Notification Service."

#### SYNC REST CALL (8:00-10:00)

1. **Clear terminal:** `clear`
2. **Start log monitoring:**
   ```bash
   docker compose logs -f --tail=30 transaction-service account-service notification-service
   ```
3. **In separate terminal, execute transfer:**
   ```bash
   curl -X POST http://localhost:8083/transactions/transfer \
     -H 'Content-Type: application/json' \
     -H 'Idempotency-Key: demo-txn-001' \
     -d '{
       "fromAccountNumber": "ACC-2026-5001",
       "toAccountNumber": "ACC-2026-5002",
       "amount": 5000,
       "reason": "Demo transfer"
     }' | jq
   ```
4. **Show the response** - transaction succeeded
5. **Point out logs:**
   - Transaction Service: received request, validating accounts
   - Account Service: validated both accounts, performed debit/credit
   - Shows exact sequence of REST calls

**Key Points to Mention:**
- REST calls with timeout/retry logic
- Account validation across service boundary
- Error handling if account not found
- Transactional consistency within service

#### ASYNC EVENT PUBLISHING (10:00-12:00)

1. **Observe logs continuing to show:**
   - Transaction Service published event to RabbitMQ
   - Notification Service consumed the event
   - Event was logged to notifications_db

2. **Check RabbitMQ UI:** `http://localhost:15672`
   - Show queues
   - Show message throughput
   - Show no messages (already consumed)

3. **Verify in Notification DB:**
   ```bash
   psql -h localhost -U notification -d notification_db -p 5436
   SELECT notification_id, account_number, event_type, message, created_at 
   FROM notifications_log 
   ORDER BY created_at DESC LIMIT 3;
   ```

**Key Points to Mention:**
- Event-driven architecture
- Decoupled services (Notification Service didn't need to be online)
- Eventual consistency
- Fault tolerance (if Notification Service is down, message queued)

---

### PART 4: DATABASE PERSISTENCE & DATA FLOW (12:00-15:00)

**Script to Say:**
> "Let's verify that all our operations have been persisted across all four databases. This demonstrates the data flow and database-per-service architecture."

#### Customer Database (12:00-12:45)
```bash
psql -h localhost -U customer -d customer_db -p 5443
```

Show:
```sql
SELECT customer_id, name, email, kyc_status, created_at FROM customers ORDER BY created_at DESC LIMIT 5;
```

**Explain:**
- Customer data isolated here
- Not shared directly with other services (replicated at creation time only)

#### Account Database (12:45-13:15)
```bash
psql -h localhost -U account -d account_db -p 5434
```

Show:
```sql
SELECT account_id, customer_id, account_number, account_type, balance, status, created_at FROM accounts;
```

**Explain:**
- Account data and balances
- Updated by both CREATE and TRANSFER operations
- Notice balance changed after transaction

#### Transaction Database (13:15-13:45)
```bash
psql -h localhost -U transaction -d transaction_db -p 5435
```

Show:
```sql
SELECT txn_id, account_number, txn_type, amount, status, created_at FROM transactions ORDER BY created_at DESC LIMIT 10;
```

**Explain:**
- Both debit and credit entries
- Timestamp ordering
- Status tracking

#### Notification Database (13:45-14:15)
```bash
psql -h localhost -U notification -d notification_db -p 5436
```

Show:
```sql
SELECT notification_id, account_number, event_type, message, status, created_at FROM notifications_log ORDER BY created_at DESC LIMIT 5;
```

**Explain:**
- Event log from async messaging
- Notification processing status

#### Data Flow Diagram Explanation (14:15-15:00)
- Open README.md Context Map
- Trace data flow through all systems
- Emphasize: sync REST calls vs async events

---

### PART 5: MONITORING & OBSERVABILITY (15:00-17:00)

**Script to Say:**
> "For a production system, monitoring is critical. Spring Boot provides out-of-the-box metrics and health checks."

#### Health Check Endpoints (15:00-15:30)
```bash
# Show all health endpoints
curl -s http://localhost:8081/actuator/health | jq
curl -s http://localhost:8082/actuator/health | jq
```

**Explain:**
- Service status
- Database connectivity
- Component health

#### Prometheus Metrics (15:30-16:00)
```bash
curl -s http://localhost:8081/actuator/prometheus | head -50
```

**Explain:**
- HTTP request counts
- Response times
- JVM metrics
- Custom business metrics

#### Docker Stats (16:00-16:30)
```bash
docker stats --no-stream
```

**Explain:**
- CPU and memory usage
- Network I/O
- Resource constraints

#### Swagger UI (16:30-17:00)
- Open: `http://localhost:8081/swagger-ui/index.html`
- Show API documentation
- Explain endpoint descriptions
- Test an endpoint directly from Swagger

**Explain:**
- API documentation is generated automatically
- OpenAPI 3.0 specification
- Useful for API consumers and testing

---

### PART 6: KUBERNETES DEPLOYMENT (17:00-19:00)

**Script to Say:**
> "For production deployment, we use Kubernetes. Let me show you the manifests and explain how to deploy on Minikube or any Kubernetes cluster."

#### Show Manifests (17:00-17:30)
```bash
cat banking-infra/k8s/00-namespace-config-secret.yaml | head -30
cat banking-infra/k8s/10-datastores.yaml | head -40
cat banking-infra/k8s/20-apps.yaml | head -50
```

**Explain:**
- Namespace isolation
- ConfigMaps for configuration
- Secrets for credentials
- Deployments for stateless services
- StatefulSets for databases

#### Deployment Instructions (17:30-18:00)
**Show but don't necessarily deploy (takes time):**

```bash
# Start Minikube
minikube start

# Apply manifests in order
kubectl apply -f banking-infra/k8s/00-namespace-config-secret.yaml
kubectl apply -f banking-infra/k8s/10-datastores.yaml
kubectl apply -f banking-infra/k8s/20-apps.yaml

# Check deployment status
kubectl get pods -n banking
kubectl get svc -n banking
```

**Explain:**
- Order of application matters
- Networking within cluster
- Service discovery

#### Monitoring in Kubernetes (18:00-18:30)
**Show commands:**
```bash
kubectl logs -n banking deployment/customer-service
kubectl describe deployment customer-service -n banking
kubectl port-forward -n banking svc/customer-service 8081:8081
```

**Explain:**
- Centralized logging
- Deployment status and events
- Port forwarding for local testing

#### Benefits of Kubernetes (18:30-19:00)
**Discuss:**
- Auto-scaling
- Self-healing
- Rolling updates
- High availability
- Resource management

---

### PART 7: ERROR HANDLING & EDGE CASES (19:00-20:00)

#### Test Insufficient Funds (19:00-19:20)
```bash
curl -X POST http://localhost:8083/transactions/transfer \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: error-insufficient-001' \
  -d '{
    "fromAccountNumber": "ACC-2026-5001",
    "toAccountNumber": "ACC-2026-5002",
    "amount": 500000
  }' | jq
```

**Expected:** Error response
**Explain:** Business rule validation

#### Test Idempotency (19:20-19:40)
```bash
# Same request with same idempotency key
curl -X POST http://localhost:8083/transactions/transfer \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: demo-txn-001' \
  -d '{
    "fromAccountNumber": "ACC-2026-5001",
    "toAccountNumber": "ACC-2026-5002",
    "amount": 5000,
    "reason": "Demo transfer"
  }' | jq
```

**Explain:**
- Should return same response as before
- Prevents duplicate transactions in case of network retries

#### Frozen Account (19:40-20:00)
```bash
# Try transfer from frozen account
curl -X POST http://localhost:8083/transactions/transfer \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: error-frozen-001' \
  -d '{
    "fromAccountNumber": "ACC-2026-5001",
    "toAccountNumber": "ACC-2026-5002",
    "amount": 1000
  }' | jq
```

**Explain:** Accounts with status FROZEN cannot process transactions

---

## Post-Recording Checklist

- [ ] All segments recorded clearly
- [ ] Audio is audible and clear
- [ ] All API calls succeeded
- [ ] Database data was visible
- [ ] Logs showed inter-service communication
- [ ] Kubernetes section was explained
- [ ] Total duration: 15-20 minutes
- [ ] No sensitive data exposed (passwords, API keys)
- [ ] File saved in multiple locations (backup)

## Video Editing Tips

1. **Cuts & Transitions:** Use fade transitions between major sections
2. **Captions:** Add captions for technical terms
3. **Overlays:** Show port numbers, endpoints on screen
4. **Slow Down:** When showing logs or database output
5. **Highlights:** Highlight important lines in terminal output
6. **Background Music:** Keep low volume during terminal sections
7. **Title Slide:** Show project name and your name
8. **End Screen:** Summary of what was demonstrated

## Upload & Sharing

- [ ] Upload to YouTube (unlisted if needed)
- [ ] Create video description with timestamps
- [ ] Include GitHub repo links
- [ ] Add demo scripts and commands as comments
- [ ] Create README section linking to video

---

## Quick Reference: Hotkeys

### Terminal
- `Ctrl+C` - Stop running process
- `Ctrl+L` - Clear screen
- `↑↓` - Previous/next command
- `Cmd+K` - Clear terminal (macOS)

### Postman
- `Cmd+K` - Quick access
- `Cmd+E` - Environments
- `Cmd+G` - Generate code
- `Cmd+R` - Run request

### VS Code
- `Cmd+K Cmd+W` - Close all tabs
- `Cmd+P` - Quick file open
- `Cmd+/` - Toggle comment
- `Cmd+Shift+P` - Command palette

---

**Good luck with your demo!** 🎥✨
