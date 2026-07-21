import { Component, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { BookAppointmentHandoffService } from '../../core/book-appointment-handoff.service';
import { WebAppointmentBookingService } from './web-appointment-booking.service';
import { WebAppointmentUnit, WebAppointmentSpeciality, WebAppointmentDoctor } from './web-appointment-booking.models';

/**
 * Rebuild of the legacy `webAppointmentBooking` state -- see
 * webAppController.js (147 lines) and
 * views/AppointmentScheduling/web-appointment-booking.html. Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entry 39.
 *
 * No data.moduleId, no moduleAccessGuard on this route -- legacy's
 * controller has ZERO session guard anywhere (no cookieObject check,
 * $scope.userId hardcoded to 1), a genuinely public, patient-facing
 * "browse doctors" landing page. orgId/unitId are real URL path params
 * (`/webAppointmentBooking/:orgId/:unitId`), not an in-memory ui-router
 * param.
 *
 * Parity targets, deliberately NOT fixed (entry 39):
 *  - The unit-address label stays permanently empty (dead in legacy too).
 *  - The radio-group visual-checked-state mismatch is not corrected --
 *    this rebuild tracks the actually-selected speciality via a signal
 *    (the correct Angular way to drive a radio group), which does NOT
 *    reproduce legacy's specific "last-rendered radio silently wins"
 *    quirk since that's an artifact of raw `checked` attributes in a
 *    shared name group, not something meaningful to port bit-for-bit.
 *    Disclosed simplification, not a silent fix of the underlying
 *    doctor-list/radio-state mismatch itself (see Step 5 notes).
 *  - getUnitListByOrgId()'s unconditional post-status-check code is
 *    replicated as-is (proceeds regardless of fetch success).
 */
@Component({
  selector: 'app-web-appointment-booking',
  imports: [FormsModule],
  templateUrl: './web-appointment-booking.html',
  styleUrl: './web-appointment-booking.scss',
})
export class WebAppointmentBooking {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly handoff = inject(BookAppointmentHandoffService);
  private readonly api = inject(WebAppointmentBookingService);

  private readonly organizationId: string;
  private readonly unitIdParam: string;

  unitList = signal<WebAppointmentUnit[]>([]);
  specialityMasterList = signal<WebAppointmentSpeciality[]>([]);
  doctorList = signal<WebAppointmentDoctor[]>([]);
  selectedUnitId = signal(0);
  isHideUnitList = signal(false);
  loadingMsg = signal('Loading...');
  selectedSpecialityId = signal<number | null>(null);

  constructor() {
    const params = this.route.snapshot.paramMap;
    this.organizationId = params.get('orgId') ?? '';
    this.unitIdParam = params.get('unitId') ?? '';

    sessionStorage.setItem(
      'webAppOrgUnitCookieObj',
      JSON.stringify({ orgId: this.organizationId, unitId: this.unitIdParam }),
    );

    this.getUnitListByOrgId();
  }

  private getUnitListByOrgId(): void {
    this.api.getUnitListByOrgId(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const units = res.listObject ?? [];
          this.unitList.set(units);
          if (this.unitIdParam === '0') {
            this.selectedUnitId.set(units[0]?.unitId ?? 0);
          } else {
            this.isHideUnitList.set(true);
            this.selectedUnitId.set(Number(this.unitIdParam));
          }
        }
        // Unconditional, matching legacy exactly -- runs regardless of
        // the status check above (entry 39 finding 3).
        this.getSpecialityMasterList();
        this.loadingMsg.set('Loading...');
        this.api.getDoctors(this.organizationId, String(this.selectedUnitId()), true, 1).subscribe({
          next: (docRes) => {
            if (docRes.status === 'success') {
              this.doctorList.set(docRes.listObject ?? []);
            }
            if (this.doctorList().length === 0) {
              this.loadingMsg.set('No Doctors Available');
            }
          },
        });
      },
    });
  }

  private getSpecialityMasterList(): void {
    this.api.getActiveSpecialityList(this.organizationId, String(this.selectedUnitId())).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.specialityMasterList.set(res.listObject ?? []);
        }
      },
    });
  }

  getDoctorsList(): void {
    this.doctorList.set([]);
    this.loadingMsg.set('Loading...');
    const specialityId = this.selectedSpecialityId();
    const flag = specialityId === null;
    this.api.getDoctors(this.organizationId, String(this.selectedUnitId()), flag, specialityId ?? '').subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.doctorList.set(res.listObject ?? []);
          if (this.doctorList().length === 0) {
            this.loadingMsg.set('No Doctors Available');
          }
        }
      },
    });
  }

  onSpecialitySelected(specialityId: number | null): void {
    this.selectedSpecialityId.set(specialityId);
    this.getDoctorsList();
  }

  getChangedUnitData(): void {
    this.selectedSpecialityId.set(null);
    this.getSpecialityMasterList();
    this.getDoctorsList();
  }

  gotoBookAppointmentPage(doc: WebAppointmentDoctor): void {
    this.handoff.setDetail({
      organizationId: this.organizationId,
      unitId: String(this.selectedUnitId()),
      doctorId: doc.doctorId,
      specialityId: doc.specialityId,
      userId: doc.userId,
      doctorName: `${doc.firstName} ${doc.lastName}`,
      specialityName: doc.specialityName,
      doctorImage: doc.doctorImage,
    });
    this.router.navigate(['/bookAppointment']);
  }
}
