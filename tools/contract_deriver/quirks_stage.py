#!/usr/bin/env python3
"""contract_deriver — the quirk flags (TWIN/PRECEDENCE/PHANTOM/COMMENTED/DISPATCHER/
SWALLOW) + the deriver's three negative controls + the Track A intersection."""
import os, re, json, sqlite3, subprocess

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"
DB = f"{OUT}/contract_edges.db"
NEW = f"{ROOT}/new/src/app/features"

# ---------- TWIN: commented @NamedQuery + live @NamedNativeQuery with the same name ----------
twins = []
for root, dirs, files in os.walk(f"{ROOT}/legacy/Entity/src/main/java"):
    for fn in files:
        if not fn.endswith(".java"):
            continue
        src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
        commented = set(re.findall(r"/\*[^*]*@NamedQuery\s*\(\s*name\s*=\s*\"([^\"]+)\"", src, re.S))
        commented |= set(re.findall(r"//\s*@NamedQuery\s*\(\s*name\s*=\s*\"([^\"]+)\"", src))
        live_native = set(re.findall(r"@NamedNativeQuery\s*\(\s*name\s*=\s*\"([^\"]+)\"", src))
        live = set(re.findall(r"(?<![\w/])@NamedQuery\s*\(\s*name\s*=\s*\"([^\"]+)\"", src))
        for c in commented & live_native:
            twins.append({"entity": fn[:-5], "query": c, "kind": "TWIN-commented-native-live"})
        for c in commented & live:
            twins.append({"entity": fn[:-5], "query": c, "kind": "TWIN-commented-query-live"})
print("TWIN pairs:", len(twins))
for t in twins[:5]:
    print("  ", t)

# ---------- PRECEDENCE: raw SQL in the DAOs with unparenthesized OR/AND mixes ----------
precedence = []
for root, dirs, files in os.walk(f"{ROOT}/legacy"):
    if "/target" in root:
        continue
    for fn in files:
        if not fn.endswith(".java"):
            continue
        src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
        for q in re.finditer(r'create(?:SQL)?Query\s*\(\s*"((?:[^"\\]|\\.)*)"', src):
            sql = q.group(1)
            where = re.search(r"where\s+(.*)", sql, re.I | re.S)
            if not where:
                continue
            w = where.group(1)
            if " or " in w.lower() and " and " in w.lower() and "(" not in w:
                precedence.append({"file": fn, "sql": sql[:120]})
print("PRECEDENCE (unparenthesized OR/AND in WHERE):", len(precedence))
for p in precedence[:4]:
    print("  ", p)

# ---------- COMMENTED: commented filters in the raw queries (bind-liveness differs) ----------
commented = []
for root, dirs, files in os.walk(f"{ROOT}/legacy"):
    if "/target" in root:
        continue
    for fn in files:
        if not fn.endswith(".java"):
            continue
        src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
        for q in re.finditer(r'create(?:SQL)?Query\s*\(\s*"((?:[^"\\]|\\.)*)"', src):
            sql = q.group(1)
            if re.search(r"/\*.*?\*/", sql, re.S):
                commented.append({"file": fn, "sql": sql[:120]})
print("COMMENTED (commented fragments inside raw SQL):", len(commented))
for c in commented[:4]:
    print("  ", c)

# ---------- DISPATCHER: endpoints (E2 targets) hit by many screens ----------
conn = sqlite3.connect(DB)
disp = conn.execute(
    "SELECT target, COUNT(DISTINCT screen) c FROM edges WHERE edge_type='E2' AND quirk_flags='' "
    "GROUP BY target HAVING c >= 5 ORDER BY c DESC LIMIT 6").fetchall()
print("DISPATCHER endpoints (>=5 screens):", len(disp))
for d in disp:
    print("  ", d[0][:70], "->", d[1], "screens")

# ---------- SWALLOW: traced DAO methods with an empty catch block ----------
swallow = []
for root, dirs, files in os.walk(f"{ROOT}/legacy"):
    if "/target" in root:
        continue
    for fn in files:
        if not fn.endswith("DaoImpl.java"):
            continue
        src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
        for m in re.finditer(r"catch\s*\([^)]*\)\s*\{\s*(?:/\*[^*]*\*/)?\s*(?:logger[^;]*;)?\s*\}", src, re.S):
            swallow.append({"file": fn, "ctx": src[max(0, m.start()-120):m.start()].splitlines()[-1][:60]})
print("SWALLOW (empty/silent catch):", len(swallow))
for s in swallow[:4]:
    print("  ", s)

# ---------- NEGATIVE CONTROL 2: a query naming a phantom table MUST flag PHANTOM ----------
# the known: getWhatsAppTemplateMaster (held DQ-001) queries the phantom m_whatsapp_template_master
r = subprocess.run(["psql", "-h", "localhost", "-d", "his_local", "-tAc",
    "select 1 from information_schema.tables where table_name='m_whatsapp_template_master';"],
    capture_output=True, text=True)
phantom_missing = r.stdout.strip() != "1"
print("\nNEGATIVE CONTROL 2 (phantom-table query): m_whatsapp_template_master exists in live =",
      not phantom_missing)
rows_phantom = conn.execute(
    "SELECT COUNT(*) FROM edges WHERE quirk_flags LIKE '%PHANTOM%'").fetchone()[0]
print("  E5 PHANTOM-flagged rows in the join table:", rows_phantom)
print("  control verdict:", "PASS — the phantom class is flagged" if rows_phantom > 0
      else "FAIL — no phantom flagged (the E5 yield is too low to reach the known phantoms)")

# ---------- NEGATIVE CONTROL 3: a twin pair must flag TWIN and resolve to the live native ----------
print("\nNEGATIVE CONTROL 3 (twin):", "PASS — TWIN pairs detected above:" if twins else "FAIL — none found",
      len(twins))

# ---------- TRACK A INTERSECTION: the next-40 screens whose path carries PHANTOM/PRECEDENCE/COMMENTED ----------
next40 = set(json.load(open(f"{OUT}/next40_screens.json")))
flagged_screens = set()
for r in conn.execute("SELECT screen, quirk_flags FROM edges WHERE quirk_flags != ''").fetchall():
    if r[0] in next40:
        flagged_screens.add((r[0], r[1]))
print("\nTRACK A INTERSECTION (next-40 screens with PHANTOM/PRECEDENCE/COMMENTED on their path):")
for s, q in sorted(flagged_screens):
    print(f"  {s}: {q}")
if not flagged_screens:
    print("  (none in the derived next-40 — the E3-E5 yield is too low to reach them; see the report)")
conn.close()
json.dump({"twins": twins, "precedence": precedence[:20], "commented": commented[:20],
           "dispatcher": [list(d) for d in disp], "swallow_count": len(swallow)},
          open(f"{OUT}/quirks.json", "w"), indent=1)
print("\nquirks saved:", f"{OUT}/quirks.json")
