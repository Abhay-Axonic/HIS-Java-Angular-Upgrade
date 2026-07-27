import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DietUnitMasterService } from './diet-unit-master.service';
import { DietUnitForm, DietUnitRow, emptyDietUnitForm } from './diet-unit-master.models';

/**
 * Parity rebuild of
 * views/master/GlobalMaster/DietNutrition/diet-unit-master.html +
 * resources/assets/js/controller/dietAndKitchen/global/dietUnitMasterController.js
 * (331 lines, `dietUnitMasterController`). State `dietUnitMater` is
 * registered in `app.js:20971` with **no `resolve` block at all** --
 * confirmed directly from the live route config, same no-gate class as
 * this codebase's cardTypeMaster/billingGroupMaster precedent, not a
 * rebuild gap.
 *
 * Confirmed real, live CRUD during Step 0-2, all 6 calls under
 * `/api/diet/master/*`: list + count (ctrl:61-97), save (ctrl:145-203),
 * edit-load (ctrl:210-238), update (ctrl:241-289), status toggle
 * (ctrl:291-327) -- see diet-unit-master.service.ts.
 *
 * ================================================================
 * SQL injection -- FIXED this session, built against the corrected
 * backend
 * ================================================================
 * `checkAvailableUnitByName`/`checkAvailableUnitById` (the duplicate-check
 * queries `save`/`update` call first) concatenated the submitted
 * `uomName` directly into a raw SQL string. Live-verified exploitable,
 * escalated (docs/DECISION_NEEDED_DietUnitMasterSqlInjection.md), fixed
 * and merged this session (fix/dietunitmater-sql-injection-parameter-
 * binding) -- this rebuild is built against the corrected, parameter-
 * bound backend, not the pre-fix vulnerable one.
 *
 * ================================================================
 * Case-insensitivity bug -- CORRECTION: already fixed as a side effect
 * of the SQL injection fix, NOT still present (docs/
 * ANGULAR_PENDING_FINDINGS.md PENDING-batch22-step1-dietunitmater-
 * caseinsensitivity-and-updateinversion originally logged this as a
 * separate, still-open bug -- live-testing during this rebuild proved
 * that description wrong)
 * ================================================================
 * The SQL-injection fix's bound parameter is `.toLowerCase()`d to match
 * the query's `lower(uom.uom_name)` column comparison -- both sides are
 * now correctly lowered, not just the column. Live-verified during this
 * build (2026-07-27): saved "Kg", then attempted "kg" (different case) --
 * correctly rejected as "Already Exists.", not silently accepted as a
 * separate row. The case-insensitivity bug is gone; this component does
 * not need to (and does not) work around it.
 *
 * ================================================================
 * Update-self-collision bug -- CONFIRMED STILL PRESENT, faithfully
 * reproduced, NOT fixed here (visible behavior, not silent)
 * ================================================================
 * Update's duplicate-check compares the record being edited against
 * ITSELF (`uom_id =:uomId` instead of `<>:uomId`) -- unrelated to the SQL-
 * injection fix (a different clause in the same query), so still exactly
 * as broken as originally found. Live-verified during this build: editing
 * "Ltr"/"Liter" without changing either field and saving spuriously
 * returns "Already Exists." Reproduced as-is; no client-side workaround
 * added -- this component just calls the real endpoint and shows
 * whatever it returns.
 *
 * ================================================================
 * Confirm-modal fix -- applied here too (owner decision, 2026-07-27,
 * same as shiftMaster/APmaster/bloodBankMaster/operationTheatreType)
 * ================================================================
 * `save()` (ctrl:145-203) has the identical `popUpFlag`-never-rearms shape
 * as this batch's other 4 screens -- live-verified during Step 2
 * (2026-07-26): a second, independent save with no failure in between
 * skips the confirm modal entirely. Fixed here via `popupArmed`, same
 * shape as shiftMaster.ts. `update()` (ctrl:241-289) has NO confirm-modal
 * gate in legacy -- fires immediately; reproduced as-is.
 *
 * ================================================================
 * Pagination is entirely cosmetic and non-functional -- reproduced by
 * NOT building fake pager controls (see below)
 * ================================================================
 * `DietUnitMasterDaoImpl.getDietUnitMasterList`'s SQL has no OFFSET/LIMIT
 * at all -- confirmed by reading the DAO directly -- so every call
 * returns the FULL result set for the org regardless of what
 * `offset`/`noOfRecordsPerPage` the caller sends. The legacy view
 * (diet-unit-master.html:123) renders `commonList` directly with no
 * `limitTo`/`startFrom` filter either, so the visible table always shows
 * every row no matter which "page" is clicked -- the pager UI
 * (view:140-157) is present in the DOM and looks interactive but has
 * zero effect on what's displayed. Reproducing a fake, deliberately-
 * non-functional pager controls block was judged not worth building
 * (unlike APmaster's broken-but-otherwise-normal-looking pagination,
 * which this batch DOES faithfully reproduce, since that one only fails
 * silently past page 1 rather than being visibly absent of any real
 * slicing logic at all) -- this component simply fetches and displays
 * the full list, matching the real end-to-end visible result exactly.
 * The "Show N entries" dropdown is kept (view:46-55) since it's a
 * genuine, visible control, even though changing it has no effect on the
 * (already-complete) list either -- same non-functional-but-present
 * treatment.
 */
@Component({
  selector: 'app-diet-unit-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './diet-unit-master.html',
  styleUrl: './diet-unit-master.scss',
})
export class DietUnitMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(DietUnitMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.common + saveBtnFlag/updateBtnFlag) ----
  form = signal<DietUnitForm>(emptyDietUnitForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag -- FIXED here, see class doc

  // ---- full list -- no real pagination, see class doc ----
  list = signal<DietUnitRow[]>([]);

  // Mirrors view:70/123's `filter:searchKeyword` -- a plain AngularJS
  // `filter` with a bare string searches every field on each row, client-
  // side, over whatever `commonList` currently holds (the full result
  // set, per class doc). Reproduced the same way here.
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter(
      (r) => r.uomName?.toLowerCase().includes(keyword) || r.uomDescription?.toLowerCase().includes(keyword),
    );
  });

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

  // ctrl:61-97 (initMasterList) -- always the full result set, see class doc.
  private loadList(): void {
    this.loader.start();
    this.api.getList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
      error: () => this.loader.stop(),
    });
  }

  updateField<K extends keyof DietUnitForm>(field: K, value: DietUnitForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // Mirrors the 2 HTML5 `required` fields (Code/Description, view:14,28)
  // that back `$scope.dietUnitMasterForm.$valid`.
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.uomName.trim() && f.uomDescription.trim());
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().uomName.trim();
  }

  isDescriptionInvalid(): boolean {
    return this.formSubmitted() && !this.form().uomDescription.trim();
  }

  // ---- save / confirm-modal (mirrors save/PopupOkBtn, ctrl:139-203) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the dietUnitMasterForm.$valid gate
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
        uomName: f.uomName,
        uomDescription: f.uomDescription,
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully.'); // ctrl:180-184, verbatim
            this.resetForm();
            this.loadList();
          } else {
            // Real failure mode here: a genuine duplicate name (case-
            // insensitive matching now works correctly, see class doc).
            this.notify.error(res.message ?? 'Something went wrong');
            this.popupArmed.set(true); // owner-approved fix, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyDietUnitForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:210-238 -- ARRAY-wrapped GET
  // response, listObject[0], NOT .object -- see diet-unit-master.service.ts) ----
  edit(row: DietUnitRow): void {
    this.loader.start();
    this.api.getById(row.uomId, this.organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        const record = res.listObject?.[0];
        if (res.status === 'success' && record) {
          this.saveMode.set(false);
          this.form.set({
            uomId: record.uomId,
            uomName: record.uomName,
            uomDescription: record.uomDescription,
          });
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors update, ctrl:241-289 -- no confirm modal). Note:
  // legacy calls $rootScope.startLoader() BEFORE checking form validity
  // (ctrl:255,262 vs the $valid check at ctrl:263) -- an invalid submit
  // there leaves the loader stuck forever with no matching stop() call.
  // Not reproduced -- this mirrors every other rebuilt screen's
  // convention of only starting the loader right before the real HTTP
  // call, matching the README's "don't port a literal trap" guidance. ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the dietUnitMasterForm.$valid gate
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        uomId: f.uomId as number,
        uomName: f.uomName,
        uomDescription: f.uomDescription,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
            this.resetForm();
            this.loadList();
          } else {
            // Real, live-verified failure mode: the update-self-collision
            // bug (see class doc) -- a no-op edit can spuriously report
            // "Already Exists." here.
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:291-327 -- DOES reload
  // the list on success) ----
  toggleStatus(row: DietUnitRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start();
    this.api
      .updateStatus({
        uomId: row.uomId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:151-152/224-225(implicit)/244-245/295-296. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
