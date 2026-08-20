import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { RegistrationService } from './registration.service';
import type { FieldConfig, GeoRow, IdNameRow, RegistrationPayload } from './registration.models';

/** The form's model — mirrors the legacy $scope patientObj + kinDetails core. */
export interface RegistrationForm {
  prefixId: number | null;
  firstName: string;
  middleName: string;
  lastName: string;
  genderId: number | null;
  dob: string;
  mobileNumber: string;
  email: string;
  identificationTypeId: number | null;
  identificationNumber: string;
  identificationExpiryDate: string;
  kinName: string;
  relationId: number | null;
  kinMobileNo: string;
  countryId: number | null;
  stateId: number | null;
  districtId: number | null;
  cityId: number | null;
  areaId: number | null;
  zipCode: string;
  address: string;
  paymentEntitlementId: number | null;
  registrationTypeId: number | null;
}

/** The owner's core registration requireds (the legacy's manualValidate core;
 * the field-config's set augments these when the corrected unit seeds it). */
const CORE_REQUIRED = new Set<string>(['firstName', 'genderId', 'dob', 'mobileNumber']);

const EMPTY_FORM: RegistrationForm = {
  prefixId: null,
  firstName: '',
  middleName: '',
  lastName: '',
  genderId: null,
  dob: '',
  mobileNumber: '',
  email: '',
  identificationTypeId: null,
  identificationNumber: '',
  identificationExpiryDate: '',
  kinName: '',
  relationId: null,
  kinMobileNo: '',
  countryId: null,
  stateId: null,
  districtId: null,
  cityId: null,
  areaId: null,
  zipCode: '',
  address: '',
  paymentEntitlementId: null,
  registrationTypeId: null,
};

/** OPD Patient Registration — the legacy patientRegistration screen's core
 * scope (per docs/SPEC_PATIENT_REGISTRATION_2026-08-16.md): personal +
 * identification + kin + address geography cascade + payment entitlement +
 * save. The field config DRIVES the required set. */
@Component({
  selector: 'app-registration',
  imports: [FormsModule],
  templateUrl: './registration.html',
  styleUrl: './registration.scss',
})
export class RegistrationComponent {
  private api = inject(RegistrationService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  // master lists
  readonly prefixList = signal<IdNameRow[]>([]);
  readonly genderList = signal<IdNameRow[]>([]);
  readonly identificationList = signal<IdNameRow[]>([]);
  readonly relationList = signal<IdNameRow[]>([]);
  readonly registrationTypes = signal<IdNameRow[]>([]);
  readonly paymentEntitlementList = signal<IdNameRow[]>([]);
  // geography cascade
  readonly countryList = signal<GeoRow[]>([]);
  readonly stateList = signal<GeoRow[]>([]);
  readonly districtList = signal<GeoRow[]>([]);
  readonly cityList = signal<GeoRow[]>([]);
  readonly areaList = signal<GeoRow[]>([]);

  // the field config's required set (the corrected backend drives this)
  readonly configRequired = signal<string[]>([]);
  readonly configLoaded = signal(false);

  readonly form = signal<RegistrationForm>({ ...EMPTY_FORM });
  readonly saving = signal(false);

  readonly isRequiredMissing = (name: keyof RegistrationForm): boolean => {
    const req = new Set(this.configRequired());
    if (!req.has(name)) return false;
    const v = this.form()[name];
    return v === null || v === undefined || v === '';
  };

  /** The field's required-set membership: the owner's core registration
   * requireds OR the field-config's required set (the config's empty on the
   * corrected unit — the core list keeps the asterisks visible). */
  readonly isRequired = (name: keyof RegistrationForm): boolean =>
    CORE_REQUIRED.has(name as string) || this.configRequired().includes(name as string);

  constructor() {
    this.loadMasters();
    this.loadFieldConfig();
  }

  private loadMasters(): void {
    this.api.getPrefixList(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.prefixList.set(r.listObject ?? []);
    });
    this.api.getGenderList(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.genderList.set(r.listObject ?? []);
    });
    this.api.getIdentificationList(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.identificationList.set(r.listObject ?? []);
    });
    this.api.getRelationList(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.relationList.set(r.listObject ?? []);
    });
    // the registration-type master — the dropdown's ids 1 + 4 only (id 7 is
    // the TEST-ARTIFACT the owner verified — never offered for selection).
    // The NEW-registration default is the master's own id-1 row, sourced
    // from the master, not a literal.
    this.api.getRegistrationTypes().subscribe((r) => {
      if (r.status === 'success') {
        this.registrationTypes.set((r.listObject ?? []).filter((row) => row.id !== 7));
        const def = (r.listObject ?? []).find((row) => row.id === 1);
        if (def && this.form().registrationTypeId == null) {
          this.form.update((f) => ({ ...f, registrationTypeId: def.id }));
        }
      }
    });
    this.api.getPaymentEntitlementList(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.paymentEntitlementList.set(r.listObject ?? []);
    });
    this.api.getCountry(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.countryList.set(r.listObject ?? []);
    });
  }

  private loadFieldConfig(): void {
    this.api.getFieldConfig(this.orgId, this.unitId).subscribe({
      next: (r) => {
        if (r.status === 'success' && r.object) {
          const cfg = r.object as FieldConfig;
          const req = cfg.required;
          this.configRequired.set(Array.isArray(req) ? (req as string[]) : []);
        }
        this.configLoaded.set(true);
      },
      error: () => this.configLoaded.set(true),
    });
  }

  updateField<K extends keyof RegistrationForm>(key: K, value: unknown): void {
    this.form.update((f) => ({ ...f, [key]: value as RegistrationForm[K] }));
  }

  // geography cascade — the parent's selection loads the child list
  onCountryChange(id: number | null): void {
    this.form.update((f) => ({ ...f, countryId: id, stateId: null, districtId: null, cityId: null, areaId: null }));
    this.stateList.set([]);
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
    if (id != null) {
      this.api.getStatesByCountry(id, this.orgId, this.unitId).subscribe((r) => {
        if (r.status === 'success') this.stateList.set(r.listObject ?? []);
      });
    }
  }

  onStateChange(id: number | null): void {
    this.form.update((f) => ({ ...f, stateId: id, districtId: null, cityId: null, areaId: null }));
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
    if (id != null) {
      this.api.getDistrictsByState(id, this.orgId, this.unitId).subscribe((r) => {
        if (r.status === 'success') this.districtList.set(r.listObject ?? []);
      });
    }
  }

  onDistrictChange(id: number | null): void {
    this.form.update((f) => ({ ...f, districtId: id, cityId: null, areaId: null }));
    this.cityList.set([]);
    this.areaList.set([]);
    if (id != null) {
      this.api.getCitiesByDistrict(id, this.orgId, this.unitId).subscribe((r) => {
        if (r.status === 'success') this.cityList.set(r.listObject ?? []);
      });
    }
  }

  onCityChange(id: number | null): void {
    this.form.update((f) => ({ ...f, cityId: id, areaId: null }));
    this.areaList.set([]);
    if (id != null) {
      this.api.getAreasByCity(id, this.orgId, this.unitId).subscribe((r) => {
        if (r.status === 'success') this.areaList.set(r.listObject ?? []);
      });
    }
  }

  save(): void {
    const req = new Set(this.configRequired());
    const f = this.form();
    const missing = [...req].filter((name) => {
      const v = f[name as keyof RegistrationForm];
      return v === null || v === undefined || v === '';
    });
    if (missing.length > 0) {
      this.notify.warning(`Required fields missing: ${missing.join(', ')}`);
      return;
    }
    const now = formatLegacyNow();
    const payload: RegistrationPayload = {
      patientId: 0,
      // the form's value — the master-sourced default on new; a loaded
      // patient's own value passes back unchanged (the screen is new-only
      // today — the load path, when added, flows through the same field).
      registrationTypeId: f.registrationTypeId ?? this.registrationTypes()[0]?.id,
      organizationId: this.orgId,
      unitId: this.unitId,
      prefixId: f.prefixId,
      firstName: f.firstName,
      middleName: f.middleName || null,
      lastName: f.lastName,
      genderId: f.genderId,
      dob: formatDob(f.dob),
      mobileNumber: f.mobileNumber || null,
      countryCallingCode: null,
      email: f.email || null,
      identificationTypeId: f.identificationTypeId,
      identificationNumber: f.identificationNumber || null,
      identificationExpiryDate: formatDob(f.identificationExpiryDate),
      address: f.address || null,
      countryId: f.countryId,
      stateId: f.stateId,
      districtId: f.districtId,
      cityId: f.cityId,
      areaId: f.areaId,
      zipCode: f.zipCode || null,
      kinDetailsDto: {
        organizationId: this.orgId,
        unitId: this.unitId,
        kinName: f.kinName || null,
        relationId: f.relationId,
        mobileNo: f.kinMobileNo || null,
      },
      status: 'A',
      createdBy: this.userId,
      updatedBy: this.userId,
      createdDate: now,
      updatedDate: now,
    };
    this.saving.set(true);
    this.api.save(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') {
          this.notify.warning('Patient registered successfully');
        } else {
          this.notify.warning(r.message ?? 'Registration failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Registration failed — try again');
      },
    });
  }
}

/** The legacy audit-time format (dd-M-yyyy HH:mm:ss — the dozer mapping's
 * literal at OPD/dozer-bean-mappings.xml:16). M is the NUMERIC month — the
 * month-name array ('Aug') failed the SimpleDateFormat parse with the dozer
 * 500 'Unable to parse source object using specified date format'. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

/** The dozer String-date convention: dd/MM/yyyy form input -> 'dd-MM-yyyy
 * HH:mm:ss' with 00:00:00 appended (the parse REQUIRES the time component —
 * the owner's curl proof: a date-only dob did not save). */
function formatDob(value: string): string | null {
  const m = /^(\d{1,2})\/(\d{1,2})\/(\d{4})$/.exec((value || '').trim());
  if (!m) return value || null;
  return `${m[1].padStart(2, '0')}-${m[2].padStart(2, '0')}-${m[3]} 00:00:00`;
}
