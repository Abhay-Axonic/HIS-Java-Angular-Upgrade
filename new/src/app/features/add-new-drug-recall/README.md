# add-new-drug-recall — parity rebuild of the legacy Add New Drug Recall screen

Legacy source:
- template: `views/PharmacyInventory/Store/add-new-drug-recall.html` (1,166 lines)
- controller: `resources/assets/js/controller/Inventory/addNewDrugRecallController.js` (28 KB)
- legacy state: `AddNewDrugRecall`, module id **139** — RESOLVED 2026-08-08
  from the Admin privilege tree (privilege.privilege modules JSON: the "Drug
  Recall" leaf, parent Store(123) -> Inventory(121)). The prior 220 assumption
  is retracted. The Admin role's tree contains 139 (role_id=1), so the guard's
  permission row exists for the owner's login.

## Endpoints (all verified against the backend)

| Legacy constant | Endpoint | Handler | Status |
|---|---|---|---|
| DRUG_RECALL.SAVE_API | POST /api/inventory/stores/drug/recall | DrugRecallController.save | verified |
| DRUG_RECALL.UPDATE_API | PATCH /api/inventory/stores/drug/recall/{id} | DrugRecallController.update | verified |
| DRUG_RECALL.DETAIL_API | GET /api/inventory/stores/drug/recall/{id} | DrugRecallController.getById | verified |
| DRUG_RECALL.SEARCH_API | POST /api/inventory/stores/drug/recall/search | DrugRecallController.search | verified |
| (reason dropdown) | GET /api/inventory/stores/drug/recall/reason/dropdown | DrugRecallController | verified |
| DRUG_RECALL.APPROVE_API | PATCH /api/inventory/stores/drug/recall/approve | DrugRecallController | verified |
| (cancel) | PATCH /api/inventory/stores/drug/recall/cancel/{id} | DrugRecallController | verified |
| STORE_STOCK_DASHBOARD.ITEM_WISE_BATCH_API | GET /api/inventory/stores/stock/dashboard/item/batch/{unitId}/{itemId} | StoreStockController | **AMBIGUOUS — HTTP 500 (Q24)** |
| STORE_STOCK_DASHBOARD.BATCH_WISE_BATCH_API | GET /api/inventory/stores/stock/dashboard/item/batch/batch/{unitId}/{batchId} | StoreStockController | verified |
| PATIENT_CONSUMABLE_ISSUE.ISSUE_SEARCH_FOR_DRUG_RECALL_API | POST /api/pharmacy/in/patient/consumable/issue/list/search/drug/recall | PatientConsumableIssueListController | **verified (200, DTO shape {itemId, unitId}); the {searchString,...} shape 400s (QF-2026-08-09-D)** |
| GLOBAL.ASSET_CATEGORY_API | GET /api/global/assets/categories/ | AssetCategoryController | verified |
| GLOBAL.PRODUCT_CATEGORY_API | GET /api/global/products/categories/ | AssetCategoryController | verified |
| GLOBAL.ITEM_API | POST /api/global/items/unit/{unitId} | ItemsController.getItemsList | **verified (200, live call — the modal's picker)** |

## RETRACTION — the RECALL_DRUG "no controller" claim (2026-08-09)

The earlier claim that `DRUG_RECALL.RECALL_DRUG` (`inventoryApi + '/stores/recall/drug/search'`)
has no backend controller is **WRONG** — a literal grep for `recall/drug/search` misses it
because the path is assembled from fragments: `RecallDrugController` maps
`@RequestMapping("/api/inventory/stores/recall/drug")` on the class and
`@RequestMapping("/search")` on the method — no single annotation contains the full
literal path. Verified live: `POST /api/inventory/stores/recall/drug/search` with the
cached token -> HTTP 200 (its query lists existing DrugRecall records by date; `[]` in
dev = no recall records). This is the **fragment-mapping false-negative class** for
literal greps — see the QA finding (2026-08-09) and the parked deriver's E2 record.

## Q23 — the add-item modal's semantic-match (implemented 2026-08-09)

The old modal search POSTed `{searchString, organizationId, unitId}` to the pharmacy
issue-search endpoint — whose DTO has no `searchString` field — Jackson rejected the
body (HIS-400) and the screen showed "Item search failed" (QF-2026-08-09-D). The modal
now matches the legacy interaction: the picker searches the **item master**
(`POST /api/global/items/unit/{unitId}`, the legacy `searchNewItem` -> `ITEM_API +
"unit/" + unitId`, 200 verified live) and the pick feeds the **DTO-shaped
`{itemId, unitId}` patients lookup** (the pharmacy issue-search, 200 verified live).
The old searchString call and the batch fetch are removed — the batch endpoint
(`/item/batch/{unitId}/{itemId}`) is ambiguously mapped in StoreStockController (both
`{storeId}/{itemId}` and `{unitId}/{itemId}` match) and returns HTTP 500
"Ambiguous handler methods mapped" (verified live + the catalina.out stack; Q24).

## Rebuilt behaviours

- Store + reason selects, recall date, the recalled-drug grid
  (item code / item name / batch / reason / quantity / actions), the
  add-item modal with the drug search + batch fetch, the remove-line action,
  the save with the session org/unit (store-accessor pattern; the legacy read
  them from the cookie).
- Grid sort: the legacy sorts the grid CLIENT-SIDE via
  `ng-repeat="item in vm.model.itemDetailList | orderBy:sortType:sortReverse"`
  (add-new-drug-recall.html:114) on the itemCode/itemName/reason columns, the
  header clicks setting `sortType` and toggling `sortReverse`. The rebuild
  reproduces it as the `sortedItems` computed + the `onSort` header handler
  (a compile fix: the header clicks were initially copied without the sort,
  which also left `sortType` undeclared and failed the build at
  .html:52-53 — the sort is now implemented, not the binding removed, because
  the legacy demonstrably sorts).

## Deliberate divergences (the serviceMaster model)

1. **Dead-path replacement (RETRACTED)**: the earlier claim that the legacy's
   recall-drug search (`/stores/recall/drug/search`) has no controller was WRONG
   (fragment-built mapping — see the RETRACTION above). The rebuild's modal
   previously called the pharmacy issue-search with a `{searchString,...}` payload
   that the DTO rejects (HIS-400 — QF-2026-08-09-D); the Q23 fix routes the picker
   through the item master and the patients lookup with DTO-shaped payloads (both
   200 verified).
2. **Two-step save simplified**: the legacy Save opens a confirm modal and the
   request fires on the modal's OK; the rebuild saves directly (one click) —
   parity divergence, flagged (same model as serviceMaster's divergence #3).
3. **Details popup simplified**: the legacy "Details" buttons open a rich
   popup; the rebuild shows a read-only line summary — flagged.
4. **Item modal search**: the legacy loads categories (asset/product) into the
   modal; the rebuild searches the item master directly (Q23 — the old
   searchString call is removed) — the category filters are not rebuilt
   (flagged, not silently dropped). The add-item modal's batch/quantity/reason
   fields were completed in the build-fix pass (the first version's hit-click
   fetched batches but added nothing — a dead-end the template-vs-class sweep
   caught; the batch fetch is now removed because its endpoint 500s — Q24).

## Not yet done

- The approve / cancel flows (the screen's list side — `drug-recall.html` is
  the sibling screen) are out of this screen's scope; the service methods
  exist for them.
- The store dropdown's data source (the legacy loads the store list from the
  org endpoint) — the rebuild's store select is bound to the form; the store
  list fetch is pending the org endpoint contract (see the service TODO in the
  click-through notes).

## Verification state — HONEST (2026-08-09)

The guard admits (moduleId 139, store 827 — clean-session verified by the owner's
incognito run), the three gates pass (preflight C1-C4 + C5 DEAD-flag, `ng build`
exit 0, the smoke PASSes both the standard and the `--exercise-modal para` runs —
no toast, no non-2xx; the exercised request is `HTTP 200 /api/global/items/unit/1`).
The modal's picker and patients lookup are **NOT ERRORING, not proven useful**:
dev data is essentially empty (`public.m_item` holds 1 row; no sales/recall
records), so a working picker and a broken one both render empty. The
pick-and-lookup path (onItemPick -> the patients lookup) is exercised in code but
cannot produce a row until seed data exists. Nothing was seeded to make it look
better.
