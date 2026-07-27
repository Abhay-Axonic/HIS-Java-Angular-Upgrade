import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ShiftMasterService } from './shift-master.service';
import { ShiftForm, ShiftRow, emptyShiftForm } from './shift-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/Nursing/shift-master.html +
 * resources/assets/js/controller/master/unit/shiftMasterController.js (356
 * lines, `shiftMasterController`). State `shiftMaster` is registered in
 * `app.js:13794` with a `resolve` block gating on module id **408**
 * (`app.js:13805`, `item.id == 408`) -- confirmed directly from the live
 * route config, not guessed.
 *
 * Confirmed real, live CRUD during Step 0/2 golden-trace, all 6 calls
 * under `/api/common/*`: list + count (ctrl:84-120), save (ctrl:176-225),
 * edit-load (ctrl:238-255), update (ctrl:266-310), status toggle
 * (ctrl:314-353) -- see shift-master.service.ts. A 7th endpoint,
 * `getShiftlistByTime`, exists on the backend and contains its own SQL
 * injection (flagged separately, docs/ANGULAR_PENDING_FINDINGS.md) but has
 * NO caller anywhere in this controller -- deliberately not wired here.
 *
 * ================================================================
 * Time fields -- HH:mm only, confirmed live, no seconds control exists
 * ================================================================
 * `fromTime`/`toTime` round-trip as plain `HH:mm` strings everywhere:
 * legacy's own clockpicker widget (`initClockPicker`, ctrl:44-47) is
 * hour:minute only, `GET_SHIFT_MASTER_LIST`/`_BY_ID` both format via
 * `to_char(shiftMaster.fromTime,'HH24:MI')` (correct 24-hour, no seconds --
 * NOT the 'HH' 12-hour-format bug found elsewhere this session on
 * WeekendMaster), and `ShiftMasterDaoImpl`'s `LocalTime.parse(...)`
 * (ISO_LOCAL_TIME) accepts an `HH:mm` string with no seconds component
 * fine. Modeled here as plain `<input type="time">` (default step, minute
 * precision) -- no seconds ever enter or leave this screen.
 *
 * The backend's `InstantConverter` UTC/systemDefault read-write asymmetry
 * (docs/ANGULAR_PENDING_FINDINGS.md, PENDING-datetime-corruption-sweep-
 * phase1) was fixed this session (`fix/instantconverter-utc-
 * systemdefault-asymmetry`, merged) -- live-verified idempotent (3
 * consecutive status toggles, no drift) before this rebuild started, so
 * this screen's status-toggle path is built against the CORRECTED
 * behavior, not the pre-fix compounding-drift bug.
 *
 * ================================================================
 * Two-click confirm flow on Save ONLY, plus the confirm-modal-never-
 * rearms bug -- FIXED here, diverging from legacy (owner decision,
 * 2026-07-27, applied uniformly across all 5 batch 22 screens)
 * ================================================================
 * `save()` (ctrl:176-225) is gated by `$scope.popUpFlag` (declared `true`
 * at ctrl:22, never reset anywhere -- not in `initMaster()`, ctrl:51-71,
 * and not in `save()`'s own success branch, ctrl:197-209): the FIRST
 * click, with `popUpFlag` true, just toggles `showModal` open (ctrl:184-
 * 186) -- no API call yet. The modal's Ok button (`PopupOkBtn`, ctrl:168-
 * 171) sets `popUpFlag = false` and calls `save()` again, which this time
 * fires the actual POST. Live-verified during Step 2 (this session,
 * 2026-07-27): after ONE successful confirmed save, a second, completely
 * independent `save()` call -- no failure anywhere in between -- skips the
 * confirm modal entirely and writes straight to the database. This is a
 * more severe sub-variant than this codebase's cardTypeMaster/
 * identificationMaster precedent (those only get stuck after a FAILED
 * save; this one never re-arms at all, success or failure). Per the
 * owner's 2026-07-27 decision, fixed here rather than faithfully
 * reproduced -- `popupArmed` below is re-armed by BOTH a successful save
 * (`resetForm()`) and (for parity with the cardTypeMaster fix shape,
 * though this controller has no failure-specific bug to diverge from --
 * legacy simply never re-arms at all) a failed one, so the modal correctly
 * reappears before every subsequent Save attempt.
 *
 * `update()` (ctrl:266-310) has NO confirm-modal gate at all in legacy --
 * fires immediately. Reproduced as-is; no modal on update.
 *
 * ================================================================
 * Status toggle -- DOES refresh the list on success (unlike this batch's
 * operationTheatreType, which doesn't)
 * ================================================================
 * `updateStatus()` (ctrl:314-353) calls `initMaster()` + `initMasterList()`
 * on a successful toggle (ctrl:335-340) -- the table correctly reflects the
 * new status immediately. Reproduced via a real `loadList()` call after a
 * successful toggle, not just an optimistic local patch.
 *
 * ================================================================
 * Pagination -- real server-side offset/count, genuinely functional
 * ================================================================
 * Unlike this batch's APmaster (confirmed live-broken -- `masterList`
 * ignores its own `offset`/`noOfRecordsPerPage` parameters and always
 * reads `$scope.offset`/`$scope.noOfRecordsPerPage` directly, so no page
 * past the first is ever reachable), shiftMaster's `masterList` (ctrl:99-
 * 118) builds its POST body from the function's own parameters correctly.
 * Reproduced as real, working offset-based pagination below.
 */
@Component({
  selector: 'app-shift-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './shift-master.html',
  styleUrl: './shift-master.scss',
})
export class ShiftMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(ShiftMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.shiftMasterObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<ShiftForm>(emptyShiftForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- list + real server-side pagination (mirrors initMasterList/masterList/setPage) ----
  list = signal<ShiftRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  // ctrl:84-120 (initMasterList) -- real offset/count pagination.
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
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

  updateField<K extends keyof ShiftForm>(field: K, value: ShiftForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 3 HTML5 `required` fields (Shift Name/Time(From)/Time(Till))
  // that back `$scope.shiftForm.$valid` (ctrl:182, 271).
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.shiftName.trim() && f.fromTime && f.toTime);
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().shiftName.trim();
  }

  isFromTimeInvalid(): boolean {
    return this.formSubmitted() && !this.form().fromTime;
  }

  isToTimeInvalid(): boolean {
    return this.formSubmitted() && !this.form().toTime;
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:168-225) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the shiftForm.$valid gate
    if (this.popupArmed()) {
      this.showConfirmModal.set(true); // mirrors `showModal = !showModal` inside the popUpFlag branch
    } else {
      // Guards only the narrow window while a confirmed save is still
      // in-flight -- legacy's OWN bug (popUpFlag never re-arms, so the
      // modal silently skips on every later attempt, success or failure)
      // is deliberately NOT reproduced here -- see class doc.
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
        shiftName: f.shiftName,
        fromTime: f.fromTime,
        toTime: f.toTime,
        isWeeklyOff: f.isWeeklyOff,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Shift Added Successfully'); // ctrl:198-201, verbatim message
            this.resetForm(); // ctrl:204 -- initMaster() runs on success, re-arms popupArmed too
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
            // Owner-approved fix (2026-07-27): legacy never re-arms popUpFlag
            // anywhere in save()'s error branch either -- re-arming here
            // means the modal correctly reappears before every subsequent
            // Save attempt, not just the first one per page load.
            this.popupArmed.set(true);
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyShiftForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors initMaster()'s (attempted) popUpFlag reset
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:231-261 -- real GET, single
  // object, NOT array-wrapped, see shift-master.models.ts) ----
  edit(row: ShiftRow): void {
    this.loader.start();
    this.api.getById(row.shiftId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.object) {
          this.saveMode.set(false);
          this.form.set({
            shiftId: res.object.shiftId,
            shiftName: res.object.shiftName,
            fromTime: res.object.fromTime,
            toTime: res.object.toTime,
            isWeeklyOff: res.object.isWeeklyOff,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:266-310 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the shiftForm.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        shiftId: f.shiftId as number,
        shiftName: f.shiftName,
        fromTime: f.fromTime,
        toTime: f.toTime,
        isWeeklyOff: f.isWeeklyOff,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Shift Updated Successfully'); // ctrl:284-287, verbatim
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:314-353 -- DOES reload
  // the list on success, see class doc) ----
  toggleStatus(row: ShiftRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start();
    this.api
      .updateStatus({
        shiftId: row.shiftId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Shift Status Updated Successfully'); // ctrl:330-333, verbatim
            this.loadList(); // ctrl:335-340 -- real reload, unlike operationTheatreType
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:151-152/179-180/234-235/291-292/317-318. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
