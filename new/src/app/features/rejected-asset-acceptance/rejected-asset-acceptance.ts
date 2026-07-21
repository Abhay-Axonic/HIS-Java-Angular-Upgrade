import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { ScreenDataService } from '../../core/screen-data.service';
import { ReportService } from '../../core/report.service';
import { RejectedAssetAcceptanceService } from './rejected-asset-acceptance.service';
import { RaaOption, RaaRow } from './rejected-asset-acceptance.models';
import { createPaginationState, PaginationState, searchData, setFirstPage, setPreviousPage, setNextPage, setLastPage, setPage, getPagesList } from '../../core/client-pagination.util';

const ALL_OPTION: RaaOption = { id: 0 };

/**
 * Rebuild of the legacy `rejectedAssetAcceptance` state -- see
 * rejectedAssetAcceptanceController.js (290 lines). Real module-access
 * resolve block (moduleId 218, ctrl:269), parameterized via canActivate's
 * data.moduleId, same pattern as assetIssueAcceptance's moduleId:215 guard
 * -- not wired here, app.routes.ts is owned centrally.
 *
 * `GetRejectedAssetAcceptance()` (ctrl:194-230) is the only real search
 * function -- `searchAssetReturnAcceptance` (ctrl:99-133) posts to
 * ASSET_RETURN_ACCEPTANCE.SEARCH_API by mistake and is never called from
 * the view or anywhere else in this controller; intentionally not ported.
 *
 * Reuses ScreenDataService (screenName 'rejectedAssetAcceptance', same
 * accumulate-only contract as assetIssueAcceptance) and
 * CommonDetailHandoffService for the "Details" link, which navigates to
 * the separate `detailsRejectedAssetAcceptance` state (registered in its
 * own controller/asset/detailsRejectedAssetAcceptance.js:343-344, not
 * built as part of this batch).
 *
 * Print icon (view:219, bare `ng-click="assetReturnAcceptance(item)"` on
 * `$scope`, ctrl:134-150) -- same controllerAs/$scope pattern already
 * confirmed harmless for asset-return-acceptance's own print button;
 * rebuilt via ReportService the same way, same REPORT_API_URI1 endpoint
 * (`/api/billing/unit/bill/Report`, global.url.config.js:266) since both
 * screens' print buttons hit that same generic report-generation API,
 * just with a different reportDesignFilePath/exportFileName.
 *
 * `self.model.Currency` (ctrl:53-55, `CurrencyService.GetCurrency()`) is
 * populated but never referenced anywhere in the view -- a dead binding,
 * skipped here for the same reason `searchAssetReturnAcceptance` is
 * skipped (unreachable/unused, not a rendering dependency).
 */
@Component({
  selector: 'app-rejected-asset-acceptance',
  imports: [FormsModule],
  templateUrl: './rejected-asset-acceptance.html',
  styleUrl: './rejected-asset-acceptance.scss',
})
export class RejectedAssetAcceptance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly screenData = inject(ScreenDataService);
  private readonly report = inject(ReportService);
  private readonly api = inject(RejectedAssetAcceptanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  fromStoreList = signal<RaaOption[]>([ALL_OPTION]);
  toStoreList = signal<RaaOption[]>([ALL_OPTION]);
  statusList = signal<RaaOption[]>([ALL_OPTION]);

  fromStore: RaaOption = ALL_OPTION;
  toStore: RaaOption = ALL_OPTION;
  status: RaaOption = ALL_OPTION;
  returnNo = '';

  pagination = signal<PaginationState<RaaRow>>(createPaginationState<RaaRow>());
  searchKeyword = '';

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.populateDropdowns();
  }

  private populateDropdowns(): void {
    this.loader.start();
    this.api.getDropdowns(this.unitId, this.organizationId, this.userId).subscribe({
      next: (res) => {
        this.fromStoreList.set([ALL_OPTION, ...(res.fromStore ?? [])]);
        this.toStoreList.set([ALL_OPTION, ...(res.toStore ?? [])]);
        this.statusList.set([ALL_OPTION, ...(res.status ?? [])]);
        this.loader.stop();
        this.initialCall();
      },
      error: () => this.loader.stop(),
    });
  }

  private initialCall(): void {
    const entries = this.screenData.getScreenData();
    const mine = entries.filter((e) => e.screenName === 'rejectedAssetAcceptance');
    mine.forEach((entry) => {
      const r = entry.searchrequest as Record<string, { id: number }>;
      this.fromStore = this.fromStoreList().find((s) => s.id === r['fromStore']?.id) ?? this.fromStore;
      this.toStore = this.toStoreList().find((s) => s.id === r['toStore']?.id) ?? this.toStore;
      this.status = this.statusList().find((s) => s.id === r['status']?.id) ?? this.status;
      this.returnNo = (r['returnNo'] as unknown as string) ?? this.returnNo;
      this.fromDate.set((r['fromDate'] as unknown as string) ?? this.fromDate());
      this.toDate.set((r['toDate'] as unknown as string) ?? this.toDate());
    });
    this.search();
  }

  private validateDate(): boolean {
    if (this.fromDate() > this.toDate()) {
      this.notify.error('From Date should lesser than To Date');
      return false;
    }
    if (!this.fromDate() || !this.toDate()) {
      this.notify.error('Enter Valid Date');
      return false;
    }
    return true;
  }

  search(): void {
    if (!this.validateDate()) return;

    this.loader.start();
    this.api
      .search({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        fromStoreId: this.fromStore?.id || undefined,
        toStoreId: this.toStore?.id || undefined,
        statusId: this.status?.id || undefined,
        returnNo: this.returnNo || undefined,
        unitId: this.unitId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          const p = this.pagination();
          p.data = [];
          if (res && res.length > 0) {
            searchData(res, p);
          }
          this.pagination.set({ ...p });
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Error');
        },
      });

    this.screenData.setScreenData({
      screenName: 'rejectedAssetAcceptance',
      searchrequest: {
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        fromStore: this.fromStore,
        toStore: this.toStore,
        status: this.status,
        returnNo: this.returnNo,
      },
    });
  }

  navigateToDetail(item: RaaRow): void {
    this.handoff.setDataId(item.id);
    this.router.navigate(['/detailsRejectedAssetAcceptance']);
  }

  printReport(item: RaaRow): void {
    this.report.generate('/api/billing/unit/bill/Report', {
      appName: 'HIS',
      appSecretKey: 'f306b6eccd9d5069d2dc71afe139271a',
      reportDesignFilePath: 'rptRejectAssetReturnAcceptance.rpt',
      reportFormat: 'PDF',
      exportFileName: 'AssetReturnAcceptance',
      reportParams: [{ paramName: 'id', paramType: 'Integer', paramValue: item.id }],
    });
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
