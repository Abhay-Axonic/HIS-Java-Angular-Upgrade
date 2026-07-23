import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DeliveryTypeMasterService } from './delivery-type-master.service';
import { DeliveryTypeForm, DeliveryTypeRow, emptyDeliveryTypeForm } from './delivery-type-master.models';
import {
  PaginationState,
  createPaginationState,
  searchData,
  setTotalPages,
  setFirstPage,
  setLastPage,
  setNextPage,
  setPage,
  setPreviousPage,
  getPagesList,
} from '../../core/client-pagination.util';

/**
 * Parity rebuild of views/master/GlobalMaster/delivery-type-master.html (148
 * lines) + resources/assets/js/controller/adt/global/
 * deliveryTypeMasterController.js (238 lines, `deliveryTypeMasterController`,
 * no `controllerAs`). Legacy state `deliveryTypeMaster` (app.js:3894-3915,
 * resolve block confirmed live directly, not guessed), moduleId 506 --
 * handled entirely by `moduleAccessGuard` at the route level, nothing to
 * check in this component itself.
 *
 * Confirmed PURE-MASTER, no safety/billing dimension (see docs/
 * ANGULAR_REBUILD_PLAYBOOK.md's Clinical/Billing-Tier Addendum) -- this
 * screen only manages a lookup list of delivery types (e.g. Normal,
 * C-Section), with no downstream calculation or billing effect.
 *
 * =====================================================================
 * TWO CONFIRMED, OWNER-APPROVED BUG FIXES (both unambiguous, narrowly
 * scoped, recoverable from existing code -- everything else below is
 * faithfully reproduced, not a general clean-up pass)
 * =====================================================================
 *
 * **FIX #1 -- list verb mismatch (GET -> POST), plus missing organizationId.**
 * Legacy's own frontend calls `GenericService.serviceAction("GET", ...)`
 * (ctrl:52-53) against `/adt/getDeliveryTypeMaster` with an empty body
 * (ctrl:48, `var data = {};`) -- but the real Java controller
 * (`DeliveryTypeMasterController.java:35`, `@RequestMapping(value=
 * "/getDeliveryTypeMaster", method = RequestMethod.POST)`) is POST-only, so
 * legacy's own list call returns a genuine HTTP 405 today, confirmed
 * directly from the controller source. The DAO's named query also filters
 * on `organizationId` (`DeliveryTypeMasterDaoImpl.getDeliveryTypeMasterList`,
 * `.setParameter("orgId", ...)`) -- with none supplied, Hibernate binds
 * `null` and the DAO's `setFirstResult(0)/setMaxResults(10)` fallback
 * silently returns up to 10 rows from ANY organization. `loadList()` below
 * uses POST and always sends `organizationId` -- see
 * `delivery-type-master.service.ts` for the endpoint-level detail.
 *
 * **FIX #2 -- restored a real Update flow.** Legacy's live template is
 * missing its Update button entirely -- only Angular's own
 * `<!-- ngIf: updateBtnFlag -->` runtime comment remains where it used to
 * render (view:88); the correct original markup calling
 * `updateDeliveryType()` still exists, commented out, a few lines above
 * (view:60-62). Worse, legacy's own flag pair is inverted at init
 * (`saveBtnFlag = false`/`updateBtnFlag = true`, ctrl:39-40) while the
 * visible Save link requires `ng-if="saveBtnFlag"` (view:85) -- so on a
 * fresh page load NEITHER button is visible; only after clicking the pencil
 * icon once (`showUpdateBtn` flips `saveBtnFlag = true`, ctrl:129) does the
 * (mislabeled) Save link appear, which would re-POST to CREATE, not update.
 * This rebuild replaces that broken flag pair with a standard `updateMode`
 * signal (Save visible while `!updateMode()`, Update visible while
 * `updateMode()` -- same shape as this codebase's `district-master`/
 * `ward-store-mapper`), wired to a real `update()` ->
 * `/adt/updateDeliveryTypeMaster` call: a real, tested, previously
 * unreachable backend endpoint (confirmed via
 * `DeliveryTypeMasterController.java:46-55`).
 *
 * =====================================================================
 * Previously-DEAD search/page-size controls -- restored for real, NOT
 * reproduced as decorative (deliberate rebuild decision, same spirit as
 * the two fixes above)
 * =====================================================================
 * Legacy's "Show N entries" `<select>` (view:72-79,
 * `ng-change="setNoOfRecords()"`) and the search `<input
 * ng-model="searchKeyword">` (view:91) are both confirmed DEAD in legacy
 * today -- `setNoOfRecords`/`searchKeyword` do not exist anywhere in the
 * 238-line controller (full-file read). Per the same "restore the
 * obviously-intended-but-never-wired UI" call as the two bugs above, both
 * are wired up for real here via this codebase's shared
 * `client-pagination.util` convention (matching `ward-store-mapper`/
 * `pharmacy-formulation-type-master`): a real page-size control
 * (`onPageSizeChange()`) and a real full-list substring search
 * (`onSearchChange()`, `p.search` + `searchData()`) -- not a copy of
 * legacy's inert markup.
 *
 * =====================================================================
 * List-fetch shape: no legacy pagination UI to match, so this fetches
 * everything once and paginates client-side (ward-store-mapper precedent)
 * =====================================================================
 * There is no server-side or client-side pagination UI in legacy beyond a
 * plain `ng-repeat` over whatever the (broken) list call returns. Now that
 * the request is well-formed (FIX #1), `loadList()` below first calls the
 * real (legacy-frontend-unused, but genuinely live) `/adt/getDeliveryTypeCount`
 * endpoint (`DeliveryTypeMasterController.java:89-98`) to size a single
 * `noOfRecordsPerPage` request big enough to fetch the full list in one
 * shot, then hands the result to `searchData`/`setPage`/etc. for all
 * subsequent client-side paging/searching.
 *
 * =====================================================================
 * NOT fixed -- out of scope, faithfully reproduced (only the two bugs
 * above were owner-approved for a real fix)
 * =====================================================================
 * - **The "exits" typo, left uncorrected.** Both create and update's
 *   duplicate-name error growl verbatim reproduce legacy's real, live copy:
 *   "Delivery type already exits...!!" (ctrl:113-118, ctrl:226-231) -- a
 *   real user-facing typo ("exits" instead of "exists"). General copy is
 *   explicitly out of scope for this batch.
 * - **Duplicate-name check is real, server-side, case-insensitive** --
 *   `DeliveryTypeMasterServiceImpl.addDeliveryTypeMaster`/
 *   `updateDeliveryTypeMaster` call `getDeliveryTypeByName`/
 *   `getDeliveryTypeByNameNotId`, both lower-casing the compared name
 *   server-side (`DeliveryTypeMasterDaoImpl`, `GET_DELIVERY_TYPE_LIST_BY_NAME*`
 *   named queries). No client-side duplicate check is added here; the
 *   server's own `ALREADY_EXIST` response is what the "exits" toast reflects.
 * - **Confirm modal does not auto-close on a failed Save** -- a real,
 *   faithfully-reproduced quirk of the shared legacy `<modal>` directive
 *   (`commonPopUpDirectives.js`). `PopupOkBtn()` (ctrl:73-76) sets
 *   `popUpFlag = false` and re-invokes `saveDeliveryType()`, which now takes
 *   the real POST branch (ctrl:90-122); only the SUCCESS path calls
 *   `$scope.init()` (ctrl:110-111), which is what resets `popUpFlag = true`
 *   and `showModal = false` (ctrl:43-46) and closes the modal via the
 *   directive's own `$watch` (`commonPopUpDirectives.js:38-48`). The ERROR
 *   branch (ctrl:112-119) touches neither flag, so on a failed Save the
 *   modal stays open with the error toast alongside it, dismissed only by
 *   the modal's own Cancel/X (which re-arms the same way). Reproduced below:
 *   `confirmSave()` only closes/resets on the SUCCESS branch; the error
 *   branch leaves `showConfirmModal` untouched.
 * - **Update has no confirm modal**, matching this batch's other siblings
 *   (Save confirms, Update does not) -- ctrl:199-236 posts immediately.
 * - **Status toggle has no list-refresh call after success, and no revert
 *   on error**, matching legacy (`updateStatus`, ctrl:164-197, only growls)
 *   -- reproduced via `toggleStatus()` below.
 * - **Edit's failure path is a raw `window.alert`**, not a growl toast
 *   (ctrl:150-152, `alert("Error!!")`), matching this codebase's
 *   `district-master` precedent for the identical legacy pattern.
 * - **Faithful copy-paste bug in the Description field's help text**
 *   (view:34): legacy's `<p>` under the Description input checks
 *   `deliverytypeform.deliveryCode.$error.required` -- the CODE field's own
 *   validity -- not `deliveryName.$error.required`. So "Description is
 *   required" only ever appears when the CODE field is empty, never in
 *   response to the Description field's own state. Reproduced verbatim via
 *   `isNameHelpTextVisible()` below, which mirrors `isCodeInvalid()`.
 */
@Component({
  selector: 'app-delivery-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './delivery-type-master.html',
  styleUrl: './delivery-type-master.scss',
})
export class DeliveryTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(DeliveryTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form/edit-mode state (mirrors $scope.deliverytype + the restored,
  // sane updateMode toggle -- see class doc comment FIX #2) ----
  form = signal<DeliveryTypeForm>(emptyDeliveryTypeForm());
  updateMode = signal(false);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- full list + entirely client-side pagination/search (see class doc
  // comment -- restored, real, matching ward-store-mapper's convention) ----
  private fullList: DeliveryTypeRow[] = [];
  pagination = signal<PaginationState<DeliveryTypeRow>>(createPaginationState<DeliveryTypeRow>());

  constructor() {
    this.loadList();
  }

  // mirrors init()'s list fetch (ctrl:48-64), FIXED per class doc comment
  // FIX #1: real POST, real organizationId, and sized via the real (if
  // legacy-frontend-unused) count endpoint to fetch everything in one shot.
  loadList(): void {
    this.loader.start();
    this.api.getDeliveryTypeCount(this.organizationId).subscribe({
      next: (countRes) => {
        const total = countRes.object ?? 0;
        this.api.getDeliveryTypeMasterList(this.organizationId, 0, total > 0 ? total : 10).subscribe({
          next: (listRes) => {
            this.loader.stop();
            this.fullList = listRes.listObject ?? [];
            const p = this.pagination();
            searchData(this.fullList, p);
            this.pagination.set({ ...p });
          },
          error: () => this.loader.stop(),
        });
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- restored real search + page-size controls (see class doc comment) ----
  onSearchChange(term: string): void {
    const p = this.pagination();
    p.search = term;
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  onPageSizeChange(): void {
    const p = this.pagination();
    setTotalPages(p);
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

  updateField<K extends keyof DeliveryTypeForm>(field: K, value: DeliveryTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors deliverytypeform.$valid -- both
  // fields carry `required`, view:17/32) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    // Guard against null/undefined, not just empty string -- found live on
    // the sibling cardTypeMaster screen, where real seed data has a null
    // code field and editing it crashed validation. Same defensive fix
    // applied here pre-emptively.
    return !!((f.deliveryTypeCode ?? '').trim() && (f.deliveryTypeName ?? '').trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().deliveryTypeCode ?? '').trim();
  }

  // Name field's own visual invalid state -- the `show-errors` directive's
  // `has-error` styling genuinely reflects each field's own control state in
  // legacy; only the help-text paragraph below has the copy-paste bug.
  isNameInvalid(): boolean {
    return this.formSubmitted() && !(this.form().deliveryTypeName ?? '').trim();
  }

  // Faithful copy-paste quirk, deliberately NOT fixed -- see class doc
  // comment. Legacy's Description-field help text checks the CODE field's
  // validity, not the Name field's, so this mirrors `isCodeInvalid()`.
  isNameHelpTextVisible(): boolean {
    return this.isCodeInvalid();
  }

  // ---- save / confirm-modal (mirrors saveDeliveryType/PopupOkBtn, ctrl:78-125) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:89 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ctrl:78-125 -- see class doc comment for why this deliberately does NOT
  // close the modal on error (only success does, matching legacy's real
  // observed behavior).
  confirmSave(): void {
    const f = this.form();
    this.api
      .saveDeliveryTypeMaster({
        deliveryTypeCode: f.deliveryTypeCode,
        deliveryTypeName: f.deliveryTypeName,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Delivery Type Added Successfully...!!'); // ctrl:104-109, verbatim
            this.showConfirmModal.set(false);
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error('Delivery type already exits...!!'); // ctrl:113-118, verbatim typo -- NOT fixed, see class doc comment
            // modal deliberately left open on error -- see class doc comment
          }
        },
      });
  }

  // ---- update (mirrors updateDeliveryType, ctrl:199-236 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:207 guard (no else branch)
    const f = this.form();
    this.api
      .updateDeliveryTypeMaster({
        deliveryTypeCode: f.deliveryTypeCode,
        deliveryTypeName: f.deliveryTypeName,
        deliveryTypeId: f.deliveryTypeId as number,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Delivery type updated Successfully...!!'); // ctrl:216-221, verbatim (note lowercase "type")
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error('Delivery type already exits...!!'); // ctrl:226-231, same verbatim typo
          }
        },
      });
  }

  // mirrors $scope.init() being re-invoked on a successful save/update
  // (ctrl:110-111, ctrl:223-224) -- resets the form and edit-mode back to
  // create-mode defaults. Only ever called from a SUCCESS branch.
  private resetForm(): void {
    this.formSubmitted.set(false);
    this.updateMode.set(false);
    this.form.set(emptyDeliveryTypeForm());
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:127-155) ----
  edit(row: DeliveryTypeRow): void {
    this.updateMode.set(true);
    this.formSubmitted.set(false);
    this.loader.start();
    this.api.getDeliveryTypeById(row.deliveryTypeId).subscribe({
      next: (res) => {
        this.loader.stop();
        const detail = res.listObject?.[0];
        if (res.status !== 'success' || !detail) {
          window.alert('Error!!'); // ctrl:150-152 -- a real native alert, not growl
          return;
        }
        this.form.set({
          deliveryTypeId: detail.deliveryTypeId,
          deliveryTypeCode: detail.deliveryTypeCode,
          deliveryTypeName: detail.deliveryTypeName,
        });
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:164-197 -- see class doc
  // comment: no list-refresh call and no revert-on-error, either outcome) ----
  toggleStatus(row: DeliveryTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.fullList = this.fullList.map((r) => (r.deliveryTypeId === row.deliveryTypeId ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.api
      .updateStatusForDeliveryType({ deliveryTypeId: row.deliveryTypeId, status: nextStatus, updatedBy: this.userId })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Delivery type Updated Successfully...!!'); // ctrl:181-187, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:190-195, verbatim
          }
        },
      });
  }
}
