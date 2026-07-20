import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PmScheduleUser,
  PmScheduleStore,
  PmAssetScheduleRow,
  PmScheduleCriteria,
  ManufacturerRow,
  VendorRow,
  AccessoryItemRow,
  AssetScheduleChecklistRow,
  ScheduledCompletionAssignedData,
} from './pm-schedule.models';

@Injectable({ providedIn: 'root' })
export class PmScheduleService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetailedStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, PmScheduleStore>> {
    return this.http.get<ApiResponse<never, PmScheduleStore>>(
      `${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`,
    );
  }

  getEmployeeListBySpecialityAndSubSpeciality(
    organizationId: number,
    unitId: number,
    specialityId: number,
  ): Observable<ApiResponse<never, PmScheduleUser>> {
    return this.http.post<ApiResponse<never, PmScheduleUser>>(`${this.base}/api/global/getEmployeeListBySpecialityAndSubSpeciality`, {
      organizationId,
      unitId,
      specialityId,
    });
  }

  getAssetDetailsByCriteria(criteria: PmScheduleCriteria): Observable<ApiResponse<never, PmAssetScheduleRow>> {
    return this.http.post<ApiResponse<never, PmAssetScheduleRow>>(
      `${this.base}/api/assetManagement/maintenance/getAssetDetailsByCriteria`,
      criteria,
    );
  }

  /** Legacy's response here is a raw array, not the usual {status,listObject} envelope. */
  getManufacturersByOrg(organizationId: number): Observable<ManufacturerRow[]> {
    return this.http.get<ManufacturerRow[]>(`${this.base}/api/globals/manufacturers/${organizationId}`);
  }

  getVendorsByUnit(unitId: number, organizationId: number): Observable<VendorRow[]> {
    return this.http.get<VendorRow[]>(`${this.base}/api/unit/vendors/${unitId}/${organizationId}`);
  }

  getAccessoryItems(
    unitId: number,
    body: { assetTypeId: number; assetCategoryId?: number; productCategoryId?: number; storeId?: number },
  ): Observable<AccessoryItemRow[]> {
    return this.http.post<AccessoryItemRow[]>(`${this.base}/api/global/items/unit/${unitId}`, body);
  }

  getCheckListByAssetScheduleId(
    assetScheduleId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, AssetScheduleChecklistRow>> {
    return this.http.post<ApiResponse<never, AssetScheduleChecklistRow>>(
      `${this.base}/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId`,
      { assetScheduleId, unitId, organizationId },
    );
  }

  fetchScheduledCompletionAssignedData(
    assetScheduleId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, ScheduledCompletionAssignedData>> {
    return this.http.post<ApiResponse<never, ScheduledCompletionAssignedData>>(
      `${this.base}/api/assetManagement/maintenance/fetchScheduledCompletionAssignedData`,
      { assetScheduleId, unitId, organizationId },
    );
  }

  saveAssignDetails(payload: {
    assetScheduleId: number | undefined;
    assignTo: number;
    assignOn: Date | string;
    assignTime: string;
    assignRemark: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveAssignDetails`, payload);
  }
}
