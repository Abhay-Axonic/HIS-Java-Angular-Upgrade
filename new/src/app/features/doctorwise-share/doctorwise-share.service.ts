/**
 * doctorwise-share — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/doctorwise-share-master.html
 *   (menu node 614 'Doctorwise Share', parent 570 Billing -> 531 Unit
 *   Master, controller doctorWiseShareMasterController loaded
 *   index.html:883).
 *
 * SCREEN 33 — LIVE class. State app.js:14814 `doctorWiseShare` (mixed-case
 * — C5's DEAD flag is the name-case mismatch, the screen-31 pattern).
 * Controller binds doctorWiseShareMasterController (NOT MasterController —
 * passes the DECOY test).
 *
 * BACKEND BY ENDPOINT TRUTH: the grid feed is
 * /unit/DoctorShare/getDoctorShareDepartmentMasterList
 * (GlobalDoctorShareMasterController:159, the 613-family controller). The
 * save family is TDoctorShareController (api/billing): saveDoctorShareList,
 * saveDoctorRefeeralList (the owner's six-method enumeration).
 *
 * LIVE-VERIFIED BY CALLING (body status): the grid feed -> 200 success
 * listObject [] object: 0 (dev-empty). PAYLOAD CONTRACT: specialityId +
 * doctorId (nulls fine); the earlier 401 was a payload-shape artifact
 * (this DTO rejects the offset/recordPerPage keys the sibling DTO takes).
 *
 * FALSE-SUCCESS FINDING (2026-08-10, the walk's most serious class — ON THE
 * DOCTOR-SHARE MONEY PATH, the family the owner's warning named):
 * TDoctorShareDaoImpl.saveDoctorShareList (lines 680-726):
 *  - line 724: when the voucher save returns non-SUCCESS (or its object is
 *    null), the doctor-share rows were ALREADY updated (the executeUpdate
 *    loop at 684-701 ran) but the voucher was NOT saved — the response is
 *    STILL SUCCESS with an ERROR_MESSAGE string. HTTP 200 + body status
 *    success while the voucher write failed and the share amounts changed.
 *  - line 726: the empty-list path — SUCCESS "saved" with NOTHING written.
 * The legacy caller checks response.data.status == "success" — the UI shows
 * success for a failed voucher write. NOT FIXED (Rule 10) — BR candidate
 * (the BR-188 family).
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class DoctorwiseShareService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // specialityId + doctorId (nulls fine) — the IndividualDoctorShareReqDto
    // contract; the sibling DTO's offset/recordPerPage keys 401 here
    return this.http.post<ApiEnvelope>(
      `${this.base}/unit/DoctorShare/getDoctorShareDepartmentMasterList`,
      { orgId, unitId, specialityId: null, doctorId: null }
    );
  }
}
