import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MortuaryBedMasterService } from './mortuary-bed-master.service';
import { emptyMortuaryBedForm, MortuaryBedForm, MortuaryBedRow } from './mortuary-bed-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/UnitMaster/Mortuary/Mortuary-Bed-Master.html`
 * (177 lines) + `resources/assets/js/controller/master/unit/mortuaryBedMasterController.js`
 * (`mortuaryBedMasterController`, no `controllerAs`, 389 lines, read in
 * full). This is the MASTER/admin screen ONLY -- registers a physical
 * mortuary bed/slot (code + description; the bed *number* is server-
 * generated, not user-entered, see models.ts doc comment). The separate
 * "allotment" workflow (`mortuaryBedManagementController.js`, assigning a
 * deceased patient to a bed) is a DIFFERENT screen/controller and is
 * entirely out of scope here -- not built, not touched, not linked to.
 *
 * ================================================================
 * Real moduleId gate -- CONFIRMED, this screen is NOT gate-less
 * ================================================================
 * State `mortuaryBedMaster` (app.js:10951-10974) has a real `resolve` block
 * gating module id `649` (`arr.length <= 0 -> $state.go("accessDenied")`).
 * Wired via `moduleAccessGuard` + `data: { moduleId: 649 }` in
 * `app.routes.ts`. The sidebar entry is also real and reachable
 * (`views/common/new-side-bar.html:4665`, `new-sidebar-16-10.html:2186`,
 * both `ng-if="checkAccess(649)"`, plus a non-gated copy in the older
 * `side-bar.html:1050`) -- wired into `shell-nav.data.ts` under "Ward/Unit
 * Master" (same section as this codebase's floor-master/wing-master
 * siblings, matching legacy's own module taxonomy).
 *
 * ================================================================
 * Wire fields confirmed directly against source (ctrl:203-213/345-351)
 * ================================================================
 * Only 2 of `MortuaryBedMaster.java`'s 3 code/desc/number columns are real
 * editable form fields: `mortuaryBedCode`/`mortuaryBedDesc`. The 3rd,
 * `mortuaryBedNumber`, is NEVER sent by the frontend -- it's generated
 * server-side (`MortuaryBedMasterServiceImpl.generateBedNumber()`, a random
 * `MBED00XXXX` string, service:90-102) on every save, and only ever comes
 * back for display in the list table (view:109). See
 * mortuary-bed-master.models.ts for the full field-by-field breakdown.
 *
 * ================================================================
 * Empty catch block in the DAO -- confirmed, faithfully NOT reproduced
 * with real error handling (there's nothing to "faithfully reproduce" on
 * the frontend, noted here for the audit trail)
 * ================================================================
 * `MortuaryBedMasterDaoImpl.saveMortuaryBeds` (dao:44-60) has a fully empty
 * `catch(HibernateException he){}` AND a fully empty `catch(Exception e){}`
 * -- any persistence failure (e.g. a DB constraint violation) is silently
 * swallowed and the method falls through to `return new Response(ERROR, ...)`
 * with no message/detail at all. Every other DAO/service/controller method
 * in this chain has the same shape (empty catches throughout `saveMortuaryBeds`,
 * `getMortuaryBedList`, `updateMortuaryBedMaster`, etc.) -- this is this
 * codebase's ordinary, already-known "swallowed exception" pattern (ADR:
 * legacy's own `try/catch(Exception e){}` around nearly every DAO method),
 * not a new severity-1 finding. This rebuild just surfaces whatever
 * `status`/`message` the Response envelope actually carries (see `doSave`'s
 * generic error branch below) -- it does not, and cannot, recover
 * information legacy itself throws away.
 *
 * ================================================================
 * Two-click confirm flow on Save ONLY -- FIXED, diverges from legacy
 * (codebase-wide sweep + owner decision, 2026-07-24, see
 * docs/ANGULAR_PENDING_FINDINGS.md entry 152), same treatment as
 * cardTypeMaster/identificationMaster/8 other already-merged screens
 * ================================================================
 * `saveMortuaryBed()` (ctrl:195-257) is gated by `$scope.popUpFlag` (`init()`
 * sets it `true`, ctrl:63): the FIRST click, with `popUpFlag` true, just
 * toggles `showModal` open (ctrl:216-218) -- no API call yet. The modal's Ok
 * button (`PopupOkBtn`, ctrl:190-193) sets `popUpFlag = false` and calls
 * `saveMortuaryBed()` again, which this time (else-branch, ctrl:219-254)
 * fires the actual POST. Reading `popUpFlag`/`showModal`/`mortuaryObj` as
 * only ever reset back by `init()` (ctrl:28-83, called on page load and
 * inside `saveMortuaryBed`'s OWN success branch, ctrl:241) -- with no reset
 * in the error branch (ctrl:242-250, the "already exists" case) -- suggests
 * a FAILED save would leave `popUpFlag` stuck permanently `false`, skipping
 * the confirm modal on every subsequent Save click. Confirmed live against
 * real, unmodified legacy (duplicate-then-retry on `cardTypeMaster`/
 * `raceMaster`) that this does NOT happen -- the shared `<modal>`
 * directive's own hardcoded Ok-button handler re-arms the gate on every
 * click, success or failure alike. `popupArmed` below is now re-armed on
 * both branches to match.
 *
 * ================================================================
 * Other legacy quirks preserved verbatim
 * ================================================================
 * - Save's error toast: "Already exits...!!" (ctrl:244-249, verbatim typo).
 *   Update's error toast is the SAME string (ctrl:376-381).
 * - Both save's and update's success AND failure branches unconditionally
 *   reload the list afterward (ctrl:251/382, both OUTSIDE the if/else) --
 *   reproduced via `loadList()` always running in `doSave()`/`update()`.
 * - `showUpdateBtn` (ctrl:259-288) flips into update mode (`saveBtnFlag =
 *   true`/`updateBtnFlag = false`) synchronously, BEFORE the
 *   `getMortuaryBedListById` call resolves, and uses a raw `alert("Error!!")`
 *   on a failed response (ctrl:282-285) -- the only place in this controller
 *   that doesn't use `growl`. Both reproduced verbatim (`saveMode.set(false)`
 *   up front; `window.alert` on failure).
 * - `updateStatus` (ctrl:296-336) never reloads the list on either branch --
 *   only a growl toast either way, with no revert of the already-flipped
 *   checkbox on failure (the view's checkbox is two-way bound directly to
 *   `info.status`, view:148). Reproduced via an optimistic local patch, no
 *   `loadList()` call on either branch.
 * - Search (`mortuaryBedList|filter:searchKeyword`, view:142) filters
 *   whatever page is ALREADY loaded (real server-side offset/count
 *   pagination, ctrl:91-138/146-168, same shape as this codebase's
 *   floor-master/identification-master) -- narrowed here to a code/desc/
 *   number substring match, the same established simplification of
 *   AngularJS's generic (all-fields) `filter:searchKeyword` used elsewhere
 *   in this codebase, not a new decision made here.
 * - `saveMortuaryBeds`'s backend chain sets `bedStatusId = 6` twice
 *   (redundantly, controller:27/31) and calls a second, separate
 *   `saveMortuaryBedLogStatus` insert server-side after a successful save
 *   (controller:28-33) -- entirely internal to the backend's own POST
 *   handler for `/adt/saveMortuaryBeds`; the frontend only ever sees one
 *   POST/one Response either way, so nothing extra is modeled here.
 */
@Component({
  selector: 'app-mortuary-bed-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './mortuary-bed-master.html',
  styleUrl: './mortuary-bed-master.scss',
})
export class MortuaryBedMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(MortuaryBedMasterService);

  private orgId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.mortuaryObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<MortuaryBedForm>(emptyMortuaryBedForm());
  saveMode = signal(true); // true = saveBtnFlag false/updateBtnFlag true (create), false = edit
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag; stays false forever after a failed save -- see class doc bug, NOT fixed here

  // ---- list + real server-side offset pagination (mirrors
  // initMortuaryBedMasterList/getMortuaryBedMasterList/PagerService, ctrl:91-186) ----
  list = signal<MortuaryBedRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE's already-fetched
  // rows only -- see class doc comment.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.mortuaryBedCode?.toLowerCase().includes(keyword) ||
        r.mortuaryBedDesc?.toLowerCase().includes(keyword) ||
        r.mortuaryBedNumber?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.orgId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.orgId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.orgId, this.unitId).subscribe({
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

  updateFormField<K extends keyof MortuaryBedForm>(field: K, value: MortuaryBedForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 2 HTML5 `required` fields (Code/Description,
  // view:16-19/31-34) that back `$scope.mortuaryBedform.$valid`. No manual
  // JS validation beyond this in legacy.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.mortuaryBedCode.trim() && f.mortuaryBedDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().mortuaryBedCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().mortuaryBedDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveMortuaryBed/PopupOkBtn, ctrl:190-257) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the mortuaryBedform.$valid gate
    if (this.popupArmed()) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
    } else {
      // Guards only the narrow window while a confirmed save is still
      // in-flight -- see class doc comment (fixed, diverges from legacy).
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
        mortuaryBedCode: f.mortuaryBedCode,
        mortuaryBedDesc: f.mortuaryBedDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.orgId,
        createdDate: now,
        updatedDate: now,
        unitId: this.unitId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Added Successfully...!!'); // ctrl:233-238, verbatim
            this.resetForm(); // ctrl:241 -- init() runs on success, re-arms popupArmed too
          } else {
            this.notify.error('Already exits...!!'); // ctrl:244-249, verbatim typo
            // Fixed, diverges from legacy (codebase-wide sweep + owner
            // decision, 2026-07-24, see docs/ANGULAR_PENDING_FINDINGS.md
            // entry 152): re-arm the confirm gate on failure too. Legacy's
            // ctrl:242-250 has no reset here when read in isolation, but
            // the shared <modal> directive's own hardcoded Ok-button
            // handler re-arms popUpFlag on every click regardless of
            // outcome, confirmed live against real, unmodified legacy
            // (duplicate-then-retry on cardTypeMaster/raceMaster).
            this.popupArmed.set(true);
          }
          this.loadList(); // ctrl:251 -- reloads unconditionally, success AND failure
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyMortuaryBedForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag = true
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:259-288) ----
  edit(mortuaryBedId: number): void {
    // ctrl:261-262 -- flips saveBtnFlag/updateBtnFlag to "Update" mode
    // IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.loader.start();
    this.api.getById(mortuaryBedId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:282-285, verbatim, raw alert not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          mortuaryBedId: d.mortuaryBedId,
          mortuaryBedCode: d.mortuaryBedCode,
          mortuaryBedDesc: d.mortuaryBedDesc,
        });
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors updateMortuaryBed, ctrl:338-386 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the mortuaryBedform.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        mortuaryBedCode: f.mortuaryBedCode,
        mortuaryBedDesc: f.mortuaryBedDesc,
        mortuaryBedId: f.mortuaryBedId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Updated Successfully...!!'); // ctrl:364-369, verbatim
            // ctrl:372-373 -- init() genuinely runs on a successful update in
            // this controller too, resetting fully back to create mode.
            this.resetForm();
          } else {
            this.notify.error('Already exits...!!'); // ctrl:376-381, verbatim, same string as save's error
          }
          this.loadList(); // ctrl:382 -- reloads unconditionally, success AND failure
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:296-336 -- never refreshes the list) ----
  toggleStatus(row: MortuaryBedRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy, so
    // the visual change happens immediately regardless of the response
    // either way. See class doc comment: no reload on either branch.
    this.list.update((rows) => rows.map((r) => (r.mortuaryBedId === row.mortuaryBedId ? { ...r, status: nextStatus } : r)));

    this.loader.start();
    this.api
      .updateStatus({
        mortuaryBedId: row.mortuaryBedId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Updated Successfully...!!'); // ctrl:318-323, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:326-333, verbatim
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:197-199/298-300/340-342. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
