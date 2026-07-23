import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ConsentTypeMasterService } from './consent-type-master.service';
import { ConsentTypeForm, ConsentTypeRow, ConsentTypeStatus, emptyConsentTypeForm } from './consent-type-master.models';
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
 * views/master/GlobalMaster/consent-type-master.html (198 lines) +
 * resources/assets/js/controller/adt/global/consentTypeMasterController.js
 * (396 lines, `consentTypeMasterController`, no `controllerAs`). State
 * `consentTypeMaster` (app.js:3977-4003), moduleId 399 -- confirmed real,
 * resolve block present, gated entirely by the route guard, no code needed
 * in this component for that.
 *
 * Confirmed PURE-MASTER/administrative for this batch's Clinical/Billing-
 * Tier Addendum review: `admissionConsent` is a plain persisted 'Y'/'N'
 * attribute on the consent-type record itself (an admission-consent-FORM-
 * REQUIREMENT flag), not a clinical-safety computation, and it does NOT
 * gate the visibility of `consentCode`/`consentName` or anything else on
 * this screen -- just another field saved alongside them.
 *
 * Real, confirmed end-to-end endpoints (base `/adt`): `saveConsentTypeMaster`
 * (create), `getConsentTypeMasterList` + `getConsentTypeCount` (paged
 * list), `updateConsentTypeMaster` (update), `updateStatusForConsentType`
 * (status toggle), `getConsentTypeById` (single-record fetch for Edit).
 * Legacy calls all six via `GenericService.serviceAction("POST", ...)`, so
 * `getById` below is POST too, not GET.
 *
 * ================================================================
 * Two-click confirm on Save, none on Update -- with the SAME re-arm
 * asymmetry already established in this codebase's `reason-type-master`
 * ================================================================
 * `saveConsentType()`'s `$scope.popUpFlag` (ctrl:58,197-267) starts `true`,
 * gates whether clicking Save opens the confirm modal (`showModal =
 * !showModal`) or actually POSTs, and is only flipped back to `true` by a
 * SUCCESSFUL save's `$scope.init()` (ctrl:249) -- a FAILED save (duplicate
 * name) does NOT call `init()`, so `popUpFlag` stays `false` and the very
 * next Save click skips the confirm modal entirely and POSTs immediately.
 * Reproduced below via `confirmArmed`: armed on load, disarmed by the
 * modal's own Ok button (`confirmSave()`), only re-armed by a successful
 * save's `resetForm()`. `updateConsentType()` (ctrl:342-394) has no
 * `popUpFlag`/modal concept at all -- `update()` below always saves
 * directly, gated only by the same required-field check.
 *
 * ================================================================
 * BUG faithfully reproduced, NOT fixed: the backend's "case-insensitive"
 * duplicate-name check is actually broken
 * ================================================================
 * The real SQL behind both `saveConsentTypeMaster` and
 * `updateConsentTypeMaster` lowercases the STORED column but never
 * lowercases the SUBMITTED value -- so typing "General Consent" when an
 * existing row is stored as "general consent" will NOT be caught as a
 * duplicate; only an exact-case (or already-lowercase) match trips the
 * "already exists" error. This is real backend behavior, not something this
 * Angular screen can or should pre-check client-side -- `doSave()`/
 * `update()` below let the real POST response flow through unmodified, with
 * no client-side case-insensitive duplicate guard added on top that would
 * mask it.
 *
 * ================================================================
 * Other faithfully reproduced quirks
 * ================================================================
 * - Both save AND update reload the list UNCONDITIONALLY after every
 *   response, success or failure (ctrl:257-263, ctrl:386-391) -- reproduced
 *   via `loadList()` called in both branches of `doSave()`/`update()`'s
 *   subscribe.
 * - A successful save/update calls `$scope.$broadcast('show-errors-reset')`
 *   (ctrl:247-248, ctrl:375-376) in addition to `$scope.init()` -- unlike
 *   this codebase's `reason-type-master` (which never broadcasts that and
 *   deliberately leaves `formSubmitted` set), THIS screen's `resetForm()`
 *   DOES clear `formSubmitted` back to `false` on a successful save/update,
 *   matching that extra broadcast's real effect (clearing inline
 *   required-field error display).
 * - `showUpdateBtn(id)` (ctrl:270-298) flips to Update mode IMMEDIATELY on
 *   click, before the GET even resolves (ctrl:272-273) -- reproduced by
 *   `saveMode.set(false)` at the very top of `edit()`, before the API call.
 *   Its failure path is a native `alert("Error!!")` (ctrl:294), not a growl
 *   toast -- reproduced verbatim via `alert(...)` below.
 * - `updateStatus(id, type)` (ctrl:301-339) neither reloads the list nor
 *   resets the form on success OR failure -- it only fires a toast either
 *   way. The row's checkbox is two-way bound in legacy, so the visual flip
 *   happens immediately regardless of the response; on failure legacy never
 *   reverts it. Reproduced via `toggleStatus()`'s optimistic flip with no
 *   revert and no reload/reset in either branch.
 * - No `startLoader()`/`stopLoader()` anywhere in this controller (grepped:
 *   zero occurrences) -- `LoaderService` is deliberately not wired here,
 *   matching this codebase's other "simple master" screens
 *   (`reason-type-master`, `ward-master`, `billing-group-master`).
 * - List/pagination: `initConsentTypeMasterList` (ctrl:87-139) POSTs
 *   `getConsentTypeMasterList` (paged) and, in parallel, the org+unit-scoped
 *   `getConsentTypeCount`. Per this batch's established convention (see
 *   `ward-store-mapper`), this rebuild fetches the total count first, then
 *   issues a SINGLE `getConsentTypeMasterList` call sized to that count (the
 *   full result set arrives in one response), and hands it to
 *   `client-pagination.util`'s `searchData`/`setPage`/etc. for all
 *   subsequent client-side paging -- both real endpoints are still
 *   genuinely called on load, just not re-called per page the way legacy's
 *   own `setPage(page, true)` does.
 * - **The search box filters the CURRENT PAGE only**, matching legacy
 *   exactly (view:151, `consentTypeList|filter:searchKeyword`, applied
 *   directly to whatever page legacy's own server call already returned).
 *   Because this rebuild holds the FULL list client-side (see bullet
 *   above), `searchKeyword` below is deliberately kept OUTSIDE
 *   `PaginationState.search` (never set) and instead filters
 *   `pagination().data` -- the already-paginated current-page slice -- via
 *   the `displayedRows` computed, the same shape already established by
 *   `ward-store-mapper`.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), legacy's
 *   shared `<modal>` directive's x close button is a new instance of the
 *   same already-adjudicated pattern: it's hardcoded to call `cancel()`, a
 *   function this controller never defines either (grepped, zero matches).
 *   Per standing policy this is an already-approved standing improvement,
 *   not a fresh decision -- the confirm modal below gives its x button
 *   ordinary working close behavior (`closeConfirmModal()`), no crash.
 */
@Component({
  selector: 'app-consent-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './consent-type-master.html',
  styleUrl: './consent-type-master.scss',
})
export class ConsentTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ConsentTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.consenttype) ----
  form = signal<ConsentTypeForm>(emptyConsentTypeForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. */
  private confirmArmed = true;

  // ---- full list + entirely client-side pagination (see class doc) ----
  private fullList: ConsentTypeRow[] = [];
  pagination = signal<PaginationState<ConsentTypeRow>>(createPaginationState<ConsentTypeRow>());

  // ---- search box -- filters the CURRENT PAGE only, see class doc ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.pagination().data;
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.consentTypeMasterCode?.toLowerCase().includes(keyword) || r.consentTypeMasterName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  // ctrl:87-139 (initConsentTypeMasterList) -- see class doc: fetches the
  // full result set once (sized by the real count endpoint) rather than
  // re-issuing a fresh server call per page like legacy's own
  // setPage(page, true) does.
  loadList(): void {
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (countRes) => {
        const total = countRes.object ?? 0;
        this.api.getList(this.organizationId, this.unitId, 0, total > 0 ? total : 10).subscribe({
          next: (listRes) => {
            this.fullList = listRes.listObject ?? [];
            const p = this.pagination();
            searchData(this.fullList, p);
            this.pagination.set({ ...p });
          },
        });
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

  updateField<K extends keyof ConsentTypeForm>(field: K, value: ConsentTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors the 2 `required` inputs backing
  // $scope.consenttypeform.$valid, view:16-17,31-32 -- admissionConsent has
  // no validation of its own) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    // Guard against null/undefined, not just empty string -- found live on
    // the sibling cardTypeMaster screen, where real seed data has a null
    // code field and editing it crashed validation. Same defensive fix
    // applied here pre-emptively.
    return !!((f.consentCode ?? '').trim() && (f.consentName ?? '').trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().consentCode ?? '').trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !(this.form().consentName ?? '').trim();
  }

  // ---- save / confirm-modal (mirrors saveConsentType/PopupOkBtn, ctrl:197-267) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the $valid gate, ctrl:224
    if (this.confirmArmed) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:197-200): disarms the confirm gate, then saves.
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
        consentTypeMasterCode: f.consentCode,
        consentTypeMasterName: f.consentName,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
        createdDate: now,
        admissionConsent: f.admissionConsent,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Consent type added successfully...!!'); // ctrl:241-246, verbatim
            this.resetForm(); // ctrl:247-249 -- broadcast('show-errors-reset') + init(); re-arms confirmArmed
          } else {
            // BUG faithfully reproduced, NOT fixed -- see class doc comment:
            // the backend's own "case-insensitive" duplicate check never
            // lowercases the submitted value, only the stored column.
            this.notify.error('Consent type already exits...!!'); // ctrl:251-256, verbatim legacy typo
            // confirmArmed stays disarmed on failure -- the very next Save
            // click skips the confirm modal, see class doc comment.
          }
          this.loadList(); // ctrl:258-263 -- reloads unconditionally either way
        },
      });
  }

  // ---- update (mirrors updateConsentType, ctrl:342-394 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // ctrl:359 -- the only gate on update
    const f = this.form();
    this.api
      .update({
        consentTypeMasterCode: f.consentCode,
        consentTypeMasterName: f.consentName,
        consentTypeMasterId: f.consentTypeId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
        unitId: this.unitId,
        admissionConsent: f.admissionConsent,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Consent type updated Successfully...!!'); // ctrl:369-374, verbatim
            this.resetForm(); // ctrl:375-377 -- broadcast('show-errors-reset') + init()
          } else {
            // Same broken case-insensitive duplicate-name bug as save -- see class doc comment.
            this.notify.error('Consent type already exits...!!'); // ctrl:379-384, verbatim legacy typo
          }
          this.loadList(); // ctrl:386-391 -- reloads unconditionally either way
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:270-298) ----
  edit(row: ConsentTypeRow): void {
    // ctrl:272-273 -- legacy flips saveBtnFlag/updateBtnFlag to "Update" mode
    // IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.api.getById(row.consentTypeMasterId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          alert('Error!!'); // ctrl:294 -- a native alert, not a growl toast
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          consentTypeId: d.consentTypeMasterId,
          consentCode: d.consentTypeMasterCode,
          consentName: d.consentTypeMasterName,
          admissionConsent: d.admissionConsent,
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:301-339) ----
  toggleStatus(row: ConsentTypeRow): void {
    const nextStatus: ConsentTypeStatus = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- legacy's checkbox is two-way bound
    // (ng-model="info.status"), so the visual change happens immediately
    // regardless of the response either way; ctrl:301-339 never reverts it
    // on error, and never reloads the list or resets the form on success
    // either -- only a toast fires. See class doc comment.
    this.fullList = this.fullList.map((r) => (r.consentTypeMasterId === row.consentTypeMasterId ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.api
      .updateStatus({
        consentTypeMasterId: row.consentTypeMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Status updated successfully...!!'); // ctrl:323-329, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:330-337, verbatim -- no revert, see class doc
          }
        },
      });
  }

  // ---- reset (mirrors init(), ctrl:28-77 as re-entered by a successful
  // save/update) ----
  private resetForm(): void {
    this.form.set(emptyConsentTypeForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false); // mirrors the extra $broadcast('show-errors-reset') on success
    this.confirmArmed = true; // ctrl's init() resets popUpFlag = true
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:205-207/303-305/344-346. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
