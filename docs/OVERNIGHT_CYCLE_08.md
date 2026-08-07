# OVERNIGHT CYCLE 08 — extended run, procurement batch 3 VERIFIED (2026-08-06 ~22:40 → ~23:20 BST)

## 1. What was attempted and what actually happened

- pb3 = 6 quotation+purchase screens (addNewEnquirySent, addNewPO, addNewQuotation,
  addNewQuotationFillIn, detailsGRNApproval, addNewLocalPR), cumulative off pb2.
- Converted 6 deref sites (5 inline hoists + 1 BARE init deref — the QF-AR crash class — in
  detailsGRNApproval, which derefs the parse directly into $scope.storeId at init). 29 gates.
- Built, FULL-deployed (boot 22:37:57, 242.8s; healthcheck 200), suite PASS, fire-test run,
  frontend restored. Restore needed a REPAIR: my first restore command used 18 sources with 3
  destination directories — BSD cp rejects that shape and dumped the 18 files into
  procurement/purchase/ instead (a stray-file mess, not a source change). Moved the 17 strays
  out; drift re-verified clean (mpesa extra only).

## 2. Per-screen Q1/Q2

- addNewEnquirySent: Q1 yes (inline hoist). Q2 yes — 5 gates (GetItemList, GetQRList, AddItems,
  GetEnquiry, CreateEnquiry). allvendors() is unit-scoped (no storeId) — not gated (see 500 note).
- addNewPO: Q1 yes (inline hoist). Q2 yes — 7 gates (GetPO, GetIndentList, GetItemList,
  GetReorderPoItemList, AddItems, CreatePurchaseOrder, GetQRIndentList). AddIndentItems =
  org/unit-scoped, not gated.
- addNewQuotation: Q1 yes. Q2 yes — 5 gates (GetQuotationEnquiry, GetIndentList, GetItemList,
  AddItems, CreateQuotationEnquiry). AddIndentItems org/unit-scoped, not gated.
- addNewQuotationFillIn: Q1 yes. Q2 yes — 5 gates (GetItemList, GetQRList, AddItems, GetEnquiry,
  CreateEnquiry).
- detailsGRNApproval: Q1 yes (BARE init deref null-safe). Q2 yes — 2 gates (ActaulSaveGRNApproval,
  rePrintBillSlip). GetGRNList is by-id unit/org (no storeId) — not gated.
- addNewLocalPR: Q1 yes (inline hoist, the i!=0-guarded variant). Q2 yes — 5 gates (GetLocalPR,
  GetItemList, AddItems, AddIndentItems, CreateLocalPR). GetIndentList is from/to-store scoped
  (dropdown values, not the session store) — not gated.

## 3. Fire-tested vs gated-by-inspection

- FIRE-TESTED (load-time, both phases): all 6 states. 4 clean both phases; 2 interpreted 500s:
  detailsGRNApproval (dataId=0 direct navigation — invalid input, pre-existing) and
  addNewEnquirySentToVendor (cold-path vendor sentinel search, unit-scoped, no storeId). NEITHER
  is a gate failure (verified from the actual lines: no storeId in either path).
- Same-session probe: SKIP (no primary action button in load DOM) — Step 6.
- GATED-BY-INSPECTION: user-action paths (search/save/approve) — enumerated + gated.

## 4. Verification runs and raw results

- node --check x6: clean. Raw parses: 0. storeReady() counts 5/7/5/5/2/5 = 29/29.
- verify_rate_limit.py: 10/10 (clean buckets; TOKEN-OK).
- FIRE-TEST: [neg] 4 clean + 2 interpreted 500s; [pos] 4 clean + 1 interpreted 500
  (detailsGRNApproval dataId=0; the vendor search is clean post-pick).
- Drift after restore + repair: mpesa extra only.

## 5. Claims retracted or corrected mid-cycle

- MY OWN BROKEN RESTORE: a multi-destination cp (18 sources, 3 dirs) is invalid on BSD cp — it
  dumped the files into the LAST directory (procurement/purchase/) instead of copying per
  directory. Repaired by moving the 17 strays to /tmp and re-verifying drift. Lesson: one cp per
  destination directory, never multi-destination cp shapes. Recorded so the next restore uses
  three single-destination cp commands.
- The two fire-test 500s were initially "failures" — traced from the lines + catalina to
  pre-existing non-store behavior (see section 3). No campaign code change made for either.

## 6. Branches ready to merge (verified, awaiting owner)

- fix/store-accessor-campaign-pb1 @ 156fca68 (6 screens, 26 gates; cycle 06).
- fix/store-accessor-campaign-pb2 @ d0447003 (6 screens, 24 gates; cycle 07).
- fix/store-accessor-campaign-pb3 @ 26aba62b + docs pending (6 screens, 29 gates; cycle 08).
- Procurement total: 18 screens, 79 gates across pb1-pb3.

## 7. Decisions queued for Abhay

1. Rule 9b for pb1, pb2, pb3 (procurement workOrder + quotation + purchase families).
2. DRIFT item #5 (workorder_completion_status_id) — schema add still owner-gated.
3. getListNewWOList undefined-function defect (3 commented files with live callers) — restore or
   delete.
4. Pre-existing 500s logged (detailsGRNApproval dataId=0; vendor sentinel search cold path) —
   owners' call whether to fix the sentinel search (it 500s on every fresh login cold load).
5. ANGULAR_PENDING_FINDINGS item 2 (addItemPO route/controller disconnect) — pre-existing,
   untouched, flagged as still open.

## 8. What I will do next

- pb4: consignment (3) + loanInOut (4) screens; then the asset family (2 batches), pharmacy
  remainder (1), nursing (1), dashboards (full trace of one screen first), masters, the dead-code
  cleanup branch, and the drift-check tool.
