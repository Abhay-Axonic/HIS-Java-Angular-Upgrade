import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CabinOption,
  DoctorCabinMapperByIdItem,
  DoctorCabinMapperRow,
  DoctorCabinMapperSavePayload,
  DetailedCabinItem,
  DoctorOption,
} from './doctor-cabin-mapper.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/global.url.config.js:185-193 (the
 * ADD/UPDATE/UPDATE_STATUS/GET_BY_ID/GET_LIST/GET_COUNT/GET_LIST_OF_DOCTOR/
 * GET_DETAILED_CABIN_LIST constants) and
 * resources/assets/js/controller/master/global/doctorCabinMapperController.js's
 * `searchGroupList()` (ctrl:79-104, the one call that hits a different
 * controller -- CabinMaster's `getActiveCabinMaster`, not
 * DoctorCabinMapperController) -- not guessed. `S = "/"` is the path-segment
 * separator legacy concatenates by hand; `BASE_URL`/`ROOT_URL` are both empty
 * strings in this deployment (see API_BASE_URL's doc comment).
 */
@Injectable({ providedIn: 'root' })
export class DoctorCabinMapperService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** searchGroupList() ctrl:79-104 -- left-hand cabin checkbox list. */
  getCabinList(organisationId: number, unitId: number): Observable<ApiResponse<never, CabinOption>> {
    return this.http.get<ApiResponse<never, CabinOption>>(
      `${this.base}/api/global/unit/getActiveCabinMaster/${organisationId}/${unitId}`,
    );
  }

  /** getDoctorList() ctrl:132-149 -- "User" <select> options (GET_LIST_OF_DOCTOR). */
  getDoctorList(organisationId: number, unitId: number): Observable<ApiResponse<never, DoctorOption>> {
    return this.http.get<ApiResponse<never, DoctorOption>>(
      `${this.base}/api/global/getDoctorList/${organisationId}/${unitId}`,
    );
  }

  /** getDoctorCabinMapperByDoctorId() ctrl:153-172 -- the info-icon "Details" modal (GET_DETAILED_CABIN_LIST). */
  getDetailedCabinList(
    doctorId: number,
    organisationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, DetailedCabinItem>> {
    return this.http.get<ApiResponse<never, DetailedCabinItem>>(
      `${this.base}/api/global/getDetailedCabinList/${doctorId}/${organisationId}/${unitId}`,
    );
  }

  /** initMasterList()/masterList() ctrl:181-239 -- the bottom table (GET_DOCTOR_CABIN_MAPPER_LIST, POST). */
  getMapperList(
    organisationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DoctorCabinMapperRow>> {
    return this.http.post<ApiResponse<never, DoctorCabinMapperRow>>(`${this.base}/api/global/getDoctorCabinMapperlist`, {
      organisationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  /** initMasterList() ctrl:208-215 -- GET_DOCTOR_CABIN_MAPPER_COUNT. */
  getMapperCount(organisationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(
      `${this.base}/api/global/getDoctorCabinMapperCount/${organisationId}/${unitId}`,
    );
  }

  /** showUpdateBtn() ctrl:345-388 -- edit-populate (GET_DOCTOR_CABIN_MAPPER_BY_ID). */
  getById(
    doctorId: number,
    organisationId: number,
    unitId: number,
  ): Observable<ApiResponse<never, DoctorCabinMapperByIdItem>> {
    return this.http.get<ApiResponse<never, DoctorCabinMapperByIdItem>>(
      `${this.base}/api/global/getDoctorCabinMapperById/${doctorId}/${organisationId}/${unitId}`,
    );
  }

  /** save() ctrl:261-342 -- ADD_DOCTOR_CABIN_MAPPER (POST). */
  save(payload: DoctorCabinMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/saveDoctorCabinMapper`, payload);
  }

  /** update() ctrl:390-449 -- UPDATE_DOCTOR_CABIN_MAPPER (POST). */
  update(payload: DoctorCabinMapperSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/global/updateDoctorCabinMapper`, payload);
  }

  /** updateStatus() ctrl:451-485 -- UPDATE_DOCTOR_CABIN_MAPPER_STATUS (GET, path-encoded). */
  updateStatus(doctorId: number, organisationId: number, unitId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/api/global/updateDoctorCabinMapperStatus/${doctorId}/${organisationId}/${unitId}/${status}`,
    );
  }
}
