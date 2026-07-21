import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DoctorCategoryMasterService } from './doctor-category-master.service';
import { DoctorCategoryRow } from './doctor-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/doctor-category-master.html +
 * resources/assets/js/controller/master/global/common/doctorCategoryMasterController.js
 * (`doctorCategoryMasterController`, plain `$scope`, no `controllerAs` --
 * confirmed 0 `vm.*` bindings in the view). Legacy state `doctorCategoryMaster`
 * (app.js:14285-14312), gated by a resolve-block check for moduleId 499 --
 * wired at the ROUTE level via `moduleAccessGuard` (`data: { moduleId: 499 }`),
 * not in this component. No `requireSuperUser` check in the resolve block.
 *
 * **Step 0 confirmed this is a real, singly-wired screen, not a duplicate.**
 * There IS a same-module sibling state, `doctorCategory` (app.js:8055,
 * moduleId 453, `doctorCategoryController`, already rebuilt separately as
 * `features/doctor-category/`) -- a genuinely different screen (billing
 * doctor-category-vs-tariff master), not a duplicate/dead-route pair like
 * the `addItemPOCreation` shape. Confirmed via grep: exactly one
 * `.state('doctorCategoryMaster', ...)` registration exists anywhere in the
 * codebase.
 *
 * **Backend confirmed real end to end** (not a `modalityTypeMaster`/entry-67
 * style dead backend): all 6 endpoints this screen calls resolve to a real
 * Controller -> Service -> DAO chain in
 * legacy/Global/src/main/java/com/param/global/billing/{controller,service,dao}/
 * DoctorCategoryMaster* (real Hibernate named queries/Criteria, not stubs).
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - Save vs. Update is asymmetric (same family as `ambulanceTypeMaster`/
 *   `doctorCategory`): clicking Save (`saveBtnFlag`, ctrl:144-202) validates
 *   the form, then opens a confirm modal ("Are you sure you want to Save?")
 *   before the create POST actually fires (`PopupOkBtn()`, ctrl:139-142, sets
 *   `popUpFlag=false` and re-invokes `save()`, which is when the real POST
 *   happens). Clicking Update (ctrl:240-289) validates and POSTs
 *   immediately -- legacy genuinely has no confirm step wired to `update()`.
 *
 * - **Uses the shared `<modal visible="showModal">` directive for the save
 *   confirm** (legacy view line 156). Per `docs/ANGULAR_PENDING_FINDINGS.md`
 *   entry 55's now-resolved standing policy: legacy's shared directive
 *   hardcodes its x-close button to call `cancel()`
 *   (`commonPopUpDirectives.js:10`), a function this controller never
 *   defines (confirmed: no `cancel` anywhere in the 331-line source) -- a
 *   real, live crash in legacy today on that one click. The standing policy
 *   is NOT to reproduce that crash: this rebuild's confirm modal below ships
 *   an ordinary working close button (`closeConfirmModal()`), matching how
 *   11 of the 12 already-affected screens' rebuilds already handle this
 *   shared-directive divergence. Not re-logged as a new finding per that
 *   policy.
 *
 * - Duplicate-code validation happens entirely server-side
 *   (`getDoctorCategoryMasterByName`/`...ByNameNotId` in the DAO) -- the
 *   save/update response comes back `status:"error"`,
 *   `message:"Already Exists."` and this rebuild just surfaces that
 *   server message verbatim, matching legacy's own `growl.error(response.data.message)`
 *   (ctrl:187-193/276-283) rather than inventing a client-side duplicate
 *   check.
 *
 * - The table's sort-arrow icons (fa-caret-up/down, view lines 81-88/92-99/
 *   etc.) are dead decoration: the view's `orderBy:column:reverse` filter
 *   expression references `column`/`reverse` scope variables that are never
 *   assigned anywhere in the controller and have no `ng-click` in the view
 *   to set them either -- `orderBy` on an undefined expression is a no-op.
 *   Not reproduced as real column sorting (there is none to reproduce).
 *
 * - Search box (`searchKeyword`) filters the CURRENT PAGE's already-fetched
 *   rows only (bare `filter:searchKeyword` over `commonList`, the
 *   server-side-paginated page of up to `noOfRecordsPerPage` rows) -- same
 *   quirk class as `ambulanceTypeMaster`/`assetCategoryMaster`. Reproduced
 *   via `displayedRows` below, not a full-dataset server search.
 *
 * - `updateStatus(id, status)` reuses the same `$scope.common` object used
 *   by the create/edit form for its status-change payload (ctrl:291-327) --
 *   looks on paper like it could leak stale form field values into an
 *   unrelated row's update, but confirmed harmless by reading the actual
 *   DAO (`DoctorCategoryMasterDaoImpl.updateDoctorCategoryMasterStatus`):
 *   it loads the target row fresh via `findById(doctorCategoryId)` and only
 *   ever sets `status`/`updatedBy`/`updatedDate` on it -- any
 *   code/description fields in the request payload are read by nothing.
 *   This rebuild sends only the 5 fields the DAO actually uses.
 */
@Component({
  selector: 'app-doctor-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './doctor-category-master.html',
  styleUrl: './doctor-category-master.scss',
})
export class DoctorCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DoctorCategoryMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  doctorCategoryCode = signal('');
  doctorCategoryDescription = signal('');
  private activeCategoryId: number | undefined;
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors PagerService.GetPager) ----
  list = signal<DoctorCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.doctorCategoryCode?.toLowerCase().includes(keyword) ||
        r.doctorCategoryDescription?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
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

  // ---- validation (mirrors doctorCateMaasterForm.$valid over the two required fields) ----
  isCodeInvalid(): boolean {
    return this.submitted() && !this.doctorCategoryCode().trim();
  }

  isDescriptionInvalid(): boolean {
    return this.submitted() && !this.doctorCategoryDescription().trim();
  }

  private validate(): boolean {
    this.submitted.set(true);
    return !!this.doctorCategoryCode().trim() && !!this.doctorCategoryDescription().trim();
  }

  // ---- save (mirrors save()'s popUpFlag confirm dance) ----
  openSaveConfirm(): void {
    if (!this.validate()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const now = formatDateTime(new Date());
    this.api
      .save({
        doctorCategoryCode: this.doctorCategoryCode(),
        doctorCategoryDescription: this.doctorCategoryDescription(),
        organizationId: this.organizationId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully.');
            this.clearFields();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Already Exists.');
          }
        },
      });
  }

  // ---- update (mirrors update() -- no confirm modal) ----
  update(): void {
    if (!this.validate()) return;
    const now = formatDateTime(new Date());
    this.api
      .update({
        doctorCategoryId: this.activeCategoryId as number,
        doctorCategoryCode: this.doctorCategoryCode(),
        doctorCategoryDescription: this.doctorCategoryDescription(),
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
            this.clearFields();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Already Exists.');
          }
        },
      });
  }

  private clearFields(): void {
    this.doctorCategoryCode.set('');
    this.doctorCategoryDescription.set('');
    this.activeCategoryId = undefined;
    this.saveMode.set(true);
    this.submitted.set(false);
  }

  // ---- edit (mirrors showUpdateBtn) ----
  edit(doctorCategoryId: number): void {
    this.api.getById(doctorCategoryId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const detail = res.listObject[0];
        this.doctorCategoryCode.set(detail.doctorCategoryCode);
        this.doctorCategoryDescription.set(detail.doctorCategoryDescription);
        this.activeCategoryId = detail.doctorCategoryId;
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- see class doc comment on why
  // only these 5 fields are sent, not the whole $scope.common object) ----
  toggleStatus(row: DoctorCategoryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .changeStatus({
        doctorCategoryId: row.doctorCategoryId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.update((rows) =>
              rows.map((r) => (r.doctorCategoryId === row.doctorCategoryId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success(res.message ?? 'Updated Successfully.');
          } else {
            this.notify.error(res.message ?? 'Something went wrong.');
          }
        },
        error: () => this.notify.error('Something went wrong.'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:151-152/243-244/293-294/300. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
