import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { GeneralInsuranceMasterService } from './general-insurance-master.service';
import {
  AddressDetails,
  emptyGeneralInsuranceForm,
  emptyValidity,
  GeneralInsuranceFormState,
  GeneralInsuranceMasterRecord,
  GeneralInsuranceValidity,
  GeoOption,
  PLACEHOLDER_AREA,
  PLACEHOLDER_CITY,
  PLACEHOLDER_COUNTRY,
  PLACEHOLDER_STATE,
  SaveGeneralInsurancePayload,
  ToggleStatusPayload,
} from './general-insurance-master.models';

const EMAIL_PATTERN = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
const PHONE_PATTERN = /^\+?\d+$/;

/**
 * Parity rebuild of
 * resources/assets/js/controller/master/AssetManagenent/generalInsuranceMasterController.js
 * ("GeneralInsuranceMaster.Controller", controllerAs "vm", self-registered
 * state -- confirmed the ONLY live registration for `generalInsuranceMaster`;
 * app.js:8488-8493 has a dead, commented-out duplicate routed through the
 * generic `MasterController`, confirmed genuinely inert) +
 * views/master/GlobalMaster/AssetManagenent/Master/general-insurance-master.html.
 * moduleId 646, confirmed from the controller's own `.config($stateProvider)`
 * resolve block (ctrl:516-536).
 *
 * SEVERITY-1 WRITE-PATH BUG IN LEGACY, NOT FIXED HERE, FLAGGED FOR THE
 * ORCHESTRATING SESSION: `GeneralInsuranceRequest.java` (extends
 * `GenralInsuranceResponse`, which has a normal `zipCode` field +
 * getter/setter) overrides `getZipCode()` to unconditionally
 * `return null;` (a leftover "TODO Auto-generated method stub"). Both
 * `GeneralInsuranceMasterService.save()` (line ~121) and `.update()`
 * (line ~174) populate the entity's zipcode via
 * `generalInsuranceRequest.getZipCode()` -- which, because of the
 * override, ALWAYS returns null, regardless of what the client actually
 * sent (Jackson deserializes the incoming JSON into the field correctly
 * via the inherited setter; the service layer then reads it back out
 * through the broken getter and discards it). Net effect: the Zip Code
 * field is silently persisted as NULL on every single Create AND every
 * single Update through this real, unmodified legacy screen today, no
 * matter what the user types -- both operations report a normal success
 * growl ("General Insurance Saved Successfully" / "Insurace Company
 * Updated Successfully", the latter typo also faithfully reproduced
 * below since it's the raw server string). This is a real, deterministic,
 * always-reachable silent data-loss bug (same class as this project's
 * `hospitalAccountMaster`/`currencyMaster`/`aiButtonConfiguration`/
 * `bedMaster` precedents), not an edge case. Per this project's standing
 * policy this is faithfully reproduced here (the rebuild sends the real
 * zipCode value to the real endpoints, same as legacy's own form does --
 * a client-side fix cannot repair a server-side bug, and both apps will
 * silently drop it identically until the Java backend is fixed on its
 * own dedicated branch with owner approval). NOT fixed inline. See this
 * batch's own findings write-up for the full file:line citation trail.
 */
@Component({
  selector: 'app-general-insurance-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './general-insurance-master.html',
  styleUrl: './general-insurance-master.scss',
})
export class GeneralInsuranceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(GeneralInsuranceMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- cascade lists, each seeded with the same real placeholder object
  // legacy uses (ctrl:14-33) -- see models.ts's PLACEHOLDER_* doc comment
  // for why this isn't a native empty-value <option>. ----
  countries = signal<GeoOption[]>([PLACEHOLDER_COUNTRY]);
  states = signal<GeoOption[]>([PLACEHOLDER_STATE]);
  cities = signal<GeoOption[]>([PLACEHOLDER_CITY]);
  areas = signal<GeoOption[]>([PLACEHOLDER_AREA]);

  form = signal<GeneralInsuranceFormState>(emptyGeneralInsuranceForm());
  validity = signal<GeneralInsuranceValidity>(emptyValidity());
  isEdit = signal(false);

  list = signal<GeneralInsuranceMasterRecord[]>([]);
  searchKeyword = signal('');
  sortType = signal<keyof GeneralInsuranceMasterRecord | ''>('');
  sortReverse = signal(false);

  showSaveConfirm = signal(false);
  showAddressModal = signal(false);
  addressDetails = signal<AddressDetails>({ address: '', country: '', state: '', city: '', area: '', zipCode: '' });

  constructor() {
    this.populateDropdown();
  }

  // ---- init cascade ----
  private populateDropdown(): void {
    this.form.update((f) => ({
      ...f,
      countryName: PLACEHOLDER_COUNTRY,
      stateName: PLACEHOLDER_STATE,
      cityName: PLACEHOLDER_CITY,
      areaName: PLACEHOLDER_AREA,
    }));
    this.loader.start();
    this.api.getDropdown(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.countries.set([PLACEHOLDER_COUNTRY, ...(res.country ?? [])]);
        this.loader.stop();
        this.loadList();
      },
      // Previously missing (unlike every other network call in this file) --
      // left the global loader spinner stuck forever on any error, e.g. the
      // real, still-open VendorController.GetDropdowns 500 (see
      // PENDING-vendorcontroller-getdropdowns-severity1-schemadrift-registrationconfig).
      // A network failure should never leave the user staring at an infinite
      // spinner; matches the error-handling shape every sibling call site in
      // this file already uses.
      error: () => this.loader.stop(),
    });
  }

  private loadList(): void {
    this.loader.start();
    this.api.getList().subscribe({
      next: (res) => {
        this.list.set(res ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  /** Every text field's `ng-change` in legacy clears just that one field's
   * own `isXValid` flag (e.g. `ng-change="vm.model.isInsuranceCompanyValid
   * = false"`) -- reproduced generically here rather than one bespoke
   * handler per field. */
  updateField<K extends keyof GeneralInsuranceFormState>(
    field: K,
    value: GeneralInsuranceFormState[K],
    validityField?: keyof GeneralInsuranceValidity,
  ): void {
    this.form.update((f) => ({ ...f, [field]: value }));
    if (validityField) {
      this.validity.update((v) => ({ ...v, [validityField]: false }));
    }
  }

  // ---- geo cascade -- reproduces PopulateStates/PopulateCities/
  // PopulateAreas (ctrl:139-223) exactly, including firing the network
  // call unconditionally even for the placeholder (id-less -> treated as
  // 0) selection, and resetting the two levels below on every change. ----
  onCountryChange(country: GeoOption): void {
    this.form.update((f) => ({ ...f, countryName: country }));
    this.validity.update((v) => ({ ...v, iscountryNameValid: false }));
    this.populateStates(country, false);
  }

  private populateStates(country: GeoOption, selectedValue: boolean): void {
    const countryId = country.countryId ?? 0;
    this.form.update((f) => ({ ...f, stateName: PLACEHOLDER_STATE, cityName: PLACEHOLDER_CITY, areaName: PLACEHOLDER_AREA }));
    this.states.set([PLACEHOLDER_STATE]);
    this.cities.set([PLACEHOLDER_CITY]);
    this.areas.set([PLACEHOLDER_AREA]);

    this.loader.start();
    this.api.getStates(this.organizationId, countryId).subscribe({
      next: (res) => {
        const list = [PLACEHOLDER_STATE, ...(res ?? [])];
        this.states.set(list);
        this.loader.stop();
        if (selectedValue) {
          const match = list.find((s) => s.stateId === country.stateId);
          if (match) this.form.update((f) => ({ ...f, stateName: match }));
        }
      },
      error: () => this.loader.stop(),
    });
  }

  onStateChange(state: GeoOption): void {
    this.form.update((f) => ({ ...f, stateName: state }));
    this.validity.update((v) => ({ ...v, isstateNameValid: false }));
    this.populateCities(state, false);
  }

  private populateCities(state: GeoOption, selectedValue: boolean): void {
    const stateId = state.stateId ?? 0;
    this.form.update((f) => ({ ...f, cityName: PLACEHOLDER_CITY, areaName: PLACEHOLDER_AREA }));
    this.cities.set([PLACEHOLDER_CITY]);
    this.areas.set([PLACEHOLDER_AREA]);

    this.loader.start();
    this.api.getCities(this.organizationId, stateId).subscribe({
      next: (res) => {
        const list = [PLACEHOLDER_CITY, ...(res ?? [])];
        this.cities.set(list);
        this.loader.stop();
        if (selectedValue) {
          const match = list.find((c) => c.cityId === state.cityId);
          if (match) this.form.update((f) => ({ ...f, cityName: match }));
        }
      },
      error: () => this.loader.stop(),
    });
  }

  onCityChange(city: GeoOption): void {
    this.form.update((f) => ({ ...f, cityName: city }));
    this.validity.update((v) => ({ ...v, iscityNameValid: false }));
    this.populateAreas(city, false);
  }

  private populateAreas(city: GeoOption, selectedValue: boolean): void {
    const cityId = city.cityId ?? 0;
    this.form.update((f) => ({ ...f, areaName: PLACEHOLDER_AREA }));
    this.areas.set([PLACEHOLDER_AREA]);

    this.loader.start();
    this.api.getAreas(this.organizationId, cityId).subscribe({
      next: (res) => {
        const list = [PLACEHOLDER_AREA, ...(res ?? [])];
        this.areas.set(list);
        this.loader.stop();
        if (selectedValue) {
          const match = list.find((a) => a.areaId === city.areaId);
          if (match) this.form.update((f) => ({ ...f, areaName: match }));
        }
      },
      error: () => this.loader.stop(),
    });
  }

  onAreaChange(area: GeoOption): void {
    this.form.update((f) => ({ ...f, areaName: area }));
    this.validity.update((v) => ({ ...v, isareaNameValid: false }));
  }

  /** Reproduces the `numbersOnly` directive (resources/assets/js/directive/
   * numbers.directive.js) applied to the Zip Code input in legacy's view --
   * strips non-digit characters as the user types. `maxlength="6"` on the
   * template handles the upper bound the same way legacy's HTML does. */
  onZipCodeInput(value: string): void {
    this.form.update((f) => ({ ...f, zipCode: value.replace(/[^0-9]/g, '') }));
    this.validity.update((v) => ({ ...v, iszipCodeValid: false }));
  }

  // ---- address details view-only modal (getAddressDetails, ctrl:130-137) ----
  viewAddress(row: GeneralInsuranceMasterRecord): void {
    this.addressDetails.set({
      address: row.address,
      country: row.countryName ?? '',
      state: row.stateName ?? '',
      city: row.cityName ?? '',
      area: row.areaName ?? '',
      zipCode: row.zipCode,
    });
    this.showAddressModal.set(true);
  }

  closeAddressModal(): void {
    this.showAddressModal.set(false);
  }

  // ---- edit load (PopulateGenInsMaster, ctrl:92-128) -- fires all 3
  // cascade levels concurrently off the single details response, exactly
  // like legacy (not chained/sequenced through each other's resolution). ----
  edit(id: number): void {
    this.validity.set(emptyValidity());
    this.api.getById(id).subscribe({
      next: (res) => {
        const country = this.countries().find((c) => c.countryId === res.countryId) ?? PLACEHOLDER_COUNTRY;

        this.populateStates(res, true);
        this.populateCities(res, true);
        this.populateAreas(res, true);

        this.form.set({
          id: res.id,
          insuranceCompany: res.insuranceCompanyName,
          contactNo: res.contactNo,
          emailAddress: res.email,
          contactPersonName: res.contactPersonName,
          contactPersonNo: res.contactPersonNumber,
          address: res.address,
          zipCode: res.zipCode,
          status: res.status,
          countryName: country,
          stateName: PLACEHOLDER_STATE,
          cityName: PLACEHOLDER_CITY,
          areaName: PLACEHOLDER_AREA,
        });
        this.isEdit.set(true);
      },
    });
  }

  // ---- validation -- deliberately asymmetric between Create and Update,
  // matching legacy exactly (ValidateGenIns/ValidateGenInsForUpdate/
  // validatePhoneNo/validateContactPersonNo/ValidateEmail, ctrl:225-388):
  // BOTH run the presence check (ValidateGenIns) + the 3 format checks
  // (phone/contactPerson/email regex). Update ADDITIONALLY runs 3
  // minimum-length checks (zip>=6, contact>=10, contactPerson>=10) that
  // Create never applies -- not a bug to "fix" into symmetry, a real
  // legacy asymmetry to preserve. ----
  private validatePresence(): boolean {
    const f = this.form();
    const v: GeneralInsuranceValidity = {
      isInsuranceCompanyValid: !f.insuranceCompany,
      isContactNoValid: !f.contactNo,
      isemailValid: !f.emailAddress,
      iscontactPersonNameValid: !f.contactPersonName,
      isContactPersonNoValid: !f.contactPersonNo,
      isaddressValid: !f.address,
      iscountryNameValid: !f.countryName.countryId,
      isstateNameValid: !f.stateName.stateId,
      iscityNameValid: !f.cityName.cityId,
      isareaNameValid: !f.areaName.areaId,
      iszipCodeValid: !f.zipCode,
    };
    this.validity.set(v);
    return !Object.values(v).some(Boolean);
  }

  private validateEmailFormat(): boolean {
    if (EMAIL_PATTERN.test(this.form().emailAddress)) return true;
    this.notify.error('Enter valid Email');
    return false;
  }

  private validatePhoneNo(): boolean {
    if (PHONE_PATTERN.test(this.form().contactNo)) return true;
    this.notify.error('Enter Valid Contact No');
    return false;
  }

  private validateContactPersonNo(): boolean {
    if (PHONE_PATTERN.test(this.form().contactPersonNo)) return true;
    this.notify.error('Enter valid Contact Person No.');
    return false;
  }

  /** ValidateGenInsForUpdate, ctrl:372-388 -- Update-only length gate. */
  private validateForUpdate(): boolean {
    const f = this.form();
    if (f.zipCode.length < 6) {
      this.notify.error('Enter Valid Zip code.');
      return false;
    }
    if (f.contactNo.length < 10) {
      this.notify.error('Enter Valid Contact no.');
      return false;
    }
    if (f.contactPersonNo.length < 10) {
      this.notify.error('Enter Valid Contact Person no.');
      return false;
    }
    return true;
  }

  // ---- save/update dispatch (SaveGenInsMaster, ctrl:304-312) ----
  save(): void {
    if (!this.validatePresence()) return;
    const f = this.form();
    if (!f.id) {
      this.create();
    } else {
      this.update();
    }
  }

  private buildPayload(): SaveGeneralInsurancePayload {
    const f = this.form();
    return {
      id: f.id,
      insuranceCompanyName: f.insuranceCompany,
      contactNo: f.contactNo,
      email: f.emailAddress,
      contactPersonName: f.contactPersonName,
      contactPersonNumber: f.contactPersonNo,
      address: f.address,
      areaId: f.areaName.areaId,
      zipCode: f.zipCode,
      status: f.status,
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
    };
  }

  /** createGenInsMaster, ctrl:314-370. Legacy never calls
   * `$rootScope.startLoader()` before this POST (only `stopLoader()` on
   * error) -- a real, harmless legacy asymmetry, reproduced as-is rather
   * than "corrected" into a symmetric start/stop pair. */
  private create(): void {
    if (!this.validatePhoneNo() || !this.validateContactPersonNo() || !this.validateEmailFormat()) return;
    const f = this.form();
    const payload = this.buildPayload();
    this.api.save(payload).subscribe({
      next: (res) => {
        const insertedData: GeneralInsuranceMasterRecord = {
          id: res.id ?? 0,
          insuranceCompanyName: f.insuranceCompany,
          contactNo: f.contactNo,
          email: f.emailAddress,
          contactPersonName: f.contactPersonName,
          contactPersonNumber: f.contactPersonNo,
          address: f.address,
          zipCode: f.zipCode,
          countryName: f.countryName.countryName,
          stateName: f.stateName.stateName,
          cityName: f.cityName.cityName,
          areaName: f.areaName.areaName,
          status: true,
        };
        this.list.update((rows) => [...rows, insertedData]);
        this.clearFields();
        this.notify.success(res.message ?? 'General Insurance Saved Successfully');
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something Went Wrong');
      },
    });
  }

  /** updateGenInsMaster, ctrl:392-453. Same startLoader() omission as
   * create() above -- faithfully reproduced. Updates the matching row via
   * the list signal's `.update()` with a NEW row object, never an
   * in-place mutation of the existing row reference (docs/
   * ANGULAR_PENDING_FINDINGS.md entry 12's shared pattern -- legacy's own
   * AngularJS $scope digest tolerates the in-place mutation it actually
   * does here; Angular signals do not, so this is the correct-by-
   * construction equivalent, not a behavioral change). */
  private update(): void {
    if (!this.validateForUpdate() || !this.validatePhoneNo() || !this.validateContactPersonNo() || !this.validateEmailFormat()) return;
    const f = this.form();
    const id = f.id as number;
    const payload = this.buildPayload();
    this.api.update(id, payload).subscribe({
      next: (res) => {
        this.list.update((rows) =>
          rows.map((r) =>
            r.id === id
              ? {
                  ...r,
                  insuranceCompanyName: payload.insuranceCompanyName,
                  contactNo: payload.contactNo,
                  email: payload.email,
                  contactPersonName: payload.contactPersonName,
                  contactPersonNumber: payload.contactPersonNumber,
                  address: payload.address,
                  countryName: f.countryName.countryName,
                  stateName: f.stateName.stateName,
                  cityName: f.cityName.cityName,
                  areaName: f.areaName.areaName,
                  zipCode: payload.zipCode,
                  status: payload.status ?? r.status,
                }
              : r,
          ),
        );
        this.clearFields();
        this.notify.success(res.message ?? 'Insurace Company Updated Successfully');
        this.isEdit.set(false);
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something Went Wrong');
      },
    });
  }

  /** clearFields, ctrl:482-502 -- resets the form and the state/city/area
   * lists back to just their placeholder (Countries itself is untouched). */
  private clearFields(): void {
    this.form.set(emptyGeneralInsuranceForm());
    this.states.set([PLACEHOLDER_STATE]);
    this.cities.set([PLACEHOLDER_CITY]);
    this.areas.set([PLACEHOLDER_AREA]);
    this.validity.set(emptyValidity());
  }

  /**
   * ToggleStatus, ctrl:455-480. Uses the shared `<app-status-toggle>`
   * component's correct-by-construction contract (never mutate
   * optimistically; only commit the new value to `list` on a confirmed
   * success -- see shared/status-toggle/status-toggle.ts's own doc
   * comment). This is a DISCLOSED, standing shared-component improvement
   * over legacy's own toggle wiring (docs/ANGULAR_REBUILD_PLAYBOOK.md's
   * "shared component/directive behavior improvements" policy, entry 55
   * precedent) -- NOT a parity violation:
   *
   * Legacy's checkbox two-way-binds `ng-model="insMaster.status"`
   * directly, so the switch visually flips the INSTANT the user clicks,
   * before any network response. Its error handler only reverts the
   * switch (`insMaster.status = true`) on `err.status == 406` -- but
   * `ServiceException` (the only exception type this endpoint's Update
   * path can throw, wrapping the service's `CustomeException`) is mapped
   * to HTTP 500, not 406 (`BillingExceptionController.java:23-27`), and
   * the `CustomeException` itself is only ever thrown when
   * `generalInsuranceRequest.getStatus() == null` (`GeneralInsuranceMasterService.
   * java:183-194`) -- which never happens, since both this screen's own
   * call sites (`updateGenInsMaster`/`ToggleStatus`) always send a real
   * boolean `status`. So on real, current legacy, this 406-revert branch
   * is dead code: ANY toggle failure (network error, a genuine 500, etc.)
   * leaves the switch showing the new, unconfirmed state indefinitely,
   * with only a generic "Something Went Wrong" growl and no visual
   * correction, until the list happens to reload. The rebuild's
   * `<app-status-toggle>` structurally cannot regress into this, because
   * it never mutates before success. The dead 406-specific branch itself
   * is still reproduced below (same message source, same condition
   * shape) since it costs nothing and documents legacy's real intent,
   * even though it is not reachable through this screen's own code paths.
   */
  toggleStatus(row: GeneralInsuranceMasterRecord): void {
    const nextStatus = !row.status;
    const payload: ToggleStatusPayload = {
      status: nextStatus,
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
    };
    this.loader.start();
    this.api.toggleStatus(row.id, payload).subscribe({
      next: () => {
        this.loader.stop();
        this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
        this.notify.success('Status changed successfully');
      },
      error: (err) => {
        this.loader.stop();
        // Dead on real legacy today (see class doc comment) -- reproduced
        // for structural fidelity only.
        if (err?.status === 406) {
          this.notify.error(err?.error?.message ?? 'Something Went Wrong');
        } else {
          this.notify.error('Something Went Wrong');
        }
      },
    });
  }

  // ---- local filter + sort -- the view has NO pagination at all
  // (a fixed-height scrolling container instead, see the legacy view's
  // `.table-container-body{height:280px;overflow-y:scroll}`) and search
  // is a plain client-side Angular `filter:search` over the whole row,
  // not a backend call -- reproduced as a simple substring match here. ----
  filteredList(): GeneralInsuranceMasterRecord[] {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const sortKey = this.sortType();
    let rows = this.list();
    if (keyword) {
      rows = rows.filter((row) => JSON.stringify(row).toLowerCase().includes(keyword));
    }
    if (sortKey) {
      const reverse = this.sortReverse();
      rows = [...rows].sort((a, b) => {
        const av = String(a[sortKey] ?? '');
        const bv = String(b[sortKey] ?? '');
        const cmp = av.localeCompare(bv);
        return reverse ? -cmp : cmp;
      });
    }
    return rows;
  }

  setSort(key: keyof GeneralInsuranceMasterRecord): void {
    if (this.sortType() === key) {
      this.sortReverse.set(!this.sortReverse());
    } else {
      this.sortType.set(key);
      this.sortReverse.set(false);
    }
  }
}
