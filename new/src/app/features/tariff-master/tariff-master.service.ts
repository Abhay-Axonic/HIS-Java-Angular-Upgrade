import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PatientTypeMapperDetail,
  RawPatientTypeOption,
  RawVisitTypeOption,
  TariffDetail,
  TariffRow,
  TariffSavePayload,
  TariffStatusPayload,
  TariffUpdatePayload,
  VisitTypeMapperDetail,
} from './tariff-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/controller/billing/unit/tariffMasterController.js`
 * and `config/billing.url.config.js`. All `/api/unit/*` calls back
 * `TariffMasterController.java` (`@RequestMapping("api/unit")`).
 *
 * Deliberately NOT modeled: `GETACTIVEPAYMENTENTITLEMENT`
 * (`/adt/getAvtivePaymentEntitlementList`) and
 * `GETACTIVEBILLINGBEDCATEGORYLIST` (`/adt/getActiveBillingBedCategoryList`)
 * -- both fire on every real page load in legacy (confirmed live), but back
 * a Payment Entitlement multiselect and a Billing Bed Category multiselect
 * that are BOTH fully commented out in `tariff-master.html`'s "Add Tariff
 * Properties" modal (view:364-392/471-509) -- there is no live control
 * anywhere that reads either list. Same precedent as this codebase's
 * `instructionMaster` (dead network calls with zero UI effect are not
 * reproduced). Similarly, `showMapperDetails`'s own
 * `getPaymentEntitlementListByTariffid`/`getBillingBedCategoryListByTariffId`
 * calls are not modeled here -- their results back the same two commented-out
 * table halves in the details popup (view:256-278/323-344).
 */
@Injectable({ providedIn: 'root' })
export class TariffMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:214-258 (initTariffList) -- real server-side offset/count pagination.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, TariffRow>> {
    return this.http.post<ApiResponse<never, TariffRow>>(`${this.base}/api/unit/getTariffMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/api/unit/tariffMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:118-136 -- GET, active Visit Type master data for the "Tariff
  // Properties" multiselect.
  getActiveVisitTypeList(unitId: number, organizationId: number): Observable<ApiResponse<never, RawVisitTypeOption>> {
    return this.http.get<ApiResponse<never, RawVisitTypeOption>>(
      `${this.base}/api/global/visitTypeMaster/unit/${unitId}/org/${organizationId}`,
    );
  }

  // ctrl:140-158 -- GET, active Patient Type master data.
  getActivePatientTypeList(organizationId: number): Observable<ApiResponse<never, RawPatientTypeOption>> {
    return this.http.get<ApiResponse<never, RawPatientTypeOption>>(`${this.base}/global/patientcategory/${organizationId}`);
  }

  // ctrl:315-390 (saveTariff) -- real create. Confirmed live
  // (docs/DECISION_NEEDED_TariffMasterLetterHeadImageColumnMissing.md) that
  // this currently 500s on every attempt against real legacy today, due to
  // an entity/schema drift wholly server-side -- not fixable from this
  // rebuild, faithfully reproduced by simply calling the real endpoint.
  save(payload: TariffSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/saveTariffMaster`, payload);
  }

  // ctrl:454-514 (showUpdateBtn) -- POST { tariffId }, response.data.object,
  // single record, NOT array-wrapped.
  getById(tariffId: number): Observable<ApiResponse<TariffDetail>> {
    return this.http.post<ApiResponse<TariffDetail>>(`${this.base}/api/unit/getTariffListById`, { tariffId });
  }

  // ctrl:519-592 (updateTariff) -- real update, no confirm modal in legacy.
  // Same live-confirmed 500-on-every-attempt gap as save() -- see the
  // decision doc referenced above.
  update(payload: TariffUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/updateTariffMaster`, payload);
  }

  // ctrl:594-633 (updateStatus) -- real status toggle. Same live-confirmed
  // 500-on-every-attempt gap as save()/update() -- see the decision doc.
  updateStatus(payload: TariffStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/updateTariffMasterStatus`, payload);
  }

  // ctrl:392-424 (showMapperDetails, Visit Type half only -- see class doc
  // on TariffMasterService above for why Payment Entitlement/Billing Bed
  // are not modeled).
  getVisitTypeMapperDetails(tariffId: number): Observable<ApiResponse<never, VisitTypeMapperDetail>> {
    return this.http.post<ApiResponse<never, VisitTypeMapperDetail>>(`${this.base}/api/unit/getVisitTypeListByTariffId`, {
      tariffId,
    });
  }

  // ctrl:425-437 (showMapperDetails, Patient Type half).
  getPatientTypeMapperDetails(tariffId: number): Observable<ApiResponse<never, PatientTypeMapperDetail>> {
    return this.http.post<ApiResponse<never, PatientTypeMapperDetail>>(`${this.base}/api/unit/getPatientTypeListByTariffId`, {
      tariffId,
    });
  }

  // ctrl:667-697 (updateStatusVisitMapper) -- individual mapper row toggle.
  updateVisitTypeMapperStatus(tariffVisitTypeMapperId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/updateStatusForTariffVisitTypeMapper`, {
      tariffVisitTypeMapperId,
      status,
      updatedBy,
      updatedDate,
    });
  }

  // ctrl:699-729 (updateStatusPatientMapper) -- individual mapper row toggle.
  updatePatientTypeMapperStatus(tariffPatientTypeMapperId: number, status: 'A' | 'I', updatedBy: number, updatedDate: string): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/unit/updateStatusForTariffPatientTypeMapper`, {
      tariffPatientTypeMapperId,
      status,
      updatedBy,
      updatedDate,
    });
  }
}
