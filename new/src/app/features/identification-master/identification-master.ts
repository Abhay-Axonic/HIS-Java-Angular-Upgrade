import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { IdentificationMasterService } from './identification-master.service';
import { emptyIdentificationForm, IdentificationForm, IdentificationRow, YN } from './identification-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/GlobalMaster/identificationMaster.html`
 * (227 lines) + `resources/assets/js/controller/adt/global/identificationMasterController.js`
 * (454 lines, `identificationMasterController`, no `controllerAs`). This is
 * the 5th and final screen of Phase 4 batch 15 (this project's first
 * clinical/billing-tier rebuild batch), built as its own follow-up after
 * the other 4 (cardTypeMaster, deliveryTypeMaster, encounterTypeMaster,
 * consentTypeMaster) were already merged.
 *
 * ================================================================
 * Real moduleId gate -- CONFIRMED, this screen is NOT gate-less
 * ================================================================
 * State `identificationMaster` (app.js:3536-3562) has a real `resolve`
 * block gating module id `388` (`arr.length <= 0 -> $state.go("accessDenied")`)
 * -- unlike this same batch's `cardTypeMaster` (self-registered state, no
 * resolve block at all, genuinely gate-less). Wired via `moduleAccessGuard`
 * + `data: { moduleId: 388 }` in `app.routes.ts`, not invented. The sidebar
 * entry is also real and reachable (`views/common/new-sidebar-16-10.html:623`,
 * `new-side-bar.html:3377`, both `ng-if="checkAccess(388)"`) -- unlike this
 * batch's `deliveryTypeMaster`, which deliberately has NO reachable menu
 * entry in legacy.
 *
 * ================================================================
 * Disclosed, approved improvement re the create-flow flag-reset bug
 * ================================================================
 * See `identification-master.models.ts`'s `IdentificationSavePayload` doc
 * comment and `docs/DECISION_NEEDED_IdentificationMasterCreateFlagReset.md`.
 * This component does a plain, straightforward create -- it submits
 * whatever the user checked for Expiry Required/Numerical/Alphanumeric and
 * trusts the (separately fixed) backend to persist it as submitted. No
 * special-case logic here discards or re-derives those three flags on
 * create.
 *
 * ================================================================
 * Mutually-exclusive Numerical/Alphanumeric rule -- faithfully reproduced
 * ================================================================
 * `data.isAlphanumeric == 'Y' && data.isNumerical == 'Y'` blocks the actual
 * submit with a warning toast (ctrl:249-253/410-414) -- but note WHERE this
 * check lives: it only runs right before the POST body is built (inside
 * `saveIdentification`'s `else` branch, i.e. after the confirm-modal's Ok
 * is clicked, not on the very first Save click that just opens the modal).
 * Reproduced identically below: `doSave()` (not `save()`) does this check.
 * Neither checked = no validation at all, silently allowed -- also
 * reproduced as-is (docs/ANGULAR_PENDING_FINDINGS.md entry 149).
 *
 * ================================================================
 * "Length" checkbox -- reveals a Defined Length number input, UI-only
 * ================================================================
 * View (identificationMaster.html:44-48): checking "Length" (`ng-model`
 * bound to `isLength`, `ng-true-value/ng-false-value` 'Y'/'N') reveals
 * `<input type="number" id="length" ng-if="identification.isLength=='Y'">`
 * bound to `definedLength`. The static `checked="checked"` attribute on
 * that checkbox in the raw HTML is an inert leftover -- AngularJS's
 * `ng-model` (initialized `'N'` in `init()`, ctrl:54-60) wins the initial
 * digest, so the real default is UNCHECKED/hidden, not checked. If
 * `isLength=='Y'` and the length value is empty, both save and update are
 * blocked client-side with "Please enter the Length" (ctrl:214-218/388-392)
 * -- checked BEFORE the confirm-modal gate on save (i.e. on every call to
 * `save()`, not just the confirmed one), and before the required-fields
 * check on update. See `save()`/`update()` below.
 *
 * ================================================================
 * Duplicate-name check -- confirmed CORRECT, no case-insensitivity bug here
 * ================================================================
 * Unlike this batch's `encounterTypeMaster` sibling (which has a real HQL
 * operator-precedence bug: `WHERE LOWER(name)=:x OR name=:x AND id=:excludeId`
 * parses as `LOWER(name)=:x OR (name=:x AND id=:excludeId)`, so the
 * id-exclusion only applies to the redundant second branch), this screen's
 * `GET_IDENTIFICATION_LIST_BY_NAME_NOT_ID` named query is correctly
 * parenthesized: `id.identificationId !=:identificationId AND
 * (id.identificationName =:identificationName OR LOWER(id.identificationName)=:identificationName)
 * AND id.organizationId=:orgId` -- the id-exclusion applies to the whole OR
 * clause. Combined with the DAO always passing `.toLowerCase()` for the
 * bound parameter, the `LOWER(...)=:identificationName` branch alone
 * already makes this genuinely case-insensitive. No client-side
 * pre-duplicate-check is added here; the real POST response drives the
 * "already exits" toast exactly as legacy does.
 *
 * ================================================================
 * FLAGGED, NOT silently fixed: a real "stuck confirm-modal" bug, same
 * shape as this batch's cardTypeMaster (which the owner already chose to
 * fix, 2026-07-23 Step 6 review) -- found independently while re-reading
 * this controller, NOT called out in the batch-15 Step 0/1 audit
 * (docs/ANGULAR_PENDING_FINDINGS.md entry 149 doesn't mention it)
 * ================================================================
 * `saveIdentification()`'s (ctrl:201-293) confirm-modal gate (`popUpFlag`)
 * is only ever reset back to `true` inside `init()` (ctrl:28-60), which is
 * called on page load AND inside this same function's OWN success branch
 * (ctrl:273) -- there is NO reset anywhere in the error branch
 * (ctrl:275-282, the duplicate-name case). Net effect: after any FAILED
 * save, `popUpFlag` stays permanently `false`, so every subsequent Save
 * click silently skips the confirm modal and fires the POST immediately,
 * until the page reloads. This is faithfully REPRODUCED here (not fixed)
 * pending an explicit owner decision for THIS screen -- `popupArmed` below
 * is only re-armed by a successful save (`resetForm()`), mirroring
 * `init()`'s real call sites exactly, NOT also re-armed on failure the way
 * `cardTypeMaster`'s was. Whoever reviews this should decide whether to
 * bring the same fix over for consistency, or leave this one alone.
 *
 * ================================================================
 * Other legacy quirks preserved verbatim
 * ================================================================
 * - Save/update error toast is the standard batch-wide "exits" typo:
 *   "Identification already exits...!!" (ctrl:277-281/436-441) -- same
 *   hardcoded string on ANY non-success response, not derived from
 *   `response.message`.
 * - Both save's and update's success AND failure branches unconditionally
 *   reload the list (ctrl:283-288/444-449 both call
 *   `initIdentificationMasterList(...)` after the if/else) -- reproduced
 *   via `loadList()` always running.
 * - `showUpdateBtn` flips Save/Update-button visibility IMMEDIATELY on
 *   click, before the GET even resolves (ctrl:297-299) -- `saveMode` is set
 *   `false` before `getById()` is called, not after. Its failure branch
 *   does a bare `alert("Error!!")` (ctrl:326-328), reproduced verbatim via
 *   `window.alert`.
 * - Status toggle never rolls back its visual state on failure (checkbox
 *   is two-way-bound directly to `info.status` in the legacy view,
 *   ctrl:335-374) -- reproduced via an optimistic local patch, never
 *   reverted, and the list is never reloaded on either branch.
 * - Search (`identificationList|filter:searchKeyword`, view:172) filters
 *   only the CURRENT server-fetched page (real server-side offset
 *   pagination + a separate count endpoint, same shape as this batch's
 *   `encounterTypeMaster` -- NOT this codebase's `client-pagination.util`,
 *   which is for screens that fetch the full list in one shot and paginate
 *   client-side; this screen's own controller genuinely does real
 *   offset/count round-trips, ctrl:89-138/173-186, so it's modeled the
 *   same way encounterTypeMaster is). Narrowed to code+name substring
 *   match below, matching this codebase's established simplification of
 *   AngularJS's generic (all-fields) `filter:searchKeyword` for this exact
 *   quirk elsewhere in the batch, not a new decision made here.
 */
@Component({
  selector: 'app-identification-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './identification-master.html',
  styleUrl: './identification-master.scss',
})
export class IdentificationMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(IdentificationMasterService);

  private orgId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.identification + saveBtnFlag/updateBtnFlag) ----
  form = signal<IdentificationForm>(emptyIdentificationForm());
  saveMode = signal(true); // true = saveBtnFlag false/updateBtnFlag true (create), false = edit
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag; see class doc's flagged "stuck modal" bug

  // ---- list + real server-side offset pagination (mirrors
  // initIdentificationMasterList/PagerService, ctrl:89-186) ----
  list = signal<IdentificationRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved verbatim: search filters the CURRENT PAGE's
  // already-fetched rows only -- see class doc.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.identificationCode?.toLowerCase().includes(keyword) || r.identificationName?.toLowerCase().includes(keyword),
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

  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.orgId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.orgId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  updateFormField<K extends keyof IdentificationForm>(field: K, value: IdentificationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  toggleFlag(field: 'isExpiryRequired' | 'isNumerical' | 'isAlphanumeric', checked: boolean): void {
    this.updateFormField(field, (checked ? 'Y' : 'N') as YN);
  }

  // `isLength` never clears `definedLength` when unchecked, matching legacy
  // (ng-if only hides the input, ng-model keeps whatever value was last
  // typed -- there is no explicit reset anywhere in the controller).
  toggleLength(checked: boolean): void {
    this.updateFormField('isLength', (checked ? 'Y' : 'N') as YN);
  }

  // Mirrors the 2 HTML5 `required` fields (Code/Identification Card,
  // view:16-19,29-33) that back `$scope.identificationform.$valid`.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!((f.identificationCode ?? '').trim() && (f.identificationName ?? '').trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().identificationCode ?? '').trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !(this.form().identificationName ?? '').trim();
  }

  // Mirrors the "Please enter the Length" gate (ctrl:209-218/383-392) --
  // runs on EVERY save() call (both the click that just opens the confirm
  // modal and the one PopupOkBtn triggers), and on every update() call.
  private isLengthMissing(): boolean {
    const f = this.form();
    return f.isLength === 'Y' && (f.definedLength == null || (f.definedLength as unknown as string) === '');
  }

  // ---- save / confirm-modal (mirrors saveIdentification/PopupOkBtn, ctrl:201-293) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (this.isLengthMissing()) {
      this.notify.warning('Please enter the Length'); // ctrl:215, verbatim
      return;
    }
    if (!this.requiredFieldsValid()) return; // mirrors the identificationform.$valid gate
    if (this.popupArmed()) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
    } else {
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
    // Mutually-exclusive rule -- checked here, right before the POST body
    // is built, matching ctrl:249-253 exactly (NOT in save()'s earlier
    // gate). Neither checked = allowed, no validation at all (entry 149).
    if (f.isAlphanumeric === 'Y' && f.isNumerical === 'Y') {
      this.notify.warning('Please mark either Numeric or Alphanumeric.'); // ctrl:250, verbatim
      return;
    }
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        identificationCode: f.identificationCode,
        identificationName: f.identificationName,
        isAlphanumeric: f.isAlphanumeric,
        isNumerical: f.isNumerical,
        definedLength: f.definedLength,
        isExpiryRequired: f.isExpiryRequired, // straightforward create -- see class doc, no reset workaround
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.orgId,
        updatedDate: now,
        createdDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Identification Added Successfully...!!'); // ctrl:264-269, verbatim
            this.resetForm(); // ctrl:273 -- init() runs on success, re-arms popupArmed too
          } else {
            this.notify.error('Identification already exits...!!'); // ctrl:276-281, verbatim typo -- see class doc's flagged "stuck modal" bug: NOT re-armed here
          }
          this.loadList(); // ctrl:283-287 -- reloads unconditionally, success AND failure
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyIdentificationForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag = true
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:296-332) ----
  edit(identificationId: number): void {
    // ctrl:297-299 -- flips saveBtnFlag/updateBtnFlag to "Update" mode
    // IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.loader.start();
    this.api.getById(identificationId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:326-328, verbatim
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          identificationId: d.identificationId,
          identificationCode: d.identificationCode,
          identificationName: d.identificationName,
          isAlphanumeric: d.isAlphanumeric,
          isNumerical: d.isNumerical,
          definedLength: d.definedLength,
          // ctrl:321-324 -- isLength is INFERRED from definedLength being
          // non-null, never read from a server field (there isn't one).
          isLength: d.definedLength != null ? 'Y' : 'N',
          isExpiryRequired: d.isExpiryRequired,
        });
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors updateIdentification, ctrl:377-452 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (this.isLengthMissing()) {
      this.notify.warning('Please enter the Length'); // ctrl:389, verbatim
      return;
    }
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    if (f.isAlphanumeric === 'Y' && f.isNumerical === 'Y') {
      this.notify.warning('Please mark either Numeric or Alphanumeric.'); // ctrl:411, verbatim
      return;
    }
    this.loader.start();
    this.api
      .update({
        identificationId: f.identificationId as number,
        identificationCode: f.identificationCode,
        identificationName: f.identificationName,
        isAlphanumeric: f.isAlphanumeric,
        isNumerical: f.isNumerical,
        definedLength: f.definedLength,
        isExpiryRequired: f.isExpiryRequired,
        status: 'A', // ctrl:404 -- hardcoded, dead server-side (see models.ts doc comment)
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.orgId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Identification Updated Successfully...!!'); // ctrl:423-428, verbatim
            // ctrl:429-432 -- init() genuinely runs on a successful update in
            // this controller too, resetting back to Save/create mode.
            this.resetForm();
          } else {
            this.notify.error('Identification already exits...!!'); // ctrl:435-441, verbatim, same string as save's error
          }
          this.loadList(); // ctrl:444-448 -- reloads unconditionally, success AND failure
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:335-374 -- never refreshes the list) ----
  toggleStatus(row: IdentificationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy, so
    // the visual change happens immediately regardless of the response
    // either way. See class doc: no reload on either branch.
    this.list.update((rows) => rows.map((r) => (r.identificationId === row.identificationId ? { ...r, status: nextStatus } : r)));

    this.loader.start();
    this.api
      .updateStatus({
        identificationId: row.identificationId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Identification Updated Successfully...!!'); // ctrl:356-362, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:364-371, verbatim, hardcoded (not response.message)
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:203-205/337-339/379-381. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
