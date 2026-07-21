import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PmsFrequency, ChecklistEquipment, AssetScheduleChecklistItem, CreateAssetScheduleRow } from './asset-scheduler.models';

@Injectable({ providedIn: 'root' })
export class AssetSchedulerService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** Legacy's response here is a raw array (`response.data`), not the usual envelope. */
  getFrequencyList(organizationId: number): Observable<PmsFrequency[]> {
    return this.http.get<PmsFrequency[]>(`${this.base}/api/global/frequencyList/${organizationId}`);
  }

  getAllEquipmentChecklistMasterList(unitId: number, organizationId: number): Observable<ApiResponse<never, ChecklistEquipment>> {
    return this.http.post<ApiResponse<never, ChecklistEquipment>>(`${this.base}/global/equipmentchecklist/getAllEquipmentChecklistMasterList`, {
      unitId,
      organizationId,
    });
  }

  fetchAssetSchedule(
    itemNameId: number | undefined,
    serialNo: string | undefined,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, CreateAssetScheduleRow>> {
    return this.http.post<ApiResponse<never, CreateAssetScheduleRow>>(`${this.base}/api/assetManagement/maintenance/fetchAssetSchedule`, {
      itemNameId,
      serialNo,
      unitId,
      organizationId,
    });
  }

  saveAssetSchedule(rows: CreateAssetScheduleRow[]): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveAssetSchedule`, rows);
  }

  updateAssetSchedule(row: CreateAssetScheduleRow): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/updateAssetSchedule`, row);
  }

  getCheckListByAssetScheduleId(
    assetScheduleId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, AssetScheduleChecklistItem>> {
    return this.http.post<ApiResponse<never, AssetScheduleChecklistItem>>(
      `${this.base}/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId`,
      { assetScheduleId, unitId, organizationId },
    );
  }

  addAndUpdateChecklist(payload: {
    getChecklistData: AssetScheduleChecklistItem[];
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
}
