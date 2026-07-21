import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AssetScheduleItem, AssetScheduleChecklistItem } from './view-asset-scheduler.models';

@Injectable({ providedIn: 'root' })
export class ViewAssetSchedulerService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  fetchAssetSchedule(
    itemNameId: number | undefined,
    serialNo: string | undefined,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, AssetScheduleItem>> {
    return this.http.post<ApiResponse<never, AssetScheduleItem>>(
      `${this.base}/api/assetManagement/maintenance/fetchAssetSchedule`,
      { itemNameId, serialNo, unitId, organizationId },
    );
  }

  fetchCheckListByAssetScheduleId(
    assetScheduleId: number,
    unitId: number,
    organizationId: number,
  ): Observable<ApiResponse<never, AssetScheduleChecklistItem>> {
    return this.http.post<ApiResponse<never, AssetScheduleChecklistItem>>(
      `${this.base}/api/assetManagement/maintenance/fetchCheckListByAssetScheduleId`,
      { assetScheduleId, unitId, organizationId },
    );
  }
}
