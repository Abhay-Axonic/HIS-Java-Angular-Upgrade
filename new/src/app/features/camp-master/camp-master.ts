import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CampMasterService } from './camp-master.service';
import {
  AreaOption,
  CampMasterRecord,
  CampMasterRow,
  CityOption,
  CountryOption,
  DistrictOption,
  PatientTypeOption,
  StateOption,
  emptyCamp,
} from './camp-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/bloodBank/camp-master.html +
 * resources/assets/js/controller/bloodBank/campMasterController.js
 * (Blood Bank module). Legacy state `campMaster` (app.js:19389-19394, title
 * "Camp Master") has NO resolve block at all -- confirmed by reading the
 * live `.state(...)` definition directly, same shape as
 * billingGroupMaster/periodicityService -- so this route wires
 * `moduleAccessGuard` with no `data.moduleId`, not a stricter check than
 * legacy actually has. `$state` IS injected in this controller's DI list,
 * so its `cookieObject == undefined -> $state.go('login')` guard (ctrl:20-24)
 * is a real, working redirect; `moduleAccessGuard`'s baseline "must be
 * logged in" check already reproduces the same effect at the route level.
 *
 * Backend is the BloodBank Maven module's
 * `com.param.bloodBank.controller.CampMasterController`
 * (`@RequestMapping("/global/bloodBank")`) for camp CRUD/list/status, plus
 * the SAME shared Global-module geo-cascade endpoints (`/adt/...`)
 * referralMasterController.js also uses, and a Billing-module
 * `/api/billing/getPatientType/{orgId}/{unitId}` for the optional patient-
 * type dropdown.
 *
 * **Golden-trace capture method used (Step 2): `tools/golden-trace/capture.js`,
 * run this session (`node capture.js <target.json>` against
 * `{"name":"campMaster","url":"/campMaster"}`) -- real Playwright automation
 * bootstrapping the actual legacy AngularJS app and driving
 * `$state.go('campMaster')`, not a hand-derived substitute.** This
 * environment has no live Java backend/DB, so the tool's own broad
 * synthetic permission set stands in for a real seeded session cookie (see
 * the tool's README "Known limitation") -- every `$http` call is *attempted*
 * exactly as the real app would attempt it (real method/URL/order/state
 * tag), just never gets a real response. The captured trace
 * (`tools/golden-trace/golden-traces/campMaster.json`) confirms exactly 4
 * calls fire on load, in this dispatch order, matching the audited
 * controller's synchronous init flow call-for-call:
 *   1. POST /global/bloodBank/getCampMasterListPaginationList (ctrl:64-103, initMasterList)
 *   2. POST /global/bloodBank/getCampMasterListCount            (ctrl:64-103, initMasterList)
 *   3. GET  /api/billing/getPatientType/1/1                     (ctrl:166-184, getActivePatientTypeList)
 *   4. GET  /adt/getActiveCountryList/1                         (ctrl:186-195)
 * All 4 fire unconditionally, one after another in source order with no
 * awaited chaining between them (each is its own fire-and-forget `.then()`);
 * the capture's dispatch order matches the source's call order exactly. No
 * state/district/city/area calls fire on load -- those only fire on a
 * user's own `ng-change`/on `edit()`, confirmed absent from the trace.
 * Reproduced identically in this component's constructor below; see Step 4
 * comparison in the batch report.
 *
 * **CONFIRMED, OWNER-APPROVED FIX (not a faithful-reproduction case) --
 * `getcampList()` is dead code, success confirmation + list refresh never
 * fire:** legacy's `saveCamp()` (ctrl:350-354), `updateStatus()`
 * (ctrl:396-405), and `updateCamp()` (ctrl:534-540) each call
 * `$scope.getcampList()` immediately before `growl.success(...)` inside
 * their success branch -- but `$scope.getcampList` itself is defined inside
 * a commented-out block (ctrl:147-164, `/*...*\/`) in the deployed
 * controller. Calling an undefined function throws a `TypeError` that
 * aborts the rest of that `.then()` callback silently (no visible error to
 * the user, since this runs inside an async promise handler with no
 * surrounding try/catch in `saveCamp`/`updateCamp` -- `updateStatus` does
 * wrap its body in try/catch, but the catch only `console.log`s, it doesn't
 * recover and still never reaches `growl.success`) -- so a successful
 * save/update/status-toggle silently never shows its success message AND
 * never refreshes the grid, even though the backend write genuinely
 * succeeded. Fixed here: `refreshList()` (a thin wrapper around the same
 * paginated `getCampMasterListPaginationList` + `getCampMasterListCount`
 * calls `initMasterList` already uses for the visible grid -- NOT the dead,
 * non-paginated `getCampMasterList` endpoint `getcampList()` was written
 * against) runs after every successful save/update/status-toggle, alongside
 * a real `notify.success(...)` call. This is the CORRECT, intended
 * behavior `getcampList()` was clearly meant to provide, not a faithful
 * port of the broken no-op -- a disclosed correction, not a new feature.
 *
 * **CONFIRMED, OWNER-APPROVED FIX (lower severity, standing disclosed-
 * correction precedent) -- edit-load stale/duplicate geo dropdown options:**
 * legacy's `showUpdateBtn(info)` (ctrl:421-446) populates the form's
 * state/district/city/area SELECTIONS from the clicked row, but instead of
 * re-fetching each dependent option list for the record's actual
 * country/state/district/city, it `.push()`es ONE fabricated
 * `{ id: info.id, name: info.name }` option onto whatever the current
 * (possibly empty, possibly stale-from-a-prior-edit) option list already
 * is. Editing the same or a different row more than once in one session
 * accumulates duplicate stale options in every one of the 4 dependent
 * dropdowns, since nothing is ever cleared. Fixed here: `edit()` below
 * calls the real `fetchStates`/`fetchDistricts`/`fetchCities`/`fetchAreas`
 * fetchers (the same ones the cascade `on*Change` handlers use) for the
 * row's actual country/state/district/city, which REPLACE each option list
 * rather than appending to it -- repeated edits never accumulate
 * duplicates. Country's own option list needs no such fetch: it's already
 * loaded once at init (see call 4 above) and never mutated per-edit in
 * legacy either.
 *
 * **Other audited behavior, reproduced faithfully (not bugs to fix):**
 * - Required fields per the view's own `required` HTML5 attributes AND a
 *   duplicated manual JS validation block in `saveCamp`/`updateCamp`
 *   (ctrl:267-340, 448-523, byte-identical field list both places):
 *   campCode, campName, address, countryId, stateId, districtId, cityId,
 *   areaId, pincode -- ALL required, unlike referralMaster where the geo
 *   chain is optional. `patientTypeId` has no validation at either layer
 *   (optional). Rendered via visible `.help-block` messages per this
 *   project's validation-message-visibility standing policy (playbook Step
 *   3) -- legacy's own messages (there are none in this view's markup, only
 *   the bare `required` attribute + a manual JS `growl.error(...)` per
 *   missing field) are reproduced here as visible inline messages instead
 *   of legacy's transient toast-only feedback, consistent with every other
 *   screen built under this policy.
 * - BOTH Save and Update go through their own confirm-modal dance
 *   (`#confirmPatientTypeSave`/`#confirmPatientTypeUpdate`, view:285-325) --
 *   unlike referralMaster's asymmetric Save-only confirm, campMaster
 *   confirms both actions. Reproduced as `openSaveConfirm()`+`confirmSave()`
 *   and `openUpdateConfirm()`+`confirmUpdate()`, both gated by the same
 *   `validate()`. Legacy's shared `<modal>`-style Cancel button here is a
 *   plain Bootstrap `data-dismiss="modal"` button with no custom `cancel()`
 *   call (unlike the entry-55 shared-directive bug class referralMaster
 *   hit) -- nothing to disclose here beyond the standard modal-close.
 * - `saveBtnFlag`/`updateBtnFlag` (ctrl:25, 440-441) are NOT a one-way
 *   switch here (unlike referralMaster/ambulanceMaster's documented
 *   quirk): `updateCamp`'s own success branch explicitly resets
 *   `saveBtnFlag = true; updateBtnFlag = false` (ctrl:535-536), flipping
 *   the form back to "Save" mode after a successful update. `saveCamp`'s
 *   success branch does not touch either flag (irrelevant in practice,
 *   since `saveBtnFlag` is already `true` unless an edit is in progress).
 *   Reproduced: `saveMode` is reset to `true` inside `confirmUpdate`'s
 *   success branch, matching this screen's own real behavior rather than
 *   copying the different one-way convention from a different screen.
 * - On successful save, legacy resets `$scope.camp = {}` (ctrl:353) --
 *   wiping every field, including `organizationId`/`unitId`/`createdBy`
 *   that were only ever set moments earlier inside the same `saveCamp()`
 *   call, not restored until the next save attempt re-sets them itself
 *   (ctrl:341-347). Reproduced via `resetForm()`, which -- since this
 *   rebuild's `record` signal only ever holds the pure form fields, never
 *   the derived audit fields the legacy mutable object conflates -- has no
 *   equivalent side effect to reproduce; the two are behaviorally
 *   equivalent from the form's own perspective (fields blank, ready for
 *   the next entry).
 * - The search input (view:180, `<input ... placeholder="Search" ...>`) has
 *   NO `ng-model` and NO `ng-change`/`ng-click` anywhere in the markup --
 *   confirmed genuinely dead, not merely unwired to a handler this
 *   controller forgot to define. Reproduced as an inert, non-functional
 *   input (no binding at all) rather than inventing a client-side filter
 *   legacy never had, matching the Step 1 "non-functional mockup control"
 *   guidance (don't build behavior a dead control never had).
 * - The `#addressDetails` modal (view:258-283, a hardcoded, non-templated
 *   lorem-ipsum-style address block) has no `data-target="#addressDetails"`
 *   trigger anywhere in this view -- confirmed unreachable dead markup, not
 *   reproduced here at all (nothing loses functionality by omitting it).
 */
@Component({
  selector: 'app-camp-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './camp-master.html',
  styleUrl: './camp-master.scss',
})
export class CampMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CampMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists ----
  countryList = signal<CountryOption[]>([]);
  stateList = signal<StateOption[]>([]);
  districtList = signal<DistrictOption[]>([]);
  cityList = signal<CityOption[]>([]);
  areaList = signal<AreaOption[]>([]);
  patientTypeList = signal<PatientTypeOption[]>([]);

  // ---- form state (mirrors $scope.camp) ----
  record = signal<CampMasterRecord>(emptyCamp());
  saveMode = signal(true); // true = saveBtnFlag shown; see class doc -- NOT one-way here
  submitted = signal(false); // drives *required* error display

  showSaveConfirm = signal(false);
  showUpdateConfirm = signal(false);

  // ---- list + pagination (mirrors $scope.campList / $scope.pager) ----
  rows = signal<CampMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    // Reproduces the source's own call order exactly -- see class doc's
    // Step 2/4 golden-trace comparison.
    this.loadList();
    this.loadPatientTypeList();
    this.loadCountryList();
  }

  updateField<K extends keyof CampMasterRecord>(field: K, value: CampMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- init-time calls ----
  private loadCountryList(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
  }

  private loadPatientTypeList(): void {
    this.api.getActivePatientTypeList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.patientTypeList.set(res.listObject ?? []);
      },
    });
  }

  // ---- list (mirrors initMasterList) ----
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getCampList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getCampCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  /**
   * DISCLOSED FIX -- the correct, intended replacement for legacy's dead
   * `getcampList()` call (see class doc). Reuses the same paginated
   * list+count fetchers `loadList()` already uses for the visible grid.
   */
  private refreshList(): void {
    this.loadList();
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

  // ---- geo cascade -- DISCLOSED FIX: each level clears every dependent
  // level's SELECTED VALUE (not just its option list) -- see class doc. ----
  onCountryChange(countryId: number | ''): void {
    this.record.update((r) => ({ ...r, countryId, stateId: '', districtId: '', cityId: '', areaId: '' }));
    this.stateList.set([]);
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
    if (countryId) this.fetchStates(countryId);
  }

  onStateChange(stateId: number | ''): void {
    this.record.update((r) => ({ ...r, stateId, districtId: '', cityId: '', areaId: '' }));
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
    if (stateId) this.fetchDistricts(stateId);
  }

  onDistrictChange(districtId: number | ''): void {
    this.record.update((r) => ({ ...r, districtId, cityId: '', areaId: '' }));
    this.cityList.set([]);
    this.areaList.set([]);
    if (districtId) this.fetchCities(districtId);
  }

  onCityChange(cityId: number | ''): void {
    this.record.update((r) => ({ ...r, cityId, areaId: '' }));
    this.areaList.set([]);
    if (cityId) this.fetchAreas(cityId);
  }

  onAreaChange(areaId: number | ''): void {
    this.record.update((r) => ({ ...r, areaId }));
  }

  // Plain list-fetchers, deliberately NOT touching `record` -- used both by
  // the on*Change handlers above (after they've set the field themselves)
  // and by `edit()` below (which populates all 5 levels from a saved row at
  // once and must NOT trigger the dependents-clearing behavior meant only
  // for a live user-driven change). REPLACES the option list (`.set(...)`),
  // never appends -- this is the fix for the edit-load duplicate-
  // accumulation bug, see class doc.
  private fetchStates(countryId: number): void {
    this.api.getStateByCountry(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateList.set(res.listObject ?? []);
      },
    });
  }

  private fetchDistricts(stateId: number): void {
    this.api.getDistrictByState(stateId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtList.set(res.listObject ?? []);
      },
    });
  }

  private fetchCities(districtId: number): void {
    this.api.getCityByDistrict(districtId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityList.set(res.listObject ?? []);
      },
    });
  }

  private fetchAreas(cityId: number): void {
    this.api.getAreaByCity(cityId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.areaList.set(res.listObject ?? []);
      },
    });
  }

  // ---- required-field validation (mirrors the duplicated HTML5 `required`
  // + manual JS block over campCode/campName/address/countryId/stateId/
  // districtId/cityId/areaId/pincode -- see class doc) ----
  isCampCodeInvalid(): boolean {
    return this.submitted() && !this.record().campCode.trim();
  }

  isCampNameInvalid(): boolean {
    return this.submitted() && !this.record().campName.trim();
  }

  isAddressInvalid(): boolean {
    return this.submitted() && !this.record().address.trim();
  }

  isCountryInvalid(): boolean {
    return this.submitted() && !this.record().countryId;
  }

  isStateInvalid(): boolean {
    return this.submitted() && !this.record().stateId;
  }

  isDistrictInvalid(): boolean {
    return this.submitted() && !this.record().districtId;
  }

  isCityInvalid(): boolean {
    return this.submitted() && !this.record().cityId;
  }

  isAreaInvalid(): boolean {
    return this.submitted() && !this.record().areaId;
  }

  isPincodeInvalid(): boolean {
    return this.submitted() && !this.record().pincode.trim();
  }

  private validate(): boolean {
    this.submitted.set(true);
    const r = this.record();
    return (
      !!r.campCode.trim() &&
      !!r.campName.trim() &&
      !!r.address.trim() &&
      !!r.countryId &&
      !!r.stateId &&
      !!r.districtId &&
      !!r.cityId &&
      !!r.areaId &&
      !!r.pincode.trim()
    );
  }

  // ---- save (mirrors saveCamp's confirm-modal dance, ctrl:267-379) ----
  openSaveConfirm(): void {
    if (!this.validate()) return;
    this.showSaveConfirm.set(true);
  }

  closeSaveConfirm(): void {
    this.showSaveConfirm.set(false);
  }

  confirmSave(): void {
    this.showSaveConfirm.set(false);
    const now = formatDateTime(new Date());
    const r = this.record();
    this.api
      .saveCamp({
        campCode: r.campCode,
        campName: r.campName,
        address: r.address,
        countryId: r.countryId,
        stateId: r.stateId,
        districtId: r.districtId,
        cityId: r.cityId,
        areaId: r.areaId,
        pincode: r.pincode,
        patientTypeId: r.patientTypeId || null,
        createdBy: this.userId,
        createdDate: now,
        status: 'A',
        updatedBy: this.userId,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Camp Master Saved Successfully...!!');
            this.resetForm();
            // DISCLOSED FIX -- see class doc: the correct, intended
            // replacement for legacy's dead getcampList() call.
            this.refreshList();
          } else {
            this.notify.error(res.message ?? 'Camp already exists...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  // ---- update (mirrors updateCamp's confirm-modal dance, ctrl:448-558) ----
  openUpdateConfirm(): void {
    if (!this.validate()) return;
    this.showUpdateConfirm.set(true);
  }

  closeUpdateConfirm(): void {
    this.showUpdateConfirm.set(false);
  }

  confirmUpdate(): void {
    this.showUpdateConfirm.set(false);
    const now = formatDateTime(new Date());
    const r = this.record();
    this.api
      .updateCamp({
        campId: r.campId,
        campCode: r.campCode,
        campName: r.campName,
        address: r.address,
        countryId: r.countryId,
        stateId: r.stateId,
        districtId: r.districtId,
        cityId: r.cityId,
        areaId: r.areaId,
        pincode: r.pincode,
        patientTypeId: r.patientTypeId || null,
        status: 'A',
        updatedBy: this.userId,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Camp Master Updated Successfully...!!');
            // See class doc: unlike referralMaster's one-way switch,
            // campMaster's own updateCamp success branch really does flip
            // back to Save mode (ctrl:535-536) -- reproduced faithfully.
            this.saveMode.set(true);
            this.resetForm();
            // DISCLOSED FIX -- see class doc.
            this.refreshList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  private resetForm(): void {
    this.record.set(emptyCamp());
    this.submitted.set(false);
    // A minimal, disclosed extension of the geo-cascade fix above: legacy
    // leaves these option lists stale after `$scope.camp = {}`; clearing
    // them here avoids showing a stale, unselected list of options for the
    // next entry (same convention as referralMaster's `resetForm()`).
    this.stateList.set([]);
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:421-446 -- reads straight off
  // the already-fetched row, no separate API call for the record itself) ----
  edit(row: CampMasterRow): void {
    this.saveMode.set(false);
    this.record.set({
      campId: row.campId,
      campCode: row.campCode,
      campName: row.campName,
      address: row.address,
      pincode: row.pincode,
      countryId: row.countryId,
      stateId: row.stateId,
      districtId: row.districtId,
      cityId: row.cityId,
      areaId: row.areaId,
      patientTypeId: row.patientTypeId ?? '',
    });
    // DISCLOSED FIX -- see class doc: real re-fetches (REPLACE, not
    // append) instead of legacy's fabricated `.push()`, so repeated edits
    // never accumulate duplicate stale options.
    this.fetchStates(row.countryId);
    this.fetchDistricts(row.stateId);
    this.fetchCities(row.districtId);
    this.fetchAreas(row.cityId);
  }

  // ---- status toggle (mirrors updateStatus, ctrl:381-418 -- optimistic,
  // never reverted on failure, same convention as referralMaster) ----
  toggleStatus(row: CampMasterRow, nextStatus: 'A' | 'I'): void {
    this.rows.update((list) => list.map((r) => (r.campId === row.campId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        campId: row.campId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Camp Master Status Updated Successfully...!!');
            // DISCLOSED FIX -- see class doc.
            this.refreshList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:46,49/342,345/384/525. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
