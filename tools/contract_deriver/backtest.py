#!/usr/bin/env python3
"""contract_deriver — THE BACK-TEST: for the rebuilt screens, compare the derived
E1 (screen -> endpoint) edges against what the rebuild ACTUALLY calls (the new/
services' HTTP client code = ground truth, it works). Per edge type: derived,
agreeing, disagreeing, missed, error rate."""
import os, re, json, sqlite3

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
OUT = f"{ROOT}/tools/contract_deriver/out"
NEW = f"{ROOT}/new/src/app/features"
DB = f"{OUT}/contract_edges.db"

def norm(s):
    return re.sub(r"[-_\s]", "", s).lower()

# ---------- ground truth: the rebuilt screens' actual HTTP calls ----------
# the new/ services use HttpClient: this.http.get<...>(`${base}/path`) or ('/path')
truth = {}  # norm(feature) -> set of endpoint paths
HTTPCALL = re.compile(r"this\.http\.(get|post|put|delete|patch)<[^>]*>?\s*\(\s*([`'\"][^`'\"]+[`'\"])")
TEMPLATE = re.compile(r"`([^`]*)`")
for feat in os.listdir(NEW):
    fdir = os.path.join(NEW, feat)
    if not os.path.isdir(fdir) or feat.startswith("_"):
        continue
    paths = set()
    for root, dirs, files in os.walk(fdir):
        for fn in files:
            if not (fn.endswith(".service.ts") or fn.endswith(".ts")):
                continue
            src = open(os.path.join(root, fn), encoding="utf-8", errors="replace").read()
            for m in HTTPCALL.finditer(src):
                arg = m.group(2)
                tm = TEMPLATE.search(arg)
                if tm:
                    t = tm.group(1)
                    # the path segment: the last quoted absolute-ish path in the template
                    seg = re.search(r"(/[A-Za-z0-9_\-/{}$.:]*)?(/[A-Za-z0-9_\-/{}]+)", t)
                    p = re.search(r"(/[A-Za-z0-9_\-/{}]+)", t)
                    if p:
                        paths.add(p.group(1))
                else:
                    q = re.search(r"[\"'](/?[A-Za-z0-9_\-/{}.]+)[\"']", arg)
                    if q:
                        paths.add(q.group(1))
    if paths:
        truth[norm(feat)] = paths
print("rebuilt screens with captured actual calls:", len(truth))
all_truth = set()
for p in truth.values():
    all_truth |= p
print("total distinct actual endpoint paths (ground truth):", len(all_truth))
for f, ps in list(truth.items())[:2]:
    print("  sample", f, sorted(ps)[:4])

# ---------- the derived E1 for the rebuilt screens ----------
conn = sqlite3.connect(DB)
derived = {}  # norm(screen) -> set of derived endpoint paths
for r in conn.execute("SELECT screen, target FROM edges WHERE edge_type='E1'").fetchall():
    d = norm(r[0])
    tgt = r[1]
    if tgt.startswith("<dynamic"):
        continue
    path = tgt.split(" ", 1)[1]
    derived.setdefault(d, set()).add(path)
print("rebuilt screens with derived E1:", len(derived))

def normpath(p):
    """normalize an endpoint path for comparison: strip the trailing slash,
    collapse the numeric/param segments to a wildcard, drop the query."""
    p = p.split("?")[0].rstrip("/")
    p = re.sub(r"\{[^}]+\}", "*", p)
    p = re.sub(r"/\d+", "/*", p)
    return p

# ---------- the comparison per screen ----------
agree = disagree = miss = 0
screens_with_truth = 0
disagreement_list = []
for feat, actual in truth.items():
    if feat not in derived:
        continue
    screens_with_truth += 1
    der = {normpath(p) for p in derived[feat]}
    act = {normpath(p) for p in actual}
    common = der & act
    only_der = der - act
    only_act = act - der
    agree += len(common)
    disagree += len(only_der)
    miss += len(only_act)
    for p in sorted(only_der)[:3]:
        disagreement_list.append(("disagree", feat, p))
    for p in sorted(only_act)[:3]:
        disagreement_list.append(("missed", feat, p))

total = agree + disagree + miss
print(f"\nscreens compared: {screens_with_truth}")
print(f"per-edge-type (E1): derived-in-scope={sum(len(v) for v in derived.values())} "
      f"agreeing={agree} disagreeing={disagree} missed={miss} total={total}")
if total:
    print(f"error rate E1 = (disagree + missed)/total = {(disagree + miss) / total:.3f} "
          f"({disagree + miss}/{total})")
print("\nsample disagreements (first 12):")
for kind, feat, p in disagreement_list[:12]:
    print(f"  {kind:8} {feat[:30]:30} {p[:50]}")
conn.close()
json.dump({"screens_compared": screens_with_truth, "agree": agree, "disagree": disagree,
           "miss": miss, "total": total, "disagreements": disagreement_list},
          open(f"{OUT}/backtest_e1.json", "w"), indent=1)
print("\nbacktest saved:", f"{OUT}/backtest_e1.json")
