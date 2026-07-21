import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BankBranchService } from './bank-branch.service';
import { BankBranchForm, BankBranchRow, emptyBankBranchForm } from './bank-branch.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/bank-Branch.html +
 * resources/assets/js/controller/master/global/billing/bankBranchController.js
 * (`bankBranchController`, no `controllerAs`). State `bankBranch`
 * (app.js:8109-8135), moduleId 455, confirmed live (not commented out).
 *
 * **Relationship to bank-master (already rebuilt, batch 7), confirmed by
 * reading bankMasterController.js:** this screen manages the exact rows that
 * feed bank-master's Branch `<select>` (via `GETACTIVEBANKBRANCHMASTERLIST`,
 * the "active-only" sibling of this screen's own list endpoint). The empty
 * dropdown observed testing bank-master live is therefore very likely just
 * a seed-data gap (no branch rows created yet in this dev DB) -- this screen
 * is exactly what unblocks it, not a separate code defect.
 *
 * Confirmed real, working CRUD during Step 0/1 audit: list+count
 * (ctrl:78-134), save (ctrl:197-263), edit-populate `showUpdateBtn`
 * (ctrl:266-291), update (ctrl:334-385), status toggle `updateStatus`
 * (ctrl:294-331) -- all wired to real endpoints (confirmed live, 0/12
 * endpoints 404'd per `check_backend_endpoints.py`), see
 * bank-branch.service.ts.
 *
 * **Sibling-divergence note (per playbook Step 1's "sibling" checklist):**
 * this controller is near-identical in skeleton to employeeCategoryController.js
 * (same DI list, same `init()`/pagination/popup shape) but does NOT have that
 * sibling's org-id typo bug -- `$scope.orgnisationId` is used consistently at
 * every call site here (ctrl:39,76,91,98,146,179,217,275,344), never a
 * separate, never-assigned `$scope.organizationId`. A textbook case of the
 * playbook's "two screens that look structurally identical can diverge in
 * exactly one place" pattern -- confirmed, not assumed, by diffing both
 * controllers line-for-line.
 *
 * **1 real, previously-uncatalogued bug shared with employee-category's
 * sibling screen, NOT fixed, faithfully reproduced:** toggling ANY row's
 * status silently resets the create/update form in progress (ctrl:294-331,
 * `updateStatus`'s success branch calls `$scope.init()`, which clears
 * `$scope.bankBranchMasterObj` and forces `saveBtnFlag=true`/
 * `updateBtnFlag=false`, with no list reload at all). Reproduced via
 * `resetForm()` inside `toggleStatus`'s success branch.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `bankId`/`branchAddress` are dead fields seeded in `init()` (ctrl:51-58)
 *   but never bound to any input and never sent in any request body -- pure
 *   inert copy-paste residue, deliberately NOT modeled as form fields at all
 *   (see bank-branch.models.ts's `BankBranchForm` doc comment).
 * - The pagination-page fetch function is literally named
 *   `getPatientCategoryMasterList` (ctrl:136) -- obvious copy-paste-from-a-
 *   different-screen residue (this screen has nothing to do with patient
 *   categories). Purely cosmetic (an internal function name never surfaced
 *   to the user); its own `orgnisationId` parameter is dead (ctrl:146 reads
 *   `$scope.orgnisationId` directly instead) but resolves to the same
 *   correct value either way, so -- unlike employee-category's equivalent --
 *   this is harmless. Reproduced as `loadListOnly()` here with no
 *   functional difference from `employee-category`'s broken counterpart.
 * - The confirm modal (shared `<modal>` directive, `PopupOkBtn`/`popUpFlag`)
 *   only gates CREATE (ctrl:197-263); `update()` (ctrl:334-385) saves
 *   immediately with no confirm step -- same asymmetry as amenity-master/
 *   bank-master/employee-category.
 * - List reload runs ONLY inside the success branch's own nested call, but
 *   note `saveBankBranch()` (ctrl:230-259) ALSO calls the reload
 *   unconditionally right after (ctrl:252-257, outside the if/else) -- so in
 *   practice `initBankBranchMaster` runs twice on a successful save (once
 *   inside the success branch at ctrl:233, once more unconditionally at
 *   ctrl:252-257) and once on a failed save. Reproduced via
 *   `loadListAndCount()` being called both inside the success branch AND
 *   again unconditionally afterward in `confirmSave()` below, matching that
 *   exact double-call shape. `updateBankBranch()` (ctrl:334-385) has no such
 *   double-call -- its reload is unconditional-only, called once.
 * - **The search box is a genuine, working client-side filter**
 *   (`ng-model="searchKeyword"` + `bankBranchList | filter: searchKeyword`,
 *   view:75,128) -- unlike employee-category's completely dead search input.
 *   Reproduced via a `displayedRows` computed signal, matching this
 *   codebase's bank-master convention for the same pattern.
 * - The edit pencil is NOT visually disabled for Inactive rows (no
 *   `ng-class="disabled"` anywhere on view:136, matching employee-category,
 *   unlike amenity-master).
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()`, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (`bankBranchController.js` never defines
 *   `$scope.cancel`) -- an accepted standing improvement.
 */
@Component({
  selector: 'app-bank-branch',
  imports: [FormsModule, StatusToggle],
  templateUrl: './bank-branch.html',
  styleUrl: './bank-branch.scss',
})
export class BankBranch {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(BankBranchService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.bankBranchMasterObj) ----
  form = signal<BankBranchForm>(emptyBankBranchForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initBankBranchMaster/getPatientCategoryMasterList) ----
  list = signal<BankBranchRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side-only filter, mirroring `bankBranchList|filter:searchKeyword`
  // (view:75,128) -- a genuine, working filter, unlike employee-category's
  // completely dead search box.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadListAndCount();
  }

  // ctrl:78-134 (initBankBranchMaster) -- fetches list + count together;
  // count's success handler resets to page 1.
  private loadListAndCount(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
          this.currentPage.set(1);
        }
      },
    });
  }

  // ctrl:136-165 (getPatientCategoryMasterList, copy-paste-residue name) --
  // list-only fetch, used by pagination clicks. See class doc comment.
  private loadListOnly(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
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

  updateField<K extends keyof BankBranchForm>(field: K, value: BankBranchForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors bankBranchForm.$valid via the 2
  // `required` inputs -- Code/Description, view:17/32) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.branchCode.trim() && f.branchDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().branchCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().branchDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveBankBranch/PopupOkBtn, ctrl:197-263) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:200 guard (no else branch)
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
        branchDesc: f.branchDesc,
        branchCode: f.branchCode,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        orgnisationId: this.organizationId,
        unitId: this.unitId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadListAndCount(); // ctrl:233 -- reload #1, inside the success branch
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:252-257 -- reload #2, UNCONDITIONALLY after the if/else,
          // so a successful save reloads twice. See class doc comment.
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyBankBranchForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:266-291) ----
  edit(branchId: number): void {
    this.saveMode.set(false); // ctrl:268-269, set synchronously BEFORE the fetch resolves
    this.api.getById(branchId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          this.notify.error('Error!'); // ctrl:286-288 -- a native `alert()` in legacy, NOT growl; kept as
          // a notify.error() here rather than a real `alert()` popup, matching this
          // codebase's established convention.
          return;
        }
        const d = res.listObject[0];
        this.form.set({ branchId: d.branchId, branchCode: d.branchCode, branchDesc: d.branchDesc });
      },
    });
  }

  // ---- update (mirrors updateBankBranch, ctrl:334-385 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:339 guard (no else branch)
    const f = this.form();
    this.api
      .update({
        branchDesc: f.branchDesc,
        branchCode: f.branchCode,
        branchId: f.branchId as number,
        orgnisationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          // ctrl:375-380 -- reload runs UNCONDITIONALLY after both branches
          // (only once here, unlike save()'s double reload -- see class doc comment).
          this.loadListAndCount();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:294-331) ----
  toggleStatus(row: BankBranchRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.branchId === row.branchId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        branchId: row.branchId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:314 -- success calls $scope.init(), which resets the
            // create/update form -- a real, previously-uncatalogued bug
            // shared with employee-category's sibling screen. See class doc
            // comment. NOT a list reload.
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:207-209/295-297/335-337. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
