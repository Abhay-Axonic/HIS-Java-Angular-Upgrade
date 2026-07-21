import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AmenityMasterService } from './amenity-master.service';
import { AmenityForm, AmenityRow, emptyAmenityForm } from './amenity-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/amenity-master.html +
 * resources/assets/js/controller/adt/unit/amenityMasterController.js
 * (`amenityMasterController`, no `controllerAs`). moduleId 539
 * (app.js:8770-8792 resolve block, confirmed live). Unit-scoped like this
 * codebase's ward-master (`orgUnitId`/`orgId` off `cookieObject`), not a
 * global/organization-only master.
 *
 * Confirmed real, working CRUD: save (ctrl:143-196), update (ctrl:229-278),
 * edit-populate `showUpdateBtn` (ctrl:198-226), status toggle `updateStatus`
 * (ctrl:280-314) -- all wired to real endpoints, see amenity-master.service.ts.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal ("Are you sure you want to Save?", the shared
 *   `<modal>` directive wired via legacy's `PopupOkBtn`/`popUpFlag`) only
 *   gates CREATE (ctrl:139-196); `update()` (ctrl:229-278) saves immediately
 *   with no confirm step at all -- same asymmetry as this codebase's
 *   ward-master/degree-master/qualification-master. Reproduced via
 *   `save()`/`confirmSave()` vs. `update()`.
 * - Both `save()` (ctrl:164) and `update()` (ctrl:246) only ever check
 *   `$scope.amenityForm.$valid` with no `else` branch -- an invalid submit is
 *   a silent no-op in both directions: no growl, no modal. Reproduced
 *   verbatim via early `return` in both `save()` and `update()`.
 * - Unlike some sibling screens, `initList()` (the list+count reload) is
 *   called ONLY inside the success branch of both save's (ctrl:171-180) and
 *   update's (ctrl:254-263) response handler -- the error branch
 *   (ctrl:181-188/264-271) only growls, it never reloads. Reproduced by
 *   placing `loadList()`/`loadCount()` strictly inside the success branch
 *   below, not unconditionally after both branches.
 * - `updateStatus` (ctrl:280-314) growls only -- unlike `save`/`update`, it
 *   never calls `initMaster()` or `initList()` on success, and never reverts
 *   the already-flipped checkbox on failure either (the view's
 *   `ng-model="info.status"` is two-way bound, so the visual toggle happens
 *   immediately regardless of what the server call actually returns).
 *   Reproduced by optimistically patching the `list` signal before the call
 *   resolves (matching this codebase's degree-master `toggleStatus`), with
 *   no form reset and no list reload on either branch.
 * - `showUpdateBtn` (ctrl:198-226) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETAMENITYMASTERLISTBYID` call
 *   resolves, and uses `growl.error` (not a native `alert`) on a failed
 *   response. Both reproduced verbatim (`saveMode.set(false)` up front;
 *   `notify.error(...)` on failure).
 * - The edit pencil icon is visually disabled (`ng-class="{disabled:
 *   info.status=='I'}"`, view:128) when a row's status is Inactive -- same
 *   pattern as this codebase's degree-master. Reproduced via
 *   `[class.disabled]` plus guarding the click.
 * - The per-column sort caret icons (view lines 84-115, "Status(n)"/"Action
 *   taken" column headers included) carry no `ng-click` anywhere in the view
 *   or controller -- confirmed dead UI, same as this codebase's
 *   ward-master/qualification-master. Omitted entirely here rather than
 *   rendered as inert markup, matching that established convention.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's x close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared-`<modal>`-
 *   directive `cancel()` crash (`commonPopUpDirectives.js` hardcodes
 *   `ng-click="cancel(); stopLoader();"`, and amenityMasterController.js never
 *   defines `$scope.cancel`) -- a working close is an accepted standing
 *   improvement, not a parity gap.
 *
 * **1 confirmed reproducible crash bug, NOT fixed, faithfully reproduced:**
 * The search input (amenity-master.html:72,
 * `ng-keyup="serachTestmaster(searchKeyword);"`) calls a function that is
 * never defined anywhere in amenityMasterController.js (confirmed via
 * full-file read -- same copy-paste-leftover bug already found in this
 * codebase's degree-master/qualification-master). Every keystroke in the
 * search box throws `serachTestmaster is not a function` in legacy, today.
 * Reproduced via a direct throw in `onSearchKeyup()`, wired to an unbound
 * search input -- not implemented as a working filter, and not omitted.
 */
@Component({
  selector: 'app-amenity-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './amenity-master.html',
  styleUrl: './amenity-master.scss',
})
export class AmenityMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AmenityMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<AmenityForm>(emptyAmenityForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initList/getList/PagerService) ----
  list = signal<AmenityRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    this.loadList();
    this.loadCount();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getAmenityMasterList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  private loadCount(): void {
    this.api.getAmenityMasterListCount(this.organizationId, this.unitId).subscribe({
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

  updateField<K extends keyof AmenityForm>(field: K, value: AmenityForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors amenityForm.$valid via the 2
  // `required` inputs -- AmenityCode/AmenityDescription, view:15/29) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.amenityCode.trim() && f.amenityDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().amenityCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().amenityDesc.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:139-196) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:164 guard (no else branch)
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
      .saveAmenityMaster({
        amenityCode: f.amenityCode,
        amenityDesc: f.amenityDesc,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:171-180 -- initMaster()/initList() only run on success;
            // the error branch (ctrl:181-188) growls only. See class comment.
            this.resetForm();
            this.loadList();
            this.loadCount();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  // ---- update (mirrors update, ctrl:229-278) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:246 guard (no else branch)
    const f = this.form();
    this.api
      .updateAmenityMaster({
        amenityCode: f.amenityCode,
        amenityDesc: f.amenityDesc,
        amenityId: f.amenityId as number,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:254-263 -- initMaster()/initList() only run on success;
            // the error branch (ctrl:264-271) growls only. See class comment.
            this.resetForm();
            this.loadList();
            this.loadCount();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyAmenityForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:198-226) ----
  edit(amenityId: number): void {
    this.saveMode.set(false);
    this.api.getAmenityMasterListById(amenityId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error('Error!'); // ctrl:216-219 -- growl.error, not a native alert
          return;
        }
        const d = res.listObject[0];
        this.form.set({ amenityId: d.amenityId, amenityCode: d.amenityCode, amenityDesc: d.amenityDesc });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:280-314) ----
  toggleStatus(row: AmenityRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // ctrl:280-314 -- growl only, no initMaster()/initList() call on success,
    // and no revert on failure (the view's checkbox is two-way bound via
    // ng-model, so it has already visually flipped regardless of the
    // response); see class comment.
    this.list.update((rows) => rows.map((r) => (r.amenityId === row.amenityId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateAmenityMasterStatus({
        amenityId: row.amenityId,
        status: nextStatus,
        updatedDate: formatDateTime(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * amenity-master.html:72 wires `ng-keyup="serachTestmaster(searchKeyword);"`
   * to a function never defined anywhere in amenityMasterController.js.
   * See docs/ANGULAR_PENDING_FINDINGS.md. */
  onSearchKeyup(): void {
    throw new TypeError('serachTestmaster is not a function');
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:146-147/233-234/282-283. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
