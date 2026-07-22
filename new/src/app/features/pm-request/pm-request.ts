import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import * as pg from '../../core/client-pagination.util';
import { PmRequestService } from './pm-request.service';
import {
  Month,
  PmRequestStore,
  PmRequestCriteria,
  PmAssetRequestRow,
  ManufacturerRow,
  VendorRow,
  AccessoryItemRow,
  InsuranceRow,
  AssetScheduleChecklistRow,
} from './pm-request.models';

/**
 * Rebuild of the legacy `PMRequest` state -- see pmRequestController.js
 * (425 lines), view PM-request.html + ng-included partials
 * views/common/accessories-details.html, views/common/PMS-details.html.
 *
 * Sibling of the already-rebuilt `PMSchedule` screen (pm-schedule.ts) --
 * same asset-management maintenance cluster, same PMS-details.html
 * partial, several byte-identical endpoints (manufacturer/vendor/
 * accessory lookups, checklist fetch, fetchScheduledCompletionAssignedData).
 * Kept as a single component file, matching PMSchedule's and
 * AssetScheduler's own convention for this screen family, rather than
 * fragmented into several small modal sub-components -- consistent with
 * how every other multi-modal screen in this cluster is actually built.
 *
 * No `data.moduleId` in legacy's own `.state('PMRequest', ...)` (confirmed
 * during Step 0/1 audit: no resolve block at all). Module ID 224 ("PM
 * Request") exists in adminPriviledges.json for sidebar-visibility-only
 * purposes today -- per the established `billingGroupMaster`-precedent
 * policy, this rebuild wires `data.moduleId: 224` into the route guard as
 * a disclosed, already-approved-pattern improvement (see app.routes.ts).
 *
 * Store-selection: legacy's own init() (pmRequestController.js:28-49) has
 * a REAL, live null-dereference crash here -- `cookieObjectStore` is
 * read via `JSON.parse(sessionStorage.getItem('selectedTopStore'))`
 * (`null` when absent), checked for `== undefined` (true), and the
 * store-popup is shown -- but immediately after that `if` block,
 * `$scope.storeId = cookieObjectStore.storeId` runs UNCONDITIONALLY,
 * throwing `TypeError: Cannot read properties of null` on any session
 * with no store selected yet. Same bug class as `assigningPickerDispenser`
 * (docs/ANGULAR_PENDING_FINDINGS.md entry 9) -- not fixed in legacy, not
 * reproduced here. This screen-specific concern (the popup/list-fetch
 * itself) is already handled screen-agnostically by StoreSelectionService,
 * used the same defensive way `assetScheduler`'s rebuild already does.
 *
 * Search filter (getAssetDetailsByCriteria): the whole `pmRequestObj` is
 * always POSTed (never conditionally, unlike buildOptionalParams()'s
 * "send only what's filled in" pattern elsewhere -- deliberately NOT used
 * here, since that's not what this screen's own legacy behavior does).
 * The backend response is then re-filtered CLIENT-SIDE with OR (not AND)
 * across all 5 criteria (pmRequestController.js:198) -- faithfully ported,
 * see getAssetDetailsByCriteria() below for the exact mechanics and two
 * further disclosed quirks found while auditing that one line.
 */
@Component({
  selector: 'app-pm-request',
  imports: [FormsModule, DatePipe],
  templateUrl: './pm-request.html',
  styleUrl: './pm-request.scss',
})
export class PmRequest {
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly storeSelection = inject(StoreSelectionService);
  private readonly api = inject(PmRequestService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private storeId = 0;

  storeList = signal<PmRequestStore[]>([]);
  months: Month[] = [
    { value: 1, text: 'January' },
    { value: 2, text: 'February' },
    { value: 3, text: 'March' },
    { value: 4, text: 'April' },
    { value: 5, text: 'May' },
    { value: 6, text: 'June' },
    { value: 7, text: 'July' },
    { value: 8, text: 'August' },
    { value: 9, text: 'September' },
    { value: 10, text: 'October' },
    { value: 11, text: 'November' },
    { value: 12, text: 'December' },
  ];
  /** Mirrors initYearList() (pmRequestController.js:410-419) -- the only
   *  year list the view actually renders. `$scope.years` (a separate
   *  50-previous-years array built at pmRequestController.js:112-121) is
   *  dead code: PM-request.html's year `<select>` binds `yearsList`, never
   *  `years` -- not ported. */
  yearsList: number[] = [];

  criteria: PmRequestCriteria;

  assetDetailsByCriteria = signal<PmAssetRequestRow[]>([]);
  pagination = signal(pg.createPaginationState<PmAssetRequestRow>('10'));

  /** No ng-change on legacy's search box (PM-request.html:111) -- like
   *  PMSchedule, this filters only the CURRENT PAGE's already-paginated
   *  slice (`pagination.data|filter:searchKeyword`), not the full result
   *  set. Ported as-is, not "corrected" to filter the full dataset. */
  searchKeyword = signal('');
  filteredPageData = computed(() => {
    const term = this.searchKeyword().toLowerCase();
    const data = this.pagination().data;
    if (!term) return data;
    return data.filter((row) => JSON.stringify(row).toLowerCase().includes(term));
  });

  // ---- PMS Details modal (4 tabs: Manufacturer, Accessory, Vendor, Insurance) ----
  showPmsDetailsModal = signal(false);
  pmsDetailsTab = signal(1);
  manufactureDataList = signal<ManufacturerRow[]>([]);
  vendorDataList = signal<VendorRow[]>([]);
  itemDataList = signal<AccessoryItemRow[]>([]);
  /** Always empty -- see InsuranceRow's doc comment; no fetch exists in legacy. */
  insuranceDataList = signal<InsuranceRow[]>([]);

  // ---- Checklist modal (read-only view, no add/remove -- legacy's Add
  // button is commented out in addViewChecklistModal, PM-request.html:236-242) ----
  showChecklistModal = signal(false);
  addChecklist1 = signal<AssetScheduleChecklistRow[]>([]);

  // ---- Schedule Details + Completion Details modals -- both populated by
  // the SAME setScheduleData(assetScheduleId) fetch (matching legacy: both
  // the Schedule and Completion row-action links call the identical
  // function, pmRequestController.js/PM-request.html:151,155) ----
  assetScheduleId = 0;
  showScheduleModal = signal(false);
  showCompletionModal = signal(false);
  scheduledDate = signal(toIsoDate(new Date()));
  scheduledTime = signal('');
  scheduledRemark = signal('');
  completionDate = signal(toIsoDate(new Date()));
  completionTime = signal('');
  completionRemark = signal('');

  constructor() {
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    // Defensive equivalent of legacy's crashing cookieObjectStore check --
    // see this class's doc comment above.
    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
      this.storeSelection.show();
    } else {
      this.storeId = this.storeSelection.getSelectedStoreId()!;
    }

    const now = new Date();
    this.criteria = {
      month: (now.getMonth() + 1).toString(),
      year: now.getFullYear().toString(),
      storeId: 0,
      itemName: '',
      processStatus: '',
      unitId: this.unitId,
      organizationId: this.organizationId,
    };

    const currentYear = now.getFullYear();
    for (let y = currentYear - 5; y <= currentYear + 5; y++) {
      this.yearsList.push(y);
    }

    this.loader.start();
    this.api.getDetailedStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.storeList.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });

    this.getAssetDetailsByCriteria();
  }

  /**
   * pmRequestController.js:176-213. Legacy always POSTs the full
   * `pmRequestObj`, then re-filters the response CLIENT-SIDE with OR
   * across all 5 criteria -- faithfully ported below, including two
   * further real quirks found while auditing this exact line:
   *
   * 1. `month`/`year` are unpadded strings ("7", "2026") but the value
   *    extracted from each row's `nextDate` is always zero-padded ("07")
   *    -- so the month criterion only ever coincidentally matches for
   *    Oct/Nov/Dec (the only months whose unpadded and zero-padded forms
   *    are identical). Not "fixed" by padding `criteria.month` -- that
   *    would change which rows a real search returns.
   * 2. `processStatus`: legacy's own raw `<select ng-model=...>` (no
   *    ng-options) binds the model to the option's STRING value, compared
   *    via `===` against the backend's numeric `processStatus` field --
   *    always false, so this criterion is dead in legacy today regardless
   *    of what the user picks. `PmRequestCriteria.processStatus` is kept
   *    typed `string` end-to-end and the template's `<option value="0">`
   *    (a plain, non-`ngValue` binding, matching legacy's own static
   *    markup) faithfully reproduces the same string-vs-number mismatch,
   *    not just its outcome. (`storeId`, by contrast, is deliberately
   *    normalized to a real `number` via `[ngValue]` -- the same
   *    convention every other rebuilt screen's numeric dropdowns already
   *    use -- since it's `0` by default either way and would be equally
   *    dead as a string; no observable behavior depends on which typing
   *    choice is used there.) Disclosed, not a business-logic fix.
   */
  getAssetDetailsByCriteria(): void {
    this.loader.start();
    this.api.getAssetDetailsByCriteria(this.criteria).subscribe({
      next: (res) => {
        this.loader.stop();
        const list = res.listObject ?? [];
        const { month, year, storeId, itemName, processStatus } = this.criteria;
        const matched: PmAssetRequestRow[] = [];
        for (const row of list) {
          const formatted = formatDMY(row.nextDate);
          const [, mnth, yr] = formatted.split('-');
          if (
            month === mnth ||
            year === yr ||
            storeId === row.storeId ||
            itemName === row.itemName ||
            (processStatus as unknown) === row.processStatus
          ) {
            matched.push(row);
          }
        }
        this.assetDetailsByCriteria.set(matched);
        // Legacy only calls PaginationService.searchData() when at least
        // one row matched (pmRequestController.js:203-205) -- on an empty
        // result, `pagination.data` is left exactly as it was, so the
        // previously-displayed page stays on screen. Faithfully ported:
        // no `else` branch clearing the table here.
        if (matched.length > 0) {
          const p = this.pagination();
          pg.searchData(matched, p);
          this.pagination.set({ ...p });
        }
      },
      error: () => this.loader.stop(),
    });
  }

  onPageSizeChange(size: string): void {
    const p = this.pagination();
    p.pageSize = size;
    pg.setTotalPages(p);
    this.pagination.set({ ...p });
  }
  goToPage(page: number): void {
    const p = this.pagination();
    pg.setPage(page, p);
    this.pagination.set({ ...p });
  }
  goToFirstPage(): void {
    const p = this.pagination();
    pg.setFirstPage(p);
    this.pagination.set({ ...p });
  }
  goToPreviousPage(): void {
    const p = this.pagination();
    pg.setPreviousPage(p);
    this.pagination.set({ ...p });
  }
  goToNextPage(): void {
    const p = this.pagination();
    pg.setNextPage(p);
    this.pagination.set({ ...p });
  }
  goToLastPage(): void {
    const p = this.pagination();
    pg.setLastPage(p);
    this.pagination.set({ ...p });
  }
  pagesList(): number[] {
    return pg.getPagesList(this.pagination());
  }

  /**
   * pmRequestController.js:145,217-263 combined -- legacy's PMS-details
   * `ng-click` calls all 3 lookups unconditionally, with no null guard on
   * manufacturerId/vendorId (PM-request.html:144-145). Ported the same
   * way (no `if (x != null)` guard added).
   */
  openPmsDetails(info: PmAssetRequestRow): void {
    this.pmsDetailsTab.set(1);

    this.api.getManufacturersByOrg(this.organizationId).subscribe({
      next: (list) => this.manufactureDataList.set((list ?? []).filter((m) => m.id === info.manufacturerId)),
    });

    // Known live-500 backend bug (see PmRequestService.getVendorsByUnit) --
    // let it fail through the app's standard error handling; just make
    // sure the tab renders an empty list instead of hanging.
    this.api.getVendorsByUnit(this.unitId, this.organizationId).subscribe({
      next: (list) => this.vendorDataList.set((list ?? []).filter((v) => v.id === info.vendorId)),
      error: () => this.vendorDataList.set([]),
    });

    this.api
      .getAccessoryItems(this.unitId, {
        assetTypeId: 2,
        assetCategoryId: info.assetCategoryId,
        productCategoryId: info.productCategoryId,
        storeId: this.storeId,
      })
      .subscribe({
        next: (list) => this.itemDataList.set((list ?? []).filter((i) => i.itemId === info.itemNameId)),
      });

    this.showPmsDetailsModal.set(true);
  }

  /**
   * pmRequestController.js:308-325. Legacy's fetch-guard
   * (`if(assetScheduleId != 0)`) only gates the API call -- the modal
   * itself always opens via `data-toggle`/`data-target`, independent of
   * the guard. Ported the same way: `showChecklistModal.set(true)` runs
   * unconditionally below.
   */
  getCheckListByAssetScheduleId(assetScheduleId: number): void {
    if (assetScheduleId !== 0) {
      this.loader.start();
      this.api.getCheckListByAssetScheduleId(assetScheduleId, this.unitId, this.organizationId).subscribe({
        next: (res) => {
          this.loader.stop();
          this.addChecklist1.set(res.listObject ?? []);
        },
        error: () => this.loader.stop(),
      });
    }
    this.showChecklistModal.set(true);
  }

  /** pmRequestController.js:327-349, shared by both row actions below. */
  private setScheduleData(assetScheduleId: number): void {
    this.assetScheduleId = assetScheduleId;
    this.api.fetchScheduledCompletionAssignedData(assetScheduleId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        const d = res.listObject?.[0];
        this.scheduledDate.set(d?.scheduledDate ? toIsoDate(d.scheduledDate) : '');
        this.scheduledTime.set(d?.scheduledTime ? toHHmm(d.scheduledTime) : '');
        this.scheduledRemark.set(d?.scheduledRemark ?? '');
        this.completionDate.set(d?.completionDate ? toIsoDate(d.completionDate) : '');
        this.completionTime.set(d?.completionTime ? toHHmm(d.completionTime) : '');
        this.completionRemark.set(d?.completionRemark ?? '');
      },
    });
  }

  openScheduleModal(assetScheduleId: number): void {
    this.setScheduleData(assetScheduleId);
    this.showScheduleModal.set(true);
  }

  openCompletionModal(assetScheduleId: number): void {
    this.setScheduleData(assetScheduleId);
    this.showCompletionModal.set(true);
  }

  /**
   * pmRequestController.js:350-378. The ONLY real enforcement of "Schedule
   * Date is required" -- the view's `required` attribute on that field is
   * inert (no wrapping `<form>`), so this manual check is what actually
   * matters for parity, not the HTML5 attribute.
   */
  saveChangedSchedule(): void {
    if (this.scheduledDate() === '' || this.scheduledTime() === '') {
      this.notify.error('Please Select Scheduled Date and Time');
      return;
    }
    this.loader.start();
    this.api
      .saveChangedSchedule({
        assetScheduleId: this.assetScheduleId,
        scheduledDate: this.scheduledDate(),
        // Legacy explicitly reformats scheduledTime via
        // moment(...).format('hh:mm:ss a') before sending -- ported below.
        scheduledTime: formatTime12Hour(this.scheduledTime()),
        scheduledRemark: this.scheduledRemark(),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Asset Schedule Saved Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.loader.stop(),
      });
    // Legacy does not close the modal or refresh the list after saving
    // (no .modal('hide'), no re-fetch of getAssetDetailsByCriteria) --
    // ported as-is; the user closes it manually via the header's Ã—.
  }

  /**
   * pmRequestController.js:380-408. Unlike scheduledTime above, legacy
   * sends completionTime RAW, with no moment() reformatting -- a real,
   * disclosed difference between these two otherwise-parallel saves,
   * ported faithfully rather than "made consistent" with the schedule save.
   */
  saveCompletionDetails(): void {
    if (this.completionDate() === '' || this.completionTime() === '') {
      this.notify.error('Please Select Completion Date and Time');
      return;
    }
    this.loader.start();
    this.api
      .saveCompletionDetails({
        assetScheduleId: this.assetScheduleId,
        completionDate: this.completionDate(),
        completionTime: this.completionTime(),
        completionRemark: this.completionRemark(),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Completion Details Saved Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

function toIsoDate(d: unknown): string {
  if (!d) return '';
  const date = new Date(d as string);
  if (isNaN(date.getTime())) return '';
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
}

function toHHmm(d: unknown): string {
  if (!d) return '';
  const date = new Date(d as string);
  if (isNaN(date.getTime())) return '';
  return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
}

/** Replicates moment(...).format('hh:mm:ss a') for an "HH:mm" 24h string input. */
function formatTime12Hour(hhmm: string): string {
  if (!hhmm) return '';
  const [h, m] = hhmm.split(':').map(Number);
  const period = h >= 12 ? 'pm' : 'am';
  const hour12 = h % 12 === 0 ? 12 : h % 12;
  return `${String(hour12).padStart(2, '0')}:${String(m).padStart(2, '0')}:00 ${period}`;
}

/** Replicates moment(nextDate).format('DD-MM-YYYY'), used only for the
 *  OR-filter's zero-padded month/year extraction (see the doc comment on
 *  getAssetDetailsByCriteria()). */
function formatDMY(d: unknown): string {
  if (!d) return '';
  const date = new Date(d as string);
  if (isNaN(date.getTime())) return '';
  const dd = String(date.getDate()).padStart(2, '0');
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const yyyy = date.getFullYear();
  return `${dd}-${mm}-${yyyy}`;
}
