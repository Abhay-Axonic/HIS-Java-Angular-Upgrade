import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BedCategoryMasterService } from './bed-category-master.service';
import {
  BedCategoryFormState,
  BedCategoryRow,
  BillingBedCategoryOption,
  ConsultationServiceOption,
  HierarchyOption,
  HierarchyRowPayload,
  OccupancyUnitOption,
  emptyBedCategoryForm,
} from './bed-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/Adt/bed-category-master-global.html
 * + resources/assets/js/controller/adt/global/bedCategoryMasterController.js.
 * State `bedCategoryMaster` (app.js:4032-4058), moduleId 543, no
 * `controllerAs` (plain `$scope`).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - `getHierarchyPopUp()` (view:215) and `closePopupsj()` (view:345) don't
 *   exist anywhere in the legacy controller -- both would throw via
 *   Angular's exception handler, but Bootstrap's `data-toggle`/
 *   `data-dismiss` attributes independently open/close the modal anyway,
 *   masking the failure visually. `openHierarchyModal()`/
 *   `closeHierarchyModal()` below reproduce that same *visible* behavior
 *   (modal opens/closes) without inventing a working
 *   `getHierarchyPopUp`/`closePopupsj` that never existed.
 * - Create (`save()`/`saveBedCategory`) has a two-step confirm-modal flow
 *   (`showConfirmModal`); Update (`update()`/`updateBedCategory`) does not
 *   -- it saves directly on click, exactly matching the two separate
 *   legacy functions/buttons.
 * - Hierarchy-duplicate checks differ by flow, preserved exactly:
 *   - Create: any existing row sharing the new hierarchyId is a duplicate,
 *     UNLESS the new record is ICU (`isIcu` skips the check entirely).
 *   - Update: the in-edit row's hierarchyId is patched into the local list
 *     FIRST (unconditionally, even if validation later fails or the
 *     request never succeeds -- ctrl:537), then counted; the threshold is
 *     `>1` (not `>=1`) because the patched row now matches itself.
 *   - Set Hierarchy: checks the WHOLE list for any duplicate hierarchyId,
 *     with NO isIcu exemption at all -- inconsistent with the two flows
 *     above, but that inconsistency is the real legacy behavior.
 * - `edit()`'s row index (`indexToUpdate`) is taken from `$index` of the
 *   SEARCH-FILTERED `ng-repeat`, but `updateBedCategory()` mutates the RAW
 *   `bedCategoryList` array at that same numeric index (ctrl:353-354,537).
 *   When a search keyword narrows the visible rows, this is a real
 *   index-mismatch bug in legacy (the wrong row gets its hierarchyId
 *   patched). Reproduced as-is via `displayedRows()`'s index.
 * - `updateStatus()`/`toggleStatus` has no revert-on-failure at all (unlike
 *   e.g. asset-category-master's 406 handler) -- the switch stays flipped
 *   even if the backend call fails. Reproduced as-is.
 * - On a successful create/update/setHierarchy, legacy's `$scope.init()`
 *   unconditionally resets pagination back to page 1 at page size 10 and
 *   refetches all four dropdowns, no matter what page/size you were on --
 *   reproduced via `resetAfterSuccess()`.
 * - A real HTTP-level failure (as opposed to an app-level
 *   `status !== 'success'` response) has no handler at all in legacy's
 *   `GenericService.serviceAction` call sites here (an unhandled promise
 *   rejection, silently swallowed) -- this rebuild instead shows a generic
 *   "Something Went Wrong" notification, matching the established
 *   convention already used by every other rebuilt master screen (e.g.
 *   asset-category-master, implant-master) rather than reproducing total
 *   silence on network failure.
 * - Legacy's edit-fetch failure uses a native `alert("Error!!")` (ctrl:386)
 *   instead of `growl` -- reproduced here as a `notify.error(...)` call for
 *   consistency with every other error path on this screen and every
 *   sibling rebuilt screen, not as a native browser `alert()`.
 */
@Component({
  selector: 'app-bed-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './bed-category-master.html',
  styleUrl: './bed-category-master.scss',
})
export class BedCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BedCategoryMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdowns ----
  hierarchyList = signal<HierarchyOption[]>([]);
  occupancyUnitList = signal<OccupancyUnitOption[]>([]);
  billingBedList = signal<BillingBedCategoryOption[]>([]);
  consultationServiceList = signal<ConsultationServiceOption[]>([]);

  // Legacy re-filters the "active" occupancy list in the VIEW itself
  // (`ng-if="info.status == 'A'"`, view:88) on top of the already-"active"
  // endpoint -- reproduced rather than trusting the endpoint's own filter.
  activeOccupancyUnits = computed(() => this.occupancyUnitList().filter((o) => o.status === 'A'));

  // ---- form state (mirrors $scope.bedCategory) ----
  form = signal<BedCategoryFormState>(emptyBedCategoryForm());
  formSubmitted = signal(false);
  private activeIndex: number | undefined; // indexToUpdate
  saveBtnFlag = signal(false); // true while editing -> hides Save
  updateBtnFlag = signal(true); // true while adding -> hides Update

  showConfirmModal = signal(false);

  // ---- list + pagination (server-driven, mirrors PagerService usage) ----
  bedCategoryList = signal<BedCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters only the CURRENT server-fetched
  // page (`bedCategoryList`), not the full dataset -- pagination is
  // server-side (offset/noOfRecordsPerPage) and the `|filter:searchKeyword`
  // in the view only ever sees that one page's rows.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.bedCategoryList();
    return this.bedCategoryList().filter((r) =>
      Object.values(r).some((v) => v != null && typeof v !== 'object' && String(v).toLowerCase().includes(keyword)),
    );
  });

  // ---- Set Hierarchy popup ----
  showHierarchyModal = signal(false);
  // Single shared flag across every row's input, exactly like legacy's
  // scope-global $scope.saveFlag (not reset when the popup opens/closes).
  hierarchySaveFlag = signal(false);

  constructor() {
    this.loadDropdowns();
    this.loadList();
  }

  private loadDropdowns(): void {
    this.api.getActiveHierarchyList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.hierarchyList.set(res.listObject ?? []);
      },
    });
    this.api.getActiveOccupancyUnitList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.occupancyUnitList.set(res.listObject ?? []);
      },
    });
    this.api.getActiveBillingBedCategoryList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.billingBedList.set(res.listObject ?? []);
      },
    });
    this.api.getAutoRenderedServices(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.consultationServiceList.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getBedCategoryList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.bedCategoryList.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getBedCategoryCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  updateField<K extends keyof BedCategoryFormState>(field: K, value: BedCategoryFormState[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
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

  clearSearch(): void {
    this.searchKeyword.set('');
  }

  // ---- required-field validation (mirrors bedcategoryform.$valid) ----
  private validateRequired(): boolean {
    const f = this.form();
    if (!f.bedCategoryCode.trim() || !f.bedCategoryName.trim()) return false;
    if (f.billingBedCategoryId === '' || f.occupancyUnitId === '') return false;
    // Hierarchy is required UNLESS isIcu (view:59-75).
    if (!f.isIcu && f.hierarchyId === '') return false;
    return true;
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- save (create) -- mirrors saveBedCategory()'s two-step confirm flow ----
  openConfirmModal(): void {
    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;
    if (!this.checkDuplicateForSave()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors saveBedCategory()'s duplicate check (ctrl:266-281): any
  // existing row sharing the new hierarchyId is a duplicate, UNLESS the new
  // record is ICU (isIcu skips the whole check, regardless of match).
  private checkDuplicateForSave(): boolean {
    const f = this.form();
    if (f.isIcu) return true;
    const hierarchyId = Number(f.hierarchyId);
    const duplicate = this.bedCategoryList().some((r) => r.hierarchyId === hierarchyId);
    if (duplicate) {
      this.notify.warning('Duplicate Hierarchy Is Not Allowed');
      return false;
    }
    return true;
  }

  save(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveBedCategory({
        bedCategoryCode: f.bedCategoryCode,
        bedCategoryDesc: f.bedCategoryName,
        isBedRetention: f.isBedRetention,
        isIcu: f.isIcu,
        hierarchyId: f.hierarchyId,
        occupancyUnitId: f.occupancyUnitId,
        billingBedCategoryId: f.billingBedCategoryId,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        updatedDate: now,
        createdDate: now,
        serviceId: f.serviceId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Bed Category added Successfully...!!');
            this.resetAfterSuccess();
          } else {
            this.notify.error('Bed Category already exits...!!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // Full $scope.init() equivalent -- resets the form, pagination (back to
  // page 1 / default page size) and refetches all four dropdowns. Only
  // called on a SUCCESSFUL save/update/setHierarchy, exactly like legacy.
  private resetAfterSuccess(): void {
    this.form.set(emptyBedCategoryForm());
    this.formSubmitted.set(false);
    this.activeIndex = undefined;
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.currentPage.set(1);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.loadDropdowns();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:353-391) ----
  // `index` is the row's position in the currently DISPLAYED (search
  // filtered) list, matching legacy's `$index` from
  // `ng-repeat="info in bedCategoryList|filter:searchKeyword"` -- see class
  // doc comment for the resulting index-mismatch quirk this preserves.
  edit(id: number, index: number): void {
    this.activeIndex = index;
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
    this.formSubmitted.set(false);
    this.api.getBedCategoryById(id).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.form.set({
            bedCategoryId: d.bedCategoryId,
            bedCategoryCode: d.bedCategoryCode,
            bedCategoryName: d.bedCategoryDesc,
            hierarchyId: d.hierarchyId,
            occupancyUnitId: d.occupancyUnitId,
            isBedRetention: d.isBedRetention,
            isIcu: d.isIcu,
            billingBedCategoryId: d.billingBedCategoryId,
            serviceId: d.serviceId ?? null,
          });
        } else {
          this.notify.error('Error!!');
        }
      },
      error: () => this.notify.error('Error!!'),
    });
  }

  // ---- update (mirrors updateBedCategory, ctrl:535-608 -- no confirm modal) ----
  update(): void {
    const f = this.form();

    // Patches the in-edit row's hierarchyId into the local list FIRST,
    // unconditionally -- even before validation runs (ctrl:537). Preserved
    // as-is, including the index-mismatch quirk documented on the class.
    if (this.activeIndex !== undefined) {
      const idx = this.activeIndex;
      this.bedCategoryList.update((rows) =>
        rows.map((r, i) => (i === idx ? { ...r, hierarchyId: Number(f.hierarchyId) } : r)),
      );
    }

    if (!f.isIcu) {
      const hierarchyId = Number(f.hierarchyId);
      const duplicateCount = this.bedCategoryList().filter((r) => r.hierarchyId === hierarchyId).length;
      if (duplicateCount > 1) {
        this.notify.warning('Duplicate Hierarchy Is Not Allowed');
        return;
      }
    }

    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;

    const now = formatDateTime(new Date());
    this.api
      .updateBedCategory({
        bedCategoryId: f.bedCategoryId,
        bedCategoryCode: f.bedCategoryCode,
        bedCategoryDesc: f.bedCategoryName,
        isBedRetention: f.isBedRetention,
        isIcu: f.isIcu,
        hierarchyId: f.hierarchyId,
        occupancyUnitId: f.occupancyUnitId,
        billingBedCategoryId: f.billingBedCategoryId,
        updatedBy: this.userId,
        updatedDate: now,
        serviceId: f.serviceId,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Bed category updated Successfully...!!');
            this.resetAfterSuccess();
          } else {
            this.notify.error('Bed category already exits...!!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:395-434) ----
  // Update via the list signal, not a direct mutation of `row` -- see
  // docs/ANGULAR_PENDING_FINDINGS.md entry 12. No revert-on-failure --
  // legacy's updateStatus() has none either (see class doc comment).
  toggleStatus(row: BedCategoryRow): void {
    const status: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.bedCategoryList.update((rows) =>
      rows.map((r) => (r.bedCategoryId === row.bedCategoryId ? { ...r, status } : r)),
    );
    this.api
      .updateStatus({
        bedCategoryId: row.bedCategoryId,
        status,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success('Bed Category Updated Successfully...!!');
          else this.notify.error('Something went wrong...!!');
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  // ---- Set Hierarchy popup ----
  // `getHierarchyPopUp()`/`closePopupsj()` don't exist in legacy -- see
  // class doc comment. These open/close the popup the same way Bootstrap's
  // data-attributes did, with no other side effect.
  openHierarchyModal(): void {
    this.showHierarchyModal.set(true);
  }

  closeHierarchyModal(): void {
    this.showHierarchyModal.set(false);
  }

  // "numbers-only" keystroke filter (view:332's `numbers-only` directive).
  onHierarchyIdInput(index: number, raw: string): void {
    const digits = raw.replace(/[^0-9]/g, '');
    const value = digits === '' ? NaN : Number(digits);
    this.bedCategoryList.update((rows) => rows.map((r, i) => (i === index ? { ...r, hierarchyId: value } : r)));
  }

  // mirrors checkValidHierarchy (ctrl:436-448) -- a single flag shared
  // across every row, only updated on blur of whichever row was last
  // touched.
  checkValidHierarchy(row: BedCategoryRow): void {
    if (Number.isNaN(row.hierarchyId) || row.hierarchyId === null || row.hierarchyId === undefined) {
      this.notify.error('Hierarchy is required');
      this.hierarchySaveFlag.set(false);
    } else {
      this.hierarchySaveFlag.set(true);
    }
  }

  // mirrors setHierarchy (ctrl:449-532).
  setHierarchy(): void {
    const rows = this.bedCategoryList();
    const values = rows.map((r) => Number(r.hierarchyId));
    const isDuplicate = values.some((v, idx) => values.indexOf(v) !== idx);
    if (isDuplicate) {
      this.notify.warning('Duplicate Hierarchy Is Not Allowed');
      return;
    }

    const f = this.form();
    const now = formatDateTime(new Date());
    const dayCareIpBedCategoryList: HierarchyRowPayload[] = rows.map((r) => ({
      bedCategoryCode: r.bedCategoryCode,
      bedCategoryDesc: r.bedCategoryDesc,
      isBedRetention: r.isBedRetention,
      isIcu: r.isIcu,
      hierarchyId: r.hierarchyId,
      occupancyUnitId: r.occupancyUnitId,
      billingBedCategoryId: r.billingBedCategoryId,
      bedCategoryId: r.bedCategoryId,
      updatedBy: this.userId,
      updatedDate: now,
      serviceId: r.serviceId ?? null,
      unitId: this.unitId,
      organizationId: this.organizationId,
    }));

    this.api
      .setHierarchy({
        bedCategoryCode: f.bedCategoryCode,
        bedCategoryDesc: f.bedCategoryName,
        isBedRetention: f.isBedRetention,
        isIcu: f.isIcu,
        hierarchyId: f.hierarchyId,
        occupancyUnitId: f.occupancyUnitId,
        billingBedCategoryId: f.billingBedCategoryId,
        bedCategoryId: f.bedCategoryId,
        updatedBy: this.userId,
        updatedDate: now,
        serviceId: f.serviceId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        dayCareIpBedCategoryList,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Bed category updated Successfully...!!');
            this.resetAfterSuccess();
            this.closeHierarchyModal();
          } else {
            this.notify.error('Bed category already exits...!!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
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
