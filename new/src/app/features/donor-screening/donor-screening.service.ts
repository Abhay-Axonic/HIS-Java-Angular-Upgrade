import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {DonorScreeningDetail, ScreeningQuestion} from './donor-screening.models';

/** Donor Screening form — the legacy call-set (donorScreeningWorklistDetailsController.js):
 * getDonorScreeningDetailByDonorId/ (the donor's existing screening detail),
 * saveDonorScreeningDetail (the save), getDonorScreeningListByGenderId (the
 * screening-question master per gender). DATE CONVENTION: String dates via
 * formatLegacyNow — dd-M-yyyy HH:mm:ss numeric month (the global/ADT dozer
 * family; month names fail the parse). */
@Injectable({providedIn: 'root'})
export class DonorScreeningService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getDetail(donorId: number): Observable<ApiResponse<never, DonorScreeningDetail>> {
    return this.post(`/api/bloodbank/getDonorScreeningDetailByDonorId/${donorId}`, {});
  }

  save(payload: DonorScreeningDetail): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/bloodbank/saveDonorScreeningDetail', payload);
  }

  questionsByGender(genderId: number): Observable<ApiResponse<never, ScreeningQuestion>> {
    return this.post('/global/bloodBank/getDonorScreeningListByGenderId', {genderId});
  }
}
