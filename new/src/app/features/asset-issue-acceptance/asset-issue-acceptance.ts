import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { ScreenDataService } from '../../core/screen-data.service';
import { AssetIssueAcceptanceService } from './asset-issue-acceptance.service';
import { AiaOption, AiaRow } from './asset-issue-acceptance.models';
import { createPaginationState, PaginationState, searchData, setFirstPage, setPreviousPage, setNextPage, setLastPage, setPage, getPagesList } from '../../core/client-pagination.util';

const ALL_OPTION: AiaOption = { id: 0 };

/**
 * Rebuild of the legacy `assetIssueAcceptance` state -- see
 * assetIssueAcceptanceController.js (261 lines). Real module-access
 * resolve block (moduleId 215) -- parameterized via canActivate's
 * data.moduleId, matching legacy's own gate exactly, not omitted like
 * some sibling screens in this batch.
 *
 * Reuses ScreenDataService/CommonDetailHandoffService (both new in this
 * batch, shared across several Asset Management screens) rather than
 * one-off equivalents.
 */
@Component({
  selector: 'app-asset-issue-acceptance',
  imports: [FormsModule],
  templateUrl: './asset-issue-acceptance.html',
  styleUrl: './asset-issue-acceptance.scss',
})
export class AssetIssueAcceptance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly screenData = inject(ScreenDataService);
  private readonly api = inject(AssetIssueAcceptanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  fromStoreList = signal<AiaOption[]>([ALL_OPTION]);
  toStoreList = signal<AiaOption[]>([ALL_OPTION]);
  indentTypeList = signal<AiaOption[]>([ALL_OPTION]);
  statusList = signal<AiaOption[]>([ALL_OPTION]);
  priorityList = signal<AiaOption[]>([ALL_OPTION]);

  fromStore: AiaOption = ALL_OPTION;
  toStore: AiaOption = ALL_OPTION;
  indentType: AiaOption = ALL_OPTION;
  status: AiaOption = ALL_OPTION;
  priority: AiaOption = ALL_OPTION;
  issueNo = '';

  pagination = signal<PaginationState<AiaRow>>(createPaginationState<AiaRow>());
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
        this.indentTypeList.set([ALL_OPTION, ...(res.indentType ?? [])]);
        this.statusList.set([ALL_OPTION, ...(res.status ?? [])]);
        this.priorityList.set([ALL_OPTION, ...(res.priority ?? [])]);
        this.loader.stop();
        this.initialCall();
      },
    });
  }

  private initialCall(): void {
    const entries = this.screenData.getScreenData();
    const mine = entries.filter((e) => e.screenName === 'assetIssueAcceptance');
    mine.forEach((entry) => {
      const r = entry.searchrequest as Record<string, { id: number }>;
      this.fromStore = this.fromStoreList().find((s) => s.id === r['fromStore']?.id) ?? this.fromStore;
      this.toStore = this.toStoreList().find((s) => s.id === r['toStore']?.id) ?? this.toStore;
      this.indentType = this.indentTypeList().find((s) => s.id === r['indentType']?.id) ?? this.indentType;
      this.status = this.statusList().find((s) => s.id === r['status']?.id) ?? this.status;
      this.issueNo = (r['issueNo'] as unknown as string) ?? this.issueNo;
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
        indentTypeId: this.indentType?.id || undefined,
        priorityId: this.priority?.id || undefined,
        issueNo: this.issueNo || undefined,
        userId: this.userId,
        unitId: this.unitId,
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
        error: () => this.loader.stop(),
      });

    this.screenData.setScreenData({
      screenName: 'assetIssueAcceptance',
      searchrequest: {
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        fromStore: this.fromStore,
        toStore: this.toStore,
        status: this.status,
        indentType: this.indentType,
        issueNo: this.issueNo,
      },
    });
  }

  navigateToDetail(item: AiaRow): void {
    this.handoff.setDataId(item.id);
    this.router.navigate(['/detailsAssetIssueAcceptance']);
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
