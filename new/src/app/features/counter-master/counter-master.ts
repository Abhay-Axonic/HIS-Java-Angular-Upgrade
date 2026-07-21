import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CounterMasterService } from './counter-master.service';
import { CounterForm, CounterRow, emptyCounterForm } from './counter-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/counter-master.html +
 * resources/assets/js/controller/master/unit/counterMasterController.js
 * (`counterMasterController`, no `controllerAs`). Route guard: moduleId 704
 * (app.js:8868-8894 resolve block), wired at the route level, not here.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - Save vs. Update is asymmetric, same family as ward-master/
 *   qualification-master's own save/update split: clicking Save (`saveBtnFlag`,
 *   ctrl:147-206) validates via `counterMasterForm.$valid`, then opens the
 *   shared "Are you sure you want to Save?" confirm modal before actually
 *   POSTing (only the modal's Ok -> `PopupOkBtn()`, ctrl:142-145, re-runs
 *   `save()` with `popUpFlag` now false, which is when the real POST fires).
 *   Clicking Update (`updateBtnFlag`, ctrl:243-292) validates and POSTs
 *   immediately, with NO confirm step at all.
 *
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`common/commonPopUpDirectives.js:10` hardcodes `ng-click="cancel();
 *   stopLoader();"`, and `counterMasterController.js` never defines
 *   `$scope.cancel`) -- but per the standing policy resolved 2026-07-21
 *   (`docs/ANGULAR_PENDING_FINDINGS.md` entry 55, `docs/
 *   ANGULAR_REBUILD_PLAYBOOK.md` Step 3), a shared component's correct,
 *   working default behavior differing from a bug baked into legacy's own
 *   shared directive is an accepted, disclosed standing improvement, not a
 *   parity gap to reproduce. The x button here is an ordinary, working
 *   `closeConfirmModal()`, same pattern as qualification-master.ts.
 *
 * - The header search box (view:70, plain `ng-model="searchKeyword"`, with NO
 *   `ng-keyup` at all -- unlike floorMaster/amenityMaster/wingMaster/
 *   designationMaster, which DO carry a search-crash bug on their own view
 *   family) is a genuinely working filter in legacy, reproduced as such here
 *   rather than as a search-crash. The view's
 *   `ng-repeat="info in commonList|orderBy:column:reverse|filter:searchKeyword"`
 *   (view:124) is a bare Angular `filter` over `commonList`, which only ever
 *   holds the CURRENT PAGE's already-fetched rows (`masterList`/
 *   `initMasterList` refetch on every page change). A user searching for a
 *   counter that exists on a different page sees no match. Reproduced via
 *   `displayedRows`, matching only the two visible text columns
 *   (code/description) -- same practical interpretation already used for
 *   this same legacy `filter:keyword` pattern in qualification-master/
 *   ward-master. `column`/`reverse` (the `orderBy` inputs feeding the
 *   decorative sort-caret icons) are never set anywhere in the controller --
 *   confirmed dead sort, not reproduced.
 *
 * - `updateStatus` (ctrl:294-330) POSTs the ENTIRE `$scope.common` edit-form
 *   object as the status-toggle payload -- a cross-cutting sloppy-payload
 *   anti-pattern shared with this codebase's cabinMaster/
 *   employeeCategoryMaster, being cleaned up as a standing improvement across
 *   all three screens in this batch. Built here as a clean minimal
 *   `{counterId, status, updatedBy, updatedDate}` payload instead (see
 *   `CounterStatusPayload`'s doc comment) -- a request-shape cleanup only,
 *   with no observable behavior difference for the user. Unlike
 *   qualification-master's `updateStatus` (which never reloads), legacy's
 *   version here DOES call `initMasterList(...)` again on success
 *   (ctrl:316-317), fully re-fetching both the list AND the count from the
 *   server -- reproduced faithfully via `loadList()`/`loadCount()` on
 *   success, alongside resetting the add/edit form back to Save mode
 *   (mirroring `initMaster()`, ctrl:316).
 *
 * - The per-column sort caret icons (view:81-120, "Status(n)"/"Action taken"
 *   column headers included) carry no `ng-click` anywhere in the view or
 *   controller -- confirmed dead UI. Rendered as inert, decorative markup
 *   here rather than given working sort behavior this rebuild would be
 *   inventing.
 *
 * - `LoaderService` (start/stopLoader) is not wired here, matching the
 *   established convention of this codebase's other "simple master" screens
 *   (qualification-master, city-master, billing-group-master), none of which
 *   wire it either despite legacy calling `$rootScope.startLoader()/
 *   stopLoader()` around its own save/update/list calls.
 */
@Component({
  selector: 'app-counter-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './counter-master.html',
  styleUrl: './counter-master.scss',
})
export class CounterMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(CounterMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<CounterForm>(emptyCounterForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initMasterList/setPage) ----
  list = signal<CounterRow[]>([]);
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
      (r) => r.counterCode?.toLowerCase().includes(keyword) || r.counterDesc?.toLowerCase().includes(keyword),
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

  updateField<K extends keyof CounterForm>(field: K, value: CounterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.counterCode.trim() && f.counterDesc.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors save()'s popUpFlag-gated
  // double call via PopupOkBtn, ctrl:142-206). ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  // Entry 55 -- see class comment: legacy's shared modal `x` button crashes
  // because $scope.cancel is never defined. Wired to a normal working close
  // here instead of reproducing the crash.
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
        counterDesc: f.counterDesc,
        counterCode: f.counterCode,
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
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's update()
  // being wired directly to the Update button (ctrl:243-292). ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        counterId: f.counterId as number,
        counterDesc: f.counterDesc,
        counterCode: f.counterCode,
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
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.reloadFirstPage();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyCounterForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:212-240) ----
  edit(row: CounterRow): void {
    this.saveMode.set(false);
    this.api.getById(row.counterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            counterId: detail.counterId,
            counterCode: detail.counterCode,
            counterDesc: detail.counterDesc,
          });
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:294-330) -- unlike
  // qualification-master's toggleStatus, legacy here DOES refetch the list
  // AND count from the server on success (see class comment). ----
  toggleStatus(row: CounterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateStatus({
        counterId: row.counterId,
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

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:154-155/246-247/296-297. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
