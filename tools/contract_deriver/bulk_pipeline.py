#!/usr/bin/env python3
"""contract_deriver — the bulk pipeline: E1-E6 edges for the next-40 + rebuilt-222
screens, the quirks, and the SQLite join table. One row per edge."""
import os, re, json, sqlite3, sys

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"
DB = f"{OUT}/contract_edges.db"

sys.path.insert(0, f"{ROOT}/tools/contract_deriver")
import stage_e234 as s234

# ---------- the scope: next-40 + rebuilt-222 ----------
next40 = set(json.load(open(f"{OUT}/next40_screens.json")))
index = json.load(open(f"{OUT}/index.json"))
rebuilt = set(index["new_features"])
# rebuilt screens' state names: the new/ feature names, normalized
def norm(s):
    return re.sub(r"[-_\s]", "", s).lower()
built_norm = {norm(f): f for f in rebuilt}
e1 = json.load(open(f"{OUT}/e1_edges.json"))
print("E1 edges loaded:", len(e1))

# ---------- E5: named queries -> the entity's query string -> tables ----------
# entities' @NamedQuery/@NamedNativeQuery definitions
ENTITY_QUERIES = {}
for p, src in s234.JAVA.items():
    for q in re.finditer(r'@Named(?:Native)?Query\s*\(\s*name\s*=\s*"([^"]+)"\s*,\s*query\s*=\s*"((?:[^"\\]|\\.)*)"', src):
        qname, qtext = q.group(1), q.group(2)
        # tables in the query: the FROM/JOIN/UPDATE/INSERT targets
        tables = set(re.findall(r"\b(?:from|join|update|into)\s+([a-zA-Z_][\w.]*)", qtext, re.I))
        tables = {t for t in tables if not t.lower() in ("select", "where", "set", "values")}
        ENTITY_QUERIES.setdefault(qname, []).append({"tables": sorted(tables), "entity": os.path.basename(p)[:-5]})

# live schema tables (for the phantom checks)
import subprocess
r = subprocess.run(["psql", "-h", "localhost", "-d", "his_local", "-tAc",
    "select table_schema||'.'||table_name from information_schema.tables "
    "where table_schema not in ('pg_catalog','information_schema');"], capture_output=True, text=True)
live_tables = {t.strip() for t in r.stdout.splitlines() if t.strip()}
def table_exists(t):
    if t in live_tables:
        return True
    base = t.split(".")[-1]
    return any(k.split(".")[-1] == base for k in live_tables)

# ---------- the bulk trace ----------
rows = []
edge_id = 0
broken_e2 = 0
scanned = 0
for e in e1:
    # scope filter: only the next-40 + the rebuilt screens
    state = e["screen"]
    if state not in next40 and norm(state) not in built_norm:
        continue
    scanned += 1
    http = e["target"].split(" ")[0]
    path = e["target"].split(" ", 1)[1]
    edge_id += 1
    if path.startswith("<dynamic"):
        rows.append({"screen": state, "edge_type": "E1", "source": state, "target": e["target"],
                     "derivation_method": "static-callsite", "confidence": "weak", "validated": "no",
                     "quirk_flags": "", "detail": "unresolved dynamic URI"})
        continue
    rows.append({"screen": state, "edge_type": "E1", "source": state, "target": e["target"],
                 "derivation_method": "static-callsite", "confidence": e["confidence"], "validated": "no",
                 "quirk_flags": "", "detail": e["detail"]})
    # E2
    hits = s234.e2_match(http, path)
    if not hits:
        broken_e2 += 1
        rows.append({"screen": state, "edge_type": "E2", "source": path, "target": "<no-controller>",
                     "derivation_method": "static-map", "confidence": "weak", "validated": "no",
                     "quirk_flags": "BROKEN", "detail": f"{http} {path}"})
        continue
    c = hits[0]
    rows.append({"screen": state, "edge_type": "E2", "source": path,
                 "target": f"{c['class']}.{c['method']}", "derivation_method": "static-map",
                 "confidence": "strong", "validated": "no", "quirk_flags": "",
                 "detail": f"{http} -> {c['file']}"})
    # E3 + E4
    ctrl_file = s234.CLASS2PATH.get(c["class"], "")
    svc_calls = s234.field_service(ctrl_file, c["method"]) if ctrl_file else None
    if not svc_calls:
        rows.append({"screen": state, "edge_type": "E3", "source": f"{c['class']}.{c['method']}",
                     "target": "<no-service-call>", "derivation_method": "static-chain",
                     "confidence": "weak", "validated": "no", "quirk_flags": "BROKEN", "detail": ""})
        continue
    for svc, m in svc_calls[:2]:
        impl = s234.service_to_impl(svc)
        dao_calls = s234.impl_dao_calls(impl, m) if impl else []
        for dao, dm in dao_calls[:2]:
            cands = [dao, dao + "Impl", dao.lstrip("i") + "Impl"]
            dao_path = next((s234.CLASS2PATH.get(x) for x in cands if s234.CLASS2PATH.get(x)), None)
            qrefs = s234.dao_query_refs(dao_path, dm) if dao_path else []
            rows.append({"screen": state, "edge_type": "E3", "source": f"{svc}.{m}",
                         "target": f"{dao}.{dm}", "derivation_method": "static-chain",
                         "confidence": "strong" if dao_path else "weak", "validated": "no",
                         "quirk_flags": "BROKEN" if not dao_path else "", "detail": ""})
            for q in qrefs[:4]:
                if q["kind"] == "named":
                    qdefs = ENTITY_QUERIES.get(q["name"], [])
                    tables = qdefs[0]["tables"] if qdefs else []
                    flags = []
                    phantom_tbls = [t for t in tables if not table_exists(t)]
                    if phantom_tbls:
                        flags.append("PHANTOM:" + ",".join(phantom_tbls))
                    rows.append({"screen": state, "edge_type": "E4", "source": f"{dao}.{dm}",
                                 "target": f"named:{q['name']}", "derivation_method": "static-chain",
                                 "confidence": "strong", "validated": "no",
                                 "quirk_flags": ";".join(flags),
                                 "detail": f"tables={tables} entity={qdefs[0]['entity'] if qdefs else '?'}"})
                    for t in tables:
                        rows.append({"screen": state, "edge_type": "E5", "source": q["name"],
                                     "target": t, "derivation_method": "static",
                                     "confidence": "strong", "validated": "no",
                                     "quirk_flags": "PHANTOM" if not table_exists(t) else "",
                                     "detail": ""})
                else:
                    tables = set(re.findall(r"\b(?:from|join|update|into)\s+([a-zA-Z_][\w.]*)", q["sql"], re.I))
                    tables = {t for t in tables if not t.lower() in ("select", "where", "set", "values")}
                    ph = [t for t in tables if not table_exists(t)]
                    rows.append({"screen": state, "edge_type": "E4", "source": f"{dao}.{dm}",
                                 "target": f"raw:{q['sql'][:60]}", "derivation_method": "static-chain",
                                 "confidence": "strong", "validated": "no",
                                 "quirk_flags": ("PHANTOM:" + ",".join(ph)) if ph else "",
                                 "detail": f"tables={sorted(tables)}"})
                    for t in tables:
                        rows.append({"screen": state, "edge_type": "E5", "source": f"raw:{dao}.{dm}",
                                     "target": t, "derivation_method": "static",
                                     "confidence": "strong", "validated": "no",
                                     "quirk_flags": "PHANTOM" if not table_exists(t) else "",
                                     "detail": ""})

print("scanned E1 edges (in scope):", scanned)
print("total rows:", len(rows))
print("broken E2:", broken_e2)
print("E5 phantom-flagged rows:", sum(1 for r in rows if r["edge_type"] == "E5" and "PHANTOM" in r["quirk_flags"]))
for et in ("E1", "E2", "E3", "E4", "E5"):
    print(f"  {et}: {sum(1 for r in rows if r['edge_type'] == et)}")

# ---------- the SQLite join table ----------
conn = sqlite3.connect(DB)
conn.execute("DROP TABLE IF EXISTS edges")
conn.execute("""CREATE TABLE edges (
  screen TEXT, edge_type TEXT, source TEXT, target TEXT,
  derivation_method TEXT, confidence TEXT, validated TEXT, quirk_flags TEXT, detail TEXT)""")
conn.executemany("INSERT INTO edges VALUES (?,?,?,?,?,?,?,?,?)",
                 [(r["screen"], r["edge_type"], r["source"], r["target"],
                   r["derivation_method"], r["confidence"], r["validated"], r["quirk_flags"], r["detail"]) for r in rows])
conn.commit()
print("join table:", DB, "rows:", conn.execute("SELECT COUNT(*) FROM edges").fetchone()[0])
conn.close()
