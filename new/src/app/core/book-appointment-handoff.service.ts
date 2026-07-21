import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `webAppDetailObj` -- both the
 * `bookAppointment` state (app.js `params: { webAppDetailObj: {} }`) and
 * `webAppController.js:141-144`'s `$state.go('bookAppointment', { webAppDetailObj: ... })`
 * call. Same reasoning as WebAppointmentHandoffService/AssetDataHandoffService:
 * a plain service, not Router `extras.state`, since legacy's own param
 * does not survive a real page refresh either.
 *
 * Sending side built here (webAppointmentBooking, screen 6/14). Receiving
 * side (bookAppointment, screen 11/14, batch 1c) not yet built.
 */
export interface WebAppointmentDetailObject {
  organizationId?: string;
  unitId?: string;
  doctorId?: number;
  specialityId?: number;
  userId?: number;
  doctorName?: string;
  specialityName?: string;
  doctorImage?: string;
}

@Injectable({ providedIn: 'root' })
export class BookAppointmentHandoffService {
  private pendingDetail: WebAppointmentDetailObject | null = null;

  setDetail(detail: WebAppointmentDetailObject): void {
    this.pendingDetail = detail;
  }

  takeDetail(): WebAppointmentDetailObject {
    return this.pendingDetail ?? {};
  }
}
