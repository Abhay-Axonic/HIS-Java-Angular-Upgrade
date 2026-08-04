import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { VisitTypeRow, VisitTypeStatusPayload } from './visit-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/global/visitTypeMasterController.js
 * and resources/assets/js/config/billing.url.config.js:95-100 -- not
 * guessed. Golden-traced live 2026-08-04 (batch-3): list `POST
 * /api/global/visitTypeMasterList {organizationId, offset,
 * noOfRecordsPerPage}`, count `POST /api/global/visitTypeMasterListCount
 * {organizationId}`, save `POST /api/global/saveVisitType`, edit-get `GET
 * /api/global/getVisitTypeById/visitTypeId/{id}/orgId/{org}`, update `POST
 * /api/global/updateVisitType`, toggle `POST
 * /api/global/updateVisitTypeStatus`.
 */
@Injectable({ providedIn: 'root' })
export class VisitTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl data1 -- POST { organizationId, offset, noOfRecordsPerPage }.
  getVisitTypeList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, VisitTypeRow>> {
    return this.http.post<ApiResponse<never, VisitTypeRow>>(`${this.base}/api/global/visitTypeMasterList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl data2 -- POST { organizationId }, count in `object`.
  getVisitTypeCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/global/visitTypeMasterListCount`, {
      organizationId,
    });
  }

  // ctrl visitTypeObj -- POST { visitTypeCode, visitTypeName, createdBy,
  // updatedBy, createdDate, status:'A', organizationId, updatedDate }.
  saveVisitType(payload: {
    visitTypeCode: string;
    visitTypeName: string;
    status: 'A';
    createdBy: number;
    updatedBy: number;
    organizationId: number;
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveVisitType`, payload);
  }

  // ctrl GETVISITTYPEBYID -- GET
  // /api/global/getVisitTypeById/visitTypeId/{id}/orgId/{orgId}; the
  // response listObject[0] is loaded into the form (generic id/desc/code).
  getVisitTypeById(id: number, orgId: number): Observable<ApiResponse<never, VisitTypeRow>> {
    return this.http.get<ApiResponse<never, VisitTypeRow>>(
      `${this.base}/api/global/getVisitTypeById/visitTypeId/${id}/orgId/${orgId}`,
    );
  }

  // ctrl visitTypebj -- POST { visitTypeCode, visitTypeName, visitTypeId,
  // updatedBy, updatedDate }.
  updateVisitType(payload: {
    visitTypeCode: string;
    visitTypeName: string;
    visitTypeId: number;
    updatedBy: number;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateVisitType`, payload);
  }

  // ctrl visitObj -- POST { visitTypeId, status, updatedDate, updatedBy }.
  updateVisitTypeStatus(payload: VisitTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateVisitTypeStatus`, payload);
  }
}
