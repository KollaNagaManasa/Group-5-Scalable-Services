# Seed data (CSV)

Run from **this directory**:

```bash
python generate_seed_csv.py
```

## Outputs

| File | Purpose |
|------|---------|
| `customers.csv` | 60 rows — matches assignment columns (`customer_id`, `name`, `email`, `phone`, `kyc_status`, `created_at`). Names/emails are synthetic but closer to sample spreadsheets than `Customer 1` placeholders. |
| `accounts.csv` | 88 rows — assignment columns only. Includes **SALARY**, **CURRENT**, **SAVINGS**, **NRE**; **EUR** on `account_id` 13 & 53 and **USD** on 28, 67, 70, 71, 83 (same pattern as reference sheets). Balances use one decimal place. `account_number` is a long numeric **string** (`5020001000xxxxx`) so it is not confused with floating IDs in CSV tools. |
| `accounts_account_service_import.csv` | Same rows as `accounts.csv` plus **`customer_name`** for bulk load into `account_db` without calling Customer Service. |
| `transactions.csv` | 300 rows — assignment shape: `txn_id`, **`account_id`**, `amount`, `txn_type`, **`counterparty`** (counterparty is **account_number**), `reference`, `created_at`. |
| `transactions_transaction_service_import.csv` | Same 300 logical transfers as `transactions.csv`, but **`account_number`** instead of `account_id`, matching `TransactionRecord`. |

## Professor CSV → transaction service

If you only have assignment `transactions.csv` (with `account_id`) and `accounts.csv`:

```bash
python remap_transactions_for_service.py transactions.csv accounts.csv transactions_mapped.csv
```

Requires `counterparty` to already be an **account_number**. If your sheet uses `counterparty` as another `account_id`, extend the script or replace that column first.

## Imports and business rules

- **Transfers** in the Java services are **INR-only**; EUR/USD accounts exist for seed realism and validation demos.
- **No overdraft** on debit applies to **SAVINGS** and **SALARY** (see Account Service).

Use `\copy` in `psql` after tables exist, or POST via REST. For `transactions_transaction_service_import.csv`, omit DB-generated keys and map columns to `transactions` (`txn_id`/`correlation_id` can be left to defaults or a follow-up SQL script).
