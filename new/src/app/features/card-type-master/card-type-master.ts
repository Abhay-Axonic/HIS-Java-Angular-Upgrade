import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { CardTypeMasterService } from './card-type-master.service';
import { CardTypeForm, CardTypeRow, emptyCardTypeForm } from './card-type-master.models';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setPage,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  getPagesList,
} from '../../core/client-pagination.util';

/**
 * Parity rebuild of views/master/GlobalMaster/card-type-master.html (133
 * lines) + resources/assets/js/controller/master/cardTypeMasterController.js
 * (285 lines, `cardTypeMasterController`, no `controllerAs`). State
 * `cardTypeMaster` is SELF-REGISTERED at the bottom of the SAME controller
 * file (ctrl:271-278, `config($stateProvider)` + `.config(config)`), not in
 * `app.js` -- and has NO `resolve` block at all, so there is no `moduleId`
 * to gate this route on. Same no-gate class as this codebase's
 * billingGroupMaster/periodicityService precedent, not a rebuild gap.
 *
 * Confirmed real, live CRUD during Step 0/1 audit, all 4 calls under
 * `/api/billing/*`: list (ctrl:70-86), save (ctrl:100-160), update
 * (ctrl:219-269), status toggle (ctrl:175-216) -- see
 * card-type-master.service.ts. Dev DB has 6 real seed rows (Maestro, Diners
 * Club, RuPay, American Express, Visa, Master Card).
 *
 * A `pinpadScheme` DTO field exists on the backend but is confirmed
 * dead/unused by THIS screen -- not modeled here, it belongs to a
 * different, unrelated endpoint.
 *
 * ================================================================
 * Wire field spelling, verified directly against source (ctrl:71,112,187,230)
 * ================================================================
 * Every one of the 4 calls uses the literal field name `orgnisationId` --
 * NOT `organisationId` and not `organizationId` -- e.g. ctrl:70-72:
 * `var data = { orgnisationId : $scope.organizationId }`. Kept verbatim
 * throughout this rebuild (same spelling this codebase's bank-master
 * already uses for the identical reason: it's the real wire contract, not a
 * local typo to silently "fix").
 *
 * ================================================================
 * Two-click confirm flow on Save ONLY, plus a real, previously-uncataloged
 * "stuck" bug found while re-verifying this screen's source
 * ================================================================
 * `saveCardType()` (ctrl:100-160) is gated by `$scope.popUpFlag` (`init()`
 * sets it `true`, ctrl:67): the FIRST click, with `popUpFlag` true, just
 * toggles `showModal` open (ctrl:118-120) -- no API call yet. The modal's Ok
 * button (`PopupOkBtn`, ctrl:94-97) sets `popUpFlag = false` and calls
 * `saveCardType()` again, which this time (else-branch, ctrl:121-157) fires
 * the actual POST. `popUpFlag`/`showModal`/`cardType` are only ever reset
 * back by `init()` (ctrl:16-87) -- called on page load and inside
 * `saveCardType`'s OWN success branch (ctrl:143) -- there is NO reset
 * anywhere in its error branch (ctrl:144-152, duplicate-name case).
 * **Net effect, faithfully reproduced below:** after any FAILED save (e.g.
 * duplicate name), `popUpFlag` stays permanently `false` -- so every
 * subsequent Save click silently SKIPS the confirm modal and fires the POST
 * immediately, until the user reloads the page. Modeled via `popupArmed`
 * below; `save()` only shows the modal while `popupArmed()` is true, and
 * only a SUCCESSFUL save (via `resetForm()`) re-arms it.
 *
 * ================================================================
 * Discrepancy found on re-verification -- flagged, not silently resolved
 * ================================================================
 * The batch brief for this screen described Update as never resetting its
 * Save/Update-mode UI flags after a successful Update (an "Update stays
 * stuck in edit mode forever" bug, by analogy with other screens in this
 * codebase that genuinely have that shape, e.g. ward-store-mapper). Reading
 * `updateCardType()`'s (ctrl:219-269) success branch directly, line by
 * line, does NOT bear this out for this controller: ctrl:249-252 read --
 * `$scope.$broadcast('show-errors-reset'); $rootScope.stopLoader();
 * $scope.init();` -- i.e. a successful Update calls `$scope.init()` exactly
 * like a successful Save does (ctrl:140-143), which resets `cardType = {}`,
 * `saveBtnFlag = true`, `updateBtnFlag = false`, `popUpFlag = true`, AND
 * reloads the list. There is no dead/unreachable code around it here (no
 * broken identifier, no thrown exception the way ward-store-mapper's own
 * `wardStoremappedList()` genuinely has) -- the call is live and runs. This
 * rebuild therefore reproduces the VERIFIED real behavior instead: `update()`
 * below calls `resetForm()` + `loadList()` on success, matching Save
 * exactly. Flagged here for whoever owns the audit trail to reconcile --
 * this file matches the actual legacy source as re-read directly, not the
 * batch brief's paraphrase of it.
 *
 * ================================================================
 * Status toggle -- list never refreshes (this bug IS confirmed as described)
 * ================================================================
 * `updateStatus()` (ctrl:175-216) only ever growls a toast on success/error
 * (ctrl:198-214) -- no `init()`/list-reload call either way. The view's
 * checkbox (`ng-model="info.status"`, view:89) is two-way bound, so the
 * visual flip happens immediately regardless of the response. Reproduced
 * via an optimistic local patch in `toggleStatus()` below, with no
 * `loadList()` call on either branch.
 *
 * ================================================================
 * Search is a plain client-side filter over the CURRENT PAGE only
 * ================================================================
 * `pagination.data|filter:searchKeyword` (view:84) filters whatever page is
 * already loaded client-side -- legacy fetches the FULL result set in one
 * shot (ctrl:70-86, no offset/count params at all) and paginates/searches it
 * all in the browser via `PaginationService`. Reproduced identically to this
 * codebase's ward-store-mapper: `searchKeyword` below is kept OUTSIDE
 * `PaginationState.search` (never set) and instead filters `pagination().data`
 * -- the already-paginated current-page slice -- via the `displayedRows`
 * computed.
 */
@Component({
  selector: 'app-card-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './card-type-master.html',
  styleUrl: './card-type-master.scss',
})
export class CardTypeMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(CardTypeMasterService);

  private orgId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.cardType + saveBtnFlag/updateBtnFlag) ----
  form = signal<CardTypeForm>(emptyCardTypeForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag; stays false forever after a failed save -- see class doc bug

  // ---- full list + entirely client-side pagination (mirrors PaginationService) ----
  private fullList: CardTypeRow[] = [];
  pagination = signal<PaginationState<CardTypeRow>>(createPaginationState<CardTypeRow>());

  // ---- search box -- filters the CURRENT PAGE only, see class doc ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.pagination().data;
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.cardTypeCode?.toLowerCase().includes(keyword) || r.cardTypeName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.orgId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  // ctrl:70-86 -- fetches the FULL result set in one shot, no offset/count
  // params at all (see class doc comment).
  loadList(): void {
    this.loader.start();
    this.api.getList(this.orgId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.fullList = res.status === 'success' ? (res.listObject ?? []) : [];
        const p = this.pagination();
        searchData(this.fullList, p);
        this.pagination.set({ ...p });
      },
      error: () => this.loader.stop(),
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

  updateField<K extends keyof CardTypeForm>(field: K, value: CardTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 2 HTML5 `required` fields (Code/Description, view:16-19,31-34)
  // that back `$scope.cardTypeform.$valid`. No manual JS validation beyond
  // this in legacy.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.cardTypeCode.trim() && f.cardTypeName.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().cardTypeCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().cardTypeName.trim();
  }

  // ---- save / confirm-modal (mirrors saveCardType/PopupOkBtn, ctrl:94-160) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the cardTypeform.$valid gate
    if (this.popupArmed()) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
    } else {
      // BUG faithfully reproduced -- popUpFlag stuck false after a prior
      // failed save; confirm modal is silently skipped. See class doc.
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.popupArmed.set(false); // mirrors PopupOkBtn's `$scope.popUpFlag = false;`
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        cardTypeCode: f.cardTypeCode,
        cardTypeName: f.cardTypeName,
        status: 'A', // hardcoded on every create -- see class doc comment, no active/inactive toggle here
        createdBy: this.userId,
        updatedBy: this.userId,
        orgnisationId: this.orgId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Card Type added Successfully...!!'); // ctrl:134-139, verbatim
            this.resetForm(); // ctrl:143 -- init() runs on success, re-arms popupArmed too
            this.loadList();
          } else {
            this.notify.error('Card Type already exits...!!'); // ctrl:145-150, verbatim typo, not corrected
            // ctrl:144-152 -- no reset here; popupArmed stays false. See class doc bug.
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyCardTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag = true
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:163-172 -- purely synchronous, no GET) ----
  edit(row: CardTypeRow): void {
    this.saveMode.set(false);
    this.form.set({
      cardTypeId: row.cardTypeId,
      cardTypeCode: row.cardTypeCode,
      cardTypeName: row.cardTypeName,
    });
  }

  // ---- update (mirrors updateCardType, ctrl:219-269 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the cardTypeform.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        cardTypeCode: f.cardTypeCode,
        cardTypeName: f.cardTypeName,
        cardTypeId: f.cardTypeId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        orgnisationId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Card Type updated Successfully...!!'); // ctrl:244-248, verbatim
            // ctrl:249-252 -- init() genuinely runs on a successful update in
            // this controller (see class doc's "Discrepancy found" section);
            // reproduced as a real reset + reload, not left stuck in edit mode.
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error('Card Type already exits...!!'); // ctrl:255-260, verbatim, same string as save's error
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:175-216 -- never refreshes the list) ----
  toggleStatus(row: CardTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy, so
    // the visual change happens immediately regardless of the response
    // either way. See class doc comment: no reload on either branch.
    this.fullList = this.fullList.map((r) => (r.cardTypeId === row.cardTypeId ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.loader.start();
    this.api
      .updateStatus({
        cardTypeId: row.cardTypeId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        orgnisationId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Card Type Updated Successfully...!!'); // ctrl:198-204, verbatim (capital "Updated" -- differs from save/update's lowercase, not a typo introduced here)
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:206-213, verbatim
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:101-103/176-178/220-222. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
