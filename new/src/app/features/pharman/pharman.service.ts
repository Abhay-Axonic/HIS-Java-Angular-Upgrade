import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PharmanDrugSuggestion, PharmanDrugForm, PharmanPopupInfo } from './pharman.models';

/**
 * Legacy routes these 3 calls through BillingGenericService, not
 * GenericService (pharmanController.js:36-129). Deliberately NOT
 * reproducing docs/ANGULAR_PENDING_FINDINGS.md entry 38 (BillingGenericService's
 * broken error handler, which silently swallows all failures app-wide) --
 * this rebuild uses the standard HttpClient, which already goes through
 * the shared, correctly-working authInterceptor (real 401 handling,
 * verified for pesapalProcessPage and viewAssetScheduler). That
 * interceptor is global, approved infrastructure, not a per-screen
 * choice -- reproducing entry 38's bug here would mean deliberately
 * breaking already-working shared code for one screen, not a faithful
 * "parity" port.
 */
@Injectable({ providedIn: 'root' })
export class PharmanService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  searchPharmanDrugList(searchKeyword: string): Observable<ApiResponse<never, PharmanDrugSuggestion>> {
    return this.http.post<ApiResponse<never, PharmanDrugSuggestion>>(`${this.base}/api/global/getPharmanDrugList`, {
      searchKeyword,
    });
  }

  getPharmanDrugFormByName(medicine: string): Observable<ApiResponse<never, PharmanDrugForm>> {
    return this.http.post<ApiResponse<never, PharmanDrugForm>>(`${this.base}/api/global/getPharmanDrugFormByName`, {
      medicine,
    });
  }

  getPopUpData(medicine: string, medicinalforms: string): Observable<ApiResponse<never, PharmanPopupInfo>> {
    return this.http.post<ApiResponse<never, PharmanPopupInfo>>(`${this.base}/api/global/getPopUpData`, {
      medicine,
      medicinalforms,
    });
  }
}
