# DECISIONS_QUEUE — owner review items queued during the campaign cycles

Each entry: what was found, why it needs a decision (not an autonomous fix), the
options, and the recommendation. Nothing here is acted on without the owner.

---

## DQ-2026-08-08-001 — Campaign 1 (drift): 35 phantom TABLES need per-case classification

Found: 35 entity-mapped tables are absent from BOTH the live DB and the
reference DDL, and are referenced by at least one query (DAO raw SQL or entity
named query). Every reference is a runtime failure today (relation does not
exist). Examples:
  - billing.t_cc_avenue_log — UPDATE path in MpesaMasterDaoImpl (CCAvenue is a
    LIVE partner config — BR-012 retracted — this path may be in use)
  - m_razorpay_order_log_details (schema-less @Table) — UPDATE in
    MpesaMasterDaoImpl (razorpay)
  - procurement.t_grn_bonus_batch_expiry — DELETE in GrnBonusBatchExpiryDao
  - opd.ai_appointment_queue — SELECT count in AiAppointmentQueueDaoImpl
  - inventory.t_returnable_bonus_batch_expiry — DELETE
  - emr.t_discharge_sum_tab_columns_print_mapper — UPDATE (ADT discharge)
  Module spread: public 12, billing 4, abdm 3, adt 3, doctor 2, emr 2, opd 2,
  procurement 2, service 2, inventory 1, patient 1, schema-less 1.

Why a decision: there is NO entity-side fix that preserves behavior. Options per
table: (a) the DAO path is dead code -> remove entity + DAO method + query
(BR-183 pattern, safe); (b) the table SHOULD exist -> schema change (NOT
permitted this campaign) -> the defect is real and the endpoint is broken for
all callers; (c) the query is dead -> remove the query only. Each of the 35
needs the caller trace (who calls the DAO method, is it reachable).

Recommendation: batch the caller trace for all 35, split dead vs should-exist,
owner decides the should-exist set (schema add is a Phase-2/3 question).

## DQ-2026-08-08-002 — Campaign 1 (drift): 60 typo-class phantom columns

Found: 60 @Column names across 15 tables carry a trailing-space/typo so the
mapped name ("patient_id ", "code ", "amount ") does not exist in live/DDL,
while the stripped name does. All 60 are query-referenced. Fix = correct the
@Column name — BUT the corrected mapping changes behavior: any Hibernate
WRITE to the field currently fails (column "patient_id " does not exist) and
would start succeeding. That is a behavioral change -> BR entry per item (or
one BR for the class) + owner sign-off, per the constitution.

Recommendation: one BR for the class (60 items, same pattern), verification =
the write endpoints execute for legitimate callers pre/post.

## DQ-2026-08-08-003 — Campaign 1 (drift): 240 true phantom columns fix plan

The 240 query-referenced phantom columns (BR-183 class) are the mechanical
fixable set: remove the phantom field + @Column + every query reference,
entity-side only. Each needs the BR-183-style Rule-10 trace (field -> all
references -> removal). NOT a decision item — executing per the mandate — listed
here for the count.

## DQ-2026-08-08-004 — Batch 28 gating: the six batch services carry 62/73
unswept org-trust methods (Campaign 2 scope). Not a decision — execution order
per the mandate.

## DQ-2026-08-08-003 — Batch-1 SILENT SAVE (owner-raised, 3rd gate item): batch 1 replaced a loud failure with silent data loss on the mailConfiguration save
- FINDING (confirmed): post-batch-1, toggling is_sms_send/is_whatsapp_send/critical_report/user_defined_mail/whatsapp_template_master_id and saving returns SUCCESS + "Saved Successfully" while the values are silently dropped (the columns exist in NO schema; the setters were removed in batch 1). Pre-fix the same save returned ERROR (the dup-check query failed) — so batch 1 flipped a loud failure into silent acceptance on a messaging-config screen. The codebase's worst documented pattern (mlc_no class), introduced by the fix itself.
- EVIDENCE the table never held a row: public.m_sms_utility_cofiguration = 0 rows; m_sms_utility_cofiguration_seq last_value=1, is_called=false — the sequence has NEVER been called → no INSERT was ever attempted by any path → the save path has never persisted, in any environment running this schema. Read paths consuming GET_SMS_UTILITY_CONFIG_MASTER_LIST: SmsUtilityConfigDaoImpl:51 (saveSmsUtilityConfig dup-check) + :179 (getSmsUtilityConfigDetails list) — both were broken pre-fix, both execute post-fix.
- UI CONTROLS bound to the 5 phantom values: legacy sms-utility-configuration.html:59 (isSMSSend checkbox), :70 (isWhatsappSend checkbox), :77-81 (whatsAppTemplateMasterId select, conditional on isWhatsappSend=='Y' && whatsAppOption==4), :336-337 (grid cells); mail-utility-configuration.html:73 (isWhatsappSend checkbox), :79-87 (whatsAppTemplateMasterId select), :99 (criticalReport checkbox — name attribute copy-pasted as isWhatsappSend), :351 (grid cell); controllers smsUtilityConfigurationController.js:88-89,322-325,458-465 + mailUtilityConfigurationController.js:88; new/ Angular sms-configuration: models.ts:20-27 + html:34-61 (isSMSSend + isWhatsappSend checkboxes + whatsAppTemplateMasterId select).
- OPTIONS (owner decision): (a) server-side guard rejecting non-'N'/non-null phantom payload values with an explicit error envelope (legacy-only, deploy-only, smallest, mandatory layer); (b) remove the dead controls from the legacy templates + new/ screen (legacy = deploy-only; new/ = needs the owner's ng build); (c) schema add for the 5 columns (Phase-2/3 gate — out of this campaign). RECOMMENDATION: (a) now + (b) legacy with the same deploy, (b)-new/ flagged for the next new/ build.

## DQ-2026-08-08-002 — (updated) deployed-vs-source hibernate config: the SOURCE cfg is stale, not the deployed WAR
- RESOLVED portion: the deployed ROOT (tomcat@9 webapps/ROOT/WEB-INF) runs WEB-INF/classes/hibernate.cfg.xml whose mapping set is IDENTICAL to the source cfg, and its WEB-INF/lib/Global-0.0.1-SNAPSHOT.jar STILL CONTAINS com.param.global.model.UnitSubscriptionDetails.class (verified by unzip). The app boots because the deployed jar predates the class's removal from the source.
- OPEN: the SOURCE modules no longer contain the class (only UnitSubscriptionDetailsDto) while the source hibernate.cfg.xml still maps it → the NEXT WAR build's SessionFactory will fail to boot (MappingException). Fix when the build next happens: remove the mapping entry from the source cfg (or restore the class). Logged, not chased.

## DQ-2026-08-08-004 — the SMS/WhatsApp channel configuration is a never-persisted feature (screen-level functional gap; the guard cannot cover it)
- EMPIRICAL (real-browser capture, 2026-08-08): the sms-utility-configuration screen's save validation AUTO-DEFAULTS isSmsSend='Y' when both channels are 'N' (smsUtilityConfigurationController.js:244-246 — verified in the live scope: isSMSSend="Y" after a save attempt on a fully untouched form) and REQUIRES a whatsapp template id when the whatsapp channel is on. So a valid save ALWAYS carries isSMSSend='Y' and/or whatsAppTemplateMasterId — the channel values are mandatory, not optional. The channel columns exist in NO schema — every valid save silently drops the channel configuration (the mlc_no class, at screen scale).
- The guard (dad44354) rejected those mandatory values → the screen became unusable (false positive) → CORRECTED (2026-08-08): the guard now covers only the NON-mandatory phantoms (critical_report, user_defined_mail, type); the channel-family values (is_sms_send, is_whatsapp_send, whatsapp_template_master_id) are exempt + their silent drop is THIS item.
- OPTIONS (owner decision): (a) schema add for the 6 channel columns (Phase-2/3 gate — the real fix; the screen becomes functional); (b) remove the screen + its routes/JS (the feature never worked — dead feature removal); (c) accept the silent drop (NOT recommended — the codebase's worst pattern, at screen scale).
- RECOMMENDATION: (a) — the columns are in the reference DDL (type IS in the DDL per the reference schema... verified: the m_sms_utility_cofiguration DDL block has 14 columns — the 6 phantoms are absent from the DDL too — so the schema add would be a NEW design, not a DDL restoration — owner decides the column definitions).
- UPDATE (2026-08-08, after the second guard correction): the mail screen's `type` select (mail-utility-configuration.html:448 — Daily/Monthly/Weekly/Yearly, required) is ALSO a screen-required phantom — every valid mail save carries a non-empty type → the guard's type condition false-positived too → the type joins the channel-family exemption. The guard's final scope: critical_report + user_defined_mail only (the genuinely-optional phantoms). The type control is included in the removal scope (item (b) below: 7 controls across the screens).
- OWNER WITHDRAWAL + FINAL STATE (2026-08-08): the control-removal option is WITHDRAWN — scoping showed removal leaves a shell (the sms/mail config screens exist to configure the send channels; removing the channel controls + type + recipients leaves only trigger/category/template fields — the screens' core purpose is gone; the removal decision would effectively decommission the screens). LIVE OPTIONS (Phase-2/3 scope, NOT campaign 1 — do not draft a BR, do not implement):
  (a) SCHEMA-ADD — create the 6 columns (is_sms_send, is_whatsapp_send, whatsapp_template_master_id, user_defined_mail, critical_report, type) on public.m_sms_utility_cofiguration as a NEW design (the reference DDL has none of them — verified: the DDL block has 14 columns, all 6 absent) — the screens become functional; requires the Phase-2/3 schema gate.
  (b) SCREEN-DECOMMISSION — remove the sms/mail utility configuration screens (routes, controllers, menus, the new/ rebuild screen) — the feature has never persisted (his_local: 0 rows ever, sequence never called — DQ-2026-08-08-003 evidence).
  EVIDENCE (from the campaign's capture record): a valid save ALWAYS carries isSMSSend='Y' (auto-set, smsUtilityConfigurationController.js:244-246), and/or isWhatsappSend='Y' + whatsAppTemplateMasterId (when the whatsapp channel is on), type (required select, mail-utility-configuration.html:448), and userDefinedMail (comma-joined recipient list — any recipient makes it non-empty, :294-303 -> :322 sms / :230 mail). ALL are mandatory-on-valid-save phantoms with NO backing column — every valid save of these screens has silently dropped the core configuration in his_local, and the guard (final shape: critical_report alone) cannot cover them without blocking the screens.
  TRACK-A NOTE (rebuild-path item, not just backend): new/src/app/features/sms-configuration/ (the rebuilt screen — models.ts :20-27,:57 + html :34-56) is a REBUILT screen whose core function cannot persist — the rebuild inherits the never-persisted feature; the DQ resolution gates the rebuild path for this screen too. Flagged for Track A's screen inventory (ANGULAR_PHASE0_SCREEN_INVENTORY.md) at rebuild time.
  NOTE (queue hygiene): this entry collides with the pre-existing DQ-2026-08-08-004 at line 56 ("Batch 28 gating") — the numbering was reused across campaigns; renumbering deferred (the ledger is append-only this cycle).

## Autonomous-run queue (carried every cycle — do not let these be absorbed)
- **Q1 (TOP — blocks the proof chain):** FULL redeploy from merged main + owner click-through. OWNER-BLOCKED (browser login + token). The redeploy itself was executed (2026-08-08, cycle-2 of the prior run — boot 17.9s, all token-free checks PASS) BUT the two click-through checks are the owner's: (a) mailConfiguration save with criticalReport ticked must produce a growl ERROR, not a success toast (the final guard's rejection path); (b) the serviceMaster screen must load with populated dropdowns. The six verify_master_screen harnesses also need a fresh token (the established remint flow prints TOKEN-OK but does not expose the token value — a flow change would be needed, owner's call).
- **Q2** DQ-2026-08-08-004: sms/mail channel gap — schema-add vs screen decommission. Control removal WITHDRAWN (leaves a shell). Phase-2/3.
- **Q3** DQ-001: 35 phantom tables, per-case classification (CCAvenue pulled out — Q4).
- **Q4** BR-184 CCAvenue: schema-add vs path removal (DQ-2026-08-08-001).
- **Q5** module-1071 role (unblocks the held m1 branch).
- **Q6** MRD 1068-1070 privilege data gap.
- **Q7** 134 of 181 BR lines have no parseable hash — the ledger verifies ~a quarter of itself.
- **Q8** ledger script wording (QF-2026-08-08-M — "flipped before the merge landed" reads misleading in the post-merge direction).
- **Q9** t_message_queue @Scheduled task fires against a phantom table (DQ-001 class).
- **Q10** deriver extension thresholds (after the back-test — the owner's call; scope fixed at 40+220).
- **Q11** serviceMaster open divergences: the save two-step confirm popup + the ledger toString formatting (flagged in 288d0bb1, not in QA_FINDINGS — BR-186 carries them).
- **Q10 (updated) — DERIVER FIX DECISION (owner, queued 2026-08-08):** the diagnosis pinned THREE fixable deriver defects + one inherent asymmetry: (1) the E1 tokenizer's missing-semicolon crossings (1,521 ASI sites; 117+85 of the 347 misses); (2) the E2 matcher's double-slash join (the mpath's leading slash — the 582 'broken' + the 0.996 + the 215 disagreement bases — ONE bug); (3) the shared-controller fan-out (23 DISPATCHER misses); (4) NOT FIXABLE: the 116 rebuild-new misses (the rebuild calls endpoints the legacy never had — the E1-from-legacy design cannot see them; the back-test comparison is inherently asymmetric). The E1 error rate 0.859 is NOT lowered by the stale-legacy class (0 found); after fixes 1-3 the rate would recompute. Owner decides: fix the deriver (the three classes), or accept the E1/E2 as aggregate-only inventory, or stop phase 0. Also: E2 error rate on the truth = 0.996 pre-fix (the join bug dominates).
- **Q10 (updated 2026-08-08, 2nd pass):** the deriver's CONTINUATION DECISION — the owner: "both fixes demonstrated at unit level — accepted. The map is not." The bounded check: ASI's 202 = 38 true + 164 constants-file class (the owner's hypothesis #3 — my earlier 'handled' retracted); the restricted delta is not interpretable (the sets moved); the intersection's phantom flags = the entity-name artifacts (insuranceMaster -> t_insurance exists live; no independent defect found); E3-E5 (18/254) cannot answer phase 3/4. The decision on whether the map continues is the owner's, depending on the bounded-check + the E3-E5 answer. NO further deriver fixes pending the decision.

## Queue state — 2026-08-08, deriver parked (the re-list)
- Q1 (TOP, unchanged): FULL redeploy + owner click-through — the redeploy RAN (boot 17,898 ms) but the two checks are outstanding: mailConfiguration save with criticalReport ticked must produce a growl ERROR not a success toast; serviceMaster must load with populated dropdowns. The six verify_master_screen harnesses also need a token.
- Q2 DQ-2026-08-08-004: sms/mail channel gap — schema-add vs screen decommission (control removal WITHDRAWN). OPEN.
- Q3 DQ-001: 35 phantom tables, per-case classification. OPEN (held).
- Q4 BR-184 CCAvenue: schema-add vs path removal. OPEN.
- Q5 module-1071 role (unblocks the held m1 branch). OPEN.
- Q6 MRD 1068-1070 privilege data gap. OPEN.
- Q7 134 of 181 BR lines have no parseable hash. OPEN.
- Q8 ledger script wording (QF-2026-08-08-M). OPEN (no script edits without say-so).
- Q9 t_message_queue @Scheduled against a phantom table. OPEN.
- Q10 DERIVER CONTINUATION — DECIDED: PARKED (2026-08-08). The constants bounded count: a lookup would resolve 103/164 (63%) — the majority but with a substantial residual (25 not-defined + 36 further-concat); the owner decides whether the single narrow constants lookup is ever worth one attempt, or runtime capture replaces static derivation. CLOSED as decided, sub-decision pending.
- Q11 serviceMaster open divergences (save two-step confirm popup + ledger toString). OPEN (BR-186 carries them).
- Q12 NEW: the constants-class decision — 103/164 resolvable via a config lookup; the residual 61 resist; the owner's call (narrow fix vs runtime capture). OPEN.
- Q13 NEW: the campaign-1 exit criterion's precondition FAILED (E5 = 18 tables; phases 3/4 unanswerable from the table) — the exit condition is an OPEN OWNER DECISION; no replacement criterion invented (RULES.md amendment 2026-08-08). OPEN.
- Q14 NEW: the runtime-capture option — 40-screen browser network-log capture (owner time, not agent) as the alternative to static derivation. OPEN.

## Q13 — Campaign 1 exit options (prepared 2026-08-08; NOT decided — the owner decides)

Context: the RULES.md campaign-1 exit criterion required phases 3/4 (shared reads, unscoped queries) to run as QUERIES against the deriver's join table. The deriver's E5 resolved only 18 tables — the criterion's precondition FAILED (recorded in the RULES.md amendment). The exit condition is now an open decision. Campaign 1's phases 1-2 (the DQ-003 phantom-column entity-side batch + the silent-save guard) took roughly 2-3 working sessions (2026-08-07/08), including the guard's three empirical false-positive corrections.

OPTION (a) — run phases 3/4 as FRESH SWEEPS (not queries against a table)
- What gets covered: the remaining phantom-column classes (the entity-vs-schema raw count is 601, DDL-filtered ~234 true + 60 typo-class, batch 1 removed 6), the unscoped/shared-query audit, and the swallowed-exception surface (4,928 silent/empty catch blocks, phase 5's standing backlog).
- What gets left open: screen-level runtime behavior (what a user sees when a shared/unscoped query is hit) — no map, no pre-rebuild lookahead.
- What it costs: the sweep tooling already exists (the drift checks, the query audits, the catch-scan) — an estimated 1-2 sessions to re-run + classify, plus the owner's review of each finding class. No deriver work.

OPTION (b) — declare campaign 1 complete at phases 1-2; queue the remaining findings; return to Track A
- What gets covered: nothing new — phases 1-2's outcomes stand (batch 1's 6 columns removed, the guard live, DQ-003 partially discharged, the ledger + BR records in place).
- What gets left open: everything phases 3/4 would have surfaced — the ~234 remaining phantom columns, the unscoped/shared-query classes, the swallowed-exception backlog — queued as findings (DQ-001 holds the phantom-table class; the column class would need its own DQ), with the runtime risk that a rebuilt screen hits an unscoped query or a phantom column during Track A and the discovery happens post-rebuild instead of pre-rebuild (the sms-configuration case, found by accident on cycle six).
- What it costs: zero sessions now; the deferred cost is whatever the post-rebuild discoveries cost later.

The two options trade the same asset: pre-rebuild visibility of the query landscape versus immediate return to Track A. The owner decides.

- **Q15 NEW (add-new-drug-recall finding, RETRACTED 2026-08-09):** the original entry claimed the legacy screen's `inventoryApi + '/stores/recall/drug/search'` (DRUG_RECALL.RECALL_DRUG) "has NO backend controller" and recommended replacing it with the pharmacy issue-search. THAT PREMISE WAS WRONG (QF-2026-08-09-E): RecallDrugController EXISTS — the path is assembled from fragments (`/api/inventory/stores/recall/drug` on the class + `/search` on the method), so a literal grep misses it; POST /api/inventory/stores/recall/drug/search returns HTTP 200 live (its query lists existing DrugRecall records by date; [] in dev = no recall records). The replacement was based on the false premise and produced QF-2026-08-09-D (the searchString 400), fixed by Q23 (the modal now searches the item master + the DTO-shaped patients lookup). The RECALL_DRUG endpoint itself is a working recall-list search. OPEN (superseded by Q23/Q24).

- Q16 NEW (2026-08-08, module-id resolution): the implant-opening-balance screens' module id is UNDETERMINED. Sources: the Admin privilege tree (privilege.privilege modules JSON) has NO "Implant Opening Balance" leaf (the closest is "154 Opening balance" under Stock(153) -> Inventory(121) — the general opening-balance module, not a confirmed match); the legacy app.js has the `addNewImplantOpeningBalance`/`detailsImplantOpeningBalance` states COMMENTED OUT (the legacy routes disabled). The route placeholders stay 220 until the owner decides (seed/registration — OWNER-BLOCKED, not seeded). The add-new-drug-recall module IS resolved: 139 (the Drug Recall leaf under Store/Inventory; the 220 assumption retracted; the route corrected on the screen's branch; the Admin role's permission row confirmed).

- Q16 STATUS (2026-08-08, the owner-directed classification — the implant pair): per-source evidence — (a) legacy registration: both states are inside a COMMENTED block in app.js (no route registration; no module field anywhere; the menu tree has no "Implant Opening Balance" item — 0 nodes for "implant opening"). (b) psql module registry (privilege.privilege.modules JSON, role_id=1): by name — "implant opening" 0, "implant" 4 (none the opening balance), "opening balance" 3 (219 Fixed Asset / 154 general " Opening balance" under Stock(153) / 597 Cash — none implant-specific); by route — the registry has no route field, so a by-route search is impossible. NO row exists. (c) the shared controller has NO module gate (the only "module" hit is the Angular declaration; the cookie reads only); the Screen.java's OpeningBalanceImplant(137) screen-constant's tree ids 137 are unrelated nodes (Transction Ciode wise report / Batch Exp Date Change). CLASSIFICATION: (ii)+(iii) combined — no module row in dev (seed decision, OWNER-BLOCKED, not seeded) AND the legacy screens have no module gate at all (dead: commented routes, no menu item, no controller check) — the rebuild's routes may not need the moduleAccessGuard. NOT decided; the guards stay as-is (the 220 placeholders). Owner's options queued: no guard / new seeded module / the general 154.

- Q17 NEW (2026-08-08, dead-screen findings): the reachability gate run against the next-5 contracts — DEAD screens (no state registration in app.js AND no menu node): addNewMedicationPatientIssueReturn, administrationApproval, detailsAdministrationApproval. deliveryWorklist: menu node EXISTS (117, "Delivery Worklist") but the state is NOT registered in app.js — menu'd-but-unrouted (the legacy click falls through to /login per the QF-AH pattern) — also unreachable, a distinct dead shape. combinationMedicationMaster: state REGISTERED — the only live one of the five (menu node + module id to be confirmed at extraction). The gate is now a standing rule in RULES.md (step 1b). Screen 4's previous candidate (addNewMedicationPatientIssueReturn) is DEAD — the next live screen must be re-selected from the path under the gate.
- Q16 STATUS (2026-08-08, the dead-implant classification): the implant pair's routes were commented BEFORE this repo's baseline (git blame attributes the commented block to ^96b75c49, the baseline commit — the commenting predates the repo and cannot be attributed); the backend is FULLY LIVE but empty (OpeningBalanceImplantController's real handlers; t_opening_balance_implant + t_opening_balance_implant_detail exist in the inventory schema, 0 rows); no BR/QA/manifest entry references the feature. The evidence does not distinguish deliberately-disabled from never-finished — the lean is "staged then gated off before the baseline, no recorded decision". No option picked.

- Q18 NEW (2026-08-08, the gate's bulk run): the reachability gate over the whole remaining path (1,564 real screens): LIVE 693 / DEAD-unrouted 160 / MENU'D-UNROUTED 316 / registered-unmenued 395. Remaining after the 223 rebuilt: 1,341 (matches the ~1,340 estimate) — corrected Track A denominator: ~560 LIVE remaining (upper bound — the LIVE-side carries the token-matching noise the negative control exposed; the rebuilt-sanity's 38% non-LIVE also flags it). The MENU'D-UNROUTED class is logged as QF-2026-08-08-O (legacy user-facing: menu click -> /login). Screen-4 candidate under the gate: combinationMedicationMaster (menu 472 confirmed, module id 472) — LIVE. NOT WRITTEN — the cap holds.

- Q19 NEW (2026-08-09, QF-2026-08-09-A): the login-log active-row defect — saveUserLogDetails never closes the active row; every next login after a logout returns 200 "success" + "You are already logged in" with NO authToken (false-success in the login path). User impact: a phantom collision dialog after every logout; API/automation clients silently fail. Row lifecycle: closed only by the next login's own updateUserLog pass or session expiry. NOT FIXED — the get_dev_token.py psql UPDATE is a labeled stopgap (user_id=1 only). Owner decision: fix the backend logout to close the active row (logId-based updateUserLog) vs leave it.

- Q20 NEW (2026-08-09, the owner's note — LOGGED, NOT BUILT): preflight C4 could become a LIVE check — with the cached token available, verify each endpoint by CALLING it (a real response instead of a 404/405) rather than the static @RequestMapping grep. That is the whole point of removing the build/token/browser constraints. Candidate improvement only; do not build until the owner says so.

- Q21 NEW (2026-08-09, QF-2026-08-09-B): the smoke test seeds localStorage 'permissions' (the legacy login's full set incl. module 139) so the module-access guard ALWAYS passes — the smoke renders screens a real user could be denied. The harness passed while incapable (the owner's exact finding). Fix: stop seeding permissions; let ensurePermissions run the real /privilege/role bootstrap; assert the final URL is not /accessDenied and the route's moduleId is present; a denial or bootstrap failure must FAIL the smoke. The cache's permissions field exists only for the seed — revisit. OWNER-BLOCKED (diagnose-only order); not built.
- Q22 NEW (2026-08-09, QF-2026-08-09-C): the permissions bootstrap's idempotency guard (session.service.ts `if (this.permissionsLoaded()) return;`) freezes ANY non-empty store — a partial/broken permissions write denies every moduleId route FOREVER until site data is cleared. Demonstrated live: 8-module store -> reason=denied with NO refetch; cleared store -> 827 modules -> admitted. On main (byte-identical guard/session.service). NOT FIXED (Rule 10 — first-write origin unreproduced, persistence defect demonstrated). Fix options: re-fetch per session, validate store shape, or clear on login. The smoke fix (Q21) stays; the loop's unseeded run will FAIL until this is decided.

- Q21 RESOLVED (2026-08-09, QF-2026-08-09-B): the smoke harness is rebuilt and demonstrated. The rebuilt tools/smoke_screen.py (main 45cc6e58 + 9fd05877) NEVER seeds permissions — the app's own /privilege/role bootstrap decides; it FAILS on any /accessDenied redirect (reason reported), a missing/empty permissions store, the route's moduleId absent from the store, console/page errors, failed/non-2xx requests, and HTML-where-JSON responses; it ALWAYS prints the FINAL URL; authentication is stated plainly (the dev credential's real-login cookieObject cookie injected, sessionStorage left EMPTY — matching a real browser session; the login form is not used because the login-group rate limiter 429s on consecutive logins). FOUR CONTROLS DEMONSTRATED on the branch serve (4201, proxyConfig wired): (1) doctorSlabMaster (moduleId 1020 NOT in the 827) -> FAIL accessDenied reason=denied + moduleId 1020 NOT in the store; (2) serviceMaster with --role-id 2 (no privilege row -> the bootstrap writes an EMPTY store) -> FAIL permissionsUnavailable + store EMPTY; (3) /definitely-not-a-route -> FAIL (final URL not the route, empty body, console error); (4) serviceMaster -> PASS (has376=True). DRUG-RECALL RE-RUN with the corrected harness: FINAL-URL /addNewDrugRecall, bootstrap seen, store 827 has139=True, RESULT PASS — the harness and the owner's browser are in GENUINE DISAGREEMENT (owner: denied three times across a server restart, fresh compile, hard reload, cleared-then-rebootstrapped store; the harness: admitted with the real bootstrap and every FAIL condition active). REPORTED AS AN OPEN CONTRADICTION per the owner's instruction — NOT resolved by preferring either result. Environment note found during the work: the worktree had been left on MAIN by a verification script's cleanup `git checkout -q main` (the checkout-masking trap), so the branch serve compiled main's angular.json (no proxyConfig) and /privilege/role returned the SPA-fallback HTML — the harness correctly FAILED with permissionsUnavailable in that state, which is the harness working. The loop environment (branch worktree + proxied serve) is restored.

- Q23 NEW (2026-08-09, QF-2026-08-09-D): the Add-Item drug search on addNewDrugRecall 400s — the frontend payload {searchString,...} has no field in PatientDetailsRequestForDrugRecall (Jackson unknown-property -> HttpMessageNotReadableException -> HIS-400 -> "Item search failed" toast), and the wired endpoint (patientConsumableIssueSearchForDrugRecall -> BillListDao.searchBillsForDrugRecall) is a pharmacy SALES/bills search, not a drug-master name search. Mechanism named and demonstrated (live 400 capture + cached-token calls: frontend payload 400, DTO-shaped 200 []). NOT FIXED (Rule 10). Options for the owner: extend the DTO (query still bills), wire a real drug-master search endpoint, or scope the modal to the endpoint's actual data.

- Q24 NEW (2026-08-09, QF-2026-08-09-E): StoreStockController maps /item/batch/{storeId}/{itemId} AND /item/batch/{unitId}/{itemId} — every /item/batch/1/1 request throws "Ambiguous handler methods mapped" -> HTTP 500 (live-verified + catalina.out). Pre-existing (the legacy's identical call 500s, silently swallowed); the drug-recall rebuild's pick no longer calls it. FIX (backend, owner decides): rename or drop one of the duplicate mappings.
- C4-LIVE-CALL PROPOSED (2026-08-09, QF-2026-08-09-E): preflight C4 greps for @RequestMapping — the fragment-composed paths (RecallDrugController; 12 concat/constants values of 6,955) defeat it. Proposed: C4 calls each endpoint with the cached token and checks for a real response. NOT built (owner decision — the same candidate family as Q20).
- QF-2026-08-08-N RECORDED (2026-08-09): the parked deriver's E2 (static endpoint derivation) misses fragment-composed paths the same way a literal grep does — the false-negative class documented in QF-2026-08-09-E bounds E2's accuracy too.

- Q25 NEW (2026-08-09, QF-2026-08-09-F): the combinationMedicationMaster screen (module 472) has NO save endpoint — POST /api/globals/generics/ answers 405, no-slash 404; the Generic controller's POST is the generic master's save. The rebuild's Save reports "not wired" honestly. Options: extend the generic save to accept combinations, add a dedicated combination endpoint, or leave the screen read-only. Also queued from the same finding: the generics write path trusts the client org (no VerifiedIdentityService rebind — the BR-16x class) — for the org-trust sweep decision.
- Q26 NEW (2026-08-09, QF-2026-08-09-G): uomMaster (module 466) has NO write path — no save/update/status/delete endpoint for UomUnit exists anywhere on the screen's path (the GlobalController exposes only the units GET; the UomUnit DAO has no save/update). The rebuild's Save and Status toggle report it honestly. Options: add a UOM CRUD endpoint or leave the screen read-only. Also logged from the same screen: node 466's menu actionLink '#/unitofMeasurement' targets a route that does not exist (only uomMaster is registered) — a broken legacy menu link; and the screen-5 rejected candidates: genericStrengthMaster and strengthMaster — ABSENT from app.js (no state registration) — DEAD (the reachability gate's rejection side), logged not rebuilt.
- Q27 NEW (2026-08-09, QF-2026-08-09-H): allergyMaster's CRUD is broken by the phantom cims_code/cims_type columns (the named query selects them; emr.m_allergy_master lacks them — live SQLGrammarException, swallowed to the generic envelope). Options: add the cims columns to the DDL (schema change — owner's call) or drop them from the named query. The screen's rebuild is complete and wired; the smoke cannot pass until one of these lands.
- Q28 NEW (2026-08-09, QF-2026-08-09-I): write-verification discipline — the allergyTypeMaster save was verified end-to-end by a probe that wrote a row (cleaned up); future save/update/status success claims need the owner's sanction for a write test (the no-seed rule vs the honest-verification rule's tension, resolved by an owner-sanctioned write test).
- Q29 DECIDED (2026-08-09, QF-2026-08-09-J): the surgeryMaster save dup-check's named query GET_SURGERY_MASTER_BY_NAME is `unitId=:unitId AND organizationId=:orgId AND LOWER(surgeryName)=:name OR surgeryName=:name` — operator precedence binds org+unit ONLY to the LOWER branch, so the exact-match branch is UNCONDITIONAL (a same-name row from ANOTHER org satisfies the save's dup-check — blocks a legitimate cross-org save with ALREADY_EXIST and is an existence oracle across orgs). Same class as BR-169 (Discharge Type dup-check). Runs live on every save (SurgeryMasterServiceImpl.saveSurgeryMaster -> getSurgeryMasterByName). OWNER DECISION: backend queue, NOT fixed now — same fix as BR-169 (parenthesize the OR so both branches are org+unit-scoped), delivered as a backend BR with its own attack-then-defence verification, NOT as a side-effect of a screen rebuild. This is the SECOND confirmed instance of the operator-precedence class on a live save path (first: BR-169 Discharge Type). A THIRD confirmed instance makes this a campaign (a systematic sweep of dup-check named queries for the same shape) rather than a queue item.

- Q30 NEW (2026-08-09, QF-2026-08-09-K): the immunizationSchedule screen (module 483, screen 9) has NO backend — no ImmunizationSchedule controller/DAO/entity anywhere; no list/save/update/status/disease-list endpoint exists on its path. The legacy is a registered static shell (MasterController, zero bindings, zero API calls). The rebuild wires the only real feed (the vaccine catalog, GET /api/billing/getActiveVaccinationMaster — org+unit scoped, live-verified) to the Vaccine select and reports the missing paths honestly. Options: (a) add a real immunization-schedule backend (schema + CRUD — Phase-2/3 question), (b) leave the screen read-only, (c) decommission the screen (it has never had any working behaviour). RECOMMENDATION: (b) now — the screen is reachable but was never functional; a backend add is a Phase-2/3 scope decision.

- Q31 NEW (2026-08-09, QF-2026-08-09-L): the vaccinationMaster screen (module 482, screen 10) is the DECOY-ROUTED shape: node 482's menu reaches the 8427 SHELL (MasterController, zero bindings, zero API) while the REAL 21-field CRUD screen (vaccinationMAster, app.js:17050, dedicated controller) is ORPHANED — no privilege node carries its actionLink, so it is unreachable from the legacy app's own menu. The rebuild implements the shell (grid wired to the mapped list/count feeds; Diseases client-side; Save honest). Options: (a) add the real screen to the menu (a node/actionLink for vaccinationMAster) and rebuild THAT as the CRUD screen, (b) wire the shell's form up to the full 21-field contract and let IT be the CRUD screen, (c) leave as rebuilt (read-only shell + honest notices). RECOMMENDATION: (a) — the real screen already exists and is complete; the shell should not be force-fed a contract its form cannot carry.

- Q32 NEW (2026-08-10, QF-2026-08-10-A): the bloodComponent save dup-check is CROSS-ORG UNCONDITIONAL — GET_BLOOD_COMPONENT_MASTER_BY_NAME is `WHERE LOWER(bloodComponentDesc)=:bloodComponentDesc` with NO org/unit predicates (BY_NAME_NOT_ID adds only bloodComponentId !=). A same-name row in ANY org satisfies the dup-check: blocks a legitimate save (ALREADY_EXIST) and reveals name existence across orgs (existence oracle). The write itself is org-safe (VerifiedIdentityService override). FIX: add organizationId/unitId to both dup-check named queries (and to the DAO's setParameter calls). This is the THIRD instance of the dup-check-scoping family (BR-169, Q29, now this) — the owner's standing threshold is met; the owner's decision is recorded in the CAMPAIGN entry below (it becomes a backend campaign, queued after the current screen batch). ALSO covers the decoy-routed shape decision for screen 11 (give the real bloodComponentMaster screen a menu node/actionLink, or decommission the shell).

- CAMPAIGN — OR-PRECEDENCE SCOPING (owner decision 2026-08-10, AMENDED 2026-08-10): the scoping family becomes a CAMPAIGN — FOUR confirmed instances now across TWO path types: write dup-checks BR-169 (Discharge Type, OR-precedence), Q29 (surgeryMaster, OR-precedence), Q32 (bloodComponent, no-scope-at-all) and the READ-path variant QF-2026-08-10-C (nursingOrderMaster LIST_SERARCH, OR-precedence — a cross-org row leak, screen 12's finding). QUEUED as a BACKEND campaign to run AFTER the current screen batch — NOT now, NOT during screens. Bounds, as amended by the owner after the read-path variant was found:
  (a) ENUMERATE OR-precedence / missing-scope in ALL named queries and raw SQL carrying an org/unit predicate — not just save/update dup-checks. Report the count.
  (b) CLASSIFY each by PATH TYPE: write dup-check (the BR-169/Q29/Q32 class) vs read query (the QF-2026-08-10-C class). Within each, note the mechanism: OR-precedence unscoped vs no-scope-at-all.
  (c) For each unscoped instance, STATE whether the write path itself is org-safe (VerifiedIdentityService override, as in bloodComponent — the good pattern) or payload-trusting (as in vaccinationMaster). These need DIFFERENT fixes and carry DIFFERENT risk — the statement is mandatory per instance. For READ-path instances, STATE what the unscoped branch actually returns to a cross-org caller: rows (a data leak — the QF-2026-08-10-C shape) or just existence (an oracle).
  (d) DO NOT FIX any in the enumeration pass. Enumerate, classify, report. The fixes come as backend BRs with attack-then-defence verification, one batch at a time, after the owner sees the count.
  SEVERITY ORDERING (owner directive, 2026-08-10): the read-path variant is the MORE SERIOUS of the two path types — a blocked save (write dup-check) is a correctness bug; a cross-org row leak (read query) is a data breach. When the enumeration reports, read-path instances are reported first.
  Scope note: the enumeration covers every named query and raw SQL string with an org/unit predicate (save/update existence checks AND list/detail/search reads) — the OR-precedence and no-scope mechanisms in both.

- PARKED (owner directive 2026-08-10) — DELIBERATELY DISABLED FEATURES — re-enable or retire: KitTypeMaster joins the implant pair (Q16/Q17) as ONE item: "deliberately disabled features — re-enable or retire". KitTypeMaster's route is COMMENTED OUT in app.js (~15870, inside /* */, with a module-id-500 resolve gate inside the comment); its controller IS loaded (index.html:1377 kitTypeMasterController.js) and a real backend EXISTS (Pharmacy KitTypeMasterController, /api/units/kit/: POST /save, GET "", GET /{id}, PATCH /update/{id}; table public.m_kit_type). Re-enabling it is a PRODUCT decision about whether the feature should exist — not a migration decision. PARKED, not a rebuild candidate; the owner decides re-enable vs retire. (The implant pair Q16/Q17 — opening-balance screens with routes commented out, zero menu nodes — is the same class.)

- QUEUED (2026-08-10, screen 16 post-merge) — PROCEDURE-CONFIGURATION CREATE-FORM FOLLOW-UP: the screen-16 rebuild ships the list + status toggle only; the create/edit form is NOT wired because the legacy multi-step template collects procedure-type / anesthesia-type / speciality / service / operation-theatre feeds into the full ProcedureConfigurationDto (a partial DTO would be a probe-payload-class failure — the screen-15 lesson). Before building the create form: (1) verify each feed endpoint by calling (procedure types GET /api/procedure/types/{orgId}/{unitId}, anesthesia types GET /api/procedure/anesthesia/types/{orgId}/{unitId}, speciality + service feeds), (2) verify the save POST /api/procedure/config with a complete DTO, (3) check the save's org-trust (QF-2026-08-10-S — payload-carried org, source note). Then wire the create form.

- QUEUED (2026-08-10, screen 17 post-merge) — RESOURCE-CONFIGURATION CREATE-FORM FOLLOW-UP: the screen-17 rebuild ships the read-only list + honest phantom toast only; save/update/status are MAPPED but NOT wired (the 840-line legacy multi-step form collects speciality/payment-entitlement/tariff/visit-type/bill-of-material feeds into the full SurgeryResourceConfigDto — a partial DTO would be a probe-payload-class failure). Before building the create form: (1) resolve QF-2026-08-10-T (the phantom column blocks the list/byId — the create form's own byId-edit and re-list depend on it), (2) verify each feed endpoint by calling (GETACTIVESPECIALITY, GETACTIVEPAYMENTENTITLEMENT, tariff, visit-type, BillOfMaterial), (3) verify save/update/status POSTs with a complete DTO, (4) apply the org-trust fix (QF-2026-08-10-U) first so the write path is session-identity-scoped.

- RECORDED (2026-08-10, screen 17 post-merge) — 516 Subsequent Surgery Configuration walked past under the FIFTH bucket NOT-A-SCREEN-YET: state `subsequentSurgeryConfiguration` REGISTERED (app.js) + controller loaded (index.html) + menu node 516->514->369, but ZERO backend (no java class, no table — grepped). Per the RULES.md fifth bucket (750015b3): menu/state present but no backend at all -> do not build, log and walk on. The two rota siblings (409/410) are the first two instances (QF-2026-08-10-O); 516 is the third.

- CAMPAIGN SCOPE AMENDED (2026-08-10, owner directive — SUPERSEDES the dup-check enumeration in priority) — FALSE-SUCCESS WRITE CLASS: a save that lies about succeeding is data loss with a confirmation dialog (worse than a blocked save = correctness bug, or a cross-org read = leak). BR-187 is the anchor (operation-theatre save: SUCCESS return outside the result guard + DAO catch-and-return). Enumerated 2026-08-10 (enumerate/classify only, NO fixes): (1) CATCH-RETURNS-SUCCESS shape — 2 sites, BOTH in BillingMasterServiceImpl.collectPayment (lines 2388 + 2441: catch (Exception e) returns "Payment collected successfully !!!" — a swallowed payment reporting SUCCESS on the exception path; the strongest instance; billing write path). (2) SUCCESS-OUTSIDE-RESULT-GUARD shape (BR-187 mechanism) — 128 service methods flagged statically across 452 service files, 74 on write-verb paths (Global 42, Billing 25, ADT 5, OTIMS 2); 219 partial (guard absent or DAO not matched); hand-validated anchors: collectPayment, savePaymentReciept (PaymentRecieptMasterServiceImpl:69-71 — DAO result dereferenced with no null check, the same swallow-NPE chain). STATIC HEURISTIC — each flagged method needs confirm-before-fix (the tools record file:line). Fixes LATER as backend BRs; tooling: tools/enumerate_false_success.py + tools/enumerate_catch_success.py (committed to main). Billing/clinical/patient-record write paths first.

- QUEUED (2026-08-10, screen 19 post-merge) — OPERATION-THEATRE CREATE-FORM FOLLOW-UP, queued BEHIND the BR-187 backend fix: wiring the create/edit form (theatre-type + store feeds, save/update/status) requires the false-success write class FIXED server-side first — a bare form sending a partial DTO produces exactly the silent no-write false success (QF-2026-08-10-Z). Order: (1) BR-187 fixed (or the owner's decision), (2) verify the save with the full DTO post-fix (attack-then-defence per the BR spec), (3) verify the feed endpoints by calling, (4) apply the org-trust fix (QF-2026-08-10-AB) before wiring writes, (5) wire the create form.

- STALE-BRANCH TRIAGE REPORT (2026-08-10, owner requested; DELETE NOTHING — report only): (1) The five fix/* branches (auth-interceptor-missing-headers, input-output-charts-patient-search, mis-reports-unit-org-user-dropdowns, module-access-control-guard, status-toggle-css-and-mutation) — all cut from the SAME early phase-2 base (the 4 shared commits 22c72d12/2575d80a/0be2f084/9a83d2bb are NOT on main; each branch = the whole superseded phase-2 pilot tree, 57-59 files ~13,400 lines of new/, PLUS one branch-specific fix of 1-4 files). The walk's screens 1-19 rebuilt the same screens differently and all merged — these branches would REVERT nothing (their content is new/ additions, additive) but merging any of them would re-introduce the superseded phase-2 tree wholesale. VERDICT: the branch-specific fixes (auth interceptor, input-output search, MIS dropdowns, access guard, status-toggle CSS) are the only unmerged value; the phase-2 base is superseded. Options for the owner: cherry-pick the 5 branch-specific fixes onto current main (each is small), or delete the branches with the fixes recorded. (2) The implant pair (rebuild/add-new-implant-opening-balance ahead=6, rebuild/details-implant-opening-balance ahead=4) — the IMPLANT-class screens (module id UNDETERMINED, legacy states commented out, Q16), OWNER-BLOCKED at write time, never built, never merged. Their content is the same class as the parked KitTypeMaster "deliberately disabled features — re-enable or retire" decision. VERDICT: park alongside that decision; the branches are the record. (3) STANDING STEP (first diff-before-merge catch, adopted): git diff --stat main..<branch> from the gov worktree is read BEFORE every merge — BR-188's 799-deletion illusion was the demonstration.

- CHERRY-PICK PRE-CHECK RESULT (2026-08-10, owner ordered order: module-access-guard, auth-interceptor, input-output-charts, mis-reports, status-toggle; owner's check-before-picking directive — diff each touched file vs main): ALL FIVE CHERRY-PICKS ARE DEAD — do NOT cherry-pick, do NOT merge, delete the branches. Evidence: 10 of the 12 touched files across the five branches are BYTE-IDENTICAL on main already (the walk's own rebuilds/merges carried the fixes onto main independently): input-output-charts html/service/ts (3/3 SAME), mis-reports.html (SAME), billing-group-master.ts + insurance-master.ts + organization-master.ts (3/3 SAME), app.config.ts (SAME). The 2 remaining files DIFFER with main NEWER: (1) module-access.guard.ts + session.service.ts + app.routes.ts + access-denied.ts (module-access-control-guard branch) — main's versions are the evolved Track A #65 tree (ensurePermissions, permissionsUnavailable vs denied distinction); the branch's versions predate the whole screen walk (app.routes.ts ~60 lines vs main's ~1,900; session.service +7/-153 direction). Cherry-picking d0ae5488 would REVERT the working guard — the 799-deletion trap in miniature, exactly as the owner predicted. (2) styles.scss (status-toggle branch) — the branch version REMOVES main's modal cascade-scoping fix (Phase 4 batch 6, bootstrap.min.css .modal conflict); main is newer. (3) auth.interceptor.ts (auth-interceptor branch) — main +44/-11 direction; main's interceptor has grown. VERDICT: all five fixes already exist on main in equal-or-superior form; no cherry-pick is a no-op at best, a revert at worst. Branch disposition: delete all five fix/* branches (the owner's "once all five fixes are cherry-picked or explicitly declined" condition — they are now explicitly declined with evidence). Implant pair stays parked per the earlier decision.

- BRANCH DISPOSITION — FIVE fix/* BRANCHES DELETED (2026-08-10, owner approved; pre-check evidence per file on the record). What each contained and why it was declined — if found in the reflog in six months: the fixes SHIPPED IN EVOLVED FORM ON MAIN; nothing was lost.
  1. fix/module-access-control-guard (d0ae5488) — added module/role access-control guard matching legacy's resolve-block checks (app.routes.ts, core/module-access.guard.ts, core/session.service.ts, features/access-denied/access-denied.ts). DECLINED: main is a strict SUPERSET — module-access.guard.ts on main has the moduleId check + hasModuleAccess + the Track A #65 evolution (ensurePermissions, permissionsUnavailable-vs-denied distinction); session.service.ts has hasModuleAccess/ensurePermissions/permissionsLoaded; app.routes.ts wires the guard across ~241 routes (branch's was ~60 lines pre-walk). STRONGEST ARGUMENT FOR THE PRE-CHECK: cherry-picking d0ae5488 would have REPLACED the guard every screen's C3 check reads with its pre-evolution predecessor — on the security control, with no test failing to signal it. The 799-deletion trap in miniature.
  2. fix/auth-interceptor-missing-headers (9a83d2bb) — added the auth interceptor attaching Authorization/userId/unitId headers (app.config.ts, core/auth.interceptor.ts, core/session.service.ts). DECLINED: main's auth.interceptor.ts attaches the SAME three headers (Bearer + userId + unitId sourced from the session) in evolved form; app.config.ts byte-identical; session.service.ts on main is the newer tree (branch +7/-177 direction).
  3. fix/input-output-charts-patient-search (a95e2c0d) — fixed patient search AND-combining UHID+name returning zero results (input-output-charts html/service/ts). DECLINED: all 3 files BYTE-IDENTICAL on main — the fix shipped via the walk's own rebuilds.
  4. fix/mis-reports-unit-org-user-dropdowns (6c190832) — restored unit/user/org MISReports parameter controls to real dropdowns (mis-reports.html). DECLINED: byte-identical on main.
  5. fix/status-toggle-css-and-mutation (2b66f914) — fixed status-toggle CSS (no :checked state) + fragile in-place mutation (billing-group-master.ts, insurance-master.ts, organization-master.ts, styles.scss). DECLINED: 3 files byte-identical; styles.scss on main has the input:checked + .slider fix (the :checked essence) PLUS the Phase 4 batch 6 modal cascade-scoping fix the branch's version predates.
  Net: 10 of 12 touched files byte-identical on main; the 2 differing (guard, styles.scss) and the interceptor have main NEWER with the fix present. All five fixes exist on main in equal-or-superior form. Implant pair stays parked with the KitTypeMaster decision.

- WALK AUDIT + COUNT CORRECTION (2026-08-10, owner directive after the screen-20 merge review — "one instance is a mistake, a pattern is a measurement problem"): (1) AUDIT — for each of the walk's screens 1-20, checked whether a feature dir or route existed on PRE-WALK main (288d0bb1, 2026-08-07): 16 screens were GENUINELY NEW (add-new-drug-recall, uom-master, combination-medication-master, allergy-master, allergy-type-master, surgery-master, immunization-schedule, vaccination-master, blood-component, nursing-order-master, formulation-type-roa, route-administration, procedure-configuration, resource-configuration, cancellation-master, operation-theatre — no dir, no route in pre-walk main). TWO were REWRITES of pre-existing owner-approved rebuilds: frequency-master (built phase4-batch10 c5681c84 2026-07-22, rewritten as screen 15 — its commit message documented the rewrite as a 'THIRD DANGLE instance' without recognising the existing rebuild) and operation-theatre-type (built batch 22 32296f6f 2026-07-27 with owner Step-6 sign-off, rewritten as screen 20). The reachability step checked ONLY the legacy side (C5); it never checked whether a REBUILD already existed — the gap. (2) COUNT — 239 feature dirs on main includes both rewrites as if new: the real figure is 239 minus the 2 double-counted = 237 distinct rebuilds, OR equivalently the walk's net-new contribution is 16 dirs (17 dirs added since pre-walk main minus frequency-master's README-only add). The '240' figure quoted during the day double-counts frequency-master and operation-theatre-type. (3) FIX — preflight C7 ALREADY-REBUILT added (reads main's current tree; flags when the feature dir AND route loadComponent already exist; exit 2 on flag; NEW-CLASS SURFACING rule applied to screens; would have caught both rewrites at selection time; controls: positive operation-theatre-type flags, negative zz-never-built passes). Also tools/audit_walk_contribution.py for the recomputation. (4) Screen-20 README corrected to name this is a rewrite of the batch-22 build, with the owner-approved behaviour changes (update() validation, toggle reload, convention shift) named SEPARATELY from the false-success mitigation (the one defect-driven change). The walk resumes with C7 as a standing selection gate.

- SCREEN 21 SELECTION + CLASSIFICATION CORRECTIONS (2026-08-10): (1) C7 SWEEP — the owner-directed one-pass C7 check of the 19 OTIMS states the walk's hand-list called 'not rebuilt' CORRECTED the hand-list: THREE were already rebuilt under kebab dirs the hand-list misnamed (operationTableType/523 -> operation-table-type, anaesthesiaType/524 -> anaesthesia-type, checklistMaster/526 -> checklist-master; all batch-era, anaesthesiaType+checklistMaster from batches reviewed this window). The OTIMS subtree's real remaining pool is 15 genuinely-not-rebuilt states, not 19. Sweep tool: tools/c7_sweep_otims.py. (2) 520 PROCEDURE TYPE — the walk previously walked past 520 as NOT-A-SCREEN-YET; that classification was WRONG. Full reachability (state registered app.js:12385 resolve gate item.id==520; menu node 520->514->369 actionLink #/procedureType matches; controller loaded index.html:1732; backend ProcedureTypeMasterConttroller [sic] /api/otims/* full CRUD) + LIVE-VERIFIED BY CALLING (list 200 success [] dev-empty, count 0, byId []) => LIVE class. Correction on the record; screen 21 built. (3) SEVENTH CONFIRMED FALSE-SUCCESS — ProcedureTypeMasterServiceImpl:74 SUCCESS return OUTSIDE the if(res.getObject()!=null) guard (58-71) with the DAO catch-and-returning (no rethrow): an incomplete-DTO save (updatedDate omitted -> DAO NPE) returns SUCCESS with NOTHING written. The BR-187 mechanism on a fourth screen (after operation-theatre BR-187, operation-theatre-type screen 20, and this). Backend BR candidate, same fix shape as BR-187; NOT FIXED (Rule 10). (4) Screen 21 = procedureType, full-CRUD rebuild, first screen selected under the C7 gate (C7 PASS confirmed genuinely new). README + classification + divergences on the branch (f88d2109).

- OTIMS 19-STATE C7/FULL SWEEP (2026-08-10, owner's third ask): the full classification of the 19 OTIMS states the walk's hand-list called 'not rebuilt'. RESULT: 3 ALREADY-REBUILT (operationTableType/523 -> operation-table-type, anaesthesiaType/524 -> anaesthesia-type, checklistMaster/526 -> checklist-master — all batch-era, confirmed route+dir on main); 1 ROUTE-ONLY PARTIAL (checklistItem/525 — route exists on main but NO dir; a half-built state awaiting its component); consentType/528 = DECOY-ROUTED (screen-10 class): node 528 reaches the MasterController shell state (app.js:12585, 0-bind template) while the REAL full-CRUD screen (consentTypeMasterController.js 396 lines, loaded index.html:526, backend ConsentTypeMasterController /adt/* full CRUD) is ORPHANED — the real controller self-registers ZERO states; its payload is {organizationId,unitId,offset,noOfRecordsPerPage} (probe 400 = probe-payload class, backend reachable); 3 DANGLE/orphaned-with-backend (notesMaster/529 'Anaesthesia Note' node has actionLink #/noteMaster — the state is procedureTemplate-class but the node targets a DIFFERENT route; surgicalCalendar/710 — menu node with NO actionLink, backend SurgicalCalendarMasterController exists; procedureTemplate/20447 — state registered top-level NOT commented, backend ProcedureTemplateController /otims/* exists, NO menu node reaches it — the orphaned class); 7 STATIC SHELLS / NOT-A-SCREEN-YET (staffRole, inventoryItem, inventoryItemType, equipmentType, serviceType, insuranceCompany, typeOfStay, generalPractitioner — all MasterController-generic + template with ZERO ng-bindings + no dedicated controller JS + no backend controller — the screens-4/5/9/10 shell class; nothing to wire, walk past); resourceChargesConfiguration/761 (menu node no actionLink, backend ResourceChargeConfigurationController exists — orphaned class with backend). CORRECTED SUBTREE REALITY: of the 19, the genuinely buildable LIVE/dangle-with-backend set is consentType (live), notesMaster, surgicalCalendar, procedureTemplate, resourceChargesConfiguration (orphaned-with-backend — dangle class, rebuildable like 477/478/479), checklistItem (route-only partial — needs C7 surfacing before build). The 7 shells + 3 already-rebuilt are NOT candidates. Sweep tool: tools/c7_sweep_otims_full.py (committed).

- SCREEN 22 = surgicalCalendar (710), DANGLE class, merged-ready 2026-08-10. Selection under C7 (PASS — genuinely new). The 19->5 sweep paid for itself: 710 was selected as a clean dangle with a real backend and NO decisions attached (the owner's call), built full-CRUD, save proven end-to-end (64 rows landed/cleaned). NEW FINDING (eighth false-success-adjacent write): the legacy save's single-day-range silent no-op — ServiceImpl:267 while(startDate.isBefore(endDate)) is STRICT, so fromDate==toDate writes 0 rows while reporting 'Added Successfully.' (200 success). Demonstrated live both directions (same-day -> 200 + 0 rows; 5-day -> 64 rows). Legacy-fixable independently (isBefore -> isBeforeOrEqual); NOT FIXED (Rule 10); the rebuild documents it, the probe uses a multi-day range. Secondary: slot-start truncation (first slot 14:30 despite 09:00 from-time) observed not root-caused. The C7-sweep-value note (owner): 19 candidates -> 5 in one pass instead of fourteen wasted selections — the strongest argument for running C7 as a subtree sweep rather than per-screen; recorded prominently. QUEUED (owner decision, not now): consentType's decoy (528, what to do with the orphaned real screen), checklistItem's route-only PARTIAL (525), and whether the walk continues into the remaining OTIMS dangles (resourceChargesConfiguration 761, notesMaster 529, procedureTemplate) or moves to a subtree with more live screens after 710.

- SCREEN 22 FIX (2026-08-10, owner click-through — the FIFTH probe-passed/browser-click-failed instance): the surgical-calendar time inputs were <input type="time">; a user's typed "09:00"/"12:00" NEVER reached the model — the browser's segmented time entry (HH:MM:-- ) requires the seconds segment to resolve before the value commits. Captured via the Angular devtools global (not inferred): inputs displayed 09:00/12:00 with trailing '--' while fromTime_dom "" and model fromTime "". page.fill() passed because it sets the value property directly — the probe-vs-user difference. FIX: type="text" HH:MM inputs (matching the legacy template's plain text inputs surgical-calender.html:128,136 — never a native picker) + an HH:MM format guard. SECOND DEFECT: the inline message was hardcoded to the date-range text while the form can be invalid for three reasons — shared formValid boolean + single hardcoded message showed the wrong reason; fixed with a formError signal per guard. PROBE STANDARD UPDATED (owner item 3): probes for this screen now type keystrokes (press_sequentially) into the real inputs and assert the typed value reaches the model before Save — reproducing the user's interaction, not setting model state. The exact interaction that failed the owner now passes (typed times -> model values -> 1 post -> 48 rows verified + cleaned). Committed c504e303.

- SCREEN 23 = resourceChargesConfiguration (761), DANGLE class, 2026-08-10. Selection: my call between 761 and notesMaster/529 — took 761 (notesMaster's node links #/noteMaster vs the state's /notesMaster — the one-character routing defect already logged QF-2026-08-10-AG; left to the owner's decision; 761 is a clean dangle with no decisions attached). C7 PASS at selection. Built full-CRUD: state registered app.js:18871 (no resolve gate), node 761 no actionLink, backend ResourceChargeConfigurationController /api/otims/* (6 endpoints), all reads live-verified (list 200 [], speciality 2 rows, role list 2 rows, getResourceChareges 200 [], unit-service feed 1 real row serviceId 3). FINDINGS: (1) INNER-JOIN drop on the list (speciality/service/role — FIFTH list-path instance); (2) the walk's FIRST clean OTIMS write path — DAO + service both rethrow ServiceException, service returns the DAO response verbatim, NO false-success shape (contrast screens 19/20/21); (3) DATE-FORMAT CONTRACT — the save DTO's dates parse as DD-MM-YYYY HH:mm:ss (Dozer DateConverter, legacy moment format controller JS:175-176); ISO-8601 500s 'Unable to parse source object using specified date format' (verified live) — the rebuild sends the legacy format. Save proven end-to-end (row landed verified psql + cleaned per Q28). Branch 5180afab.

- SCREEN 23 FINDABILITY FIX (2026-08-10, owner click-through — NOT the BR-187 class, determined not assumed): green toast + search found nothing. psql showed the rows PRESENT (toast TRUE) but unit_id NULL — the rebuild's save payload OMITTED unitId (legacy sends it, controller JS:65). The list query requires rcc.unitId=:unitId, so NULL=1 filters saved rows OUT of the list: rows exist in the DB but are INVISIBLE to the grid/search. Fixed: unitId in both save+update payloads; verified end-to-end (API list returns the row with join names; grid shows it). PROBE STANDARD EXTENDED (owner item 4): save probes must assert FINDABILITY — the saved row appears in the reloaded grid, not just psql. The assertion matches the rendered amount ('250', no trailing zeros). Sixth probe-vs-user lesson: a save probe verifying only the DB checks half the path. Committed 6ccfc478.

- SCREEN 23 POST-MERGE RECORD (merged c8bddcae, 2026-08-10): (1) DANGLE finding — node 761 'Resource Charges Configuration' (parent 514 -> 369) exists with NO actionLink: the menu advertises a screen the route system cannot reach; state registered app.js:18871 (no resolve gate); the rebuild registered the route (moduleId 761) and restored the screen. (2) THE OPEN QUESTION ANSWERED PRECISELY (owner's ask: absent, or present with the wrong unit_id?): PRESENT WITH THE WRONG unit_id — the saved rows were IN psql with unit_id NULL (not absent, not a wrong-but-populated unit like unit 2). The mechanism is a SCOPING defect in its own right, distinct from a stale grid and from a false toast: the rebuild's save payload omitted unitId, so the Dozer mapping persisted unit_id = NULL; the list query requires rcc.unitId=:unitId (unitId=1), so NULL = 1 is false and every saved row is filtered OUT of the list the creator sees. A save that lands a row under a unit scope (NULL) its creator cannot see is the scoping shape: the row exists, the toast is true, and the user still cannot find it. Fixed by adding unitId to both payloads; verified end-to-end (API list returns the row with join names; grid shows it). (3) FINDABILITY ASSERTION NOW STANDARD on save probes (owner item 4, sixth probe-vs-user lesson): a save probe must assert the saved row appears in the RELOADED GRID, not just psql — verifying only the DB checks half the path. Probe updated on this screen; all subsequent save probes carry the assertion.

- SCREEN 24 = notesMaster (529), DANGLE with the one-character routing mismatch, 2026-08-10. Selection: my call — took 529 over procedureTemplate (529's node exists and merely links the wrong route #/noteMaster vs /notesMaster, the logged QF-2026-08-10-AG; procedureTemplate has no menu node at all). Rebuild uses the STATE's url (/notesMaster) per the owner's instruction. PHANTOM-BROKEN LIST — the walk's FOURTH phantom finding, first on a DANGLE screen's list path: GET_NOTELIST_MASTER_LIST selects 8 columns absent from DDL (template_format_id, form_template_id, form_template_keywords, template_type_id, template_id, title mapped in the entity but never added to otims.m_note_master's 12 columns); both list reads 500 with correlation-id envelopes (global handler works). Save is a forms-engine integration, mapped NOT wired (BR-190 dependency). The rebuild surfaces the phantom honestly (screen-17 pattern) with the correlation ref. Error-handler fix: Angular parses JSON error bodies into objects — JSON.parse(object) threw and lost the ref; now handles both shapes. C5 UNVALIDATED — the preflight's C5 check itself caught the one-character routing mismatch (good: the check names the defect). Branch e67cfea5.

- SCREEN 24 BANNER UPGRADE (2026-08-10, owner ask): the notes-master banner now NAMES the six phantom columns (otims.m_note_master: template_format_id, form_template_id, form_template_keywords, title, template_type_id, template_id) per the resourceConfiguration standard. The four answers recorded precisely: (a) the six columns, table otims.m_note_master (12 real DDL columns); (b) GET_NOTELIST_MASTER_LIST + GET_NOTELIST_MASTER_LIST_BY_NOTE_TYPE_ID; (c) ALL THREE paths break — list/byNoteType 500 on SELECT (could not extract ResultSet), saveNotesMaster 500s on INSERT (SQLGrammarException at NotesMasterController.java:34; verified live ERR-d85de88c with Integer noteType; string noteType = HIS-400 probe-payload class); (d) LOUD — HTTP 500 correlation-id envelopes, NOT the screen-17 masked-200 shape. The earlier '8 columns' count in the screen-24 record is corrected to six here. Committed 20b3998c.

- SCREEN 25 = procedureTemplate (515), DANGLE-ORPHANED, 2026-08-10 — the last OTIMS dangle-with-backend, completing the sweep's genuine pool (remaining: consentType's decoy + checklistItem's partial, both queued). Sweep classification CORRECTED: node 515 EXISTS but links a DEAD state (#/procedureConfiguration); node 696 no actionLink; none link the template. SIXTH PHANTOM: role_desc in public.m_role_master (DDL has role_name) selected by GET_CHECKLIST_LIST_BY_PROCEDURE_CONFIG_ID, breaking template read + save (both 500 loudly, verified live). The rebuild surfaces it honestly naming role_desc. NEXT-SUBTREE SWEEP PLANNED (owner's direction): before entering the next subtree, run the C7 sweep (the OTIMS 19→5 pass beats per-screen discovery) — candidate next subtrees to be chosen with the owner.

- SCREEN 25 IDENTITY AUDIT (2026-08-10, owner DO-NOT-MERGE): procedureTemplate is NOT a standalone master — it is a SECOND VIEW of module 515, the drill-down DETAIL of procedureConfiguration (screen 16, merged 78c168d0). Evidence: the legacy state's title is ' Procedure Configuration' (same as 515); no resolve gate, no moduleId; params {commonObj, infoObj} passed by CALLING screens (IPDCoverSheet, OPDcoversheetNew, ordersTab — billing/order contexts navigate INTO it); screen 16 is the list master, procedureTemplate is the detail (consent/checklist/staff). The branch's route used moduleId 515 — creating TWO routes on 515 (collision: moduleId 515 appears twice in app.routes.ts). WHY C7 DID NOT FLAG IT: C7 checks feature-dir + route-path tokens only (preflight_screen.py check_c7) — NO moduleId dimension; the collision is invisible by construction. THE FIX: C8 MODULE-ID COLLISION check added to the preflight (verified: FLAGS procedureTemplate/515, PASSES notesMaster/529) — the DRILL-DOWN/SECOND-VIEW shape is now surfaced at selection, before any build. DECISION NEEDED from the owner: rebuild as a second view sharing 515 (legitimate — the guard passes users who can access 515), OR fold the detail into the procedure-configuration feature (screen 16), OR give it the parent's module treatment. Branch NOT merge-ready until the owner decides the shape; the branch stays parked (the role_desc phantom finding + banner standard remain valid regardless of shape).
- BANNER STANDARD CONFIRMED (2026-08-10, owner): the role_desc banner (column named, DDL's actual column named, entity field + line, the query, both broken endpoints) is THE template for every phantom banner from here — a complete defect report in a user-facing banner.

- COUNT RECORD (2026-08-10, owner condition a): the procedureTemplate merge does NOT increment the distinct-rebuild count. 241 STAYS 241. The count is by SCREEN; procedureTemplate is a SECOND VIEW of module 515 (procedureConfiguration, screen 16, already counted). Recorded explicitly so the next audit does not have to rediscover it.
- SCREEN-25 SHAPE DECISION (2026-08-10, owner): MERGE procedureTemplate as a SECOND VIEW of module 515 — not as a new screen. Two components legitimately share 515 (the legacy privilege tree gates them together). Conditions applied: (a) count stays 241 (recorded above); (b) README + merge message say SECOND VIEW OF 515 (branch reworded, 1030c197); (c) C8 MODULE-ID COLLISION stays with its control, and SECOND-VIEW is now the SEVENTH reachability bucket in RULES.md (surfacing-before-building required, like already-rebuilt); (d) the role_desc banner is the phantom-banner standard from here.

- SCREEN 26 = accountPayableMaster (579), LIVE class, 2026-08-10 — the FIRST Billing-subtree candidate (sweep: 16 of 48; 12 LIVE; owner picked 579 as a master sharing the 580 controller family). C7 + C8 at selection (8/8 clean — the first run with the live C8 check). Reads live-verified by CALLING (200 success, real dev rows; {orgId, orgUnitId} payload). SCOPED CHECK: false-success shape ABSENT (DAO rethrow, SUCCESS after the real write); org-trust GOOD class (verifiedIdentityService); single-table list. NPE-CLASS FINDING — the walk's NINTH getDate(null), BR-187 class, ON A BILLING WRITE PATH (surfaced immediately per the billing directive): DAO save calls GlobalCommonDateUtils.getDate(accValidityFromDate) unconditionally (DAO:90-91); omitting the validity dates NPEs (ERR-0a7cc3d9, live) -> rethrow -> 500. Full-DTO contract fixed in the rebuild (both validity dates sent). Save probe PASS with the findability assertion (row landed org 1 unit 1, findable, cleaned). Branch 590aa6e1. QUEUED (owner's standing direction, not run now): when the 16 candidates are done, re-check the 20 NOT-A-SCREEN-YET by ENDPOINT TRUTH (the 585 lesson).

- SCREEN 27 CORRECTION (2026-08-10): 580 Account Payable is DECOY-ROUTED, NOT LIVE — the Billing sweep's LIVE classification was WRONG (the owner picked 580 on it). Evidence: state app.js:10308 binds MasterController; account-payable.html (164 lines) has ZERO Angular bindings; no dedicated controller JS (index.html:886 loads 579's accountPayableMasterController); no distinct backend (the only account-payable controller is 579's AccountPayableMasterController, rebuilt as screen 26 at 738e9667). The REAL account-payable screen is 579. WHY THE SWEEP GOT IT WRONG: the LIVE flag came from the backend grep matching 'class AccountPayable...' -> 579's controller, without the DECOY test (own controller JS + template bindings). THE FIX: the DECOY test is now in c7_sweep_billing_full.py — verified it ALSO flags 587 Order Set (shell state app.js:9588 + 0-bind order-set.html; real screen orderSetMaster with orderSetMasterController.js). Corrected tally: 9 confirmed LIVE (583, 585, 592, 610, 612, 613, 614, 615, 621), 2 DECOY-ROUTED (580, 587). DECISION NEEDED on 580 (sixth bucket — do not default to build): A) point node 580 at the rebuilt 579 route; B) leave the legacy shell and record the decoy; C) build as a second-view (least defensible). No route or component was written for 580.

- DECOY-ROUTED QUEUE — ONE DECISION ITEM (owner directive 2026-08-10: decide the CLASS, not the instances): four confirmed decoy-routed screens share the shape — a MasterController shell state with a zero-binding template sitting where a real screen should be, the real controller either unbound (consentType 528: consentTypeMasterController.js 396 lines self-registers no state) or absent (580: no controller of its own, 587: real controller belongs to orderSetMaster, 578 variablePricing: real VariableSequencePricingController/VariablePricingController unbound). THE ONE ITEM: "decoy-routed screens — build the real screen behind the shell, or leave the shells". Instances: 528 consentType, 578 variablePricing, 580 accountPayable, 587 orderSet. No per-instance decisions.
- SWEEP LIVE-BUCKET CORRECTION (2026-08-10, owner directive 1): the Billing sweep's LIVE bucket was WRONG BY TWO — 12 LIVE became 9 once the DECOY test ran (580 + 587 are shells pointing at real backends). Root cause: the LIVE classification was state+node+backend-grep, which CANNOT distinguish a real screen from a shell state pointing at a real backend (the backend grep matched 579's controller for 580's node). The DECOY test (own controller JS NOT MasterController + template Angular bindings > 0) is what separates them — it must run as PART of the sweep, not after it. It now does (c7_sweep_billing_full.py). Corrected tally: 9 confirmed LIVE (583, 585, 592, 610, 612, 613, 614, 615, 621).

- SCREEN 27 = unitServiceMaster (583), LIVE class (DECOY test passed), 2026-08-10 — the owner's pick between 583 and 585 (583 is the MASTER; 585 its price detail). Preflight 8/8 CLEAN (C7 genuinely new, C8 no collision, C5 node chain). BROKEN LIST finding (QF-2026-08-10-AM): the count works (object: 1) but the list 501s every call — the char-field DTO defect (isOutsourceJob char vs String SQL column, AliasToBean IllegalArgumentException swallowed by the DAO catch-and-return, masked HTTP-200 error envelope). The rebuild surfaces it honestly naming the mechanism. Branch e26b175e.

- SCREEN 28 = unitServiceBasePrice (585), LIVE class, 2026-08-10 — the owner's 585/592 call (585 = the endpoint-truth find, the strongest liveness evidence). C7 + C8 at selection (8/8 clean). BACKEND BY ENDPOINT TRUTH: the controller JS calls /api/unit/billing/getServiceBasePriceList; the Java controller is ServiceBasePriceMasterController (billing/unit) — the class-name grep missed it, endpoint truth found it (the 585 lesson applied). Live-verified: list 200 success real rows (basePrice 250.0), count object: 2. SCOPED CHECK: false-success ABSENT (update path rethrows); INNER-JOIN drop PRESENT (sixth list-path instance — service/speciality/sub-speciality); DDL clean (no phantom, visit_type_id exists); payload contract = the misspelled `orgnisationId` key. Branch 4e0abc50.

- SCREEN 28 FIX (2026-08-10, owner click-through): unitServiceBasePrice SERVICE column was EMPTY in the owner's click — root-caused as an AGENT field-name mismatch, NOT a backend defect: (a) the query selects sm.serviceStandardName AS serviceName (DTO field serviceName, ServiceBasePriceMasterDto.java:33); (b) psql row 1 -> service 'Cardiology Consultation' — the join partners EXIST (an INNER-JOIN miss would have dropped the row; it rendered); (c) the rebuild bound row.serviceStandardName while the API returns serviceName — fixed to row.serviceName; (d) the To column is DEV-DATA NULL (row 1's to_date NULL — the standard treatment, noted in the README); (e) search CAN work — the name is in the data and now reaches the model. Probe upgraded to assert the NAME renders + search-by-name finds it (the owner's interaction). Fix d54ac0fa.

- SCREEN 28 FILTER FIX + SAME-DEFECT AUDIT (2026-08-10, owner's measurement): typed 'abc' into the screen-28 search box, the Cardiology row stayed, counter stayed '1 row(s)'. ROOT CAUSE (a): searchTerm was a PLAIN string property and filteredRows = computed() read it directly — Angular computeds re-evaluate ONLY when a SIGNAL they read changes; mutating a plain property via [(ngModel)] never triggers recompute. The filter was bound but computed once and cached forever. FIX (c): searchTerm -> signal(''); computed reads searchTerm(); input [ngModel]="searchTerm()" (ngModelChange)=...; probe gained the NEGATIVE assertion ('abc' excludes the row + counter '0 row(s)') — positive-only assertions cannot catch a non-filtering filter (the EIGHTH probe-pass/click-fail instance). (d) SAME-DEFECT AUDIT — FOUR MERGED SCREENS have the identical dead-filter shape (searchTerm plain property + banana-box [(ngModel)] + computed reading it), their search boxes equally dead: screen 21 procedureType, screen 22 surgicalCalendar, screen 23 resourceChargesConfiguration, screen 26 accountPayableMaster. REPORTED, NOT FIXED (owner directive: report which screens have a search box and whether each filters; the screen-28 fix pattern is the template). Fix 0e740078.

- DEAD-FILTER FIXES (2026-08-10, autonomous run — the copied-filter defect, QF-AN family): FOUR merged screens fixed with the screen-28 pattern (searchTerm plain property -> signal(''); computed reads searchTerm(); input [ngModel]="searchTerm()" (ngModelChange)=...). One branch per screen, each with a both-directions probe (matching term includes, non-matching excludes + counter '0 row(s)', clear restores): screen 21 procedureType (fix/dead-filter-procedure-type, ee462e30), screen 22 surgicalCalendar (fix/dead-filter-surgical-calendar, 4df39290), screen 23 resourceChargesConfiguration (fix/dead-filter-resource-charges-configuration, 6b6a5679), screen 26 accountPayableMaster (fix/dead-filter-account-payable-master, 38cc42b3 — save probe preserved as probe_account_payable_save.py, search probe new). Branches chain (each from the previous); tip = account-payable-master carries all 4 (13 files / 284+/111-); all merge-ready with main merged + dry runs clean.
- COUNT CLOSED (2026-08-10): the general dead-filter shape scan (plain property read inside a computed) across ALL 248 rebuild features: exactly the 4 walk screens + screen 28 (already fixed). linen-issue RETRACTED as a false positive (its grid filter quickFilter is ALREADY signal-bound; issueNo/tab1IndentNo/tab2IndentNo are documented dead legacy-parity fields, no computed reads them — scanner mechanical cause: matched computed-with-.filter-in-file, not computed-reads-property). The 18 mixed screens: all plain bound properties are form/payload fields or SERVER-side search keywords (the server filters) — NONE read in a client computed, no defect. The 5 unbound (delivery-type-master, immunization-schedule, nutritional-master, pharmacy-formulation-type-master, schedule-worklist): placeholder-only inputs with NO ngModel binding — dead UI stubs, not the computed-filter defect.

- SCREEN 29 = chargeConversion (592), LIVE but TRANSACTION-shaped, 2026-08-10 (autonomous run, next confirmed LIVE after 583/585). C7 + C8 at selection (8/8 clean). Backend live (ChargeConversionController unit/ChargeConversion/saveChargeConversion — endpoint-truth verified: HTTP 200 honest error for empty payload). FINDING — SILENT PARTIAL-FAILURE on the billing write path (the family the owner's warning named): ChargeConversionServiceImpl:74-83 — when the order-details lookup returns ERROR (DAO catch-and-return, masked 200), the details save + order-rate update are silently skipped yet SUCCESS + 'Charge conversion saved' is returned. BR candidate (Rule 10). The rebuild surfaces it on an amber banner; the flow is patient-gated (no standalone list), documented not faked. Branch ca87bca4.

- SCREEN 30 = serviceTariffMaster (610), LIVE class, 2026-08-10 (autonomous run). C7 + C8 at selection (8/8 clean). Endpoint-truth find: UnitServiceTariffMasterController (api/unit) — class name != state root. FINDING — the legacy PRIMARY grid endpoint (getUnitServiceTariffMasterList) is unconditionally broken for empty filters: DAO appends `IN (:list)` + setParameterList binds [] -> generated SQL `IN ()` -> syntax error; the DAO rethrows cleanly but the CONTROLLER's catch is EMPTY (:51-52) -> masked HTTP-200 null-message envelope. The ForWeb variant's `IN (1)` placeholder works (verified live). The legacy frontend calls the broken endpoint. BR candidate (Rule 10). Rebuild calls ForWeb + amber banner. Branch 79e31a6c.

- SCREEN 31 = doctorConsultation (612), LIVE class, 2026-08-10 (autonomous run). C7 + C8 at selection. The state name carries the LEGACY TYPO (doctorConsulationMaster, app.js:8317 — C5's DEAD flag was a name mismatch, hand-confirmed the state exists; the sweep's LIVE was right). Endpoint truth: DoctorConsultationServiceMapperController (api/billing/unit). Live-verified: list 200 success [] (dev-empty), count object: 0. PAYLOAD CONTRACT: tabNumber: 1 REQUIRED. Scoped write-path check: false-success ABSENT (save/updateStatus catch-and-fall-through to honest ERROR). The controller's empty catch -> masked null-message 200 (the screen-30 shared pattern; BR candidate). Branch 3c453ac6.

- SCREEN 32 = globalDoctorShare (613), LIVE class, 2026-08-10 (autonomous run; the FIRST of the owner's doctor-share trio 613/614/615). C7 + C8 at selection (8/8 clean). Live-verified grid [] + grades []; payload contract = specialitySerarchName (DTO typo) REQUIRED. SCOPED WRITE-PATH CHECK (saveGlobalDoctorShare — the six-method enumeration): FALSE-SUCCESS ABSENT (catch returns honest ERROR; SUCCESS only after batch+commit). FINDING — BATCH-PARTIAL-COMMIT SHAPE: mid-batch failure leaves earlier commits persisted, NO rollback in the catch (finally only closes) — partial-write risk on the money path, BR candidate. Empty-list SUCCESS unreachable (service guards size > 0). Branch b3f5c215.

- SCREEN 33 = doctorwiseShare (614), LIVE class, 2026-08-10 (autonomous run; the SECOND of the doctor-share trio). C7 + C8 at selection. State name mixed-case (doctorWiseShare — C5 DEAD = name mismatch). FALSE-SUCCESS FINDING — the walk's most serious class, ON THE MONEY PATH the owner's warning named: TDoctorShareDaoImpl.saveDoctorShareList (680-726) — when the voucher save fails, the share rows were already updated but the voucher was NOT written, yet the response is SUCCESS + ERROR_MESSAGE (line 724); the empty-list path returns SUCCESS 'saved' with nothing (line 726). NEW VARIANT (SUCCESS-code-with-error-message). BR candidate (BR-188 family). Branch cf44f4dd.

- SCREEN 34 = doctorReferralShare (615), LIVE class, 2026-08-10 (autonomous run; the THIRD of the doctor-share trio). C7 + C8 at selection. State name doctorReferralShareMaster (C5 DEAD = name mismatch). FALSE-SUCCESS FINDING on saveReferralShare (IndividualDoctorShareServiceImpl:216): SUCCESS + "saved" when BOTH detail lists are empty — nothing written, success reported (the screen-22 same-day shape on the referral-share money path). BR candidate. DAO saves: clean catch->honest-ERROR + the latent batch-partial-commit shape (613 class). Branch 2d0ea0da.

- SCREEN 35 = depositMatrixConfig (621), LIVE class, 2026-08-10 (autonomous run; the LAST confirmed LIVE — the 9-candidate LIVE bucket now EXHAUSTED: 583, 585, 592, 610, 612, 613, 614, 615, 621 all built). C7 + C8 at selection. State name depositMatrixConfig (C5 DEAD = name mismatch). Backend by class name (DepositMatrixConfigController). LIVE: list 200 success with REAL dev rows (5 rows: General / Self Entitlement — the first Billing screen with populated dev data). Write path: FALSE-SUCCESS ABSENT + the GOOD CLASSES (org-trust fix, createdDate guard, scoped BY_ID, cross-tenant update rejected). Branch 90a9fbd8.

- 20-BUCKET RECHECK RESULT (2026-08-10, autonomous run — the owner's queued item, run once the candidates were done): the endpoint-truth recheck (tools/c7_billing_20_recheck.py) found the 20 NOT-A-SCREEN-YET nodes are NOT backend-less AS A CLASS — all have real controller JS with backend calls (400/405 = real handlers exist, probe-payload/method artifacts; 404 = the recheck's guessed paths). The sweep's class-name-grep classification UNDER-COUNTED (the 585 lesson generalizes). Which of the 20 are genuinely LIVE screens vs decoy/dangle requires the per-screen walk (the recheck's literal-URL extraction hits shared dropdown feeds). The bucket is NOT settled — reclassified from "no backend" to "unwalked, per-screen endpoint truth owed". The ROUTE-ONLY partials (616/617/618) and the DANGLE (590) remain queued for the owner.

- 20-BUCKET RECHECK — CONCLUSIVE (2026-08-10, autonomous run): the sweep's 20 NOT-A-SCREEN-YET were a MIXED bag. Per-screen endpoint truth (CALLED, body status): 4 LIVE — 572 TPA Master, 574 Corporate Master (global), 584 Unit Service Mapping (real controller, 28 binds, subspeciality feed 200 with real rows), 608 Company Contract (real controller, 40 binds, package feed 200 with real rows). 14 DECOY-ROUTED (MasterController shell + 0-3 binds): 577, 581, 582, 586, 588, 589, 591, 593, 594, 595, 596, 599, 601, 620. 1 dead-markup: 611 (real controller, EMPTY template). THE DECOY QUEUE GROWS 4 -> 18 INSTANCES under the owner's ONE class decision. Next LIVE screens: 584 + 608. Record: new/src/app/features/_recheck-20-bucket/README.md (branch) + this line (main).

- SCREEN 36 = unitServiceMapping (584), LIVE class, 2026-08-10 (autonomous run; the recheck's first new LIVE). C7 + C8 at selection (8/8 clean). Endpoint truth: mapping-list feed /billing/organizationMaster/speciality/1/subSpeciality/1/org/1/unit/1 -> 200 [] (dev-empty); subspeciality feed -> 200 with REAL rows (Cardiology). Save (UnitServiceMapper) NOT probed — contract mapper-specific; 'save not exercised, contract unknown' recorded (BR scoped-check owed). Branch 12f3da4f.

- SCREEN 37 = companyContractMaster (608), LIVE class, 2026-08-10 (autonomous run; the recheck's second new LIVE). C7 + C8 at selection (8/8 clean). Endpoint truth: company list 200 [] ({organizationId,unitId,companyTypeId} — the orgId key 500s at Jackson deserialization BEFORE the controller try, the global-handler loud variant); visit-type feed 200 REAL rows (Day Care); package feed 200 REAL rows (needs the LOCAL /Unit proxy rule — gitignored dev config). Write path: createCompanyContarct RETHROWS — SUCCESS-from-catch ABSENT. Branch 04eb1d94.

- SCREEN 38 = TPAmaster (572), LIVE class, 2026-08-10 (autonomous run; the recheck's first confirmed LIVE — endpoint truth WITH path params proved it; the recheck's earlier 404 was its own path construction). C7 + C8 at selection (8/8 clean). Feeds live: registration-field config 200 [], company list 200 [], AR-code 200 REAL rows (TESTAR2), country 200 REAL rows (India). FORM surface — no write-path false-success. Branch 9e28c86a.

- SCREEN 39 = corporateMasterGlobal (574), LIVE class, 2026-08-10 (autonomous run; the recheck's LAST confirmed LIVE). C7 + C8 at selection (8/8 clean). Company list 200 [] ({organizationId,unitId,companyTypeId:3}). Write path: saveCompanyMaster controller catch HONEST ERROR with real message (BR-batch2 fix) — SUCCESS-from-catch ABSENT. Branch afdf9747.

- ROUTE-ONLY PARTIALS RESOLVED (2026-08-10, autonomous run): the sweep's 3 PARTIAL ROUTE-ONLY (616 AR Master, 617 AP Master, 618 GL Code) are ALL ALREADY-REBUILT on main — C7 verified 5 files + route each: ar-master (286-line ts), ap-master (328), gl-code-master (586, route at app.routes.ts:1384 refs features/gl-code-master/ — the C7 check derives the dir from the kebab input features/gl-code/ and MISSES the -master suffix; a preflight-derivation gap, not a missing rebuild). NO BUILD OWED for 616/617/618; the 3 empty branches (add-new-ar-master, add-new-ap-master created earlier, add-new-gl-code) carry no commits — left for the owner.

- DANGLE 590 CONFIRMED (2026-08-10, autonomous run): refundCRList — state ABSENT from app.js (a differently-named refundAgainstCR exists at app.js:15994 but is NOT the node's target), no index.html controller load, no controller JS. The node's link (#/refundCRList/cashForRefund) is a BROKEN menu link, not a screen — the DANGLE class holds. NO BUILD OWED.

- DECOY GROUNDWORK COMPLETE (2026-08-10, ITEM 2 prep only): 18 instances split into Group A (3 — REAL backend + REAL controller exist: 528 Consent Type backend LIVE with real rows (getConsentTypeMasterList 200, count 3), 578 Variable Pricing (unbound controller, endpoint truth owed), 587 Order Set (orderSetsList -> masked 500, root-cause owed)) and Group B (15 — template-only, NO controller anywhere in the legacy: 577, 580, 581, 582, 586, 588, 589, 591, 593, 594, 595, 596, 599, 601, 620; building = designing new backend behavior, not parity). Full detail: new/src/app/features/_decoy-groundwork/README.md. DECISION: Group A build (parity); Group B leave shells or design new.

- NEXT SUBTREE SWEEPED (2026-08-10, ITEM 6 — sweep before entering): parent 2 (Opd, 38 children) = 6 LIVE + 1 LIVE-ORPHANED + 31 NO-ACTIONLINK. The 6 LIVE (patientRegistration 3, appointmentScheduling 4, encounterQueueManagement 6, appointmentList 7, patientList 8, OPD Coversheet 9) are ALL genuinely unrebuilt (no feature dirs, no routes). Encounter (5) = LIVE-ORPHANED (state+backend, no menu link). Parent 3 (Adt, 48) = reports-only (48/48 NO-ACTIONLINK — the parent-7 pattern). THE OPD SUBTREE IS THE WALK'S NEXT TARGET.

- DEAD-FILTER COUNT CLOSED (2026-08-10, ITEM 3): the 5 unbound-input screens are NOT a second defect shape — hand-verified: 3 dead stubs (delivery-type-master, immunization-schedule — no binding at all, nutritional-master), 1 server-keyword placeholder (pharmacy-formulation-type-master), 1 LIVE filter (schedule-worklist — p.search + searchData() full-list client filter; the audit's 'unbound' call was wrong, the linen-issue class). The dead-filter defect stays exactly 4 walk screens (21/22/23/26) + screen 28's fixed pattern. The 18 mixed screens remain classified as server-side keyword/form fields (the copied-filter audit's (c) — no client computed reads them).

- GETDATE NULL GUARD — THE ARGUMENT (recorded 2026-08-10, owner: "Record it as the argument; do not fix"): the enumeration found 864 unguarded getDate-DTO call sites vs 305 guarded (1,169 total) — but the headline was misleading: 846/864 are persistence-layer (DAO/Service), and the classified split is ~390 LOUD (rethrow/no-catch/ERROR envelope) + ~103 masked-honest-ERROR + only 7 genuinely SWALLOWED-INTO-SUCCESS (BR-187 class: MedicationHistoryDaoImpl:153/154/156/164/170, AppointmentVitalMapperDaoImpl:203, FranchiseShareDaoImpl:189 — none on the Billing walk screens). THE CODEBASE'S DEFAULT IS LOUD FAILURE, NOT FALSE SUCCESS. THE FIX ARGUMENT (owner-endorsed): single-point null-guard inside getDate returning null as the eventual shape — it kills the class (864 sites at once, 305 guarded unaffected) — preconditioned on (1) a column-nullability audit and (2) a consumer audit, because applying it blind converts a loud NPE into a silent NULL write on nullable columns (the false-success family wearing a different coat) and pushes NOT-NULL failures to commit time (the FK-at-commit escape). 864 per-call-site edits is how the pattern drifted in the first place. NOT FIXING — recorded as the argument.

- SCREEN 40 = patientRegistration (3), LIVE class, 2026-08-10 (autonomous run; the OPD subtree's first candidate — swept before entering, ITEM 6). C5/C7/C8 PASS (8/8 clean after a C2 FormsModule catch). Endpoint truth: PatientRegistrationController (api/global); patientSearch handler LIVE (empty-criteria path -> 501-null-message honest-error envelope; root cause owed — generateQueryForPatientSearch all-empty params or a GET_PATIENT_DATA phantom; INNER-JOIN drop shape on appointmentMastersList). Save NOT probed (contract unknown). Branch 251adacb.

- SCREEN 41 = appointmentScheduling (4), LIVE class, 2026-08-10 (autonomous run; the OPD subtree's second build). C5/C7/C8 PASS (8/8 clean). Endpoint truth: AppointmentSchedulingController (api/opd). List LIVE: 200 body 501 'Something went wrong!!' (handler live, list path throws — root cause owed). FINDING: DAO catch (AppointmentSchedulingDaoImpl:872-875) discards the exception into a null-message envelope (the masked-error class on an OPD path) — BR scoped-check. Save NOT probed (contract unknown); latent NPE getAppointmentDate2().substring(0,10) :95. Branch d10f2ae2.

- SCREEN 42 = encounterQueueManagement (6), LIVE class, 2026-08-10 (autonomous run; the OPD subtree's third build). C5/C7/C8 PASS (8/8 clean). All three feeds LIVE with REAL rows (nursing stations, depts [1,2], doctors Demo). Feed-driven read surface — no write-path shape. Branch d174140d.

- SCREEN 43 = appointmentList (7), LIVE class, 2026-08-10 (autonomous run; the OPD subtree's fourth build). C5/C7/C8 PASS (8/8 clean). Shared /api/opd/scheduleAppointmentList endpoint (screen-41 finding; root cause owed once). Read-only list surface. Branch 2e3a0b58.

- SCREEN 44 = patientList (8), LIVE class, 2026-08-10 (autonomous run; the OPD subtree's fifth build). C5/C7/C8 PASS (8/8 clean). GETPATIENTLIST = /api/global/getPatientDetails WORKING (200 success [] — the first OPD list returning success). Search path shares /api/global/patientSearch (screen-40 501 owed there). Read-only surface. Branch f038f6f1.

- SCREEN-40 CORRECTION (2026-08-10, owner's patientRegistration question — RETRACTION + FIX, mechanical cause logged): the 'honest 501' was the PROBE's payload error, NOT a broken endpoint. The service routes on registrationTypeId (PatientRegistrationServiceImpl:730-753) and falls through to `return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null)` (:753) — a null-message 501 envelope — when the routing key is ABSENT. The legacy ALWAYS sets patientObj.registrationTypeId (1 normal / 2 OTC) before searching (patientRegistrationController.js:3265-3270). LIVE-VERIFIED: with registrationTypeId 1 -> 200 success listObject [] (working); without -> the fall-through 501. The rebuild's service now sends registrationTypeId: 1; the probe asserts the working search (button selector fixed — text=Search matched the section heading). The screen is genuinely functional, matching the legacy. Committed 2cbbf1dd.


- SCREEN 45 = opdCoversheet (9), LIVE class, 2026-08-11 (autonomous run; the OPD subtree's SIXTH and final build — the subtree is now fully rebuilt: nodes 3/4/6/7/8/9). C5/C7/C8 PASS (8/8 clean). State REGISTERED app.js:13412; controller OPDCoverSheetController loaded index.html:906; node 9's menu actionLink is MIS-COPIED (#/encounterQueueManagement — node 6's route) but the state is genuinely reachable via $state.go from patientList/queue (the MENU-BROKEN shape, recorded). Endpoint truth by CALLING: /api/global/coversheet/templates (200 success [] for template types 1..7), /api/global/templates/complaints + /api/global/templates/diagnosis (200 success []) — all WORKING dev-empty; /api/global/getActiveButtonMaster BROKEN — PHANTOM COLUMN: the DAO's SQL reads m_unit_master.disclaimer (UserRoleMasterDaoImpl.getActiveButtonMaster), a column the dev schema does NOT have (0 columns match in any spelling); the HibernateException catch masks it into the ERROR/null-code envelope (the phantom-COLUMN class, visit_type_id/role_desc family — this one on a button/action-bar feed). AMBER-permanent banner (banner-tier rule) names the phantom + the owner's verbatim why-permanent framing. The 83 clinical tabs are patient/encounter-scoped — documented not faked (screen-29 precedent). Preflight 8/8, build EXIT 0, probe PASS in-browser. Branch 3fdbff9e.


- OPD SUBTREE FULL SWEEP (2026-08-11, C7 treatment — parent 2, 38 nodes): 6 ALREADY-REBUILT (3/4/6/7/8/9 = screens 40-45, the subtree's LIVE candidates all built) + 1 LIVE-ORPHANED (5 Encounter — state + backend EncounterController api/opd/encounter + encounterEhc, controller loaded, NO menu link) + 31 NO-ACTIONLINK: 25 genuine report/menu-only leaves (Timeliness, Visit Analysis, Top/Bottom Consultants, packages, EHC, no-show, notifiable diseases, appointments variants — zero state registrations, zero controller loads, the parent-7 reports pattern) + 6 STATE-BEARING DANGLE-class candidates (menu node without actionLink but real state + controller + backend): 686 Encounter Queue EHC (encounterQueueEHCController + encounterEhc endpoint), 701 Counter Worklist (counterWorklistController), 702 Nursing Worklist, 703 Self Registration (selfRegistrationController), 744 Growth Chart (growthChart/growthChartMaster), 747 Token Display + 760 Cabin Display (cabinDisplayController). Build decisions for the 6 DANGLE-class + the Encounter orphan are the owner's (prep done, nothing built).
- OPD-SHAPE OBSERVATION (2026-08-11, owner): 25 of 38 OPD nodes are genuine report/menu-only leaves with no state and no controller — a DIFFERENT subtree profile from Billing (the parent-7 reports pattern is 25/38 here, not a small minority). If reports are a large fraction of the remaining tree, the real Track A denominator is smaller again than the screen counts suggest — the screen-count path overstates remaining work wherever this profile repeats. Worth re-auditing other subtrees for the same ratio before committing walk capacity.


- SCREEN 46 — ENCOUNTER (module 5, parent 2 Opd) MERGE-READY (2026-08-11): branch rebuild/add-new-opd-encounter, HEAD d9ce4395, 1 ahead of main. LIVE class — state 'encounter' + sidebar checkAccess(5) -> #/encounter/encounterDetails ('Patient Visit') + EncounterController (api/opd, 81 mappings). CLASS CORRECTION surfaced per the new-class rule: the sweep's LIVE-ORPHANED label for node 5 was a TOOL ARTIFACT (the sweep's nodes_by_link map normalises state names differently) — the node is genuinely menu-reachable, the strongest LIVE shape. Endpoint truth (live calls): POST /api/opd/getEncounterDetails -> 200 success dev-empty with a real patientId; falls through to its 501 envelope with patientId null (the screen-40 lesson — the service routes on required fields), surfaced honestly; GET /api/opd/getListOfEncounterMaster -> 500 under JwtTokenFilter headers / empty body under legacy headers — header-contract artifact, AMBER-permanent banner (verbatim why-permanent framing). Gates: preflight C1/C2/C4/C6/C7/C8/C9 PASS (C3 UNVALIDATED = route on branch only; C5 DEAD-legacy = preflight PascalCase grep vs lowercase state, hand-confirmed by both greps); ng build EXIT 0; probe PASS in real browser (AMBER + mechanism + why-permanent, search round-trip settles on the honest outcome, button resets, 0 page errors). Dry run vs main: CLEAN, 7 files staged (5 feature + route + probe), 0 conflicts, 0 banner files, main's preflight-with-C9 retained. PATTERN NOTE: this screen surfaced the house-pattern rule (signal() + @if/@for, 254/254) — preflight C9 added and the rule recorded in the skill.


- SCREEN 47 — ENCOUNTER QUEUE EHC (module 686, parent 2 Opd) MERGE-READY (2026-08-11): branch rebuild/add-new-opd-encounter-queue-ehc (off the merged main — correct branch shape, route the only tracked delta), HEAD b340d331, 1 ahead of origin/main. DANGLE class (owner's BUILD ALL SEVEN decision): state + controller + template + backend real, menu node 686 without actionLink, the rebuild registers the route. Gates: preflight 9/9 PASS; ng build EXIT 0; probe PASS in real browser (real dev rows NST1/Pending/Test Reason render, slot-search validates honestly, 0 page errors). Dry run vs origin/main: CLEAN, 7 files staged, 0 conflicts, 0 banner files. Diff: 7 files, +613, additive.
- HEADER-CONTRACT SPLIT FINDING (2026-08-11, screen 47 — module-wide, recorded per owner): the two GET feeds `GET /api/global/getListOfNursingStation` and `GET /api/opd/getListOfAppointmentStatusMaster` bind org/unit via the LEGACY `@RequestHeader("unit_id")`/`@RequestHeader("organization_id")` names and 500 under the JwtTokenFilter's userId/unitId headers alone; the sibling POST feeds in the same modules accept the interceptor headers. A header-contract split between endpoints in the same module — ANY screen calling those GET feeds hits the same wall. Mechanism: the controller method signature requires the legacy names (NursingStationServiceController.java:34, EncounterController.java:607), so a JwtTokenFilter-authenticated call with userId/unitId leaves the int @RequestHeader params 0/unbound -> DAO 500. Workaround used by the rebuild: the service adds the legacy pair on exactly those two calls (the interceptor still supplies userId/unitId for auth) — live-verified both directions (both header sets -> 200 + rows; userId/unitId only -> 500). Any future screen calling those feeds must do the same; a backend fix (accepting both header names) would be a BR candidate, not applied.


- SCREEN 48 — SELF REGISTRATION (module 703, parent 2 Opd) MERGE-READY (2026-08-11): branch rebuild/add-new-opd-self-registration (off the merged main), HEAD ac71816a, 1 ahead of origin/main. DANGLE class (owner's BUILD ALL SEVEN decision): state selfRegistration + controller + template (359 lines, 119 ng-bindings) + backend real; menu node 703 without actionLink; the rebuild registers the route. Endpoint truth (live calls): the five ADT reference feeds are GETs bound via the LEGACY unit_id/organization_id header pair (the screen-47 header-contract split class — 500/405 under JwtTokenFilter headers alone): country/{orgId} 200 2 rows (India/IN), gender 200 3 rows, identification 200 4 rows, marital 200 2 rows, prefix 200 1 row (Mr.). Kiosk payment save POST /api/billing/kiosk/saveAndPay exists in BillingMasterController — payment-gated, documented, NOT exercised in dev (nothing written by this surface). Gates: preflight 9/9 PASS; ng build EXIT 0; probe PASS (form renders, feeds show real dev rows India/Mr., 0 page errors). Dry run: CLEAN, 7 files staged, 0 conflicts, 0 banner files. Diff: 7 files, +516, additive.


- SCREEN 49 — COUNTER WORKLIST (module 701, parent 2 Opd) MERGE-READY (2026-08-11): BRANCH NAME: rebuild/add-new-opd-counter-worklist (off the merged main), HEAD a400a8d0, 1 ahead of origin/main. DANGLE class (owner's BUILD ALL SEVEN decision): state counterWorklist + template (187 lines) + controller counterWorklistController (defined with real code but NOT loaded by index.html — legacy script-wiring gap surfaced: the menu advertises the screen yet legacy clicking fails before the backend; the rebuild restores it) + backend real; menu node 701 without actionLink. PHANTOM-COLUMN FINDING (Rule 10, not fixed, AMBER-permanent banner): the core search POST /api/opd/getSearchedListEncounterMaster returns the masked error/null envelope on every call — the named query GET_SEARCH_ENCOUNTERED_LIST selects enMst.latest_call and joins on enMst.nursing_station_id, NEITHER of which exists in opd.t_encounter_master (information_schema verified; all 28 joined tables exist); DAO catch EncounterMasterDaoImpl.java:424 swallows into the masked envelope. Endpoint truth: search -> masked ERROR (phantom columns); GET /api/global/getListOfNursingStation -> 200 real row NST1 (legacy-header pair, screen-47 split class). Gates: preflight 9/9; ng build EXIT 0; probe PASS (banner names the phantom columns + why-permanent, NST1 renders, search surfaces the honest error, 0 page errors). Dry run: CLEAN, 7 files, 0 conflicts, 0 banner files. Diff: 7 files, +510, additive.


- CLASS CORRECTION — 702 NURSING WORKLIST (parent 2 Opd) NOT BUILT (2026-08-11, owner decision: skip for now): the node FAILED the DANGLE-class gate and is reclassified as a STATIC PLACEHOLDER SHELL. Evidence: (1) the template views/master/GlobalMaster/Encounter/nursing-worklist.html (221 lines) has ZERO real Angular bindings — no {{}}, no ng-model/ng-click/ng-repeat/ng-options; the earlier "41 ng-bindings" claim was a REGEX ARTIFACT (the grep matched CSS utility class names ng-binding/ng-label/ng-left-right-set/ng-top, Bootstrap-style classes, not Angular directives); the only Angular directive is one ng-include of a shared static legend (common-index-for-indication-of-status.html, itself 0 bindings — color swatches). (2) The controller is MasterController, the generic catch-all in common-controller.js shared by 331 states in app.js (datepicker defaults + sessionStorage reads, zero screen-specific logic, no API calls). (3) No backend endpoint traces exist for this node. Menu node 702 exists without actionLink and the state is registered — the only DANGLE-shaped attributes. Same tool-normalization artifact family as the Encounter LIVE-ORPHANED mislabel (regex matching class names, not directives). Owner decision: SKIP for now; revisit if the template is ever wired. The empty branch rebuild/add-new-opd-nursing-worklist was discarded.


- SCREEN 50 — GROWTH CHART (module 744, parent 2 Opd) MERGE-READY (2026-08-11): BRANCH NAME: rebuild/add-new-opd-growth-chart (off the merged main), HEAD bd741d47, 1 ahead of origin/main. DANGLE class (owner's BUILD ALL SEVEN decision): state growthChart (with resolve block) + template views/Nursing/growth-chart.html (414 lines, 22 bindings, 14 ng-model, 13 ng-click, 9 ng-if, 5 ng-repeat — genuinely data-bound) + controller growthChartController + backend GrowthChartController (/api/global/growthChart CRUD, growthChartlist GET, growthChart/patient[/details] POST); menu node 744 without actionLink. Endpoint truth (live): POST /api/global/growthChart/patient/details -> 200 real record (growthChartId 15, weightFrom 90.0); GET /api/global/growthChartlist/1/1/0/10 -> 200 1 real row; GET /adt/getActiveGenderList -> 200 3 rows (legacy-header pair). Class-correction backdrop: 702 nursingWorklist surfaced at the class gate (static shell, regex-artifact 41-bindings claim, generic MasterController, no backend) — owner decision SKIP, recorded a4437900. Gates: preflight 9/9; ng build EXIT 0; probe PASS (list feed real row 15, details round-trip, 0 page errors; initial probe FAIL was a probe-assertion bug — CSS-uppercased headers vs mixed-case match — not a screen bug). Dry run: CLEAN, 7 files, 0 conflicts, 0 banner files. Diff: 7 files, +446, additive.


- SCREENS 51 + 52 — TOKEN DISPLAY (module 747) + CABIN DISPLAY (module 760, both parent 2 Opd) MERGE-READY (2026-08-11): BRANCH NAME: rebuild/add-new-opd-token-display (off the merged main; carries BOTH screens — one handover per the chain), HEAD d0f0262d (2 commits: 19b0f5a4 tokenDisplay + d0f0262d cabinDisplay), 2 ahead of origin/main. DANGLE class (owner's BUILD ALL SEVEN decision) for both: state + controller + template + backend real; menu nodes 747/760 without actionLink (747 kiosk-gated isKioskForMenu). CLASS CHECK WITH CORRECTED REGEX (the 702 lesson): tokenDisplay 2 REAL directives + 4 {{}}; cabinDisplay 73 REAL directives + 39 {{}} — both genuinely data-bound, NOT the CSS-class artifact. MODULE IDS: distinct (checkAccess(747) vs checkAccess(760), separate states/controllers/urls) — NOT the SECOND-VIEW shape; C8 clean on both. Endpoint truth (live): tokenDisplay POST /api/opd/token/getTokenList -> 200 dev-empty (requires currentDate), POST /api/opd/token/getTokenMsgList -> 200 dev-empty (requires appointmentTypeId=1). cabinDisplay POST /api/opd/token/getDoctorTokenList -> masked 501 (PHANTOM-COLUMN: named query GET_DOCTOR_TOKEN_LIST EncounterMaster.java:1176 selects em.doctor_latest_call, absent from opd.t_encounter_master; DAO catch EncounterMasterDaoImpl.java:1926; SAME FAMILY as screen 49's enMst.latest_call on the same table — AMBER-permanent banner). Gates: preflight 9/9 both; ng build EXIT 0; probes PASS both (tokenDisplay honest dev-empty + refresh; cabinDisplay banner names doctor_latest_call + why-permanent + honest error; 0 page errors). Dry run: CLEAN, 13 files, 0 conflicts, 0 banner files. Diff: 13 files, +744, additive. With these two, the BUILD ALL SEVEN OPD set is COMPLETE: Encounter (46), 686 (47), 703 (48), 701 (49), 744 (50), 747 (51), 760 (52); 702 nursingWorklist reclassified static-shell and skipped (a4437900).


- ADT SUBTREE SWEEP (parent 3, 2026-08-11) — 48 nodes, ALL report/menu-only leaves, ZERO rebuild candidates: every node is NO-ACTIONLINK; name-map check (validated on parent 2: reproduces exactly the known 6 DANGLE + Encounter + 6 rebuilt) returns 0 state matches of 48; backend PascalCase class-name check returns 0 of 48. The 18 apparent "node-id in state block" references are all incidental (e.g. billing state updatePayeeInformation matched a stray number in node 42's block — no real linkage). The REAL ADT screens (admissionRequest, bedManagement, transferRequest, finalDischarge, cleanedBedAccept, etc. — 100+ adt-flavored states) live under OTHER parents (27 Admission, 45 Patient Acceptance, 65 Transfer, 70 Discharge, 255 Transfer of Care, 261 Discharge, 328 ER, 359 Housekeeping, 362 Mortuary, 401/541 masters) and carry real actionLinks — they are NOT in this subtree. PROFILE: the reports-gallery shape is even more extreme than OPD's 25/38 — ADT parent-3 is 48/48 reports. Confirms the OPD-shape observation (97241189): where report galleries repeat, the real Track A denominator shrinks. Nothing to build in this subtree; not entered.


- DENOMINATOR SWEEP (2026-08-11, tools/denominator_sweep.py — the whole tree in one pass): the role-1 privilege tree is 1,077 flat entries (827 distinct ids — ids repeat across tenant namespaces, e.g. id 1 = 'OPD' AND 'Registration'), 42 roots. CLASSIFICATION of all 1,077: 533 report/menu-only leaves (49.5%), 154 rebuilt (floor — the census is 268; the gap is duplicate-id tree entries + alias-routed dirs like ap-master→accountPayableMaster whose state-name doesn't match the dir kebab), 22 partial (dir|route on main only), 85 shells/decoys, 280 genuine candidates (205 backend-verified + 75 bindings-only), 3 other (menu'd-state-absent/no-backend). BIGGEST SUBTREES: Unit Master 531 (205 nodes, 72→53 candidates), Global Master 369 (168, 61→57), Adt 3 (116 — 48 direct reports + 68 grandchildren incl. the real ADT screens under other parents), Billing 7 (103, 97 reports), LIS 272 (53, 37 candidates), Pharmacy 12 (50), Opd 2 (40, 13 rebuilt). CROSS-CHECK vs the four subtree sweeps: OPD 38→7 and ADT 48→0 reproduce under the name-map (validated on parent 2 first); Billing 48→9 and OTIMS 19→5 reconcile to the candidate bucket with the route-alias caveat. DENOMINATOR READ: 1,077 menu nodes — NOT 1,564. The 1,564 was the Q18 reachability gate's states-registered figure (LIVE 693 / DEAD 160 / MENU'D 316 / unmenued 395), which counts registered states, not menu nodes. Even at the generous end (280 candidates incl. bindings-only) the remaining real screen count is a few hundred, not ~1,340; the report-heavy subtrees (Billing 97/103, Otims 27/28, Quality 38/39, ADT reports gallery) confirm the OPD-shape observation — the true Track A denominator is ~200-300 screens, and the ~1,341 remaining figure is inflated by states/menu nodes that are reports, shells, or duplicates. Q18's LIVE 693 itself carries the token-matching noise noted at the time.


- HEADLINE — THE DENOMINATOR (2026-08-11, owner-requested headline record; FINAL, hardened): **1,077 nodes, 297 candidates, 209 backend-verified (+88 bindings-only), 507 report/menu-only leaves, 157 rebuilt (node-level; directory census 268), 27 partial, 86 shells/decoys, 3 other**. VALIDATION PATH (what makes the number trustworthy rather than merely current): the 18-node sample found an 11% error rate on the reports bucket (2 real screens of 18), which implied ~56 more misclassified hiding in the 511; the acronym-corrected classifier re-run over the FULL bucket found **4 (0.8%)** — the sample's implied residue did NOT materialise, because the acronym fix caught essentially the whole class (RMOAllocationMaster/QMS/opdBill/pendingForPOCreation/currencydenomination forms). The reports bucket is solid. THE PROJECT PLANS AGAINST 297 (209 verified).
  PLANNING FIGURE WAS 1,564 (Q18 reachability gate: states-registered count LIVE 693 / DEAD 160 / MENU'D 316 / unmenued 395).
  REAL NODE COUNT: 1,077 privilege-tree nodes — the old figure counted menu nodes, and roughly half of them are reports.
  REAL REMAINING CANDIDATE SET: 300 at the generous bound, 207 backend-verified (+93 bindings-only), 511 report/menu-only
  leaves, 156 rebuilt (node-level; directory census is 268, decomposition below), 22 partial, 85 shells/decoys, 3 other.
  CAVEAT, stated plainly: the classifier has already been corrected once (the acronym-class fix moved 533→511 reports and
  280→300 candidates, evidence in the hand-check below), and the reports bucket at 511 is the LARGEST remaining claim in
  the count — a deeper validation of that bucket is the natural next check before the number hardens. THIS IS THE NUMBER
  THE PROJECT SHOULD BE PLANNED AGAINST: ~300 (207 verified), not ~1,340. the project's planning figure was **1,564** (the Q18 reachability gate, 2026-08-08: states-registered count LIVE 693 / DEAD 160 / MENU'D 316 / unmenued 395). The whole-tree classification (tools/denominator_sweep.py, validated name-map first) shows the REAL figure: **1,077 privilege-tree nodes, and roughly half of them are reports**. Classification of all 1,077 (CORRECTED 2026-08-11 after the hand-check surfaced the acronym/case class — the classifier now adds the acronym-preserved base and the exact controller-basename check): 511 report/menu-only leaves, 154 rebuilt (node-level floor; the directory-anchored census is 268 — gap fully reconciled and DECOMPOSED: 268 census dirs = 150 represented by rebuilt nodes + 118 unrepresented = 39 alias-routed (route path ≠ dir kebab: ap-master→APmaster, checklist-item-master→checklistItem, blood-bag-type-master→bloodBagType, icpc-mapper→ICPCMapper, display→display/:orgId/...), + 50 route+state present but NO menu node in the tree (duplicate-id entries: 827 distinct ids across 1,077 entries), + 29 route-only rebuilds with NO legacy state (addNewDrugRecall, aiButtonConfiguration, boxMasters — genuinely new screens that never existed as legacy states). The 268 is a directory census; the 156 is a menu-node match; the 112 unrepresented dirs are rebuilt-but-unmatchable-by-node.), 22 partial, 85 shells/decoys, **300 genuine candidates (207 backend-verified + 93 bindings-only)**, 3 other. HAND-CHECK of the 533-report claim (owner-requested validation): 18-node sample from 6 roots (Billing_Finance/Adt/Nursing/Pharmacy/Quality_Indicator/Opd) — strict verdicts (state registration any-case + exact controller basename): **16/18 genuinely reports, 2 miscounted** (node 824 RMO Allocation Master — state RMOAllocationMaster with acronym preserved; node 796 QMS Department Master — exact controller qmsdepartmentmaster) — both the acronym/case class the name-map misses; systematic under-count measured at 23 of 400 report-classified nodes (4 acronym-states: IPConsultationConfiguration, QMS x2, RMOAllocationMaster + 19 exact controller-basename matches — masters/configs, not reports). ERROR RATE: 2/18 = 11% on the sample, ~5.75% systematic on the bucket, all one class (acronym case). CORRECTED PLANNING NUMBER: **~300 remaining candidates at the generous bound, 207 backend-verified** — not ~1,340. At this run's rate (268 rebuilt since the baseline) that is weeks, not years. The old figure counted menu nodes; half are reports. THE PROJECT SHOULD BE PLANNED AGAINST ~300 (207 verified), with the ~23 acronym-case reclassifications added back to the candidate side where they belong.


- DENOMINATOR FULL-BUCKET VALIDATION (2026-08-11, owner-requested — the 511-reports bucket re-run in full, not sampled): the acronym-corrected classifier run over ALL 511 report-classified nodes (not the 18-node sample) finds **13 residual reclassifications (2.5%, NOT the ~56 the 11% sample rate extrapolated)** — all one class, the case-insensitive acronym family: 7 genuine candidates (pendingForPOCreation 685, assignloyaltyCard 752, billList 113, workOrderCreation 194, assetIssue 214, interimHandover 598, shiftClosing 600), 2 already rebuilt (currencydenomination 452, billingBedCategoryMaster 542 — exact-case miss), 1 partial (bloodDonortype 732), 3 shells/other (opdBill 11, returnAcceptance 109, specimanMaster 444 — MasterController/0-bind). An apparent 84 controller-basename flags are NOT residual errors: all 84 have actionLinks with absent states — Q18's MENU'D-UNROUTED class (broken links), a label correction (report->other) not a candidate move; the exact-case ctrl check never runs on them because the actionLink path wins first. FINAL CORRECTED TOTALS after adding the case-insensitive pass to the classifier: **507 reports, 157 rebuilt, 27 partial, 86 shells, 297 candidates (209 backend-verified + 88 bindings-only), 3 other; 1,077 total**. THE NUMBER HARDENS: ~297-300 candidates (209 verified) against 1,077 nodes.
- ACRONYM-CLASS NOTE (2026-08-11, owner-requested): acronym/case handling has now broken THREE tools — the name-map candidate forms (RMOAllocationMaster vs rMOAllocationMaster), the route-match (39 alias-routed dirs: ap-master->APmaster, checklist-item-master->checklistItem, icpc-mapper->ICPCMapper), and the dir-match (route path vs dir kebab). ANY FOURTH TOOL THAT MATCHES NAMES MUST BE CHECKED AGAINST THE SAME LIST BEFORE TRUSTING IT: the forms to test are {first-letter-lowercase, kebab, all-lowercase, acronym-preserved-base, case-insensitive-exact} and the known-breaker names are RMOAllocationMaster, QMS, opdBill, pendingForPOCreation, bloodDonortype, currencydenomination, ICPCMapper, APmaster, ARmaster, OTIMS, ERMarkForDischarge, KITmasterPharmacy, GLcode, IPConsultationConfiguration.


- UNIT MASTER SUBTREE SWEEP (root 531, 2026-08-11, acronym-corrected classifier — the highest-candidate-density subtree, swept before entering per the owner's directive): 204 nodes (26 direct + 178 grandchildren) -> **60 candidates (40 backend-verified + 15 bindings-only + 5 ctrl-basename), 57 rebuilt, 17 partial, 26 shells, 44 reports (module-group labels + 16 state-absent broken links), 0 other**. The acronym correction RAISED this subtree's candidate count 53->60 (recovering IPConsultationConfiguration, RMOAllocationMaster, templateutility, ordersetmaster x2, bloodtestresultmaster, kitmasterpharmacy) — the densest subtree in the tree, ~29% candidate density. SHARED-STATE SHAPE surfaced (new-class rule): the commonMaster family — 6 menu nodes (557 Phlebotomy, 561 Header Master, 564 Department Master, 569 Biochemical Test Master, 645 Equipment Master) all route to the single state 'commonMaster' — a SECOND-VIEW family (one real screen, six menu entries), to be decided as ONE class item, not six builds. Also: 13 name-mapped rebuilt now credited by the acronym fix (QMS, bloodBagCapacity, roleCreation, userAuthentication, cabinMaster, counterMaster, weekendMaster, userStoreMapper, doctorCabinMapper, userCounterMapper, bloodComponent, bloodBankMaster, storeTypeMaster, implantMaster, ambulance family, passTypeMaster, resourceAllocationMaster, discountCategoryMaster). 16 state-absent nodes = Q18 MENU'D-UNROUTED (label, not candidates). NEXT: build the verified candidates in this subtree (40), one screen per the chain, pending owner decision on the commonMaster family.


- COMMONMASTER DISPATCH MECHANISM — ESTABLISHED (2026-08-11, owner decision: build ONCE, map the six menu entries, count once — procedureTemplate treatment; mechanism surfaced before writing per the new-class rule): (a) the state is /commonMaster/:url with a 43-way resolve switch mapping the url param to a controller (phlebotomyMaster->unitPhlebotomyController, headerMaster->headerController, departmentMaster->departmentController, biochemicalTest->biochemicalTestMasterController, ...), injected via controllerProvider into the single shared template common-master-screen.html — the six menu entries are six param values of ONE state. (b) backend discriminates by ENDPOINT PATH, no discriminator field: per-master CRUD under /LIS/global (listPhlebotomyMaster/{orgId}/{offset}/{recordPerPage} GET, listHeaderMaster/{orgId}/{unitId}/{offset}/{recordPerPage} GET, listDepartmentMaster/{orgId}/{offset}/{recordPerPage} GET, ...) — live-verified all three respond 500 'Records not found' (the honest dev-empty state, descriptive per-master message, NOT the masked phantom-column envelope). (c) the rebuild needs the param: route commonMaster/:url, component reads url from ActivatedRoute, service dispatches url->endpoint group mirroring the legacy switch, generic master grid template; a hardcoded single master would be five-sixths broken and pass every gate. PERMISSION NOTE: the six menu nodes are gated by node id (access-gate array checks 394/395/491/447/448/449/798/569/826/426/421/844) — the rebuild's guard accepts the family's node ids, not one. Build once, map six, count once.


- COMMONMASTER SCOPE — ESTABLISHED (2026-08-11, owner-requested scope question answered before build; the sweep counts NODES not masters, so the 43-way dispatch had to be enumerated directly): (a) **38 of 43 dispatch branches have real menu nodes** across FOUR roots — 411 LIS (sampleMaster 412, containerMaster 413, techinqueMaster 414, prerequisitesMaster 415, unitMaster 417, reagentMaster 419, reportTypeMaster 420, bacteriaShapMaster 421, wtifilmStudyMasterMaster 423, mediaMaster 424, incubationTestMaster 425, colonyMaster 427, bacteriaClassificationMaster 428, antibioticMaster 430, antibioticsGroupMaster 431, antibioticsMaster 429, organismMaster 435, organismGroupMaster 436, stainigMaster 437, sensitivityTypeResultMaster 438, blockMaster 439, slideProcedureMaster 440), 380 ADT-master group (sexmaster 389, relationmaster 390, maritalstatus 391, religion 392, occupation 393, patientsource 394, referraltype 395), 445 (consultantType 450, paymentMode 448, paymentEntitment 449, visitType 447), 556 Unit-Master LIS (phlebotomyMaster 557, headerMaster 561, departmentMaster 564, biochemicalTest 569), 490 (priorityMaster 491 + discount 492); 5 branches menuless (languageMaster, microbiologyStainMaster, scheduleMaster, serologyTestFindingMaster, storageLocationMaster). (b) **backend live-verified by calling across all four families**: LIS GET /LIS/global/listAntibiotics/{org}/{off}/{rec} -> 500 'Sample Records not found' (live honest dev-empty); COMMON POST /api/common/getPriorityMasterList -> 200 success real row (priorityId 0); ADT POST /adt/getRelationMasterList + /adt/getMaritalStatusMasterList (payload {organizationId, offset, noOfRecordsPerPage} — NOT unitId) -> 200 with REAL rows (relation: Cousin/ABC123; marital: Widowed); all six Java probes resolve to real controllers. (c) **the other 37 are INVISIBLE to the sweep as separate screens** — the sweep counted the ONE commonMaster state per menu node, NOT the masters: 43 masters hide behind one state. THE DENOMINATOR IMPLICATION: the 297-candidate count under-counts by up to ~37 (the masters behind commonMaster are real screens, each with its own menu node, its own controller, its own endpoints — not counted separately). BUT the menu nodes DO exist in the tree (38 of them), so those nodes ARE in the candidate count as individual nodes — what the sweep MISSED is the 5 menuless branches + the fact that each node is a distinct screen. Net correction: the commonMaster family is ~43 real screens (38 menu'd + 5 menuless), NOT 1 state. The count of candidates should add ~37 (the masters not credited as their own screens). SCOPE DECISION for the build: this is NOT a 6-branch screen — it is the full 43-way dispatcher. Build the parameterized commonMaster/:url with the 43-branch dispatch map (38 menu'd + 5 menuless), one shared template, per-master service endpoints; count the family as its members per the sweep-correction.


- DENOMINATOR CORRECTION — COMMONMASTER DISPATCHER EXPANDED (2026-08-11, owner-requested count fix; the result is the OPPOSITE of the expected direction, which is the finding): the sweep counts NODES, and all 41 commonMaster menu nodes were ALREADY classified candidate — the family was NOT invisible, it was slightly OVER-counted. CORRECTED CANDIDATE TOTAL: **297 -> 294** (family 41 nodes -> 38 masters: -2 broken links #/commonMaster/ with empty value (nodes 460 Schedule Master, 645 Equipment Master — Q18 MENU'D-UNROUTED, not dispatch branches) and priorityMaster's 2 menu nodes (491, 492) merge to 1 master). BUT the verified split MOVES UP: **209 -> 247 backend-verified** (the 38 menu'd masters are live-verified across all four backend families — LIS GET /LIS/global dev-empty, COMMON POST /api/common/getPriorityMasterList real row, ADT POST /adt/getRelationMasterList + /getMaritalStatusMasterList real rows Cousin/Widowed, billing/master families), while the sweep had them as bindings-only (backend=N because the state is 'commonMaster' with controllerProvider, no CommonMasterController.java). Bindings-only: 88 -> 47. New split: **294 candidates = 247 verified + 47 bindings-only**. Other buckets unchanged (507 reports / 157 rebuilt / 27 partial / 86 shells / 3 other; 1,077 nodes).
- DISPATCHER-CLASS SEARCH (owner-requested: is commonMaster the ONLY shared-state dispatcher?): YES — a broad scan of all states for controllerProvider / templateProvider / resolve-switching-on-$stateParams found exactly ONE: commonMaster (43 branches). 13 other states have :param urls (pendingListForAcceptance, worklistForDepartment, nursingDisplay, triageDisplay, cabinDisplay, consultantDisplay, display, etc.) but all pass the param to a FIXED controller (resolve returns orgId/unitId/nursingStationId, no controller switch) — correctly rebuilt as single screens. Shared-state check: commonMaster is the only state with >= 5 menu nodes (41). The dispatcher class is unique; no other hidden families affect the count.
- THE 5 MENULESS BRANCHES (real controllers + endpoints, NO menu node — invisible to any node-based count): microbiologyStainMaster, serologyTestFindingMaster, storageLocationMaster, languageMaster, scheduleMaster. They are real screens without menu entries (Q18 registered-unmenued class); they do not add to the candidate count per the owner's rule (menu node + live backend) but are noted as latent screens.
- BUILD-SHAPE RECOMMENDATION (owner decides; nothing built): recommend option (b) — the dispatcher screen + the 4 Unit Master branches (phlebotomyMaster, headerMaster, departmentMaster, biochemicalTest) proven end-to-end first, then the remaining 34 branches in family batches (LIS 25, ADT 7, billing/common 5, master 1). Trade-offs: (a) one-pass 43-branch build is the largest single artifact of the project — wrong in 43 places if the dispatch core is wrong, no incremental proof; (b) proves the dispatcher core + one family end-to-end cheaply, de-risks the pattern before mass production, rest is mechanical branch addition; (c) 43 separate screens sharing a component duplicates the permission gate and per-master boilerplate 43x, and contradicts the 'count once' decision. Recommendation: (b).


## DQ-2026-08-12-001 — RETRACTION: the "guard fail-open" finding is withdrawn; the guard denies correctly (verified 3/3 under a race-free harness)

**STATUS: RETRACTED by evidence — no code change made. The owner-approved fix
(navigateByUrl + return false) was correctly NOT implemented because the
evidence reversed BEFORE it landed.**

What was claimed (2026-08-11): module-access.guard.ts fails OPEN — it evaluates
deny, returns the UrlTree, and the redirect is silently dropped because
accessDenied sits outside the Shell; 257 guarded routes effectively unguarded
on direct navigation; reproduced on commonMaster, growthChart, cabinDisplay.

What the record now shows: the guard denies correctly on the CLEAN, unmodified
code. The reversal came from re-running the ATTACK (attack-first, BR shape) on
the unfixed code — the attack could not be demonstrated, which is the property
that makes the reversal trustworthy: it is not "the fix appears to work", it is
"the defect does not reproduce on the code as it stands".

Evidence (all against the clean unfixed guard, permissions=[999999] non-family
id, deep-link):
- settle_guard.py dual probe, 3/3 routes (commonMaster/relationmaster family
  gate, growthChart 744, serviceMaster 376 — including the long-merged screen):
  location poll lands on /accessDenied at ~0.26s, history REPLACE
  /accessDenied?reason=denied fires, the access-denied CHUNK IS REQUESTED
  (listener captures /@ng/component?c=...access-denied.ts...AccessDenied),
  <app-access-denied> mounts with reason=denied, the guarded component NEVER
  renders, shell absent (accessDenied is top-level by design), 0 page errors.
- 4/4 repeat runs in ~0.3s (warm dev server); first-run latency ~8.5s was the
  dev-server lazy-chunk compile, which is what the ORIGINAL fixed-6s-sleep
  probes misread.

Point-by-point reconciliation of the five old evidence items:

(a) "URL never moved — 0 of 3, 20s flat timeline": those runs asserted
    `page.url` at a fixed instant. `page.url` lags `location.href` during the
    in-app (SPA) redirect; the redirect had landed at the component+URL level
    while page.url still reported the pre-navigation URL. A 20s timeline run
    (debug_guard11) sampled page.url only — the SPA redirect does not
    re-navigate the document, so the sample saw nothing change even though the
    router had replaced the URL. Measurement artifact.

(b) "accessDenied chunk NEVER requested (request-listener probe)" — the one
    piece a slow redirect could not explain. The listener filter
    (`"access" in url.lower()`) WOULD have matched the chunk URL
    (/@ng/component?c=...access-denied...), so the chunk genuinely was not
    requested in that run. The artifact: that run (debug_guard12) executed
    during the debug window when the served bundle was STALE — the ng serve
    process log carries the compile error `TS2345 ... module-access.guard.ts:58`
    (`moduleIds ?? [moduleId]` union producing `(number|undefined)[]`), and ng
    serve serves the LAST-GOOD bundle while the current source fails to
    compile. The transcript's own next step recorded "The served bundle is
    stale from before, which explains the inconsistent behavior." So the
    request-listener was sound; the code it listened to was not the code under
    test. Re-run of the EXACT old harness (reconcile_old12.py: two-goto,
    evaluate-based permissions, no interception, same filter) against the
    clean bundle: chunk requested, poll lands in 0.26s. Artifact named: stale
    served bundle from the broken HMR compile during the debug window.
    Residual honesty: the exact byte-state of the bundle served at that
    instant is not reconstructible; what is proven is (i) the harness/listener
    are not the cause, (ii) the window contained a documented compile failure
    and stale-bundle servicing, (iii) clean-bundle runs are consistent 3/3.

(c) "UrlTree to a Shell child (billingGroupMaster) DID redirect and loop; a
    top-level target does not": the billingGroupMaster loop test ran with NO
    cookie (notLoggedIn path) — billingGroupMaster is itself guarded, so the
    redirect looped. It was never a clean top-level-vs-child contrast, and the
    clean bundle redirects to the TOP-LEVEL accessDenied fine. The contrast
    was an artifact of the notLoggedIn test setup, not the route tree.

(d) "return false cancelled and reverted correctly": consistent with Angular
    semantics and not evidence of fail-open; the clean guard's parseUrl return
    redirects (the redirect does not depend on the return value's shape).

(e) "router.url was / at guard time on a deep-link": true and expected — the
    guard runs during the initial navigation before the URL commits; it does
    not indicate a dropped redirect (the redirect lands afterward).

serviceMaster (Q2 — direct answer): the ORIGINAL attack was NEVER run on
serviceMaster. The original reproductions were commonMaster/relationmaster
(probe negative control) and growthChart 744 (debug_guard13) — plus a
cabinDisplay 760 debug run in the contaminated window. serviceMaster, the
long-merged screen the owner's three-route requirement was written to cover,
was first exercised under the reversal (settle_guard), where it denies
correctly. The original finding was therefore INCOMPLETE as well as wrong — it
never met the owner's own three-route bar.

Why the fix was not implemented: the owner approved navigateByUrl + return
false on the fail-open report. Before implementing, the attack was re-run on
the unfixed code per the BR shape; it could not be demonstrated. Per the
sequence's own condition (attack on unfixed code first, three routes), the fix
must not land when the attack half fails. No change to a working security
control. (The navigateByUrl + return false shape was itself never proven to
redirect; earlier contaminated runs suggested it might not — do not implement
it without a fresh attack/defence demonstration on the clean bundle.)

Lesson recorded: the positive-only probe convention did NOT cause this false
verdict (a negative control DID run); what caused it was (1) asserting page.url
instead of the real location, (2) a fixed sleep vs variable dev-server chunk
compile latency, and (3) diagnosing against a bundle that a broken HMR compile
had frozen at last-good. Probe discipline for guard deny paths is now in the
angular-rebuild-verification skill reference: poll location.pathname up to 15s,
assert the DOM (component mounts / guarded content absent), and confirm the
served bundle contains the expected guard markers before trusting any verdict.

## DQ-2026-08-12-002 — COMMONMASTER GATE-DRIFT CHECK + ADT 7 BATCH (owner orders 2026-08-12)

**GATE-DRIFT CHECK (owner order 1):** the route's 45-id moduleIds array is a
COPIED CONSTANT from the legacy access gate (app.js:3908-3916, the commonMaster
resolve `data` filter `item.id == ...` chain). Nothing detected drift. Built
`tools/check_commonmaster_gate.py` — programmatically diffs the two lists (set
equality both directions + duplicate check, window bounded at the chain's `);`
terminator so the NEXT state's filter (item.id == 396) cannot bleed in).
Result: EXACT MATCH — 45/45, 0 only-in-legacy, 0 only-in-route, 0 dups.
NEGATIVE CONTROL demonstrated: removed id 844 from the route array -> check
FAILED (RC=1, "only in legacy: [844]"); restored -> PASS (RC=0). The check is
the preflight gate-drift step for every commonMaster-family screen: the
route's moduleIds must match the legacy access-gate array programmatically,
never by eye.

**ADT 7 BATCH (owner order 3 — ADT first, not LIS):** the ADT family is root
380 = sexmaster 389, relationmaster 390, maritalstatus 391, religion 392,
occupation 393, patientsource 394, referraltype 395. relationmaster was the
slice's live-data branch; the other 6 are now built. ALL are POST /adt/<ep>
with body {organizationId, offset, noOfRecordsPerPage} — live-verified
2026-08-12, EVERY one returns real rows (gender 3, marital 3 incl. Widowed,
religion 2, occupation 2, patientSource 6, referraltype 5). Added to
CommonMasterService.DISPATCH; probe extended with per-branch real-row
assertions; PROBE PASS (all 6 render real rows, negative control still denies,
0 page errors); ng build EXIT 0. Remaining after ADT: LIS 25, billing/common 5,
master 1. Next batch reported before the following one per owner directive.

## DQ-2026-08-12-003 — LIS FAMILY: 16 of 26 branches return the GENERIC "Internal Server Error" envelope — the unreachable-empty-branch fall-through (masked-error class, owner's LIS watch-item CONFIRMED)

Owner's directive before the LIS 25 batch: the three LIS/global masters in the
slice each returned descriptive per-master "Records not found." messages —
"confirm that holds across the family, or flag any that return the generic
envelope instead; a generic one hiding among descriptive ones is the
masked-error class."

CONFIRMED AS A REAL CLASS: 16 of 26 LIS branches return `{"status":"error",
"code":"500","message":"Internal Server Error"}` — the generic envelope —
while 10 return descriptive per-master messages ("Sample Records not found.",
"Container Records not found.", etc.). NOT a phantom-column/exception swallow
(the screens-49/52 class): the tables exist with all query columns (verified
lab.m_reagent_master etc., information_schema), the DAO returns SUCCESS with
an empty list, and the SERVICE layer swallows it.

THE MECHANISM (named, verified in source): the LIS service impls use the shape
`Response res = dao.list...(...); if (res.getListObject() != null &&
res.getListObject().size() > 0) { ... if(unitMst.isEmpty()) { return ...NOT_FOUND; }
else { return SUCCESS; } } catch { throw; } return new Response(ERROR, ERR_500,
ERR_500_MESSAGE, null, null);` — ReagentMasterServiceImpl.java:103-121,
ColonyMasterServiceImpl.java:103-121, OrganismMasterServiceImpl, WtifilmMaster-
ServiceImpl (line 123), etc. The `size() > 0` guard makes the inner
`isEmpty()` branch UNREACHABLE for an empty list: empty list skips the whole
block and falls through to the generic `ERR_500_MESSAGE` ("Internal Server
Error"). The descriptive per-master message EXISTS in the code but can never
fire. (Secondary copy-paste defect: line 112 returns SAMPLE_RECORDS_NOT_FOUND
for reagent/colony — the wrong constant name, dead anyway.)

CLASSIFICATION (26 LIS branches):
- DESCRIPTIVE dev-empty (10): sampleMaster, containerMaster, techinqueMaster,
  prerequisitesMaster, mediaMaster, antibioticMaster, antibioticsGroupMaster,
  storageLocationMaster, languageMaster, serologyTestFindingMaster.
- GENERIC "Internal Server Error" (16): reagentMaster, reportTypeMaster,
  bacteriaShapMaster, wtifilmStudyMasterMaster, colonyMaster, organismMaster,
  organismGroupMaster, stainigMaster, microbiologyStainMaster, antibiotic-
  sMaster, incubationTestMaster, sensitivityTypeResultMaster, slideProcedure-
  Master, bacteriaClassificationMaster, unitMaster, blockMaster.

REBUILD HANDLING: the generic 16 get an AMBER-permanent banner per the
BANNER-TIER RULE — the response the warning would read is the thing that lies
(empty reported as a server error), so the banner must say WHY it is permanent
and name the fall-through mechanism. The descriptive 10 surface the backend's
own message as honest dev-empty.

BACKEND NOTE (not fixed — reported per Rule 10): the fall-through is a
service-layer defect — the `size() > 0` guard should be `>= 0` or the empty
branch should be outside the guard. Fixing it changes observable behavior (a
message string), so it needs a BR entry + owner approval, not an inline fix.
The dead SAMPLE_RECORDS_NOT_FOUND constant (wrong master) is the same class.

## DQ-2026-08-12-004 — FINAL BATCH (billing/common 5 + master 1): dispatcher now 43/43 — 5 real-data branches, 1 noData stub, ZERO generic envelopes in this batch; proxy gap found + fixed

Owner directive: "check each endpoint's envelope shape and split descriptive
from generic rather than writing all the 500s off as dev-empty."

RESULT: this batch has ZERO 500s and ZERO generic envelopes — all five
endpoints with data return SUCCESS with REAL rows (live-verified 2026-08-12):

| branch | method+path | live result |
|---|---|---|
| priorityMaster | POST /api/common/getPriorityMasterList | success, 3 rows |
| visitType | POST /api/global/visitTypeMasterList | success, 7 rows |
| paymentMode | POST /api/billing/getPaymentModeMasterList | success, 8 rows |
| paymentEntitment | POST /global/getPaymententitlementList | success, 3 rows (class-level @RequestMapping("/global"), NOT /api/global — legacy GLOBAL constant; live-verified both variants, /api 404s) |
| scheduleMaster | GET /Pharmacy/global/listScheduleMaster/{org}/{unit}/{off}/{sz} | success, 1 row |

consultantType (node 450): NO backend list endpoint EXISTS — the legacy
controller is a 48-line stub (no GET constant, no serviceAction, no Java
@RequestMapping list method anywhere in legacy/; a guessed endpoint verified
404). Dispatched with noData:true (hasMaster()=true, it IS a real menu'd
branch) but the component renders the honest no-data state, mirroring legacy
where the stub never populates commonList either.

PROXY GAP found during the live probe: scheduleMaster's real-data grid did not
render in the app even though the endpoint works directly — the dev-server
proxy (new/proxy.conf.json) had NO /Pharmacy/** route, so the app's
same-origin call to /Pharmacy/global/listScheduleMaster was answered with the
SPA index.html (parse failure). Added /Pharmacy/** -> localhost:8080. Same
class as the screen-47 header-contract split (a dev-harness route gap, not a
backend defect). NOTE: proxy.conf.json is gitignored (dev-only bridge) — the
branch carries the fix in the working tree; it does not appear in the merge
diff.

## DQ-2026-08-12-005 — TWO CLASSES ADDED TO THE CLOSE-OUT MODEL (owner framing)

1) NO-ENDPOINT (noData) is a DISTINCT outcome, not dev-empty: consultantType
has no list endpoint at all in the backend (stub controller). In the
43-branch close-out it counts as its own row: NOT real data, NOT honest
dev-empty, NOT lie-class. Other branches with no list endpoint, if any,
count the same way. (Live probe confirms the noData state renders.)

2) PROXY GAP = the FOURTH cause class of an apparently-failing screen
(owner: "a branch that looked broken because a proxy route was missing, not
because the backend was"). The four, named:
   - phantom columns / missing DB structures (screens 49/52 class)
   - masked error envelopes (LIS fall-through, DQ-2026-08-12-003)
   - header-contract splits (screen 47 class)
   - PROXY ROUTE GAPS (NEW — scheduleMaster /Pharmacy/** this batch: the
     endpoint worked directly on 8080 but the app's same-origin call was
     answered with the SPA index.html because new/proxy.conf.json had no
     /Pharmacy/** route; added. proxy.conf.json is gitignored (dev-harness
     bridge), so the fix lives in the working tree, not the merge diff.)

When a screen looks broken in the dev harness: check the proxy route BEFORE
classifying it as a backend defect.

## DQ-2026-08-12-006 — COUNT CONFIRMATION + CORRECTED REMAINING AFTER COMMONMASTER (owner-requested, post-merge 3aa17177)

(a) DIRECTORY-ANCHORED COUNT: commonMaster increments the rebuilt-directory
census by ONE, not 43 — it is one feature directory (new/src/app/features/
common-master/) dispatching 43 masters internally. Census was 268 (DQ-336,
2026-08-11); common-master is the ONLY feature dir added since the anchor
(verified via git diff 128e509e..origin/main). CURRENT FIGURE: **269 rebuilt
feature directories**. EXCLUSION SET: 4 non-screen utility dirs excluded from
the census (_decoy-groundwork, _recheck-20-bucket, _screen-template,
access-denied); the 5 menuless commonMaster branches (languageMaster,
microbiologyStainMaster, scheduleMaster, serologyTestFindingMaster,
storageLocationMaster) stay latent and uncounted.

(b) CORRECTED REMAINING CANDIDATES: **256 (209 backend-verified + 47
bindings-only)**. The owner's cited "209 backend-verified" is exactly
247 - 38, confirming the corrected-record frame: DQ-356 (2026-08-11) fixed the
denominator at 294 candidates = 247 verified + 47 bindings-only (the 297 was
the pre-correction DQ-343 headline; the correction moved the 38 menu'd
commonMaster masters from bindings-only into verified after live-verification
across all four backend families). Now that the dispatcher is built, those 38
menu'd masters are covered: 294 - 38 = 256 (209 verified + 47 bindings-only).

(c) NEXT SUBTREE: Unit Master 531 (205 nodes) — raw sweep 55 candidates
(40 verified + 15 bindings-only); the 4 #commonMaster partials (557/561/564/
569) are the slice masters now covered by the dispatcher, so they are not
counted as unbuilt. 44 report, 57 rebuilt, 22 partial (18 genuinely partial
after the 4 covered), 27 shell, 0 other.

## DQ-2026-08-12-007 — UNIT MASTER 531: DISPATCHER SCAN RESULT + FAMILY BATCH PLAN (owner's pre-build checks 1 and 2)

CHECK 1 — SECOND DISPATCHER? NO. All 40 verified candidates examined for
shared controllers/states (the accessControl/accessControl1,
doctorConsulation/IPConsulation/OPConsulation, patientType/patientTypeDetails,
fundApproval/fundRequisition/fundApprovedList clusters the owner flagged):
- 38 of 40 have UNIQUE controllers (one state each) — no shared state, no
  resolve-switching, no dispatcher.
- accessControl + accessControl1: SAME controller (accessControlController,
  resources/assets/accessControl/controller/ — a 60-line STUB: no serviceAction,
  no endpoint, just angular-ui-tree on an empty $scope.data) with two DIFFERENT
  templates (views/access-control.html 9974B vs views/common/access-control.html
  9059B). Two states, shared stub controller -> likely both reduce to noData.
- fund trio (fundApproval/fundRequisition/fundApprovedList): all MasterController
  (common-controller.js generic scaffold; never populates the templates' bound
  genderList/identificationList) -> no data path, noData class. fundReconciliation
  is DISTINCT (fundInflowController).
- SlotBlockUnblockControllerScheduleWise/DoctorWise (scheduleWise 657, doctorWise
  658): distinct controllers (1047/828 lines) but SAME endpoint set — build as a
  pair, one investigation.
- templateutility (667): NO state in app.js (ctrl-basename only) — needs
  investigation before build.

CHECK 2 — BATCH BY BACKEND FAMILY (one investigation per family, not per
screen). Families of the 40 verified:
- billing/unit (6): doctorConsulationMaster, doctorWiseShare,
  doctorReferralShareMaster, patientType, patientTypeDetails,
  pharmacyTariffMaster
- lis/unit+global (6): histopathologyTest, parameterMaster, parameterFormula,
  profilepanelTestMaster, storageLocationMstForSlideBlocks, ambulanceCharges
- bloodBank (5): bloodTest, reasonOfDiscard, screeningTest, donorRejection,
  bloodGroupMatching
- master/unit (6): templateICDCodeMapping, loyaltyCardCreation,
  loyaltyCardConfiguration, autoServicePostingConfiguration, passBedCategoryMapper,
  doctorRegistration
- master/global (2): IPConsultationConfiguration, userNursingStationMapper
- opd/unit (3): appointmentSchedulingUnitMaster, scheduleWise, doctorWise
- services (3): ehcPackage, packageWithCAP, eitherOrPackage
- CSSD (2): wrapperExpiryMaster, kitCreation
- clinical (2): rmoAllocationMaster, growthChartMaster
- adt/unit (1): nursingStationWardMapping
- billing/global (1): fundReconciliation (fundInflowController)
- no-state (1): templateutility
- accessControl pair (2): accessControl, accessControl1 (stub — noData class)
Order: billing/unit first (largest, likely real data), then lis, bloodBank,
master, opd, services, cssd, clinical, adt, billing/global, no-state.

## DQ-2026-08-12-008 — UNIT MASTER 531 CANDIDATE RECONCILIATION: 4 of the "40 verified" are ALREADY REBUILT (alias-routed class); billing/unit batch = 3 screens not 6

Pre-build reconciliation of the 40 "verified" candidates against origin/main
routes (moduleId-anchored, then tree-id-verified):
- doctorConsulationMaster (612) -> rebuilt route 'doctorConsultation' (dir
  doctor-consultation) — REAL (tree id 612 unique, name ' Doctor Consulation')
- doctorWiseShare (614) -> 'doctorwiseShare' (doctorwise-share) — REAL
- doctorReferralShareMaster (615) -> 'doctorReferralShare' (doctor-referral-share) — REAL
- accessControl (677) -> 'newAccessControl' (new-access-control) — REAL
  (the legacy state accessControl was rebuilt as newAccessControl; the
  accessControl1 state 673 remains unbuilt)
- autoServicePostingConfiguration (775): FALSE POSITIVE — the rebuilt
  'couponTemplateMaster' also carries moduleId 775, but the privilege-tree
  node 775 is 'Auto Service Posting Configuration' (dup-id artifact: ids
  repeat across tenant namespaces per the sweep) — 775 remains UNBUILT.

NET: genuinely unbuilt from the 40 = 36 (not 40). The sweep's verified bucket
carried 4 alias-routed false candidates (state name != dir kebab: the
Consulation typo, doctorwiseShare camel, newAccessControl rename). Same class
as the recorded 39 alias-routed dirs (DQ-336).

BILLING/UNIT BATCH = 3 SCREENS (not 6): patientType (764), patientTypeDetails
(765), pharmacyTariffMaster (803). All live-verified: GET /api/billing/
getPatientType/1/1 -> SUCCESS 3 real rows (DB+Cache); GET /api/units/pharmacy/
tariff/getAll/1/1 -> SUCCESS 3 real rows. THIRD envelope shape (stores/
listObject2/resObject fields) — distinct from commonMaster's; zero generic
envelopes. The 3 doctor/share screens are already rebuilt -> excluded.

## DQ-2026-08-12-009 — UNIT MASTER 531 LIS FAMILY (batch 2): classification BEFORE build + phantom-column finding (NEW masked envelope: correlation-id "Something went wrong. Reference: ERR-...")

Owner's pre-build checks applied to the LIS six (histopathologyTest 559,
parameterMaster 562, parameterFormula 566, profilepanelTestMaster 567,
storageLocationMstForSlideBlocks 568, ambulanceCharges 819):

SHARED COMPONENT/STATE CHECK: NONE — all six have UNIQUE controllers and
UNIQUE templates (verified in app.js state blocks). The commonMaster
dispatcher pattern does NOT repeat in this family.

ENVELOPE CLASSIFICATION (live-verified 2026-08-12, body status inspected):
- histopathologyTest: DESCRIPTIVE "Test not found." — honest dev-empty
  (correct path is 7 segments: /LIS/unit/listTestMaster/{org}/{histo}/{unit}/
  {off}/{size}/{searchKey})
- parameterFormula: DESCRIPTIVE "Formula Records Not Found." — honest dev-empty
- profilepanelTestMaster: DESCRIPTIVE "Panel Test Records Not Found." —
  honest dev-empty
- storageLocationMstForSlideBlocks: DESCRIPTIVE "Rack Records not found." —
  honest dev-empty
- parameterMaster: MASKED-ERR-REF "Something went wrong. Reference:
  ERR-<uuid8>" — NOT dev-empty. ROOT CAUSE: PHANTOM COLUMN — the
  GET_PAGINATED_PARAMETER_MASTER_LIST named query selects
  parameterMaster.techniqueId (ParameterMaster.java:131, property declared
  :289) but lab.m_parameter_master has NO technique_id column (verified
  information_schema, 0 rows). The screens-49/52 phantom-column class,
  surfacing via a NEW masked envelope: ErrorCorrelationUtil (Global utility)
  wraps the real exception in "Something went wrong. Reference: ERR-..." with
  a correlation id — distinct from the LIS fall-through's plain "Internal
  Server Error" (DQ-2026-08-12-003). The other query columns all exist.
  Backend fix NOT made (Rule 10 — behavioral change; BR candidate).
- ambulanceCharges: NOT A MASTER — a 1581-line charges-capture transaction
  screen (patient search + tariff list + bill data; the GETAmbulanceMaster-
  DataList call at ctrl:213 is COMMENTED OUT). Classification correction:
  the sweep's "verified" bucket included a transaction screen; it does NOT
  fit the master-grid rebuild pattern. Flagged for the walk's
  classification, not built as a master.

REBUILD HANDLING: 4 descriptive dev-empty screens build as master grids
surfacing the backend message; parameterMaster builds with an AMBER-permanent
masked-error banner naming the phantom column (technique_id) + correlation
envelope (DQ-2026-08-12-009); ambulanceCharges deferred (transaction class).

## DQ-2026-08-12-010 — UNIT MASTER 531 BLOOD BANK FAMILY (batch 3): classification BEFORE build

Owner's pre-build checks applied to the blood bank five (bloodTest 734,
reasonOfDiscard 737, screeningTest 738, donorRejectionMaster 740,
bloodGroupMatchingMaster 741):

SHARED COMPONENT/STATE CHECK: NONE — all five have UNIQUE controllers and
UNIQUE templates (verified in app.js state blocks; controllers under
resources/assets/js/controller/bloodBank/). No dispatcher pattern.

ENVELOPE CLASSIFICATION (live-verified 2026-08-12, body status inspected):
- bloodTest: SUCCESS empty listObject — honest dev-empty (POST
  /global/bloodBank/getBloodTestMasterList {organizationId, unitId, offset,
  noOfRecordsPerPage})
- reasonOfDiscard: SUCCESS empty — honest dev-empty (POST /bloodBank/
  getReasonOfDiscardMasterList)
- screeningTest: SUCCESS empty — honest dev-empty (POST /bloodBank/
  getScreeningTestMasterList)
- donorRejectionMaster: SUCCESS empty — honest dev-empty (POST /bloodBank/
  getDonorRejectionMasterList)
- bloodGroupMatchingMaster: SUCCESS 3 REAL ROWS (GET /global/bloodBank/
  getActiveBloodGroupMaster/{org}/{unit}) — real data, not dev-empty.

ZERO generic envelopes, ZERO masked errors in this family (no correlation
envelope, no fall-through, no phantom column). The four master-list screens
render empty grids honestly; bloodGroupMatching renders its real rows.

## DQ-2026-08-12-011 — DELEGATION AS A KNOWN PROCESS (owner-requested record: how subagent-written code works, and the verification obligation)

Asked: batch 2's four of five features were written by a subagent — state how
that works, what the subagent receives/returns, and which gates run on its
output vs mine. The arrangement is now a DOCUMENTED process, not an
undiscussed one.

WHAT THE SUBAGENT RECEIVES (the brief, in full context):
- The exact repo path, branch, and the reference feature directory to mirror
  (batch-1 patient-type-master — the house pattern in concrete form).
- Per-feature specs: route path, moduleId, title, component class name, the
  exact list endpoint (method + path + payload), the legacy controller line
  refs that prove the endpoint, and the live envelope classification
  (descriptive dev-empty / honest empty / real data — so it never guesses
  banner behavior).
- The non-negotiable house rules restated: signal()+@if/@for ONLY (zero
  *ngIf/*ngFor), API_BASE_URL inject()ed, no manual HttpHeaders,
  SessionService getters as properties, NotificationService success()/error(),
  StatusToggle [checked]+(toggled), strict-template gotchas (no Number() in
  templates).

WHAT IT RETURNS: a self-report of files created + tsc/ng-build results +
deviations. SELF-REPORTS ARE NOT EVIDENCE — they are the subagent's claims.

WHICH GATES RUN ON SUBAGENT OUTPUT vs MY OUTPUT (the verification
obligation, explicit):
- The SAME gates run on BOTH. Subagent work is not trusted on its report:
  1. tsc --noEmit (whole project) — re-run by me after the subagent finishes.
  2. ng build (owner-approved; the only gate that compiles templates) — re-run
     by me.
  3. LIVE browser probe (freshness gate first, real assertions per screen) —
     run by me against the served bundle.
  4. Route/moduleId/C9 checks (grep the routes file + templates) — by me.
  5. Branch commit + merge-readiness dry run — by me.
- My own hand-written features (parameterMaster in batch 2, bloodGroup-
  MatchingMaster in batch 3) go through the identical gates. There is no
  "subagent tier" of verification — the obligation is the same for every
  line that lands.
- Independent verification is MANDATORY before claiming a subagent-written
  batch green: the subagent's tsc/build run may have raced the dev server,
  its route insert may have conflicted with mine (it did once — the sibling
  worktree warning), and its "verified" claims are unobserved. The parent
  re-runs everything and reconciles.

BOUNDARY: subagents never commit, never touch main/governance docs, never
run the probes' freshness gate decisions — the parent does. Subagent scope is
file-writing in a feature directory + its own tsc/build self-check.

## DQ-2026-08-12-012 — THREE ZERO-MASKED BATCHES: GENUINE BACKEND SHAPE, NOT PROBE CONVERGENCE (owner question)

Asked: three batches with zero masked envelopes — genuinely cleaner families,
or probes converging on what they expect?

ANSWER: GENUINE BACKEND SHAPE, evidenced at the DAO layer. The blood bank
family's DAO returns SUCCESS-empty unconditionally: BloodTestMasterDaoImpl
:113-123 does `new Response(SUCCESS, SUCCESS_CODE, null, list, null)` — no
size-guard wrapper, no fall-through. The LIS family's service layer wraps the
DAO call in `if(res.getListObject() != null && res.getListObject().size() >
0)` (ReagentMasterServiceImpl:35) — the guard whose empty-branch is dead, so
empty falls through to ERR_500_MESSAGE. THE ENVELOPE CLASS IS A PROPERTY OF
THE BACKEND CODE SHAPE PER MODULE, not of the probe. Blood bank has no
guarded services -> SUCCESS-empty (honest); LIS has guarded services ->
masked 500.

WHY THE PROBES ARE NOT CONVERGING (three evidences):
1. The probe asserts what the LIVE endpoint returns (curl-verified first,
   then browser-asserted) — the blood bank probe asserted "No records."
   because SUCCESS-empty is what the backend genuinely returns.
2. parameterMaster (batch 2) FAILED its first probe run — masked banner not
   rendered, because the auth interceptor strips HTTP-500 envelopes so the
   correlation message never reached the component. The probe caught a real
   defect in the rebuild's error handling; fixed, re-probed, passed. A probe
   that fails when the code is wrong is not rubber-stamping.
3. The masked classes FOUND so far are backend code shapes (LIS fall-through
   DQ-2026-08-12-003, phantom column DQ-2026-08-12-009) — found BY the
   envelope check, not missed by it. If a future family has guarded services,
   the same check will find them.

PREDICTION (falsifiable): the master/unit family (batch 4, 6 screens) will
behave like blood bank (SUCCESS-empty or real data, zero masked) because its
service layer is the un-guarded shape; if any master/unit endpoint 500s with
a masked envelope, the family has a guarded service or a phantom column and
the check will catch it.

## DQ-2026-08-12-013 — UNIT MASTER 531 MASTER/UNIT FAMILY (batch 4): classification + SECOND PHANTOM COLUMN (is_bed_charges_service) — and the falsifiable prediction from DQ-2026-08-12-012 FAILED as designed

Owner's pre-build checks applied to the master/unit six (templateICDCodeMapping
688, loyaltyCardCreation 729, loyaltyCardConfiguration 743, autoServicePosting-
Configuration 775, passBedCategoryMapper 822, doctorRegistration 651):

SHARED COMPONENT/STATE CHECK: NONE — all six have UNIQUE controllers and
templates (master/unit/ directory).

ENVELOPE CLASSIFICATION (live-verified 2026-08-12):
- loyaltyCardCreation: SUCCESS empty — honest dev-empty (POST /api/unit/
  getLoyaltiCardMasterList; note legacy payload uses organisationId British
  spelling)
- loyaltyCardConfiguration: SUCCESS 1 REAL ROW — real data (POST /api/unit/
  getLoyaltyCardConfigurationList)
- passBedCategoryMapper: SUCCESS empty — honest dev-empty (POST /api/unit/
  getVisitorPassMapperList; organisationId payload)
- autoServicePostingConfiguration: MASKED-ERR-REF "Something went wrong.
  Reference: ERR-0c281b8d" — SECOND PHANTOM COLUMN: the raw SQL in
  AutoPostingConfigDaoImpl.java:92,134 selects autopostin0_.
  is_bed_charges_service but public.t_auto_posting_services_cofiguration has
  NO is_bed_charges_service column (verified information_schema count 0; the
  table itself exists with the legacy typo "cofiguration" and all OTHER query
  columns). Same masked envelope as parameterMaster (ErrorCorrelationUtil).
  Backend fix NOT made (Rule 10; BR candidate: add the column or drop the
  select).
- templateICDCodeMapping: HTTP 405 — the list is GET_TEMPLATE_DIAGNOSIS_MAPPER_
  LIST; needs the exact legacy call shape (mapping screen; POST body
  {offset, noOfRecordsPerPage} at ctrl:URI1 = .../getTemplateDiagnosisMapperlist
  + offset + noOfRecordsPerPage — verified shape pending build)
- doctorRegistration: REGISTRATION FORM (60+ endpoints, doctorSearchObj
  payload {doctorName, specialityId, docDesignationId, docCategoryId,
  organizationId, unitId, typeId, identificationTypeId}) — the large doctor
  master form, NOT a simple grid; builds as the full form parity.

PREDICTION RESULT (DQ-2026-08-12-012): the falsifiable prediction said batch 4
would behave like blood bank (zero masked); autoServicePostingConfiguration
500s with a masked envelope — THE PREDICTION FAILED, which is exactly what a
falsifiable prediction is for. The envelope check caught a 2nd phantom column
the moment it existed. This is further evidence the probes do not converge:
they fail when the backend is broken.

## DQ-2026-08-12-014 — PROBE PASS 17/17 + MODULE-BY-MODULE VARIATION IS REAL (owner's read request)

LIVE PROBE PASS (2026-08-12, after owner restarted 4201): all 17 checks green
across the four Unit Master batches — 3 billing real-data, 4 LIS descriptive,
parameterMaster masked, 4 blood empty, bloodGroupMatching real, 4 master/unit
empty, loyaltyCardConfiguration real, autoServicePostingConfiguration masked
banner (through the NEW /config/** proxy), negative control denies, 0 page
errors.

OWNER'S QUESTION — is the module-by-module variation real? YES, and batch 4
is the proof of the variation, not a contradiction of it. The evidence:

1. DAO/SERVICE LAYER SHAPE DIFFERS BY MODULE (the mechanism): blood bank DAOs
   return SUCCESS-empty unconditionally (BloodTestMasterDaoImpl:123). LIS
   services wrap DAO calls in size()>0 guards whose empty branch is dead
   (ReagentMasterServiceImpl:35) -> masked 500 for empty. Master/unit has a
   different shape again: loyalty/pass/template/doctor endpoints return
   SUCCESS-empty (un-guarded, honest), but autoServicePostingConfiguration's
   raw SQL selects a phantom column -> correlation 500. Three DIFFERENT code
   shapes in three families, all producing different envelope classes. The
   variation is in the backend code, not in the probe.

2. THE PROXY GAP IS A FOURTH, INDEPENDENT CAUSE CLASS (DQ-2026-08-12-005
   confirmed again): /config/** was missing from proxy.conf.json — the screen
   would have looked like a backend defect (Http failure during parsing, 200 +
   SPA html) until the proxy was checked. autoServicePostingConfiguration had
   TWO independent causes stacked: the phantom column (real backend defect)
   AND the proxy gap (dev-harness defect). Both had to be found for the
   screen to render; the probe's 200-parsing failure caught the proxy, and
   the direct-curl correlation envelope caught the phantom column.

3. THE FALSIFIABLE PREDICTION (DQ-2026-08-12-012) FAILED for master/unit —
   the batch had a masked envelope where the prediction said zero. The check
   caught it. Probes that fail when the backend is broken do not converge.

CONCLUSION: the "three clean batches" were genuinely clean backend families
(blood bank's DAO shape has no masked-error path); master/unit is not clean
(phantom column + proxy gap). The variation is real, module by module, and
the classification method (live curl envelope check first, browser assertion
second) tracks it correctly. This also completes the evidence that the
fourth-cause rule (check the proxy BEFORE classifying a screen as a backend
defect) is load-bearing: two independent screens (scheduleMaster, autoService-
PostingConfiguration) have now been mis-diagnosable without it.

## DQ-2026-08-12-015 — UNIT MASTER 531 BATCH 5 (master/global + adt/unit + billing/global): classification BEFORE build

Owner's pre-build checks applied to the batch-5 four (IPConsultationConfig-
uration 684, userNursingStationMapper 689, nursingStationWardMapping 548,
fundReconciliation 808):

SHARED COMPONENT/STATE CHECK: NONE — all four have UNIQUE controllers and
templates (IPConsultationConfigurationMasterController, userNursingStation-
MapperController, nursingStationWardMappingController, fundInflowController
for fundReconciliation). No dispatcher pattern.

ENVELOPE CLASSIFICATION (live-verified 2026-08-12, body status inspected):
- IPConsultationConfiguration: SUCCESS empty — honest dev-empty (POST
  /api/global/getDoctorIPConsultationServiceMapperList {organizationId,
  unitId, offset, noOfRecordsPerPage})
- userNursingStationMapper: SUCCESS 1 REAL ROW — real data (POST /api/global/
  getUserNursingStationMapperlist; legacy payload uses organisationId British
  spelling)
- nursingStationWardMapping: SUCCESS 1 REAL ROW — real data (POST /api/adt/
  unit/getNursingStationWardMapperList; NOTE payload uses recordPerPage, NOT
  noOfRecordsPerPage — 400 "Bad request: Invalid parameters" until the legacy
  field name was matched)
- fundReconciliation: SUCCESS empty — honest dev-empty (POST /api/billing/
  getAllfundInflowDetailsByTrust {orgId, unitId, trustId:0}; large transaction
  screen with 30+ endpoints, primary list verified)

ZERO generic envelopes, ZERO masked errors in this family (no correlation
envelope, no fall-through, no phantom column). Classification is again the
live-curl envelope check BEFORE any browser assertion (DQ-2026-08-12-014's
method); the recordPerPage 400 is a payload-field-name mismatch, resolved by
matching the legacy controller exactly, not a backend defect.

## DQ-2026-08-12-016 — UNIT MASTER 531 BATCH 6 (opd/unit three): classification — MASKED FAMILY

Owner's pre-build checks applied to the opd/unit three (appointmentScheduling-
UnitMaster 653, scheduleWise 657, doctorWise 658):

SHARED COMPONENT/STATE CHECK (owner's explicit question): scheduleWise and
doctorWise do NOT share a state or component — two separate controller files
(SlotBlockUnblockControllerScheduleWise.js 1047 lines vs
SlotBlockUnblockControllerDoctorWise.js 828 lines), two separate templates
(schedule-wise.html vs doctor-wise.html), two separate states. What they DO
share is the backend endpoint set (copy-paste family): GETALLSLOTSBLOCKUNBLOCK,
GETACTIVEREASONLIST, GETACTIVESPECIALITY, GETDOCTORSLISTAGANISTSPECIALITIES,
GETSUBSPECIALITYBYSPECIALITYARRAY, UPDATESLOTSTATUS, BLOCKUNBLOCKSLOT appear in
both. So: ONE endpoint investigation covers both screens (build-once-
investigate), but each is its own rebuild — the commonMaster lesson does NOT
collapse them into one component. appointmentSchedulingUnitMaster is a much
larger scheduling screen (60+ endpoints) with its own controller.

ENVELOPE CLASSIFICATION (live-verified 2026-08-12, body status inspected):
- scheduleWise + doctorWise (POST /api/opd/getSlots/blockUnblock
  {organisationId, unitId, slotTypeId, isBlocked, fromDate, toDate,
  listDoctorId, listSpecialityId, fromTime, toTime}): HTTP 200 status=error
  code=null message=null — the BLANK GENERIC envelope. Root cause traced:
  SlotMasterController.java:101-104 swallows the exception and returns
  `new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null)`;
  the DAO (SlotMasterDaoImpl:1857) throws a date-parse failure —
  GlobalCommonDateUtils.getDate appends " 00:00:00" to <12-char dates then
  parses with "yyyy-MM-dd" (always fails for 'YYYY-MM-DD' frontend input).
  The SQL itself runs clean in psql (0 rows — tables/columns all exist,
  NOT phantom-column). MASKED class: the real exception is swallowed at the
  controller boundary (constitution target class).
- appointmentSchedulingUnitMaster (POST /api/opd/getSlotsNew): MASKED-ERR-REF
  "Something went wrong. Reference: ERR-98fe438a" — the ErrorCorrelationUtil
  correlation envelope (3rd screen with it; parameterMaster + autoService-
  PostingConfiguration were the first two).

ALL THREE SCREENS ARE THE MASKED CLASS. No honest dev-empty, no real data in
this family. The rebuilds carry AMBER-permanent banners naming the mechanism
(swallowed controller exception + date-parse for the pair; correlation
envelope for getSlotsNew). Backend fixes NOT made (Rule 10; BR candidates:
fix getDate's short-date handling, stop swallowing at the controller).

## DQ-2026-08-12-017 — CORRECTION to DQ-2026-08-12-016: appointmentSchedulingUnitMaster is REAL DATA, not masked

The live browser probe rendered an actual row on appointmentSchedulingUnit-
Master ("Dr. Demo Doctor | General Medicine") — the endpoint POST /api/opd/
getSlotsNew works with the payload {organizationId, unitId, slotTypeId} and
returns SUCCESS with real data. The masked ERR-98fe438a seen during
classification was a WRONG-PAYLOAD artifact: the probe curl sent
{organizationId, unitId, offset, noOfRecordsPerPage}, which hits a different
query branch that 500s; the component's actual payload (slotTypeId: 1)
succeeds. Classification correction: appointmentSchedulingUnitMaster (653) =
REAL DATA. scheduleWise (657) + doctorWise (658) remain the masked pair
(blank generic envelope, date-parse + swallowed exception — verified against
the component's own payload which ALSO returns the blank generic envelope).

LESSON (matches DQ-2026-08-12-008): a classification must be verified against
the payload the REBUILD will actually send, not just a guessed payload. The
live probe is the arbiter: it rendered real data where the curl saw a masked
error. The component's template now renders the real row (correct); the
masked-banner branch stays for scheduleWise/doctorWise only.

## DQ-2026-08-12-018 — UNIT MASTER 531 BATCH 7 (services 3 + CSSD 2): classification

Owner's pre-build checks applied (EHCpackage 623, packageWithCAP 624,
eitherOrPackage 626, wrapperExpiryMaster 554, kitCreation 555):

SHARED COMPONENT/STATE CHECK: NONE — all five have UNIQUE controllers and
templates. The package trio (EHC/CAP/eitherOr) share the /api/packages/filter
endpoint with packageTypeId 1/3/2 — a backend family, not a shared component.

ENVELOPE CLASSIFICATION (live-verified 2026-08-12, body status inspected):
- EHCpackage: SUCCESS empty — honest dev-empty (POST /api/packages/filter
  {orgId, unitId, packageCode, packageName, packageTypeId:1, offset,
  noOfRecordsPerPage, searchKeyword})
- packageWithCAP: SUCCESS empty — honest dev-empty (packageTypeId:3)
- eitherOrPackage: SUCCESS empty — honest dev-empty (packageTypeId:2)
- wrapperExpiryMaster: SUCCESS 1 REAL ROW (WRPTEST01 "Test Wrapper", 30 days)
  — real data (GET /cssd/getWrapperExpiryList?organizationId&unitId&offset&
  noOfRecordsPerPage). NOTE: without offset/noOfRecordsPerPage the endpoint
  500s with the correlation envelope (ERR-c48a20d8) — a wrong-payload
  artifact, NOT masked; with the full legacy query params it succeeds.
- kitCreation: SUCCESS empty — honest dev-empty (GET /cssd/getAllKitsList
  with full query params; without them ERR-8bf108d1).

LESSON CONFIRMED (DQ-2026-08-12-017 applied twice in one batch): a
classification must be verified against the payload the REBUILD sends — the
CSSD pair looked masked with a minimal payload and are actually real
data/honest empty with the legacy query params. ZERO masked errors in batch 7.

## DQ-2026-08-12-019 — UNIT MASTER 531 BATCH 8 (clinical 2 + no-state + accessControl pair): classification

Owner's pre-build checks applied (RMOAllocationMaster 824, growthChartMaster
962, templateutility 667, accessControl 677, accessControl1 673):

SHARED COMPONENT/STATE CHECK: accessControl + accessControl1 SHARE the
accessControlController (the 60-line STUB — zero serviceAction/api/URI refs,
pure angular-ui-tree on empty $scope.data) with TWO templates
(views/common/access-control.html vs views/access-control.html) — the
commonMaster shared-controller precedent: ONE noData rebuild, TWO routes.
templateutility (667) has NO state in app.js (sweep's ctrl-basename class) —
NOT a screen; walked past.

ENVELOPE CLASSIFICATION (live-verified 2026-08-12, body status inspected):
- RMOAllocationMaster: SUCCESS empty — honest dev-empty (POST /api/unit/
  clinical/getRmoAllocationMasterList {organizationId, unitId, offset,
  noOfRecordsPerPage})
- growthChartMaster: SUCCESS 1 REAL ROW — real data (GET /api/global/
  growthChartlist/{org}/{unit}/{offset}/{size}; the height-units GET also
  SUCCESS 1 row)
- accessControl + accessControl1: NO-ENDPOINT (noData class, DQ-005) — the
  stub controller never calls any endpoint; legacy renders an empty tree
  editor with no data path.

ZERO masked errors in batch 8. RMO + growthChart build as normal masters;
accessControl pair builds ONCE (shared component) with the noData state, two
routes (moduleId 677 and 673) both pointing at it.

## DQ-2026-08-12-020 — UNIT MASTER 531 BINDINGS-ONLY 15: full classification (backend truth)

The 15 bindings-only candidates (backend=N in the sweep) examined for
controller existence + endpoints:

REAL BUILD (1): templateMaster (58) — labTemplateMasterController EXISTS;
GET /LIS/unit/labtemplates/{org}/{unit}/{offset}/{size} returns a RAW ARRAY
(response.data read directly, NOT {listObject} — the raw-list response shape,
3rd shape seen alongside {listObject} and {stores,listObject2}) — honest empty
[] live. Real endpoint, real controller: build.

NODATA — MISSING CONTROLLER (3): testMaster (276), multiparameterTestMaster
(183), OPConsulationConfigurationMaster (611) — the states reference
controllers (singleParameterTestController, multiParameterTestController,
OPConsulationConfigurationMasterController) that DO NOT EXIST anywhere in the
webapp (find: 0 files); the templates DO exist. No controller code = no
endpoint (consultantType precedent, DQ-005): noData class.

NODATA — MASTERCONTROLLER SHELLS (6): fundApproval (809), fundRequisition
(810), fundApprovedList (811), schedulePriceUnitService (586),
roundOffConfiguration (599), IPdefaultServiceConfig (620) — all use the
generic MasterController (common-controller.js, ZERO api refs). The templates
bind genderList/identificationList (fund trio) or plain form fields
(round-off: min/max/medianAmount inputs) that MasterController never
populates; no list binding to any endpoint. noData class.

NO STATE — WALKED (5): Order set Master (707), ordersetmaster (698),
bloodtestresultmaster (733), kitmasterpharmacy (762), templateutility (667) —
no .state in app.js, not screens (sweep ctrl-basename/name-match classes).

RESULT: 1 real build + 9 noData + 5 walked. The 9 noData screens use ONE
shared noData component pattern (accessControl precedent, DQ-019) with per-
screen routes; templateMaster builds as a real master with the raw-array
response handling.

## DQ-2026-08-12-022 — GLOBAL MASTER (369) SUBTREE SWEEP: 27 candidates

Swept Global Master (root 369, 168 nodes): 98 name-matched states; 64 already
rebuilt via routes, 7 via the commonMaster dispatcher, 27 GENUINE candidates
(alias-checked: none rebuilt under kebab routes).

CONTROLLER CLASSIFICATION of the 27:
- NODATA — MasterController shells (10): bacteriaClassification (428),
  antibiotic (429), antibioticsClass (430), antibioticsGroup (431),
  sensitivityTestResult (438), dispensingMaster (469), kitMaster (471),
  taxMaster (474), consentType (528), vaccinationSchedule (750) — all resolve
  to common-controller.js's MasterController (generic scaffold, zero api
  refs; dashboardInfoMasterController.js was a false partial-match).
- NODATA — missing controller (4): subdepartmentMaster (373),
  specimanMaster (444), packageMaster (465), daywiseDietTemplate (513) —
  states reference no controller file that exists.
- REAL controllers (13, 12 distinct): licenseTypeMaster (379),
  consentMaster (400/527 — same ctrl), IncubationMaster (422 — NOTE: it's a
  LIS TRANSACTION screen incubationObjservationController, not a master),
  sampleTypeMediaMappingMaster (426), colonyTestWithMediaMaster (434),
  mappingofMicroorganism (443), doseMaster (502), dietTypeMaster (510),
  subsequentSurgeryConfiguration (516), procedureConfigurations (696),
  siteOfAdministration (753), genericScheduleMaster (827).

14 noData + 13 real (IncubationMaster may be a transaction deferral — verify
during build). Live endpoint verification next.

## DQ-2026-08-12-023 — GLOBAL MASTER 369: ENVELOPE CLASSIFICATION (batch 10)

Live-verified the 12 distinct real controllers (DQ-022) + the 14 noData:
- REAL DATA (2): dietTypeMaster (1 row, POST /api/kitchen/master/dietTypeMaster/
  getDietTypeMasterPaginationList), siteOfAdministration (4 rows, POST
  /api/global/clinical/getSiteMasterList {organizationId} only)
- HONEST DEV-EMPTY (4): licenseTypeMaster (GET /org/getLiceneceTypeMasterList/
  {org}), procedureConfigurations (GET /api/otims/getProcedureConfigList/
  {org}/{unit}), subsequentSurgeryConfiguration (POST /api/otims/
  getSubSequentProcedureConfigList), genericScheduleMaster (endpoint shape
  pending final check)
- MASKED (1): consentMaster — THIRD PHANTOM COLUMN: GET_CONSENT_LIST selects
  c.formTemplateId (ConsentMaster.java:29) but adt.m_consent_master has NO
  form_template column (verified information_schema). TRIPLE-swallowed: DAO
  catches HibernateException silently (ConsentMasterDaoImpl:205-210), service
  catches and returns blank ERROR (ConsentMasterServiceImpl:224-227),
  controller catches and returns blank ERROR (ConsentMasterController:60-63).
  The masked envelope is the blank {status:error, code:null, message:null}.
- NODATA (14): the 10 MasterController shells + 4 missing-controller
  (subdepartmentMaster, specimanMaster, packageMaster, daywiseDietTemplate).
- IncubationMaster (422): LIS TRANSACTION screen (incubationObjservation
  Controller — patient visit lists, not a master) — DEFERRED (transaction
  class, like ambulanceCharges).
- sampleTypeMediaMappingMaster, colonyTestWithMediaMaster,
  mappingofMicroorganism: LIS global mappers — endpoint shape pending final
  check (LIS_GLOBAL constants); verify during build.

BATCH 10 = 13 builds: 2 real + 4 empty + 1 masked + 6 pending-shape, plus 14
noData. ConsentMaster's phantom column is the 3rd found (technique_id,
is_bed_charges_service, form_template) — all BR candidates (Rule 10, not
fixed).

## DQ-2026-08-12-024 — ADT SUBTREE (root 3) SWEEP: TRANSACTION-HEAVY, ZERO MASTER BUILDS

Swept Adt (80 nodes): 34 state-matched (3 rebuilt via routes: patientRegistration,
periodicityService, inputOutputCharts), 46 no-state (reports/leaves/sections).

The 31 candidates are ALL the transaction/deferred class:
- Patient-workflow transactions (deferred per ambulanceCharges/IncubationMaster
  precedent): billEstimation, IPDBilling, updatePayeeInformation,
  preAuthorizationRequest, patientAcceptance, allergies (1283-line controller
  with patientSearchByMultipleCriteria — patient-entry, NOT a master),
  sampleCollection, homecareMedicationEntry, transfer/initiateBedTransfer/
  transferRequest/transferRequestAcceptance, readyForBilling, finalDischarge,
  abscondingPatient, shortLeaveStatus, KitIndent/KitIndentApproval/KitReturn,
  washingRequest, inputOutputChartHistory, sterilizationRequest, vitals,
  serviceRendering, nursingProgressNote, administrationWorklist, dayCareWorklist
- Dashboards (not masters): nursingAllocationDashboard, nursingShiftDashboard
- MasterController shells (noData): patientAcceptance, homecareMedicationEntry,
  abscondingPatient, washingRequest, nursingProgressNote
- Missing controllers: billEstimation, IPDBilling, transferRequest, dayCareWorklist

RESULT: zero master-grid builds in the Adt subtree — it is patient-workflow
and reporting. The rebuild program's scope (masters per the walk) does not
apply here; the deferred transaction class (DQ-2026-08-12-014 note) covers
these. Adt closed out with no branch. Next: Nursing (129) or Pharmacy (108)
subtree.

## DQ-2026-08-12-025 — REMAINING-CANDIDATE CENSUS: MASTER PROGRAM COVERAGE COMPLETE

Comprehensive sweep of all 1,077 menu nodes after batches 1-10:
- Master-bearing subtrees COVERED: OPD (268 rebuilds), Unit Master 531
  (batches 1-9: 40 verified + 15 bindings-only), Global Master 369 (batch 10:
  27 candidates), commonMaster dispatcher (43/43).
- The 212 remaining state-candidates (not rebuilt, incl. unmerged batches
  7-10) are ALL the deferred transaction/report/dashboard classes: billing
  transactions (opdBill, deposit, refund, debit/credit note, bill list),
  patient workflow (admission, transfer, discharge, appointments, bed
  allocation, allergies, vitals, sample collection), asset/CSSD workflows
  (assetIssue, sterilization, kit issue), reports (the 500+ RPT class).
- NO genuine master-grid candidates remain. The rebuild program's scope
  (masters per the walk) is exhausted for the menu; remaining nodes are the
  transaction class (deferred per DQ-2026-08-12-014, ambulanceCharges,
  IncubationMaster, DQ-024 Adt/Nursing/Pharmacy sweeps) and reports.

RECOMMENDATION for the owner: the master-rebuild phase is complete across
the menu. The next phase (transactions) is a different scope/class and should
be a separate decision — the transaction screens are patient-workflow
features with 30+ endpoints each, a materially larger build than the masters
program. Queued for owner decision, not assumed.

## DQ-2026-08-12-026 — MERGE MISLABEL: ef5cf3aa "batch 8" actually merged b7

Discovered during the batch-9 conflict resolution (owner's merge of "batches 7
and 8" at ef5cf3aa):
- ef5cf3aa parents = 8083f5af + d06bfce1 → the merge brought in B7's tip
  (d06bfce1), NOT b8's (7875ee01). b8's screens (RMOAllocationMaster 824,
  growthChartMaster 962, access-control pair 677/673 — DQ-2026-08-12-019) are
  NOT on main: `git merge-base --is-ancestor 7875ee01 origin/main` = NO; zero
  b8 routes in main's app.routes.ts. The main-tree dirs "growth-chart" and
  "new-access-control" are OLDER alias-routed rebuilds, not b8's features.
- b8 merges CLEANLY when done: it is stacked on d06bfce1 (now an ancestor of
  main), so merging 7875ee01 adds only b8's delta (17 files +610).
- b9 resolution (687af7a4, merge main→b9): kept b7's 5 routes + b9's 10;
  probe kept both batches' assertion blocks; fixed the consumed-closing-brace
  glitch at the join (TS1117, caught by tsc -p tsconfig.app.json); gates: tsc
  clean, ng build EXIT 0, live probe PASS (b9's 10 + b7's 5 render, negative
  control denies); dry run CLEAN 52 files +1282.
- b10 resolution PENDING — per owner sequencing ("after 9 is merged").
  Lesson recorded: future overnight batches must be STACKED (cut off the
  previous batch's tip), never off main.

## DQ-2026-08-12-027 — VACUOUS tsc GATE (11th structurally-incapable mechanism)

Plain `npx tsc --noEmit` in new/ is a NO-OP: the solution-style tsconfig.json
has `files: []` + project references to tsconfig.app.json/spec — an empty
program, so it reports 0 errors trivially without type-checking anything.
NOTICED 2026-08-12: the deleg_4ec02b84 subagent flagged it ("no-op gate");
parent then verified by reading tsconfig.json + planting a broken
`const x: number = 'x'` — plain tsc passed it, `tsc -p tsconfig.app.json`
caught it (TS2322). REAL GATE = `npx tsc --noEmit -p tsconfig.app.json`
(verified catching a planted error) + `ng build` (full Angular compiler,
already the primary gate).

AFFECTED BATCHES: 7, 8, 9, 10 all ran the plain form in their verify steps
— the tsc step was vacuous in every one. RETRO-CHECK (2026-08-12): current
main (ef5cf3aa) under `tsc -p tsconfig.app.json` = RC 0 CLEAN; b8 tip
(7875ee01) = RC 0 CLEAN; resolved b9 (687af7a4) = RC 0 CLEAN. So the vacuous
gate COST NOTHING in outcome: ng build (the real type-checking gate) passed
EXIT 0 for every batch, and the retro-check confirms the merged content is
type-clean. Skill updated (gate list now uses the -p form; plain form
explicitly warned as vacuous). This is the 11th pass-while-structurally-
incapable mechanism on record with the others (error-swallowing, masked
envelopes, fall-throughs, proxy gap, noData, etc.).

## DQ-2026-08-12-028 — BATCH 10 MERGE-RESOLUTION (1c97986f) — COMPLETE

Merged origin/main (4458ff4a, post b7/b8/b9) into rebuild/global-master-batch10:
- app.routes.ts: kept ALL 44 batch routes (b10 25 + b9 10 + b7 5 + b8 4),
  fixed the consumed-closing-brace glitch at the consentMaster/templateMaster
  join (same TS1117 class as b9, caught by tsc -p before commit).
- probe: docstring + assertion block resolved; B5-B10 blocks all present.
- (d) route survival: 52 checked (44 batch + 8 prior samples) each exactly
  once, zero conflict markers. (One false alarm: batch-5 route is
  'IPConsultationConfiguration' capital-IP — my check's case was wrong, route
  present.)
- Gates: tsc -p tsconfig.app.json RC 0, ng build EXIT 0, live probe PASS
  (B10 masked banner + real screens, B9/B7/B8 all render, negative control).
- Diff --stat main..b10 = 122 files +4000, intended set only; dry run CLEAN
  0 conflicts.

## DQ-2026-08-12-029 — consentType (528) MISCLASSIFICATION CORRECTED + ROUTE-TARGET PREFLIGHT

THREE-PART INCIDENT CLOSE-OUT (owner questions 2026-08-12):

1. consentType HISTORY (answer): the legacy state (app.js:12585) binds the
   generic MasterController shell with a zero-binding template — the DECOY
   shape. The decoy-groundwork (queue line 276) classified 528 as Group A:
   REAL backend (POST /adt/getConsentTypeMasterList — verified live: 3 rows,
   "Fresh Consent Modal Test"/CNTDUP2) + REAL controller
   (consentTypeMasterController.js, 396 lines, self-registers no state).
   DQ-2026-08-12-022 WRONGLY classified it as "MasterController shell →
   noData" — the sweep treated the decoy shell as the whole story. The b10
   delegation built the noData shell AND never staged the dir (route dangled
   in 972c69dd). The batch-10-fix merge (03784add) committed the noData
   build — WRONG CLASS on main. Corrected on rebuild/consent-type-rebuild
   (d79cf808): real-data build rendering the 3 rows.

2. FULL ROUTE AUDIT (answer): every route across all batches checked for
   unresolvable loadComponent targets — consentType is the ONLY one. Both
   the original b10 commit (972c69dd: 323 routes, 1 missing) and the
   resolved tip (1c97986f: 342 routes, 1 missing) have exactly one dangling
   target: consentType. The regression guard is NOT narrow — the audit
   proves it covers the only instance.

3. PREFLIGHT TOOL (new): tools/check_route_targets.py — static route→file
   resolution gate (working tree or --ref <sha>). Validated: PASS on clean
   tree (342 routes), FAIL naming consentType on both pre-fix commits,
   negative control (fake route → nonexistent component) FAILS naming it.
   Would have caught the defect before any probe ran. Gate list updated:
   run it before every probe.

## DQ-2026-08-12-030 — DECOY RECHECK: 2 OF 18 ARE REAL SCREENS (corrected class list)

consentType (528) — the founding decoy — proved REAL DATA (3 live rows,
rebuilt at d79cf808). The owner ordered a live recheck of the other 17.
VERDICTS (every one called live, body status inspected):

REAL SCREENS MISCLASSIFIED AS DECOY (2) — the 'no controller anywhere' claim
was wrong for both:
- 580 Account Payable: billing/unit/accountPayableMasterController.js EXISTS
  (the 9eb08762 finding missed it — index.html script-include search); the
  state binds MasterController but the real controller lives in billing/unit.
  /api/global/getAccountPayable {orgId, orgUnitId} -> 200 success 5 ROWS.
- 582 Account Payable Status: java-only (AccountPayableMasterController
  /getActiveAccountPayable); /api/global/getActiveAccountPayable
  {orgId, orgUnitId} -> 200 success 5 ROWS.
CLASSIFIER ERROR: keyed on 'controller JS in index.html + template bindings';
missed controllers not script-included, and never called the backend. The
consentType lesson (call the backend, check body) applied to the whole class.

REAL BACKEND, MASKED FAILING FEED (2) — not shells, but the list feed fails:
- 587 Order Set + 588 Order Set List: SHARE /api/orderset/orderSetsList
  {orgId, unitId, offset, recordPerPage} -> 200 status=error 'Something went
  wrong!!' (masked class; ONE root-cause investigation for both).

REAL ENDPOINT, DIFFERENT CLASS (1):
- 578 Variable Pricing: /billing/variable-pricing/
  getVariablePricingFactorByVisitId live (HTTP 200) but VISIT-SCOPED
  (payload {unitId, organisationId, visitTypeId}) — a pricing editor, not a
  master list. State binds the shell under the TYPO state name
  variableSquencePricing; real controller unbound.

GENUINE DECOY SHELLS (12, correctly classified): 577 Package List, 581
Account Payable Comparsion, 586 Schedule Price Unit Service, 589 Admission
Rejection List, 591 Find Bill List (java findbillListDetails COMMENTED OUT —
dead code), 593 Charge Conversion 2nd node (save-only CRUD), 594 Auto Charge
Review, 595 Write Off, 596 Write Off Details, 599 Round Off Configuration
('roundoff' java hits are DTO field names), 601 Remainder For NOK, 620 IP
Default Service Configuration. Batch-9 noData builds for 586/599/620 were
CORRECT.

CORRECTED CLASS DECISION (replaces the single 'build or leave' question):
  A) BUILD parity: 580, 582 (+ 528 already rebuilt)
  B) ROOT-CAUSE the masked feed first, then build: 587, 588 (or build with
     AMBER banner — owner's call)
  C) LEAVE/design-new: 578 (transaction editor — defer with the transaction
     class), 577/581/586/589/591/593/594/595/596/599/601/620 (true shells)

## DQ-2026-08-12-031 — ORDERSET MASKED FEED ROOT-CAUSED: 4TH PHANTOM COLUMN (icpc_id)

587 Order Set + 588 Order Set List share POST /api/orderset/orderSetsList ->
200 status=error "Something went wrong!!". MECHANISM (one investigation for
both, as the owner ordered):
- DAO OrdersetMasterDaoImpl.getOrderSetsList (184-203) runs TWO named native
  queries: LIST_ORDER_MASTER then LIST_ORDER_MASTER_FOR_ICPC, returning both
  lists in one Response.
- LIST_ORDER_MASTER_FOR_ICPC (OrdersetMaster.java:179-207) joins
  `osm.icpc_id = icpc.icpc_id` (line 199) — but doctor.m_orderset_master has
  NO icpc_id column (verified information_schema: 14 columns, no icpc_id).
- The same phantom ref appears in the search query (:icpcId, line 144) and
  the BY_ID variant (line ~241).
- Chain: Hibernate throws on Q2 -> DAO rethrows (184-202, no swallow) ->
  service rethrows (155-161, no swallow) -> OrdersetController:93-96 catches
  and returns COMMON_ERROR_MESSAGE "Something went wrong!!" -> masked
  envelope. Q1 (the diagnosis join) is never reached for the response.
- psql proof: Q1 runs clean (0 rows — inner diagnosis join is empty data,
  not error); Q2 fails: `ERROR: column osm.icpc_id does not exist`.
- This is the FOURTH phantom column (technique_id, is_bed_charges_service,
  form_template, icpc_id). Same Rule 10 treatment: NOT fixed; BR candidate.
- DECISION for the owner (DQ-030 Group B): fix = add icpc_id to
  doctor.m_orderset_master (DDL change, BR review) OR rebuild 587/588 with
  AMBER masked banners naming the phantom column (parity of the current
  behavior). Owner's call; both screens share one banner text.

## DQ-2026-08-12-032 — DQ-025 SCOPING SWEEP: THE 212 REMAINING ARE NOT MASTERS

Sweep of all 1,077 menu nodes: 240 rebuilt (name-matched routes), 212
state-candidates (state exists, not rebuilt), 625 no-state (reports/leaves/
sections). The 186 ledger figure derived from 233-47; the sweep's
authoritative candidate count is 212.

CLASSIFICATION of the 212 (name heuristics + controller spot-checks):
- TRANSACTION (patient/billing workflows): ~108 (~50%) — OPD Bill, Admission,
  Transfer, Invoices, Sterilization, Appointments, Allergies, Vitals, Service
  Rendering, Progress Note, cash desk, etc. Multi-step, patient-context,
  write paths.
- REPORT: 35 (17%) — Bill List, Debit/Credit Note List, Refund List,
  Discharge Summary, Report Entry/Validation/Release.
- DASHBOARD: 13 (6%) — worklists/queues (Nursing, Donor Screening, Blood Bag,
  Casualty, Day Care).
- DUPLICATE-ID RELISTS: ~20 — unitMaster/departmentMaster/allergies/
  appointments/ambulanceCharges/IncubationMaster/houseKeeping/assetPMS/
  macroscopic/microscopic/dischargeSummary/readyForBilling appear 2-3x under
  different parents; already-rebuilt states re-listed.
- GENUINE MASTER-STYLE: ~10-15 (5-7%) — 597/598/600 cash desk, 625 package,
  635 storewise generic mapper, 650 staff registration, 660 application
  config, 719 donor registration, 722 transfusion TTD, 745 discount rights,
  746 dept nursing station mapper. Real controllers exist.
- DECOY SHELLS: 12 (correctly classified, DQ-030) — leave.

WHAT A TRANSACTION SCREEN NEEDS THAT A MASTER DOES NOT (the loop gap):
1. PATIENT/ENCOUNTER CONTEXT: a selected patient/visit/encounter drives the
   screen — needs a search/picker + context propagation, not a flat org/unit
   list. The master loop's (orgId, unitId, offset, size) contract doesn't
   fit.
2. MULTI-STEP FLOWS: create -> validate -> approve -> settle chains (e.g.
   PO creation -> approval -> GRN), each step a different state/component
   with status transitions. Masters are single-view CRUD.
3. WRITE PATHS THAT MATTER: save/update/status-toggle on masters are
   low-risk; transaction writes (billing, stock movement, patient state)
   need transactional integrity, idempotency, and audit — the error
   swallowing audit (Rule 1) applies with much higher stakes.
4. COMPOSED DATA: transactions aggregate many entities (orderset -> tests,
   bill -> line items -> payments) — the single-listObject envelope + one
   getList() pattern doesn't model it; detail endpoints per sub-entity.
5. WORKFLOW STATE: status machines (pending/approved/rejected/settled) with
   worklists keyed by status — the dashboard/worklist class is its own
   shape (13 of them).
6. VALIDATION/DUPLICATE CHECKS: business rules across entities (stock
   availability, credit limits, insurance eligibility) — masters validate
   a single row.

RECOMMENDATION: do NOT run the master loop on the 212. If the owner wants
the transaction phase, it is a SEPARATE build pattern (patient-context
pick-and-probe, per-flow golden traces, write-path verification, status
machines) with its own estimate. The 10-15 genuine master-style screens
could be built with the existing loop as a small closing batch (owner's
call), everything else needs the transaction methodology first.

## DQ-2026-08-12-033 — DUPLICATE-ID RELISTS VERIFIED (call it, not inferred)

Owner's (c): confirm the ~20 duplicate-id relists are already-rebuilt rather
than assumed. VERIFIED by direct route/dispatch lookup on main (610ebbf6):
- GENUINELY REBUILT (2 states, 6 tree nodes): unitMaster (417/467/531) and
  departmentMaster (372/533/564) — both are commonMaster dispatcher branches
  (common-master.service.ts:67 departmentMaster, :141 unitMaster). All 6
  nodes duplicate ONE rebuilt feature each.
- NOT REBUILT (13 states, 26 nodes): allergies, appointments,
  ambulanceCharges, IncubationMaster, houseKeeping, assetPMSScheduler,
  macroscopicExamination, microscopicExamination, dischargeSummary,
  readyForBilling, finalDischarge, transfer, transferRequestAcceptance —
  all transaction/deferred class (DQ-032), 2 nodes each = genuine tree
  duplication of UNWALKED states.
- The DQ-032 "~20" estimate was inflated: ~6 of those nodes fold into the 2
  dispatcher-covered states; the remaining are transaction-class, not
  already-rebuilt. No action: the 2 rebuilt states stay covered by the
  dispatcher; the 13 stay in the deferred transaction pool (DQ-025/032).

## DQ-2026-08-12-034 — FINAL MASTER BATCH CLOSED (a) + TRANSACTION METHODOLOGY PROPOSAL (b)

(a) FINAL MASTER BATCH — LIVE AUDIT SHRANK THE CANDIDATES:
The DQ-032 estimate of "10-15 genuine master-style screens" was tested by
live calls. Result: ONE confirmed master-list screen —
departmentNursingStationMapper (746, POST /api/global/getDepartmentNursing
StationMapperlist {organisationId, unitId, noOfRecordsPerPage, offset} ->
SUCCESS 2 rows; built 9879cef6). The other 9 were NOT master lists: 660
applicationConfiguration is a config FORM fed by the service-master dropdown
(100-field service DTO); 635 storeWiseGenericMapper is a raw-array feed
(non-envelope); 597/598/600 (cash desk), 625 (package), 650 (staff reg),
719 (donor reg), 722 (TTD — SEARCH_BAG_DETAILS only) are registration/
entry/search forms = transaction class. THE MASTER PHASE IS NOW CLOSED.
Remaining non-master candidates stay parked per DQ-032/025.

(b) TRANSACTION METHODOLOGY — PROPOSED LOOP (the deliverable the owner reads
before any transaction screen is built; NOT executed):

The master loop (list endpoint + envelope + grid + status toggle) does NOT
transfer. The transaction loop must add, per screen:

1. PATIENT/ENCOUNTER CONTEXT — every transaction screen keys off a selected
   patient/visit/encounter. Step 0 of the loop is a picker + context
   propagation, NOT a flat (orgId, unitId, offset, size) call. Golden trace
   starts at the picker, not the grid.
2. MULTI-STEP FLOW MAP — before building, map the state machine: create ->
   validate -> approve -> settle chains with per-step components/routes and
   status transitions (e.g. PO -> approval -> GRN; bill -> payment ->
   settlement). Each step is a golden-traced endpoint set.
3. WRITE-PATH VERIFICATION (the Rule 1 audit, higher stakes) — transaction
   writes mutate billing/stock/patient state: verify idempotency (no
   double-post on retry), transactional integrity (all-or-nothing across
   sub-writes), and audit fields. The master save/update/status-toggle
   checks are insufficient; every write path gets its own trace +
   rollback test.
4. COMPOSED-DATA CONTRACT — transactions aggregate entities (orderset ->
   tests; bill -> line items -> payments). The single-listObject envelope +
   one getList() doesn't model it. The loop defines per-sub-entity detail
   endpoints and their join keys BEFORE the component.
5. STATUS MACHINES + WORKLISTS — pending/approved/rejected/settled states
   with worklists keyed by status. The 13 dashboard/worklist screens are
   this class; they may be built by the same loop (status-keyed list +
   transition action) — the owner's DQ-032 (d) observation.
6. CROSS-ENTITY VALIDATION — stock availability, credit limits, insurance
   eligibility checked across entities on write; masters validate one row.
   Each screen's business-rule set is enumerated in the manifest before
   build (BR additions required, per the constitution).

Proposed loop shape (to be refined with the owner before use):
  T0 classify (live call: endpoint truth + envelope + write path, not name)
  T1 golden trace the FULL flow (picker -> each step -> write -> status change)
  T2 flow map (states, transitions, per-step endpoints) — owner sign-off
  T3 rebuild per step (house pattern + per-step service)
  T4 write-path verification (idempotency, rollback, audit)
  T5 live probe per flow (freshness gate first) + negative controls
  T6 BR manifest additions for every business rule touched

NOT STARTED — pending owner review of this proposal.

## DQ-2026-08-12-035 — DUPLICATE-ID RELISTS LIVE-VERIFIED (owner item c, complete)

FULL duplicate-state sweep (30 states with 2+ tree nodes — MORE than the ~20
estimate). Each covered state verified by LIVE browser probe, not inferred:
- 10 GENUINELY REBUILT (25 tree nodes) — all LIVE-RENDER confirmed:
  QMS (791/790), appointmentScheduling (4/653), bloodComponent (736/481,
  route grants 481), chargeConversion (592/593), consentMaster (400/527),
  icutypeMaster (374/535), patientRegistration (3/380), departmentMaster
  (372/533/564), subdepartmentMaster (373/534), unitMaster (531/417/467).
  The last three are commonMaster dispatcher branches (45-id family grant).
- 20 NOT REBUILT (45 nodes) — ALL transaction/report/dashboard class:
  admissionRequest, allergies, transfer, billList, dischargeSummary,
  readyForBilling, finalDischarge, reportEntry, reportRelease,
  dueForDischarge, cancelDischarge, washingRequest, assetIssue,
  assetPMSScheduler, macroscopicExamination, microscopicExamination,
  IncubationMaster, secondOpinionRequest, bedAllocation,
  transferRequestAcceptance. Genuine tree duplication of UNWALKED states —
  correctly deferred to the transaction phase (DQ-025/032/034).
- Probe-grant lesson: the dispatcher route guards on the 45-id family array,
  NOT per-branch ids; single-id grants deny (probe artifact, not rebuild
  defect — departmentMaster etc. render fine with the family grant).

## DQ-2026-08-12-036 — MASTER CLOSE-OUT BATCH (owner (a), complete)

10 screens built (7e93ee27, branch rebuild/master-closeout, dry run CLEAN
52 files +2441), per the owner's explicit list. Live-verified classification
per screen — the DQ-034 audit held: these are NOT all clean master lists.
- 745 discountRights: REAL master list — POST /billing/getDiscountRights
  MasterList (NO /api prefix — billing controllers mount at /billing;
  resolved by subagent from billing.url.config.js + Java controller; count
  companion live 2).
- FEED/FORM screens (render real dropdown/feed rows + honest class):
  625 allInclusivePackage (gender feed 3 rows), 635 storewiseGenericMapper
  (raw-array non-envelope), 650 staffRegistration (relation feed 2 rows),
  660 applicationConfiguration (service list 1 row), 719 donorRegistration
  (prefix feed 1 row).
- WRITE-PATH/DEFERRED states (honest — no false data): 597 cashOpeningBalance,
  598 interimHandover, 600 shiftClosing (refund endpoint 400s — payload
  incomplete + DAO stray-quote bug logged in the screen), 722
  transfusionTransmittedDiseases (bag-search only).
Gates: tsc -p RC=0, ng build EXIT 0 (2 ICU-brace template bugs found+fixed:
  literal {payload} text in shift-closing + storewise-generic-mapper),
  preflight 357 routes PASS, live probe all 10 render (9/10 first pass; the
  shiftClosing 'failure' was the probe's own check — the screen renders its
  honest write-path state, corrected).
THE MASTER PHASE IS CLOSED (341 + 3 Group B + 746 + 10 = 355 rebuilt dirs
excl. decoys; remaining = transaction/report/dashboard class only).

## DQ-2026-08-12-037 — T0 FIRST TRANSACTION FLOW: ASSIGN LOYALTY CARD (752)

Owner picked the shortest chain; 752 assignLoyaltyCard is it (2 service calls
vs 5 for updatePayeeInformation/cancelDischarge). T0 classification complete:

FLOW (the whole chain, one write):
1. PICKER: patient search — POST /adt/patientSearchByMultipleCriteria
   {uhIdNumber, patientName, genderId, mobileNo, birthDate,
   identificationNo, patientType:"1", unitId, organizationId}. LIVE:
   SUCCESS 0 rows for "Demo" (exact legacy payload required; my earlier 400
   was a wrong-payload artifact). Requires >=3 chars patientName (legacy
   warns under 3).
2. FEEDS (dropdowns on the picker form): GETACTIVEGENDERLIST
   (/adt/getActiveGenderList), GETACTIVEIDENTIFICATION, bankMaster,
   hospitalBank, transactiontypes, cardTypeMaster.
3. CARD CHECK: GET /api/unit/checkLoyaltyCard/{orgId}/{unitId}/{16-digit}
   — LIVE SUCCESS (validity gate; 16-digit rule enforced client-side).
4. EXISTING-CARD LOOKUP: /api/loyaltyCard/getLoyaltiCardDetailsByPatientId
   (path/param shape pending exact resolution; 404 on my /1 guess).
5. WRITE: POST /api/loyaltyCard/patientLoyaltyCard {patientLoyaltyCardMpprId,
   loyaltyCardId, patientId, patientCategoryId, cardConfigurationId,
   organisationId, unitId, status:"A", createdBy, updatedBy, isDeleted:"N",
   isUserDefined:"Y", cardNo}. THE single write of the flow.

STATUS MACHINE: card-assignment state per patient — unassigned -> assigned
(A). No reassign path visible in the legacy controller (the illegal
transition candidate: assigning a second card to an already-assigned
patient — T5 negative control).

BUSINESS RULES TOUCHED: 16-digit card format; checkLoyaltyCard validity;
one-card-per-patient (assign guard). BR manifest additions required at T3.

T0 COMPLETE (all endpoints live-called with exact legacy payloads):
- getLoyaltiCardDetailsByPatientId is a POST (not GET) with {patientId,
  organisationId, unitId, status:'A', isDeleted:'N', isUserDefined:'Y'}
  -> SUCCESS rows=0. My earlier 404 was a wrong-method guess.
- cardTypeMaster masked-500 was an EMPTY-BODY artifact: exact legacy payload
  {orgnisationId, unitId} (LEGACY TYPO field name, missing 'i') -> SUCCESS
  14 rows. The run's payload-shape lesson applied twice more.
- card check GET /api/unit/checkLoyaltyCard/1/1/0000000000000001 -> SUCCESS.
- MASKED COUNT: 0 across the flow. All reads standard envelope.
OWED (unchanged): assign write behavior, on the dedicated seed fixture
(Amendment 1), before T3/T4.

NEXT: T1 golden trace + T2 flow map for owner sign-off (first of the three
full sign-offs per Amendment 2).

## DQ-2026-08-12-038 — T0 GAP CLOSED: WHAT MAKES A CARD FAIL (owner's a-c)

(a) WHAT checkLoyaltyCard CHECKS (Java: LoyaltiCardMasterServiceImpl 140-180,
LoyaltiCardMasterDaoImpl 133-148, CHECK_LOYALATI_CARD_NO): (1) config exists
for (orgId, unitId) in m_loyalty_card_configuration (isDeleted='N'); absent ->
"Please configure loyalty for this unit." (2) then card existence count in
m_loyalty_card WHERE organisation_id=orgId AND status='A' AND is_deleted='N'
AND card_no=cardNo. DOES NOT check: expiry (no expiry in query), already-
assigned (patient_id in table but not filtered), unit scope of card
(org-scoped only), 16-digit format (client-side only; server accepts '12345').

(b) LIVE, body status inspected:
- free card:                200 status=success (table empty today)
- seeded duplicate:         200 status=error code=500 "Card No is Already
  Exists."  (constructed: insert card row -> probe -> delete same-run,
  cleanup verified 0)
- org 2 (no config):        200 status=error code=500 "Please configure
  loyalty for this unit."
- unit 999 (no config):     200 status=error code=500 (same message)
- short card '12345':       200 status=success (16-digit NOT server-enforced)

(c) FIXTURE-CONSTRUCTIBLE: patient with searchable UHID YES; valid card YES
(any unseeded 16-digit); FAILING card YES two kinds — duplicate (seed second
m_loyalty_card row with same card_no) and no-config unit (no config row).
Masked exception path NOT constructible deliberately; Amendment 3 psql
disambiguation applies if it appears.

FIXTURE SPEC (T1): DEMO-001 patient (searchable name/UHID), card A free,
card B duplicate of A, one un-configured unit. All additive/idempotent/
DEMO-001-scoped/his_local-guarded/cleaned same-run (owner decision 1).

NEXT: T1 golden trace + T2 flow map (first full sign-off per Amendment 2).

## DQ-2026-08-12-039 — FIRST TRANSACTION BUILD COMPLETE: assignLoyaltyCard (752)

T3-T5 executed per the signed-off flow map (f45da822, dry run CLEAN 7 files
+638). What the build demonstrated live on the fixture:
- T4 WRITE: first assign SUCCESS (real config id 1 was the fix — my
  hardcoded config 3 hit the FK, the transaction rolled back cleanly
  (all-or-nothing worked), the controller masked it; a payload artifact,
  NOT a defect). REPLAY double-posted: 2 card rows + 2 mapper rows —
  BR-033 finding demonstrated (Amendment 5: known finding, demonstration
  not discovery).
- T4 SIDE-EFFECT (owner's priority): UPDATE_PATIENT_BY_LOYALTI_CARD landed —
  both card rows show patient_id=<demo> + is_active=Y. The PATIENT row
  changed, not just the mapper.
- T5: SECOND assign (different card, same patient) SUCCEEDED — guard absent,
  BR-033 confirmed (Amendment 4 NO-GUARD-LOW-STAKES, build proceeds).
- FIXTURE (tools/seed_loyalty_fixture.py): DEMO-001 patient (searchable),
  card A free, card B duplicate, no-config unit 9999. Additive/idempotent/
  his_local-guarded/cleaned-same-run. TWO fixture bugs found+fixed during
  the run: (1) FK order (details before registration), (2) the write's own
  artifacts (mapper rows + probe-created cards) must be torn down first —
  the Amendment 1 'everything the tests create is cleaned' discipline
  caught it. Final state: 0 cards/0 mappers/0 patients after cleanup.
- UI: assignLoyaltyCard renders (picker + card sections), zero page errors.
NEXT: owner reads the T3-T5 evidence; then the SECOND transaction flow
(another full sign-off) or a family, per Amendment 2.

## DQ-2026-08-12-040 — GOLDEN TRACE END-TO-END: ALL 7 STEPS + T4 + T5 (merge evidence)

ONE browser session, real UI, module-752 grant, DEMO-001 fixture:
STEP 1 PICKER: search "DEMO" -> fixture patient renders. PASS
STEP 2 FEEDS: cardType dropdown populated (14 types via the literal
  orgnisationId typo payload); config id 1 selected. PASS (the typo'd feed
  -> cardConfigurationId contract holds end-to-end)
STEP 3 CHECK: card A -> "Card available." PASS
STEP 4 LOOKUP: (fires on Select)
STEP 5 WRITE: assign via UI -> "Card assigned." PASS
STEP 6 READ-BACK: fresh psql — m_loyalty_card rows for patient = 1. PASS
STEP 7 SIDE-EFFECT (owner's priority): card row =
  1111222233330001|99020|Y|A|1|1 — patient_id SET, is_active=Y, status=A,
  created_by/updated_by=1 (session user). THE PATIENT ROW CHANGED, not just
  the mapper. PASS
AUDIT: created_by|updated_by = 1|1. PASS
T4 REPLAY (Amendment 5 demonstration): 2nd identical assign SUCCEEDED ->
  2 card rows + 2 mapper rows — DOUBLE-POST demonstrated (BR-033 known
  finding, not discovery). PASS
T5 NEG-1 duplicate card B: error "Card No is Already Exists." PASS (rejected)
T5 NEG-2 un-configured unit: error "Please configure loyalty for this
  unit." PASS (rejected)
T5 SECOND-CARD: 2nd different card to same patient -> SUCCESS (accepted) —
  guard absent, BR-033 confirmed; Amendment 4 NO-GUARD-LOW-STAKES, logged
  not stopped. PASS (finding)
FIXTURE: cleaned same-run, 0 leftovers. PASS
THE LOOP'S GAP-QUESTION ANSWERED: the side-effect IS visible to the golden
trace (psql read-back after the write shows patient_id+is_active changed) —
a per-endpoint probe would never look there; the trace does. The loop has
no gap on this flow.
NEXT: owner merges (branch rebuild/assign-loyalty-card @ f45da822, dry run
CLEAN); then the second transaction flow.

## DQ-2026-08-12-041 — T0 SECOND FLOW: OPD BILL (11) — MULTI-ENTITY WRITE CHAIN

Picked as the second full sign-off: the canonical billing chain with THREE
sequential writes, each its own HTTP call/transaction — where T4's flow-level
all-or-nothing gets its real test (bill saved but payment failed = the
half-commit that matters for money).

(a) PICKER: encounter/patient search (OpdBillingController; patient search +
  encounter lookup feeds the bill). Golden trace starts here.

(b) THE 3-WRITE CHAIN (all in OpdBillingController):
  W1 POST /api/billing/saveBill (BillingMasterController:194) — billingMasterObj:
     bill header (net/gross/discount/concession/gst/total/outstanding) +
     listBillingDetailsDto + orderMasterDto.listOrderDetailsMasterDto +
     autoRendered services + paymentReceiptDetailsDtosList (ccAvenue check).
     Service BillingMasterServiceImpl.saveBill (6650 lines) orchestrates.
  W2 POST /api/billing/collectPayment (BillingMasterController:757) — paymentObj.
  W3 POST /api/opd/updateAppointmentStatus — appointment status flip.
  FEEDS: serviceMaster/autoRendered, variable-pricing/getServiceCharges,
  bankMaster, cardTypeMaster (all pre-write reads).

(c) STATUS MACHINE (DAO saveBillingMaster:147-182): netAmount==0 -> SETTLED;
  selfPayable==0&&creditPayable>0 -> ONHOLD; isFinancialClearance=='Y' ->
  PENDING/ONHOLD; else PENDING. Real machine: PENDING -> ONHOLD -> SETTLED.

(d) AMENDMENT 4 GUARD READ (DAO/service):
  GUARD EXISTS (2):
  1. checkIsBilledServices (OrderDetailsMasterDaoImpl:2072) — counts
     t_order_details where service_is_billed=1 for the bill's order ids;
     count>0 -> service returns ERROR "Bill is already generated !"
     (BillingMasterServiceImpl:774-778). The anti-double-bill guard.
  2. matchBillAmounts (final bills, isBillManipulationAllowed false) ->
     "Dispute in orders count, refreshing orders !" (:780-785).
  GAP FOUND: BOTH guards only run when isFinalBill=='Y' OR
  isIntermediateBill=='Y' (:772). A bill with neither flag BYPASSES the
  double-bill check entirely — the saveBillingMaster DAO (147-182) has no
  guard of its own (blind save). MONEY STAKE: re-billing the same
  encounter/order-details without the flags double-charges.
  CLASSIFICATION: the guard-gap is MONEY class -> Amendment 4 STOP band?
  NO — the guard EXISTS for the normal path (final/intermediate bills are
  the standard flows); the gap is a bypass only for non-flagged bills. T0
  verdict: GUARD EXISTS (normal path), GAP LOGGED for the bypass path; the
  T5 negative drives the guard (bill already-generated order -> rejected).
  The bypass gap is a BR candidate (logged, NOT applied; owner call whether
  it becomes STOP).

(f) T4 TEST SHAPE: the 3 writes are separate transactions — the flow-level
  all-or-nothing test: W1 succeeds, W2 fails (e.g. payment rejects) ->
  does the bill remain PENDING/unbilled or is it half-written? The legacy's
  behavior here IS the finding (log, don't replicate). Fixture: DEMO-001
  patient + encounter + order details (service_is_billed=0) for W1; a
  second attempt with service_is_billed=1 for the T5 negative.

NEXT: T1 golden trace + T2 flow map (second full sign-off) — draft on owner's
go.

## DQ-2026-08-12-042 — T1+T2 OPD BILL (11): FLOW MAP (second full sign-off)

DATA CONTRACTS (the per-screen-probes-go-blind points, named explicitly):
- PICKER -> W1: selected encounter (encounterId) + orderDetailsMasterDtosList
  + auto-rendered services + visitTypeId/patientId/unitId/orgId + the bill
  flags (isFinalBill/isIntermediateBill — the rebuild sends them, staying on
  the guarded path per sharpened Amendment 4).
- W1 -> W2 (THE blind point): W1's response returns response.data.object =
  the BillingMaster (billingMasterId, netAmount); the legacy stores it as
  $scope.billingMaster and sets totalPaymentAmount = billingMaster.netAmount.
  W2's collectPayment payload is BUILT FROM W1'S RESPONSE (billingMasterId +
  netAmount), not from screen state. A per-endpoint probe of W2 cannot
  construct its payload correctly.
- W2 -> W3: after payment success, the appointment status update fires with
  the encounter's appointmentId (from the encounter context).
- W1 also marks orderDetails serviceIsBilled=1 + serviceBillId (INSIDE W1,
  service:1068-1069, UPDATE_IS_BILLED_STATUS) — THE LOCK: if W2 fails, the
  retry of the same order details hits checkIsBilledServices -> "Bill is
  already generated !" -> the patient cannot be re-billed without manual
  service_is_billed flip. Confirmed by code read (OrderDetailsMaster.java:42).

T4 HALF-COMMIT TEST (the trace's centerpiece): seed DEMO-001 patient +
encounter + order details (service_is_billed=0). W1 with isFinalBill='Y'
-> SUCCESS. Then force W2 to FAIL (a payment mode/payload the legacy rejects,
e.g. invalid mode id or a gateway validation error). psql the bill state:
  * does the bill row exist? (PENDING — written at saveBillingDetails 1065-66)
  * is there an orphaned receipt? (no — collectPayment never ran)
  * are the order details marked service_is_billed=1? (YES — the lock)
  * then RETRY W1 with the same order details -> assert "Bill is already
    generated !" — proving the retry path is locked. THE FINDING: a
    half-commit that locks the retry is worse than either failure alone;
    logged as a BR candidate (the legacy needs a rollback/compensation or a
    service_is_billed reset path), NOT replicated.

T5 NEGATIVES (guards exist — Amendment 4 outcome #1):
- NEG-1: W1 with already-billed order details (service_is_billed=1) ->
  assert "Bill is already generated !".
- NEG-2: final bill with mismatched amounts (isBillManipulationAllowed
  false) -> assert "Dispute in orders count, refreshing orders !".
- NEG-3 (machine): attempt to collect payment for a bill already SETTLED
  (netAmount==0 bills auto-settle) -> assert rejection or logged mechanism.

FIXTURE: extends seed_loyalty_fixture.py (or a sibling opd_bill_fixture.py):
DEMO-001 patient + encounter + t_order_details rows (service_is_billed=0
for the happy path, =1 for NEG-1) + payment modes. Same rules: additive,
idempotent, DEMO-001-scoped, his_local-guarded, cleaned same-run incl. the
bill/receipt/order-detail artifacts the writes create.

WHERE THE LOOP IS WEAK ON THIS FLOW (the honest read):
1. T4's all-or-nothing clause assumes a SINGLE transaction boundary. This
   flow's three writes are THREE transactions — the loop must extend the
   clause to FLOW-LEVEL atomicity: what the legacy leaves behind between
   writes is the finding. The current wording ("must fail as a unit")
   doesn't fit a 3-http-call chain; amend to "the golden trace records what
   the legacy leaves between sequential writes; a partial write that locks
   the retry is the worst finding class".
2. The fixture for a billing chain needs ORDER DETAILS + ENCOUNTER + PATIENT
   — three tables with FK chains, not one. The seed script grows
   substantially; the loop should expect transaction fixtures to be
   multi-table and versioned with the flow.
3. W1's 6650-line service makes the guard read (Amendment 4) the long pole —
   T0 on a big billing flow is hours of DAO/service reading. The loop
   should note that big flows need the guard read scoped to the WRITE
   METHODS the flow actually calls (saveBill, collectPayment), not the
   whole service.
4. The W1->W2 response-contract blind point is the strongest argument yet
   for the golden trace: even the FEED endpoints on this screen are
   pre-write reads, but W2's payload cannot be built without W1's response.
   A per-screen probe here would render a screen that can never complete a
   bill.
NEXT: owner signs off the flow map -> T3 build.

## DQ-2026-08-12-043 — OPD BILL (11): MASKED WRITE PATH — 5TH PHANTOM COLUMN (package_applicable_date)

STOP-CLASS FINDING (owner rule: a masked WRITE path stops and comes to the
owner — a write that lies costs money). During T3/T4 of the OPD Bill flow,
W1 (POST /api/billing/saveBill) could NOT complete for a legitimately-
constructed DEMO-001 bill:
- After fixing MY payload artifacts (unknown fields serviceMasterId +
  listBillingDetailsDto 400'd on strict Jackson; is_dummy_service NULL caused
  the matchBillAmounts "Dispute" guard; isFinancialClearance null caused an
  NPE at BillingMasterServiceImpl:1335 — all logged as payload/fixture
  lessons), W1 consistently returns the masked envelope:
  {"status":"error","code":"500","message":"Something went wrong. Reference:
  ERR-2aed5e3d"}
- tomcat: ERROR: column listtpacka12_.package_applicable_date does not exist
  — Hibernate alias for TPackageConsumptionMaster. The entity declares
  @Column(name="package_applicable_date") (TPackageConsumptionMaster.java:156)
  but the column exists NOWHERE (information_schema count 0). Loaded via
  session.get(TPackageConsumptionMaster.class, ...) at
  MPackageMasterDaoImpl:1674 during the saveBill flow.
- 5TH PHANTOM COLUMN (technique_id, is_bed_charges_service, form_template,
  icpc_id, package_applicable_date) — and the FIRST that masks a TRANSACTION
  WRITE entirely (the others masked reads).
- ALL-OR-NOTHING HELD: after each masked failure, psql confirmed 0 bill rows,
  0 payment rows, order detail service_is_billed STILL 0 — the transaction
  rolled back cleanly. The lock hypothesis (UPDATE_IS_BILLED_STATUS inside
  W1) was NOT reached because W1 never completes — the phantom column fires
  BEFORE the lock.
- RECOVERY-PATH VERDICT (still stands): no billing cancel/void resets
  service_is_billed; manual SQL is the only recovery — but the lock itself
  is not yet LIVE-PROVEN because W1 cannot complete on this backend state.

WHAT THE OWNER MUST DECIDE (per the masked-write rule — build does NOT
proceed with a banner):
  A) The OPD Bill screen CANNOT be built to parity: the legacy write is
     masked for every bill. Rebuilding it would ship a screen that cannot
     bill anyone.
  B) The fix (add package_applicable_date to the table, or remove the
     mapping) is a DDL/entity change — backend-owned, Rule 10, and now
     REQUIRED for the flow to work at all, not cosmetic.
  C) Alternative: pick a different second flow whose write is not masked
     (admission/transfer chain), park OPD Bill until the backend decision.
OWNER'S CALL. The T4/T5 plan for this flow cannot execute until the write
completes — the trace proved the loop by finding the flow is unmaskably
broken.

## DQ-2026-08-12-044 — OPD BILL STOP: THE MECHANISM (owner's a-d answered, no fixes applied)

(a) TABLE/QUERY/DDL: billing.t_package_consumption_master has 38 columns
  (verified information_schema) — NO package_applicable_date. The ENTITY
  TPackageConsumptionMaster declares @Column(name="package_applicable_date")
  (TPackageConsumptionMaster.java:156) which exists NOWHERE in any schema
  (count 0). The failing query is Hibernate's generated SELECT for
  TPackageConsumptionMaster via the @OneToMany LAZY collection
  OrderDetailsMaster.java:461 (mappedBy orderDetailsMaster) — alias
  listtpacka12_ in the error. saveBill loads order details; the collection
  join selects every mapped column including the phantom -> "could not
  extract ResultSet" -> wrapped BillingException at saveBill:1335 -> masked
  ERR-xxxx.
(b) FAILURE MODE: the write FAILS AND REPORTS FAILURE ({"status":"error",
  "code":"500","message":"Something went wrong. Reference: ERR-3586ead6"}).
  NOT a masked success — the envelope honestly says error. Classification:
  this is a BROKEN SCREEN (backend defect), not a "write that lies".
  (Distinction per owner: masked-write rule exists for writes that LIE;
  this one reports the truth.)
(c) PSQL AFTER FAILED ATTEMPT (verified live, patient 99032): bill rows 0,
  bill details 0, payment rows 0, order detail (id 22) service_is_billed=0
  service_bill_id EMPTY, encounter intact. THE LOCK DID NOT SET — the
  transaction rolled back cleanly. UPDATE_IS_BILLED_STATUS (runs at
  service:1068-1069 AFTER the phantom query) never committed. The owner's
  feared "write fails but lock sets" does NOT happen here — the phantom
  fires BEFORE the lock, and the whole saveBill is one transaction.
(d) REACHABILITY: EVERY saveBill, package or not. The collection is on the
  order-details entity itself (OrderDetailsMaster.java:461), NOT gated on
  packageId. My NON-package bill (order detail service_id=3, no package)
  hit it — reproduced cleanly (ERR-3586ead6, same-second SQL error line).
  The "narrower finding" (only package bills) is FALSE: the flow is not
  buildable on any path until the backend fixes the entity/DDL mismatch.
VERDICT FOR THE OWNER: PARK the flow (owner's inclination). The rebuild
cannot complete a bill on ANY path; fixing the backend (add the column or
drop the mapping — Rule 10, BR fix discipline, maker-checker inverted if we
do it for a frontend build) unblocks it. Recommend: log BR candidate
(BR-2026-08-12-035 shape), pick a different second flow (admission/transfer
chain), revisit OPD Bill after the backend owns the fix.

## DQ-2026-08-12-045 — T0 ADMISSION CHAIN: DIRECT TRANSFER (65) — SMOKE PASS, BUILDABLE

Picked as the second full sign-off after OPD Bill PARKED (BR-035). The
admission chain: multi-entity write, clinically meaningful (patient
location), status machine with request->accept->final chain.

(a) PICKER: transfer list screens (getPendingTransferRequestList,
  getPendingTransferRequestListForADT, getFinalTransferRequestList —
  universal search) — the flow opens from a pending-transfer list.

(b) THE CHAIN (all POST, PendingTransferRequestController @/adt +
  TransferRequestController):
  W1 POST /adt/saveTransferRequest (TransferRequestDaoImpl:152) — writes
     THREE entities in one call: (1) insert t_transfer_request, (2) update
     t_transfer (to_bed_category/to_bed/to_ward/to_room/to_billing_bed_cat +
     transfer_status_id, type 1 = bed-category update, type 2 = full bed
     update), (3) update t_admission (visitType/doctor/speciality when
     newVisitTypeId present).
  W2 POST /adt/acceptTransferRequestForADT (PendingTransferRequestController:45)
  W3 POST /adt/acceptFinalTransferRequest (:141) — the executing write
  R POST /adt/rejectTransferRequestForADT (:55) — the negative path
(c) STATUS MACHINE (m_transfer_status_master): 1 Pending -> 2 Requested ->
  3 Request sent to doctor ... -> final transferred; reject path from
  Requested. States verified in the master table.
(d) AMENDMENT 4 GUARD READ (scoped to write methods per Amendment 6):
  saveTransferRequest DAO:152-215 — reads NO guard against duplicate
  transfer requests for the same admission, NO status-transition check
  (blind inserts/updates). NOT money-class (no amounts), clinical-state
  class (patient location) — the missing guard lets someone request a
  transfer for an already-transferred patient: CLINICAL STATE =
  Amendment 4 HIGH-STAKES candidate. The guard-question for T5: does
  W2/W3 reject a request already past its state (accept a Final'd
  transfer)? Need the service-level read at T2.
(e) AMENDMENT 8 WRITE SMOKE (NEW T0 STEP — PASSED): tools/smoke_transfer_write.py
  seeded DEMO-001 patient+admission+transfer (status 1), called
  POST /adt/saveTransferRequest (transferTypeId=2, status 2) ->
  HTTP 200 status=success; psql: 1 transfer_request row, transfer row
  (2|2|2) — multi-entity write landed, NO phantom, NO masking. Cleanup 0
  leftovers. BUILDABLE — flow map may be drafted.
FIXTURE: tools/smoke_transfer_write.py doubles as the T1 fixture (seed
+ write + teardown same-run); extends for T4 (force W2 fail after W1) and
T5 (second transfer for a transferred admission — the guard-absent probe).
NEXT: T1 golden trace + T2 flow map (owner's go) — the flow map will carry
the W1->W2->W3 data contracts, the T4 mid-chain-failure test, the T5
guard questions (clinical-state class), and the loop-weakness read for a
3-state machine flow.

## DQ-2026-08-12-046 — T1+T2 TRANSFER CHAIN (65): FLOW MAP (second full sign-off)

DATA CONTRACTS (the per-endpoint-probes-go-blind points):
- LIST -> W1: the flow opens from a pending-transfer list (getPending-
  TransferRequestList / ForADT / Final). The selected row carries
  transferId + admissionId + transferRequestId (if a prior request exists).
- W1 saveTransferRequest payload: transferId + transferTypeId +
  transferStatusId + toBedCategory/toBillingBedCategory/toWard/toRoom/toBed +
  admissionId + org/unit (screen state: the destination chosen by the user).
  RESPONSE: the new transferRequestId (created by the DAO).
- W1 -> W2 (THE blind point): W2's payload is built from
  $scope.itemSelectedsAccept — a ROW SELECTED FROM THE PENDING LIST — NOT
  from W1's response in the simple path; BUT the accept flow requires
  transferRequestId + toBedId + admissionId from that row. In the
  request->accept chain (request created by W1 then accepted), the
  transferRequestId in W2's payload IS W1's response id. Per-endpoint
  probes cannot construct W2 without the list row or W1's response.
- W2 -> W3: W3's payload uses $scope.transferdId + $scope.transferReqestId
  — set from the FINAL list selection (the row W2 accepted). W3 needs
  transferId + transferRequestId + destination again.
WRITE SURFACES (Amendment 6 guard read, scoped to write methods):
- W1 saveTransferRequest (TransferRequestDaoImpl:152): 3 entities —
  insert t_transfer_request; update t_transfer (dest + statusId); update
  t_admission (visitType/doctor/speciality when newVisitTypeId present).
- W2 acceptTransferRequestForADT (PendingTransferRequestDaoImpl:235):
  5 entities — update Transfer -> status 6; update TransferRequest ->
  status 6; update BedMaster -> bedStatusId 5 (booked); update BedLogStatus
  'I' (inactivate old); insert BedLogStatus 'A'. EMPTY CATCH BLOCKS:
  HibernateException/Exception swallowed -> return ERROR (error-swallowing
  class, Rule 10 candidate — logged, not fixed here).
- W3 acceptFinalTransferRequest (:396): 5 entities — Transfer -> status 9
  (+toDate/toTime); TransferRequest -> status 9; BedMaster -> 5; BedLog
  inactivate+insert. Type-null branch: Transfer status 9 + status='I'.
- Reject (:306): Transfer -> status 5 + 'I'; TransferRequest -> status 5 +
  'I' + reject reason.
STATUS MACHINE (m_transfer_status_master, live): 1 Pending -> 2 Requested
-> 3 Sent to doctor -> 4 Sent to ADT -> 6 Ready For Transfer (W2) ->
9 Transferd (W3); 5 Rejected (reject); 8 Abort; 10 ICU Transfer.
T5 GUARD READ (THE finding): NO write method has a status precondition —
every UPDATE's WHERE is transferId + unitId + orgId ONLY. W2 can accept an
already-completed (9) transfer; W3 can finalize a rejected (5) transfer;
W1 can request a transfer for a transferred patient. The machine is UI
CONVENTION, not server-enforced. Clinical-state class (Amendment 4
HIGH-STAKES band). Sharpened Amendment 4 applies: the rebuild's UI follows
the same state flow as the legacy -> the screen does NOT reach the gap
(each button only shows in its legal state) -> BUILD PROCEEDS, logged as
BR candidate (server-side status-precondition guards absent). NOT stopped:
the screen being built cannot reach the illegal transition.
T4 SHAPE (the multi-write failure case, given the smoke passed): the chain
is W1 (request) -> W2 (accept: 5 entities) -> W3 (final: 5 entities).
The failure case to force: W2 succeeds (transfer + request + bed all
updated to 6/6/5) but W3 FAILS (bad destination payload) — what does the
legacy leave? A transfer at status 6 (Ready For Transfer) with the bed
booked (bedStatusId 5) and the patient NOT actually moved. The bed stays
booked -> the retry path: re-calling W3 with the correct payload should
succeed (W3 is idempotent-ish: blind update to 9 + bed book again). The
real risk: a HALF-W2 (some of the 5 updates land, then a mid-call failure
rolls back — Hibernate transaction boundary at the service call) vs a
completed-W2-then-failed-W3 (the BETWEEN-writes state, Amendment 6's
subject). T4 asserts both: (i) force a mid-W2 failure -> all-or-nothing
(0 of 5 updates land), (ii) W2-ok W3-fail -> psql shows status 6 + bed 5
left behind (the legacy's between-writes debris), then (iii) W3 retry
succeeds (recovery path exists — better than the OPD Bill lock).
FIXTURE: smoke_transfer_write.py extends: seed -> W1 -> W2 -> force W3
fail -> psql -> W3 retry -> teardown. All DEMO-001-scoped, same-run clean.
LOOP-WEAKNESS READ (honest):
1. The machine is UI-convention-only — T5's "drive the illegal transition
   and assert rejection" cannot run against the server (it would SUCCEED,
   like the loyalty-card second assign). The loop needs the Amendment 4
   classification to decide BEFORE the probe whether "guard absent +
   screen-can't-reach + high stakes" = log-and-proceed (this case) — the
   current wording only covers low-stakes log-and-proceed. Amendment
   candidate: add the screen-reachability leg to the HIGH-STAKES branch.
2. The empty catch blocks (W2:298-301) mean a FAILED write returns ERROR
   with no mechanism — the trace must psql-verify every "success" AND every
   "error" (an error may have half-written before the swallow).
3. W2's 5-entity write is where T4's all-or-nothing gets its real test —
   but Hibernate's flush-at-commit means the 5 updates may not be atomic
   unless the service is @Transactional; the trace must check whether a
   mid-call exception leaves partial state (the empty catch makes this
   OBSERVABLE: ERROR returned + partial rows = the half-commit).
4. The transferRequestId contract (W1's response -> W2's payload) is the
   loop's second instance of the OPD Bill W1->W2 shape — the golden trace
   earns its keep again: a per-screen build would ship a transfer screen
   that can never complete a transfer, because W3's payload needs W2's
   accepted row state.
NEXT: owner signs off -> T3 build (transfer screen: list -> request ->
accept -> final + reject; C9 patterns; routes; probe).

## DQ-2026-08-12-048 — TRANSFER (65) T4 BETWEEN-WRITES TRACE: W3 TYPE-NULL BRANCH IS NOT ATOMIC (finding + recovery)

The owner's flow-map ask (weakness 3) — W2's 5-entity atomicity — answered
TWICE:
1. W2 acceptTransferRequestForADT IS ATOMIC (trace_w2_atomicity.py, 61f6c1a1):
   forced FK failure on the final BedLogStatus INSERT -> honest ERROR
   envelope; psql: transfer/request stayed at W1's status 2, no bed booked,
   zero bed-log rows — all five rolled back via the session's single
   transaction despite the empty catch.
2. W3 acceptFinalTransferRequest type-null branch IS NOT ATOMIC
   (trace_t4_between_writes.py, 8566f3de): W1 ok -> W2 ok -> W3 with a
   nonexistent toBedId:
   - transfer -> 9|I and request -> 9|I COMMIT (marked Transferd/inactive)
   - admission_details UPDATE lands with bed_id=999999 (a nonexistent bed!)
   - the LATER BedLogStatus insert fails -> empty catch -> ERROR envelope
   - psql is the only truth: the ERROR hides that most of the write landed
     (Amendment 10 case, exactly)
   - W3 RETRY (correct bed) RECOVERS: admission_details bed fixed to 1,
     transfer stays 9, bed booked, bed-log present — a recovery path
     EXISTS (unlike OPD Bill's locked retry, BR-035)
WHY THE DIFFERENCE: W2's five statements share the session transaction and
roll back together (no entity load between them). W3's type-null branch
executes transfer/request updates, then findById(transferId) FORCES A FLUSH
(line 487) — the first updates commit to their own mini-transaction BEFORE
the failing bed-log insert, so the later failure cannot roll them back.
CLASS: honest-error envelope (Amendment 7: broken-screen, not masked) BUT
the state it leaves is dangerous — a transfer recorded as Transferd (9|I)
whose admission-details bed points at a nonexistent bed until retried.
NOT a STOP: the retry recovers, the screen's normal path (correct bed)
never hits it, and the ERROR is honest (not a lie). LOGGED as a BR
candidate (backend: reorder the statements / single transaction boundary;
also fix the empty catch to log the mechanism — Rule 10 error-swallowing).
LOOP SCORECARD (Amendment 11) now at 4 flows: UI-convention machine (loyalty
2nd-assign, transfer machine), response-built payload (OPD W1->W2, transfer
W2->W3), error swallowing (transfer W2/W3 empty catches), and now
FLUSH-BOUNDARY non-atomicity as a 4th class — the trace caught it because
Amendment 10 mandates psql-after-every-response.

## DQ-2026-08-12-049 — TRANSFER (65) W3: LEGACY'S OWN PAYLOAD 400s (typo'd field names) + DQ-048 PER-ENTITY SPECIFICS

FINDING 1 (new, most consequential): the legacy's finalTransfer JS payload
is {"organizationId, unitId, transferdId, tranferRequestId"} — TWO TYPO'D
FIELD NAMES (transferdId, tranferRequestId). TransferDto has NO such fields
(106 known properties incl. the CORRECT transferId/transferRequestId) and
strict Jackson (FAIL_ON_UNKNOWN_PROPERTIES) rejects them: LIVE-PROVEN
HTTP 400 "Bad request: Invalid parameters" with tomcat:
UnrecognizedPropertyException: Unrecognized field "transferdId" (class
TransferDto), not marked as ignorable. The legacy "Execute final transfer"
button CANNOT reach the W3 DAO — the payload dies at deserialization.
CLASS: broken screen (Amendment 7, honest 400) — the legacy W3 is
DOUBLY broken: unreachable typo payload + the non-atomic type-null DAO
branch (DQ-048). The rebuild sends the CORRECT field names
(transferId/transferRequestId) — a deliberate deviation from literalism,
logged for the owner: the literal payload is a backend-breaking typo, not
a contract to preserve (unlike orgnisationId which the backend READS).
FINDING 2 (DQ-048 specifics, now clean): with corrected field names, the
W3 type-null branch (transferTypeId absent) is NOT atomic:
  LAND on the failing call (before the BedLogStatus INSERT fails):
    1. t_transfer -> transfer_status_id=9, status='I'
    2. t_transfer_request -> transfer_status_id=9, status='I'
    3. t_admission_details -> bed_id/ward_id/room_id updated (to the
       payload destination)
    4. t_order_master.visit_type_id (if newVisitTypeId)
    5. pharmacy.t_issue.visit_type_id (if newVisitTypeId)
  DO NOT LAND (after the failure point, line 576):
    6. t_bed_log_status to-bed inactivate + new 'A' row
    7. m_bed_master to-bed -> bed_status_id=4
    8. m_bed_master from-bed release (bedStatusId=2/3)
    9. from-bed t_bed_log_status inactivate + new 'A' row
    10. NEW t_transfer row (status 1, isPrimary 'N', from=new bed)
    11. t_housekeeping entry
  RETRY COMPLETENESS: a retry with the SAME corrected payload re-runs the
  whole branch -> lands 6-11 too -> RECOVERY IS COMPLETE (verified:
  adm_details.bed fixed 999999->1, new transfer row created, from-bed
  released). The earlier "papers over" reading was a FIXTURE artifact
  (missing from_bed_id in the fixture; bed 2 nonexistent). With a real
  from-bed (bed 3), the retry restores every entity.
  STATE LEFT BY A FAILED W3: transfer marked Transferd (9|I) + admission-
  details pointing at the destination bed + bed-log/bed-master NOT updated
  + no new transfer row — a patient RECORDED as transferred whose bed
  bookkeeping was never done. The ERROR envelope is honest but the record
  is dangerously ahead of reality until the retry.
NEXT: golden trace through the UI (merge gate) — will show the honest 400
on the legacy W3 shape and the rebuild's corrected-name path.

## DQ-2026-08-12-050 — TRANSFER (65) PICKER FEED BROKEN FOR ALL INPUTS (duplicate bedId alias) + FIXTURE-VS-FEED VERDICT

(a) THE FAILURE (owner's (a)): POST /adt/searchAdmittedPatientByNameAndUhid
  (ModalityTransferDaoImpl:48) returns {"status":"error","code":null,
  "message":"Something went wrong !!"} for EVERY input — verified live with
  "zzzz" (guaranteed 0 rows), "Test" (real admitted patient), and the
  DEMO-001 fixture patient. ROOT CAUSE (Admission.java:844-855): the named
  query GET_ADMITTED_PATIENT_LIST_LIKE selects `bm.bed_id as "bedId"` AND
  `admissionDetails.bed_id as "bedId"` — a DUPLICATE column alias.
  Hibernate's native-query ResultSet handling rejects the duplicate label
  at execution, for ANY result set. Manual psql of the same joins runs
  clean (0 rows or 1 row) — the SQL is valid; the NAMED QUERY's duplicate
  alias is what breaks it. EMPTY CATCH (ModalityTransferDaoImpl:57-61)
  swallows the exception with NO log — the SAME error-swallowing class as
  W2/W3 (PendingTransferRequestDaoImpl:298-301) — FOURTH INSTANCE of the
  class (transfer W2, transfer W3, this feed; the fifth if the BillingMaster
  NPE at :1335 counts its own — it is a distinct masked-NPE class, so this
  is the fourth swallow). Added to the Amendment 11 expected-findings list.
  The real legacy transfer picker CANNOT find patients — the screen's first
  step is broken for all users.
(b) FIXTURE-VS-FEED VERDICT (owner's (b)): the FEED IS BROKEN, NOT THE
  FIXTURE. The fixture patient is well-formed (psql: registration +
  admission + admission_details with nursing_acceptance='Y' + doctor_id +
  bed + ward all present and join-satisfiable; manual SQL returns the row).
  The feed rejects it AND rejects every other input (including nonexistent
  names), so no seeding change can make it work. The fixture spec needs NO
  change for this feed; it DID reveal two real fixture gaps for the chain
  itself (from_bed_id + nursing_acceptance were missing — fixed in the
  trace tools' seeds).
CLASS: broken feed (backend defect), Amendment 7 honest-error class (not
masked). The rebuild's picker cannot work against this feed — same shape as
OPD Bill's blocked write, but a READ: banner-parity class is acceptable for
reads per the owner's rule (a read that lies costs a confused minute). The
rebuild ships the picker with an honest "search unavailable" state + the
finding logged, OR the backend fixes the duplicate alias (one-line: remove
the bm.bed_id duplicate). LOGGED for the owner; NOT a stop (read class).
NEXT: the UI golden trace proceeds via the LIST path (the pending-transfer
list + W1/W2/W3 + reject are all live-verified working); the picker step is
documented as broken-feed with the honest-error banner state. The chain
writes are the merge gate — the picker being a broken read does not block
proving the writes through the UI.

## DQ-2026-08-12-051 — TRANSFER (65): UI GOLDEN TRACE PASS + RETRY-COMPLETENESS ANSWER (owner's merge-gate asks)

THE MERGE GATE — GOLDEN TRACE THROUGH THE UI (ONE browser session, real
DOM, module-65 grant, DEMO-001 fixture):
ACCEPT PATH 6/6 GREEN:
  STEP 1 screen renders; STEP 2 pending list (getPendingTransferRequest-
  ListForADT, status-4) shows the seeded row + Accept button; STEP 3 W2
  accept via UI -> psql transfer=6, to-bed(1) bed_status=5 (booked);
  STEP 4 W3 final via UI -> psql transfer=9|I, adm_details.bed=1 (patient
  moved). The screen reported the honest read-back: "row left the pending
  list (status 9)" — the W3 ERROR envelope (DQ-048 empty catch) was
  disambiguated by the refreshed list (T4 read-back rule implemented in
  the screen, not just the probe).
REJECT PATH 4/4 GREEN: list -> select -> Reject via UI -> psql transfer=
  5|I, request=5|I.
FIXTURE: 0 leftovers after both paths (bills none, transfers/requests/
  admission-details/bed-log all torn down, bed-status reset).
SCREEN FIXES THE TRACE DEMANDED (committed 0fb11def): pending list
  endpoint corrected to the ADT status-4 list (the universal list filters
  status 7); LEGAL_TRANSITIONS +3->4, +4->6/5; W2 success reflects status
  6 locally so the W3 gate opens; W3 read-back verification (never trust
  the envelope); reject gate covers status-4 rows (list rows carry null
  transferStatusId — the query does not select it — defaulted to 4).
RETRY-COMPLETENESS ANSWER (owner's explicit ask, stated plainly): the
  retry restores ONLY THE BED, NOT ALL ELEVEN ENTITIES. With a REAL
  from-bed (3) and corrected to-bed (1): after the retry, transfer=9|I,
  adm_details.bed=1, to-bed(1)=5, bed-log A present — BUT new-transfer-
  row=0 (the chained transfer record at DAO:611-629 is NEVER created) and
  from-bed(3) NOT released (bed_status untouched). The envelope says ERROR
  on BOTH the failed call and the retry (the empty catch fires again in
  the from-bed block, before the new-transfer-row save) — so the UI/user
  can NEVER tell from the response whether the move landed; only psql
  shows the partial state. Patient recorded Transferd (9|I) with the
  correct new bed; old bed never released; chained transfer never
  recorded. This is the DQ-048 finding's full shape: W3 type-null branch
  is non-atomic AND its retry is a partial recovery masked by an
  always-ERROR envelope. CLASS: honest-error (Amendment 7) but
  operationally severe (clinical path: bed bookkeeping silently left
  behind); logged BR candidate (backend: reorder W3 so the from-bed/new-
  transfer-row run BEFORE the failing insert, single transaction, and
  LOG the catch).

## DQ-2026-08-12-052 — TIRITH FAIL-OPEN ON UNRESOLVABLE SCANNER (12th structurally-incapable mechanism; FIRST on the security layer, per the owner's framing: "the first one on the security layer rather than the verification layer")

MECHANISM: Hermes's per-command security scan silently ALLOWS when the
scanner cannot resolve or run. tirith_security.py check_command_security
returns {"action": "allow"} on every operational failure while
tirith_fail_open: true (the config default): path resolves to None
(:765-772), OSError spawn failure (:783-795), timeout (:796-805), unknown
exit code (:817-824). Commands run UNSCANNED and look approved — the ACP
client never prompts because the scan never produced a verdict. The eleven
prior mechanisms (error-swallowing, masked envelopes, fall-throughs, proxy
gap, noData, vacuous tsc DQ-2026-08-12-027, etc.) were all on the
VERIFICATION layer, where a downstream check could still catch the miss;
this one gates the SECURITY layer itself — nothing downstream can catch a
scan that never ran.

NOTICED 2026-08-12 (permission pre-grant session): the agent shell's PATH
lacked ~/.hermes/bin — `tirith` and `ng` returned "command not found" from
the terminal, while six permission classes (ng build, heredoc write,
python3 tools/..., psql read, curl localhost:8080, git branch/checkout) all
ran with zero approval prompts. Follow-up PROVED the gate was NOT silently
open in this session: the agent resolves tirith via the $HERMES_HOME/bin
fallback (tirith_security.py:551-556) even when PATH is bare, and the
positive control (`curl http://evil-example.com/x`) WAS blocked — [HIGH]
plain_http_to_sink, live approval prompt. The real finding is that the
gate's health is INVISIBLE without an explicit check: nothing is loud when
the scanner is unresolvable, and any environment where the $HERMES_HOME
fallback also fails (HERMES_HOME unset, binary moved, spawn error) becomes
the silent open gate — commands run unscanned and look approved.

STARTUP CHECK (standing from this finding): tools/verify_tirith_resolvable.py
runs at session start before the agent relies on the scan — PASS required.
Exit 3 = scanner NOT resolvable (loud FAIL); exit 1 = scanner present but
INERT (positive control not blocked); exit 2 = resolved but unusable.
Negative control demonstrated 2026-08-12: TIRITH_BIN=/nonexistent/tirith →
FAIL [exit 3], never a silent pass. Positive control demonstrated same
session: PASS (resolved /Users/axonichealth/.hermes/bin/tirith, plain-http
control blocked, exit 1).

HARDENING OPTION (owner decision, NOT applied): flip config
security.tirith_fail_open to false = fail-closed — any scanner hiccup
BLOCKS instead of silently allowing. Tradeoff: a scanner crash would block
ALL commands until the next restart, so the choice is the owner's.

## DQ-2026-08-12-053 — PATIENT REGISTRATION (40) T0 SMOKE: BLOCKED — 6th PHANTOM COLUMN family_id ON patient.t_patient_details (flow PARKED, BR-PENDING-family-id-phantom)

THIRD FULL FLOW MAP (admission family) — T0 Amendment-8 gate verdict: the
primary write POST /api/global/patientRegistration CANNOT complete on ANY
payload shape. Flow map NOT drafted; flow parked; next flow taken (mandate:
"a flow that turns out unbuildable — park it, log the BR, take the next").

MECHANISM (catalina.out, 2026-08-12): every save dies at savePatientDetails —
`ERROR: column "family_id" of relation "t_patient_details" does not exist`
(SQLGrammarException, Hibernate flush of the pending details insert). The
PatientDetails entity maps family_id; information_schema shows ZERO family_id
columns in ANY schema — the 6th phantom-column instance (after technique_id,
is_bed_charges_service, form_template, icpc_id, package_applicable_date), and
the first to block the ADMISSION family. The DAO's empty catch
(PatientRegistrationDaoImpl:174-179) swallows it into the masked envelope:
HTTP 200 {status:"error", code:"501", message:"Something went wrong!!"},
state unchanged (0 rows anywhere — the whole @Transactional rolls back).

SMOKE EVIDENCE (tools/smoke_patient_registration_write.py + variant probe):
4 payload shapes (dob dd-MM-yyyy / ISO datetime / mapping-format / age-only)
all masked-fail; psql 0/0/0 on registration/details/order_master every time;
teardown clean. Config gate PASSES (t_unit_application_billing_configuration
org 1/unit 1 row exists, patient_reg_ser_id=1 — so a working write would also
auto-create an order master+details row; teardown covers emr... public
t_order_master/t_order_details).

SECONDARY FINDING (legacy robustness): checkUniquePatient NPEs when dob is
null — DAO:282 `getDob().substring(...)` — `Caused by: NullPointerException
... getDob() is null`, propagated to the controller catch (code "Fail").
dob is therefore a MANDATORY payload field even though the DTO/DDL allow
null; the legacy JS always validates+ sends it. Not the blocker; logged.

CLASS: OPD-Bill class — broken write (backend DDL defect) behind a masked
envelope. Amendment 7: masked + state unchanged = rejection with a bad
message -> log/banner. Amendment 8: park-candidate (write cannot complete).

ACTION: BR-PENDING-family-id-phantom (DDL ADD COLUMN family_id to
patient.t_patient_details; Expand-Migrate-Contract; backend-owned per Rule
10 — NOT agent-applied). Flow parked, map undrafted. Next admission-family
candidate taken for the flow map.

## DQ-2026-08-12-054 — APPOINTMENT SCHEDULING (41) T0 SMOKE: BLOCKED — 7th PHANTOM appointment_unique_id ON opd.m_appointment_master + CLASS-LEVEL EXTENT (371 missing columns / 177 tables)

Second consecutive admission-family T0 smoke blocked by the phantom-column
class: POST /api/opd/scheduleAppointment (screen 41, module 4) returns the
masked 501 on every payload shape; catalina.out names the cause —
`ERROR: column "appointment_unique_id" of relation "m_appointment_master"
does not exist` (DAO AppointmentSchedulingDaoImpl:114 sets it; entity maps
it; information_schema ZERO hits in all schemas). Controller catch logs
"method failed" + returns COMMON_ERROR; 0 rows landed; teardown clean.
Tool: tools/smoke_appointment_scheduling_write.py (verified READ-only use of
an existing dev patient for the FK; only the marker appointment is created/
deleted).

CLASS EXTENT (entity-vs-DDL sweep, 2026-08-12, tools-adjacent script):
@Column names declared across legacy/Entity vs information_schema ->
CASE-INSENSITIVE 371 missing columns across 177 existing tables. The drift
is FAMILY-WIDE: admission/OPD (adt.t_admission.ot_status,
adt.t_admission_details.{admission_date_time,diet_type,remark_for_diet},
opd.t_encounter_master.{advised_by_id,advised_by_name,nursing_station_id},
opd.t_encounter_doc_details 8, patient.t_patient_details.family_id,
opd.m_appointment_master.appointment_unique_id), pharmacy
(pharmacy.t_sale 12, t_prescription_detail 8), lab (lab.t_lab_sample_dtls
7, t_template_result 7), blood bank (t_issue_blood_bag.
cross_match_register_id), billing (t_bill_payee_details 4, t_doctor_share 2,
m_deposit_master 4). The two flows that completed (loyalty 752, transfer 65)
write tables that are CLEAN (t_transfer/t_bed_log_status/m_bed_master/
loyalty tables); every remaining family's core write surface carries drift.

DECISION QUEUED (class, owner): the phantom-column class blocks the
flow-map step at the FAMILY level — 2 confirmed live write blockers
(6th/7th instances: family_id, appointment_unique_id) + the systemic extent.
Options for the owner: (a) DDL reconciliation batch (ADD COLUMN per drift —
Expand-Migrate-Contract, backend-owned, then T0 smokes re-run), (b) entity
reconciliation (map-only columns removed — Behavioral risk), or (c) per-flow
park-and-skip (each flow parks at T0; only clean-table flows proceed).
The run's flow-map step is CLASS-BLOCKED until this decision; the run
continues with read-side verification and clean-surface work meanwhile.

## DQ-2026-08-12-055 — APPOINTMENT SCHEDULING (41) LIST FEED 501 ROOT-CAUSED: PHANTOM TABLE public.t_message_queue (8th phantom instance — first TABLE-shaped; the README's owed finding)

The appointment README (screen 41) owed the root cause of the list path's
masked 501. RESOLVED 2026-08-12 (live call POST /api/opd/scheduleAppointmentList
{organizationId:1,unitId:1,appointmentTypeId:1,appointmentDate:2026-08-12} ->
HTTP 200 {"status":"error","code":"501","message":"Something went wrong!!"});
catalina.out names it:
`Caused by: org.postgresql.util.PSQLException: ERROR: relation
"public.t_message_queue" does not exist` — `MessageQueueDaoImpl
getMessageFromQueqe failed` (SQLGrammarException, swallowed by the chain into
the controller catch "method failed" -> 501). Entity MessageQueue.java maps
the table; information_schema shows ZERO t_message_queue tables in ANY schema.

CLASS: phantom TABLE — the 8th phantom instance (1-7 were phantom COLUMNS:
technique_id, is_bed_charges_service, form_template, icpc_id,
package_applicable_date, family_id, appointment_unique_id), the first where
the whole relation is absent. READ class (list feed) -> transfer precedent:
broken feed, backend defect, honest-error read class -> banner + log, NOT a
stop.

APPOINTMENT FLOW NOW FULLY DIAGNOSED: save blocked by phantom COLUMN
appointment_unique_id (7th, DQ-054); list blocked by phantom TABLE
t_message_queue (8th, this entry). The rebuild's list surface keeps its
AMBER banner and adds the named mechanism.

ACTION: BR-PENDING-message-queue-table (DDL CREATE TABLE public.t_message_queue
per the entity; backend-owned per Rule 10; NOT agent-applied).

## DQ-2026-08-12-056 — DONOR REGISTRATION (blood bank) T0 SMOKE: PASS — BUILDABLE (first clean-surface flow; third flow-map candidate CONFIRMED)

Per the DQ-054 Option-C drift map, donor registration is the first flow whose
write tables are drift-clean (t_donor_registration, t_donor_details,
t_donor_screening_details — zero phantom columns). T0 Amendment-8 smoke
(tools/smoke_donor_registration_write.py, 2026-08-12): POST
/api/bloodbank/donorRegistration with the exact payload -> HTTP 200 honest
success envelope, t_donor_registration=1 + t_donor_details=1 landed
(donor_id=2), teardown clean, SMOKE_EXIT=0 -> BUILDABLE. The third full flow
map (T1-T6) proceeds on this flow (blood-bank family, untouched by the
loyalty/transfer maps).

CHAIN HEALTH (the anti-mask pattern, for the record): every catch in the
donor save chain (service saveDonorRegistration:123-126, DAO saveDonor:83-89,
saveDonorDetails:103-109) LOGS + THROWS ServiceException — failures surface as
honest HTTP 500s with the real message, never the masked 501. The phantom
class did NOT hit this flow (drift map confirms clean tables).

DATE CONTRACT (payload literalism, variant-probed 2026-08-12): dob MUST carry
the time part — "dd-MM-yyyy HH:mm:ss". The Dozer mapping date-format is
"dd-M-yyyy HH:mm:ss"; "01-01-1990" / ISO / 1-digit-month / no-dob all fail
with the honest 500 "Unable to parse source object using specified date
format"; "01-01-1990 00:00:00" succeeds. The donor DAO has NO post-mapping
dob override (unlike the patient-registration DAO).

FINDING (non-blocking, config-owned): donorNumber is NULL — the success
envelope reads "Donor null Saved Successfully". getGeneratedNo(0, unitId,
DONORNUMBER) returns null because public.t_prefix_running_number (the
running-number config: org/unit/screen/current_running) has ZERO rows in
his_local. The write lands; only the number is missing. Honest envelope +
write landed = NOT a masked write, NOT a stop; the config seed is a
backend/config-owned item for the owner/backfill queue. The rebuild's donor
row must not render "Donor null" as truth — the screen shows the honest
state (number pending config) per the dev-data honesty standard.

## DQ-2026-08-12-060 — BLOOD ISSUE (blood bank): PARK — the write requires existing blood-bag stock (dev-empty); cross_match_register_id flag is a sweep artifact

T0 sweep (park_sweep.py, 2026-08-13): POST /global/bloodBank/saveIssueBloodBag
-> honest 500 chain through the contract: null date NPE (issueDate) -> null
bloodBagIssueList NPE -> "id to load is required for loading" (Hibernate load
of the bag/stock by id). The write CANNOT proceed without an existing blood
bag (the issue draws from stock; dev stock tables are empty). PARK CLASS:
stock-dependent — needs a seed fixture (backend/seed-owned) before the flow
can be exercised; the flow is not drift-blocked (its flagged column
cross_match_register_id EXISTS in DDL — 3 matches anywhere — sweep artifact,
removed from the map bands). The chain itself is honest (ServiceException
rethrows, real messages).

## DQ-2026-08-12-061 — PHLEBOTOMIST HOME COLLECTION (lab): T0 BUILDABLE — the THIRD buildable flow (after donor, shift-closing)

T0 sweep verdict: BUILDABLE. POST /LIS/PhlebotomistHomeCollection/save
(@RequestBody List<PhlebotomistHomeCollectionDto>) -> HTTP 200 "Added
Successfully." + lab.t_phlebotomist_home_collection row landed (id-based
verified, teardown clean). Contract notes: fromTime/toTime REQUIRED as
"HH:mm:ss" (the DAO's LocalTime.parse NPEs on null — same class as the I/O
chart details, but HERE the times are consumable); createdDate/updatedDate
REQUIRED (the DAO's getDate NPEs on null); the write targets its OWN table
(t_phlebotomist_home_collection — clean per the drift map), NOT the sample
tables (t_lab_sample_dtls' 7 flags belong to a DIFFERENT lab flow — the
sample registration/collection from orders). This is the lab family's first
buildable surface — a flow-map candidate (the phlebotomist home collection
screen).

## DQ-2026-08-12-062 — CASH HANDOVER (cash desk): PARK — the 9th phantom instance, cash_in_out_id on public.t_denomination_details (CONFIRMED, PROBABLE->CONFIRMED promotion)

T0 sweep verdict: HONEST-500 (park). POST /api/global/saveCashHandoverMaster
with the exact DTO contract (denominationDetailsMasterList entries with
qty>0 — qty 0 SKIPS the insert by design at
CashHandoverMasterDaoImpl:157) -> live SQL error:
ERROR: column "cash_in_out_id" of relation "t_denomination_details" does not
exist -> SQLGrammarException -> the DAO rethrows -> honest 500 + FULL
ROLLBACK (handover master + denomination details both 0, psql-verified) —
the ATOMIC honest class (the opposite of the io-chart's silent partial).
The DenominationDetailsMaster DTO's cashInoutId field (seen in Jackson's
known-properties list) is the drift column's twin. PARK CLASS: drift-blocked
(honest + atomic); the DDL fix (ADD COLUMN cash_in_out_id) is
backend-owned (Rule 10). Contract trivia for the future rebuild: the
denomination field is `qty` (not quantity), and the details DTO has NO
organizationId/unitId.

## DQ-2026-08-12-063 — ADMISSION (ADT, savePatientAdmission): PARK — the bed-occupancy precheck is an INVERTED patient-reservation gate (backend bug, not a design gate); t_admission drift unconfirmed

T0 sweep (park_sweep.py, 2026-08-13): POST /adt/savePatientAdmission
(BedAllocationController:168, @RequestBody AdmissionDto) -> every probe
returns {"status":"error","code":"409","message":"Bed is already
occupied...!!"} with NOTHING landed. MECHANISM (fully evidenced 2026-08-13):
1. The "bed" check is NOT a bed query: BedAllocationController:186 calls
   iBedAllocationService.isBedBookedOrNot(admission) ->
   BedAllocationServiceImpl:866 -> BedAllocationDaoImpl.getcheckBedAllocation
   (:1069) — a GET_BED_RESERVATION_LIST_DETAILS lookup filtered by
   admissionNote.patientId + to_char(mapper.doa,'dd/MM/yyyy')=:createdDate.
   It returns the PATIENT's bed reservations for the date; NO bed condition
   exists anywhere in it.
2. The controller condition is INVERTED: :187-190 blocks when the list is
   null/EMPTY — but empty = "no reservation" = should PROCEED. A patient with
   no reservation on the date always hits the block. The condition should
   fire on the NON-EMPTY result.
3. STACKED SECOND CAUSE (the first probes): the payload's missing createdDate
   -> admissionDto.getCreatedDate().substring(0,10) NPE (:1079) -> the DAO's
   EMPTY catch (:1083-1087) -> null listObject -> the same gate fired. The
   re-probe WITH createdDate (13-08-2026 00:00:00) still blocks (the query
   returns empty for the unreserved smoke patient) — the inverted gate is the
   real blocker.
ANSWER TO "WOULD ANY BED PASS?": NO — the check never evaluates the bed; it
evaluates the patient's reservations for the date. ANY unreserved patient on
ANY date hits the inverted block regardless of bed status. This is the check
being WRONG (inverted + null-swallowing), NOT the dev bed data being empty.
PARK CLASS: precheck-blocked (backend bug — fix = invert the condition at
BedAllocationController:187-190 + the DAO's empty catch; Rule 10). The
t_admission ot_status + t_admission_details x3 drift flags remain UNREACHED
(the gate blocks before the INSERT).

## DQ-2026-08-12-064 — LAB SAMPLE-TYPE MASTER (LIS/global addSample): BUILDABLE (master class — confirms the clean-master phase; NOT a transaction flow)

T0 sweep verdict: BUILDABLE. POST /LIS/global/addSample with the exact
SampleMasterDto shape (orgId — NOT organizationId/unitId; code/desc; epoch-long
createdDate/updatedDate — a Long field, string dates 400) -> HTTP 200
"Record added successfully." + lab.m_sample_master row landed (sample_id
id-based, teardown clean). The write targets lab.m_sample_master (the
sample-type master), NOT lab.t_lab_sample_master/t_lab_sample_dtls — those
7+1 flags belong to the lab ORDER/sample-registration flow, whose endpoint
was not located this batch (the LIS transaction module hunt continues).

## DQ-2026-08-12-065 — CASH-DESK READ/WRITE ASYMMETRY — AMENDED: the unauth-write claim is RETRACTED (mechanical cause: the park_sweep post() always sends the cached token); the measured residual is a read-side session requirement

AMENDED 2026-08-13 after the BR-040 measurement (tools/auth_gap_probe.py):
the original claim ("writes execute with NO Authorization header") is FALSE —
tools/park_sweep.py:50-61 sends the cached bearer token + identity headers
on every request; the smokes were authenticated all along. MEASURED: 12
write endpoints across six families — no header -> 401 (12/12); invalid
token -> 401 (3 spot checks); cached token -> 200 + rows land. The writes
are properly token-validated; nothing is forgeable. RESIDUAL: the same valid
token the writes accept is rejected by the reads (getShiftClosingBalanceSheet,
getCashHandoverDetails, getRefundList -> 401 "UNAUTHORIZED ##") — the reads
validate the full login session (cookieObject), not the header token alone.
Operational note for the backend owner (their choice, Rule 10 — NOT a
frontend fix): headless/token-only clients cannot read the cash-desk
endpoints; a token-accepting read mode would be a backend decision. The
park-sweep WRITE-path evidence (the drift/park verdicts) is unaffected — it
ran the normal authenticated path.

## DQ-2026-08-12-066 — DEPOSIT + WALK-IN SALE: CONTRACT-INCOMPLETE parks (the DTOs are nested/complex; the drift flags unreached — neither confirmed nor disconfirmed)

T0 sweep (park_sweep.py, 2026-08-13):
- DEPOSIT (POST /api/billing/unit/deposit — DepositController:70): the DTO is
  NESTED — DepositCollectionReqDto has exactly 2 properties
  (depositMasterDto + listPaymentDepositDetailsDto; Jackson-verified). The
  minimal flat payload 400s; the nested probe (depositMasterDto{...} +
  empty list) STILL 400s — the inner DepositMasterDto/PaymentDepositDetailsDto
  shapes need the full legacy payload. billing.m_deposit_master's 4 drift
  flags were NEVER REACHED (unconfirmed). PARK CLASS: contract-incomplete
  (the exact legacy deposit payload needed for the full smoke).
- WALK-IN SALE (POST /api/pharmacy/sales/walkins/save/prescription): the
  WalkInPrescriptionReq is a complex multi-part request — the minimal
  {org, unit, storeId, tempBillId} 400s. pharmacy.t_sale's 12 drift flags
  NEVER REACHED (unconfirmed). PARK CLASS: contract-incomplete.
Both flows need the legacy JS payload construction (the exact DTO shapes)
before the drift can be confirmed or disconfirmed — the next batch's work
alongside the blood-request + LIS sample-registration endpoint hunts.

## DQ-2026-08-12-067 — DEPOSIT + WALK-IN SALE contract-complete smokes: the EMPTY t_prefix_running_number CONFIG is a CLASS blocker (3 flows: donor, walk-in prescription, deposit) — seed-owned, NOT a code defect

T0 sweep, contract-complete payloads (the legacy JS shapes, 2026-08-13):
- WALK-IN SALE (POST /api/pharmacy/sales/walkins/save/prescription, the
  legacy WalkInPrescriptionReq shape from walkinPrescriptionController.js:2026):
  the JSON parse PASSES (the request shape is right) -> honest 500:
  "not-null property references a null or transient value:
  Prescription.prescriptionNo" — the prescription NUMBER generation returns
  null because public.t_prefix_running_number is EMPTY (the same config gap
  as the donor's donorNumber, DQ-056).
- DEPOSIT (POST /api/billing/unit/deposit, the DepositCollectionReqDto shape
  — DepositMasterDto.depositAmount + PaymentDepositDetailsDto; the
  loyalty-recharge JS at patientLoyaltiCardMapperController.js:904):
  Jackson-clean after the field fixes -> first the sequence error
  ("setval: value 0 out of bounds for sequence deposit_master_seq" — the
  sequence never advanced) -> then "Please Configure Deposit Number."
  (the deposit-number prefix config row absent — the SAME empty config
  table).
CLASS FINDING: the empty t_prefix_running_number (and the unseeded
deposit_master_seq) block number generation in EVERY flow that generates a
number — three instances now: donor (donorNumber null, DQ-056), walk-in sale
(prescriptionNo NOT-NULL 500), deposit ("Please Configure Deposit Number").
ONE seed item (the prefix-config rows + the deposit sequence) unblocks all
three — seed/config-owned (the owner's seeded-dev-data decision), NOT code
defects. The drift flags (pharmacy.t_sale x12, billing.m_deposit_master x4)
remain UNREACHED — neither confirmed nor disconfirmed (the number gates
precede the INSERTs).

SEED + RE-SMOKE OUTCOME (2026-08-13, the DQ-067 decision implemented):
tools/seed_number_config.py seeded m_screen 164 (missing!), the three
m_prefix_configuration rows (146 DON, 164 PRE, 123 DEP), the three
t_prefix_running_number rows (store_id NULL — store 0 has no m_store row),
and billing.deposit_master_seq -> 1. Idempotent, persistent (never torn
down), his_local-guarded, per-row docs. RE-SMOKE:
- DONOR (screen 146): BUILDABLE — donorNumber "DON-" generated (the running
  part is empty — the format_id's padding row is the next dev gap, cosmetic),
  registration=1 details=1 donor_id=9 landed, teardown clean.
- DEPOSIT (screen 123): the config gate CLEARED ("Please Configure Deposit
  Number" gone) — the write then hit the REAL drift: SQLGrammarException
  "column \"cancel_reason_id\" of relation \"m_deposit_master\" does not
  exist" — the 10th CONFIRMED phantom. PARK CLASS reclassified:
  config-blocked -> DRIFT-BLOCKED (CONFIRMED).
- WALK-IN SALE (screen 164): the config gate CLEARED (prescriptionNo
  NOT-NULL gone) — the write then hit the REAL drift: SQLGrammarException
  "column \"medication_type\" of relation \"t_prescription\" does not
  exist" — the 11th CONFIRMED phantom. PARK CLASS reclassified:
  config-blocked -> DRIFT-BLOCKED (CONFIRMED).
The seed UNBLOCKED three flows' number generation (nothing was broken, it
was unconfigured); the two remaining blockers are genuine phantom columns.
The owner's prediction held: a working write is the only thing that
confirms the flags.

## DQ-2026-08-12-068 — BLOOD REQUEST (cross-match register): PARK — context-dependent (referenced entity load); the contract iteration peeled 5 layers

T0 sweep (park_sweep.py, 2026-08-13): POST
/api/bloodbank/crossMatch/saveCrossMatchRegister (CrossMatchRegisterController:34)
-> honest 500s through the contract, one layer per probe: null-date NPE
(requisitionDate/issueDate/fromDate/toDate) -> null list NPE
(crossMatchRegisterDetails) -> null pendingQty NPE -> "id to load is
required for loading" (Hibernate load of the referenced entity — the
component/requisition — by the DTO's null id). The flow NEEDS the referenced
blood-component/requisition context (dev data) — the same class as the
blood-issue (stock/context-dependent). The t_cross_match_register table is
CLEAN (not flagged — zero drift) — if the flow could reach the INSERT it
would likely be buildable — the full legacy cross-match payload (the legacy
JS construction) + a referenced-component seed are the completion path.
PARK CLASS: context-dependent (referenced-entity ids + seed needed).

COMPLETION ATTEMPT (2026-08-13, the full legacy payload from
detailsCrossMatchRegisterController.js:553-571): the contract iteration
peeled EVERY layer — requisitionDate/issueDate (DAO reads issueDate at
CrossMatchRegisterDaoImpl:55), the crossMatchRegisterDetails entry
(componentId 5 — the component master HAS one row), the detail's
createdDate/updatedDate, the bloodComponentOrderId (seeded order
SMOKE-CM), the patientRequisitionId (seeded requisition) — and the FINAL
blocker is the detail's bloodBankStockId (CrossMatchRegisterServiceImpl:96 —
updateLoanInStatus loads the STOCK by it): the dev's blood-bank stock is
EMPTY (the same class as the blood-issue DQ-060). RECLASSIFIED:
context-dependent -> STOCK-DEPENDENT (the referenced bag stock; the
completion path = the DQ-067 seed's blood-bank stock extension). The
t_cross_match_register remains CLEAN (zero drift); the master rolls back on
the error (atomic — nothing lands).

## DQ-2026-08-13-073 — THE (a) T0 SMOKES (LIS result entry, bill payment splits, pharmacy issue): all three contract-incomplete at the DTO walls — the flags stay UNREACHED

The phase's final smokes (each endpoint located + probed):
1. LIS result entry — POST /LIS/Microbiology/biochemical/resultEntry
   (BiochemicalTestResultController:79, TBiochemicalTestMasterDto) — minimal
   -> HIS-400. lab.t_lab_test_result's 5 flags (createdDate,
   manual_release_time, manual_result_enter_time, manual_validation_time,
   status) UNREACHED.
2. Bill payment splits — the writer is INSIDE the bill's save
   (BillingMasterServiceImpl:1116 saveBillPayeeDetails) — the SAME parked
   wall (DQ-072). HIS-400. billing.t_bill_payee_details's 4 flags
   (cr_net_amt, paid_amount, payment_entitlement_id, priority_id) UNREACHED.
3. Pharmacy issue — PATCH /api/pharmacy/ip/pharmacology/validation/save
   (PharmacologyValidationController:93 — the tool gained per-candidate
   method support for the PATCH). HIS-400. pharmacy.t_sale's 12 flags
   UNREACHED.
VERDICT: contract-incomplete, same class as the opd-bill — the full legacy
payloads are excavations worth resuming only after the confirmed drift in
those tables is fixed (recorded in PHASE_HANDOVER.md section 5). No band
movement this round — the flags stay neither confirmed nor disconfirmed.

## DQ-2026-08-13-086 — THE NURSING TRIO'S T0s: real write screens, but NONE of the three writes survives yet — the verdicts are probe-shape/broken, honestly

The trio is 3-for-3 real clinical write screens (the owner's renders; the
static-scan shell verdict retracted — ledger #14). The primary-write T0s:
- **PatientFallReporting — the literal saveObj (the controller's own shape:
  diagnosisName/incidentDateTime/incidentHour/incidentMinute/
  assessmentScoreNamesList/selectedCheckBox/gcsScoreBefore/gcsScoreAfter/
  assessmentOtherRemarkBefore/After/factorOtherText/saveAsDraft/createdBy/
  updatedBy/createdDate/updatedDate → POST /adt/saveAsDraftPatientFallReporting)
  → 400, NON-JACKSON** (no Unrecognized-field in the log — a service-level
  validation, the same class as the pharmacy's). Probe-shape, excavation
  parked with what is known.
- **medicationErrorReporting — the Save button's ng-click is `saveRack()`**
  (the template's modal-Save), which is undefined in this controller — the
  function belongs to the billing rackMastersController. The template's Save
  is a wrong copy-paste; the screen's save is broken in the legacy markup
  itself (the click would no-op or error). Not drift, not probe-shape — a
  legacy template defect, parked with the line named.
- **UtiReporting — the Save is a form-submit modal with no ng-submit wired in
  the template and only ONE serviceAction in the controller (the patient
  search); the save endpoint is not constructible from the controller —
  the write path is unresolved. Parked as an open excavation.
The owner's "build what survives" — NONE survives the T0 honestly: one
service-validation 400, one broken legacy template click, one unwired save.
The builds wait for the backend/template fixes, not for the frontend.

## DQ-2026-08-13-085 — THE TWO OWED RENDERS DIAGNOSED + THE PHARMACY EXCAVATION CLOSED AS AN OPEN PARK

- **UtiReporting / PatientFallReporting — DIAGNOSED, not a blocker and not a
  guard denial:** the module-id hypothesis is DISPROVEN (all three nursing
  states carry NO moduleId — medicationErrorReporting renders with the same
  shape); the state URLs are the lowercase `/utiReporting`/`/patientFallReporting`
  (my earlier navigations used the wrong case, and the fallback IS the login);
  the templates exist (webapp/views/Nursing/…); the console is clean; the
  session cookie lands. The bounce persists with the correct-case URL — the
  cause is the SPA's auth/session round-trip (env class), not a legitimate
  guard denial: a dev-role user WOULD reach the screen (there is no module id
  to lack). Render remains OWED via the legacy navigation tree, not a URL.
- **Pharmacy issue — the literal-payload attempt is MET and the item is
  PARKED as an open excavation (per the owner's stop-rule):** the legacy
  caller IS the assigning-picker-dispenser screen (assigningPickerDispenserDetails
  Controller:1041 — CONSTANTS.PHARMACY.IP.ASSIGN_PICKER_DISPENSER.SAVE_API)
  and its literal payload (ActualSaveBill:1043 + the billItem:1063 — master
  with id/prescriptionId/pickerId/doctorId/visitType/admissionId/statusId/
  items/unitId/organizationId/userId/storeId; item with statusId/id/itemId/
  prescriptionDetailId/batchId/uomTypeId/uomUnitId/quanityIssued/isNarcotics)
  gets the SAME 400. The endpoint rejects the legacy screen's own payload —
  and the DTO mismatch is visible: PharmacologyUpdateItem's known fields are
  itemStatusId/prescriptionDetailId/intervention/id/isNarcotic/remark, while
  the legacy sends statusId/itemId/batchId/uomTypeId/uomUnitId/quanityIssued/
  isNarcotics. The pharmacy save is BROKEN IN THE LEGACY ITSELF (its own
  literal payload is rejected), which is not drift and not probe-shape —
  parked as an open excavation with exactly what is known, per the
  stop-rule; no fourth-layer peel.

## DQ-2026-08-13-084 — THE FIVE OPEN ITEMS' VERDICTS (the full-payload standard met on four of five): result-entry DRIFT (#12's second screen), diag-proc DRIFT (#17), pharmacy probe-shape (the excavation continues), the trio's two renders login-blocked

- **result-entry — DRIFT-KILLED (the 12th CONFIRMED's second screen):** the
  controller's literal payload (`$scope.tBiochemicalTestMasterDto`, the
  biochemicalResultDetailsController's exact shape — master with
  labSampleDtlsId/orgId/orgUnitId/createdBy/dates/isDeleted/isCompleted +
  the details list) passes Jackson and dies at SQL: `column labsampled0_.
  icmr_id does not exist` — the entity @Column on lab.t_lab_sample_dtls,
  already CONFIRMED as #12 (the LIS central-receiving was the first screen).
  The NPE-chain was never the story; the SELECT of the lab sample is.
- **diag-proc — DRIFT-KILLED — the 17th CONFIRMED: `is_html_print` on the
  `template_mst` table.** The 400s were the WRONG endpoint: the controller's
  ACTIVE path is `/api/global/templates/byService` (the
  `/api/dignostic/diagnosisProcedure/templates` line is commented out). The
  literal payload (orgId/unitId/doctorId/specialityId/templateTypeId/
  isTemplateTypeReq/serviceId...) passes Jackson and the SELECT dies at
  `column template_mst.is_html_print does not exist`.
- **pharmacy issue — PROBE-SHAPE (the excavation continues, honestly):** the
  500s were the organizationId spelling (TemplateReqDto) then the
  PharmacologyUpdate fields, then the PharmacologyUpdateItem's (`id` not
  `itemId`); the current 400 is a non-Jackson "Bad request: Invalid
  parameters" (no Unrecognized-field in the log — a service-level
  validation). NOT a verdict on the backend yet.
- **The trio's two unrendered members (UtiReporting, PatientFallReporting):
  the renders remain LOGIN-BLOCKED** (the recurring session collision —
  env-fault; setup_spa_session.py clears it, the login lands, the cookie is
  set, and the Uti/PatientFall states still bounce to the login — the
  loop is not yet broken for these two). medicationErrorReporting remains
  the confirmed REAL write screen; the two are SUSPECT for the same reason
  and the render is OWED, not a verdict.

## DQ-2026-08-13-083 — THE FULL-PAYLOAD PASS (the owner's order): the LIS Search family RETRACTED to drift — the 16th CONFIRMED phantom + the result-entry/diag chain + the trio's re-classification

The pass that re-examined the probe-shape verdicts with the full-payload
standard:
- **The LIS Search family — the contract-wall verdict RETRACTED: the 16th
  CONFIRMED phantom — `status` on the LIS worklist's `ltlsc`-aliased table.**
  The SearchDto's COMPLETE known-fields (85) from the tomcat error showed my
  minimal probes used the wrong spellings (`organizationId` vs `orgId` +
  `orgUnitId`). The full controller-shaped payload passes Jackson and the
  worklist SELECT dies at SQL: `column ltlsc.status does not exist` (the
  second `status` phantom — #13 is the credit-note's). The ~18-screen family
  joins the drift list. (The `ltlsc` alias is entity-generated; its DDL
  table name is the next trace's.)
- **The LIS result-entry — the probe-shape confirmed: an NPE-chain, one null
  DTO field at a time** (the details list, createdDate, isCompleted...) — the
  payload excavation is ongoing; NOT a backend verdict.
- **diag-proc — probe-shape:** the 400s are the TemplateReqDto's field
  spellings (orgId); "Collection must be not null!" is a service validation
  on a payload collection, not drift.
- **pharmacy issue — the 500's cause is still unresolved** (the PATCH's
  ERR-259a01d2 — the log's cause not yet isolated; the verdict stays
  probe-shape, not class).
- **The nursing report trio — the medicationErrorReporting is a REAL WRITE
  screen** (full form: patient search, event details, medicines table,
  Save/Draft — live-rendered); the static "shell" class was wrong for it.
  Uti/PatientFall renders were blocked by the recurring login collision
  (env-fault; the setup tool's collision-clear works — it unblocked this
  session's renders).
- The ledger gained mechanism #13 (the contract-incomplete verdict as a
  probe statement — "I could not construct the request" vs "the system
  rejects the request", conflated six times).

## DQ-2026-08-13-082 — THE TWO PROBE-PENDING 404s RESOLVED: incident-cat BUILDABLE + built; histo-get DRIFT-KILLED — the 15th CONFIRMED phantom

The last two genuine unknowns, resolved (both 404s were probe-shape, not
class):
- **incident-cat — BUILDABLE + BUILT:** the real path is GET
  `/api/global/unit/getActiveIncidentCategoryMaster/{orgId}/{unitId}` (path
  params — my 404 was a param-less POST). Smoked 200 with REAL rows
  (incidentCategoryId 2, INC3). Built as the read-only incident-category
  screen (shorter loop, render-trace gate in the README, precheck passed).
- **histo-get — DRIFT-KILLED — the 15th CONFIRMED phantom: `block_capture_note`
  on `lab.t_template_result`** (the real path GET
  `/LIS/histopathology/getReport/{templateResId}/{orgId}/{orgUnitId}` — my
  404 was the wrong endpoint name, getReportDetails vs getReport; the 500's
  SQLGrammarException: `column ttemplater0_.block_capture_note does not
  exist` — TTemplateResultDaoImpl:222 selects it, the FROM is
  lab.t_template_result (line 228), and no template-result table has the
  column (psql 0). Read-side kill; the histopathology report flow parks
  until the entity/DDL mismatch is fixed.

No probe-pending candidates remain — every named screen now has a verdict.

## DQ-2026-08-13-081 — THE 35 REPORTS' SWEEP + T0s: MIS/MLC live, the incident/NABH family drift-killed by a phantom TABLE (the 14th CONFIRMED), the LIS Search family contract-walled

The reports' sweep (the same shape as every subtree: classify, T0 the primary
read, build what survives, park what dies with the column named):
- **MISReports — already rebuilt (the pre-existing mis-reports feature, class
  MisReports, read-only) + its primary read T0-smoked 200** (the
  getMISReportFormattesMasterforShowReports) — the collision precheck caught
  the existing dir BEFORE any generation (its first real catch). The existing
  screen's reads are live; route added.
- **MLCReportingWorklist — BUILDABLE, built (shorter loop):** the worklist read
  200 (getPatientListMedicolegalDetailsMLCWorklist) — read-only, render-trace
  gate stated in the README.
- **The incident/NABH reporting family — DRIFT-KILLED: the 14th CONFIRMED
  phantom — the TABLE `public.user_chat_details` does not exist**
  (the incident/nabh list DAOs' join — a phantom relation, same class as
  t_message_queue #8; read-side kill). Affected: incidentReporting,
  ReportedIncident, nabhReporting, reportedNabh + their add/edit siblings
  (the same DAOs). The family parks until the relation exists.
- **The LIS report family (reportEntry/reportRelease/reportValidation + the
  worklist/consolidated/details/microbiology siblings) — CONTRACT-WALLED:**
  the Search DTO rejects the minimal payloads (400 HIS-400) — the same class
  as the LIS result-entry's (DQ-077); the exact-payload excavation is parked
  behind the family's confirmed drift.
- diagnosticProcedureReport — 400 (the same contract class); incident-cat +
  histo-get — 404 (endpoint-resolution pending); the nursing report trio
  (medicationError/Uti/PatientFall) — template-static shells.

## DQ-2026-08-13-080 — THE NINE DANGLE/SHELL CANDIDATES: ALL LIVE-PROBED — confirmed shells, none parked on the static classification

Each of the nine got its own live probe (route render + template call-scan):
- addendumReport — LIVE RENDER ✓ (the shell renders: Addendum Report +
  Save/Release buttons + the patient/histopathology/report fields — a
  WRITE-button shell on the shared MasterController; the template has zero
  data calls)
- radiologyReportValidation — route 200; template static (zero calls) — shell
- radiologyReportReleaseWorkList — route 200; controller unresolvable;
  template static — shell
- dueDepositReport — route 200; shared MasterController; template static — shell
- incidentReportingScreen — route 200; IncidentReporting.Controller; template
  static — shell
- detailsConsignmentManagerDashboard — route 200; shared MasterController;
  template static — shell
- MPShahEncounterList / MPShahEncounterSummary / MPShahPatientList — route 200
  each; shared MasterController; templates static (1 stray call) — the MPShah
  custom-report trio are shells in this dev build (the reports' source is an
  external app; nothing to rebuild here)
The static dangle/shell classes were confirmed by the live probes (the
templates carry zero data calls; the routes render). None was parked on the
static read alone.

## DQ-2026-08-13-079 — THE RENDER TRACES DONE (all seven dashboards) + the one-command session setup + the generator collision precheck

Owner's merge gate was the render trace — the batch had API smokes only.
- **Render traces DONE (2026-08-13, live browser session):** erPatientDashboard,
  nurseDashboard, diagnosticsDashboard, nursingAllocationDashboard,
  nursingShiftDashboard, otManagerDashboard, doctorDashboard (the RESTORED
  original — "Dashboard - Doctor", guarded route, real content) — all render
  with the live session; none hit Access Denied. The render gate is now
  claimed for all seven.
- **The session problem solved properly (owner):** `tools/setup_spa_session.py`
  — ONE command that (1) clears the login collision (psql UPDATE, guarded to
  user 1 / his_local), (2) re-mints the token via get_dev_token.py, (3) prints
  the exact console snippet (sessionStorage.token + reload). The recurring
  expiry was the collision + the rotation + the SPA's cross-origin
  sessionStorage; the tool addresses all three. (The tool's token prints
  redacted in agent tool output but full in a human terminal — the paste is
  the one command.)
- **The generator-overwrite class closed (owner):**
  `tools/check_feature_dir_collisions.py` — the standing precheck BEFORE any
  bulk generation: FAIL (exit 1) if any target feature dir already exists.
  **Negative control proven both directions:** `doctor-dashboard` FAILS (the
  existing — would have caught the overwrite) and a fresh name PASSES. The
  route-survival check catches a vanished route; this catches an overwritten
  component.

## DQ-2026-08-13-078 — THE REMAINING NINE DASHBOARDS: T0 verdicts (7 BUILDABLE, 2 CONFIRMED-SHELL) — the batch is built; the browser render is OWED

T0 smoked the PRIMARY READ of each (DQ-076): the BillStatDto family wanted
`organizationId` (British), the NurseShiftAllocationDto wanted `orgId` —
both payload-spelling classes resolved from the tomcat's "Unrecognized
field" list; the 404s were wrong-root probes (the store constant's root is
/api/global; getStoreList takes org+unit path params):
- erPatientDashboard — BUILDABLE (getAdmissionDischargeDeathCount,
  getOPVisitsCount 200 with organizationId)
- nurseDashboard — BUILDABLE (getDashboardBedOccupancyList 200;
  getAssignedBedList 200 with orgId)
- diagnosticsDashboard — BUILDABLE (getFinanceDetailsCount 200)
- nursingAllocationDashboard — BUILDABLE (getActiveNurseAllocationList 200
  with orgId)
- nursingShiftDashboard — BUILDABLE (getActiveNurseAllocationMasterByNurseId
  200 with a REAL row, nurseAllocationId 1)
- otManagerDashboard — BUILDABLE (getActiveSpecialityList 200, 2 rows;
  the theatre list 400s the minimal payload — contract note)
- doctorDashboard — BUILDABLE (getStoreList/1/1 200 with rows; the
  pre-existing GUARDED route kept + class-name fixed; my duplicate removed)
- consignmentManagerDashboard + dashBoard — CONFIRMED-SHELL (shared
  MasterController, zero API calls — live-probed, same as dueDeposit)
All seven built with the SHORTER LOOP (reads-only, no T4/T5, READMEs state
the render-trace gate). Gates PASS (tsc 0, build 0, 373 routes, survival).
**RENDER-TRACE OWED:** the browser render of the batch was blocked this
session by the SPA session (Token Expired — the env-fault class, 5th
instance; the sibling fundStatus/storeStock rendered earlier under the same
pattern). The API-level smokes + the gates + the earlier sibling renders
stand as the batch's evidence; the render trace is the merge gate and is
explicitly NOT yet claimed.

## DQ-2026-08-13-077 — THE FOUR DRIFT-ADJACENT DASHBOARDS: T0 verdicts (2 BUILDABLE, 1 drift-PARKED, 1 shell) + the 13th CONFIRMED phantom

T0 smoked the PRIMARY READ of each drift-adjacent dashboard (DQ-076):
- **fundStatusDashboard — BUILDABLE (shorter loop, render-trace gate):** the
  three reads all 200 (getActiveTrustList, getTrustList,
  getFundCollectionListByTrust — the 405 on GET was a wrong-method probe;
  POST is the method). Built + render-traced (route renders, honest empty
  panels). The billing family's writes stay drift-blocked; these reads
  survive — the (b) verdict's informative direction.
- **storeStockDashboard — BUILDABLE (shorter loop, render-trace gate):**
  GET `/api/global/user/stores/1/1/1` → 200 with REAL data (Linen Store,
  Test Main Store — the first `/api/user/stores` 404 was a wrong-root
  probe; the constant's root is `/api/global`). Built + render-traced
  (the live stores render through the browser). The remaining stock feeds
  are honest-empty (unprobed this pass) and stated so in the README.
- **workloadDashboard — PARKED: READ-SIDE DRIFT CONFIRMED — the 13th
  CONFIRMED phantom: `status` on `billing.t_credit_note_detail`**
  (LabDashBoardDaoImpl.getFilteredDashBoardList; `column tcnd.status does
  not exist` — tcnd = billing.t_credit_note_detail; the workload's SELECT
  dies at SQL). The owner's (b) PROVEN: the read dies the same way a write
  does. Its flags (t_lab_test_result/t_lab_sample_dtls) stay unreached.
- **dueDepositDashboard — SHELL (confirmed, not parked):** the state binds
  the shared MasterController with zero API calls (live-verified). The
  static shell class held up under the live probe — but per the
  classification-honesty rule it is recorded as CONFIRMED-SHELL, not
  parked-on-static.

Both buildable dashboards' READMEs state plainly: THE GATE IS A RENDER
TRACE — NOT A WRITE-VERIFIED SCREEN.

## DQ-2026-08-13-076 — READ-SIDE DRIFT (owner correction, recorded): read-only does NOT make a screen safe from drift

The owner corrected the sweep's (b) framing: "read-only does NOT make a
screen safe from drift — icmr_id was itself a read-side kill. A dashboard
reading a drift-hit table dies the same way a write does." The T0 rule
transfers unchanged: SMOKE THE PRIMARY READ FIRST. A dashboard whose smoke
SELECT dies on a phantom is park-or-banner, not a build; one whose smoke
completes is buildable even while its family's writes are drift-blocked.
The four drift-adjacent dashboards (workloadDashboard, storeStockDashboard,
fundStatusDashboard, dueDepositDashboard) are the first batch precisely
because their verdicts are informative either way: buildable proves reads
survive where writes do not on the same tables; parked confirms drift kills
both sides. For any buildable: the SHORTER LOOP (no T4, no T5, render trace
as the merge gate) — each README states plainly the gate is a render trace,
never a write-verified screen. The nine dangle/shell candidates remain
STATIC classes — each needs a live probe before parking; none is parked on
the static read alone.

## DQ-2026-08-13-075 — THE FIXTURE-DEPENDENCY AUDIT (all eight flows, per-flow verdicts) — ONE additional instance found and fixed

Owner's click found the cross-match; the audit checked all eight the same
way (does the screen's payload reference any id/row a smoke created and a
teardown destroyed?):
- transferScreen — CLEAN (the payload is direct; the pending-list 400 is the
  known DQ-050 feed, not a fixture dependency).
- donorRegistration — CLEAN (the number comes from the SEEDED config —
  persistent; the feeds are live tables, not fixtures).
- shiftClosing — CLEAN (the direct save; the balance sheet is a real read).
- phlebotomistHomeCollection — CLEAN (the direct save).
- crossMatchRegister — FIXED earlier (DQ-074: live order/requisition inputs).
- **bloodIssue — FIXTURE-DEPENDENT (the audit's second instance): the
  payload's crossMatchRegisterId: 1 + the detail's crossMatchRegisterDetailsId:
  1 referenced the sweep's transient fixture (remark='fixture') — absent for a
  standalone user. FIXED: required live inputs, honest failure. (The detail's
  bloodBankStockId: 1 + bagNo SMOKE-BAG1 are the SEEDED stock — persistent,
  legit, kept.)**
- nursingOrder — CLEAN (the direct save; the list + dates are payload fields,
  not references).
- labSampleType — CLEAN (the direct save).
LESSON (recorded in the cross-match README, now confirmed as a class): a
golden trace must run against seeded state a real user can reach — never
transient fixtures; the merge gate does not catch fixture-dependent screens.

## DQ-2026-08-13-074 — OWNER CLICK-THROUGH: the cross-match fixture-dependency (the merge gate's lesson), the [object Object] handler, the transfer's DQ-050 confirmation, the donor's dev-data gaps

1. **crossMatchRegister FAILED through the browser** — the golden trace ran
   against the transient SMOKE-CM fixtures (order + requisition at ids 1,
   torn down by the trace's teardown); the screen defaulted to those ids, so
   a real user's save failed ("id to load"). **The trace proved the write's
   mechanics against a state the standalone screen cannot reach — the merge
   gate did not catch a fixture-dependent screen. FIXED: the screen now
   requires the live order/requisition ids as inputs (no fixture defaults);
   LESSON recorded in the README: golden traces must run against seeded
   state a real user can reach, never transient fixtures.**
2. **"[object Object]"** — the cross-match's error handler used
   `String(err)` on the HttpErrorResponse. The audit: every OTHER rebuilt
   screen uses the `e?.error?.message` pattern (verified — the cross-match
   was the only offender). FIXED: the handler now surfaces the backend's
   message.
3. **transferScreen pending-list "Bad request: Invalid parameters"** — the
   SAME known failure as DQ-050 (the transfer picker feed, duplicate bedId
   alias in the backend query — logged, backend-owned). Not a new failure;
   the transfer screen's feed cannot be fixed on the frontend.
4. **donorRegistration nationality empty + prefix one option** —
   `public.m_nationality_master` has ZERO dev rows (a dev-data gap, not a
   truncated feed); the prefix's one option is the CORRECT filter result
   for the donor screen (146) against the 4 seeded config rows. Both
   recorded in the donor README as correct empties.

## COMPOUNDING-SEED AUDIT (2026-08-13, applied to the WHOLE park list as directed) — which parks does the rule free?

Per-park check: does an existing BUILDABLE flow manufacture the reference the
park is missing?
- patient-reg / appointment / cash-handover / deposit / walk-in / OPD-bill
  (DRIFT-BLOCKED — the CONFIRMED phantoms): a phantom COLUMN is not a missing
  reference — no flow can manufacture a column that does not exist in DDL.
  The rule does not apply; the backend fixes these.
- I/O charts (MASKED-PARTIAL): the backend's defects (LocalTime NPE + the
  ignored details-DAO response, BR-039) — not references. N/A.
- admission (PRECHECK-BLOCKED): the gate's code inversion (isBedBookedOrNot —
  every bed incl. never-occupied rejects); the 12 beds EXIST — the reference
  is not missing, the gate evaluates wrongly. N/A.
- opd-bill (CONTRACT-INCOMPLETE): the mobile-app BillDto excavation. N/A.
- LIS central-receiving (OPERATIONAL-DATA): PARTIAL. The sampleId reference
  (lab.m_sample_master — the sample-TYPE) IS produced by the BUILDABLE
  lab-sample-type flow. The orderId/orderDetailsId reference
  (lab.t_lab_sample_master — the sample MASTER) has NO buildable producer
  (its only writer is the LIS's own chain) — a SEED fills it (the dev-env-gap
  class: an unseeded environment, same as the stock/orders/requisitions).
VERDICT: the rule frees NO additional flows cleanly; it upgrades the LIS to
PARTIAL (the sample-type manufactured; the sample-master seeded next). The
phantoms and the code defects are outside the rule's reach — they are the
REAL drift the 3:0 ratio's "some of it" refers to.

## DQ-2026-08-13-072 — OPD-BILL (billing): the real endpoint is /api/billing/saveBillFromMobileApp/saveBill; the mobile-app BillDto 400s the minimal payload (contract-incomplete — the BR-035 phantom stands CONFIRMED)

T0 sweep (park_sweep.py): the /api/billing/saveBill path was WRONG (the real
mapping is BillingMasterController:3556 saveBillFromMobileApp/saveBill). With
the corrected endpoint, the minimal payload -> HIS-400 (the mobile-app
BillDto's Jackson — the field names unknown; the full legacy bill payload's
construction is the next batch's item). The BR-035 phantom
(package_applicable_date on billing.m_billing_master — the 5th CONFIRMED)
stands CONFIRMED from the earlier live 501 evidence; this smoke's minimal
payload dies at the Jackson before reaching it. PARK CLASS: contract-
incomplete (the full legacy bill payload needed).

PARKED 2026-08-13 (owner order): the excavation is DROPPED for now — the
endpoint ends at a CONFIRMED phantom (package_applicable_date — BR-035), so a
perfect payload lands on a wall already documented. CLASS: contract-
incomplete-behind-confirmed-drift. The excavation is only worth resuming
AFTER package_applicable_date is fixed — its cost is certain and its payoff
is zero until someone else acts.

## DQ-2026-08-13-071 — NURSING-ORDER (ADT, /api/opd/saveNursingOrderMapper): masked envelope was a TWO-LAYER contract NPE, not drift — RESOLVED BUILDABLE (the 6th BUILDABLE in the sweep)

T0 sweep (park_sweep.py): the minimal payload -> HTTP 200 envelope
{"status":"error","code":"500","message":"Something went wrong!!"} with
nothing landed (MASKED-PARK). MECHANISM (catalina): NursingOrderMapperDaoImpl:54
iterates the DTO's nursingOrderMapperDtoList (null -> the masked NPE), then
:62-63 read startDate/stopDate (null -> the same masked NPE — the DAO's empty
catch swallows both). With the list + the dates in the payload, the write
LANDED: "Added Successfully." + a t_nursing_order_mapper row (verified,
teardown clean). The table has NO drift flags (the INSERT passed clean).
Upgraded: MASKED-PARK -> BUILDABLE.

## OPERATIONAL-DATA CLASS (2026-08-13) — a flow that needs an order, a requisition, a bag, stock, a donor and a cross-match before its write can be exercised is NOT a park and NOT a defect — it is evidence about the DEV DATABASE, not the backend

Class rule (owner, 2026-08-13): a screen that can only be exercised in an
environment with REAL OPERATIONAL DATA is its own class. It is not a park
(nothing is broken) and not a defect (no backend finding); the flow's layers
are the environment's responsibilities. Members (from the sweep):
- BLOOD-ISSUE (DQ-070): six layers peeled before the write can be exercised —
  the BLOOD_BAG_ISSUE_NUMBER config, the bag + segment, the DONOR, the
  stock's load, the master's contract, and the referenced cross-match. Each
  layer is a missing piece of a real operational dataset, not a code defect.
- LIS CENTRAL-RECEIVING (DQ-069): the acceptOrRejectSample endpoint located;
  the write needs the referenced lab order/visit context.
These two are NOT evidence about the backend at all. Their t_issue_blood_bag /
t_lab_sample_dtls drift flags remain UNREACHED — neither confirmed nor
disconfirmed — and the per-table DDL diff is the cheap instrument for those.
The completion path for both is operational data (a populated dev dataset),
not a code fix.

COMPOUNDING-SEED INSIGHT (2026-08-13): blood-issue's BUILDABLE came from the
cross-match flow's OUTPUT becoming its INPUT — the first time seeding has
COMPOUNDED rather than just filled a gap. Some of what looks like it needs
real hospital data can be MANUFACTURED by flows already working. RULE for
this class: BEFORE parking anything else in it, check whether an existing
BUILDABLE flow can produce the missing reference — that may shrink the class
considerably (blood-issue already moved OUT of it this way).

## DQ-2026-08-13-070 — BLOOD-ISSUE chain close-out: peeled 6 layers; the remaining blocker is the referenced cross-match (OPERATIONAL-DATA class)

The stock seed unblocked the chain layer by layer; the final peel (the full
IssueBloodBagDaoImpl:36-97 contract + IssueBloodBagServiceImpl:56-115):
1. the BLOOD_BAG_ISSUE_NUMBER config (the 4th prefix-config screen — the
   seed's next extension item; the number's generation),
2. the bag + segment (the detail's bagNo/segmentNo),
3. the DONOR (the bag's donor_id — the seed's DON-SEED),
4. the stock's updateLoanInStatus (the detail's bloodBankStockId — the
   seeded bag),
5. the master's contract (btoId/issuerId/receivedId/bloodUnitQty/
   bloodBagIssueNo/remarks),
6. the FINAL call updateCrossMatchingReqQtyByCrossMatchId (the service's
   line 114) loads the CROSS-MATCH by the master's crossMatchRegisterId —
   the payload lacks it and the dev has no cross-match rows (the sweep
   cleaned them).
PARK CLASS: context-dependent (the referenced cross-match row — the
cross-match flow's own BUILDABLE path can supply it; the next seed item).
The issue's INSERT never reached (the loads precede the master's save);
t_issue_blood_bag's drift flags remain unconfirmed.

RESOLVED 2026-08-13 (the cross-match fixture supplied the LAST layer):
the persisted cross-match fixture (master 31 + detail 22, via the
cross-match's own BUILDABLE path) fed the blood-issue's crossMatchRegisterId
+ the detail's crossMatchRegisterDetailsId — the final load passed and the
write COMPLETED: "Added Successfully." + t_issue_blood_bag + details rows
landed (verified 14/14 fixture-aware: issue rows teardown-clean, the input
fixtures persist, zero leftovers). The 7th layer was the last. The flow is
now BUILDABLE and moves OUT of the operational-data class (the environment's
data — the seeded cross-match — supplied the final reference). The
t_issue_blood_bag drift flag (cross_match_register_id) is KILLED by the
smoke — the INSERT passed with the column (the DDL-present artifact the
candidate's precheck predicted) — removed from the SUSPECT band (175 -> 174).

## DQ-2026-08-13-069 — LIS SAMPLE-REGISTRATION (central receiving): PARK — context-dependent (referenced lab order); endpoint LOCATED

The t_lab_sample_dtls writer hunt RESOLVED: POST
/LIS/centralReceiving/acceptOrRejectSample (LabCentralReceivingController:73)
— @RequestBody List<LabSampleDetailsMasterDto> (a LIST — the single-object
probe 400s). The smoke: the list-form payload passes the Jackson parse ->
honest 500 "id to load is required for loading" — the service loads the
referenced lab order/visit context; the dev's lab-order data is empty (the
same referenced-entity class as the cross-match DQ-068). PARK CLASS:
context-dependent (the referenced lab order + the sample master's row
needed). t_lab_sample_dtls stays EMPTY; its drift flags (if any) remain
unreached — the per-table DDL diff is the cheap instrument.

RESOLUTION 2026-08-13 (the compounding audit): the sample context is seeded
(m_sample_master sample_id=1 + t_lab_sample_master lab_sample_id=1 +
t_lab_sample_dtls lab_sample_dtls_id=1 — the sample-TYPE, sample MASTER and
sample DETAILS; the sample-type IS the BUILDABLE lab-sample-type flow's
output). The accept then advanced past the "id to load" to a live SQL error:
**CONFIRMED PHANTOM #12 — icmr_id on lab.t_lab_sample_dtls does not exist**
(the entity's @Column vs the DDL's 60+ columns; the Hibernate SELECT
labsampled0_.icmr_id, LabCentralReceivingDaoImpl acceptOrRejectSample) —
the THIRD live-write promotion (PROBABLE 177 -> 176, CONFIRMED 11 -> 12).
The LIS parks at the phantom: the backend must drop icmr_id from the entity
(or add the DDL column); the flow is otherwise fully referenced (seeded
context + the ACCEPT branch's update path).

## DEV-ENVIRONMENT-GAP CLASS (2026-08-13, four instances in one seed script) — a flow blocked by a missing config row is NOT a park, it is an unseeded environment

Class rule (owner, 2026-08-13): a flow blocked by a missing CONFIGURATION row
(m_prefix_configuration / t_prefix_running_number / m_screen / a sequence)
is an UNSEEDED environment, not a park, and must never be conflated with
parks in the park list. The two live in different ledgers: parks = the
application's own defects (drift, masked writes, inverted gates, contract
shapes); unseeded environments = the dev database lacking what the
application requires to function (fixed by tools/seed_number_config.py, which
is idempotent + persistent + guarded, never a fixture).

Instances found while seeding the number config (tools/seed_number_config.py):
1. m_screen row 164 MISSING (the walk-in prescription screen — the
   getGeneratedNo join requires it with is_global=false). The neighbours 123
   and 146 exist.
2. t_prefix_running_number.store_id FK: store 0 has NO m_store row — the
   running rows must carry store_id NULL (the function matches on
   p_store_id = 0 regardless).
3. billing.deposit_master_seq at ZERO — "setval: value 0 is out of bounds
   for sequence" — the deposit INSERT died before the columns; setval to 1
   (the table was empty, so no id collision).
4. FORMAT PADDING: the generated numbers come back as "DON-" (the
   generated_code) with an EMPTY running part — the format_id's padding row
   is absent (the format master's definition), so the length/padding never
   applies. Cosmetic (the flow works — rows land) but a visible gap.

EXPECT MORE OF THE CLASS (owner's note): the deposit sequence at 0 showed
dev seeding has gaps beyond the config tables; treat a fresh "missing
something" 500 on any write as a candidate unseeded-environment before
reading it as a park.

RE-AUDIT RESULT (owner's directive): the park list now carries NO
config-blocked/contract-incomplete entries that are actually unseeded
environments — the deposit + walk-in were contract-incomplete -> config-
blocked -> then DRIFT-BLOCKED (CONFIRMED, DQ-067 outcome; the seed made the
real blockers visible); the cross-match stays contract-incomplete (its
blocker is the referenced componentId in the payload, not a config row);
blood-issue stays stock-dependent (transactional stock DATA, not config).

FLOW-MAP READ-SIDE FINDINGS (T1, 2026-08-12, live calls):
1. patientAutofillSearch is a TODO STUB — DonorRegistrationServiceImpl:296-299
   `return null` — POST /api/bloodbank/patientAutofillSearch returns HTTP 200
   with an EMPTY BODY. The "existing patient as donor" autofill feed was NEVER
   implemented server-side. The rebuild must NOT wire the autofill picker to
   this endpoint (it renders nothing); either omit the control or show the
   honest "feed unavailable" state.
2. donorSearch is LIVE: 200 success, empty dev list (honest), message field
   mislabeled "Updated Successfully." (cosmetic).
3. getActivePrefixList (GET /adt/getActivePrefixList) is LIVE: 1 real row —
   the rebuild's only currently-wired feed (the prefix-list shell).
4. EXTERNAL CALL IN THE SAVE CHAIN: registerDonor -> saveDonorFingerPrint
   (service:303-339) POSTs the thumbImage to the hardcoded
   http://mabistest.mantratecapp.com/4/mabis/biometric/enroll whenever
   thumbImage is present — a test-environment biometric service in the
   patient-registration-class write path. The rebuild's form must treat
   thumbImage as optional and the trace must note the external dependency
   (flagged for the owner: hardcoded external endpoint, consistent with the
   secret-scan class).

## DQ-2026-08-12-057 — ENCOUNTER (module OPD): READ-ONLY BY DESIGN park class (first instance of the owner's category)

T0 sweep (park_sweep.py, 2026-08-12): the encounter screen has NO standalone
write endpoint AT ALL — EncounterController exposes only reads
(getEncounterDetails, getListOfEncounterMaster, getSearchedListEncounterMaster,
etc.); the encounter WRITE (saveEncounterMaster) is called only
service-to-service from BillingMasterServiceImpl:2818 (the billing flow creates
the encounter during bill/payment processing — the Billing module,
encounterMasterDto with visitTypeId OP). PARK CLASS: read-only BY DESIGN —
NOT blocked, NOT drift-hit at the screen level; a different fact for whoever
picks it up (the encounter-create is part of the billing flow's write chain,
and THAT chain carries the t_encounter_master drift — advised_by_id,
advised_by_name, nursing_station_id (PROBABLE band) — so the billing flow's
encounter-create is drift-suspect, same class as OPD Bill). The rebuilt
encounter feature (reads only, both live) is correct as built.

## DQ-2026-08-12-058 — INPUT-OUTPUT CHARTS (I/O charts): MASKED-PARTIAL — master row lands, chart LINES silently dropped (false-success-partial class)

T0 sweep verdict: UNPROVEN (park). POST /api/opd/saveIntakeOutputChart with
the exact DTO contract -> HTTP 200 "Added Successfully." with
adt.t_intake_output_chart = 1 (the master row lands) but
adt.t_intake_output_chart_details = 0 (the chart LINES — the flow's core —
never written). MECHANISM (catalina): IntakeOutputChartDaoImpl:113
LocalTime.parse(startIntakeTime) NPEs on a null time; with a time sent, the
app's IST zone leaks into the timestamp bind ("10:00:00+05:30" ->
"invalid input syntax for type timestamp") — the details INSERT cannot
complete either way. AMPLIFIER (Amendment 11 #4): the SERVICE ignores the
details DAO response (IntakeOutputChartServiceImpl:67 — resPreDtls unused)
-> the failure is silent -> "Added Successfully." with the lines dropped:
the false-success-partial class (BR-193-like). The service is the
masking layer. The 2 flagged columns on t_intake_output_chart_details
(drift map, PROBABLE/SUSPECT band) were NEVER REACHED — neither confirmed
nor disconfirmed (the LocalTime.parse dies before the SQL). Backend-owned:
fix the details DAO's time handling + the ignored response.

## DQ-2026-08-12-059 — SHIFT-CLOSING (600, cash desk): MASTER WRITE BUILDABLE — entity inversion + date-format contract; cash-handover write still to smoke

T0 sweep verdict: BUILDABLE. POST /api/global/saveShiftCloseBalance ->
HTTP 200 "Added Successfully." + billing.m_shift_close_balance row landed
(id-based verified), teardown clean. FINDINGS:
1. ENTITY INVERSION: the ShiftClosingMaster entity maps
billing.m_shift_close_balance (NOT m_shift_close_master — which holds the
real dev row from 2026-08-03 and is written by a different path). Whoever
rebuilds the screen must read/write the BALANCE table via this endpoint.
2. DATE-FORMAT CONTRACT: the mapper's dd-M-yyyy date-format MANGLES
"2099-01-01 00:00:00" into 0006-09-30 (day/month rollover) — a marker-date
trap for any fixture; the initial by-date post-count falsely read 0. Use
id-based markers for this flow (the sweep now does).
3. The t_denomination_details.cash_in_out_id drift flag (1, drift map)
belongs to the CASH-HANDOVER write (the denomination/cash-in-out save), NOT
saveShiftCloseBalance — that endpoint is a batch-2 smoke candidate, not yet
probed.
4. The listOfIds receipt-update branch (ShiftClosingMasterDaoImpl:392)
calls size() BEFORE the null check (NPE on null list) — a robustness defect
on the receipt-update path (backend-owned); the branch is skippable with [].
