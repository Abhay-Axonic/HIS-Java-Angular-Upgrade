import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { IpfFeedbackItem, IpfSelectedItem } from './in-patient-feedback.models';

@Injectable({ providedIn: 'root' })
export class InPatientFeedbackService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getInPatientFeedback(
    unitId: number,
    organizationId: number,
    patientId: number,
    visitType: string,
    visitTypeId: string,
    admissionId: number,
  ): Observable<ApiResponse<number, IpfFeedbackItem>> {
    return this.http.get<ApiResponse<number, IpfFeedbackItem>>(
      `${this.base}/api/inPatientFeedback/${unitId}/${organizationId}/${patientId}/${visitType}/${visitTypeId}/${admissionId}`,
    );
  }

  saveInPatientFeedback(payload: {
    admissionId: number;
    patientId: number;
    visitType: string;
    visitTypeId: string;
    unitId: number;
    organizationId: number;
    feedbackSelectedList: IpfSelectedItem[];
    createdDate: string;
    updatedDate: string;
  }): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/inPatientFeedback/saveInPatientFeedback`, payload);
  }
}
