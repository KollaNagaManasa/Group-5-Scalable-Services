import csv
import requests

# -------------------------------
# CONFIG (update if needed)
# -------------------------------
CUSTOMER_API = "http://localhost:8081/customers"
ACCOUNT_API = "http://localhost:8082/accounts"
TRANSACTION_API = "http://localhost:8083/transactions/transfer"

# -------------------------------
# STORAGE (mapping account_id → account_number)
# -------------------------------
account_map = {}

# -------------------------------
# LOAD CUSTOMERS
# -------------------------------
print("📥 Loading customers...")

with open('bank_customers.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        try:
            response = requests.post(CUSTOMER_API, json={
                "name": row["name"],
                "email": row["email"]
            })
            print(f"Customer {row['name']} → {response.status_code}")
        except Exception as e:
            print(f"Error loading customer {row['name']}: {e}")

# -------------------------------
# LOAD ACCOUNTS
# -------------------------------
print("\n📥 Loading accounts...")

with open('bank_accounts.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        try:
            # store mapping
            account_map[row["account_id"]] = row["account_number"]

            response = requests.post(ACCOUNT_API, json={
                "customerId": int(row["customer_id"]),
                "customerName": f"User-{row['customer_id']}",
                "accountNumber": row["account_number"],
                "accountType": row["account_type"],
                "balance": float(row["balance"])
            })

            print(f"Account {row['account_number']} → {response.status_code}")

        except Exception as e:
            print(f"Error loading account {row['account_number']}: {e}")

# -------------------------------
# LOAD TRANSACTIONS
# -------------------------------
print("\n Loading transactions...")

with open('bank_transactions.csv') as file:
    reader = csv.DictReader(file)
    for row in reader:
        try:
            account_id = row["account_id"]
            account_number = account_map.get(account_id)

            if not account_number:
                print(f"Skipping txn {row['txn_id']} (no account mapping)")
                continue

            response = requests.post(
                TRANSACTION_API,
                json={
                    "fromAccountNumber": account_number,
                    "toAccountNumber": account_number,  # simplified
                    "amount": float(row["amount"]),
                    "reference": row["reference"]
                },
                headers={
                    "Idempotency-Key": row["txn_id"]
                }
            )

            print(f"Transaction {row['txn_id']} → {response.status_code}")

        except Exception as e:
            print(f"Error loading txn {row['txn_id']}: {e}")

# -------------------------------
# DONE
# -------------------------------
print("\n Data loading completed successfully!")
