import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BedMasterService } from './bed-master.service';
import {
  BedCategoryOption,
  BedMasterForm,
  BedMasterRow,
  DialysisAssetOption,
  MovingBedForm,
  MovingBedRow,
  RoomOption,
  WardOption,
  emptyBedMasterForm,
  emptyMovingBedForm,
} from './bed-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of legacy `bedMaster` (state title "Ward Bed Master",
 * app.js:8764-8789, moduleId 546). View:
 * `views/master/UnitMaster/organizationMaster/bed-master.html`. Controller:
 * `bedMasterController.js` (1000 lines, no `controllerAs`, read in full).
 * TWO tabs -- "Normal Bed" and "Moving Bed" -- driven by this one controller,
 * with mostly-separate list/save/update/status logic per tab. Full
 * golden-trace audit (live against real, unmodified legacy, 2026-07-29) in
 * `docs/ANGULAR_PENDING_FINDINGS.md` (`PENDING-bedmaster-goldentrace-SEVERITY1`)
 * -- summary of the load-bearing findings below; see that entry for the full
 * evidence trail (exact DB states, response bodies, etc).
 *
 * ================================================================
 * SEVERITY-1 #1 -- Save (`addBed`) fails on EVERY attempt, both tabs
 * ================================================================
 * `BedMasterServiceImpl.addBed` unconditionally calls
 * `getConfiguredBedLimit`, which queries a column
 * (`t_unit_application_billing_configuration.allocated_beds`) that does not
 * exist in this DB's current schema -- a `SQLGrammarException` propagates up
 * and is swallowed by the controller's empty `catch(Exception e){}`, surfacing
 * as `{"status":"error","code":null,"message":null}`. Confirmed live for
 * BOTH tabs' payload shapes. Wired to the real (broken) endpoint anyway --
 * `growl.error(response.data.message, ...)` with a null message is the real,
 * visible (if unhelpful) legacy behavior, faithfully reproduced via
 * `notify.error(res.message ?? 'Error!')` below (the `?? 'Error!'` fallback
 * is purely so the toast isn't literally blank -- not a fix to the crash
 * itself). The ONE path that still works: an EXACT duplicate is correctly
 * rejected before ever reaching the broken bed-limit check, with a real
 * `{code:'409', message:'Already Exists.'}`.
 *
 * ================================================================
 * SEVERITY-1 #2 -- Moving Bed's Update silently no-ops (real "success"
 * response, zero DB write)
 * ================================================================
 * `BedMasterDaoImpl.updateBedMaster`'s `isMovingBed=='Y'` branch returns a
 * genuine `{"status":"success","message":"Updated Successfully."}` but the
 * row is provably never modified (live-confirmed via direct DB checks,
 * repeated twice). The Normal Bed branch of the SAME method DOES persist
 * correctly. Root cause not fully pinned down (no source available for the
 * `GenericDao` base class this DAO extends) -- reported for the human
 * owner's own investigation, not fixed here; no frontend-side workaround
 * exists since the payload shape is already correct.
 *
 * ================================================================
 * Date-corruption on the paths that DO persist -- silent, faithfully
 * reproduced
 * ================================================================
 * `updateBedMaster` (Normal Bed) and `updateStatus` (both tabs) each parse
 * the frontend's `DD-MM-YYYY HH:mm:ss` date string with a mismatched
 * backend pattern (`yyyy-dd-mm .../yyyy-mm-dd ...`), corrupting
 * `updated_date` into garbage years (confirmed live: `"0029-01-07..."` /
 * `"0034-07-19..."`). Silent -- normal success toast, no `updated_date`
 * column ever displayed. The rebuild sends the same date string legacy
 * sends; this is a backend parsing defect with no safe frontend fix.
 *
 * ================================================================
 * 409 in-use guard -- confirmed on BOTH edit-fetch and status-toggle,
 * asymmetric revert between tabs
 * ================================================================
 * A non-vacant bed (`bedStatusId != 3` server-side) rejects both the edit
 * pencil (`GET /bed/{id}`) and the status toggle with a real `409`, shown
 * verbatim as `"Bed is used, you can't change status."` on both actions
 * (reproduced in `edit()`/`toggleStatus()` below). Normal Bed's 409 case
 * correctly reverts the optimistic checkbox flip in legacy (its view passes
 * `$index` into `updateStatus`); Moving Bed's view does NOT pass an index,
 * so legacy's shared revert line silently no-ops there and the checkbox is
 * left showing the WRONG (toggled) state after a blocked Moving Bed toggle
 * -- confirmed live. Reproduced exactly: `toggleStatusNormal` reverts on
 * 409, `toggleStatusMoving` does not.
 *
 * ================================================================
 * Disclosed simplification (NOT a silent fix) -- independent per-tab
 * save/update-mode + edit-buffer, not one shared mutable scope object
 * ================================================================
 * Legacy's `saveBtnFlag`/`updateBtnFlag`/`bedMasterObj` are ONE shared
 * scope object used by BOTH tabs' templates; `setFirstTabLevel` (tab switch)
 * never resets them. Live-confirmed: editing a Normal Bed row and switching
 * to Moving Bed WITHOUT saving leaves the Moving Bed tab ALSO showing
 * "Update" (misleadingly). Separately, `updateMovingBed()` has its own
 * validation entirely commented out (fires even with blank fields, unlike
 * `saveMovingBed`) and its success branch clears the code/desc fields but
 * never resets `updateBtnFlag`/the stale `bedId` -- so after one successful
 * Moving Bed update the button is permanently stuck on "Update". Currently
 * harmless in practice only because Moving Bed's `updateBed` is itself
 * SEVERITY-1 #2's silent no-op. This rebuild gives each tab its OWN
 * independent `saveMode`/edit-buffer signal (matching this codebase's
 * established multi-tab convention, e.g. `linenIssue`) rather than literally
 * threading one AngularJS scope object's accidental cross-tab leakage through
 * two structurally-unrelated forms -- a disclosed simplification of an
 * implementation artifact, not a business rule, per
 * `docs/ANGULAR_PENDING_FINDINGS.md`.
 *
 * ================================================================
 * Other real, faithfully-reproduced findings
 * ================================================================
 * - `getBedCategory()`/GETACTIVEBILLINGBEDCATEGORYBYBEDCATEGORYID is
 *   unreachable from CREATE (its own `<select>` is hardcoded `disabled`) --
 *   the ONLY way `billingBedCategoryId`/`billingBedDesc` populate on create
 *   is reading them directly off the already-loaded room list (no API call,
 *   `onRoomChange` below); the real endpoint only ever fires on `edit()`.
 * - `movingBedListCount` always returns `0` (the count-request payload never
 *   includes `isMovingBed`, and the DAO's null-valued `Restrictions.eq` never
 *   matches) -- the Moving Bed tab's pager is always empty, reproduced as-is.
 * - `bedListCount` has no room/ward/floor join unlike `bedList` itself (which
 *   implicitly excludes Moving Beds and any room-less row via its 3 INNER
 *   JOINs) -- a count/list mismatch reproduced by calling the same 2 real
 *   endpoints, not papered over.
 * - Search is real server-side full-text on both tabs
 *   (`bedMasterUniversalSearch`), NOT a client-side filter over the current
 *   page -- confirmed live.
 * - Confirm-modal on Save ONLY (both tabs); Update has no confirm step at
 *   all -- matches `roomMaster`'s established asymmetry. `popupArmed` rearms
 *   on both success and failure, per this project's already-established
 *   standing policy for the shared `<modal>` directive (not re-litigated
 *   here).
 * - Ward Bed Number (`bedDesc`) has a real `pattern="[A-Za-z0-9-/_]+"`
 *   validator on the Normal Bed tab (view:154) -- Moving Bed's equivalent
 *   field has no such pattern, just `required`. Reproduced exactly.
 * - `hideBed`/`showDialysisSelect` ward-type rules (`onWardChange` below):
 *   a labor ward hides Bed Category/Billing Category/Bed Number+Code; a
 *   hemodialysis ward does the same but reveals a "Dialysis chair" select
 *   sourced from `/api/opd/getAsset` (fired unconditionally on load, matching
 *   legacy). This dev DB's 2 test wards are neither type, so this path is
 *   wired from source reading, not independently exercised end-to-end.
 */
@Component({
  selector: 'app-bed-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './bed-master.html',
  styleUrl: './bed-master.scss',
})
export class BedMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BedMasterService);

  private orgId = 0;
  private unitId = 0;
  private userId = 0;

  activeTab = signal<1 | 2>(1);

  // ---- shared lookups (ctrl:73-108/413-430, init()) ----
  wardList = signal<WardOption[]>([]);
  bedCategoryList = signal<BedCategoryOption[]>([]);
  roomList = signal<RoomOption[]>([]);
  dialysisAssetList = signal<DialysisAssetOption[]>([]);

  // ================= Normal Bed tab =================
  form = signal<BedMasterForm>(emptyBedMasterForm());
  hideBed = signal<'Y' | 'N'>('N');
  showDialysisSelect = signal<'Y' | 'N'>('N');
  saveMode = signal(true); // true = Save, false = Update
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- rearms on both outcomes

  list = signal<BedMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');
  loadingMsg = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ================= Moving Bed tab =================
  movingForm = signal<MovingBedForm>(emptyMovingBedForm());
  movingSaveMode = signal(true);
  movingFormSubmitted = signal(false);
  movingShowConfirmModal = signal(false);
  private movingPopupArmed = signal(true);

  movingList = signal<MovingBedRow[]>([]);
  movingTotalCount = signal(0);
  movingPageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  movingCurrentPage = signal(1);
  movingSearch = signal('');
  movingLoadingMsg = signal('');

  movingTotalPages = computed(() => Math.max(1, Math.ceil(this.movingTotalCount() / this.movingPageSize())));
  movingPageNumbers = computed(() => Array.from({ length: this.movingTotalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.orgId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.api.getActiveWardMasterList(this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.wardList.set(res.listObject ?? []);
      },
    });
    this.api.getActiveBedCategoryList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.bedCategoryList.set(res.listObject ?? []);
      },
    });
    this.api.getDialysisChair().subscribe({
      next: (res) => {
        if (res.status === 'success') this.dialysisAssetList.set(res.listObject ?? []);
      },
    });

    this.loadList();
    this.loadMovingList();
  }

  selectTab(tab: 1 | 2): void {
    this.activeTab.set(tab);
  }

  // ================= Normal Bed: list =================
  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getBedList(this.orgId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.status === 'success' ? (res.listObject ?? []) : []);
        if (!this.list().length) this.loadingMsg.set('No record found...');
      },
      error: () => this.loader.stop(),
    });
    this.api.getBedListCount(this.orgId, this.unitId).subscribe({
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

  onSearchChange(value: string): void {
    this.search.set(value);
    if (value.trim() === '') {
      this.loadList();
      return;
    }
    this.loader.start();
    this.loadingMsg.set('Loading...');
    this.api.searchBedMaster(this.orgId, this.unitId, value, 'N').subscribe({
      next: (res) => {
        this.loader.stop();
        const rows = (res.listObject as BedMasterRow[] | undefined) ?? [];
        this.list.set(rows);
        if (!rows.length) this.loadingMsg.set('No record found...');
      },
      error: () => this.loader.stop(),
    });
  }

  // ================= Normal Bed: ward/room cascade =================
  onWardChange(wardId: number | null): void {
    this.updateFormField('wardId', wardId);
    this.updateFormField('roomId', null);
    this.roomList.set([]);
    if (wardId == null) {
      this.hideBed.set('N');
      this.showDialysisSelect.set('N');
      return;
    }
    const ward = this.wardList().find((w) => w.wardId === wardId);
    if (ward?.islaborWard === 'Y') {
      this.hideBed.set('Y');
      this.showDialysisSelect.set('N');
    } else if (ward?.ishemodialysisward === 'Y') {
      this.hideBed.set('Y');
      this.showDialysisSelect.set('Y');
    } else {
      this.hideBed.set('N');
      this.showDialysisSelect.set('N');
    }
    this.updateFormField('floorId', ward?.floorId ?? null);

    this.api.getRoomByWardId(this.unitId, this.orgId, wardId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.roomList.set(res.listObject ?? []);
      },
    });
  }

  // ctrl:544-558 (getBillingAndBedCategoryId) -- client-side lookup off the
  // already-loaded room list, NO API call. See class doc.
  onRoomChange(roomId: number | null): void {
    this.updateFormField('roomId', roomId);
    if (roomId == null) return;
    const room = this.roomList().find((r) => r.roomId === roomId);
    if (!room) return;
    this.updateFormField('bedCategoryId', room.bedCategoryId != null ? room.bedCategoryId : null);
    this.updateFormField('billingBedCategoryId', room.billingBedCategoryId ?? null);
    this.updateFormField('billingBedDesc', room.billingBedDesc ?? '');
  }

  updateFormField<K extends keyof BedMasterForm>(field: K, value: BedMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validity (mirrors bedForm's real HTML5 required set
  // -- ward/room/code/desc always, bedCategory only when hideBed==='N') ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    if (!f.wardId || !f.roomId || !f.bedCode.trim() || !f.bedDesc.trim()) return false;
    if (this.hideBed() === 'N' && !f.bedCategoryId) return false;
    if (!/^[A-Za-z0-9\-/_]+$/.test(f.bedDesc.trim())) return false;
    return true;
  }

  isWardInvalid(): boolean {
    return this.formSubmitted() && !this.form().wardId;
  }
  isRoomInvalid(): boolean {
    return this.formSubmitted() && !this.form().roomId;
  }
  isBedCategoryInvalid(): boolean {
    return this.formSubmitted() && this.hideBed() === 'N' && !this.form().bedCategoryId;
  }
  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().bedCode.trim();
  }
  isDescRequiredInvalid(): boolean {
    return this.formSubmitted() && !this.form().bedDesc.trim();
  }
  isDescPatternInvalid(): boolean {
    const v = this.form().bedDesc.trim();
    return this.formSubmitted() && !!v && !/^[A-Za-z0-9\-/_]+$/.test(v);
  }

  // ================= Normal Bed: save/update =================
  onSaveClick(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.form().roomId) {
      this.notify.warning('Please Select Room..!!'); // ctrl:355-358, verbatim, checked BEFORE the form-valid gate
      return;
    }
    if (!this.requiredFieldsValid()) return;
    if (this.popupArmed()) {
      this.showConfirmModal.set(true);
    } else {
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.popupArmed.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .addBed({
        floorId: f.floorId,
        wardId: f.wardId,
        roomId: f.roomId,
        bedCode: f.bedCode,
        bedDesc: f.bedDesc,
        isEarMarking: f.earMarking,
        isVirtual: f.isVirtual,
        earmarkingDesc: f.earMarkingDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.orgId,
        createdDate: now,
        updatedDate: now,
        isMovingBed: 'N',
        bedStatusId: 3,
        bedTypeId: 1,
        ...(f.bedCategoryId != null ? { bedCategoryId: f.bedCategoryId } : {}),
        ...(f.billingBedCategoryId != null ? { billingBedCategoryId: f.billingBedCategoryId } : {}),
        ...(f.itemId != null ? { itemId: f.itemId } : {}),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            // ctrl:390-397 -- SEVERITY-1: this is the real, current legacy
            // behavior for every Save attempt today (see class doc). The
            // `?? 'Error!'` fallback only avoids a literally-blank toast.
            this.notify.error(res.message ?? 'Error!');
            this.popupArmed.set(true);
          }
          this.loadList();
        },
        error: () => {
          this.loader.stop();
          this.popupArmed.set(true);
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyBedMasterForm());
    this.hideBed.set('N');
    this.showDialysisSelect.set('N');
    this.roomList.set([]);
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.form().roomId) {
      this.notify.warning('Please Select Room..!!');
      return;
    }
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .updateBed({
        floorId: f.floorId,
        wardId: f.wardId,
        roomId: f.roomId,
        bedCategoryId: f.bedCategoryId ?? undefined,
        billingBedCategoryId: f.billingBedCategoryId ?? undefined,
        bedCode: f.bedCode,
        bedDesc: f.bedDesc,
        isEarMarking: f.earMarking,
        isVirtual: f.isVirtual,
        earmarkingDesc: f.earMarkingDesc,
        updatedBy: this.userId,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.orgId,
        bedId: f.bedId as number,
        isMovingBed: 'N',
        ...(f.itemId != null ? { itemId: f.itemId } : {}),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:432-490) ----
  edit(row: BedMasterRow): void {
    this.saveMode.set(false); // flips BEFORE the fetch resolves, matching legacy
    this.loader.start();
    this.api.getBedById(row.bedId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.listObject?.length) {
          if (res.code === '409') {
            // ctrl:468-473, verbatim -- real in-use guard, see class doc.
            this.notify.warning("Bed is used, you can't change status.");
          } else {
            this.notify.error('Error!!'); // ctrl:476-482, verbatim
          }
          this.saveMode.set(true); // ctrl:474-475/483-484, reverted on failure
          return;
        }
        const d = res.listObject[0];
        this.updateFormField('wardId', d.wardId);
        this.onWardChange(d.wardId);
        this.updateFormField('floorId', d.floorId ?? null);
        this.updateFormField('roomId', d.roomId);
        this.updateFormField('bedCode', d.bedCode);
        this.updateFormField('bedDesc', d.bedDesc);
        this.updateFormField('isVirtual', d.isVirtual);
        this.updateFormField('earMarking', d.isEarMarking);
        this.updateFormField('earMarkingDesc', d.earmarkingDesc ?? '');
        this.updateFormField('bedId', d.bedId);
        if (d.bedCategoryId != null) {
          this.updateFormField('bedCategoryId', d.bedCategoryId);
          // ctrl:454-458 -- the ONE place getBedCategory (the real billing-
          // category endpoint) is actually reachable, see class doc.
          this.api.getBillingBedCategoryByBedCategoryId(this.orgId, d.bedCategoryId).subscribe({
            next: (bres) => {
              if (bres.status === 'success' && bres.listObject?.length) {
                const first = bres.listObject[0];
                this.updateFormField('billingBedDesc', first.billingBedDesc ?? '');
                this.updateFormField('billingBedCategoryId', first.billingBedCategoryId ?? null);
              }
            },
          });
        }
      },
      error: () => {
        this.loader.stop();
        this.saveMode.set(true);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:493-542 -- reverts on 409
  // for Normal Bed only, see class doc) ----
  toggleStatusNormal(row: BedMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.bedId === row.bedId ? { ...r, status: nextStatus } : r)));

    this.loader.start();
    this.api
      .updateStatus({ bedId: row.bedId, status: nextStatus, updatedBy: this.userId, updatedDate: formatDateTime(new Date()) })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm(); // ctrl:514 -- init() genuinely resets the form on a successful toggle
          } else if (res.code === '409') {
            this.notify.warning("Bed is used, you can't change status.");
            this.list.update((rows) => rows.map((r) => (r.bedId === row.bedId ? { ...r, status: row.status } : r)));
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ================= Moving Bed tab =================
  private loadMovingList(): void {
    this.loader.start();
    const offset = (this.movingCurrentPage() - 1) * this.movingPageSize();
    this.api.getMovingBedList(this.orgId, this.unitId, offset, this.movingPageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.movingList.set(res.status === 'success' ? (res.listObject ?? []) : []);
        if (!this.movingList().length) this.movingLoadingMsg.set('No record found...');
      },
      error: () => this.loader.stop(),
    });
    // Always returns 0 -- see class doc / findings. Called anyway to match
    // the real request legacy fires.
    this.api.getMovingBedListCount(this.orgId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.movingTotalCount.set(res.object);
      },
    });
  }

  onMovingPageSizeChange(): void {
    this.movingCurrentPage.set(1);
    this.loadMovingList();
  }

  goToMovingPage(page: number): void {
    if (page < 1 || page > this.movingTotalPages()) return;
    this.movingCurrentPage.set(page);
    this.loadMovingList();
  }

  onMovingSearchChange(value: string): void {
    this.movingSearch.set(value);
    if (value.trim() === '') {
      this.loadMovingList();
      return;
    }
    this.loader.start();
    this.movingLoadingMsg.set('Loading...');
    this.api.searchBedMaster(this.orgId, this.unitId, value, 'Y').subscribe({
      next: (res) => {
        this.loader.stop();
        const rows = (res.listObject as MovingBedRow[] | undefined) ?? [];
        this.movingList.set(rows);
        if (!rows.length) this.movingLoadingMsg.set('No record found...');
      },
      error: () => this.loader.stop(),
    });
  }

  updateMovingFormField<K extends keyof MovingBedForm>(field: K, value: MovingBedForm[K]): void {
    this.movingForm.update((f) => ({ ...f, [field]: value }));
  }

  isMovingCodeInvalid(): boolean {
    return this.movingFormSubmitted() && !this.movingForm().bedCode.trim();
  }
  isMovingDescInvalid(): boolean {
    return this.movingFormSubmitted() && !this.movingForm().bedDesc.trim();
  }

  onMovingSaveClick(): void {
    this.movingFormSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    const f = this.movingForm();
    if (!f.bedCode.trim() || !f.bedDesc.trim()) return; // mirrors bedFormMoving.$valid
    if (this.movingPopupArmed()) {
      this.movingShowConfirmModal.set(true);
    } else {
      this.doMovingSave();
    }
  }

  closeMovingConfirmModal(): void {
    this.movingShowConfirmModal.set(false);
  }

  confirmMovingSave(): void {
    this.movingShowConfirmModal.set(false);
    this.movingPopupArmed.set(false);
    this.doMovingSave();
  }

  private doMovingSave(): void {
    const f = this.movingForm();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .addMovingBed({
        bedCode: f.bedCode,
        bedDesc: f.bedDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.orgId,
        createdDate: now,
        updatedDate: now,
        isMovingBed: 'Y',
        bedStatusId: 3,
        bedTypeId: 1,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            // ctrl:792-794 -- clears code/desc only, does NOT call init()
            // (unlike Normal Bed's Save) -- see class doc.
            this.updateMovingFormField('bedCode', '');
            this.updateMovingFormField('bedDesc', '');
          } else {
            // SEVERITY-1: real, current behavior for every Save attempt
            // today -- see class doc.
            this.notify.error(res.message ?? 'Error!');
            this.movingPopupArmed.set(true);
          }
          this.loadMovingList();
        },
        error: () => {
          this.loader.stop();
          this.movingPopupArmed.set(true);
        },
      });
  }

  // ---- update (mirrors updateMovingBed, ctrl:819-866 -- NO validation at
  // all, fires even with blank fields, see class doc) ----
  onMovingUpdateClick(): void {
    const f = this.movingForm();
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .updateMovingBed({
        bedCode: f.bedCode,
        bedDesc: f.bedDesc,
        updatedBy: this.userId,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.orgId,
        bedId: f.bedId as number,
        isMovingBed: 'Y',
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            // SEVERITY-1: real "success" response, DB never actually
            // written -- see class doc. Reproduced verbatim regardless.
            this.notify.success(res.message ?? 'Success!');
            this.updateMovingFormField('bedCode', '');
            this.updateMovingFormField('bedDesc', '');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.loadMovingList();
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- edit (mirrors showUpdateBtnMovingBed, ctrl:874-919) ----
  editMoving(row: MovingBedRow): void {
    this.movingSaveMode.set(false);
    this.loader.start();
    this.api.getMovingBedById(row.bedId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.listObject?.length) {
          if (res.code === '409') {
            this.notify.warning("Bed is used, you can't change status.");
          } else {
            this.notify.error('Error!!');
          }
          this.movingSaveMode.set(true);
          return;
        }
        const d = res.listObject[0];
        this.movingForm.set({ bedId: d.bedId, bedCode: d.bedCode, bedDesc: d.bedDesc });
      },
      error: () => {
        this.loader.stop();
        this.movingSaveMode.set(true);
      },
    });
  }

  // ---- status toggle -- does NOT revert on 409 (legacy's revert line
  // crashes for Moving Bed, see class doc) ----
  toggleStatusMoving(row: MovingBedRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.movingList.update((rows) => rows.map((r) => (r.bedId === row.bedId ? { ...r, status: nextStatus } : r)));

    this.loader.start();
    this.api
      .updateStatus({ bedId: row.bedId, status: nextStatus, updatedBy: this.userId, updatedDate: formatDateTime(new Date()) })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else if (res.code === '409') {
            this.notify.warning("Bed is used, you can't change status.");
            // Deliberately NOT reverted -- matches legacy's real, confirmed
            // Moving-Bed-specific gap (see class doc).
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching every
 * `$scope.currentDate` assignment in the controller. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
