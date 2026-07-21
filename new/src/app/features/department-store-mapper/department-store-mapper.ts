import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DepartmentStoreMapperService } from './department-store-mapper.service';
import { DepartmentStoreMapperRow, MultiSelectOption } from './department-store-mapper.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/assetManagement/department-store-mapper.html +
 * resources/assets/js/controller/pharmacy/departmentStoreMapperController.js
 * (`departmentStoreMapperController`, no `controllerAs`). Legacy state
 * `departmentStoreMapper` (app.js:20435-20439) has NO `resolve` block at
 * all -- confirmed by reading the state block directly -- so there is no
 * `moduleId` to gate this route on. This is a genuine, by-design gap in
 * legacy itself, the same class as `billingGroupMaster`/`periodicityService`
 * (docs/ANGULAR_REBUILD_PLAYBOOK.md "known caveats" #1), not a rebuild bug
 * and not something to invent a restriction for.
 *
 * ================================================================
 * KNOWN, CONFIRMED-INERT LEGACY BUG -- NOT REPRODUCED (see rationale below)
 * ================================================================
 * The controller's dependency-injection array (ctrl:9-25) and its function
 * signature (ctrl:26-29) have drifted out of position at slots 8/9:
 *
 *   DI array:  ... 'GenericService'(7), 'PagerService'(8), 'promiseFactory'(9), ...
 *   function:  ... GenericService(7),   promiseFactory(8),   PagerService(9),  ...
 *
 * so inside the function body, the local identifier `PagerService` is
 * actually bound to whatever AngularJS injected for the STRING
 * `'promiseFactory'`, and vice versa -- a classic array-annotation DI
 * argument-order bug. Separately, the function signature also declares a
 * 17th parameter, `$modalInstance` (ctrl:29), with no corresponding 17th
 * string in the DI array, so `$modalInstance` is always `undefined`.
 *
 * Proof this is currently INERT, not merely low-severity: a full-file grep
 * for `PagerService`/`promiseFactory` returns exactly three hits -- the two
 * DI-array declarations and ONE dereference, `$scope.pager =
 * PagerService.GetPager(...)` at ctrl:183. That line sits inside a JS block
 * comment spanning ctrl:107 (`/* START FORFOR SEROLOGY MASTER`) to ctrl:198
 * (`*­/`) -- the entire `setNoOfRecords`/`initDeptStoreMappedList`/
 * `getDeptStoreMappedList`/`setPage` block is dead, commented-out code,
 * superseded by the live `deptStoremappedList()` + client-side
 * `PaginationService` pagination actually used below. `promiseFactory` and
 * `$modalInstance` are never dereferenced anywhere in this file, live or
 * dead. No live code path reads either misnamed identifier today.
 *
 * NOT reproduced in this rebuild, and not fixed either -- deliberately
 * left undone, per instruction: Angular's DI is type-token-based
 * (constructor parameter types / `inject()`), not a positional string
 * array, so there is no structural equivalent of "the 8th string in an
 * array vs. the 8th function parameter" to port -- there is nothing here
 * that could even be miswired the same way. Documented here so the record
 * is faithful without requiring reproduction of something the target
 * framework cannot express.
 *
 * ================================================================
 * Other findings, faithfully reproduced (or deliberately NOT ported), below
 * ================================================================
 *
 * - **Two orphaned legacy functions, not ported at all.**
 *   `saveDeptStroreMapper` (ctrl:267-332) and `updateDeptStroreMapper`
 *   (ctrl:445-492) call `/api/globals/saveDeptStoremapper` /
 *   `updateDeptStoremapper` respectively, but neither is referenced by any
 *   `ng-click` anywhere in the view -- only `SaveAndUpdateDeptStroreMapper`
 *   (view:85) is wired to the Save/Update button. There is nothing live to
 *   carry over, so this rebuild implements only the one real, reachable
 *   path (`save()` below, calling `saveAndUpdate()`).
 * - **`saveBtnFlag`/`updateBtnFlag` are dead state, not modeled.** Legacy
 *   sets both (ctrl:33-34, 52-53, 410-411) but the view never reads either
 *   one -- the Save/Update button's title is driven entirely by
 *   `updateDepartmentStoreMapper` (view:85, `{{updateDepartmentStoreMapper
 *   ? 'Update' : 'Save'}}`), and the SAME `ng-click` fires regardless of
 *   mode. Reproduced here as a single `save()` method and a single
 *   `updateMode` signal (mirrors `updateDepartmentStoreMapper` only) --
 *   `saveBtnFlag`/`updateBtnFlag` have zero observable effect and are
 *   intentionally not modeled, same precedent as this codebase's
 *   qms-department-master (`$scope.dt`) and doctor-cabin-mapper
 *   (`orderBy:column:reverse`).
 * - **`initDeptStoreMapped()`/`$scope.deptStoreMapped` are also dead.**
 *   Defined (ctrl:72-88) and called from `cancelUpdate()` and the two
 *   orphaned functions above, but `deptStoreMapped` itself is never read by
 *   anything else in the file (not sent to any live endpoint, not bound in
 *   the view) -- not modeled here for the same reason.
 * - **Entirely client-side pagination/search -- a real, material
 *   divergence from this codebase's other Mapper siblings
 *   (user-store-mapper/doctor-cabin-mapper use real server-side offset
 *   pagination).** `deptStoremappedList()` (ctrl:337-366) fetches the
 *   WHOLE mapping list in one shot -- no offset/pageSize is ever sent to
 *   the backend. Legacy's own `PaginationService` (resources/assets/js/service/paginationService.js)
 *   then paginates/searches entirely in memory: `pagination.search` is
 *   never assigned anywhere in this controller (`searchKeyword`, the
 *   view's real search box, is a completely separate variable), so
 *   `PaginationService.searchData` always treats `pagination.search` as
 *   empty and slices the FULL, unfiltered list into `pagination.data` by
 *   page. The view then applies a SECOND, independent filter directly on
 *   top of that already-paginated slice (`pagination.data |
 *   orderBy:column:reverse | filter:searchKeyword`, view:115) -- so
 *   `searchKeyword` only ever filters WITHIN the current page, the same
 *   "search filters only the current page" quirk already established in
 *   this codebase's user-store-mapper/doctor-cabin-mapper. `column`/
 *   `reverse` are never assigned anywhere and no header has a click
 *   handler -- the `orderBy` half is permanently inert and is not modeled,
 *   same precedent as doctor-cabin-mapper's identical dead-sort callout.
 *   Reproduced below via `pagedRows`/`displayedRows`, both computed
 *   entirely client-side with NO server call for page-size change or
 *   Next/Previous/page-number navigation -- matching
 *   `PaginationService.setPage`/`setFirstPage`/etc., which only ever
 *   re-slice already-fetched data, never re-fetch.
 * - **A real, LIVE (not dead-code) wiring gap: selecting/deselecting
 *   options in either multiselect fires a real backend call whose result
 *   never reaches the visible table.** `onSelectionChanged`
 *   (ctrl:527-529) fires `GetDepartmentStoreMapperNew` (ctrl:532-557) on
 *   every store/department selection change, which POSTs the selected
 *   id arrays and assigns the response to `$scope.getDeptStoremappedList`
 *   -- but the visible table is bound to `pagination.data`
 *   (view:115), which this handler never touches (no
 *   `PaginationService.searchData`/`setPagedData` call anywhere in
 *   `GetDepartmentStoreMapperNew`). So changing the Store/Department
 *   selections genuinely calls the backend every time, but has NO visible
 *   effect on the table -- confirmed via full-file read, no `$watch`
 *   anywhere reconciling the two. Reproduced faithfully below:
 *   `onSelectionChanged()` fires the real call and stores its result in
 *   `lastFilterPreview` (deliberately not read by the template, not fed
 *   into `rows`) rather than inventing a working live-filter.
 * - **`edit()`'s dropdown-index lookup has no bounds guard (ctrl:425-428).**
 *   `getMultipleStoreList.findIndex(x => x.id === deptObj.id)` /
 *   the department equivalent can return `-1` if the fetched store/dept id
 *   isn't currently present in the loaded options list (e.g. Edit clicked
 *   before `storeList()`/`deptList()` finish loading, or the mapped
 *   store/department was since deactivated). Legacy does not guard against
 *   this -- `getMultipleStoreList[-1]` is `undefined`, and `selectedStore`
 *   silently becomes `[undefined]`. Reproduced verbatim below, no `-1` check
 *   added.
 * - **No `<modal>` directive used anywhere in this screen.** Save/Update is
 *   a direct call with no confirm-dialog step at all (unlike
 *   amenity-master/qms-department-master/pass-type-master) -- none added here.
 * - **`toggleStatus` unconditionally resets the in-progress edit/picker
 *   state on success (ctrl:369-403), regardless of which row was
 *   toggled.** `updateStatus()`'s success handler calls `initCommon()`
 *   only -- it does not reload the mapped list and does not touch
 *   `pagination.data`, but it DOES reset `deptObj` (and, indirectly here,
 *   `updateMode`/the picker) back to create-mode defaults. A real, if odd,
 *   quirk: toggling any row's status while mid-edit of a different row
 *   silently discards that in-progress edit. Reproduced as-is in
 *   `toggleStatus()` below; the row's own status flips optimistically via
 *   `rows.update()` per this codebase's established convention (the
 *   view's checkbox is two-way bound in legacy, so the visual flip happens
 *   immediately regardless of the response either way).
 * - **`$rootScope.startLoader()`/`stopLoader()` calls reproduced 1:1** via
 *   `LoaderService`, including two real asymmetries: `storeList()`/
 *   `deptList()` never call `startLoader()` at all but do call
 *   `stopLoader()` on success (ctrl:209/244); `SaveAndUpdateDeptStroreMapper`
 *   never calls `startLoader()` either, and calls `stopLoader()` only on
 *   the success branch, not the error branch (ctrl:585-608).
 */
@Component({
  selector: 'app-department-store-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './department-store-mapper.html',
  styleUrl: './department-store-mapper.scss',
})
export class DepartmentStoreMapper {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(DepartmentStoreMapperService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly orgId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists (mirrors getMultipleStoreList/getMultipleDepartmentList) ----
  storeOptions = signal<MultiSelectOption[]>([]);
  deptOptions = signal<MultiSelectOption[]>([]);

  // ---- multiselect state (mirrors selectedStore/selectedDepartment) ----
  // NOT the classic dual-checklist-with-commit-button pattern used by
  // user-store-mapper/doctor-cabin-mapper -- legacy's own
  // `ng-dropdown-multiselect` commits selections immediately as the user
  // checks/unchecks, with no separate "<< >>" commit step. Modeled here as
  // two checkbox lists bound directly to these arrays. See class doc.
  selectedStores = signal<MultiSelectOption[]>([]);
  selectedDepartments = signal<MultiSelectOption[]>([]);

  // Real, live call fired on every selection change -- deliberately NOT
  // read by the template. See class doc comment's "live wiring gap" finding.
  lastFilterPreview = signal<DepartmentStoreMapperRow[]>([]);

  // ---- form/edit-mode state (mirrors updateDepartmentStoreMapper/deptObj.deptStoreMapperId) ----
  updateMode = signal(false);
  private currentMapperId = signal<number | ''>('');

  // ---- full mapping list + entirely client-side pagination (see class doc) ----
  rows = signal<DepartmentStoreMapperRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.rows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  /** Mirrors `PaginationService.setPagedData` -- slices the (always
   * unfiltered -- `pagination.search` is dead, see class doc) full list by
   * page/size, entirely client-side. */
  pagedRows = computed(() => {
    const size = this.pageSize();
    const offset = (this.currentPage() - 1) * size;
    return this.rows().slice(offset, offset + size);
  });

  /** Mirrors the view's second, independent `filter:searchKeyword` applied
   * on top of the already-paginated slice (view:115) -- searches only the
   * CURRENT page, not the full dataset. */
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.pagedRows();
    return this.pagedRows().filter(
      (r) => r.storeName?.toLowerCase().includes(keyword) || r.deptName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadStoreOptions();
    this.loadDeptOptions();
    this.loadMappedList();
  }

  // ctrl:201-227 (storeList) -- no startLoader() call in legacy, only
  // stopLoader() on success; raw-array response, no envelope.
  private loadStoreOptions(): void {
    this.api.getStoreListRaw(this.orgId, this.unitId, this.userId).subscribe({
      next: (list) => {
        this.loader.stop();
        this.storeOptions.set((list ?? []).map((s) => ({ id: s.id, label: s.store })));
      },
    });
  }

  // ctrl:233-259 (deptList) -- same "no startLoader, stopLoader-on-success-only" shape.
  private loadDeptOptions(): void {
    this.api.getDeptListRaw(this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.deptOptions.set((res.listObject ?? []).map((d) => ({ id: d.deptId, label: d.deptName })));
      },
    });
  }

  // ctrl:337-366 (deptStoremappedList) -- balanced startLoader()/stopLoader().
  private loadMappedList(): void {
    this.loader.start();
    this.api.getMappedList(this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.rows.set(res.listObject ?? []);
        this.currentPage.set(1);
      },
      error: () => this.loader.stop(),
    });
  }

  onPageSizeChange(): void {
    // mirrors PaginationService.setTotalPages -- resets to page 1, no server call.
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    // mirrors PaginationService.setPage/setFirstPage/etc -- purely client-side re-slice, no server call.
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- multiselect (mirrors addCabin-equivalent checkbox toggling + onSelectionChanged) ----
  isStoreSelected(id: number): boolean {
    return this.selectedStores().some((s) => s.id === id);
  }

  isDeptSelected(id: number): boolean {
    return this.selectedDepartments().some((d) => d.id === id);
  }

  toggleStoreSelection(option: MultiSelectOption, checked: boolean): void {
    this.selectedStores.update((list) => (checked ? [...list, option] : list.filter((s) => s.id !== option.id)));
    this.onSelectionChanged();
  }

  toggleDeptSelection(option: MultiSelectOption, checked: boolean): void {
    this.selectedDepartments.update((list) => (checked ? [...list, option] : list.filter((d) => d.id !== option.id)));
    this.onSelectionChanged();
  }

  /** Mirrors `multiSelectEvents.onSelectionChanged` -> `GetDepartmentStoreMapperNew`
   * (ctrl:527-557). See class doc comment: real call, response deliberately
   * NOT wired to `rows`/the visible table -- reproducing the actual gap, not fixing it. */
  private onSelectionChanged(): void {
    this.loader.start();
    this.api
      .getFilteredListNew({
        orgId: this.orgId,
        unitId: this.unitId,
        storeIds: this.selectedStores().map((s) => s.id),
        deptIds: this.selectedDepartments().map((d) => d.id),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          this.lastFilterPreview.set(res.listObject ?? []);
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- save/update (mirrors SaveAndUpdateDeptStroreMapper, ctrl:559-608) ----
  save(): void {
    const storeIds = this.selectedStores().map((s) => s.id);
    const deptIds = this.selectedDepartments().map((d) => d.id);
    if (storeIds.length === 0) {
      this.notify.error('Store is required');
      return;
    }
    if (deptIds.length === 0) {
      this.notify.error('Department is required');
      return;
    }

    this.api
      .saveAndUpdate({
        deptStoreMapperId: this.currentMapperId(),
        orgId: this.orgId,
        unitId: this.unitId,
        storeIds,
        deptIds,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.loader.stop(); // ctrl:590 -- success stops the loader; error does not (see class doc).
            if (res.message) {
              this.notify.warning(res.message);
            } else {
              this.notify.success('Department Store Mapped Save Successfully..!');
            }
            this.cancelUpdate();
            this.loadMappedList();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  // ---- cancel update (mirrors cancelUpdate, ctrl:610-616) ----
  cancelUpdate(): void {
    this.updateMode.set(false);
    this.currentMapperId.set('');
    this.selectedStores.set([]);
    this.selectedDepartments.set([]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:407-441) ----
  edit(deptStoreMapperId: number): void {
    this.loader.start();
    this.api.getById(this.orgId, this.unitId, deptStoreMapperId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.object) {
          window.alert('Error In Fetching Data'); // ctrl:432-434
          return;
        }
        const detail = res.object;
        this.updateMode.set(true);
        this.currentMapperId.set(detail.deptStoreMapperId);
        // ctrl:425-428 -- no bounds guard on a -1 findIndex result; see class doc.
        const storeIndex = this.storeOptions().findIndex((x) => x.id === detail.id);
        const deptIndex = this.deptOptions().findIndex((x) => x.id === detail.deptId);
        this.selectedStores.set([this.storeOptions()[storeIndex]]);
        this.selectedDepartments.set([this.deptOptions()[deptIndex]]);
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:369-403) ----
  toggleStatus(row: DepartmentStoreMapperRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((list) =>
      list.map((r) => (r.deptStoreMapperId === row.deptStoreMapperId ? { ...r, status: nextStatus } : r)),
    );
    this.loader.start();
    this.api.updateStatus(this.orgId, this.unitId, row.deptStoreMapperId, nextStatus).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Status Update Successfully..!');
          // ctrl:389 -- updateStatus()'s success handler only calls
          // initCommon(); no list reload. See class doc comment.
          this.cancelUpdate();
        } else {
          this.notify.error(res.message ?? 'Something went wrong...!!');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}
