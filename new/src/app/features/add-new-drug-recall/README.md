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
| STORE_STOCK_DASHBOARD.ITEM_WISE_BATCH_API | GET /api/inventory/stores/stock/dashboard/item/batch/{unitId}/{itemId} | StoreStockController | verified |
| STORE_STOCK_DASHBOARD.BATCH_WISE_BATCH_API | GET /api/inventory/stores/stock/dashboard/item/batch/batch/{unitId}/{batchId} | StoreStockController | verified |
| PATIENT_CONSUMABLE_ISSUE.ISSUE_SEARCH_FOR_DRUG_RECALL_API | POST /api/pharmacy/in/patient/consumable/issue/list/search/drug/recall | PatientConsumableIssueListController | verified |
| GLOBAL.ASSET_CATEGORY_API | GET /api/global/assets/categories/ | AssetCategoryController | verified |
| GLOBAL.PRODUCT_CATEGORY_API | GET /api/global/products/categories/ | AssetCategoryController | verified |
| GLOBAL.ITEM_API | POST /api/global/items/unit/ | (item master search) | verified |
| GLOBAL.BATCH_API | POST /api/global/items/batches/unit/ | (batch search) | verified |

## FINDING — dead legacy endpoint (logged, not fixed)

`DRUG_RECALL.RECALL_DRUG` = `inventoryApi + '/stores/recall/drug/search'`
(the "recall drug" search inside the legacy add-item flow) has **NO backend
controller** — 0 hits across `legacy/*Controller.java` (grep for
`recall/drug/search`). The legacy screen's recall-drug search is therefore
already broken. The rebuild does NOT call that dead path; the add-item search
uses the live `POST /api/pharmacy/in/patient/consumable/issue/list/search/drug/recall`
endpoint the legacy also calls for the same purpose. Logged to
DECISIONS_QUEUE.md tagged `add-new-drug-recall`.

## Rebuilt behaviours

- Store + reason selects, recall date, the recalled-drug grid
  (item code / item name / batch / reason / quantity / actions), the
  add-item modal with the drug search + batch fetch, the remove-line action,
  the save with the session org/unit (store-accessor pattern; the legacy read
  them from the cookie).

## Deliberate divergences (the serviceMaster model)

1. **Dead-path replacement**: the legacy's recall-drug search
   (`/stores/recall/drug/search`, no controller) is replaced by the live
   consumable-issue search endpoint — the legacy path is broken, so this is
   not a behaviour regression.
2. **Two-step save simplified**: the legacy Save opens a confirm modal and the
   request fires on the modal's OK; the rebuild saves directly (one click) —
   parity divergence, flagged (same model as serviceMaster's divergence #3).
3. **Details popup simplified**: the legacy "Details" buttons open a rich
   popup; the rebuild shows a read-only line summary — flagged.
4. **Item modal search**: the legacy loads categories (asset/product) into the
   modal; the rebuild searches the live drug-search endpoint directly — the
   category filters are not rebuilt (flagged, not silently dropped).

## Not yet done

- The approve / cancel flows (the screen's list side — `drug-recall.html` is
  the sibling screen) are out of this screen's scope; the service methods
  exist for them.
- The store dropdown's data source (the legacy loads the store list from the
  org endpoint) — the rebuild's store select is bound to the form; the store
  list fetch is pending the org endpoint contract (see the service TODO in the
  click-through notes).

## Verification state

WRITTEN, NOT BUILT. The screen needs `ng build` + a click-through by the owner
(approvals.mode=smart denies npx for the agent). TypeScript reviewed by eye +
the LSP; endpoints traced to real controllers (table above); parity documented
here. Click-through: log in, open the route `addNewDrugRecall` (module 220),
pick a store, add an item, save — the save should POST
`/api/inventory/stores/drug/recall` and the growl should show the response
message; the item search should hit
`/api/pharmacy/in/patient/consumable/issue/list/search/drug/recall`.
