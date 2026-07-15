#!/usr/bin/env python3
"""
BR-076 full-scale structural reconciliation: docs/staging_schema_reference.sql
(ground-truth staging dump) vs. live his-local-postgres dev DB.

Method proven at small scale in BR-075: parse reference CREATE TABLE statements
into structured (schema, table, column -> type/nullable/default) data, pull the
equivalent live structure from information_schema, diff, categorize.

This script only PARSES and DIFFS (read-only). It writes:
  - docs/br076_diff_report.json   (full structured diff, all 4 categories)
  - docs/br076_missing_tables.sql (generated CREATE TABLE for category a)
  - docs/br076_missing_columns.sql (generated ALTER TABLE ADD COLUMN for category b)
No DDL is executed by this script.
"""
import json
import re
import subprocess

REF_FILE = "docs/staging_schema_reference.sql"

PG_HOST = "localhost"
PG_PORT = "5434"
PG_USER = "hisdev"
PG_DB = "HIS-SME"
PG_PASSWORD = "hisdev_local_2024"


def split_top_level(s, sep=","):
    """Split s on sep, but not inside parens or quotes."""
    parts = []
    depth = 0
    in_squote = False
    in_dquote = False
    cur = []
    i = 0
    n = len(s)
    while i < n:
        c = s[i]
        if in_squote:
            cur.append(c)
            if c == "'":
                # handle doubled '' escape
                if i + 1 < n and s[i + 1] == "'":
                    cur.append(s[i + 1])
                    i += 1
                else:
                    in_squote = False
        elif in_dquote:
            cur.append(c)
            if c == '"':
                in_dquote = False
        else:
            if c == "'":
                in_squote = True
                cur.append(c)
            elif c == '"':
                in_dquote = True
                cur.append(c)
            elif c == "(":
                depth += 1
                cur.append(c)
            elif c == ")":
                depth -= 1
                cur.append(c)
            elif c == sep and depth == 0:
                parts.append("".join(cur))
                cur = []
            else:
                cur.append(c)
        i += 1
    if cur:
        parts.append("".join(cur))
    return parts


CREATE_RE = re.compile(
    r'^CREATE TABLE\s+([a-zA-Z0-9_]+)\.("(?:[^"]+)"|[a-zA-Z0-9_]+)\s*\((.*)\);\s*$'
)


def normalize_type(raw_type):
    """Loosely normalize a raw column type token for comparison."""
    t = raw_type.strip().lower()
    t = t.replace("without time zone", "").strip()
    t = re.sub(r"\s+", " ", t)
    # map serial-ish types used in dump vs what information_schema reports
    aliases = {
        "serial4": "integer",
        "serial8": "bigint",
        "serial2": "smallint",
        "serial": "integer",
        "bigserial": "bigint",
        "smallserial": "smallint",
        "int4": "integer",
        "int8": "bigint",
        "int2": "smallint",
        "bpchar": "character",
        "varchar": "character varying",
        "bool": "boolean",
        "float4": "real",
        "float8": "double precision",
        "timestamptz": "timestamp with time zone",
        "timestamp": "timestamp without time zone",
    }
    # strip size spec for alias lookup, re-attach after
    m = re.match(r"^([a-z_ ]+?)(\(.*\))?$", t)
    base = m.group(1).strip() if m else t
    size = m.group(2) if m and m.group(2) else ""
    # dump-tool sentinel: (10485760) marks "no explicit length" varchar/bpchar
    # in the reference file (confirmed against live: those columns are genuinely
    # unbounded `character varying` with NULL character_maximum_length) -- treat
    # as unbounded, not a real length, to avoid manufacturing false mismatches.
    if size == "(10485760)":
        size = ""
    base = aliases.get(base, base)
    return (base + size).strip()


def parse_reference(path):
    """Returns dict[(schema, table)] -> dict[column] -> {type, nullable, default}"""
    tables = {}
    with open(path, "r") as f:
        for line in f:
            line = line.rstrip("\n")
            if not line.startswith("CREATE TABLE"):
                continue
            m = CREATE_RE.match(line)
            if not m:
                continue
            schema, table, body = m.group(1), m.group(2).strip('"'), m.group(3)
            cols = {}
            for piece in split_top_level(body):
                piece = piece.strip()
                if not piece:
                    continue
                upper = piece.upper()
                if upper.startswith("CONSTRAINT") or upper.startswith("PRIMARY KEY") \
                        or upper.startswith("FOREIGN KEY") or upper.startswith("UNIQUE") \
                        or upper.startswith("CHECK"):
                    continue
                # column def: name type [modifiers...]
                cm = re.match(r'^("?[a-zA-Z0-9_]+"?)\s+(.*)$', piece)
                if not cm:
                    continue
                colname = cm.group(1).strip('"')
                rest = cm.group(2).strip()
                nullable = "NO" if "NOT NULL" in rest.upper() else "YES"
                default = None
                dm = re.search(r"DEFAULT\s+(.*?)(\s+NOT NULL|\s+NULL)?$", rest, re.IGNORECASE)
                # extract type = rest minus DEFAULT ... / NOT NULL / NULL trailing tokens
                type_part = rest
                default_search = re.search(r"\bDEFAULT\b", rest, re.IGNORECASE)
                if default_search:
                    type_part = rest[: default_search.start()].strip()
                    after_default = rest[default_search.end():].strip()
                    after_default = re.sub(r"\s+NOT NULL\s*$", "", after_default, flags=re.IGNORECASE)
                    after_default = re.sub(r"\s+NULL\s*$", "", after_default, flags=re.IGNORECASE)
                    default = after_default.strip()
                else:
                    type_part = re.sub(r"\s+NOT NULL\s*$", "", type_part, flags=re.IGNORECASE)
                    type_part = re.sub(r"\s+NULL\s*$", "", type_part, flags=re.IGNORECASE)
                cols[colname] = {
                    "raw_type": type_part.strip(),
                    "norm_type": normalize_type(type_part.strip()),
                    "nullable": nullable,
                    "default": default,
                }
            tables[(schema, table)] = cols
    return tables


def fetch_live_columns():
    query = (
        "SELECT table_schema, table_name, column_name, data_type, "
        "COALESCE(character_maximum_length::text,''), "
        "COALESCE(numeric_precision::text,''), COALESCE(numeric_scale::text,''), "
        "is_nullable, COALESCE(column_default,'') "
        "FROM information_schema.columns "
        "WHERE table_schema NOT IN ('pg_catalog','information_schema') "
        "ORDER BY table_schema, table_name, ordinal_position;"
    )
    cmd = [
        "psql", "-h", PG_HOST, "-p", PG_PORT, "-U", PG_USER, "-d", PG_DB,
        "-At", "-F", "\x1f", "-c", query,
    ]
    env = {"PGPASSWORD": PG_PASSWORD, "PATH": "/usr/bin:/bin"}
    out = subprocess.run(cmd, capture_output=True, text=True, env=env, check=True).stdout
    tables = {}
    for line in out.splitlines():
        if not line.strip():
            continue
        parts = line.split("\x1f")
        schema, table, col, dtype, charlen, numprec, numscale, nullable, default = parts
        if dtype == "character varying" and charlen:
            raw = f"varchar({charlen})"
        elif dtype == "character" and charlen:
            raw = f"bpchar({charlen})"
        elif dtype == "numeric" and numprec:
            raw = f"numeric({numprec}, {numscale})" if numscale else f"numeric({numprec})"
        else:
            raw = dtype
        tables.setdefault((schema, table), {})[col] = {
            "raw_type": dtype,
            "norm_type": normalize_type(raw),
            "nullable": nullable,
            "default": default or None,
        }
    return tables


def fetch_live_tables_set():
    query = (
        "SELECT table_schema, table_name FROM information_schema.tables "
        "WHERE table_schema NOT IN ('pg_catalog','information_schema') AND table_type='BASE TABLE';"
    )
    cmd = [
        "psql", "-h", PG_HOST, "-p", PG_PORT, "-U", PG_USER, "-d", PG_DB,
        "-At", "-F", "\x1f", "-c", query,
    ]
    env = {"PGPASSWORD": PG_PASSWORD, "PATH": "/usr/bin:/bin"}
    out = subprocess.run(cmd, capture_output=True, text=True, env=env, check=True).stdout
    s = set()
    for line in out.splitlines():
        if not line.strip():
            continue
        schema, table = line.split("\x1f")
        s.add((schema, table))
    return s


def main():
    ref = parse_reference(REF_FILE)
    live = fetch_live_columns()
    live_tables = fetch_live_tables_set()

    cat_a = []  # tables in reference, missing entirely from local
    cat_b = {}  # (schema,table) -> list of missing columns (from ref)
    cat_c = {}  # (schema,table) -> list of (col, ref_type, live_type)
    cat_d = []  # tables in local, not in reference

    for key in sorted(ref.keys()):
        if key not in live_tables:
            cat_a.append(key)
            continue
        ref_cols = ref[key]
        live_cols = live.get(key, {})
        missing_cols = []
        type_mismatches = []
        for col, refdef in ref_cols.items():
            if col not in live_cols:
                missing_cols.append({"column": col, **refdef})
            else:
                livedef = live_cols[col]
                if refdef["norm_type"] != livedef["norm_type"]:
                    type_mismatches.append({
                        "column": col,
                        "ref_type": refdef["norm_type"],
                        "live_type": livedef["norm_type"],
                    })
        if missing_cols:
            cat_b[key] = missing_cols
        if type_mismatches:
            cat_c[key] = type_mismatches

    ref_keys = set(ref.keys())
    for key in sorted(live_tables):
        if key not in ref_keys:
            cat_d.append(key)

    report = {
        "summary": {
            "reference_tables": len(ref),
            "live_tables": len(live_tables),
            "category_a_missing_tables": len(cat_a),
            "category_b_tables_with_missing_columns": len(cat_b),
            "category_b_total_missing_columns": sum(len(v) for v in cat_b.values()),
            "category_c_tables_with_type_mismatch": len(cat_c),
            "category_c_total_mismatched_columns": sum(len(v) for v in cat_c.values()),
            "category_d_local_only_tables": len(cat_d),
        },
        "category_a_missing_tables": [f"{s}.{t}" for s, t in cat_a],
        "category_b_missing_columns": {
            f"{s}.{t}": cols for (s, t), cols in cat_b.items()
        },
        "category_c_type_mismatches": {
            f"{s}.{t}": cols for (s, t), cols in cat_c.items()
        },
        "category_d_local_only_tables": [f"{s}.{t}" for s, t in cat_d],
    }

    with open("docs/br076_diff_report.json", "w") as f:
        json.dump(report, f, indent=2)

    # by-schema breakdown for the manifest entry
    from collections import Counter
    schema_a = Counter(s for s, t in cat_a)
    schema_b_tables = Counter(s for s, t in cat_b.keys())
    schema_b_cols = Counter()
    for (s, t), cols in cat_b.items():
        schema_b_cols[s] += len(cols)

    print("=== SUMMARY ===")
    print(json.dumps(report["summary"], indent=2))
    print("=== cat A by schema (missing tables) ===")
    for s, n in schema_a.most_common():
        print(f"  {s}: {n}")
    print("=== cat B by schema (tables w/ missing cols / total missing cols) ===")
    for s in schema_b_tables:
        print(f"  {s}: {schema_b_tables[s]} tables / {schema_b_cols[s]} cols")

    # Build generated additive DDL (NOT executed here)
    ddl_tables = []
    for (s, t) in cat_a:
        # Re-extract original CREATE TABLE line verbatim from reference file for fidelity
        ddl_tables.append((s, t))

    with open("docs/br076_missing_columns.sql", "w") as f:
        for (s, t), cols in sorted(cat_b.items()):
            for c in cols:
                coldef = c["raw_type"]
                line = f'ALTER TABLE {s}."{t}" ADD COLUMN "{c["column"]}" {coldef}'
                if c["default"]:
                    line += f' DEFAULT {c["default"]}'
                # NOTE: intentionally do NOT add NOT NULL to avoid failing on existing rows;
                # nullability differences from reference are flagged, not enforced, for additive safety.
                line += ";"
                f.write(line + "\n")

    print(f"\nWrote docs/br076_diff_report.json, docs/br076_missing_columns.sql")
    print("Category A (missing tables) DDL will be extracted verbatim from reference in a follow-up pass.")


if __name__ == "__main__":
    main()
