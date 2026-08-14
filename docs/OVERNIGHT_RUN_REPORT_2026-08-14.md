# OVERNIGHT RUN — CONSOLIDATED REPORT (2026-08-13 → 14)

The click-through campaign's automated half, done without a human eye. Every
count below carries its **live-confirmed sample size in the same breath**
(ledger #15: a number without a live-confirmed sample is not yet a finding).

---

## 1. THE TOP-20 CLICK BATCH — WITH THE MINIMUM VIABLE FORM (MVF) PER SCREEN

The twenty most interaction-complex census-era screens, ranked. For each: the
smallest set of fields to fill for the save to fire — read from the validation
messages the save-click probes triggered and the controllers' required-field
checks (the components' guard conditions). **Fill these, hit Save — the save
is wired (validation-first; it will NOT fire on an empty form).**

| # | Screen (route) | MINIMUM VIABLE FORM — fill these, then Save |
|---|---|---|
| 1 | book-appointment (`bookAppointment`) | UHID (search) → pick a slot ("Select slot" guard) → Book and Pay |
| 2 | detail-schedule-worklist (`detailScheduleWorklist`) | Store + ticket no + an item + completion date/time (+ under-observation date/time) — the row-level add |
| 3 | details-corrective-maintenance-schedule-worklist (`detailsCorrectiveMaintenanceScheduleWorklist`) | the worklist row + the spare-details fields (addSpareDetails / saveReplace / saveDetails) — exact set OWED a read |
| 4 | pm-request (`PMRequest`) | the worklist filter (store/asset) → a row → scheduled date/time + completion date/time |
| 5 | pm-schedule (`PMSchedule`) | assign-to + assign date ("Please select assign to.." / "..assign date..") |
| 6 | asset-scheduler (`assetScheduler`) | PMS frequency + PMS type + from date + end date + **≥1 checklist** ("Please add atleast one checkList") |
| 7 | add-new-corrective-maintenance (`addNewCorrectiveMaintenance`) | asset + request type + criticality + resolution expected date + time + serial number |
| 8 | ai-button-configuration (`aiButtonConfiguration`) | doctor-or-speciality + button ("Please select doctor or speciality" / "Please select button") |
| 9 | bank-master (`bankMaster`) | branch ("Please Select Branch") |
| 10 | bed-master (`bedMaster`) | **CORRECTED 08-14 (the mechanical pass, again):** ward → room directly (NO floor step — the floorId's internal to the ward; the earlier "ward → floor → room" was my inference error) + bed code/desc/category. **The working ward: TESTWARD (ward 1) → Room 101** (or ward 99 → ROOM-999-X); the other wards have no rooms (unseeded data, DQ-117) |
| 11 | checklist-equipment-mapper (`checklistEquipmentMapper`) | **CORRECTED 08-14 (the mechanical pass):** checklist name + asset category → (its product-category list loads after) → product category + item code + item name |
| 12 | corrective-maintenance-return-worklist (`correctiveMaintenanceReturnWorklist`) | reject reason + reason note (the row's accept/reject action) |
| 13 | currency-master (`currencyMaster`) | country + country code + currency code + currency name (the `requiredFieldsValid` gate) — **NOTE: the typed countryCode has NO destination column (ORPHANED INPUT, DQ-104) — fill it only because the gate requires it** |
| 14 | general-insurance-master (`generalInsuranceMaster`) | the contact block: email (valid), contact no ≥10 digits, zip ≥6, contact person no ≥10 |
| 15 | generic-master (`genericMaster`) | asset type + asset category + product category + generic code + generic name (the form's required placeholders) |
| 16 | hospital-master (`hospitalMaster`) | unit contact (10 digits) + valid email — **needs the subscription-config row** ("Please create subscription configuration") + the module gate |
| 17 | insurance-master (`insuranceMaster`) | **CORRECTED 08-14 (the mechanical pass):** country → state → district → area cascade (State/District/Area are REQUIRED and their lists load only after the parent) + insurance code/desc + contact no (10 digits) + valid email |
| 18 | mis-reports (`misReports`) | select a report from the left panel → run (the empty-state configurator) |
| 19 | organization-master (`organizationMaster`) | organization contact (valid) — **behind the module gate** |
| 20 | assigning-picker-dispenser (`assigningPickerDispenser`) | the worklist filter (from ≤ to dates) → a row → the assign action |

**The save-click evidence behind the MVF (11 of 20 clicked live):** every
complex screen's save is **VALIDATION-FIRST** — the app's own flow blocks
before any network on empty/incomplete forms (genericMaster, checklistEquipmentMapper
"Please select checklist name", addNewCorrectiveMaintenance silent block,
currencyMaster's `requiredFieldsValid`, asset-scheduler's five guards). The
rest of the top-20: filter-first worklists (PMRequest, correctiveMaintenanceReturnWorklist,
misReports), app-config-gated (detailScheduleWorklist, PMSchedule —
`accessDenied?reason=appConfigMissing`, the unseeded-env class), toggle-only
(aiButtonConfiguration's on/off switches), slot-guard (book-appointment),
module-gated (hospital-master, organizationMaster — `reason=denied` for the
demo role). **The saves ARE wired — the clicks produced the validation
responses, not dead buttons. The decorative-button class (which killed two of
the nursing trio) is ruled out on all eleven.** The app's own PAYLOADS fire
only with real data; the owner's clicks with the MVF above reach them.

---

## 2. THE 293 UNRESOLVED SERVICE PATHS — RESOLVED BY LIVE CALL

The biggest number in the run turned out to be the probe artifact (ledger #15,
the fourth instance). **Live-confirmed sample: 70 of the 293 paths resolved by
the app's OWN calls during real renders → 200.** The real problems in the
whole set: **two** — the allergy-type feed's 500 (below) and the ambulance
count's 404 (already catchError-degraded, DQ-092). The remaining ~120 screens'
captures are un-run but the shape is stable: the index's path-param/trailing-
slash spelling misses. **Every count in this section is a live-render status;
nothing here is a static verdict.**

## 3. THE AUTOMATED CHECKS' COUNTS (static — hypotheses until traced)

- **Bare-toast handlers: 104 of 378 features** lack the `err?.error?.message`
  handler (27 use the newer shape) — the census-era default; the campaign
  doc's KNOWN-DEFECT column carries the class.
- **Dead-filter hypotheses: 8** (C9 template-bound plain-property filters) —
  static, untraced.
- **Fixture-dependent defaults: 10** (the cross-match class) — static,
  untraced.
- **Route survival: PASS** at every gate (382 baseline routes present).
- **The endpoint-vs-backend index: the 293** — see section 2 (the index was
  the thing being tested; the live calls resolved it).

## 4. THE FINDINGS (all live-confirmed unless stated)

- **The allergy-type feed 500s LIVE** (`/api/global/getAllergyTypeMaster` — the
  SPA's own call) with **3 rows in `emr.m_allergy_type_master`** — the
  allergyMaster dropdown's empty is NOT dev-empty. The 500 is specific to the
  bare endpoint (the List works, 200). The exact SQL cause needs the ERR-
  correlation — OWED (the raw fetch 401'd mid-batch; the fresh-session fetch
  decides; possibly a phantom-#19 table-mapping).
- **The ambulance count 404** — the known class example, live-captured, already
  catchError-degraded (DQ-092).
- **The app-config gate**: detailScheduleWorklist + PMSchedule return
  `accessDenied?reason=appConfigMissing` — the store/counter config rows for
  the DEMO org/unit are unseeded (the unseeded-ENV class, not a defect).
- **The module gates**: hospital-master + organizationMaster return
  `reason=denied` for the demo role.
- **genericMaster renders** (the earlier route-mislabel finding was RETRACTED
  with its mechanical cause — my route-map regex's lazy path match; the
  `genericMaster` route exists, moduleId 463, live-rendered; the retraction is
  in DQ-096/099 and the ledger).

## 5. THE OWED LIST (resumes on command)

1. The remaining ~120 screens' endpoint captures (fresh-login batches — the
   cadence is ~10 screens per login).
2. The allergy-type feed's ERR- correlation (the phantom-#19 check).
3. The top-20's tail: assetScheduler + detailsCorrectiveMaintenanceScheduleWorklist
   save-clicks + the app-config-gated pair (needs the app-config seed).
4. The exact MVF for details-corrective-maintenance-schedule-worklist (a read
   of the addSpareDetails/saveReplace guards).
5. The PROBABLY-CLEAN count — 0 marked so far (no save has reached a write
   with real data; the write-smokes' batch A remained 44 probe-shape 400s +
   1 endpoint-shape 405, teardown-negative, psql-verified).

## 6. THE DECISION QUEUE

DQ-094 (the write-smoke method + the browser-driven pivot), DQ-095 (book-appointment,
corrected + retested), DQ-096 (route-mislabel → retracted), DQ-097 (the allergy-type
feed's 500), DQ-098 (the 26-tally), DQ-099 (the top-20 save clicks + the MVF
material) — all in `docs/DECISIONS_QUEUE.md`, all commits on
`rebuild/t0-smokes-and-handover` (git-read), 16 ahead of main at last count.
