/**
 * doctor-referral-share — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/doctor-referrel-share-master.html
 *   (menu node 615 'Doctor Referral Share', parent 570 Billing -> 531 Unit
 *   Master, controller doctorReferralShareMasterController loaded
 *   index.html:791).
 *
 * SCREEN 34 — LIVE class. State app.js:14858 `doctorReferralShareMaster`
 * (C5's DEAD flag is the name mismatch — the node's link target; the
 * screen-31 pattern). Controller binds doctorReferralShareMasterController
 * (NOT MasterController — passes the DECOY test).
 *
 * BACKEND BY ENDPOINT TRUTH: the grid feed is
 * /unit/DoctorShare/getIndividualDoctorReferralDetailsByDepartmentId
 * (GlobalDoctorShareMasterController:229 — the 613-family controller); the
 * save is saveReferralShare (:215, the owner's enumeration). The details
 * feed (getDoctorReferralShareDetailsByRefDocId) is COMMENTED OUT in the
 * legacy JS (the legacy navigates to detailsDoctorReferralShareMaster
 * instead).
 *
 * LIVE-VERIFIED BY CALLING (body status): the grid feed -> 200 success
 * listObject [] object: 0 (dev-empty). PAYLOAD CONTRACT: referralId
 * (not departmentId — the 400 probe-payload class).
 *
 * FALSE-SUCCESS FINDING (2026-08-10, ON THE REFERRAL-SHARE MONEY PATH):
 * IndividualDoctorShareServiceImpl.saveReferralShare (line 216) returns
 * SUCCESS + DOCTOR_SHARE ("saved") when BOTH detail lists are empty/null —
 * nothing is written yet the response is success (the screen-22 same-day
 * shape on the money path). The dup-checks throw ServiceException (clean
 * rethrow); the two DAO saves (GroupWise + ServiceWise ForReferral) follow
 * the clean catch->honest-ERROR pattern with the SAME latent
 * batch-partial-commit shape as 613 (commits per batch, no rollback in the
 * catch). NOT FIXED (Rule 10) — BR candidates.
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
export class DoctorReferralShareService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // referralId (not departmentId) — the IndividualDoctorShareReqDto
    // contract for this feed
    return this.http.post<ApiEnvelope>(
      `${this.base}/unit/DoctorShare/getIndividualDoctorReferralDetailsByDepartmentId`,
      { orgId, unitId, referralId: 1 }
    );
  }
}
