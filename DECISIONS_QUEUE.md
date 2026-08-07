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

## 13. [RESOLVED 2026-08-07] walkInSales dead-code cleanup — EXECUTED on fix/walkinsales-deadcode-cleanup @ f3ddf304

- Owner: "The dead-code cleanup (BatchChangeOld and the 4716 no-op) is approved in principle as its
  own branch, but not tonight."
- EXECUTED per the extended-run queue: BatchChangeOld (2524-2589) DELETED (verified unreferenced —
  grep: declaration-only; outPatientSalesWorklistDetails has its OWN separate BatchChangeOld,
  sibling find, out of scope, untouched). 4716 empty-if no-op DELETED. walkInSales now 0 raw
  parses. initialStore (line 7) left as a declaration-only var (remaining refs are comments).
  node --check clean. STATIC-ONLY (server down) — screen-load fire-test queued.

## 15. [QUEUED] Extended-run complete: Step 6 priority = fire-test coverage for STATIC-ONLY batches

- Everything after the boot failure is STATIC-ONLY (syntax, parse elimination, gate placement,
  family shape proven; RUNTIME behavior NOT proven — no live fire-test). Abhay's Step 6 click-
  through / a future deploy run must cover these FIRST:
  - pr1  (fix/store-accessor-campaign-pr1, 97c3f42d): 8 screens (detailsPatientReturnAcceptanceMedication,
    assigningPickerDispenser, OPassigningPickerDispenser, walkinPrescription, acceptanceOfEmptyAmpoules,
    detailsNarcoticsIssueWorklist, deliveryWorklist, narcoticsIssueWorklist), 25 gates. Deploy attempted,
    boot STUCK, killed, never fire-tested.
  - ab3  (fix/store-accessor-campaign-ab3, 1a7a99e4): preventiveMaintenanceController, 2 gates.
  - n1   (fix/store-accessor-campaign-n1, 690652fb): 4 screens (consumableIndent,
    addNewComsumablePatientIssueReturn, addNewMedicationPatientIssueReturn, administrationWorklistMaster), 16 gates.
  - db1  (fix/store-accessor-campaign-db1, 7f648a9f): 5 screens (CEOdashbord, doctorDashboard,
    financerDashboard, nurseDashboardMaster, bedManagement), 0 gates — DIVERGENT SHAPE (see cycle 12).
  - m1   (fix/store-accessor-campaign-m1, b8e51c26): 2 screens (globalServiceMaster, otBillingCompletionDetails), 13 gates.
  - wd1  (fix/walkinsales-deadcode-cleanup, f3ddf304): walkInSales cleanup — screen-load check.
  = 20 screens + 1 cleanup across 6 branches. The 6 LIVE-verified branches (pb1-pb4, ab1, ab2)
  need only the owner's Rule 9b + routine Step 6.
- BOOT-FAILURE EVIDENCE (recorded per directive; NO reboot attempted — Abhay's call):
  - ab2 boot: "06-Aug-2026 23:25:13 ... Server startup in [971108] ms" — 16.2 min, the last SUCCESSFUL boot.
  - pr1 deploy boot: started ~00:15 BST 2026-08-07; never emitted a startup line; ran ~5.7h at
    ~500-640% CPU (PIDs 18414/18609 held :8080); killed 06:00 BST. catalina.out's last line during
    the stuck window: HikariPool DEBUG fills (pool at sufficient level) — the app-init grind.
  - Restore boot (main's frontend, startup ~05:58 BST): also failed to complete within ~40 min;
    the java process (19075) holds :8080; catalina still advancing slowly (HikariPool DEBUG) as of
    06:10 BST. Server remains UNAVAILABLE. NO port conflicts (each boot held :8080 exclusively;
    lsof showed one listener at a time). Machine has sustained ~20h of builds/deploys; a reboot is
    the queued remedy (owner's call — the session token and deployed state depend on it).
  - The healthcheck (curl :8080/healthcheck.html) is the live authority; catalina's "Server startup
    in" line lags behind the actual readiness (observed across b3/b4/pb4/ab1/ab2).

## 16. [QUEUED] Frontend drift tool committed — DEPLOY_PROCEDURE.md position + folding proposal

- tools/verify_frontend_drift.py committed on fix/frontend-drift-tool @ eaaa79a7. Replaces the
  ad-hoc `diff -rq` (which required /tmp/main-wt). Hash-compares deployed ROOT resources/assets
  vs a git-ref source tree (default = current branch HEAD). KNOWN_EXCEPTIONS = the pre-existing
  deployed-only mpesaApplicationConfigurationController (report-only). VERIFIED BOTH DIRECTIONS
  (clean PASS 3353 files; injected-drift FAIL).
- PROPOSED position in DEPLOY_PROCEDURE.md canonical order: after the post-deploy verify_* suite
  (rate-limit/regressions/allowlist), BEFORE the fire-test — the fire-test must run against a tree
  that is EXACTLY main + the batch's own build.
- Folding question: RECOMMEND KEEP SEPARATE. verify_deployed_regressions.py is HTTP-layer
  behaviour (its failure = an API 500); the drift tool is file-tree hash comparison (its failure =
  a stray file). Mixed failure semantics in one tool would obscure which layer broke. Run them
  sequentially in the canonical order instead.

## 17. [RESOLVED] Dashboard/masters family divergences — recorded in OVERNIGHT_CYCLE_12.md §8

- The dashboard family (5 screens) and globalServiceMaster (deref-only) diverged from the proven
  getStoreObj() pattern on evidence, not convenience. Full rationale in the cycle report for
  Abhay's judgment.

## 18. [RESOLVED 2026-08-07] Open-ended analysis items 1-3 COMPLETE (cycle 13) — nothing fixed

- ITEM 1 (census re-run, broader net): zero new application-code sites — the original census
  population was complete. The only 2 sites the original literal missed are STRING-READs inside
  the pattern-anchor's own getStoreObj/storeReady (app.js:2712/2727) — deliberate. Rule 10
  method validation done both directions (known positive matched; 26/27 merged files clean, the
  27th = the fire-test tool's own fixtures).
- ITEM 2 (dark-list designs): docs/DARK_LIST_TEST_DESIGNS.md — password-reset journey (needs an
  OTP capture seam + a full-journey driver + identity-binding regression coverage; none exist),
  feedback submissions (6-segment scoping contract undocumented; whitelist matrix lacks the
  feedback patterns), AI appointment endpoints (queue bookkeeping untested; boundary: local
  tests cover bookkeeping, not any external AI prediction). Designs only — build nothing.
- ITEM 3 (swallowed-exception sweep, comprehensive): 8,077 swallow-shape catches with GLOBAL
  included for the first time (EMPTY 456 / LOG-ONLY 6,581 / LOG+RETURN 1,032 / PRINTSTACKTRACE
  8; Global 7,614, Billing 385, HIS-WAR 72, Entity 6). Top-10 worst = EMPTY catches in the
  Billing procedure DAOs (OT completion + procedure order surfaces — invisible failure on
  clinical/financial decisions). Method + blind spots + sample verification in
  SWALLOWED_EXCEPTION_SWEEP.md §5. NOTHING FIXED. Suggested fix batches for the owner: the
  procedure-DAO EMPTY cluster first, then decision-surface LOG-ONLY (trace-verified), then the
  success-looking LOG+RETURN subset. A committed sweep tool is worth adding once a fix batch
  is chosen.

## 19. [RESOLVED 2026-08-07] Cycle 14 — top-10 traces CORRECTED, observability quantified, OTP seam found

- TRACE CORRECTION (plainly): the sweep's top-10 claim ("empty/successful view") was WRONG
  for 9 of 10 catch lines — they are followed IN-METHOD by `return Response(ERROR, ...)`
  (observable generic envelopes; the cost is lost exception detail). The ONE genuinely
  invisible site: ProcedureOrderDaoImpl:177 getProcedureOrderListByStatus — EMPTY
  HibernateException catch falls through to `return null` -> HTTP 200 null body ->
  indistinguishable from "no procedure orders exist". Frontend residual (the 5 screens'
  handlers) is the remaining unknown — queued for the next run.
- OBSERVABILITY SPLIT (method + blind spots in SWALLOWED_EXCEPTION_SWEEP.md §7): of 8,077
  swallow-shape catches, 6,587 (81.6%) return ERROR-marked envelopes (observable; degraded);
  1,490 (18.4%) indistinguishable-or-unknown (DEFINITE-invisible ~640 / ~7.9%, UNKNOWN ~850).
  CONCLUSION: NOT a campaign — a targeted fix on ~640 (filtered by decision surface to
  tens) is the shape; per-site fix proposal with cost/risk in OVERNIGHT_CYCLE_14.md §2.
- OTP SEAM FOUND: forgotPasswordGeneration writes the OTP to public.m_user_master.otp (+
  otp_expiry_date/otp_count/blocked) in the same flow that mails it (UserMasterDaoImpl
  :337-349). The password-reset journey IS locally testable (DB read + mail-inert profile);
  the dark-list design's headline NEED is downgraded — journey priority UP. Recommend it
  becomes a near-term test (identity-binding + enumeration legs are the high-value asserts).
- Nothing fixed/built. Item 15 stays top for the morning.

## 20. [RESOLVED 2026-08-07] Cycle 15 — residual CLOSED (the nine are invisible one layer up), decision filter applied, OTHER sampled, reset spec written

- RESIDUAL CLOSED (overrides item 19's "degraded observability" framing): traced the shared
  services + 2 of the 5 screens (Rule 10, least-confident first). GenericService's GET path
  inspects ONLY HTTP-level errors — a 200+ERROR envelope passes UNINSPECTED; the traced
  handlers render empty on it (otCompletionController null-checks listObject ->
  tabs render false/empty; masterDischargeSummary status-gates with no else -> list
  empty/stale). The save-path service CAN growl on envelopes but the traced save call
  passes notificationStatus=false. ANSWER: the nine are INVISIBLE at the UI — nine more
  instances of the class one layer up, NOT fixed-by-convention. The fix shape must include
  the frontend; highest-leverage = the shared GET serviceAction inspects
  response.data.status and growls (one change, every GenericService GET screen).
- DECISION FILTER (sampling method stated in cycle 15 §2 — file-area, not caller-chain):
  of 640 definite-invisible, 188 (29%) are decision-surface (OPD 80 / billing 35 / doctor
  21 / procedure 11 / otims 11 / pharmacy 11 / admission 8 / nursing 5 / referral 3 / lab
  2 / appointment 1). Worst: ProcedureOrderDaoImpl:177 (grounded), ProcedureOrderServiceImpl
  getBatchList:4443, TIndividualDoctorShareGroupWiseDaoImpl checkUniqueIndividualGroup
  (:351) + checkUniqueIndividualGroupForReferral (:431), DoctorSlabMasterController:31-67,
  UnitServiceTariffMasterController:91, BillingMasterDaoImpl getNextValGetBillId:1057 +
  getTallyInterfaceDataInJsonFormat:4232. Order of magnitude: ~190 (of 8,077 = ~2%).
- OTHER BUCKET (~850): unknown because the post-catch return is a plain value/var (0, "OK",
  dto, response) — not statically marked. Sampled first 15 in file order: ~13/15
  INVISIBLE-class (e.g. BillingMasterController.myfileSynch returns "OK" on failure —
  FALSE SUCCESS; updateFinalBill returns 0 — indistinguishable from 0 rows); ~2 ambiguous.
  Extrapolation: invisible population ≈ 640 + ~0.85x850 ≈ 1,350 (~17%); decision-surface
  subset ≈ 400. Full pass = one read per site, not a dataflow problem.
- PASSWORD-RESET SPEC written (cycle 15 §4, spec only): tools/golden-trace/
  test_password_reset_journey.js; fixtures = seeded user + psql read of m_user_master.otp +
  mail-inert profile + token; drives forgotPasswordGeneration -> otpVerification (wrong/
  right/single-use) -> resetPassword (incl. the unverified-identity NEGATIVE = the identity-
  binding regression) -> enumeration leg -> login with new password. NOT BUILT.
- Nothing fixed/built. Item 15 stays top for the morning.

## 21. [RESOLVED 2026-08-07] Cycle 16 — GenericService scoped, OTHER bucket finished, MORNING_BRIEFING.md written

- GENERICSERVICE SCOPED (decision-ready; cycle 16 §1): 14,554 serviceAction call sites, 2,859
  GET across 906 files. Three handler classes traced (Rule 10): null-check render
  (otCompletion — blank on ERROR), status-gate-no-else (masterDischargeSummary — blank on
  ERROR), ERROR-AS-NORMAL-PATH (walkInSales:1179 — "no active package" is a normal outcome;
  surfacing = FALSE ALARM). 2,373 handlers already consume status=='error' — a global growl
  double-surfaces on all of them. RECOMMENDATION: opt-in per call site (flag mirroring the
  save path's notificationStatus, default FALSE), decision-surface screens opt in — NOT a
  global growl.
- OTHER BUCKET FULL PASS (850): FALSE_SUCCESS 29 (11 decision-surface; myfileSynch "OK",
  updateFinalBill 0, JwtTokenFilter true, AppointmentSchedulingDaoImpl 0s...), OBSERVABLE 0,
  INVISIBLE 128 (40 decision), AMBIGUOUS 693 (202 decision — method-call returns, one read
  each). FINAL INVISIBLE POPULATION 797 (239 decision-surface); targeted-fix scope =
  decision-surface 239-441.
- MORNING PACKAGE: docs/MORNING_BRIEFING.md — decisions 1-6 ordered by consequence
  (GenericService shape, machine reboot, campaign merges, swallow-fix shape, password-reset
  test, drift tool/dark-list), branches with STATIC-ONLY marking, Step 6 list, top-3 findings
  (GenericService gap first). ~10-minute read.
- Nothing fixed/built. Item 15 stays top.

## 22. [2026-08-07, EXECUTION IN PROGRESS] Rule 9b approved — merges DONE local, deploy FAILED, reboot taken

- APPROVAL (verbatim, SESSION_LOG 07:24 BST): merge the live-verified store-accessor campaign
  batches into main, --no-ff, per Rule 9. STATIC-ONLY branches NOT included, await Step 6.
- EXECUTED: dependency structure confirmed (pb1-pb3 ⊆ pb4, ab1 ⊆ ab2 — two cumulative chains)
  → TWO tip merges: pb4 @ 40bc56f3 + ab2 @ c7c49b39 (--no-ff, Rule 9a flipped, docs conflicts
  resolved both-sides). Token independently verified.
- FAILED at the deploy boot (3rd stuck boot, same signature): 517% CPU, HikariPool-only
  catalina, no startup line by 44 min. Restored from backup, Tomcat stopped, nothing pushed.
- DIAGNOSIS (4-point): Postgres UP; 1/100 connections (pool NOT starved); HikariPool shapes =
  normal noise (fills skipped, stats active=0/waiting=0, zero timeouts); NO orphan java.
  Pool theory DISPROVEN → INCONCLUSIVE → REBOOT taken per the supervisor's instruction.
- PENDING after reboot: mint a fresh token FIRST (the token dies with the reboot; /tmp is
  wiped) → redeploy merged main → canonical proof chain (rate-limit FIRST on clean buckets,
  regressions, allowlist, six harness specs, drift, TOKEN-OK login) → push → main ==
  origin/main → 9d ledger. Then Step 6 fire-tests (item 15 stays top).

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

---

## 23. [2026-08-07 ~08:47 BST, OPEN — BLOCKER] Post-reboot boot FAILED-FAST — Postgres 14/16 port race (his_local offline)

- REBOOT DONE 08:34:16 BST; /tmp wiped (the pre-merge backup copy /tmp/ROOT-backup-pre-pb4ab2-merge
  is GONE; the same content survives as webapps/ROOT.pre-pb4ab2-merge — the deployed ROOT is
  byte-identical to it, so the restored pre-merge build is intact in webapps).
- BOOT RESULT: Tomcat auto-started by the brew LaunchAgent (PID 506, 08:35:10, PPID 1) on the
  restored pre-merge ROOT — but the boot FAILED FAST (NOT the stuck-boot class: 0.1% CPU,
  sleeping): spring-dispatcher init threw; chain revertAllocatedQuantity -> TimeZoneComponent.init
  -> "Could not open Hibernate Session... Could not open connection" -> PSQLException FATAL:
  database "his_local" does not exist (22x in localhost.2026-08-07.log). Every request on :8080
  now returns 500.
- ROOT CAUSE (verified read-only): TWO brew postgres services race for 5432 at login.
  postgresql@16 OWNS his_local (its cluster has a 4-DB set incl. a 5,850-file user database; last
  successful start Jul 15 — it ran continuously until the reboot killed it). postgresql@14 (EMPTY
  cluster — psql -l shows only postgres/template0/template1; its log's FIRST successful start ever
  was today 08:35:12 BST) WON the port; @16 failed to bind ("Address already in use" x5) and is
  DOWN.
- STUCK-BOOT CLASS: not reproduced today — the reboot-vs-orphans theory remains UNTESTED;
  today's failure is a distinct, fully-explained class (database absent).
- APP WILL NOT SELF-HEAL: the Spring context init already failed — even after the DB is restored,
  Tomcat must be restarted to re-attempt the boot.
- QUESTION (decision needed): authorize (a) `brew services stop postgresql@14` + (b) `brew services
  start postgresql@16` (restores his_local on 5432), then (c) restart Tomcat (kill PID 506 +
  startup.sh), confirm a normal boot on the restored ROOT — then the sequence resumes: fresh token
  (Abhay mints) -> FULL deploy of merged main -> canonical proof chain -> push -> 9d ledger.
  ALSO: why is postgresql@14 registered (LaunchAgent dated Aug 1)? If unused, keep it stopped so
  the race cannot recur on the next login/reboot.
- Nothing fixed, nothing merged, nothing pushed (limits held); the deploy sequence is stopped at
  step 1 per the failure rule.
- UPDATE 09:16 BST — option A EXECUTED; boot FAILED again, but the stuck-boot class is now
  MECHANICALLY EXPLAINED. Done: `brew services stop postgresql@14` (port freed), postgresql@16
  came up on 5432 (launchd retry) — his_local restored, psql -l confirms; killed the failed
  Tomcat (PID 506) -> launchd KeepAlive restarted it (PID 2074) on the restored ROOT. Boot
  ground through 13 of 23 webapps dirs (every ROOT.* backup = a FULL app copy, ~18-250s each,
  all deploying successfully against the live DB) then FAILED: `java.lang.OutOfMemoryError:
  Java heap space` (4x, localhost.2026-08-07.log:7094+) — jstat on PID 2074: old gen 99.72%,
  metaspace 99.91%, 327 full GCs / 342s GCT in ~10 min (GC death spiral — the historical
  "CPU-pinned, Hikari-only, no startup line" signature). ROOT's own context never started
  (instant abort, no log lines). Connections during boot: 74-76 (each copy's pools; max 100,
  no "too many clients") — pool/orphan theory stays DISPROVEN. THE CAUSE IS MEMORY: 19 app
  copies (18 ROOT.* backups + ROOT) in ONE JVM with no -Xmx flag. This also explains pr1's
  5.7h boot and the three 44-min stuck deploy boots (same spiral at smaller pile sizes).
  STOPPED per the failure rule: killed 2074, `brew services stop tomcat@9` (prevents the
  KeepAlive auto-restart spiral); 8080 free, no java. NOTHING restored (ROOT content is
  already the pre-merge build; the backups were NOT moved — no fixes).
- NEW DECISION NEEDED (the boot path cannot work without it): move the 18 ROOT.* backup dirs
  OUT of webapps/ (e.g., to a sibling backup dir) so Tomcat deploys ONLY the real ROOT, then
  `brew services start tomcat@9` -> expect a normal ~20-25s boot (the 08:39 ROOT deploy took
  16.8s under the pile). Optional: set -Xmx (e.g. -Xmx4g) in the launchd env/startup.sh as a
  second line of defense. ALSO decide: the tomcat LaunchAgent (KeepAlive=true) auto-starts
  Tomcat at EVERY login — with the pile present it re-enters the spiral; keep it registered
  or run Tomcat manually (the historical startup.sh pattern)? The merged-build deploy stays
  blocked until the boot path works. NO SECOND REBOOT (limit).
- RESOLVED 09:50 BST — A/B/C EXECUTED and VERIFIED. A: the 18 ROOT.* backup dirs moved to
  /opt/homebrew/Cellar/tomcat@9/9.0.120/libexec/webapps.archive-20260807/ (nothing deleted;
  webapps/ now holds only ROOT + docs/examples/host-manager/manager). B: libexec/bin/setenv.sh
  written with CATALINA_OPTS="-Xmx4g" (confirmed active: jcmd MaxHeapSize=4294967296). C: the
  tomcat LaunchAgent KEPT (safe with the pile gone; removes the manual-start failure mode).
  BOOT: `brew services start tomcat@9` -> PID 3181 -> "Server startup in [16839] ms" (16.8s,
  the normal signal) -> healthcheck 200 in 0.11s -> 6 connections to his_local (one copy's
  pools; the pile-era math confirmed: 76/13 ≈ 6/copy). The stuck-boot class is CLOSED as a
  machine-config issue (heap exhaustion from the webapps pile — not the app, not the merges).
  NEXT: Abhay mints a fresh token -> FULL deploy of merged main (pb4+ab2) -> canonical proof
  chain -> push -> main == origin/main -> 9d ledger.
- UPDATE 10:45 BST — deploy EXECUTED; proof chain 4/8; LOGIN BLOCKER ROOT-CAUSED:
  JWT_SIGNING_KEY env var missing from post-reboot Tomcat processes. Full detail in
  QF-2026-08-07-E. Summary: merged build booted 18.8s (baseline), HC 200, 6 conns;
  verify_rate_limit 12/12 rate-limit assertions (0 HIS-400); regressions PASS; allowlist 0;
  DRIFT CLEAN (3,353 files, mpesa-only); loginController guard x1 + roleId x4 present.
  EVERY POST /adt/userLogin -> 401 EMPTY (pre-controller): ExceptionInInitializerError from
  JwtTokenUtil.<clinit> -> IllegalStateException "JWT_SIGNING_KEY environment variable must be
  set" (catalina.out; UserMasterServiceImpl.userLogin:247 generateToken). This is the
  DECISION_NEEDED_HardcodedJwtSigningKey.md remediation (code REQUIRES the env var). The
  pre-reboot process was startup.sh from a shell that exported it; post-reboot launchd
  processes have no EnvironmentVariables -> missing -> login 401s. The value is NOWHERE on
  disk (no profiles/scripts/.env) — Abhay holds the only copy. NOT a deploy regression:
  every login-path class is byte-identical pre-merge vs merged (verified by cmp). Also:
  /tmp/fresh_token.txt does NOT exist (reboot wiped it — the 07:28 token died with the
  reboot); tooling's hardcoded admin/12345 != this DB's stored hash (encrypt('12345')=
  5ebTyzUzig57g5D4OK409A== vs stored RGcKiWDPcb7Giyfymr6FSA==, which also doesn't decrypt
  with the current key); admin is OTP-blocked (otp_count=5, blocked='t'); an active
  login-log entry from the 07:28 mint exists (next real login hits the already-logged-in
  dialog — tooling handles it). DECISION NEEDED: (1) supply JWT_SIGNING_KEY (or authorize a
  fresh openssl rand value) -> add to libexec/bin/setenv.sh (works for launchd AND
  startup.sh) -> restart Tomcat -> login recovers; (2) then the real admin password for the
  verification tooling (or a DB-side reset — owner's call); (3) then owner mints the token
  -> six harness + permissions bootstrap + TOKEN-OK -> push -> 9d.
- UPDATE 11:30 BST — RESOLVED (owner authorized option b): JWT_SIGNING_KEY generated and
  wired into setenv.sh alongside the heap cap; login FIXED and proven (200 / TOKEN-OK /
  verifier 10/10). OTP-block + password-hash investigations complete (report-only, nothing
  unblocked/reset). Remaining: owner mints a fresh token -> six harness + permissions
  bootstrap -> push -> main == origin/main -> 9d. Full detail in the 18V session-log entry.
