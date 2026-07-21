import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { QualificationMasterService } from './qualification-master.service';
import { QualificationForm, QualificationRow, emptyQualificationForm } from './qualification-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/qualification-master.html +
 * resources/assets/js/controller/master/global/common/qualificationMasterController.js
 * (`qualificationMasterController`, no `controllerAs`). Route guard: moduleId
 * 496 (app.js:14173-14198 resolve block), wired at the route level, not here.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - Save vs. Update is asymmetric, same family as doctor-category /
 *   country-master's own save/update split: clicking Save (`saveBtnFlag`,
 *   ctrl:189-252) validates, then opens the shared "Are you sure you want to
 *   Save?" confirm modal before actually POSTing (only the modal's Ok ->
 *   `PopupOkBtn()`, ctrl:189-192, re-runs `saveQualificationMaster()` with
 *   `popUpFlag` now false, which is when the real POST fires). Clicking
 *   Update (`updateBtnFlag`, ctrl:323-370) validates and POSTs immediately,
 *   with NO confirm step at all -- not a copy-paste omission, legacy
 *   genuinely has no popup wired to `updateQualificationMaster()`.
 *
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`common/commonPopUpDirectives.js:10` hardcodes `ng-click="cancel();
 *   stopLoader();"`, and `qualificationMasterController.js` never defines
 *   `$scope.cancel`) -- but per the standing policy resolved 2026-07-21
 *   (`docs/ANGULAR_PENDING_FINDINGS.md` entry 55, `docs/
 *   ANGULAR_REBUILD_PLAYBOOK.md` Step 3), a shared component's correct,
 *   working default behavior differing from a bug baked into legacy's own
 *   shared directive is an accepted, disclosed standing improvement, not a
 *   parity gap to reproduce. The x button here is an ordinary, working
 *   `closeConfirmModal()`, same as every other screen using this shared
 *   modal pattern except `doctorCategory` (built before this policy
 *   existed, left as-is rather than retrofitted either way).
 *
 * - The header search box (view line 75, `ng-model="searchKeyword"`) IS
 *   wired here (unlike the dead search box found in doctor-category) -- the
 *   view's `ng-repeat="info in qualificationList | filter:searchKeyword"`
 *   (view:128) is a bare Angular `filter` over `qualificationList`, which
 *   only ever holds the CURRENT PAGE's already-fetched rows (`getList`/
 *   `getQualificationList` refetch on every page change, ctrl:136-163). A
 *   user searching for a qualification on a different page sees no match
 *   even though it exists. Reproduced via `displayedRows`, matching only the
 *   two visible text columns (code/description) -- same practical
 *   interpretation already used for this same legacy `filter:keyword`
 *   pattern in asset-category-master.
 *
 * - `showUpdateBtn` (ctrl:255-280) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETQUALIFICATIONMASTERBYID` call
 *   resolves, and uses a raw `alert("Error!!")` on a failed response -- the
 *   only place in this controller that doesn't use `growl`. Both reproduced
 *   verbatim (`saveMode.set(false)` up front; `window.alert` on failure).
 *
 * - `updateStatus` (ctrl:283-320) calls `$scope.init()` on success (which
 *   resets the form/`saveBtnFlag`/`updateBtnFlag` back to blank Save-mode)
 *   but never re-fetches the list afterward on EITHER branch -- toggling a
 *   row's status never re-fetches the list from the server, and never
 *   reverts on failure either. Reproduced by patching the `list` signal
 *   locally, resetting the form on success only, and never reloading or
 *   reverting.
 *
 * - Both `saveQualificationMaster` and `updateQualificationMaster` build
 *   `createdDate`/`updatedDate` client-side via
 *   `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (ctrl:196-198,
 *   284-286, 324-326) rather than letting the server stamp them -- kept via
 *   the same local `formatDmyHms()` used elsewhere in this rebuild (e.g.
 *   doctor-category.ts, country-master.ts's `formatDateTime`).
 *
 * - The per-column sort caret icons (view lines 82-126, "Status(n)"/"Action
 *   taken" column headers included) carry no `ng-click` anywhere in the view
 *   or controller -- confirmed dead UI. Rendered as inert, decorative
 *   markup here rather than given working sort behavior this rebuild would
 *   be inventing.
 *
 * - The legacy description field's extra validation message ("Please Enter
 *   Valid Qualification Description", view:34-35) is gated on
 *   `qualificationForm.desc.$invalid && !$error.required` -- but the input
 *   carries no validator besides `required` (no pattern/minlength/maxlength),
 *   so that condition can never be true independent of the required message
 *   already showing. Confirmed dead/unreachable in legacy; not reproduced as
 *   there is no code path that would ever surface it.
 */
@Component({
  selector: 'app-qualification-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './qualification-master.html',
  styleUrl: './qualification-master.scss',
})
export class QualificationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(QualificationMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.qualificationMasterObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<QualificationForm>(emptyQualificationForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initQualificationList/setPage) ----
  list = signal<QualificationRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE's already-fetched
  // rows only (see class comment), matching only the two visible text columns.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.qualificationCode?.toLowerCase().includes(keyword) ||
        r.qualificationDesc?.toLowerCase().includes(keyword),
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

  private reloadFirstPage(): void {
    this.currentPage.set(1);
    this.loadList();
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

  updateField<K extends keyof QualificationForm>(field: K, value: QualificationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.qualificationCode.trim() && f.qualificationDesc.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors saveQualificationMaster's
  // popUpFlag-gated double call via PopupOkBtn, ctrl:189-252). ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.create();
  }

  private create(): void {
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        qualificationCode: f.qualificationCode,
        qualificationDesc: f.qualificationDesc,
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
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's
  // updateQualificationMaster() being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        qualificationCode: f.qualificationCode,
        qualificationDesc: f.qualificationDesc,
        qualificationId: f.qualificationId as number,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyQualificationForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode BEFORE the
  // GETQUALIFICATIONMASTERBYID response arrives, and uses a raw alert() on
  // failure; both preserved verbatim; see class comment). ----
  edit(row: QualificationRow): void {
    this.saveMode.set(false);
    this.api.getById(row.qualificationId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            qualificationId: detail.qualificationId,
            qualificationCode: detail.qualificationCode,
            qualificationDesc: detail.qualificationDesc,
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
  toggleStatus(row: QualificationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.qualificationId === row.qualificationId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        qualificationId: row.qualificationId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
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
}

function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
