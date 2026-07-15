# Sweep Plan — Codebase-wide Exception-Swallowing Pattern (6,175 occurrences)

**Status: planning document only. No occurrences fixed in this session or by this document.**
Owner review/approval required before any batch below is executed.

## Re-confirmed count (this session, 2026-07-14)

Re-ran `scripts/find_swallowed_return_pattern2.py` (the character-level scanner; do not use
`find_swallowed_return_pattern.py`, the earlier line-based heuristic that undercounts by ~2 orders
of magnitude) against the same five modules the prior session (BR-089) scanned:

| Module | Occurrences (this session) | Occurrences (BR-089, prior session) | Files |
|---|---|---|---|
| Global | 4,313 | 4,315 | 593 |
| ADT | 1,608 | 1,612 | 199 |
| Billing | 254 | 254 | 53 |
| CSSDMaster | 0 | 0 | 0 |
| Pharmacy | 0 | 0 | 0 |
| **Total** | **6,175** | **6,181** | **845** |

The count dropped by exactly 6 (Global −2, ADT −4), fully explained by the intervening, already-merged
BR-088/BR-089 fixes: both `patientSearchByMultipleCriteriaGrowthChart` (Global, 3 files) and
`saveDischargeRequest` (ADT, 3 files) now `return` the correlation-ID response **from inside each
`catch` block** rather than via a trailing unconditional `return` after the try/catch chain — which is
exactly the shape the scanner excludes (it only counts trailing-return methods where no `catch` body
itself contains a `return`/`throw`). No other explanation needed; the scan is stable and reproducible.

## Why this can't be one session

Precedent: BR-078's own sweep (715 catches / 123 files) needed multiple sessions. The single largest
one-session sweep on record, Pharmacy's (711 of 726 findings / 128 of 134 files), still required
individually reading every one of its 99 billing-critical findings in source before fixing — and that
was against a batch roughly **12x smaller** than this one. Mechanically fixing 6,175 occurrences without
that same per-finding discipline (confirm real live caller, confirm no fabricated-success-on-exception
variant, confirm message-content classification) risks exactly the kind of unreviewed mass edit
RULES.md's Maker-Checker and Behavioral-Change rules exist to prevent.

## Batching logic

Two axes, combined:

1. **Billing-criticality first** (CLAUDE.md mission order #1: billing accuracy is the top priority,
   ahead of DB work and version upgrades). "Billing-critical" = the `Billing` module itself, plus any
   `Global`/`ADT` file with a direct `import com.param.billing.*` (confirmed via grep against this
   session's scan output — 30 such files: 23 Global, 7 ADT).
2. **Module, then rough size**, for everything else — mirrors how BR-078/BR-089/Pharmacy were already
   scoped, and keeps each batch inside one Maven module so a single `mvn -pl <module> -am compile`
   verifies the whole batch.

| Batch | Scope | Occurrences | Files |
|---|---|---|---|
| **1 (do first)** | Billing-critical: all of `Billing` (254/53) + billing-importing `Global` files (219/23) + billing-importing `ADT` files (94/7) | **567** | **83** |
| 2–4 | `Global`, non-billing-critical (4,313 − 219 = 4,094), split ~3 sub-batches by package (e.g. alphabetically or by existing sub-domain folders under `com.param.global`) | ~4,094 | ~570 |
| 5–6 | `ADT`, non-billing-critical (1,608 − 94 = 1,514), split ~2 sub-batches | ~1,514 | ~192 |

Layer breakdown across all 6,175 (informational, not a batching axis by itself — controller-layer
occurrences are the ones with a directly-confirmed live HTTP entry point; service/dao-layer occurrences
require one more hop of call-chain tracing per occurrence, same discipline BR-089/Pharmacy already used,
not pre-classified here at this scale):

| Layer | Occurrences |
|---|---|
| `controller/` | 2,000 |
| `service/` | 1,746 |
| `dao/` | 2,420 |
| other | ~9 |

## Recommended batch size and session count

Pharmacy's 711/726-in-one-session result is the largest verified precedent, and it still needed the
full care described above. Recommend capping each future session at **~700 occurrences**, matching that
precedent rather than exceeding it. At that size:

- Batch 1 (567/83 files, billing-critical): **1 session** — smaller than the cap, but do not merge it
  with Batch 2 just to hit the cap; billing-critical work should stand alone for Checker review.
  See `docs/DECISION_NEEDED_Pharmacy_NullProbeIdiom.md`-style flagging precedent — expect to find a
  similar "null-probe idiom" or self-manufactured-`throw`-as-goto subset within this batch that should
  be held out for owner decision rather than mechanically fixed, per Pharmacy's finding of the same
  patterns in its own billing-critical subset.
- Batches 2–7 (Global non-billing, ~4,094 total): **~6 sessions** at ~680/session.
- Batches 8–10 (ADT non-billing, ~1,514 total): **~3 sessions** at ~505/session.

**Total recommended: ~10 future sweep sessions**, batch 1 first.

**Standing requirement, all batches (not just Batch 1):** per RULES.md item 6, every backend
exception-handling change requires Maker-Checker review (a separate session/model) before any human
sign-off is sought. This applies to every batch in this plan, 1 through 10 — the Batch-1-specific
note above should not be read as implying Batches 2-10 are exempt.

## Which batch first, and why

**Batch 1 (billing-critical, 567/83 files) should go first**, ahead of the numerically larger Global/ADT
non-billing batches, for two reasons:
1. CLAUDE.md's mission order is explicit and non-negotiable: billing accuracy is priority #1, ahead of
   DB work and version upgrades — this document defers to that ordering rather than optimizing for
   "smallest batch first" or "fewest files first."
2. It is also, independently, the smallest and most tractable batch (567 occurrences/83 files — smaller
   than Pharmacy's own 726/134 precedent), so there is no tradeoff being made here between risk and
   effort; the highest-value batch happens to also be the easiest one to execute carefully.

## Explicit instruction to whoever runs a batch

- Do not fix mechanically. Read each occurrence's real call chain before changing it, exactly as
  BR-089/Pharmacy did — confirm a real live caller exists, confirm no `catch` fabricates a `SUCCESS`
  response, and classify each fix's manifest entry as Behavioral Change (message content changes) unless
  proven byte-identical.
- Compile-check per module after each batch: `mvn -pl <module> -am compile -DskipTests -o -q` from
  `legacy/`.
- Log every batch's fixes in `docs/BUSINESS_RULE_MANIFEST.md` (+ `docs/BR_INDEX.md`), same as
  BR-078/BR-088/BR-089.
- This document does not commit the owner to any specific batch order beyond Batch 1 — Batches 2–10's
  internal split points (which Global sub-packages, which ADT sub-packages) are a starting suggestion,
  not a fixed contract; re-derive them from whatever `com.param.global`/ADT sub-domain structure looks
  most natural at the time, same as this session found Global's own sub-packages (`ipd`, `templet`, etc.)
  only by reading the scan output, not by assuming a structure in advance.
