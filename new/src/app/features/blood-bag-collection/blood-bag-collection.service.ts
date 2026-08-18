import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {BagCollectionDetail, Examination, IdNameRow} from './blood-bag-collection.models';

/** Blood Bag Collection form — the legacy call-set (bloodBagCollectionController.js):
 * getBagCollectionDetailById (:113, load), getExaminationListByCollectionId (:156),
 * getCampMasterList (:195, camp dropdown), getActiveBloodBankMaster/{org}/{unit}
 * (:204, bank dropdown), getActiveBloodBagCapacityMaster/{org}/{unit} (:223,
 * capacity dropdown), GET_REASON_MASTER_LIST (:364, termination reason), and
 * saveBloodBagCollection (:311, the save). Every payload carries
 * organizationId + unitId. Dates: dd-M-yyyy HH:mm:ss strings via formatLegacyNow
 * — GlobalCommonDateUtils.getDate NPEs on null; send '' for absent dates. */
@Injectable({providedIn: 'root'})
export class BloodBankService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getDetail(bloodBagCollectionId: number): Observable<ApiResponse<never, BagCollectionDetail>> {
    return this.post('/api/bloodbank/getBagCollectionDetailById', {bloodBagCollectionId});
  }

  getExaminations(bloodBagCollectionId: number): Observable<ApiResponse<never, Examination>> {
    return this.post('/api/bloodbank/getExaminationListByCollectionId', {bloodBagCollectionId});
  }

  camps(): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/global/bloodBank/getCampMasterList', {organizationId: this.org(), unitId: this.unit()});
  }

  bloodBanks(): Observable<ApiResponse<never, IdNameRow>> {
    return this.post(`/bloodBank/getActiveBloodBankMaster/${this.org()}/${this.unit()}`, {});
  }

  capacities(): Observable<ApiResponse<never, IdNameRow>> {
    return this.post(`/global/bloodBank/getActiveBloodBagCapacityMaster/${this.org()}/${this.unit()}`, {});
  }

  reasons(): Observable<ApiResponse<never, IdNameRow>> {
    return this.post('/api/common/getActiveReasonMasterList', {organizationId: this.org(), unitId: this.unit()});
  }

  save(payload: BagCollectionDetail): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/bloodbank/saveBloodBagCollection', payload);
  }

  private org(): number {
    return Number(sessionStorage.getItem('organizationId') ?? 1);
  }

  private unit(): number {
    return Number(sessionStorage.getItem('unitId') ?? 1);
  }
}
