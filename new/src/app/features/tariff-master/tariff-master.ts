import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { TariffMasterService } from './tariff-master.service';
import {
  PatientTypeMapperDetail,
  PickerOption,
  TariffForm,
  TariffRow,
  VisitTypeMapperDetail,
  emptyTariffForm,
} from './tariff-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of `views/master/GlobalMaster/tariff-master.html` +
 * `resources/assets/js/controller/billing/unit/tariffMasterController.js`
 * (762 lines, `tariffMasterController`). State `tariffMaster` is registered
 * in `app.js:7944` with a `resolve` block gating on module id **609**
 * (confirmed directly from the live route config).
 *
 * ================================================================
 * SEVERITY-1, CROSS-CUTTING: Create/Update/status-toggle all 500 on real,
 * unmodified live legacy TODAY -- confirmed live, not a rebuild defect
 * ================================================================
 * `TariffMaster.java` maps `@Column(name="letter_head_image_tariff")`, but
 * the real table `service.m_tariff_master` has no such column anywhere --
 * not even in the tracked DDL source, confirmed via `psql \d` and a live
 * `catalina.out` trace (`PSQLException: column "letter_head_image_tariff"
 * ... does not exist`). `saveTariffMaster`'s full-entity `save()` and both
 * `updateTariffMaster`/`updateStatusForTariff`'s `findById(...)` calls all
 * touch every mapped column, so all three write paths fail identically.
 * List/search/edit-load are unaffected (explicitly-projected named
 * queries). Full root-cause writeup, live verification log, and two
 * proposed fix options in
 * `docs/DECISION_NEEDED_TariffMasterLetterHeadImageColumnMissing.md` --
 * flagged and escalated, NOT fixed here (out of scope for this Phase 4
 * rebuild batch, needs its own dedicated branch + owner approval). Per the
 * established `nutritionalMaster` precedent for this exact bug class, this
 * rebuild's `confirmSave()`/`update()`/`toggleStatus()` below call the real
 * endpoints normally -- no client-side code can fix a server-side write
 * failure, so both this rebuild and real legacy fail identically today, and
 * will both start succeeding with zero further code changes once the
 * backend column is fixed.
 *
 * ================================================================
 * The already-known Code-blind duplicate-check bug -- re-verified live this
 * batch, still real, FAITHFULLY REPRODUCED (not fixed)
 * ================================================================
 * `GET_TARIFF_MASTER_LIST_BY_NAME`/`_NOT_ID` (backing save/update's
 * duplicate check) filter on `tariffDesc` only, case-sensitive (no
 * `LOWER()`) -- `tariffCode` is never checked. Confirmed live via a direct
 * authenticated `fetch`: a same-Code/different-Desc create attempt
 * correctly passes the duplicate check (then separately 500s on the broken
 * INSERT above); a same-Desc/different-Code attempt is correctly blocked
 * ("Already Exists."); a different-case same-text Desc is NOT blocked
 * (case-sensitive, confirmed). Per this project's standing faithful-
 * reproduction policy (this is a visible/reproducible bug, not a silent
 * one), this rebuild does NOT add a client-side Code duplicate check --
 * only the server's own (buggy) Desc-only check applies, exactly as in
 * legacy. See `docs/DECISION_NEEDED_CompanyMasterTariffMasterCodeBlindDuplicateCheck.md`.
 *
 * ================================================================
 * Confirm modal on Save ONLY -- shared `<modal>` directive, ALWAYS closes
 * and ALWAYS re-arms on Ok-click, regardless of success/failure
 * ================================================================
 * `saveTariff()` (ctrl:315-390) is gated by `$scope.popUpFlag` (true
 * initially): the first click just toggles `showModal` open (no API call).
 * The modal's Ok button (`PopupOkBtn`, ctrl:310-313) sets `popUpFlag =
 * false` and re-calls `saveTariff()`, which this time fires the real POST.
 * Per this project's corrected, re-audited understanding of the shared
 * `<modal visible="showModal">` directive (`commonPopUpDirectives.js`): its
 * own `oneClickOnly`/`$watch` machinery closes `showModal` AND re-arms
 * `popUpFlag = true` SYNCHRONOUSLY on every Ok-click, independent of
 * whatever the screen's own success/failure branch does -- confirmed by
 * tracing the directive's `scope.$parent["popUpFlag"] = true` assignment,
 * which fires on the very next digest after Ok is clicked, well before any
 * async POST response arrives. So the modal closes and re-arms for the
 * next Save attempt EVERY time, win or lose. Reproduced here via
 * `popupArmed`, reset to `true` in BOTH the success and error branches of
 * `doSave()` below (same effective end-state as the directive's immediate,
 * unconditional reset -- see the `shift-master.ts` reference template this
 * batch was built from).
 *
 * `updateTariff()` (ctrl:519-592) has NO confirm-modal gate at all --
 * fires immediately. Reproduced as-is; no modal on update.
 *
 * ================================================================
 * Status toggle -- "Processing Transaction" guard, real, faithfully
 * reproduced
 * ================================================================
 * `updateStatusForTariff` (DAO) only runs its "in-use" check when
 * DEACTIVATING (`status.equals('I')`) -- it counts active
 * `t_order_details`/`m_company_contract_master` rows referencing this
 * tariff, and if any exist, blocks the deactivation with a warning growl
 * ("Tariff Is Under Transaction Can Not Be Deactivated...!!") AND reloads
 * the list (ctrl:612-619) -- undoing the optimistic checkbox flip in the
 * UI. A plain success does NOT reload the list (ctrl:620-625, just a
 * success growl) -- the optimistically-flipped checkbox stays flipped,
 * matching the real new backend state. A generic error also doesn't
 * revert/reload (ctrl:626-631) -- the checkbox stays optimistically
 * flipped even if the server state is unknown, a real legacy quirk,
 * faithfully reproduced.
 *
 * ================================================================
 * "Tariff Properties" modal -- Visit Type / Patient Type multiselects only
 * ================================================================
 * The view's "Add Tariff Properties" modal (view:352-525) also has Payment
 * Entitlement and Billing Bed Category multiselects, but BOTH are fully
 * commented out in the markup (view:364-392/471-509) -- only Visit Type and
 * Patient Type are live, functional controls. The modal's own "Save"
 * button (view:520-522) has NO `ng-click` at all -- it only
 * `data-dismiss="modal"`s; the actual selections are two-way-bound
 * directly to `$scope.selectedVisitType`/`selectedPatientTypeList` via the
 * `custom-drop-down-btn` directive and picked up by `saveTariff()`/
 * `updateTariff()` when the MAIN Save/Update button is clicked. Reproduced
 * the same way here: `selectedVisitTypeIds`/`selectedPatientTypeIds` are
 * plain component state, read directly by `doSave()`/`update()` below; this
 * modal's own "Close" button does not submit anything.
 *
 * Payment Entitlement itself is silently carried through on Update (legacy
 * populates `$scope.selectedPaymentEnt` from the edited record's own
 * `applicablePaymentEntitlementIdList2` during edit-load, then silently
 * re-sends it back unchanged on Update -- ctrl:454-514/540-559) despite
 * having no visible UI anywhere. Reproduced: `editPaymentEntitlementIds`
 * below stores whatever edit-load returned and is re-sent verbatim on
 * Update; it is always `[]` on Create (never populated there, matching
 * legacy). `billingBedList` is always `null` on Create (ctrl:348) and
 * entirely absent from the Update payload (ctrl:540-559 has no such key),
 * matching the models' payload shapes exactly.
 */
@Component({
  selector: 'app-tariff-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './tariff-master.html',
  styleUrl: './tariff-master.scss',
})
export class TariffMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(TariffMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  // ---- form/edit-mode state (mirrors $scope.tariffMasterObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<TariffForm>(emptyTariffForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);

  // ---- confirm-modal state (mirrors showModal/popUpFlag -- Save ONLY, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true); // mirrors popUpFlag, always re-armed win or lose -- see class doc

  // ---- list + real server-side pagination (mirrors initTariffList/getTariffList/setPage) ----
  list = signal<TariffRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side filter over the current page only -- mirrors legacy's
  // `tariffMasterList | filter:searchKeyword` (no server-side search call
  // anywhere on this screen).
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.tariffCode?.toLowerCase().includes(keyword) ||
        r.tariffDesc?.toLowerCase().includes(keyword) ||
        r.validFrom?.toLowerCase().includes(keyword) ||
        r.validTo?.toLowerCase().includes(keyword),
    );
  });

  // ---- "Tariff Properties" picker master data + selections (see class doc) ----
  visitTypeOptions = signal<PickerOption[]>([]);
  patientTypeOptions = signal<PickerOption[]>([]);
  private selectedVisitTypeIds = signal<Set<number>>(new Set());
  private selectedPatientTypeIds = signal<Set<number>>(new Set());
  private editPaymentEntitlementIds: number[] = []; // silently carried through on Update, see class doc
  showPropertiesModal = signal(false);

  selectedVisitTypeLabels = computed(() => {
    const ids = this.selectedVisitTypeIds();
    return this.visitTypeOptions()
      .filter((o) => ids.has(o.id))
      .map((o) => o.label);
  });
  selectedPatientTypeLabels = computed(() => {
    const ids = this.selectedPatientTypeIds();
    return this.patientTypeOptions()
      .filter((o) => ids.has(o.id))
      .map((o) => o.label);
  });
  isVisitTypeChecked = (id: number) => this.selectedVisitTypeIds().has(id);
  isPatientTypeChecked = (id: number) => this.selectedPatientTypeIds().has(id);

  // ---- mapper-details popup (mirrors showMapperDetails, Visit Type/Patient
  // Type halves only -- see class doc) ----
  showDetailsModal = signal(false);
  visitTypeDetails = signal<VisitTypeMapperDetail[]>([]);
  patientTypeDetails = signal<PatientTypeMapperDetail[]>([]);

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
    this.loadVisitTypeOptions();
    this.loadPatientTypeOptions();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    error: (err) => console.error('tariff-master error', err),
    });
  }

  private loadVisitTypeOptions(): void {
    this.api.getActiveVisitTypeList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.visitTypeOptions.set((res.listObject ?? []).map((v) => ({ id: v.visitTypeId, label: v.visitTypeName })));
        }
      },
    error: (err) => console.error('tariff-master error', err),
    });
  }

  private loadPatientTypeOptions(): void {
    this.api.getActivePatientTypeList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.patientTypeOptions.set((res.listObject ?? []).map((v) => ({ id: v.id, label: v.name })));
        }
      },
    error: (err) => console.error('tariff-master error', err),
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

  updateField<K extends keyof TariffForm>(field: K, value: TariffForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  onDateChange(field: 'validFrom' | 'validTo', dateStr: string): void {
    const date = dateStr ? new Date(`${dateStr}T00:00:00`) : null;
    this.updateField(field, date);
  }

  /** `yyyy-MM-dd` string for the native `<input type="date">`'s value binding. */
  toDateInputValue(d: Date): string {
    const yyyy = d.getFullYear();
    const mm = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    return `${yyyy}-${mm}-${dd}`;
  }

  // Mirrors the 4 HTML5 `required` fields (Code/Desc/Validity From/Validity
  // To) that back `$scope.tariffMasterForm.$valid` (ctrl:354, 565).
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.tariffCode.trim() && f.tariffDesc.trim() && f.validFrom && f.validTo);
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().tariffCode.trim();
  }
  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().tariffDesc.trim();
  }
  isValidFromInvalid(): boolean {
    return this.formSubmitted() && !this.form().validFrom;
  }
  isValidToInvalid(): boolean {
    return this.formSubmitted() && !this.form().validTo;
  }

  // ---- "Tariff Properties" modal (see class doc) ----
  openPropertiesModal(): void {
    this.showPropertiesModal.set(true);
  }
  closePropertiesModal(): void {
    this.showPropertiesModal.set(false);
  }
  toggleVisitType(id: number): void {
    const next = new Set(this.selectedVisitTypeIds());
    if (next.has(id)) next.delete(id);
    else next.add(id);
    this.selectedVisitTypeIds.set(next);
  }
  togglePatientType(id: number): void {
    const next = new Set(this.selectedPatientTypeIds());
    if (next.has(id)) next.delete(id);
    else next.add(id);
    this.selectedPatientTypeIds.set(next);
  }

  // ---- save / confirm-modal (mirrors saveTariff/PopupOkBtn, ctrl:310-390) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) return; // mirrors the tariffMasterForm.$valid gate
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
        tariffCode: f.tariffCode,
        tariffDesc: f.tariffDesc,
        validFrom: formatDateTime(f.validFrom as Date),
        validTo: formatDateTime(f.validTo as Date),
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.organizationId,
        status: 'A',
        paymentEntitlementIdList: [], // always [] on create -- see class doc
        visitTypeList: [...this.selectedVisitTypeIds()].map((id) => ({ id })),
        patientTypeList: [...this.selectedPatientTypeIds()].map((id) => ({ id })),
        billingBedList: null,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Tariff master added successfully...!!'); // ctrl:366-371, verbatim
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Tariff master already exits...!!'); // ctrl:380-386, verbatim
            this.popupArmed.set(true); // shared <modal> directive always re-arms -- see class doc
          }
        },
        error: () => {
          this.loader.stop();
          this.popupArmed.set(true); // shared <modal> directive always re-arms -- see class doc
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyTariffForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
    this.selectedVisitTypeIds.set(new Set());
    this.selectedPatientTypeIds.set(new Set());
    this.editPaymentEntitlementIds = [];
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:454-514) ----
  edit(row: TariffRow): void {
    this.loader.start();
    this.api.getById(row.tariffId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.object) {
          const obj = res.object;
          this.saveMode.set(false);
          this.form.set({
            tariffId: obj.tariffId,
            tariffCode: obj.tariffCode,
            tariffDesc: obj.tariffDesc,
            // `MM/dd/yyyy` from getTariffListById -- a DIFFERENT format than
            // the list query, see the models.ts doc comment.
            validFrom: parseMonthDayYear(obj.validFrom),
            validTo: parseMonthDayYear(obj.validTo),
          });
          this.selectedVisitTypeIds.set(new Set((obj.visitTypeList ?? []).map((v) => v.visitTypeId)));
          this.selectedPatientTypeIds.set(new Set((obj.patientTypeList ?? []).map((v) => v.patientTypeId)));
          this.editPaymentEntitlementIds = (obj.applicablePaymentEntitlementIdList2 ?? []).map(
            (v) => v.applicablePaymentEntitlementId,
          );
          this.formSubmitted.set(false);
        } else {
          this.notify.error('Something went wrong'); // ctrl:511-513, alert("Error!!")
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors updateTariff, ctrl:519-592 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        tariffId: f.tariffId as number,
        tariffCode: f.tariffCode,
        tariffDesc: f.tariffDesc,
        validFrom: formatDateTime(f.validFrom as Date),
        validTo: formatDateTime(f.validTo as Date),
        unitId: this.unitId,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        paymentEntitlementIdList: this.editPaymentEntitlementIds.map((id) => ({ id })), // silently carried through, see class doc
        visitTypeList: [...this.selectedVisitTypeIds()].map((id) => ({ id })),
        patientTypeList: [...this.selectedPatientTypeIds()].map((id) => ({ id })),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Tariff master updated successfully...!!'); // ctrl:572-575, verbatim
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Tariff master already exits...!!'); // ctrl:583-587, verbatim
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:594-633 -- see class doc
  // for the "Processing Transaction" branch) ----
  toggleStatus(row: TariffRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip, mirrors the view's ng-model="info.status" two-way binding.
    this.list.update((rows) => rows.map((r) => (r.tariffId === row.tariffId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        tariffId: row.tariffId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success' && res.message === 'Processing Transaction') {
            // ctrl:612-619 -- blocked deactivation: reload the list, which
            // reverts the optimistic flip back to the real (unchanged) status.
            this.loadList();
            this.notify.warning('Tariff Is Under Transaction Can Not Be Deactivated...!!');
          } else if (res.status === 'success') {
            this.notify.success('Tariff Master Updated Successfully...!!'); // ctrl:620-625, verbatim, no reload
          } else {
            this.notify.error('Something went wrong...!!'); // ctrl:626-631, verbatim, no reload/revert either
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  // ---- mapper-details popup (mirrors showMapperDetails, ctrl:392-452 --
  // Visit Type/Patient Type halves only, see class doc) ----
  viewDetails(row: TariffRow): void {
    this.api.getVisitTypeMapperDetails(row.tariffId).subscribe({
      next: (res) => this.visitTypeDetails.set(res.status === 'success' ? res.listObject ?? [] : []),
      error: () => this.notify.error('Error!!'), // ctrl:421-423, alert("Error!!")
    });
    this.api.getPatientTypeMapperDetails(row.tariffId).subscribe({
      next: (res) => this.patientTypeDetails.set(res.status === 'success' ? res.listObject ?? [] : []),
      error: () => this.notify.error('Error!!'), // ctrl:434-436
    });
    this.showDetailsModal.set(true);
  }

  closeDetailsModal(): void {
    this.showDetailsModal.set(false);
  }

  // ---- individual mapper status toggles (mirrors updateStatusVisitMapper/
  // updateStatusPatientMapper, ctrl:667-729 -- growl only, no list reload
  // either way) ----
  toggleVisitMapperStatus(row: VisitTypeMapperDetail): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.visitTypeDetails.update((rows) =>
      rows.map((r) => (r.tariffVisitTypeMapperId === row.tariffVisitTypeMapperId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateVisitTypeMapperStatus(row.tariffVisitTypeMapperId, nextStatus, this.userId, formatDateTime(new Date()))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success('Updated successfully...!!');
          else this.notify.error('Something went wrong...!!');
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  togglePatientMapperStatus(row: PatientTypeMapperDetail): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.patientTypeDetails.update((rows) =>
      rows.map((r) => (r.tariffPatientTypeMapperId === row.tariffPatientTypeMapperId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updatePatientTypeMapperStatus(row.tariffPatientTypeMapperId, nextStatus, this.userId, formatDateTime(new Date()))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success('Updated Successfully...!!');
          else this.notify.error('Something went wrong...!!');
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(...).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:326-327/332-337/523-525/544-549/597-598. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}

/** `new Date(response.data.object.validFrom)` (ctrl:479-482), reproduced
 * explicitly -- `getTariffListById` formats via `to_char(..., 'MM/dd/yyyy')`
 * (see tariff-master.models.ts doc comment), which is what makes JS's
 * native Date parsing land on the correct date there. Parsed manually here
 * (month/day/year) rather than relying on the JS engine's own locale-
 * dependent string-Date parsing. */
function parseMonthDayYear(mmddyyyy: string): Date | null {
  const parts = mmddyyyy?.split('/');
  if (!parts || parts.length !== 3) return null;
  const [mm, dd, yyyy] = parts.map((p) => parseInt(p, 10));
  if (!mm || !dd || !yyyy) return null;
  return new Date(yyyy, mm - 1, dd);
}
