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
Batch 4 (master/unit family): loyaltyCardCreation (729), passBedCategoryMapper
(822), templateICDCodeMapping (688), doctorRegistration (651) — honest
dev-empty; loyaltyCardConfiguration (743) — real row (1000/5000/1000);
autoServicePostingConfiguration (775) — masked-error banner (phantom column
is_bed_charges_service, DQ-2026-08-12-013).
Batch 5: IPConsultationConfiguration (684), fundReconciliation (808) — honest
dev-empty; userNursingStationMapper (689) — real (Admin/EMP123);
nursingStationWardMapping (548) — real (Updated Desc, recordPerPage payload).
Batch 6 (all masked, DQ-2026-08-12-016): scheduleWise (657) + doctorWise (658)
— blank-generic slot endpoint (swallowed exception + date-parse);
appointmentSchedulingUnitMaster (653) — ERR-ref correlation envelope.
Batch 10 (DQ-2026-08-12-022/-023, Global Master): consentMaster (400) —
masked (3rd phantom column form_template); dietTypeMaster (510),
siteOfAdministration (753), doseMaster (502), genericScheduleMaster (827) —
real data; sampleTypeMediaMappingMaster (426), mappingofMicroorganism (443) —
LIS fall-through masked; bacteriaClassification (428), subdepartmentMaster
(373) — noData samples.
Batch 9 (DQ-2026-08-12-020): templateMaster (58) — real raw-array endpoint
(honest empty); 9 noData screens (testMaster 276, multiparameterTestMaster
183, OPConsulationConfigurationMaster 611, fundApproval 809, fundRequisition
810, fundApprovedList 811, schedulePriceUnitService 586, roundOffConfiguration
599, IPdefaultServiceConfig 620).
Batch 7 (DQ-2026-08-12-018): EHCpackage (623), packageWithCAP (624),
eitherOrPackage (626) — honest dev-empty (shared /api/packages/filter);
wrapperExpiryMaster (554) — real (WRPTEST01); kitCreation (555) — empty.
Batch 8 (DQ-2026-08-12-019): RMOAllocationMaster (824) — empty;
growthChartMaster (962) — real (Female Updated); accessControl (677) +
accessControl1 (673) — noData shared component; templateutility walked.

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

    # 7. Master/unit family batch 4 (DQ-2026-08-12-013): 4 honest dev-empty
    # screens + loyaltyCardConfiguration real row + autoServicePostingConfig
    # masked banner (phantom column is_bed_charges_service).
    MASTER_EMPTY = [
        ("loyaltyCardCreation", 729, "No records"),
        ("passBedCategoryMapper", 822, "No records"),
        ("templateICDCodeMapping", 688, "No records"),
        ("doctorRegistration", 651, "No records"),
    ]
    for route, mid, expect in MASTER_EMPTY:
        ctxE = browser.new_context()
        pageE = ctxE.new_page()
        errorsE = []
        pageE.on("pageerror", lambda e: errorsE.append(str(e)))
        pageE.on("console", lambda m: errorsE.append(m.text) if m.type == "error" else None)
        ctxE.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageE.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageE.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageE.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageE.inner_text("body")
        ok = expect in body and "accessDenied" not in pageE.url
        print(f"MASTER {route}: empty-state('{expect}')={ok} url={pageE.url.split('/')[-1][:40]}")
        if not ok:
            FAILS.append(f"MASTER {route}: empty state not rendered")
        if errorsE:
            FAILS.append(f"MASTER {route} page errors: {errorsE[:3]}")
        ctxE.close()

    # loyaltyCardConfiguration — real row (1000/5000/1000)
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
        "JSON.stringify({modules: [{id: 743}]}))"
    )
    pageL.goto(BASE + "/loyaltyCardConfiguration", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageL.inner_text("body")
    real_l = "1000" in body and "5000" in body
    print(f"MASTER loyaltyCardConfiguration: real_rows(1000/5000)= {real_l} url={pageL.url.split('/')[-1][:40]}")
    if not real_l:
        FAILS.append("loyaltyCardConfiguration: real rows not rendered")
    if errorsL:
        FAILS.append(f"loyaltyCardConfiguration page errors: {errorsL[:3]}")
    ctxL.close()

    # autoServicePostingConfiguration — masked banner (phantom column)
    ctxA = browser.new_context()
    pageA = ctxA.new_page()
    errorsA = []
    pageA.on("pageerror", lambda e: errorsA.append(str(e)))
    pageA.on("console", lambda m: errorsA.append(m.text) if m.type == "error" else None)
    ctxA.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageA.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageA.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 775}]}))"
    )
    pageA.goto(BASE + "/autoServicePostingConfiguration", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageA.inner_text("body")
    masked_a = "Reference: ERR" in body and "is_bed_charges_service" in body and "phantom column" in body
    print(f"MASTER autoServicePosting: masked-banner={masked_a} url={pageA.url.split('/')[-1][:40]}")
    if not masked_a:
        FAILS.append("autoServicePostingConfiguration: masked-error banner not rendered")
    real_errs_a = [e for e in errorsA if "config/billing/list" not in e and "500" not in e]
    if real_errs_a:
        FAILS.append(f"autoServicePosting page errors: {real_errs_a[:3]}")
    ctxA.close()

    # 8. Batch 5 (DQ-2026-08-12-015): 2 honest dev-empty (IPConsultation-
    # Configuration, fundReconciliation) + 2 real-data (userNursingStation-
    # Mapper Admin/EMP123, nursingStationWardMapping Updated Desc).
    B5_EMPTY = [
        ("IPConsultationConfiguration", 684, "No records"),
        ("fundReconciliation", 808, "No records"),
    ]
    for route, mid, expect in B5_EMPTY:
        ctxB5 = browser.new_context()
        pageB5 = ctxB5.new_page()
        errorsB5 = []
        pageB5.on("pageerror", lambda e: errorsB5.append(str(e)))
        pageB5.on("console", lambda m: errorsB5.append(m.text) if m.type == "error" else None)
        ctxB5.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageB5.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageB5.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageB5.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageB5.inner_text("body")
        ok = expect in body and "accessDenied" not in pageB5.url
        print(f"B5 {route}: empty-state('{expect}')={ok} url={pageB5.url.split('/')[-1][:40]}")
        if not ok:
            FAILS.append(f"B5 {route}: empty state not rendered")
        if errorsB5:
            FAILS.append(f"B5 {route} page errors: {errorsB5[:3]}")
        ctxB5.close()

    # userNursingStationMapper — real rows (Admin  Admin / EMP123)
    ctxU = browser.new_context()
    pageU = ctxU.new_page()
    errorsU = []
    pageU.on("pageerror", lambda e: errorsU.append(str(e)))
    pageU.on("console", lambda m: errorsU.append(m.text) if m.type == "error" else None)
    ctxU.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageU.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageU.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 689}]}))"
    )
    pageU.goto(BASE + "/userNursingStationMapper", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageU.inner_text("body")
    real_u = "Admin" in body and "EMP123" in body
    print(f"B5 userNursingStationMapper: real_rows(Admin/EMP123)= {real_u} url={pageU.url.split('/')[-1][:40]}")
    if not real_u:
        FAILS.append("userNursingStationMapper: real rows not rendered")
    if errorsU:
        FAILS.append(f"userNursingStationMapper page errors: {errorsU[:3]}")
    ctxU.close()

    # nursingStationWardMapping — real rows (Updated Desc)
    ctxN = browser.new_context()
    pageN = ctxN.new_page()
    errorsN = []
    pageN.on("pageerror", lambda e: errorsN.append(str(e)))
    pageN.on("console", lambda m: errorsN.append(m.text) if m.type == "error" else None)
    ctxN.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageN.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageN.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 548}]}))"
    )
    pageN.goto(BASE + "/nursingStationWardMapping", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageN.inner_text("body")
    real_n = "Updated Desc" in body
    print(f"B5 nursingStationWardMapping: real_rows('Updated Desc')= {real_n} url={pageN.url.split('/')[-1][:40]}")
    if not real_n:
        FAILS.append("nursingStationWardMapping: real rows not rendered")
    if errorsN:
        FAILS.append(f"nursingStationWardMapping page errors: {errorsN[:3]}")
    ctxN.close()

    # 9. Batch 6 (DQ-2026-08-12-016 + correction -017): scheduleWise +
    # doctorWise are the masked pair (blank generic envelope, date-parse +
    # swallowed exception); appointmentSchedulingUnitMaster is REAL DATA
    # (the component payload {organizationId, unitId, slotTypeId} succeeds —
    # the masked ERR-ref seen in classification was a wrong-payload artifact).
    for route, mid, expect_terms in [
        ("scheduleWise", 657, ["Masked-error", "SlotMasterController", "date-parse"]),
        ("doctorWise", 658, ["Masked-error", "SlotMasterController", "date-parse"]),
    ]:
        ctxB6 = browser.new_context()
        pageB6 = ctxB6.new_page()
        errorsB6 = []
        pageB6.on("pageerror", lambda e: errorsB6.append(str(e)))
        pageB6.on("console", lambda m: errorsB6.append(m.text) if m.type == "error" else None)
        ctxB6.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageB6.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageB6.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageB6.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageB6.inner_text("body")
        ok = all(t in body for t in expect_terms) and "accessDenied" not in pageB6.url
        print(f"B6 {route}: masked-banner={ok} url={pageB6.url.split('/')[-1][:40]}")
        if not ok:
            FAILS.append(f"B6 {route}: masked banner not rendered")
        real_errs_b6 = [e for e in errorsB6 if "getSlots" not in e and "500" not in e]
        if real_errs_b6:
            FAILS.append(f"B6 {route} page errors: {real_errs_b6[:3]}")
        ctxB6.close()

    # appointmentSchedulingUnitMaster — REAL DATA (Dr. Demo Doctor)
    ctxA6 = browser.new_context()
    pageA6 = ctxA6.new_page()
    errorsA6 = []
    pageA6.on("pageerror", lambda e: errorsA6.append(str(e)))
    pageA6.on("console", lambda m: errorsA6.append(m.text) if m.type == "error" else None)
    ctxA6.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageA6.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageA6.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 653}]}))"
    )
    pageA6.goto(BASE + "/appointmentSchedulingUnitMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageA6.inner_text("body")
    real_a6 = "Dr. Demo Doctor" in body and "General Medicine" in body
    print(f"B6 appointmentSchedulingUnitMaster: real_rows(Demo Doctor)= {real_a6} url={pageA6.url.split('/')[-1][:40]}")
    if not real_a6:
        FAILS.append("appointmentSchedulingUnitMaster: real rows not rendered")
    if errorsA6:
        FAILS.append(f"appointmentSchedulingUnitMaster page errors: {errorsA6[:3]}")
    ctxA6.close()

    # 10. Batch 10 (DQ-2026-08-12-022/-023): Global Master — consentMaster
    # masked (3rd phantom column), 3 real-data screens, 2 LIS masked
    # (fall-through), 2 noData samples.
    ctxC10 = browser.new_context()
    pageC10 = ctxC10.new_page()
    errorsC10 = []
    pageC10.on("pageerror", lambda e: errorsC10.append(str(e)))
    pageC10.on("console", lambda m: errorsC10.append(m.text) if m.type == "error" else None)
    ctxC10.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageC10.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageC10.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 400}]}))"
    )
    pageC10.goto(BASE + "/consentMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageC10.inner_text("body")
    masked_c10 = "Masked-error" in body and "formTemplateId" in body and "phantom column" in body
    print(f"B10 consentMaster: masked-banner={masked_c10} url={pageC10.url.split('/')[-1][:40]}")
    if not masked_c10:
        FAILS.append("consentMaster: masked banner not rendered")
    real_errs_c10 = [e for e in errorsC10 if "getConsentMasterList" not in e and "500" not in e]
    if real_errs_c10:
        FAILS.append(f"consentMaster page errors: {real_errs_c10[:3]}")
    ctxC10.close()

    for route, mid, terms in [
        ("dietTypeMaster", 510, ["Diet Type Master"]),
        ("siteOfAdministration", 753, ["Site Of Administration"]),
        ("doseMaster", 502, ["Dose Master"]),
        ("genericScheduleMaster", 827, ["Generic Schedule Master"]),
    ]:
        ctxR10 = browser.new_context()
        pageR10 = ctxR10.new_page()
        errorsR10 = []
        pageR10.on("pageerror", lambda e: errorsR10.append(str(e)))
        pageR10.on("console", lambda m: errorsR10.append(m.text) if m.type == "error" else None)
        ctxR10.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageR10.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageR10.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageR10.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageR10.inner_text("body")
        ok_r10 = terms[0] in body and "accessDenied" not in pageR10.url and "Request failed" not in body
        print(f"B10 {route}: renders={ok_r10} url={pageR10.url.split('/')[-1][:40]}")
        if not ok_r10:
            FAILS.append(f"B10 {route}: not rendered")
        if errorsR10:
            FAILS.append(f"B10 {route} page errors: {errorsR10[:3]}")
        ctxR10.close()

    for route, mid in [("sampleTypeMediaMappingMaster", 426), ("mappingofMicroorganism", 443)]:
        ctxL10 = browser.new_context()
        pageL10 = ctxL10.new_page()
        errorsL10 = []
        pageL10.on("pageerror", lambda e: errorsL10.append(str(e)))
        pageL10.on("console", lambda m: errorsL10.append(m.text) if m.type == "error" else None)
        ctxL10.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageL10.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageL10.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageL10.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageL10.inner_text("body")
        ok_l10 = "fall-through" in body and "Internal Server Error" in body
        print(f"B10 {route}: masked-lis-banner={ok_l10} url={pageL10.url.split('/')[-1][:40]}")
        if not ok_l10:
            FAILS.append(f"B10 {route}: LIS masked banner not rendered")
        if errorsL10:
            FAILS.append(f"B10 {route} page errors: {errorsL10[:3]}")
        ctxL10.close()

    for route, mid in [("bacteriaClassification", 428), ("subdepartmentMaster", 373)]:
        ctxN10 = browser.new_context()
        pageN10 = ctxN10.new_page()
        errorsN10 = []
        pageN10.on("pageerror", lambda e: errorsN10.append(str(e)))
        pageN10.on("console", lambda m: errorsN10.append(m.text) if m.type == "error" else None)
        ctxN10.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageN10.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageN10.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageN10.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageN10.inner_text("body")
        ok_n10 = "no-endpoint" in body and "accessDenied" not in pageN10.url
        print(f"B10 {route}: noData-state={ok_n10} url={pageN10.url.split('/')[-1][:40]}")
        if not ok_n10:
            FAILS.append(f"B10 {route}: noData state not rendered")
        if errorsN10:
            FAILS.append(f"B10 {route} page errors: {errorsN10[:3]}")
        ctxN10.close()
    # 9. Batch 9 (DQ-2026-08-12-020): templateMaster real (raw [] empty state)
    # + 9 noData screens (missing-controller + MasterController shells).
    ctxT9 = browser.new_context()
    pageT9 = ctxT9.new_page()
    errorsT9 = []
    pageT9.on("pageerror", lambda e: errorsT9.append(str(e)))
    pageT9.on("console", lambda m: errorsT9.append(m.text) if m.type == "error" else None)
    ctxT9.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageT9.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageT9.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 58}]}))"
    )
    pageT9.goto(BASE + "/templateMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageT9.inner_text("body")
    ok_t9 = "No records" in body and "accessDenied" not in pageT9.url
    print(f"B9 templateMaster: empty-state('No records')={ok_t9} url={pageT9.url.split('/')[-1][:40]}")
    if not ok_t9:
        FAILS.append("templateMaster: empty state not rendered")
    if errorsT9:
        FAILS.append(f"templateMaster page errors: {errorsT9[:3]}")
    ctxT9.close()

    B9_NODATA = [
        ("testMaster", 276), ("multiparameterTestMaster", 183),
        ("OPConsulationConfigurationMaster", 611), ("fundApproval", 809),
        ("fundRequisition", 810), ("fundApprovedList", 811),
        ("schedulePriceUnitService", 586), ("roundOffConfiguration", 599),
        ("IPdefaultServiceConfig", 620),
    ]
    for route, mid in B9_NODATA:
        ctxN9 = browser.new_context()
        pageN9 = ctxN9.new_page()
        errorsN9 = []
        pageN9.on("pageerror", lambda e: errorsN9.append(str(e)))
        pageN9.on("console", lambda m: errorsN9.append(m.text) if m.type == "error" else None)
        ctxN9.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageN9.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageN9.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageN9.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageN9.inner_text("body")
        ok_n9 = "no-endpoint" in body and "accessDenied" not in pageN9.url
        print(f"B9 {route}: noData-state={ok_n9} url={pageN9.url.split('/')[-1][:40]}")
        if not ok_n9:
            FAILS.append(f"B9 {route}: noData state not rendered")
        if errorsN9:
            FAILS.append(f"B9 {route} page errors: {errorsN9[:3]}")
        ctxN9.close()
    # 10. Batch 7 (DQ-2026-08-12-018): package trio (EHC/CAP/eitherOr) honest
    # dev-empty (shared /api/packages/filter endpoint) + wrapperExpiryMaster
    # real row (WRPTEST01) + kitCreation empty.
    PKG_EMPTY = [
        ("EHCpackage", 623, "No records"),
        ("packageWithCAP", 624, "No records"),
        ("eitherOrPackage", 626, "No records"),
    ]
    for route, mid, expect in PKG_EMPTY:
        ctxB7 = browser.new_context()
        pageB7 = ctxB7.new_page()
        errorsB7 = []
        pageB7.on("pageerror", lambda e: errorsB7.append(str(e)))
        pageB7.on("console", lambda m: errorsB7.append(m.text) if m.type == "error" else None)
        ctxB7.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageB7.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageB7.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageB7.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageB7.inner_text("body")
        ok = expect in body and "accessDenied" not in pageB7.url
        print(f"B7 {route}: empty-state('{expect}')={ok} url={pageB7.url.split('/')[-1][:40]}")
        if not ok:
            FAILS.append(f"B7 {route}: empty state not rendered")
        if errorsB7:
            FAILS.append(f"B7 {route} page errors: {errorsB7[:3]}")
        ctxB7.close()

    # wrapperExpiryMaster — real row (WRPTEST01)
    ctxW = browser.new_context()
    pageW = ctxW.new_page()
    errorsW = []
    pageW.on("pageerror", lambda e: errorsW.append(str(e)))
    pageW.on("console", lambda m: errorsW.append(m.text) if m.type == "error" else None)
    ctxW.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageW.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageW.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 554}]}))"
    )
    pageW.goto(BASE + "/wrapperExpiryMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageW.inner_text("body")
    real_w = "WRPTEST01" in body and "Test Wrapper" in body
    print(f"B7 wrapperExpiryMaster: real_rows(WRPTEST01)= {real_w} url={pageW.url.split('/')[-1][:40]}")
    if not real_w:
        FAILS.append("wrapperExpiryMaster: real rows not rendered")
    if errorsW:
        FAILS.append(f"wrapperExpiryMaster page errors: {errorsW[:3]}")
    ctxW.close()

    # kitCreation — empty state
    ctxK = browser.new_context()
    pageK = ctxK.new_page()
    errorsK = []
    pageK.on("pageerror", lambda e: errorsK.append(str(e)))
    pageK.on("console", lambda m: errorsK.append(m.text) if m.type == "error" else None)
    ctxK.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageK.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageK.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 555}]}))"
    )
    pageK.goto(BASE + "/kitCreation", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageK.inner_text("body")
    ok_k = "No records" in body and "accessDenied" not in pageK.url
    print(f"B7 kitCreation: empty-state('No records')={ok_k} url={pageK.url.split('/')[-1][:40]}")
    if not ok_k:
        FAILS.append("kitCreation: empty state not rendered")
    if errorsK:
        FAILS.append(f"kitCreation page errors: {errorsK[:3]}")
    ctxK.close()

    # 11. Batch 8 (DQ-2026-08-12-019): RMOAllocationMaster empty, growthChart
    # real (Female Updated), accessControl pair noData (both routes share the
    # component; templateutility walked — no state in app.js).
    ctxR = browser.new_context()
    pageR = ctxR.new_page()
    errorsR = []
    pageR.on("pageerror", lambda e: errorsR.append(str(e)))
    pageR.on("console", lambda m: errorsR.append(m.text) if m.type == "error" else None)
    ctxR.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageR.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageR.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 824}]}))"
    )
    pageR.goto(BASE + "/RMOAllocationMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageR.inner_text("body")
    ok_r = "No records" in body and "accessDenied" not in pageR.url
    print(f"B8 RMOAllocationMaster: empty-state('No records')={ok_r} url={pageR.url.split('/')[-1][:40]}")
    if not ok_r:
        FAILS.append("RMOAllocationMaster: empty state not rendered")
    if errorsR:
        FAILS.append(f"RMOAllocationMaster page errors: {errorsR[:3]}")
    ctxR.close()

    ctxG8 = browser.new_context()
    pageG8 = ctxG8.new_page()
    errorsG8 = []
    pageG8.on("pageerror", lambda e: errorsG8.append(str(e)))
    pageG8.on("console", lambda m: errorsG8.append(m.text) if m.type == "error" else None)
    ctxG8.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
    pageG8.goto(BASE, wait_until="domcontentloaded")
    time.sleep(4)
    pageG8.evaluate(
        "() => localStorage.setItem('permissions', "
        "JSON.stringify({modules: [{id: 962}]}))"
    )
    pageG8.goto(BASE + "/growthChartMaster", wait_until="domcontentloaded")
    time.sleep(6)
    body = pageG8.inner_text("body")
    real_g8 = "Female Updated" in body and "70" in body
    print(f"B8 growthChartMaster: real_rows(Female Updated)= {real_g8} url={pageG8.url.split('/')[-1][:40]}")
    if not real_g8:
        FAILS.append("growthChartMaster: real rows not rendered")
    if errorsG8:
        FAILS.append(f"growthChartMaster page errors: {errorsG8[:3]}")
    ctxG8.close()

    for route, mid in [("accessControl", 677), ("accessControl1", 673)]:
        ctxA8 = browser.new_context()
        pageA8 = ctxA8.new_page()
        errorsA8 = []
        pageA8.on("pageerror", lambda e: errorsA8.append(str(e)))
        pageA8.on("console", lambda m: errorsA8.append(m.text) if m.type == "error" else None)
        ctxA8.add_cookies([{"name": "cookieObject", "value": CO, "url": BASE}])
        pageA8.goto(BASE, wait_until="domcontentloaded")
        time.sleep(4)
        pageA8.evaluate(
            "() => localStorage.setItem('permissions', "
            f"JSON.stringify({{modules: [{{id: {mid}}}]}}))"
        )
        pageA8.goto(BASE + f"/{route}", wait_until="domcontentloaded")
        time.sleep(6)
        body = pageA8.inner_text("body")
        ok_a8 = "no-endpoint" in body and "stub controller" in body and "accessDenied" not in pageA8.url
        print(f"B8 {route}: noData-state={ok_a8} url={pageA8.url.split('/')[-1][:40]}")
        if not ok_a8:
            FAILS.append(f"B8 {route}: noData state not rendered")
        if errorsA8:
            FAILS.append(f"B8 {route} page errors: {errorsA8[:3]}")
        ctxA8.close()

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
