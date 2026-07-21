import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ScheduleWorklistUser, ScheduleWorklistStore, AssetScheduleRow, ScheduleWorklistCriteria } from './schedule-worklist.models';

@Injectable({ providedIn: 'root' })
export class ScheduleWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getEmployeeListBySpecialityAndSubSpeciality(
    organizationId: number,
    unitId: number,
    specialityId: number,
  ): Observable<ApiResponse<never, ScheduleWorklistUser>> {
    return this.http.post<ApiResponse<never, ScheduleWorklistUser>>(
      `${this.base}/api/global/getEmployeeListBySpecialityAndSubSpeciality`,
      { organizationId, unitId, specialityId },
    );
  }

  getDetailedStoreList(userId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, ScheduleWorklistStore>> {
    return this.http.get<ApiResponse<never, ScheduleWorklistStore>>(
      `${this.base}/api/global/getDetailedStoreList/${userId}/${organizationId}/${unitId}`,
    );
  }

  getAssetDetailsByCriteria(criteria: ScheduleWorklistCriteria): Observable<ApiResponse<never, AssetScheduleRow>> {
    return this.http.post<ApiResponse<never, AssetScheduleRow>>(
      `${this.base}/api/assetManagement/maintenance/getAssetDetailsByCriteria`,
      criteria,
    );
  }
}
