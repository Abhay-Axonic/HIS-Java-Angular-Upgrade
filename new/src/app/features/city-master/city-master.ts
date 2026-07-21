import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CityMasterService } from './city-master.service';
import { CityRecord, CityRow, CountryOption, DistrictOption, StateOption, emptyCity } from './city-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/city-master.html +
 * resources/assets/js/controller/master/cityMasterController.js.
 * moduleId 386 (app.js:3482-3508 resolve block).
 *
 * Audited as real, full CRUD with real cascading dropdowns -- see the
 * per-method comments below for exact endpoint/behavior citations back to
 * the controller.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `onCountryChange`/`onStateChange` (mirrors `getStateByCountry`/
 *   `getDistrictByState`, ctrl:202-244) have NO guard on an empty id and do
 *   NOT clear the downstream dropdown's list or selected value -- unlike
 *   this codebase's `organizationMaster` cascade, which explicitly resets
 *   child lists/selections on every parent change. Legacy city-master
 *   simply re-fetches with whatever id it's given (including `''`) and
 *   leaves any previously-selected state/district id and options in place.
 *   Reproduced as-is: no added guards, no added resets.
 * - `edit()` (mirrors `showUpdateBtn`, ctrl:318-358) calls `.toString()`
 *   directly on the fetched `countryId`/`stateId`/`districtId` with no
 *   null-check (ctrl:341-346) -- if the backend ever returned a null id
 *   here, legacy would throw. Reproduced verbatim, not guarded.
 * - The Save button's confirm-modal step (`saveCity`'s `popUpFlag` dance,
 *   ctrl:246-315) only exists for CREATE. `updateCity` (ctrl:403-457) saves
 *   immediately with no confirm step. This asymmetry is real, not an
 *   omission -- reproduced via `confirmSave()`/`updateCity()` below.
 * - Search filters only the CURRENT PAGE's already-fetched rows
 *   (`cityList|filter:searchKeyword` over the server-paginated list, ctrl
 *   view line 201), not the full dataset -- same pattern already noted in
 *   this codebase's `asset-category-master`.
 * - On both save and update, the legacy controller reloads the list
 *   unconditionally (success AND failure paths both call
 *   `initCityMasterList`) but only resets the form (`$scope.init()`) on
 *   success. Reproduced: `loadList()` always runs; `resetForm()` only runs
 *   after a successful save/update.
 * - `updateStatus` (ctrl:361-400) does not manually flip the row's status
 *   in either branch -- it just reloads the whole list from the server,
 *   which is what actually reflects the persisted (or unchanged, on
 *   failure) status. Reproduced the same way rather than adding an
 *   optimistic-then-revert dance this screen's legacy code doesn't have.
 * - Legacy's `saveBtnFlag`/`updateBtnFlag` (ctrl:56-57) are two separate,
 *   inverted-polarity flags; this rebuild uses one `saveMode` boolean
 *   (matching this codebase's `organizationMaster` convention) since the
 *   net visible behavior -- Save shown/Update hidden by default, swapped
 *   after clicking edit -- is identical either way.
 * - `$scope.unitId` (ctrl:40) is read from the cookie but never used
 *   anywhere else in the controller; not carried into this rebuild as
 *   dead state.
 */
@Component({
  selector: 'app-city-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './city-master.html',
  styleUrl: './city-master.scss',
})
export class CityMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CityMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.city) ----
  city = signal<CityRecord>(emptyCity());
  saveMode = signal(true);
  formSubmitted = signal(false);

  countryList = signal<CountryOption[]>([]);
  stateList = signal<StateOption[]>([]);
  districtList = signal<DistrictOption[]>([]);

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initCityMasterList/PagerService) ----
  cityList = signal<CityRow[]>([]);
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
    const rows = this.cityList();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.countryName?.toLowerCase().includes(keyword) ||
        r.stateName?.toLowerCase().includes(keyword) ||
        r.districtName?.toLowerCase().includes(keyword) ||
        r.cityCode?.toLowerCase().includes(keyword) ||
        r.cityName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadCountryList();
    this.loadList();
  }

  private loadCountryList(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getCityList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cityList.set(res.listObject ?? []);
      },
    });
    this.api.getCityCount(this.organizationId).subscribe({
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

  updateCityField<K extends keyof CityRecord>(field: K, value: CityRecord[K]): void {
    this.city.update((c) => ({ ...c, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- cascading dropdowns (mirrors getStateByCountry/getDistrictByState) --
  // no guard on empty id, no clearing of the downstream list/selection --
  // see class comment. ----
  onCountryChange(countryId: number | ''): void {
    this.updateCityField('countryId', countryId);
    this.api.getStatesByCountry(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateList.set(res.listObject ?? []);
      },
    });
  }

  onStateChange(stateId: number | ''): void {
    this.updateCityField('stateId', stateId);
    this.api.getDistrictsByState(stateId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.districtList.set(res.listObject ?? []);
      },
    });
  }

  private requiredFieldsValid(): boolean {
    const c = this.city();
    return !!(c.countryId !== '' && c.stateId !== '' && c.districtId !== '' && c.cityCode.trim() && c.cityName.trim());
  }

  // ---- save (mirrors saveCity's popUpFlag confirm-modal dance, ctrl:253-315) ----
  saveCity(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const c = this.city();
    const now = formatDateTime(new Date());
    this.api
      .saveCity({
        cityCode: c.cityCode,
        cityName: c.cityName,
        districtId: c.districtId,
        stateId: c.stateId,
        countryId: c.countryId,
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
            this.notify.success('City added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('City already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // ---- update (mirrors updateCity, ctrl:403-457) -- no confirm modal ----
  updateCity(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const c = this.city();
    this.api
      .updateCity({
        cityCode: c.cityCode,
        cityName: c.cityName,
        districtId: c.districtId,
        stateId: c.stateId,
        countryId: c.countryId,
        organizationId: this.organizationId,
        cityId: c.cityId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('City updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('City already exits...!!');
          }
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.city.set(emptyCity());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.loadCountryList();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:318-358) ----
  edit(cityId: number): void {
    this.saveMode.set(false);
    this.api.getCityById(cityId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const detail = res.listObject[0];
        // ctrl:341-346 -- unguarded `.toString()` on the fetched ids, no
        // null-check. Preserved as-is (see class comment); this will throw
        // the same way legacy does if the backend ever returns a null id.
        const countryId = Number(detail.countryId.toString());
        const stateId = Number(detail.stateId.toString());
        const districtId = Number(detail.districtId.toString());
        this.city.set({
          cityId: detail.cityId,
          cityCode: detail.cityCode,
          cityName: detail.cityName,
          countryId,
          stateId,
          districtId,
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
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:361-400) -- always
  // reloads the list from the server in both branches rather than
  // optimistically flipping/reverting `row` locally. ----
  toggleStatus(row: CityRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateCityStatus(row.cityId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('City Updated Successfully...!!');
        } else {
          this.notify.warning(res.message ?? 'Update failed');
        }
        this.loadList();
      },
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:254-256/363-365/404-406. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
