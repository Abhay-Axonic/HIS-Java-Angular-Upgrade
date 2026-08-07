# MORNING BRIEFING — 2026-08-07 (extended-run handover; ~10 minutes)

Everything below is analysis/verification state. NOTHING was merged, pushed, or fixed overnight.
The detailed ledger is DECISIONS_QUEUE.md; this is the executive layer. Machine clock note: the
session clock drifted; times are approximate — the SEQUENCE is accurate.

## Top 3 findings of the overnight run

1. THE GENERICSERVICE GAP (the finding of the run) — errors are marked by the backend, travel
   correctly, and are DISCARDED by the frontend at the last step. The shared GET path
   (GenericService.serviceAction) inspects ONLY HTTP-level failures; a 200-with-ERROR-envelope
   is passed to the screen handler uninspected. The traced handlers then render empty on it:
   otCompletionController null-checks the payload (tabs render blank), masterDischargeSummary
   status-gates with no else (procedure list renders blank on the discharge summary). The DAO
   marks the error, the envelope arrives, the UI throws it away. The "82% observable" figure
   collapses: on GenericService GET screens, marked errors are seen by NOBODY. (2,859 GET call
   sites across 906 files ride this path.)
2. SWALLOWED-EXCEPTION SURFACE, NOW SIZED: 8,077 swallow-shape catch blocks (EMPTY 456,
   LOG-ONLY 6,581, LOG+RETURN 1,032, PRINTSTACKTRACE 8); ~82% of the 8,077 return ERROR-marked
   envelopes (observable at the envelope — but see finding 1); the definite-invisible
   population after the full OTHER-bucket pass: 797 sites (239 in clinical/financial decision
   surfaces), plus 693 ambiguous (202 decision-surface). The targeted-fix scope is the
   decision-surface subset: 239-441 sites — NOT a campaign. 29 false-success instances named
   (BillingMasterController.myfileSynch returns "OK" on failure; BillingMasterDaoImpl
   updateFinalBill returns 0 — indistinguishable from "0 rows").
3. THE STORE-ACCESSOR CAMPAIGN IS COMPLETE — every store-cookie deref in the 80-site census is
   converted (55 screens this run, 187 gates; the whole census re-checked with a broader net —
   zero new sites). 13 branches parked ready, but the machine's Tomcat stopped booting
   (~5.7h stuck boot) after ~20h of builds — a machine reboot is needed before any further
   live verification. NO reboot was attempted (your call; the session token + deployed state
   depend on it).

## Decisions needed — ordered by consequence (each phrased as a question)

1. GENERICSERVICE: how should the shared GET path surface ERROR envelopes? (Finding 1.)
   - Options: (a) LOG-AND-FLAG — console.warn + a scope flag, zero user-visible change,
     diagnosable; (b) OPT-IN per call site — a flag like the save path's notificationStatus,
     default FALSE, decision-surface screens opt in; (c) GLOBAL GROWL on ERROR — highest
   - Recommendation: (b) — opt-in per call site, mirroring the existing save-path convention.
   - Trade-off: (a) is the safest but invisible to users; (c) risks FALSE ALARMS — 2,373
     handlers already consume status=='error' (duplicate checks, entitlement checks like
     walkInSales:1179 where ERROR is a NORMAL "no active package" and the screen intentionally
     continues) and would double-surface; (b) fixes the decision-surface screens without the
     2,859-site blast radius. My recommended first batch: the ~10-50 decision-surface handlers
     (OT completion, discharge summary, procedure order lists) opt in + render the envelope.
2. MACHINE: may I / should the machine be REBOOTED before more live work? Tomcat has not
   completed a boot since ab2 (16.2 min — the last success); pr1's deploy boot ran ~5.7h
   without completing; the restore boot also fails. Everything static is done; everything
   live (fire-tests, the new test designs) waits on this.
3. CAMPAIGN MERGES (Rule 9b): 13 branches are parked. The 6 LIVE-verified (pb1-pb4, ab1, ab2 —
   25 procurement + 10 asset screens, 109+22 gates) can merge on your 9b approval; the 6
   STATIC-ONLY (pr1, ab3, n1, db1, m1 — 20 screens) + the walkInSales cleanup + the drift tool
   should merge AFTER their fire-tests (which wait on decision 2). Suggested order: the live
   six first, then the static six after fire-test coverage.
4. SWALLOWED-EXCEPTION FIX SHAPE: approve the targeted-fix scope (239-441 decision-surface
   sites; 29 false-success named) and the per-site shape (catch -> log -> Response(ERROR,
   e.getMessage()) — the codebase's own convention), as a strictly-filtered batch. NOT a
   blanket rewrite. Cost: the caller-chain audit + frontend verification (finding 1 makes the
   frontend half mandatory). Risk: null->ERROR contract changes (the frontend must render
   envelopes — see decision 1).
5. PASSWORD-RESET TEST: the OTP seam EXISTS (the OTP is written to public.m_user_master.otp
   in the same flow that mails it) — approve building test_password_reset_journey.js (spec in
   OVERNIGHT_CYCLE_15.md §4)? It covers the identity-binding regression + enumeration.
   Recommendation: yes — it is the first fully locally-testable dark-list design.
6. DARK-LIST DESIGNS + DRIFT TOOL: approve the drift tool (verify_frontend_drift.py,
   committed, verified both directions) into DEPLOY_PROCEDURE.md (position: after the
   verify_* suite, before the fire-test; keep separate from verify_deployed_regressions.py);
   and schedule the feedback/AI-appointment tests per DARK_LIST_TEST_DESIGNS.md.

## Branches ready to merge (13, all local, nothing pushed)

LIVE-VERIFIED (fire-test passed both phases; merge on your 9b):
  pb1-pb4 (fix/store-accessor-campaign-pb1..pb4) — procurement 25 screens, 109 gates
  ab1, ab2 (asset 10 screens, 22 gates) — ab1 also carries the fire-test tool's probe
STATIC-ONLY (converted + node-checked + counts verified; fire-test PENDING — Step 6 priority):
  pr1 (pharmacy remainder 8 screens, 25 gates) — deploy attempted, boot stuck, never tested
  ab3 (preventiveMaintenanceController, 2 gates) — the asset family's 11th (census find)
  n1 (nursing 4 screens, 16 gates)
  db1 (dashboards 5 screens, 0 gates — DIVERGENT SHAPE: storeReady + their own
      storePopupAcesss, NOT getStoreObj — rationale in OVERNIGHT_CYCLE_12.md §8)
  m1 (globalServiceMaster + otBillingCompletionDetails, 13 gates)
  wd1 (walkInSales dead-code cleanup, -73 lines, BatchChangeOld + the 4716 no-op)
  fdt (verify_frontend_drift.py — committed tool, verified both directions)

## Step 6 — screens needing your click-through (and why)

STATIC-ONLY fire-test coverage first (they have NO live evidence): the 20 screens of
pr1/ab3/n1/db1/m1 + the walkInSales cleanup screen-load. Highest-value first:
  - walkinPrescription + detailsNarcoticsIssueWorklist (pr1 — pharmacy billing screens)
  - the 5 dashboards (db1 — the divergent shape needs your eyes: store-less load must show
    ONLY storePopupAcesss, never the transactional storePopup)
  - the 4 nursing screens (n1)
  - otBillingCompletionDetails (m1)
  - preventiveMaintenanceController (ab3)
Also, the pre-existing probe NOTE sites (addNewConsignmentInward, addNewAssetIndent — the
same-session probe advisories) — click-through to confirm the gated actions.
And the LIVE-verified six still get your routine Step 6 pass per your standard process.

## Bottom line

The campaign is done and queued. The run's real finding is the GenericService gap: marked
errors are discarded at the UI on every GET screen. The decisions that matter most are 1
(GenericService shape) and 2 (reboot) — everything else follows from them.
