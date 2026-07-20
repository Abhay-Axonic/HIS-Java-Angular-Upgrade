import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { AssigningPickerDispenserService } from './assigning-picker-dispenser.service';
import {
  AssigningPickerRow,
  CancelReason,
  DoctorTypeaheadResult,
  DropdownOption,
  PatientTypeaheadResult,
} from './assigning-picker-dispenser.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
const ALL_VISIT_TYPE: DropdownOption = { id: 0, type: 'All' };
const ALL_STATUS: DropdownOption = { id: 0, status: 'All' };

/**
 * Parity rebuild of
 * views/PharmacyInventory/InPatient/assigning-picker-dispenser.html +
 * resources/assets/js/controller/pharmacy/assigningPickerDispenserController.js.
 * Second pilot batch (controllerAs:'vm' self-registered screens). This
 * screen's wiring was confirmed sound (unlike two other candidates in this
 * same batch) -- almost all `vm.model.*` references resolve correctly.
 *
 * **Deliberate deviations, not bugs in this rebuild:**
 * - `CurrencyService.GetCurrency()` is still fired on load (matching
 *   legacy's real network activity, for golden-trace parity) even though
 *   its result is never used anywhere -- confirmed unused in legacy too,
 *   same "real call, discarded result" shape as this pilot batch's
 *   `periodicityService` writeup.
 * - The store-switch flow (`popupStore`, the `#pharmacyPopupBeforLeftexpand`
 *   modal) and the patient-photo "document flipbook" modal
 *   (`patientDocumnetInformation`/`generateFlipbookData`, which calls a
 *   hardcoded external AWS API Gateway URL with an embedded API key) are
 *   both out of scope -- app-shell-level and third-party-integration
 *   concerns respectively, not this screen's own audited behavior. Report
 *   printing (`rePrintBillSlip`-equivalent `printAssigningPickerDispenserReport`)
 *   is stubbed with a notice, same precedent as this pilot batch's `GRN`
 *   and the first pilot's `inputOutputCharts`.
 * - Legacy's `sessionStorage.getItem('selectedTopStore')` read has a real
 *   crash bug (dereferences `.storeName` before checking for `null`) --
 *   not reproduced; this rebuild checks for `null` first, the same
 *   "use the safe pattern, don't reproduce the crash" precedent already
 *   established for `organizationMaster`'s `$state`-injection fix.
 * - Legacy's stale-pagination-metadata bug (an empty search result leaves
 *   the previous search's page count/rows visible underneath "No Records
 *   Found") is architecturally not reproducible here -- `pagedList`/
 *   `totalPages` are computed signals that always re-derive from the
 *   current `rows` signal, the same "self-correcting by construction"
 *   deviation already documented for `insuranceMaster`'s ledger backfill
 *   (`docs/ANGULAR_PENDING_FINDINGS.md` entry 4).
 */
@Component({
  selector: 'app-assigning-picker-dispenser',
  imports: [FormsModule, DatePipe],
  templateUrl: './assigning-picker-dispenser.html',
  styleUrl: './assigning-picker-dispenser.scss',
})
export class AssigningPickerDispenser {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AssigningPickerDispenserService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  storeName = signal(this.readStoreName());
  userAccess = signal(true);

  // ---- search filters (mirrors self.model.*) ----
  fromDate = signal<Date>(new Date());
  toDate = signal<Date>(new Date());
  visitTypeDataList = signal<DropdownOption[]>([ALL_VISIT_TYPE]);
  statusDataList = signal<DropdownOption[]>([ALL_STATUS]);
  selectedVisitType = signal<DropdownOption>(ALL_VISIT_TYPE);
  selectedStatus = signal<DropdownOption>(ALL_STATUS);

  patientName = signal('');
  patientResults = signal<PatientTypeaheadResult[]>([]);
  private readonly patientSearch$ = new Subject<string>();
  private patientId: number | undefined;

  uhid = signal('');
  uhidResults = signal<PatientTypeaheadResult[]>([]);
  private readonly uhidSearch$ = new Subject<string>();

  doctorName = signal('');
  doctorResults = signal<DoctorTypeaheadResult[]>([]);
  private readonly doctorSearch$ = new Subject<string>();

  // ---- list + client-side pagination (mirrors PaginationService) ----
  rows = signal<AssigningPickerRow[]>([]);
  searchKeyword = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  filteredRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.rows();
    return this.rows().filter((r) =>
      Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)),
    );
  });
  totalPages = computed(() => Math.max(1, Math.ceil(this.filteredRows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedRows = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.filteredRows().slice(offset, offset + limit);
  });

  // ---- cancel modal ----
  reasons = signal<CancelReason[]>([]);
  showCancelModal = signal(false);
  cancelReason = signal<number | undefined>(undefined);
  rejectNote = signal('');
  isReasonValid = signal(false);
  private cancelTargetId: number | undefined;

  constructor() {
    this.api.getPharmacyRole(this.organizationId, this.userId, this.unitId).subscribe({
      next: (res) => {
        if (res.listObject && res.listObject.length > 0) this.userAccess.set(false);
      },
    });

    this.api.getCurrency(this.unitId).subscribe({ next: () => {} });
    this.api.getCancelReasons().subscribe({ next: (r) => this.reasons.set(r) });

    this.api.getDropdowns().subscribe({
      next: (res) => {
        this.visitTypeDataList.set([ALL_VISIT_TYPE, ...(res.visitType ?? []).filter((v) => v.id !== 1)]);
        this.statusDataList.set([ALL_STATUS, ...(res.status ?? [])]);
        this.search();
      },
      error: () => this.notify.error('Failed to load dropdowns'),
    });

    this.patientSearch$
      .pipe(
        debounceTime(0),
        switchMap((term) => this.api.searchPatientNames(this.unitId, term, this.selectedVisitType().id)),
      )
      .subscribe({ next: (r) => this.patientResults.set(r) });

    this.uhidSearch$
      .pipe(
        debounceTime(0),
        switchMap((term) => this.api.searchUhid(term, this.organizationId)),
      )
      .subscribe({ next: (r) => this.uhidResults.set(r) });

    this.doctorSearch$
      .pipe(
        debounceTime(0),
        switchMap((term) => this.api.searchDoctorNames(this.organizationId, this.unitId, term)),
      )
      .subscribe({ next: (r) => this.doctorResults.set(r) });
  }

  private readStoreName(): string {
    try {
      const raw = sessionStorage.getItem('selectedTopStore');
      if (!raw) return '';
      return JSON.parse(raw)?.storeName ?? '';
    } catch {
      return '';
    }
  }

  // ---- typeaheads (mirrors SearchpatientNames/SearchUhid/SearchdoctorNames) ----
  onPatientNameChange(term: string): void {
    this.patientName.set(term);
    if (term.length === 3) this.patientSearch$.next(term);
  }

  selectPatientByName(result: PatientTypeaheadResult): void {
    this.patientName.set(result.patientName ?? '');
    this.patientResults.set([]);
  }

  onUhidChange(term: string): void {
    this.uhid.set(term);
    if (term.length === 1) this.uhidSearch$.next(term);
  }

  selectPatientByUhid(result: PatientTypeaheadResult): void {
    this.uhid.set(result.uhid ?? '');
    this.uhidResults.set([]);
  }

  onDoctorNameChange(term: string): void {
    this.doctorName.set(term);
    if (term.length === 1) this.doctorSearch$.next(term);
  }

  selectDoctor(result: DoctorTypeaheadResult): void {
    this.doctorName.set(result.doctorName);
    this.doctorResults.set([]);
  }

  onVisitTypeChange(type: DropdownOption): void {
    this.selectedVisitType.set(type);
    this.patientName.set('');
    this.uhid.set('');
  }

  // ---- search (mirrors AssigningPickerDispenser()) ----
  private formatDate(d: Date): string {
    return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
  }

  search(): void {
    if (!this.fromDate() || !this.toDate()) {
      this.notify.error('Enter Valid Date');
      return;
    }
    if (this.fromDate() > this.toDate()) {
      this.notify.error('From Date should lesser than To Date');
      return;
    }
    this.api
      .search({
        fromDate: this.formatDate(this.fromDate()),
        toDate: this.formatDate(this.toDate()),
        visitType: this.selectedVisitType().id || undefined,
        patientId: this.patientId,
        patientName: this.patientName() || undefined,
        doctorName: this.doctorName() || undefined,
        statusId: this.selectedStatus().id || undefined,
        unitId: this.unitId,
      })
      .subscribe({
        next: (rows) => {
          this.rows.set(rows ?? []);
          this.currentPage.set(1);
          this.patientId = undefined;
        },
        error: () => this.notify.error('Failed to load list'),
      });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  isRowDisabled(row: AssigningPickerRow): boolean {
    return ['Completed', 'Partially Completed', 'Cancelled'].includes(row.status) || !this.userAccess();
  }

  // ---- cancel flow (mirrors cancelBillPopup/cancelItem) ----
  openCancelModal(row: AssigningPickerRow): void {
    this.cancelTargetId = row.prescriptionId;
    this.showCancelModal.set(true);
  }

  closeCancelModal(): void {
    this.showCancelModal.set(false);
    this.rejectNote.set('');
    this.cancelReason.set(undefined);
  }

  onCancelReasonChange(id: number): void {
    this.cancelReason.set(id);
    this.isReasonValid.set(false);
  }

  confirmCancel(): void {
    if (!this.cancelReason()) {
      this.isReasonValid.set(true);
      return;
    }
    if (this.cancelTargetId == null) return;
    this.api.cancel(this.cancelTargetId, this.cancelReason(), this.rejectNote()).subscribe({
      next: () => {
        this.closeCancelModal();
        this.notify.success('Cancelled successfully');
        this.search();
      },
      error: () => this.notify.error('Cancel failed'),
    });
  }

  printReport(_row: AssigningPickerRow): void {
    this.notify.warning('Report generation is not available in this preview environment');
  }

  openStorePopup(): void {
    this.notify.warning('Store switching is an app-shell concern, not part of this screen (out of scope)');
  }
}
