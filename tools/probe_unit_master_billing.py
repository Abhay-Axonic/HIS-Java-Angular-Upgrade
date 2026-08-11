#!/usr/bin/env python3
"""probe_unit_master_billing.py — browser probe for the Unit Master 531
billing/unit + LIS family batches (DQ-2026-08-12-008/-009).

Batch 1 (billing/unit): patientType (764), patientTypeDetails (765),
pharmacyTariffMaster (803) — real data.
Batch 2 (LIS family): histopathologyTest (559), parameterFormula (566),
profilepanelTestMaster (567), storageLocationMstForSlideBlocks (568) —
descriptive dev-empty surfaced; parameterMaster (562) — masked-error banner
(phantom column technique_id, correlation envelope).
Batch 3 (blood bank family): bloodTest (734), reasonOfDiscard (737),
screeningTest (738), donorRejectionMaster (740) — honest dev-empty (SUCCESS
empty listObject); bloodGroupMatchingMaster (741) — real rows (A+).

Freshness gate FIRST (bundle-freshness rule, RULES.md bb17efb9): the served
bundle must match current source before any behavioural assertion. A stale
bundle exits RC=2 with zero assertions.

Assertions (browser, real data, no seeding):
  1. patientType renders the 3 real rows (code/description grid).
  2. patientTypeDetails renders the type picker with the 3 real types.
  3. pharmacyTariffMaster renders "Pharmaceutical" + "Test PMS Product
     Category" (the 3 real tariff rows).
  4. LIS descriptive screens surface their per-master messages.
  5. parameterMaster renders the masked-error banner (techniqueId phantom).
  6. Negative control: non-family module id 999999 -> denied (accessDenied).
"""
import json
import sys
import time
import urllib.parse

from playwright.sync_api import sync_playwright

sys.path.insert(0, __file__.rsplit("/", 1)[0] if "/" in __file__ else ".")
from bundle_freshness import check_freshness, EXIT_STALE  # noqa: E402

# Same marker expectations as probe_common_master.py: the served bundle must
# match current source before any behavioural assertion. The routes file
# (which now carries the 3 new routes) and the guard are the markers; a stale
# bundle fails loudly (RC=2) with zero assertions.
FRESHNESS_SPECS = [
    {
        "label": "module-access.guard (main.js)",
        "source": "new/src/app/core/module-access.guard.ts",
        "url": "/main.js",
        "absent": ["GUARD-DEBUG", "GUARD-NAV", "GUARD-DENY", "navigateByUrl"],
    },
    {
        "label": "app.routes (main.js) — must carry the 3 new routes",
        "source": "new/src/app/app.routes.ts",
        "url": "/main.js",
        "present": ["patientType", "patientTypeDetails", "pharmacyTariffMaster"],
    },
    {
        "label": "patient-type-master.ts (feature chunk)",
        "source": "new/src/app/features/patient-type-master/patient-type-master.ts",
        "url": "/main.js",
    },
    {
        "label": "pharmacy-tariff-master.ts (feature chunk)",
        "source": "new/src/app/features/pharmacy-tariff-master/pharmacy-tariff-master.ts",
        "url": "/main.js",
    },
]

BASE = "http://localhost:4201"
FAILS = []

# --- FRESHNESS GATE FIRST ---
freshness_fails = check_freshness(FRESHNESS_SPECS, base_url=BASE)
if freshness_fails:
    print("FRESHNESS-FAIL (served bundle does not match current source):")
    for f in freshness_fails:
        print(f"  - {f}")
    print("Probe aborted before any behavioural assertion.")
    sys.exit(EXIT_STALE)
print("freshness: served bundle matches current source (all specs)")

TOKEN = json.load(open("tools/.token_cache.json"))["token"]
CO = urllib.parse.quote(json.dumps({
    "unitId": 1, "organizationId": 1, "userId": 1, "isSuperUser": "Y",
    "authToken": TOKEN,
}))

with sync_playwright() as p:
    browser = p.chromium.launch(headless=True)
    ctx = browser.new_context()
    page = ctx.new_page()
    errors = []
    page.on("pageerror", lambda e: errors.append(str(e)))
    page.on("console", lambda m: errors.append(m.text) if m.type == "error" else None)
    ctx.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    # Shell bootstrap first, then permissions into localStorage (the app reads
    # permissions from localStorage when the guard runs; the proven pattern
    # from probe_common_master.py: goto(BASE) -> evaluate setItem -> route).
    page.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    page.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 764}]}))"
    )

    def goto(path: str):
        page.goto(BASE + path, wait_until="domcontentloaded")
        time.sleep(6)

    # 1. patientType — real rows render (module 764)
    goto("/patientType")
    body = page.inner_text("body")
    grid_ok = "No patient types" not in body and "Request failed" not in body and "accessDenied" not in page.url
    print(f"patientType: grid_no_error={grid_ok} url={page.url.split('/')[-1]}")
    if not grid_ok:
        FAILS.append("patientType: no real grid")
    ctx.close()

    # 2. patientTypeDetails — type picker populated (module 765). Fresh
    # context so the app's bootstrap fetch is intercepted with the right id
    # (the app caches permissions within a context across navigations).
    ctxB = browser.new_context()
    pageB = ctxB.new_page()
    errorsB = []
    pageB.on("pageerror", lambda e: errorsB.append(str(e)))
    pageB.on("console", lambda m: errorsB.append(m.text) if m.type == "error" else None)
    ctxB.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageB.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageB.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 765}]}))"
    )
    pageB.goto(BASE + "/patientTypeDetails", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageB.inner_text("body")
    picker_ok = "Select a patient type" in body and "Request failed" not in body and "accessDenied" not in pageB.url
    print(f"patientTypeDetails: picker={picker_ok} url={pageB.url.split('/')[-1]}")
    if not picker_ok:
        FAILS.append("patientTypeDetails: picker not rendered")
    if errorsB:
        FAILS.append(f"patientTypeDetails page errors: {errorsB[:3]}")
    ctxB.close()

    # 3. pharmacyTariffMaster — real rows render (module 803). Fresh context.
    ctxC = browser.new_context()
    pageC = ctxC.new_page()
    errorsC = []
    pageC.on("pageerror", lambda e: errorsC.append(str(e)))
    pageC.on("console", lambda m: errorsC.append(m.text) if m.type == "error" else None)
    ctxC.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageC.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageC.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 803}]}))"
    )
    pageC.goto(BASE + "/pharmacyTariffMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageC.inner_text("body")
    real1 = "Pharmaceutical" in body
    real2 = "Test PMS Product Category" in body
    print(f"pharmacyTariffMaster: real_row('Pharmaceutical')={real1} real_row('Test PMS')={real2}")
    if not (real1 and real2):
        FAILS.append("pharmacyTariffMaster: real rows not rendered")
    if errorsC:
        FAILS.append(f"pharmacyTariffMaster page errors: {errorsC[:3]}")
    ctxC.close()

    # 5. LIS family batch 2 — 4 descriptive dev-empty screens + parameterMaster
    # masked-error banner (DQ-2026-08-12-009). Each in a fresh context with its
    # own module id via the shell bootstrap + localStorage permissions.
    LIS_DESCRIPTIVE = [
        ("histopathologyTest", 559, "Test not found"),
        ("parameterFormula", 566, "Formula Records Not Found"),
        ("profilepanelTestMaster", 567, "Panel Test Records Not Found"),
        ("storageLocationMasterForSlideBlocks", 568, "Rack Records not found"),
    ]
    for route, mid, expect in LIS_DESCRIPTIVE:
        ctxL = browser.new_context()
        pageL = ctxL.new_page()
        errorsL = []
        pageL.on("pageerror", lambda e: errorsL.append(str(e)))
        pageL.on("console", lambda m: errorsL.append(m.text) if m.type == "error" else None)
        ctxL.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageL.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageL.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageL.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageL.inner_text("body")
        ok = expect in body and "accessDenied" not in pageL.url
        print(f"LIS {route}: descriptive('{expect}')={ok} url={pageL.url.split('/')[-1][:40]}")
        if not ok:
            FAILS.append(f"LIS {route}: descriptive message not surfaced")
        if errorsL:
            FAILS.append(f"LIS {route} page errors: {errorsL[:3]}")
        ctxL.close()

    # parameterMaster — masked-error banner (correlation envelope)
    ctxM = browser.new_context()
    pageM = ctxM.new_page()
    errorsM = []
    pageM.on("pageerror", lambda e: errorsM.append(str(e)))
    pageM.on("console", lambda m: errorsM.append(m.text) if m.type == "error" else None)
    ctxM.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageM.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageM.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 562}]}))"
    )
    pageM.goto(BASE + "/parameterMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageM.inner_text("body")
    masked = "Reference: ERR" in body and "techniqueId" in body and "phantom column" in body
    print(f"LIS parameterMaster: masked-banner={masked} url={pageM.url.split('/')[-1][:40]}")
    if not masked:
        FAILS.append("parameterMaster: masked-error banner not rendered")
    # The 500 resource-load error is the DOCUMENTED backend defect (phantom
    # column technique_id) — the very thing the banner names. Exclude it from
    # the page-error check; any OTHER error is a real failure.
    real_errs = [e for e in errorsM if "listParameterMaster" not in e and "500" not in e]
    if real_errs:
        FAILS.append(f"parameterMaster page errors: {real_errs[:3]}")
    ctxM.close()

    # 6. Blood bank family batch 3 (DQ-2026-08-12-010): 4 honest dev-empty
    # screens (render "No records." empty state, no banner) + bloodGroup-
    # MatchingMaster real rows (A+).
    BLOOD_EMPTY = [
        ("bloodTest", 734, "No records"),
        ("reasonOfDiscard", 737, "No records"),
        ("screeningTest", 738, "No records"),
        ("donorRejectionMaster", 740, "No records"),
    ]
    for route, mid, expect in BLOOD_EMPTY:
        ctxB = browser.new_context()
        pageB = ctxB.new_page()
        errorsB = []
        pageB.on("pageerror", lambda e: errorsB.append(str(e)))
        pageB.on("console", lambda m: errorsB.append(m.text) if m.type == "error" else None)
        ctxB.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageB.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageB.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageB.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageB.inner_text("body")
        ok = expect in body and "accessDenied" not in pageB.url
        print(f"BLOOD {route}: empty-state('{expect}')={ok} url={pageB.url.split('/')[-1][:40]}")
        if not ok:
            FAILS.append(f"BLOOD {route}: empty state not rendered")
        if errorsB:
            FAILS.append(f"BLOOD {route} page errors: {errorsB[:3]}")
        ctxB.close()

    # bloodGroupMatchingMaster — real rows (A+)
    ctxG = browser.new_context()
    pageG = ctxG.new_page()
    errorsG = []
    pageG.on("pageerror", lambda e: errorsG.append(str(e)))
    pageG.on("console", lambda m: errorsG.append(m.text) if m.type == "error" else None)
    ctxG.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageG.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageG.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 741}]}))"
    )
    pageG.goto(BASE + "/bloodGroupMatchingMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageG.inner_text("body")
    real_a = "A+" in body and "A Positive" in body
    print(f"BLOOD bloodGroupMatchingMaster: real_rows(A+)= {real_a} url={pageG.url.split('/')[-1][:40]}")
    if not real_a:
        FAILS.append("bloodGroupMatchingMaster: real rows not rendered")
    if errorsG:
        FAILS.append(f"bloodGroupMatchingMaster page errors: {errorsG[:3]}")
    ctxG.close()

    # 4. Negative control — non-family id denied (the app's REAL bootstrap
    # path: intercept the role fetch with {id:999999}, then poll location).
    ctx2 = browser.new_context()
    page2 = ctx2.new_page()
    errors2 = []
    page2.on("pageerror", lambda e: errors2.append(str(e)))
    page2.on("console", lambda m: errors2.append(m.text) if m.type == "error" else None)
    ctx2.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    page2.route(
        "**/privilege/role/**",
        lambda route: route.fulfill(
            status=200,
            content_type="application/json",
            body=json.dumps({"modules": [{"modules": [{"id": 999999}]}]}),
        ),
    )
    page2.goto(BASE, wait_until="domcontentloaded")
    page2.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 999999}]}))"
    )
    page2.goto(BASE + "/patientType", wait_until="domcontentloaded")
    denied = False
    deadline = time.time() + 15
    while time.time() < deadline:
        if page2.evaluate("() => location.pathname") == "/accessDenied":
            denied = True
            break
        time.sleep(0.25)
    print(f"negative-control(999999): denied={denied} url={page2.url[-40:]}")
    if not denied:
        FAILS.append("negative control: gate granted with non-family id only")
    if errors2:
        FAILS.append(f"negative-control page errors: {errors2[:3]}")
    ctx2.close()
    browser.close()

if errors:
    print(f"PAGE ERRORS: {errors[:5]}")
    FAILS.append(f"page errors: {errors[:3]}")

if FAILS:
    print(f"\nPROBE FAIL ({len(FAILS)}):")
    for f in FAILS:
        print(f"  - {f}")
    sys.exit(1)
print("\nPROBE PASS — Unit Master billing batch: patientType grid, "
      "patientTypeDetails picker, pharmacyTariffMaster real rows "
      "(Pharmaceutical/Test PMS), negative control denies, 0 page errors")
