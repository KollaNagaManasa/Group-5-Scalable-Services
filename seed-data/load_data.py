import csv
import requests
import time
import random

# -------------------------------
# CONFIG
# -------------------------------
CUSTOMER_API = "http://localhost:8081/customers"
ACCOUNT_API = "http://localhost:8082/accounts"
TRANSACTION_API = "http://localhost:8083/transactions/transfer"

account_map = {}

# -------------------------------
# LOAD CUSTOMERS
# -------------------------------
print("📥 Loading customers...")

with open('bank_customers.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        try:
            res = requests.post(CUSTOMER_API, json={
                "name": row["name"],
                "email": row["email"]
            })
            print(f"Customer {row['name']} → {res.status_code}")
        except Exception as e:
            print(f"Error loading customer {row['name']}: {e}")

# -------------------------------
# LOAD ACCOUNTS
# -------------------------------
print("\n Loading accounts...")

with open('bank_accounts.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        try:
            account_map[row["account_id"]] = row["account_number"]

            res = requests.post(ACCOUNT_API, json={
                "customerId": int(row["customer_id"]),
                "customerName": f"User-{row['customer_id']}",
                "accountNumber": row["account_number"],
                "accountType": row["account_type"],
                "balance": float(row["balance"])
            })

            print(f"Account {row['account_number']} → {res.status_code}")

        except Exception as e:
            print(f"Error loading account {row['account_number']}: {e}")

# -------------------------------
# LOAD TRANSACTIONS
# -------------------------------
print("\n Loading transactions...")

account_numbers = list(account_map.values())

with open('bank_transactions.csv') as file:
    reader = csv.DictReader(file)

    for i, row in enumerate(reader, start=1):
        try:
            from_acc = account_map.get(row["account_id"])

            # pick random different account as receiver
            to_acc = random.choice(account_numbers)
            while to_acc == from_acc:
                to_acc = random.choice(account_numbers)

            payload = {
                "fromAccount": from_acc,
                "toAccount": to_acc,
                "amount": float(row["amount"]),
                "reference": row["reference"]
            }

            res = requests.post(
                TRANSACTION_API,
                json=payload,
                headers={"Idempotency-Key": row["txn_id"]},
                timeout=5
            )

            print(f"Txn {row['txn_id']} → {res.status_code}")

        except Exception as e:
            print(f"Error loading txn {row['txn_id']}: {e}")

        # prevent overload
        time.sleep(0.2)

print("\n Data loading completed successfully!")
