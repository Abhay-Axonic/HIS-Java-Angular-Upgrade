import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CountryMasterService } from './country-master.service';
import { CountryForm, CountryRow, emptyCountryForm } from './country-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const CALLING_CODE_PATTERN = /^[+-]?\d+$/;

/**
 * Parity rebuild of views/master/GlobalMaster/country-master.html +
 * resources/assets/js/controller/master/countryMasterController.js
 * (`countryMasterController`, no `controllerAs`). Root of the
 * country -> state -> district -> city -> area cascade family, but this
 * screen itself has no cascading dropdown -- just 5 plain required fields.
 * Route guard: moduleId 383 (app.js:3401-3427 resolve block), wired at the
 * route level, not here.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 * - Save vs. Update is asymmetric in legacy, and that asymmetry is kept:
 *   clicking Save (`saveBtnFlag`) validates, then opens a "Are you sure you
 *   want to Save?" confirm modal (the `<modal>` directive in
 *   commonPopUpDirectives.js) before actually POSTing; clicking Update
 *   (`updateBtnFlag`) validates and POSTs immediately, with NO confirm step
 *   at all (ctrl:347-410 vs. 195-269). This is not a copy-paste omission on
 *   this rebuild's part -- legacy genuinely has no popup wired to
 *   `updateCountry()`.
 * - Both `saveCountry` and `updateCountry` build `createdDate`/`updatedDate`
 *   client-side via `moment().format('DD-MM-YYYY HH:mm:ss')` (ctrl:196-198,
 *   308-309, 348-350) rather than letting the server stamp them -- kept via
 *   the same local `formatDateTime()` used elsewhere in this rebuild (see
 *   asset-scheduler.ts).
 * - The country-calling-code format check (`/^[+-]?\d+$/`, ctrl:223-231,
 *   365-373) only runs after the required-fields check already passed, and
 *   only fires `growl.warning(...)` + aborts -- it does not mark the field
 *   invalid the way the required-check does. Reproduced as a separate guard
 *   in `create()`/`update()`, not folded into `isFieldInvalid`.
 * - `showUpdateBtn` (ctrl:272-303) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETSINGLECOUNTRY` call resolves, and
 *   on a failed response uses a raw `alert("Error!!")` -- the only place in
 *   this controller that doesn't use `growl`. Both are reproduced verbatim
 *   (`saveMode.set(false)` up front; `window.alert` on failure) rather than
 *   "corrected" to the growl-toast pattern used everywhere else on this screen.
 * - `updateStatus` (ctrl:306-344) reloads the list via
 *   `initCountryMasterList(...,$scope.offset /* always 0 *\/,...)` on BOTH
 *   its success and failure branches -- i.e. toggling status always snaps
 *   the grid back to page 1, rather than refreshing the current page in
 *   place. Reproduced via `reloadFirstPage()` for both branches, rather than
 *   the in-place single-row `.update()` pattern used by other rebuilt
 *   screens' status toggles (this screen's own legacy behavior is a full
 *   reload, not a targeted patch).
 * - Likewise, `saveCountry`/`updateCountry` call
 *   `initCountryMasterList(...)` unconditionally after the response comes
 *   back (ctrl:259-263, 401-405) -- on a business-level failure ("Country
 *   already exits...!!") the list still reloads to page 1 even though
 *   nothing changed; only a transport-level error (no response at all) skips
 *   the reload here, since legacy has no `.catch` on these calls either.
 * - The success-message casing genuinely differs between the three
 *   operations in legacy source and is kept verbatim: "Country added
 *   Successfully...!!" (save), "Country updated Successfully...!!" (update,
 *   lowercase u), "Country Updated Successfully...!!" (status toggle,
 *   uppercase U).
 * - Legacy's `countryCode`/`contactDigit` inputs carry a custom `numbers-only`
 *   keypress-filter directive with no rebuild-side precedent yet in this
 *   codebase (confirmed: no other rebuilt screen has ported it). Reproduced
 *   inline via a small `digitsOnly()` strip on `(ngModelChange)` rather than
 *   introducing a new shared directive for a single screen.
 * - The search box (`ng-model="searchKeyword"` + `filter:searchKeyword` over
 *   `$scope.countryList`, view line 165/185) only ever filters whatever page
 *   of data is currently loaded -- this screen's list is server-paginated via
 *   offset/noOfRecordsPerPage (not fetched-in-full like asset-category-master),
 *   so unlike that screen's "quirk", here filtering the current page IS the
 *   entire available dataset client-side; there is nothing more to filter.
 */
@Component({
  selector: 'app-country-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './country-master.html',
  styleUrl: './country-master.scss',
})
export class CountryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CountryMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.country + saveBtnFlag/updateBtnFlag) ----
  form = signal<CountryForm>(emptyCountryForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initCountryMasterList/setPage) ----
  list = signal<CountryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Current page's rows only -- see class comment on why this is the whole
  // dataset available to filter, not a quirk of a client-side full fetch.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (r) =>
        r.countryCode?.toLowerCase().includes(keyword) ||
        r.countryName?.toLowerCase().includes(keyword) ||
        r.countryInitial?.toLowerCase().includes(keyword) ||
        r.countryCallingCode?.toLowerCase().includes(keyword) ||
        r.contactDigit?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  private reloadFirstPage(): void {
    this.currentPage.set(1);
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

  updateField<K extends keyof CountryForm>(field: K, value: CountryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  digitsOnly(value: string): string {
    return value.replace(/[^0-9]/g, '');
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.countryCode.trim() && f.countryName.trim() && f.countryInitial.trim() && f.countryCallingCode.trim() && f.contactDigit.trim());
  }

  private validateCallingCode(code: string): boolean {
    if (code && !CALLING_CODE_PATTERN.test(code)) {
      this.notify.warning('Please enter valid country calling code');
      return false;
    }
    return true;
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors saveCountry's
  // popUpFlag-gated double call via PopupOkBtn). ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.create();
  }

  private create(): void {
    const f = this.form();
    if (!this.validateCallingCode(f.countryCallingCode)) return;
    const now = formatDateTime(new Date());
    this.api
      .create({
        countryCode: f.countryCode,
        countryName: f.countryName,
        countryInitial: f.countryInitial,
        countryCallingCode: f.countryCallingCode,
        contactDigit: f.contactDigit,
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
            this.notify.success('Country added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Country already exits...!!');
          }
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's
  // updateCountry() being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    if (!this.validateCallingCode(f.countryCallingCode)) return;
    this.api
      .update({
        countryCode: f.countryCode,
        countryName: f.countryName,
        countryInitial: f.countryInitial,
        countryCallingCode: f.countryCallingCode,
        countryId: f.countryId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        contactDigit: f.contactDigit,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Country updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Country already exits...!!');
          }
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyCountryForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode BEFORE the
  // GETSINGLECOUNTRY response arrives, and uses a raw alert() on failure,
  // both preserved verbatim; see class comment). ----
  edit(row: CountryRow): void {
    this.saveMode.set(false);
    this.api.getById(row.countryId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            countryId: detail.countryId,
            countryCode: detail.countryCode,
            countryName: detail.countryName,
            countryInitial: detail.countryInitial,
            countryCallingCode: detail.countryCallingCode,
            contactDigit: detail.contactDigit,
          });
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- always reloads the list at
  // page 1 on both branches, see class comment; nothing to `.update()`
  // in-place here since legacy itself never patches a single row). ----
  toggleStatus(row: CountryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateStatus(row.countryId, nextStatus, this.userId, formatDateTime(new Date()), this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success('Country Updated Successfully...!!');
        } else {
          this.notify.warning(res.message ?? '');
        }
        this.reloadFirstPage();
      },
    });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
