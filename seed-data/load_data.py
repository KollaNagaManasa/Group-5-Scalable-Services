import csv
import requests

BASE_URL_CUSTOMER = "http://localhost:8081/customers"
BASE_URL_ACCOUNT = "http://localhost:8082/accounts"
BASE_URL_TRANSACTION = "http://localhost:8083/transactions/transfer"

# ----------------------
# Load Customers
# ----------------------
print("Loading customers...")
with open('bank_customers.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        requests.post(BASE_URL_CUSTOMER, json={
            "name": row["name"],
            "email": row["email"]
        })

# ----------------------
# Load Accounts
# ----------------------
print("Loading accounts...")
with open('bank_accounts.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        requests.post(BASE_URL_ACCOUNT, json={
            "customerId": int(row["customerId"]),
            "customerName": row["customerName"],
            "accountNumber": row["accountNumber"],
            "accountType": row["accountType"],
            "balance": float(row["balance"])
        })

# ----------------------
# Load Transactions
# ----------------------
print("Loading transactions...")
with open('bank_transactions.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        requests.post(BASE_URL_TRANSACTION, json={
            "fromAccountNumber": row["fromAccount"],
            "toAccountNumber": row["toAccount"],
            "amount": float(row["amount"]),
            "reference": "seed-data"
        }, headers={
            "Idempotency-Key": row["txnId"]
        })

print("Data loading complete")
