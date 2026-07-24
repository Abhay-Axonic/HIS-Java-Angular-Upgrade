import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { NursingStationMasterService } from './nursing-station-master.service';
import {
  emptyNursingStationForm,
  NursingStationForm,
  NursingStationRow,
  StoreOption,
} from './nursing-station-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/nursing-station-master.html
 * (207 lines) + resources/assets/js/controller/adt/unit/
 * nursingStationMasterController.js (`nursingStationMasterController`, no
 * `controllerAs`, 384 lines, read in full). Master/admin screen for
 * "Nursing Station" (code + description + a Store FK) -- the SEPARATE
 * `nursingStationWardMapping` screen (mapping nursing stations to wards) is
 * a different state/controller and is entirely out of scope here.
 *
 * ================================================================
 * Real moduleId gate -- CONFIRMED, this screen is NOT gate-less
 * ================================================================
 * State `nursingStationMaster` (app.js:8575-8601) has a real `resolve`
 * block gating module id `547` (`arr.filter(item => item.id == 547)` ->
 * `accessDenied` on empty). Wired via `moduleAccessGuard` +
 * `data: { moduleId: 547 }` in `app.routes.ts`. Sidebar entry confirmed
 * real and reachable (`new-sidebar-16-10.html:1526`,
 * `ng-if="checkAccess(547)"`; `adminPriviledges.json` id 547, "Nursing
 * Station Master", parentId 541) -- wired into `shell-nav.data.ts` under
 * "Ward/Unit Master" (same section as this codebase's mortuary-bed-master
 * sibling, both living under `views/master/UnitMaster/...`).
 *
 * ================================================================
 * Server-side `updated_by` corruption on UPDATE -- KNOWN, NOT fixed here,
 * proceeding per owner's recorded decision
 * ================================================================
 * See docs/DECISION_NEEDED_NursingStationMasterUpdatedByCorruption.md and
 * NursingStationUpdatePayload's own doc comment (nursing-station-master
 * .models.ts). `NursingStationDaoImpl.updateNursingStationMaster` reads the
 * wrong DTO field (`getCreatedBy()` instead of `getUpdatedBy()`) when
 * persisting `updated_by`, so every edit via this screen silently records
 * `updated_by = 0` server-side. This is a purely server-side bug with NO
 * client-observable effect and NO client-side workaround -- `update()`
 * below sends the real, correct `updatedBy` value, exactly as legacy's own
 * frontend does. A one-line legacy fix is proposed on
 * `fix/nursingstationmaster-updatedby-corruption`, its own branch, held for
 * owner approval, NOT part of this Angular rebuild branch. Owner's decision
 * was to proceed with Step 2+ now, wiring to the endpoint exactly as it
 * exists today (see the decision doc, section 5).
 *
 * ================================================================
 * Two-click confirm flow on Save ONLY -- FIXED, diverges from legacy
 * (codebase-wide sweep + owner decision, 2026-07-24, see
 * docs/ANGULAR_PENDING_FINDINGS.md entry 152), same treatment as
 * cardTypeMaster/identificationMaster/currencydenomination/
 * incidentCategoryMaster/mortuaryBedMaster (8 total, this being the 9th)
 * ================================================================
 * `saveNursingStation()` (ctrl:206-265) is gated by `$scope.popUpFlag`
 * (`init()` sets it `true`, ctrl:63): the FIRST click, with `popUpFlag`
 * true, just toggles `showModal` open (ctrl:226-227) -- no API call yet.
 * The modal's Ok button (`PopupOkBtn`, ctrl:200-203) sets `popUpFlag =
 * false` and calls `saveNursingStation()` again, which this time
 * (else-branch, ctrl:229-262) fires the actual POST. Reading `popUpFlag` as
 * only ever reset back by `init()` (ctrl:29-79, called on page load and
 * inside `saveNursingStation`'s OWN success branch, ctrl:239) -- with no
 * reset in the error branch (ctrl:246-253) -- suggests a FAILED save would
 * leave `popUpFlag` stuck permanently `false`, skipping the confirm modal
 * on every subsequent Save click. Confirmed live against real, unmodified
 * legacy (duplicate-then-retry on cardTypeMaster/raceMaster, entry 152)
 * that this does NOT happen -- the shared `<modal>` directive
 * (`<modal visible="showModal"></modal>`, view:207) has its own hardcoded
 * Ok-button handler that re-arms the gate on every click, success or
 * failure alike. `popupArmed` below is now re-armed on both branches to
 * match.
 *
 * ================================================================
 * Server-side duplicate-DESCRIPTION check -- real business rule, found
 * live during Step 5 testing, NOT previously documented from the Step 0/1
 * controller/view read (the check lives entirely in the Java service
 * layer, invisible from the frontend controller alone)
 * ================================================================
 * `NursingStationServiceImpl.saveNursingStationMaster`
 * (legacy/Global/.../service/NursingStationServiceImpl.java:43-57) calls
 * `getNursingStationMasterByName` BEFORE the DAO save -- a real duplicate
 * check on `LOWER(nursingStationDesc)`, scoped to
 * `organizationId`+`unitId` (NursingStationMaster.java's
 * `GET_NURSING_STATION_MASTER_BY_NAME` named query) -- NOT on
 * `nursingStationCode`. A duplicate description (case-insensitive) returns
 * `{status:'error', message:'Already Exists.'}` (`ALREADY_EXIST`
 * constant), no server exception involved. `updateNursingStationMaster`
 * (service:81-94) has the equivalent check via
 * `getNursingStationMasterByNameNotById` (`GET_NURSING_STATIONMASTER_
 * BYNAME_NOTBYID`), correctly excluding the record's own id -- but that
 * query is scoped ONLY to `organizationId`, NOT `unitId` (confirmed
 * directly against the named query source, a real, narrow divergence from
 * the save-side check's scope) -- faithfully reproduced here since this is
 * purely a server-side query difference with no client-side action
 * required either way (the update payload never carried `unitId` to begin
 * with, see NursingStationUpdatePayload's doc comment). Both are handled
 * identically on this screen via the generic `res.status !== 'success'`
 * failure branch below -- `notify.error(res.message)` surfaces the real
 * "Already Exists." text verbatim, same as every other failure path.
 *
 * ================================================================
 * Other legacy quirks preserved verbatim
 * ================================================================
 * - Both save's and update's success AND failure branches unconditionally
 *   reload the list afterward (ctrl:255-259/374-378, both OUTSIDE the
 *   if/else) -- reproduced via `loadList()` always running in `doSave()`/
 *   `update()`.
 * - `showUpdateBtn` (ctrl:268-293) flips into update mode (`saveBtnFlag =
 *   false`/`updateBtnFlag = true`) synchronously, BEFORE the
 *   `getNursingStationMasterById` call resolves, and uses a raw
 *   `alert("Error!!")` on a failed response (ctrl:288-290) -- reproduced
 *   verbatim (`saveMode.set(false)` up front; `window.alert` on failure).
 * - `updateStatus` (ctrl:296-333) never reloads the list on either branch,
 *   but DOES call `$scope.init()` on SUCCESS (ctrl:317) -- which resets the
 *   create/edit form fields AND re-fetches the store dropdown, even though
 *   the toggle target is unrelated to whatever's currently in the form.
 *   Same "toggling status wipes the form" quirk as this codebase's
 *   currency-master. Reproduced via `resetForm()` (which re-fetches the
 *   store list) on the success branch only, no `loadList()` call either
 *   way.
 * - Search (`nursingStationList|filter:searchKeyword`, view:172) is REAL
 *   here (unlike currency-master's decorative search box) -- a genuine
 *   `ng-model="searchKeyword"` bound to Angular's built-in generic `filter`,
 *   applied CLIENT-SIDE to whatever page is already loaded (real
 *   server-side offset/count pagination, ctrl:89-192), same shape as this
 *   codebase's mortuary-bed-master/floor-master. Narrowed here to a
 *   code/desc/store-name substring match, the same established
 *   simplification of AngularJS's generic (all-fields) filter used
 *   elsewhere in this codebase.
 * - Store dropdown filters to `ng-if="info.status == true"` (only active
 *   stores shown, view:46) -- reproduced via a computed filter over
 *   `storeList()`.
 * - `init()` re-fetches the store dropdown on EVERY call (page load, save
 *   success, update success, status-toggle success) -- modeled by
 *   `resetForm()` calling `loadStoreList()` every time it runs, matching
 *   `init()`'s own unconditional re-fetch (ctrl:66-77).
 */
@Component({
  selector: 'app-nursing-station-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './nursing-station-master.html',
  styleUrl: './nursing-station-master.scss',
})
export class NursingStationMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(NursingStationMasterService);

  private orgId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.nursingMasterMasterObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<NursingStationForm>(emptyNursingStationForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- store dropdown (ctrl:66-77) ----
  private storeListRaw = signal<StoreOption[]>([]);
  // Only active stores shown, matching view:46's `ng-if="info.status == true"`.
  storeList = computed(() => this.storeListRaw().filter((s) => s.status === true));

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag; re-armed on both success AND failure -- see class doc fix

  // ---- list + real server-side offset pagination (mirrors
  // initNursingList/getNursingList/PagerService, ctrl:89-192) ----
  list = signal<NursingStationRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Real client-side filter over the CURRENT PAGE's already-fetched rows --
  // see class doc comment.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.nursingStationCode?.toLowerCase().includes(keyword) ||
        r.nursingStationDesc?.toLowerCase().includes(keyword) ||
        r.storeName?.toLowerCase().includes(keyword),
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

    this.loadStoreList();
    this.loadList();
  }

  // ---- store dropdown (ctrl:66-77) ----
  private loadStoreList(): void {
    this.api.getStoreList(this.unitId, this.orgId).subscribe({
      next: (stores) => this.storeListRaw.set(stores ?? []),
    });
  }

  // ---- list (ctrl:89-144) ----
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

  updateFormField<K extends keyof NursingStationForm>(field: K, value: NursingStationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 3 HTML5 `required` fields (NS Code/NS Description/Store,
  // view:16,31,45) that back `$scope.nursingStationForm.$valid`. No manual
  // JS validation beyond this in legacy.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.nursingStationCode.trim() && f.nursingStationDesc.trim()) && f.storeId !== '';
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().nursingStationCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().nursingStationDesc.trim();
  }

  isStoreInvalid(): boolean {
    return this.formSubmitted() && this.form().storeId === '';
  }

  // ---- save / confirm-modal (mirrors saveNursingStation/PopupOkBtn, ctrl:200-265) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the nursingStationForm.$valid gate
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
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .save({
        nursingStationCode: f.nursingStationCode,
        nursingStationDesc: f.nursingStationDesc,
        storeId: f.storeId as number,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.orgId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:239-245, verbatim shape
            this.resetForm(); // ctrl:239 -- init() runs on success, re-arms popupArmed too
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:247-253, verbatim shape
            // Fixed, diverges from legacy (codebase-wide sweep + owner
            // decision, 2026-07-24, see docs/ANGULAR_PENDING_FINDINGS.md
            // entry 152): re-arm the confirm gate on failure too. Legacy's
            // ctrl:246-253 has no reset here when read in isolation, but the
            // shared <modal> directive's own hardcoded Ok-button handler
            // re-arms popUpFlag on every click regardless of outcome,
            // confirmed live against real, unmodified legacy.
            this.popupArmed.set(true);
          }
          this.loadList(); // ctrl:255-259 -- reloads unconditionally, success AND failure
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyNursingStationForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag = true
    this.loadStoreList(); // mirrors init() re-fetching the store dropdown
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:268-293) ----
  edit(nursingStationId: number): void {
    // ctrl:270-271 -- flips saveBtnFlag/updateBtnFlag to "Update" mode
    // IMMEDIATELY on click, before the GET even resolves.
    this.saveMode.set(false);
    this.loader.start();
    this.api.getById(nursingStationId, this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:288-290, verbatim, raw alert not growl
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          nursingStationId: d.nursingStationId,
          nursingStationCode: d.nursingStationCode,
          nursingStationDesc: d.nursingStationDesc,
          storeId: d.storeId,
        });
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors updateNursingStation, ctrl:336-383 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the nursingStationForm.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        nursingStationCode: f.nursingStationCode,
        nursingStationDesc: f.nursingStationDesc,
        nursingStationId: f.nursingStationId as number,
        storeId: f.storeId as number,
        updatedBy: this.userId,
        organizationId: this.orgId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:358-364, verbatim shape
            this.resetForm(); // ctrl:359 -- init() genuinely runs on a successful update too
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:366-372, verbatim shape
          }
          this.loadList(); // ctrl:374-378 -- reloads unconditionally, success AND failure
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:296-333 -- never reloads
  // the list, but DOES reset the form + store dropdown on success) ----
  toggleStatus(row: NursingStationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy, so
    // the visual change happens immediately regardless of the response
    // either way.
    this.list.update((rows) => rows.map((r) => (r.nursingStationId === row.nursingStationId ? { ...r, status: nextStatus } : r)));

    this.loader.start();
    this.api
      .updateStatus({
        nursingStationId: row.nursingStationId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!'); // ctrl:318-323, verbatim shape
            // ctrl:317 -- a successful toggle calls $scope.init(), which
            // resets the create/edit form and re-fetches the store
            // dropdown, even though the toggle target is unrelated to
            // whatever's currently in the form. See class doc comment.
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!'); // ctrl:325-331, verbatim shape
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:207-209/297-299/337-339. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
