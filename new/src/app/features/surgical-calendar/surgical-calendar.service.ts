/**
 * surgical-calendar — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/surgical-calender.html (note the legacy
 *   'calender' spelling throughout — state, templateUrl, controller, and the
 *   backend URL mappings all use it).
 *
 * SCREEN 22 — the second screen selected under the C7 ALREADY-REBUILT gate
 * (owner directive 2026-08-10). CLASS: DANGLE (the 477/478/479 precedent) —
 * state registered (app.js:12670, resolve gate item.id==710), menu node 710
 * 'Surgical Calendar' EXISTS but carries NO actionLink (the menu cannot
 * navigate to it), controller loaded (index.html:1537), REAL backend
 * (SurgicalCalendarMasterController /api/otims/*, 7 endpoints). The rebuild
 * REGISTERS the route (moduleId 710) and RESTORES a menu-advertised screen.
 *
 * LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
 * POST /api/otims/surgicalCalender/list {orgId:1,unitId:1,offset:0,
 * recordPerPage:10,status:"A"} -> HTTP 200 body success listObject []
 * (dev-empty — the list payload uses recordPerPage + status, NOT
 * noOfRecordsPerPage — probe-payload class caught before code);
 * POST /api/otims/surgicalCalender/list/count {orgId:1,unitId:1} -> 200
 * success object 0; POST /adt/getActiveSpecialityList -> 200 success 2 REAL
 * rows; POST /adt/getDoctorListAganistSpeciality -> 200 success 1 real row.
 *
 * SCOPED BACKEND CHECK: phantom PASS (17-column DDL matches the entity,
 * live 200s prove resolution); INNER-JOIN FINDING — GET_SURGICAL_CALENDER_LIST
 * INNER JOINs public.m_day_master + doctor.m_doctor_master +
 * doctor.m_speciality_master (a calendar row with a missing day/doctor/
 * speciality is DROPPED from the list — the walk's fourth list-path
 * instance); dup-check N/A (no name-based existence check — slot generator);
 * org-trust FINDING (module-wide payload-carried class, screen-17 record);
 * swallowed PASS (the save's SUCCESS is the slot-loop's natural end, the
 * outer catch returns ERROR — NOT the false-success shape; the mid-loop
 * business return at ServiceImpl:84 is a true ERROR).
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface SurgicalCalendarRow {
  procedureDate: string;
  speciality: string;
  specialityId: number;
  doctorId: number;
  fromTime: string;
  toTime: string;
  doctorName: string;
  fromDate: string;
  toDate: string;
  day: string;
  dayId: number;
}

export interface SpecialityRow {
  specialityId: number;
  specialityName: string;
}

export interface DoctorRow {
  doctorId: number;
  firstName: string;
  lastName: string;
  specialityId?: number;
}

export interface SlotSavePayload {
  specialityId: number;
  doctorId: number;
  fromDate: string;
  toDate: string;
  organisationId: number;
  unitId: number;
  createdBy?: number;
  updatedBy?: number;
  fromTime?: string;
  toTime?: string;
  dayId?: number;
  status?: string;
  surgicalCalendarDetailsDtoList?: Array<{
    dayId: number;
    fromTime: string | null;
    toTime: string | null;
    specialityId: number;
    doctorId: number;
    organisationId: number;
    unitId: number;
  }>;
}

export interface ApiEnvelope {
  status: string;
  code?: string | null;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class SurgicalCalendarService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number, offset: number, recordPerPage: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/surgicalCalender/list`, {
      orgId, unitId, offset, recordPerPage, status: 'A',
    });
  }

  count(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/surgicalCalender/list/count`, { orgId, unitId });
  }

  specialities(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/adt/getActiveSpecialityList`, { organizationId: orgId, unitId });
  }

  doctorsBySpeciality(specialityId: number, orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/adt/getDoctorListAganistSpeciality`, {
      specialityId, organizationId: orgId, unitId,
    });
  }

  save(payload: SlotSavePayload): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/api/otims/surgicalCalender`, payload);
  }
}
