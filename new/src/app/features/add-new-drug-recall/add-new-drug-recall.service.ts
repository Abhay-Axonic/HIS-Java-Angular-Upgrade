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

  /** GET /api/inventory/stores/stock/dashboard/item/batch/{unitId}/{itemId} */
  getItemWiseBatches(
    unitId: number,
    itemId: number,
  ): Observable<ApiResponse<never, BatchRecord>> {
    return this.http.get<ApiResponse<never, BatchRecord>>(
      `${this.base}/api/inventory/stores/stock/dashboard/item/batch/${unitId}/${itemId}`,
    );
  }

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
   * POST /api/pharmacy/in/patient/consumable/issue/list/search/drug/recall
   * The add-item flow's drug search (the legacy's RECALL_DRUG constant points
   * at /stores/recall/drug/search which has NO controller — FINDING; this
   * endpoint is the live one the legacy also calls for the same purpose).
   */
  searchDrugsForRecall(payload: Record<string, unknown>): Observable<ApiResponse<never, ItemSearchHit>> {
    return this.http.post<ApiResponse<never, ItemSearchHit>>(
      `${this.base}/api/pharmacy/in/patient/consumable/issue/list/search/drug/recall`,
      payload,
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
