import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { SubSpecialityMasterService } from './sub-speciality-master.service';
import {
  DepartmentOption,
  GeneralLedgerOption,
  SubSpecialityForm,
  SubSpecialityRow,
  emptySubSpecialityForm,
} from './sub-speciality-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/sub-department-Master.html +
 * resources/assets/js/controller/master/subSpecialityMasterController.js
 * (`subSpecialityMasterController`, no `controllerAs`). State
 * `subSpecialityMaster` (app.js:3231-3252), moduleId 373, confirmed live
 * (not commented out). Org-scoped Global Master (not unit-scoped) --
 * `organizationId` off `cookieObject`, matching legacy's own
 * `$scope.organizationId` usage throughout.
 *
 * Confirmed real, working CRUD during Step 0/1 audit: list+count
 * (ctrl:120-169), save (ctrl:241-347), edit-populate `showUpdateBtn`
 * (ctrl:352-387), update (ctrl:433-494), status toggle `updateStatus`
 * (ctrl:390-430), a live general-ledger typeahead (ctrl:495-516), and a
 * CONFIRMED LIVE (not dead) server-side universal search (ctrl:517-543) --
 * all wired to real endpoints, see sub-speciality-master.service.ts.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - **Asymmetric validation between create and update.** `save()`
 *   (ctrl:241-284) runs FOUR sequential manual JS checks (Department, Code,
 *   Name, General Ledger -- each an early `return false` with its own growl)
 *   BEFORE ever reaching the `$scope.subSpecialityform.$valid` gate;
 *   `update()` (ctrl:433-494) has none of these manual checks at all and
 *   relies solely on the 3 HTML5 `required` attributes (Department/Code/
 *   Name -- General Ledger is not HTML5-required either way). Reproduced
 *   exactly: `save()` calls `runManualValidation()` then
 *   `requiredFieldsValid()`; `update()` calls only `requiredFieldsValid()`.
 * - **Faithful copy-paste bug, NOT fixed.** The Sub Department Name
 *   required-check inside `runManualValidation()` (ctrl:266-273) growls
 *   `'Please Enter Code'` -- the exact same message as the Code field's own
 *   check immediately above it (ctrl:257-264) -- instead of a
 *   name-specific message. Reproduced verbatim, not corrected.
 * - **Dual validation layers, consistent (not contradictory) here.** The 3
 *   HTML5-`required` fields (Department/Code/Name) are ALSO re-checked by
 *   the manual block above; both layers must pass, and since the manual
 *   block already guarantees those 3 are non-empty by the time
 *   `requiredFieldsValid()` runs, the second gate is redundant-but-harmless
 *   in `save()` -- reproduced as two separate checks anyway, matching
 *   legacy's own two independent gates rather than collapsing them into one.
 * - Both `save()` (ctrl:303) and `update()` (ctrl:456) only ever check
 *   `$valid` with no `else` branch -- an invalid submit beyond that point is
 *   a silent no-op (no growl, no modal). Reproduced via early `return` in
 *   both.
 * - The confirm modal (shared `<modal>` directive, `PopupOkBtn`/`popUpFlag`)
 *   only gates CREATE (ctrl:306-308); `update()` saves immediately with no
 *   confirm step -- same asymmetry as this codebase's amenity-master/
 *   ward-master/degree-master.
 * - **List reload runs unconditionally after BOTH branches**, unlike
 *   amenity-master (where reload is success-only): `initSubSpecialityMasterList`
 *   is called after the if/else in both `save()` (ctrl:340-345) and
 *   `update()` (ctrl:487-492), regardless of success or error. Only the
 *   SUCCESS branch also resets the form (`$scope.init()`, ctrl:329 /
 *   `$broadcast('show-errors-reset')`, ctrl:328,472). Reproduced via
 *   `resetForm()` inside the success branch only, then an unconditional
 *   `loadListAndCount()` call after the branch.
 * - `showUpdateBtn` (ctrl:352-387) flips into edit mode (`saveMode.set(false)`)
 *   synchronously, BEFORE the `GETSINGLESUBSPECIALITY` call resolves, and
 *   uses a native `alert("Error!!")` (ctrl:381-384) on a failed response --
 *   NOT `growl.error` like every other failure path in this controller.
 *   Both reproduced verbatim (`saveMode.set(false)` up front; a real
 *   `alert(...)` call, not `notify.error(...)`, on failure).
 * - The edit pencil icon is visually disabled (`ng-class="{disabled:
 *   info.status=='I'}"`, view ~232) when a row's status is Inactive --
 *   reproduced via `[class.disabled]` plus a guarded click, same pattern as
 *   this codebase's amenity-master.
 * - `updateStatus` (ctrl:390-430) growls only -- it never calls `init()` or
 *   `initSubSpecialityMasterList()` on success, and never reverts the
 *   already-flipped checkbox on failure (the view's `ng-model="info.status"`
 *   is two-way bound, so the visual toggle happens immediately regardless of
 *   the response). Reproduced by optimistically patching the `list` signal
 *   before the call resolves, with no reload on either branch.
 * - **The Next/Previous/page-number pager quirk.** `setPage(page, true)`
 *   (view pagination links) always calls `getSubSpecialityMasterList`
 *   (ctrl:172-202) -- the plain offset-based list fetch -- REGARDLESS of
 *   whether the currently displayed rows came from a search
 *   (`serachSubDepartmentMaster`, ctrl:517-543) or the normal list. Paging
 *   while viewing search results silently drops back to the unfiltered
 *   list. Reproduced as-is: `goToPage()`/`loadPage()` below always call the
 *   plain list endpoint, never the search endpoint, matching this exact
 *   quirk rather than "fixing" it to keep paginating search results.
 * - **The empty-list message is blank by default, not a static "No Record
 *   Found."** Legacy's `{{loadingMsg}}` (view:236) is only ever assigned
 *   inside `serachSubDepartmentMaster` (`"Loading..."` / `"No record
 *   found..."`, ctrl:519,537) -- on a normal (non-search) empty list it
 *   stays `undefined`/blank, unlike this codebase's amenity-master/
 *   qms-department-master, which both show a static "No Record Found"
 *   row unconditionally. Reproduced via a `loadingMessage` signal that
 *   defaults to `''` and is only set by `onSearchKeyup()`.
 * - **The client-side `filter:searchKeyword` still runs on top of the
 *   server-side search.** The view's `ng-repeat` (`subSpecialityList
 *   |filter:searchKeyword`) keeps its Angular `filter` even though
 *   `searchKeyword`'s `ng-keyup` also fires a real server call
 *   (`serachSubDepartmentMaster`) that already re-populates the list by the
 *   same term -- both layers are reproduced (`displayedRows` re-filters
 *   `list()` client-side by the same `searchKeyword` signal the server call
 *   uses).
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's x close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (`subSpecialityMasterController.js` never
 *   defines `$scope.cancel`) -- a working close is an accepted standing
 *   improvement, not a parity gap.
 * - No real typeahead/autocomplete component exists yet in
 *   `new/src/app/shared/` (checked before building this screen) -- the
 *   General Ledger field (`uib-typeahead` in legacy, ctrl:495-516) is
 *   reproduced as a plain text input plus a simple suggestion dropdown
 *   (`ledgerSuggestions`), not a drop-in typeahead widget. `clearText()`
 *   (ctrl:233-239, clears the text on blur if no ledger id is selected) is
 *   reproduced via `onLedgerBlur()`.
 */
@Component({
  selector: 'app-sub-speciality-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './sub-speciality-master.html',
  styleUrl: './sub-speciality-master.scss',
})
export class SubSpecialityMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(SubSpecialityMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- parent-department dropdown (mirrors $scope.specialityList) ----
  departmentOptions = signal<DepartmentOption[]>([]);

  // ---- general-ledger typeahead suggestions (mirrors ledgerNameSearch) ----
  ledgerSuggestions = signal<GeneralLedgerOption[]>([]);

  // ---- form state (mirrors $scope.subSpeciality) ----
  form = signal<SubSpecialityForm>(emptySubSpecialityForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initSubSpecialityMasterList/getSubSpecialityMasterList) ----
  list = signal<SubSpecialityRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');
  /** Mirrors `{{loadingMsg}}` (view:236) -- blank by default, see class doc comment. */
  loadingMessage = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: the client-side `filter:searchKeyword` still
  // runs on top of whatever `list()` currently holds (search results or the
  // normal page) -- see class doc comment.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadDepartments();
    this.loadListAndCount();
  }

  // ctrl:59-75.
  private loadDepartments(): void {
    this.api.getDepartments(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.departmentOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initSubSpecialityMasterList (ctrl:120-169): fetches list + count
  // together and resets to page 1 once the count responds.
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getList(this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors getSubSpecialityMasterList (ctrl:172-202) via setPage(page,true)
  // -- always the plain offset-based list fetch, even when currently
  // displaying search results (see class doc comment pager quirk).
  private loadPage(page: number): void {
    this.loader.start();
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
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

  updateField<K extends keyof SubSpecialityForm>(field: K, value: SubSpecialityForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- general ledger typeahead (mirrors ledgerNameSearch/SelectLedgerName/clearText) ----
  onLedgerInput(keyword: string): void {
    this.updateField('generalLedgerName', keyword);
    this.updateField('generalLedgerId', undefined); // ctrl:497 -- resets the id on every keystroke
    if (!keyword.trim()) {
      this.ledgerSuggestions.set([]);
      return;
    }
    this.api.searchLedgers(this.organizationId, keyword).subscribe({
      next: (res) => {
        if (res.status === 'success') this.ledgerSuggestions.set(res.listObject ?? []);
      },
    });
  }

  selectLedger(option: GeneralLedgerOption): void {
    this.updateField('generalLedgerId', option.generalLedgerId);
    this.updateField('generalLedgerName', option.generalLedgerName);
    this.ledgerSuggestions.set([]);
  }

  // ctrl:233-239 -- clears the typed text on blur if no ledger was actually
  // selected. Delayed slightly so a click on a suggestion row still
  // registers before the list is cleared.
  onLedgerBlur(): void {
    setTimeout(() => {
      if (!this.form().generalLedgerId) {
        this.updateField('generalLedgerName', '');
      }
      this.ledgerSuggestions.set([]);
    }, 200);
  }

  // ---- search (mirrors serachSubDepartmentMaster, ctrl:517-543 -- CONFIRMED LIVE) ----
  onSearchKeyup(keyword: string): void {
    this.searchKeyword.set(keyword);
    if (keyword.trim() !== '') {
      this.list.set([]);
      this.loadingMessage.set('Loading...');
      this.api.universalSearch(this.organizationId, keyword).subscribe({
        next: (res) => {
          const rows = res.listObject ?? [];
          this.list.set(rows);
          this.totalCount.set(rows.length);
          this.currentPage.set(1);
          this.loadingMessage.set(rows.length === 0 ? 'No record found...' : '');
        },
      });
    } else {
      this.loadingMessage.set('');
      this.loadListAndCount();
    }
  }

  // ---- required-field validation ----
  // Mirrors the 3 HTML5 `required` fields (Department/Code/Name,
  // view:17,35,50) that back `$scope.subSpecialityform.$valid`.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.specialityId && f.subSpecialityCode.trim() && f.subSpecialityName.trim());
  }

  // Mirrors ctrl:249-284's 4 sequential manual checks -- Department, Code,
  // Name (SAME growl message as Code, faithful copy-paste bug -- see class
  // doc comment), General Ledger. `save()`-only; `update()` never calls this.
  private runManualValidation(): boolean {
    const f = this.form();
    if (!f.specialityId) {
      this.notify.error('Please Select Department');
      return false;
    }
    if (!f.subSpecialityCode.trim()) {
      this.notify.error('Please Enter Code');
      return false;
    }
    if (!f.subSpecialityName.trim()) {
      // ctrl:266-273 -- faithful copy-paste bug: same message as Code's
      // check above, not a name-specific one. NOT corrected here.
      this.notify.error('Please Enter Code');
      return false;
    }
    if (!f.generalLedgerName.trim()) {
      this.notify.error('Please Select General Ledger Code');
      return false;
    }
    return true;
  }

  isDeptInvalid(): boolean {
    return this.formSubmitted() && !this.form().specialityId;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().subSpecialityCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().subSpecialityName.trim();
  }

  isLedgerInvalid(): boolean {
    return this.formSubmitted() && !this.form().generalLedgerName.trim();
  }

  // ---- save / confirm-modal (mirrors saveSubSpeciality/PopupOkBtn, ctrl:241-347) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.runManualValidation()) return; // ctrl:249-284, save()-only -- see class doc comment
    if (!this.requiredFieldsValid()) return; // mirrors the `$valid` gate, ctrl:303
    this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
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
        specialityId: f.specialityId,
        subSpecialityMasterCode: f.subSpecialityCode,
        subSpecialityMasterName: f.subSpecialityName,
        generalLedgerId: f.generalLedgerId,
        status: 'A',
        isModality: f.isModality,
        isMriModality: f.isMriModality,
        isCTModality: f.isCTModality,
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        subHeaderName: f.subHeaderName,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Sub department added successfully...!!'); // ctrl:321-326, literal text
            this.resetForm();
          } else {
            this.notify.error('Sub department already exits...!!'); // ctrl:333-338, legacy's own typo preserved
          }
          // ctrl:340-345 -- runs UNCONDITIONALLY after both branches. See class doc comment.
          this.loadListAndCount();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptySubSpecialityForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.ledgerSuggestions.set([]);
    this.loadDepartments(); // mirrors $scope.init() reloading specialityList too
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:352-387) ----
  edit(row: SubSpecialityRow): void {
    this.saveMode.set(false); // ctrl:354-355, set synchronously BEFORE the fetch resolves
    this.api.getById(row.subSpecialityMasterId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          alert('Error!!'); // ctrl:381-384 -- a native alert, NOT growl.error, unlike every other failure path here
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          subSpecialityId: d.subSpecialityMasterId,
          specialityId: String(d.specialityId),
          subSpecialityCode: d.subSpecialityMasterCode,
          subSpecialityName: d.subSpecialityMasterName,
          generalLedgerId: d.generalLedgerId,
          generalLedgerName: d.generalLedgerName,
          subHeaderName: d.subHeaderName ?? '',
          isModality: d.isModality,
          isMriModality: d.isMriModality,
          isCTModality: d.isCTModality,
        });
      },
    });
  }

  // ---- update (mirrors updateSubSpeciality, ctrl:433-494 -- no confirm modal, no manual validation) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // ctrl:456 -- ONLY gate on update; no manual checks, see class doc comment
    const f = this.form();
    this.api
      .update({
        subSpecialityMasterId: f.subSpecialityId as number,
        specialityId: f.specialityId,
        subSpecialityMasterCode: f.subSpecialityCode,
        subSpecialityMasterName: f.subSpecialityName,
        organizationId: this.organizationId,
        isModality: f.isModality,
        isCTModality: f.isCTModality,
        isMriModality: f.isMriModality,
        generalLedgerId: f.generalLedgerId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        subHeaderName: f.subHeaderName,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Sub department updated successfully...!!'); // ctrl:465-471, literal text
            this.resetForm();
          } else {
            this.notify.error('Sub department already exits...!!'); // ctrl:479-484, same text reused from save()'s error
          }
          // ctrl:487-492 -- runs UNCONDITIONALLY after both branches, same as save().
          this.loadListAndCount();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:390-430) ----
  toggleStatus(row: SubSpecialityRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // ctrl:390-430 -- growl only, no init()/initSubSpecialityMasterList()
    // call on success, and no revert on failure. See class doc comment.
    this.list.update((rows) =>
      rows.map((r) => (r.subSpecialityMasterId === row.subSpecialityMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        subSpecialityMasterId: row.subSpecialityMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Sub department Updated successfully...!!'); // ctrl:413-418, literal text (capital "Updated")
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:420-426, literal text
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:244-246/392-394/436-437. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
