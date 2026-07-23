import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { EncounterTypeMasterService } from './encounter-type-master.service';
import { EncounterTypeForm, EncounterTypeRow, EncounterTypeStatus, emptyEncounterTypeForm } from './encounter-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * `views/master/GlobalMaster/encounter-type-master.html` +
 * `resources/assets/js/controller/master/encounterTypeMasterController.js`
 * (366 lines, `encounterTypeMasterController`, no `controllerAs`). State
 * `encounterTypeMaster` (app.js:3922-3949) has a real `resolve` block
 * gating module id 397 -- handled entirely by this route's
 * `moduleAccessGuard` (wired centrally in `app.routes.ts`), no code needed
 * in this component for that gate.
 *
 * PURE-MASTER/classification screen (no dosing/patient-ID computation of
 * its own) -- but it IS a widely-referenced FK downstream, so its status
 * vocabulary (`'A'`/`'I'`, one active/inactive toggle per row) and its
 * save/duplicate wording are reproduced verbatim below, not "cleaned up."
 *
 * Same GlobalMaster family/shape as this codebase's `city-master` (simple
 * code+description master, real server-offset pagination + a separate
 * count endpoint, Save-has-a-confirm-modal/Update-does-not asymmetry,
 * search-filters-current-page-only) -- structured the same way, with the
 * differences noted below.
 *
 * **Bugs faithfully reproduced, per explicit owner decision (do NOT fix):**
 * 1. **No client-side (or legacy backend) uniqueness check on
 *    `encounterTypeCode`** -- only `encounterTypeName` is ever validated
 *    for duplicates by the backend (see #2); the code field is free-text,
 *    required-only. Reproduced as-is: `requiredFieldsValid()` below only
 *    checks for non-empty code/name, nothing more.
 * 2. **Editing a row WITHOUT changing its name is likely to spuriously
 *    fail** with the exact legacy error toast ("Encounter type already
 *    exits...!!") even though nothing is actually a duplicate. Root cause
 *    is a real HQL operator-precedence bug in the backend's update-path
 *    duplicate-name check (`WHERE LOWER(name)=:x OR name=:x AND
 *    id=:excludeId` parses as `LOWER(name)=:x OR (name=:x AND
 *    id=:excludeId)`, so the id-exclusion only applies to the redundant
 *    second branch) -- not fixable from this Angular frontend, and not
 *    papered over here: `update()` below does no client-side "is this
 *    actually a duplicate" pre-check of its own. The real POST response
 *    (and its real error) flows straight through to the user via the
 *    normal `notify.error(...)` path, exactly as legacy does.
 * 3. **Status toggle never rolls back its visual state on failure.**
 *    Legacy's checkbox is two-way-bound directly to `info.status`
 *    (`ng-true-value="'A'" ng-false-value="'I'"`), so the flip is applied
 *    to the UI immediately and unconditionally, regardless of what the
 *    `updateStatus` POST later returns -- a failed response only shows an
 *    error toast, never reverts the switch. Reproduced in `toggleStatus()`
 *    below: `list` is updated optimistically before the POST fires, and is
 *    never reverted in the error branch.
 *
 * **Other legacy quirks preserved (not bugs, just verbatim):**
 * - Save/Update/status-toggle success and error toast text is copy-pasted
 *   verbatim from the controller, including its inconsistent
 *   capitalization across the three flows ("added successfully", capital-U
 *   "Updated successfully" for the status toggle, lowercase-u "updated
 *   successfully" for the Update button) -- not normalized here.
 * - Both `save()`'s and `update()`'s success AND failure branches
 *   unconditionally reload the list (ctrl:196-207/330-336 both call
 *   `initEncounterTypeMasterList(...)` after the `if/else`, unconditionally)
 *   -- reproduced via `loadList()` always running; `resetForm()` only runs
 *   after a successful save/update, same asymmetry as `city-master`.
 * - `showUpdateBtn`'s (edit's) failure branch does a bare `alert("Error!!")`
 *   (ctrl:262-263) rather than a growl -- reproduced verbatim via
 *   `window.alert('Error!!')`.
 * - Edit switches Save/Update button visibility IMMEDIATELY on click,
 *   before the GET even resolves (ctrl:246-248) -- `saveMode` is set
 *   `false` before `getById()` is called, not after.
 */
@Component({
  selector: 'app-encounter-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './encounter-type-master.html',
  styleUrl: './encounter-type-master.scss',
})
export class EncounterTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(EncounterTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.encountertype) ----
  form = signal(emptyEncounterTypeForm());
  saveMode = signal(true); // true = create (saveBtnFlag hidden/updateBtnFlag shown), false = update
  formSubmitted = signal(false);

  showConfirmModal = signal(false);

  // ---- list + real server-side offset pagination (mirrors
  // initEncounterTypeMasterList/PagerService, ctrl:97-131) ----
  list = signal<EncounterTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved verbatim: search filters the CURRENT PAGE's
  // already-fetched rows only (`encounterTypeList|filter:searchKeyword` in
  // the legacy view, applied to whatever single server page is currently
  // loaded) -- NOT a full-dataset search. See class doc.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.encounterTypeCode?.toLowerCase().includes(keyword) || r.encounterTypeName?.toLowerCase().includes(keyword),
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

  updateFormField<K extends keyof EncounterTypeForm>(field: K, value: EncounterTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isRequiredMissing(value: string | number | undefined): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- validation (mirrors encountertypeform.$valid -- code and name are
  // both plain `required` inputs, nothing else, see bug #1 in class doc) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.encounterTypeCode.trim() && f.encounterTypeName.trim());
  }

  // ---- save (mirrors saveEncounterType's popUpFlag confirm-modal dance,
  // ctrl:170-208) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        encounterTypeCode: f.encounterTypeCode,
        encounterTypeName: f.encounterTypeName,
        status: 'A', // hardcoded on every create, see EncounterTypeSavePayload's doc comment
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Encounter type added successfully...!!'); // ctrl:190-193, verbatim
            this.resetForm();
          } else {
            this.notify.error('Encounter type already exits...!!'); // ctrl:198-201, verbatim typo -- see bug #2
          }
          this.loadList(); // ctrl:206 -- reloads unconditionally, success AND failure
        },
      });
  }

  // ---- update (mirrors updateEncounterType, ctrl:308-337) -- no confirm
  // modal, fires immediately ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .update({
        encounterTypeCode: f.encounterTypeCode,
        encounterTypeName: f.encounterTypeName,
        encounterTypeId: f.encounterTypeId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Encounter type updated successfully...!!'); // ctrl:317-320, verbatim (lowercase 'u')
            this.resetForm();
          } else {
            // ctrl:325-328, verbatim typo -- see bug #2: a same-name edit can
            // land here spuriously due to the backend's own HQL bug. No
            // client-side pre-check masks this; the real response drives it.
            this.notify.error('Encounter type already exits...!!');
          }
          this.loadList(); // ctrl:335 -- reloads unconditionally, success AND failure
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyEncounterTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:246-264) ----
  edit(encounterTypeId: number): void {
    // ctrl:247-248 -- flips saveBtnFlag/updateBtnFlag to "Update" mode
    // IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.api.getById(encounterTypeId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:262-263, verbatim
          return;
        }
        const detail = res.listObject[0];
        this.form.set({
          encounterTypeId: detail.encounterTypeId,
          encounterTypeCode: detail.encounterTypeCode,
          encounterTypeName: detail.encounterTypeName,
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:266-296) -- optimistic
  // flip, NEVER reverted on failure, see bug #3 in class doc ----
  toggleStatus(row: EncounterTypeRow): void {
    const nextStatus: EncounterTypeStatus = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.encounterTypeId === row.encounterTypeId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        encounterTypeId: row.encounterTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Encounter type Updated successfully...!!'); // ctrl:280-283, verbatim (capital 'U')
          } else {
            this.notify.error(res.message ?? 'Something went wrong...!!'); // ctrl:288-291, verbatim -- no rollback, see bug #3
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:174-176/270-272/312-314. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
