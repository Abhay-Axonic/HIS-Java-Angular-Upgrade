import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { CorrectiveMaintenanceWorklistService } from './corrective-maintenance-worklist.service';
import { CmwRow, CompletionObj } from './corrective-maintenance-worklist.models';
import { createPaginationState, PaginationState, searchData, setPage, setFirstPage, setPreviousPage, setNextPage, setLastPage, getPagesList } from '../../core/client-pagination.util';

/**
 * Rebuild of the legacy `correctiveMaintenanceWorklist` state -- see
 * correctiveMaintenanceWorklistController.js (259 lines) +
 * corrective-maintenance-worklist.html (357 lines). No data.moduleId (no
 * resolve block), real cookieObject guard only.
 *
 * Sharpened Step 1 checklist applied:
 * - Wrong-workflow-category / structural-surprise check: the view has
 *   TWO of its three `<tr ng-repeat>` blocks (`corectiveMaintenanceList`,
 *   `corectiveMaintenanceWorklist`) entirely HTML-commented-out
 *   (corrective-maintenance-worklist.html:138-219) -- only the third,
 *   `pagination.data`-driven table is live. Confirmed by reading the
 *   CURRENT view directly, not assumed from the controller alone -- the
 *   controller still fetches into all the underlying scope variables,
 *   but only one is ever actually rendered.
 * - Bug baked into the "reference" screen itself: because only the
 *   `pagination.data` table is live, `getCurrentIndex(info, $index, 2)`
 *   is ALWAYS called with `label=2` (hardcoded in the one live
 *   ng-repeat) -- the controller's `if(label==1)` branch is dead,
 *   unreachable code. The `else` branch's own `$scope.corectiveMaintenanceWorklist=info`
 *   (overwriting a whole array with one clicked row) looks like real
 *   data corruption on paper, but is confirmed HARMLESS in practice:
 *   nothing renders `corectiveMaintenanceWorklist` anymore (that table
 *   is the dead one), so the overwrite has zero observable effect. Not
 *   modeled -- see `openCompletionDetails()` below, which reads
 *   straight from the clicked row instead of replicating the inert
 *   scope-variable side effect.
 * - Race condition, logged as a new finding (not fixed, per standing
 *   instruction): `getCorectiveMaintenance()` and
 *   `getCorectiveMaintenanceWorklist()` both fire on load with no
 *   sequencing between them. The FIRST resets `finalList = []` and
 *   rebuilds it fresh from its own response; the SECOND does not reset
 *   `finalList`, it only appends. If the worklist call's response
 *   arrives before the plain-maintenance call's response, the later
 *   arrival silently wipes the worklist rows that were already merged
 *   in. Reproduced faithfully below (both calls fire independently, no
 *   sequencing added) -- this is legacy's real, existing behavior, not
 *   something this rebuild introduces.
 */
@Component({
  selector: 'app-corrective-maintenance-worklist',
  imports: [FormsModule],
  templateUrl: './corrective-maintenance-worklist.html',
  styleUrl: './corrective-maintenance-worklist.scss',
})
export class CorrectiveMaintenanceWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(CorrectiveMaintenanceWorklistService);

  private unitId = 0;
  private organizationId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  requestCriticalityId = 0;
  statusId = 0;
  searchKeyword = '';

  private finalList: CmwRow[] = [];
  pagination = signal<PaginationState<CmwRow>>(createPaginationState<CmwRow>());

  showCompletionModal = signal(false);
  completionObj = signal<CompletionObj>({ completionDate: new Date(), completionTime: '', isComplete: false, completionRemak: '' });

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;

    this.search();
  }

  search(): void {
    this.getCorrectiveMaintenance();
    this.getCorrectiveMaintenanceWorklist();
  }

  private getCorrectiveMaintenance(): void {
    this.loader.start();
    this.api
      .getCorrectiveMaintenance({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        maintenanceStatusId: this.statusId,
        requestCriticalityId: this.requestCriticalityId || 0,
        storeId: 0,
        unitId: this.unitId,
        organizationId: this.organizationId,
        isWorklist: 1,
      })
      .subscribe({
        next: (res) => {
          const list = res.listObject ?? [];
          this.finalList = list.map((value) => ({ ...value, maintenanceWorklistStatusId: value['maintenanceStatusId'] as number | undefined }));
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  private getCorrectiveMaintenanceWorklist(): void {
    this.loader.start();
    this.api
      .getCorrectiveMaintenanceWorklist({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        maintenanceWorklistStatusId: this.statusId,
        requestCriticalityId: this.requestCriticalityId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        isWorklist: 1,
      })
      .subscribe({
        next: (res) => {
          const list = res.listObject ?? [];
          this.finalList = [...this.finalList, ...list];
          if (this.finalList.length > 0) {
            const p = this.pagination();
            searchData(this.finalList, p);
            this.pagination.set({ ...p });
          }
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  setPageSize(): void {
    const p = this.pagination();
    searchData(this.finalList, p);
    this.pagination.set({ ...p });
  }

  goToDetails(info: CmwRow): void {
    this.router.navigate(['/detailsCorrectiveMaintenanceWorklist']);
  }

  openCompletionDetails(info: CmwRow): void {
    this.completionObj.set({
      completionDate: info.completionDate ?? '',
      completionTime: info.completionTime ?? '',
      isComplete: info.complete ?? false,
      completionRemak: info.completionRemak ?? '',
    });
    this.showCompletionModal.set(true);
  }

  setColor(referenceId: number | null | undefined): string {
    return referenceId != null ? 'red' : '';
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
