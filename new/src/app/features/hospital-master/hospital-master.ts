import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe, DecimalPipe } from '@angular/common';
import { HttpEventType } from '@angular/common/http';
import { debounceTime, Subject, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UploadProgressService } from '../../core/upload-progress.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { HospitalMasterService } from './hospital-master.service';
import {
  AreaSearchResult,
  GeoOption,
  HospitalMasterRecord,
  HospitalMasterRow,
  ManualSubscriptionForm,
  OrganizationOption,
  SubscriptionConfig,
  SubscriptionHistoryRow,
  SubscriptionTypeOption,
  TimezoneOption,
  emptyHospitalMasterRecord,
  emptySubscriptionConfig,
} from './hospital-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const EMAIL_PATTERN = /^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;

/**
 * Parity rebuild of views/master/GlobalMaster/hospital-master.html +
 * resources/assets/js/controller/master/hospitalMasterController.js
 * (`hospitalMasterController`, no `controllerAs`, wired via app.js's
 * central `$stateProvider` at app.js:7852 -- NOT self-registered).
 * moduleId 371, requireSuperUser: true (the resolve block checks BOTH
 * module 371 AND `cookieObject.isSuperUser == 'Y'`, app.js:7857-7873 --
 * this is a genuinely DIFFERENT, stricter gate than this batch's sibling
 * `roomMaster`, which has no requireSuperUser check at all).
 *
 * **NEW instance of the recurring missing-`$state`-DI crash bug**
 * (docs/ANGULAR_PENDING_FINDINGS.md entries 8/10, previously found in
 * `organizationController`/`assetCategoryMasterController`/
 * `implantMasterController` -- NOT previously catalogued for this
 * controller): `hospitalMasterController`'s own DI list (ctrl:4-19) never
 * includes `$state`, yet its missing-cookie guard (ctrl:71-75) calls
 * `$state.go('login')` -- a live, real `ReferenceError` on session expiry
 * in legacy today, confirmed via full-file grep. Not applicable to this
 * rebuild's architecture: `moduleAccessGuard` already redirects at the
 * ROUTE level before this component ever mounts if the session cookie is
 * missing, so there's no equivalent "cookie disappears mid-component" path
 * to defend against here -- the safe pattern the playbook calls for is
 * structural, not a specific line of code to add.
 *
 * **Does NOT use the shared `<modal>`/`<dietCssdPopUp>`/`<shortcut>`
 * directives** -- confirmed via grep, this view has zero `<modal`
 * occurrences; its two popups (License Details, Subscription) are plain
 * Bootstrap `.modal` markup toggled via `document.getElementById(...)
 * .style.display`, not the shared prototypal-scope directive entry 55
 * covers. No blast-radius applicability here.
 *
 * **License-details sub-feature is entirely dead, not rebuilt** -- BOTH
 * the add/edit license-details form (view:399-440) AND the view-trigger
 * icon that would open `#licenseDetailsModal` (view:550) are wrapped in
 * HTML comments in the live view. `listLicenceDetailsMaster`/
 * `addLicenceDetails`/`removeLicenceDetails`/`licenceDetailsEdit`/
 * `licenceDetails` are all real controller code with zero reachable UI --
 * unlike this codebase's sibling `organization-master`, which has the same
 * dead-commented sub-table and made the identical call. `getLicenceTypeList`
 * is still fired unconditionally on load purely for golden-trace network
 * parity (see the service's header comment), matching that sibling exactly.
 *
 * **`editMode` never resets after the first edit -- a genuine, real
 * legacy bug, faithfully preserved.** `$scope.editMode` (ctrl:770/780) is
 * set `true` the first time `editUnit()` runs and is NEVER set back to
 * `false` anywhere -- not in `saveUnit()`'s or `updateUnit()`'s success
 * handlers (which reset `saveBtnFlag`/`updateBtnFlag`/`updateScope` but not
 * `editMode`), not anywhere else in the file (confirmed via full-file
 * grep). Since the Subscription button is gated on
 * `ng-if="editMode && isSuperAdmin==='Y'"` (view:379-382), it stays visible
 * for the rest of the session after the first edit click -- even once the
 * form has since reverted to "create new" (Save) mode after a successful
 * save/update, when `$scope.unit.unitId` has already been reset to `""` by
 * `initOrganizationMaster()`. Clicking Subscription in that state opens the
 * popup and fetches subscription config/history for a blank `unitId`.
 * Reproduced verbatim below: `editMode` is a signal set `true` in `edit()`
 * and never reset by `resetForm()`.
 *
 * **Save has no mobile-number-length or email-format validation; Update
 * does** (ctrl:684-705 vs. 872-891) -- the exact same asymmetry already
 * disclosed on this codebase's sibling `organization-master`. Reproduced
 * verbatim: `save()` skips both checks, `update()` runs both.
 *
 * **A dead `$scope.init()` call in the Save+logo-upload success path is
 * NOT reproduced (disclosed simplification).** `saveUnit()`'s nested
 * logo-image-upload success callback (ctrl:730-745) ends with a call to
 * `$scope.init()`, a function never defined anywhere in this controller
 * (confirmed via grep -- `initOrganizationMaster`/`initOrganizationList`
 * exist, a generic `init` does not). Because this sits inside an AngularJS
 * `$q` `.then()` callback with no `.catch()`, the resulting exception is
 * swallowed as a silent rejected promise, not a page-breaking crash --
 * by the time it fires, the outer `saveUnit()` success handler has already
 * run synchronously (growl, form reset, list reload all already visible to
 * the user). Unlike this batch's `roomMaster` findings and this codebase's
 * `weekend-master` (whose dead search handler IS reproduced, since it's a
 * real interactive per-keystroke crash the user would notice), this one has
 * no user-visible effect at all -- console-only, matching the same
 * "disclosed simplification, not a behavior change to any real data path"
 * reasoning weekend-master's own class comment already uses for its
 * `initTime()` dead call.
 */
@Component({
  selector: 'app-hospital-master',
  imports: [FormsModule, StatusToggle, DatePipe, DecimalPipe],
  templateUrl: './hospital-master.html',
  styleUrl: './hospital-master.scss',
})
export class HospitalMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(HospitalMasterService);
  readonly uploadProgress = inject(UploadProgressService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- reference data ----
  organizationList = signal<OrganizationOption[]>([]);
  countryList = signal<GeoOption[]>([]);
  stateList = signal<GeoOption[]>([]);
  districtList = signal<GeoOption[]>([]);
  cityList = signal<GeoOption[]>([]);
  timezoneList = signal<TimezoneOption[]>([]);
  isSuperAdmin = signal<string | undefined>(undefined);
  /** See hospital-master.service.ts's `getAdminPrivileges()` doc comment --
   * only included on the SAVE (create-new) payload, never update, matching
   * legacy exactly (editUnit() fully replaces $scope.unit with the fetched
   * record before update, discarding whatever previlageString the blank
   * template held). */
  private adminPrivileges: unknown = null;

  // ---- area typeahead (ctrl:1069-1095) ----
  private readonly areaSearch$ = new Subject<string>();
  areaResults = signal<AreaSearchResult[]>([]);

  // ---- form state ----
  record = signal<HospitalMasterRecord>(emptyHospitalMasterRecord());
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  editMode = signal(false); // deliberately sticky -- see class comment
  formSubmitted = signal(false);
  selectedLogo = signal<File | null>(null);
  imageSrc = signal('');

  // ---- list + pagination ----
  list = signal<HospitalMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) =>
      [r.organizationName, r.unitCode, r.unitName, r.registrationNumber, r.unitAddress, r.countryName, r.stateName, r.cityName, r.unitContact, r.unitEmailId]
        .filter(Boolean)
        .some((f) => f!.toLowerCase().includes(keyword)),
    );
  });

  // ---- subscription sub-feature ----
  showSubscriptionModal = signal(false);
  subscriptionTypeList = signal<SubscriptionTypeOption[]>([]);
  subscriptionConfig = signal<SubscriptionConfig>(emptySubscriptionConfig());
  subscriptionHistory = signal<SubscriptionHistoryRow[]>([]);
  showManualSubscriptionModal = signal(false);
  manualSubscription = signal<ManualSubscriptionForm | null>(null);
  canCreateSubscription = computed(() => {
    const id = this.subscriptionConfig().subscriptionConfigId;
    return id != null && id > 0;
  });

  constructor() {
    this.api.getAdminPrivileges().subscribe({
      next: (data) => (this.adminPrivileges = data),
    });
    this.api.getLicenceTypeList(this.organizationId).subscribe();
    this.api.getSuperAdminUser(this.organizationId, this.unitId, this.userId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.isSuperAdmin.set(res.object?.isSuperUser);
      },
    });
    this.api.getTimezones().subscribe({
      next: (res) => {
        if (res.status === 'success') this.timezoneList.set(res.listObject ?? []);
      },
    });
    this.api.getCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
    this.api.getOrganizationList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.organizationList.set(res.listObject ?? []);
      },
    });
    this.api.getSubscriptionTypeList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.subscriptionTypeList.set(res.listObject ?? []);
      },
    });
    this.loadList();

    this.areaSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.searchAreaByCity(this.record().unitCityId as number, term)),
      )
      .subscribe({
        next: (res) => this.areaResults.set(res.status === 'success' ? res.listObject ?? [] : []),
      });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getUnitList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getUnitCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof HospitalMasterRecord>(field: K, value: HospitalMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- organization change (mirrors setTimeZoneOrganizationWise, ctrl:568-576) ----
  onOrganizationChange(organizationId: number): void {
    this.updateField('organizationId', organizationId);
    const org = this.organizationList().find((o) => o.organizationId === organizationId);
    if (org?.unitTimeZone) this.updateField('unitTimeZone', org.unitTimeZone);
  }

  // ---- geo cascade (mirrors getStateMasterList/getDistrictByState/getCityByDistrict) ----
  onCountryChange(countryId: number): void {
    this.updateField('unitCountryId', countryId);
    this.stateList.set([]);
    this.districtList.set([]);
    this.cityList.set([]);
    if (!countryId) return;
    this.api.getStatesByCountry(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateList.set(res.listObject ?? []);
      },
    });
  }

  onStateChange(stateId: number): void {
    this.updateField('unitStateId', stateId);
    this.districtList.set([]);
    this.cityList.set([]);
    if (!stateId) return;
    this.api.getDistrictsByState(stateId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtList.set(res.listObject ?? []);
      },
    });
  }

  onDistrictChange(districtId: number): void {
    this.updateField('unitDistrictId', districtId);
    this.cityList.set([]);
    if (!districtId) return;
    this.api.getCitiesByDistrict(districtId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityList.set(res.listObject ?? []);
      },
    });
  }

  onCityChange(cityId: number): void {
    this.updateField('unitCityId', cityId);
  }

  // ---- area typeahead (mirrors searchAreaByCityId/addAreaById, ctrl:1063-1095) ----
  onAreaSearchChange(term: string): void {
    this.updateField('areaName', term);
    this.updateField('unitAreaId', '');
    if (this.record().unitCityId) this.areaSearch$.next(term);
  }

  selectArea(area: AreaSearchResult): void {
    this.updateField('unitAreaId', area.areaId);
    this.updateField('areaName', area.areaName);
    if (area.postCode) this.updateField('postCode', area.postCode);
    this.areaResults.set([]);
  }

  // ---- logo upload ----
  onLogoSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    if (file) {
      const ext = file.name.split('.').pop()?.toLowerCase() ?? '';
      if (!['jpg', 'jpeg', 'png'].includes(ext)) {
        this.notify.warning('You can only upload *.jpg, *.jpeg, *.png');
        input.value = '';
        this.selectedLogo.set(null);
        return;
      }
    }
    this.selectedLogo.set(file);
  }

  private uploadLogoIfSelected(unitId: number, onDone: () => void): void {
    const file = this.selectedLogo();
    if (!file) {
      onDone();
      return;
    }
    this.uploadProgress.reset();
    this.api.uploadLogo(unitId, file).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.UploadProgress && event.total) {
          this.uploadProgress.report(event.loaded, event.total);
        } else if (event.type === HttpEventType.Response) {
          this.uploadProgress.reset();
          this.imageSrc.set('');
          onDone();
        }
      },
    });
  }

  isRequiredMissing(): boolean {
    if (!this.formSubmitted()) return false;
    const r = this.record();
    return !(
      r.organizationId !== '' &&
      r.unitCode.trim() &&
      r.unitName.trim() &&
      r.registrationNumber.trim() &&
      r.unitAddress.trim() &&
      r.unitCountryId !== '' &&
      r.unitStateId !== '' &&
      r.unitDistrictId !== '' &&
      r.unitCityId !== '' &&
      r.unitAreaId !== '' &&
      r.postCode.trim() &&
      r.unitContact.trim() &&
      r.unitEmailId.trim() &&
      r.unitTimeZone
    );
  }

  // ---- save (mirrors saveUnit, ctrl:675-768) -- NO mobile/email format
  // checks here, matching legacy's own asymmetry with update() below. ----
  save(): void {
    this.formSubmitted.set(true);
    if (this.isRequiredMissing()) return;

    const payload: HospitalMasterRecord = {
      ...this.record(),
      ...(this.adminPrivileges != null ? { previlageString: JSON.stringify(this.adminPrivileges) } : {}),
    };
    this.api.save(payload).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error(res.message ?? 'Error!');
          return;
        }
        const newUnitId = res.object?.unitId as number;
        this.uploadLogoIfSelected(newUnitId, () => {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
          this.loadList();
        });
      },
      error: () => this.notify.error('Error!'),
    });
  }

  // ---- update (mirrors updateUnit, ctrl:853-960) -- HAS mobile-length and
  // email-format checks, unlike save() above. ----
  update(): void {
    this.formSubmitted.set(true);
    if (this.isRequiredMissing()) return;
    const r = this.record();

    // ctrl:872-881 -- hardcoded 10-digit check, matching this codebase's
    // sibling organization-master's own already-disclosed simplification of
    // the dynamic $rootScope.registrationFieldObj.mobileDigit config.
    if (r.unitContact.length !== 10) {
      this.notify.warning('Please enter valid mobile number');
      return;
    }
    if (!EMAIL_PATTERN.test(r.unitEmailId)) {
      this.notify.warning('Please Enter Valid Email Id');
      return;
    }

    this.api.update(r).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error(res.message ?? 'Error!');
          return;
        }
        const unitId = r.unitId as number;
        this.uploadLogoIfSelected(unitId, () => {
          this.notify.success(res.message ?? 'Success!');
          this.resetForm();
          this.loadList();
        });
      },
      error: () => this.notify.error('Error!'),
    });
  }

  private resetForm(): void {
    this.record.set(emptyHospitalMasterRecord());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.selectedLogo.set(null);
    this.imageSrc.set('');
    // editMode deliberately NOT reset here -- see class comment.
  }

  // ---- edit (mirrors editUnit, ctrl:772-850) ----
  edit(unitId: number): void {
    this.saveMode.set(false);
    this.editMode.set(true);
    this.imageSrc.set('');
    this.api.getUnitById(unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.object) {
          this.notify.error('Error In Fetching Data');
          return;
        }
        const unit = res.object;
        this.record.set(unit);
        if (unit.unitLogo) this.imageSrc.set(unit.unitLogo);
        if (unit.unitCountryId) this.onCountryChange(unit.unitCountryId as number);
        if (unit.unitStateId) this.onStateChange(unit.unitStateId as number);
        if (unit.unitDistrictId) this.onDistrictChange(unit.unitDistrictId as number);
        // ctrl:830 -- getAreaByCity(...) is commented out in legacy on this
        // exact line too, so the Area select's options are never
        // re-populated on edit; only unit.areaName (already on the fetched
        // record) is shown. Faithfully NOT calling an area-list fetch here.
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:642-670) ----
  toggleStatus(row: HospitalMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateStatus(row.unitId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) => rows.map((r) => (r.unitId === row.unitId ? { ...r, status: nextStatus } : r)));
          this.notify.success(res.message ?? 'Success!');
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
      error: () => this.notify.error('Error!'),
    });
  }

  // ---- subscription sub-feature (mirrors openSubscriptionPopup, ctrl:1231-1236) ----
  openSubscriptionPopup(): void {
    this.showSubscriptionModal.set(true);
    this.subscriptionConfig.set(emptySubscriptionConfig());
    const unitId = this.record().unitId as number;
    this.api.getSubscriptionConfig(unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) this.subscriptionConfig.set(res.object);
      },
    });
    this.api.getSubscriptionHistory(unitId, this.record().organizationId as number).subscribe({
      next: (res) => {
        if (res.status === 'success') this.subscriptionHistory.set(res.listObject ?? []);
        else this.notify.error(res.message ?? 'Error!');
      },
    });
  }

  closeSubscriptionPopup(): void {
    this.showSubscriptionModal.set(false);
  }

  updateSubscriptionConfigField<K extends keyof SubscriptionConfig>(field: K, value: SubscriptionConfig[K]): void {
    this.subscriptionConfig.update((c) => ({ ...c, [field]: value }));
  }

  createSubscriptionConfig(): void {
    const c = this.subscriptionConfig();
    if (!c.subscriptionType || c.subscriptionAmount == null || c.subscriptionDiscount == null || c.subscriptionTax == null || c.subscriptionNetAmount == null) {
      return;
    }
    const payload: SubscriptionConfig = {
      ...c,
      unitId: this.record().unitId as number,
      organizationId: this.record().organizationId as number,
    };
    this.api.saveSubscriptionConfig(payload).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          if (res.object) this.subscriptionConfig.set(res.object);
          this.notify.success(res.message ?? 'Success!');
        } else {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- manual subscription popup (mirrors openManualSubScripionPopup, ctrl:1284-1314) ----
  openManualSubscriptionPopup(): void {
    const c = this.subscriptionConfig();
    if (c.subscriptionConfigId == null || c.subscriptionConfigId <= 0) {
      this.notify.error('Please create subscription configuration.');
      return;
    }
    this.manualSubscription.set({
      subscriptionConfigId: c.subscriptionConfigId,
      subscriptionType: c.subscriptionType,
      subscriptionAmount: c.subscriptionAmount,
      subscriptionDiscount: c.subscriptionDiscount,
      subscriptionNetAmount: c.subscriptionNetAmount,
      subscriptionTax: c.subscriptionTax,
      paidAmount: null,
      amountPaidStatus: 'PAID',
      subscriptionStartDate: null,
      subscriptionEndDate: null,
      subscriptionStatus: 'ACTIVE',
      paymentType: 'BANK',
      finalReminderCount: 3,
      reminderCount: 3,
    });
    this.showManualSubscriptionModal.set(true);
  }

  closeManualSubscriptionPopup(): void {
    this.showManualSubscriptionModal.set(false);
  }

  updateManualField<K extends keyof ManualSubscriptionForm>(field: K, value: ManualSubscriptionForm[K]): void {
    this.manualSubscription.update((f) => (f ? { ...f, [field]: value } : f));
  }

  // ---- start-date change (mirrors updateEndDate, ctrl:1237-1255) ----
  onSubscriptionStartDateChange(dateStr: string): void {
    const f = this.manualSubscription();
    if (!f) return;
    let start = dateStr ? new Date(`${dateStr}T00:00:00`) : null;
    const now = new Date();
    if (start && start <= now) start = now;
    let end: Date | null = null;
    if (start) {
      end = new Date(start);
      switch (f.subscriptionType) {
        case 'MONTHLY':
          end.setMonth(start.getMonth() + 1);
          break;
        case 'QUARTERLY':
          end.setMonth(start.getMonth() + 3);
          break;
        case 'YEARLY':
          end.setFullYear(start.getFullYear() + 1);
          break;
        case 'HALF-YEARLY':
          end.setMonth(start.getMonth() + 6);
          break;
      }
    }
    this.manualSubscription.set({ ...f, subscriptionStartDate: start, subscriptionEndDate: end });
  }

  createSubscription(): void {
    const f = this.manualSubscription();
    if (!f || !f.subscriptionStartDate || !f.subscriptionEndDate || f.paidAmount == null) return;
    this.api
      .createSubscription({
        ...f,
        unitId: this.record().unitId as number,
        organizationId: this.record().organizationId as number,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.openSubscriptionPopup();
            this.closeManualSubscriptionPopup();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}
