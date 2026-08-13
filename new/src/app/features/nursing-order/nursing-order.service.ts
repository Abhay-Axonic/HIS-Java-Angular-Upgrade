import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { NursingOrderSave } from './nursing-order.models';

@Injectable({ providedIn: 'root' })
export class NursingOrderService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  save(payload: NursingOrderSave): Observable<any> {
    return this.http.post(`${this.base}/api/opd/saveNursingOrderMapper`, payload);
  }
}
