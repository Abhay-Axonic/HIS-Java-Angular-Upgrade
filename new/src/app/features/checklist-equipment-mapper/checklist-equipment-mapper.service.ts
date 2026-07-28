import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  AssetCategoryOption,
  ChecklistEquipmentMapperForm,
  ChecklistEquipmentMapperRow,
  ChecklistOption,
  ProductCategoryOption,
} from './checklist-equipment-mapper.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/global/equipmentMaster/checklistEquipmentMapperMasterController.js
 * -- not guessed. Confirmed live 2026-07-28: real create/upsert-update/
 * status-toggle/cascade round trips, all HTTP 200 (Product Category
 * required a real backend schema-drift fix first -- see
 * docs/BUSINESS_RULE_MANIFEST.md, BR-local-dev-db-drift-productcategory-generalledgerid).
 */
@Injectable({ providedIn: 'root' })
export class ChecklistEquipmentMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:64-77 -- Checklist Name dropdown source (unpaginated).
  getChecklistOptions(unitId: number, organizationId: number): Observable<ApiResponse<never, ChecklistOption>> {
    return this.http.post<ApiResponse<never, ChecklistOption>>(
      `${this.base}/global/equipmentchecklist/getAllEquipmentChecklistMasterList`,
      { unitId, organizationId },
    );
  }

  // ctrl:79-88 -- the `2` path segment is a fixed asset-type-code constant,
  // confirmed live, not guessed.
  getAssetCategoryOptions(unitId: number, organizationId: number): Observable<AssetCategoryOption[]> {
    return this.http.get<AssetCategoryOption[]>(`${this.base}/api/global/assets/categories/2/${unitId}/${organizationId}`);
  }

  // ctrl:118-130 -- cascades from Asset Category.
  getProductCategoryOptions(assetCategoryId: number, organizationId: number): Observable<ProductCategoryOption[]> {
    return this.http.get<ProductCategoryOption[]>(
      `${this.base}/api/global/products/categories/${assetCategoryId}/${organizationId}`,
    );
  }

  // ctrl:91-110.
  getMapperList(unitId: number, organizationId: number): Observable<ApiResponse<never, ChecklistEquipmentMapperRow>> {
    return this.http.post<ApiResponse<never, ChecklistEquipmentMapperRow>>(
      `${this.base}/global/equipmentchecklistmapper/getAllEquipmentChecklistMapperMasterList`,
      { unitId, organizationId },
    );
  }

  // ctrl:134-197 -- single upsert endpoint, real server-side duplicate
  // handling (same shape as checklistEquipmentMaster's sibling).
  save(
    payload: ChecklistEquipmentMapperForm & { organizationId: number; unitId: number; userId: number },
  ): Observable<ApiResponse> {
    const now = formatDateTime(new Date());
    return this.http.post<ApiResponse>(`${this.base}/global/equipmentchecklistmapper/saveChecklistEquipmentMapperMaster`, {
      checklistEquipmentMapperMasterId: payload.checklistEquipmentMapperMasterId,
      checklistEquipmentMasterId: payload.checklistEquipmentMasterId,
      assetCategoryId: payload.assetCategoryId,
      productCategoryId: payload.productCategoryId,
      itemCode: payload.itemCode,
      itemName: payload.itemName,
      status: 'A',
      createdBy: payload.userId,
      createdDate: now,
      updatedBy: payload.userId,
      updatedDate: now,
      unitId: payload.unitId,
      organizationId: payload.organizationId,
    });
  }

  // ctrl:199-233.
  updateStatus(payload: {
    checklistEquipmentMapperMasterId: number;
    status: ChecklistEquipmentMapperRow['status'];
    updatedBy: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/equipmentchecklistmapper/updateChecklistEquipmentMapperStatus`, {
      checklistEquipmentMapperMasterId: payload.checklistEquipmentMapperMasterId,
      status: payload.status,
      updatedBy: payload.updatedBy,
      updatedDate: formatDateTime(new Date()),
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:56/59/202. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
