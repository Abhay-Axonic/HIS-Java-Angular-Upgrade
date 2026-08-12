import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PatientSearchRow,
  CardTypeRow,
  LoyaltyCardRow,
  AssignPayload,
} from './assign-loyalty-card.models';

/**
 * assign-loyalty-card (752) — the FIRST transaction-loop flow (DQ-037/038,
 * flow map signed off 2026-08-12). All endpoints + payloads read from the
 * literal legacy controller signature (patientLoyaltiCardMapperController.js)
 * per the T0 payload-literalism amendment.
 */
@Injectable({ providedIn: 'root' })
export class AssignLoyaltyCardService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // Step 1 PICKER — POST /adt/patientSearchByMultipleCriteria
  searchPatients(p: {
    uhIdNumber: string; patientName: string; genderId: string; mobileNo: string;
    birthDate: string; identificationNo: string; patientType: string;
    unitId: number; organizationId: number;
  }): Observable<ApiResponse<never, PatientSearchRow>> {
    return this.http.post<ApiResponse<never, PatientSearchRow>>(
      `${this.base}/adt/patientSearchByMultipleCriteria`, p,
    );
  }

  // Step 2 FEED — POST /api/billing/cardTypeMaster with the LITERAL legacy
  // typo payload {orgnisationId, unitId} (byte-for-byte or the feed 500s).
  getCardTypes(orgId: number, unitId: number): Observable<ApiResponse<never, CardTypeRow>> {
    return this.http.post<ApiResponse<never, CardTypeRow>>(
      `${this.base}/api/billing/cardTypeMaster`,
      { orgnisationId: orgId, unitId },
    );
  }

  // Step 3 CHECK — GET /api/unit/checkLoyaltyCard/{orgId}/{unitId}/{cardNo}.
  checkCard(orgId: number, unitId: number, cardNo: string): Observable<ApiResponse<never, never>> {
    return this.http.get<ApiResponse<never, never>>(
      `${this.base}/api/unit/checkLoyaltyCard/${orgId}/${unitId}/${cardNo}`,
    );
  }

  // Step 4 LOOKUP — POST /api/loyaltyCard/getLoyaltiCardDetailsByPatientId
  // (POST, not GET — T0 method finding).
  getExistingCards(patientId: number, orgId: number, unitId: number): Observable<ApiResponse<never, LoyaltyCardRow>> {
    return this.http.post<ApiResponse<never, LoyaltyCardRow>>(
      `${this.base}/api/loyaltyCard/getLoyaltiCardDetailsByPatientId`,
      { patientId, organisationId: orgId, unitId, status: 'A', isDeleted: 'N', isUserDefined: 'Y' },
    );
  }

  // Step 5 WRITE — POST /api/loyaltyCard/patientLoyaltyCard.
  assign(payload: AssignPayload): Observable<ApiResponse<never, never>> {
    return this.http.post<ApiResponse<never, never>>(
      `${this.base}/api/loyaltyCard/patientLoyaltyCard`, payload,
    );
  }
}
