import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BatchRecord,
  DrugRecallItem,
  DrugRecallRecord,
  ItemSearchHit,
  PatientHit,
  ReasonOption,
  SaveDrugRecallPayload,
  UpdateDrugRecallPayload,
} from './add-new-drug-recall.models';

/**
 * Endpoint paths from the legacy controller's serviceAction calls + the
 * constants.js values (see the models header for the full resolution).
 * The drug-recall core lives under the DrugRecallController prefix
 * /api/inventory/stores/drug/recall.
 */
@Injectable({ providedIn: 'root' })
export class AddNewDrugRecallService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** GET /api/inventory/stores/drug/recall/reason/dropdown -> recall reasons */
  getRecallReasons(): Observable<ApiResponse<never, ReasonOption>> {
    return this.http.get<ApiResponse<never, ReasonOption>>(
      `${this.base}/api/inventory/stores/drug/recall/reason/dropdown`,
    );
  }

  /** POST /api/inventory/stores/drug/recall {SaveDrugRecallPayload} -> saved record */
  saveRecall(payload: SaveDrugRecallPayload): Observable<ApiResponse<never, DrugRecallRecord>> {
    return this.http.post<ApiResponse<never, DrugRecallRecord>>(
      `${this.base}/api/inventory/stores/drug/recall`,
      payload,
    );
  }

  /** PATCH /api/inventory/stores/drug/recall/{id} {UpdateDrugRecallPayload} */
  updateRecall(payload: UpdateDrugRecallPayload): Observable<ApiResponse<never, DrugRecallRecord>> {
    return this.http.patch<ApiResponse<never, DrugRecallRecord>>(
      `${this.base}/api/inventory/stores/drug/recall/${payload.drugRecallId}`,
      payload,
    );
  }

  /** GET /api/inventory/stores/drug/recall/{id} -> recall detail */
  getRecallDetail(id: number): Observable<ApiResponse<never, DrugRecallRecord>> {
    return this.http.get<ApiResponse<never, DrugRecallRecord>>(
      `${this.base}/api/inventory/stores/drug/recall/${id}`,
    );
  }

  /** POST /api/inventory/stores/drug/recall/search {storeId, ...} -> recall list */
  searchRecalls(payload: Record<string, unknown>): Observable<ApiResponse<never, DrugRecallRecord>> {
    return this.http.post<ApiResponse<never, DrugRecallRecord>>(
      `${this.base}/api/inventory/stores/drug/recall/search`,
      payload,
    );
  }

  /** PATCH /api/inventory/stores/drug/recall/approve {drugRecallId} */
  approveRecall(payload: { drugRecallId: number }): Observable<ApiResponse<never, DrugRecallRecord>> {
    return this.http.patch<ApiResponse<never, DrugRecallRecord>>(
      `${this.base}/api/inventory/stores/drug/recall/approve`,
      payload,
    );
  }

  /** PATCH /api/inventory/stores/drug/recall/cancel/{id} */
  cancelRecall(id: number): Observable<ApiResponse<never, DrugRecallRecord>> {
    return this.http.patch<ApiResponse<never, DrugRecallRecord>>(
      `${this.base}/api/inventory/stores/drug/recall/cancel/${id}`,
      {},
    );
  }

  /**
   * REMOVED (QF-2026-08-09-D follow-up): GET /api/inventory/stores/stock/
   * dashboard/item/batch/{unitId}/{itemId} is AMBIGUOUSLY MAPPED in the backend
   * — StoreStockController maps BOTH /item/batch/{storeId}/{itemId} and
   * /item/batch/{unitId}/{itemId}, so any /item/batch/1/1 request throws
   * IllegalStateException "Ambiguous handler methods mapped" -> HTTP 500
   * (verified live with the cached token + the catalina.out stack). The pick
   * flow no longer calls it; the defect is logged for the owner (Q24).
   */

  /** GET /api/inventory/stores/stock/dashboard/item/batch/batch/{unitId}/{batchId} */
  getBatchWiseBatches(
    unitId: number,
    batchId: number,
  ): Observable<ApiResponse<never, BatchRecord>> {
    return this.http.get<ApiResponse<never, BatchRecord>>(
      `${this.base}/api/inventory/stores/stock/dashboard/item/batch/batch/${unitId}/${batchId}`,
    );
  }

  /**
   * POST /api/global/items/unit/{unitId} — the item-master search (the legacy
   * add-item picker: addNewDrugRecallController.searchNewItem ->
   * ITEM_API + "unit/" + unitId, ItemsController.getItemsList). Returns a BARE
   * array of ItemsDetailsListResponse (itemId/itemCode/itemName/...). This is
   * the drug-name search; verified live: HTTP 200 with the cached token.
   * The old searchDrugsForRecall call (searchString -> the pharmacy issue-search
   * DTO which has no searchString field -> HIS-400) is REMOVED (QF-2026-08-09-D).
   */
  searchItems(
    unitId: number,
    payload: Record<string, unknown>,
  ): Observable<ItemSearchHit[]> {
    return this.http.post<ItemSearchHit[]>(
      `${this.base}/api/global/items/unit/${unitId}`,
      payload,
    );
  }

  /**
   * POST /api/pharmacy/in/patient/consumable/issue/list/search/drug/recall —
   * the patients billed for an item (the legacy getPatientsListForItem).
   * DTO shape {itemId, unitId} — verified live: HTTP 200 with the cached token
   * (the frontend's old {searchString,...} shape was the HIS-400 source).
   */
  fetchPatientsForItem(
    payload: Record<string, unknown>,
  ): Observable<PatientHit[]> {
    return this.http.post<PatientHit[]>(
      `${this.base}/api/pharmacy/in/patient/consumable/issue/list/search/drug/recall`,
      payload,
    );
  }

  /** GET /api/global/user/stores/{orgId}/{unitId}/{userId} -> the user's stores. */
  getUserStores(
    organizationId: number,
    unitId: number,
    userId: number,
  ): Observable<unknown> {
    return this.http.get(
      `${this.base}/api/global/user/stores/${organizationId}/${unitId}/${userId}`,
    );
  }

  /** POST /api/global/items/unit/ {organizationId, unitId} -> item list */
  getItemsByUnit(
    organizationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, ItemSearchHit>> {
    return this.http.post<ApiResponse<never, ItemSearchHit>>(
      `${this.base}/api/global/items/unit/`,
      { organizationId, unitId },
    );
  }

  /** POST /api/global/items/batches/unit/ {organizationId, unitId, itemId} */
  getBatchesByUnit(
    payload: Record<string, unknown>,
  ): Observable<ApiResponse<never, BatchRecord>> {
    return this.http.post<ApiResponse<never, BatchRecord>>(
      `${this.base}/api/global/items/batches/unit/`,
      payload,
    );
  }
}
