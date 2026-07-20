import { Injectable } from '@angular/core';

/**
 * Field shapes match webAppointmentBookingConfirmationController.js's
 * `$scope.webAppConfDetObj` (raw fields set by whichever screen navigates
 * here, plus the 3 fields ~lines 29-53 compute: scheduledDate,
 * visitDuration, patientVisit). `areaOfExperty` keeps legacy's exact field
 * name typo -- this is a wire-contract shape, not free to "correct".
 */
export interface WebAppointmentConfirmationDetail {
  unitAddress?: string;
  doctorImage?: string | null;
  doctorName?: string;
  areaOfExperty?: string;
  doctorDegrees?: string;
  visitFee?: number | string;
  consultingSpeciality?: string;
  patientName?: string;
  mobileNumber?: string;
  emailId?: string;
  appointmentReason?: string;
  appointmentDate?: string;
  fromTime?: string;
  toTime?: string;
  visitTypeId?: number | string;
  isVideoApp?: number;
}

/**
 * In-memory handoff between `webAppointmentBooking` (sets this right before
 * navigating) and `webAppointmentBookingConfirmation` (reads it on arrival).
 *
 * Deliberately a plain Angular service, not Angular Router's `extras.state`
 * navigation-state mechanism. Legacy passes `webAppConfirmationObj` via
 * ui-router's own non-URL custom `params: { webAppConfirmationObj: {} }`
 * (app.js:21011-21013) -- a value that does NOT survive a real full-page
 * reload (it resets to the configured `{}` default, since nothing encodes
 * it into the URL). `history.state` WOULD incorrectly survive a reload,
 * which would silently diverge from legacy's actual behavior on that
 * screen (a real full-page refresh currently produces the broken/blank
 * confirmation page described in docs/ANGULAR_PENDING_FINDINGS.md entry
 * 19 -- not just a direct link or bookmark). A plain service, re-instantiated
 * fresh on every app bootstrap, is the correct analog.
 */
@Injectable({ providedIn: 'root' })
export class WebAppointmentHandoffService {
  private pendingConfirmationDetail: WebAppointmentConfirmationDetail | null = null;

  setConfirmationDetail(detail: WebAppointmentConfirmationDetail): void {
    this.pendingConfirmationDetail = detail;
  }

  /**
   * Always returns whatever is currently pending, defaulting to `{}` --
   * exactly matching ui-router's own default-value behavior for this
   * param. Deliberately NOT cleared after reading: legacy's own `isEmpty`
   * safety-net check (entry 19) can never fire due to a type-mismatched
   * `angular.equals()` comparison, so re-reading this screen's data (e.g.
   * on an Angular-side re-render without a full reload) is not something
   * legacy actually guards against either -- replicating that gap exactly,
   * not inventing stricter behavior legacy doesn't have.
   */
  takeConfirmationDetail(): WebAppointmentConfirmationDetail {
    return this.pendingConfirmationDetail ?? {};
  }
}
