import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {API_BASE_URL} from '../core/api-config';
import type {ApiResponse} from '../core/api-response';
import type {IdNameRow} from '../features/blood-bag-collection/blood-bag-collection.models';

/**
 * Shared dropdown-feed layer — the 11-feed cluster from the endpoint inventory
 * (GETACTIVEREASONLIST ×20 screens, GETACTIVEIDENTIFICATION ×18, GETACTIVEGENDERLIST ×15,
 * GETACTIVEBEDCATEGORYLISTBYUNIT ×10, GETACTIVEPREFIXLIST ×6, GETACTIVESPECIALITY ×6,
 * GETPATIENTDIAGNOSISBYPATIENTID ×5, GETACTIVEFLOORLIST ×5, GETADMITTEDPATIENTBYKEWWORD ×4).
 *
 * Every feed is backend-quoted (Phase 1, 2026-08-20):
 *   - reasons:        POST /api/common/getActiveReasonMasterList (ReasonMasterController,
 *                     @RequestBody ReasonMasterDto) -> {reasonId, reasonDesc} (ReasonMasterDto.java)
 *   - identifications: POST /adt/getActiveIdentificationList -> {identificationId, identificationName}
 *                     (IdentificationMaster.java)
 *   - genders:        POST /adt/getActiveGenderList -> {id, desc} (GenderMaster.java aliases)
 *   - bedCategories:  POST /adt/getBedCategoryListByOrgUnit (BedAllocationController,
 *                     @RequestBody BedAllocationDto) -> {bedCategoryId, bedCategoryDesc}
 *   - prefixes:       POST /adt/getActivePrefixList -> {prefixId, prefixCode, prefixDesc} (PrefixMaster.java)
 *   - specialities:   POST /adt/getActiveSpecialityList -> {specialityId, specialityName} (SpecialityMaster.java)
 *   - diagnoses:      POST /api/opd/getPatientDiagnosisByPatientId (PatientDiagnosisDaoImpl) ->
 *                     {diagnosisId, diagnosisName, code, isComorbidity, isResolved, isNotifiable,
 *                     resolvedDate, patientId, remark}
 *   - floors:         POST /adt/getActiveFloorList (BedAllocationController, @RequestBody BedMasterDto)
 *                     -> {floorId, floorName}
 *   - admittedSearch: POST /adt/searchAdmittedPatientByNameAndUhid -> the BedManagementDto-family rows
 *                     (uhidNumber/patientName/patientId)
 *
 * EXCLUDED in Phase 1 (shape not quotable within budget — criteria-mapped, transformer target
 * unread; do not bind on assumption):
 *   - GET_PATIENT_DOCUMENT_ENCOUNTER (/api/opd/patientDoc/details)
 *   - GETDOCTORBYSPECIALITYID (/adt/getDoctorListAganistSpeciality)
 *
 * org/unit convention: every feed takes orgId/unitId as parameters and sends them in the
 * request body — the caller sources them from the session. Nothing is hardcoded (the two
 * blood-bank /1/1 feeds were explicitly NOT replicated).
 * The module-specific field names are mapped to {id, desc} via the toIdName pattern
 * (blood-bag-collection / donor-registration), so templates can bind .desc uniformly.
 */
@Injectable({providedIn: 'root'})
export class SharedDropdownsService {
  private readonly http = inject(HttpClient);

  /** POST /api/common/getActiveReasonMasterList -> {reasonId, reasonDesc} */
  activeReasons(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{reasonId: number; reasonDesc: string}>(
      '/api/common/getActiveReasonMasterList', {organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.reasonId, desc: r.reasonDesc}))));
  }

  /** POST /adt/getActiveIdentificationList -> {identificationId, identificationName} */
  activeIdentifications(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{identificationId: number; identificationName: string}>(
      '/adt/getActiveIdentificationList', {organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.identificationId, desc: r.identificationName}))));
  }

  /** POST /adt/getActiveGenderList -> {id, desc} (already the uniform shape) */
  activeGenders(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{id: number; desc: string}>('/adt/getActiveGenderList', {organizationId: orgId, unitId});
  }

  /** POST /adt/getBedCategoryListByOrgUnit -> {bedCategoryId, bedCategoryDesc} */
  bedCategories(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{bedCategoryId: number; bedCategoryDesc: string}>(
      '/adt/getBedCategoryListByOrgUnit', {organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.bedCategoryId, desc: r.bedCategoryDesc}))));
  }

  /** POST /adt/getActivePrefixList -> {prefixId, prefixCode, prefixDesc} */
  prefixes(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{prefixId: number; prefixDesc: string}>(
      '/adt/getActivePrefixList', {organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.prefixId, desc: r.prefixDesc}))));
  }

  /** POST /adt/getActiveSpecialityList -> {specialityId, specialityName} */
  specialities(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{specialityId: number; specialityName: string}>(
      '/adt/getActiveSpecialityList', {organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.specialityId, desc: r.specialityName}))));
  }

  /** POST /api/opd/getPatientDiagnosisByPatientId -> {diagnosisId, diagnosisName, ...} */
  diagnoses(patientId: number, orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{diagnosisId: number; diagnosisName: string}>(
      '/api/opd/getPatientDiagnosisByPatientId', {patientId, organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.diagnosisId, desc: r.diagnosisName}))));
  }

  /** POST /adt/getActiveFloorList -> {floorId, floorName} */
  floors(orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{floorId: number; floorName: string}>(
      '/adt/getActiveFloorList', {organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.floorId, desc: r.floorName}))));
  }

  /** POST /adt/searchAdmittedPatientByNameAndUhid -> the BedManagementDto-family rows */
  searchAdmittedPatients(keyword: string, orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{patientId: number; patientName: string; uhidNumber: string}>(
      '/adt/searchAdmittedPatientByNameAndUhid', {keyword, organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.patientId, desc: `${r.patientName} (${r.uhidNumber})`}))));
  }

  private post<T>(path: string, body: unknown): Observable<T[]> {
    return this.http.post<ApiResponse<T[]>>(API_BASE_URL + path, body)
      .pipe(map((res) => (res?.listObject ?? []) as T[]));
  }
}
