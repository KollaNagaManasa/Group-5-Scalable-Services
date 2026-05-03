"""
Maps assignment-style transactions.csv (uses account_id + counterparty account_number)
to transaction-service columns (account_number throughout).

Usage (from this directory):
  python remap_transactions_for_service.py transactions.csv accounts.csv transactions_mapped.csv

accounts.csv must contain account_id and account_number columns.
"""

import argparse
import csv
import sys


def load_account_numbers(path: str) -> dict[str, str]:
    by_id: dict[str, str] = {}
    with open(path, newline="", encoding="utf-8") as f:
        r = csv.DictReader(f)
        for row in r:
            by_id[row["account_id"].strip()] = row["account_number"].strip()
    return by_id


def main() -> None:
    p = argparse.ArgumentParser()
    p.add_argument("transactions_csv")
    p.add_argument("accounts_csv")
    p.add_argument("out_csv")
    args = p.parse_args()

    nums = load_account_numbers(args.accounts_csv)

    with open(args.transactions_csv, newline="", encoding="utf-8") as inf, open(
        args.out_csv, "w", newline="", encoding="utf-8"
    ) as outf:
        tr = csv.DictReader(inf)
        fieldnames = ["account_number", "amount", "txn_type", "counterparty", "reference", "created_at"]
        tw = csv.DictWriter(outf, fieldnames=fieldnames)
        tw.writeheader()
        for row in tr:
            aid = row["account_id"].strip()
            if aid not in nums:
                print(f"Unknown account_id {aid}", file=sys.stderr)
                sys.exit(1)
            cp_raw = row["counterparty"].strip()
            # Counterparty may already be account_number, or may be account_id (numeric FK style).
            cp_resolved = nums[cp_raw] if cp_raw in nums else cp_raw
            tw.writerow(
                {
                    "account_number": nums[aid],
                    "amount": row["amount"].strip(),
                    "txn_type": row["txn_type"].strip(),
                    "counterparty": cp_resolved,
                    "reference": row["reference"].strip(),
                    "created_at": row["created_at"].strip(),
                }
            )

    print(f"Wrote {args.out_csv}")


if __name__ == "__main__":
    main()
