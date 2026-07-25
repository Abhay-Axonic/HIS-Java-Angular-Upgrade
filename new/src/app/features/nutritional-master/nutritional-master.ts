import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import {
  PaginationState,
  createPaginationState,
  searchData,
  setFirstPage,
  setLastPage,
  setNextPage,
  setPage,
  setPreviousPage,
  getPagesList,
} from '../../core/client-pagination.util';
import { NutritionalMasterService } from './nutritional-master.service';
import { NutritionalMasterForm, NutritionalMasterRow, emptyNutritionalMasterForm } from './nutritional-master.models';

/** Large enough to fetch every real-world org's nutrition master in one
 * request -- see class doc comment for why this screen doesn't use
 * server-side offset/limit paging. */
const FULL_LIST_SIZE = 100000;

/**
 * Parity rebuild of
 * `views/master/GlobalMaster/DietNutrition/nutritional-master.html` +
 * `resources/assets/js/controller/dietAndKitchen/global/nutritionalMasterController.js`.
 * Full Step 1 audit: `docs/ANGULAR_PENDING_FINDINGS.md`, Batch 19.
 *
 * Module gate: id 508, confirmed via the live `.state('nutritionalMaster',
 * ...)` resolve block (`app.js:11534-11559`).
 *
 * ================================================================
 * Pagination -- CORRECTED here (owner-approved): client-side, not
 * dependent on the broken count endpoint
 * ================================================================
 * `getNutritionMasterCount` throws HTTP 500 on every call server-side
 * (`docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-nutritionalmaster-count-endpoint-crash`, root-caused to a stray
 * `session.close()` call, proposed as a separate one-line legacy fix, not
 * yet applied). Rather than depend on that endpoint at all -- which would
 * still fail today regardless of what this Angular code does, since the bug
 * lives in the shared backend -- this screen fetches the full org+unit-
 * scoped list once (the `getAll` endpoint's own offset/limit IS confirmed
 * working) and paginates it entirely client-side via the shared
 * `client-pagination.util`, the same faithful port of legacy's own
 * `PaginationService` already used by `transaction-type-master`/
 * `ward-store-mapper`.
 *
 * ================================================================
 * Edit -- copies straight off the row (NOT a GET-by-id call, unlike
 * ARmaster/anaesthesiaType's siblings)
 * ================================================================
 * `editNutrition(nutritionObj)` (ctrl) assigns the clicked row object
 * directly to `$scope.nutritionalMasterObj`, no server round-trip.
 * Reproduced via `edit()` building the form straight from the row's own
 * values.
 *
 * ================================================================
 * Validation -- HTML5 required + broadcast, single layer
 * ================================================================
 * Both `nutritionCode`... actually only `nutritionName` is HTML5-required in
 * legacy's own markup (`nutritionCode` has no `required` attribute) --
 * confirmed via the view; `$scope.nutritionform.$valid` gates both
 * save/update. Reproduced: only Name is enforced as required below.
 *
 * ================================================================
 * Confirm-modal: fires ONLY on this page-load's first-ever Save (same
 * one-way `popUpFlag` shape confirmed across this entire batch)
 * ================================================================
 * `$scope.popUpFlag` is set `true` exactly once and never reset back,
 * including after a successful save's `$scope.init()` call (confirmed:
 * `init()` does not touch `popUpFlag`). Reproduced via `confirmArmed`.
 *
 * ================================================================
 * Duplicate-check: name AND code (both required to match), org-scoped only
 * ================================================================
 * Create/Update both reject only when BOTH `nutritionName` and
 * `nutritionCode` match an existing row (case-insensitive), scoped by
 * `organizationId` only -- `unitId` is accepted through the whole stack but
 * commented out in the actual named queries (model/NutritionalMaster.java),
 * so this screen is functionally org-only scoped for both listing and
 * duplicate-checking, despite looking unit-scoped. Faithfully reproduced;
 * no client-side duplicate pre-check added.
 *
 * ================================================================
 * SEVERITY-1, confirmed, NOT fixable from this frontend: cross-org write on
 * Update/status-toggle
 * ================================================================
 * `NutritionalMasterDaoImpl.updateNutritionalMaster`/
 * `.updateNutritionMasterStatus` load the entity by primary key ONLY --
 * no organizationId/unitId check anywhere in either write path
 * (`docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-nutritionalmaster-crossorg-unscoped-write`). This is server-side
 * logic in the SAME live endpoints both legacy and this rebuild call --
 * there is nothing a well-behaved frontend can send to close this gap; the
 * separate, pending legacy-fix branch is what actually resolves it. Worth
 * noting explicitly: legacy's own status-update payload doesn't even
 * attempt to send `organizationId`/`unitId` (both commented out in
 * `updateStatus`'s own `data` object) -- reproduced faithfully in
 * `NutritionalMasterStatusPayload` above, not "corrected" to send fields
 * the backend wouldn't check anyway.
 *
 * ================================================================
 * Known, disclosed, non-blocking issues -- NOT fixed here
 * ================================================================
 * Both the shared `<modal>` and `<dietCssdPopUp>` directives hardcode calls
 * to `cancel()`/`stopLoader()`, which this controller never defines -- per
 * the standing entry-55 policy, this rebuild's dialogs have ordinary working
 * close behavior instead.
 */
@Component({
  selector: 'app-nutritional-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './nutritional-master.html',
  styleUrl: './nutritional-master.scss',
})
export class NutritionalMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(NutritionalMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<NutritionalMasterForm>(emptyNutritionalMasterForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. */
  private confirmArmed = true;

  private fullList: NutritionalMasterRow[] = [];
  pagination = signal<PaginationState<NutritionalMasterRow>>(createPaginationState<NutritionalMasterRow>());

  constructor() {
    this.loadList();
  }

  loadList(): void {
    this.api.getAll(this.organizationId, this.unitId, 0, FULL_LIST_SIZE).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.fullList = res.listObject ?? [];
          const p = this.pagination();
          searchData(this.fullList, p);
          this.pagination.set({ ...p });
        }
      },
    });
  }

  setPageSize(): void {
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  onSearchChange(term: string): void {
    const p = this.pagination();
    p.search = term;
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }

  updateField<K extends keyof NutritionalMasterForm>(field: K, value: NutritionalMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation -- only Name is HTML5-required in
  // legacy's own markup, see class doc comment ----
  private requiredFieldsValid(): boolean {
    return !!this.form().nutritionName.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().nutritionName.trim();
  }

  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    if (this.confirmArmed) {
      this.showConfirmModal.set(true);
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        nutritionCode: f.nutritionCode,
        nutritionName: f.nutritionName,
        organizationId: this.organizationId,
        unitId: this.unitId,
        status: 'A',
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Nutrition Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Nutrition already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // ---- edit (mirrors editNutrition -- copies straight off the row, no
  // GET-by-id call, see class doc) ----
  edit(row: NutritionalMasterRow): void {
    this.saveMode.set(false);
    this.formSubmitted.set(false);
    this.form.set({ nutritionId: row.nutritionId, nutritionCode: row.nutritionCode, nutritionName: row.nutritionName });
  }

  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .update({
        nutritionId: f.nutritionId as number,
        nutritionCode: f.nutritionCode,
        nutritionName: f.nutritionName,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Nutrition Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Nutrition already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // ---- status toggle -- payload deliberately has no org/unit, see class
  // doc comment ----
  toggleStatus(row: NutritionalMasterRow): void {
    const nextStatus = row.status === 'A' ? 'I' : 'A';
    this.fullList = this.fullList.map((r) => (r.nutritionId === row.nutritionId ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.api
      .updateStatus({ nutritionId: row.nutritionId, status: nextStatus, updatedBy: this.userId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Nutrition Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }

  // Deliberately does NOT touch `confirmArmed` -- legacy's own `init()`
  // (called here from `doSave`/`update` on both success and failure) never
  // resets `popUpFlag` either. See class doc comment.
  private resetForm(): void {
    this.form.set(emptyNutritionalMasterForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false);
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
