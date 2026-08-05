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
