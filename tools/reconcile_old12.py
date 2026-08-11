#!/usr/bin/env python3
"""reconcile_old12.py — EXACT debug_guard12 harness (two-goto, evaluate-based
permissions, NO interception, same listener filter) against the CURRENT clean
bundle. If the chunk is now requested + poll lands -> the old run's bundle was
the artifact. If it still shows no chunk -> the harness is the artifact.
"""
import json
import time
import urllib.parse

from playwright.sync_api import sync_playwright

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]
BASE = "http://localhost:4201"

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    ctx = browser.new_context()
    page = ctx.new_page()
    reqs = []
    logs = []
    page.on("request", lambda r: reqs.append(r.url) if "access" in r.url.lower() or "role" in r.url.lower() else None)
    page.on("console", lambda m: logs.append(m.text) if "GUARD" in m.text else None)
    co = urllib.parse.quote(json.dumps({
        "unitId": 1, "organizationId": 1, "userId": 1,
        "isSuperUser": "Y", "authToken": TOKEN,
    }))
    ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
    page.goto(BASE, wait_until="domcontentloaded")
    time.sleep(3)
    page.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 999999}]}))")
    page.goto(BASE + "/commonMaster/relationmaster", wait_until="domcontentloaded")
    # poll the REAL location like the corrected probe
    landed = None
    start = time.time()
    while time.time() - start < 20:
        time.sleep(0.25)
        if page.evaluate("() => location.pathname") == "/accessDenied":
            landed = round(time.time() - start, 2)
            break
    time.sleep(1)
    print("final URL:", page.url)
    print("location.href:", page.evaluate("() => location.href"))
    print(f"poll landed at: {landed}s")
    print("REQUESTS (access/role):")
    for u in reqs:
        print(f"  {u[:130]}")
    print("access-denied chunk requested:",
          any("access-denied" in u for u in reqs))
    print("app-access-denied mounted:",
          page.query_selector("app-access-denied") is not None)
    print("GUARD LOGS:")
    for l in logs:
        print("  ", l[:180])
    browser.close()
