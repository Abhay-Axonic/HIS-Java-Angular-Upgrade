#!/usr/bin/env python3
"""phantom_drift_sweep.py — entity-vs-DDL phantom-column sweep (DQ-2026-08-12-054).

Scans every entity under legacy/Entity/src/main/java for @Table/@Column
declarations and diffs them case-insensitively against the his_local
information_schema. Output: every table whose entity declares columns the
DDL lacks — the phantom-column class (live instances: family_id on
patient.t_patient_details — 6th, appointment_unique_id on
opd.m_appointment_master — 7th; both block their flow's primary write with
a masked 501 while the DAO catch swallows the SQLGrammarException).

Usage:
    python3 tools/phantom_drift_sweep.py [--admission-only]

Exit 0 always (reporting tool); print-only.
"""

import os
import re
import subprocess
import sys
import collections

ENTITY_ROOT = os.path.join(os.path.dirname(os.path.abspath(__file__)),
                           "..", "legacy", "Entity", "src", "main", "java")
ADMISSION_PREFIXES = ("adt.", "opd.", "patient.")


def psql(q):
    r = subprocess.run(["psql", "-h", "localhost", "-d", "his_local", "-tAc", q],
                       capture_output=True, text=True, timeout=60)
    if r.returncode != 0:
        raise RuntimeError(f"psql failed: {r.stderr.strip()[:300]}")
    return r.stdout.strip()


def main():
    admission_only = "--admission-only" in sys.argv
    col_re = re.compile(r'@Column\s*\(\s*name\s*=\s*"([^"]+)"')
    table_re = re.compile(r'@Table\s*\(\s*name\s*=\s*"([^"]+)"(?:\s*,\s*schema\s*=\s*"([^"]+)")?')

    tables = collections.defaultdict(set)
    for dirpath, _, files in os.walk(ENTITY_ROOT):
        for fn in files:
            if not fn.endswith(".java"):
                continue
            try:
                src = open(os.path.join(dirpath, fn), encoding="utf-8", errors="replace").read()
            except OSError:
                continue
            m = table_re.search(src)
            if not m:
                continue
            tname, tschema = m.group(1), m.group(2) or "public"
            cols = set(col_re.findall(src))
            if cols:
                tables[(tschema, tname)].update(cols)

    all_tables = list(tables.keys())
    exists = set()
    for i in range(0, len(all_tables), 60):
        chunk = all_tables[i:i + 60]
        q = ("select table_schema||'.'||table_name from information_schema.tables where "
             + " or ".join(f"(table_schema='{s}' and table_name='{t}')" for s, t in chunk))
        exists.update(psql(q).splitlines())

    ddl_cols = collections.defaultdict(set)
    for i in range(0, len(all_tables), 60):
        chunk = all_tables[i:i + 60]
        q = ("select table_schema||'.'||table_name||'|'||column_name from information_schema.columns where "
             + " or ".join(f"(table_schema='{s}' and table_name='{t}')" for s, t in chunk))
        for line in psql(q).splitlines():
            if "|" in line:
                key, col = line.split("|", 1)
                ddl_cols[key].add(col.lower())

    real_missing = {}
    for (s, t), cols in sorted(tables.items()):
        key = f"{s}.{t}"
        if key not in exists:
            continue
        if admission_only and not key.startswith(ADMISSION_PREFIXES):
            continue
        miss = sorted(c for c in cols if c.strip().lower() not in ddl_cols.get(key, set()))
        if miss:
            real_missing[key] = miss

    total = sum(len(v) for v in real_missing.values())
    print(f"# phantom-column drift (entity declares, DDL lacks) — "
          f"{'admission-family only' if admission_only else 'all schemas'}")
    print(f"TOTAL missing columns: {total} across {len(real_missing)} tables")
    for k, v in sorted(real_missing.items(), key=lambda kv: -len(kv[1])):
        print(f"  {k}: {len(v)} -> {', '.join(v)}")


if __name__ == "__main__":
    main()
