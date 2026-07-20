import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BaApplicationConfig, BaGender, BaPatientSearchResult, BaPrefix, BaSlot, BaSpeciality, BaWebAppDetail } from './book-appointment.models';

@Injectable({ providedIn: 'root' })
export class BookAppointmentService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getActivePrefixList(organizationId: string): Observable<ApiResponse<never, BaPrefix>> {
    return this.http.get<ApiResponse<never, BaPrefix>>(`${this.base}/api/webappointment/getActivePrefixList/${organizationId}`);
  }

  getApplicationConfig(organizationId: string, unitId: string): Observable<ApiResponse<BaApplicationConfig>> {
    return this.http.post<ApiResponse<BaApplicationConfig>>(`${this.base}/api/doctorApp/getApplicationConfig/`, { organizationId, unitId });
  }

  getActiveGenderList(): Observable<ApiResponse<never, BaGender>> {
    return this.http.get<ApiResponse<never, BaGender>>(`${this.base}/adt/getActiveGenderList`);
  }

  getDetailsForBookAppointment(payload: {
    organizationId: string;
    unitId: string;
    doctorId?: number;
    userId?: number;
    specialityId?: number;
    appointmentDate: string;
  }): Observable<ApiResponse<BaWebAppDetail>> {
    return this.http.post<ApiResponse<BaWebAppDetail>>(`${this.base}/api/webappointment/getDetailsForBookAppointment`, payload);
  }

  getSpecialityListByDoctorIdByOrg(organizationId: string, doctorId?: number): Observable<ApiResponse<never, BaSpeciality>> {
    return this.http.post<ApiResponse<never, BaSpeciality>>(`${this.base}/adt/getSpecialityListByDoctorIdByOrg`, { organizationId, doctorId });
  }

  getDailySlots(payload: {
    organizationId: string;
    unitId: string;
    slotTypeId: number;
    doctorId?: number;
    specialityId?: number;
    appointmentDate: string;
  }): Observable<ApiResponse<never, BaSlot>> {
    return this.http.post<ApiResponse<never, BaSlot>>(`${this.base}/api/opd/getDailySlots`, payload);
  }

  searchPatientByUhid(uhidNumber: string, orgId: string): Observable<ApiResponse<never, BaPatientSearchResult>> {
    return this.http.get<ApiResponse<never, BaPatientSearchResult>>(`${this.base}/api/webappointment/getPatientDetailsByUhid`, {
      params: { uhidNumber, orgId },
    });
  }

  /** This endpoint's response is not the usual {status, object, listObject}
   * envelope -- legacy reads `response.data.toString()` directly, so the
   * raw body itself is the visit-type value. */
  getSystemGeneratedVisitTypeId(payload: {
    currentDate: string;
    doctorId?: number;
    organizationId: string;
    patientId: number;
    specialityId?: number;
    unitId: string;
  }): Observable<number | string> {
    return this.http.post<number | string>(`${this.base}/api/opd/getSystemGeneratedVisitTypeId`, payload);
  }

  getServiceRates(payload: Record<string, unknown>): Observable<ApiResponse<{ listOrderDetailsMasterDto: { netAmount: number }[] }>> {
    return this.http.post<ApiResponse<{ listOrderDetailsMasterDto: { netAmount: number }[] }>>(`${this.base}/api/opd/billAmount/appointment`, payload);
  }

  checkPatientUnique(payload: Record<string, unknown>): Observable<ApiResponse<never, BaPatientSearchResult>> {
    return this.http.post<ApiResponse<never, BaPatientSearchResult>>(`${this.base}/api/global/patient/unique`, payload);
  }

  scheduleAppointment(payload: Record<string, unknown>): Observable<ApiResponse<{ appointmentId: number }>> {
    return this.http.post<ApiResponse<{ appointmentId: number }>>(`${this.base}/api/opd/scheduleAppointment`, payload);
  }

  getOrderIdForRazorPayPayment(payload: Record<string, unknown>): Observable<ApiResponse<{ orderId: string }>> {
    return this.http.post<ApiResponse<{ orderId: string }>>(`${this.base}/api/getOrderIdForRazorPayPayment`, payload);
  }
}
