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

## 11. [RECORDED] Owner decisions 2026-08-07 (verbatim substance, from his merge-review message)

- Claude Code OAuth (item 8): "supervisor-direct is fine as the standing mode; I will re-login when
  I am next at the machine." -> RESOLVED (supervisor-direct is the standing conversion mode).
- stockAdjustmentApproval live coverage: "defer. Step 6 is mine and needs a permissioned role I do
  not currently have set up."
- The 4 local-DB drift items: "defer, document only. No schema changes without me." (No schema
  change was made; all 4 items documented in QF-2026-08-07-A/C.)
- The two unmerged null-check branches (assigningpickerdispenser, asset-management): "keep
  separate. I want them reviewable on their own."
- walkInSales stragglers (item 10): pending the supervisor's analysis — recommendation: fold the
  2-line cleanup into batch 5 (not into b1-b4 under merge review).
- Merge shape: owner confirmed cumulative b4-only merge is the intended shape; ONE merge commit
  naming all four batches. NOT executed — awaiting verbatim Rule 9b approval.
- Open: nothing blocks the merge review except the owner's Rule 9b approval itself.

## 12. [RESOLVED] Rule 9 execution complete — store-accessor campaign merged

- Rule 9b approval verbatim 2026-08-06 21:35:01 BST; --no-ff merge c3359a37; Rule 9a recorded
  797a847c. FULL frontend redeploy from merged main (backup /tmp/ROOT-backup-pre-merge-b4; boot
  167.75s; healthcheck 200). Post-merge proof: rate-limit 10/10 (TOKEN-OK), regressions PASS,
  allowlist 0 failures, six harness specs ALL PASS, drift 0/1724 (mpesa extra only). Pushed;
  main == origin/main == 797a847c. Rule 9d ledger PASS (42 hash-verified). b1/b2/b3 fully
  contained in b4 (git branch --merged main confirms all four + pilot) — deletable.

## 13. [QUEUED] walkInSales dead-code cleanup — approved in principle, own branch, NOT tonight

- Owner: "The dead-code cleanup (BatchChangeOld and the 4716 no-op) is approved in principle as its
  own branch, but not tonight."
- Scope (from the Rule 10 trace): delete the never-invoked BatchChangeOld function
  (walkInSalesController.js:2524-2589) and the no-op `if(initialStore==JSON.parse(...)) { }` at
  :4716 (the disabled store-change-confirmation remains; the commented block 2506-2522 stays as
  history or is cleaned with it — supervisor's choice at execution time, zero-risk branch).

## 14. [HOLD] Remaining store-accessor campaign families wait for the owner's go

- procurement ~21, asset ~10, nursing ~4, pharmacy remainder ~6, dashboards ~7, masters ~1.
  Do NOT start until the owner says go. The enumeration tooling (classify + fire-test +
  verify_store_accessor_campaign.py) and the pattern are proven and ready.

## 10. [INFO] walkInSales (merged pilot) still carries 2 gated raw parses

- walkInSalesController.js:2527 (`initialStore=JSON.parse(...)` inside BatchChangeOld, AFTER a
  storeReady() gate) and :4716 (`if(initialStore==JSON.parse(...))` — a comparison parse). Both
  are SAFE (gated / comparison-only, no crash, no call) but not pattern-pure (the accessor owns
  the parse). Also preventiveMaintenance:26 is QF-AR-guarded (safe). Decision: convert on a later
  batch or leave as-is?
