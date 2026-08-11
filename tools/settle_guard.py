#!/usr/bin/env python3
"""settle_guard.py — THE decisive run: polling probe + request-listener, same run.

On the UNFIXED guard (parseUrl returns), permissions=[999999] (non-family id),
deep-link to each of the three routes. For each:
  - request-listener: does the access-denied CHUNK get requested? (the old
    probe (b) said NEVER)
  - polling probe: does location.pathname reach /accessDenied? (the old probe
    (a) said URL never moved in 20s)
  - history hook: was there a REPLACE to /accessDenied? (the router's action)
  - DOM: does <app-access-denied> mount? does the guarded component render?
  - page.url at the end (Playwright's signal vs location.href)

If the listener sees the chunk AND the poll succeeds -> old probes were wrong.
If the poll succeeds but NO chunk request -> something else satisfies the poll
and the guard still fails open.
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

# Served-bundle freshness gate (standing rule, owner directive 2026-08-12):
# this probe's verdict is a SECURITY-CONTROL reversal claim, so the served
# bundle must be proven to match the current source before any behavioural
# assertion. A stale bundle fails loudly (exit 2) — exactly the class that
# produced the original false fail-open finding.
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
]

CACHE = "tools/.token_cache.json"
TOKEN = json.load(open(CACHE))["token"]
BASE = "http://localhost:4201"

TARGETS = [
    ("/commonMaster/relationmaster", ".cm-wrap", "commonMaster (family gate)"),
    ("/growthChart", ".gc-screen", "growthChart (744)"),
    ("/serviceMaster", ".service-master-page", "serviceMaster (376)"),
]


def run(route, root_sel, label):
    # FRESHNESS GATE FIRST — before ANY behavioural assertion (standing rule,
    # owner directive 2026-08-12). A stale served bundle fails loudly (exit 2)
    # rather than producing a behavioural verdict against frozen code.
    freshness_fails = check_freshness(FRESHNESS_SPECS, base_url=BASE)
    if freshness_fails:
        print("FRESHNESS-FAIL (served bundle does not match current source):")
        for f in freshness_fails:
            print(f"  - {f}")
        print("Probe aborted before any behavioural assertion.")
        sys.exit(EXIT_STALE)

    with sync_playwright() as p:
        browser = p.chromium.launch(headless=True)
        ctx = browser.new_context()
        ctx.add_init_script("""
          localStorage.setItem('permissions', JSON.stringify({modules: [{id: 999999}]}));
          window.__hist = [];
          const push = history.pushState.bind(history);
          const rep = history.replaceState.bind(history);
          history.pushState = function(...a) { window.__hist.push('PUSH ' + (a[2] ?? '')); return push(...a); };
          history.replaceState = function(...a) { window.__hist.push('REPLACE ' + (a[2] ?? '')); return rep(...a); };
        """)
        page = ctx.new_page()
        requests = []
        page.on("request", lambda r: requests.append(r.url))
        errors = []
        page.on("pageerror", lambda e: errors.append(str(e)))
        co = urllib.parse.quote(json.dumps({
            "unitId": 1, "organizationId": 1, "userId": 1,
            "isSuperUser": "Y", "authToken": TOKEN,
        }))
        ctx.add_cookies([{"name": "cookieObject", "value": co, "url": BASE}])
        page.route("**/privilege/role/**", lambda r: r.fulfill(
            status=200, content_type="application/json",
            body=json.dumps({"modules": [{"modules": [{"id": 999999}]}]})))
        page.goto(BASE + route, wait_until="domcontentloaded")
        # POLLING PROBE: location.pathname -> /accessDenied?
        landed = None
        start = time.time()
        while time.time() - start < 20:
            time.sleep(0.25)
            if page.evaluate("() => location.pathname") == "/accessDenied":
                landed = round(time.time() - start, 2)
                break
        time.sleep(2)
        chunk = [u for u in requests if "access-denied" in u]
        ad_mounted = page.query_selector("app-access-denied") is not None
        guarded = page.query_selector(root_sel) is not None
        hist = page.evaluate("() => window.__hist") or []
        print(f"--- {label} ---")
        print(f"  poll landed at:      {landed}s  ({'NEVER' if landed is None else ''})")
        print(f"  access-denied chunk: {'REQUESTED' if chunk else 'NEVER REQUESTED'}")
        for u in chunk:
            print(f"      {u.replace(BASE, '')}")
        print(f"  history:             {hist}")
        print(f"  app-access-denied:   {ad_mounted}")
        print(f"  guarded cmp:         {guarded}")
        print(f"  page.url:            {page.url}")
        print(f"  location.href:       {page.evaluate('() => location.href')}")
        print(f"  page errors:         {len(errors)}")
        browser.close()
        return {
            "landed": landed, "chunk": bool(chunk), "ad": ad_mounted,
            "guarded": guarded, "label": label,
        }


results = [run(r, s, l) for r, s, l in TARGETS]
print()
print("=" * 60)
for r in results:
    ok = r["landed"] is not None and r["chunk"] and r["ad"] and not r["guarded"]
    print(f"{r['label']}: poll={r['landed']}s chunk={'Y' if r['chunk'] else 'N'} "
          f"accessDenied={'Y' if r['ad'] else 'N'} guarded={'Y' if r['guarded'] else 'N'} "
          f"-> {'GUARD WORKS' if ok else 'CHECK'}")
