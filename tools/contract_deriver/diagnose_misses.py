#!/usr/bin/env python3
"""DIAGNOSIS (owner-directed, 2026-08-08): the deriver's back-test misses +
disagreements, full sets, mechanism-classified. No deriver fixes."""
import os, re, json, random

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"
NEW = f"{ROOT}/new/src/app/features"
JS_ROOT = f"{ROOT}/legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller"
random.seed(42)

def norm(s):
    return re.sub(r"[-_\s]", "", s).lower()
def normpath(p):
    p = p.split("?")[0].rstrip("/")
    p = re.sub(r"\{[^}]+\}", "*", p)
    p = re.sub(r"/\d+", "/*", p)
    return p

# ---------- the ground truth (full, per feature) ----------
truth = {}  # feat -> set of normpaths with the sample sites
HTTPCALL = re.compile(r"this\.http\.(get|post|put|delete|patch)<[^>]*>?\s*\(\s*([`'\"][^`'\"]+[`'\"])")
TEMPLATE = re.compile(r"`([^`]*)`")
for feat in os.listdir(NEW):
    fdir = os.path.join(NEW, feat)
    if not os.path.isdir(fdir) or feat.startswith("_"):
        continue
    sites = {}
    for root, dirs, files in os.walk(fdir):
        for fn in files:
            if not (fn.endswith(".service.ts") or fn.endswith(".ts")):
                continue
            src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
            for m in HTTPCALL.finditer(src):
                arg = m.group(2)
                tm = TEMPLATE.search(arg)
                p = None
                if tm:
                    mm = re.search(r"(/[A-Za-z0-9_\-/{}]+)", tm.group(1))
                    if mm:
                        p = mm.group(1)
                else:
                    q = re.search(r"[\"'](/?[A-Za-z0-9_\-/{}.]+)[\"']", arg)
                    if q:
                        p = q.group(1)
                if p:
                    sites.setdefault(normpath(p), (os.path.join(root, fn).replace(NEW + "/", ""), m.group(0)[:100]))
    if sites:
        truth[feat] = sites

# ---------- the derived E1 (full, per feature, from the join table) ----------
import sqlite3
conn = sqlite3.connect(f"{OUT}/contract_edges.db")
derived = {}
for r in conn.execute("SELECT screen, target FROM edges WHERE edge_type='E1'").fetchall():
    tgt = r[1]
    if tgt.startswith("<dynamic"):
        continue
    derived.setdefault(norm(r[0]), set()).add(normpath(tgt.split(" ", 1)[1]))
conn.close()

# ---------- the full miss + disagree sets ----------
misses = []   # (feat, normpath, site)
disagrees = []
for feat, actual in truth.items():
    f = norm(feat)
    if f not in derived:
        continue
    der = derived[f]
    act = set(actual)
    for p in sorted(act - der):
        misses.append((feat, p, actual[p]))
    for p in sorted(der - act):
        disagrees.append((feat, p, ""))

print(f"FULL misses: {len(misses)}  FULL disagrees: {len(disagrees)}")
json.dump({"misses": [[m[0], m[1], m[2]] for m in misses],
           "disagrees": [[d[0], d[1]] for d in disagrees]},
          open(f"{OUT}/diagnosis_sets.json", "w"), indent=1)

# ---------- the legacy JS index: controller file -> full text ----------
legacy_js = {}
for root, dirs, files in os.walk(JS_ROOT):
    for fn in files:
        if fn.endswith(".js"):
            legacy_js[fn] = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
# the route rows (state -> controller file)
index = json.load(open(f"{OUT}/index.json"))
state_ctrl = {}
for row in index["route_rows"]:
    fields = [f.strip() for f in row["row"].split("|")]
    if len(fields) > 4 and fields[4]:
        state_ctrl.setdefault(norm(row["state"]), fields[4])

# ---------- the mechanism classification for the misses ----------
def last_seg(p):
    m = re.search(r"([A-Za-z][A-Za-z0-9_]*)$", p)
    return m.group(1).lower() if m else p.lower()

def classify_miss(feat, p):
    """why did the deriver not derive this endpoint for this screen?"""
    seg = last_seg(p)
    f = norm(feat)
    ctrl_file = state_ctrl.get(f)
    # 1. is the endpoint in the screen's OWN legacy controller?
    if ctrl_file and ctrl_file in legacy_js:
        src = legacy_js[ctrl_file]
        if seg in src.lower():
            # the call exists in the legacy JS but the E1 missed it -> find the call form
            idx = src.lower().find(seg)
            ctx = src[max(0, idx - 220):idx + 40]
            if re.search(r"serviceAction", ctx):
                return "M1-varURI", ctx[-90:]
            if re.search(r"\$http\.", ctx):
                return "M1-http", ctx[-90:]
            if re.search(r"\.(?:get|post|put|delete)Data?\s*\(", ctx):
                return "M2-wrapper", ctx[-90:]
            return "M1-other", ctx[-90:]
        return "M3-legacy-other-controller", f"state controller {ctrl_file} has no {seg}"
    # 2. no state mapping / no controller -> the rebuild's own endpoint
    return "M4-rebuild-new", f"no legacy state match (feature {feat})"

mc = {}
for feat, p, site in misses:
    mech, ctx = classify_miss(feat, p)
    mc.setdefault(mech, []).append((feat, p, site, ctx))
for mech, items in sorted(mc.items(), key=lambda kv: -len(kv[1])):
    print(f"\n{mech}: {len(items)}")
    for feat, p, site, ctx in items[:3]:
        print(f"    {feat[:22]:22} {p[:44]:44} | {ctx[-70:]}")

json.dump({k: [[i[0], i[1], i[3]] for i in v] for k, v in mc.items()},
          open(f"{OUT}/diagnosis_miss_mechanisms.json", "w"), indent=1)
