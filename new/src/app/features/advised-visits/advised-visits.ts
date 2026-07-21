import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { AdvisedVisitsService } from './advised-visits.service';
import { AdvisedVisitRow, DoctorAutoFillOption } from './advised-visits.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Rebuild of legacy `advisedVisits` (OPD) -- see
 * advisedVisitsController.js (346 lines, plain $scope-style, no
 * controllerAs) / advised-visits.html (1261 lines).
 *
 * SCOPE, an owner-approved, pre-verified decision (see
 * docs/ANGULAR_PENDING_FINDINGS.md, this batch's entry -- same treatment
 * `boxMasters` got for its own smaller copy-paste debris): lines 157-1261
 * of the legacy view (bed-reservation, deposit-matrix,
 * payment-collection, bed-status, rejection modals, a stray inline
 * jQuery `<script>` that hardcodes the page title to "Pending Admission
 * Request", and a stray trailing `</body></html>`) are dead, copy-pasted
 * markup from an unrelated IPD screen -- independently re-verified here:
 * every single `ng-click`/`ng-model`/`ng-repeat` target referenced in
 * that range was grepped individually against the real controller and
 * returned ZERO matches, for all of them. NONE of that is rebuilt.
 * Additionally, the one `<modal visible="showModal">` at line 86 (inside
 * the "real" zone) is ALSO dropped -- `showModal` is never set anywhere
 * in the controller, so that modal can never actually open in legacy
 * either; reproducing an unreachable, always-closed modal has no
 * observable effect worth the code.
 *
 * FURTHER SCOPE NARROWING (this rebuild's own decision, disclosed, not
 * silently dropped): the real zone (lines 1-156) also wires a Print
 * button (`printAdvisedDateAppointment()`) and a CSV/PDF export button
 * (`getExcel()`), both hitting a generic Crystal-Reports-style
 * `reportService.generate`/`BillingGenericService` blob-download
 * subsystem (`/api/billing/unit/bill/Report`,
 * `/api/billing/unit/bill/Report/file`) shared across many unrelated
 * billing screens -- no Angular-side equivalent of that generic reporting
 * service exists yet in `new/src/app/core/`. Building one is out of
 * scope for a single screen's rebuild. NOT wired here; see report for
 * the explicit decision write-up. This means golden-trace re-audit will
 * show 2 of the real controller's on-load calls
 * (list + count) instead of 3 (the third being
 * `getMISReportFormattesMasterforShowReports`, which only exists to
 * support the excluded export button) -- a disclosed, deliberate gap,
 * not a miss.
 *
 * Real, in-scope behavior faithfully reproduced, including one confirmed
 * legacy quirk: `getAdvisedDateAppointmentListCount` (see
 * AdvisedVisitsService's doc comment) never accepts date/doctor filters,
 * so the pagination total always reflects ALL advised visits for the
 * unit, not the currently-filtered subset -- and the plain "search"
 * button (`masterList()` in legacy, called with zero arguments) always
 * resets to a plain offset-0/10-row page and never rebuilds the pager at
 * all, regardless of the current "Show N entries" selection. Both
 * reproduced here exactly, not fixed.
 */
@Component({
  selector: 'app-advised-visits',
  imports: [FormsModule],
  templateUrl: './advised-visits.html',
  styleUrl: './advised-visits.scss',
})
export class AdvisedVisits {
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(AdvisedVisitsService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));

  doctorSearchTerm = signal('');
  doctorOptions = signal<DoctorAutoFillOption[]>([]);
  private selectedDoctorId: number | undefined;

  pageSizeOptions = PAGE_SIZE_OPTIONS;
  noOfRecordsPerPage = signal<number>(PAGE_SIZE_OPTIONS[0]);

  searchKeyword = signal('');

  list = signal<AdvisedVisitRow[]>([]);
  totalCount = signal(0);
  currentPage = signal(1);

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.noOfRecordsPerPage())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  filteredList = computed(() => {
    const term = this.searchKeyword().trim().toLowerCase();
    if (!term) return this.list();
    return this.list().filter((row) => JSON.stringify(row).toLowerCase().includes(term));
  });

  constructor() {
    this.loadListAndCount();
  }

  private buildListPayload(offset: number, noOfRecordsPerPage: number): Record<string, unknown> {
    return {
      organizationId: this.organizationId,
      offset,
      noOfRecordsPerPage,
      unitId: this.unitId,
      fromDate: this.fromDate(),
      toDate: this.toDate(),
      doctorId: this.selectedDoctorId ?? null,
    };
  }

  /** Legacy's `initMasterList` -- fires the list fetch AND the (date/doctor-blind) count fetch together. */
  private loadListAndCount(): void {
    this.loader.start();
    this.api.search(this.buildListPayload(0, this.noOfRecordsPerPage())).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
    this.api.count(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
          this.currentPage.set(1);
        }
      },
    });
  }

  /**
   * Legacy's `masterList()` -- the search-icon click handler. Called with
   * zero arguments in legacy, so it ALWAYS refetches offset 0 / a 10-row
   * page and never touches the count/pager, regardless of the current
   * "Show N entries" selection -- faithfully reproduced, not "fixed" to
   * respect the current page size.
   */
  search(): void {
    this.loader.start();
    this.api.search(this.buildListPayload(0, 10)).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  onNoOfRecordsPerPageChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loader.start();
    this.api.search(this.buildListPayload((page - 1) * this.noOfRecordsPerPage(), this.noOfRecordsPerPage())).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  onDoctorSearchChange(term: string): void {
    this.doctorSearchTerm.set(term);
    if (!term) {
      this.doctorOptions.set([]);
      return;
    }
    this.api.searchDoctors(term, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.doctorOptions.set(res.listObject ?? []);
      },
    });
  }

  selectDoctor(option: DoctorAutoFillOption): void {
    this.selectedDoctorId = option.doctorId;
    const label = `Dr. ${option.firstName ?? ''} ${option.middleName ?? ''} ${option.lastName ?? ''}`
      .replace(/\s+/g, ' ')
      .trim();
    this.doctorSearchTerm.set(label);
    this.doctorOptions.set([]);
  }
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
