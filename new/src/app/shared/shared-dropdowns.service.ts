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
 *   - patientDocs:    GET /api/opd/patientDoc/details (EncounterController, class /patientDoc;
 *                     @PathVariable encounterId + @RequestParam admissionId/patientId) ->
 *                     PatientDocDto.class (EncounterMasterDaoImpl transformer target):
 *                     {encounterDocId, docTypeId, documentTypeId, documentName, fileName,
 *                     documentPath, uploadDoccumentDate, analyzedDocDate, remark, videolink,
 *                     bucketFlag, incidentReportingMapperId, nabhReportingMapperId, createdDate}
 *                     — a patient-document LOOKUP, exposed with its real shape, not forced
 *                     into {id, desc}.
 *   - doctorsBySpeciality: POST /adt/getDoctorListAganistSpeciality (AdmissionRequestController,
 *                     @RequestBody DoctorSpecialityMapperDto) ->
 *                     DoctorSpecialityMapperDto.class (AdmissionRequestDaoImpl transformer):
 *                     {doctorId, doctorName, firstName, lastName, specialityId, specialityName,
 *                     subSpecialityId, subSpecialityName, mobileNumber, userId, status...}
 *                     mapped to {id: doctorId, desc: doctorName}.
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

  /** GET /api/opd/patientDoc/details — the patient-document lookup (real PatientDocDto shape) */
  patientDocuments(encounterId: number, admissionId: number, patientId: number): Observable<PatientDocRow[]> {
    return this.http.get<ApiResponse<PatientDocRow[]>>(API_BASE_URL + '/api/opd/patientDoc/details', {
      params: {encounterId: String(encounterId), admissionId: String(admissionId), patientId: String(patientId)},
    }).pipe(map((res) => (res?.listObject ?? []) as PatientDocRow[]));
  }

  /** POST /adt/getDoctorListAganistSpeciality -> {doctorId, doctorName, specialityName...} */
  doctorsBySpeciality(specialityId: number, orgId: number, unitId: number): Observable<IdNameRow[]> {
    return this.post<{doctorId: number; doctorName: string}>(
      '/adt/getDoctorListAganistSpeciality', {specialityId, organizationId: orgId, unitId})
      .pipe(map((rows) => rows.map((r) => ({id: r.doctorId, desc: r.doctorName}))));
  }

  private post<T>(path: string, body: unknown): Observable<T[]> {
    return this.http.post<ApiResponse<T[]>>(API_BASE_URL + path, body)
      .pipe(map((res) => (res?.listObject ?? []) as T[]));
  }
}

/** The patient-document lookup's row (PatientDocDto.class — the transformer target). */
export interface PatientDocRow {
  encounterDocId: number;
  docTypeId?: number;
  documentTypeId?: number;
  documentName?: string;
  fileName?: string;
  documentPath?: string;
  uploadDoccumentDate?: string;
  analyzedDocDate?: string;
  remark?: string;
  videolink?: string;
  bucketFlag?: string;
  incidentReportingMapperId?: number;
  nabhReportingMapperId?: number;
  createdDate?: string;
}
