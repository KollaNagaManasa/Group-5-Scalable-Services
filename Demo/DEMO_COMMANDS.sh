#!/bin/bash
# Banking Microservices Demo - Quick Commands Reference
# Use this file as a reference for all terminal commands needed in the video demo

echo "=========================================="
echo "BANKING MICROSERVICES DEMO COMMANDS"
echo "=========================================="

# ============================================
# SECTION 1: DEPLOYMENT & INFRASTRUCTURE
# ============================================

echo -e "\n--- SECTION 1: DEPLOYMENT & INFRASTRUCTURE ---\n"

echo "1.1) Check all running services:"
echo "$ docker compose ps"

echo -e "\n1.2) View logs for specific service:"
echo "$ docker compose logs --tail=50 customer-service"
echo "$ docker compose logs --tail=50 account-service"
echo "$ docker compose logs --tail=50 transaction-service"
echo "$ docker compose logs --tail=50 notification-service"

echo -e "\n1.3) Stream logs in real-time:"
echo "$ docker compose logs -f --tail=100"

echo -e "\n1.4) View Docker container statistics:"
echo "$ docker stats --no-stream"

echo -e "\n1.5) View Kubernetes manifests:"
echo "$ cat banking-infra/k8s/00-namespace-config-secret.yaml"
echo "$ cat banking-infra/k8s/10-datastores.yaml | head -50"
echo "$ cat banking-infra/k8s/20-apps.yaml | head -80"

# ============================================
# SECTION 2: CRUD OPERATIONS (Via Terminal)
# ============================================

echo -e "\n--- SECTION 2: CRUD OPERATIONS (Terminal Examples) ---\n"

echo "2.1) CREATE Customer:"
echo "$ curl -X POST http://localhost:8081/customers \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"name\":\"Rashi Verma\",\"email\":\"rashi@bank.com\",\"phone\":\"+91-9876543210\",\"kycStatus\":\"VERIFIED\"}'"

echo -e "\n2.2) READ Customer:"
echo "$ curl -X GET http://localhost:8081/customers/1"

echo -e "\n2.3) UPDATE Customer:"
echo "$ curl -X PUT http://localhost:8081/customers/1 \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"kycStatus\":\"PENDING_REVIEW\"}'"

echo -e "\n2.4) CREATE Account:"
echo "$ curl -X POST http://localhost:8082/accounts \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"customerId\":1,\"accountNumber\":\"ACC-2026-5001\",\"accountType\":\"SAVINGS\",\"currency\":\"INR\",\"initialBalance\":50000}'"

echo -e "\n2.5) READ Account:"
echo "$ curl -X GET http://localhost:8082/accounts/ACC-2026-5001"

echo -e "\n2.6) GET Account Balance:"
echo "$ curl -X GET http://localhost:8082/accounts/ACC-2026-5001/balance"

# ============================================
# SECTION 3: INTER-SERVICE COMMUNICATION
# ============================================

echo -e "\n--- SECTION 3: INTER-SERVICE COMMUNICATION ---\n"

echo "3.1) Transfer Money (REST + RabbitMQ):"
echo "$ curl -X POST http://localhost:8083/transactions/transfer \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -H 'Idempotency-Key: txn-2026-001' \\"
echo "  -d '{\"fromAccountNumber\":\"ACC-2026-5001\",\"toAccountNumber\":\"ACC-2026-5002\",\"amount\":5000,\"reason\":\"Payment for services\"}'"

echo -e "\n3.2) View Transaction Service Logs:"
echo "$ docker compose logs --tail=30 transaction-service"

echo -e "\n3.3) View Account Service Logs (REST calls from Transaction Service):"
echo "$ docker compose logs --tail=30 account-service"

echo -e "\n3.4) View Notification Service Logs (RabbitMQ event consumption):"
echo "$ docker compose logs --tail=30 notification-service"

echo -e "\n3.5) Check RabbitMQ Management UI:"
echo "Open browser: http://localhost:15672"
echo "Username: guest, Password: guest"

# ============================================
# SECTION 4: DATABASE PERSISTENCE
# ============================================

echo -e "\n--- SECTION 4: DATABASE PERSISTENCE & DATA VERIFICATION ---\n"

echo "4.1) Connect to Customer Database:"
echo "$ psql -h localhost -U customer -d customer_db -p 5443"
echo "Password: customer"
echo "Query: SELECT * FROM customers LIMIT 5;"

echo -e "\n4.2) Connect to Account Database:"
echo "$ psql -h localhost -U account -d account_db -p 5434"
echo "Password: account"
echo "Query: SELECT account_number, account_type, balance, status FROM accounts LIMIT 5;"

echo -e "\n4.3) Connect to Transaction Database:"
echo "$ psql -h localhost -U transaction -d transaction_db -p 5435"
echo "Password: transaction"
echo "Query: SELECT txn_id, account_number, txn_type, amount, status FROM transactions LIMIT 10;"

echo -e "\n4.4) Connect to Notification Database:"
echo "$ psql -h localhost -U notification -d notification_db -p 5436"
echo "Password: notification"
echo "Query: SELECT notification_id, account_number, event_type, status FROM notifications_log LIMIT 5;"

echo -e "\n4.5) Quick Database Verification Script:"
cat << 'EOF'
#!/bin/bash
# Run this to verify all databases have data

echo "=== CUSTOMER DATABASE ==="
psql -h localhost -U customer -d customer_db -p 5443 -c "SELECT COUNT(*) as customer_count FROM customers;"

echo "=== ACCOUNT DATABASE ==="
psql -h localhost -U account -d account_db -p 5434 -c "SELECT COUNT(*) as account_count FROM accounts;"

echo "=== TRANSACTION DATABASE ==="
psql -h localhost -U transaction -d transaction_db -p 5435 -c "SELECT COUNT(*) as transaction_count FROM transactions;"

echo "=== NOTIFICATION DATABASE ==="
psql -h localhost -U notification -d notification_db -p 5436 -c "SELECT COUNT(*) as notification_count FROM notifications_log;"
EOF

# ============================================
# SECTION 5: MONITORING & HEALTH CHECKS
# ============================================

echo -e "\n--- SECTION 5: MONITORING & HEALTH CHECKS ---\n"

echo "5.1) Service Health Check Endpoints:"
echo "$ curl -s http://localhost:8081/actuator/health | jq"
echo "$ curl -s http://localhost:8082/actuator/health | jq"
echo "$ curl -s http://localhost:8083/actuator/health | jq"
echo "$ curl -s http://localhost:8084/actuator/health | jq"

echo -e "\n5.2) Prometheus Metrics Endpoints:"
echo "$ curl -s http://localhost:8081/actuator/prometheus | head -30"
echo "$ curl -s http://localhost:8082/actuator/prometheus | head -30"

echo -e "\n5.3) Swagger UI (Open in Browser):"
echo "Customer Service: http://localhost:8081/swagger-ui/index.html"
echo "Account Service:  http://localhost:8082/swagger-ui/index.html"
echo "Transaction Service: http://localhost:8083/swagger-ui/index.html"
echo "Notification Service: http://localhost:8084/swagger-ui/index.html"

echo -e "\n5.4) Real-time Docker Logs:"
echo "$ docker compose logs -f --tail=50"

echo -e "\n5.5) Service Resource Usage:"
echo "$ docker stats"

# ============================================
# SECTION 6: KUBERNETES DEPLOYMENT
# ============================================

echo -e "\n--- SECTION 6: KUBERNETES DEPLOYMENT ---\n"

echo "6.1) Start Minikube:"
echo "$ minikube start"

echo -e "\n6.2) Apply Kubernetes Manifests (in order):"
echo "$ kubectl apply -f banking-infra/k8s/00-namespace-config-secret.yaml"
echo "$ kubectl apply -f banking-infra/k8s/10-datastores.yaml"
echo "$ kubectl apply -f banking-infra/k8s/20-apps.yaml"

echo -e "\n6.3) Check Pod Status:"
echo "$ kubectl get pods -n banking"
echo "$ kubectl get svc -n banking"

echo -e "\n6.4) View Pod Logs:"
echo "$ kubectl logs -n banking deployment/customer-service"
echo "$ kubectl logs -n banking deployment/account-service"
echo "$ kubectl logs -n banking deployment/transaction-service"

echo -e "\n6.5) Port Forward to Local Machine:"
echo "$ kubectl port-forward -n banking svc/customer-service 8081:8081"
echo "$ kubectl port-forward -n banking svc/account-service 8082:8082"
echo "$ kubectl port-forward -n banking svc/transaction-service 8083:8083"

echo -e "\n6.6) Describe Deployment:"
echo "$ kubectl describe deployment customer-service -n banking"

# ============================================
# SECTION 7: ERROR HANDLING & ADVANCED
# ============================================

echo -e "\n--- SECTION 7: ERROR HANDLING & ADVANCED SCENARIOS ---\n"

echo "7.1) Transfer with Insufficient Funds (should fail):"
echo "$ curl -X POST http://localhost:8083/transactions/transfer \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -H 'Idempotency-Key: error-test-001' \\"
echo "  -d '{\"fromAccountNumber\":\"ACC-2026-5001\",\"toAccountNumber\":\"ACC-2026-5002\",\"amount\":500000}'"

echo -e "\n7.2) Test Idempotency (same request, same key - should return same result):"
echo "$ curl -X POST http://localhost:8083/transactions/transfer \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -H 'Idempotency-Key: txn-2026-001' \\"
echo "  -d '{\"fromAccountNumber\":\"ACC-2026-5001\",\"toAccountNumber\":\"ACC-2026-5002\",\"amount\":5000}'"

echo -e "\n7.3) Test Daily Transfer Limit:"
echo "$ # Create multiple transfers that exceed INR 200000 daily limit"

echo -e "\n7.4) Freeze Account and Try Transaction:"
echo "$ curl -X PUT http://localhost:8082/accounts/ACC-2026-5001/status \\"
echo "  -H 'Content-Type: application/json' \\"
echo "  -d '{\"status\":\"FROZEN\"}'"
echo "$ # Then try to transfer from frozen account (should fail)"

# ============================================
# CLEANUP & RESET
# ============================================

echo -e "\n--- CLEANUP & RESET ---\n"

echo "8.1) Stop all services:"
echo "$ docker compose down"

echo -e "\n8.2) Stop and remove volumes (full reset):"
echo "$ docker compose down -v"

echo -e "\n8.3) Restart all services:"
echo "$ docker compose up -d"

echo -e "\n8.4) Full rebuild:"
echo "$ docker compose up -d --build"

echo -e "\n=========================================="
echo "END OF COMMANDS REFERENCE"
echo "=========================================="
