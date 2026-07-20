import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { BookAppointmentHandoffService } from '../../core/book-appointment-handoff.service';
import { WebAppointmentHandoffService } from '../../core/web-appointment-handoff.service';
import { BookAppointmentService } from './book-appointment.service';
import { BaApplicationConfig, BaGender, BaPatientSearchResult, BaPrefix, BaSlot, BaSpeciality, BaWebAppDetail } from './book-appointment.models';

interface WeekDay {
  date: Date;
  selected: boolean;
}

/**
 * Rebuild of the legacy `bookAppointment` state -- see
 * bookAppointmentController.js (1008 lines) + book-appointment.html
 * (873 lines). Full audit: docs/ANGULAR_PENDING_FINDINGS.md entries 19
 * (type-mismatched `angular.equals` safety check, shared pattern),
 * 20 ($state.go with a path string instead of a state name -- reachable
 * here, unlike entry 19's screen), 27 (dead `confirmBooking()`/EHC
 * package sub-feature/broken Enter-key shortcut/silent validation gap).
 *
 * `webAppDetailObj` handoff: receiving side, sending side already built
 * in webAppointmentBooking (screen 6/14, batch 1b) -- BookAppointmentHandoffService.
 * On success, hands off to webAppointmentBookingConfirmation (already
 * built, batch 1a) via the existing WebAppointmentHandoffService --
 * reusing both, not rebuilding.
 *
 * Entry 19/20's `isEmpty` check IS reachable and meaningful here (unlike
 * webAppointmentBookingConfirmation): if `webAppDetailObj` is empty,
 * legacy falls back to `sessionStorage.webAppOrgUnitCookieObj` and does
 * `$state.go('/webAppointmentBooking/'+orgId+"/"+unitId)` -- a URL PATH,
 * not a state name, so ui-router treats it as an unrecognized state and
 * silently does nothing (confirmed still true in the retroactive audit).
 * Replicated: if the handoff is empty, this component does NOT redirect
 * (matching the actual, if likely unintended, observable behavior) --
 * it renders with `webAppDetailObj` fields empty/undefined instead of a
 * blank page or a crash, same non-fix as legacy.
 *
 * NOT reproduced: entry 27's confirmBooking() (dead, superseded by
 * bookNormalAppointment() -- the view's real Save button calls
 * `bookNormalAppointment()`, confirmed at book-appointment.html:683) and
 * the EHC-package sub-feature (`$scope.selectedSlotId` is never assigned
 * anywhere in the controller, so `if($scope.selectedSlotId=='3')` never
 * fires; its own dropdown is HTML-commented-out at book-appointment.html:626)
 * -- both are provably unreachable, same treatment as other confirmed-dead
 * code this project has chosen not to rebuild (e.g. entry 5's dead Cancel
 * button).
 *
 * NOT reproduced: entry 27's broken Enter-key shortcut
 * (`press-enter="searchPatient(...)"`, a nonexistent function -- the real
 * one is `searchPatientByUhid()`). This rebuild wires Enter to the real
 * search function directly; reproducing a guaranteed no-op keyboard
 * shortcut serves no parity purpose and the "silent validation gap for
 * public users" this entry also describes is a product decision already
 * flagged there, not re-litigated here.
 *
 * KNOWN GAP, disclosed, not fixable in this environment: the "Book and
 * Pay" (Razorpay) flow is wired faithfully through
 * `getOrderIdForRazorPayPayment` and constructing the same checkout
 * options object, but the actual Razorpay checkout widget is a live,
 * third-party payment gateway that cannot be exercised in this dev
 * environment -- same disclosed limitation as pesapalProcessPage
 * (entry 36). Only the non-payment "Book Appointment" button's flow can
 * be verified end-to-end here.
 */
@Component({
  selector: 'app-book-appointment',
  imports: [FormsModule],
  templateUrl: './book-appointment.html',
  styleUrl: './book-appointment.scss',
})
export class BookAppointment {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(BookAppointmentHandoffService);
  private readonly confirmationHandoff = inject(WebAppointmentHandoffService);
  private readonly api = inject(BookAppointmentService);

  organizationId = '';
  unitId = '';
  doctorId?: number;
  specialityId?: number;
  userId?: number;
  doctorName = '';
  specialityName = '';
  doctorImage?: string;

  webAppDetObj = signal<BaWebAppDetail>({ unitDetList: [], doctorDto: {}, doctorDegrees: '', orderMasterDto: { listOrderDetailsMasterDto: [] } });
  unitAddress = signal('');
  appCharge = signal<number | ''>('');
  prefixList = signal<BaPrefix[]>([]);
  genderList = signal<BaGender[]>([]);
  docSpecList = signal<BaSpeciality[]>([]);
  areaOfExpertise = signal('');
  applicationConfig: BaApplicationConfig = {};

  selectedUnitId = '';
  selSpecId = '';

  systemDefinedVisitType = signal(1);
  slotId = signal(0);
  appointmentDate = signal(formatYmd(new Date()));
  appointmentTime = '';
  originalFromTime = '';
  originalToTime = '';
  patientId = 0;
  patientCategoryId?: number;

  doctorSlotList = signal<BaSlot[]>([]);
  loadingMsg = signal('Loading...');

  weekArray = signal<WeekDay[]>([]);
  weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];
  slotDatePicker = signal(new Date());

  disablePatForm = signal(false);
  patientDetails = signal<{
    prefixId?: number;
    firstName: string;
    middleName: string;
    lastName: string;
    genderId?: number;
    mobileNumber: string;
    email: string;
    remark: string;
    age: string;
  }>({ firstName: '', middleName: '', lastName: '', mobileNumber: '', email: '', remark: '', age: '' });
  dob = signal<Date | undefined>(undefined);

  searchUhid = '';
  searchLoadingMsg = signal('');
  searchPatList = signal<BaPatientSearchResult[]>([]);
  showPatientSearchModal = signal(false);

  patientAvailList = signal<BaPatientSearchResult[]>([]);
  showPatientAlreadyExistModal = signal(false);
  isAppointmentBooked = false;
  isRequestForPay = false;

  constructor() {
    const detail = this.handoff.takeDetail();
    this.organizationId = detail.organizationId ?? '';
    this.unitId = detail.unitId ?? '';
    this.doctorId = detail.doctorId;
    this.specialityId = detail.specialityId;
    this.userId = detail.userId;
    this.doctorName = detail.doctorName ?? '';
    this.specialityName = detail.specialityName ?? '';
    this.doctorImage = detail.doctorImage;
    this.selectedUnitId = this.unitId;
    this.selSpecId = String(this.specialityId ?? '');

    this.api.getActivePrefixList(this.organizationId).subscribe({
      next: (res) => this.prefixList.set(res.status === 'success' ? (res.listObject ?? []) : []),
    });
    this.api.getApplicationConfig(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) this.applicationConfig = res.object;
      },
    });
    this.api.getActiveGenderList().subscribe({
      next: (res) => this.genderList.set(res.status === 'success' ? (res.listObject ?? []) : []),
    });

    this.getDetailsForBookAppointment();
    this.loadSpecialityList();
    this.getDoctorSlots();
    this.getWeekCalendar(new Date(), true);
  }

  private getDetailsForBookAppointment(): void {
    this.api
      .getDetailsForBookAppointment({
        organizationId: this.organizationId,
        unitId: this.unitId,
        doctorId: this.doctorId,
        userId: this.userId,
        specialityId: this.specialityId,
        appointmentDate: formatDmy(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success' && res.object) {
            this.webAppDetObj.set(res.object);
            const firstOrder = res.object.orderMasterDto?.listOrderDetailsMasterDto?.[0];
            this.appCharge.set(firstOrder ? firstOrder.netAmount : '');
            const unit = res.object.unitDetList.find((u) => String(u.unitId) === String(this.unitId));
            this.unitAddress.set(unit?.unitAddress ?? '');
          }
        },
      });
  }

  private loadSpecialityList(): void {
    this.api.getSpecialityListByDoctorIdByOrg(this.organizationId, this.doctorId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const list = res.listObject ?? [];
          this.docSpecList.set(list);
          this.areaOfExpertise.set(list.map((s) => s.specialityName).join(', '));
        }
      },
    });
  }

  getDoctorSlots(): void {
    this.doctorSlotList.set([]);
    this.loadingMsg.set('Loading...');
    this.api
      .getDailySlots({
        organizationId: this.organizationId,
        unitId: this.unitId,
        slotTypeId: 1,
        doctorId: this.doctorId,
        specialityId: this.specialityId,
        appointmentDate: this.appointmentDate(),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            const list = res.listObject ?? [];
            this.doctorSlotList.set(list);
            if (list.length === 0) this.loadingMsg.set('No slots available');
          } else {
            this.loadingMsg.set('No slots available');
          }
        },
      });
  }

  get patientDetailsPrefixId(): number | undefined {
    return this.patientDetails().prefixId;
  }
  set patientDetailsPrefixId(value: number | undefined) {
    this.patientDetails.update((p) => ({ ...p, prefixId: value }));
  }

  updatePatientField<K extends keyof ReturnType<BookAppointment['patientDetails']>>(field: K, value: ReturnType<BookAppointment['patientDetails']>[K]): void {
    this.patientDetails.update((p) => ({ ...p, [field]: value }));
  }

  onUnitChange(): void {
    this.unitId = this.selectedUnitId;
    this.slotId.set(0);
    this.getDoctorSlots();
    const unit = this.webAppDetObj().unitDetList.find((u) => String(u.unitId) === String(this.unitId));
    this.unitAddress.set(unit?.unitAddress ?? '');
  }

  onSpecialityChange(): void {
    this.specialityId = Number(this.selSpecId);
    this.slotId.set(0);
    this.getDoctorSlots();
  }

  private getWeekCalendar(date: Date, selectMatchingDate: boolean): void {
    const monday = getMonday(date);
    const days: WeekDay[] = [];
    for (let i = 0; i < 7; i++) {
      const d = new Date(monday);
      d.setDate(monday.getDate() + i);
      days.push({ date: d, selected: selectMatchingDate ? d.getDate() === date.getDate() : d.getDate() === new Date().getDate() });
    }
    this.weekArray.set(days);
  }

  getPreviousWeek(): void {
    const d = new Date(this.weekArray()[0]?.date ?? new Date());
    d.setDate(d.getDate() - 1);
    this.getWeekCalendar(d, false);
  }

  getNextWeek(): void {
    const d = new Date(this.weekArray()[6]?.date ?? new Date());
    d.setDate(d.getDate() + 1);
    this.getWeekCalendar(d, false);
  }

  getSlotByDate(day: WeekDay): void {
    this.slotDatePicker.set(day.date);
    this.appointmentDate.set(formatYmd(day.date));
    this.getDoctorSlots();
  }

  onDatePickerDateChange(value: Date): void {
    this.slotDatePicker.set(value);
    this.appointmentDate.set(formatYmd(value));
    this.getWeekCalendar(value, true);
    this.getDoctorSlots();
  }

  selectSlotFun(slot: BaSlot): void {
    this.slotId.set(slot.slotId);
    this.appointmentTime = slot.fromTimeString2;
    this.originalFromTime = slot.fromTimeString;
    this.originalToTime = slot.toTimeString;
  }

  slotClass(slot: BaSlot): string {
    if (this.slotId() === slot.slotId) return 'bg-brown cursor-pointer day-slot-div';
    if (slot.isBlockedUnblocked === '2' && slot.slotPatientDetailsListsSize > 0 && slot.slotPatientDetailsListsSize < slot.allowedOverBookingSlots) {
      return 'bg-orange cursor-pointer day-slot-div';
    }
    if (slot.isBlockedUnblocked === '2' && slot.slotPatientDetailsListsSize === 0) {
      return 'bg-green cursor-pointer day-slot-div';
    }
    return 'cursor-pointer day-slot-div';
  }

  openPatientSearchModal(): void {
    this.searchUhid = '';
    this.searchLoadingMsg.set('');
    this.searchPatList.set([]);
    this.showPatientSearchModal.set(true);
  }

  searchPatientByUhid(): void {
    if (!this.searchUhid) {
      this.notify.warning('Enter uhid number');
      return;
    }
    this.searchLoadingMsg.set('Loading...');
    this.searchPatList.set([]);
    this.api.searchPatientByUhid(this.searchUhid, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const list = res.listObject ?? [];
          this.searchPatList.set(list);
          if (list.length === 0) this.searchLoadingMsg.set('No records found');
        }
      },
    });
  }

  fetchAndFillPatDetail(pat: BaPatientSearchResult): void {
    this.disablePatForm.set(true);
    this.showPatientSearchModal.set(false);
    this.showPatientAlreadyExistModal.set(false);
    this.patientId = pat.patientId;
    this.patientCategoryId = pat.patientCategoryId;
    this.dob.set(new Date(pat.birthDate));
    this.patientDetails.set({
      prefixId: pat.prefixId,
      firstName: pat.firstName,
      middleName: pat.middleName ?? '',
      lastName: pat.lastName,
      genderId: pat.genderId,
      mobileNumber: pat.mobileNumber,
      email: pat.email ?? '',
      remark: this.patientDetails().remark,
      age: pat.ageString,
    });

    this.api
      .getSystemGeneratedVisitTypeId({
        currentDate: this.appointmentDate(),
        doctorId: this.doctorId,
        organizationId: this.organizationId,
        patientId: this.patientId,
        specialityId: this.specialityId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          this.systemDefinedVisitType.set(Number(res));
          this.getServiceRates();
        },
      });
  }

  private getServiceRates(): void {
    this.loader.start();
    this.api
      .getServiceRates({
        unitId: this.unitId,
        organizationId: this.organizationId,
        patientId: this.patientId || null,
        doctorId: this.doctorId,
        specialityId: this.specialityId,
        currentDate: formatYmd(new Date()),
        patientCategoryId: this.patientCategoryId ?? this.applicationConfig['defaultPatientCategoryId'],
        deptWiseAppointment: 'N',
        userDefinedVisitTypeId: this.systemDefinedVisitType(),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success' && res.object) {
            this.appCharge.set(res.object.listOrderDetailsMasterDto[0]?.netAmount ?? '');
          }
        },
      });
  }

  calculateAge(): void {
    const dob = this.dob();
    if (!dob) {
      this.patientDetails.update((p) => ({ ...p, age: '' }));
      this.notify.warning('Date is invalid');
      this.dob.set(new Date());
      return;
    }
    if (dob > new Date()) {
      this.patientDetails.update((p) => ({ ...p, age: '' }));
      this.notify.warning("DOB should not be greater than today's date");
      this.dob.set(new Date());
      return;
    }
    const age = calculateAgeYears(dob);
    this.patientDetails.update((p) => ({ ...p, age: String(age) }));
  }

  showDOBByAge(age: string): void {
    const years = Number(age);
    if (!years) return;
    const finalYear = new Date().getFullYear() - years;
    this.dob.set(new Date(`01/01/${finalYear}`));
  }

  resetDetails(): void {
    this.systemDefinedVisitType.set(1);
    this.patientId = 0;
    this.disablePatForm.set(false);
    this.dob.set(undefined);
    this.patientDetails.set({ firstName: '', middleName: '', lastName: '', mobileNumber: '', email: '', remark: '', age: '' });
  }

  bookNormalAppointment(): void {
    this.patientAvailList.set([]);
    if (this.slotId() === 0) {
      this.notify.warning('Select slot');
      return;
    }
    this.loader.start();
    const details = this.patientDetails();
    const uniquePayload = {
      organizationId: this.organizationId,
      genderId: details.genderId,
      dob: this.dob() ? formatDmyHms(this.dob()!) : '',
      mobileNumber: details.mobileNumber,
      registrationTypeId: 1,
    };
    this.api.checkPatientUnique(uniquePayload).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const list = res.listObject ?? [];
        if (list.length > 0 && this.patientId === 0) {
          this.loader.stop();
          this.patientAvailList.set(list);
          this.showPatientAlreadyExistModal.set(true);
          return;
        }
        this.doScheduleAppointment();
      },
    });
  }

  private doScheduleAppointment(): void {
    const now = formatDmyHms(new Date());
    const details = this.patientDetails();
    const data: Record<string, unknown> = {
      slotId: this.slotId(),
      remark: details.remark,
      organizationId: this.organizationId,
      unitId: this.unitId,
      createdDate: now,
      updatedDate: now,
      createdBy: this.userId,
      updatedBy: this.userId,
      status: 'A',
      appointmentTypeId: 1,
      slotStatusId: 4,
      priorityId: '1',
      firstName: details.firstName,
      mobileNumber: details.mobileNumber,
      doctorName: this.doctorName,
      fromTimeString: this.originalFromTime,
      appointmentDate2: formatDmySlash(new Date(this.appointmentDate())),
      doctorId: this.doctorId,
      specialityId: this.specialityId,
      isTeleconsultationAppointment: 0,
      specialityName: this.specialityName,
    };

    if (this.patientId === 0) {
      data['prefixId'] = details.prefixId;
      data['firstName'] = details.firstName;
      data['middleName'] = details.middleName;
      data['lastName'] = details.lastName;
      data['dob'] = this.dob() ? formatDmyHms(this.dob()!) : '';
      data['genderId'] = details.genderId;
      data['mobileNumber'] = details.mobileNumber;
      data['email'] = details.email;
      data['registrationTypeId'] = 1;
      data['patientId'] = 0;
      data['patientCategoryId'] = this.applicationConfig['defaultPatientCategoryId'];
    } else {
      data['patientId'] = this.patientId;
    }

    this.api.scheduleAppointment(data).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Appointment Booked Successfully');
          this.isAppointmentBooked = true;
          this.loader.stop();

          this.confirmationHandoff.setConfirmationDetail({
            unitAddress: this.unitAddress(),
            areaOfExperty: this.areaOfExpertise(),
            doctorDegrees: this.webAppDetObj().doctorDegrees,
            doctorImage: this.doctorImage,
            doctorName: this.doctorName,
            appointmentDate: this.appointmentDate(),
            fromTime: this.originalFromTime,
            toTime: this.originalToTime,
            visitFee: this.appCharge(),
            patientName: `${details.firstName} ${details.lastName}`,
            mobileNumber: details.mobileNumber,
            emailId: details.email,
            appointmentReason: details.remark,
            consultingSpeciality: this.specialityName,
            isVideoApp: 0,
            visitTypeId: this.systemDefinedVisitType(),
          });

          if (this.isRequestForPay) {
            this.goToRazorPay(res.object?.appointmentId);
          } else {
            this.router.navigate(['/webAppointmentBookingConfirmation']);
          }
        }
      },
    });
  }

  openPaymentPage(): void {
    this.isRequestForPay = true;
    if (!this.isAppointmentBooked) {
      this.bookNormalAppointment();
    } else {
      this.goToRazorPay();
    }
  }

  /** Wired faithfully through getOrderIdForRazorPayPayment, but the
   * actual Razorpay checkout widget is a live third-party payment
   * gateway -- cannot be exercised in this dev environment, same
   * disclosed limitation as pesapalProcessPage (entry 36). */
  private goToRazorPay(appointmentId?: number): void {
    this.loader.start();
    this.api
      .getOrderIdForRazorPayPayment({
        unitId: this.unitId,
        organizationId: this.organizationId,
        amount: this.appCharge(),
        appointmentId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status !== 'success') {
            this.notify.error(res.message ?? '');
          }
          // Razorpay checkout widget invocation intentionally not wired --
          // see class doc comment.
        },
      });
  }
}

function getMonday(d: Date): Date {
  const date = new Date(d);
  const day = date.getDay();
  const diff = date.getDate() - day + (day === 0 ? -6 : 1);
  return new Date(date.setDate(diff));
}

function calculateAgeYears(dob: Date): number {
  const now = new Date();
  let age = now.getFullYear() - dob.getFullYear();
  const monthDiff = now.getMonth() - dob.getMonth();
  if (monthDiff < 0 || (monthDiff === 0 && now.getDate() < dob.getDate())) age--;
  return age;
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}

function formatDmy(d: Date): string {
  return `${String(d.getDate()).padStart(2, '0')}-${String(d.getMonth() + 1).padStart(2, '0')}-${d.getFullYear()}`;
}

function formatDmySlash(d: Date): string {
  return `${String(d.getDate()).padStart(2, '0')}/${String(d.getMonth() + 1).padStart(2, '0')}/${d.getFullYear()}`;
}

function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
