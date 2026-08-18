import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {DonorScreeningDetail, ScreeningQuestion} from './donor-screening.models';

/** Donor Screening form — the legacy call-set (donorScreeningWorklistDetailsController.js):
 * getDonorScreeningDetailByDonorId/{donorId}/{organizationId}/{unitId} — GET with
 * three path-vars (DonorScreeningDetailController.java:51) — the donor's
 * existing screening detail; saveDonorScreeningDetail (POST, :29) — the save
 * (payload requires donorScreeningMapperList — the questionnaire answers);
 * getDonorScreeningListByGenderId (POST) — the question master per gender.
 * DATE CONVENTION: String dates dd-M-yyyy HH:mm:ss (numeric month — the DAO's
 * GlobalCommonDateUtils literal at DonorScreeningDetailDaoImpl.java:85). */
@Injectable({providedIn: 'root'})
export class DonorScreeningService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getDetail(donorId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, DonorScreeningDetail>> {
    return this.http.get<ApiResponse<never, DonorScreeningDetail>>(
      `${this.base}/api/bloodbank/getDonorScreeningDetailByDonorId/${donorId}/${organizationId}/${unitId}`,
    );
  }

  save(payload: DonorScreeningDetail): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/bloodbank/saveDonorScreeningDetail', payload);
  }

  questionsByGender(genderId: number): Observable<ApiResponse<never, ScreeningQuestion>> {
    return this.post('/global/bloodBank/getDonorScreeningListByGenderId', {genderId});
  }
}
