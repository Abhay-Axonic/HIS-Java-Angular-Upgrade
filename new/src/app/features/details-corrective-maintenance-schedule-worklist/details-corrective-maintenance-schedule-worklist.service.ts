import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DcmswReplaceDetail,
  DcmswSerialOption,
  DcmswSpareConsumption,
  DcmswSpareSearchResult,
  DcmswStockRow,
  DcmswUser,
  DcmswVendorObj,
  DcmswVendorOption,
} from './details-corrective-maintenance-schedule-worklist.models';

@Injectable({ providedIn: 'root' })
export class DetailsCorrectiveMaintenanceScheduleWorklistService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getEmployeeList(payload: Record<string, unknown>): Observable<ApiResponse<never, DcmswUser>> {
    return this.http.post<ApiResponse<never, DcmswUser>>(`${this.base}/api/global/getEmployeeListBySpecialityAndSubSpeciality`, payload);
  }

  saveAllocation(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCorrectiveMaintenance/worklist`, payload);
  }

  getVendorListByBatch(payload: Record<string, unknown>): Observable<ApiResponse<DcmswVendorObj>> {
    return this.http.post<ApiResponse<DcmswVendorObj>>(`${this.base}/api/assetManagement/maintenance/getvendorlistByBatchId`, payload);
  }

  /** Raw array response (not ApiResponse-wrapped) -- see model doc comment on `DcmswVendorOption`. Double-slash is intentional, matches real controller base path. */
  getVendorList(organizationId: number, unitId: number): Observable<DcmswVendorOption[]> {
    return this.http.get<DcmswVendorOption[]>(`${this.base}/api/unit/vendors//${organizationId}/${unitId}`);
  }

  searchSpareItem(payload: Record<string, unknown>): Observable<ApiResponse<never, DcmswSpareSearchResult>> {
    return this.http.post<ApiResponse<never, DcmswSpareSearchResult>>(`${this.base}/api/assetManagement/maintenance/getCorrectiveMainSparesComsumptionByitemId`, payload);
  }

  /** Raw array response (not ApiResponse-wrapped) -- see `DcmswStockRow` doc comment. */
  checkItemStock(payload: Record<string, unknown>): Observable<DcmswStockRow[]> {
    return this.http.post<DcmswStockRow[]>(`${this.base}/api/inventory/stores/stock/dashboard/searchdatewise`, payload);
  }

  getSparesConsumptionList(payload: Record<string, unknown>): Observable<ApiResponse<never, DcmswSpareConsumption>> {
    return this.http.post<ApiResponse<never, DcmswSpareConsumption>>(`${this.base}/api/assetManagement/maintenance/getSparesComsumptionlistById`, payload);
  }

  getReplaceDetails(payload: Record<string, unknown>): Observable<ApiResponse<never, DcmswReplaceDetail>> {
    return this.http.post<ApiResponse<never, DcmswReplaceDetail>>(`${this.base}/api/assetManagement/maintenance/getreplaceItemById`, payload);
  }

  getSerialNoByItemId(itemId: number | string, unitId: number, organizationId: number): Observable<ApiResponse<never, DcmswSerialOption>> {
    return this.http.get<ApiResponse<never, DcmswSerialOption>>(
      `${this.base}/api/assetManagement/maintenance/getSerialNoByItemId/${itemId}/${unitId}/${organizationId}`,
    );
  }

  updateStartedTask(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/cancelCorrectiveMaintenanceRequest`, payload);
  }

  saveResolution(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/assetManagement/maintenance/saveCorrectiveMaintenanceSchedule`, payload);
  }
}
