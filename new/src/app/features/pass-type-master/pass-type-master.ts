import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PassTypeMasterService } from './pass-type-master.service';
import { PassTypeForm, PassTypeRow, emptyPassTypeForm } from './pass-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/Visit Type/pass-Type-Master.html +
 * resources/assets/js/controller/master/unit/visitorTypeMasterController.js
 * (`visitorTypeMasterController`, no `controllerAs`). Legacy state
 * `passTypeMaster` (app.js:19937-19960), gated by a resolve-block check for
 * moduleId 821 -- confirmed live, wired at the ROUTE level via
 * `moduleAccessGuard` (`data: { moduleId: 821 }`), not in this component.
 *
 * Audited in full (Step 0/Step 1) and found completely clean -- no bugs
 * found in this screen. The items below are deliberate, faithfully-
 * preserved quirks, not defects.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal ("Are you sure you want to Save?", the shared
 *   `<modal>` directive wired via legacy's `PopupOkBtn`/`popUpFlag`,
 *   ctrl:169-172) gates CREATE only (`save()`, ctrl:175-227); `update()`
 *   (ctrl:295-339) posts immediately with no confirm step -- same asymmetry
 *   as this codebase's amenity-master/qms-department-master.
 * - Unlike `employeeCategoryMaster`/`wingMaster`'s once-per-session
 *   regression (docs/ANGULAR_PENDING_FINDINGS.md entry 64), this screen's
 *   confirm modal correctly REAPPEARS on every save: `init()` resets
 *   `popUpFlag = true` (ctrl:50), and `init()` is re-invoked on every
 *   successful save/update (ctrl:211, ctrl:320), not just once at
 *   construction. Reproduced via `resetForm()` re-arming the modal every
 *   time, mirroring the already-established `resetForm()`-reinitializes
 *   pattern (amenity-master/qms-department-master).
 * - Both `save()` (ctrl:186) and `update()` (ctrl:303) only ever check
 *   `$scope.visitorPassForm.$valid` with no `else` branch -- an invalid
 *   submit is a silent no-op in both directions: no growl, no modal.
 *   Reproduced verbatim via early `return` in both `save()` and `update()`.
 * - `init()` (ctrl:24-62) -- re-run after every successful save/update --
 *   hardcodes `offset = 0` / `noOfRecordsPerPage = 10` every time, and the
 *   post-save/update reload (ctrl:221, ctrl:330-334) always uses those
 *   freshly-reset values. So a successful save/update forcibly snaps the
 *   list back to page 1 / page-size 10, discarding whatever page size the
 *   user had selected -- reproduced by `resetForm()` also resetting
 *   `pageSize`/`currentPage` to their defaults, not just the form fields.
 * - `init()` also clears `$scope.searchKeyword` (ctrl:40) on every
 *   save/update -- reproduced by clearing the search box in `resetForm()` too.
 * - `showUpdateBtn` (ctrl:229-254) flips into update mode
 *   (`updateBtnFlag = true`) synchronously, BEFORE the
 *   `getPassTypeMasterById` call resolves, and uses a native `alert(...)`
 *   (ctrl:250), NOT `growl.error`, on a failed response -- a real
 *   divergence from amenity-master's equivalent path. Both reproduced
 *   verbatim (`saveMode.set(false)` up front; `window.alert(...)` on
 *   failure).
 * - The edit pencil icon (view:124-127) has NO disabled-when-inactive
 *   guard, unlike this codebase's amenity-master/degree-master -- inactive
 *   rows remain editable here, matching legacy exactly. Deliberately NOT
 *   given a `[class.disabled]`/guarded click.
 * - The search box (view:80, `ng-model="searchKeyword"`) is a genuinely
 *   LIVE, working filter here -- bound directly to AngularJS's built-in
 *   `filter:searchKeyword` table expression (view:114), with no custom
 *   `ng-keyup` handler at all. This is NOT the dead "serachTestmaster"
 *   copy-paste crash bug found in this codebase's amenity-master/
 *   degree-master/qualification-master -- that pattern does not appear
 *   anywhere in this controller (confirmed via full-file read). Reproduced
 *   here as a real, working `displayedRows` filter across both rendered
 *   text columns (Code/Description), matching legacy's default `filter`
 *   behavior on the current, already-paginated page.
 * - `updateStatus` (ctrl:256-293) growls only -- it never calls `init()` or
 *   reloads the list on success, and never reverts the already-flipped
 *   checkbox on failure either (the view's `ng-model="info.status"` is
 *   two-way bound, so the visual toggle happens immediately regardless of
 *   what the server call actually returns). Reproduced by optimistically
 *   patching the `list` signal before the call resolves, with no form reset
 *   and no list reload on either branch.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's x close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared-
 *   `<modal>`-directive `cancel()` crash (`commonPopUpDirectives.js`
 *   hardcodes `ng-click="cancel(); stopLoader();"`, and
 *   visitorTypeMasterController.js never defines `$scope.cancel`) -- a
 *   working close is an accepted standing improvement, not a parity gap.
 */
@Component({
  selector: 'app-pass-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './pass-type-master.html',
  styleUrl: './pass-type-master.scss',
})
export class PassTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PassTypeMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.visitorType) ----
  form = signal<PassTypeForm>(emptyPassTypeForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initVisitorTypeMasterList/PagerService) ----
  list = signal<PassTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Live filter (see class doc comment) across the current page's two
  // rendered text columns -- matches legacy's bare `filter:searchKeyword`
  // expression, which searches only the already-fetched page.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (r) => r.visitorPassTypeCode?.toLowerCase().includes(keyword) || r.visitorPassTypeDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  /** Mirrors `initVisitorTypeMasterList` (ctrl:72-114) -- fetches list +
   * count together, always from offset 0 / size 10 (ctrl:24-62's `init()`
   * hardcodes both). Used for initial load and after every successful
   * save/update; NOT used for real Next/Previous/page-number navigation. */
  private loadListAndCount(): void {
    this.api.getPassTypeMasterList(this.unitId, this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getPassTypeMasterCount(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  /** Mirrors `getVisitorTypeMasterList` (ctrl:116-143) via `setPage(page,
   * true)` -- real page/size navigation, honors the requested offset/size,
   * no count refetch. */
  private loadPage(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getPassTypeMasterPage(this.unitId, this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadPage();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage();
  }

  updateField<K extends keyof PassTypeForm>(field: K, value: PassTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors visitorPassForm.$valid via the 2
  // `required` inputs -- visitorPassTypeCode/visitorPassTypeDesc, view:12/26) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.visitorPassTypeCode.trim() && f.visitorPassTypeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().visitorPassTypeCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().visitorPassTypeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:175-227) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:186 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    this.api
      .savePassTypeMaster({
        visitorPassTypeCode: f.visitorPassTypeCode,
        visitorPassTypeDesc: f.visitorPassTypeDesc,
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Added Successfully...!!');
            // ctrl:211 -- init() (re-arms popUpFlag, resets page/search) only
            // runs on success; see class doc comment.
            this.resetForm();
          } else {
            this.notify.error('Already exits...!!');
          }
          // ctrl:221 -- the reload fires unconditionally after either branch.
          this.loadListAndCount();
        },
      });
  }

  // ---- update (mirrors update, ctrl:295-339) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:303 guard (no else branch)
    const f = this.form();
    this.api
      .updatePassTypeMaster({
        visitorPassTypeId: f.visitorPassTypeId as number,
        visitorPassTypeCode: f.visitorPassTypeCode,
        visitorPassTypeDesc: f.visitorPassTypeDesc,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Already exits...!!');
          }
          this.loadListAndCount();
        },
      });
  }

  /** Mirrors `init()` (ctrl:24-62), re-run after every successful
   * save/update -- resets the form, re-arms the confirm modal
   * (`popUpFlag = true`), and snaps page-size/page/search back to defaults.
   * See class doc comment. */
  private resetForm(): void {
    this.form.set(emptyPassTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.currentPage.set(1);
    this.searchKeyword.set('');
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:229-254) ----
  edit(visitorPassTypeId: number): void {
    this.saveMode.set(false);
    this.api.getPassTypeMasterById(visitorPassTypeId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:250 -- a native alert, not growl.error
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          visitorPassTypeId: d.visitorPassTypeId,
          visitorPassTypeCode: d.visitorPassTypeCode,
          visitorPassTypeDesc: d.visitorPassTypeDesc,
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:256-293) ----
  toggleStatus(row: PassTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // ctrl:256-293 -- growl only, no init()/list reload on success, and no
    // revert on failure (the view's checkbox is two-way bound via
    // ng-model, so it has already visually flipped regardless of the
    // response); see class comment.
    this.list.update((rows) =>
      rows.map((r) => (r.visitorPassTypeId === row.visitorPassTypeId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updatePassTypeMasterStatus({
        visitorPassTypeId: row.visitorPassTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:257-259. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
