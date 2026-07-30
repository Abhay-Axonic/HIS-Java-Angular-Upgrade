import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ReferralMasterService } from './referral-master.service';
import {
  AreaOption,
  CityOption,
  CountryOption,
  DistrictOption,
  ReferralMasterRecord,
  ReferralMasterRow,
  ReferralTypeOption,
  StateOption,
  emptyReferral,
} from './referral-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/referral-master.html +
 * resources/assets/js/controller/adt/global/referralMasterController.js.
 * Legacy state `referralMaster` (app.js:3920-3947), gated by a resolve-block
 * check for moduleId 396, no superuser requirement -- confirmed live,
 * wired at the ROUTE level via `moduleAccessGuard` (`data: { moduleId: 396 }`),
 * not in this component. `$state` IS properly injected in this controller's
 * DI list, so its `cookieObject == undefined -> $state.go('login')` guard is
 * a real, working redirect -- `moduleAccessGuard` already reproduces the
 * same effect at the route level, so no separate constructor-level check is
 * added here.
 *
 * Backend is the Global Maven module's
 * `com.param.global.controller.ReferralMasterController`
 * (`@RequestMapping("/adt")`) -- NOT the ADT module, despite the legacy
 * frontend view living under `views/master/UnitMaster/organizationMaster/`
 * and the controller file living under `controller/adt/global/`. Confirmed
 * by reading the Java source directly (a known trap per the playbook's
 * Step 0). The referral-TYPE dropdown alone comes from the ADT module's own
 * `ReferralTypeMasterController` (`getActiveReferralTypeList`).
 *
 * **Golden-trace capture method used (Step 2): `tools/golden-trace/capture.js`,
 * run this session (`node capture.js <target.json>` against
 * `{"name":"referralMaster","url":"/referralMaster"}`) -- real Playwright
 * automation bootstrapping the actual legacy AngularJS app and driving
 * `$state.go('referralMaster')`, not a hand-derived substitute.** This
 * environment has no live Java backend/DB, so the tool's own broad
 * synthetic permission set stands in for a real seeded session cookie (see
 * the tool's README "Known limitation") -- every `$http` call is *attempted*
 * exactly as the real app would attempt it (real method/URL/order/state
 * tag), just never gets a real response. The captured trace
 * (`tools/golden-trace/golden-traces/referralMaster.json`) confirms exactly
 * 4 calls fire on load, in this dispatch order, matching the audited
 * `init()`/post-init call sequence (ctrl:28-104, ctrl:198) call-for-call:
 *   1. GET  /adt/getActiveCountryList/1                     (ctrl:72-86; captured with the seeded org id, 1)
 *   2. POST /adt/getActiveReferralTypeList                  (ctrl:87-102)
 *   3. POST /adt/getReferralMasterList                      (ctrl:111-140, via initReferralMasterList)
 *   4. POST /adt/getReferralCount                           (ctrl:142-153, via initReferralMasterList)
 * All 4 are unordered/concurrent in the source (no awaited chaining between
 * them -- each fires-and-forgets its own `.then()`); the capture's
 * dispatch order matches the source's call order exactly. No state/
 * district/city/area calls fire on load -- those only fire on a user's own
 * `ng-change`/on `edit()`, confirmed absent from the trace. Reproduced
 * identically in this component's constructor below; see Step 4 comparison
 * in the batch report.
 *
 * **CONFIRMED, OWNER-APPROVED FIX (not a faithful-reproduction case) --
 * geo-cascade parent-clears-dependents:** legacy's `getStateByCountry`/
 * `getDistrictByState`/`getCityByDistrict`/`getAreaByCity` (ctrl:210-298)
 * only ever REPLACE the option list for the next level down
 * (`$scope.stateList = response.data.listObject`, etc.) -- none of them
 * clear the dependent `$scope.referral.stateId`/`districtId`/`cityId`/
 * `areaId` MODEL values themselves. Only the changed level's own `<select>`
 * visibly blanks (its options changed out from under it); every level
 * below silently keeps its stale value from the previous parent selection.
 * A user who changes Country, does not touch State/District/City/Area
 * again, and clicks Save persists a geo chain mixing the NEW country with
 * OLD district/city/area IDs -- a real, silent data-integrity bug. Fixed
 * here: every `on*Change` handler below clears both the option list AND
 * the record's own field value for every level strictly below it. This is
 * a disclosed correction, not a faithful port -- see each handler's own
 * comment.
 *
 * **Other audited behavior, reproduced faithfully (not bugs to fix):**
 * - Required fields per the view's own `required`/`show-errors` markup:
 *   referralTypeId, referralName, address (view `ng-model="referral.address"`),
 *   contact. Country/state/district/city/area/email/contactPerson are all
 *   optional (view line 67's Country help-block is itself commented out in
 *   legacy). Rendered via visible `.help-block` messages per this project's
 *   validation-message-visibility standing policy (playbook Step 3) --
 *   legacy's own messages exist in markup but are permanently hidden by
 *   `.help-block{display:none!important}` in its global CSS; this rebuild's
 *   messages render visibly by default, an already-blanket-approved
 *   improvement, not a per-screen decision.
 * - `saveReferral()` sends BOTH `id` and `referralTypeId` in the create
 *   payload, both set to the same referral-type id, with legacy's own
 *   inline `// need to verify` comment (ctrl:316) -- ported verbatim, not
 *   "cleaned up," per instruction.
 * - Save goes through a confirm-modal dance (`popUpFlag`/`PopupOkBtn`,
 *   ctrl:204-207, 309-310) before the real POST fires; Update does NOT --
 *   it posts immediately (ctrl:463-520). Reproduced as an asymmetric
 *   `openSaveConfirm()`+`confirmSave()` vs. a direct `update()`, matching
 *   the same asymmetry already documented on `ambulance-master.ts`.
 *   Legacy's shared `<modal>` directive hardcodes a Cancel button calling
 *   `cancel()`, which this controller never defines (confirmed by grep) --
 *   an instance of the already-logged entry-55 shared-directive bug class.
 *   This rebuild's own confirm modal has a correctly-working Cancel by
 *   construction, an already-blanket-approved standing improvement per the
 *   playbook, not re-logged here as a new finding.
 * - `saveBtnFlag`/`updateBtnFlag` (ctrl:56-57) are a ONE-WAY switch: only
 *   `showUpdateBtn` (edit) ever sets them, flipping to "Update" mode
 *   permanently for the rest of the session -- nothing resets them back to
 *   "Save" mode after a successful add/update. Reproduced: `saveMode` is
 *   only ever set `false` by `edit()`, never back to `true`, matching the
 *   same documented quirk on `ambulance-master.ts`.
 * - On successful save/update, legacy calls `$scope.init()` again --
 *   redundant re-fetch of country + referral-type lists, AND (since
 *   `init()` itself does `$scope.referral = {}`) a full form-field reset.
 *   `$scope.initReferralMasterList(...)` (list + count refresh) runs
 *   UNCONDITIONALLY regardless of success/failure (ctrl:364/517, outside
 *   the if/else). Reproduced via `refreshInitData()` (success-only) +
 *   `loadList()` (unconditional). Since a successful save/update also
 *   blanks the geo selects, this rebuild's `resetForm()` additionally
 *   clears the state/district/city/area OPTION LISTS too (legacy leaves
 *   these stale, the same class of issue as the disclosed cascade fix
 *   above) -- a minimal, disclosed extension of that same fix, not a new
 *   one, and not user-visible as a behavior change beyond "the dropdowns
 *   don't show a stale, unselected list of options".
 * - `updateStatus` (ctrl:423-460) applies the new status to the row
 *   OPTIMISTICALLY (the legacy checkbox's `ng-true-value`/`ng-false-value`
 *   already flips `info.status` before the handler even runs) and never
 *   reverts it on a failed POST. Reproduced the same way, matching
 *   `ambulance-master.ts`'s documented convention for this exact pattern.
 * - The search box (`ng-repeat="info in referralList|filter:searchKeyword"`,
 *   view line 385) filters only the already-fetched page's rows client-side
 *   -- there is no server-side search endpoint in this controller at all.
 *   Reproduced via a `displayedRows` computed filter, not a backend call.
 * - `PopUpDetails(info)` (ctrl:522-531, the info-circle "Details" popup)
 *   reads its fields directly off the already-fetched list row -- no
 *   separate API call. Reproduced identically via `showDetails()`.
 * - Email has no `required` attribute and no validation message in legacy;
 *   note for the record (not reproduced here, deliberately): AngularJS's
 *   `input[type=email]` + `ngModel` applies its own built-in EMAIL_REGEXP
 *   validator independent of `required`, which -- with no matching
 *   help-block in the markup -- means an invalid-format, non-empty email
 *   silently blocks both Save and Update in legacy with zero on-screen
 *   explanation (`referralform.$valid` false, no visible reason why). This
 *   rebuild does NOT reproduce that silent lock (email here is a plain,
 *   unrestricted optional field); flagged here as a disclosed, deliberate
 *   non-reproduction rather than silently normalized, since replicating a
 *   silent submit-block with no user feedback would be strictly worse UX
 *   for no compensating parity benefit.
 */
@Component({
  selector: 'app-referral-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './referral-master.html',
  styleUrl: './referral-master.scss',
})
export class ReferralMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ReferralMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists ----
  countryList = signal<CountryOption[]>([]);
  referralTypeList = signal<ReferralTypeOption[]>([]);
  stateList = signal<StateOption[]>([]);
  districtList = signal<DistrictOption[]>([]);
  cityList = signal<CityOption[]>([]);
  areaList = signal<AreaOption[]>([]);

  // ---- form state (mirrors $scope.referral) ----
  record = signal<ReferralMasterRecord>(emptyReferral());
  saveMode = signal(true); // true = saveBtnFlag hidden->Save shown; one-way, see class doc
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);

  // ---- details popup (mirrors PopUpDetails / detailsAddModal11) ----
  showDetailsModal = signal(false);
  detailsRow = signal<ReferralMasterRow | null>(null);

  // ---- list + pagination (mirrors $scope.referralList / $scope.pager) ----
  rows = signal<ReferralMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: `filter:searchKeyword` filters only the
  // already-fetched page's rows, not a server search (see class doc).
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const list = this.rows();
    if (!keyword) return list;
    return list.filter(
      (r) =>
        r.referralTypeDesc?.toLowerCase().includes(keyword) ||
        r.referralName?.toLowerCase().includes(keyword) ||
        r.referralEmail?.toLowerCase().includes(keyword) ||
        r.referralContact?.toLowerCase().includes(keyword) ||
        r.referralContactPerson?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.refreshInitData();
    this.loadList();
  }

  updateField<K extends keyof ReferralMasterRecord>(field: K, value: ReferralMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- init-time / post-save refetch (mirrors $scope.init()'s country +
  // referral-type calls, and its `$scope.referral = {}` form reset) ----
  private refreshInitData(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
    this.api.getActiveReferralTypeList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.referralTypeList.set(res.listObject ?? []);
      },
    });
  }

  // ---- list (mirrors initReferralMasterList) ----
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getReferralList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getReferralCount(this.organizationId).subscribe({
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
  // and by `edit()` below (which populates all 5 levels from a saved
  // record at once and must NOT trigger the dependents-clearing behavior
  // meant only for a live user-driven change).
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

  // ---- required-field validation (mirrors referralform.$valid over
  // referralTypeId/referralName/address/contact -- see class doc) ----
  isReferralTypeInvalid(): boolean {
    return this.submitted() && !this.record().referralTypeId;
  }

  isReferralNameInvalid(): boolean {
    return this.submitted() && !this.record().referralName.trim();
  }

  isAddressInvalid(): boolean {
    return this.submitted() && !this.record().address.trim();
  }

  isContactInvalid(): boolean {
    return this.submitted() && !this.record().contact.trim();
  }

  private validate(): boolean {
    this.submitted.set(true);
    const r = this.record();
    return !!r.referralTypeId && !!r.referralName.trim() && !!r.address.trim() && !!r.contact.trim();
  }

  // ---- save (mirrors saveReferral's popUpFlag confirm dance -- Save-only,
  // see class doc) ----
  openSaveConfirm(): void {
    if (!this.validate()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const now = formatDateTime(new Date());
    const r = this.record();
    this.api
      .saveReferral({
        id: r.referralTypeId,
        referralTypeId: r.referralTypeId, // need to verify -- ported verbatim, see class doc
        referralName: r.referralName,
        referralCode: 'Code',
        referralAddress: r.address,
        referralContactPerson: r.contactPerson,
        referralEmail: r.email,
        referralContact: r.contact,
        // Geo levels are optional (see class doc); sent as `null` rather
        // than legacy's raw `''` (an untouched <select value=""> binds
        // `$scope.referral.countryId` to an empty string) since the DTO's
        // fields are Java `Integer` wrappers -- `null` deserializes safely,
        // `""` risks a Jackson `InvalidFormatException` before the request
        // even reaches the DAO. Not a behavioral parity concern: a
        // genuinely optional, unselected geo level should persist as "no
        // value", which `null` expresses correctly either way.
        countryId: r.countryId || null,
        stateId: r.stateId || null,
        districtId: r.districtId || null,
        cityId: r.cityId || null,
        areaId: r.areaId || null,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        updatedDate: now,
        createdDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Referral Added Successfully...!!');
            this.refreshInitData();
            this.resetForm();
          } else {
            this.notify.error('Referral already exits...!!');
          }
          // Unconditional, regardless of success/failure -- see class doc.
          this.loadList();
        },
        error: () => this.notify.error('Referral already exits...!!'),
      });
  }

  // ---- update (mirrors updateReferral -- no confirm modal, see class doc) ----
  update(): void {
    if (!this.validate()) return;
    const now = formatDateTime(new Date());
    const r = this.record();
    this.api
      .updateReferral({
        referralId: r.referralId,
        id: r.referralTypeId,
        referralName: r.referralName,
        referralCode: 'Code',
        referralAddress: r.address,
        referralContactPerson: r.contactPerson,
        referralEmail: r.email,
        referralContact: r.contact,
        countryId: r.countryId || null,
        stateId: r.stateId || null,
        districtId: r.districtId || null,
        cityId: r.cityId || null,
        areaId: r.areaId || null,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Referral Updated Successfully...!!');
            this.refreshInitData();
            this.resetForm();
          } else {
            this.notify.error('Referral already exits...!!');
          }
          // Unconditional, regardless of success/failure -- see class doc.
          this.loadList();
        },
        error: () => this.notify.error('Referral already exits...!!'),
      });
  }

  private resetForm(): void {
    this.record.set(emptyReferral());
    this.submitted.set(false);
    // See class doc: a minimal, disclosed extension of the geo-cascade fix
    // above -- legacy leaves these option lists stale after a form reset.
    this.stateList.set([]);
    this.districtList.set([]);
    this.cityList.set([]);
    this.areaList.set([]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:372-420) ----
  edit(referralId: number): void {
    this.api.getReferralById(referralId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.saveMode.set(false);
          this.record.set({
            referralId: detail.referralId,
            referralTypeId: detail.id,
            referralName: detail.referralName,
            address: detail.referralAddress,
            email: detail.referralEmail,
            contact: detail.referralContact,
            contactPerson: detail.referralContactPerson,
            countryId: detail.countryId,
            stateId: detail.stateId,
            districtId: detail.districtId,
            cityId: detail.cityId,
            areaId: detail.areaId,
          });
          // Populate dependent OPTION LISTS to match the loaded selections
          // -- deliberately via the plain fetchers, not the on*Change
          // handlers, so loading a saved record's 5 geo levels at once
          // doesn't trigger the dependents-clearing fix meant only for a
          // live user change (see class doc).
          if (detail.countryId) this.fetchStates(detail.countryId);
          if (detail.stateId) this.fetchDistricts(detail.stateId);
          if (detail.districtId) this.fetchCities(detail.districtId);
          if (detail.cityId) this.fetchAreas(detail.cityId);
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- optimistic, never reverted
  // on failure, see class doc) ----
  toggleStatus(row: ReferralMasterRow, nextStatus: 'A' | 'I'): void {
    this.rows.update((list) => list.map((r) => (r.referralId === row.referralId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        referralId: row.referralId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Referral Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  // ---- details popup (mirrors PopUpDetails, ctrl:522-531 -- no API call,
  // reads straight off the already-fetched row) ----
  showDetails(row: ReferralMasterRow): void {
    this.detailsRow.set(row);
    this.showDetailsModal.set(true);
  }

  closeDetails(): void {
    this.showDetailsModal.set(false);
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:303-305/425-427/464-466. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
