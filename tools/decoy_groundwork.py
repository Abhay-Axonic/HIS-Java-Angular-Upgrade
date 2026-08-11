#!/usr/bin/env python3
"""decoy_groundwork.py — ITEM 2: per-instance decoy preparation (PREP ONLY,
NO BUILDS). For each of the 18 decoy instances (14 from the 20-bucket
recheck + 4 pre-existing: 528, 578, 580, 587):

  1. the menu node + state + template path (what the real screen is);
  2. the template's Angular bind count (the DECOY signature);
  3. whether a REAL backend exists: find the controller JS (state's
     controller binding, or the legacy screen's siblings), extract its
     primary endpoint, CALL it with the org/unit contract, report the body
     status — LIVE (200 success) vs MASKED (200 error) vs DEAD (404/401);
  4. what building would involve (one line: surface shape + feeds).

Prep only — nothing is written to new/.
"""
import json
import os
import re
import sys
import urllib.request
import urllib.error

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
APPJS = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js")
INDEX = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/index.html")

appjs = open(APPJS, encoding="utf-8").read()
index = open(INDEX, encoding="utf-8").read()

cache = json.load(open(os.path.join(ROOT, "tools/.token_cache.json")))
H = {"Authorization": "Bearer " + cache["token"], "Content-Type": "application/json",
     "userId": "1", "unitId": "1"}

# (node id, state name from link root, template path hint, prior context)
INSTANCES = [
    (528, "consentType", "consent-type.html", "pre-existing decoy"),
    (578, "variableSquencePricing", "variable-sequence-pricing.html", "pre-existing decoy (state typo; real controllers UNBOUND)"),
    (580, "accountPayable", "account-payable.html", "pre-existing decoy (merged 9eb08762)"),
    (587, "orderSet", "order-set.html", "pre-existing decoy"),
    (577, "packageList", "package-list.html", "recheck decoy"),
    (581, "accountPayableComparsion", "account-payable-comparsion.html", "recheck decoy"),
    (582, "accountPayableStatus", "account-payable-status.html", "recheck decoy"),
    (586, "schedulePriceUnitService", "schedule-price-unit-service.html", "recheck decoy"),
    (588, "orderSetList", "order-set-list.html", "recheck decoy"),
    (589, "admissionRejectionList", "admission-rejection-list.html", "recheck decoy"),
    (591, "findBillList", "find-bill-list.html", "recheck decoy"),
    (593, "entitlementCoversion", "entitlement-coversion.html", "recheck decoy (empty template)"),
    (594, "autoChargeReview", "auto-charge-review.html", "recheck decoy"),
    (595, "writeOff", "write-off.html", "recheck decoy"),
    (596, "writeOffDetails", "write-off-details.html", "recheck decoy"),
    (599, "roundOffConfiguration", "round-off-configuration.html", "recheck decoy"),
    (601, "reminderForNOK", "reminder-for-nok.html", "recheck decoy"),
    (620, "IPdefaultServiceConfig", "IP-default-service-config.html", "recheck decoy"),
]


def state_info(st):
    m = re.search(r"\.state\('" + re.escape(st) + r"'[\s\S]{0,800}?controller:\s*[\"']([\w]+)[\"']", appjs)
    tm = re.search(r"\.state\('" + re.escape(st) + r"'[\s\S]{0,800}?templateUrl:\s*'([^']+)'", appjs)
    return (m.group(1) if m else None, tm.group(1) if tm else None)


def controller_file(ctrl):
    m = re.search(r'src="resources/assets/js/controller/[^"]*' + re.escape(ctrl) + r'[^"]*\.js', index)
    if m:
        return os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp", m.group(0)[5:])
    return None


def primary_endpoint(src):
    urls = re.findall(r'["\'](/[A-Za-z0-9_/{}]+)["\']', src)
    for u in urls or []:
        if "getSpecialityListOfActiveInactive" in u or "getActiveSpecialityList" in u or "user/stores" in u:
            continue
        return u
    return None


def call(full):
    req = urllib.request.Request("http://localhost:8080" + full,
                                 data=json.dumps({"orgId": 1, "unitId": 1}).encode(),
                                 headers=H, method="POST")
    try:
        with urllib.request.urlopen(req, timeout=15) as r:
            b = r.read().decode()[:80]
            try:
                body = json.loads(b)
                return f"HTTP {r.status} status={body.get('status')} code={body.get('code')}"
            except Exception:
                return f"HTTP {r.status} BODY {b}"
    except urllib.error.HTTPError as e:
        return f"HTTP {e.code}"


def main():
    sys.path.insert(0, os.path.join(ROOT, "tools"))
    from preflight_screen import load_privilege_tree
    tree = load_privilege_tree()
    node_by_id = {n["id"]: n for n in tree}
    for nid, st, tpl, ctx in INSTANCES:
        n = node_by_id.get(nid, {})
        name = n.get("name", "?").strip()
        ctrl, turl = state_info(st)
        # template bind count
        binds = 0
        tsize = 0
        if turl:
            tpath = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp", turl)
            if os.path.exists(tpath):
                tsrc = open(tpath, encoding="utf-8").read()
                binds = len(re.findall(r"ng-(model|click|bind|repeat|change|options|show|if)", tsrc))
                tsize = len(tsrc)
        # backend
        cfile = controller_file(ctrl) if ctrl else None
        verdict = "NO-CTRL-JS"
        ep = None
        if cfile and os.path.exists(cfile):
            ep = primary_endpoint(open(cfile, encoding="utf-8").read())
            if ep:
                full = ep if ep.startswith(("/api", "/unit", "/adt", "/global", "/billing")) else "/api" + ep
                if "/{" in full:
                    full = re.sub(r"\{[^}]*\}", "1", full)
                verdict = call(full.rstrip("/"))
            else:
                verdict = "CTRL-JS-NO-LITERAL-EP"
        else:
            verdict = f"NO-CTRL-JS (state ctrl: {ctrl})"
        print(f"{nid} | {name} | state={st} | ctrl={ctrl} | binds={binds} size={tsize} | {verdict} | ep={ep} | [{ctx}]")


if __name__ == "__main__":
    main()
