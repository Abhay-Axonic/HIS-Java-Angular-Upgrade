#!/usr/bin/env python3
"""c7_billing_20_recheck.py — the endpoint-truth recheck of the 20
NOT-A-SCREEN-YET Billing nodes (the owner's queued item; active now that the
candidates are done). The 585 lesson: the class-name grep UNDER-COUNTS — a
screen with a real backend whose Java class name differs from the state root
was classified NOT-A-SCREEN-YET. This recheck calls each node's PRIMARY
endpoint (extracted from its controller JS) with the standard org/unit
payload and classifies by the BODY STATUS.

Zero seeding; his_local only. Output: one line per node with the endpoint +
HTTP + body status, classified LIVE (real handler responds) vs STILL-NOTHING
(no endpoint found / no handler / honest empty).
"""
import json
import os
import re
import subprocess
import sys
import urllib.request
import urllib.error

ROOT = "/Users/axonichealth/Downloads/His_Java_Angular_Pgrade/HIS_Java_Angular_upgrade"
APPJS = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js/config/app.js")
INDEX = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/index.html")
CTRL = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller")

appjs = open(APPJS, encoding="utf-8").read()
index = open(INDEX, encoding="utf-8").read()

# the 20 NOT-A-SCREEN-YET (the sweep's classification; parent 570 children
# not already classified as rebuilt/live/decoy/dangle/route-only/commented)
TARGETS = [572, 574, 577, 581, 582, 584, 586, 588, 589, 591,
           593, 594, 595, 596, 599, 601, 608, 611, 620]

cache = json.load(open(os.path.join(ROOT, "tools/.token_cache.json")))
H = {"Authorization": "Bearer " + cache["token"], "Content-Type": "application/json",
     "userId": "1", "unitId": "1"}


def main():
    sys.path.insert(0, os.path.join(ROOT, "tools"))
    from preflight_screen import load_privilege_tree
    tree = load_privilege_tree()
    node_by_id = {n["id"]: n for n in tree}

    for nid in TARGETS:
        n = node_by_id.get(nid)
        if not n:
            print(f"{nid}: NO TREE NODE")
            continue
        link = (n.get("actionLink") or "").replace("#/", "")
        st = link.split("/")[0]
        name = n.get("name", "").strip()
        # controller JS: read the STATE's controller binding from app.js,
        # then find that controller in index.html (the accurate path — the
        # controller name often differs from the state root)
        ctrl_file = None
        ctrl_name = None
        sm = re.search(r"\.state\('" + re.escape(st) + r"'[\s\S]{0,600}?controller:\s*\"(\w+)\"", appjs)
        if sm:
            ctrl_name = sm.group(1)
        elif re.search(r"\.state\('" + re.escape(st) + r"'", appjs):
            seg = appjs[appjs.find(f".state('{st}'"):appjs.find(f".state('{st}'") + 700]
            cm = re.search(r'controller:\s*["\']([\w]+)["\']', seg)
            if cm:
                ctrl_name = cm.group(1)
        if ctrl_name:
            m = re.search(r'src="resources/assets/js/controller/[^"]*' + re.escape(ctrl_name) + r'[^"]*\.js', index)
            if m:
                ctrl_file = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp", m.group(0)[5:])
        if not ctrl_file:
            m = re.search(r'src="resources/assets/js/controller/[^"]*' + re.escape(st) + r'[^"]*\.js', index)
            if m:
                ctrl_file = os.path.join(ROOT, "legacy/HIS-WAR/src/main/webapp", m.group(0)[5:])
        if not ctrl_file or not os.path.exists(ctrl_file):
            print(f"{nid} | {name} | {st} | NO CONTROLLER JS FOUND (state ctrl {ctrl_name})")
            continue
        src = open(ctrl_file, encoding="utf-8").read()
        # the first literal URL that is NOT the shared dropdown feeds
        # (/adt/getSpecialityListOfActiveInactive, getActiveSpecialityList)
        urls = re.findall(r'["\'](/[A-Za-z0-9_/{}]+)["\']', src)
        url = None
        for u in urls:
            if "getSpecialityListOfActiveInactive" in u or "getActiveSpecialityList" in u:
                continue
            url = u
            break
        if not url:
            print(f"{nid} | {name} | {st} | CONTROLLER JS but NO screen-specific literal URL")
            continue
        # resolve the base — most controllers use BASE_URL+ROOT_URL+"/api/..."
        full = url if url.startswith("/api") or url.startswith("/unit") or url.startswith("/adt") else "/api" + url
        # try POST org payload (the standard), GET if POST 400s
        def attempt(payload, method="POST"):
            req = urllib.request.Request("http://localhost:8080" + full,
                                         data=json.dumps(payload).encode() if method == "POST" else None,
                                         headers=H, method=method)
            try:
                with urllib.request.urlopen(req, timeout=20) as r:
                    b = r.read().decode()[:90]
                    try:
                        body = json.loads(b)
                        st_ = body.get("status")
                        code = body.get("code")
                        obj = body.get("object")
                        listn = len(body.get("listObject") or [])
                        return f"HTTP {r.status} status={st_} code={code} obj={obj} list={listn}"
                    except Exception:
                        return f"HTTP {r.status} BODY {b}"
            except urllib.error.HTTPError as e:
                try:
                    b = e.read().decode()[:60]
                except Exception:
                    b = ""
                return f"HTTP {e.code} {b}"
        r1 = attempt({"orgId": 1, "unitId": 1, "organizationId": 1, "offset": 0, "noOfRecordsPerPage": 10})
        live = "status=success" in r1 or "status='success'" in r1
        verdict = "LIVE?" if live else "still-nothing?"
        print(f"{nid} | {name} | {st} | {full} | {r1} | {verdict}")


if __name__ == "__main__":
    main()
