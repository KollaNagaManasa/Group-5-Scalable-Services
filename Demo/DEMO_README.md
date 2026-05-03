# Banking Microservices Demo - Complete Package

## 📹 What You Have

You now have a complete demo package with everything needed to record a professional video demonstrating your Banking Microservices application. Here's what's included:

### Documents Created:

1. **`DEMO_SCRIPT.md`** ⭐ Main Demo Script
   - Complete 7-part structured demonstration
   - Detailed explanations for each section
   - Total duration: 15-20 minutes
   - Includes all commands, curl requests, and expected outputs
   - Bonus scenarios for advanced use cases

2. **`DEMO_COMMANDS.sh`** - Quick Reference Guide
   - All terminal commands organized by section
   - Copy-paste ready commands
   - Organized by demo phase
   - Error handling and cleanup commands

3. **`Banking-Microservices-Demo.postman_collection.json`** - Postman Collection
   - Pre-built API test collection
   - All 4 services endpoints
   - Health and metrics endpoints
   - Ready to import into Postman

4. **`DEMO_RECORDING_GUIDE.md`** - Detailed Recording Guide
   - Step-by-step recording instructions
   - Timeline and talking points
   - Pre-recording checklist
   - Window layout recommendations
   - Video editing tips

---

## 📋 Demo Coverage Matrix

Your demo will cover ALL required aspects:

| Requirement | Coverage | Location |
|-------------|----------|----------|
| **CRUD Operations** | ✅ Complete | DEMO_SCRIPT Part 2, Timeline 3:00-8:00 |
| **Customer Create/Read/Update** | ✅ Yes | Sections 2.1-2.4 |
| **Account Create/Read/Update** | ✅ Yes | Sections 2.1-2.5 |
| **Transaction Create (Transfer)** | ✅ Yes | Part 3, Inter-service comm |
| **Delete/Status Change** | ✅ Yes | Section 2.6 |
| **Inter-Service Communication** | ✅ Complete | DEMO_SCRIPT Part 3, Timeline 8:00-12:00 |
| **REST Calls (Customer Service calls)** | ✅ Yes | Sync REST during transfer |
| **Async Messaging (RabbitMQ)** | ✅ Yes | Transaction → Notification |
| **Event Publishing** | ✅ Yes | TransactionCreated event |
| **Database Persistence** | ✅ Complete | DEMO_SCRIPT Part 4, Timeline 12:00-15:00 |
| **Customer DB** | ✅ Verified | Direct `psql` connection |
| **Account DB** | ✅ Verified | Direct `psql` connection |
| **Transaction DB** | ✅ Verified | Direct `psql` connection |
| **Notification DB** | ✅ Verified | Direct `psql` connection |
| **Data Flow Visualization** | ✅ Yes | ER diagrams in README |
| **Monitoring & Logs** | ✅ Complete | DEMO_SCRIPT Part 5, Timeline 15:00-17:00 |
| **Docker Logs** | ✅ Yes | `docker compose logs` |
| **Health Endpoints** | ✅ Yes | `/actuator/health` |
| **Metrics Endpoints** | ✅ Yes | `/actuator/prometheus` |
| **Swagger Documentation** | ✅ Yes | `/swagger-ui/index.html` |
| **Container Stats** | ✅ Yes | `docker stats` |
| **Kubernetes Deployment** | ✅ Yes | DEMO_SCRIPT Part 6, Timeline 17:00-19:00 |
| **K8s Manifests** | ✅ Shown | `banking-infra/k8s/` |
| **Deployment Process** | ✅ Explained | `kubectl apply` workflow |
| **Namespace Setup** | ✅ Shown | 00-namespace-config-secret.yaml |
| **Data Store Config** | ✅ Shown | 10-datastores.yaml |
| **App Services Config** | ✅ Shown | 20-apps.yaml |
| **Docker & Compose** | ✅ Explained | DEMO_SCRIPT Part 1 & 7 |
| **Automation** | ✅ Shown | DEMO_SCRIPT Part 7 |
| **Dockerfiles** | ✅ Shown | Service build process |
| **Docker Compose** | ✅ Complete | Orchestration demo |

---

## 🚀 Quick Start to Recording

### Phase 1: Preparation (10 minutes)
```bash
# 1. Verify all services running
cd /Users/rashiverma@286/Desktop/scalable_services_assignment_2026/Group-5-Scalable-Services
docker compose ps

# 2. Test all health endpoints
curl -s http://localhost:8081/actuator/health | jq '.status'
curl -s http://localhost:8082/actuator/health | jq '.status'
curl -s http://localhost:8083/actuator/health | jq '.status'
curl -s http://localhost:8084/actuator/health | jq '.status'

# 3. Open Postman and import collection
# File > Import > Banking-Microservices-Demo.postman_collection.json

# 4. Setup windows
# Terminal 1: docker compose logs -f
# Terminal 2: Ready for commands
# VS Code: Open docker-compose.yml and README.md
# Postman: Collection imported
# Browser: Ready for Swagger and databases
```

### Phase 2: Record (15-20 minutes)
Follow the **DEMO_SCRIPT.md** exactly as written:
- Section 1: Introduction & Deployment (3 min)
- Section 2: CRUD Operations (5 min)
- Section 3: Inter-Service Communication (4 min)
- Section 4: Database Persistence (4 min)
- Section 5: Monitoring (2 min)
- Section 6: Kubernetes (2 min)
- Section 7: Advanced Scenarios (1 min)

### Phase 3: Post-Production (Edit & Upload)
- Edit video with transitions
- Add captions and timestamps
- Create descriptive YouTube description
- Include command references

---

## 📊 Your Microservices Architecture

```
┌─────────────────────────────────────────────────────┐
│         Banking Microservices Platform               │
├─────────────────────────────────────────────────────┤
│                                                       │
│  ┌──────────────┐  ┌──────────────┐                 │
│  │   Customer   │  │   Account    │                 │
│  │  Service     │  │  Service     │                 │
│  │ (port 8081)  │  │ (port 8082)  │                 │
│  └──────┬───────┘  └──────┬───────┘                 │
│         │                  │                         │
│  ┌──────▼──────┐  ┌────────▼─────┐                 │
│  │ Customer DB │  │  Account DB  │                 │
│  │ (port 5443) │  │ (port 5434)  │                 │
│  └─────────────┘  └──────────────┘                 │
│                                                       │
│  ┌──────────────┐  ┌──────────────┐                 │
│  │ Transaction  │  │ Notification │                 │
│  │  Service     │  │  Service     │                 │
│  │ (port 8083)  │  │ (port 8084)  │                 │
│  └──────┬───────┘  └──────┬───────┘                 │
│         │                  │                         │
│  ┌──────▼──────┐  ┌────────▼─────┐                 │
│  │Transaction  │  │Notification  │                 │
│  │    DB       │  │     DB       │                 │
│  │ (port 5435) │  │ (port 5436)  │                 │
│  └─────────────┘  └──────────────┘                 │
│                                                       │
│            ┌─────────────────┐                      │
│            │    RabbitMQ     │                      │
│            │  (ports 5672,   │                      │
│            │    15672)       │                      │
│            └─────────────────┘                      │
│                   ▲                                  │
│                   │                                  │
│        Transaction Service publishes events        │
│    Notification Service consumes asynchronously     │
│                                                       │
└─────────────────────────────────────────────────────┘
```

---

## 🎯 Key Talking Points

### Architecture
- "Database-per-service ensures loose coupling and independent scaling"
- "Synchronous REST calls for immediate consistency (Account Service)"
- "Asynchronous events for eventual consistency (Notification Service)"

### CRUD
- "All CRUD operations validated at service boundary"
- "Data isolation means each service owns its schema"
- "No direct database access between services"

### Inter-Service Communication
- "Account Service validates that account exists and is active"
- "Transaction Service performs debit and credit atomically within its DB"
- "Event published to RabbitMQ so Notification Service learns of transaction"
- "System resilient - if Notification Service is down, event is queued"

### Data Persistence
- "Each service has its own PostgreSQL database"
- "Data written to database immediately, visible in all CRUD operations"
- "Transaction log shows both debit and credit entries"
- "Notification log shows async event processing"

### Monitoring
- "Spring Boot Actuator provides health, metrics, and audit logs"
- "Prometheus metrics can be scraped for time-series monitoring"
- "Docker logs show real-time request/response flow"
- "Each service independently monitorable"

### Kubernetes
- "YAML manifests define entire infrastructure as code"
- "Can deploy to Minikube locally or production Kubernetes cluster"
- "ConfigMaps handle configuration, Secrets handle credentials"
- "Services provide DNS-based discovery between pods"

---

## 📝 Notes for Your Recording

1. **Speak Clearly:** This is technical but try to explain business value
2. **Show Error Cases:** Demonstrate error handling (insufficient funds, etc.)
3. **Explain Why:** Not just "what" but "why" microservices matter
4. **Take Pauses:** Let output display, don't rush through
5. **Use Zoom:** Show specific parts of output with screen zoom
6. **Terminal Fonts:** Use 16pt+ for visibility on video
7. **Credibility:** Show your actual running system, not slides

---

## 🔗 Important URLs for Demo

| Service | URL |
|---------|-----|
| Customer Service | http://localhost:8081 |
| Account Service | http://localhost:8082 |
| Transaction Service | http://localhost:8083 |
| Notification Service | http://localhost:8084 |
| Customer Swagger | http://localhost:8081/swagger-ui/index.html |
| Account Swagger | http://localhost:8082/swagger-ui/index.html |
| Transaction Swagger | http://localhost:8083/swagger-ui/index.html |
| Notification Swagger | http://localhost:8084/swagger-ui/index.html |
| Customer Health | http://localhost:8081/actuator/health |
| Account Health | http://localhost:8082/actuator/health |
| Transaction Health | http://localhost:8083/actuator/health |
| Notification Health | http://localhost:8084/actuator/health |
| RabbitMQ Admin | http://localhost:15672 (guest/guest) |
| Customer Metrics | http://localhost:8081/actuator/prometheus |
| Account Metrics | http://localhost:8082/actuator/prometheus |

---

## 📚 Database Connection Details

| Database | Host | Port | User | Password |
|----------|------|------|------|----------|
| customer_db | localhost | 5443 | customer | customer |
| account_db | localhost | 5434 | account | account |
| transaction_db | localhost | 5435 | transaction | transaction |
| notification_db | localhost | 5436 | notification | notification |

**Connection Command Template:**
```bash
psql -h localhost -U {user} -d {database} -p {port}
```

---

## ✅ Final Checklist Before Recording

- [ ] All 10 containers running (`docker compose ps`)
- [ ] All 4 services responding to health checks
- [ ] Postman collection imported with all endpoints
- [ ] Terminal windows arranged and ready
- [ ] VS Code with project open
- [ ] Microphone tested
- [ ] Recording software launched
- [ ] Screen resolution optimal (1920x1080+)
- [ ] Font sizes increased for visibility
- [ ] Notifications disabled
- [ ] Background applications closed
- [ ] DEMO_SCRIPT.md open for reference

---

## 🎬 Recording Commands Cheat Sheet

```bash
# Start recording here

# === PART 1: DEPLOYMENT ===
docker compose ps
cat docker-compose.yml | head -50
ls banking-infra/k8s/

# === PART 2: CRUD ===
# Use Postman for these - it's cleaner

# === PART 3: INTER-SERVICE ===
docker compose logs -f --tail=30 transaction-service account-service notification-service
# In another terminal:
curl -X POST http://localhost:8083/transactions/transfer \
  -H 'Content-Type: application/json' \
  -H 'Idempotency-Key: demo-txn-001' \
  -d '{"fromAccountNumber":"ACC-2026-5001","toAccountNumber":"ACC-2026-5002","amount":5000,"reason":"Demo"}'

# === PART 4: DATABASE ===
psql -h localhost -U customer -d customer_db -p 5443
SELECT * FROM customers LIMIT 5;
# Repeat for other databases

# === PART 5: MONITORING ===
curl -s http://localhost:8081/actuator/health | jq
docker stats --no-stream

# === PART 6: KUBERNETES ===
cat banking-infra/k8s/20-apps.yaml | head -50

# End recording here
```

---

## 🎓 Learning Resources (to mention in video)

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Docker Documentation: https://docs.docker.com/
- Kubernetes Documentation: https://kubernetes.io/docs/
- RabbitMQ Documentation: https://www.rabbitmq.com/documentation.html
- PostgreSQL Documentation: https://www.postgresql.org/docs/

---

## 💡 Pro Tips

1. **Practice First:** Do a dry run without recording
2. **Create Backup:** Record in multiple locations
3. **Clean Data:** Reset databases before final recording for clarity
4. **Terminal Settings:**
   - Increase font size to 16pt
   - Dark theme for better visibility
   - Clear shell aliases that might confuse viewers
5. **Pacing:** Speak slower than you think necessary
6. **Editing:** Add captions for technical terms
7. **Timestamps:** Include them in video description for navigation

---

**Your demo is ready! Let's make it great! 🎬✨**

For questions or clarifications, refer to the main `DEMO_SCRIPT.md` file.



