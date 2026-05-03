# 🎥 Banking Microservices Demo - Complete Package Overview

## 📦 What's Included

You now have a **complete, production-ready demo package** with 4 comprehensive documents and 1 Postman collection:

### 1. 📝 **DEMO_README.md** (START HERE!)
   - **Purpose:** Overview and quick start guide
   - **Contains:**
     - Complete coverage matrix showing ALL requirements met ✅
     - Architecture diagram
     - Key talking points
     - Important URLs and database connections
     - Final checklist before recording
     - Pro tips for video recording

### 2. 🎬 **DEMO_SCRIPT.md** (MAIN SCRIPT)
   - **Purpose:** Full structured demonstration script
   - **Contains:**
     - 7 major sections with timing
     - Word-for-word script to say
     - Terminal commands to run
     - Expected responses
     - 15-20 minute total demo

   **Sections:**
   1. Deployment & Infrastructure (2-3 min)
   2. CRUD Operations (4-5 min)
   3. Inter-Service Communication (3-4 min)
   4. Database Persistence (3-4 min)
   5. Monitoring & Logs (2-3 min)
   6. Kubernetes & Minikube (2-3 min)
   7. Advanced Scenarios (2-3 min)

### 3. 🛠️ **DEMO_COMMANDS.sh** (QUICK REFERENCE)
   - **Purpose:** Copy-paste ready terminal commands
   - **Contains:**
     - All 8 sections of commands organized
     - Database connection commands
     - API testing commands
     - Kubernetes deployment commands
     - Cleanup and reset commands
     - Works with zsh shell

### 4. 🎓 **DEMO_RECORDING_GUIDE.md** (DETAILED INSTRUCTIONS)
   - **Purpose:** Step-by-step recording guide with timeline
   - **Contains:**
     - Pre-recording checklist
     - Detailed timeline with talking points (0:00-20:00)
     - Window layout recommendations
     - System preparation steps
     - Video editing tips
     - Post-recording checklist

### 5. 🔗 **Banking-Microservices-Demo.postman_collection.json**
   - **Purpose:** Pre-built API test collection
   - **Contains:**
     - All 4 services endpoints organized by service
     - Health check endpoints
     - CRUD operation examples
     - Error handling examples
     - Ready to import into Postman
     - Just right-click "Import" in Postman

---

## 🚀 Quick Start (5 minutes)

### Step 1: Verify Everything is Running
```bash
cd /Users/rashiverma@286/Desktop/scalable_services_assignment_2026/Group-5-Scalable-Services
docker compose ps
```
✅ Should show all 10 containers as "Up"

### Step 2: Import Postman Collection
1. Open Postman
2. Click "Import"
3. Select: `Banking-Microservices-Demo.postman_collection.json`
4. Click "Import"

### Step 3: Open VS Code
```bash
code /Users/rashiverma@286/Desktop/scalable_services_assignment_2026
```
- Open `DEMO_SCRIPT.md` for main script
- Open `DEMO_RECORDING_GUIDE.md` for timeline

### Step 4: Setup Terminal
```bash
# Terminal 1: Live logs
docker compose logs -f --tail=50

# Terminal 2: Ready for commands
# (Keep it available)
```

### Step 5: Start Recording!
Follow `DEMO_SCRIPT.md` section by section

---

## 📋 Demo Content Breakdown

### ✅ All Requirements Covered

| Requirement | Evidence | Duration |
|-------------|----------|----------|
| **CRUD Operations** | Postman + Terminal | 5 min |
| - Create Customer | Step 2.1 | 1 min |
| - Create Account | Step 2.2 | 1 min |
| - Read/Update | Step 2.3-2.4 | 1.5 min |
| - Delete/Close | Step 2.5 | 0.5 min |
| - Database Verification | Step 4 | 1 min |
| **Inter-Service Communication** | Real API calls | 4 min |
| - REST: Transaction→Account | Step 3.1 | 1.5 min |
| - Async: Transaction→RabbitMQ→Notification | Step 3.3 | 1.5 min |
| - Show logs of all interactions | Step 3.2 | 1 min |
| **Database Persistence** | Direct DB queries | 4 min |
| - Customer DB | Query | 1 min |
| - Account DB | Query | 1 min |
| - Transaction DB | Query | 1 min |
| - Notification DB | Query | 1 min |
| **Monitoring & Logs** | Health + Metrics | 3 min |
| - Docker logs | `docker compose logs` | 1 min |
| - Spring Boot health | `/actuator/health` | 0.5 min |
| - Prometheus metrics | `/actuator/prometheus` | 0.5 min |
| - Swagger docs | `/swagger-ui` | 1 min |
| **Kubernetes Deployment** | K8s manifests | 2 min |
| - Show manifests | Files | 0.5 min |
| - Explain deployment | Tutorial | 1 min |
| - Minikube instructions | Commands | 0.5 min |
| **Docker & Automation** | Infrastructure | 2 min |
| - Docker Compose | Orchestration | 0.75 min |
| - Dockerfiles | Build | 0.75 min |
| - CI/CD concepts | Explanation | 0.5 min |
| **TOTAL** | | ~19 minutes |

---

## 🎬 Timeline at a Glance

```
0:00 ──────────────────────────────────────────── 20:00
│
0:00 INTRO & DEPLOYMENT
     ├─ 0:30 Show docker-compose.yml
     ├─ 1:00 Show running services
     ├─ 1:30 Show Dockerfiles
     └─ 2:00 Show K8s manifests
│
3:00 CRUD OPERATIONS
     ├─ 3:30 Create customer (Postman)
     ├─ 4:00 Create account (Postman)
     ├─ 4:30 Update customer (Postman)
     ├─ 5:00 Update account (Postman)
     ├─ 5:30 Query database (psql)
     └─ 7:00 Verify data in DBs
│
8:00 INTER-SERVICE COMMUNICATION
     ├─ 8:00 Start log streaming
     ├─ 8:30 Execute transfer (curl)
     ├─ 9:00 Show REST calls in logs
     ├─ 9:30 Show async event in logs
     ├─ 10:00 Check RabbitMQ
     └─ 11:00 Verify notification DB
│
12:00 DATABASE PERSISTENCE
     ├─ 12:00 Query customer_db (psql)
     ├─ 12:45 Query account_db (psql)
     ├─ 13:30 Query transaction_db (psql)
     ├─ 14:15 Query notification_db (psql)
     └─ 14:45 Explain data flow diagram
│
15:00 MONITORING & HEALTH
     ├─ 15:00 Health endpoints (curl)
     ├─ 15:30 Prometheus metrics (curl)
     ├─ 16:00 Docker stats (CLI)
     └─ 16:30 Swagger UI (Browser)
│
17:00 KUBERNETES
     ├─ 17:00 Show manifests (cat)
     ├─ 17:30 Explain deployment
     ├─ 18:00 Minikube setup steps
     └─ 18:30 Kubernetes benefits discussion
│
19:00 ADVANCED SCENARIOS
     ├─ 19:00 Error handling demo
     ├─ 19:30 Idempotency test
     └─ 20:00 Frozen account test
│
20:00 END
```

---

## 🎯 How to Use Each Document

### 📖 Reading DEMO_SCRIPT.md
- Read the **entire** script first (takes 20 min)
- Understand the flow and talking points
- Note down any custom details for your system
- Practice sections 1-3 (25% of content) first
- Then practice sections 4-7

### 💻 Using DEMO_COMMANDS.sh
- Don't run the whole script - it's just reference
- Copy individual commands as needed
- Modify if needed (e.g., account numbers)
- Keep it open in a text editor during recording
- Use for pasting into terminals

### 🛠️ Using DEMO_RECORDING_GUIDE.md
- Follow the timeline exactly
- Read the "Script to Say" sections
- Do exactly what's in "Actions"
- Pause at checkmarks to ensure you're on track
- Reference the "Talking Points"

### 📮 Using Postman Collection
- Import first thing before recording
- Keep Postman visible during CRUD section
- Click pre-built requests instead of typing
- Cleaner than curl in video
- Shows professional API testing approach

---

## 🎓 Key Points to Emphasize During Demo

When recording, make sure to emphasize these architectural advantages:

### 1. Microservices Architecture
> "Each service is independent - can be developed, deployed, and scaled separately without affecting others."

### 2. Database-Per-Service
> "Data isolation ensures services can't accidentally create tight coupling through shared databases."

### 3. Synchronous Communication (Account Service)
> "For immediate data consistency, Transaction Service calls Account Service synchronously via REST API."

### 4. Asynchronous Messaging
> "For decoupled operations, Transaction Service publishes events to RabbitMQ that Notification Service consumes independently."

### 5. Error Handling
> "The system validates all business rules - insufficient funds, frozen accounts, daily limits - all enforced at service level."

### 6. Observability
> "Each service provides its own logs, metrics, and health checks - critical for debugging distributed systems."

### 7. Container Orchestration
> "Docker Compose for development, Kubernetes for production - same containers, different orchestration."

---

## ⚠️ Common Mistakes to Avoid

❌ **DON'T:**
- Rush through responses - let viewers read
- Skip error handling - show what happens when things go wrong
- Forget to show database persistence - that's proof it works
- Ignore logs - they tell the story of inter-service communication
- Use placeholder values - use realistic data

✅ **DO:**
- Speak clearly and deliberately
- Pause after each major step
- Show all output on screen
- Explain what you're seeing
- Use consistent account/customer IDs throughout
- Show successful AND failed operations

---

## 📸 Screenshot Opportunities

Consider capturing screenshots for presentation deck:

1. **Architecture Diagram** → docker-compose.yml visualization
2. **Service Running** → `docker compose ps` output
3. **CRUD Flow** → Customer creation in Postman
4. **Transfer Success** → API response with transaction ID
5. **Database Query** → psql output showing persisted data
6. **Logs** → docker logs showing inter-service communication
7. **Health Check** → Spring Boot health endpoint response
8. **K8s Manifests** → deployment.yaml snippet
9. **Metrics** → Prometheus endpoint output
10. **RabbitMQ** → Management UI showing queue

---

## 🎤 Suggested Intro Script

**Use this to open your video:**

> "Hi! I'm demonstrating a Banking Microservices application built with Java Spring Boot, Docker, and Kubernetes. 
>
> This is a production-ready system with four independent microservices: Customer, Account, Transaction, and Notification services. Each service has its own PostgreSQL database, ensuring data isolation and independent scaling.
>
> In this demo, I'll show you:
> - Complete CRUD operations across multiple services
> - How services communicate both synchronously and asynchronously
> - Real-time database persistence and verification
> - Built-in monitoring and health checks
> - Kubernetes deployment manifests
> 
> Let's dive in!"

---

## ✅ Final Checklist

Before you hit "Record":

- [ ] All services running (`docker compose ps` shows all UP)
- [ ] All health checks passing (curl /actuator/health)
- [ ] Postman collection imported
- [ ] VS Code open with demo files visible
- [ ] Terminal windows arranged
- [ ] Screen recording software ready
- [ ] Microphone working
- [ ] Screen at 1920x1080 minimum
- [ ] Font sizes increased to 16pt+
- [ ] Notifications disabled
- [ ] Browser bookmarks ready (local URLs)
- [ ] This README open for reference
- [ ] DEMO_SCRIPT.md open as your script

---

## 📤 After Recording

1. **Save immediately** to multiple locations (backup!)
2. **Edit video:**
   - Add intro slide (5 sec)
   - Add section transitions
   - Add captions for technical terms
   - Add timestamps in description
   - Add audio normalization
   - Adjust pacing if needed
3. **Create description:**
   - Include all command reference links
   - Add GitHub repo link
   - Include timestamps for each section
   - Add learning resources
4. **Upload:**
   - YouTube, Vimeo, or internal platform
   - Add this README as description
   - Include Postman collection link
   - Include GitHub repo link

---

## 🆘 Troubleshooting During Recording

| Issue | Solution |
|-------|----------|
| Service not responding | Run `docker compose restart {service}` |
| Database query fails | Check connection: `psql -h localhost -U user -d db -p port` |
| RabbitMQ not showing event | Check Notification Service logs: `docker compose logs notification-service` |
| Postman request fails | Verify service running with `docker compose ps` |
| Kubernetes section not clear | Just show the manifests and files - don't need to deploy live |
| Audio too quiet | Increase microphone level in recording settings |
| Terminal hard to read | Increase font to 18pt and use high contrast theme |

---

## 🎓 Bonus: Create a Blog Post

After recording, consider writing a blog post:

**Title:** "Building and Deploying Microservices with Spring Boot, Docker, and Kubernetes"

**Sections:**
1. Architecture Overview
2. Service Design (Database per service)
3. Inter-Service Communication Patterns
4. Building the Services (Technology Stack)
5. Containerization with Docker
6. Orchestration with Docker Compose
7. Production Deployment with Kubernetes
8. Monitoring and Observability
9. Lessons Learned

This adds significant value to your portfolio!

---

## 🚀 You're Ready!

Everything is prepared. Follow the timeline in `DEMO_RECORDING_GUIDE.md`, refer to the script in `DEMO_SCRIPT.md`, and you'll have an excellent demo video.

**Remember:**
- Speak clearly
- Take your time
- Show all the details
- Enjoy the process!

**Total recording time: 15-20 minutes**
**Total editing time: 30-45 minutes**

Good luck! 🎬✨

---

*For questions, refer to the specific document:*
- *Architecture questions → DEMO_README.md*
- *What to say → DEMO_SCRIPT.md*
- *When to say it → DEMO_RECORDING_GUIDE.md*
- *How to type it → DEMO_COMMANDS.sh*
