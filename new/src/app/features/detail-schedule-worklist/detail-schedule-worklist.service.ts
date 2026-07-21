import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DswChecklistEquipment,
  DswStore,
  DswAssetChecklistItem,
  DswItemSearchResult,
  DswPmsDetails,
} from './detail-schedule-worklist.models';

@Injectable({ providedIn: 'root' })
export class DetailScheduleWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getAllEquipmentChecklistMasterList(unitId: number, organizationId: number): Observable<ApiResponse<never, DswChecklistEquipment>> {
    return this.http.post<ApiResponse<never, DswChecklistEquipment>>(`${this.base}/global/equipmentchecklist/getAllEquipmentChecklistMasterList`, {
      unitId,
      organizationId,
    });
  }

  getDetailedStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, DswStore>> {
    return this.http.get<ApiResponse<never, DswStore>>(`${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`);
  }

  fetchPMSByPMSId(pmsDetailsId: number, unitId: number, organizationId: number): Observable<ApiResponse<DswPmsDetails>> {
    return this.http.post<ApiResponse<DswPmsDetails>>(`${this.base}/api/assetManagement/maintenance/fetchPMSByPMSId`, {
      pmsDetailsId,
      unitId,
      organizationId,
    });
  }

  getCheckListByAssetScheduleId(assetScheduleId: number, unitId: number, organizationId: number): Observable<ApiResponse<never, DswAssetChecklistItem>> {
    return this.http.post<ApiResponse<never, DswAssetChecklistItem>>(`${this.base}/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId`, {
      assetScheduleId,
      unitId,
      organizationId,
    });
  }

  addAndUpdateChecklist(payload: {
    getChecklistData: DswAssetChecklistItem[];
    createdBy: number;
    createdDate: string;
    unitId: number;
    organizationId: number;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/addAndUpdateChecklist`, payload);
  }

  deleteChecklist(assetScheduleChecklistId: number, createdBy: number, createdDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/deleteChecklist`, {
      assetScheduleChecklistId,
      createdBy,
      createdDate,
    });
  }

  searchItemByCodeOrName(
    itemCode: string,
    itemName: string,
    itemId: number | undefined,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, DswItemSearchResult>> {
    return this.http.post<ApiResponse<never, DswItemSearchResult>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMainSparesComsumptionByitemId`, {
      itemCode,
      itemName,
      itemId,
      unitId,
      organizationId,
    });
  }

  deleteSpareById(spareConsumptionId: number, createdBy: number, createdDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/deleteSpareById`, {
      spareConsumptionId,
      createdBy,
      createdDate,
    });
  }

  saveOrUpdatePMSDetails(payload: Record<string, unknown>): Observable<ApiResponse<{ pmsDetailsId: number }>> {
    return this.http.post<ApiResponse<{ pmsDetailsId: number }>>(`${this.base}/api/assetManagement/maintenance/saveOrUpdatePMSDetails`, payload);
  }
}
