import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { WebAppointmentUnit, WebAppointmentSpeciality, WebAppointmentDoctor } from './web-appointment-booking.models';

@Injectable({ providedIn: 'root' })
export class WebAppointmentBookingService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getUnitListByOrgId(organizationId: string): Observable<ApiResponse<never, WebAppointmentUnit>> {
    return this.http.get<ApiResponse<never, WebAppointmentUnit>>(`${this.base}/unit/org/${organizationId}`);
  }

  getActiveSpecialityList(organizationId: string, unitId: string): Observable<ApiResponse<never, WebAppointmentSpeciality>> {
    return this.http.post<ApiResponse<never, WebAppointmentSpeciality>>(`${this.base}/adt/getActiveSpecialityList`, {
      organizationId,
      unitId,
    });
  }

  getDoctors(
    organizationId: string,
    unitId: string,
    flag: boolean,
    specialityId: number | string,
  ): Observable<ApiResponse<never, WebAppointmentDoctor>> {
    return this.http.post<ApiResponse<never, WebAppointmentDoctor>>(`${this.base}/api/global/webappointment/getDoctors`, {
      organizationId,
      unitId,
      flag,
      specialityId,
    });
  }
}
