/**
 * add-new-drug-recall — legacy state `AddNewDrugRecall`
 *   views/PharmacyInventory/Store/add-new-drug-recall.html +
 *   resources/assets/js/controller/Inventory/addNewDrugRecallController.js
 * (module id 139 — RESOLVED 2026-08-08 from the Admin privilege tree
 *  (privilege.privilege modules JSON: the "Drug Recall" leaf, parent
 *  Store(123) -> Inventory(121)); the prior 220 assumption is RETRACTED).
 *
 * Endpoint paths taken from the legacy controller's serviceAction calls and
 * the constants.js values (pharmacyApi = rootUrl + '/api/pharmacy',
 * inventoryApi = rootUrl + '/api/inventory', globalApi = rootUrl +
 * '/api/global') — not guessed:
 *   DRUG_RECALL.SAVE_API     inventoryApi + '/stores/drug/recall'
 *   DRUG_RECALL.UPDATE_API   inventoryApi + '/stores/drug/recall/'
 *   DRUG_RECALL.DETAIL_API   inventoryApi + '/stores/drug/recall/'
 *   DRUG_RECALL.SEARCH_API   inventoryApi + '/stores/drug/recall/search'
 *   DRUG_RECALL.APPROVE_API  inventoryApi + '/stores/drug/recall/approve'
 *   DRUG_RECALL.RECALL_DRUG  inventoryApi + '/stores/recall/drug/search'  (see FINDING)
 *   STORE_STOCK_DASHBOARD.ITEM_WISE_BATCH_API
 *                            inventoryApi + '/stores/stock/dashboard/item/batch/'
 *   STORE_STOCK_DASHBOARD.BATCH_WISE_BATCH_API
 *                            inventoryApi + '/stores/stock/dashboard/item/batch/batch/'
 *   PATIENT_CONSUMABLE_ISSUE.ISSUE_SEARCH_FOR_DRUG_RECALL_API
 *                            pharmacyApi + '/in/patient/consumable/issue/list/search/drug/recall'
 *   GLOBAL.ASSET_CATEGORY_API  globalApi + '/assets/categories/'
 *   GLOBAL.PRODUCT_CATEGORY_API globalApi + '/products/categories/'
 *   GLOBAL.ITEM_API             globalApi + '/items/'
 *   GLOBAL.BATCH_API            globalApi + '/items/batches/'
 *
 * Backend handlers verified (DrugRecallController prefix
 * /api/inventory/stores/drug/recall, StoreStockController prefix
 * /api/inventory/stores/stock/dashboard, PatientConsumableIssueListController
 * prefix /api/pharmacy/in/patient/consumable/issue/list):
 *   POST  (prefix)   save     DrugRecallController
 *   PATCH /{id}      update   DrugRecallController
 *   GET   /{id}      detail   DrugRecallController
 *   POST  /search    search   DrugRecallController
 *   GET   /reason/dropdown    DrugRecallController
 *   PATCH /approve            DrugRecallController
 *   PATCH /cancel/{id}        DrugRecallController
 *   GET   /item/batch/{...}   StoreStockController
 *   GET   /item/batch/batch/{...} StoreStockController
 *   POST  /search/drug/recall PatientConsumableIssueListController
 *
 * FINDING (logged, not fixed): the legacy calls
 *   inventoryApi + '/stores/recall/drug/search' (DRUG_RECALL.RECALL_DRUG,
 *   the "recall drug" search inside the add-item flow) — NO controller
 *   exists for that path (grep across legacy/*Controller.java: 0 hits).
 *   The legacy screen's add-item recall-drug search is therefore already
 *   broken. The rebuild does not call it; the add-item flow uses the
 *   issue-search-for-drug-recall endpoint instead, and this is documented
 *   in README.md as a deliberate divergence on a dead legacy path.
 *
 * Methods and URL shapes are reproduced as-is — do not "normalize" them.
 */
export interface DrugRecallRecord {
  drugRecallId: number;
  storeId: number;
  storeName?: string;
  reasonId: number | null;
  reasonName?: string;
  recallDate?: string;
  status: 'A' | 'I';
  organizationId?: number;
  unitId?: number;
  [key: string]: unknown;
}

/** A recalled drug line as rendered in the grid and sent with the save. */
export interface DrugRecallItem {
  itemId: number;
  itemCode?: string;
  itemName?: string;
  batchId?: number;
  batchNo?: string;
  reason?: string;
  quantity: number;
  unitId?: number;
  organizationId?: number;
}

export interface ReasonOption {
  reasonId: number;
  reasonName: string;
}

export interface ItemSearchHit {
  itemId: number;
  itemCode?: string;
  itemName?: string;
  genericName?: string;
  unitId?: number;
  organizationId?: number;
  [key: string]: unknown;
}

export interface BatchRecord {
  batchId: number;
  batchNo?: string;
  expiryDate?: string;
  availableQuantity?: number;
  [key: string]: unknown;
}

export interface SaveDrugRecallPayload {
  storeId: number;
  reasonId: number | null;
  recallDate?: string;
  itemList: DrugRecallItem[];
  organizationId: number;
  unitId: number;
  createdBy?: number;
}

export interface UpdateDrugRecallPayload extends SaveDrugRecallPayload {
  drugRecallId: number;
}

export interface DrugRecallForm {
  storeId: number | null;
  reasonId: number | null;
  recallDate?: string;
  items: DrugRecallItem[];
}

export const emptyDrugRecallForm = (): DrugRecallForm => ({
  storeId: null,
  reasonId: null,
  items: [],
});
