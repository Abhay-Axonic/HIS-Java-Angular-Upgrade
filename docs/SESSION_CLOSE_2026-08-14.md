# SESSION CLOSE — 2026-08-14 (the DQ-122 → DQ-128 stretch)

## THE FREEZE POINT

**main is at `e5fa0169`** ("merge: patientTypeMaster toggle — the third
instance of the missing-date class") — this is the FREEZE POINT for a
baseline. A parallel project exists with a corrected backend and an optimised
schema; **both sides will be frozen before any reconciliation is attempted**.
Do not treat a served tree as main (ledger #17), and do not treat either side
as reconciled until both are frozen and the diff is taken deliberately.

## WHAT IS MERGED (main @ e5fa0169)

- **378 screens** rebuilt (the feature-dir count; the class closed at 378 per
  PHASE_HANDOVER).
- **Four rebuild-side defects found by the click campaign, three fixed**:
  1. insuranceMaster's save — the omitted audit/identity set (DQ-125); fixed,
     **confirmed by the owner's own click** (the fix landed before the
     campaign; the campaign proved it both directions and the owner re-clicked
     it on the served tree carrying both fixes).
  2. insuranceMaster's edit — the getById response-key read (`res.object`
     vs `listObject`, DQ-127); fixed, proven by a live Edit → populate →
     Update → psql cycle.
  3. patient-type-details' toggle — the wrong wire name + the missing
     updatedDate; fixed, contract-verified (negative 400 / positive 200).
  4. patient-type-master's toggle — the missing updatedDate (DQ-128, the
     third instance of the DQ-125 class); fixed, proven by a live toggle
     click → row flip with updated_by/updated_date.
- **Seventeen ledger mechanisms** (VERIFICATION_FAILURE_LEDGER.md #1–#17, the
  latest being SERVED-TREE INVISIBILITY — a served tree is not main; a claim
  about what a click exercised must state the served branch).

## QUEUED FOR THE BACKEND OWNER

- **Nineteen phantoms** (PHASE_HANDOVER.md §2, #1–#19) — the columns/tables
  the application code references that do not exist in the database.
- **Two code defects** (PHASE_HANDOVER.md §2) — the inverted Admission
  bed-occupancy gate (`BedAllocationController.savePatientAdmission` /
  `isBedBookedOrNot`) and the I/O-charts masked-partial
  (`IntakeOutputChartServiceImpl:67` ignores the details-DAO response).
- **The DQ-126 constraint, with the product position** — `fkey_area` on
  `public.m_general_insurance_company` is `FOREIGN KEY (id) REFERENCES
  m_area_master(area_id)` instead of `(area_id)`; the table, the correct form,
  the screen it blocks (general-insurance-master's save — parked behind it),
  and the position: **unique in the source DDL** (HIS-SME-DDL
  _20210430_150255584.sql:136246) while its siblings (m_vendor :136228,
  m_insurance_company :136237) are correct — a deliberate-looking wrong
  constraint, the owner's call to change.

## QUEUED AS UNFINISHED WORK

- **The 9×500 triage** — assetTypeMaster, fixedAssetMaster,
  autoServicePostingConfiguration, detailsAssetIssueAcceptance, detailsAssetReturn,
  detailsFixedAssetMaster, linenIssueAcceptanceDetails, linenIssuePendingDetails,
  parameterMaster (ERR refs + the DAO-side check in CAPTURE_BATCH_2026-08-14.md).
- **The four phantom endpoints** — dueDepositReport, modalityTypeMaster,
  correctiveMaintenanceReturnWorklist, the mpShah trio: the rebuilds call
  paths absent from both the legacy source and the deployed jars (fix = the
  real endpoint names, or a backend owner decision).
- **The response-key candidates awaiting rows** — clinic-master,
  department-store-mapper, ward-store-mapper, hospital-master,
  organization-master, machine-parameter-master (the silent-return pattern;
  their lists are empty in dev — the live check is owed when rows exist;
  clinic-master re-traced CORRECT as of this close).
- **The remaining click queue** — the env-blocked screens in the DQ-126
  entry (empty tables: checklists, fixed-assets, CM worklist, assign-picker,
  mis-reports; the appConfigMissing pair; the module-gated screens) — the
  owner's click queue once seeds/decisions land.

## READING ORDER FOR A FRESH SESSION

1. `CLAUDE.md` — the constitution (missions, non-negotiables, roles).
2. `RULES.md` — the standing rules, in full.
3. `docs/SESSION_LOG.md` — the tail (the recent stretches in one place).
4. `docs/SESSION_CLOSE_2026-08-14.md` — this close: the freeze, the queues.
5. `docs/PHASE_HANDOVER.md` — the freeze baseline, the phantoms, the code
   defects, the drift bands.
6. `docs/VERIFICATION_FAILURE_LEDGER.md` — the seventeen mechanisms (read the
   ones relevant to today's task; #15 the biggest-number rule, #16 the
   drive-or-report rule, #17 the served-branch rule).
7. `docs/BUSINESS_RULE_MANIFEST.md` + `docs/BR_INDEX.md` — before any
   behavioral change.
8. `docs/DECISIONS_QUEUE.md` — the resolved + open decisions (DQ-122…DQ-128).
9. `docs/CLICKTHROUGH_CAMPAIGN.md` — the click queue and the KNOWN-DEFECT
   markers (do not re-derive what the ledger names).

## RECONCILIATION NOTE (billing calculations)

The rebuilt billing screens carry the billing calculation **ported faithfully
from the legacy frontend**. If the parallel backend moves that logic
server-side, those screens need **rework, not reconnection** — the two sides
will disagree until the port is re-derived. The planned approach: **display
BOTH values to the biller** (the frontend-port calculation and the backend's),
so discrepancies surface where a human is already checking, and **retire the
frontend calculation once the backend's numbers are trusted**. No side is
retired early; both values render until the backend's output is proven.
