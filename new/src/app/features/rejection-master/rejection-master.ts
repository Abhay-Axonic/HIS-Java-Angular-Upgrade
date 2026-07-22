import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RejectionMasterService } from './rejection-master.service';
import { RejectionMasterFormRecord, RejectionMasterListRow, emptyRejectionMaster } from './rejection-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/rejection-master.html +
 * resources/assets/js/controller/master/global/common/rejectionMasterController.js.
 * Route `/rejectionMaster`, moduleId 493 (app.js:8379-8405, no
 * `requireSuperUser` check in the resolve block). Confirmed real: `$state`
 * IS in this controller's own DI list (ctrl:21/25, so the missing-`$state`-
 * DI crash class -- entries 8/10 -- does not apply here), and the full
 * backend chain exists (RejectionMasterController.java,
 * `@RequestMapping("api/common")` -> RejectionMasterServiceImpl ->
 * RejectionMasterDaoImpl, all 6 endpoints confirmed present and wired).
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal (legacy's shared `<modal>` directive,
 *   `commonPopUpDirectives.js`) gates CREATE (`saveRejectionMaster`,
 *   ctrl:195-253) but NOT update (`updateRejectionMaster`, ctrl:324-371,
 *   saves immediately) -- same asymmetry documented on several sibling
 *   screens (e.g. speciality-master, city-master). Reproduced via
 *   `save()`/`confirmSave()` vs. `update()`.
 * - Per entry 55's standing policy: legacy's shared `<modal>` directive's
 *   x-close button calls a bare `cancel()` that this controller's own
 *   `$scope` never defines -- a live, disclosed legacy crash on every
 *   screen using that directive that doesn't define its own `cancel`.
 *   `rejectionMaster` is confirmed to be one more (previously uncounted)
 *   instance of this pattern (grep of this controller found no `cancel`
 *   definition under any binding style). Per the standing policy, this
 *   rebuild uses a plain working `closeConfirmModal()` for the x/Cancel
 *   buttons rather than reproducing the crash -- not re-litigated further.
 * - The search box's `ng-keyup="serachTestmaster(searchKeyword)"`
 *   (view:80) calls a function that does not exist anywhere in this
 *   controller -- but the box's real filtering comes entirely from
 *   `ng-model="searchKeyword"` feeding Angular's built-in
 *   `filter:searchKeyword` on the `ng-repeat` (view:131), which works
 *   independently of the broken keyup handler. Net observable effect:
 *   typing filters correctly; the broken handler only ever produces an
 *   invisible console error, never a user-visible symptom. Reproduced by
 *   keeping the real client-side filter and simply not inventing a
 *   `serachTestmaster` equivalent (there is nothing for it to do).
 * - That client-side filter only ever runs over the CURRENTLY LOADED PAGE
 *   of results (`rejectionList`, ctrl:108/157 -- populated fresh from each
 *   paginated server fetch, capped at `noOfRecordsPerPage`), not the full
 *   dataset -- a "current-page-only search" quirk, same class already
 *   documented on several sibling master screens this project has
 *   rebuilt. Reproduced via `filteredList` filtering only `list()`, the
 *   current page's rows.
 * - `saveRejectionMaster`'s success AND failure branches both
 *   unconditionally re-fetch the list afterward (ctrl:243-249, outside the
 *   success/fail growl branching) -- reproduced via an unconditional
 *   `loadList()` call after every create attempt, matching this
 *   codebase's speciality-master convention for the identical shape.
 *   `updateRejectionMaster` does the same (ctrl:362-367).
 * - The table's sort-caret icons (view:90-129, `fa-caret-up`/`fa-caret-down`
 *   on every column header) have no `ng-click` anywhere in legacy's own
 *   view or controller -- purely decorative, not a rebuild-introduced
 *   omission. Reproduced as static, non-interactive icons.
 * - `showUpdateBtn`'s failure branch uses a raw `alert("Error!!")`
 *   (ctrl:277), not `growl` -- same convention already established
 *   elsewhere in this codebase (e.g. ambulance-master.ts's `edit()`).
 *   Reproduced verbatim via `window.alert`.
 * - The header label "Status(n)" (view:110) is legacy's own literal text,
 *   not a placeholder or typo introduced here.
 * - Status toggle (`updateStatus`, ctrl:284-321): the view's own checkbox
 *   (`ng-model="info.isActive"`, `ng-change="updateStatus(...)"`) flips
 *   optimistically client-side BEFORE the request resolves (standard
 *   AngularJS ng-model-before-ng-change timing), and legacy's handler has
 *   NO revert branch at all -- unlike some sibling screens' 409-specific
 *   revert, a failure here just growls an error and leaves the optimistic
 *   flip in place permanently. Reproduced via `<app-status-toggle>` +
 *   an unconditional optimistic `list.update()`, never reverted in the
 *   subscribe callback regardless of response status.
 */
@Component({
  selector: 'app-rejection-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './rejection-master.html',
  styleUrl: './rejection-master.scss',
})
export class RejectionMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RejectionMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.rejectionMasterObj, ctrl:59) ----
  rejection = signal<RejectionMasterFormRecord>(emptyRejectionMaster());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initRejectionMasterList/PagerService) ----
  list = signal<RejectionMasterListRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side filter over only the currently-loaded page -- see class
  // comment ("current-page-only search").
  filteredList = computed(() => {
    const kw = this.searchKeyword().trim().toLowerCase();
    if (!kw) return this.list();
    return this.list().filter(
      (row) => row.code.toLowerCase().includes(kw) || row.reasonDesc.toLowerCase().includes(kw),
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

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateField<K extends keyof RejectionMasterFormRecord>(field: K, value: RejectionMasterFormRecord[K]): void {
    this.rejection.update((r) => ({ ...r, [field]: value }));
  }

  // ---- required-field validation (mirrors rejectionForm.$valid via the 2
  // `required` inputs -- rejectCode, rejectDesc, view:16-40) ----
  private requiredFieldsValid(): boolean {
    const r = this.rejection();
    return !!(r.code.trim() && r.rejectDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.rejection().code.trim();
  }

  isDescriptionInvalid(): boolean {
    return this.formSubmitted() && !this.rejection().rejectDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveRejectionMaster/PopupOkBtn, ctrl:195-253) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:212 guard
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const r = this.rejection();
    const now = formatDateTime(new Date());
    this.api
      .create({
        code: r.code,
        reasonDesc: r.rejectDesc,
        isActive: true,
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:243-249 -- reloads the list unconditionally after every
          // save attempt (success or failure).
          this.loadList();
        },
      });
  }

  // ---- update (mirrors updateRejectionMaster, ctrl:324-371) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const r = this.rejection();
    const now = formatDateTime(new Date());
    this.api
      .update({
        code: r.code,
        reasonDesc: r.rejectDesc,
        id: r.rejectId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.rejection.set(emptyRejectionMaster());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:256-281) ----
  edit(id: number): void {
    this.api.getById(id, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.rejection.set({ rejectId: d.id, code: d.code, rejectDesc: d.reasonDesc });
          this.saveMode.set(false);
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:284-321) ----
  toggleStatus(row: RejectionMasterListRow): void {
    const nextActive = !row.isActive;
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing --
    // never reverted below, per the class comment (legacy has no revert
    // branch at all for this screen).
    this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, isActive: nextActive } : r)));
    this.api
      .updateStatus({
        id: row.id,
        isActive: nextActive,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:305 -- legacy calls $scope.init() on success, which
            // fully resets the form (losing in-progress edit state) even
            // though this is a list-row action, not a form save. Reproduced
            // via the same resetForm() the save/update flows use.
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:196-198/285-287/325-327. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
