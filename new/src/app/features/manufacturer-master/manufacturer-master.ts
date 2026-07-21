import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ManufacturerMasterService } from './manufacturer-master.service';
import { ManufacturerForm, ManufacturerRow, emptyManufacturerForm } from './manufacturer-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
type SortField = 'code' | 'name';

/**
 * Parity rebuild of views/master/GlobalMaster/manufacturer-master.html +
 * resources/assets/js/controller/master/pharmacy/manufacturerMasterController.js
 * (`ManufacturerMaster.Controller`, `controllerAs:'vm'`, self-registered
 * state -- controller's own `.config($stateProvider)` block, ctrl:319-348).
 * moduleId 461 (ctrl:333, resolve block, confirmed live). Global/
 * organization-only master (`organizationId` off `cookieObject`, no
 * `unitId` scoping in any endpoint) -- unlike this codebase's adt/unit
 * masters (amenity-master, floor-master).
 *
 * Confirmed real, working CRUD: create (ctrl:200-235), update
 * (ctrl:238-278), edit-populate `PopulateManufacturer` (ctrl:108-133),
 * status toggle `ToggleStatus` (ctrl:141-163) -- all wired to real
 * `/api/globals/manufacturers/...` endpoints, see manufacturer-master.service.ts.
 *
 * **Deliberate quirks/bugs preserved, per audit + explicit rebuild decision:**
 * - **NOT reproduced -- legacy-AngularJS-DI-only artifact:** ctrl:3 injects
 *   `($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,
 *   PaginationService, growl)` -- no `$state` -- yet ctrl:6 calls
 *   `$state.go('login')` on a missing session cookie, throwing
 *   `ReferenceError` in legacy today (same bug class as the already-fixed
 *   `organizationMaster`/`assetCategoryMaster`/`implantMaster`, entries 8/10).
 *   This is structurally impossible to reproduce in this architecture --
 *   there is no per-controller DI list to omit `$state` from -- and
 *   `moduleAccessGuard` + the normal Angular router already handle an
 *   unauthenticated visit safely. Not reproduced; see scaffold README.
 * - **Reproduced -- cross-row form wipe (real bug, ctrl:147-149):**
 *   `ToggleStatus` calls `ClearFields()` whenever a row is deactivated,
 *   unconditionally -- with no check that the toggled row is the one
 *   currently loaded into the add/edit form above. A user mid-edit of one
 *   manufacturer loses that in-progress edit if a different row is
 *   deactivated. Reproduced verbatim in `toggleStatus()` below.
 * - **Reproduced -- asymmetric status-revert-on-error (real bug, ctrl:154-158):**
 *   on a 406 response the row is always forced back to `status: true`,
 *   regardless of which direction the toggle was going -- correct for a
 *   failed deactivation, a silent no-op (not a real revert) for a failed
 *   activation. Reproduced as-is (same pattern already established for
 *   this codebase's asset-category-master).
 * - Status vocabulary is **boolean** here (`true`/`false`), not the `'A'`/'I'`
 *   string vocabulary this codebase's adt/unit masters use -- kept boolean,
 *   matching legacy exactly (status-vocabulary drift between sibling
 *   masters is expected and NOT normalized away).
 * - The search box (view:81, `vm.model.pagination.search` /
 *   `PaginationService.searchData`) is REAL and working in legacy --
 *   fuzzy-matches every field across the FULL list (not just the current
 *   page, unlike this codebase's asset-category-master), then re-paginates.
 *   Implemented as a genuine filter in `displayedRows` below, not a dead
 *   binding.
 * - The Code/Manufacturer column sort carets (view:104-122) ARE real and
 *   working (`PaginationService.sortBy`), unlike the dead sort carets found
 *   on this codebase's amenity-master/floor-master/qms-department-master --
 *   implemented via `sortBy()` below.
 * - **Duplicate-code validation preserved** (`ValidateCode`/
 *   `ValidateCodeForUpdate`, ctrl:165-197): client-side check against the
 *   already-loaded list, alongside the three required-field checks
 *   (`ValidateManufacturer`, ctrl:293-309).
 * - **Dead validation NOT reproduced:** both `ValidateCode` and
 *   `ValidateCodeForUpdate` also carry a commented-out alphanumeric-code
 *   regex check (ctrl:174-177, 192-195) -- genuinely dead in legacy today
 *   (commented out, not merely unused), unlike this codebase's
 *   asset-category-master, where the *identical-looking* regex check is
 *   live. This is a real "live-vs-commented-out guard" divergence between
 *   two sibling code-validators; the dead one is correctly NOT
 *   implemented here.
 * - The Save link (view:88-93) opens the confirm modal via `data-toggle`
 *   unconditionally, on every click, regardless of field validity -- only
 *   a CSS `disabled` look (`ng-class`) reacts to empty required fields, it
 *   does not block the click. The actual `SaveManufacturer()` (which does
 *   validate) only runs when "Ok" is clicked. Reproduced the same way this
 *   codebase's asset-category-master already does: `openConfirmModal()`
 *   never validates, `save()` (wired to "Ok") does.
 * - The Edit pencil (view:152-156) is only COSMETICALLY disabled when a row
 *   is Inactive (`ng-class` on the icon; the `ng-click` lives on the
 *   parent `<a>`, ungated) -- clicking an inactive row's edit icon still
 *   calls `PopulateManufacturer` in legacy. This is a REAL difference from
 *   this codebase's asset-category-master/floor-master, whose edit icons
 *   are genuinely click-gated. Reproduced faithfully: `edit()` below has no
 *   status guard, only a cosmetic `[class.disabled]`.
 * - `self.model.previousCode` (ctrl:36,83,260-266): populated once on init
 *   and again after every successful create (`getPrefix()`); after a
 *   successful UPDATE, the code field is refilled from `previousCode`
 *   rather than left blank (ctrl:258-266) -- a "next code" hint left over
 *   from the create flow. `getPrefix()`'s own `response.data.code`
 *   assignment (ctrl:82) is commented out, so the prefix call never
 *   actually sets the code field directly either. Both quirks reproduced
 *   verbatim below.
 * - `self.model.codeDisableFlag`/`isEdit` (ctrl:51,110-111,125-126,271) are
 *   assigned throughout the controller but the CURRENT (live, uncommented)
 *   view markup never reads either of them -- a dead-flag class of finding
 *   (only the commented-out old markup block, view:14-21, used
 *   `codeDisableFlag`). Not reproduced as a visible disable state; there is
 *   nothing observable to preserve.
 * - `PopulateManufacturer` (ctrl:104-106,118-119) looks up the row in the
 *   local list via `getManufacturerById` and then immediately discards that
 *   result by overwriting it with `response.data` on the very next line --
 *   harmless copy-paste residue, correctly not reproduced as a real lookup.
 */
@Component({
  selector: 'app-manufacturer-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './manufacturer-master.html',
  styleUrl: './manufacturer-master.scss',
})
export class ManufacturerMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ManufacturerMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- form state (mirrors self.model.*) ----
  form = signal<ManufacturerForm>(emptyManufacturerForm());
  private previousCode: string | undefined;

  isCodeInvalid = signal(false);
  isNameInvalid = signal(false);
  isAddressInvalid = signal(false);

  showConfirmModal = signal(false);
  showAddressModal = signal(false);
  detailAddress = signal('');

  // ---- list + client-side pagination/search/sort (mirrors PaginationService) ----
  list = signal<ManufacturerRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');
  private sortField: SortField | null = null;
  private sortReverse = false;

  // Real, working search (ctrl-confirmed live, see class doc comment) --
  // fuzzy-matches every field across the FULL list, mirroring
  // `PaginationService.searchData`'s `$filter('filter', {$: search})`.
  private filteredAndSorted = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    let rows = this.list();
    if (keyword) {
      rows = rows.filter((r) =>
        Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
      );
    }
    if (this.sortField) {
      const field = this.sortField;
      const dir = this.sortReverse ? -1 : 1;
      rows = [...rows].sort((a, b) => (a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0) * dir);
    }
    return rows;
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredAndSorted().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  displayedRows = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.filteredAndSorted().slice(offset, offset + limit);
  });

  constructor() {
    this.loadManufacturers();
    this.loadPrefix();
  }

  private loadManufacturers(): void {
    this.api.getManufacturers(this.organizationId).subscribe({
      next: (rows) => this.list.set(rows ?? []),
    });
  }

  // ctrl:72-93. Only `.code` is ever read into `previousCode` -- see class
  // doc comment on why the code field itself is not auto-filled from this.
  private loadPrefix(): void {
    this.api.getPrefix(this.organizationId).subscribe({
      next: (res) => {
        this.previousCode = res?.code;
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  onSearchChange(value: string): void {
    this.search.set(value);
    this.currentPage.set(1); // mirrors PaginationService.setTotalPages resetting currentPage to 1
  }

  clearSearch(): void {
    this.onSearchChange('');
  }

  sortBy(field: SortField): void {
    this.sortReverse = this.sortField === field ? !this.sortReverse : false;
    this.sortField = field;
    this.currentPage.set(1);
  }

  updateField<K extends keyof ManufacturerForm>(field: K, value: ManufacturerForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
    if (field === 'code') this.isCodeInvalid.set(false);
    if (field === 'name') this.isNameInvalid.set(false);
    if (field === 'address') this.isAddressInvalid.set(false);
  }

  // ---- required-field validation (mirrors ValidateManufacturer, ctrl:293-309) ----
  private validateRequired(): boolean {
    const f = this.form();
    let valid = true;
    if (!f.code) {
      this.isCodeInvalid.set(true);
      valid = false;
    }
    if (!f.name) {
      this.isNameInvalid.set(true);
      valid = false;
    }
    if (!f.address) {
      this.isAddressInvalid.set(true);
      valid = false;
    }
    return valid;
  }

  // ---- duplicate-code validation (mirrors ValidateCode/ValidateCodeForUpdate,
  // ctrl:165-197 -- the dead commented-out regex check is NOT reproduced,
  // see class doc comment) ----
  private validateCode(excludeId?: number): boolean {
    const code = this.form().code;
    const duplicate = this.list().some((r) => r.code === code && r.id !== excludeId);
    if (duplicate) {
      this.notify.error('Duplicate Manufacture code');
      return false;
    }
    return true;
  }

  // ---- confirm-modal open (mirrors the unconditional data-toggle click,
  // ctrl/view:88-93 -- never validates; see class doc comment) ----
  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ---- save (mirrors SaveManufacturer -> CreateManufacturer/updateManufacturer,
  // ctrl:41-49 -- wired to the modal's "Ok" button) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validateRequired()) return;

    const f = this.form();
    if (!f.id) {
      this.create();
    } else {
      this.update(f.id);
    }
  }

  private create(): void {
    if (!this.validateCode()) return;
    const f = this.form();
    this.api
      .create(this.organizationId, { name: f.name, code: f.code, address: f.address, status: true })
      .subscribe({
        next: (res) => {
          this.list.update((rows) => [
            ...rows,
            { id: res.id, name: f.name, code: f.code, address: f.address, status: true },
          ]);
          this.clearFields();
          this.loadPrefix(); // ctrl:225 -- getPrefix() re-run after create
          this.notify.success(res.message ?? 'Success!');
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private update(id: number): void {
    if (!this.validateCode(id)) return;
    const f = this.form();
    this.api.update(this.organizationId, id, { name: f.name, code: f.code, address: f.address }).subscribe({
      next: (res) => {
        this.list.update((rows) =>
          rows.map((r) => (r.id === id ? { ...r, name: f.name, code: f.code, address: f.address } : r)),
        );
        this.clearFields();
        // ctrl:260-266 -- code refilled from previousCode rather than left
        // blank; getPrefix() only re-runs if previousCode is still unset.
        if (this.previousCode !== undefined) {
          this.form.update((form) => ({ ...form, code: this.previousCode as string }));
        } else {
          this.loadPrefix();
        }
        this.notify.success(res.message ?? 'Success!');
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  clearFields(): void {
    this.form.set(emptyManufacturerForm());
  }

  // ---- edit (mirrors PopulateManufacturer, ctrl:108-133 -- no status
  // guard, see class doc comment) ----
  edit(row: ManufacturerRow): void {
    this.api.getManufacturerById(this.organizationId, row.id).subscribe({
      next: (d) => {
        this.isCodeInvalid.set(false);
        this.isNameInvalid.set(false);
        this.isAddressInvalid.set(false);
        this.form.set({ id: d.id, code: d.code, name: d.name, address: d.address });
      },
    });
  }

  // ---- address details popup (mirrors getAddress, ctrl:135-138) ----
  showAddress(row: ManufacturerRow): void {
    this.detailAddress.set(row.address);
    this.showAddressModal.set(true);
  }

  closeAddressModal(): void {
    this.showAddressModal.set(false);
  }

  // ---- status toggle (mirrors ToggleStatus, ctrl:141-163) ----
  toggleStatus(row: ManufacturerRow): void {
    const nextStatus = !row.status;
    this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    // ctrl:147-149 -- cross-row form wipe, real bug, reproduced verbatim:
    // deactivating ANY row clears the add/edit form, regardless of which
    // row (if any) is currently loaded into it. See class doc comment.
    if (!nextStatus) this.clearFields();

    this.api.toggleStatus(this.organizationId, row.id, { status: nextStatus }).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: (err) => {
        if (err?.status === 406) {
          this.notify.error(err?.error?.message ?? 'Something Went Wrong');
          // ctrl:157 -- always forced back to true regardless of toggle
          // direction; real, asymmetric bug, reproduced verbatim.
          this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: true } : r)));
        } else {
          this.notify.error('Something Went Wrong');
        }
      },
    });
  }
}
