import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ReasonMasterService } from './reason-master.service';
import { ActiveStatus, ReasonForm, ReasonRow, ReasonTypeOption, emptyReasonForm } from './reason-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/reason-master.html +
 * resources/assets/js/controller/master/global/common/reasonMasterController.js
 * (`reasonMasterController`, no `controllerAs`). State `reasonMaster`
 * (app.js:14229-14251), moduleId 495, confirmed live (resolve block checks
 * `permissions.modules` for id 495, no `requireSuperUser` anywhere in this
 * state or anywhere in app.js's grep for that term). Org-scoped Global
 * Master (not unit-scoped) -- `organizationId` off `cookieObject`. Its own
 * `<script>` include IS live (index.html:1575), unlike its sibling
 * `reasonTypeMasterController.js` (index.html:1571, commented out -- see
 * reason-type-master.ts's class doc comment for that severity-1 finding).
 *
 * Confirmed real, working CRUD during Step 0/1 audit: list+count
 * (ctrl:99-152), save (ctrl:213-273), edit-populate `showUpdateBtn`
 * (ctrl:276-303), update (ctrl:349-400), status toggle `updateStatus`
 * (ctrl:306-346), and a live client-side search filter (view:90,153) --
 * all wired to real endpoints, backed by a real, fully-wired
 * Controller -> Service -> DAO chain (`ReasonMasterController.java` ->
 * `ReasonMasterServiceImpl.java` -> `ReasonMasterDaoImpl.java`, see
 * reason-master.service.ts).
 *
 * **Deliberate deviations/preserved quirks, not bugs fixed here:**
 * - **A real, confirmed "stuck loader" bug in `showUpdateBtn`
 *   (ctrl:276-303), NOT shared with any other action in this controller.**
 *   `$rootScope.startLoader()` fires before the `GETREASONMASTERBYID` call;
 *   on a SUCCESSFUL response, `$rootScope.stopLoader()` is called
 *   (ctrl:297) -- but on a failed/empty response, the code only does
 *   `alert("Error!!")` (ctrl:299) and NEVER calls `stopLoader()` at all.
 *   Every other loader-wrapped action in this controller (save, update,
 *   status-toggle) correctly stops the loader in BOTH branches; only this
 *   one path can leave the global loading overlay stuck indefinitely.
 *   Reproduced faithfully below: `loader.stop()` is called only inside the
 *   success branch of `edit()`, matching the asymmetry exactly.
 * - The confirm modal (shared `<modal>` directive, `PopupOkBtn`/`popUpFlag`)
 *   only gates CREATE (ctrl:230-234); `updateReasonMaster()` saves
 *   immediately with no confirm step. Same asymmetry as this codebase's
 *   sub-speciality-master/amenity-master/ward-master/degree-master.
 * - **Shared, non-divergent quirk with sibling `reasonTypeMaster`:**
 *   `$scope.popUpFlag` starts `true`, is permanently disarmed by
 *   `PopupOkBtn`, and is only re-armed by `$scope.init()` -- called again
 *   after a SUCCESSFUL save, but not after a failed one (e.g. a
 *   duplicate-name rejection). So the very next Save click after a failed
 *   save skips the confirm dialog and saves immediately. Reproduced via
 *   `confirmArmed` below.
 * - **Also shared with `reasonTypeMaster`:** a successful status-toggle
 *   calls the FULL `$scope.init()` (ctrl:328), which reloads the list AND
 *   resets the create/edit form to blank -- toggling one row's status
 *   while mid-edit of a different row silently discards the in-progress
 *   edit. Reproduced via `toggleStatus()` calling `resetForm()` on success.
 * - **A dead extra search handler, harmless because the real search
 *   already works independently.** The search `<input>`
 *   (view:90) has BOTH a working `ng-model="searchKeyword"` (feeding the
 *   view's own `ng-repeat="...|filter:searchKeyword"`, view:153 -- genuine,
 *   live client-side search) AND an `ng-keyup="serachTestmaster(searchKeyword)"`
 *   handler calling a function that does not exist anywhere in this
 *   controller (grepped). The typo'd name matches a known dead-search-
 *   handler family elsewhere in this codebase (entry 71's
 *   `mealPreferenceMaster`), but here the underlying `filter:` already
 *   does the real, live work regardless -- the missing function only
 *   throws to Angular's own exception handler (console-logged, no visible
 *   effect) on every keystroke. Reproduced by never implementing
 *   `serachTestmaster` -- the search box below relies purely on the
 *   client-side `filter`-equivalent (`displayedRows`), same as legacy's
 *   real, working mechanism.
 * - **`reasonTypeId` kept as a string on the form**, matching legacy's own
 *   `.toString()` on edit (ctrl:296) and the view's raw
 *   `value="{{info.reasonTypeId}}"` `<option>` binding.
 * - **Inherits reasonTypeMaster's real backend bug via the shared "Reason
 *   Type" dropdown.** `init()` (ctrl:70-87) populates this screen's
 *   dropdown by calling the exact same `GETREASONTYPEMASTERLIST` endpoint
 *   reasonTypeMaster's own list uses -- and that endpoint's real backend
 *   (`ReasonTypeMasterDaoImpl.getReasonTypeMasterList()`) has no
 *   organization filter at all (see reason-type-master.ts's class doc
 *   comment for the full citation). This dropdown will list every
 *   organization's reason types, not just this org's -- not a bug in this
 *   screen's own code, but a real, inherited one. Reproduced as-is; no
 *   client-side filter added to mask it.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), this
 *   screen also uses the shared `<modal>` directive (view:199) whose ×
 *   close button is hardcoded to call `cancel()` -- a function this
 *   controller never defines either (grepped, zero matches). The confirm
 *   modal below uses an ordinary working `closeConfirmModal()`, matching
 *   the already-adopted standing policy, not a fresh write-up.
 * - A dead `<input type="hidden" ng-model="reasonId" value="0">` (view:16)
 *   -- `reasonId` is never read anywhere in the controller (the real ID
 *   field used throughout is `reasonMasterObj.reasonMasterId`). Not
 *   reproduced -- there is nothing for it to do.
 */
@Component({
  selector: 'app-reason-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './reason-master.html',
  styleUrl: './reason-master.scss',
})
export class ReasonMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ReasonMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- "Reason Type" dropdown (mirrors $scope.reasonTypeList) ----
  reasonTypeOptions = signal<ReasonTypeOption[]>([]);

  // ---- form state (mirrors $scope.reasonMasterObj) ----
  form = signal<ReasonForm>(emptyReasonForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. */
  private confirmArmed = true;

  // ---- list + server-side pagination (mirrors initReasonlist/getReasonList) ----
  list = signal<ReasonRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  /** Mirrors the view's own `ng-model="searchKeyword"` +
   * `|filter:searchKeyword` -- genuine, live client-side search over
   * whatever page is currently loaded (see class doc comment). */
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadReasonTypeOptions();
    this.loadListAndCount();
  }

  // ctrl:70-87.
  private loadReasonTypeOptions(): void {
    this.api.getReasonTypeOptions(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.reasonTypeOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initReasonlist (ctrl:99-152): fetches list + count together and
  // resets to page 1 once the count responds. NO loader calls here,
  // matching legacy exactly (list/count are not loader-wrapped).
  private loadListAndCount(): void {
    this.api.getList(this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors getReasonList (ctrl:154-181) via setPage(page,true).
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
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
    this.loadPage(page);
  }

  updateField<K extends keyof ReasonForm>(field: K, value: ReasonForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors the 3 HTML5 `required`
  // attributes backing `$scope.reasionMasterform.$valid`, view:17,34,48) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.reasonTypeId && f.reasonCode.trim() && f.reasonDescription.trim());
  }

  isReasonTypeInvalid(): boolean {
    return this.formSubmitted() && !this.form().reasonTypeId;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().reasonCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().reasonDescription.trim();
  }

  // ---- save / confirm-modal (mirrors saveReasonMaster/PopupOkBtn, ctrl:207-273) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the $valid gate, ctrl:230
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:207-210): disarms the confirm gate, then saves.
  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    this.loader.start(); // ctrl:237
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        reasonCode: f.reasonCode,
        reasonDesc: f.reasonDescription,
        reasonTypeId: f.reasonTypeId,
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
            this.resetForm(); // ctrl:245 -- $scope.init() -- re-arms confirmArmed, resets formSubmitted (show-errors-reset)
          } else {
            this.notify.error(res.message ?? 'Error!'); // confirmArmed stays disarmed
          }
          // ctrl:263-267 -- reloads the list unconditionally after both branches.
          this.loadListAndCount();
          this.loader.stop(); // ctrl:268 -- always stopped here, both branches
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyReasonForm());
    this.saveMode.set(true);
    this.confirmArmed = true; // ctrl's $scope.init() resets popUpFlag = true
    this.formSubmitted.set(false); // ctrl:252 -- $broadcast('show-errors-reset'), success-only
    this.loadReasonTypeOptions(); // $scope.init() reloads reasonTypeList too
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:276-303) ----
  edit(row: ReasonRow): void {
    this.saveMode.set(false); // ctrl:278-279, set synchronously BEFORE the fetch resolves
    this.loader.start(); // ctrl:284
    this.api.getById(row.reasonMasterId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          alert('Error!!'); // ctrl:299 -- a native alert, NOT growl.error
          // ctrl:299-300 -- deliberately NOT calling loader.stop() here: a
          // real, confirmed "stuck loader" bug, see class doc comment.
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          reasonMasterId: d.reasonMasterId,
          reasonTypeId: String(d.reasonTypeId),
          reasonCode: d.reasonCode,
          reasonDescription: d.reasonDesc,
        });
        this.loader.stop(); // ctrl:297 -- success-only, see class doc comment
      },
    });
  }

  // ---- update (mirrors updateReasonMaster, ctrl:349-400 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // ctrl:364 -- ONLY gate on update
    this.loader.start(); // ctrl:365
    const f = this.form();
    this.api
      .update({
        reasonMasterId: f.reasonMasterId as number,
        reasonCode: f.reasonCode,
        reasonDesc: f.reasonDescription,
        reasonTypeId: f.reasonTypeId,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loader.stop(); // ctrl:382 -- success branch
          } else {
            this.notify.error(res.message ?? 'Error!');
            this.loader.stop(); // ctrl:390 -- error branch too, symmetric here
          }
          // ctrl:392-396 -- reloads the list unconditionally after both branches.
          this.loadListAndCount();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:306-346) ----
  toggleStatus(row: ReasonRow): void {
    const nextStatus: ActiveStatus = row.status === 'A' ? 'I' : 'A';
    this.loader.start(); // ctrl:319
    this.list.update((rows) => rows.map((r) => (r.reasonMasterId === row.reasonMasterId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        reasonMasterId: row.reasonMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:328 -- a successful toggle calls the FULL $scope.init(),
            // which reloads the list AND resets the create/edit form to
            // blank -- see class doc comment (shared, non-divergent quirk).
            this.resetForm();
            this.loadListAndCount();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.loader.stop(); // ctrl:335/343 -- both branches, symmetric here
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:214-216/307-309/350-352. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
