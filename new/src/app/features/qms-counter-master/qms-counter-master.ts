import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { QmsCounterMasterService } from './qms-counter-master.service';
import {
  DepartmentOption,
  KioskOption,
  QMSCounterCommon,
  QMSCounterDetail,
  QMSCounterRow,
  emptyQMSCounterCommon,
} from './qms-counter-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/QMSMaster/QMS-counter-Master.html +
 * resources/assets/js/controller/qms/QMScounterMasterController.js. State
 * `QMScounterMaster` (app.js ~line 20050-20073), moduleId 797, no
 * `controllerAs` (plain `$scope`). Sibling of this codebase's
 * qms-department-master (nearly identical architecture -- same author,
 * same era) and qms-kiosk-master (the kiosk FK this screen's own dropdown
 * reads from).
 *
 * Confirmed real, working CRUD: save (ctrl:216-268), update (ctrl:315-374),
 * edit-populate `showUpdateBtn` (ctrl:270-312), status toggle
 * `updateStatus` (ctrl:376-409), and a read-only "Department Details"
 * lookup popup (`departmentDetails`, ctrl:412-442) -- all wired to real
 * endpoints, see qms-counter-master.service.ts.
 *
 * **Two real bugs, faithfully reproduced here, NOT fixed (per audit +
 * explicit rebuild instruction):**
 * 1. The "Show N entries" page-size dropdown (`ng-change="setNoOfRecords()"`,
 *    view:114) calls `$scope.initQMSDeptMasterList(...)` (ctrl:126) -- a
 *    function never defined anywhere in QMScounterMasterController.js
 *    (confirmed via full-file grep: the ONLY occurrence of that name in the
 *    entire file is this one call site; it's presumably a copy-paste/typo
 *    of `initQMSCounterMasterList`, the function that actually exists).
 *    Every page-size change throws `initQMSDeptMasterList is not a
 *    function` in legacy, today. Reproduced below via a direct `throw` in
 *    `onPageSizeChange()`, the same way this codebase's amenity-master
 *    reproduces its own dead search handler -- NOT a working reload.
 *    Ordinary Next/Previous/page-number pagination is unaffected: that goes
 *    through a separate, correctly-defined legacy function (`qmsMasterList`,
 *    ctrl:173-192), reproduced as a normal working `goToPage()` below.
 * 2. `updateStatus()` (ctrl:376-409) sends `updatedBy: $scope.updatedBy` --
 *    a scope property never assigned anywhere in this controller (only
 *    `$scope.userId` is ever set, and IS used correctly by save/update) --
 *    so every status-toggle silently sends `updatedBy: undefined`, losing
 *    the audit trail for who flipped the status. Reproduced by
 *    `toggleStatus()` below always passing `undefined` explicitly for that
 *    one call, while `confirmSave()`/`update()` correctly use the real
 *    session user id, matching legacy's exact asymmetry.
 *
 * **Deliberate deviations/preserved quirks, NOT bugs in this rebuild:**
 * - Save has a confirm modal (generic "Are you sure you want to Save?",
 *   ctrl:212-268 via the shared `<modal>` directive / `PopupOkBtn()`);
 *   Update (ctrl:315-374) has NO confirm step at all -- posts directly on
 *   click. Same asymmetry as this codebase's qms-department-master.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's x close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared-
 *   `<modal>`-directive `cancel()` crash (`commonPopUpDirectives.js`
 *   hardcodes `ng-click="cancel(); stopLoader();"`, and
 *   QMScounterMasterController.js never defines `$scope.cancel`) -- a
 *   working close is an accepted standing improvement, not a parity gap.
 * - Both save's and update's completion handlers call
 *   `$scope.initQMSMaster()` UNCONDITIONALLY afterward (ctrl:258, ctrl:367)
 *   -- even a failed (app-level error) save/update wipes the form back to
 *   blank create-mode defaults, exactly like this codebase's
 *   qms-department-master. Reproduced via `resetFormFields()` called
 *   outside the `res.status === 'success'` branch in both `confirmSave()`
 *   and `update()` below.
 * - Selecting a kiosk (`getQMSActiveDeptMasterListByKioskId`, ctrl:103-123)
 *   refreshes the AVAILABLE department options for that kiosk but never
 *   clears whatever departments are already selected -- reproduced by
 *   `onKioskChange()` leaving `selectedDepartmentIds` untouched, even
 *   though the freshly loaded `departmentOptions` may no longer include
 *   some of those ids.
 * - The dept-selection-required check ("Please select at least one QMS
 *   department") runs BEFORE the code/description/kiosk required-field
 *   check in both `save()` (ctrl:219-226) and `update()` (ctrl:319-326) --
 *   reproduced in that same order in `openConfirmModal()`/`update()` below.
 * - SAVE's `departmentList` payload is the FULL `{id,label}` widget shape
 *   (ctrl:227); UPDATE's is independently rebuilt as `{id}`-only
 *   (ctrl:327-336) -- a real asymmetry between the two payloads, kept as-is
 *   rather than normalized to match each other (see models.ts).
 * - `getActiveDeptListByKioskId`'s response has no `status==='success'`
 *   gate in legacy (ctrl:111-122) -- reproduced by not gating on it here
 *   either, same as qms-department-master's identical convention for its
 *   own kiosk/reception lookups.
 */
@Component({
  selector: 'app-qms-counter-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './qms-counter-master.html',
  styleUrl: './qms-counter-master.scss',
})
export class QmsCounterMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(QmsCounterMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- kiosk dropdown / department multiselect ----
  kioskOptions = signal<KioskOption[]>([]);
  departmentOptions = signal<DepartmentOption[]>([]);
  selectedDepartmentIds = signal<number[]>([]);

  // ---- form state (mirrors $scope.common) ----
  common = signal<QMSCounterCommon>(emptyQMSCounterCommon(this.userId, this.organizationId, this.unitId));
  formSubmitted = signal(false);
  saveBtnFlag = signal(true); // ng-if="saveBtnFlag" -- Save button shown while adding
  updateBtnFlag = signal(false); // ng-if="updateBtnFlag" -- Update button shown while editing
  showConfirmModal = signal(false);

  // ---- read-only "Department Details" popup (mirrors departmentDetails, ctrl:412-442) ----
  viewDepartmentList = signal<QMSCounterDetail['departmentList']>([]);
  showDeptDetailsModal = signal(false);

  // ---- list + server-side pagination ----
  rows = signal<QMSCounterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters only the CURRENT server-fetched
  // page (`commonList | filter:searchKeyword`, view:171), not the full
  // dataset -- same pattern as this codebase's qms-department-master.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.rows();
    return this.rows().filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadKioskOptions();
    this.loadListAndCount();
  }

  // ctrl:89-100.
  private loadKioskOptions(): void {
    this.api.getActiveKiosks(this.organizationId, this.unitId).subscribe({
      next: (res) => this.kioskOptions.set(res.listObject ?? []),
    });
  }

  // ctrl:103-123 -- remaps the raw {qmsDeptMasterId,qmsDeptName} pairs into
  // {id,label} here, matching where legacy itself does that remap (in the
  // controller, ctrl:115-118), not in the service.
  private loadDepartmentOptionsForKiosk(kioskId: number): void {
    this.api.getActiveDepartmentsByKiosk(this.organizationId, this.unitId, kioskId).subscribe({
      next: (res) => {
        this.departmentOptions.set((res.listObject ?? []).map((d) => ({ id: d.qmsDeptMasterId, label: d.qmsDeptName })));
      },
    });
  }

  // mirrors initQMSCounterMasterList (ctrl:129-170): fetches list + count
  // together, always from offset 0, and forces the pager back to page 1
  // once the count responds -- used for initial load and after a
  // successful save/update.
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors qmsMasterList (ctrl:173-192) via setPage(page,true): re-fetches
  // just the list at the new offset, no count refetch, no forced reset.
  private loadPage(page: number): void {
    this.loader.start();
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof QMSCounterCommon>(field: K, value: QMSCounterCommon[K]): void {
    this.common.update((c) => ({ ...c, [field]: value }));
  }

  // ---- kiosk selection (mirrors ng-change="getQMSActiveDeptMasterListByKioskId(...)") ----
  onKioskChange(kioskId: number): void {
    this.updateField('qmsKioskMasterId', kioskId);
    this.loadDepartmentOptionsForKiosk(kioskId);
    // Deliberately NOT clearing selectedDepartmentIds -- see class doc comment.
  }

  onDepartmentSelectionChange(select: HTMLSelectElement): void {
    this.selectedDepartmentIds.set(Array.from(select.selectedOptions).map((o) => Number(o.value)));
  }

  // ---- required-field validation (mirrors QMSCounterForm.$valid) ----
  private validateRequired(): boolean {
    const c = this.common();
    return !!(c.qmsCounterCode.trim() && c.qmsCounterDesc.trim() && c.qmsKioskMasterId !== '');
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  /**
   * Faithful crash replication -- see class doc comment. Legacy's "Show N
   * entries" dropdown wires `ng-change="setNoOfRecords()"`, which calls
   * `$scope.initQMSDeptMasterList(...)`, a function never defined anywhere
   * in QMScounterMasterController.js. Reproduced via a direct throw here --
   * NOT a working reload, and NOT omitted. Ordinary pagination
   * (`goToPage()` above) is unaffected.
   */
  onPageSizeChange(): void {
    throw new TypeError('initQMSDeptMasterList is not a function');
  }

  // ---- save (create) -- mirrors save()'s confirm-modal flow (ctrl:216-244) ----
  openConfirmModal(): void {
    // ctrl:219-226 -- the department-selection check runs BEFORE the
    // code/description/kiosk required-field check.
    if (this.selectedDepartmentIds().length === 0) {
      this.notify.warning('Please select at least one QMS department');
      return;
    }
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const c = this.common();
    // ctrl:227 -- the FULL {id,label} widget shape, sent as-is.
    const departmentList: DepartmentOption[] = this.departmentOptions().filter((d) =>
      this.selectedDepartmentIds().includes(d.id),
    );
    this.api.save({ ...c, departmentList }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.formSubmitted.set(false); // mirrors $broadcast('show-errors-reset')
          this.loadListAndCount();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
        this.resetFormFields(); // mirrors the unconditional initQMSMaster() call (ctrl:258)
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  // resets the form/flags/pagination-counters back to create-mode defaults
  // -- mirrors initQMSMaster() (ctrl:19-60).
  private resetFormFields(): void {
    this.common.set(emptyQMSCounterCommon(this.userId, this.organizationId, this.unitId));
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
    this.selectedDepartmentIds.set([]);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:270-312) ----
  edit(row: QMSCounterRow): void {
    this.loader.start();
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.common.set({
      ...emptyQMSCounterCommon(this.userId, this.organizationId, this.unitId),
      qmsCounterId: row.qmsCounterId,
    });
    this.selectedDepartmentIds.set([]);
    this.api.getById(row.qmsCounterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.object) {
          const d = res.object;
          this.common.set(d);
          this.loadDepartmentOptionsForKiosk(d.qmsKioskMasterId as number);
          this.selectedDepartmentIds.set((d.departmentList ?? []).map((x) => x.qmsDeptMasterId));
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update(), ctrl:315-374 -- no confirm modal) ----
  update(): void {
    // ctrl:319-326 -- department check first, same order as save().
    if (this.selectedDepartmentIds().length === 0) {
      this.notify.warning('Please select at least one QMS department');
      return;
    }
    // ctrl:341-343 -- {id}-only shape, NOT the {id,label} shape save() sends
    // (see class/model doc comments).
    const departmentList = this.selectedDepartmentIds().map((id) => ({ id }));
    // ctrl:341-342 patches these two fields unconditionally.
    this.common.update((c) => ({ ...c, organisationId: this.organizationId, unitId: this.unitId }));
    if (!this.validateRequired()) return;

    const c = this.common();
    if (c.qmsCounterId == null) return;
    this.api.update({ ...c, qmsCounterId: c.qmsCounterId, departmentList }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.formSubmitted.set(false);
          this.loadListAndCount();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
        this.resetFormFields(); // mirrors the unconditional initQMSMaster() call (ctrl:367)
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:376-409) ----
  // Updates via the list signal, not a direct mutation of `row` -- see
  // docs/ANGULAR_PENDING_FINDINGS.md entry 12. No revert-on-failure --
  // legacy's updateStatus() has none either.
  toggleStatus(row: QMSCounterRow): void {
    const status: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.qmsCounterId === row.qmsCounterId ? { ...r, status } : r)));
    // ctrl:376-409 -- `updatedBy` deliberately `undefined` here, see
    // class/service doc comments (faithful bug reproduction, NOT present on
    // save/update, which correctly use the real session user id above).
    this.api.updateStatus(row.qmsCounterId, status, undefined).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notify.success(res.message ?? 'Success!');
        else if (res.status === 'error') this.notify.error(res.message ?? 'Error!');
      },
    });
  }

  // ---- read-only department-details popup (mirrors departmentDetails, ctrl:412-442) ----
  viewDepartmentDetails(row: QMSCounterRow): void {
    this.loader.start();
    this.api.getById(row.qmsCounterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.object) {
          this.viewDepartmentList.set(res.object.departmentList ?? []);
          this.showDeptDetailsModal.set(true);
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  closeDeptDetailsModal(): void {
    this.showDeptDetailsModal.set(false);
  }
}
