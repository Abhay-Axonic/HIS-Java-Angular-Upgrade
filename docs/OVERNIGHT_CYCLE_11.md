# OVERNIGHT CYCLE 11 — extended run, asset batch 2 VERIFIED (2026-08-06 ~23:10 → ~23:45 BST)

## 1. What was attempted and what actually happened

- ab2 = asset family batch 2 (5 schedule/worklist screens), cumulative off ab1:
  correctiveMaintenanceReturnWorklist, createAssetScheduler, detailScheduleWorklist,
  detailsCorrectiveMaintenanceScheduleWorklist, preventiveScheduleWorklist.
- Converted 5 deref sites (2 already-guarded parses, 3 popup-else patterns — else-branch derefs
  made null-safe); 7 gates. Built, FULL-deployed (boot took 971s — 16 minutes, the longest yet;
  the machine is severely loaded after ~22h of builds/deploys), suite partial, fire-test PASS.

## 2. Per-screen Q1/Q2

- correctiveMaintenanceReturnWorklist: Q1 yes (guarded parse converted). Q2 yes — 1 gate
  (getCorectiveMaintenanceReturnWorklist).
- createAssetScheduler: Q1 yes (popup-else converted; else deref null-safe). Q2 yes — 1 gate
  (createAssetSchedule). NOTE: direct navigation redirects to assetPMSScheduler (the controller
  requires an assetInfo param) — full path is Step 6.
- detailScheduleWorklist: Q1 yes. Q2 yes — 1 gate (addSpareConsumption). NOTE: direct navigation
  resolves to scheduleWorklist (requires assetDataObject param) — full path is Step 6.
- detailsCorrectiveMaintenanceScheduleWorklist: Q1 yes (guarded parse converted). Q2 yes — 3 gates
  (itemStock, saveReplace, saveDetails).
- preventiveScheduleWorklist: Q1 yes (popup-else converted). Q2 yes — 1 gate
  (getAssetDetailsByCriteria).

## 3. Fire-tested vs gated-by-inspection

- FIRE-TESTED: all 5 states, both phases — NEGATIVE 5/5 clean, POSITIVE 5/5 clean. Probe SKIP
  (correctiveMaintenanceReturnWorklist — no primary action button in load DOM). Two states
  redirect on direct navigation (param-required) — their deref branches are static-verified;
  the click-through with real params is Step 6.
- GATED-BY-INSPECTION: user-action paths (search/save/stock/replace) — enumerated + gated.

## 4. Verification runs and raw results

- node --check x5: clean. Raw parses: 0. storeReady() counts 1/1/1/3/1 = 7/7.
- verify_rate_limit.py: run 1 = 9/10 (ALL 12 rate-limit assertions PASS; ONLY the positive-control
  UI login failed: NO-TOKEN on the cold 16-min boot). Run 2 = 4/10 — INVALID: it collided with
  run 1's polluted in-memory buckets (the documented bucket-pollution trap; a restart is the
  remedy, costing ~16 min). No third run per the 2-iteration rule. The fire-test's own full UI
  login SUCCEEDED right after — confirming the app is healthy and the positive-control failure
  was a cold-boot flake. Recorded; the next clean-bucket run happens after the next deploy.
- FIRE-TEST: negative 5/5 clean; positive 5/5 clean; probe SKIP.
- Drift after restore (5 ab2 + 5 ab1 files — the ab2 build is family-cumulative): mpesa extra only.

## 5. Claims retracted or corrected mid-cycle

- TIME-DRIFT CORRECTION: my cycle reports 06-10 estimated times up to ~00:00-01:00 BST; the
  machine clock is authoritative and read 23:17 BST mid-cycle — the estimates were too late by
  ~1h (the log4j logs are in IST, the JVM in BST; I mis-read the log4j timestamps as current).
  Sequence and content are unaffected; the SESSION_LOG 00:35 BST entry for ab1 was ~40 min early
  in reality (actual ~23:50 BST). A correction line is added to SESSION_LOG.
- Rate-verifier bucket pollution handled per the documented remedy (no third run without restart).

## 6. Branches ready to merge (verified, awaiting owner)

- fix/store-accessor-campaign-pb1 @ 156fca68 (6, 26) — 06; pb2 @ d0447003 (6, 24) — 07;
  pb3 @ 5f38ec91 (6, 29) — 08; pb4 @ 90c5dd87 (7, 30) — 09; ab1 @ a45ec6d1 (5, 15) — 10;
  ab2 @ 870deccf + docs pending (5, 7) — 11.
- ASSET FAMILY COMPLETE: 10 screens, 22 gates. Grand total: 35 screens, 131 gates.

## 7. Decisions queued for Abhay

1. Rule 9b for pb1-pb4 + ab1 + ab2. 2. Drift #5. 3. getListNewWOList defect. 4. Pre-existing
   500s (GRN dataId=0, vendor sentinel). 5. Probe NOTEs/SKIPs — Step 6 (incl. the two
   param-required asset screens). 6. ANGULAR_PENDING_FINDINGS item 2. 7. Docs reconciliation at
   merge time. 8. Rate-verifier positive-control flake on cold boots (documented; next clean run
   after a deploy restart).

## 8. What I will do next

- Queue item 3: PHARMACY REMAINDER (6: detailsPatientReturnAcceptanceMedication,
  narcoticsIssueWorklist, detailsNarcoticsIssueWorklist, walkinPrescription,
  assigningPickerDispenser, OPassigningPickerDispenser) — 1 batch; then nursing (4), dashboards
  (trace one fully first), masters (1), dead-code cleanup, drift tool.
