import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  AcceptTransferPayload,
  AdmittedPatientRow,
  BedRow,
  RejectTransferPayload,
  SaveTransferRequestPayload,
  TransferRow,
  WardRow,
} from './transfer.models';

/**
 * Transfer screen (65) service — direct transfer chain.
 * Legacy anchors (DQ-045/046): directTransferController.js;
 * adt.url.config.js:459 ADDTRANSFERREQUEST, :482 ACCEPTTRANSFERREQUESTFORADT,
 * :490 ACCEPTFINALTRANSFERREQUEST, :483 REJECTTRANSFERREQUESTFORADT,
 * :504 GETADMITTEDPATIENTBYKEWWORD.
 * Write chain: W1 saveTransferRequest (3 entities) -> W2 accept (5 entities,
 * ATOMIC per DQ-047) -> W3 acceptFinalTransferRequest (5 entities); reject
 * path parallel to W2. Status machine: 1 Pending -> 2 Requested -> 6 Ready
 * For Transfer -> 9 Transferd; 5 Rejected (BR-036: UI convention only).
 */
@Injectable({ providedIn: 'root' })
export class TransferService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  /** Legacy: GETADMITTEDPATIENTBYKEWWORD — POST /adt/searchAdmittedPatientByNameAndUhid */
  searchAdmittedPatients(term: string, organizationId: number, unitId: number): Observable<unknown> {
    return this.http.post(
      `${this.base}/adt/searchAdmittedPatientByNameAndUhid`,
      { keyword: term, organizationId, unitId },
    );
  }

  /** Legacy: universalSearchForTransferRequestList — pending transfer rows */
  getPendingTransfers(organizationId: number, unitId: number): Observable<unknown> {
    return this.http.post(
      `${this.base}/adt/universalSearchForTransferRequestList`,
      { organizationId, unitId },
    );
  }

  /** Legacy: GETBEDLISTBYWARDIDFORBEDTOBED — beds for a ward */
  getBedsByWard(wardId: number, organizationId: number, unitId: number): Observable<unknown> {
    return this.http.get(
      `${this.base}/adt/getBedListByWardIdForBedtoBedTransfer/${wardId}/${organizationId}/${unitId}`,
    );
  }

  /** Legacy: getWardForDirectTransfer — wards available for the patient */
  getWardsForDirectTransfer(genderId: number, visitTypeId: number, unitId: number, organizationId: number): Observable<unknown> {
    return this.http.get(
      `${this.base}/api/adt/getWardForDirectTransfer/${genderId}/${visitTypeId}/${unitId}/${organizationId}`,
    );
  }

  /** W1 — POST /adt/saveTransferRequest (3-entity write: request + transfer + admission) */
  saveTransferRequest(payload: SaveTransferRequestPayload): Observable<unknown> {
    return this.http.post(`${this.base}/adt/saveTransferRequest`, payload);
  }

  /** W2 — POST /adt/acceptTransferRequestForADT (5-entity write, ATOMIC) */
  acceptTransferRequest(payload: AcceptTransferPayload): Observable<unknown> {
    return this.http.post(`${this.base}/adt/acceptTransferRequestForADT`, payload);
  }

  /** W3 — POST /adt/acceptFinalTransferRequest (5-entity write, executes the move) */
  acceptFinalTransferRequest(payload: AcceptTransferPayload): Observable<unknown> {
    return this.http.post(`${this.base}/adt/acceptFinalTransferRequest`, payload);
  }

  /** Reject — POST /adt/rejectTransferRequestForADT */
  rejectTransferRequest(payload: RejectTransferPayload): Observable<unknown> {
    return this.http.post(`${this.base}/adt/rejectTransferRequestForADT`, payload);
  }
}

export type { AcceptTransferPayload, AdmittedPatientRow, BedRow, RejectTransferPayload, SaveTransferRequestPayload, TransferRow, WardRow };
