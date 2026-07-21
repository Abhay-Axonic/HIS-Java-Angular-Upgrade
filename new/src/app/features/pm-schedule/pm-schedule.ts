import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import * as pg from '../../core/client-pagination.util';
import { PmScheduleService } from './pm-schedule.service';
import {
  PmScheduleUser,
  PmScheduleStore,
  PmAssetScheduleRow,
  PmScheduleCriteria,
  ManufacturerRow,
  VendorRow,
  AccessoryItemRow,
  AssetScheduleChecklistRow,
} from './pm-schedule.models';

/**
 * Rebuild of the legacy `PMSchedule` state -- see pmScheduleController.js
 * (422 lines). Full audit: docs/ANGULAR_PENDING_FINDINGS.md entries 17
 * (RESOLVED, re-verified here too), 21, 23 (both still open, faithfully
 * replicated), 35 (see note below).
 *
 * IMPORTANT structural difference from scheduleWorklist (screen 7):
 * legacy's `selectedTopStore`/storePopup check
 * (pmScheduleController.js:27-31) is entirely commented out -- this
 * screen does NOT show the shared store-selection modal at all. Entry
 * 37 does NOT apply here; StoreSelectionService is deliberately not
 * wired into this screen.
 *
 * Entry 35 (unguarded `.length = 0` crash when init() exits early) is
 * NOT reproduced here: this rebuild uses an Angular signal, initialized
 * as a field (`= signal([])`), which runs before the constructor body
 * and is always initialized regardless of guard state -- structurally
 * different from legacy's imperative `$scope.X = []` inside `init()`,
 * which only runs if execution reaches that line. Entry 35 itself
 * assesses this as low-severity/harmless (redirect still completes,
 * just one extra silent console error) -- there is no user-observable
 * behavior this omission changes; both apps redirect away identically
 * in the missing-appConfig case. Not a "fix" of a parity target, a
 * structural non-issue in how Angular signals initialize.
 *
 * PMS-details modal: legacy uses a global $rootScope tab counter
 * (`setFirstTabLevel`/`isSetFirstTabLevel`, app.js:1775-1778, same
 * mechanism already noted for bookView in entry 31) -- replicated here
 * with a local signal instead, a disclosed simplification (global
 * cross-screen tab-counter sharing has no meaningful analog in an
 * isolated Angular component). 3 of its 4 tabs are genuinely wired in
 * this controller (Manufacturer, Accessory/Item, Vendor); the 4th
 * (Insurance) is fed by a fetch function that's entirely commented out
 * in the source (`pmScheduleController.js:267-291`) -- confirmed always
 * empty, rendered as an empty tab, not fixed. The separate, fully static
 * `accessories-details.html`/`#accessoriesDetailsModal` (confirmed dead
 * everywhere it's used) is confirmed UNREACHABLE from this specific
 * screen -- no trigger in PM-schedule.html references it -- so it is
 * not modeled here at all.
 */
@Component({
  selector: 'app-pm-schedule',
  imports: [FormsModule, DatePipe],
  templateUrl: './pm-schedule.html',
  styleUrl: './pm-schedule.scss',
})
export class PmSchedule {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PmScheduleService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private assetMaintenanceSpecialityId: number | undefined;

  storeList = signal<PmScheduleStore[]>([]);
  usersList = signal<PmScheduleUser[]>([]);
  assetDetailsByScheduleCriteria = signal<PmAssetScheduleRow[]>([]);
  pagination = signal(pg.createPaginationState<PmAssetScheduleRow>('10'));

  /**
   * PMSchedule's search box has NO ng-change handler in legacy
   * (PM-schedule.html:118-119) -- unlike scheduleWorklist's explicit
   * `pagination.searchData()` call against the FULL dataset, this one
   * relies on AngularJS's live `filter` pipe applied directly to
   * `pagination.data` -- i.e. it filters only the CURRENT PAGE's already-
   * paginated slice, not the full result set. A real, if minor,
   * inconsistency between these two sibling screens -- replicated
   * exactly via this computed signal, not "corrected" to match
   * scheduleWorklist's behavior.
   */
  searchKeyword = signal('');
  filteredPageData = computed(() => {
    const term = this.searchKeyword().toLowerCase();
    const data = this.pagination().data;
    if (!term) return data;
    return data.filter((row) => JSON.stringify(row).toLowerCase().includes(term));
  });

  criteria: PmScheduleCriteria;

  // PMS Details modal
  showPmsDetailsModal = signal(false);
  pmsDetailsTab = signal(1);
  manufactureDataList = signal<ManufacturerRow[]>([]);
  vendorDataList = signal<VendorRow[]>([]);
  itemDataList = signal<AccessoryItemRow[]>([]);

  // Checklist modal
  showChecklistModal = signal(false);
  addChecklist1 = signal<AssetScheduleChecklistRow[]>([]);

  // Schedule (assign) modal
  showScheduleModal = signal(false);
  assetScheduleId: number | undefined;
  assignTo = signal<number | undefined>(undefined);
  assignOn = signal<Date | string | undefined>(undefined);
  assignTime = signal('');
  assignRemark = signal('');

  constructor() {
    if (this.session.appConfig == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'appConfigMissing' } });
      this.criteria = { fromDate: new Date(), toDate: new Date(), storeId: 0, pmsType: '', processStatus: '', unitId: 0, organizationId: 0 };
      return;
    }

    this.assetMaintenanceSpecialityId = this.session.appConfig.assetMaintenanceSpecialityId;
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.criteria = {
      fromDate: new Date(),
      toDate: new Date(),
      storeId: 0,
      pmsType: '',
      processStatus: '',
      unitId: this.unitId,
      organizationId: this.organizationId,
    };

    this.loader.start();
    this.api.getDetailedStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => this.storeList.set(res.listObject ?? []),
    });

    if (this.assetMaintenanceSpecialityId != null) {
      this.api
        .getEmployeeListBySpecialityAndSubSpeciality(this.organizationId, this.unitId, this.assetMaintenanceSpecialityId)
        .subscribe({
          next: (res) => this.usersList.set(res.status === 'success' ? (res.listObject ?? []) : []),
        });
    }

    this.getAssetDetailsByCriteria();
  }

  getAssetDetailsByCriteria(): void {
    this.loader.start();
    this.api.getAssetDetailsByCriteria(this.criteria).subscribe({
      next: (res) => {
        this.loader.stop();
        const list = res.listObject ?? [];
        const fDate = formatDate(this.criteria.fromDate);
        const tDate = formatDate(this.criteria.toDate);
        const store = this.criteria.storeId;
        const pmstype = this.criteria.pmsType;
        const proStatus = this.criteria.processStatus;

        const matched: PmAssetScheduleRow[] = [];
        for (const row of list) {
          const newDate = formatDate(row.nextDate);
          // Entry 21, faithfully replicated: OR, not AND.
          if (fDate >= newDate || tDate <= newDate || store === row.storeId || pmstype === row.pmsType || String(proStatus) === String(row.processStatus)) {
            matched.push(row);
          }
        }
        this.assetDetailsByScheduleCriteria.set(matched);
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

  getAssignNameById(id: number | undefined): string {
    if (id == null || id === 0) return '';
    return this.usersList().find((u) => u.userId === id)?.name ?? '';
  }

  openPmsDetails(info: PmAssetScheduleRow): void {
    this.pmsDetailsTab.set(1);
    if (info.manufacturerId != null) {
      this.api.getManufacturersByOrg(this.organizationId).subscribe({
        next: (list) => this.manufactureDataList.set((list ?? []).filter((m) => m.id === info.manufacturerId)),
      });
    }
    if (info.vendorId != null) {
      this.api.getVendorsByUnit(this.unitId, this.organizationId).subscribe({
        next: (list) => this.vendorDataList.set((list ?? []).filter((v) => v.id === info.vendorId)),
      });
    }
    this.api
      .getAccessoryItems(this.unitId, {
        assetTypeId: 2,
        assetCategoryId: info.assetCategoryId,
        productCategoryId: info.productCategoryId,
        storeId: this.criteria.storeId,
      })
      .subscribe({
        next: (list) => this.itemDataList.set((list ?? []).filter((i) => i.itemId === info.itemNameId)),
      });
    this.showPmsDetailsModal.set(true);
  }

  getCheckListByAssetScheduleId(assetScheduleId: number): void {
    if (assetScheduleId !== 0) {
      this.loader.start();
      this.api.getCheckListByAssetScheduleId(assetScheduleId, this.unitId, this.organizationId).subscribe({
        next: (res) => {
          this.loader.stop();
          this.addChecklist1.set(res.listObject ?? []);
        },
      });
    }
    this.showChecklistModal.set(true);
  }

  setId(data: PmAssetScheduleRow): void {
    this.assetScheduleId = data.assetScheduleId;
    this.api.fetchScheduledCompletionAssignedData(this.assetScheduleId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        const d = res.listObject?.[0];
        if (d) {
          this.assignTo.set(d.assignTo);
          this.assignOn.set(new Date());
          this.assignTime.set(formatTime(new Date()));
          this.assignRemark.set(d.assignRemark ?? '');
        }
      },
    });
    this.showScheduleModal.set(true);
  }

  saveAssignDetails(): void {
    if (this.assignTo() == null) {
      this.notify.warning('Please select assign to..');
      return;
    }
    if (this.assignOn() == null) {
      this.notify.warning('Please select assign date..');
      return;
    }

    this.loader.start();
    this.api
      .saveAssignDetails({
        assetScheduleId: this.assetScheduleId,
        assignTo: this.assignTo()!,
        assignOn: this.assignOn()!,
        assignTime: this.assignTime(),
        assignRemark: this.assignRemark(),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Schedule Assigned Successfully...!!');
            this.showScheduleModal.set(false);
            this.getAssetDetailsByCriteria();
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.loader.stop(),
      });
  }
}

function formatDate(d: Date | string | undefined): string {
  if (!d) return '';
  const date = new Date(d);
  const dd = String(date.getDate()).padStart(2, '0');
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const yyyy = date.getFullYear();
  return `${dd}-${mm}-${yyyy}`;
}

function formatTime(d: Date): string {
  return ` ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`;
}
