# OVERNIGHT CYCLE 16 — GenericService scoping + OTHER bucket full pass + morning package (2026-08-07)

Analysis and proposal only. Nothing fixed/built/merged. Limits unchanged.

## 1. GENERICSERVICE FIX — SCOPED (decision-ready)

- CALL-SITE CENSUS (method stated: any `serviceAction(` line across all js under
  resources/assets; both GenericService and BillingGenericService): 14,554 total call sites,
  2,859 GET sites, 906 files with GET usage. The shared-function blast radius is ~2,900 GET
  screens/calls.
- HANDLER CLASSES (Rule 10 — three classes traced, least-confident/most-likely-to-break
  first):
  (1) NULL-CHECK RENDER (otCompletionController:119-139): ERROR envelope has listObject=null
      -> tabs render blank. Currently invisible; surfacing = fix.
  (2) STATUS-GATE NO-ELSE (masterDischargeSummary:8176): ERROR -> list empty/stale, no growl.
      Currently invisible; surfacing = fix.
  (3) ERROR-AS-NORMAL-PATH (walkInSalesController:1179): `else if(status=="error"){ stopLoader;
      }` then the screen CONTINUES (getSelfPaidConsultation). ERROR = "no active package" —
      a normal outcome the screen intentionally proceeds past. Surfacing = FALSE ALARM.
  PLUS the 2,373 handlers that already compare response.data.status == 'error' (duplicate
  checks, validation, entitlement) — a global growl would DOUBLE-SURFACE on all of them.
- FALSE-ALARM CLASSES: any endpoint whose ERROR envelope is the ANSWER (entitlement checks,
  duplicate/exists checks, validate*). These exist in quantity (the 2,373 handlers are their
  consumers).
- SMALLEST SAFE VERSION (recommendation): OPT-IN PER CALL SITE — a flag on serviceAction
  mirroring the save path's notificationStatus convention, default FALSE; the decision-surface
  handlers (OT completion, discharge summary, procedure lists) opt in and render the envelope.
  Alternatives: log-and-flag (safest, invisible to users) / global growl (highest leverage,
  highest false-alarm risk — NOT recommended). Cost: the flag + the ~10-50 handler updates +
  their verification. Risk: none to the 2,859 GET sites that don't opt in; the opt-in screens
  must render the envelope (their handlers' no-else branches).

## 2. OTHER BUCKET — FULL PASS (850 sites; method + blind spots in the computation)

Resolution rules: literal success values ("OK"/0/1/true) -> FALSE-SUCCESS (named); a returned
variable the catch body never mentions -> INVISIBLE (returns its pre-try value, typically
null/default); a variable the catch assigns an error-marked value -> OBSERVABLE; method-call
returns and catch-touched variables -> AMBIGUOUS (one-read-per-site to finish). Blind spots:
reference-mutation (the catch mutating an object the variable points to without a text
assignment) under-counts observability; AMBIGUOUS is counted separately, not forced.
RESULT: FALSE_SUCCESS 29 (11 decision-surface) | OBSERVABLE 0 | INVISIBLE 128 (40 decision) |
AMBIGUOUS 693 (202 decision). Named false-success (top): BillingMasterController.myfileSynch
("OK" on failure, :5121/:5130), BillingMasterDaoImpl.updateFinalBill (return 0, :1358),
JwtTokenFilter:322/326 (return true), AppointmentSchedulingDaoImpl:1903-2603 (return 0),
OrderDetailsMasterDaoImpl (:1865-2944, return 0/1), UserMasterDaoImpl:993 (return 0),
EncounterMasterDaoImpl:9687/9689 (return 0).
FINAL INVISIBLE POPULATION: 797 definite (was 640; +157 from OTHER), of which 239 decision-
surface (was 188); plus 693 ambiguous (202 decision-surface) — the targeted-fix scope is the
decision-surface range 239-441. The 693 ambiguous are method-call returns (the dominant
subclass) — one read each (~hours), NOT a dataflow problem.

## 3. MORNING PACKAGE — docs/MORNING_BRIEFING.md

Written (executive layer, ~10 min): decisions 1-6 ordered by consequence (GenericService shape
first, machine reboot second — everything else follows); 13 branches with verification status
and STATIC-ONLY marking; Step 6 list (20 static screens + the walkInSales load + the probe-NOTE
sites, dashboards flagged for the divergent shape); top-3 findings with the GenericService gap
first. DECISIONS_QUEUE.md remains the detailed layer.

## 4. State

- main @ d6062a73 + this cycle's commits. 13 branches parked. Machine down (no reboot).
- Next: Abhay's decisions per the briefing; the static-batch fire-tests after a reboot.
