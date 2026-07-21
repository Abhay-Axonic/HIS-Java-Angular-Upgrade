import { Component, inject, signal } from '@angular/core';
import moment from 'moment';
import { WebAppointmentHandoffService, WebAppointmentConfirmationDetail } from '../../core/web-appointment-handoff.service';

export interface WebAppointmentConfirmationViewModel extends WebAppointmentConfirmationDetail {
  scheduledDate: string;
  visitDuration: number;
  patientVisit: string;
}

/**
 * Rebuild of the legacy `webAppointmentBookingConfirmation` state -- see
 * webAppointmentBookingConfirmationController.js (65 lines, no backend
 * calls at all -- purely a display screen driven by data handed off from
 * whichever screen navigated here). Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entry 19.
 *
 * Parity target: entry 19's "no real data, bounce back" safety check is
 * BROKEN in legacy (`angular.equals('{}', obj)` compares a string to an
 * object and is always false) and is faithfully NOT implemented here --
 * this is not the parity target for this screen's rebuild. A direct link,
 * bookmark, or real page refresh renders the same misleading confirmation
 * page legacy does: blank patient/doctor fields, a NaN-derived visit
 * duration, and a default "Follow Up (Video)" label regardless of what
 * was actually booked.
 */
@Component({
  selector: 'app-web-appointment-booking-confirmation',
  imports: [],
  templateUrl: './web-appointment-booking-confirmation.html',
  styleUrl: './web-appointment-booking-confirmation.scss',
})
export class WebAppointmentBookingConfirmation {
  private readonly handoff = inject(WebAppointmentHandoffService);

  detail = signal<WebAppointmentConfirmationViewModel>(this.buildViewModel());

  private buildViewModel(): WebAppointmentConfirmationViewModel {
    const webAppConfDetObj = this.handoff.takeConfirmationDetail();

    const scheduledDate = moment(webAppConfDetObj.appointmentDate).format('DD MMM YYYY');

    const formattedCurrDate = moment(new Date()).format('YYYY/MM/DD');
    const date1 = new Date(`${formattedCurrDate} ${webAppConfDetObj.fromTime}`);
    const date2 = new Date(`${formattedCurrDate} ${webAppConfDetObj.toTime}`);
    const visitDuration = (date2.getTime() - date1.getTime()) / (1000 * 60);

    const visitTypeId = parseInt(String(webAppConfDetObj.visitTypeId), 10);
    const isTeleConsultApp = webAppConfDetObj.isVideoApp;
    let patientVisit: string;
    if (visitTypeId === 1 && isTeleConsultApp === 0) {
      patientVisit = 'Consultation (In Person)';
    } else if (visitTypeId === 2 && isTeleConsultApp === 0) {
      patientVisit = 'Follow Up (In Person)';
    } else if (visitTypeId === 2 && isTeleConsultApp === 1) {
      patientVisit = 'Consultation (Video)';
    } else {
      patientVisit = 'Follow Up (Video)';
    }

    return { ...webAppConfDetObj, scheduledDate, visitDuration, patientVisit };
  }
}
