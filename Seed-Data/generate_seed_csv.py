"""Thin shim: delegates to banking-infra/seed-data/generate_seed_csv.py."""

import pathlib
import runpy
import sys

_workspace = pathlib.Path(__file__).resolve().parent.parent
_canonical = _workspace / "banking-infra" / "seed-data" / "generate_seed_csv.py"
if not _canonical.exists():
    raise SystemExit(f"Missing {_canonical}; run from banking-infra/seed-data instead.")

sys.argv[0] = str(_canonical)
runpy.run_path(str(_canonical), run_name="__main__")
