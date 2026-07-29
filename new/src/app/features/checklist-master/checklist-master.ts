import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ChecklistMasterService } from './checklist-master.service';
import {
  ChecklistItemDetail,
  ChecklistItemSuggestion,
  ChecklistMasterForm,
  ChecklistMasterRow,
  OtimsRole,
  STAGE_OPTIONS,
  emptyChecklistMasterForm,
} from './checklist-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/GlobalMaster/OTIMS/checklist-item-master.html`
 * (334 lines) + `resources/assets/js/controller/otims/global/
 * checklistMasterController.js` (361 lines, `checklistMasterController`, no
 * `controllerAs` -- classic `$scope.*` throughout, confirmed zero `vm.*` in
 * the view). State `checklistMaster`, **module ID 526, no `requireSuperUser`**,
 * confirmed directly from the live `.state(...)` resolve block
 * (app.js:12539-12566). Golden trace (2026-07-29) confirmed exactly 2
 * unchained POST calls on initial load -- `/api/global//getActiveOTRoleList`
 * and `/global/otims/getAllChecklistMasterList` -- matching `otimsRole()`
 * (ctrl:76-89) and `getChecklistMasterList()` (ctrl:93-116) both being
 * called back-to-back at init with no dependency between them. Reproduced
 * exactly by `loadRoleList()`/`loadList()` below, both fired unconditionally
 * from the constructor.
 *
 * An OT/Theatre "Checklist Master" screen: a Code/Description/Role/Stage/
 * Is-WHO header record with a NESTED, dynamically-built list of individual
 * checklist items (`checkListItemNameList`), submitted together as
 * `checklistMasterDetails` on Save. `saveChecklistMaster` is a genuine
 * single-endpoint UPSERT (`ChecklistMasterDaoImpl.saveChecklistMaster`:
 * `if(checklistMasterId!=null) update(...) else save(...)`), not
 * separate Create/Update endpoints -- the rebuild's `save()` mirrors this
 * with one method, keyed on `form().checklistMasterId`, same as legacy's
 * single floppy-disk Save icon (view:132-134) used for both.
 *
 * ================================================================
 * TOP-PRIORITY FINDING -- confirmed, real, silent WRITE-PATH data-integrity
 * bug in legacy's Add-Checklist-Item flow. FIXED here (not faithfully
 * reproduced), disclosed prominently. See the orchestrating session's own
 * report for the full writeup; summarized here for anyone reading this file
 * in isolation.
 * ================================================================
 * Legacy's "Add Checklist Item" search box (view:274-276) is a
 * `uib-typeahead` bound to `checklistDetailsObj.description` with
 * `typeahead-editable="false"`. Reading `ui-bootstrap-tpls-2.3.1.js:7162-
 * 7198` (the exact vendored version this app loads, confirmed via
 * `index.html:357`) shows that with `isEditable=false`, the directive's
 * `$parsers` pipeline resets the bound ng-model to `undefined` on EVERY
 * keystroke that isn't a confirmed selection from the dropdown -- the input
 * box still visually shows what was typed, but `checklistDetailsObj.description`
 * silently becomes `undefined` the moment the user types anything that
 * doesn't exactly match/select a suggestion.
 *
 * `checklistItemMasterId`, however, is NEVER reset by typing -- it is only
 * ever set by `selectCheckItem()` (ctrl:137-142, on an autocomplete pick) or
 * cleared by `addChecklistItemName()` itself AFTER a push (ctrl:162-163).
 * Net effect, fully reproducible from source alone: select an existing item
 * A (sets `checklistItemMasterId=X`), then change your mind and type a
 * DIFFERENT search string without selecting a new match -- `description`
 * silently becomes `undefined` but `checklistItemMasterId` STAYS `X` (stale).
 * Clicking "Add" (ctrl:144-164) still passes the `!= ""` guard (since
 * `undefined != ""` is `true` in JS) and pushes `{checklistItemMasterId: X,
 * description: undefined}` into the list -- with ZERO visible error.
 *
 * This is NOT merely a cosmetic blank-row bug: `ChecklistMasterDetailsDaoImpl
 * .saveChecklistMasterDetails` (legacy/OTIMS/.../dao/
 * ChecklistMasterDetailsDaoImpl.java:33-52) never persists `description` at
 * all -- the `ChecklistMasterDetails` entity has no such column (confirmed:
 * `ChecklistMasterDetails.java` model, no `description` field/setter
 * anywhere) -- only `checklistItemMasterId` (a real FK to
 * `otims.m_checklist_item_master`) is written, and the description shown on
 * every subsequent read is JOINed live from `checklistItemMaster.description`
 * via that FK. So the stale-ID entry above doesn't save as "blank" -- it
 * silently attaches checklist item A (X) -- a completely different item than
 * whatever the user most recently typed -- to this checklist master, and
 * every future read (including immediately after Save) will show item A's
 * real name, not a blank and not what was typed. Success growl, no error,
 * no warning, in current live legacy.
 *
 * There is no possible "fix" that lets legacy's free-typed text become a
 * genuinely NEW checklist item either way: the save path never inserts into
 * `checklist_item_master`, only ever references an existing row's id. New
 * checklist-ITEM names are created on the separate, dedicated
 * `checklistItemMaster` screen (`checklistItemMasterController.js` /
 * `checklist_Item.html`), not here -- this screen only composes EXISTING
 * items into a checklist. `typeahead-editable="false"` reads as legacy's
 * (badly-implemented) attempt to enforce exactly that constraint.
 *
 * Corrected behavior implemented below: `addItem()` requires an actual
 * confirmed selection (`selectedSuggestion()` non-null) before allowing an
 * Add -- `onItemSearchInput()` clears `selectedSuggestion` the instant the
 * text changes, so a stale id can never outlive the search text that
 * produced it. Attempting to Add without a valid selection shows a visible
 * validation message (`itemAddError`) instead of legacy's silent
 * corruption. This is a pure FRONTEND-controller bug (nothing server-side
 * needs to change, and nothing here alters what `saveChecklistMaster`
 * accepts) -- same class of fix as this project's `identificationMaster`/
 * `currencyMaster` precedent (fix directly, disclose prominently, flag for
 * the owner's sign-off on the classification), NOT the
 * hold-and-faithfully-reproduce treatment used for backend-side bugs like
 * `hospitalAccountMaster`'s default-account reset.
 *
 * ================================================================
 * Double-slash URLs -- investigated, both harmless, kept verbatim. Full
 * root-cause writeup in `checklist-master.service.ts`'s class doc comment.
 * ================================================================
 *
 * ================================================================
 * Other faithfully reproduced details
 * ================================================================
 * - `saveCheckListMaster()`'s field validation (ctrl:172-198) fires in a
 *   strict else-if chain -- code, then description, then role, then
 *   "checklist name" (i.e. at least one item in the nested list) -- only the
 *   FIRST failing check's growl.warning shows per attempt. Reproduced
 *   exactly by `validate()` below, still rendered as a VISIBLE inline
 *   message per this project's standing validation-visibility policy (not
 *   legacy's globally-hidden `.help-block` CSS) in addition to the
 *   equivalent toast.
 * - Duplicate check: `ChecklistMasterDaoImpl.saveChecklistMaster` (:32-90)
 *   runs a REAL, working, org+unit-scoped, self-excluding-on-update
 *   Code-then-Description uniqueness check and returns
 *   `status:"Already Exists."` (`ICommonConstants.ALREADY_EXIST`) with a
 *   specific message ("Code Already Exist"/"Description Already Exist") --
 *   confirmed the frontend's literal string check for
 *   `response.data.status == 'Already Exists.'` (ctrl:222) matches this
 *   constant's value byte-for-byte, so this branch is genuinely live, not
 *   dead code. Reproduced via the same 3-way status switch in `save()`.
 * - Status toggle (view:180-183, `updateStatus()` ctrl:235-270): optimistic
 *   `ng-model` flip BEFORE the request resolves, exactly like this batch's
 *   `hospitalAccountMaster` sibling, but UNLIKE that sibling this controller
 *   also triggers a full list reload on success (ctrl:251,
 *   `getChecklistMasterList($scope.organizationId,$scope.offset,...)`).
 *   Since `$scope.offset` is declared once (ctrl:32) and never reassigned
 *   anywhere else in the file (confirmed via grep), this reload is always
 *   for OFFSET 0 -- toggling any row's status on any page silently jumps
 *   the visible table back to page 1. Cosmetic/display-only (no data loss),
 *   faithfully reproduced via `loadList()`'s call in `toggleStatus()`'s
 *   success branch below.
 * - Pagination has two distinct fetch paths hitting the SAME endpoint:
 *   `getChecklistMasterList` (full reload -- list + count together in one
 *   response, `response.data.object` is the total count -- then resets to
 *   page 1) on init/Save-success/status-toggle-success/page-size-change,
 *   vs. `masterList` (ctrl:318-342, list-only, no count re-read) on
 *   pagination-button clicks. Reproduced as `loadList()` vs `loadPage()`.
 * - "Checklist Items" info icon (view:175-177) opens a READ-ONLY modal
 *   (`checklistNameList`, ctrl:274-294 label==1) fetched fresh via
 *   `getChecklistMasterDetailsByMasterId` every click -- separate from the
 *   EDITABLE nested list (`checkListItemNameList`) populated only via the
 *   edit-pencil (label==2, ctrl:296-316), which ALSO calls the same fetch
 *   and additionally overwrites the working item list. Reproduced as two
 *   separate signals (`viewItemsList` vs `checkListItemNameList`) fed by
 *   the same service call with different assignment targets, matching
 *   `getChecklistNameListById(info,label)`'s branch exactly.
 * - Both modal footer buttons ("Ok" on the read-only modal, view:251;
 *   "Save" on the Add-Checklist-Item modal, view:316) have NO `ng-click` in
 *   legacy at all -- purely a Bootstrap `data-dismiss="modal"` close, no
 *   actual save semantics (the real work happens via per-item "Add"
 *   clicks, which mutate `checkListItemNameList` immediately, and the
 *   top-level floppy-disk Save icon, which submits the whole form). Not a
 *   bug -- reproduced as plain "Close" actions below, matching legacy's
 *   real (if slightly confusingly-labeled) behavior.
 * - `checkListItemNameList` is NOT reset when the Add-Checklist-Item modal
 *   is opened/closed (no `ng-click` hook on modal open in legacy at all --
 *   it's a bare Bootstrap `data-toggle`) -- only `init()` (page load /
 *   post-Save-success) or the edit-pencil's fetch ever replace it.
 *   Reproduced by never clearing `checkListItemNameList`/`itemSearchTerm`
 *   on `openAddItemModal()`/`closeAddItemModal()` below.
 * - `checklistMasterDetailsId` on a freshly-added (never-saved) item is
 *   always `null` in legacy (`checklistDetailsObj.checklistMasterDetailsId`
 *   is initialized once in `init()` and never reassigned anywhere else) and
 *   is IGNORED server-side regardless (`ChecklistMasterDetailsDaoImpl
 *   .saveChecklistMasterDetails` never calls `setChecklistMasterDetailsId`
 *   on the new entity it constructs -- confirmed by reading the whole
 *   method). Every Save is effectively "soft-delete all existing detail
 *   rows for this master (`updateChecklistMasterDetails`, sets
 *   `status='I'`), then insert everything currently in the submitted list
 *   as brand-new rows" -- reproduced faithfully, not treated as an
 *   incremental diff/patch.
 */
@Component({
  selector: 'app-checklist-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './checklist-master.html',
  styleUrl: './checklist-master.scss',
})
export class ChecklistMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ChecklistMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  readonly stageOptions = STAGE_OPTIONS;

  // ---- header form (mirrors $scope.checklistObj, ctrl:42-57) ----
  form = signal<ChecklistMasterForm>(emptyChecklistMasterForm());
  formSubmitted = signal(false);

  codeRequiredError = computed(() => this.formSubmitted() && this.form().code.trim() === '');
  descriptionRequiredError = computed(() => this.formSubmitted() && this.form().description.trim() === '');
  roleRequiredError = computed(() => this.formSubmitted() && this.form().roleId == null);
  itemsRequiredError = computed(() => this.formSubmitted() && this.checkListItemNameList().length === 0);

  // ---- role dropdown (mirrors otimsRoleList, ctrl:75-89) ----
  roleList = signal<OtimsRole[]>([]);

  // ---- list + pagination (mirrors checklistList/pager, ctrl:92-116/318-359) ----
  list = signal<ChecklistMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side-only, current-page-only filter mirroring
  // `checklistList | filter:searchKeyword` (view:163) -- AngularJS's bare
  // `filter:expression` matches against every property of each row object.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  // ---- Add-Checklist-Item modal (mirrors checkListItemNameList/
  // checklistDetailsObj, ctrl:59-70/120-170) ----
  showAddItemModal = signal(false);
  checkListItemNameList = signal<ChecklistItemDetail[]>([]);
  itemSearchTerm = signal('');
  itemSuggestions = signal<ChecklistItemSuggestion[]>([]);
  /** Confirmed selection from the autocomplete dropdown -- see this class's
   * doc comment ("TOP-PRIORITY FINDING") for why this must be cleared on
   * every keystroke, not just after a successful Add. */
  selectedSuggestion = signal<ChecklistItemSuggestion | null>(null);
  itemAddError = signal<string | null>(null);
  private searchDebounce?: ReturnType<typeof setTimeout>;

  // ---- read-only "Checklist Items" view modal (mirrors checklistNameList,
  // ctrl:273-294 label==1) ----
  showViewItemsModal = signal(false);
  viewItemsList = signal<ChecklistItemDetail[]>([]);

  constructor() {
    this.loadRoleList();
    this.loadList();
  }

  // ---- initial load: otimsRole() (ctrl:76-89) ----
  private loadRoleList(): void {
    this.api.getActiveOtRoleList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.roleList.set(res.listObject ?? []);
      },
    });
  }

  // mirrors getChecklistMasterList (ctrl:93-116): ONE call returns both the
  // page's rows AND the total count together, then resets to page 1
  // (setPage(1,false) -- pager recompute only, no extra fetch).
  private loadList(): void {
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          this.totalCount.set(res.object ?? 0);
          this.currentPage.set(1);
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // mirrors masterList (ctrl:318-342), called only from pagination clicks --
  // list-only reload, no count re-read.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
  }

  // mirrors setNoOfRecords (ctrl:344-346).
  onPageSizeChange(): void {
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof ChecklistMasterForm>(field: K, value: ChecklistMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- Add-Checklist-Item modal ----
  openAddItemModal(): void {
    // Not reset on open, matching legacy's bare data-toggle modal (no
    // ng-click hook) -- see class doc comment.
    this.showAddItemModal.set(true);
  }

  closeAddItemModal(): void {
    // Plain, working close -- legacy's own footer "Save" button here has no
    // ng-click at all (view:316), same real behavior.
    this.showAddItemModal.set(false);
  }

  // mirrors the combination of the uib-typeahead's ng-model binding and
  // selectCheckItem's implicit invalidation -- see class doc comment.
  onItemSearchInput(value: string): void {
    this.itemSearchTerm.set(value);
    this.selectedSuggestion.set(null);
    this.itemAddError.set(null);
    clearTimeout(this.searchDebounce);
    const term = value.trim();
    if (!term) {
      this.itemSuggestions.set([]);
      return;
    }
    this.searchDebounce = setTimeout(() => this.searchChecklistItemName(term), 250);
  }

  // mirrors getCheckItemListByName (ctrl:120-132).
  private searchChecklistItemName(term: string): void {
    this.api.searchChecklistItemName(term, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.itemSuggestions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors selectCheckItem (ctrl:137-142).
  selectSuggestion(item: ChecklistItemSuggestion): void {
    this.selectedSuggestion.set(item);
    this.itemSearchTerm.set(item.description);
    this.itemSuggestions.set([]);
    this.itemAddError.set(null);
  }

  // mirrors addChecklistItemName (ctrl:144-164), with the corrected
  // stale-id/blank-description guard from this class's doc comment.
  addItem(): void {
    const term = this.itemSearchTerm().trim();
    if (!term) return; // matches legacy's silent no-op on an empty box
    const selected = this.selectedSuggestion();
    if (!selected || selected.description !== this.itemSearchTerm()) {
      this.itemAddError.set('Please select a checklist item from the list.');
      return;
    }
    const now = formatDateTime(new Date());
    this.checkListItemNameList.update((list) => [
      ...list,
      {
        checklistMasterDetailsId: null,
        checklistItemMasterId: selected.checklistItemMasterId,
        description: selected.description,
        status: 'A',
      },
    ]);
    this.itemSearchTerm.set('');
    this.selectedSuggestion.set(null);
    this.itemSuggestions.set([]);
    this.itemAddError.set(null);
  }

  // mirrors remove (ctrl:166-170).
  removeItem(index: number): void {
    this.checkListItemNameList.update((list) => list.filter((_, i) => i !== index));
  }

  // ---- read-only "Checklist Items" view modal ----
  // mirrors getChecklistNameListById(info,1) (ctrl:274-294, label==1).
  viewItems(row: ChecklistMasterRow): void {
    this.loader.start();
    this.api.getChecklistMasterDetailsByMasterId(this.organizationId, this.unitId, row.checklistMasterId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.viewItemsList.set(res.listObject ?? []);
          this.showViewItemsModal.set(true);
        }
      },
      error: () => this.loader.stop(),
    });
  }

  closeViewItemsModal(): void {
    this.showViewItemsModal.set(false);
  }

  // ---- edit (mirrors update(info,2), ctrl:296-316) ----
  edit(row: ChecklistMasterRow): void {
    this.loader.start();
    this.api.getChecklistMasterDetailsByMasterId(this.organizationId, this.unitId, row.checklistMasterId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.viewItemsList.set(res.listObject ?? []);
          this.checkListItemNameList.set(res.listObject ?? []); // label==2 branch
        }
      },
      error: () => this.loader.stop(),
    });
    this.form.set({
      checklistMasterId: row.checklistMasterId,
      code: row.code,
      description: row.description,
      roleId: row.roleId,
      stageId: row.stageId,
      isWho: row.isWho,
      status: row.status,
    });
    this.formSubmitted.set(false);
  }

  // ---- Save (mirrors saveCheckListMaster, ctrl:172-233 -- single upsert
  // endpoint, keyed on whether checklistMasterId is present) ----
  private validate(): boolean {
    this.formSubmitted.set(true);
    const f = this.form();
    if (f.code.trim() === '') {
      this.notify.warning('Please select code');
      return false;
    }
    if (f.description.trim() === '') {
      this.notify.warning('Please select description');
      return false;
    }
    if (f.roleId == null) {
      this.notify.warning('Please select Role');
      return false;
    }
    if (this.checkListItemNameList().length === 0) {
      this.notify.warning('Please select Checklist name');
      return false;
    }
    return true;
  }

  save(): void {
    if (!this.validate()) return;
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        checklistMasterId: f.checklistMasterId,
        code: f.code,
        description: f.description,
        roleId: f.roleId as number,
        stageId: f.stageId,
        isWho: f.isWho,
        checklistMasterDetails: this.checkListItemNameList().map((item) => ({
          checklistItemMasterId: item.checklistItemMasterId,
          description: item.description,
          checklistMasterDetailsId: item.checklistMasterDetailsId,
          createdBy: this.userId,
          createdDate: now,
          status: 'A',
          updatedBy: this.userId,
          updatedDate: now,
          unitId: this.unitId,
          organizationId: this.organizationId,
        })),
        createdBy: this.userId,
        createdDate: now,
        status: 'A',
        updatedBy: this.userId,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else if (res.status === 'Already Exists.') {
            // ChecklistMasterDaoImpl's real, live duplicate-check response
            // (ICommonConstants.ALREADY_EXIST) -- see class doc comment.
            this.notify.error(res.message ?? 'Already Exists.');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // mirrors init() (ctrl:40-72) as re-entered by save()'s success branch
  // (ctrl:206).
  private resetForm(): void {
    this.form.set(emptyChecklistMasterForm());
    this.formSubmitted.set(false);
    this.checkListItemNameList.set([]);
  }

  // ---- status toggle (mirrors updateStatus, ctrl:235-270) ----
  toggleStatus(row: ChecklistMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, matching legacy's ng-model-before-ng-change timing.
    this.list.update((rows) => rows.map((r) => (r.checklistMasterId === row.checklistMasterId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        status: nextStatus,
        checklistMasterId: row.checklistMasterId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:251 -- a full reload always fetches offset 0; see class
            // doc comment on the resulting jump-to-page-1 quirk.
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:51/54/64/67/154/157/238/298. Same convention as this codebase's
 * `hospital-account-master`/`bank-master` siblings. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
