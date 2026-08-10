/**
 * global-doctor-share — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/doctor-share-master.html
 *   (menu node 613 'Global Doctor Share', parent 570 Billing -> 531 Unit
 *   Master, controller globalDoctorShareMasterController loaded
 *   index.html:881).
 *
 * SCREEN 32 — LIVE class. State app.js:14764 binds
 * globalDoctorShareMasterController (NOT MasterController — passes the DECOY
 * test). Backend: GlobalDoctorShareMasterController (billing/unit,
 * @RequestMapping unit/DoctorShare) — found BY CLASS NAME this time:
 * saveGlobalDoctorShare, duplicate/check/globalDoctorShare (dup-check),
 * saveIndividualDoctorShare, getGlobalDoctorShareDepartmentMasterList (the
 * grid feed), getDoctorGradeMaster, DetailsByDepartmentId (+ Search).
 *
 * LIVE-VERIFIED BY CALLING (body status): the grid feed -> 200 success
 * listObject [] object: 0 (dev-empty); the grade feed -> 200 success [].
 * PAYLOAD CONTRACT (named divergence): the DTO's search field is
 * `specialitySerarchName` (the typo, DoctorShareReqDto:21) — REQUIRED;
 * omitting it 400s HIS-400 (the probe-payload class).
 *
 * SCOPED BACKEND CHECK (billing write-path — the owner's doctor-share
 * priority): FALSE-SUCCESS ABSENT — saveGlobalDoctorShareGroupWiseDao's
 * catch (TGlobalDoctorShareGroupWiseDaoImpl:105-107) returns the honest
 * ERROR envelope (never SUCCESS-from-catch); SUCCESS (:102) only after the
 * batch executed + committed; ConstraintViolation -> ALREADY_EXIST (a legit
 * dup signal). FINDING — BATCH-PARTIAL-COMMIT SHAPE: the batch commits
 * every 100 rows or on the last; a mid-batch failure after a commit leaves
 * earlier rows persisted with NO rollback in the catch (the finally only
 * closes). On a money path (doctor-share rates) this is a partial-write
 * risk; NOT FIXED (Rule 10) — BR candidate. The service guards the DAO
 * call with size > 0, so the DAO's empty-list SUCCESS is unreachable from
 * this endpoint (dead code). The dup-check is composite-scoped
 * (dept/bed-category/visit/patient/payment/tariff/effective-date). The
 * getDate chain (getStringDate -> getDate) is the latent null-date class.
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
export class GlobalDoctorShareService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    // specialitySerarchName (the DTO's typo'd field, DoctorShareReqDto:21)
    // is REQUIRED — omitting it 400s HIS-400
    return this.http.post<ApiEnvelope>(
      `${this.base}/unit/DoctorShare/getGlobalDoctorShareDepartmentMasterList`,
      { orgId, unitId, noOfRecordsPerPage: 100, offset: 0, departmentId: null, specialitySerarchName: null }
    );
  }

  grades(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/unit/DoctorShare/getDoctorGradeMaster/${orgId}/${unitId}`
    );
  }
}
