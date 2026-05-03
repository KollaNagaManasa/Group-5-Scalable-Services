"""
Generates banking assignment CSVs aligned with the reference datasets:
- 60 customers (realistic-style names, string phones, KYC mix)
- 88 accounts (SALARY/CURRENT/SAVINGS/NRE; INR + fixed EUR/USD rows like samples)
- 300 transactions (assignment shape + transaction-service import file)

Run from this directory: python generate_seed_csv.py
"""

import csv
import random
from datetime import datetime, timedelta

random.seed(42)

base_time = datetime(2026, 1, 1, 10, 0, 0)

FIRST_NAMES = [
    "Vivaan", "Aarav", "Myra", "Raj", "Ira", "Vihaan", "Anaya", "Kabir", "Diya", "Arjun",
    "Advika", "Reyansh", "Kiara", "Veer", "Ahana", "Rohan", "Navya", "Ishaan", "Tara", "Kunal",
    "Zara", "Dev", "Meera", "Siddharth", "Anika", "Raghav", "Pari", "Neel", "Sanvi", "Manav",
]

LAST_NAMES = [
    "Khan", "Kulkarni", "Bose", "Verma", "Mukherjee", "Patel", "Singh", "Reddy", "Iyer", "Menon",
    "Sharma", "Kapoor", "Nair", "Desai", "Joshi", "Mehta", "Chopra", "Banerjee", "Rao", "Agarwal",
    "Malhotra", "Ghosh", "Shetty", "Pillai", "Das", "Khanna", "Bhat", "Kaur", "Talwar", "Sen",
]

ACCOUNT_TYPES = ["SALARY", "SAVINGS", "CURRENT", "NRE"]
ACCOUNT_TYPE_WEIGHTS = [3, 3, 2, 1]

STATUSES = ["ACTIVE", "ACTIVE", "ACTIVE", "FROZEN", "CLOSED"]

KYC_STATUSES = ["VERIFIED", "VERIFIED", "VERIFIED", "PENDING", "REJECTED"]


def dt(i: int) -> str:
    return (base_time + timedelta(minutes=i)).isoformat()


def customer_row(i: int) -> tuple:
    fn = FIRST_NAMES[(i * 17) % len(FIRST_NAMES)]
    ln = LAST_NAMES[(i * 31 + i // 5) % len(LAST_NAMES)]
    name = f"{fn} {ln}"
    email_local = f"{fn.lower()}.{ln.lower()}{i if i > 1 else ''}"
    email = f"{email_local}@example.com"
    phone = str(9_000_000_000 + (i * 1_048_573) % 999_999_999).zfill(10)
    kyc = random.choice(KYC_STATUSES)
    return i, name, email, phone, kyc, dt(i)


def currency_for_account(account_id: int) -> str:
    if account_id in (13, 53):
        return "EUR"
    if account_id in (28, 67, 70, 71, 83):
        return "USD"
    return "INR"


def account_number_for(account_id: int) -> str:
    # Long numeric-style string (quoted in CSV); avoids ACC prefix so shape matches spreadsheet-style IDs.
    # Still safe for Excel if opened via "From Text" / consistent text column.
    return f"5020001000{account_id:05d}"


def main() -> None:
    customers_by_id: dict[int, tuple] = {}
    with open("customers.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["customer_id", "name", "email", "phone", "kyc_status", "created_at"])
        for i in range(1, 61):
            row = customer_row(i)
            customers_by_id[i] = row
            w.writerow(row)

    account_numbers_by_id: dict[int, str] = {}
    accounts_meta: list[tuple] = []

    with open("accounts.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(
            ["account_id", "customer_id", "account_number", "account_type", "balance", "currency", "status", "created_at"]
        )
        for i in range(1, 89):
            customer_id = random.randint(1, 60)
            acct_no = account_number_for(i)
            account_numbers_by_id[i] = acct_no
            acct_type = random.choices(ACCOUNT_TYPES, weights=ACCOUNT_TYPE_WEIGHTS, k=1)[0]
            balance = round(random.uniform(5_000.0, 400_000.0), 1)
            currency = currency_for_account(i)
            status = random.choice(STATUSES)
            row = (i, customer_id, acct_no, acct_type, balance, currency, status, dt(i))
            accounts_meta.append(row)
            w.writerow(row)

    with open("accounts_account_service_import.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(
            [
                "account_id",
                "customer_id",
                "customer_name",
                "account_number",
                "account_type",
                "balance",
                "currency",
                "status",
                "created_at",
            ]
        )
        for row in accounts_meta:
            aid, cid, acct_no, atype, bal, cur, stat, ts = (
                row[0],
                row[1],
                row[2],
                row[3],
                row[4],
                row[5],
                row[6],
                row[7],
            )
            cust_name = customers_by_id[cid][1]
            w.writerow([aid, cid, cust_name, acct_no, atype, bal, cur, stat, ts])

    id_list = list(range(1, 89))

    txn_rows: list[tuple] = []
    for tid in range(1, 301):
        account_id = random.randint(1, 88)
        cp_id = random.choice([x for x in id_list if x != account_id])
        amount = round(random.uniform(100.0, 50_000.0), 2)
        txn_type = random.choice(["DEBIT", "CREDIT"])
        cp_number = account_numbers_by_id[cp_id]
        ref = f"seed-{tid:04d}"
        txn_rows.append((tid, account_id, amount, txn_type, cp_number, ref, dt(tid)))

    with open("transactions.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["txn_id", "account_id", "amount", "txn_type", "counterparty", "reference", "created_at"])
        for tid, account_id, amount, txn_type, cp_number, ref, ts in txn_rows:
            w.writerow([tid, account_id, amount, txn_type, cp_number, ref, ts])

    with open("transactions_transaction_service_import.csv", "w", newline="", encoding="utf-8") as f:
        w = csv.writer(f)
        w.writerow(["account_number", "amount", "txn_type", "counterparty", "reference", "created_at"])
        for tid, account_id, amount, txn_type, cp_number, ref, ts in txn_rows:
            acc_no = account_numbers_by_id[account_id]
            w.writerow([acc_no, amount, txn_type, cp_number, ref, ts])

    print(
        "Wrote customers.csv (60), accounts.csv (88), accounts_account_service_import.csv, "
        "transactions.csv (300), transactions_transaction_service_import.csv (300)"
    )


if __name__ == "__main__":
    main()
