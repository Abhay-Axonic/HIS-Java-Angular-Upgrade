import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { GradeCreationMasterService } from './grade-creation-master.service';
import { GradeForm, GradeRow, emptyGradeForm } from './grade-creation-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/grade-creation-master.html +
 * resources/assets/js/controller/billing/unit/gradeCreationMasterController.js
 * (`gradeCreationMasterController`, no `controllerAs`). State
 * `gradeCreationMaster` self-registered at app.js:7999-8021 (`templateUrl:
 * 'views/master/GlobalMaster/grade-creation-master.html'`, `controller:
 * "gradeCreationMasterController"`), moduleId 575 (resolve block,
 * app.js:8008-8016, confirmed live). Script-loaded at index.html:1626, not
 * orphaned. Unit + org-scoped like this codebase's bank-master
 * (`orgUnitId`/`orgId` off `cookieObject`), despite living under
 * `views/master/GlobalMaster/` alongside plain global masters -- its own
 * controller physically lives in
 * `resources/assets/js/controller/billing/unit/`, and every endpoint is
 * under `/api/billing/*`, same Billing-tagged-but-Global-Master-folder
 * pattern already flagged for `bankMaster`.
 *
 * Backend confirmed real and live (not the entry-67/77 dead-backend
 * pattern): navigated the running dev Tomcat instance directly and captured
 * real 200 responses for `/api/billing/getContractGradeMasterList` and
 * `/api/billing/getContractGradeMasterCount`, backed by
 * `legacy/Billing/src/main/java/com/param/service/controller/ContractGradeMasterController.java`
 * (no seeded rows exist in this dev environment, but the endpoints
 * themselves are real).
 *
 * Confirmed real, working CRUD: save (ctrl:145-201), update (ctrl:234-280),
 * edit-populate `showUpdateBtn` (ctrl:203-231), status toggle `updateStatus`
 * (ctrl:282-317) -- all wired to real endpoints, see
 * grade-creation-master.service.ts.
 *
 * **Deliberate quirks/bugs preserved, not fixed:**
 * - **A real, previously-uncataloged stuck-loader bug on invalid Update
 *   submit, found while auditing this screen, faithfully reproduced.**
 *   `update()` (ctrl:234-280) calls `$rootScope.startLoader()`
 *   unconditionally at ctrl:253 -- BEFORE the `if
 *   ($scope.gradeCreationMasterform.$valid)` guard at ctrl:254. If Code/Name
 *   are empty, the guard short-circuits and the network call (whose success/
 *   error branches are the ONLY places that call `stopLoader()`) never
 *   fires -- the global loading overlay is left showing indefinitely, in
 *   legacy today, until some unrelated later action elsewhere in the app
 *   happens to call `stopLoader()` itself. Reproduced verbatim: `loader.start()`
 *   is called before `requiredFieldsValid()`'s early return in `update()`
 *   below, not after. Worth logging to docs/ANGULAR_PENDING_FINDINGS.md.
 * - By contrast, `save()` (ctrl:145-201) only starts the loader once
 *   `popUpFlag` is already `false` (i.e. the real `PopupOkBtn`-triggered
 *   save, ctrl:148), which is only reachable after the same `$valid` check
 *   already passed once to open the confirm modal -- no equivalent stuck
 *   state in practice on the create path. Modeled by calling `loader.start()`
 *   inside `confirmSave()` only, immediately before the real POST.
 * - The confirm modal (shared `<modal>` directive, grade-creation-master.html:136,
 *   wired via legacy's `PopupOkBtn`/`popUpFlag`) only gates CREATE
 *   (ctrl:167-170); `update()` saves immediately with no confirm step --
 *   same asymmetry as this codebase's amenity-master/bank-master.
 * - **List reload runs ONLY on the success branch**, in both `save()`
 *   (ctrl:177-186) and `update()` (ctrl:257-266) -- an error response growls
 *   and stops, nothing else. Reproduced via `loadListAndCount()` called
 *   strictly inside each success branch.
 * - `save()`/`update()`/`showUpdateBtn()`/`updateStatus()` all use the
 *   server's own dynamic `response.data.message` for both growl.success and
 *   growl.error (ctrl:180,190,220,260,270,300,308) -- NOT the hardcoded
 *   copy-paste message bug found in this same batch's `prefixMaster`.
 *   Reproduced via `res.message ?? '...'`, never a hardcoded string.
 * - `showUpdateBtn` (ctrl:203-231) flips into update mode (`saveBtnFlag =
 *   false`, `updateBtnFlag = true`) synchronously BEFORE the by-id fetch
 *   resolves, wraps the fetch in `startLoader()`/`stopLoader()` (both
 *   branches correctly balanced, unlike `update()`'s bug above), and uses
 *   `growl.error(response.data.message)` on failure -- a real dynamic
 *   message, not a native `alert()` like this same batch's `prefixMaster`.
 * - The edit pencil (grade-creation-master.html:104-108,
 *   `ng-class="{disabled: info.status=='I'}"`) IS visually disabled/guarded
 *   for Inactive rows here -- same pattern as this codebase's amenity-master,
 *   and a genuine divergence from this same batch's sibling `prefixMaster`,
 *   whose edit icon has no such guard at all. Reproduced by gating `edit()`
 *   on `row.status === 'A'`.
 * - `updateStatus` (ctrl:282-317) growls the server's own message on both
 *   branches, never calls `initMaster()`/`initMasterList()` on success, and
 *   never reverts the already-flipped checkbox on failure (the view's
 *   `ng-model="info.status"` is two-way bound, so the visual toggle happens
 *   immediately regardless of the response). Reproduced by optimistically
 *   patching the `list` signal before the call resolves, with no reload on
 *   either branch.
 * - **Legacy's local try/catch-swallow pattern is NOT reproduced here --
 *   there is no direct Angular/RxJS analog.** `save()`, `update()`,
 *   `showUpdateBtn()`, and `updateStatus()` each wrap their entire body in
 *   `try{...}catch(e){console.log("Exception-",e);}` with no rethrow and no
 *   route to any centralized handler -- the same billing-adjacent
 *   error-swallowing pattern this project's CLAUDE.md calls its #1 problem
 *   to fix, in a screen physically under `controller/billing/`. RxJS's
 *   `subscribe({next})` here already doesn't swallow synchronous errors the
 *   way a bare try/catch-log does, so there's no equivalent gap to
 *   intentionally leave open. Noted for the separate billing/error-handling
 *   workstream; not actionable from this Angular-only rebuild.
 * - **No missing-`$state`-DI crash class bug** (docs/ANGULAR_PENDING_FINDINGS.md
 *   entries 8/10): `$state` IS in this controller's DI list (ctrl:7-8) and
 *   IS used correctly in the session guard (ctrl:24-28) -- confirmed clean.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (`gradeCreationMasterController.js` never
 *   defines `$scope.cancel`) -- a working close is the accepted standing
 *   improvement, not a parity gap.
 * - The search box (grade-creation-master.html:76, `ng-model="searchKeyword"`
 *   feeding `filter:searchKeyword` on the `ng-repeat`, view:93) is LIVE,
 *   client-side filtering -- reproduced as a genuinely working filter.
 */
@Component({
  selector: 'app-grade-creation-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './grade-creation-master.html',
  styleUrl: './grade-creation-master.scss',
})
export class GradeCreationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(GradeCreationMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<GradeForm>(emptyGradeForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initMasterList/masterList/PagerService) ----
  list = signal<GradeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ctrl:93 -- `filter:searchKeyword` client-side filter, live (see class doc comment).
  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((row) => [row.code, row.gradeDesc].some((v) => v?.toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadListAndCount();
  }

  // mirrors initMasterList (ctrl:57-96): fetches list + count together,
  // always from the current offset, and resets to page 1 once the count responds.
  private loadListAndCount(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
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
    this.loadListAndCount();
  }

  updateField<K extends keyof GradeForm>(field: K, value: GradeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors gradeCreationMasterform.$valid
  // via the 2 `required` inputs -- code/gradeDesc, view:18/34) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.code.trim() && f.gradeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().code.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().gradeDesc.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:145-201) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:166 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.loader.start(); // ctrl:148-151 -- started only once popUpFlag is already false, see class doc comment
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        code: f.code,
        gradeDesc: f.gradeDesc,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        status: 'A',
        unitId: this.unitId,
        orgId: this.organizationId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:180, real server message
            this.resetForm();
            this.loadListAndCount(); // ctrl:185-186 -- SUCCESS-branch-only reload, see class doc comment
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!'); // ctrl:190, real server message
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyGradeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:203-231) ----
  edit(row: GradeRow): void {
    if (row.status !== 'A') return; // grade-creation-master.html:105 -- disabled for Inactive rows
    this.saveMode.set(false); // ctrl:206-208, set synchronously BEFORE the fetch resolves
    this.loader.start(); // ctrl:209
    this.api.getById(row.contractGradeMasterId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.listObject?.length) {
          this.form.set(res.listObject[0]);
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!'); // ctrl:221-225, real server message (not a native alert)
        }
      },
    });
  }

  // ---- update (mirrors update(), ctrl:234-280 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    // ctrl:253 -- REAL BUG, faithfully reproduced (see class doc comment):
    // legacy starts the loader unconditionally, BEFORE checking $valid. If
    // required fields are empty, the guard below returns early and the
    // loader is left running forever (nothing else in this method ever
    // stops it).
    this.loader.start();
    if (!this.requiredFieldsValid()) return; // ctrl:254 guard -- loader stays stuck in this path
    const f = this.form();
    this.api
      .update({
        code: f.code,
        gradeDesc: f.gradeDesc,
        contractGradeMasterId: f.contractGradeMasterId as number,
        orgId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:260, real server message
            this.resetForm();
            this.loadListAndCount(); // ctrl:265-266 -- SUCCESS-branch-only reload, see class doc comment
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!'); // ctrl:270, real server message
          }
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:282-317) ----
  toggleStatus(row: GradeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start(); // ctrl:293
    // ctrl:282-317 -- growl only, no initMaster()/initMasterList() call on
    // success, and no revert on failure. See class doc comment.
    this.list.update((rows) =>
      rows.map((r) => (r.contractGradeMasterId === row.contractGradeMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        contractGradeMasterId: row.contractGradeMasterId,
        status: nextStatus,
        updatedDate: formatDateTime(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:300, real server message
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!'); // ctrl:308, real server message
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:152-153/238-239/284-285. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
