import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { OpfFeedbackItem, OpfSelectedItem } from './out-patient-feedback.models';

@Injectable({ providedIn: 'root' })
export class OutPatientFeedbackService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getOutPatientFeedback(
    unitId: number,
    organizationId: number,
    patientId: number,
    visitType: string,
    visitTypeId: string,
    encounterId: number,
  ): Observable<ApiResponse<number, OpfFeedbackItem>> {
    return this.http.get<ApiResponse<number, OpfFeedbackItem>>(
      `${this.base}/api/outPatientFeedback/${unitId}/${organizationId}/${patientId}/${visitType}/${visitTypeId}/${encounterId}`,
    );
  }

  saveOutPatientFeedback(payload: {
    encounterId: number;
    patientId: number;
    visitType: string;
    visitTypeId: string;
    unitId: number;
    organizationId: number;
    feedbackSelectedList: OpfSelectedItem[];
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/outPatientFeedback/saveOutPatientFeedback`, payload);
  }
}
