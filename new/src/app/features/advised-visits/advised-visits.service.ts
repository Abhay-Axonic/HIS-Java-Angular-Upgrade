import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { AdvisedVisitRow, DoctorAutoFillOption } from './advised-visits.models';

/**
 * Endpoints confirmed against the real Java backend (full chain,
 * Controller -> Service -> DAO, all present):
 *  - MedicationController.java, @RequestMapping("api/opd") ->
 *    /getAdvisedDateAppointmentList (POST), /getAdvisedDateAppointmentListCount (POST)
 *  - DoctorMasterController.java, @RequestMapping("api/global") ->
 *    /getDoctorMasterListAutoFillSerach (GET, unit_id/organization_id as
 *    HEADERS, doctor_name as a query param -- NOT the usual body-based
 *    convention most other endpoints use, matching legacy's own use of
 *    the special `GenericServiceParamHeader` service variant for this
 *    one call).
 *
 * NOTE on the count endpoint (confirmed by reading
 * AdvisedDateAppointmentDaoImpl.getAdvisedDateAppointmentListCount): it
 * filters ONLY by organizationId/unitId -- it does NOT accept
 * fromDate/toDate/doctorId at all, even though the list endpoint does.
 * This is a genuine, faithfully-reproduced legacy quirk (not a rebuild
 * bug): the pagination total always reflects ALL advised visits for the
 * unit, not the currently-filtered subset. See findings entry.
 */
@Injectable({ providedIn: 'root' })
export class AdvisedVisitsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  search(payload: Record<string, unknown>): Observable<ApiResponse<never, AdvisedVisitRow>> {
    return this.http.post<ApiResponse<never, AdvisedVisitRow>>(`${this.base}/api/opd/getAdvisedDateAppointmentList`, payload);
  }

  count(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/opd/getAdvisedDateAppointmentListCount`, {
      organizationId,
      unitId,
    });
  }

  searchDoctors(term: string, unitId: number, organizationId: number): Observable<ApiResponse<never, DoctorAutoFillOption>> {
    const params = new HttpParams().set('doctor_name', term ?? '');
    return this.http.get<ApiResponse<never, DoctorAutoFillOption>>(`${this.base}/api/global/getDoctorMasterListAutoFillSerach`, {
      params,
      headers: { unit_id: String(unitId), organization_id: String(organizationId) },
    });
  }
}
