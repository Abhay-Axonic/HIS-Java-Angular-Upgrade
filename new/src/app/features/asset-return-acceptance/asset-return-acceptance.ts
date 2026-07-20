import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { ReportService } from '../../core/report.service';
import { AssetReturnAcceptanceService } from './asset-return-acceptance.service';
import { AraOption, AraRow } from './asset-return-acceptance.models';
import { createPaginationState, PaginationState, searchData, setFirstPage, setPreviousPage, setNextPage, setLastPage, setPage, getPagesList } from '../../core/client-pagination.util';

const ALL_OPTION: AraOption = { id: 0 };

/**
 * Rebuild of the legacy `assetReturnAcceptance` state -- see
 * assetReturnAcceptanceController.js (197 lines). Real module-access
 * resolve block (moduleId 217).
 *
 * `$scope.assetReturnAcceptance` (the print button's handler) is bound
 * on plain `$scope` while everything else in that controller is
 * `self.model.X` (controllerAs) -- checked against the view
 * (asset-return-acceptance.html:267 calls it as a bare
 * `assetReturnAcceptance(item)`, not `vm.model.X`) and confirmed this
 * still resolves correctly in AngularJS: `controllerAs` only adds a
 * `vm` property to the same underlying $scope, it doesn't remove access
 * to $scope's own properties. Not a bug -- rebuilt as a normal component
 * method like everything else, since Angular has no equivalent
 * ambiguity to preserve.
 */
@Component({
  selector: 'app-asset-return-acceptance',
  imports: [FormsModule],
  templateUrl: './asset-return-acceptance.html',
  styleUrl: './asset-return-acceptance.scss',
})
export class AssetReturnAcceptance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly report = inject(ReportService);
  private readonly api = inject(AssetReturnAcceptanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  fromStoreList = signal<AraOption[]>([ALL_OPTION]);
  toStoreList = signal<AraOption[]>([ALL_OPTION]);
  statusList = signal<AraOption[]>([ALL_OPTION]);
  indentTypeList = signal<AraOption[]>([ALL_OPTION]);

  fromStore: AraOption = ALL_OPTION;
  toStore: AraOption = ALL_OPTION;
  status: AraOption = ALL_OPTION;
  indentType: AraOption = ALL_OPTION;
  returnNo = '';

  pagination = signal<PaginationState<AraRow>>(createPaginationState<AraRow>());

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.getDropdowns();
    this.search();
  }

  private getDropdowns(): void {
    this.loader.start();
    this.api.getDropdowns(this.unitId, this.organizationId, this.userId).subscribe({
      next: (res) => {
        this.fromStoreList.set([ALL_OPTION, ...(res.fromStore ?? [])]);
        this.toStoreList.set([ALL_OPTION, ...(res.toStore ?? [])]);
        this.statusList.set([ALL_OPTION, ...(res.status ?? [])]);
        this.indentTypeList.set([ALL_OPTION, ...(res.indentType ?? [])]);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  search(): void {
    this.loader.start();
    this.api
      .search({
        fromStoreId: this.fromStore?.id || undefined,
        toStoreId: this.toStore?.id || undefined,
        statusId: this.status?.id || undefined,
        returnNo: this.returnNo || undefined,
        unitId: this.unitId,
        fromDate: this.fromDate(),
        toDate: this.toDate(),
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          const list = (res ?? []).map((r) => ({ ...r, isChecked: false }));
          const p = this.pagination();
          p.data = [];
          if (list.length > 0) searchData(list, p);
          this.pagination.set({ ...p });
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Error');
        },
      });
  }

  assetReturnAcceptanceDetails(item: AraRow): void {
    this.handoff.setDataId(item.id);
    this.router.navigate(['/assetReturnAcceptanceDetails']);
  }

  printReport(item: AraRow): void {
    this.report.generate('/api/billing/unit/bill/Report', {
      appName: 'HIS',
      appSecretKey: 'f306b6eccd9d5069d2dc71afe139271a',
      reportDesignFilePath: 'rptAssetReturnAcceptance.rpt',
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
