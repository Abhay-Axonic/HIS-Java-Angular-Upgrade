import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RoomMasterService } from './room-master.service';
import {
  BedCategoryOption,
  BillingBedCategoryOption,
  RoomMasterForm,
  RoomMasterRow,
  WardOption,
  emptyRoomMasterForm,
} from './room-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/room-master.html +
 * resources/assets/js/controller/adt/unit/roomMasterController.js
 * (`roomMasterController`, no `controllerAs` -- 0 `vm.` bindings, classic
 * `$scope` pattern). Wired via app.js's central `$stateProvider`
 * (app.js:8656), moduleId 550, no requireSuperUser. `$state` IS in this
 * controller's own DI list (unlike hospitalMasterController, a NEW instance
 * of the recurring missing-`$state`-DI crash bug found while auditing this
 * batch's sibling screen -- see hospital-master.ts) and the missing-cookie
 * guard in `init()` returns before anything else runs.
 *
 * Confirmed real, working backend, full Controller -> Service -> DAO chain
 * for all 4 initial-load endpoints and all 5 CRUD endpoints -- see
 * room-master.service.ts's header comment. Golden-trace capture
 * (tools/golden-trace/golden-traces/roomMaster.json) confirms the exact
 * initial-load signature matches source: POST getBedCategoryListByOrgUnit +
 * getActiveWardMasterList + getRoomMasterList + getRoomMasterCount, no
 * anomalies.
 *
 * **Uses the shared `<modal>` directive** (view:373,
 * `<modal visible="showModal"></modal>`) -- per
 * `docs/ANGULAR_PENDING_FINDINGS.md` entry 55's already-resolved standing
 * policy, the directive's hardcoded close-button `ng-click="cancel();
 * stopLoader();"` crashes in legacy today (roomMasterController.js never
 * defines `$scope.cancel`, confirmed via full-file grep) -- this is a new,
 * previously-uncatalogued instance of the same blast radius (not one of the
 * 12 originally counted, nor #13 `designationMaster`/entry 63 -- worth its
 * own pending-findings entry). `closeConfirmModal()` below is an ordinary,
 * working close, matching the accepted standing improvement.
 *
 * **Save vs. Update is asymmetric:** clicking Save (`saveBtnFlag`,
 * ctrl:285-354) validates the form, then -- because `popUpFlag` starts
 * `true` -- only toggles the confirm modal open; the actual POST only fires
 * when the modal's Ok button calls `PopupOkBtn()` (ctrl:279-282), which sets
 * `popUpFlag = false` and re-invokes `saveRoomMaster()`. Clicking Update
 * (`updateBtnFlag`, ctrl:442-501) validates the same form and POSTs
 * immediately, with no confirm step at all. Reproduced verbatim below:
 * `onSaveClick()` opens the modal, `confirmSave()` (the modal's Ok) does the
 * real POST; `onUpdateClick()` POSTs directly.
 *
 * **`hideBed` UI rule** (`checkIsLaborward`, ctrl:141-162): selecting a ward
 * flagged `islaborWard`/`ishemodialysisward` = 'Y' hides the Bed Category
 * select, the Billing Ward Bed Category display, the Isolation checkbox,
 * and the "Red" ER-category checkbox (Yellow/Green/Black/Is Modality stay
 * visible regardless) -- reproduced via `hideBed` below.
 *
 * **ER category checkboxes share one legacy `ng-model`** (view:141-144,
 * `roomMasterObj.erCategoryId`, `ng-true-value="1"/"2"/"3"/"4"`, no
 * `ng-false-value` on any of them) -- functionally a fake radio group:
 * AngularJS re-evaluates every checkbox bound to the same model expression
 * on each digest, so checking one un-checks the others in the same tick.
 * Modeled here as a single nullable `erCategoryId` on the form with
 * `setEr()`/`isErChecked()` reproducing the identical mutual-exclusion
 * behavior (and legacy's `erCategoryId==false ? null : erCategoryId`
 * save-time conversion, which this form's typing makes unnecessary since
 * `erCategoryId` is never set to boolean `false` here).
 *
 * **Server-side duplicate-name check, not just a frontend nicety**
 * (`RoomMasterServiceImpl.saveRoomMasterMaster`/`updateRoomMaster`, both
 * call `getRoomMasterByName`/`getRoomMasterByNameNotById` first and return
 * `ALREADY_EXIST` if a room with the same `roomDesc` -- case-insensitive --
 * already exists for the org/unit): the frontend has no independent
 * client-side duplicate check, so this is surfaced purely through the
 * normal `res.message` error path below, matching legacy exactly.
 *
 * **Placeholder `<option>` divergence (deliberate, not a parity gap
 * worth preserving):** legacy's Ward/Bed-Category `<select>`s use a bare
 * `<option>Select Ward</option>` with no `value` attribute, so HTML
 * defaults its value to the literal text "Select Ward" -- a real (if
 * essentially unreachable in practice, since AngularJS's `ngModel` select
 * directive renders its own blank "unknown option" until the user actually
 * picks something) edge case where explicitly re-selecting the placeholder
 * would pass `required` validation with garbage data. This rebuild uses a
 * real empty-value placeholder instead (closing that edge case), the same
 * class of harmless native-`<select>` behavior improvement already accepted
 * elsewhere in this codebase without its own disclosure entry.
 */
@Component({
  selector: 'app-room-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './room-master.html',
  styleUrl: './room-master.scss',
})
export class RoomMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RoomMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- lookups ----
  wardList = signal<WardOption[]>([]);
  bedCategoryList = signal<BedCategoryOption[]>([]);

  // ---- form state (mirrors $scope.roomMasterObj) ----
  form = signal<RoomMasterForm>(emptyRoomMasterForm());
  hideBed = signal<'Y' | 'N'>('N');
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initRoomMasterList/PagerService) ----
  list = signal<RoomMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy's `roomMasterList | filter:searchKeyword` has no field spec --
  // AngularJS's default filter matches ANY string property. Reproduced
  // here across the columns actually rendered in the table.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) =>
      [r.wardName, r.roomCode, r.roomDesc, r.billingBedDesc, r.bedCategoryDesc]
        .filter(Boolean)
        .some((f) => f!.toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.api.getBedCategoryListByOrgUnit(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.bedCategoryList.set(res.listObject ?? []);
      },
    });
    this.api.getActiveWardMasterList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.wardList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getRoomMasterList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getRoomMasterCount(this.organizationId, this.unitId).subscribe({
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

  updateFormField<K extends keyof RoomMasterForm>(field: K, value: RoomMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- ward change (mirrors checkIsLaborward, ctrl:141-162) ----
  onWardChange(wardId: number): void {
    this.updateFormField('wardId', wardId);
    const ward = this.wardList().find((w) => w.wardId === wardId);
    this.hideBed.set(ward?.islaborWard === 'Y' || ward?.ishemodialysisward === 'Y' ? 'Y' : 'N');
  }

  // ---- bed category change (mirrors getBedCategory, ctrl:119-139) ----
  onBedCategoryChange(bedCategoryId: number): void {
    this.updateFormField('bedCategoryId', bedCategoryId);
    if (!bedCategoryId) return;
    this.api.getBillingBedCategoryListByBedCategory(this.organizationId, bedCategoryId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const first = res.listObject?.[0];
          if (first) {
            this.updateFormField('billingBedDesc', first.billingBedDesc);
            this.updateFormField('billingBedCategoryId', first.billingBedCategoryId);
          }
        }
      },
    });
  }

  // ---- shared-model ER-category checkboxes (see class comment) ----
  isErChecked(value: number): boolean {
    return this.form().erCategoryId === value;
  }

  setEr(value: number, checked: boolean): void {
    this.updateFormField('erCategoryId', checked ? value : this.form().erCategoryId === value ? null : this.form().erCategoryId);
  }

  isRequiredMissing(): boolean {
    const f = this.form();
    if (!this.formSubmitted()) return false;
    if (!f.wardId || !f.roomCode.trim() || !f.roomDesc.trim()) return true;
    if (this.hideBed() === 'N' && !f.bedCategoryId) return true;
    return false;
  }

  // ---- save flow: Save validates then opens the confirm modal; only the
  // modal's Ok actually POSTs (mirrors saveRoomMaster()'s popUpFlag-gated
  // double call via PopupOkBtn). ----
  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (this.isRequiredMissing()) return;
    this.showConfirmModal.set(true);
  }

  /** Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy): the
   * shared `<modal>` directive's x close button crashes in legacy
   * (`cancel()` is never defined on roomMasterController's $scope) -- an
   * ordinary working close here is an accepted standing improvement, not a
   * parity violation. */
  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveRoomMaster({
        erCategoryId: f.erCategoryId,
        isIsolation: f.isIsolation,
        roomCode: f.roomCode,
        roomDesc: f.roomDesc,
        wardId: f.wardId,
        isModality: f.isModality,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        ...(f.billingBedCategoryId != null ? { billingBedCategoryId: f.billingBedCategoryId } : {}),
        ...(f.bedCategoryId != null ? { bedCategoryId: f.bedCategoryId } : {}),
      })
      .subscribe({
        next: (res) => {
          // ctrl:327-349 -- `init()` (which resets the form) only runs on
          // success; `initRoomMasterList()` (list reload) runs either way.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- update (mirrors updateRoomMaster, ctrl:442-501) -- no confirm modal ----
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (this.isRequiredMissing()) return;
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .updateRoomMaster({
        isIsolation: f.isIsolation,
        roomCode: f.roomCode,
        roomDesc: f.roomDesc,
        roomId: f.roomId as number,
        wardId: f.wardId,
        erCategoryId: f.erCategoryId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedDate: now,
        isModality: f.isModality,
        ...(f.billingBedCategoryId != null ? { billingBedCategoryId: f.billingBedCategoryId } : {}),
        ...(f.bedCategoryId != null ? { bedCategoryId: f.bedCategoryId } : {}),
      })
      .subscribe({
        next: (res) => {
          // ctrl:474-496 -- same success-only-reset / reload-either-way
          // shape as saveRoomMaster above.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyRoomMasterForm());
    this.hideBed.set('N');
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:357-399) ----
  edit(row: RoomMasterRow): void {
    this.saveMode.set(false);
    this.api.getRoomMasterById(row.roomId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            roomId: detail.roomId,
            wardId: detail.wardId,
            roomCode: detail.roomCode,
            roomDesc: detail.roomDesc,
            bedCategoryId: detail.bedCategoryId,
            billingBedCategoryId: detail.billingBedCategoryId,
            billingBedDesc: detail.billingBedDesc ?? '',
            isIsolation: detail.isIsolation,
            erCategoryId: detail.erCategoryId,
            isModality: detail.isModality,
          });
          this.hideBed.set(detail.islaborWard === 'Y' || detail.ishemodialysisward === 'Y' ? 'Y' : 'N');
        } else {
          // ctrl:394-396 -- legacy shows a raw browser alert() here; this
          // rebuild uses the same NotificationService every other screen's
          // error path uses instead, for consistency across the app.
          this.notify.error('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:402-439) ----
  toggleStatus(row: RoomMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateRoomMasterStatus({
        roomId: row.roomId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            // Update via the list signal, not a direct mutation of `row` --
            // see docs/ANGULAR_PENDING_FINDINGS.md entry 12. Mirrors
            // ctrl:422-423's init() reload after a successful toggle.
            this.list.update((rows) => rows.map((r) => (r.roomId === row.roomId ? { ...r, status: nextStatus } : r)));
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }
}

/** `moment(date).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:286-288/403-405. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
