#!/usr/bin/env python3
"""probe_common_master_unsupported.py — unsupported-state probe (owner order 2,
2026-08-12): (A) a legacy branch OUTSIDE the slice (sampleMaster — a real 43-way
dispatch branch, NOT in the slice's DISPATCH map) with family permissions must
render the named "not rebuilt yet" message, not a generic blank; (B) the SAME
unsupported key with a NON-family permission must still be denied by the guard —
an unsupported key must not bypass the permission check.

Freshness gate first (standing rule): served bundle must match current source.
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

sys.path.insert(0, "tools")
from bundle_freshness import check_freshness, EXIT_STALE  # noqa: E402

FRESHNESS_SPECS = [
    {
        "label": "module-access.guard (main.js)",
        "source": "new/src/app/core/module-access.guard.ts",
        "url": "/main.js",
        "absent": ["GUARD-DEBUG", "GUARD-NAV", "GUARD-DENY", "navigateByUrl"],
    },
    {
        "label": "app.routes (main.js)",
        "source": "new/src/app/app.routes.ts",
        "url": "/main.js",
    },
    {
        "label": "common-master.html (feature chunk)",
        "source": "new/src/app/features/common-master/common-master.html",
        "url": "/main.js",
    },
]

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]
BASE = "http://localhost:4201"
FAMILY_IDS = [
    412, 413, 414, 415, 417, 419, 420, 392, 423, 424, 427, 435, 436,
    437, 825, 430, 429, 425, 431, 438, 440, 428, 439, 561, 564, 557,
    389, 390, 391, 450, 460, 492, 393, 394, 395, 491, 447, 448, 449,
    798, 569, 826, 426, 421, 844,
]
FAILS = []


def make_cookie(ctx):
    co = urllib.parse.quote(json.dumps({
        "unitId": 1, "organizationId": 1, "userId": 1,
        "isSuperUser": "Y", "authToken": TOKEN,
    }))
    ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])


def main():
    freshness_fails = check_freshness(FRESHNESS_SPECS, base_url=BASE)
    if freshness_fails:
        print("FRESHNESS-FAIL:")
        for f in freshness_fails:
            print(f"  - {f}")
        sys.exit(EXIT_STALE)

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)

        # ---------- (A) family permission + unsupported key ----------
        ctx = browser.new_context()
        page = ctx.new_page()
        errors = []
        page.on("pageerror", lambda e: errors.append(str(e)))
        page.on("console", lambda m: errors.append(m.text) if m.type == "error" else None)
        make_cookie(ctx)
        ctx.add_init_script(
            "localStorage.setItem('permissions', JSON.stringify({modules: [{id: 412}]}));")
        page.route("**/privilege/role/**", lambda r: r.fulfill(
            status=200, content_type="application/json",
            body=json.dumps({"modules": [{"modules": [{"id": 412}]}]})))
        page.goto(BASE + "/commonMaster/sampleMaster", wait_until="domcontentloaded")
        time.sleep(5)
        body = page.inner_text("body")
        named = "has not been rebuilt yet" in body and "Sample Master" in body
        print(f"A. unsupported key + family perm: named_msg={named} "
              f"url={page.url.split('/')[-1]}")
        if not named:
            FAILS.append("A: unsupported key did not render named not-rebuilt message")
        if "accessDenied" in page.url:
            FAILS.append("A: unsupported key wrongly denied despite family permission")
        ctx.close()

        # ---------- (B) unsupported key + NON-family permission ----------
        ctx2 = browser.new_context()
        page2 = ctx2.new_page()
        errors2 = []
        page2.on("pageerror", lambda e: errors2.append(str(e)))
        page2.on("console", lambda m: errors2.append(m.text) if m.type == "error" else None)
        make_cookie(ctx2)
        ctx2.add_init_script(
            "localStorage.setItem('permissions', JSON.stringify({modules: [{id: 999999}]}));")
        page2.route("**/privilege/role/**", lambda r: r.fulfill(
            status=200, content_type="application/json",
            body=json.dumps({"modules": [{"modules": [{"id": 999999}]}]})))
        page2.goto(BASE + "/commonMaster/sampleMaster", wait_until="domcontentloaded")
        denied = False
        deadline = time.time() + 15
        while time.time() < deadline:
            if page2.evaluate("() => location.pathname") == "/accessDenied":
                denied = True
                break
            time.sleep(0.25)
        print(f"B. unsupported key + non-family perm: denied={denied} "
              f"url={page2.url.split('/')[-1]}")
        if not denied:
            FAILS.append("B: unsupported key BYPASSED the guard (rendered with "
                         "non-family permission)")
        ctx2.close()

        browser.close()

    print()
    if FAILS:
        print(f"PROBE FAIL ({len(FAILS)}):")
        for f in FAILS:
            print(f"  - {f}")
        sys.exit(1)
    print("PROBE PASS — unsupported state: names the master + not-rebuilt message "
          "with family permission; guard still denies non-family permission on "
          "an unsupported key")


if __name__ == "__main__":
    main()
