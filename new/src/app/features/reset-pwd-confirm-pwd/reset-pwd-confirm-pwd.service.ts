import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ResetPasswordRequest } from './reset-pwd-confirm-pwd.models';

/**
 * Single endpoint, taken directly from
 * resetPwdconfirmPwdController.js:113-122 (`GenericService.serviceAction("POST",
 * BASE_URL + ROOT_URL + "/adt/resetPassword", data)`) -- the only backend call
 * this screen makes. `authInterceptor` (new/src/app/core/auth.interceptor.ts)
 * is already wired globally in app.config.ts, so the Authorization/userId/
 * unitId headers legacy's own GenericService attaches are added automatically;
 * nothing to add here.
 */
@Injectable({ providedIn: 'root' })
export class ResetPwdConfirmPwdService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  resetPassword(request: ResetPasswordRequest): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/resetPassword`, request);
  }
}
