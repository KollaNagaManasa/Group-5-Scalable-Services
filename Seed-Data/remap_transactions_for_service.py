"""Canonical script lives in ../banking-infra/seed-data/ — copied here for visibility."""

import pathlib
import runpy
import sys

p = pathlib.Path(__file__).resolve().parent.parent / "banking-infra" / "seed-data" / "remap_transactions_for_service.py"
if not p.exists():
    raise SystemExit(f"Missing {p}")
sys.argv[0] = str(p)
runpy.run_path(str(p), run_name="__main__")
