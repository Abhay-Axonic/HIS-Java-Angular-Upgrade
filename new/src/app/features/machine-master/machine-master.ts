import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MachineMasterService } from './machine-master.service';
import { MachineMasterRecord, emptyMachineMasterRecord } from './machine-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/LIS/machine-Master.html +
 * resources/assets/js/controller/lis/unit/machineMasterController.js
 * (self-registered... no -- state-config-registered `machineMaster`, app.js
 * ~line 19446, `resolve` block gating on module id 840, `controllerAs`
 * NOT used -- classic `$scope` wiring, confirmed by reading the controller
 * in full: 0 `vm.*`/`self.*` assignments).
 *
 * **SEVERITY-1 FINDING, corrected + disclosed here, not silently ported:**
 * legacy's pagination is split across two different functions that hit two
 * DIFFERENT backend endpoints:
 *  - `initMachineMasterList()` (ctrl:89-114) -- called on controller init,
 *    on the "Show entries" dropdown change, and after every successful
 *    save/update -- calls the real `LIST_MACHINE_MASTER`
 *    (`/LIS/global/listMachineMaster/...`) + `COUNT_MACHINE_MASTER`
 *    (`/LIS/global/getTotalMachineMasterRecord/...`) endpoints. Correct.
 *  - `getMachineMasterList()` (ctrl:116-135) -- called from `setPage(page,
 *    true)` (ctrl:140-152), i.e. from EVERY pagination link click
 *    (First/Previous/a page number/Next/Last all pass `'true'` as the
 *    second argument, view:188-200) -- calls `LIST_HEADER_MASTER`
 *    (`/LIS/global/listHeaderMaster/...`) instead.
 *
 * `listHeaderMaster` is NOT a shared/generic endpoint that happens to also
 * serve machine data -- confirmed by reading the Java backend directly:
 * it's `HeaderMasterController.listHeaderMaster`
 * (legacy/LIS/src/main/java/com/param/lis/unit/controller/HeaderMasterController.java:93-109),
 * backed by `HeaderMasterDaoImpl.listHeaderMaster`
 * (.../dao/HeaderMasterDaoImpl.java:160-186) running the
 * `GET_PAGINATED_HEADER_MASTER_LIST` named query
 * (legacy/Entity/src/main/java/com/param/entity/lis/unit/HeaderMaster.java:97-109),
 * which selects from `lab.m_header_master` -- a completely different LIS
 * master entity (lab test "headers"/panel groupings) than
 * `lab.m_machine_master` (`GET_PAGINATED_MACHINE_MASTER_LIST`,
 * MachineMaster.java:62-75). Both DAOs return the same `Response` shape
 * (`listObject` in the same constructor slot) so the JS-side assignment
 * `$scope.commonList = response.data.listObject` never throws -- the bug is
 * silent, not a visible error.
 *
 * The practical effect on any pagination click (not just "page 2+" --
 * clicking "First" from page 2 back to page 1 ALSO goes through
 * `setPage(page, 'true')`, so it's broken too): `commonList` gets replaced
 * with Header Master rows shaped as `GlobalCommonDto` (`id`/`code`/`desc`/
 * `status` -- legacy/LIS/src/main/java/com/param/lis/global/dto/GlobalCommonDto.java),
 * not `MachineMasterDto` (`machineId`/`code`/`machineDesc`/`status`). The
 * view's Code column (`info.code`) still renders something (`GlobalCommonDto`
 * has `code` too, so an unrelated header's code string displays as if it
 * were a machine's code), but Machine Description
 * (`info.machineDesc`) renders blank (`GlobalCommonDto` has `desc`, not
 * `machineDesc`), and critically `info.machineId` is `undefined`
 * (`GlobalCommonDto` has `id`, not `machineId`) -- so the edit pencil
 * (`showUpdateBtn(info.machineId)`) and the status toggle
 * (`updateStatus(info.machineId, info.status)`) on any row displayed this
 * way call `/LIS/global/getMachinebyId/{orgId}/{unitId}/undefined` and
 * `/LIS/global/activeInactiveMachine/{orgId}/undefined/{status}` --
 * `undefined` fails Spring's `Integer` path-variable binding, so both
 * actions error out. This is a real, live, cross-cutting bug: wrong-entity
 * data displayed, cascading into broken edit/toggle actions on it. Per this
 * project's standing silent-bug-correction policy (a bug that cascades into
 * the write path gets corrected + disclosed, not silently ported --
 * `hospitalAccountMaster`/`currencyMaster`/`bedMaster` precedent), this
 * rebuild's `MachineMasterService` calls the REAL `listMachineMaster`
 * endpoint for every pagination action, not just the initial load -- see
 * `machine-master.service.ts`'s class comment.
 *
 * Call-SHAPE parity is otherwise preserved deliberately: legacy's
 * `getMachineMasterList()` only re-fetches the list, never the count
 * (`$scope.commonListCount` is cached from the one-time
 * `initMachineMasterList()` call and reused by `PagerService.GetPager` on
 * every `setPage`) -- `goToPage()` below mirrors that (1 list-only call per
 * page turn, using the corrected endpoint), while `reload()` (initial load,
 * page-size change, post-save) fires both calls, matching
 * `initMachineMasterList()` exactly.
 *
 * **Other legacy behavior preserved deliberately:**
 * - `$scope.commonList`/`$scope.commonListCount` are assigned
 *   unconditionally from the response, regardless of `status` (ctrl:106-113)
 *   -- `list`/`totalCount` below are set the same way (not gated on
 *   `res.status === 'success'`), so a zero-record page correctly clears the
 *   table instead of leaving stale rows.
 * - Pagination window: `PagerService.GetPager`
 *   (resources/assets/js/controller/lis/common/pagerService.js:12-66) caps
 *   the page-number strip at 10 visible buttons with a sliding window, not
 *   "show every page" -- `pagerWindow` below reproduces that exact
 *   start/end-page arithmetic.
 * - `updateStatus()`'s success handler resets the create/edit form back to
 *   create mode (`$scope.initCommon()`, ctrl:181) even if the user was
 *   mid-edit of a different row -- a real, if mildly surprising, legacy
 *   quirk, reproduced by `toggleStatus()` below.
 * - Search (`ng-model="searchKeyword"` + `filter:searchKeyword`, view:124 +
 *   166) is a pure client-side filter over the CURRENTLY LOADED PAGE only --
 *   there is no server-side search endpoint for this screen at all.
 *   `filteredList` below reproduces that same page-local, no-server-call
 *   behavior (checked against `code`/`machineDesc`, the two fields
 *   AngularJS's built-in string filter would realistically match against).
 * - The 4 sort-caret icons (Code/Machine Description columns, view:139-159)
 *   call `sortColumn("code")`/`sortColumn("machineDesc")`, a function that
 *   does not exist anywhere in this controller (confirmed by grep) -- same
 *   dead-decoration class as other already-built screens (see
 *   docs/ANGULAR_PENDING_FINDINGS.md entry 111 precedent). Kept as inert
 *   markup in the template, not wired to anything.
 * - The shared `<modal visible="showModal">` directive (view:71) is present
 *   in legacy's markup but `showModal` is never set truthy anywhere in this
 *   controller -- the modal never actually shows in legacy today. Omitted
 *   entirely from this rebuild rather than reproducing genuinely dead
 *   markup (standing shared-directive-divergence policy, entry 55).
 * - `showUpdateBtn`'s error path uses a native `alert("Error In Fetching
 *   Data")` (ctrl:216) -- reproduced here via the shared toast
 *   (`notify.error(...)`) instead of a blocking native alert, consistent
 *   with every other error path on this screen and every other already-
 *   built screen in this codebase.
 */
@Component({
  selector: 'app-machine-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './machine-master.html',
  styleUrl: './machine-master.scss',
})
export class MachineMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(MachineMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organisationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common / $scope.machineMaster) ----
  record = signal<MachineMasterRecord>(emptyMachineMasterRecord(this.unitId, this.organisationId, this.userId));
  saveMode = signal(true); // true = create (saveBtnFlag), false = update (updateBtnFlag)
  submitted = signal(false); // drives *required*/*maxlength* error display, like show-errors-check-validity

  // ---- list + pagination state ----
  list = signal<MachineMasterRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  searchKeyword = signal('');
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));

  // Mirrors PagerService.GetPager's sliding 10-button window exactly
  // (resources/assets/js/controller/lis/common/pagerService.js:12-66).
  pagerWindow = computed(() => {
    const totalPages = this.totalPages();
    const currentPage = this.currentPage();
    let startPage: number;
    let endPage: number;
    if (totalPages <= 10) {
      startPage = 1;
      endPage = totalPages;
    } else if (currentPage <= 6) {
      startPage = 1;
      endPage = 10;
    } else if (currentPage + 4 >= totalPages) {
      startPage = totalPages - 9;
      endPage = totalPages;
    } else {
      startPage = currentPage - 5;
      endPage = currentPage + 4;
    }
    return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
  });

  // Client-side filter over the currently loaded page only -- see class
  // comment (legacy has no server-side search for this screen at all).
  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (row) => row.code.toLowerCase().includes(keyword) || row.machineDesc.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.reload();
  }

  // Mirrors initMachineMasterList(): fetches list + count together, always
  // for offset 0 (legacy's $scope.offset is fixed at 0 for the lifetime of
  // the controller -- confirmed by reading the whole file, nothing ever
  // reassigns it), and unconditionally overwrites list/count regardless of
  // response status (ctrl:106-113).
  private reload(): void {
    this.currentPage.set(1);
    this.api.getList(this.organisationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => this.list.set(res.listObject ?? []),
    });
    this.api.getCount(this.organisationId, this.unitId).subscribe({
      next: (res) => this.totalCount.set(typeof res.object === 'number' ? res.object : 0),
    });
  }

  // Mirrors setNoOfRecords() -> initMachineMasterList().
  onPageSizeChange(): void {
    this.reload();
  }

  // Mirrors setPage(page, true) -> getMachineMasterList() (corrected to
  // call the real listMachineMaster endpoint -- see class comment). Only
  // re-fetches the list, matching legacy's call shape (count is not
  // re-fetched per page turn).
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organisationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => this.list.set(res.listObject ?? []),
    });
  }

  // ---- create/update form ----
  isCodeInvalid(): boolean {
    const code = this.record().code;
    return this.submitted() && (code.trim().length === 0 || code.length > 10);
  }

  isDescInvalid(): boolean {
    const desc = this.record().machineDesc;
    return this.submitted() && (desc.trim().length === 0 || desc.length > 256);
  }

  updateField<K extends keyof MachineMasterRecord>(field: K, value: MachineMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // Mirrors save()/update() -- both branches share identical validation +
  // reset + reload behavior, differing only in which endpoint/HTTP verb is
  // used (ctrl:225-266 vs 268-307).
  save(): void {
    this.submitted.set(true);
    const r = this.record();
    if (!r.code.trim() || r.code.length > 10 || !r.machineDesc.trim() || r.machineDesc.length > 256) {
      return;
    }

    const payload: MachineMasterRecord = {
      ...r,
      machineId: r.machineId ?? undefined,
      // Legacy always sends the current session's org id here
      // (initMachineMaster() uses `$scope.orgId`, NOT `common.orgId`/
      // `common.organisationId` -- ctrl:76), not whatever org the fetched
      // record itself carried.
      organisationId: this.organisationId,
    };

    const request = this.saveMode() ? this.api.create(payload) : this.api.update(payload);
    request.subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Saved successfully');
          this.resetForm();
          this.reload();
        } else {
          this.notify.error(res.message ?? 'Save failed');
        }
      },
      error: () => this.notify.error('Save failed'),
    });
  }

  private resetForm(): void {
    this.record.set(emptyMachineMasterRecord(this.unitId, this.organisationId, this.userId));
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // Mirrors showUpdateBtn(commonId) -> getMachinebyId (ctrl:196-223).
  edit(machineId: number): void {
    this.api.getById(this.organisationId, this.unitId, machineId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.record.set(res.object);
          this.saveMode.set(false);
          this.submitted.set(false);
        } else {
          this.notify.error('Error In Fetching Data');
        }
      },
      error: () => this.notify.error('Error In Fetching Data'),
    });
  }

  // Mirrors updateStatus(commonId, status) (ctrl:159-194) -- including the
  // success-path form reset back to create mode, see class comment.
  toggleStatus(row: MachineMasterRecord): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.changeStatus(this.organisationId, row.machineId as number, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) =>
            rows.map((r) => (r.machineId === row.machineId ? { ...r, status: nextStatus } : r)),
          );
          this.notify.success(res.message ?? 'Status updated');
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
      error: () => this.notify.error('Update failed'),
    });
  }
}
