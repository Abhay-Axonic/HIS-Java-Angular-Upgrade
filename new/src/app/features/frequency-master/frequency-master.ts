import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { FrequencyMasterService } from './frequency-master.service';
import {
  ClockRow,
  FrequencyForm,
  FrequencyRow,
  LanguageMasterRow,
  TranslatedLanguageEntry,
  defaultClockRow,
  emptyFrequencyForm,
} from './frequency-master.models';
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
 * Parity rebuild of
 * views/master/GlobalMaster/AssetManagenent/Master/frequency-master.html +
 * resources/assets/js/controller/master/pharmacy/frequencyMasterController.js
 * (`frequencyMasterController`, `controllerAs: 'vm'` -- confirmed real,
 * heavy `vm.model.*` usage throughout the view, self-registered state
 * `frequencyMaster` in the controller's own `.config($stateProvider)`
 * block, NOT app.js's central router). moduleId 478 (resolve block reads
 * `localStorage.getItem('permissions').modules`, filters `id == 478`), no
 * `requireSuperUser`.
 *
 * **Backend confirmed real end to end** for every endpoint this screen
 * calls: `FrequencyController.java`
 * (legacy/Pharmacy/.../master/global/controller/, `@RequestMapping("/api/globals/frequency/")`)
 * -> `FrequencyService.java` -> `IFrequencyDao`, all 4 CRUD methods
 * (GetAll/GetById/Update/Save) plus `getConvertedLanguageListById` (dead in
 * this controller -- `$scope.showLanguageList` is defined but never called
 * from the view, not reproduced here) genuinely persist via Hibernate, not
 * stubs. The two "Customize Language" sub-feature calls
 * (`listLanguageMaster`/`getTotalLanguageMasterRecordActive`) resolve to a
 * SEPARATE, also-real controller (`legacy/LIS/.../unit/controller/
 * LanguageMasterController.java`) -- only `listLanguageMaster` is actually
 * invoked by this screen (`getTotalLanguageMasterRecordActive` backs
 * `$scope.getLanguageList`, defined but commented-out at ctrl:525, dead,
 * not reproduced).
 *
 * **Missing-`$state`-DI crash bug, same recurring class as
 * `docs/ANGULAR_PENDING_FINDINGS.md` entries 8/10.** ctrl:3-13's DI list
 * (`$scope, $rootScope, $http, $filter, CONSTANTS, $cookies, GenericService,
 * growl, PaginationService`) never includes `$state`, yet ctrl:17's
 * `if(cookieObject == undefined){ $state.go('login'); return;}` guard calls
 * it -- a `ReferenceError` (blank page, no visible error) on session
 * expiry/missing cookie, not a redirect. A THIRD independent instance of
 * this exact bug class beyond the two already logged. Not ported literally
 * here -- `SessionService`'s own cookie read is used instead, matching this
 * codebase's established safe pattern for this bug class.
 *
 * **Uses only plain, native Bootstrap `.modal` markup for all 4 of its
 * popups** (`#addfrequency`, `#detailsTimeModal`, `#customiseLanguagePoup`,
 * `#confirmSave`) -- confirmed via full-view read, NONE of them is the
 * shared `<modal visible="...">` element directive `docs/
 * ANGULAR_PENDING_FINDINGS.md` entry 55 covers. Entry 55's standing policy
 * does NOT apply to this screen; every close/cancel button already works
 * correctly in legacy itself (plain `data-dismiss="modal"`), so this
 * rebuild's modals need no special "working-close-button" disclosure.
 *
 * **Two real, previously-undocumented validation-bypass bugs in the "Add
 * Administration Time" flow, both faithfully reproduced (new findings, this
 * batch):**
 * 1. `GetTime()` (ctrl:160-185, the modal's Ok handler) sets
 *    `self.model.isOk = true` and `self.model.timeList = items`
 *    UNCONDITIONALLY, before `ValidateTime()` even runs -- a failed
 *    format/business-rule check only keeps the modal open and shows a
 *    growl; it does NOT undo the `isOk = true`/`timeList` assignment
 *    already made. A user who dismisses the still-open modal after a
 *    validation error (backdrop click, browser back, etc.) leaves the
 *    screen in a state where Save's own `isOk` gate is already satisfied
 *    with genuinely invalid time data sitting in `timeList`, ready to be
 *    submitted on the next Save click. Reproduced verbatim below
 *    (`confirmAdminTimes()`).
 * 2. `ValidateTime()`'s per-row format check (`validateTimeFormat`,
 *    ctrl:656-701) writes its result to a single SHARED flag
 *    (`$scope.isValidTimeFormat`), reset and reassigned on every row inside
 *    the `forEach` -- so only the LAST row's format-validity survives to
 *    the final `if(!$scope.isValidTimeFormat || !isValid)` check. An
 *    earlier row with a malformed time still growls an error but does NOT
 *    reliably block the overall save gate if a later row's format happens
 *    to be valid. Reproduced verbatim below (`isValidTimeFormatFlag`, a
 *    single field overwritten per-row exactly like `$scope.isValidTimeFormat`).
 *
 * **Editing an existing row does not reset `isOk`** (`populateFrequencyMaster`,
 * ctrl:269-312, never touches `self.model.isOk`) -- so after clicking Edit,
 * Save/Update remains gated on `isOk` being `true`, which it will NOT be
 * unless the user re-opens "Add Administration Time" and clicks Ok again
 * (even though the correct times were already loaded into `clockList`).
 * Editing an existing frequency master therefore always requires this
 * extra, easy-to-miss re-confirmation step or Update silently fails with
 * "Add Items in Frequency Details". Reproduced verbatim -- `edit()` below
 * does not touch `formOk`.
 *
 * **Create vs. Update validation asymmetry, faithfully reproduced:**
 * `ValidateCode()` (create) checks the duplicate-code regex
 * (`/^[a-z0-9]*[-]?[a-z0-9]*$/i`, alphanumeric with at most one hyphen) --
 * `ValidateCodeForUpdate()` (update) does NOT run this regex at all, only
 * the duplicate-code check (excluding the row being edited) and the same
 * `isOk` gate, with a DIFFERENT error message ("Add Items in Frequency
 * Details" vs. create's "Save Items in Frequency Details").
 *
 * **Status is a real boolean** (`GetFrequencyResponse.status: Boolean`),
 * not `'A'/'I'` -- see `.models.ts`'s header comment for the cross-screen
 * status-vocabulary-drift note against this batch's `role-creation`.
 * Deactivating a row (`activateDeactivateFrequencyMaster`, ctrl:314-335)
 * also unconditionally clears the top-of-page create/edit form
 * (`clearFields()`) even if the in-progress form belongs to an unrelated
 * row -- reproduced verbatim via `toggleStatus()` below.
 *
 * **Search box only filters the CURRENT PAGE's already-paginated slice**
 * (view:161, `vm.model.pagination.data | filter:vm.model.search`) -- the
 * same recurring "current-page-only search" quirk class documented for
 * several sibling screens elsewhere in this codebase (e.g. `role-management`),
 * confirmed fresh here from source: `vm.model.search` is plain `ng-model`,
 * never wired to `PaginationService.searchData()`. Reproduced via
 * `displayedRows` below, filtering only `pagination().data`.
 */
@Component({
  selector: 'app-frequency-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './frequency-master.html',
  styleUrl: './frequency-master.scss',
})
export class FrequencyMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(FrequencyMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- list + client-side pagination (PaginationService port) ----
  private fullList: FrequencyRow[] = [];
  pagination = signal<PaginationState<FrequencyRow>>(createPaginationState<FrequencyRow>());
  search = signal('');
  sortType = signal<'code' | 'description' | 'noOfAdministrationTime' | ''>('');
  sortReverse = signal(false);

  // Legacy quirk preserved (see class doc comment): search filters only the
  // CURRENT PAGE's already-sliced rows, never the full dataset.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    let rows = this.pagination().data;
    if (keyword) {
      rows = rows.filter((r) => JSON.stringify(r).toLowerCase().includes(keyword));
    }
    const type = this.sortType();
    if (type) {
      const reverse = this.sortReverse();
      rows = [...rows].sort((a, b) => {
        const av = a[type] as any;
        const bv = b[type] as any;
        const cmp = av < bv ? -1 : av > bv ? 1 : 0;
        return reverse ? -cmp : cmp;
      });
    }
    return rows;
  });

  // ---- top-of-page create/edit form ----
  form = signal<FrequencyForm>(emptyFrequencyForm());
  /** `self.model.isOk` -- gates Save/Update. See class doc comment: set
   * unconditionally true by `confirmAdminTimes()`, even on failed validation. */
  formOk = signal(false);
  /** `self.model.timeList` -- the raw item list actually submitted on
   * save/update, built unconditionally by `confirmAdminTimes()`. */
  private timeList: Array<{ id?: number; administrationTime: string; preBufferAdministrationTime: string; postBufferAdministrationTime: string }> = [];

  canAct = computed(() => !!this.form().frequencyCode && !!this.form().frequencyDescription);

  // ---- Add Administration Time modal (clockList) ----
  showAdminTimeModal = signal(false);
  clockList = signal<ClockRow[]>([defaultClockRow()]);
  /** `$scope.isValidTimeFormat` -- deliberately a single shared flag,
   * overwritten per-row inside the validation loop; see class doc comment
   * finding #2. */
  private isValidTimeFormatFlag = true;

  // ---- View Times modal (read-only) ----
  showViewTimesModal = signal(false);
  viewTimesRows = signal<FrequencyRow['items']>([]);
  popupSortType = signal<'administrationTime' | 'preBufferAdministrationTime' | 'postBufferAdministrationTime' | ''>('');
  popupSortReverse = signal(false);
  sortedViewTimesRows = computed(() => {
    const type = this.popupSortType();
    const rows = this.viewTimesRows() ?? [];
    if (!type) return rows;
    const reverse = this.popupSortReverse();
    return [...rows].sort((a, b) => {
      const av = (a as any)[type];
      const bv = (b as any)[type];
      const cmp = av < bv ? -1 : av > bv ? 1 : 0;
      return reverse ? -cmp : cmp;
    });
  });

  // ---- Customize Language modal ----
  showLanguageModal = signal(false);
  languageList = signal<LanguageMasterRow[]>([]);
  selectedLanguage = signal<LanguageMasterRow | null>(null);
  convertedText = signal('');
  addedLanguageList = signal<TranslatedLanguageEntry[]>([]);

  // ---- Confirm Save modal ----
  showConfirmSaveModal = signal(false);

  constructor() {
    // Order matches legacy exactly (confirmed via golden-trace capture):
    // `$scope.getLanguageMasterList()` (ctrl:643-654) is called BEFORE
    // `initializeController()` (ctrl:703) in source order, so its HTTP call
    // fires first, even though `getFrequencyMasterData()` is the
    // "primary" list load.
    this.api.getLanguageMasterList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.languageList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    this.api.getAll(this.organizationId).subscribe({
      next: (rows) => {
        this.fullList = rows ?? [];
        if (this.fullList.length > 0) {
          const p = this.pagination();
          searchData(this.fullList, p);
          this.pagination.set({ ...p });
        } else {
          const p = this.pagination();
          p.data = [];
          p.filteredData = [];
          this.pagination.set({ ...p });
        }
      },
    });
  }

  onPageSizeChange(): void {
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

  sortBy(type: 'code' | 'description' | 'noOfAdministrationTime'): void {
    this.sortType.set(type);
    this.sortReverse.update((r) => !r);
  }

  updateFormField<K extends keyof FrequencyForm>(field: K, value: FrequencyForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- Clear button (`ClearFields(); clearSearch();`, ctrl:454-467/101) ----
  clearAll(): void {
    this.form.set(emptyFrequencyForm());
    this.formOk.set(false);
    this.clockList.set([defaultClockRow()]);
    this.timeList = [];
    this.addedLanguageList.set([]);
    this.convertedText.set('');
    this.search.set('');
  }

  // ---- Edit (populateFrequencyMaster, ctrl:262-312) -- deliberately does
  // NOT reset formOk, see class doc comment. ----
  edit(row: FrequencyRow): void {
    if (!row.status) return; // .disabled pointer-events:none in legacy -- genuinely unclickable when inactive
    this.api.getById(this.organizationId, row.id).subscribe({
      next: (detail) => {
        this.form.set({ id: detail.id, frequencyCode: detail.code, frequencyDescription: detail.description });
        this.clockList.set(
          (detail.items ?? []).map((it) => ({
            id: it.id,
            adminTime: it.administrationTimeString,
            preAdminTime: it.preBufferAdministrationTimeString,
            postAdminTime: it.postBufferAdministrationTimeString,
          })),
        );
        this.addedLanguageList.set(detail.addedLanguageList ?? []);
        // formOk deliberately untouched -- see class doc comment.
      },
    });
  }

  // ---- View Times (info-circle icon, ViewTimes(), ctrl:76-82) ----
  openViewTimes(row: FrequencyRow): void {
    this.viewTimesRows.set(row.items ?? []);
    this.showViewTimesModal.set(true);
  }

  closeViewTimes(): void {
    this.showViewTimesModal.set(false);
  }

  popupSortBy(type: 'administrationTime' | 'preBufferAdministrationTime' | 'postBufferAdministrationTime'): void {
    this.popupSortType.set(type);
    this.popupSortReverse.update((r) => !r);
  }

  // ---- status toggle (activateDeactivateFrequencyMaster, ctrl:314-335) ----
  toggleStatus(row: FrequencyRow): void {
    const nextStatus = !row.status;
    this.fullList = this.fullList.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    p.data = p.data.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r));
    p.filteredData = p.filteredData.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r));
    this.pagination.set({ ...p });
    if (!nextStatus) {
      // ctrl:321-323 -- deactivating also clears the whole top form, even
      // mid-edit of an unrelated row. Faithfully reproduced.
      this.clearAll();
    }
    this.api.updateStatus(this.organizationId, row.id, { status: nextStatus }).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: () => this.notify.error('Something went wrong'),
    });
  }

  // ---- Add Administration Time modal ----
  openAdminTimeModal(): void {
    this.showAdminTimeModal.set(true);
  }

  addClockRow(): void {
    this.clockList.update((rows) => [...rows, defaultClockRow()]);
    this.formOk.set(false);
  }

  removeClockRow(index: number): void {
    this.clockList.update((rows) => rows.filter((_, i) => i !== index));
    this.formOk.set(false);
  }

  updateClockRow(index: number, field: 'adminTime' | 'preAdminTime' | 'postAdminTime', value: string): void {
    this.clockList.update((rows) =>
      rows.map((r, i) => {
        if (i !== index) return r;
        const next: ClockRow = { ...r, [field]: value };
        if (field === 'adminTime') next.isAdminTime = false;
        if (field === 'preAdminTime') next.isPreAdminTime = false;
        if (field === 'postAdminTime') next.isPostAdminTime = false;
        return next;
      }),
    );
  }

  /** GetTime(), ctrl:160-185. See class doc comment finding #1: `formOk`
   * and the submitted `timeList` are set UNCONDITIONALLY, before
   * validation -- a real, faithfully-reproduced validation-bypass bug. */
  confirmAdminTimes(): void {
    this.formOk.set(true);
    this.timeList = this.clockList().map((x) => ({
      id: x.id,
      administrationTime: x.adminTime,
      preBufferAdministrationTime: x.preAdminTime,
      postBufferAdministrationTime: x.postAdminTime,
    }));
    if (this.validateTime()) {
      this.showAdminTimeModal.set(false);
    }
  }

  /** ValidateTime(), ctrl:187-243. */
  private validateTime(): boolean {
    let isValid = true;
    const rows = this.clockList();
    const updated: ClockRow[] = [];
    for (const row of rows) {
      const r = { ...row };
      if (this.validateTimeFormat(r)) {
        let adminTime = parseFloat(r.adminTime);
        let preTime = parseFloat(r.preAdminTime);
        let postTime = parseFloat(r.postAdminTime);
        if (adminTime === 0) adminTime = 24;
        if (postTime === 0) postTime = 24;
        if (preTime > adminTime) {
          r.isPreAdminTime = true;
          r.isAdminTime = true;
          r.isPostAdminTime = false;
          isValid = false;
          this.notify.error('Pre Administration Time should be less than Administration Time');
        }
        if (postTime < adminTime) {
          r.isAdminTime = true;
          r.isPostAdminTime = true;
          r.isPreAdminTime = false;
          isValid = false;
          this.notify.error('Post Administration Time should be greater than Administration Time');
        }
        if (preTime > postTime) {
          r.isPreAdminTime = true;
          r.isPostAdminTime = true;
          r.isAdminTime = false;
          isValid = false;
          this.notify.error('Pre Administration Time should be less than Post Administration Time');
        }
      } else {
        isValid = false;
      }
      updated.push(r);
    }
    this.clockList.set(updated);
    // ctrl:237-242 -- shared-flag bug, see class doc comment finding #2:
    // only the LAST row's format result survives here.
    return this.isValidTimeFormatFlag && isValid;
  }

  /** validateTimeFormat(), ctrl:656-701 -- writes to `isValidTimeFormatFlag`,
   * a single field shared/overwritten across every row in the loop above,
   * exactly like legacy's `$scope.isValidTimeFormat`. */
  private validateTimeFormat(item: ClockRow): boolean {
    const times = [item.adminTime, item.preAdminTime, item.postAdminTime];
    this.isValidTimeFormatFlag = true;
    for (const time of times) {
      if (time !== undefined && time !== null && time !== '') {
        if (!/^[0-9:]*$/.test(time)) {
          this.isValidTimeFormatFlag = false;
          this.notify.error('Please enter valid time!');
          return this.isValidTimeFormatFlag;
        }
        const parts = time.split(':');
        if (parts.length !== 2 || parts[0].length > 2 || parts[1].length > 2) {
          this.isValidTimeFormatFlag = false;
          this.notify.error('Please enter valid time!');
          return this.isValidTimeFormatFlag;
        }
        const hours = parseInt(parts[0], 10);
        const minutes = parseInt(parts[1], 10);
        if (isNaN(hours) || isNaN(minutes) || hours < 0 || hours > 24 || minutes < 0 || minutes > 59) {
          this.isValidTimeFormatFlag = false;
          this.notify.error('Please enter valid time!');
          return this.isValidTimeFormatFlag;
        }
      } else {
        this.isValidTimeFormatFlag = false;
        this.notify.error('Please enter valid time!');
        return this.isValidTimeFormatFlag;
      }
    }
    return this.isValidTimeFormatFlag;
  }

  // ---- Customize Language modal ----
  openLanguageModal(): void {
    // assignToLang(), ctrl:474-479 -- the guard there is a tautology
    // (`self.model.frequencyCode !== undefined && self.model.frequencyCode
    // !== undefined`, the same field compared to itself twice, never
    // actually checking description) -- so `convertedText` is unconditionally
    // reset to the current Frequency Description every time this opens.
    // Faithfully reproduced (not "fixed" into a real dual-field guard).
    this.convertedText.set(this.form().frequencyDescription);
    this.selectedLanguage.set(null);
    this.showLanguageModal.set(true);
  }

  closeLanguageModal(): void {
    this.showLanguageModal.set(false);
  }

  /** addLanguageDetails(), ctrl:537-588. */
  addLanguageDetail(): void {
    const lang = this.selectedLanguage();
    if (!lang || lang.languageId == null) {
      this.notify.error('Please select language');
      return;
    }
    if (!this.convertedText()) {
      this.notify.error('Please fill-up text');
      return;
    }
    const already = this.addedLanguageList().some((l) => Number(l.languageId) === Number(lang.languageId));
    if (already) {
      this.notify.error('Already Exist..');
      return;
    }
    this.addedLanguageList.update((list) => [...list, { languageId: lang.languageId, desc: lang.desc, convertedText: this.convertedText() }]);
    this.selectedLanguage.set(null);
  }

  removeLanguage(index: number): void {
    this.addedLanguageList.update((list) => list.filter((_, i) => i !== index));
  }

  // ---- Save button (bottom floppy icon) -> confirm modal ----
  openConfirmSave(): void {
    this.showConfirmSaveModal.set(true);
  }

  closeConfirmSave(): void {
    this.showConfirmSaveModal.set(false);
  }

  /** SaveFrequencyMaster(), ctrl:338-344 -- branches on `form().id`. */
  confirmSave(): void {
    this.closeConfirmSave();
    if (this.form().id) {
      this.updateFrequency();
    } else {
      this.createFrequency();
    }
  }

  /** createFrequencyMaster(), ctrl:387-415, gated by ValidateCode(). */
  private createFrequency(): void {
    if (!this.validateCode()) return;
    const f = this.form();
    this.api
      .save({
        code: f.frequencyCode,
        description: f.frequencyDescription,
        noOfAdministrationTime: this.timeList.length,
        items: this.timeList,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
        addedLanguageList: this.addedLanguageList(),
      })
      .subscribe({
        next: (res) => {
          this.loadList();
          this.notify.success(res.message ?? 'Saved');
          this.clearAll();
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  /** updateFrequencyMaster(), ctrl:418-451, gated by ValidateCodeForUpdate() --
   * NOTE: no `userId` field sent here, unlike create (asymmetric, faithful). */
  private updateFrequency(): void {
    if (!this.validateCodeForUpdate()) return;
    const f = this.form();
    this.api
      .update(this.organizationId, f.id as number, {
        code: f.frequencyCode,
        description: f.frequencyDescription,
        noOfAdministrationTime: this.timeList.length,
        items: this.timeList,
        unitId: this.unitId,
        organizationId: this.organizationId,
        addedLanguageList: this.addedLanguageList(),
      })
      .subscribe({
        next: (res) => {
          this.loadList();
          this.notify.success(res.message ?? 'Updated');
          this.clearAll();
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  /** ValidateCode(), ctrl:346-365. */
  private validateCode(): boolean {
    let isValid = true;
    const code = this.form().frequencyCode;
    for (const row of this.fullList) {
      if (row.code === code) {
        this.notify.error('Duplicate Frequency code');
        isValid = false;
      }
    }
    if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(code)) {
      this.notify.error('Name can only be alpha numeric with hypen.');
      return false;
    }
    if (!this.formOk()) {
      this.notify.error('Save Items in Frequency Details');
      return false;
    }
    return isValid;
  }

  /** ValidateCodeForUpdate(), ctrl:367-383 -- NO regex check here, unlike
   * create; different error message text, both faithful to legacy. */
  private validateCodeForUpdate(): boolean {
    let isValid = true;
    const code = this.form().frequencyCode;
    const activeId = this.form().id;
    for (const row of this.fullList) {
      if (row.id !== activeId && row.code === code) {
        this.notify.error('Duplicate Frequency code');
        isValid = false;
      }
    }
    if (!this.formOk()) {
      this.notify.error('Add Items in Frequency Details');
      return false;
    }
    return isValid;
  }
}
