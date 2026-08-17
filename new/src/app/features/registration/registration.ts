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
      organizationId: this.orgId,
      unitId: this.unitId,
      prefixId: f.prefixId,
      firstName: f.firstName,
      middleName: f.middleName || null,
      lastName: f.lastName,
      genderId: f.genderId,
      dob: f.dob || null,
      mobileNumber: f.mobileNumber || null,
      countryCallingCode: null,
      email: f.email || null,
      identificationTypeId: f.identificationTypeId,
      identificationNumber: f.identificationNumber || null,
      identificationExpiryDate: f.identificationExpiryDate || null,
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

/** The legacy audit-time format (dd-M-yyyy HH:mm:ss) — the same convention as
 * insurance-master / patient-type-master. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = [
    'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec',
  ];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
