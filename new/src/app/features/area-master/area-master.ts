import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AreaMasterService } from './area-master.service';
import { AreaRecord, AreaRow, CityOption, CountryOption, DistrictOption, StateOption, emptyArea } from './area-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/area-master.html +
 * resources/assets/js/controller/master/areaMasterController.js.
 * moduleId 387 (app.js:3509-3535 resolve block). Single, non-duplicated
 * state registration confirmed (grep across the whole legacy tree found no
 * second `.state('areaMaster', ...)`/self-registration) -- not an
 * addItemPOCreation-shaped dead-frontend case.
 *
 * Backend-existence confirmed end-to-end via source read (no live token
 * available, matching entry 67's methodology): all 6 area-specific
 * endpoints (saveAreaMaster/getAreaMasterList/updateAreaMaster/
 * getAreaById/updateStatusForArea/getAreaCount) implemented by
 * AreaMasterController.java -> AreaMasterServiceImpl.java ->
 * AreaMasterDaoImpl.java; the 3 cascading lookups (getActiveCountryList,
 * getActiveStateListByCountryId, getActiveDistrictListByStateId,
 * getActiveCityListByDistrictId) resolve to CountryMasterController /
 * DistrictMasterController / CityMasterController / this screen's own
 * AreaMasterController respectively -- same "next level's controller
 * serves the previous level's children" structure city-master already
 * documented. `public.m_area_master` confirmed present with live data in
 * this dev DB (1 seed row, organization_id=1) -- no dev-DB gap.
 *
 * Uses the shared `<modal>` directive (view:153) whose legacy controller
 * never defines `cancel` -- entry 55's standing policy applies (this
 * rebuild's working close button is a disclosed, pre-approved shared-
 * component improvement, not re-litigated here).
 *
 * **CORRECTION (2026-07-22, live Step 5 testing):** an earlier draft of
 * this file's own doc comment claimed `updateArea` (ctrl:430-487) never
 * includes `cityId`, framing the omission as a legacy bug to faithfully
 * reproduce. That claim was a misread of legacy source, caught during
 * live testing (the update payload really did omit `cityId`, confirmed via
 * network capture -- but re-reading `areaMasterController.js` directly,
 * independently, three separate ways, showed line 445's `updateArea`
 * payload DOES include `cityId : $scope.area.cityId` and line 362's
 * `showUpdateBtn` DOES set `$scope.area.cityId` on edit-populate -- legacy
 * has no such bug at all, in either `saveArea` (ctrl:296) or `updateArea`).
 * This was a genuine rebuild-introduced defect, not a parity target --
 * fixed below (`updateArea()` now includes `cityId`), not held for owner
 * approval, since correct parity with legacy's real (bug-free) behavior
 * here is the task itself, not a deviation from it. See
 * `docs/ANGULAR_PENDING_FINDINGS.md` entry 120 for the retraction.
 * **Deliberate bugs/quirks preserved, not fixed by this rebuild:**
 * - **Status toggle never reverts and never reloads, in either branch**
 *   (ctrl:389-427) -- unlike sibling `city-master`'s `updateStatus`, which
 *   reloads the whole list on both success AND failure. Because legacy's
 *   `ng-model="info.status"` mutates the row in place the instant the
 *   checkbox is clicked (before the POST even resolves), a FAILED status
 *   update leaves the checkbox showing the new (wrong, never-persisted)
 *   state with no correction -- a real, silent display/data mismatch on
 *   failure. Reproduced faithfully: `toggleStatus()` below flips the row
 *   locally and permanently regardless of the server's response, with no
 *   reload call.
 * - **Page navigation (`setPage`/`goToPage`) only re-fetches the row list,
 *   never the count** -- matching legacy's own `getAreaMasterList` vs.
 *   `initAreaMasterList` split (ctrl:101-149 vs 151-178), the same pattern
 *   already disclosed for `holidayCalendarMasterUnit` (entry 111). Only
 *   initial load, page-size change, and post-save/post-update/post-edit
 *   reloads re-fetch the count. (Note: sibling `city-master`'s own rebuild
 *   re-fetches both on every page nav -- a divergence from ITS OWN legacy
 *   source, not a pattern to copy here; see playbook's "bugs baked into
 *   the reference screen" sibling-divergence check.)
 * - `onCountryChange`/`onStateChange`/`onDistrictChange` (mirrors
 *   `getStateByCountry`/`getDistrictByState`/`getCityByDistrict`,
 *   ctrl:204-269) have NO guard on an empty id and do NOT clear the
 *   downstream dropdown's list or selected value -- same quirk already
 *   documented for city-master. Reproduced as-is.
 * - `edit()` (mirrors `showUpdateBtn`, ctrl:344-386) calls `.toString()`
 *   directly on all 4 fetched ids (cityId/countryId/stateId/districtId)
 *   with no null-check -- same unguarded pattern already documented for
 *   city-master's own `edit()`. Reproduced verbatim.
 * - The Save button's confirm-modal step (`saveArea`'s `popUpFlag` dance,
 *   ctrl:278-341) only exists for CREATE. `updateArea` (ctrl:430-487)
 *   saves immediately with no confirm step -- same asymmetry already
 *   documented for city-master/organizationMaster.
 * - Search filters only the CURRENT PAGE's already-fetched rows
 *   (`areaList|filter:searchKeyword`, view:255), not the full dataset --
 *   same pattern as city-master/asset-category-master.
 * - Server-side duplicate-name check confirmed (`getAreaByName`/
 *   `getAreaByNameNotId`, case-sensitive-or-exact per dao) -- no
 *   independent frontend uniqueness check exists; only HTML5 `required`.
 * - `$scope.unitId` (ctrl:41) is read from the cookie but never used
 *   anywhere else in the controller; not carried into this rebuild as
 *   dead state.
 */
@Component({
  selector: 'app-area-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './area-master.html',
  styleUrl: './area-master.scss',
})
export class AreaMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AreaMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.area) ----
  area = signal<AreaRecord>(emptyArea());
  saveMode = signal(true);
  formSubmitted = signal(false);

  countryList = signal<CountryOption[]>([]);
  stateList = signal<StateOption[]>([]);
  districtList = signal<DistrictOption[]>([]);
  cityList = signal<CityOption[]>([]);

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initAreaMasterList/PagerService) ----
  areaList = signal<AreaRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.areaList();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.countryName?.toLowerCase().includes(keyword) ||
        r.stateName?.toLowerCase().includes(keyword) ||
        r.districtName?.toLowerCase().includes(keyword) ||
        r.cityName?.toLowerCase().includes(keyword) ||
        r.areaCode?.toLowerCase().includes(keyword) ||
        r.areaName?.toLowerCase().includes(keyword) ||
        r.postCode?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadCountryList();
    this.loadListAndCount();
  }

  private loadCountryList(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
  }

  // ---- mirrors initAreaMasterList (ctrl:101-149) -- list AND count together. ----
  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getAreaList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.areaList.set(res.listObject ?? []);
      },
    });
    this.api.getAreaCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  // ---- mirrors getAreaMasterList (ctrl:151-178) -- list ONLY, used by page nav. ----
  private loadListOnly(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getAreaList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.areaList.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadListOnly();
  }

  updateAreaField<K extends keyof AreaRecord>(field: K, value: AreaRecord[K]): void {
    this.area.update((a) => ({ ...a, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- cascading dropdowns (mirrors getStateByCountry/getDistrictByState/
  // getCityByDistrict, ctrl:204-269) -- no guard on empty id, no clearing of
  // the downstream list/selection -- see class comment. ----
  onCountryChange(countryId: number | ''): void {
    this.updateAreaField('countryId', countryId);
    this.api.getStatesByCountry(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateList.set(res.listObject ?? []);
      },
    });
  }

  onStateChange(stateId: number | ''): void {
    this.updateAreaField('stateId', stateId);
    this.api.getDistrictsByState(stateId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtList.set(res.listObject ?? []);
      },
    });
  }

  onDistrictChange(districtId: number | ''): void {
    this.updateAreaField('districtId', districtId);
    this.api.getCitiesByDistrict(districtId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityList.set(res.listObject ?? []);
      },
    });
  }

  private requiredFieldsValid(): boolean {
    const a = this.area();
    return !!(
      a.countryId !== '' &&
      a.stateId !== '' &&
      a.districtId !== '' &&
      a.cityId !== '' &&
      a.areaCode.trim() &&
      a.areaName.trim() &&
      a.postCode.trim()
    );
  }

  // ---- save (mirrors saveArea's popUpFlag confirm-modal dance, ctrl:278-341) ----
  saveArea(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const a = this.area();
    const now = formatDateTime(new Date());
    this.api
      .saveArea({
        areaCode: a.areaCode,
        areaName: a.areaName,
        districtId: a.districtId,
        stateId: a.stateId,
        countryId: a.countryId,
        cityId: a.cityId,
        postCode: a.postCode,
        status: 'A',
        updatedBy: this.userId,
        createdBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Area added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Area already exits...!!');
          }
          this.loadListAndCount();
        },
      });
  }

  // ---- update (mirrors updateArea, ctrl:430-487) -- no confirm step ----
  updateArea(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const a = this.area();
    this.api
      .updateArea({
        areaCode: a.areaCode,
        areaName: a.areaName,
        districtId: a.districtId,
        stateId: a.stateId,
        countryId: a.countryId,
        cityId: a.cityId,
        areaId: a.areaId as number,
        postCode: a.postCode,
        status: 'A',
        updatedBy: this.userId,
        organizationId: this.organizationId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Area Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Area already exits...!!');
          }
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.area.set(emptyArea());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.loadCountryList();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:344-386) ----
  edit(areaId: number): void {
    this.saveMode.set(false);
    this.api.getAreaById(areaId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const detail = res.listObject[0];
        // ctrl:362-373 -- unguarded `.toString()` on all 4 fetched ids, no
        // null-check. Preserved as-is (see class comment).
        const cityId = Number(detail.cityId.toString());
        const countryId = Number(detail.countryId.toString());
        const stateId = Number(detail.stateId.toString());
        const districtId = Number(detail.districtId.toString());
        this.area.set({
          areaId: detail.areaId,
          areaCode: detail.areaCode,
          areaName: detail.areaName,
          postCode: detail.postCode,
          countryId,
          stateId,
          districtId,
          cityId,
        });
        this.api.getStatesByCountry(countryId).subscribe({
          next: (r) => {
            if (r.status === 'success') this.stateList.set(r.listObject ?? []);
          },
        });
        this.api.getDistrictsByState(stateId).subscribe({
          next: (r) => {
            if (r.status === 'success') this.districtList.set(r.listObject ?? []);
          },
        });
        this.api.getCitiesByDistrict(districtId).subscribe({
          next: (r) => {
            if (r.status === 'success') this.cityList.set(r.listObject ?? []);
          },
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:389-427) -- flips the row
  // locally and PERMANENTLY (never reverts, never reloads from server, in
  // either branch) -- see class comment for why this differs from sibling
  // city-master's own toggleStatus. ----
  toggleStatus(row: AreaRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.areaList.update((rows) => rows.map((r) => (r.areaId === row.areaId ? { ...r, status: nextStatus } : r)));
    this.api.updateAreaStatus(row.areaId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Area Updated Successfully...!!');
        } else {
          this.notify.error('Something went wrong...!!');
        }
      },
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:280-281/391-393/432-434. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
