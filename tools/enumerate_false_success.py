#!/usr/bin/env python3
"""false-success write class enumeration (owner directive 2026-08-10).

SCOPE: enumerate and classify ONLY. No fixes.

Class definition (the operation-theatre mechanism, BR-187):
  A SERVICE method that returns a SUCCESS envelope at a point where the
  method's own result guard is NOT satisfied — i.e. the SUCCESS return sits
  at a nesting level OUTSIDE every `if (result != null)`-style guard, and
  the path to it runs through a DAO call whose failure was converted into a
  return value (catch-and-return) rather than a propagated exception.

Static proxies applied (per file, per method):
  P1  DAO-call site: a line calling a method whose name starts with
      save/update/delete/status/get... in the same module's DAO.
  P2  catch-and-return DAO: any DAO method containing `catch` followed by a
      `return new Response<>(ERROR` (or COMMON_ERROR) — i.e. the swallow
      shape.
  P3  guard-outside-return: a `return new Response<>(SUCCESS` line whose
      brace depth is EQUAL TO OR LESS THAN the depth of a preceding
      `if (... getObject()!=null)` / `if(res != null)` guard in the SAME
      method — the SUCCESS return is outside the guard.
  P4  service method whose body contains BOTH a P3 SUCCESS return AND a DAO
      call to a P2 DAO method.

These are HEURISTICS (static, brace-depth based), not proof — each flagged
method is recorded with its file:line so a human can confirm. The report
separates CONFIRMED-pattern (P1+P2+P3+P4 all present) from PARTIAL.
"""
import os
import re
import sys
from collections import defaultdict

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
MODULES = ["Global", "OTIMS", "ADT", "Billing", "Pharmacy", "Inventory",
           "PatientRegistration", "Radiology", "Laboratory", "Pathology",
           "Nursing", "DietAndKitchen", "OPD", "AssetManagement", "Entity"]

SERVICE_DIRS = []
for m in MODULES:
    base = os.path.join(ROOT, "legacy", m, "src", "main", "java")
    if os.path.isdir(base):
        for dirpath, dirnames, filenames in os.walk(base):
            for f in filenames:
                if f.endswith("ServiceImpl.java"):
                    SERVICE_DIRS.append(os.path.join(dirpath, f))

print(f"scanning {len(SERVICE_DIRS)} service impl files\n")

# ---- P2: DAO catch-and-return ----
P2_DAOS = set()   # (file, method_name)
for m in MODULES:
    base = os.path.join(ROOT, "legacy", m, "src", "main", "java")
    if not os.path.isdir(base):
        continue
    for dirpath, dirnames, filenames in os.walk(base):
        for f in filenames:
            if f.endswith("DaoImpl.java"):
                path = os.path.join(dirpath, f)
                try:
                    src = open(path, encoding="utf-8", errors="replace").read()
                except OSError:
                    continue
                # method split by 'public Response' / 'public Response<'
                methods = re.split(r"\n\s*public\s+Response(?:<[^>]+>)?\s+(\w+)\s*\(", src)
                # methods[0] is preamble; then pairs (name, body)
                for i in range(1, len(methods), 2):
                    name = methods[i]
                    body = methods[i + 1] if i + 1 < len(methods) else ""
                    # catch-and-return: has a catch and a return new Response<>(ERROR/COMMON_ERROR
                    if re.search(r"catch\s*\(", body) and re.search(r"return\s+new\s+Response<>(?:<[^>]+>)?\(\s*(?:ERROR|COMMON_ERROR|ERR_500)", body):
                        P2_DAOS.add((os.path.relpath(path, ROOT), name))

print(f"P2 DAO catch-and-return methods: {len(P2_DAOS)}")
for f, n in sorted(P2_DAOS)[:8]:
    print(f"   {f} :: {n}")
print()

# ---- P1/P3/P4: services ----
def brace_depths(lines):
    """Return list of (line_idx, depth_at_line_start) for 'return' lines."""
    out = []
    depth = 0
    for i, ln in enumerate(lines):
        out.append((i, depth, ln))
        depth += ln.count("{") - ln.count("}")
    return out

CONFIRMED = []   # (module, svc_file, svc_method, daos_called, guard_line, success_line)
PARTIAL = []     # (module, svc_file, svc_method, reason)
TOTAL_SVC_METHODS = 0

for svc_path in SERVICE_DIRS:
    rel = os.path.relpath(svc_path, ROOT)
    module = rel.split(os.sep)[1]
    try:
        src = open(svc_path, encoding="utf-8", errors="replace").read()
    except OSError:
        continue
    methods = re.split(r"\n\s*public\s+Response(?:<[^>]+>)?\s+(\w+)\s*\(", src)
    for i in range(1, len(methods), 2):
        name = methods[i]
        body = methods[i + 1] if i + 1 < len(methods) else ""
        TOTAL_SVC_METHODS += 1
        lines = body.split("\n")
        # find SUCCESS returns with depth
        success_returns = []
        depth = 0
        for li, ln in enumerate(lines):
            depth_now = depth
            depth += ln.count("{") - ln.count("}")
            if re.search(r"return\s+new\s+Response<>(?:<[^>]+>)?\(\s*SUCCESS", ln):
                success_returns.append((li, depth_now))
        if not success_returns:
            continue
        # find guards in the method
        guard_lines = []
        for li, ln in enumerate(lines):
            if re.search(r"if\s*\(.*(?:getObject\(\)\s*!=|!=?\s*null|getStatus\(\)\s*[!=]=|getListObject\(\)\s*[!=]=)", ln):
                guard_lines.append(li)
        if not guard_lines:
            # SUCCESS return with NO result guard at all in the method
            PARTIAL.append((module, rel, name, "SUCCESS return but NO result guard in method"))
            continue
        # P3: a SUCCESS return OUTSIDE all guards (its depth <= a guard's depth
        # AND it is not textually inside any guard block)
        outside = []
        for (sli, sdepth) in success_returns:
            # is this SUCCESS return inside any guard block?
            inside = False
            for gli in guard_lines:
                if gli < sli:
                    # guard spans from gli; find its closing brace
                    sub = lines[gli:sli + 1]
                    gdepth = 0
                    for subln in sub:
                        gdepth += subln.count("{") - subln.count("}")
                        if gdepth <= 0:
                            break
                    # if the guard's block closed before sli, SUCCESS is outside it
                    # (approx: compute net depth of lines between gli and sli)
                    net = sum((ln2.count("{") - ln2.count("}")) for ln2 in lines[gli:sli])
                    if net > 0 and net - (lines[sli].count("{") - lines[sli].count("}")) <= 0:
                        inside = True
                        break
            if not inside:
                outside.append((sli, sdepth))
        if not outside:
            continue
        # P1: DAO calls in the method (to any P2 DAO method)
        daos_called = set()
        for d_file, d_method in P2_DAOS:
            if re.search(r"\b" + re.escape(d_method) + r"\s*\(", body):
                daos_called.add(d_method)
        if daos_called:
            CONFIRMED.append((module, rel, name, sorted(daos_called),
                              guard_lines[0], outside[0][0]))
        else:
            PARTIAL.append((module, rel, name,
                            "SUCCESS outside guard but DAO call not matched to a P2 DAO"))

print(f"TOTAL service methods scanned: {TOTAL_SVC_METHODS}")
print(f"CONFIRMED (SUCCESS-outside-guard AND P2 DAO call): {len(CONFIRMED)}")
print(f"PARTIAL: {len(PARTIAL)}\n")

# order CONFIRMED by module importance: billing/clinical/patient first
ORDER = {"Billing": 0, "ADT": 1, "PatientRegistration": 2, "OPD": 3,
         "Nursing": 4, "Pharmacy": 5, "Inventory": 6, "Radiology": 7,
         "Laboratory": 8, "Pathology": 9, "Global": 10, "OTIMS": 11,
         "DietAndKitchen": 12, "AssetManagement": 13}
CONFIRMED.sort(key=lambda x: (ORDER.get(x[0], 99), x[2]))

print("===== CONFIRMED (top 40 by path importance) =====")
for module, rel, name, daos, gl, sl in CONFIRMED[:40]:
    print(f"{module:<20} {name:<45} guard~L{gl} success~L{sl} daos={','.join(daos[:3])}")
print()
print(f"...and {max(0, len(CONFIRMED)-40)} more confirmed")

print("\n===== PARTIAL (top 15) =====")
for module, rel, name, reason in PARTIAL[:15]:
    print(f"{module:<20} {name:<45} {reason}")

import json
json.dump({"confirmed": [(m, n, d, gl, sl) for m, r, n, d, gl, sl in CONFIRMED],
           "partial": [(m, n, r) for m, r, n, r2 in PARTIAL]},
          open("/tmp/false_success_enum.json", "w"), indent=1)
print("\nwrote /tmp/false_success_enum.json")
