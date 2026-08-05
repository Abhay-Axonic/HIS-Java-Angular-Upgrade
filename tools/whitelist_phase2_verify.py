#!/usr/bin/env python3
"""PHASE 2 verification: every one of the 45 removed whitelist entries now returns
401 without a token; the 4 revived entries still pass; spot-checks (coversheet
med-history, login). ALL 45 are checked, not a sample."""
import subprocess, sys, time
from _whitelist_dead45 import DEAD45

BASE = "http://localhost:8080"

def no_token(url, method="GET"):
    cmd = ["curl", "-s", "-m", "15", "-X", method, BASE + url, "-o", "/dev/null", "-w", "%{http_code}"]
    return subprocess.run(cmd, capture_output=True, text=True).stdout

print("=== 1. the 45 removed entries: no-token call must be 401 ===")
bad = []
for frag in DEAD45:
    code = no_token(frag)
    status = "PASS" if code == "401" else "FAIL"
    print(f"[{status}] {frag} -> {code}")
    if code != "401":
        bad.append((frag, code))
print(f"\n45-entry result: {45 - len(bad)}/45 return 401" + (f"  FAILURES: {bad}" if bad else ""))
if bad:
    sys.exit(1)

print("\n=== 2. the 4 revived entries (must NOT be blocked) ===")
# /br/ and /checkParti/ are no-token by design
for url, method in [("/adt/br/1", "GET"), ("/checkParti/1", "GET")]:
    code = no_token(url, method)
    status = "PASS (not 401)" if code != "401" else "FAIL (401!)"
    print(f"[{status}] no-token {method} {url} -> {code}")
# bill/service/cancel + medicationUpdate have AUTHENTICATED callers — token'd call must work
tok = open("/tmp/fresh_token.txt").read().strip()
H = ["-H", "Authorization: Bearer " + tok, "-H", "userId: 1", "-H", "unitId: 1", "-H", "Content-Type: application/json"]
for url, method, payload in [("/api/billing/bill/service/cancel", "POST", '{}'),
                             ("/api/opd/medicationUpdate", "POST", '{}')]:
    cmd = ["curl", "-s", "-m", "15", "-X", method, BASE + url] + H + ["-d", payload, "-o", "/dev/null", "-w", "%{http_code}"]
    code = subprocess.run(cmd, capture_output=True, text=True).stdout
    status = "PASS (token'd, not 401)" if code != "401" else "FAIL (401 with token!)"
    print(f"[{status}] token'd {method} {url} -> {code}")

print("\n=== 3. spot-checks ===")
# coversheet med-history: the SAFE entry still whitelisted; the seeded rows render
cmd = ["curl", "-s", "-m", "15", "-X", "POST", BASE + "/api/opd/getMedicationHistoryList"] + H + [
    "-d", '{"organizationId":1,"unitId":1,"patientId":1}']
r = subprocess.run(cmd, capture_output=True, text=True).stdout
rows = r.count("itemName") if '"status":"success"' in r else -1
print(f"[{'PASS' if rows >= 1 else 'FAIL'}] coversheet med-history (token'd): status success, {rows} rows")
# login endpoint still public
code = no_token("/adt/userLogin", "POST")
print(f"[{'PASS (not 401)' if code != '401' else 'FAIL (401!)'}] login endpoint no-token -> {code}")

print("\n=== 4. regression checker ===")
r = subprocess.run(["python3", "tools/verify_deployed_regressions.py"], capture_output=True, text=True,
                   cwd="/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade")
print("regression:", r.stdout.strip().splitlines()[-1])
sys.exit(0 if "PASS" in r.stdout else 1)
