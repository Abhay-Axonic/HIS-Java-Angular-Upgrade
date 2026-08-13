# Rebuilt Screens — Authoritative Count (single source of truth)

THIS FILE IS THE AUTHORITATIVE REBUILD COUNT. Updated at EVERY merge.
Do not reconstruct the count from branch docs, skills, or session history —
read it here. (Owner directive 2026-08-12: the census lives only in docs that
go stale on a branch; this file on main is the fix.)

## Current count: 357 rebuilt on main

| # | Merge | Screens added | Running total |
|---|-------|---------------|---------------|
| — | Census baseline (525-dir ledger, exclusions applied) | — | 339 |
| 1 | consentType real-data rebuild (8c08997d) | 1 (528) | 340 |
| 2 | Group B (e34bf81c) | 4 | 344 |
| 3 | Master close-out (60273eba) | 11 (597/598/600/625/635/650/660/719/722/745/746) | 355 |
| 4 | assignLoyaltyCard (5b1861e8) | 1 (752) | 356 |
| 5 | transferScreen (65) — pending merge of rebuild/transfer-screen | 1 (65) | **357** |
| 6 | donorRegistration (719) — RECLASSIFICATION: master-shell (row 3) -> transaction form (T0-T6 signed flow map, DQ-056); count unchanged (one dir counted once) — pending merge of rebuild/transfer-screen | 0 new | 357 |
| 7 | shiftClosing (600) — RECLASSIFICATION: honest-state shell (row 3) -> transaction write form (T0-T6 signed flow map: balance write BUILDABLE DQ-059, golden trace landed balance id 24, teardown clean; reads session-gated/honest; DQ-065/BR-040 auth measured: 12/12 writes token-validated, reads need the session — brief) — pending merge of rebuild/transfer-screen | 0 new | 357 |
| 8 | phlebotomistHomeCollection (155) — FIRST LAB-FAMILY TRANSACTION FORM (T0-T6 signed flow map: write BUILDABLE DQ-061, golden trace landed collection id 17, teardown clean; PARITY RECORD: legacy state ungated + menu module 155 gating -> route carries the guard(155) reproducing the menu gate; search feed session-gated/unwired) — pending merge of rebuild/transfer-screen | 1 new (155) | **358** |

## How the count is computed
- Denominator: 1,077 menu nodes; exclusion set = `_decoy-groundwork`,
  `_recheck-20-bucket`, `_screen-template`, `access-denied` (4 dirs).
- Each rebuilt screen = ONE feature directory under `new/src/app/features/`
  with a live-routed, probed screen. Denominator counts ONE dir per rebuild.
- Class accounting counts separately: noData shells, feeds, write-path
  screens, and transaction screens each count as rebuilt once live-verified.

## Update rule
On every merge of a rebuilt screen (or screen group), add one row to the
table above with the merge hash and the running total. The transaction phase
targets the remaining ~170 (≈108 transaction + 35 reports + 13 dashboards
per DQ-025/032/034) — update the count as each merges.
