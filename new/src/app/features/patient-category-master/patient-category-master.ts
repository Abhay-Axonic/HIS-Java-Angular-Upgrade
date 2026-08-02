import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PatientCategoryMasterService } from './patient-category-master.service';
import { PatientCategoryForm, PatientCategoryRow, emptyPatientCategoryForm } from './patient-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/GlobalMaster/patient-category-master.html`
 * + `resources/assets/js/controller/master/global/billing/patientCategoryController.js`.
 * Legacy state `patientCategory` (`resources/assets/js/config/app.js:7879-7883`),
 * `templateUrl: 'views/master/GlobalMaster/patient-category-master.html'`,
 * `title: 'Patient Category Master'`, gated by a resolve-block check for
 * moduleId 446 -- wired at the ROUTE level via `moduleAccessGuard`
 * (`data: { moduleId: 446 }`), not in this component.
 *
 * **Real findings from this screen's own audit + a live golden-trace
 * session against `http://localhost:8080` on 2026-07-29 (not assumed from
 * sibling screens):**
 *
 * - **Growl messages are the raw server `response.data.message` passed
 *   through verbatim** (ctrl:220-236/344-360/299-316) -- NOT screen-authored
 *   copy the way `ambulanceTypeMaster`/`maritalStatusMaster` hardcode their
 *   own strings. Reproduced by passing `res.message` straight to
 *   `NotificationService` on both outcomes, matching legacy's own
 *   pass-through exactly (confirmed live: success = "Added Successfully."/
 *   "Updated Successfully.", duplicate = "Already Exists.", all server-authored).
 * - **The Save button opens a real confirm modal** ("Are you sure you want
 *   to Save?", the shared `<modal>` directive) before the create POST fires
 *   (`savePatient`'s `popUpFlag`/`PopupOkBtn` dance, ctrl:182-248). The
 *   Update button does NOT -- `updatePatient` (ctrl:321-370) posts
 *   immediately with no confirmation step. Confirm-modal gate rearms on
 *   BOTH outcomes here, matching this codebase's standing convention (the
 *   shared modal directive's own `$watch` resets `popUpFlag` synchronously
 *   on every Ok click, independent of the save's own success/failure).
 * - **`updateStatus` (ctrl:280-318) calls `$scope.init()` on a SUCCESSFUL
 *   toggle** -- and `init()` unconditionally resets the create/update form
 *   back to empty AND resets `noOfRecordsPerPage` to 10, which is
 *   two-way-bound to the "Show N entries" dropdown. **Confirmed live via
 *   direct scope inspection**: toggling ANY row's status while the
 *   create/update form has unsaved input, and/or the entries-per-page
 *   dropdown is set to something other than 10, silently discards the
 *   in-progress form AND snaps the entries dropdown back to 10 -- even
 *   though the user only clicked an unrelated row's status switch.
 *   `updateStatus` does NOT reload the list/count and does NOT revert the
 *   optimistic status flip on a failed response. All three behaviors
 *   reproduced exactly below (`toggleStatus`): the form/`pageSize` reset
 *   fires on success only, no `loadList()` call at all, no revert-on-error.
 * - **Two rows are permanently hidden from the status-toggle switch AND the
 *   edit pencil**: `patient-category-master.html:156/161` gate both behind
 *   `ng-if="(info.patientCategoryId != '1' && info.patientCategoryId !='2')"`.
 *   This is a hardcoded pair of numeric IDs, not a `status`/`isSystem`
 *   flag -- whatever rows happen to occupy IDs 1 and 2 (typically seeded
 *   "General"/foreigner-category defaults) can never be edited or
 *   deactivated from this UI, confirmed live (this dev DB's own "General"/
 *   id 1 row renders with empty Status/Action cells). The backend itself
 *   has no such restriction -- `updatePatientCategoryMaster`/
 *   `updatePatientCategoryMasterStatus` on id 1 or 2 would succeed if
 *   called directly; this is a frontend-only guard, reproduced as such.
 * - **Neither Code nor Description has a `maxlength` in the real view**
 *   (`patient-category-master.html:16-20/33-37`) -- unlike several sibling
 *   master screens. Not inventing a 10/256-char cap here.
 * - **The search input (`patient-category-master.html:91-93`) has zero
 *   binding at all** -- no `ng-model`, and the table's `ng-repeat` has no
 *   `filter:`. A completely dead, decorative control (same class as
 *   `reasonTypeMaster`'s, `docs/ANGULAR_PENDING_FINDINGS.md` entry 126) --
 *   omitted from this rebuild rather than ported as inert markup, per that
 *   entry's established precedent.
 * - **The 5 column-header sort carets are pure static decoration** with
 *   zero click wiring in legacy itself (`patient-category-master.html:101-150`)
 *   -- same class as `holidayCalendarMasterUnit`'s (entry 111). Kept as
 *   inert decorative markup in the template, matching that precedent.
 * - `initPatientCategoryMaster` (ctrl:72-125) fires 2 real, separate calls
 *   (list + count) on initial load, on `setNoOfRecords`
 *   (page-size-change), and after every save/update (success OR failure).
 *   Plain pagination clicks (`setPage`) only call the list endpoint, never
 *   re-fetch count. Reproduced via `loadListAndCount()` vs `loadListOnly()`.
 * - A once-off, non-reproducible `{status:"error", code:null, message:null}`
 *   response was observed on the very first live save attempt this session,
 *   before any test data existed; an immediate retry with an identical
 *   payload succeeded normally and every subsequent save/update/toggle in
 *   the same session was consistent. Not logged as a backend finding --
 *   could not be reproduced a second time, and the observed shape matches
 *   `PatientCategoryController.savePatientCategoryMaster`'s outermost
 *   catch-all (`return new Response(ERROR, null, null, null, null)`), i.e.
 *   *some* exception, transient and environment-specific (this session ran
 *   under heavy concurrent multi-agent load against the same dev backend).
 */
@Component({
  selector: 'app-patient-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './patient-category-master.html',
  styleUrl: './patient-category-master.scss',
})
export class PatientCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PatientCategoryMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.patientCategory, ctrl:49-51) ----
  form = signal<PatientCategoryForm>(emptyPatientCategoryForm());
  private activeId: number | undefined;
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);
  // ctrl:58/182-248 -- rearmed on BOTH outcomes here (see class doc comment
  // for the shared-modal-directive mechanism this matches).
  private popupArmed = true;

  // ---- list + server-side pagination (mirrors PagerService.GetPager) ----
  list = signal<PatientCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadListAndCount();
  }

  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: (err) => console.error('patient-category-master list error', err),
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
      error: (err) => console.error('patient-category-master count error', err),
    });
  }

  // mirrors setPage->getPatientCategoryMasterList (ctrl:127-155/160-173) --
  // pagination clicks never re-fetch count.
  private loadListOnly(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: (err) => console.error('patient-category-master list error', err),
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadListOnly();
  }

  updateField<K extends keyof PatientCategoryForm>(field: K, value: PatientCategoryForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- validation (mirrors patientCategoryForm.$valid over the 2 required
  // fields -- neither has a maxlength in the real view) ----
  isCodeInvalid(): boolean {
    return this.submitted() && !this.form().code.trim();
  }
  isDescInvalid(): boolean {
    return this.submitted() && !this.form().desc.trim();
  }
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.code.trim() && f.desc.trim());
  }

  // ---- save (mirrors savePatient's popUpFlag confirm dance, ctrl:188-248) ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.requiredFieldsValid()) return;
    if (this.popupArmed) {
      this.showConfirmModal.set(true);
      return;
    }
    this.confirmSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmFromModal(): void {
    this.popupArmed = false;
    this.showConfirmModal.set(false);
    this.confirmSave();
  }

  private confirmSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        isForeginerCategory: f.isForeigner ? 'Y' : 'N',
        patientCategory: f.desc,
        patientCategoryCode: f.code,
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
            this.notify.success(res.message ?? '');
            this.resetForm();
            this.pageSize.set(PAGE_SIZE_OPTIONS[0]); // ctrl:29-62 init() -- success only
          } else {
            this.notify.error(res.message ?? '');
          }
          this.currentPage.set(1); // ctrl:238-244 -- always, both outcomes
          this.loadListAndCount();
          this.popupArmed = true; // rearm on both outcomes -- see class doc comment
        },
      });
  }

  // ---- update (mirrors updatePatient, ctrl:321-370) -- no confirm modal ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .update({
        isForeginerCategory: f.isForeigner ? 'Y' : 'N',
        patientCategory: f.desc,
        patientCategoryCode: f.code,
        patientCategoryId: this.activeId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? '');
            this.resetForm();
            this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
          } else {
            this.notify.error(res.message ?? '');
          }
          this.currentPage.set(1);
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyPatientCategoryForm());
    this.activeId = undefined;
    this.saveMode.set(true);
    this.submitted.set(false);
  }

  // mirrors showUpdateBtn (ctrl:251-277). No defensive empty-list check in
  // legacy -- `response.data.listObject[0]` is read unconditionally on a
  // "success" status; reproduced the same way rather than adding a guard
  // legacy doesn't have.
  edit(row: PatientCategoryRow): void {
    this.api.getById(row.patientCategoryId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          window.alert('Error!!'); // ctrl:273 -- a real native alert, not growl
          return;
        }
        const d = res.listObject![0];
        this.form.set({
          code: d.patientCategoryCode,
          desc: d.patientCategory,
          isForeigner: d.isForeginerCategory === 'Y',
        });
        this.activeId = d.patientCategoryId;
        this.saveMode.set(false);
      },
    });
  }

  // Legacy hides both the toggle and the edit pencil entirely for these 2
  // hardcoded IDs (view:156/161) -- see class doc comment.
  isProtected(row: PatientCategoryRow): boolean {
    return row.patientCategoryId === 1 || row.patientCategoryId === 2;
  }

  // mirrors updateStatus (ctrl:280-318) -- see class doc comment for the
  // live-confirmed side effects on success: form reset + pageSize reset to
  // 10, no list reload either way, no revert-on-failure.
  toggleStatus(row: PatientCategoryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.patientCategoryId === row.patientCategoryId ? { ...r, status: nextStatus } : r)));
    this.api
      .changeStatus({
        patientCategoryId: row.patientCategoryId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? '');
            this.resetForm();
            this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
          } else {
            this.notify.error(res.message ?? '');
          }
        },
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:189-191/281-283/322-324. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
