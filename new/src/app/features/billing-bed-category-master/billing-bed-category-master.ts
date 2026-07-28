import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BillingBedCategoryMasterService } from './billing-bed-category-master.service';
import {
  BillingBedCategoryFormState,
  BillingBedCategoryRow,
  HierarchyOption,
  VISIT_TYPE_OPTIONS,
  VisitTypeOption,
  emptyBillingBedCategoryForm,
} from './billing-bed-category-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/billing-bed-category-master-unit.html
 * + resources/assets/js/controller/adt/unit/billingBedCategoryMasterUnitController.js
 * (`billingBedCategoryMasterUnitController`, 538 lines). State
 * `billingBedCategoryMaster` (app.js:8590-8613) is gated by module id **544**
 * (app.js:8602, `item.id == 544`), confirmed directly from the live route
 * config's `resolve` block, not guessed. Backend: `/adt/*`
 * (`BillingBedCategoryMasterController.java`, `@RequestMapping("/adt")`),
 * `BillingBedCategoryMasterServiceImpl`/`BillingBedCategoryMasterDaoImpl`
 * (`legacy/ADT/.../unit/{service,dao}`), entity
 * `legacy/Entity/.../unit/model/BillingBedCategoryMaster.java`
 * (`adt.m_billing_bed_category_master`).
 *
 * ================================================================
 * Confirm modal -- Save only, always closes + always re-arms on Ok click
 * ================================================================
 * The view carries the shared `<modal visible="showModal"></modal>`
 * directive (view:352, `commonPopUpDirectives.js`). Per this project's
 * established, live-reverified conclusion (docs/ANGULAR_PENDING_FINDINGS.md
 * entry 152 + `PENDING-confirmmodal-shared-directive-reaudit`), that
 * directive's own `oneClickOnly`/`$watch` mechanism closes `showModal` and
 * re-arms `popUpFlag` synchronously and unconditionally on every real Ok
 * click, regardless of the screen's own success/failure branching -- so the
 * modal can never actually get stuck in real legacy. Modeled here exactly
 * like `shift-master.ts`: `popupArmed` re-arms on both the success AND
 * failure branch of `doSave()`. `updateBillingBed()` (ctrl:436-536) has NO
 * confirm-modal gate at all in legacy -- fires immediately, reproduced as-is
 * (no modal on Update).
 *
 * ================================================================
 * "Amount must be greater than zero" -- Save-only quirk, reproduced as-is
 * ================================================================
 * `saveBillingBed()` (ctrl:266-267) gates the actual save on
 * `billingForm.$valid && gstAmountRange != 0` -- since `gstAmountRange` is
 * only ever set when the (conditionally-rendered) Amount field is shown
 * (`isGstApplicable` checked), an untouched value stays `undefined`/`null`
 * and the loose `!= 0` comparison passes; the block only fires when the
 * user explicitly types `0` into Amount while GST-applicable is checked.
 * Reproduced below as `f.gstAmountRange === 0` (only blocks on an explicit
 * zero, never on "not applicable"/null). `updateBillingBed()` has NO
 * equivalent check anywhere -- a real, preserved Save/Update inconsistency,
 * not invented here.
 *
 * ================================================================
 * Visit-type diff on Update -- legacy's reference-equality bug, deliberately
 * NOT reproduced (silent/invisible bug, disclosed deviation)
 * ================================================================
 * `updateBillingBed()` (ctrl:472-496) diffs `selectedVisitType` against
 * `oldSelectVisitList` via `Array.prototype.indexOf`, which in legacy JS
 * compares by OBJECT REFERENCE, not by `.id` value. Independently verified
 * via direct source read (this controller + the actual loaded
 * `ngDropdownMultiselect` directive, `angularjs-dropdown-multiselect.min.js`)
 * that this is a real bug, but it is SILENT: `showUpdateBtn` (ctrl:383-390)
 * seeds both arrays with the SAME object references per visit type, so an
 * untouched checkbox still matches by reference and a genuinely different
 * selection (different `.id`) never matches by reference OR by value --
 * both produce the identical newlyAdded/removed sets a correct value-based
 * diff would. The ONLY case where legacy's reference bug diverges is
 * unchecking then re-checking the SAME already-selected visit type within
 * one edit session: the multiselect directive's `setSelectedItem`
 * (`push(find(options,...))`) replaces that entry with a fresh object from
 * the shared `options` array, so the (unchanged) id ends up in BOTH
 * `removedVisitTypeList` and `newlyAddedVisitTypeList`. Server-side
 * (`BillingBedCategoryMasterServiceImpl.updateBillingBedCategoryMaster`,
 * service:195-229) that just soft-inactivates the old mapper row and inserts
 * a fresh active one for the same id -- the active mapping set (and
 * therefore everything ever rendered to a user, since
 * `getBillingBedCategoryVistList` only ever selects `status='A'` rows) ends
 * up identical either way. The only externally observable difference is
 * extra inactive audit-history rows accumulating in
 * `adt.t_billing_bed_visit_type_mapper` -- not visible through any UI path.
 * Per this project's silent-bug policy, implemented here as a clean,
 * `.id`-based diff (`newlyAddedVisitTypeList`/`removedVisitTypeList` computed
 * against the id set loaded at edit time) instead of reproducing the
 * reference-identity quirk -- disclosed here as a deliberate deviation, not
 * a silent fix; no BUSINESS_RULE_MANIFEST entry filed since no calculated,
 * displayed, or queried output differs for any input (Refactoring/Safe per
 * RULES.md's own definition, not a Behavioral Change).
 *
 * ================================================================
 * Visit Type multiselect -- simplified to a plain checkbox group
 * ================================================================
 * Legacy uses a 3rd-party dropdown-checkbox widget
 * (`ng-dropdown-multiselect`) over the same 4 static options
 * (ctrl:67-84). Modeled here as a plain checkbox group over the same 4
 * `{id,label}` pairs -- identical selectable set, identical payload shape,
 * no behavior lost.
 *
 * ================================================================
 * Visit Type / GST "details" popups -- info-icon-triggered, per-row
 * ================================================================
 * `visitDetails(item)`/`getGstDetails(item)` (ctrl:336-360) just stash the
 * clicked row for a small read-only popup (view:249-350). Modeled as
 * `detailRow` signals rather than legacy's redundant
 * `addedvisitList`/`gstDetailsList` arrays (always exactly 0 or 1 row) --
 * same rendered output, no separate array needed.
 */
@Component({
  selector: 'app-billing-bed-category-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './billing-bed-category-master.html',
  styleUrl: './billing-bed-category-master.scss',
})
export class BillingBedCategoryMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BillingBedCategoryMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  visitTypeOptions: VisitTypeOption[] = VISIT_TYPE_OPTIONS;

  // ---- dropdowns ----
  hierarchyList = signal<HierarchyOption[]>([]);

  // ---- form/edit-mode state (mirrors billingBedMasterObj + saveBtnFlag/updateBtnFlag) ----
  form = signal<BillingBedCategoryFormState>(emptyBillingBedCategoryForm());
  saveMode = signal(true); // true = saveBtnFlag (create), false = updateBtnFlag (edit)
  formSubmitted = signal(false);
  private originalVisitTypeIds: number[] = []; // oldSelectVisitList, id-only (see class doc)

  // ---- confirm-modal state (Save only, see class doc) ----
  showConfirmModal = signal(false);
  private popupArmed = signal(true);

  // ---- list + real server-side pagination ----
  list = signal<BillingBedCategoryRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side filter over the current server-fetched page only, matching
  // `|filter:searchKeyword` (view:194) applied on top of real offset paging.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) =>
      [r.billingBedCode, r.billingBedDesc, r.hierarchyDesc].some((v) => v?.toLowerCase().includes(keyword)),
    );
  });

  // ---- Visit Type / GST details popups (ctrl:336-360) ----
  visitDetailRow = signal<BillingBedCategoryRow | null>(null);
  gstDetailRow = signal<BillingBedCategoryRow | null>(null);

  constructor() {
    this.loadDropdowns();
    this.loadList();
  }

  private loadDropdowns(): void {
    this.api.getActiveHierarchyList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.hierarchyList.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
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

  updateField<K extends keyof BillingBedCategoryFormState>(field: K, value: BillingBedCategoryFormState[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  toggleVisitType(id: number): void {
    this.form.update((f) => {
      const has = f.selectedVisitTypeIds.includes(id);
      return {
        ...f,
        selectedVisitTypeIds: has ? f.selectedVisitTypeIds.filter((v) => v !== id) : [...f.selectedVisitTypeIds, id],
      };
    });
  }

  isVisitTypeSelected(id: number): boolean {
    return this.form().selectedVisitTypeIds.includes(id);
  }

  // ---- required-field validation (mirrors billingForm.$valid) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    if (!f.billingBedCode.trim() || !f.billingBedDesc.trim() || f.hierarchyId === '') return false;
    if (f.isGstApplicable && (f.gstAmountRange === null || f.gstPercentage === null)) return false;
    return true;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().billingBedCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().billingBedDesc.trim();
  }

  isHierarchyInvalid(): boolean {
    return this.formSubmitted() && this.form().hierarchyId === '';
  }

  isAmountInvalid(): boolean {
    return this.formSubmitted() && this.form().isGstApplicable && this.form().gstAmountRange === null;
  }

  isPercentageInvalid(): boolean {
    return this.formSubmitted() && this.form().isGstApplicable && this.form().gstPercentage === null;
  }

  onGstApplicableChange(checked: boolean): void {
    this.form.update((f) => ({ ...f, isGstApplicable: checked }));
  }

  // ---- save / confirm-modal (mirrors saveBillingBed/PopupOkBtn, ctrl:229-334) ----
  save(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) {
      this.notify.error('Please enter all mandatory field');
      return;
    }
    // ctrl:266-267/314-322 -- "Amount must be greater than zero", Save only.
    if (this.form().gstAmountRange === 0) {
      this.notify.error('Amount must be greater than zero');
      return;
    }
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
      .save({
        billingBedCode: f.billingBedCode,
        billingBedDesc: f.billingBedDesc,
        hierarchyId: f.hierarchyId as number,
        isGstApplicable: f.isGstApplicable,
        gstAmountRange: f.gstAmountRange,
        gstPercentage: f.gstPercentage,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        isPeriodicBilling: f.isPeriodicBilling,
        visitTypeMasterDtosList: f.selectedVisitTypeIds.map((id) => ({ id })),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Added Successfully.');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong !!');
            this.popupArmed.set(true); // re-arm on failure too, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyBillingBedCategoryForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
    this.originalVisitTypeIds = [];
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:362-393) ----
  edit(row: BillingBedCategoryRow): void {
    this.saveMode.set(false);
    this.formSubmitted.set(false);
    const visitTypeIds = (row.visitTypeMasterDtosList ?? []).map((v) => v.id);
    this.originalVisitTypeIds = visitTypeIds;
    this.form.set({
      billingBedCategoryId: row.billingBedCategoryId,
      billingBedCode: row.billingBedCode,
      billingBedDesc: row.billingBedDesc,
      hierarchyId: row.hierarchyId,
      isGstApplicable: row.isGstApplicable,
      gstAmountRange: row.gstAmountRange,
      gstPercentage: row.gstPercentage,
      isPeriodicBilling: row.isPeriodicBilling ?? 'N',
      selectedVisitTypeIds: [...visitTypeIds],
    });
  }

  // ---- update (mirrors updateBillingBed, ctrl:436-536 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true); // mirrors $broadcast('show-errors-check-validity')
    if (!this.requiredFieldsValid()) {
      this.notify.error('Please enter all mandatory field');
      return;
    }
    const f = this.form();
    const now = formatDateTime(new Date());

    // Clean, id-based diff -- see class doc for why this deliberately does
    // NOT reproduce legacy's reference-equality quirk.
    const newlyAddedVisitTypeList = f.selectedVisitTypeIds
      .filter((id) => !this.originalVisitTypeIds.includes(id))
      .map((id) => ({ id }));
    const removedVisitTypeList = this.originalVisitTypeIds
      .filter((id) => !f.selectedVisitTypeIds.includes(id))
      .map((id) => ({ id }));

    this.loader.start();
    this.api
      .update({
        billingBedCode: f.billingBedCode,
        billingBedDesc: f.billingBedDesc,
        hierarchyId: f.hierarchyId as number,
        billingBedCategoryId: f.billingBedCategoryId as number,
        isGstApplicable: f.isGstApplicable,
        gstAmountRange: f.gstAmountRange,
        gstPercentage: f.gstPercentage,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
        unitId: this.unitId,
        isPeriodicBilling: f.isPeriodicBilling,
        newlyAddedVisitTypeList,
        removedVisitTypeList,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Something went wrong !!');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:396-433) ----
  toggleStatus(row: BillingBedCategoryRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start();
    this.api
      .updateStatus({
        billingBedCategoryId: row.billingBedCategoryId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
            this.loadList(); // ctrl:416-417 -- $scope.init() runs list refresh on success
          } else {
            this.notify.error(res.message ?? 'Something went wrong !!');
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- Visit Type / GST details popups (ctrl:336-360) ----
  openVisitDetails(row: BillingBedCategoryRow): void {
    this.visitDetailRow.set(row);
  }

  closeVisitDetails(): void {
    this.visitDetailRow.set(null);
  }

  openGstDetails(row: BillingBedCategoryRow): void {
    this.gstDetailRow.set(row);
  }

  closeGstDetails(): void {
    this.gstDetailRow.set(null);
  }

  visitTypeLabel(id: number): string {
    return this.visitTypeOptions.find((v) => v.id === id)?.label ?? String(id);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:238-240/397-399/437-439. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
