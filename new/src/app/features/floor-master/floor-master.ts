import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { FloorMasterService } from './floor-master.service';
import { FloorMasterForm, FloorMasterRow, emptyFloorMasterForm } from './floor-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/floor-master.html +
 * resources/assets/js/controller/master/unit/floorMasterController.js
 * (`floorMasterController`, no `controllerAs`). moduleId 537 (per prior
 * audit). Unit-scoped simple master, same shape as ward-master's sibling
 * screens -- closest structural template is actually qualification-master
 * (2 plain text fields, no dropdowns), not ward-master (which has floor/ICU
 * /gender dependent fields this screen doesn't).
 *
 * Confirmed real, working CRUD: save (ctrl:197-255), update (ctrl:325-371),
 * edit-populate `showUpdateBtn` (ctrl:258-282), status toggle `updateStatus`
 * (ctrl:285-322) -- all wired to real endpoints, see floor-master.service.ts.
 *
 * **New divergence found while auditing this screen (not in the prior audit
 * summary):** the ENTIRE controller is a copy-paste of a "Designation
 * Master" controller that was never fully renamed -- function/comment names
 * throughout still say "Designation": `initDesignationList`,
 * `getDesignationList`, "Function For save Designation", "get single
 * designation entry", "update Designation status", "update Designation"
 * (ctrl:74-371, comments verbatim). This has zero observable effect (all
 * the actual data/URLs/fields are correctly Floor-specific), so it is not
 * reproduced here -- this rebuild's method names describe what the code
 * does (`loadList`, `edit`, `toggleStatus`, ...), not the leftover
 * copy-paste names.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - Save vs. Update is asymmetric, same family as qualification-master/
 *   degree-master: clicking Save (`saveBtnFlag`, ctrl:197-255) validates,
 *   then opens the shared "Are you sure you want to Save?" confirm modal
 *   (`<modal visible="showModal">`, view:164) before actually POSTing (only
 *   the modal's Ok -> `PopupOkBtn()`, ctrl:191-194, re-runs
 *   `saveFloorMaster()` with `popUpFlag` now false, which is when the real
 *   POST fires). Clicking Update (`updateBtnFlag`, ctrl:325-371) validates
 *   and POSTs immediately, with NO confirm step at all.
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`common/commonPopUpDirectives.js` hardcodes `ng-click="cancel();
 *   stopLoader();"`, and `floorMasterController.js` never defines
 *   `$scope.cancel`) -- but per the standing policy (`docs/
 *   ANGULAR_PENDING_FINDINGS.md` entry 55, `docs/ANGULAR_REBUILD_PLAYBOOK.md`
 *   Step 3), a shared component's correct, working default behavior
 *   differing from a bug baked into legacy's own shared directive is an
 *   accepted, disclosed standing improvement, not a parity gap to
 *   reproduce. The x button here is an ordinary, working
 *   `closeConfirmModal()`.
 * - Both `saveFloorMaster` and `updateFloorMaster` (ctrl:224-251, 341-368)
 *   reset the form (legacy's `$scope.init()`) ONLY on a "success" response,
 *   but reload the list (`initDesignationList`) UNCONDITIONALLY after
 *   either branch, success or failure. Reproduced verbatim: `resetForm()`
 *   only inside the success branch, `loadList()` always runs after.
 * - `updateStatus` (ctrl:285-322), by contrast, never reloads the list at
 *   all on either branch -- it only calls `$scope.init()` (reset the
 *   form/mode) on success, and shows a growl either way, with no revert of
 *   the already-flipped checkbox on failure. Reproduced by patching the
 *   `list` signal locally up front, resetting the form on success only, and
 *   never reloading or reverting -- same pattern as this codebase's
 *   qualification-master.
 * - `showUpdateBtn` (ctrl:258-282) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETFLOORBYID` call resolves, and
 *   uses a raw `alert("Error!!")` on a failed response -- the only place in
 *   this controller that doesn't use `growl`. Both reproduced verbatim
 *   (`saveMode.set(false)` up front; `window.alert` on failure).
 * - GETFLOORBYID is a POST with just `{ floorId }` in the body (ctrl:263-267),
 *   NOT a GET with path params like ward-master/qualification-master's
 *   by-id lookups -- kept as POST here to match this screen's own backend
 *   contract exactly (see floor-master.service.ts).
 *
 * **1 confirmed reproducible crash bug, NOT fixed, faithfully reproduced:**
 * The search input (view:72, `ng-keyup="serachTestmaster(searchKeyword);"`)
 * calls a function that is never defined anywhere in
 * floorMasterController.js (confirmed via full-file read) -- every
 * keystroke throws `serachTestmaster is not a function` in legacy, today.
 * Reproduced via `onSearchKeyup()` on an unbound search input, matching the
 * exact convention established in this codebase's degree-master (see that
 * file's class comment / docs/ANGULAR_PENDING_FINDINGS.md).
 */
@Component({
  selector: 'app-floor-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './floor-master.html',
  styleUrl: './floor-master.scss',
})
export class FloorMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(FloorMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.floorMasterObj) ----
  form = signal<FloorMasterForm>(emptyFloorMasterForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initDesignationList/PagerService) ----
  list = signal<FloorMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getFloorMasterList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getFloorCount(this.organizationId, this.unitId).subscribe({
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

  updateFormField<K extends keyof FloorMasterForm>(field: K, value: FloorMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  // ---- required-field validation (mirrors floorForm.$valid -- the 2
  // `required` inputs, floorCode/description, view:14-15/28-29) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.floorCode.trim() && f.floorName.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors saveFloorMaster's
  // popUpFlag-gated double call via PopupOkBtn, ctrl:191-255). ----
  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  /** Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy): the
   * shared `<modal>` directive's x close button crashes in legacy
   * (`cancel()` is never defined on this controller's $scope) -- an
   * ordinary working close here is an accepted standing improvement, not a
   * parity violation. */
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveFloorMaster({
        floorCode: f.floorCode,
        floorName: f.floorName,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
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
          // ctrl:245-249 -- the list is reloaded unconditionally after every
          // save attempt, success or not (unlike the form reset, which is
          // success-only -- see class comment).
          this.loadList();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update (mirrors updateFloorMaster, ctrl:325-371) -- no confirm modal ----
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updateFloorMaster({
        floorCode: f.floorCode,
        floorName: f.floorName,
        floorId: f.floorId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:362-366 -- same unconditional reload as save, see above.
          this.loadList();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyFloorMasterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode BEFORE the
  // GETFLOORBYID response arrives, and uses a raw alert() on failure; both
  // preserved verbatim; see class comment). ----
  edit(row: FloorMasterRow): void {
    this.saveMode.set(false);
    this.api.getFloorById(row.floorId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            floorId: detail.floorId,
            floorCode: detail.floorCode,
            floorName: detail.floorName,
          });
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- patches the list in place,
  // resets the add/edit form on success only, and never reloads or reverts
  // on failure; see class comment). ----
  toggleStatus(row: FloorMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.floorId === row.floorId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatusForFloor({
        floorId: row.floorId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * floor-master.html:72 wires `ng-keyup="serachTestmaster(searchKeyword);"`
   * to a function never defined anywhere in floorMasterController.js. */
  onSearchKeyup(): void {
    throw new TypeError('serachTestmaster is not a function');
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:198-200/286-288/326-328. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
