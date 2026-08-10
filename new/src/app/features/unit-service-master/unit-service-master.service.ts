/**
 * unit-service-master — parity rebuild of the legacy
 *   views/master/UnitMaster/billingMaster/unit-service-master.html
 *   (menu node 583 'Unit Service Master', parent 570 Billing -> 531 Unit
 *   Master, controller UnitServiceMasterController — the state binds
 *   UnitServiceMasterController at app.js:9499, NOT MasterController: this
 *   is a REAL screen, verified by the DECOY test).
 *
 * SCREEN 27 — LIVE class (the next confirmed Billing candidate after 580's
 * decoy correction; the owner's pick between 583 and 585 — 583 is the
 * MASTER, 585 its price detail).
 *
 * BROKEN LIST — a real legacy defect on the Billing read path (the walk's
 * masked-error class, screen-17 shape): GET /api/global/UnitServiceMaster-
 * Details/org/1/unit/1/0/20 -> HTTP 200 body {"status":"error","code":"501",
 * "message":"Something went wrong!!"} EVERY call. The count works (object: 1
 * — a row exists), so the list query itself fails. ROOT CAUSE (verified live,
 * the server stack): IllegalArgumentException at AliasToBean transform —
 * UnitServiceMasterDetailsDto.isOutsourceJob is `char` (DTO:17, and
 * isOutsource DTO:15 is the same) while the SQL column is_outsource_job
 * returns a String ('N'); Hibernate cannot set String->char; the DAO's
 * catch-and-return (UnitServiceMasterDetailsDaoImpl:101-106) SWALLOWS the
 * exception and returns the ERROR envelope with HTTP 200. The masked-error
 * shape: the caller sees HTTP 200 and must read the body status.
 *
 * NOT FIXED (Rule 10) — a backend BR candidate (the char-field DTO type vs
 * SQL column). The rebuild CALLS the list, detects the error envelope, and
 * surfaces the defect honestly (the screen-17/24/25 pattern) — the screen
 * cannot be green while the list 501s.
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
export class UnitServiceMasterService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/api/global/UnitServiceMasterDetails/org/${orgId}/unit/${unitId}/0/20`
    );
  }

  count(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.get<ApiEnvelope>(
      `${this.base}/api/global/UnitServiceMasterDetails/count/org/${orgId}/unit/${unitId}`
    );
  }
}
