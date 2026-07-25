import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { TransactionTypeMasterService } from './transaction-type-master.service';
import {
  TransactionTypeForm,
  TransactionTypeRow,
  TransactionTypeStatus,
  emptyTransactionTypeForm,
} from './transaction-type-master.models';
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

/**
 * Parity rebuild of `views/master/GlobalMaster/transaction-type-master.html`
 * + `resources/assets/js/controller/master/transactionTypeMasterController.js`
 * (`transactionTypeMasterController`, no `controllerAs` -- classic
 * `$scope.*` only, confirmed zero `vm.*` in the view). Full Step 1 audit:
 * `docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-batch18-step1-transactiontypemaster`.
 *
 * ================================================================
 * No module gate at all (nothing for this component to enforce)
 * ================================================================
 * The live `.state('transactionTypeMaster', {...})` block (`app.js:20911-
 * 20916`) has only `url`/`templateUrl`/`controller`/`title` -- no `resolve`,
 * no `data`. Confirmed real, pre-existing legacy gap (same class as
 * `billingGroupMaster`/`periodicityService`). The route registered for this
 * screen (wired separately, outside this file) carries no `data.moduleId`,
 * matching legacy exactly -- there is no access-control concern for this
 * component to implement beyond the guard already being global
 * infrastructure.
 *
 * ================================================================
 * Org-only scoping -- deliberate, entity-wide, NOT a bug being fixed here
 * ================================================================
 * Every query touching this entity (list, both duplicate-name checks) is
 * scoped by `orgId` only; `unitId` is either omitted entirely or explicitly
 * commented out in the DAO (`TransactionTypeMasterDaoImpl.java:46`,
 * `//.setParameter("unitId", unitId)`) despite the entity having a real
 * `unit_id` column. This looks like an intentional org-wide design for
 * transaction-type categories (shared across every unit in an organization),
 * not a screen-specific scoping bug -- but it diverges from most sibling
 * master screens' unit+org convention. Built here WITHOUT any unit-filtering
 * assumption legacy doesn't actually enforce: no `unitId` is sent on
 * list/save/update/status-change, matching the real, current backend
 * contract. The audit doc recommends the owner confirm this is genuinely
 * intentional; either way, this rebuild faithfully reproduces current,
 * unmodified behavior.
 *
 * ================================================================
 * Duplicate-check: real and working, but name-only (desc never compared)
 * ================================================================
 * `saveTransactionTypeMaster`/`updateTransactionTypeMaster` both run a real,
 * correctly-parenthesized (`(LOWER(x)=:p OR x=:p) AND orgId=:orgId`)
 * duplicate check against `transactionTypeName` before saving/updating --
 * genuinely functioning, not dead code. `transactionTypeDesc` is never
 * compared for uniqueness on either path, even though it's a required,
 * UI-presented field. No client-side duplicate check is added on top here --
 * that would mask/diverge from the real server behavior. Faithfully
 * reproduced as-is.
 *
 * ================================================================
 * Confirm-modal gating -- clean and correctly gated (unlike several batch
 * siblings)
 * ================================================================
 * `saveTransactionType()` (ctrl) gates the real POST behind `popUpFlag`:
 * the first click (once the form is valid) opens the confirm modal without
 * saving; the actual save only fires via `PopupOkBtn` after the modal's Ok.
 * `updateTransactionType()` has no modal concept at all -- update always
 * saves directly, gated only by the same required-field check. Reproduced
 * below as a genuine two-step confirm-before-save on Create, direct-save on
 * Update. Per this codebase's NOW-STANDARD, already-owner-approved
 * correction (see `docs/ANGULAR_PENDING_FINDINGS.md` entry 152, applied
 * project-wide to every sibling screen sharing this exact
 * popUpFlag-reset-only-on-success-init() shape -- `consent-type-master`/
 * `sterilization-type-master` among them), `confirmArmed` below re-arms on
 * BOTH the success and error branches of the create call, not only on
 * success -- a small, already-standing, disclosed divergence, not a fresh
 * per-screen decision.
 *
 * ================================================================
 * Client-side pagination -- there is no separate count endpoint for this
 * screen
 * ================================================================
 * Golden trace (live, this session) showed exactly 2 calls on load: a
 * template fetch + `getTransactionTypeMasterList`, both 200 -- no
 * count-endpoint call anywhere. Legacy's own `init()` POSTs `{ orgId }` once,
 * gets back the FULL org-scoped list unconditionally (no offset/limit params
 * sent, no server-side paging at all), and hands it straight to
 * `PaginationService.searchData()` for entirely client-side paging
 * thereafter. Reproduced via the shared `client-pagination.util` (the same
 * faithful port of `PaginationService` already used by
 * `consent-type-master`/`ward-store-mapper`) over the one full-list response
 * -- there is no `getCount()`/paged `getList(offset, ...)` on this screen's
 * service, matching the real, confirmed absence of a server-side count call.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - `showUpdateBtn(item)` (ctrl) copies only the 3 primitive fields it needs
 *   (`transactionTypeId`/`transactionTypeDesc`/`transactionTypeName`)
 *   directly onto `$scope.transactionType` -- no GET call, no whole-row
 *   aliasing (confirmed clean in the audit, unlike `operationTableType`'s
 *   sibling finding). Reproduced via `edit()` building a fresh form object
 *   from the row's own primitive values.
 * - Both Save and Update growl the SAME literal error text on a rejected
 *   response, `'Transaction Type already exits...!!'` (legacy's own typo,
 *   ctrl) -- reproduced verbatim, not corrected.
 * - Legacy's own form labels BOTH fields "Transaction Type Code"/
 *   "Transaction Type Description" even though the underlying field names
 *   are `transactionTypeName`/`transactionTypeDesc` (and the table header
 *   literally reads "Transaction Type Code" while binding
 *   `info.transactionTypeName`) -- a real, consistent legacy label choice
 *   (form label and table header agree), not a data-binding bug. Reproduced
 *   with the same labels.
 * - `PagerService` is injected in legacy but never referenced anywhere in
 *   the controller -- dead dependency, not reproduced (nothing to port).
 * - Per `docs/ANGULAR_PENDING_FINDINGS.md` entry 55 (standing policy),
 *   legacy's shared `<modal>` directive's close (x) button is hardcoded to
 *   call `cancel()`, a function this controller never defines (grepped,
 *   zero matches) -- a live, disclosed crash on unmodified legacy. This
 *   rebuild's confirm modal gives its x button/backdrop/Cancel button
 *   ordinary working close behavior instead, per the already-approved
 *   standing improvement -- not a fresh decision.
 * - Validation messages: both `transactionTypeName` and `transactionTypeDesc`
 *   are genuinely `required` in legacy's own markup (view, tied to
 *   correctly distinct `name` attributes -- no duplicate-`name` collision
 *   bug here, unlike `sterilizationTypeMaster`'s sibling finding) and render
 *   VISIBLY here per the standing validation-visibility policy (playbook
 *   entry 111/72), instead of legacy's own globally-hidden
 *   `.help-block{ display:none !important; }`.
 */
@Component({
  selector: 'app-transaction-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './transaction-type-master.html',
  styleUrl: './transaction-type-master.scss',
})
export class TransactionTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(TransactionTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.transactionType) ----
  form = signal<TransactionTypeForm>(emptyTransactionTypeForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  formSubmitted = signal(false); // drives visible required-field errors (mirrors show-errors-check-validity)
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment's confirm-modal-gating section. */
  private confirmArmed = true;

  // ---- full list + entirely client-side pagination (see class doc) ----
  private fullList: TransactionTypeRow[] = [];
  pagination = signal<PaginationState<TransactionTypeRow>>(createPaginationState<TransactionTypeRow>());

  // ---- search box -- legacy filters `pagination.data|filter:searchKeyword`,
  // i.e. only the CURRENTLY DISPLAYED PAGE, not the full list (view). ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.pagination().data;
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.transactionTypeName?.toLowerCase().includes(keyword) ||
        r.transactionTypeDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  // mirrors init() -- POST { orgId } once, full list back, client-paginate it.
  loadList(): void {
    this.api.getList(this.organizationId).subscribe({
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

  updateField<K extends keyof TransactionTypeForm>(field: K, value: TransactionTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (both fields genuinely required in
  // legacy's own markup -- $scope.transactionTypeform.$valid gates both
  // save and update) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.transactionTypeName.trim() && f.transactionTypeDesc.trim());
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().transactionTypeName.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().transactionTypeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveTransactionType/PopupOkBtn) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the $valid gate
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl): disarms the gate, closes the modal, then saves.
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
        transactionTypeName: f.transactionTypeName,
        transactionTypeDesc: f.transactionTypeDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        orgId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Transaction Type added Successfully...!!'); // ctrl, verbatim
            this.resetForm(); // ctrl: init() -- re-arms confirmArmed, reloads the full list
          } else {
            // Real, working duplicate-name check -- see class doc comment.
            this.notify.error('Transaction Type already exits...!!'); // ctrl, verbatim legacy typo
            // Already-standing, project-wide re-arm-on-failure-too correction
            // (entry 152) -- see class doc comment.
            this.confirmArmed = true;
          }
        },
        error: () => {
          this.confirmArmed = true;
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn -- copies 3 primitives straight off the
  // row, no GET call, no whole-row aliasing) ----
  edit(row: TransactionTypeRow): void {
    this.saveMode.set(false);
    this.formSubmitted.set(false);
    this.form.set({
      transactionTypeId: row.transactionTypeId,
      transactionTypeName: row.transactionTypeName,
      transactionTypeDesc: row.transactionTypeDesc,
    });
  }

  // ---- update (mirrors updateTransactionType -- no confirm modal, ever) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .update({
        transactionTypeId: f.transactionTypeId as number,
        transactionTypeName: f.transactionTypeName,
        transactionTypeDesc: f.transactionTypeDesc,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        orgId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Transaction Type updated Successfully...!!'); // ctrl, verbatim
            this.resetForm(); // ctrl: init()
          } else {
            this.notify.error('Transaction Type already exits...!!'); // ctrl, verbatim legacy typo
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus) ----
  toggleStatus(row: TransactionTypeRow): void {
    const nextStatus: TransactionTypeStatus = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- legacy's checkbox is two-way bound
    // (ng-model="info.status"), so the visual change happens immediately
    // regardless of the response; ctrl never reverts it on error, and never
    // reloads the list on success either -- only a toast fires either way.
    this.fullList = this.fullList.map((r) =>
      r.transactionTypeId === row.transactionTypeId ? { ...r, status: nextStatus } : r,
    );
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.api
      .updateStatus({
        transactionTypeId: row.transactionTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        orgId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Transaction Type Updated Successfully...!!'); // ctrl, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl, verbatim -- no revert
          }
        },
      });
  }

  // ---- reset (mirrors init(), re-entered by a successful save/update) ----
  private resetForm(): void {
    this.form.set(emptyTransactionTypeForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false);
    this.confirmArmed = true; // ctrl's init() resets popUpFlag = true
    this.loadList(); // ctrl's init() also re-fetches the full list
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl's
 * `$scope.currentDate` formatting used on every save/update/status call. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
