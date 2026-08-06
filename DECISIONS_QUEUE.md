# DECISIONS QUEUE — items requiring Abhay (owner/SME)

Night-shift supervisor protocol: blocked/owner-gated items are logged here, the loop pivots to
independent work, and this file is read aloud at the next "Morning Debrief". Items are numbered;
do not delete — mark status and append.

---

## 1. [RESOLVED 2026-08-06 (day session)] BR-173 webjar/swagger prefix decision

- Evidence of resolution: main's merged JwtTokenFilter now carries 5 STATIC_PREFIXES — /resources/,
  /views/, /HIS-WAR/webjars/, /webjars/ — plus a documented /fonts/ investigation (font files
  absent from the WAR; entry commented with that note). The /webjars/ widening option (a) was
  applied during the day. Verified read-only via git show main.

## 2. [RESOLVED 2026-08-06 (day session)] Fresh JWT for BR-173 Layer 3

- Evidence: the day session ran the full Layer 3 — six harness ALL PASS + TOKEN-OK (SESSION_LOG
  18:39 BST entry, pilot post-merge proof). BR-173 merged.

## 3. [RESOLVED 2026-08-06 (day session)] BR-173 merge

- Evidence: merged via the Phase-2 rewrite branch; main now carries the default-deny filter
  (46 exact + 5 prefixes + 7 anchored patterns per the briefing; prefix list verified above).

## 4. [INFO, no action] Pre-existing working-tree state on fix/br173-default-deny, untouched

- .claude/worktrees/batch25 (gitlink) and docs/ANGULAR_PENDING_FINDINGS.md show as modified;
  20 untracked worktree dirs + SUPERVISOR.md. None are mine; all left untouched per discipline.

## 5. [OPEN — BLOCKER] Approval guard denied the deploy/verify command (overnight supervisor)

- What: at 19:0x BST 2026-08-06 the command `git archive main ... | tar -x -C /tmp/main_tree_check`
  + deployed-vs-main drift diff was DENIED by the approval client. The overnight contract permits
  build/deploy/verify locally; the guard requires consent. I stopped the deploy/verify flow and
  did not route around the denial (standing rule).
- Impact: batch-1's pending fire-test (4 zero-inline sites) and the whole batch-2 verify loop are
  blocked until either the guard auto-approves the supervisor's terminal commands or Abhay
  consents per-command.
- Question: should the overnight supervisor's deploy/verify terminal commands be auto-approved?

## 6. [OPEN] Batch-2 family choice for the store-accessor campaign

- Raw-deref census (re-derived from source 2026-08-06): 80 sites across ~78 files. Clusters:
  Inventory ~25 files, procurement/workOrder+quotation ~20, asset ~10, nursing
  (addNewComsumablePatientIssueReturn x3 + addNewMedicationPatientIssueReturn), pharmacy remainder.
  Also: fix/assigningpickerdispenser-storename-null-check and
  fix/asset-management-selectedtopstore-null-check are unmerged and their files still carry raw
  parses — fold into the campaign or keep separate?
- Question: which family next (or proceed Inventory-first by size)?

## 7. [INFO, no action] mpesaApplicationConfigurationController.js deployed but not in main

- The deployed ROOT contains mpesaApplicationConfigurationController.js; main's source tree does
  not. Pre-existing unmerged live fix (drift item). Left untouched; flag for the day session.

---

*Cycle 01 (2026-08-06 19:07 BST) appended by Hermes (night-shift supervisor). Full detail in
docs/OVERNIGHT_CYCLE_01.md. ntfy webhook still not configured (no topic anywhere); the queue
file remains the durable alert.*

## 8. [OPEN] Claude Code OAuth expired — delegation unavailable overnight

- `claude auth status` → "Login: Expired — log in again"; delegation attempts fail with "OAuth
  session expired and could not be refreshed". No API key configured in env/config (empty strings).
  Interactive re-login (`claude auth login`) needs the owner's claude.ai account — credentials
  territory, untouched by the supervisor.
- Impact: batches 3+ cannot be delegated to Claude Code headless until re-authenticated.
  Fallback used for batch 2: supervisor-direct conversion (explicitly permitted by the overnight
  contract), same pattern + same verification bar — batch 2 verified PASS.
- Question: re-login Claude Code for the next shift, or is supervisor-direct the accepted mode?

## 9. [RESOLVED] Batch-2 family choice (cycle 02 answer: Inventory stock cluster)

- Done as proposed: batch 2 = 6 Inventory stock-family screens, VERIFIED PASS (see
  QF-2026-08-07-B). Batch 3 plan: addNew cluster (addNewBatchExpDateChange, addNewDrugRecall,
  addNewMaterialInward, addNewMaterialReturn, addNewNonBillableConsumption,
  addNewReturnableGatepass), then b4 (addNewScrapRequest, addNewStoreIndenting,
  addDepartmentConsumption, issueListDirectIssue, storeStockDashboard, uploadStockApproval,
  uploadStockDetails), then procurement/asset/nursing/dashboards.

## 10. [INFO] walkInSales (merged pilot) still carries 2 gated raw parses

- walkInSalesController.js:2527 (`initialStore=JSON.parse(...)` inside BatchChangeOld, AFTER a
  storeReady() gate) and :4716 (`if(initialStore==JSON.parse(...))` — a comparison parse). Both
  are SAFE (gated / comparison-only, no crash, no call) but not pattern-pure (the accessor owns
  the parse). Also preventiveMaintenance:26 is QF-AR-guarded (safe). Decision: convert on a later
  batch or leave as-is?
