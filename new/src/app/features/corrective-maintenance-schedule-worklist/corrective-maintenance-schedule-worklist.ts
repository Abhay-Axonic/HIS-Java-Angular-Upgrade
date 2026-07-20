import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { CorrectiveMaintenanceScheduleWorklistService } from './corrective-maintenance-schedule-worklist.service';
import { CmswRow, CmswStore, CompletionObj } from './corrective-maintenance-schedule-worklist.models';
import { createPaginationState, PaginationState, searchData, setFirstPage, setPreviousPage, setNextPage, setLastPage, setPage, getPagesList } from '../../core/client-pagination.util';

/**
 * Rebuild of the legacy `correctiveMaintenanceScheduleWorklist` state --
 * see correctiveMaintenanceScheduleWorklistController.js (211 lines) +
 * corrective-maintenance-schedule-worklist.html (409 lines). No
 * data.moduleId (no resolve block), real cookieObject guard only.
 *
 * Sharpened Step 1 checklist applied, cross-referenced against its
 * naming-similar sibling `correctiveMaintenanceWorklist` (screen 16,
 * this same batch): despite the near-identical name and shared
 * "Completion Details" modal/pattern, this screen genuinely diverges --
 * it renders BOTH of its ng-repeat tables live (confirmed: no
 * commented-out markup here, unlike its sibling where one of two tables
 * is dead), and its `getCurrentIndex(index, label)` takes 2 params
 * matching both live call sites exactly (no argument-mismatch bug --
 * that risk was specific to the sibling's different, 3-param signature
 * colliding with a since-deleted 2-arg call site). Two real, separate,
 * correctly-wired worklists shown together: `getSchedulingList()`'s
 * result (with real completion dates) and
 * `getCorrectiveMaintenanceWorklist()`'s result (completion date always
 * shown as "-", scheduling not yet done). Confirmed via direct current
 * source read, not assumed from the sibling's shape.
 */
@Component({
  selector: 'app-corrective-maintenance-schedule-worklist',
  imports: [FormsModule],
  templateUrl: './corrective-maintenance-schedule-worklist.html',
  styleUrl: './corrective-maintenance-schedule-worklist.scss',
})
export class CorrectiveMaintenanceScheduleWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(CorrectiveMaintenanceScheduleWorklistService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  requestCriticalityId = 0;
  statusId = 0;
  storeId = 0;
  searchKeyword = '';

  storeList = signal<CmswStore[]>([]);
  correctiveMaintenanceWorklist = signal<CmswRow[]>([]);
  pagination = signal<PaginationState<CmswRow>>(createPaginationState<CmswRow>());

  showCompletionModal = signal(false);
  completionObj = signal<CompletionObj>({ completionDate: '', completionTime: '', isComplete: false, completionRemak: '' });

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.getStoreList();
    this.search();
  }

  private getStoreList(): void {
    this.api.getStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => this.storeList.set(res.listObject ?? []),
    });
  }

  search(): void {
    this.getCorrectiveMaintenanceWorklist();
    this.getSchedulingList();
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
        storeId: this.storeId,
        assignTo: this.userId,
        isWorklist: 3,
      })
      .subscribe({
        next: (res) => {
          this.correctiveMaintenanceWorklist.set(res.listObject ?? []);
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  private getSchedulingList(): void {
    this.loader.start();
    this.api
      .getSchedulingList({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        maintenanceScheduleStatusId: this.statusId,
        requestCriticalityId: this.requestCriticalityId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        storeId: this.storeId,
        assignTo: this.userId,
      })
      .subscribe({
        next: (res) => {
          const list = res.listObject ?? [];
          const p = this.pagination();
          p.data = [];
          if (list.length > 0) searchData(list, p);
          this.pagination.set({ ...p });
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  goToDetails(info: CmswRow): void {
    this.router.navigate(['/detailsCorrectiveMaintenanceScheduleWorklist']);
  }

  openCompletionDetails(info: CmswRow): void {
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
