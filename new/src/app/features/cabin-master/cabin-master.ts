import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CabinMasterService } from './cabin-master.service';
import { CabinForm, CabinRow, emptyCabinForm } from './cabin-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/cabin-master.html +
 * resources/assets/js/controller/master/unit/cabinMasterController.js
 * (`cabinMasterController`, no `controllerAs`). moduleId 705, wired at the
 * route level, not here. Same two-field (code + description) shape as
 * `qualification-master`, same save/update asymmetry family.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - Save vs. Update is asymmetric: clicking Save (`saveBtnFlag`, ctrl:148-206)
 *   validates, then opens the shared "Are you sure you want to Save?" confirm
 *   modal before actually POSTing (only the modal's Ok -> `PopupOkBtn()`,
 *   ctrl:143-146, re-runs `save()` with `popUpFlag` now false, which is when
 *   the real POST fires). Clicking Update (`updateBtnFlag`, ctrl:244-293)
 *   validates and POSTs immediately, with no confirm step at all.
 *
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`common/commonPopUpDirectives.js:10` hardcodes `ng-click="cancel();
 *   stopLoader();"`, and `cabinMasterController.js` never defines
 *   `$scope.cancel`) -- but per the standing policy resolved 2026-07-21
 *   (`docs/ANGULAR_PENDING_FINDINGS.md` entry 55, `docs/
 *   ANGULAR_REBUILD_PLAYBOOK.md` Step 3), a shared component's correct,
 *   working default behavior differing from a bug baked into legacy's own
 *   shared directive is an accepted, disclosed standing improvement, not a
 *   parity gap to reproduce. The x button here is an ordinary, working
 *   `closeConfirmModal()`, same pattern as `qualification-master.ts`.
 *
 * - The header search box (view:70, `ng-model="searchKeyword"`) binds
 *   straight to a plain Angular `filter:searchKeyword` over `commonList`
 *   (view:123) -- no custom crash function of any name, unlike some sibling
 *   screens. `commonList` only ever holds the CURRENT PAGE's already-fetched
 *   rows (`initMasterList`/`masterList` refetch on every page change,
 *   ctrl:61-121). Reproduced via `displayedRows`, matching only the two
 *   visible text columns (code/description), same practical interpretation
 *   used for this same legacy `filter:keyword` pattern elsewhere in this
 *   rebuild.
 *
 * - The per-column sort caret icons (view:80-119, "Status(n)"/"Action taken"
 *   column headers included) carry no `ng-click` anywhere in the view or
 *   controller -- confirmed dead UI. Rendered as inert, decorative markup
 *   here rather than given working sort behavior this rebuild would be
 *   inventing.
 *
 * - `showUpdateBtn` (ctrl:213-241) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETCABINMASTERBYID` call resolves.
 *   Reproduced verbatim (`saveMode.set(false)` up front).
 *
 * - `updateStatus` (ctrl:295-331) POSTs the ENTIRE `$scope.common` edit-form
 *   object as the payload (a sloppy full-object POST, not a deliberate
 *   contract -- same anti-pattern family as counterMaster/
 *   employeeCategoryMaster). This rebuild sends a clean minimal
 *   `{cabinId, status, updatedBy, updatedDate}` payload instead (see
 *   `CabinStatusPayload` in cabin-master.models.ts) -- a low-severity
 *   anti-pattern fix, not a behavior change a user would notice, since the
 *   backend only reads those four fields for this call.
 *
 * - All three write flows (`save`/`update`/`updateStatus`, ctrl:180-201,
 *   270-288, 308-327) only call `$scope.initMaster()` (reset the add/edit
 *   form) and `$scope.initMasterList(...)` (re-fetch the table) on the
 *   SUCCESS branch -- the error branch is growl-only, no reset, no reload.
 *   Reproduced identically: `resetForm()`/`reloadFirstPage()` (or
 *   `loadList()`/`loadCount()` for the status toggle) only run inside the
 *   `res.status === 'success'` branch below.
 *
 * - Both `save` and `update` build `createdDate`/`updatedDate` client-side via
 *   `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')` (ctrl:155-156, 247-248)
 *   rather than letting the server stamp them -- kept via the same local
 *   `formatDmyHms()` used elsewhere in this rebuild.
 */
@Component({
  selector: 'app-cabin-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './cabin-master.html',
  styleUrl: './cabin-master.scss',
})
export class CabinMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CabinMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<CabinForm>(emptyCabinForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initMasterList/setPage) ----
  list = signal<CabinRow[]>([]);
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
      (r) => r.cabinCode?.toLowerCase().includes(keyword) || r.cabinDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
    this.loadCount();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  private loadCount(): void {
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  private reloadFirstPage(): void {
    this.currentPage.set(1);
    this.loadList();
    this.loadCount();
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

  updateField<K extends keyof CabinForm>(field: K, value: CabinForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.cabinCode.trim() && f.cabinDesc.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors save()'s popUpFlag-gated
  // double call via PopupOkBtn, ctrl:143-206). ----
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
        cabinDesc: f.cabinDesc,
        cabinCode: f.cabinCode,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.reloadFirstPage();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's update()
  // being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        cabinId: f.cabinId as number,
        cabinDesc: f.cabinDesc,
        cabinCode: f.cabinCode,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.reloadFirstPage();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyCabinForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:213-241 -- flips into update mode
  // BEFORE the GETCABINMASTERBYID response arrives; reproduced verbatim). ----
  edit(row: CabinRow): void {
    this.saveMode.set(false);
    this.api.getById(row.cabinId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            cabinId: detail.cabinId,
            cabinCode: detail.cabinCode,
            cabinDesc: detail.cabinDesc,
          });
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:295-331 -- resets the
  // add/edit form on success only, and always re-fetches the list on both
  // branches; see class comment for the minimal-payload deviation). ----
  toggleStatus(row: CabinRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.cabinId === row.cabinId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        cabinId: row.cabinId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
            this.loadCount();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:155-156/247-248. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
