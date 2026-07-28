import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { IcuTypeMasterService } from './icu-type-master.service';
import { ICUTypeForm, ICUTypeRow, emptyICUTypeForm } from './icu-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/UnitMaster/organizationMaster/icu-type-master.html
 * + resources/assets/js/controller/adt/global/icuTypeController.js (450
 * lines, `icuTypeController`). State `icutypeMaster` registered at
 * app.js:3226-3260 with a `resolve` block gating on module id **374**
 * (`item.id == 374`) -- confirmed directly from the live route config.
 *
 * Real, live CRUD confirmed against the actual Java backend
 * (`ICUTypeMasterController`, `@RequestMapping("/adt")`): list+count
 * (ctrl:85-137), save (ctrl:201-281, server-side case-insensitive
 * duplicate-NAME check scoped to organizationId+unitId --
 * `GET_ICU_LIST_BY_NAME`), edit-load (ctrl:284-329), update (ctrl:375-447,
 * duplicate check excludes self -- `GET_ICU_LIST_BY_NAME_NOT_ID`), status
 * toggle (ctrl:332-372, no duplicate check, no list reload).
 *
 * ================================================================
 * Confirm modal -- Save only, always closes + always re-arms on Ok click
 * ================================================================
 * This screen's view uses the shared `<modal visible="showModal">`
 * directive (view:99, `commonPopUpDirectives.js`). That directive's own
 * `oneClickOnly`/`$watch` mechanism closes `showModal` and re-arms
 * `popUpFlag` SYNCHRONOUSLY on every Ok click, independent of whatever
 * `PopupOkBtn()`/`saveIcuType()` do in their own success/failure branches
 * (see docs/ANGULAR_PENDING_FINDINGS.md entry 152 and
 * `PENDING-confirmmodal-shared-directive-reaudit` -- verified live,
 * project-wide, not a per-screen judgment call). Modeled below exactly
 * like `shift-master.ts`: `confirmSave()` closes the modal and fires the
 * save immediately; `doSave()` re-arms `popupArmed` in BOTH the success
 * and failure branches, so the net behavior always matches real legacy
 * (gate can never get stuck, modal never lingers open after Ok).
 *
 * **Live-verified directly against real, unmodified legacy** (localhost:
 * 8080, this session): saved `TSTICU1`/`Test ICU Ward` (Open ICU) --
 * "Icu Type Added Successfully...!!", modal closed immediately. Then
 * saved `TSTICU2` with the SAME type name lowercased ("test icu ward")
 * -- real duplicate rejection ("Icu Type already exits...!!" verbatim
 * typo), modal closed immediately, form NOT cleared (matches ctrl:262-267
 * not calling init() on failure). Changed the type name to a unique value
 * and clicked Save again on the SAME page, with no reload -- confirm
 * modal correctly reopened, proving the gate is never stuck. Both test
 * rows deactivated afterward via the status toggle.
 *
 * `updateIcuType()` (ctrl:375-447) has NO confirm-modal gate at all --
 * fires immediately. Reproduced as-is; no modal on update.
 *
 * ================================================================
 * Update's loader stops BEFORE the response arrives -- faithfully kept
 * ================================================================
 * `updateIcuType()`'s `$rootScope.stopLoader()` at ctrl:444 runs
 * SYNCHRONOUSLY right after the async POST is fired (it's OUTSIDE the
 * `.then()` callback, at the same level as the `if ($scope.icuTypeform
 * .$valid)` block, not inside it) -- the loading spinner disappears
 * immediately on clicking Update, not when the network response actually
 * comes back. `saveIcuType()` has no such early stop (its only
 * `stopLoader()` calls are inside the `.then()` callback). Reproduced
 * below: `update()` calls `loader.stop()` synchronously right after
 * subscribing, `save()`'s `doSave()` only stops it inside the response
 * handler.
 *
 * ================================================================
 * List refresh after Save/Update -- unconditional, success OR failure
 * ================================================================
 * `initIcUTypeMasterList(...)` is called after the `if/else` in BOTH
 * `saveIcuType()` (ctrl:270-276) and `updateIcuType()` (ctrl:436-441),
 * regardless of whether the save/update succeeded or hit the duplicate-
 * name rejection -- unlike shift-master (which only reloads on success).
 * Reproduced via an unconditional `loadList()` call in both branches.
 *
 * ================================================================
 * Open/Closed ICU radio -- no `required`, and edit doesn't reset it
 * ================================================================
 * The two radios (view:53-62) share `ng-model="icuTypeObj.isOpen"`
 * (values `'open'`/`'close'`) with NO `required` attribute anywhere, and
 * `$scope.icuTypeObj` starts at `{isOpen:'N', isClose:'N'}` (ctrl:48-51)
 * -- neither radio is checked by default, and a record CAN be saved with
 * neither picked (`isOpenICU`/`isCloseICU` both persist as `'N'`, see
 * `mapOpenSelection` below). `showUpdateBtn` (ctrl:311-320) only sets
 * `icuTypeObj.isOpen` when the loaded record has `isOpenICU=='Y'` or
 * `isCloseICU=='Y'` -- if BOTH are `'N'` on the loaded record, the branch
 * is skipped entirely and `icuTypeObj.isOpen` is never reset first, so it
 * keeps whatever value was left over from the PREVIOUS edit/create. This
 * is a real, visible (the radio literally renders as checked/unchecked on
 * screen) legacy quirk, not a silent bug -- faithfully reproduced in
 * `edit()` below by carrying over `form().isOpen` instead of defaulting
 * to `'N'` before applying the record's own flags. Live-verified the
 * "leftover selection persists through a failed save" half of this same
 * carryover mechanism directly (the Closed-ICU radio chosen before a
 * duplicate-name rejection was still Closed on the very next successful
 * save, with no re-selection in between).
 *
 * `showUpdateBtn` also flips `saveBtnFlag`/`updateBtnFlag` to "Update
 * mode" SYNCHRONOUSLY, before the GET even resolves, and never flips them
 * back on a failed fetch (`alert("Error!!")` is the only failure
 * handling) -- reproduced by setting `saveMode` to false before the
 * `getById` call, not inside its success branch.
 *
 * ================================================================
 * Status toggle -- optimistic only, does NOT reload the list
 * ================================================================
 * `updateStatus()` (ctrl:332-372) never calls `initIcUTypeMasterList`
 * on success -- the table relies entirely on the checkbox's own
 * `ng-model="info.status"` two-way binding for its visual update. Modeled
 * as a local optimistic signal update, no `loadList()` call (same
 * pattern as this batch's convention for screens that don't reload).
 *
 * ================================================================
 * Search -- client-side filter over the currently-loaded page only
 * ================================================================
 * `ng-repeat="info in icuTypeList|filter:searchKeyword"` (view:191) is
 * Angular's generic (all-fields) client-side filter applied to whatever
 * page is already loaded via real server-side offset/count pagination --
 * not a server-side search call. Modeled as a `displayedRows` computed
 * signal doing a substring match across code/type, the established
 * simplification this codebase already uses for this exact pattern (see
 * nursing-station-master).
 *
 * Pagination is real, working, server-side offset/count (same shape as
 * shift-master, unlike this project's known-broken APmaster).
 */
@Component({
  selector: 'app-icu-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './icu-type-master.html',
  styleUrl: './icu-type-master.scss',
})
export class IcuTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(IcuTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form/edit-mode state (mirrors $scope.icuTypeObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<ICUTypeForm>(emptyICUTypeForm());
  saveMode = signal(true); // true = saveBtnFlag (create); false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag

  // ---- list + real server-side pagination (mirrors initIcUTypeMasterList/getIcUTypeMasterList/setPage) ----
  list = signal<ICUTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ctrl:191 -- client-side filter over the currently-loaded page only.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (row) => row.icutypeCode.toLowerCase().includes(keyword) || row.icutype.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  // ctrl:85-137 (initIcUTypeMasterList) -- real offset/count pagination.
  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
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

  updateField<K extends keyof ICUTypeForm>(field: K, value: ICUTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 2 HTML5 `required` fields (ICU Code/ICU Type) that back
  // `$scope.icuTypeform.$valid` (ctrl:207, 382) -- the Open/Closed radio
  // has no `required`, see class doc.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.icuCode.trim() && f.icuType.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().icuCode.trim();
  }

  isTypeInvalid(): boolean {
    return this.formSubmitted() && !this.form().icuType.trim();
  }

  // ctrl:216-226/385-395 -- maps the tri-state radio selection to the two
  // Y/N flags. Neither picked ('N') faithfully maps to Y/N both 'N'.
  private mapOpenSelection(selection: ICUTypeForm['isOpen']): { isOpenICU: 'Y' | 'N'; isCloseICU: 'Y' | 'N' } {
    if (selection === 'open') return { isOpenICU: 'Y', isCloseICU: 'N' };
    if (selection === 'close') return { isOpenICU: 'N', isCloseICU: 'Y' };
    return { isOpenICU: 'N', isCloseICU: 'N' };
  }

  // ---- save / confirm-modal (mirrors saveIcuType/PopupOkBtn, ctrl:195-281) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the icuTypeform.$valid gate
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
    this.showConfirmModal.set(false); // matches the shared directive's synchronous close-on-Ok, see class doc
    this.popupArmed.set(false); // mirrors PopupOkBtn's `$scope.popUpFlag = false;`
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    const { isOpenICU, isCloseICU } = this.mapOpenSelection(f.isOpen);
    this.loader.start();
    this.api
      .save({
        icutypeCode: f.icuCode,
        icutype: f.icuType,
        isOpenICU,
        isCloseICU,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Icu Type Added Successfully...!!'); // ctrl:251-256, verbatim
            this.resetForm(); // ctrl:259 -- init() runs on success, re-arms popupArmed too
          } else {
            this.notify.error('Icu Type already exits...!!'); // ctrl:262-267, verbatim typo kept
            this.popupArmed.set(true); // re-arm on failure too -- see class doc, matches real legacy net effect
          }
          this.loadList(); // ctrl:270-276 -- unconditional, success or failure
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyICUTypeForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true); // mirrors init()'s popUpFlag reset
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:284-329) ----
  edit(row: ICUTypeRow): void {
    this.saveMode.set(false); // ctrl:286-287 -- flipped synchronously, before the GET resolves, see class doc
    this.loader.start();
    this.api.getById(row.icutypeMasterId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          // ctrl:311-320 -- carries over the PREVIOUS form's isOpen value
          // when the record has neither flag set, see class doc.
          let isOpen = this.form().isOpen;
          if (d.isOpenICU === 'Y') isOpen = 'open';
          else if (d.isCloseICU === 'Y') isOpen = 'close';
          this.form.set({
            icuTypeId: d.icutypeMasterId,
            icuCode: d.icutypeCode,
            icuType: d.icutype,
            isOpen,
          });
        } else {
          this.notify.error('Error!!'); // ctrl:323-326, verbatim (alert("Error!!") -> growl-equivalent)
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors updateIcuType, ctrl:375-447 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the icuTypeform.$valid gate
    const f = this.form();
    const { isOpenICU, isCloseICU } = this.mapOpenSelection(f.isOpen);
    this.loader.start();
    this.api
      .update({
        icutypeCode: f.icuCode,
        icutype: f.icuType,
        isOpenICU,
        isCloseICU,
        icutypeMasterId: f.icuTypeId as number,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Icu Type Updated Successfully...!!'); // ctrl:418-423, verbatim
            this.resetForm();
          } else {
            this.notify.error('Icu Type already exits...!!'); // ctrl:428-434, verbatim typo kept
          }
          this.loadList(); // ctrl:436-441 -- unconditional, success or failure
        },
        error: () => {},
      });
    this.loader.stop(); // ctrl:444 -- fires synchronously, NOT inside the response handler, see class doc
  }

  // ---- status toggle (mirrors updateStatus, ctrl:332-372 -- optimistic
  // only, no list reload, see class doc) ----
  toggleStatus(row: ICUTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) =>
      rows.map((r) => (r.icutypeMasterId === row.icutypeMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.loader.start();
    this.api
      .updateStatus({
        icutypeMasterId: row.icutypeMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Icu Type Updated Successfully...!!'); // ctrl:355-360, verbatim
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:363-368, verbatim
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:203-205/334-336/377-379. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
