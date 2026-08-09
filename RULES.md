# RULES.md — Non-Negotiables & Circuit Breaker

These rules apply to every AI session (Blueprint, Maker, or Checker), regardless of which model or tool is being used. If any instruction anywhere else conflicts with this file, this file wins.

## Keep exactly as written (do not "improve" or reinterpret these)

1. **Expand-Migrate-Contract** for all database changes — old and new structures coexist until sign-off.
2. **Shadow testing with real historical transactions** before any live cutover.
3. **Synthetic edge-case injection**: month-end, fiscal quarter-end, December 31st, and February 29th (leap year) batches, plus timezone-shift variants, must be run through the shadow test before any phase is marked complete.
4. **Feature flag kill switch** for every user-facing change — must be flippable without a code deploy.
5. **Backend-first, version-upgrade-last** ordering (see `CLAUDE.md` mission order).
6. **Maker-Checker** for every database migration script and every backend exception-handling change — one AI/model writes, a separate AI/model (different session, ideally different vendor) reviews, before a human ever sees it.
7. **One quote/definition of any business term** — only `docs/GLOSSARY.json` is authoritative. No code may reference an undefined term.
8. **Manifest ID assignment**: new `docs/BUSINESS_RULE_MANIFEST.md` entries use a provisional ID (`BR-PENDING-<file-or-topic-slug>`) for the life of the branch. The provisional ID is replaced with the next real sequential `BR-XXX` number (computed from master's current highest ID) in the same commit that immediately precedes merge to master — never earlier.
9. **Merge-record rule** (owner-approved 2026-08-05, after the ledger-staleness finding: BR-147/148/153/154/156/160/161 sat in main for days with "held, not merged" statuses because the record was written at entry-creation time and never revisited): on any BR-closing merge, the same session that executes it must, in that session: (a) flip the BR's `docs/BUSINESS_RULE_MANIFEST.md` and `docs/BR_INDEX.md` status to "Merged into main" with the introducing commit hash (merge-commit hash for real merges; fix/docs commit hashes for fast-forwards) — an entry written before an approval is stale by definition, so the BR's SESSION_LOG entry must also be re-read and corrected at session close; (b) record the owner's merge approval verbatim with its timestamp in SESSION_LOG, in the session that receives it — never left to be reconstructed from transcripts; (c) use `git merge --no-ff` for BR-closing merges so every merge leaves a reviewable, datable commit (a detection aid, not a substitute for (a)/(b)); (d) run `tools/check_ledger_merge_state.py` (two-direction assertions: held ⇒ fix NOT an ancestor of main; merged ⇒ cited hash IS an ancestor of main) at session close and paste its output into the session log.
10. **Trace-before-trust rule** (owner-approved 2026-08-05, after two pattern passes over the same code produced two wrong classifications in opposite directions — "11 CRASH sections", then a corrected pass — and only an end-to-end trace of a single instance settled it): any classification produced by a pattern pass (grep, regex, static scan — org-trust categories, query states, caller counts, cache classes, anything counted) is a HYPOTHESIS until at least one instance is traced end to end: the query text, the effective predicates after comment-stripping, every bind in the calling method, the catch shape, the caller chain, and what the user actually sees. Trace the instance the classification is LEAST confident about, not the clearest one. No classification drives a fix, a report, or an escalation until that trace is done, and the trace must be stated in the finding. Known failure modes this rule exists for: (a) the 11-CRASH miscount — a caller-bind pass that missed binds on multi-line createQuery chains (the DAOs did bind organizationId/patientId; the queries ran org-scoped and the sections rendered); (b) the 2c "no callers" finding — a grep-exclusion artifact that filtered out the very pattern it was counting. Verify the search method against a known positive before recording any negative.

    Standing note (2026-08-05, after the seventh wrong-on-first-attempt grep of the day — the
    whitelist caller audit's first run counted the JwtTokenFilter's OWN whitelist lines as callers,
    inverting the entire result): in this codebase, ASSUME any pattern pass is wrong on its first
    run. Budget a correction round into every grep/scan/audit. Specifically check whether the search
    matched the DEFINITION SITE (the entity declaration, the whitelist's own entry, the controller's
    @RequestMapping, a config-file URL constant, a bean field name) rather than a USAGE — the
    definition-site matches are not callers.

    Standing note (2026-08-05, after the third shell gap — /fonts/ — was found by catalina, not by
    any sweep): every static-asset enumeration so far was scoped to a SINGLE artifact type —
    index.html references (1,342 assets), WAR directory structure, .css url() rules, JS lazy-fetch
    calls — and each scope was blind to the others. The /fonts/ tree existed only as relative
    ../fonts/ references inside INLINE <style> blocks in a view template (web-appointment.html);
    the files are absent from the WAR, so the directory-structure sweep had nothing to find, the
    index sweep never saw the template, and the .css sweep excluded inline styles. Any future asset
    enumeration must either cover ALL artifact types (html references, inline <style> blocks in
    templates, .css url() rules, JS fetches, directory structure) or state explicitly which types
    it excludes and why the exclusion cannot hide a gap. Ground truth for "what the app actually
    requests" is the server log, not the source tree.

    Standing note (2026-08-06, after the third environment gap hid a real vulnerability — BR-176:
    the anonymous org-registration entitlement bypass is unreachable on this dev box only because
    `payment_details` is absent locally, while any DDL-built environment has the table and the
    bypass live; preceded by `family_id` for patientRegistration and the empty table for
    saveAnalyzedReport): ON THIS DEVELOPMENT BOX, "IT DOES NOT WORK" IS NEVER EVIDENCE THAT IT IS
    SAFE. When a code path fails or 404s locally, establish whether the blocker is DESIGN (the
    code never worked, by construction) or ENVIRONMENT (a local schema/config gap) before
    concluding anything about exposure. A missing local table, column, or config can mask a live
    vulnerability; the DDL and the production-shaped environment are the ground truth for whether
    the path is reachable elsewhere.
Standing note (2026-08-06, after the 6A deploy failure — a partial class copy): INNER CLASSES
    SHIP AS SEPARATE .class FILES. Copying only the outer class (RateLimitFilter.class) while the
    inner class (RateLimitFilter$TargetConfig.class) stays behind produces NoClassDefFoundError,
    a failed webapp deploy, and a FAST boot (seconds instead of ~20) with everything 404ing —
    which reads as a filter defect rather than a deploy error. When deploying a changed class:
    copy the class AND all its inner classes (RateLimitFilter*.class), and treat BOOT TIME as a
    signal — a 5-second boot where 20 is normal means the Spring context did not load.

Standing note (2026-08-07, after the BR-183 stale-jar false pass — a verification that
inspected the wrong artifact and reported clean): a verification that reads an artifact must
FAIL when the artifact is unreadable. "End-of-central-directory signature not found" followed
by a clean result is a check reporting on nothing. Any file-reading assertion needs a
parse-success precondition: force a fresh extraction (never reuse a file that merely exists),
confirm the artifact actually parses (zip validity, expected byte count, expected structure)
BEFORE the assertion runs, and treat the parse failure itself as a FAIL. The false pass
happened because a stale partial jar file existed, the extraction step was skipped on the
existence check, and strings on the broken file returned zero matches — a check whose
artifact does not parse cannot be trusted.
11. **Docs-commit governance** (folded from the Stage-1 note): commits made directly to main under the CLAUDE.md docs exception (governance/tooling/config files) still require the end-of-session SESSION_LOG entry; where such a commit touches the ledger (`docs/BR_INDEX.md`, `docs/BUSINESS_RULE_MANIFEST.md`, any BR status), it also requires the ledger-check run appropriate to the change (`tools/check_ledger_merge_state.py` for merge states) with its output pasted into the session log. The docs exception relaxes Maker-Checker review, not record-keeping.

## Circuit breaker — emergency stop conditions

Any ONE of these triggers an automatic rollback to legacy, no debate and no waiting for a human:

- Any invoice or billing amount differs from the legacy calculation by more than $0.01.
- The feature flag service is unavailable for more than 5 minutes.
- Shadow test failure rate is greater than 0% on any run.
- Database replication lag exceeds 30 seconds.
- Database CPU utilization exceeds 80% for more than 2 minutes.
- A `try/catch` block is removed without a documented alternative error path in the centralized handler.
- Unhandled exception count in the dashboard goes above 5 in a 24-hour window.

A monitoring daemon should check these conditions every 60 seconds, flip the feature flag to LEGACY automatically if any condition is violated, and alert the owner. The owner should never need to be the one to notice a problem first.

## What requires the owner's sign-off (nothing else does)

- Any new entry in `docs/BUSINESS_RULE_MANIFEST.md` marked "Behavioral Change" **or** "Financial Correctness."
- The Plain-English Impact Summary for each database migration batch.
- Weekly staging click-through (Wednesdays).
- The final Go/No-Go checklist in `MASTER_PLAN.md`.

Everything else — code review passing, tests passing, PR merges where the Checker approved and CI is green — can proceed without waiting on the owner. The point of this file is that the owner's attention is a scarce, expensive resource reserved for genuine decisions, not for babysitting routine work.

## Definition of "Behavioral Change" vs "Refactoring (Safe)"

- **Refactoring (Safe)**: output is provably identical for all tested inputs (byte-identical PDF, identical calculated values) — only internal structure changed.
- **Behavioral Change**: any difference in output, however small, including rounding, threshold, ordering, or timing differences — even if the new behavior is "more correct." The owner decides whether a bug fix that changes output is acceptable; the AI does not decide this unilaterally.
- **Financial Correctness**: a Behavioral Change specifically to a money calculation (an amount charged, billed, or transmitted to a payment provider) — used instead of the generic "Behavioral Change" label when the manifest entry should call out that the change affects a currency value. It carries the identical sign-off requirement as "Behavioral Change"; it is a more specific label, not an exemption.

## CRUD sibling-action rule (owner-approved 2026-08-04)

Whenever a field-mapping or similar data-correctness bug is found and fixed on one CRUD action (Create, Edit, Toggle, etc.), explicitly check **every other CRUD action on that same screen** for the same underlying pattern before considering the screen fully verified. Same logic as the BR-160 sibling-endpoint rule for security vulnerabilities, now extended to correctness bugs — not just security.

## Campaign 1 exit criterion (owner directive 2026-08-08, autonomous run)

Campaign 1 ends when the contract deriver covers the rebuild's next 40 screens (Track A path) and phases 3 and 4 (shared reads, unscoped queries) have been run as QUERIES against the deriver's join table rather than as fresh sweeps. Phase 5 (swallowed exceptions) becomes a standing backlog, not a blocking run. Findings not on the rebuild's path are logged, not chased. Campaign 1 batch 2 (the next DQ-003 phantom-column batch) resumes only if the deriver's E5 error rate is too high to answer the phase 3-4 questions. Extension thresholds are the owner's call — the deriver scope is fixed at 40 + 220 (the next-40 + the rebuilt-223 validation set) and must NOT be extended regardless of the error rate; the rates are logged and the extension decision is queued for the owner.

**STATUS AMENDMENT (2026-08-08, owner decision): the criterion's PRECONDITION FAILED.** The deriver's E5 resolution reached 18 tables across the scope (254 E4 / 828 E2), which cannot answer the phase 3-4 questions; the deriver's back-test error rate never fell below 0.78-0.83 and the intersection list produced zero true positives. The static deriver is PARKED. The campaign-1 exit condition is now an OPEN OWNER DECISION (queued, Q13) — no replacement criterion has been invented; phases 3/4 as fresh sweeps and the runtime-capture option (40-screen browser network-log capture, owner time) are the live alternatives.

## REACHABILITY GATE (standing rule, owner directive 2026-08-08) — step 1b of every screen rebuild, BEFORE contract extraction

A legacy screen is only rebuildable if it is REACHABLE in the legacy. Before extracting any contract, run the four checks:
1. State registration: is the state registered in the legacy app.js, or commented out / absent?
2. Menu node: does a node for the screen exist in the privilege registry (privilege.privilege modules JSON)?
3. Controller gate: does the controller carry a permission/module check?
4. Module id: does the screen resolve to a module id?

REACHABILITY IS CONFIRMED BY HAND, PER SCREEN, AT SELECTION TIME (owner directive 2026-08-08 — the bulk gate is PARKED, QF-2026-08-08-P; no tool, no bulk pass, no inferred classification): two direct greps before any extraction —
  - the legacy app.js: is there a .state registration for this screen? Registered, commented, or absent — the grep output goes in the screen's report.
  - the privilege registry (privilege.privilege modules JSON): is there a menu node? Give its id and its parent chain.
A screen that is commented or absent is DEAD — do not extract, do not write; log it and take the next candidate. Every screen's reachability evidence goes in its report.
A screen failing checks 1 or 2 is DEAD — do not extract its contract, do not write it. Log it as a dead-screen finding (tagged with the screen, queued), and take the next screen on the path instead. This rule exists because two of the first three Track A rebuild screens (the implant opening balance pair) were rebuilds of legacy screens that no user can open: routes commented out in app.js, zero menu nodes, no permission check — dead in legacy (2026-08-08, logged Q16/Q17).

## REACHABILITY GATE — THE LIMIT OF ITS USE (owner directive 2026-08-08, added to the standing rule)
The gate is a REJECTION tool, never a confirmation tool. Its DEAD-side output (the app.js registration absence — a deterministic find) is reliable; its LIVE-side output is NOT (the bulk run failed its own control on the live side: the inventory-parse produced junk template anchors and the name-matching could not substitute for the actual state registrations; the resulting counts — LIVE 693 / MENU'D-UNROUTED 316 / registered-unmenued 395 / corrected denominator ~560 — are UNVALIDATED estimates and must not be cited as figures). A screen the gate calls LIVE must be HAND-CONFIRMED (the state's registration read from app.js + the menu node read from the privilege registry) before its contract is extracted. The gate may reject a candidate as dead; it may never qualify one as live.

## STATIC-SHELL REBUILD RULE (owner-approved 2026-08-09, standing — screens 4 & 5)

When a legacy screen is a DEAD SHELL — registered in app.js and present in the
privilege tree, but its template has no (or only decorative) ng-bindings and no
API calls, and its controller is the generic MasterController with no
screen-specific logic — the rebuild IMPLEMENTS the screen rather than
reproducing the shell, PROVIDED real backend endpoints exist for its fields.
Mandatory obligations in every such rebuild:
1. Every behaviour the rebuild ADDS that the legacy shell never had must be
   listed in the screen README's divergences section (dropdowns wired, grid
   populated, toggles/edits active, search filtering).
2. Every endpoint that is MISSING (e.g. a save path that does not exist) must
   be reported honestly in the UI (an error toast naming the gap), never a
   silent dead button.
3. The static-shell state itself is a legacy DEFECT — log it as a QA finding
   and queue the owner decision for the missing backend path.
4. The dev-data honesty standard still applies: empty data = "not erroring,
   usefulness UNVERIFIED", never "working"; nothing is seeded to look better.

## C5 NODE LOOKUP — UNVALIDATED IS NEVER A PASS (owner directive 2026-08-09)

Preflight C5's automatic menu-node lookup matches the node's actionLink to the
route (the exact token) first, then falls back to the display-name heuristic —
the class the parked reachability gate failed on. Neither can succeed when the
node's metadata is internally inconsistent (e.g. node 466's actionLink
`#/unitofMeasurement` while the route is /uomMaster — the MENU-BROKEN shape).
When C5 returns UNVALIDATED that is NOT a pass: the preflight output says so
(the NOTE line and a RESULT that is not a clean PASS), and the screen's report
MUST carry the two hand greps (the app.js registration + the privilege-tree
node with id and parent chain) regardless. Hand-confirmation is the
authoritative check; C5 is an aid that must never read as a soft pass.

## BACKEND-DEFECT MERGE RULE (owner-approved 2026-08-09, standing — screen 6)

A screen MAY merge with a documented backend defect on its path, provided all
four hold:
1. The rebuild did not introduce it (the defect exists in the legacy backend
   on the same path).
2. The failure surfaces honestly in the UI (the backend's own message), never
   silently and never as a crash.
3. The defect is logged with its mechanism (the live stack / the named cause)
   and queued for the owner's decision.
4. The README states the honest verification state (nothing seeded, the
   defect named, "not erroring" only when true).
A smoke FAIL caused SOLELY by a documented backend defect does not block the
merge, but the merge message must name it. When any gate fails for a backend
reason, the report's FIRST LINE states whether the rebuild introduced it:
"Gate 3 FAILs — backend-caused, not rebuild-introduced" is the shape (or the
reverse if the rebuild is at fault).

## SCOPED BACKEND CHECK — THE PER-SCREEN CLASSES AND THE CROSS-ORG REACHABILITY QUESTION (standing, owner directive 2026-08-09)

Every screen rebuild runs the scoped backend check on its own path and reports
EACH class explicitly (PASS or FINDING) in the README: phantom columns/tables,
named-query shape (INNER JOIN null-FK drops), operator precedence on dup-checks,
org-trust (payload-carried client org), twin queries, swallowed exceptions on
write handlers. When the OPERATOR-PRECEDENCE class fires (a dup-check whose
org+unit scope binds only one branch of an OR), the finding MUST also state
whether the unscoped branch is reachable by a cross-org caller — i.e. whether
the unscoped predicate reads rows the caller's org does not own (the
existence-oracle shape, screen 8's Q29) or only blocks a legitimate save
(ALREADY_EXIST). That reachability statement is what makes the finding
actionable rather than theoretical; it is a required part of the class report.
The precedence class is a confirmed recurring shape: BR-169 (Discharge Type)
was the first instance, Q29 (surgeryMaster dup-check) the second — a third
confirmed instance on a live save path makes it a campaign (a systematic sweep
of dup-check named queries) rather than a queue item (Q29's owner decision).


## WRITE-VERIFICATION RULE (Q28 decided 2026-08-09, standing)

A screen's save/update/status claims are verified by an OWNER-SANCTIONED
write test only. Without the owner's sanction, a save that returns the
success envelope is reported as "not erroring", NEVER "working" — "working"
requires a write AND a read-back of the written row. Any write test must
satisfy ALL six conditions:
1. One row per verification — exactly one probe row is written, never a batch.
2. An unmistakable marker string — the probe payload carries a unique marker
   (e.g. 'Probe <Screen> YYYY-MM-DD') so the written row is identifiable in
   any later query.
3. Deleted in the SAME run, with a re-query verification — the probe row is
   deleted before the run ends and a re-query proves its absence (never
   "deleted later").
4. Before/after row counts reported — the count before the write, after the
   write, and after the delete are all reported; a count mismatch is a
   FINDING, not a pass.
5. his_local only — a write test never runs against a real-clinical-data
   table or environment; the dev database only.
6. The probe payload must match the LEGACY contract exactly — every field
   the legacy controller sends (createdDate/updatedDate/user ids formatted
   as the legacy formats them). An incomplete probe is WORSE than no probe:
   it produces a false backend defect (the QF-2026-08-09-I dateString-null
   NPE class — the probe omitted the dates the legacy always sends, and the
   DAO's GlobalCommonDateUtils.getDate NPE'd on the null).
The Q28 no-seed rule and this rule are the same rule: the dev-data honesty
standard. A screen whose write paths were NOT write-tested reports "not
erroring" (the call returns the backend's own envelope; success with real
data is UNVERIFIED), never "working".
