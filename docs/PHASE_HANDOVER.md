# PHASE HANDOVER — the transaction survey and rebuild, for the backend owner

2026-08-13. This document is the phase's output. It is written for the
person who owns the backend drift — you can act on it without reading the
ledger (docs/DECISIONS_QUEUE.md holds the full per-flow evidence; every
claim here traces to a live endpoint error or a psql row).

---

## 1. WHAT WORKS — the flows built and traced (8 signed flow maps)

Each of these was: a live write smoke (T0) → a working frontend screen →
a **browser golden trace** (a real form save through the browser, verified
by its psql rows, torn down cleanly). They are on `main` and the routes are
live. What they prove: **the write path from UI to row works end-to-end for
these flows, and the number/stock/sample context they need is now seeded.**

| Flow | Route | Golden-trace evidence |
|---|---|---|
| Transfer screen (65) | transferScreen | W2/W3 accept + reject |
| Donor registration (719) | donorRegistration | donor row 6 |
| Shift closing (600) | shiftClosing | balance id 24 |
| Phlebotomist home collection (155) | phlebotomistHomeCollection | collection id 17 |
| Cross-match register (blood bank) | crossMatchRegister | master id 22 + detail |
| Blood issue (blood bank) | bloodIssue | issue row 87 + detail |
| Nursing order (ADT) | nursingOrder | mapper row 9 |
| Lab sample type (LIS) | labSampleType | success + clean teardown |

Route count: 364. Gates: tsc 0, ng build 0, route targets + route-survival
standing checks PASS. REBUILD_COUNT: 362.

The T0 smoke instrument also PROVED the method's reach and its limit: a
live write reaching SQL is the only thing that confirms or kills a drift
flag (three promotions + one demotion below).

## 2. WHAT IS BROKEN AND NEEDS YOU — the confirmed phantoms + the code defects

**Confirmed phantoms (17) — a column/table the application code references
that does not exist in the database.** Each was hit by a live call (write or
read). The fix is one of: drop the entity mapping / add the DDL column /
create the missing relation.

| # | Table | Missing column | How it was confirmed |
|---|---|---|---|
| 1 | lab.m_parameter_master | `technique_id` | live SQL error |
| 2 | public.t_auto_posting_services_cofiguration | `is_bed_charges_service` | live SQL error |
| 3 | adt.m_consent_master | `form_template_id` | live SQL error |
| 4 | doctor.m_orderset_master | `icpc_id` | live SQL error |
| 5 | billing.t_package_consumption_master | `package_applicable_date` | live 501 on the OPD bill write |
| 6 | patient.t_patient_details | `family_id` | patient-reg 501 (masked) |
| 7 | opd.m_appointment_master | `appointment_unique_id` | appointment write 501 (masked) |
| 8 | public (whole schema) | TABLE `t_message_queue` | appointment write 501 (masked) |
| 9 | public.t_denomination_details | `cash_in_out_id` | cash-handover, atomic rollback |
| 10 | billing.m_deposit_master | `cancel_reason_id` | deposit write reached SQL after the seed |
| 11 | pharmacy.t_prescription | `medication_type` | walk-in write reached SQL after the seed |
| 12 | lab.t_lab_sample_dtls | `icmr_id` | LIS accept's live SELECT (entity @Column) |
| 13 | billing.t_credit_note_detail | `status` | workloadDashboard's live SELECT (`column tcnd.status does not exist` — a READ-side kill; DQ-077) |
| 14 | public (whole schema) | TABLE `user_chat_details` | incident/NABH reporting lists' live SELECT (`relation "public.user_chat_details" does not exist` — a phantom relation like #8, READ-side; DQ-081) |
| 15 | lab.t_template_result | `block_capture_note` | histopathology getReport's live SELECT (`column ttemplater0_.block_capture_note does not exist` — a READ-side kill; DQ-082) |
| 16 | LIS worklist (SearchDto family, `ltlsc`-aliased) | `status` | the report worklists' live SELECT (`column ltlsc.status does not exist` — the second `status` phantom; DQ-083) |
| 17 | template_mst | `is_html_print` | diagnosticProcedureReport's live SELECT via `/api/global/templates/byService` (`column template_mst.is_html_print does not exist`; DQ-084) |
| 18 | emr.m_allergy_master | `cims_code` + `cims_type` | allergyMaster's live save SQL `column allergymas0_.cims_code does not exist` (ERR-d8701a37, catalina 8782261 — DQ-091, live-confirmed by the owner's click-through) |

**THE POSITION, RESTATED (2026-08-13, after the renders + the T0s — the
boundary moved twice since this document first described it):**
- **BUILDABLE (7 screens — read-verified, render-trace gated, read-only,
  shorter loop):** radiologyReportValidation (feeds live), radiologyReportReleaseWorkList,
  dueDepositReport (live rows), detailsConsignmentManagerDashboard,
  MPShahEncounterList, MPShahEncounterSummary, MPShahPatientList.
  **ALL SEVEN ARE BUILT (c11533a6) and render through the app's own login
  flow — the class is CLOSED at 378 rebuilt (computed from the feature dirs
  with the exclusion set; REBUILD_COUNT.md is the log, not the source).**
- **DRIFT-BLOCKED (~20 screens — the 15 confirmed phantoms + the 2 code
  defects, each named above and in section 5):** the incident/NABH family
  (the user_chat_details table), the LIS family (the ltlsc status + icmr_id),
  result-entry (icmr_id), diag-proc (is_html_print), workloadDashboard
  (tcnd.status), histopathology (block_capture_note), + the transaction
  parks.
- **LEGACY-DEFECT (3 — the screens' own writes are broken in the legacy):**
  medicationErrorReporting (Save calls the undefined saveRack), UtiReporting
  (no wired save path), PatientFallReporting (its own literal payload gets a
  service-level 400).
- **OPEN EXCAVATION (1):** the pharmacy save (its own literal payload gets a
  non-Jackson 400; the DTO mismatch named in DQ-085).
- **THE SHELL CLASS IS EMPTY** (11 of 11 live-rendered "shells" are real
  screens — ledger #14); **the contract-wall class has dissolved** (six of
  its verdicts were probe-shape and have since become confirmed drift,
  legacy defects, or builds — ledger #13).

**THE FINAL POSITION (2026-08-13, session close — every verdict live-tested):**
- **378 rebuilt** (computed from the feature dirs with the exclusion set).
- **Four classes remain, with counts:**
  1. **DRIFT-BLOCKED ~20** — behind the 15 confirmed phantoms + 2 code
     defects (each named above with its table/column and resume condition).
  2. **LEGACY-DEFECT 3** — the nursing trio's writes (medication's
     saveRack-undefined, Uti's unwired save — both live-click-tested:
     the Save opens the store-select modal and never reaches a save
     function; PatientFall's literal payload gets a service-level 400).
  3. **OPEN EXCAVATION 1** — the pharmacy save (its own literal payload
     gets a non-Jackson 400; the DTO mismatch named in DQ-085).
  4. **BACKEND-OWNED 0 open** — everything else has a verdict; the next
     move belongs to the backend owner (the handover's section 2 + 5).

**Two code defects (not drift — the code evaluates wrongly):**
1. **Admission bed-occupancy gate** (`BedAllocationController.savePatientAdmission`,
   the `isBedBookedOrNot` gate): returns "Bed is already occupied" for
   EVERY bed — including beds never present in the occupancy log. The 12
   beds in `m_bed_master` all reject. The gate's logic is inverted.
2. **I/O charts masked-partial** (`IntakeOutputChartServiceImpl:67` ignores
   the details-DAO response): the master row lands, the chart lines are
   silently dropped, and the API answers "Added Successfully." — a
   false-success write (escalated as a masked-partial class).

## 3. WHAT WAS NEVER BROKEN — the unseeded environment, and what the seed fixed

The seed (`tools/seed_number_config.py` — idempotent, persistent, guarded)
is the instrument that separates the environment from the drift. **The 3:0
ratio is the headline: seeding unblocked THREE flows (donor, walk-in,
deposit); code changes unblocked NONE.** What was actually missing:

- the prefix-number config (screens 123/146/164/149 — `m_prefix_configuration`
  + `t_prefix_running_number` + the `m_screen` rows incl. one missing
  entirely), plus the `[SF]` sequential token format;
- the blood-bank stock + donor + bag rows the blood-issue/cross-match
  reference (dev stock was empty);
- the LIS sample-type / sample-master / sample-details rows the LIS
  central-receiving references.

Seeding also COMPOUNDS: the cross-match flow's output became the blood-issue
flow's input — one working flow can manufacture the reference another flow
is missing. Check that before parking anything in the operational-data class.

## 4. THE DRIFT BANDS — confidence

Screening: every entity `@Column` vs the DDL, case-insensitive.
**CONFIRMED 12 / PROBABLE 176 / SUSPECT 174.**

- **CONFIRMED (12)**: reproduced by a live endpoint error — certain work.
- **PROBABLE (176)**: the entity declares the column; ZERO matches anywhere
  in the DB. **The honest statement: this band is SAMPLE-TESTED, not
  proven.** Three of three PROBABLE flags reached by a live write confirmed,
  which establishes the band is REAL rather than regex noise; the remaining
  173 are unverified, and the per-table DDL diff confirms or kills each in
  seconds.
- **SUSPECT (174)**: the name exists on ANOTHER table — plausible
  typo/artifact; lowest priority, verify when touching the table.

## 5. WHAT THE REBUILD RESUMES THE MOMENT EACH ITEM IS FIXED

| Backend item | Resumes |
|---|---|
| The 12 confirmed phantoms (2) | the drift-blocked screens: patient registration, appointment scheduling, cash handover, deposit, walk-in sale, OPD bill, LIS central-receiving (result entry follows) — each write is smoke-ready and will flip to BUILDABLE the same way the seed-unblocked flows did |
| The admission gate's inversion | admission + the bed-allocation flow (smoke-ready, precheck-verified) |
| The I/O-chart masking | I/O charts (the master already lands; the details' DAO needs its NPE + the ignored response fixed) |
| `package_applicable_date` | the OPD-bill excavation (parked as contract-incomplete-behind-confirmed-drift — its cost is certain, its payoff zero until this lands) |

The (a) smokes (LIS result entry, bill payment splits, pharmacy issue) all
die at their DTOs' Jackson walls — contract-incomplete, same class as the
OPD bill; their 21 combined flags stay unreached until the full legacy
payloads are excavated, which is only worth it once the confirmed drift in
those tables is fixed.

---

**Bottom line for the owner of the drift:** the survey is complete and
bounded. Every park has a cause; the causes are sorted into drift (12
confirmed columns + 2 code defects), unseeded environment (fixed by one
idempotent script), and contract walls (parked). The rebuild's remaining
backend-independent work is exhausted. The screens are evidence that the
method works; this list is what someone else needs to act.
