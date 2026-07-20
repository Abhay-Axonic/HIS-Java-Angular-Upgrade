import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { PesapayPaymentStatus } from './pesapal-process-page.models';

@Injectable({ providedIn: 'root' })
export class PesapalProcessPageService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getPesapayPaymentStatus(
    transactionReferenceId: string,
    transactionId: string,
    unitId: number,
    organizationId: number,
    callbackUrl: string,
  ): Observable<ApiResponse<PesapayPaymentStatus>> {
    return this.http.post<ApiResponse<PesapayPaymentStatus>>(`${this.base}/api/global/getPesapayPaymentStatus`, {
      transactionReferenceId,
      transactionId,
      unitId,
      organizationId,
      callbackUrl,
    });
  }
}
