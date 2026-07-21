import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { QmsDepartmentMasterService } from './qms-department-master.service';
import { KioskOption, QMSDeptCommon, QMSDeptRow, emptyQMSDeptCommon } from './qms-department-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/QMSMaster/QMS-department-Master.html +
 * resources/assets/js/controller/qms/QMSdepartmentMasterController.js.
 * State `QMSdepartmentMaster` (app.js:20022-20049), moduleId 796, no
 * `controllerAs` (plain `$scope`).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - Save has a confirm modal (generic "Are you sure you want to Save?",
 *   ctrl:197-244 via the shared `<modal>` directive / `PopupOkBtn()`);
 *   Update (ctrl:286-327) has NO confirm step at all -- it posts directly
 *   on click. Two separate legacy functions/buttons (`saveBtnFlag`/
 *   `updateBtnFlag`), reproduced the same way, non-inverted (true = show
 *   that button), matching legacy's own flag semantics directly.
 * - **The one latent bug this ticket asked to reproduce, not fix:**
 *   create/update both stamp `createdBy`/`updatedBy` from the real session
 *   user (ctrl:56-57, carried through `emptyQMSDeptCommon`); on UPDATE
 *   specifically, `updatedBy`/`createdBy` are never re-stamped at all after
 *   the by-id fetch overwrites `common` (ctrl:293-294 only patches
 *   `organisationId`/`unitId`) -- so an update round-trips whatever the
 *   backend returned for those fields, still a real id. `updateStatus`
 *   (ctrl:329-362, `qms-department-master.service.ts`) is the one path that
 *   sends no user id at all, reading the never-assigned `$scope.updatedBy`.
 *   Not "finished" to be consistent with the other two paths.
 * - Both SUCCESS and (app-level) ERROR responses from save()/update() call
 *   `$scope.initQMSMaster()` unconditionally afterward (ctrl:234, ctrl:320)
 *   -- even a failed save wipes the form back to blank create-mode
 *   defaults and resets the page-size/offset counters. Only the SUCCESS
 *   branch also re-broadcasts `show-errors-reset` (ctrl:224, ctrl:306) and
 *   reloads the list (ctrl:225, ctrl:307). Net effect, reproduced exactly:
 *   an app-level error blanks the form AND immediately re-displays every
 *   required-field validation message (since the "show errors" flag was
 *   never cleared), because `formSubmitted` is intentionally NOT reset in
 *   `resetFormFields()` -- only success clears it. A genuine HTTP-level
 *   failure never reaches legacy's `.then()` at all (no reset, no reload,
 *   totally silent) -- like every other rebuilt master screen, this shows
 *   a generic "Something Went Wrong" notice instead of reproducing total
 *   silence, matching the established convention (see e.g.
 *   bed-category-master, asset-category-master).
 * - `showUpdateBtn` (ctrl:246-283) replaces `$scope.common` with a bare
 *   3-field object (`qmsDeptMasterId`/`organisationId`/`unitId` only)
 *   BEFORE the by-id fetch resolves -- if that fetch then fails, the form
 *   is left in this incomplete state and any Update attempt is silently
 *   blocked by required-field validation. Reproduced via the same
 *   intermediate value in `edit()` (with the other fields defaulted blank
 *   rather than left `undefined`, a TS-typing simplification with no
 *   observable difference).
 * - The Reception checkbox's enabled/disabled state is TRUE functional
 *   disablement in legacy, not merely cosmetic: `getKiosk(kioskId)`
 *   (ctrl:173-196) toggles a jQuery `.disabled` class on `#isReceptionId`,
 *   and `.disabled{pointer-events:none}` (param.common.master.css:22-25) is
 *   a real global rule that blocks clicks -- reproduced here via a scoped
 *   `.disabled` class in this screen's own stylesheet (see scss header
 *   comment) rather than a `[disabled]` attribute, since legacy's own
 *   `ng-model` binding for the checkbox has no such attribute either.
 * - The kiosk dropdown (`getActiveKiosks`) and the reception lookup
 *   (`getReceptionListByKiosk`) both skip the `status === 'success'` gate
 *   present on every other call in this controller (ctrl:70-75, ctrl:181-196)
 *   -- reproduced by not gating on it in the service/component either.
 * - Table row's Reception/Voice columns render the raw `'A'`/`'I'` code
 *   directly (view:179-180), not a translated Yes/No -- reproduced as-is.
 * - The Action-taken edit icon has no disabled-when-inactive guard (unlike
 *   e.g. asset-category-master) -- inactive rows remain editable, matching
 *   legacy exactly.
 * - `updateStatus`'s unused `moment(...)`-formatted `$scope.dt` (ctrl:331-332)
 *   is computed but never sent or displayed anywhere in legacy -- dropped
 *   here as genuinely dead code with zero observable effect.
 * - Legacy's `initQMSDeptMasterList` (list+count fetch) always forces the
 *   pager back to page 1 on its count response (`setPage(1,false)`,
 *   ctrl:125), but ordinary Next/Previous/page-number clicks
 *   (`setPage(page,true)`) go through the separate `qmsMasterList()`
 *   function, which never re-fetches the count or resets the page.
 *   Reproduced via two separate methods below (`loadListAndCount()` vs
 *   `loadPage()`) rather than one shared "reload" function.
 */
@Component({
  selector: 'app-qms-department-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './qms-department-master.html',
  styleUrl: './qms-department-master.scss',
})
export class QmsDepartmentMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(QmsDepartmentMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- kiosk dropdown ----
  kioskOptions = signal<KioskOption[]>([]);

  // ---- form state (mirrors $scope.common) ----
  common = signal<QMSDeptCommon>(emptyQMSDeptCommon(this.userId, this.organizationId, this.unitId));
  formSubmitted = signal(false);
  saveBtnFlag = signal(true); // ng-if="saveBtnFlag" -- Save button shown while adding
  updateBtnFlag = signal(false); // ng-if="updateBtnFlag" -- Update button shown while editing
  showConfirmModal = signal(false);

  // True functional lock (see class doc comment), driven by getKiosk()'s
  // reception-list lookup. Starts unlocked, matching the view's hardcoded
  // `class="enabled"` before any kiosk has ever been selected.
  receptionLocked = signal(false);

  // ---- list + server-side pagination (mirrors PagerService usage) ----
  rows = signal<QMSDeptRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters only the CURRENT server-fetched
  // page (`commonList | filter:searchKeyword`), not the full dataset.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.rows();
    return this.rows().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadKioskOptions();
    this.loadListAndCount();
  }

  // ctrl:64-76. No `status` gate -- see class doc comment.
  private loadKioskOptions(): void {
    this.api.getActiveKiosks(this.organizationId, this.unitId).subscribe({
      next: (res) => this.kioskOptions.set(res.listObject ?? []),
    });
  }

  // mirrors initQMSDeptMasterList (ctrl:82-130): fetches list + count
  // together, always from offset 0 (legacy's `$scope.offset` is never
  // reassigned anywhere), and forces the pager back to page 1 once the
  // count responds -- used for initial load, page-size changes, and after
  // a successful save/update.
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

  // mirrors qmsMasterList (ctrl:133-152) via setPage(page,true): re-fetches
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

  onPageSizeChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof QMSDeptCommon>(field: K, value: QMSDeptCommon[K]): void {
    this.common.update((c) => ({ ...c, [field]: value }));
  }

  // ---- kiosk selection / reception lock (mirrors getKiosk, ctrl:173-196) ----
  onKioskChange(kioskId: number): void {
    this.updateField('qmsKioskMasterId', kioskId);
    this.refreshReceptionLock(kioskId);
  }

  // No `status` gate in legacy -- see class doc comment. The `!=null ||
  // !=undefined` guard in legacy is a no-op tautology for these two values
  // (both evaluate false when the list truly is null/undefined thanks to
  // loose equality), so `(listObject?.length ?? 0) > 0` is behaviorally
  // identical, just written directly.
  private refreshReceptionLock(kioskId: number): void {
    this.api.getReceptionListByKiosk(this.organizationId, this.unitId, kioskId).subscribe({
      next: (res) => this.receptionLocked.set((res.listObject?.length ?? 0) > 0),
    });
  }

  onReceptionToggle(checked: boolean): void {
    this.updateField('isReception', checked ? 'A' : 'I');
  }

  onVoiceToggle(checked: boolean): void {
    this.updateField('isVoice', checked ? 'A' : 'I');
  }

  // ---- required-field validation (mirrors qmsDeptForm.$valid) ----
  private validateRequired(): boolean {
    const c = this.common();
    return !!(c.qmsDeptName.trim() && c.startLetter.trim() && c.startNumber.trim() && c.qmsKioskMasterId !== '');
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- save (create) -- mirrors save()'s confirm-modal flow (ctrl:201-244) ----
  openConfirmModal(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  save(): void {
    this.showConfirmModal.set(false);
    this.api.save(this.common()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.formSubmitted.set(false); // mirrors $broadcast('show-errors-reset')
          this.loadListAndCount();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
        this.resetFormFields(); // mirrors the unconditional initQMSMaster() call (ctrl:234)
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  // resets the form/flags/pagination-counters back to create-mode defaults
  // -- mirrors initQMSMaster() (ctrl:19-62). Deliberately does NOT clear
  // `formSubmitted` -- see class doc comment.
  private resetFormFields(): void {
    this.common.set(emptyQMSDeptCommon(this.userId, this.organizationId, this.unitId));
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:246-283) ----
  edit(row: QMSDeptRow): void {
    this.loader.start();
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.common.set({
      ...emptyQMSDeptCommon(this.userId, this.organizationId, this.unitId),
      qmsDeptMasterId: row.qmsDeptMasterId,
    });
    this.api.getById(row.qmsDeptMasterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.listObject?.length) {
          this.common.set(res.listObject[0]);
          this.refreshReceptionLock(row.qmsKioskMasterId);
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update(), ctrl:286-327 -- no confirm modal) ----
  update(): void {
    // ctrl:293-294 patches these two fields unconditionally, even before
    // validation runs.
    this.common.update((c) => ({ ...c, organisationId: this.organizationId, unitId: this.unitId }));
    if (!this.validateRequired()) return;

    this.api.update(this.common()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success!');
          this.formSubmitted.set(false);
          this.loadListAndCount();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
        this.resetFormFields(); // mirrors the unconditional initQMSMaster() call (ctrl:320)
      },
      error: () => this.notify.error('Something Went Wrong'),
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:329-362) ----
  // Update via the list signal, not a direct mutation of `row` -- see
  // docs/ANGULAR_PENDING_FINDINGS.md entry 12. No revert-on-failure --
  // legacy's updateStatus() has none either.
  toggleStatus(row: QMSDeptRow): void {
    const status: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.qmsDeptMasterId === row.qmsDeptMasterId ? { ...r, status } : r)));
    this.api.updateStatus(row.qmsDeptMasterId, status).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notify.success(res.message ?? 'Success!');
        else if (res.status === 'error') this.notify.error(res.message ?? 'Error!');
      },
    });
  }
}
