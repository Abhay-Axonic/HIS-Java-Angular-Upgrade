import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AmbulanceServiceProviderMasterService } from './ambulance-service-provider-master.service';
import {
  AmbulanceTariffOption,
  AreaOption,
  CityOption,
  CountryOption,
  DistrictOption,
  ServiceProviderForm,
  ServiceProviderRow,
  StateOption,
  emptyServiceProviderForm,
} from './ambulance-service-provider-master.models';

const OFFSET = 0;
const PAGE_SIZE = 10;

/**
 * Parity rebuild of
 * views/master/UnitMaster/ambulanceService/ambulance-Service-Provider-Master.html +
 * resources/assets/js/controller/lis/global/ambulanceServiceProviderController.js
 * (`ambulanceServiceProviderController`, no `controllerAs`). Legacy state
 * `ambulanceServiceProviderMaster` (app.js:19699-19726), moduleId 815 --
 * gated at the ROUTE level via `moduleAccessGuard` (`data: { moduleId: 815 }`),
 * not in this component, matching ambulance-master/ambulance-type-master.
 *
 * Confirmed real, working CRUD: save (ctrl:300-378), update (ctrl:515-577),
 * edit-populate `showUpdateBtn` (ctrl:428-471), status toggle `updateStatus`
 * (ctrl:474-512) -- all wired to real endpoints, see
 * ambulance-service-provider-master.service.ts. 19 form fields, the largest
 * of this codebase's "simple master" screens.
 *
 * **SEVERITY-ADJACENT, FAITHFULLY REPRODUCED per this ticket's explicit
 * parity decision -- pagination is completely dead:** `initServiceProviderMasterList`
 * (ctrl:260-288) has no count-fetch call anywhere (confirmed via full-file
 * read -- there is no `GETSERVICEPROVIDERCOUNT`/`getServiceProviderCount`
 * call in this controller at all, unlike its sibling
 * `modalityTypeMasterController`'s `initStateMasterList`), and never calls
 * `setPage(...)` either, so `$scope.pager` stays `{}` forever and the view's
 * `ng-if="pager.pages.length"` pagination footer never renders. Every load
 * always fetches exactly offset 0 / 10 records
 * (`$scope.offset`/`$scope.noOfRecordsPerPage` are never reassigned
 * anywhere), and there is no way to reach any row beyond the first page.
 * Reproduced verbatim: `loadList()` always fetches `OFFSET`/`PAGE_SIZE`
 * (0/10), no count call is ever made, and there is no pagination UI in the
 * template at all -- matching this codebase's `ambulance-master.ts`
 * precedent for the identical class of bug.
 *
 * **Faithfully reproduced form-label bug (this ticket's explicit parity
 * decision):** the (disabled, tariff-auto-computed) `serviceProviderAdditionalMileage`
 * field and the real editable `serviceProviderAdditionalMileageRate` field
 * are BOTH labeled "Additional Mileage Rate Per Km" in legacy
 * (view:278/294) -- the table's own column headers correctly distinguish
 * them ("Additional Mileage" vs "Additional Mileage Rate Per Km",
 * view:373-377). A real, user-visible copy-paste bug; both labels kept
 * identical here, not silently corrected.
 *
 * **Other real, faithfully-reproduced quirks/facts, not bugs in this
 * rebuild:**
 * - This controller NEVER sends `unitId` anywhere (confirmed via full-file
 *   read) -- every list/count/save/update/status/lookup call is
 *   organizationId-only, unlike its unit-scoped siblings
 *   (modality-type-master, amenity-master). Reproduced as-is.
 * - `init()` (ctrl:21-137) unconditionally preloads the FULL, UNFILTERED
 *   state/district/city master lists (`getStateMasterList`/
 *   `getDistrictMasterList`/`getCityMasterList`) alongside the country and
 *   tariff lists, before the user has picked any country at all -- these
 *   are only narrowed to the real cascade (`getActiveStateListByCountryId`
 *   etc.) once the user actually changes a parent select. The Area list has
 *   NO such unfiltered preload anywhere in `init()` -- it starts genuinely
 *   empty and is only ever populated by `getAreaByCity` (fired from the
 *   City select's own `ng-change`). Reproduced verbatim via
 *   `refreshDropdowns()` (country/tariff/state/district/city, no area) plus
 *   the 4 per-field cascade handlers below.
 * - `showUpdateBtn` (ctrl:428-471) assigns all 19 fields directly from the
 *   fetched record but NEVER re-triggers any of the 4 cascade fetches --
 *   so editing an existing row can show an EMPTY Area dropdown (and
 *   State/District/City dropdowns that only happen to contain the right
 *   option because they were unfiltered-preloaded, per the point above)
 *   even though the record has a real area value, until the user manually
 *   re-touches the City select. Reproduced verbatim: `edit()` sets the
 *   form's raw field values only, with no cascade re-fetch of its own.
 * - Save opens a real confirm modal ("Are you sure you want to Save?", the
 *   shared `<modal>`/`PopupOkBtn`/`popUpFlag` dance, ctrl:300-378) before
 *   the create POST fires; `update()` (ctrl:515-577) posts immediately, no
 *   confirm step -- same asymmetry as every other rebuilt sibling.
 * - On success, both save and update call `$scope.init()` again (ctrl:359,
 *   565) -- re-fetching country/tariff/state/district/city AND resetting
 *   `$scope.serviceProvider` back to `{}` (create mode) -- before the list
 *   is reloaded; on failure that full reset/refetch is skipped, but the
 *   list reload (`initServiceProviderMasterList`) always runs regardless
 *   of success/failure. Reproduced via `resetFormAndDropdowns()`
 *   (success-only) + `loadList()` (unconditional).
 * - The search box (view:332, `ng-model="searchKeyword"`) DOES work in
 *   legacy, via a bare Angular `filter:searchKeyword` over the ALREADY
 *   FETCHED page of rows (view:387) -- a generic, all-fields substring
 *   filter, not a field-specific one. Reproduced the same way this
 *   codebase's `qms-department-master.ts` models the identical bare-filter
 *   pattern (`Object.values(row).some(...)`), unlike its sibling
 *   `modalityTypeMaster`'s completely dead/unbound search box.
 * - `updateStatus` (ctrl:474-512) never reverts the checkbox on a failed
 *   POST -- the toggle is applied optimistically and stays applied no
 *   matter what the server returns, same as every status-toggle in this
 *   codebase.
 * - `showUpdateBtn`'s failure branch uses a raw `window.alert("Error!!")`,
 *   not `growl` -- same convention already established in
 *   `ambulance-master.ts`/`modality-type-master.ts`'s `edit()`. Reproduced
 *   verbatim.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared
 *   `<modal>` directive's `cancel()` crash (`commonPopUpDirectives.js`
 *   hardcodes `ng-click="cancel(); stopLoader();"`, and
 *   `ambulanceServiceProviderController.js` never defines `$scope.cancel`)
 *   -- a working close is an accepted standing improvement, not a parity
 *   gap.
 * - Contract dates use the established ISO (`yyyy-MM-dd`) `<input
 *   type="date">` convention for legacy's `uib-datepicker-popup` fields
 *   (see `asset-issue-acceptance.ts`), converted to legacy's
 *   `DD-MM-YYYY HH:mm:ss` wire format at save/update time via
 *   `toLegacyDateTime()`, and parsed back from that same wire format on
 *   `edit()` via `fromLegacyDateToIso()`.
 */
@Component({
  selector: 'app-ambulance-service-provider-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ambulance-service-provider-master.html',
  styleUrl: './ambulance-service-provider-master.scss',
})
export class AmbulanceServiceProviderMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AmbulanceServiceProviderMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists (mirrors $scope.countryList/stateList/
  // districtList/cityList/areaList/ambulanceTypeList) ----
  countryOptions = signal<CountryOption[]>([]);
  stateOptions = signal<StateOption[]>([]);
  districtOptions = signal<DistrictOption[]>([]);
  cityOptions = signal<CityOption[]>([]);
  areaOptions = signal<AreaOption[]>([]); // starts empty -- see class doc comment
  tariffOptions = signal<AmbulanceTariffOption[]>([]);

  // ---- form state (mirrors $scope.serviceProvider) ----
  form = signal<ServiceProviderForm>(emptyServiceProviderForm());
  saveMode = signal(true); // true = saveBtnFlag (create); false = updateBtnFlag (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + search (NO pagination UI on this screen -- see class doc) ----
  rows = signal<ServiceProviderRow[]>([]);
  search = signal('');

  // Legacy quirk preserved: a bare, generic `filter:searchKeyword` over the
  // already-fetched page (see class doc comment), not a field-specific one.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const list = this.rows();
    if (!keyword) return list;
    return list.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.refreshDropdowns();
    this.loadList();
  }

  // mirrors init()'s dropdown fetches (ctrl:73-135) -- country, tariff, and
  // the UNFILTERED state/district/city master lists. No area fetch here --
  // see class doc comment.
  private refreshDropdowns(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryOptions.set(res.listObject ?? []);
      },
    });
    this.api.getAmbulanceTariffActiveList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.tariffOptions.set(res.listObject ?? []);
      },
    });
    this.api.getStateMasterList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateOptions.set(res.listObject ?? []);
      },
    });
    this.api.getDistrictMasterList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtOptions.set(res.listObject ?? []);
      },
    });
    this.api.getCityMasterList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initServiceProviderMasterList (ctrl:260-288) -- ALWAYS offset
  // 0 / 10 records, no count call. See class doc comment for the
  // confirmed-dead-pagination bug this reproduces.
  private loadList(): void {
    this.api.getServiceProviderList(this.organizationId, OFFSET, PAGE_SIZE).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
  }

  updateField<K extends keyof ServiceProviderForm>(field: K, value: ServiceProviderForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- geo cascade (mirrors getStateByCountry/getDistrictByState/
  // getCityByDistrict/getAreaByCity, ctrl:140-227) ----
  onCountryChange(countryId: string): void {
    this.updateField('serviceProviderCountry', countryId);
    this.api.getStateListByCountryId(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateOptions.set(res.listObject ?? []);
      },
    });
  }

  onStateChange(value: string): void {
    const stateId = Number(value);
    this.updateField('serviceProviderState', stateId);
    this.api.getDistrictListByStateId(stateId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtOptions.set(res.listObject ?? []);
      },
    });
  }

  onDistrictChange(value: string): void {
    const districtId = Number(value);
    this.updateField('serviceProviderDistrict', districtId);
    this.api.getCityListByDistrictId(districtId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityOptions.set(res.listObject ?? []);
      },
    });
  }

  onCityChange(value: string): void {
    const cityId = Number(value);
    this.updateField('serviceProviderCity', cityId);
    this.api.getAreaByCityId(cityId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.areaOptions.set(res.listObject ?? []);
      },
    });
  }

  onAreaChange(value: string): void {
    this.updateField('serviceProviderArea', Number(value));
  }

  // ---- tariff auto-fill (mirrors getNameByAmbType, ctrl:229-249) ----
  onAmbulanceTariffChange(ambulanceTarrifMasterId: string): void {
    this.updateField('serviceProviderAmbulanceType', ambulanceTarrifMasterId);
    this.api.getSingleAmbulanceTarrif(ambulanceTarrifMasterId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const d = res.listObject[0];
        this.form.update((f) => ({
          ...f,
          serviceProviderPersonName: d.ambulanceOutSrcVender,
          serviceProviderFirstDistance: d.ambulanceFirstDist,
          serviceProviderFirstDistanceRate: d.ambulanceFirstDistRate,
          serviceProviderAdditionalMileage: d.ambulanceAddMileage,
          serviceProviderAdditionalMileageRate: d.ambulanceAddMileageRate,
        }));
      },
    });
  }

  // ---- required-field validation (mirrors serviceProviderForm.$valid --
  // every one of the 19 fields carries a `required` attribute in legacy) ----
  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(
      f.serviceProviderName.trim() &&
      f.serviceProviderAddress.trim() &&
      f.serviceProviderCountry.trim() &&
      f.serviceProviderState !== '' &&
      f.serviceProviderDistrict !== '' &&
      f.serviceProviderCity !== '' &&
      f.serviceProviderArea !== '' &&
      f.serviceProviderZipCode.trim() &&
      f.serviceProviderAmbulanceType.trim() &&
      f.serviceProviderPersonName.trim() &&
      f.serviceProviderPhoneNo.trim() &&
      f.serviceProviderCellNo.trim() &&
      f.serviceProviderEmailAddress.trim() &&
      f.serviceProviderContractFromDate.trim() &&
      f.serviceProviderContractToDate.trim() &&
      f.serviceProviderFirstDistance.trim() &&
      f.serviceProviderFirstDistanceRate.trim() &&
      f.serviceProviderAdditionalMileage.trim() &&
      f.serviceProviderAdditionalMileageRate.trim()
    );
  }

  // ---- save / confirm-modal (mirrors saveServiceProvider's popUpFlag
  // confirm dance, ctrl:300-378) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        serviceProviderName: f.serviceProviderName,
        serviceProviderAddress: f.serviceProviderAddress,
        serviceProviderCountry: f.serviceProviderCountry,
        serviceProviderState: f.serviceProviderState,
        serviceProviderDistrict: f.serviceProviderDistrict,
        serviceProviderCity: f.serviceProviderCity,
        serviceProviderArea: f.serviceProviderArea,
        serviceProviderZipCode: f.serviceProviderZipCode,
        serviceProviderAmbulanceType: f.serviceProviderAmbulanceType,
        serviceProviderPersonName: f.serviceProviderPersonName,
        serviceProviderPhoneNo: f.serviceProviderPhoneNo,
        serviceProviderCellNo: f.serviceProviderCellNo,
        serviceProviderEmailAddress: f.serviceProviderEmailAddress,
        serviceProviderContractFromDate: toLegacyDateTime(f.serviceProviderContractFromDate),
        serviceProviderContractToDate: toLegacyDateTime(f.serviceProviderContractToDate),
        serviceProviderFirstDistance: f.serviceProviderFirstDistance,
        serviceProviderFirstDistanceRate: f.serviceProviderFirstDistanceRate,
        serviceProviderAdditionalMileage: f.serviceProviderAdditionalMileage,
        serviceProviderAdditionalMileageRate: f.serviceProviderAdditionalMileageRate,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Service Provider Added Successfully...!!');
            this.formSubmitted.set(false);
            this.resetFormAndDropdowns();
          } else {
            this.notify.error('Service Provider already exits...!!');
          }
          // ctrl:369-373 -- unconditional, runs on both success and failure.
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // ---- update (mirrors updateServiceProvider, ctrl:515-577 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .update({
        serviceProviderMasterId: f.serviceProviderMasterId as number,
        serviceProviderName: f.serviceProviderName,
        serviceProviderAddress: f.serviceProviderAddress,
        serviceProviderCountry: f.serviceProviderCountry,
        serviceProviderState: f.serviceProviderState,
        serviceProviderDistrict: f.serviceProviderDistrict,
        serviceProviderCity: f.serviceProviderCity,
        serviceProviderArea: f.serviceProviderArea,
        serviceProviderZipCode: f.serviceProviderZipCode,
        serviceProviderAmbulanceType: f.serviceProviderAmbulanceType,
        serviceProviderPersonName: f.serviceProviderPersonName,
        serviceProviderPhoneNo: f.serviceProviderPhoneNo,
        serviceProviderCellNo: f.serviceProviderCellNo,
        serviceProviderEmailAddress: f.serviceProviderEmailAddress,
        serviceProviderContractFromDate: toLegacyDateTime(f.serviceProviderContractFromDate),
        serviceProviderContractToDate: toLegacyDateTime(f.serviceProviderContractToDate),
        serviceProviderFirstDistance: f.serviceProviderFirstDistance,
        serviceProviderFirstDistanceRate: f.serviceProviderFirstDistanceRate,
        serviceProviderAdditionalMileage: f.serviceProviderAdditionalMileage,
        serviceProviderAdditionalMileageRate: f.serviceProviderAdditionalMileageRate,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Service Provider Updated Successfully...!!');
            this.formSubmitted.set(false);
            this.resetFormAndDropdowns();
          } else {
            this.notify.error('Service Provider already exits...!!');
          }
          // ctrl:570-574 -- unconditional, runs on both success and failure.
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // mirrors $scope.init() being re-invoked on a successful save/update
  // (ctrl:359, 565) -- resets the form to create-mode defaults AND
  // re-fetches country/tariff/state/district/city (NOT area). Only called
  // from the SUCCESS branch, matching legacy exactly.
  private resetFormAndDropdowns(): void {
    this.form.set(emptyServiceProviderForm());
    this.saveMode.set(true);
    this.refreshDropdowns();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:428-471 -- no cascade re-fetch, see class doc) ----
  edit(row: ServiceProviderRow): void {
    this.saveMode.set(false);
    this.api.getSingle(row.serviceProviderMasterId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:467 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          serviceProviderMasterId: d.serviceProviderMasterId,
          serviceProviderName: d.serviceProviderName,
          serviceProviderAddress: d.serviceProviderAddress,
          serviceProviderCountry: d.serviceProviderCountry,
          serviceProviderState: d.serviceProviderState,
          serviceProviderDistrict: d.serviceProviderDistrict,
          serviceProviderCity: d.serviceProviderCity,
          serviceProviderArea: d.serviceProviderArea,
          serviceProviderZipCode: d.serviceProviderZipCode,
          serviceProviderAmbulanceType: d.serviceProviderAmbulanceType,
          serviceProviderPersonName: d.serviceProviderPersonName,
          serviceProviderPhoneNo: d.serviceProviderPhoneNo,
          serviceProviderCellNo: d.serviceProviderCellNo,
          serviceProviderEmailAddress: d.serviceProviderEmailAddress,
          serviceProviderContractFromDate: fromLegacyDateToIso(d.serviceProviderContractFromDate),
          serviceProviderContractToDate: fromLegacyDateToIso(d.serviceProviderContractToDate),
          serviceProviderFirstDistance: d.serviceProviderFirstDistance,
          serviceProviderFirstDistanceRate: d.serviceProviderFirstDistanceRate,
          serviceProviderAdditionalMileage: d.serviceProviderAdditionalMileage,
          serviceProviderAdditionalMileageRate: d.serviceProviderAdditionalMileageRate,
        });
        // ctrl:428-471 -- no cascade re-fetch here at all, see class doc
        // comment for the resulting "Area can show empty" quirk.
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:474-512) ----
  toggleStatus(row: ServiceProviderRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) =>
      rows.map((r) => (r.serviceProviderMasterId === row.serviceProviderMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        serviceProviderMasterId: row.serviceProviderMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Service Provider Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:301-303/476-478/516-517. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}

/** `moment(dateObj).format('DD-MM-YYYY HH:mm:ss')` applied to a date built
 * from a date-only `<input type="date">` picker -- always midnight, same
 * as legacy's uib-datepicker-sourced JS Date (ctrl:320-321). See
 * `add-new-corrective-maintenance.ts`'s `toLegacyDateTime` for the
 * established precedent this follows. */
function toLegacyDateTime(iso: string): string {
  if (!iso) return '';
  const [y, m, d] = iso.split('-').map(Number);
  if (!y || !m || !d) return '';
  return formatDateTime(new Date(y, m - 1, d, 0, 0, 0));
}

/** Reverse of `toLegacyDateTime` -- parses legacy's stored
 * `DD-MM-YYYY HH:mm:ss` (or bare `DD-MM-YYYY`) back into the ISO
 * `yyyy-MM-dd` this screen's `<input type="date">` fields need, for
 * `edit()`'s populate step. */
function fromLegacyDateToIso(legacy: string): string {
  if (!legacy) return '';
  const [datePart] = legacy.split(' ');
  const [dd, mm, yyyy] = datePart.split('-');
  if (!dd || !mm || !yyyy) return '';
  return `${yyyy}-${mm.padStart(2, '0')}-${dd.padStart(2, '0')}`;
}
