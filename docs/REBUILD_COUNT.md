# Rebuilt Screens — Count (LOG, not the source of truth)

> **THE AUTHORITATIVE FIGURE IS COMPUTED, NOT STORED HERE.**
> Recompute it at any time with:
> `ls new/src/app/features | grep -v -E '^(_decoy-groundwork|_recheck-20-bucket|_screen-template|access-denied)$' | wc -l`
> i.e. every feature directory under `new/src/app/features/` MINUS the
> exclusion set (`_decoy-groundwork`, `_recheck-20-bucket`,
> `_screen-template`, `access-denied` — 4 dirs). Each rebuilt screen = ONE
> feature directory with a live-routed, probed screen.
> THIS FILE IS A MERGE LOG. It drifts the moment a merge lands without a row;
> the computed figure never drifts. (2026-08-13: the file was found 21 rows
> behind — it said 357 against a computed 378. Fixed here; the computed
> figure is the count, this table is the history.)

## Computed count on 2026-08-13: **378 rebuilt** (382 dirs − 4 excluded)

| # | Merge | Screens added | Running total (log) |
|---|-------|---------------|---------------------|
| — | Census baseline (525-dir ledger, exclusions applied) | — | 339 |
| 1 | consentType real-data rebuild (8c08997d) | 1 | 340 |
| 2 | Group B (e34bf81c) | 4 | 344 |
| 3 | Master close-out (60273eba) | 11 | 355 |
| 4 | assignLoyaltyCard (5b1861e8) | 1 | 356 |
| 5 | transferScreen (65) | 1 | 357 |
| 6 | donorRegistration (719) — reclassified master-shell → transaction form | 0 | 357 |
| 7 | shiftClosing (600) — reclassified → transaction write form | 0 | 357 |
| 8 | phlebotomistHomeCollection (155) — first lab transaction form | 1 | 358 |
| 9 | crossMatchRegister — 5th transaction form (deposit-walkin batch) | 1 | 359 |
| 10 | bloodIssue — 6th signed flow (473fb869) | 1 | 360 |
| 11 | nursingOrder — 7th signed flow (473fb869) | 1 | 361 |
| 12 | labSampleType — 8th signed flow (473fb869) | 1 | 362 |
| 13 | first dashboard batch (af6d9af8) — fundStatusDashboard, storeStockDashboard | 2 | 364 |
| 14 | the nine-dashboards batch (fb10a05e) — erPatient, nurse, diagnostics, nursingAllocation, nursingShift, otManager (the doctorDashboard dir pre-existed; counted once) | 6 | 370 |
| 15 | reports batch (04a39885) — mlc-reporting-worklist, incident-category | 2 | 372 |
| 16 | the seven buildable (c11533a6) — radiology pair, due-deposit-report, consignment detail, MPShah trio | 6 net new after the census reconciliation | **378** |

## Update rule (revised 2026-08-13)
On every merge of a rebuilt screen (or screen group), append one row with the
merge hash and the running total — AND recompute the authoritative figure from
the directories. If the two disagree, the directories win and the row notes
the reconciliation. The count file is a log; it is never the source of truth.
