import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { GrnService } from './grn.service';
import {
  CancelReason,
  DropdownOption,
  GrnListRow,
  ItemSearchResult,
  StatusMasterEntry,
  VendorSearchResult,
} from './grn.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const SELECT_STORE: DropdownOption = { id: 0, store: 'Select Store' };
const SELECT_STATUS_ALL: DropdownOption = { id: 0, status: 'All' };
const SELECT_GRN_TYPE: DropdownOption = { id: 0, type: 'Select GRN Type' };

/**
 * Parity rebuild of views/procurement/quotation/GRN.html +
 * resources/assets/js/controller/procurement/quotation/grnController.js.
 * Second Phase-2-style pilot batch (controllerAs:'vm' self-registered
 * screens), audited the same way as docs/PHASE2_PILOT_AUDIT.md's original
 * 5. No new validation, no new fields, no behavior not present in the
 * legacy screen -- exact parity is the goal.
 *
 * **Deliberate deviations from legacy, both already known and tracked,
 * not bugs in this rebuild:**
 * - The view's Cancel button called `vm.model.PopulateReasonDropdown()`,
 *   a function that does not exist anywhere in grnController.js -- a real
 *   dead reference in legacy (silent console error on every Cancel click,
 *   harmless because the reasons list is already loaded once at init and
 *   never needed refreshing). Not reproduced here; the cancel modal just
 *   opens with the already-loaded `reasons` list, which is legacy's actual
 *   observable behavior once you discount the broken call.
 * - `rePrintBillSlip` (legacy: POST /api/billing/unit/bill/Report via
 *   reportService.generate, a Crystal Reports PDF generator) is stubbed
 *   with a notice rather than wired -- same precedent as the first pilot's
 *   inputOutputCharts Print/PDF stub: no report-rendering backend to
 *   integrate against in this sandbox.
 * - The `addItemGRN`/`GRNdetails` cross-screen navigation (Add New / Edit /
 *   Details buttons) and the resolve-block permission gate (module id 181
 *   against localStorage.permissions) are both out of this pilot's scope,
 *   same reasoning as the first pilot's ngUpgrade-hybrid-wiring carve-out --
 *   route-level/app-shell concerns, not this screen's own behavior.
 * - screenDataService's cross-navigation filter-caching (restoring the last
 *   search criteria when returning to this screen) is not reproduced --
 *   out of scope for initial-load characterization, same as legacy's own
 *   fresh-load behavior when no prior screen data exists.
 */
@Component({
  selector: 'app-grn',
  imports: [FormsModule, DatePipe],
  templateUrl: './grn.html',
  styleUrl: './grn.scss',
})
export class Grn {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(GrnService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- search filters (mirrors self.model.*) ----
  fromDate = signal<Date>(new Date());
  toDate = signal<Date>(new Date());
  storeList = signal<DropdownOption[]>([SELECT_STORE]);
  fromstoreList = signal<DropdownOption[]>([SELECT_STORE]);
  statusFilterList = signal<DropdownOption[]>([SELECT_STATUS_ALL]);
  assetTypes = signal<DropdownOption[]>([SELECT_GRN_TYPE]);

  selectedStore = signal<DropdownOption>(SELECT_STORE);
  selectedFromstore = signal<DropdownOption>(SELECT_STORE);
  selectedStatus = signal<DropdownOption>(SELECT_STATUS_ALL);
  selectedGrnType = signal<DropdownOption>(SELECT_GRN_TYPE);
  grnNumber = signal('');

  // ---- vendor typeahead (mirrors SearchVendorNames/AddVendorName) ----
  vendorSearchTerm = signal('');
  vendorResults = signal<VendorSearchResult[]>([]);
  vendorId = signal<number | undefined>(undefined);
  private readonly vendorSearch$ = new Subject<string>();

  // ---- item typeahead (mirrors SearchItems/AddItemName) ----
  itemSearchTerm = signal('');
  itemResults = signal<ItemSearchResult[]>([]);
  itemId = signal<number | undefined>(undefined);
  private readonly itemSearch$ = new Subject<string>();

  // ---- reasons + status master (mirrors cancelReasonService/StatusService) ----
  reasons = signal<CancelReason[]>([]);
  statusMasterList = signal<StatusMasterEntry[]>([]);

  // ---- list + client-side pagination (mirrors self.model.grnList + PaginationService) ----
  grnList = signal<GrnListRow[]>([]);
  searchKeyword = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.grnList();
    return this.grnList().filter((row) =>
      Object.values(row).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });
  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredList().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedList = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.filteredList().slice(offset, offset + limit);
  });

  // ---- row selection (mirrors SelectAllGRN/SelectGRN/SelectedGRNIds) ----
  selectedGrnIds = signal<Set<number>>(new Set());
  selectAllRows = signal(false);

  // ---- cancel modal (mirrors cancelGRN modal) ----
  showCancelModal = signal(false);
  cancelReason = signal<number | undefined>(undefined);
  rejectNote = signal('');
  isReasonValid = signal(false); // true = show "Select Reason" error, mirrors legacy's inverted naming
  private cancelTargetId: number | undefined;

  // ---- confirm/approve modal (mirrors confirmSave modal) ----
  showConfirmModal = signal(false);
  saveBtnDisabled = signal(false);

  constructor() {
    this.api.getCancelReasons().subscribe({ next: (r) => this.reasons.set(r) });
    this.api.getStatusMaster().subscribe({ next: (s) => this.statusMasterList.set(s) });
    this.api.getDropdowns(this.unitId, this.organizationId, this.userId).subscribe({
      next: (res) => {
        this.storeList.set([SELECT_STORE, ...res.deliveryStores]);
        this.fromstoreList.set([SELECT_STORE, ...res.requestingStores]);
        this.statusFilterList.set([SELECT_STATUS_ALL, ...res.status]);
        this.assetTypes.set([SELECT_GRN_TYPE, ...res.assetType]);
        this.searchGrn();
      },
      error: () => this.notify.error('Failed to load GRN dropdowns'),
    });

    this.vendorSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.searchVendors(term, this.unitId)),
      )
      .subscribe({ next: (results) => this.vendorResults.set(results) });

    this.itemSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.searchItems(this.unitId, term, this.selectedFromstore().id || undefined)),
      )
      .subscribe({ next: (results) => this.itemResults.set(results) });
  }

  // ---- date handling (mirrors OpenFromDate/OpenToDate/calculateThreeMonthsLater) ----
  fromDateValue = computed(() => this.formatDate(this.fromDate()));
  toDateValue = computed(() => this.formatDate(this.toDate()));

  onFromDateInputChange(value: string): void {
    if (!value) return;
    const date = new Date(value);
    this.fromDate.set(date);
    const diffMonths = (new Date().getTime() - date.getTime()) / (1000 * 60 * 60 * 24 * 30.436875);
    if (diffMonths >= 3) {
      const capped = new Date(date);
      capped.setMonth(capped.getMonth() + 3);
      capped.setDate(capped.getDate() - 1);
      this.toDate.set(capped);
    } else {
      this.toDate.set(new Date());
    }
  }

  onToDateInputChange(value: string): void {
    if (!value) return;
    this.toDate.set(new Date(value));
  }

  private validateDate(): boolean {
    if (!this.fromDate() || !this.toDate()) {
      this.notify.error('Please enter valid date');
      return false;
    }
    if (this.fromDate() > this.toDate()) {
      this.notify.error('From date should be lesser than to date');
      return false;
    }
    return true;
  }

  private formatDate(d: Date): string {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
  }

  // ---- search (mirrors GetGRNList) ----
  searchGrn(): void {
    if (!this.validateDate()) return;

    this.api
      .searchGrn({
        fromDate: this.formatDate(this.fromDate()),
        toDate: this.formatDate(this.toDate()),
        storeId: this.selectedStore().id || undefined,
        requestingStoreId: this.selectedFromstore().id || undefined,
        grnNumber: this.grnNumber() || undefined,
        grnTypeId: this.selectedGrnType().id || undefined,
        vendorName: this.vendorSearchTerm() || undefined,
        vendorId: this.vendorId(),
        statusId: this.selectedStatus().id || undefined,
        unitId: this.unitId,
        itemId: this.itemId(),
      })
      .subscribe({
        next: (rows) => {
          this.grnList.set(rows ?? []);
          this.currentPage.set(1);
          this.selectedGrnIds.set(new Set());
          this.selectAllRows.set(false);
        },
        error: () => this.notify.error('Failed to load GRN list'),
      });
  }

  // ---- vendor typeahead ----
  onVendorSearchChange(term: string): void {
    this.vendorSearchTerm.set(term);
    this.vendorId.set(undefined);
    if (term.length >= 1) this.vendorSearch$.next(term);
  }

  selectVendor(vendor: VendorSearchResult): void {
    this.vendorSearchTerm.set(vendor.vendorName);
    this.vendorId.set(vendor.id);
    this.vendorResults.set([]);
  }

  // ---- item typeahead ----
  onItemSearchChange(term: string): void {
    this.itemSearchTerm.set(term);
    if (term.length >= 2) this.itemSearch$.next(term);
  }

  selectItem(item: ItemSearchResult): void {
    this.itemSearchTerm.set(item.details.split('|')[1] ?? item.details);
    this.itemId.set(item.id);
    this.itemResults.set([]);
  }

  // ---- pagination ----
  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- row eligibility (mirrors isDisable/isDisableButton) ----
  isDisable(row: GrnListRow): boolean {
    return ['Approved', 'Rejected', 'Cancelled'].includes(row.approvalStatus);
  }

  isDisableButton(row: GrnListRow): boolean {
    return row.status !== 'New';
  }

  // ---- selection (mirrors SelectAllGRN/SelectGRN) ----
  toggleSelectAll(): void {
    const next = new Set<number>();
    if (this.selectAllRows()) {
      for (const row of this.grnList()) {
        if (!this.isDisableButton(row)) {
          row.isSelected = true;
          next.add(row.id);
        }
      }
    } else {
      for (const row of this.grnList()) row.isSelected = false;
    }
    this.selectedGrnIds.set(next);
  }

  toggleSelect(row: GrnListRow): void {
    const next = new Set(this.selectedGrnIds());
    if (row.isSelected) next.add(row.id);
    else next.delete(row.id);
    this.selectedGrnIds.set(next);
    this.selectAllRows.set(this.grnList().every((r) => this.isDisableButton(r) || r.isSelected));
  }

  // ---- approve flow (mirrors OpenPopup('Update', 'confirmSave', {}) + UpdateGRN) ----
  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  private lookupStatusId(name: string): number | undefined {
    return this.statusMasterList().find((s) => s.status === name)?.id;
  }

  confirmApprove(): void {
    if (this.selectedGrnIds().size === 0) {
      this.notify.error('Please select GRN to approve');
      this.showConfirmModal.set(false);
      return;
    }
    this.saveBtnDisabled.set(true);
    const statusId = this.lookupStatusId('Pending');
    const approvalStatusId = this.lookupStatusId('Pending Approval');
    this.api.approveGrn(Array.from(this.selectedGrnIds()), statusId ?? 0, approvalStatusId ?? 0, this.userId).subscribe({
      next: (res) => {
        this.selectedGrnIds.set(new Set());
        this.selectAllRows.set(false);
        this.showConfirmModal.set(false);
        this.saveBtnDisabled.set(false);
        this.notify.success(res.message ?? 'Approved successfully');
        this.searchGrn();
      },
      error: () => {
        this.showConfirmModal.set(false);
        this.saveBtnDisabled.set(false);
        this.notify.error('Approve failed');
      },
    });
  }

  // ---- cancel flow (mirrors OpenPopup('Cancel', 'cancelGRN', grn) + UpdateGRN) ----
  openCancelModal(row: GrnListRow): void {
    this.cancelTargetId = row.id;
    this.showCancelModal.set(true);
  }

  closeCancelModal(): void {
    this.showCancelModal.set(false);
    this.rejectNote.set('');
    this.cancelReason.set(undefined);
  }

  onCancelReasonChange(reasonId: number): void {
    this.cancelReason.set(reasonId);
    this.isReasonValid.set(false);
  }

  confirmCancel(): void {
    if (!this.cancelReason()) {
      this.isReasonValid.set(true);
      return;
    }
    if (this.cancelTargetId == null) return;
    this.saveBtnDisabled.set(true);
    const statusId = this.lookupStatusId('Closed');
    const approvalStatusId = this.lookupStatusId('Cancelled');
    this.api
      .cancelGrn(this.cancelTargetId, statusId ?? 0, approvalStatusId ?? 0, this.cancelReason(), this.rejectNote(), this.userId)
      .subscribe({
        next: (res) => {
          this.selectedGrnIds.set(new Set());
          this.closeCancelModal();
          this.saveBtnDisabled.set(false);
          this.notify.success(res.message ?? 'Cancelled successfully');
          this.searchGrn();
        },
        error: () => {
          this.saveBtnDisabled.set(false);
          this.notify.error('Cancel failed');
        },
      });
  }

  // ---- print (stub, see class comment) ----
  rePrintBillSlip(_row: GrnListRow): void {
    this.notify.warning('Report generation is not available in this preview environment');
  }
}
