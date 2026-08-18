import {HttpClient} from '@angular/common/http';
import {inject, Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {API_BASE_URL} from '../../core/api-config';
import type {ApiResponse} from '../../core/api-response';
import type {CollectionRow} from './blood-bag-collection-worklist.models';

/** Blood Bag Collection Worklist — the legacy call-set is a single grid/search
 * endpoint (bloodBagCollectionWorklistController.js:85); the row action
 * navigates to the bloodBagCollection form (the save's owner, :121). The
 * worklist has NO save of its own. DATE CONVENTION: String dates via
 * formatLegacyNow — the registration/prescription convention. */
@Injectable({providedIn: 'root'})
export class BloodBankService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  search(criteria: Record<string, unknown>): Observable<ApiResponse<never, CollectionRow>> {
    return this.post('/api/bloodbank/getDonorAndScreeningAndBagCollectionDetail', criteria);
  }
}
