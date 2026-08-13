import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { SampleTypeSave } from './lab-sample-type.models';

@Injectable({ providedIn: 'root' })
export class LabSampleTypeService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  addSample(payload: SampleTypeSave): Observable<any> {
    return this.http.post(`${this.base}/LIS/global/addSample`, payload);
  }
}
